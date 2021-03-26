package chess.gui;

import java.io.IOException;

import com.googlecode.lanterna.gui2.Button;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import chess.server.Callback;

public class GameGUIPanelServerRoom extends GameGUIPanel {

    private static final Logger LOGGER = Logger.getLogger(GameGUIPanelServerRoom.class);

    public GameGUIPanelServerRoom(final GameGUI game) {
        this(game, null);
    }

    public GameGUIPanelServerRoom(final GameGUI game, final GameGUIPanel previous) {
        super(game, previous);

        this.addComponent(new Button("Quit room", new ActionBackWrapper(this)));

    }

    @Override
    public void update() {
        this.getGame().getServer().setUpdateCallback(new UpdateCallback(this));
    }

    private class ActionBackWrapper extends ActionBack {

        public ActionBackWrapper(final GameGUIPanel panel) {
            super(panel);
        }

        @Override
        public void run() {
            try {
                this.panel.getGame().getServer().post("/stop");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class UpdateCallback implements Callback {

        private final GameGUIPanelServerRoom panel;

        public UpdateCallback(final GameGUIPanelServerRoom panel) {
            this.panel = panel;
        }

        @Override
        public void onUpdate(String data) {
            try {
                JSONObject root = new JSONObject(data);
                switch (root.getString("scope")) {
                case "state":
                    processSTATE(root);
                    break;

                default:
                    // TODO
                    break;
                }
            } catch (JSONException e) {
                // TODO
            }
        }

        private void processSTATE(final JSONObject root) throws JSONException {
            switch (root.getString("action")) {
            case "switch_to_lobby": {
                this.panel.getGame().getServer().setUpdateCallback(null);
                new ActionBack(this.panel).run();
                break;
            }
            }
        }

    }

}
