package game;

public class Square {
	/**
	 * Piece contenu par la case
	 */
	private Chessman cm;
	
	/**
	 * Constructeurs
	 */
	public Square()
	{
		this.cm = null;
	}
	
	public Square(Chessman chessman)
	{
		this.setChessman(chessman);
	}
	
	
	public Chessman getChessman () {
		return this.cm;
	}
	
	/**
	 * Methode met un objet Piece sur la case
	 * @param Piece � placer
	 */
	public void setChessman(Chessman chessman) {
		this.cm = chessman;
	}
	
	/**
	 * Methode estOccupe servant a savoir si la case est occupe ou non.
	 */
	public boolean isTaken()
	{
		return (this.cm != null);	
	}
	
	public boolean isTaken(String couleur)
	{
		if (this.cm == null)
			return false;
		else
			return (this.cm.getColor().equals(couleur));
	}
}
