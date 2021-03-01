package chess.cli;

import com.googlecode.lanterna.gui2.Button;

public abstract class GameCLIPanelServerError extends GameCLIPanel {

    protected final String error;

    public GameCLIPanelServerError(final GameCLI game, final String error, final GameCLIPanel previous) {
        super(game, previous);
        
        this.error = error;
        this.addComponent(new Button("Go back", new ActionBack(this)));
    }
}
