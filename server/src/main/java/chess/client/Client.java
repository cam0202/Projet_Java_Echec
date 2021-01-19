package chess.client;

import java.net.Socket;
import java.util.UUID;


public class Client {

    private UUID uuid;

    private final Socket socketTCP;

    public Client(final Socket socketTCP) {
        this.socketTCP = socketTCP;
    }

}
