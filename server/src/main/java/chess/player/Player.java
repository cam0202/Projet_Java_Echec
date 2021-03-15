package chess.player;

import java.net.Socket;
import java.util.UUID;

/**
 * Represents a player, so they can be disassociated from they transport layer
 * session
 */
public class Player {
    private final UUID uuid;
    private final Socket socketTCP;
    private String name;

    public Player(final UUID uuid) {
        this(uuid, null);
    }

    public Player(final UUID uuid, final Socket socketTCP) {
        if (uuid == null) {
            throw new IllegalArgumentException("uuid is null");
        }

        this.uuid = uuid;
        this.socketTCP = socketTCP;
        this.name = this.uuid.toString();
    }

    public void setName(final String name) {
        this.name = name;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public Socket getSocket() {
        return this.socketTCP;
    }

    public String getName() {
        return this.name;
    }

}
