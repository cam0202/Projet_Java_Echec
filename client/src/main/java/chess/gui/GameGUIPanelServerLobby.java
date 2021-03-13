package chess.gui;

import java.io.IOException;

import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Label;

import org.apache.log4j.Logger;

import chess.server.Callback;

public class GameGUIPanelServerLobby extends GameGUIPanel {

    private static final Logger LOGGER = Logger.getLogger(GameGUIPanelServerLobby.class);

    public GameGUIPanelServerLobby(final GameGUI game) {
        this(game, null);
    }

    public GameGUIPanelServerLobby(final GameGUI game, final GameGUIPanel previous) {
        super(game, previous);

        this.addComponent(new Button("Go back", new ActionBackWrapper(this)));
        this.addComponent(new Label("CONNECTED!"));

        this.getGame().getServer().setUpdateCallback(new UpdateCallback());
    }

    @Override
    public void update() {

    }

    private class ActionBackWrapper extends ActionBack {

        public ActionBackWrapper(GameGUIPanel panel) {
            super(panel);
        }

        @Override
        public void run() {
            try {
                this.panel.getGame().getServer().setUpdateCallback(null);
                this.panel.getGame().getServer().disconnect();
                super.run();
            } catch (IOException e) {
                LOGGER.error("Failed to disconnect from server", e);
            }
        }
    }

    private class UpdateCallback implements Callback {

        @Override
        public void onUpdate(String data) {
            System.out.println("TEST!!");
        }

    }

}
