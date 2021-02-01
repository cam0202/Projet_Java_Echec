package chess.game.chessman;

import chess.game.Move;
import chess.player.Player;

public class Rook extends Chessman {

    public Rook(Player player) {
        super("rook", player);
    }

    @Override
    public boolean canMove(Move move) {
        return move.getDirectionRow() * move.getDirectionCol() == 0 && !move.isNull();
    }

}
