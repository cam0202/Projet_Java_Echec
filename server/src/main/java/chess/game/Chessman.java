package chess.game;
import chess.player.*;
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
	 * player
	 */
	private Player player;
	/**
	 * num joueur
	 */
	private int c;
	
	/**
	 * Chessman Constructor
	 * Constructeur de la pièce
	 * @param name
	 * @param color
	 */
	Chessman(String name, Player p,int i){
		this.n = name;
		this.player = p;
		this.c = i;
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
	
	Player getPlayer() {
		return this.player;
	}
	
	int getNumber() {
		return this.c;
	}
	
	/**
	 * isOk, abstract méthode permettant d'implementer un movement selon le type de la pièce.
	 * @param move
	 * @return
	 */
	public abstract boolean isOk(Move move);


}
