package chess.gui;

import com.googlecode.lanterna.gui2.Label;

public class GameGUIPanelServerErrorConnect extends GameGUIPanelServerError {

    public GameGUIPanelServerErrorConnect(final GameGUI game, final String error, final GameGUIPanel previous) {
        super(game, error, previous);

        this.addComponent(new Label("Failed to connect to server: " + this.error));
    }

    @Override
    public void update() {

    }

}
