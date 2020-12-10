package game;

public class Color {
	
	/**
	 * Color sert à l'utilisateur à faire un choix de couleur.
	 * Cette classe permet de faire la diffèrence entre les utilisateur dans une partie
	 */
	
	private String color;
	
	/**
	 *  Constructeur initialise color à nul
	 */
	Color(){
		color = null;
	}
	
	/**
	 * Fonction permettant de choisir sa couleur 
	 */
	void Green() { 
		this.color = "green";
	}
	
	void Blue() { 
		this.color = "blue";
	}
	
	void Red() { 
		this.color = "red";
	}
	
	void Black() { 
		this.color = "black";
	}
	
	void White() { 
		this.color = "white";
	}
	
	void Yellow() {
		this.color = "yellow";
	}
	
	void Orange() {
		this.color = "orange";
	}
	
	void Violet() {
		this.color = "violet";
	}
	
	/**
	 * Fonction de récupération
	 * @return color 
	 */
	String getColor() {
		return this.color;
	}
}
