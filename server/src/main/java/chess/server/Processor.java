package chess.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
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

            case "room": {
                Room room = this.server.getPlayerRoom(uuid);
                response = room.getState();
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

                if (this.server.getPlayerRoom(uuid) != null) {
                    return new MessagePacket(request, this.error("you are already in a game room"));
                }

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
                if (room == null) {
                    return new MessagePacket(request, error("game is not started"));
                }

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
                    return new MessagePacket(request, this.error("failed to send message to one or more peers"));
                }

                break;
            }

            case "save": {
                Room room = this.server.getPlayerRoom(player.getUUID());
                if (room == null) {
                    return new MessagePacket(request, error("game is not started"));
                }

                // Trigger going back to lobby, then save
                JSONObject root = new JSONObject(request.getMessage().getData());
                root.put("payload", "/stop");
                request.getMessage().setData(root.toString());
                this.processPOST(request);

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Yes, this is a hack to make sure the GUI is ready
                            // I don't have time to redo half of the GUI system
                            // and this was flawed from the beginning
                            Thread.sleep(200);

                            JSONObject root = new JSONObject();
                            root.put("scope", "chat");
                            root.put("action", "system_info");

                            try {
                                File file = File.createTempFile("chessroom-", ".json");

                                try (FileWriter writer = new FileWriter(file)) {
                                    writer.write(room.getState().toString(2));
                                }

                                // Extract room ID
                                Pattern pattern = Pattern.compile("chessroom-(.*?).json");
                                Matcher matcher = pattern.matcher(file.getName());
                                if (matcher.find()) {
                                    root.put("data", new JSONObject().put("message",
                                            "Your room has been saved! Use /restore " + matcher.group(1)));
                                }

                            } catch (IOException e) {
                                root.put("data", new JSONObject().put("message", "Failed to save your room, sorry"));
                            }

                            Message msg = new Message(Message.Type.PUSH);
                            msg.setData(root.toString());

                            Player other = room.getPlayerWhite().equals(player) ? room.getPlayerBlack()
                                    : room.getPlayerWhite();
                            try {
                                MessageTCP.send(player.getSocket(), new MessagePacket(player.getSocket(), msg));
                                MessageTCP.send(other.getSocket(), new MessagePacket(other.getSocket(), msg));
                            } catch (IOException ignore) {
                            }

                        } catch (InterruptedException ignore) {
                        }
                    }
                });

                t.setDaemon(true); // No cleanup
                t.start();
                break;
            }

            case "restore": {
                if (this.server.getPlayerRoom(uuid) != null) {
                    return new MessagePacket(request, this.error("you are already in a game room"));
                }

                if (args.length != 2)
                    return new MessagePacket(request, this.error("missing room identifier"));

                File file = new File(System.getProperty("java.io.tmpdir"), "chessroom-" + args[1] + ".json");
                if (!file.exists())
                    return new MessagePacket(request, this.error("unknown room identifier"));

                try {
                    JSONObject root = null;
                    try (FileInputStream stream = new FileInputStream(file)) {
                        byte[] data = new byte[(int) file.length()];
                        stream.read(data);
                        root = new JSONObject(new String(data));
                    } catch (IOException e) {
                        return new MessagePacket(request, this.error("could not retrieve save data"));
                    }

                    JSONObject rootBoard = new JSONObject(root.getString("board"));

                    Player white = null;
                    Player black = null;
                    for (Player p : this.server.getPlayers()) {
                        if (white == null && p.getName().equals(rootBoard.getString("white"))) {
                            white = p;
                        }

                        if (black == null && p.getName().equals(rootBoard.getString("black"))) {
                            black = p;
                        }

                        if (white != null && black != null)
                            break;
                    }

                    if (white == null || black == null) {
                        return new MessagePacket(request, this.error("one or more player is missing, cannot start"));
                    }

                    Room room = new Room(white, black, root.getString("board"));
                    this.server.addRoom(room);

                    root = new JSONObject();
                    root.put("scope", "state");
                    root.put("action", "switch_to_room");

                    Message msg = new Message(Message.Type.PUSH);
                    msg.setData(root.toString());

                    try {
                        MessageTCP.send(white.getSocket(), new MessagePacket(white.getSocket(), msg));
                        MessageTCP.send(black.getSocket(), new MessagePacket(black.getSocket(), msg));
                    } catch (IOException e) {
                        return new MessagePacket(request, error("failed to send message to one or more peers"));
                    }

                } catch (JSONException e) {
                    return new MessagePacket(request, this.error("save data is corrupted"));
                }

                break;
            }

            case "play": {
                Room room = this.server.getPlayerRoom(player.getUUID());
                if (room == null) {
                    return new MessagePacket(request, error("your are not in a game room"));
                }

                if (args.length != 2)
                    return new MessagePacket(request, this.error("missing command"));

                try {
                    JSONObject root = new JSONObject();
                    root.put("scope", "chat");
                    root.put("action", "play");
                    root.put("data", new JSONObject().put("message", room.play(player, args[1])));

                    Message msg = new Message(Message.Type.PUSH);
                    msg.setData(root.toString());

                    Player other = room.getPlayerWhite().equals(player) ? room.getPlayerBlack() : room.getPlayerWhite();
                    MessageTCP.send(player.getSocket(), new MessagePacket(player.getSocket(), msg));
                    MessageTCP.send(other.getSocket(), new MessagePacket(other.getSocket(), msg));

                } catch (BoardException e) {
                    return new MessagePacket(request, this.error(e.getMessage()));
                } catch (IOException e) {
                    return new MessagePacket(request, this.error("failed to send message to one or more peers"));
                }

                break;
            }

            default: {
                return new MessagePacket(request, this.error("unknown command " + args[0]));
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
                Message msg = new Message(Message.Type.PUSH);
                msg.setData(root.toString());

                Room room = this.server.getPlayerRoom(player.getUUID());

                // If the player is in a room, sent the message to players in the room,
                // otherwise send it in the global chat
                if (room == null) {
                    for (Player p : this.server.getPlayers()) {
                        MessageTCP.send(p.getSocket(), new MessagePacket(p.getSocket(), msg));
                    }
                } else {
                    Player white = room.getPlayerWhite();
                    Player black = room.getPlayerBlack();
                    MessageTCP.send(white.getSocket(), new MessagePacket(white.getSocket(), msg));
                    MessageTCP.send(black.getSocket(), new MessagePacket(black.getSocket(), msg));
                }
            } catch (IOException e) {
                return new MessagePacket(request, error("failed to send message to one or more peers"));
            }
        }

        return new MessagePacket(request, new Message(Message.Type.OK));
    }
}
