package game;

public class Bishop {
	private Chessman cm;
	private String c;
	
	/**
	 * Constructeur
	 * @param chessman
	 */
	Bishop(Chessman chessman){
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
	boolean moveBishop(Move move){
		// déplacement en diagonale 
		return Math.abs(move.getLocationX()) - Math.abs(move.getLocationY()) == 0 && !move.isNul();
	}
}
