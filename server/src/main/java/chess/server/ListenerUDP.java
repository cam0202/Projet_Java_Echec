package chess.server;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;

import org.apache.log4j.Logger;

import chess.network.ExchangePacket;
import chess.network.UDPExchange;

/**
 * UDP packets are rare and fast to process, so they will be handled
 * synchronously. If we wanted to scale up, we should create a worker per packet
 * or a worker pool.
 */
final class ListenerUDP extends Listener {
    private final static Logger LOGGER = Logger.getLogger(ListenerUDP.class);

    private final DatagramSocket socket;

    public ListenerUDP(final Server server, final DatagramSocket socket) {
        super(server);
        this.socket = socket;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            ExchangePacket request = null;

            try {
                request = UDPExchange.receive(this.socket);
            } catch (SocketTimeoutException ignore) {
                continue;
            } catch (IOException e) {
                LOGGER.error("Failed to receive UDP packet", e);
                continue;
            }

            Processor processor = new Processor(this.server);
            ExchangePacket response = processor.process(request);

            try {
                UDPExchange.send(this.socket, response);
            } catch (IOException e) {
                LOGGER.error("Failed to send UDP packet", e);
            }
        }
    }
}
