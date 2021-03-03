package chess.gui;

public abstract class GameGUIAction implements Runnable {
    
    protected final GameGUIPanel panel;

    public GameGUIAction(final GameGUIPanel panel) {
        this.panel = panel;
    }

}
