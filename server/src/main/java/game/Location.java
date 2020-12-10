package game;

public class Location {
	private int r;
	private int c;
	
	/**
	 * Constructeur
	 * Position de la pièce
	 */
	Location(int row, int column){
		this.r = row;
		this.c = column;
	}
	
	/**
	 * Fonction de récupération des positions 
	 */
	int getRow() {
		return this.r;
	}
	
	int getColumn() {
		return this.c;
	}
	
	/**
	 * Fonctions modification de la position 
	 */
	void setRow(int newRow) {
		this.r = newRow;
	}
	
	void setColumn(int newColumn) {
		this.c = newColumn;
	}
	
}
