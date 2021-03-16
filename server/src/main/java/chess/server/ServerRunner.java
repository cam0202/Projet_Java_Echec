package chess.server;

/**
 * Abstract class defining what a server listener should look like
 */
abstract class ServerRunner implements Runnable {

    protected final Server server;

    private boolean stop;

    public ServerRunner(final Server server) {
        this.server = server;
        this.stop = false;
    }

    public void requireStop() {
        this.stop = true;
    }

    public boolean shouldStop() {
        return this.stop;
    }
}
