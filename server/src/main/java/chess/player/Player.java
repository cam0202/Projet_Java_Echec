package chess.player;

import java.util.UUID;

import chess.game.Color;

public class Player {
    private final UUID uuid;
    private String name;

    public Color color; // TODO: REMOVE

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
