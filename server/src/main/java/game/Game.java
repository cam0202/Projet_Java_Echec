package game;

public class Game {
	
	// Tableau des cases
	private Square[][] chess; 
	private Color[] colorPlayers;	
	/**
	 * Constructeur 
	 */
	Game(Color colorP1, Color colorP2){
		// init des 64 case de l'échequier
		this.chess = new Square[8][8] ;
		// création des case de l'échequier 
		for(int row = 0; row <= 7; row++){
			for(int column = 0; column <= 7 ; column++) {
				this.chess[column][row] = new Square();
			}
		}
		// init couleur
		this.colorPlayers = new Color[2] ;
		this.colorPlayers[0] = colorP1 ;
		this.colorPlayers[1] = colorP2 ;
	}
	
	/**
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	Square getSquare(int row, int column) {
		return this.chess[row][column];
	}
	
	void cleanSquare(int row, int column) {
		this.chess[row][column].setChessman(null);
	}
	
	void setSquare(int row, int column, Chessman c) {
		this.chess[row][column].setChessman(c);
	}
	
	/**
	 * Placement de toutes les pièces sur l'échéquier
	 */
	void startGame(){
		// player 1
		int row = 7;
		
		(this.chess[0][row]).setChessman(new Rook(this.colorPlayers[1])); 
		(this.chess[1][row]).setChessman(new Knight(this.colorPlayers[1])); 
		(this.chess[2][row]).setChessman(new Bishop(this.colorPlayers[1])); 
		(this.chess[3][row]).setChessman(new Queen(this.colorPlayers[1])); 
		(this.chess[4][row]).setChessman(new King(this.colorPlayers[1])); 
		(this.chess[5][row]).setChessman(new Bishop(this.colorPlayers[1])); 
		(this.chess[6][row]).setChessman(new Knight(this.colorPlayers[1])); 
		(this.chess[7][row]).setChessman(new Rook(this.colorPlayers[1])); 
			
		for (int column = 0; column <= 7; column++) {
			(chess[column][row-1]).setChessman(new Pawn(this.colorPlayers[1])); 
		}

		// payer 2
		row = 0;
		(this.chess[0][row]).setChessman(new Rook(this.colorPlayers[0])); 
		(this.chess[1][row]).setChessman(new Knight(this.colorPlayers[0])); 
		(this.chess[2][row]).setChessman(new Bishop(this.colorPlayers[0])); 
		(this.chess[3][row]).setChessman(new Queen(this.colorPlayers[0])); 
		(this.chess[4][row]).setChessman(new King(this.colorPlayers[0])); 
		(this.chess[5][row]).setChessman(new Bishop(this.colorPlayers[0])); 
		(this.chess[6][row]).setChessman(new Knight(this.colorPlayers[0])); 
		(this.chess[7][row]).setChessman(new Rook(this.colorPlayers[0])); 
			
		for (int column = 0; column <= 7; column++) {
			(this.chess[column][row+1]).setChessman(new Pawn(this.colorPlayers[0])); 
		}
		
	}
	
	/**
	 * vérification d'un déplacement 
	 * @param move
	 * @return
	 */
	boolean moveOk(Move move) {
		// récupération de la pièce qu'on veut bouger 
		Chessman startChessman = this.chess[move.getStart().getColumn()][move.getStart().getRow()].getChessman();
		String chessmanColor = startChessman.getColor();
		// première condition la case doit être libre et que le movement ne soit pas null
		if(this.getSquare(move.getEnd().getRow(),move.getEnd().getColumn()).isTaken() == false && move.isNul() == false) {
			// on commence le traitement 
			if(!(startChessman.getName().equals("Knight"))) {
				
				if(!(startChessman.getName().equals("Pawn"))) {
				
					int jumpX = move.getLocationX()== 0 ? 0 : (int)(move.getEnd().getColumn() - move.getStart().getRow())/Math.abs((int)(move.getEnd().getColumn() - move.getStart().getRow()));
			
					int jumpY = move.getLocationY() == 0 ? 0 : (int)(move.getEnd().getRow() - move.getStart().getRow())/Math.abs((int)(move.getEnd().getRow() - move.getStart().getRow()));
					// A part un cavalier aucune pièce ne peut en sauter une autre
					System.out.println(jumpX + " " + jumpY);
					
					for (int ctrX = move.getStart().getColumn() + jumpX; ctrX != move.getEnd().getColumn() ;ctrX += jumpX) {
						for(int ctrY = (int)move.getStart().getColumn()+ jumpY; ctrY != move.getEnd().getRow() ;ctrY += jumpY) {
							if (this.chess[ctrX][ctrY].isTaken()){
								return false;
							}
						}
					}
					return true;
				} else {
					return !this.getSquare(move.getEnd().getRow(),move.getEnd().getColumn()).isTaken();
				}
				
			} else {
				// cas cavalier il peut sauter au dessus des autre
				return true;
			}
		}	
		// contient une pièce donc faux
		return false;
	}
	
	
	void move(Move move) {
		if(this.moveOk(move)) {
			Chessman c = this.chess[move.getStart().getColumn()][move.getStart().getRow()].getChessman();
			if(c.isOk(move)) {
				int x = move.getEnd().getColumn();
				int y = move.getEnd().getRow();
				this.setSquare(x, y, c);
				this.cleanSquare(move.getStart().getColumn(),move.getStart().getRow());
			}
		}
		
	}
	

}
