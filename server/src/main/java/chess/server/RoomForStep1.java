package chess.server;

import org.apache.log4j.Logger;

import chess.game.Game;
import chess.game.Move;
import chess.player.Player;

public class RoomForStep1 {
    private final static Logger LOGGER = Logger.getLogger(RoomForStep1.class);

    private final Game board;

    private final Player player1;
    private final Player player2;

    public RoomForStep1(final Player player1, final Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.board = new Game(player1, player2);
    }

    public void doCommand(Player player, String command) {
        if (command.length() != 4) {
            throw new RuntimeException("invalid command");
        }

        char[] data = command.toUpperCase().toCharArray();
        Move move = this.board.createMove(data[0], Integer.valueOf(data[1]), data[2], Integer.valueOf(data[3]));

        this.board.move(player, move);
        LOGGER.debug("MOVE command " + command);

        /*
         * Move move = this.board.getMove(command);
         * 
         * if (player.getUUID().equals(player1.getUUID())) {
         * this.board.move(player1.color, move); } else if
         * (player.getUUID().equals(player1.getUUID())) { this.board.move(player2.color,
         * move); } else { throw new RuntimeException("failed to do command..."); }
         */
    }
}