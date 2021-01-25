package chess.game;
/**
 * 
 * Knight Class
 * Cavalier
 *
 */
public class Knight extends Chessman{
	
	/**
	 * Constructor
	 * @param Color
	 */
	Knight(Color c){
		super("Knight", c);
	}
	
	/**
	 * isOk, vérification du déplacement pour un cavalier
	 * @param Move
	 */
	public boolean isOk(Move move) {
	// movement en L 
	return (Math.abs(move.getLocationX() / move.getLocationY())) == 2 |
			(Math.abs(move.getLocationX() / move.getLocationY())) == .5;
	}

}
