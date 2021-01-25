package chess.server;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.UUID;

import org.apache.log4j.Logger;

import chess.player.Player;

public class Server {
    private final static Logger LOGGER = Logger.getLogger(Server.class);

    private final HashMap<UUID, Player> players = new HashMap<>();

    private final int port;

    public Server(final int port) {
        this.port = port;
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

    public String getName() {
        return "The Chess Server"; // TODO
    }

    public String getDescription() {
        return "Where everything is happening"; // TODO
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
            // Set timeouts because accept() and receive() are blocking and
            // non-interruptible
            socketTCP.setSoTimeout(200);
            socketUDP.setSoTimeout(200);

            listenerTCP = new ListenerTCP(this, socketTCP);
            listenerTCP.start();

            listenerUDP = new ListenerUDP(this, socketUDP);
            listenerUDP.start();

            listenerTCP.join();
            listenerUDP.join();

        } catch (InterruptedException e) {
            if (listenerTCP != null)
                listenerTCP.interrupt();
            if (listenerUDP != null)
                listenerUDP.interrupt();

        } catch (IOException e) {
            LOGGER.fatal("Failed to create server sockets", e);
        }
    }
}
