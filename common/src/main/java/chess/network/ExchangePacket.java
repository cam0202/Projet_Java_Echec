package chess.network;

import java.net.InetAddress;
import java.net.Socket;

import chess.protocol.Message;

/**
 * This class wraps a message and a destination for compatibility between
 * TCP and UDP transmisson methods
 */
public class ExchangePacket {

    private final InetAddress address;
    private final int port;
    private final Message message;

    public ExchangePacket(final ExchangePacket template, final Message message) {
        this(template.getAddress(), template.getPort(), message);
    }

    public ExchangePacket(final Socket socket, final Message message) {
        this(socket.getInetAddress(), socket.getPort(), message);
    }

    public ExchangePacket(final InetAddress address, final int port, final Message message) {
        if (address == null) {
            throw new IllegalArgumentException("destination address is null");
        }

        if (!(0 < port && port <= 65535)) {
            throw new IllegalArgumentException("invalid port " + port);
        }

        if (message == null) {
            throw new IllegalArgumentException("message is null");
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
