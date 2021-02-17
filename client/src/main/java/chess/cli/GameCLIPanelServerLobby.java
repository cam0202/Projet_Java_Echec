package chess.cli;

import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Label;

public class GameCLIPanelServerLobby extends GameCLIPanel {

    public GameCLIPanelServerLobby(final GameCLI game) {
        this(game, null);
    }

    public GameCLIPanelServerLobby(final GameCLI game, final GameCLIPanel previous) {
        super(game, previous);

        this.addComponent(new Button("Go back", new ActionBack(this)));
        this.addComponent(new Label("CONNECTED!"));
    }

    @Override
    public void update() {

    }

}
