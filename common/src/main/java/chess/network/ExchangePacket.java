package chess.network;

import java.net.InetAddress;

import chess.protocol.Message;

public class ExchangePacket {

    private InetAddress address;
    private int port;
    private Message message;

    public ExchangePacket(InetAddress address, int port, Message message) {
        if (address == null) {
            throw new IllegalArgumentException("Destination address is null");
        }

        if (!(0 < port && port <= 65535)) {
            throw new IllegalArgumentException("Invalid port " + port);
        }

        if (message == null) {
            throw new IllegalArgumentException("Message is null");
        }

        this.address = address;
        this.port = port;
        this.message = message;
    }

    public InetAddress getAddress() {
        return this.address;
    }

    public int getPort() {
        return this.port;
    }

    public Message getMessage() {
        return this.message;
    }
}
