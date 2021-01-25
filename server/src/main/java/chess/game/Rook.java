package chess.game;
/**
 * 
 * Rook Class
 * Represente le chessman tours
 *
 */
public class Rook extends Chessman{
	
	
	/**
	 * Constructeur
	 * @param Color
	 */
	Rook(Color c){
		super("Rook", c);
	}

	/**
	 * isOk, vérification du déplacement pour une tour
	 * @param Move
	 */
	public boolean isOk(Move move) {
		// La tour ne peut que ce déplacer de façon verticale 
		return move.getLocationX() * move.getLocationY() == 0 && !move.isNul();
	}



}
