package chess.game.chessman;

import chess.game.Move;
import chess.player.Player;

/**
 * 
 * Knight Class
 * Cavalier
 *
 */
public class Knight extends Chessman {

    private static Attack[] attacks = { new Attack("Torch"), new Attack("Dance"), new Attack("Bolt") };

    public Knight(Player player) {
        super("knight", player, 30, attacks);
    }

    @Override
    public boolean canMove(Move move) {
    	// movement en L 
        int row = Math.abs(move.getDirectionRow());
        int col = Math.abs(move.getDirectionCol());
        return ((row == 2 && col == 1) || (row == 1 && col == 2));
    }

}
