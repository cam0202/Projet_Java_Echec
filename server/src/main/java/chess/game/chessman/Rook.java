package chess.game.chessman;

import chess.game.Move;
import chess.player.Player;

public class Rook extends Chessman {

    private static Attack[] attacks = { new Attack("Enrage"), new Attack("Thrash"), new Attack("Calamity") };

    public Rook(Player player) {
        super("rook", player,50, attacks);
    }

    @Override
    public boolean canMove(Move move) {
    	// La tour ne peut que ce déplacer de façon verticale 
        return move.getDirectionRow() * move.getDirectionCol() == 0 && !move.isNull();
    }

}
