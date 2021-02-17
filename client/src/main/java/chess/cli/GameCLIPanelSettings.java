package chess.cli;

import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Label;

public class GameCLIPanelSettings extends GameCLIPanel {

    public GameCLIPanelSettings(GameCLI game) {
        this(game, null);
    }

    public GameCLIPanelSettings(GameCLI game, GameCLIPanel previous) {
        super(game, previous);

        this.addComponent(new Button("Go back", new ActionBack(this)));
        this.addComponent(new Label("TODO")); // TODO
    }

    @Override
    public void update() {
        // Nothing to do here
    }

}
