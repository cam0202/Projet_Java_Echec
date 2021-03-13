package chess.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.apache.log4j.Logger;

/**
 * This subclass will create a worker thread per player. This is ok for a small
 * app like this one, but we should create a worker pool if we wanted to scale
 * up.
 */
final class ListenerTCP extends ServerRunner {
    private final static Logger LOGGER = Logger.getLogger(ListenerTCP.class);

    private final ServerSocket socket;

    public ListenerTCP(final Server server, final ServerSocket socket) throws IOException {
        super(server);
        this.socket = socket;

        // Set timeout because accept() is blocking and non-interruptible, but we want
        // to be able to cleanly exit on SIGINT so we need a little bit of polling
        this.socket.setSoTimeout(200);
    }

    @Override
    public void run() {
        while (!this.shouldStop()) {
            try {
                Socket player = socket.accept();
                LOGGER.debug("TCP connection from player [" + player.getInetAddress() + "]:" + player.getPort());

                Thread worker = new Thread(new WorkerTCP(server, player));
                worker.setDaemon(true);
                worker.start();

            } catch (SocketTimeoutException expected) {
                continue;
            } catch (IOException e) {
                LOGGER.error("Failed to establish TCP connection", e);
            }
        }
    }
}
