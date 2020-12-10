package game;

public class Move {
	/**
	 * Deplacement sur l'axe des X
	 */
	private double moveX;
	
	/**
	 * Deplacement sur l'axe des Y
	 */
	private double moveY;
	
	/**
	 * Coordonnee de la case d'arrivee
	 */
	private Location e;
	
	/**
	 * Coordonnee de la case de depart
	 */
	private Location s;
	

	/**Constructeur d'un objet déplacement, 
	 * @param Prend en paramètre les coordonnees de depart et d'arrive du deplacement => position 
	 */
	Move(Location start, Location end){
		this.e = end;
		this.s = start;
		this.moveX = end.getColumn() - start.getColumn();
		this.moveY = end.getRow() - start.getRow();
	}


	double getLocationX() {
		return moveX;
	}

	double getLocationY() {
		return moveY;
	}
	
	Location getEnd() {
		return e;
	}

	Location getStart() {
		return s;
	}
	
	boolean isNul(){
		return moveX == 0 && moveY == 0;
	}
}
