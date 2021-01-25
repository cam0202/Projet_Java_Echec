package chess.game;
/**
 * 
 * Queen Class 
 * Reine
 *
 */
public class Queen extends Chessman{
	
	/**
	 * Constructor
	 * @param color
	 */
	Queen(Color c){
		super("Queen",c);
	}


	/**
	 * isOk, vérification du déplacement pour une reine
	 * @param Move
	 */
	public boolean isOk(Move move) {
		// déplacement en diagonale ou rectiligen 
		return (Math.abs(move.getLocationX()) - Math.abs(move.getLocationY()) == 0 
				|| move.getLocationX() * move.getLocationY() == 0) && !move.isNul();
	}
}
