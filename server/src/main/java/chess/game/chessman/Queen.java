package chess.game.chessman;

import chess.game.Move;
import chess.player.Player;

/**
 * 
 * Queen Class 
 * Reine
 *
 */
public class Queen extends Chessman {

    private static Attack[] attacks = { new Attack("Blizzard"), new Attack("Warp Time"), new Attack("Punch")};

    public Queen(Player player) {
        super("queen", player, 90, attacks);
    }

    @Override
    public boolean canMove(Move move) {
    	// déplacement en diagonale ou rectiligen 
        return (Math.abs(move.getDirectionRow()) - Math.abs(move.getDirectionCol()) == 0
                || move.getDirectionRow() * move.getDirectionCol() == 0) && !move.isNull();
    }

}
