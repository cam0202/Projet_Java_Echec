package chess.server;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import chess.network.ExchangePacket;
import chess.network.TCPExchange;

 class WorkerTCP implements Runnable {
    private final static Logger LOGGER = Logger.getLogger(WorkerTCP.class);

    private final Socket socket;
    private final Server server;

    public WorkerTCP(final Server server, final Socket socket) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                ExchangePacket request = TCPExchange.receive(this.socket);
                Processor processor = new Processor(this.server);
                ExchangePacket response = processor.process(request);
                TCPExchange.send(this.socket, response);

            } catch (IOException e) {
                // TODO
                LOGGER.debug(e);
            }
        }
    }

    
}