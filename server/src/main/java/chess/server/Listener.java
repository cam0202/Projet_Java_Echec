package chess.server;

/**
 * Abstract class defining what a server listener should look like
 */
abstract class Listener extends Thread {

    protected final Server server;

    public Listener(final Server server) {
        this.server = server;
    }
}
