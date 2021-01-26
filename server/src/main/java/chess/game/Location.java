package chess.game;
/**
 * 
 * Location Class 
 * sert à representer une coordonnee sur l'echiquier
 *
 */
public class Location {
	/**
	 * int en y
	 */
	private int r;
	/**
	 * int en x
	 */
	private int c;
	
	/**
	 * Location Constructor
	 * @param column
	 * @param row
	 */
	Location(int column, int row){
		this.r = row;
		this.c = column;
	}
	/**
	 * getRow, position en y
	 * @return int y
	 */
	int getRow() {
		return this.r;
	}
	/**
	 * getColumn position en x
	 * @return int x
	 */
	int getColumn() {
		return this.c;
	}
	
	/**
	 * setRow, change y
	 * @param newRow
	 */
	void setRow(int newRow) {
		this.r = newRow;
	}
	/**
	 * setColumn, change x
	 * @param newColumn
	 */
	void setColumn(int newColumn) {
		this.c = newColumn;
	}
	
	
}
