package game;

public class Pawn {
	
	private String c;
	private Chessman cm;
	/**
	 * Contructeur du Pion
	 */
	Pawn(Chessman chessman){
		this.cm = chessman;
		this.c = this.cm.getColor();
	}
	
	/**
	 * Fonctions qui retourne les infos
	 */
	String getType() {
		return this.cm.getName();
	}
	
	String getColor() {
		return this.c;
	}
	
	/**
	 * Déplacement du pion
	 * @param m
	 */
	boolean movePawn(Move move) {
		
		if(move.getLocationX() == 0 ) {
			// le déplacement possible dépend de la couleur 
			if(this.c.equals("black") || this.c.equals("red") || this.c.equals("orange") || this.c.equals("violet")) {
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
