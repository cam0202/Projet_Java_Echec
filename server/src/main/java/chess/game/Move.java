package chess.game;
/**
 * 
 * Move Class
 * Sert à représenter un déplacement sur le plateau de jeu
 *
 */
public class Move {
	/**
	 * double Deplacement sur l'axe des X
	 */
	private double moveX;
	
	/**
	 * double Deplacement sur l'axe des Y
	 */
	private double moveY;
	
	/**
	 * Location Coordonnee de la case d'arrivee
	 */
	private Location e;
	
	/**
	 * Location Coordonnee de la case de depart
	 */
	private Location s;
	

	/**
	 * Move Constructor 
	 * @param start
	 * @param end
	 */
	Move(Location start, Location end){
		this.e = end;
		this.s = start;
		this.moveX = end.getColumn() - start.getColumn();
		this.moveY = end.getRow() - start.getRow();
	}
	
	

	/**
	 * getLocationX, donne la coordonné en x
	 * @return double
	 */
	double getLocationX() {
		return moveX;
	}
	
	/**
	 * getLocationY, donne la coordonné en y
	 * @return double
	 */
	double getLocationY() {
		return moveY;
	}
	
	/**
	 * getEnd, Coordonnees finale
	 * @return Location
	 */
	Location getEnd() {
		return e;
	}
	
	/**
	 * getStart coordonnees de départ
	 * @return Location
	 */
	Location getStart() {
		return s;
	}
	
	/**
	 * isNul, permet de savoir si la pièce ce déplace 
	 * @return boolean
	 */
	boolean isNul(){
		return moveX == 0 && moveY == 0;
	}
	
}
