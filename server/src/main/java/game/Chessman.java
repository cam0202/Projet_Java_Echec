package game;

public class Chessman {
	
	private String n;
	private String c; 
	/**
	 * Constructeur de la pièce
	 * @param name
	 * @param color
	 */
	Chessman(String name, Color color){
		this.c = n;
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
	String name() {
		return this.name();
	}
	
	String color() {
		return this.c;
	}


}
