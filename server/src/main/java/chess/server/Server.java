package chess.server;

import java.util.logging.Logger;

public class Server extends Thread {
    private final static Logger LOGGER = Logger.getLogger(Server.class.getName());

    private final int port;

    public Server(int port) {
        this.port = port;
    }

    public Server() {
        this(chess.protocol.Server.DEFAULT_PORT);
    }

    public int getPort() {
        return port;
    }

    @Override
    public void run() {
        ServerTCP serverTCP = null;
        ServerUDP serverUDP = null;
        try {
            serverTCP = new ServerTCP(port);
            serverUDP = new ServerUDP(port);

            serverTCP.start();
            serverUDP.start();
            serverTCP.join();
            serverUDP.join();

        } catch (InterruptedException e) {
            if (serverTCP != null) serverTCP.interrupt();
            if (serverUDP != null) serverUDP.interrupt();
        }
    }
}
