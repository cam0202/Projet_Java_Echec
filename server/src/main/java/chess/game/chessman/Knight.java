package chess.game.chessman;

import chess.game.Move;
import chess.player.Player;

public class Knight extends Chessman {

    public Knight(Player player) {
        super("knight", player);
    }

    @Override
    public boolean canMove(Move move) {
        int row = Math.abs(move.getDirectionRow());
        int col = Math.abs(move.getDirectionCol());
        return ((row == 2 && col == 1) || (row == 1 && col == 2));

        /*
         * return (Math.abs(move.getDirectionRow() / move.getDirectionCol())) == 2 |
         * (Math.abs(move.getDirectionRow() / move.getDirectionCol())) == .5;
         */
    }

}
