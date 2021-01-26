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
	public Color(){
		color = null;
	}
	
	/**
	 * green, choix de la couleur verte
	 */
	public void Green() { 
		this.color = "green";
	}
	
	/**
	 * blue, choix de la couleur bleu
	 */
	public void Blue() { 
		this.color = "blue";
	}
	
	/**
	 * red, choix de la couleur rouge
	 */
	public void Red() { 
		this.color = "red";
	}
	
	/**
	 * black, choix de la couleur noire
	 */
	public void Black() { 
		this.color = "black";
	}
	
	/**
	 * white, choix de la couleur blanc
	 */
	public void White() { 
		this.color = "white";
	}
	
	/**
	 * yellow, choix de la couleur jaune
	 */
	public void Yellow() {
		this.color = "yellow";
	}
	
	/**
	 * orange, choix de la couleur orange
	 */
	public void Orange() {
		this.color = "orange";
	}
	
	/**
	 * violet, choix de la couleur violet
	 */
	public void Violet() {
		this.color = "violet";
	}
	
	/**
	 * getColor, récupère la couleur
	 * @return color 
	 */
	public String getColor() {
		return this.color;
	}
}
