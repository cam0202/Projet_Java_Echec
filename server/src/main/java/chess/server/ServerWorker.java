package chess.server;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import chess.network.ExchangePacket;
import chess.protocol.Message;

abstract class ServerWorker {
    private final static Logger LOGGER = Logger.getLogger(ServerWorker.class);

    public ExchangePacket process(ExchangePacket request) {
        LOGGER.debug("Processing message from [" + request.getAddress() + "]:" + request.getPort() + "\n"
                + request.getMessage().getData());

        switch (request.getMessage().getType()) {
            case Message.Type.DISCOVER:
                return processDISCOVER(request);

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

    private ExchangePacket processDISCOVER(ExchangePacket request) {
        JSONObject root = new JSONObject(request.getMessage().getData());
        int port = root.getInt("port");

        ExchangePacket response = new ExchangePacket(request.getAddress(), port, new Message(Message.Type.DISCOVER));
        root = new JSONObject();
        root.put("name", "TheJavaProject");
        root.put("description", "Serveur principal de jeu");
        root.put("online_players", 0);
        root.put("max_players", 50);

        response.getMessage().setData(root.toString());

        return response;
    }
}