package game;

public class Pawn extends Chessman{
	

	/**
	 * Contructeur du Pion
	 */
	Pawn(Color c){
		super("Pawn", c);
	}


	@Override
	public boolean isOk(Move move) {
		if(move.getLocationX() == 0 ) {
			// le déplacement possible dépend de la couleur 
			if(this.getColor().equals("black") || this.getColor().equals("red") || this.getColor().equals("orange") || this.getColor().equals("violet")) {
				// si la couleur du joueur 1 est noir, bleu, orange ou violet alors le déplacement sera positif
				return move.getLocationY() <= (move.getStart().getRow() == 1 ? 2 : 1) && move.getLocationY() > 0 ;
				// 1 ? 2 : 1 cela permet de prendre en compte le fait qu'au premier déplacement le joueur peut avancer de 2 case
			} else {
				// sinon autre couleur et alors le déplacement et considérer comme négatif 
				return move.getLocationY() <= (move.getStart().getRow() == 6 ? -2 : -1) && move.getLocationY() < 0 ;
			}
		} else {
			return false;
		}
	}
}
