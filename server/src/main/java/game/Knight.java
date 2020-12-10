package game;

public class Knight extends Chessman{
	
	/**
	 * Constructeur
	 * @param chessman
	 */
	Knight(Color c){
		super("Knight", c);
	}
	
	@Override
	public boolean isOk(Move move) {
	// movement en L 
	return (Math.abs(move.getLocationX()) / move.getLocationY()) == 2   
			|| (Math.abs(move.getLocationX() / move.getLocationY()) == .5);
	}

}
