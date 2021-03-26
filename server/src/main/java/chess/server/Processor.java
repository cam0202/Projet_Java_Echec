package chess.server;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import chess.game.BoardException;
import chess.network.MessagePacket;
import chess.network.MessageTCP;
import chess.player.Player;
import chess.protocol.Message;

/**
 * This class centralizes the processing of player messages and provides the
 * appropriate responses to send back
 */
class Processor {
    private final static Logger LOGGER = Logger.getLogger(WorkerTCP.class);

    private final Server server;
    private final Socket socketTCP;

    public Processor(final Server server, final Socket socketTCP) {
        if (server == null) {
            throw new IllegalArgumentException("server is null");
        }

        this.server = server;
        this.socketTCP = socketTCP;
    }

    private Message error(final String message) {
        Message error = new Message(Message.Type.KO);
        error.setData(message);
        return error;
    }

    private UUID decodeUUID(final JSONObject root) throws JSONException {
        if (root == null) {
            throw new IllegalArgumentException("root is null");
        }

        try {
            String s = root.getString("uuid");
            return UUID.fromString(s);
        } catch (JSONException e) {
            return null;
        }
    }

    private void encodeUUID(final JSONObject root, final UUID uuid) {
        if (root == null) {
            throw new IllegalArgumentException("root is null");
        }

        if (uuid == null) {
            throw new IllegalArgumentException("uuid is null");
        }

        root.put("uuid", uuid.toString());
    }

    /**
     * Main function to process a request
     * 
     * @param request The request packet
     * @return The appropriate response, or null if no response can be sent
     */
    public MessagePacket process(final MessagePacket request) {
        if (request == null) {
            throw new IllegalArgumentException("request cannot be null");
        }

        switch (request.getMessage().getType()) {
        case Message.Type.DISCOVER:
            return processDISCOVER(request);

        case Message.Type.CONNECT:
            return processCONNECT(request);

        case Message.Type.DISCONNECT:
            return processDISCONNECT(request);

        case Message.Type.GET:
            return processGET(request);

        case Message.Type.POST:
            return processPOST(request);

        default: {
            return new MessagePacket(request, this.error("unknown message type"));
        }
        }
    }

    /**
     * Handler for DISCOVER request
     */
    private MessagePacket processDISCOVER(final MessagePacket request) {
        if (request.getMessage().getData().length() > 0) {
            return new MessagePacket(request, this.error("payload must be empty"));
        }

        Message response = new Message(Message.Type.DISCOVER);
        JSONObject root = new JSONObject(); // TODO
        root.put("uuid", this.server.getUUID());
        root.put("name", this.server.getName());
        root.put("description", this.server.getDescription());
        root.put("online_players", this.server.getOnlinePlayers());
        root.put("max_online_players", this.server.getMaxOnlinePlayers());

        response.setData(root.toString());

        LOGGER.debug("Discovery request from [" + request.getAddress() + "]:" + request.getPort());

        return new MessagePacket(request, response);
    }

    /**
     * Handler for CONNECT request
     */
    private MessagePacket processCONNECT(final MessagePacket request) {
        if (request.getMessage().getData().length() <= 0) {
            return new MessagePacket(request, this.error("payload is empty"));
        }

        UUID uuid = null;
        String name = "";

        try {
            JSONObject root = new JSONObject(request.getMessage().getData());
            uuid = this.decodeUUID(root);
            name = root.getString("name");
        } catch (JSONException e) {
            return new MessagePacket(request, this.error("failed to decode payload: " + e.getMessage()));
        }

        Player player;
        if (uuid == null) {
            // Player does not provide a UUID: this is someone new
            uuid = UUID.randomUUID();
            player = new Player(uuid, this.socketTCP);
            player.setName(name);
        } else {
            // Player provides a UUID: this player is attempting to get their
            // session back
            player = this.server.getPlayer(uuid);
            if (player == null) {
                // The session for this player does not exists
                return new MessagePacket(request, this.error("uuid is unknown"));
            }
            // The session exists. Following requests will be valid
        }

        Message response = new Message(Message.Type.OK);
        JSONObject root = new JSONObject();
        this.encodeUUID(root, uuid);
        response.setData(root.toString());

        if (uuid != null) {
            if (this.server.getOnlinePlayers() < this.server.getMaxOnlinePlayers()) {
                this.server.addPlayer(uuid, player);
            } else {
                LOGGER.debug("Cannot accept new player because server reached max player limit");
                return new MessagePacket(request, this.error("server is full"));
            }
        }

        // Notify players
        root = new JSONObject();
        root.put("scope", "chat");
        root.put("action", "player_join");
        root.put("data", new JSONObject().put("username", player.getName()));

        try {
            for (Player p : this.server.getPlayers()) {
                if (!p.equals(player)) {
                    Message msg = new Message(Message.Type.PUSH);
                    msg.setData(root.toString());
                    MessageTCP.send(p.getSocket(), new MessagePacket(p.getSocket(), msg));
                }
            }
        } catch (IOException ignore) {
        }

        return new MessagePacket(request, response);
    }

