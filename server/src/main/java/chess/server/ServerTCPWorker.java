package chess.server;

import java.io.IOException;
import java.net.Socket;

import org.apache.log4j.Logger;

import chess.network.ExchangePacket;
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
                ExchangePacket request = TCPExchange.receive(this.client);
                ExchangePacket response = this.process(request);
                TCPExchange.send(this.client, response);

            } catch (IOException e) {

            }
        }
    }
}
