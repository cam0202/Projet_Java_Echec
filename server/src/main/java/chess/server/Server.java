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
    private final Map<UUID, Room> playersToRooms = new HashMap<>();

    private final int port;
    private final UUID uuid;

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

    public void addRoom(final Room room) {
        if (room == null) {
            throw new IllegalArgumentException("room is null");
        }

        this.playersToRooms.put(room.getPlayerWhite().getUUID(), room);
        this.playersToRooms.put(room.getPlayerBlack().getUUID(), room);
    }

    public void removeRoom(final Room room) {
        if (room == null) {
            throw new IllegalArgumentException("room is null");
        }

        this.playersToRooms.remove(room.getPlayerWhite().getUUID());
        this.playersToRooms.remove(room.getPlayerBlack().getUUID());
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

    public List<Player> getPlayers() {
        return new ArrayList<Player>(this.players.values());
    }

    public Room getPlayerRoom(final UUID playerUUID) {
        return this.playersToRooms.get(playerUUID);
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
