package chess.server;

import java.util.UUID;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import chess.network.MessagePacket;
import chess.player.Player;
import chess.protocol.Message;

/**
 * This class centralizes the processing of player messages and provides the
 * appropriate responses to send back
 */
class Processor {
    private final static Logger LOGGER = Logger.getLogger(WorkerTCP.class);

    private final Server server;

    public Processor(final Server server) {
        this.server = server;
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

            case Message.Type.PLAY:
                return processPLAY(request);

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
            player = new Player(uuid);
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

        // TODO: REMOVE
        if (this.server.getOnlinePlayers() == 2) {
            LOGGER.debug("Game started");
            this.server.startRoomForStep1();
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

        // TODO: REMOVE
        if (this.server.getOnlinePlayers() < 2) {
            this.server.endRoomForStep1();
            LOGGER.debug("Game ended");
        }

        return new MessagePacket(request, new Message(Message.Type.OK));
    }

    private MessagePacket processPLAY(final MessagePacket request) {
        if (request.getMessage().getData().length() <= 0) {
            return new MessagePacket(request, this.error("payload is empty"));
        }

        UUID uuid = null;
        String command = null;
        try {
            JSONObject root = new JSONObject(request.getMessage().getData());
            uuid = this.decodeUUID(root);
            command = root.getString("command");
        } catch (JSONException e) {
            return new MessagePacket(request, this.error("failed to decode payload: " + e.getMessage()));
        }

        Player player = this.server.getPlayer(uuid);

        if (player == null) {
            return new MessagePacket(request, this.error("not connected"));
        }

        // TODO HANDLE ROOMS

        // TODO EXEC
        // TODO: REMOVE
        RoomForStep1 room = this.server.getRoomForStep1();
        if (room == null) {
            return new MessagePacket(request, this.error("game not started"));
        }
        try {
            room.doCommand(player, command);
        } catch (RuntimeException e) {
            return new MessagePacket(request, this.error(e.getMessage()));
        }

        return new MessagePacket(request, new Message(Message.Type.OK));
    }
}
