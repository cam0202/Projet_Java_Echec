package chess.gui;

import com.googlecode.lanterna.gui2.Panel;

public abstract class GameGUIPanel extends Panel {
    
    private final GameGUI game;
    private final GameGUIPanel previous;

    private boolean requireUpdate;

    public GameGUIPanel(final GameGUI game) {
        this(game, null);
    }

    public GameGUIPanel(final GameGUI game, final GameGUIPanel previous) {
        if (game == null) {
            throw new IllegalArgumentException("game cannot be null");
        }

        this.game = game;
        this.previous = previous;

        this.requireUpdate = true;
    }

    protected GameGUI getGame() {
        return this.game;
    }

    public void setRequireUpdate(boolean value) {
        this.requireUpdate = value;
    }

    public boolean getRequireUpdate() {
        return this.requireUpdate;
    }

    public abstract void update();

    protected class ActionBack extends GameGUIAction {

        public ActionBack(final GameGUIPanel panel) {
            super(panel);
        }

        @Override
        public void run() {
            if (this.panel.previous != null) {
                this.panel.game.switchPanel(this.panel.previous);
            }
        }

    }

}
