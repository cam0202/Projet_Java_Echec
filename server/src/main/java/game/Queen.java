package game;

public class Queen extends Chessman{
	
	/**
	 * Constructeur
	 * @param chessman
	 */
	Queen(Color c){
		super("Queen",c);
	}


	@Override
	public boolean isOk(Move move) {
		// déplacement en diagonale ou rectiligen 
		return (Math.abs(move.getLocationX()) - Math.abs(move.getLocationY()) == 0 
				|| move.getLocationX() * move.getLocationY() == 0) && !move.isNul();
	}
}
