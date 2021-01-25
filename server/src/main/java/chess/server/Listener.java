package chess.server;

abstract class Listener extends Thread {

    protected final Server server;

    public Listener(final Server server) {
        this.server = server;
    }
}
