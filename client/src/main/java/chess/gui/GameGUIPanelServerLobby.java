package chess.gui;

import java.io.IOException;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import chess.server.Callback;

public class GameGUIPanelServerLobby extends GameGUIPanel {

    private static final Logger LOGGER = Logger.getLogger(GameGUIPanelServerLobby.class);

    private final Panel pServerInfo;
    private final Panel pChat;

    private final TextBox cInput;

    public GameGUIPanelServerLobby(final GameGUI game) {
        this(game, null);
    }

    public GameGUIPanelServerLobby(final GameGUI game, final GameGUIPanel previous) {
        super(game, previous);

        this.pServerInfo = new Panel();
        this.pServerInfo.addComponent(new Label(String.format("<%s>", this.getGame().getServer().getName())));
        this.pServerInfo.addComponent(new Label(String.format("%s", this.getGame().getServer().getDescription())));

        this.pChat = new Panel();

        this.cInput = new InputTextBox(this);

        Panel top = new Panel();
        top.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
        top.addComponent(this.pServerInfo
                .withBorder(Borders.singleLine(" Connected as " + this.getGame().getServer().getUsername() + " ")));
        top.addComponent(this.pChat);

        Panel bottom = new Panel();
        bottom.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
        bottom.addComponent(this.cInput.withBorder(Borders.singleLine(" Your input ")));

        this.setLayoutManager(new LinearLayout(Direction.VERTICAL));
        this.addComponent(new Button("Quit server", new ActionBackWrapper(this)));
        this.addComponent(new EmptySpace());
        this.addComponent(top);
        this.addComponent(new EmptySpace());
        this.addComponent(bottom);

        this.pChat.addComponent(makeChatSystemItem("Welcome to the server! Use the /start command to play."));
    }

    @Override
    public void update() {
        this.getGame().getServer().setUpdateCallback(new UpdateCallback(this));
    }

    private Component makeChatPlayerItem(String playerName, String message) {
        return new Label(String.format("<%s> %s", playerName, message));
    }

    private Component makeChatSystemItem(String message) {
        return new Label(String.format(">>> %s", message));
    }

    private class InputTextBox extends TextBox {

        private final GameGUIPanelServerLobby panel;

        public InputTextBox(final GameGUIPanelServerLobby panel) {
            super(new TerminalSize(80, 1));
            this.panel = panel;
        }

        @Override
        public synchronized Result handleKeyStroke(KeyStroke keyStroke) {
            // Send message on enter key
            if (keyStroke.getKeyType() == KeyType.Enter) {
                try {
                    if (!this.getText().isEmpty()) {
                        this.panel.getGame().getServer().post(this.getText());
                        this.setText("");
                    }
                } catch (IOException e) {
                    this.panel.pChat.addComponent(makeChatSystemItem("Error: " + e.getMessage()));
                }

                return Result.HANDLED;
            }

            return super.handleKeyStroke(keyStroke);
        }

    }

    private class ActionBackWrapper extends ActionBack {

        public ActionBackWrapper(final GameGUIPanel panel) {
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

        private final GameGUIPanelServerLobby panel;

        public UpdateCallback(final GameGUIPanelServerLobby panel) {
            this.panel = panel;
        }

        @Override
        public void onUpdate(String data) {
            try {
                JSONObject root = new JSONObject(data);
                switch (root.getString("scope")) {
                case "chat":
                    processCHAT(root);
                    break;

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

        private void processCHAT(final JSONObject root) throws JSONException {
            switch (root.getString("action")) {
            case "player_chat": {
                JSONObject dataRoot = root.getJSONObject("data");
                this.panel.pChat.addComponent(
                        makeChatPlayerItem(dataRoot.getString("username"), dataRoot.getString("message")));
                break;
            }

            case "player_join": {
                JSONObject dataRoot = root.getJSONObject("data");
                this.panel.pChat.addComponent(makeChatSystemItem(dataRoot.getString("username") + " joined the game"));
                break;
            }

            case "player_leave": {
                JSONObject dataRoot = root.getJSONObject("data");
                this.panel.pChat.addComponent(makeChatSystemItem(dataRoot.getString("username") + " left the game"));
                break;
            }

            default:
                throw new JSONException("unknown action");
            }
        }

        private void processSTATE(final JSONObject root) throws JSONException {
            switch (root.getString("action")) {
            case "switch_to_room": {
                this.panel.getGame().getServer().setUpdateCallback(null);
                this.panel.getGame().switchPanel(new GameGUIPanelServerRoom(this.panel.getGame(), this.panel));
                break;
            }
            }

            // TODO
        }

    }

}
