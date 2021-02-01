package chess.game.chessman;

import chess.game.Move;
import chess.player.Player;

public class King extends Chessman {

    public King(Player player) {
        super("king", player);
    }

    @Override
    public boolean canMove(Move move) {
        return (!move.isNull()) && (Math.abs(move.getDirectionRow()) == 1 || Math.abs(move.getDirectionCol()) == 1);

        /*
         * return (Math.abs(move.getDirectionRow()) == 0 &&
         * Math.abs(move.getDirectionCol()) == 1) || (Math.abs(move.getDirectionRow())
         * == 1 && Math.abs(move.getDirectionCol()) == 0);
         */
    }

}
