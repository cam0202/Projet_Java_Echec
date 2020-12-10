package game;

public class Knight {
	private Chessman cm;
	private String c;
	
	/**
	 * Constructeur
	 * @param chessman
	 */
	Knight(Chessman chessman){
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
	boolean moveKnight(Move move){
		// movement en L 
		return (Math.abs(move.getLocationX()) / move.getLocationY()) == 2   
				|| (Math.abs(move.getLocationX() / move.getLocationY()) == .5);
	}

}
