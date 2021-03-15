package chess.server;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;

import org.apache.log4j.Logger;

import chess.network.MessagePacket;
import chess.network.MessageUDP;

/**
 * UDP packets are rare and fast to process, so they will be handled
 * synchronously. If we wanted to scale up, we should create a worker per packet
 * or a worker pool.
 */
final class ListenerUDP extends ServerRunner {
    private final static Logger LOGGER = Logger.getLogger(ListenerUDP.class);

    private final DatagramSocket socket;

    public ListenerUDP(final Server server, final DatagramSocket socket) throws IOException {
        super(server);
        this.socket = socket;

        // Set timeouts because receive() is blocking and non-interruptible, but we want
        // to be able to cleanly exit on SIGINT so we need a little bit of polling
        this.socket.setSoTimeout(200);
    }

    @Override
    public void run() {
        while (!this.shouldStop()) {
            MessagePacket request = null;

            try {
                request = MessageUDP.receive(this.socket);
            } catch (SocketTimeoutException expected) {
                continue;
            } catch (IOException e) {
                LOGGER.error("Failed to receive UDP packet", e);
                continue;
            }

            Processor processor = new Processor(this.server, null);
            MessagePacket response = processor.process(request);

            try {
                MessageUDP.send(this.socket, response);
            } catch (IOException e) {
                LOGGER.error("Failed to send UDP packet", e);
            }
        }
    }
}
