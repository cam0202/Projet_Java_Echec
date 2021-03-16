package chess.server;

abstract class ServerRunner implements Runnable {

    private boolean stop;

    public ServerRunner() {
        this.stop = false;
    }

    public void requireStop() {
        this.stop = true;
    }

    public boolean shouldStop() {
        return this.stop;
    }
}
