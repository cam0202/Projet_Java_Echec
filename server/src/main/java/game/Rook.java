package game;

public class Rook {
	
	private Chessman cm;
	private String c;
	
	/**
	 * Constructeur
	 * @param chessman
	 */
	Rook(Chessman chessman){
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
	 *  Déplacement de la reine 
	 * @param move
	 * @return
	 */
	boolean moveRook(Move move){
		// déplacement en vertical 
		return move.getLocationX() * move.getLocationY() == 0 && !move.isNul();
	}

}
