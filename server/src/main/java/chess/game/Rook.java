package chess.game;
import chess.player.*;
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
	Rook(Player p,int c){
		super("Rook", p, c);
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
