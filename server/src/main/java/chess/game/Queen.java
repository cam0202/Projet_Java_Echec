package chess.game;
import chess.player.*;
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
	Queen(Player p, int c){
		super("Queen", p, c);
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
