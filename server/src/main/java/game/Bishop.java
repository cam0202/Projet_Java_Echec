package game;

public class Bishop extends Chessman{
	
	/**
	 * Constructeur
	 * @param chessman
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
