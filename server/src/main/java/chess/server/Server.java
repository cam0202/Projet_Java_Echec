package chess.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashSet;

import org.apache.log4j.Logger;

import chess.client.Client;

public class Server {
    private final static Logger LOGGER = Logger.getLogger(Server.class);

    private HashSet<Client> online = new HashSet<>();

    private final int port;

    public Server(int port) {
        this.port = port;
    }

    public String getName() {
        return "The Chess Server";
    }

    public String getDescription() {
        return "Where everything is happening";
    }

    public int getOnlinePlayers() {
        return this.online.size();
    }

    public void loop() {
        try (ServerSocket socketTCP = new ServerSocket(port)) {
            // Set timeouts because accept() is blocking and
            // non-interruptible
            socketTCP.setSoTimeout(200);

            while (!Thread.interrupted()) {
                // We create one thread per client to manage the socket
                // This is a small app so it's fine, but we should consider creating a pool if
                // we want to scale up
                try {
                    Socket clientSocket = socketTCP.accept();
                    LOGGER.debug(
                            "New TCP connection from client [" + clientSocket.getInetAddress() + "]:" + clientSocket.getPort());

                    Thread worker = new Thread(new ServerWorker(clientSocket));
                    worker.setDaemon(true);
                    worker.start();

                } catch (SocketTimeoutException ignore) {
                } catch (IOException e) {
                    LOGGER.error("Failed to accept new client", e);
                }
            }
        } catch (IOException e) {
            LOGGER.fatal("Failed to create server socket", e);
        }
    }
}
