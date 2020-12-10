package chess.server;

import java.util.logging.Logger;

import chess.protocol.Message;

class Worker {
    private final static Logger LOGGER = Logger.getLogger(Worker.class.getName());

    public void processMessage(Message message) {
        System.out.println(message.getPayload().getData().toString());
    }
}