package chess.client;

import java.util.UUID;

public class Client {
    private final UUID uuid;
    private String name;

    public Client(final UUID uuid) {
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
