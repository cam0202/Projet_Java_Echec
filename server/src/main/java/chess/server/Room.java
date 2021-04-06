package chess.server;

import java.util.Random;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import chess.game.Board;
import chess.game.BoardException;
import chess.game.Move;
import chess.player.Player;

public class Room {

    private final static Logger LOGGER = Logger.getLogger(Room.class);

    private final Player white;
    private final Player black;

    private final Board board;

    private final int number;

    public Room(final Player white, final Player black) {
        this(white, black, null);
    }

    public Room(final Player white, final Player black, final String boardSerialized) {
        if (white == null) {
            throw new IllegalArgumentException("player white is null");
        }

        if (black == null) {
            throw new IllegalArgumentException("player black is null");
        }

        this.white = white;
        this.black = black;

        this.board = new Board(this.white, this.black);
        if (boardSerialized != null) {
            this.board.deserialize(boardSerialized);
        }

        this.number = new Random().nextInt(8999) + 1000;
    }

    public Player getPlayerWhite() {
        return this.white;
    }

    public Player getPlayerBlack() {
        return this.black;
    }

    public void sendChatMessage(final Player sender, final String message) {
        if (sender == null) {
            throw new IllegalArgumentException("sender is null");
        }

        if (message == null) {
            throw new IllegalArgumentException("message is null");
        }

        if (sender.equals(this.white)) {

        } else if (sender.equals(this.black)) {

        } else {
            throw new IllegalArgumentException("player is not in this room");
        }
    }

    public JSONObject getState() {
        JSONObject root = new JSONObject();
        root.put("number", this.number);
        root.put("board", this.board.serialize());
        return root;
    }

    public String play(final Player player, final String command) throws BoardException {
        LOGGER.debug("PLAY command " + command);

        String cmd = command.toLowerCase();
        if (cmd.length() == 4) {
            // This may be a MOVE command
            char[] data = cmd.toCharArray();
            if (Character.isDigit(data[1]) && Character.isDigit(data[3])) {
                // This is a MOVE command
                Move move = new Move(data[0], data[1], data[2], data[3]);
                return this.board.play(player, move, null);
            }
        }

        // This is an ATTACK command
        return this.board.play(player, null, cmd);
    }

}
