package chess.game.chessman;

import chess.game.Move;
import chess.player.Player;

public class Pawn extends Chessman {

    private final boolean positive;
    private static Attack[] attacks = { new Attack("Hello"), new Attack("Tempest"), new Attack("Seism") };

    public Pawn(Player player, boolean positive) {
        super("pawn", player, 10, attacks);
        this.positive = positive;
    }

    @Override
    public boolean canMove(Move move) {
        if (move.isNull()) {
            return false;
        }

        if (move.getDirectionRow() != 0) {
            return false;
        }

        if (this.positive) {
            return ((move.getDirectionCol()) <= (move.getStart().getCol() == 1 ? 2 : 1)) && (move.getDirectionCol() > 0);
        } else {
            return ((move.getDirectionCol()) <= (move.getStart().getCol() == 6 ? -2 : -1)) && (move.getDirectionCol() < 0);
        }
    }

}
