package game;

public class Rook extends Chessman{
	
	
	/**
	 * Constructeur
	 * @param chessman
	 */
	Rook(Color c){
		super("Rook", c);
	}

	@Override
	public boolean isOk(Move move) {
		// déplacement vertival
		return move.getLocationX() * move.getLocationY() == 0 && !move.isNul();
	}



}
