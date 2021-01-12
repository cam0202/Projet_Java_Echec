package chess.server;

import java.util.UUID;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import chess.network.ExchangePacket;
import chess.protocol.Message;

abstract class ServerWorker {
    private final static Logger LOGGER = Logger.getLogger(ServerWorker.class);

    public ExchangePacket process(ExchangePacket request) {
        LOGGER.debug("Processing message from [" + request.getAddress() + "]:" + request.getPort());

        switch (request.getMessage().getType()) {
            case Message.Type.DISCOVER:
                return processDISCOVER(request);

            case Message.Type.CONNECT:
                return processCONNECT(request);

            case Message.Type.DISCONNECT:
                return processDISCONNECT(request);

            default: {
                ExchangePacket response = new ExchangePacket(request.getAddress(), request.getPort(),
                        new Message(Message.Type.KO));
                JSONObject root = new JSONObject();
                root.put("error", "unknown message type");
                response.getMessage().setData(root.toString());
                return response;
            }
        }
    }

    private ExchangePacket error(ExchangePacket request, String message) {
        ExchangePacket packet = new ExchangePacket(request.getAddress(), request.getPort(),
                new Message(Message.Type.KO));
        packet.getMessage().setData(message);
        return packet;
    }

    private ExchangePacket processDISCOVER(ExchangePacket request) {
        if (request.getMessage().getData().length() <= 0) {
            return error(request, "payload is empty");
        }

        int port = -1;

        try {
            JSONObject root = new JSONObject(request.getMessage().getData());
            port = root.getInt("port");
        } catch (JSONException e) {
            return error(request, "failed to decode payload: " + e.getMessage());
        }

        ExchangePacket response = new ExchangePacket(request.getAddress(), port, new Message(Message.Type.DISCOVER));
        JSONObject root = new JSONObject();
        root.put("name", "TheJavaProject");
        root.put("description", "Serveur principal de jeu");
        root.put("online_players", 0);
        root.put("max_players", 50);

        response.getMessage().setData(root.toString());

        return response;
    }

    private ExchangePacket processCONNECT(ExchangePacket request) {
        if (request.getMessage().getData().length() <= 0) {
            return error(request, "payload is empty");
        }

        UUID uuid = null;
        String name = "";

        try {
            JSONObject root = new JSONObject(request.getMessage().getData());
            uuid = UUID.fromString(root.getString("uuid"));
            name = root.getString("name");

        } catch (JSONException e) {
            return error(request, "failed to decode payload: " + e.getMessage());
        }

        LOGGER.debug("User " + uuid.toString() + " joined the server");

        ExchangePacket response = new ExchangePacket(request.getAddress(), request.getPort(),
                new Message(Message.Type.OK));
        JSONObject root = new JSONObject();
        root.put("uuid", uuid.toString());

        return response;
    }

    private ExchangePacket processDISCONNECT(ExchangePacket request) {
        if (request.getMessage().getData().length() <= 0) {
            return error(request, "payload is empty");
        }

        UUID uuid = null;

        try {
            JSONObject root = new JSONObject(request.getMessage().getData());
            uuid = UUID.fromString(root.getString("uuid"));

        } catch (JSONException e) {
            return error(request, "failed to decode payload: " + e.getMessage());
        }

        LOGGER.debug("User " + uuid.toString() + " left the server");

        ExchangePacket response = new ExchangePacket(request.getAddress(), request.getPort(),
                new Message(Message.Type.OK));

        return response;
    }
}