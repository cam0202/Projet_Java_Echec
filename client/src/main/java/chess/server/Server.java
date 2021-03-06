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

public class Server {
    private static final Logger LOGGER = Logger.getLogger(Server.class);

    private Socket socketTCP;
    private ListenerTCP listenerTCP;
    private UUID clientUUID;

    private String name;
    private String description;

    private String forcedUsername;
    private String username;

    public Server() {
        this.socketTCP = null;
        this.listenerTCP = null;
        this.clientUUID = null;

        this.name = null;
        this.description = null;

        this.forcedUsername = null;
    }

    public boolean isConnected() {
        return this.socketTCP != null;
    }

    public void setUpdateCallback(Callback callback) {
        if (!this.isConnected()) {
            throw new IllegalStateException("cannot set callback function because not connected");
        }

        assert (this.listenerTCP != null);
        this.listenerTCP.setCallback(callback);
    }

    public void setForcedUsername(final String username) {
        this.forcedUsername = username;
    }

    public String getForcedUsername() {
        return this.forcedUsername;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getUsername() {
        return this.username;
    }

    public List<MessagePacket> discover() throws IOException {
        try (DatagramSocket socket = new DatagramSocket()) {
            // Broadcast packets are slow
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
                            servers.put(uuid, packet); // Replace IPv4 with IPv6 version
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

    public void connect(final InetAddress address, final int port) throws IOException {
        if (this.socketTCP != null) {
            throw new IllegalStateException("already connected to a server");
        }

        if (address == null) {
            throw new IllegalArgumentException("address is null");
        }

        Socket socketTCP = new Socket(address, port);
        String username = this.forcedUsername != null ? this.forcedUsername : "User-" + socketTCP.getLocalPort();

        Message request = new Message(Message.Type.CONNECT);
        try {
            JSONObject root = new JSONObject();
            root.put("name", username);
            request.setData(root.toString());
        } catch (JSONException e) {
            throw new IOException("failed to create request");
        }

        ListenerTCP listenerTCP = new ListenerTCP(socketTCP);
        Thread t1 = new Thread(listenerTCP);
        t1.setDaemon(true);
        t1.start();

        MessageTCP.send(socketTCP, new MessagePacket(socketTCP, request));

        Message response = listenerTCP.receive().getMessage();
        if (response.getType() != Message.Type.OK) {
            throw new IOException(response.getData());
        }

        UUID uuid = null;
        try {
            JSONObject root = new JSONObject(response.getData());
            uuid = UUID.fromString(root.getString("uuid"));
        } catch (JSONException e) {
            throw new IOException("server response is ill-formed");
        }

        this.clientUUID = uuid;
        this.socketTCP = socketTCP;
        this.listenerTCP = listenerTCP;

        this.username = username;

        this.updateServerInfo();
    }

    public void disconnect() throws IOException {
        if (this.socketTCP == null) {
            throw new IllegalStateException("already disconnected from server");
        }

        Message request = new Message(Message.Type.DISCONNECT);
        try {
            JSONObject root = new JSONObject();
            root.put("uuid", this.clientUUID.toString());
            request.setData(root.toString());
        } catch (JSONException e) {
            throw new IOException("failed to create disconnect request");
        }

        MessageTCP.send(this.socketTCP, new MessagePacket(this.socketTCP, request));

        Message response = this.listenerTCP.receive().getMessage();
        if (response.getType() != Message.Type.OK) {
            throw new IOException(response.getData());
        }

        this.listenerTCP.requireStop();
        this.clientUUID = null;
        this.socketTCP = null;
        this.listenerTCP = null;
        this.username = null;
    }

    public String get(String scope) throws IOException {
        if (scope == null) {
            throw new IllegalArgumentException("scope is null");
        }

        Message request = new Message(Message.Type.GET);
        try {
            JSONObject root = new JSONObject();
            root.put("uuid", this.clientUUID.toString());
            root.put("scope", scope);
            request.setData(root.toString());
        } catch (JSONException e) {
            throw new IOException("failed to create request");
        }

        MessageTCP.send(this.socketTCP, new MessagePacket(this.socketTCP, request));

        Message response = this.listenerTCP.receive().getMessage();
        if (response.getType() != Message.Type.OK) {
            throw new IOException(response.getData());
        }
        
        return response.getData();
    }

    public void post(String message) throws IOException {
        if (message == null) {
            throw new IllegalArgumentException("message is null");
        }

        Message request = new Message(Message.Type.POST);
        try {
            JSONObject root = new JSONObject();
            root.put("uuid", this.clientUUID.toString());
            root.put("payload", message);
            request.setData(root.toString());
        } catch (JSONException e) {
            throw new IOException("failed to create request");
        }

        MessageTCP.send(this.socketTCP, new MessagePacket(this.socketTCP, request));

        Message response = this.listenerTCP.receive().getMessage();
        if (response.getType() != Message.Type.OK) {
            throw new IOException(response.getData());
        }
    }

    private void updateServerInfo() throws IOException {
        String data = this.get("server");

        try {
            JSONObject root = new JSONObject(data);
            this.name = root.getString("name");
            this.description = root.getString("description");
        } catch (JSONException e) {
            throw new IOException("server response is ill-formed");
        }
    }
}
