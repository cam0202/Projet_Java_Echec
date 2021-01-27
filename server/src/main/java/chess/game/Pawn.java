package chess.game;
import chess.player.*;
/**
 * 
 * Pawn Class
 *
 */
public class Pawn extends Chessman{
	

	/**
	 * Pawn Constructor 
	 * @param color
	 */
	Pawn(Player p, int c){
		super("Pawn", p , c);
	}

	/**
	 * isOk, vérification du déplacement pour un pion
	 * @param Move
	 */
	public boolean isOk(Move move) {
		/**
		 * Un pion ne peut aller que vert l'avant pour un déplacement 
		 * donc sa coordonnee en x ne doit pas bouger.
		 */
		if(move.getLocationX() == 0 ) {
			/**
			 * Ici on définit que le jours un pourra avoir les couleurs suivante :
			 * noir, rouge, orange et violet 
			 * cela nous permet de vérifier que le joueur un pourra avancer son pion qu'en direction de l'avant
			 */
			if(this.getNumber() == 0) {
				/**
				 * Lors du premier tour d'un pion on peu avancer de une ou deux case => 1 ? 2 : 1
				 * de plus un déplacement sera positif dans ce cas 
				 */
				return ((move.getLocationY() <= (move.getStart().getRow() == 1 ? 2 : 1)) && (move.getLocationY() > 0)) ;
			} else {
				// Même principe mais le jours est de l'autre côté d'ou un déplacement négatif  
				return ((move.getLocationY() <= (move.getStart().getRow() == 6 ? -2 : -1)) && (move.getLocationY() < 0)) ;
			}
		} else {
			return false;
		}
	}
}
