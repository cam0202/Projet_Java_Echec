package chess.server;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;

import chess.player.Player;

/**
 * This class represents the server instance. It launches one TCP and one UDP
 * listener
 */
public class Server {
    private final static Logger LOGGER = Logger.getLogger(Server.class);

    private final Map<UUID, Player> players = new HashMap<>();

    private final Map<UUID, Room> rooms = new HashMap<>();

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

        LOGGER.debug("User " + player.getName() + " joined the server");
    }

    public void removePlayer(final UUID uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("uuid cannot be null");
        }

        Player player = this.players.get(uuid);
        if (player == null) {
            throw new IllegalArgumentException("this player does not exists");
        }

        LOGGER.debug("User " + player.getName() + " left the server");

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

    public void trackRoom(final Room room) {
        if (room == null) {
            throw new IllegalArgumentException("room is null");
        }

        this.rooms.put(room.getPlayerWhite().getUUID(), room);
        this.rooms.put(room.getPlayerBlack().getUUID(), room);
    }

    public void untrackRoom(final Room room) {
        if (room == null) {
            throw new IllegalArgumentException("room is null");
        }

        this.rooms.remove(room.getPlayerWhite().getUUID());
        this.rooms.remove(room.getPlayerBlack().getUUID());
    }

    public Room getRoom(final UUID playerUUID) {
        return this.rooms.get(playerUUID);
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
        ServerRunner listenerTCP = null;
        ServerRunner listenerUDP = null;

        try (ServerSocket socketTCP = new ServerSocket(port); DatagramSocket socketUDP = new DatagramSocket(port)) {
            listenerTCP = new ListenerTCP(this, socketTCP);
            listenerUDP = new ListenerUDP(this, socketUDP);

            Thread t1 = new Thread(listenerTCP);
            t1.start();

            LOGGER.debug("Started TCP listener");

            Thread t2 = new Thread(listenerUDP);
            t2.start();

            LOGGER.debug("Started UDP listener");

            t1.join();
            t2.join();

        } catch (InterruptedException e) {
            LOGGER.debug("Interrupting listeners...");

            if (listenerTCP != null)
                listenerTCP.requireStop();
            if (listenerUDP != null)
                listenerUDP.requireStop();

        } catch (IOException e) {
            LOGGER.fatal("Failed to create server listeners", e);
        }
    }
}
