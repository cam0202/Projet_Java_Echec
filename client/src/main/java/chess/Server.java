package chess;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import chess.network.ExchangePacket;
import chess.network.TCPExchange;
import chess.protocol.Message;

/**
 * This class wraps a TCP socket and provides useful protocol functions
 */
public class Server {
    private static final Logger LOGGER = Logger.getLogger(Server.class);

    private final Socket socketTCP;
    private boolean connected = false;

    public Server(final InetAddress address, final int port) throws IOException {
        this(new Socket(address, port));
    }

    public Server(final Socket socketTCP) {
        if (socketTCP == null) {
            throw new IllegalArgumentException("TCP socket is null");
        }

        if (!socketTCP.isConnected()) {
            throw new IllegalArgumentException("TCP socket is not connected");
        }

        this.socketTCP = socketTCP;
    }

    public Message connect() throws IOException {
        if (this.connected) {
            throw new IllegalStateException("already connected to server");
        }

        ExchangePacket request = new ExchangePacket(this.socketTCP, new Message(Message.Type.CONNECT));
        JSONObject root = new JSONObject();
        root.putOpt("uuid", null); // TODO
        root.put("name", "Hahaha");
        request.getMessage().setData(root.toString());

        TCPExchange.send(this.socketTCP, request);

        ExchangePacket response = TCPExchange.receive(this.socketTCP);
        if (response.getMessage().getType() != Message.Type.OK) {
            throw new IOException("failed to connect to server: " + response.getMessage().getData());
        }

        this.connected = true;

        return response.getMessage();
    }

    public void disconnect() throws IOException {
        if (!this.connected) {
            throw new IllegalStateException("already disconnected from server");
        }

        ExchangePacket request = new ExchangePacket(this.socketTCP, new Message(Message.Type.DISCONNECT));
        
        TCPExchange.send(this.socketTCP, request);

        ExchangePacket response = TCPExchange.receive(this.socketTCP);
        if (response.getMessage().getType() != Message.Type.OK) {
            throw new IOException("failed to cleanly from server: " + response.getMessage().getData());
        }

        this.connected = false;
    }
}
