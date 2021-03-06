package chess.game.chessman;

import chess.game.Move;
import chess.player.Player;

/**
 * 
 * Bishop Class
 * Fou
 *
 */
public class Bishop extends Chessman {

    private static Attack[] attacks = { new Attack("Baffle"), new Attack("Crush"), new Attack("Bloom") };

    public Bishop(Player player) {
        super("bishop", player,30,attacks);
    }

    @Override
    public boolean canMove(Move move) {
    	// déplacement en diagonale 
        return Math.abs(move.getDirectionRow()) - Math.abs(move.getDirectionCol()) == 0 && !(move.isNull());
    }

}
