package chess.server;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import chess.client.Client;
import chess.network.ExchangePacket;
import chess.network.UDPExchange;

public class Server {
    private final static Logger LOGGER = Logger.getLogger(Server.class);

    private final int port;
    private final HashMap<ServerTCPWorker, Client> clients;

    public Server(int port) {
        this.port = port;
        this.clients = new HashMap<>();
    }

    public void loop() {
        ServerTCP serverTCP = null;
        ServerUDP serverUDP = null;

        try (ServerSocket socketTCP = new ServerSocket(port); DatagramSocket socketUDP = new DatagramSocket(port)) {
            // Set timeouts because accept() and receive() are blocking and
            // non-interruptible
            socketTCP.setSoTimeout(200);
            socketUDP.setSoTimeout(200);

            serverTCP = new ServerTCP(socketTCP);
            serverTCP.start();

            serverUDP = new ServerUDP(socketUDP);
            serverUDP.start();

            serverTCP.join();
            serverUDP.join();

        } catch (InterruptedException e) {
            if (serverTCP != null)
                serverTCP.interrupt();
            if (serverUDP != null)
                serverUDP.interrupt();

        } catch (IOException e) {
            LOGGER.fatal("Failed to create server sockets", e);
        }
    }

    /**
     * This subclass will create a worker thread per client. This is ok for a small
     * app like this one, but we should create a worker pool if we want to scale up.
     */
    private class ServerTCP extends Thread {
        private ServerSocket socket;

        public ServerTCP(ServerSocket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            while (!Thread.interrupted()) {
                try {
                    Socket client = socket.accept();
                    LOGGER.debug(
                            "New TCP connection from client [" + client.getInetAddress() + "]:" + client.getPort());

                    Thread worker = new Thread(new ServerTCPWorker(client));
                    worker.start();
                    // TODO track workers

                } catch (SocketTimeoutException ignore) {
                } catch (IOException e) {
                    LOGGER.error("Failed to accept new client", e);
                }
            }

            // Interrupt running threads
        }
    }

    /**
     * UDP packets are rare and fast to process, so they will be handled
     * synchronously. If we wanted to scale up, we should create a worker per packet
     * or a worker pool.
     */
    private class ServerUDP extends Thread {
        private DatagramSocket socket;

        public ServerUDP(DatagramSocket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            while (!Thread.interrupted()) {
                try {
                    ExchangePacket request = UDPExchange.receive(this.socket);

                    ExchangePacket response = (new ServerUDPWorker()).process(request);

                    UDPExchange.send(this.socket, response);

                } catch (SocketTimeoutException ignore) {
                } catch (IOException e) {
                    LOGGER.error("Failed to process packet", e);
                }
            }
        }
    }
}
