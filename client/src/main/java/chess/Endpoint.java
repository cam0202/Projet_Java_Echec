package chess;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import chess.network.ExchangePacket;
import chess.network.TCPExchange;
import chess.protocol.Message;

/**
 * This class wraps a TCP socket and provides useful protocol functions
 */
public class Endpoint {
    private static final Logger LOGGER = Logger.getLogger(Endpoint.class);

    protected Socket socketTCP = null;
    private boolean connected = false;

    public Endpoint(InetAddress address, int port) throws IOException {
        this(new Socket(address, port));
    }

    public Endpoint(Socket socketTCP) {
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
            throw new IllegalStateException("Already connected to endpoint");
        }

        ExchangePacket request = new ExchangePacket(this.socketTCP, new Message(Message.Type.CONNECT));
        JSONObject root = new JSONObject();
        root.put("uuid", UUID.randomUUID()); // TODO
        root.put("name", "Hahaha");
        request.getMessage().setData(root.toString());

        TCPExchange.send(this.socketTCP, request);

        ExchangePacket response = TCPExchange.receive(this.socketTCP);
        if (response.getMessage().getType() != Message.Type.OK) {
            throw new IOException("failed to connect to endpoint: " + response.getMessage().getData());
        }

        this.connected = true;

        return response.getMessage();
    }

    public void disconnect() throws IOException {
        if (!this.connected) {
            throw new IllegalStateException("already disconnected from endpoint");
        }

        ExchangePacket request = new ExchangePacket(this.socketTCP, new Message(Message.Type.DISCONNECT));
        JSONObject root = new JSONObject();
        root.put("uuid", UUID.randomUUID()); // TODO
        request.getMessage().setData(root.toString());
        
        TCPExchange.send(this.socketTCP, request);

        ExchangePacket response = TCPExchange.receive(this.socketTCP);
        if (response.getMessage().getType() != Message.Type.OK) {
            throw new IOException("failed to cleanly from endpoint: " + response.getMessage().getData());
        }

        this.connected = false;
    }
}
