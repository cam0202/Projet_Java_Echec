package chess.server;

import java.io.IOException;
import java.net.DatagramSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

class ServerUDP extends Thread {
    private final static Logger LOGGER = Logger.getLogger(ServerUDP.class.getName());

    private final int port;

    public ServerUDP(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try (DatagramSocket socket = new DatagramSocket(port)) {
            while (!this.isInterrupted()) {

            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
