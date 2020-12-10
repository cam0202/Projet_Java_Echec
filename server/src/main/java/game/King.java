package game;

public class King extends Chessman{

	
	/**
	 * Constructeur
	 * @param chessman
	 */
	King(Color c){
		super("King", c);
	}

	@Override
	public boolean isOk(Move move) {
		// le roi ne peux se déplacer que 1 case 
		return Math.abs(move.getLocationX()) * Math.abs(move.getLocationY()) <= 1  
				&& Math.abs(move.getLocationX()) - Math.abs(move.getLocationY()) <= 1
				&& Math.abs(move.getLocationX()) - Math.abs(move.getLocationY()) >= -1
				&& !move.isNul();
	}

}
