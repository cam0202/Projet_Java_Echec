package chess.game;
/**
 * 
 * Bishop Class
 * Fou
 *
 */
public class Bishop extends Chessman{
	
	/**
	 * Constructor
	 * @param Color
	 */
	Bishop(Color c){
		super("Bishop", c);
	}

	@Override
	public boolean isOk(Move move) {
		// déplacement en diagonale 
		return Math.abs(move.getLocationX()) - Math.abs(move.getLocationY()) == 0 && !move.isNul();

	}


}
