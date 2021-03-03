package chess.gui;

import com.googlecode.lanterna.gui2.Button;

public abstract class GameGUIPanelServerError extends GameGUIPanel {

    protected final String error;

    public GameGUIPanelServerError(final GameGUI game, final String error, final GameGUIPanel previous) {
        super(game, previous);
        
        this.error = error;
        this.addComponent(new Button("Go back", new ActionBack(this)));
    }
}
