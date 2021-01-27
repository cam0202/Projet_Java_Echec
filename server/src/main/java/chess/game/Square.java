package chess.game;
import chess.player.*;
/**
 * 
 * Square Class
 * Sert à représenter une case du jeu
 */
public class Square {
	/**
	 * Chessman, pièce pouvant être sur une case
	 */
	private Chessman cm;
	
	/**
	 * Square Constructor default
	 */
	public Square()
	{
		this.cm = null;
	}
	
	/**
	 * Square constructor
	 * @param chessman, pièce pouvant être sur la case
	 */
	public Square(Chessman chessman)
	{
		this.setChessman(chessman);
	}
	
	/**
	 * getChessman, retourn la valeur de la pièce sur la case
	 * @return Chessman
	 */
	public Chessman getChessman () {
		return this.cm;
	}
	
	/**
	 * setChessman, sert à placer un chessman sur la case
	 * @param Chessman 
	 */
	public void setChessman(Chessman chessman) {
		this.cm = chessman;
	}
	
	/**
	 * isTaken, sert à savoir si la case est utilisé
	 * @return
	 */
	public boolean isTaken()
	{
		return (this.cm != null);	
	}
	
	/**
	 * isTaken, sert à savoir si la case est utilisé par une couleur particulière
	 * @param couleur
	 * @return
	 */
	public boolean isTaken(int player)
	{
		if (this.cm == null)
			return false;
		else
			return (this.cm.getNumber() == player);
	}
}
