package game;

public abstract class Chessman {
	
	private String n;
	private String c; 
	/**
	 * Constructeur de la pièce
	 * @param name
	 * @param color
	 */
	Chessman(String name, Color color){
		this.n = name;
		this.c = color.getColor();
	}
	
	/**
	 * Fonction évolution pièce
	 */
	void setName(String newName) {
		this.n = newName;
	}
	
	/**
	 * Fonctions recupération infos
	 */
	String getName() {
		return this.n;
	}
	
	String getColor() {
		return this.c;
	}
	
	/**
	 * Méthode permettant d'implementer un movement selon le type de la pièce.
	 * @param move
	 * @return
	 */
	public abstract boolean isOk(Move move);


}
