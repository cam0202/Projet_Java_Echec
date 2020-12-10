package game;

public class King {
	private Chessman cm;
	private String c;
	
	/**
	 * Constructeur
	 * @param chessman
	 */
	King(Chessman chessman){
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
	 *  Déplacement du roi
	 * @param move
	 * @return
	 */
	boolean moveKing(Move move){
		// le roi ne peux se déplacer que 1 case 
		return Math.abs(move.getLocationX()) * Math.abs(move.getLocationY()) <= 1  
				&& Math.abs(move.getLocationX()) - Math.abs(move.getLocationY()) <= 1
				&& Math.abs(move.getLocationX()) - Math.abs(move.getLocationY()) >= -1
				&& !move.isNul();
	}

}
