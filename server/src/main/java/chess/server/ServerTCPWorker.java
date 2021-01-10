package chess.server;

import java.io.IOException;
import java.net.Socket;

import org.apache.log4j.Logger;

import chess.network.TCPExchange;

public class ServerTCPWorker extends ServerWorker implements Runnable {
    private final static Logger LOGGER = Logger.getLogger(ServerTCPWorker.class);

    private Socket client;

    public ServerTCPWorker(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {

                TCPExchange.receive(this.client);
            } catch (IOException e) {

            }
        }
    }
}
