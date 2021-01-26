package chess.server;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import chess.network.ExchangePacket;
import chess.network.TCPExchange;
import chess.network.UDPExchange;
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

    public static Collection<ExchangePacket> discover() throws IOException {
        class Receiver extends Thread {
            private final Map<UUID, ExchangePacket> servers;
            private final DatagramSocket socket;

            public Receiver(final DatagramSocket socket) {
                this.servers = new HashMap<>();
                this.socket = socket;
            }

            public Map<UUID, ExchangePacket> getServers() {
                return this.servers;
            }

            @Override
            public void run() {
                while (true) {
                    try {
                        ExchangePacket packet = UDPExchange.receive(this.socket);
                        LOGGER.debug("Discovered server [" + packet.getAddress() + "]:" + packet.getPort());
                        try {
                            JSONObject root = new JSONObject(packet.getMessage().getData());
                            UUID uuid = UUID.fromString(root.getString("uuid"));

                            // Add it to the list if not a duplicate. Prefer IPv6
                            ExchangePacket current = this.servers.get(uuid);
                            if (current == null) {
                                this.servers.put(uuid, packet);
                            } else if (current.getAddress() instanceof Inet4Address
                                    && packet.getAddress() instanceof Inet6Address) {
                                this.servers.put(uuid, packet);
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
            }
        }

        try (DatagramSocket receiverSocket = new DatagramSocket()) {
            receiverSocket.setSoTimeout(200);
            Receiver receiver = new Receiver(receiverSocket);
            receiver.start();

            try (DatagramSocket senderSocket = new DatagramSocket()) {
                Message request = new Message(Message.Type.DISCOVER);
                try {
                    JSONObject root = new JSONObject();
                    root.put("port", receiverSocket.getLocalPort());
                    request.setData(root.toString());
                } catch (JSONException e) {
                    throw new IOException("failed to create request");
                }

                UDPExchange.send(senderSocket, new ExchangePacket(Inet4Address.getByName("255.255.255.255"),
                        chess.protocol.Server.DEFAULT_PORT, request));
                UDPExchange.send(senderSocket, new ExchangePacket(Inet6Address.getByName("ff02::1"),
                        chess.protocol.Server.DEFAULT_PORT, request));
                // TODO: ipv6 is sketchy there, fix this
            }

            try {
                receiver.join();
            } catch (InterruptedException ignore) {
            }

            return receiver.getServers().values();
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

        TCPExchange.send(this.socketTCP, new ExchangePacket(this.socketTCP, request));

        Message response = TCPExchange.receive(this.socketTCP).getMessage();
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

        TCPExchange.send(this.socketTCP, new ExchangePacket(this.socketTCP, request));

        Message response = TCPExchange.receive(this.socketTCP).getMessage();
        if (response.getType() != Message.Type.OK) {
            throw new IOException("failed to disconnect: " + response.getData());
        }

        this.clientUUID = null;
        this.connected = false;
    }
}
