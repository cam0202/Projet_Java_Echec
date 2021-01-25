package chess.player;

import java.util.UUID;

public class Player {
    private final UUID uuid;
    private String name;

    public Player(final UUID uuid) {
        this.uuid = uuid;
        this.name = this.uuid.toString();
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}