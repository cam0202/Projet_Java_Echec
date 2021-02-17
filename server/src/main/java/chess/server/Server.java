package chess.server;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.UUID;

import org.apache.log4j.Logger;

import chess.player.Player;

/**
 * This class represents the server instance. It launches one TCP and one UDP listener
 */
public class Server {
    private final static Logger LOGGER = Logger.getLogger(Server.class);

    private final HashMap<UUID, Player> players = new HashMap<>();

    private final int port;
    private final UUID uuid;

    private RoomForStep1 room = null; // TODO REMOVE

    public Server(final int port) {
        this.port = port;
        this.uuid = UUID.randomUUID();
    }

    public void addPlayer(final UUID uuid, final Player player) {
        if (uuid == null) {
            throw new IllegalArgumentException("uuid cannot be null");
        }

        if (player == null) {
            throw new IllegalArgumentException("player cannot be null");
        }

        if (this.players.get(uuid) != null) {
            throw new IllegalArgumentException("this player already exists");
        }

        this.players.put(uuid, player);
    }

    public void removePlayer(final UUID uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("uuid cannot be null");
        }

        this.players.remove(uuid);
    }

    // TODO: REMOVE
    public void startRoomForStep1() {
        Player p1 = null;
        Player p2 = null;
        int i = 0;
        for (Player p : this.players.values()) { // Get first two players
            if (i == 0)
                p1 = p;
            else if (i == 1)
                p2 = p;
            else
                break;
            i++;
        }
        this.room = new RoomForStep1(p1, p2);
    }

    // TODO: remove
    public void endRoomForStep1() {
        this.room = null;
    }

    // TODO: REMOVE
    public RoomForStep1 getRoomForStep1() {
        return this.room;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public int getPort() {
        return this.port;
    }

    public String getName() {
        return "The Main Server"; // TODO
    }

    public String getDescription() {
        return "The is the main chess server"; // TODO
    }

    public int getOnlinePlayers() {
        return this.players.size(); // TODO
    }

    public int getMaxOnlinePlayers() {
        return chess.protocol.Server.DEFAULT_MAX_ONLINE_PLAYERS; // TODO
    }

    public Player getPlayer(final UUID uuid) {
        return this.players.get(uuid);
    }

    public void loop() {
        ListenerTCP listenerTCP = null;
        ListenerUDP listenerUDP = null;

        try (ServerSocket socketTCP = new ServerSocket(port); DatagramSocket socketUDP = new DatagramSocket(port)) {
            listenerTCP = new ListenerTCP(this, socketTCP);
            listenerTCP.start();

            LOGGER.debug("Started TCP listener");

            listenerUDP = new ListenerUDP(this, socketUDP);
            listenerUDP.start();

            LOGGER.debug("Started UDP listener");

            listenerTCP.join();
            listenerUDP.join();

        } catch (InterruptedException e) {
            LOGGER.debug("Interrupting listeners...");

            if (listenerTCP != null)
                listenerTCP.interrupt();
            if (listenerUDP != null)
                listenerUDP.interrupt();

        } catch (IOException e) {
            LOGGER.fatal("Failed to create server listeners", e);
        }
    }
}
