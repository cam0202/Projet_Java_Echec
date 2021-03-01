package chess.cli;

import com.googlecode.lanterna.gui2.Label;

public class GameCLIPanelServerErrorConnect extends GameCLIPanelServerError {

    public GameCLIPanelServerErrorConnect(final GameCLI game, final String error, final GameCLIPanel previous) {
        super(game, error, previous);

        this.addComponent(new Label("Failed to connect to server: " + this.error));
    }

    @Override
    public void update() {

    }

}
