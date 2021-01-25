package chess.server;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import chess.client.Client;
import chess.network.ExchangePacket;
import chess.network.TCPExchange;
import chess.protocol.Message;

public class ServerWorker implements Runnable {
    private final static Logger LOGGER = Logger.getLogger(ServerWorker.class);

    private final Socket socket;
    private Client client;

    public ServerWorker(Socket socket) {
        this.socket = socket;
        this.client = null;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                ExchangePacket request = TCPExchange.receive(this.socket);
                ExchangePacket response = this.process(request);
                TCPExchange.send(this.socket, response);

            } catch (IOException e) {
                // TODO
                LOGGER.debug(e);
            }
        }
    }

    // Shorthand function to generate an error message
    private ExchangePacket error(String message) {
        ExchangePacket packet = new ExchangePacket(this.socket, new Message(Message.Type.KO));
        packet.getMessage().setData(message);
        return packet;
    }

    private ExchangePacket process(ExchangePacket request) {
        LOGGER.debug("Processing message from [" + request.getAddress() + "]:" + request.getPort());

        switch (request.getMessage().getType()) {
            case Message.Type.DISCOVER:
                return processDISCOVER(request);

            case Message.Type.CONNECT:
                return processCONNECT(request);

            case Message.Type.DISCONNECT:
                return processDISCONNECT(request);

            default: {
                return error("unknown message type");
            }
        }
    }

    private ExchangePacket processDISCOVER(ExchangePacket request) {
        if (request.getMessage().getData().length() <= 0) {
            return error("payload is empty");
        }

        int port = -1;

        try {
            JSONObject root = new JSONObject(request.getMessage().getData());
            port = root.getInt("port");
        } catch (JSONException e) {
            return error("failed to decode payload: " + e.getMessage());
        }

        ExchangePacket response = new ExchangePacket(request.getAddress(), port, new Message(Message.Type.DISCOVER));
        JSONObject root = new JSONObject(); // TODO
        root.put("name", "TheJavaProject");
        root.put("description", "Serveur principal de jeu");
        root.put("online_players", 0);
        root.put("max_players", 50);

        response.getMessage().setData(root.toString());

        return response;
    }

    private ExchangePacket processCONNECT(ExchangePacket request) {
        if (request.getMessage().getData().length() <= 0) {
            return error("payload is empty");
        }

        UUID uuid = null;
        String name = "";

        try {
            JSONObject root = new JSONObject(request.getMessage().getData());
            String uuidS = root.optString("uuid");
            if (!uuidS.isEmpty()) {
                uuid = UUID.fromString(root.getString("uuid"));
            }
            name = root.getString("name");
        } catch (JSONException e) {
            return error("failed to decode payload: " + e.getMessage());
        }

        if (client != null) {
            return error("already connected");
        }

        Client client;

        if (uuid == null) {
            // NEW CLIENT
            client = new Client(UUID.randomUUID());
        } else {
            // ATTEMPT TO RECONNECT CLIENT
            // TODO
            client = new Client(uuid);
        }

        client.setName(name);

        ExchangePacket response = new ExchangePacket(this.socket, new Message(Message.Type.OK));
        JSONObject root = new JSONObject();
        root.put("uuid", client.getUUID());
        response.getMessage().setData(root.toString());

        this.client = client;

        LOGGER.debug("User " + this.client.getName() + " joined the server");

        return response;
    }

    private ExchangePacket processDISCONNECT(ExchangePacket request) {
        if (request.getMessage().getData().length() > 0) {
            return error("payload must be empty");
        }

        if (this.client == null) {
            return error("not connected");
        }

        LOGGER.debug("User " + this.client.getName() + " left the server");

        ExchangePacket response = new ExchangePacket(this.socket, new Message(Message.Type.OK));
        return response;
    }
}