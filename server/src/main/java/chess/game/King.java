package chess.game;
import chess.player.*;
/**
 * 
 * King Class 
 * Roi
 * 
 */
public class King extends Chessman{

	
	/**
	 * Constructor
	 * @param Color
	 */
	King(Player p,int c){
		super("King", p, c);
	}

	/**
	 * isOk, vérification du déplacement pour un roi
	 * @param Move
	 */
	public boolean isOk(Move move) {
		// le roi ne peux se déplacer que d'un case 
		return Math.abs(move.getLocationX()) * Math.abs(move.getLocationY()) <= 1  
				&& Math.abs(move.getLocationX()) - Math.abs(move.getLocationY()) <= 1
				&& Math.abs(move.getLocationX()) - Math.abs(move.getLocationY()) >= -1
				&& !move.isNul();
	}

}
