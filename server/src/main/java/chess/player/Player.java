package chess.player;

import java.util.UUID;

/**
 * Represents a player, so they can be disassociated from they transport layer
 * session
 */
public class Player {
    private final UUID uuid;
    private String name;

    public Player(final UUID uuid) {
        this.uuid = uuid;
        this.name = this.uuid.toString();
    }

    public void setName(final String name) {
        this.name = name;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

}
