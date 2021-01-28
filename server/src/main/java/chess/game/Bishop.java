package chess.game;
import chess.player.*;
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
	Bishop(Player p,int c){
		super("Bishop",p, c);
	}

	@Override
	public boolean isOk(Move move) {
		// déplacement en diagonale 
		return Math.abs(move.getLocationX()) - Math.abs(move.getLocationY()) == 0 && !move.isNul();

	}


}
