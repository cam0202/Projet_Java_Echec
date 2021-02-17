package chess.cli;

import com.googlecode.lanterna.gui2.Panel;

public abstract class GameCLIPanel extends Panel {
    
    private final GameCLI game;
    private final GameCLIPanel previous;

    public GameCLIPanel(final GameCLI game) {
        this(game, null);
    }

    public GameCLIPanel(final GameCLI game, final GameCLIPanel previous) {
        if (game == null) {
            throw new IllegalArgumentException("game cannot be null");
        }

        this.game = game;
        this.previous = previous;
    }

    protected GameCLI getGame() {
        return this.game;
    }

    public abstract void update();

    protected class ActionBack extends GameCLIAction {

        public ActionBack(final GameCLIPanel panel) {
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
