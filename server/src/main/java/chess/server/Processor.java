package chess.server;

import java.util.UUID;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import chess.network.ExchangePacket;
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

    /**
     * Shorthand function to generate an error message
     * 
     * @param message Message wrap
     * @return Wrapped message
     */
    private Message error(final String message) {
        Message error = new Message(Message.Type.KO);
        error.setData(message);
        return error;
    }

    private UUID decodeUUID(final JSONObject root) throws JSONException {
        UUID uuid = null;
        String s = root.optString("uuid");
        if (s != null) {
            uuid = UUID.fromString(s);
        }
        return uuid;
    }

    private void encodeUUID(final JSONObject root, final UUID uuid) {
        root.put("uuid", uuid.toString());
    }

    /**
     * Main function to process a request
     * 
     * @param request The request packet
     * @return The appropriate response, or null if no response can be sent
     */
    public ExchangePacket process(final ExchangePacket request) {
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

            default: {
                return new ExchangePacket(request, this.error("unknown message type"));
            }
        }
    }

    private ExchangePacket processDISCOVER(final ExchangePacket request) {
        if (request.getMessage().getData().length() <= 0) {
            // Request does not provide a response port, and we know the player
            // won't listen on the port we know, so just ignore it
            return null;
        }

        int port = -1;

        try {
            JSONObject root = new JSONObject(request.getMessage().getData());
            port = root.getInt("port");
        } catch (JSONException e) {
            // Again, we could not decode the response port, so we can't respond
            return null;
        }

        Message response = new Message(Message.Type.DISCOVER);
        JSONObject root = new JSONObject(); // TODO
        root.put("name", this.server.getName());
        root.put("description", this.server.getDescription());
        root.put("online_players", this.server.getOnlinePlayers());
        root.put("max_online_players", this.server.getMaxOnlinePlayers());

        response.setData(root.toString());

        return new ExchangePacket(request.getAddress(), port, response);
    }

    private ExchangePacket processCONNECT(final ExchangePacket request) {
        if (request.getMessage().getData().length() <= 0) {
            return new ExchangePacket(request, this.error("payload is empty"));
        }

        UUID uuid = null;
        String name = "";

        try {
            JSONObject root = new JSONObject(request.getMessage().getData());
            uuid = this.decodeUUID(root);
            name = root.getString("name");
        } catch (JSONException e) {
            return new ExchangePacket(request, this.error("failed to decode payload: " + e.getMessage()));
        }

        Player player;
        if (uuid == null) {
            // Player does not provide a UUID: this is someone new
            player = new Player(UUID.randomUUID());
            player.setName(name);
        } else {
            // Player provides a UUID: this player is attempting to get their
            // session back
            player = this.server.getPlayer(uuid);
            if (player == null) {
                // The session for this player does not exists
                return new ExchangePacket(request, this.error("uuid is unknown"));
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
                return new ExchangePacket(request, this.error("server is full"));
            }
        }

        LOGGER.debug("User " + player.getName() + " joined the server");

        return new ExchangePacket(request, response);
    }

    private ExchangePacket processDISCONNECT(final ExchangePacket request) {
        if (request.getMessage().getData().length() > 0) {
            return new ExchangePacket(request, this.error("payload must be empty"));
        }

        UUID uuid = null;
        try {
            JSONObject root = new JSONObject(request.getMessage().getData());
            uuid = this.decodeUUID(root);
        } catch (JSONException e) {
            return new ExchangePacket(request, this.error("failed to decode payload: " + e.getMessage()));
        }

        Player player = this.server.getPlayer(uuid);

        if (player == null) {
            return new ExchangePacket(request, this.error("not connected"));
        }

        Message response = new Message(Message.Type.OK);

        this.server.removePlayer(uuid);

        LOGGER.debug("User " + player.getName() + " left the server");

        return new ExchangePacket(request, response);
    }
}
