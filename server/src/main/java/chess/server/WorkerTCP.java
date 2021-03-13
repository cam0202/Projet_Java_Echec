package chess.server;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import chess.network.MessagePacket;
import chess.network.MessageTCP;
import chess.protocol.Message;

/**
 * This class handles requests from a unique client, until they disconnect
 */
class WorkerTCP extends ServerRunner {
    private final static Logger LOGGER = Logger.getLogger(WorkerTCP.class);

    private final Socket socket;

    public WorkerTCP(final Server server, final Socket socket) {
        super(server);
        this.socket = socket;
    }

    @Override
    public void run() {
        while (!this.shouldStop()) {
            MessagePacket request = null;
            try {
                request = MessageTCP.receive(this.socket);
            } catch (TimeoutException expected) {
                continue;
            } catch (IOException exit) {
                // Failed to read -> lost connection to player -> this worker should exit
                LOGGER.debug("TCP connection closed [" + this.socket.getInetAddress() + "]:" + this.socket.getPort());
                break;
            }

            LOGGER.debug("Request from [" + request.getAddress().toString() + "]:" + request.getPort());

            Processor processor = new Processor(this.server);
            MessagePacket response = processor.process(request);

            try {
                MessageTCP.send(this.socket, response);
            } catch (IOException e) {
                LOGGER.error("Failed to send TCP packet", e);
            }
        }
    }
}