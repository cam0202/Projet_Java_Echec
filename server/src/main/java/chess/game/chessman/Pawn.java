package chess.game.chessman;

import chess.game.Move;
import chess.player.Player;

/**
 * 
 * Pawn Class
 * Pion
 *
 */
public class Pawn extends Chessman {

    private final boolean positive;
    private static Attack[] attacks = { new Attack("Hellos"), new Attack("Tempest"), new Attack("Seism") };

    public Pawn(Player player, boolean positive) {
        super("pawn", player, 10, attacks);
        // ajout d'un boolean car le pion ne peut aller que vers l'avant, aide pour canMove.
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
        /**
		 * Un pion ne peut aller que vert l'avant pour un déplacement 
		 * donc sa coordonnee en x ne doit pas bouger.
		 * positive permet donc de savoir dans quel sens on avance
		 */
        if (this.positive) {
        	/**
			 * Lors du premier tour d'un pion on peu avancer de une ou deux case => 1 ? 2 : 1
			 * de plus un déplacement sera positif dans ce cas 
			 */
            return ((move.getDirectionCol()) <= (move.getStart().getCol() == 1 ? 2 : 1)) && (move.getDirectionCol() > 0);
        } else {
        	// Même principe mais le jours est de l'autre côté d'ou un déplacement négatif
            return ((move.getDirectionCol()) <= (move.getStart().getCol() == 6 ? -2 : -1)) && (move.getDirectionCol() < 0);
        }
    }

}
