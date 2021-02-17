package chess.cli;

public abstract class GameCLIAction implements Runnable {
    
    protected final GameCLIPanel panel;

    public GameCLIAction(final GameCLIPanel panel) {
        this.panel = panel;
    }

}
