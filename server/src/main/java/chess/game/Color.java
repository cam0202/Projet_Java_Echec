package chess.game;
/**
 * Color Class
 * sert à l'utilisateur à faire un choix de couleur
 */
public class Color {
	
	/**
	 * String qui permettra de savoir la couleur
	 */
	private String color;
	
	/**
	 *  Constructor
	 */
	Color(){
		color = null;
	}
	
	/**
	 * green, choix de la couleur verte
	 */
	void Green() { 
		this.color = "green";
	}
	
	/**
	 * blue, choix de la couleur bleu
	 */
	void Blue() { 
		this.color = "blue";
	}
	
	/**
	 * red, choix de la couleur rouge
	 */
	void Red() { 
		this.color = "red";
	}
	
	/**
	 * black, choix de la couleur noire
	 */
	void Black() { 
		this.color = "black";
	}
	
	/**
	 * white, choix de la couleur blanc
	 */
	void White() { 
		this.color = "white";
	}
	
	/**
	 * yellow, choix de la couleur jaune
	 */
	void Yellow() {
		this.color = "yellow";
	}
	
	/**
	 * orange, choix de la couleur orange
	 */
	void Orange() {
		this.color = "orange";
	}
	
	/**
	 * violet, choix de la couleur violet
	 */
	void Violet() {
		this.color = "violet";
	}
	
	/**
	 * getColor, récupère la couleur
	 * @return color 
	 */
	String getColor() {
		return this.color;
	}
}
