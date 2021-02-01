package chess.game.chessman;

import chess.game.Move;
import chess.player.Player;

public class Bishop extends Chessman {

    public Bishop(Player player) {
        super("bishop", player);
    }

    @Override
    public boolean canMove(Move move) {
        return Math.abs(move.getDirectionRow()) - Math.abs(move.getDirectionCol()) == 0 && !(move.isNull());
    }

}