    /**
     * Handler for DISCONNECT request
     */
    private MessagePacket processDISCONNECT(final MessagePacket request) {
        if (request.getMessage().getData().length() <= 0) {
            return new MessagePacket(request, this.error("payload is empty"));
        }

        UUID uuid = null;
        try {
            JSONObject root = new JSONObject(request.getMessage().getData());
            uuid = this.decodeUUID(root);
        } catch (JSONException e) {
            return new MessagePacket(request, this.error("failed to decode payload: " + e.getMessage()));
        }

        Player player = this.server.getPlayer(uuid);

        if (player == null) {
            return new MessagePacket(request, this.error("not connected"));
        }

        this.server.removePlayer(uuid);

        // Notify players
        JSONObject root = new JSONObject();
        root.put("scope", "chat");
        root.put("action", "player_leave");
        root.put("data", new JSONObject().put("username", player.getName()));

        try {
            for (Player p : this.server.getPlayers()) {
                if (!p.equals(player)) {
                    Message msg = new Message(Message.Type.PUSH);
                    msg.setData(root.toString());
                    MessageTCP.send(p.getSocket(), new MessagePacket(p.getSocket(), msg));
                }
            }
        } catch (IOException ignore) {
        }

        return new MessagePacket(request, new Message(Message.Type.OK));
    }

    private MessagePacket processGET(final MessagePacket request) {
        if (request.getMessage().getData().length() <= 0) {
            return new MessagePacket(request, this.error("payload is empty"));
        }

        UUID uuid = null;
        JSONObject response = null;
        try {
            JSONObject root = new JSONObject(request.getMessage().getData());
            uuid = this.decodeUUID(root);
            switch (root.getString("scope")) {
            case "server": {
                response = new JSONObject().put("name", this.server.getName()).put("description",
                        this.server.getDescription());
                break;
            }

            default:
                throw new JSONException("unknown scope");
            }
        } catch (JSONException e) {
            return new MessagePacket(request, this.error("failed to decode payload: " + e.getMessage()));
        }

        assert (response != null);

        Message r = new Message(Message.Type.OK);
        r.setData(response.toString());

        return new MessagePacket(request, r);

    }

    private MessagePacket processPOST(final MessagePacket request) {
        if (request.getMessage().getData().length() <= 0) {
            return new MessagePacket(request, this.error("payload is empty"));
        }

        UUID uuid = null;
        String payload = null;
        try {
            JSONObject root = new JSONObject(request.getMessage().getData());
            uuid = this.decodeUUID(root);
            payload = root.getString("payload");
        } catch (JSONException e) {
            return new MessagePacket(request, this.error("failed to decode payload: " + e.getMessage()));
        }

        Player player = this.server.getPlayer(uuid);

        if (player == null) {
            return new MessagePacket(request, this.error("not connected"));
        }

        if (payload.startsWith("/")) {
            String[] args = payload.substring(1).split(" ");
            switch (args[0]) {
            case "start": {
                if (args.length != 2)
                    return new MessagePacket(request, this.error("missing other player"));

                if (player.getName().equals(args[1]))
                    return new MessagePacket(request, this.error("cannot start a game with yourself"));

                Player other = null;
                for (Player p : this.server.getPlayers()) {
                    if (p.getName().equals(args[1])) {
                        other = p;
                    }
                }

                if (other == null)
                    return new MessagePacket(request, this.error("other player not found"));

                Room room = new Room(player, other);
                this.server.addRoom(room);

                JSONObject root = new JSONObject();
                root.put("scope", "state");
                root.put("action", "switch_to_room");

                Message msg = new Message(Message.Type.PUSH);
                msg.setData(root.toString());

                try {
                    MessageTCP.send(player.getSocket(), new MessagePacket(player.getSocket(), msg));
                    MessageTCP.send(other.getSocket(), new MessagePacket(other.getSocket(), msg));
                } catch (IOException e) {
                    return new MessagePacket(request, error("failed to send message to one or more peers"));
                }
                break;
            }

            case "stop": {
                Room room = this.server.getPlayerRoom(player.getUUID());
                if (room == null)
                    return new MessagePacket(request, error("game is not started"));

                this.server.removeRoom(room);

                JSONObject root = new JSONObject();
                root.put("scope", "state");
                root.put("action", "switch_to_lobby");

                Message msg = new Message(Message.Type.PUSH);
                msg.setData(root.toString());

                Player other = room.getPlayerWhite().equals(player) ? room.getPlayerBlack() : room.getPlayerWhite();
                try {
                    MessageTCP.send(player.getSocket(), new MessagePacket(player.getSocket(), msg));
                    MessageTCP.send(other.getSocket(), new MessagePacket(other.getSocket(), msg));
                } catch (IOException e) {
                    return new MessagePacket(request, error("failed to send message to one or more peers"));
                }

                break;
            }
            }

            // This is a command to execute
            // TODO
        } else {
            JSONObject jsonmsg = new JSONObject();
            jsonmsg.put("username", player.getName());
            jsonmsg.put("message", payload);

            JSONObject root = new JSONObject();
            root.put("scope", "chat");
            root.put("action", "player_chat");
            root.put("data", jsonmsg);

            // This is a chat message
            try {
                for (Player p : this.server.getPlayers()) {
                    if (this.server.getPlayerRoom(p.getUUID()) == null) {
                        Message msg = new Message(Message.Type.PUSH);
                        msg.setData(root.toString());
                        MessageTCP.send(p.getSocket(), new MessagePacket(p.getSocket(), msg));
                    }
                }
            } catch (IOException e) {
                return new MessagePacket(request, error("failed to send message to one or more peers"));
            }
        }

        return new MessagePacket(request, new Message(Message.Type.OK));
    }
}
