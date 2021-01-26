package chess.server;

import java.io.IOException;
import java.net.Socket;

import org.apache.log4j.Logger;

import chess.network.ExchangePacket;
import chess.network.TCPExchange;

/**
 * This class handles requests from a unique client, until they disconnect
 */
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
            ExchangePacket request = null;
            try {
                request = TCPExchange.receive(this.socket);
            } catch (IOException exit) {
                // Failed to read -> player disconnected -> this worker should exit
                LOGGER.debug("TCP connection closed [" + this.socket.getInetAddress() + "]:" + this.socket.getPort());
                break;
            }

            LOGGER.debug("Request from [" + request.getAddress().toString() + "]:" + request.getPort());

            Processor processor = new Processor(this.server);
            ExchangePacket response = processor.process(request);

            try {
                TCPExchange.send(this.socket, response);
            } catch (IOException e) {
                LOGGER.error("Failed to send TCP packet", e);
            }
        }
    }
}