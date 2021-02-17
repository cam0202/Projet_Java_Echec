package chess.server;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import chess.network.MessagePacket;
import chess.network.MessageTCP;
import chess.network.MessageUDP;
import chess.protocol.Message;

/**
 * This class wraps a TCP socket and provides useful protocol functions
 */
public class Server {
    private static final Logger LOGGER = Logger.getLogger(Server.class);

    private final Socket socketTCP;
    private UUID clientUUID = null;
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

    public boolean isConnected() {
        return this.connected;
    }

    public InetAddress getAddress() {
        return this.socketTCP.getInetAddress();
    }

    public int getPort() {
        return this.socketTCP.getPort();
    }

    public static List<MessagePacket> discover() throws IOException {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(600);

            Map<UUID, MessagePacket> servers = new HashMap<>();

            Message request = new Message(Message.Type.DISCOVER);

            LOGGER.debug(String.format("Discovering servers... (timeout: %sms)", socket.getSoTimeout()));
            MessageUDP.send(socket, new MessagePacket(Inet4Address.getByName("255.255.255.255"),
                    chess.protocol.Server.DEFAULT_PORT, request));
            MessageUDP.send(socket,
                    new MessagePacket(Inet6Address.getByName("ff02::1"), chess.protocol.Server.DEFAULT_PORT, request));

            while (true) {
                try {
                    MessagePacket packet = MessageUDP.receive(socket);
                    LOGGER.debug("Discovered server [" + packet.getAddress() + "]:" + packet.getPort());
                    try {
                        JSONObject root = new JSONObject(packet.getMessage().getData());
                        UUID uuid = UUID.fromString(root.getString("uuid"));

                        // Add it to the list if not a duplicate. Prefer IPv6
                        MessagePacket current = servers.get(uuid);
                        if (current == null) {
                            servers.put(uuid, packet);
                        } else if (current.getAddress() instanceof Inet4Address
                                && packet.getAddress() instanceof Inet6Address) {
                            servers.put(uuid, packet);
                        }

                    } catch (JSONException e) {
                        LOGGER.debug("Ignoring server [" + packet.getAddress() + "]:" + packet.getPort());
                    }
                } catch (SocketTimeoutException exit) {
                    // This is our exit condition
                    break;
                } catch (IOException ignore) {
                }
            }

            return new ArrayList<>(servers.values());
        }
    }

    public void connect() throws IOException {
        if (this.connected) {
            throw new IllegalStateException("already connected to server");
        }

        Message request = new Message(Message.Type.CONNECT);
        try {
            JSONObject root = new JSONObject();
            root.put("name", "TODO");
            request.setData(root.toString());
        } catch (JSONException e) {
            throw new IOException("failed to create request");
        }

        MessageTCP.send(this.socketTCP, new MessagePacket(this.socketTCP, request));

        Message response = MessageTCP.receive(this.socketTCP).getMessage();
        if (response.getType() != Message.Type.OK) {
            throw new IOException("failed to connect: " + response.getData());
        }

        UUID uuid = null;
        try {
            JSONObject root = new JSONObject(response.getData());
            uuid = UUID.fromString(root.getString("uuid"));
        } catch (JSONException e) {
            throw new IOException("server response is ill-formed");
        }

        this.clientUUID = uuid;
        this.connected = true;
    }

    public void disconnect() throws IOException {
        if (!this.connected) {
            throw new IllegalStateException("already disconnected from server");
        }

        Message request = new Message(Message.Type.DISCONNECT);
        try {
            JSONObject root = new JSONObject();
            root.put("uuid", this.clientUUID.toString());
            request.setData(root.toString());
        } catch (JSONException e) {
            throw new IOException("failed to create request");
        }

        MessageTCP.send(this.socketTCP, new MessagePacket(this.socketTCP, request));

        Message response = MessageTCP.receive(this.socketTCP).getMessage();
        if (response.getType() != Message.Type.OK) {
            throw new IOException("failed to disconnect: " + response.getData());
        }

        this.clientUUID = null;
        this.connected = false;
    }

    // TODO: REMOVE
    public void move(String command) throws IOException {
        if (command == null) {
            throw new IllegalArgumentException("command is null");
        }

        if (!this.connected) {
            throw new IllegalStateException("not connected");
        }

        Message request = new Message(Message.Type.MOVE);
        try {
            JSONObject root = new JSONObject();
            root.put("uuid", this.clientUUID.toString());
            root.put("command", command);
            request.setData(root.toString());
        } catch (JSONException e) {
            throw new IOException("failed to create request");
        }

        MessageTCP.send(this.socketTCP, new MessagePacket(this.socketTCP, request));

        Message response = MessageTCP.receive(this.socketTCP).getMessage();
        if (response.getType() != Message.Type.OK) {
            throw new IOException(response.getData());
        }
    }
}
