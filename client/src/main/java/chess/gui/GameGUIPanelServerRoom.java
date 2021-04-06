package chess.gui;

import java.io.IOException;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import chess.server.Callback;

public class GameGUIPanelServerRoom extends GameGUIPanel {

    private static final Logger LOGGER = Logger.getLogger(GameGUIPanelServerRoom.class);

    private final Panel pRoomInfo;
    private final Panel pChat;
    private final Panel pBoard;

    private final TextBox cInput;

    public GameGUIPanelServerRoom(final GameGUI game) {
        this(game, null);
    }

    public GameGUIPanelServerRoom(final GameGUI game, final GameGUIPanel previous) {
        super(game, previous);

        this.pRoomInfo = new Panel();

        this.pChat = new Panel();

        this.pBoard = new Panel();

        this.cInput = new InputTextBox(this);

        Panel topLeft = new Panel();
        topLeft.setLayoutManager(new LinearLayout(Direction.VERTICAL));
        topLeft.addComponent(this.pRoomInfo);
        topLeft.addComponent(new EmptySpace());
        topLeft.addComponent(this.pBoard);

        Panel top = new Panel();
        top.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
        top.addComponent(topLeft.withBorder(Borders.singleLine(String.format(" %s (%s) ",
                this.getGame().getServer().getName(), this.getGame().getServer().getUsername()))));
        top.addComponent(pChat);

        Panel bottom = new Panel();
        bottom.setLayoutManager(new LinearLayout(Direction.VERTICAL));
        bottom.addComponent(cInput.withBorder(Borders.singleLine(" Your input ")));

        this.setLayoutManager(new LinearLayout(Direction.VERTICAL));
        this.addComponent(new Button("Quit room", new ActionBackWrapper(this)));
        this.addComponent(top);
        this.addComponent(new EmptySpace());
        this.addComponent(bottom);

        this.pChat.addComponent(makeChatSystemItem("Welcome to the game room!"));
    }

    @Override
    public void update() {
        this.getGame().getServer().setUpdateCallback(new UpdateCallback(this));

        // Setup info we did not know

        // Refresh the room state
        try {
            this.pRoomInfo.removeAllComponents();

            JSONObject root = new JSONObject(this.getGame().getServer().get("room"));
            int roomNum = root.getInt("number");

            root = new JSONObject(root.getString("board"));
            String whiteUsername = root.getString("white");
            String blackUsername = root.getString("black");
            String whoIsNextUsername = root.getString("whoIsNext");

            String ourUsername = this.getGame().getServer().getUsername();

            TextColor ourColor = whiteUsername.equals(ourUsername) ? TextColor.ANSI.WHITE : TextColor.ANSI.BLACK;
            TextColor otherColor = blackUsername.equals(ourUsername) ? TextColor.ANSI.WHITE : TextColor.ANSI.BLACK;

            this.pRoomInfo.addComponent(new Label(String.format("Playing in room #%d against %s", roomNum,
                    ourUsername.equals(whiteUsername) ? blackUsername : whiteUsername)));

            this.pRoomInfo.addComponent(
                    new Label(String.format("You are %s", ourColor.equals(TextColor.ANSI.WHITE) ? "white" : "black")));
            this.pRoomInfo.addComponent(new Label(
                    String.format("It's %s", whoIsNextUsername.equals(ourUsername) ? "your turn" : "not your turn")));

            // Update whole board
            this.pBoard.removeAllComponents();
            this.pBoard.setLayoutManager(new GridLayout(9).setHorizontalSpacing(0).setVerticalSpacing(0));

            JSONArray arr = root.getJSONArray("board");

            for (int number = 8 - 1; number >= 0; number--) {
                this.pBoard.addComponent(new BoardTile((char) ('1' + number)));

                for (int letter = 0; letter < 8; letter++) {
                    JSONObject chessman = arr.getJSONArray(letter).optJSONObject(number);
                    TextColor bg = (letter + number) % 2 == 0 ? TextColor.ANSI.BLUE_BRIGHT
                            : TextColor.ANSI.GREEN_BRIGHT;

                    if (chessman != null) {
                        TextColor fg = chessman.getString("player").equals(this.getGame().getServer().getUsername())
                                ? ourColor
                                : otherColor;

                        char symbol = '?';
                        switch (chessman.getString("chessman")) {
                        case "pawn":
                            symbol = '♟';
                            break;
                        case "bishop":
                            symbol = '♝';
                            break;
                        case "king":
                            symbol = '♚';
                            break;
                        case "knight":
                            symbol = '♞';
                            break;
                        case "rook":
                            symbol = '♜';
                            break;
                        case "queen":
                            symbol = '♛';
                            break;
                        }

                        this.pBoard.addComponent(new BoardTile(symbol, fg, bg));
                    } else {
                        this.pBoard.addComponent(new BoardTile(' ', TextColor.ANSI.DEFAULT, bg));
                    }
                }
            }

            this.pBoard.addComponent(new BoardTile(' '));

            for (int col = 0; col < 8; col++) {
                this.pBoard.addComponent(new BoardTile((char) ('a' + col)));

            }

        } catch (IOException e) {
            this.pChat.addComponent(makeChatSystemItem("Error: " + e.getMessage()));
        }
    }

    private Component makeChatPlayerItem(String playerName, String message) {
        return new Label(String.format("<%s> %s", playerName, message));
    }

    private Component makeChatSystemItem(String message) {
        return new Label(String.format(">>> %s", message));
    }

    private class BoardTile extends Label {

        public BoardTile(char value) {
            super(value + " ");
        }

        public BoardTile(char value, TextColor fg, TextColor bg) {
            super(value + " ");
            this.setForegroundColor(fg);
            this.setBackgroundColor(bg);
        }

    }

    private class InputTextBox extends TextBox {

        private final GameGUIPanelServerRoom panel;

        public InputTextBox(final GameGUIPanelServerRoom panel) {
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

            case "play": {
                JSONObject dataRoot = root.getJSONObject("data");
                this.panel.pChat.addComponent(makeChatSystemItem(dataRoot.getString("message")));
                this.panel.setRequireUpdate(true);
            }

            default:
                throw new JSONException("unknown action");
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
