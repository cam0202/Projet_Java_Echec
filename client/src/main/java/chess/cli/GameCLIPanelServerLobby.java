package chess.cli;

import java.io.IOException;

import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Label;

import org.apache.log4j.Logger;

public class GameCLIPanelServerLobby extends GameCLIPanel {

    private static final Logger LOGGER = Logger.getLogger(GameCLIPanelServerLobby.class);

    public GameCLIPanelServerLobby(final GameCLI game) {
        this(game, null);
    }

    public GameCLIPanelServerLobby(final GameCLI game, final GameCLIPanel previous) {
        super(game, previous);

        this.addComponent(new Button("Go back", new ActionBackWrapper(this)));
        this.addComponent(new Label("CONNECTED!"));
    }

    @Override
    public void update() {

    }

    private class ActionBackWrapper extends ActionBack {

        public ActionBackWrapper(GameCLIPanel panel) {
            super(panel);
        }

        @Override
        public void run() {
            try {
                this.panel.getGame().getServer().disconnect();
                super.run();
            } catch (IOException e) {
                LOGGER.error("Failed to disconnect from server", e);
            }
        }
    }

}
