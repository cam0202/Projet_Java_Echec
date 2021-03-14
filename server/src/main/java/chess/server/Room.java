package chess.server;

import org.apache.log4j.Logger;

import chess.game.Board;
import chess.game.BoardException;
import chess.game.Move;
import chess.player.Player;

public class Room {

    private final static Logger LOGGER = Logger.getLogger(Room.class);

    private final Player white;
    private final Player black;

    private final Board board;

    public Room(final Player white, final Player black) {
        if (white == null) {
            throw new IllegalArgumentException("player white is null");
        }

        if (black == null) {
            throw new IllegalArgumentException("player black is null");
        }

        this.white = white;
        this.black = black;

        this.board = new Board(this.white, this.black);
    }

    public Board getBoard() {
        return this.board;
    }

    public Player getPlayerWhite() {
        return this.white;
    }

    public Player getPlayerBlack() {
        return this.black;
    }

    public String play(final Player player, final String command) throws BoardException {
        LOGGER.debug("PLAY command " + command);

        String cmd = command.toLowerCase();
        if (cmd.length() == 4) {
            // This is a MOVE command
            char[] data = cmd.toCharArray();
            Move move = new Move(data[0], data[1], data[2], data[3]);
            return this.board.play(player, move, null);
        } else {
            // This is an ATTACK command
            return this.board.play(player, null, cmd);
        }
    }

}
