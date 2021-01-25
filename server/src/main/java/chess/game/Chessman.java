package chess.game;
/**
 * Chessman Abstract Class
 * Point de départ de toute pièce du jeu
 */
public abstract class Chessman {
	/**
	 * nom de la pièce
	 */
	private String n;
	/**
	 * couleur de la pièce
	 */
	private String c; 
	
	/**
	 * Chessman Constructor
	 * Constructeur de la pièce
	 * @param name
	 * @param color
	 */
	Chessman(String name, Color color){
		this.n = name;
		this.c = color.getColor();
	}
	
	/**
	 * setName, met un nom à la pièce
	 */
	void setName(String newName) {
		this.n = newName;
	}
	
	/**
	 * getName, recupération du nom
	 * @return String
	 */
	String getName() {
		return this.n;
	}
	
	/**
	 * getColor, récupération de la couleur 
	 * @return String
	 */
	String getColor() {
		return this.c;
	}
	
	/**
	 * isOk, abstract méthode permettant d'implementer un movement selon le type de la pièce.
	 * @param move
	 * @return
	 */
	public abstract boolean isOk(Move move);


}
