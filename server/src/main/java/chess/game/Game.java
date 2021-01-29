package chess.game;

import chess.player.*;

/**
 * Class Game Elle permet de réprésenter le jeux.
 *
 */
public class Game {

	/**
	 * Tableau d'objet Square, permet de réprésenter les case d'un échiquier.
	 */
	private Square[][] chess;
	/**
	 * Tableau de couleur, permet de réprésenter la couleur des joueur.
	 */
	private Player[] players;

	/**
	 * Game Constructor Crée un tableau de 8x8 case et crée un tableau de 2 couleurs
	 * 
	 * @param colorP1, couleur du joueur 1
	 * @param colorP2, couleur du joueur 2
	 */
	public Game(Player p1, Player p2) {
		// init des 64 case de l'échequier
		this.chess = new Square[8][8];
		// création des case de l'échequier
		for (int row = 0; row <= 7; row++) {
			for (int column = 0; column <= 7; column++) {
				this.chess[column][row] = new Square();
			}
		}

		// init players
		this.players = new Player[2];
		this.players[0] = p1;
		this.players[1] = p2;
		
		// init pieces
		int row = 7;

		(this.chess[0][row]).setChessman(new Rook(this.players[1], 1));
		(this.chess[1][row]).setChessman(new Knight(this.players[1], 1));
		(this.chess[2][row]).setChessman(new Bishop(this.players[1], 1));
		(this.chess[3][row]).setChessman(new Queen(this.players[1], 1));
		(this.chess[4][row]).setChessman(new King(this.players[1], 1));
		(this.chess[5][row]).setChessman(new Bishop(this.players[1], 1));
		(this.chess[6][row]).setChessman(new Knight(this.players[1], 1));
		(this.chess[7][row]).setChessman(new Rook(this.players[1], 1));

		for (int column = 0; column <= 7; column++) {
			(chess[column][row - 1]).setChessman(new Pawn(this.players[1], 1));
		}

		// payer 2
		row = 0;
		(this.chess[0][row]).setChessman(new Rook(this.players[0], 0));
		(this.chess[1][row]).setChessman(new Knight(this.players[0], 0));
		(this.chess[2][row]).setChessman(new Bishop(this.players[0], 0));
		(this.chess[3][row]).setChessman(new Queen(this.players[0], 0));
		(this.chess[4][row]).setChessman(new King(this.players[0], 0));
		(this.chess[5][row]).setChessman(new Bishop(this.players[0], 0));
		(this.chess[6][row]).setChessman(new Knight(this.players[0], 0));
		(this.chess[7][row]).setChessman(new Rook(this.players[0], 0));

		for (int column = 0; column <= 7; column++) {
			(this.chess[column][row + 1]).setChessman(new Pawn(this.players[0], 0));
		}
		
	}

	Square getSquare(int column, int row) {
		return this.chess[column][row];
	}
	
	/**
	 * clenSquare, vide la case chosi
	 * @param column, x
	 * @param row, y
	 */
	void cleanSquare(int column, int row) {
		this.chess[column][row].setChessman(null);
	}
	
	/**
	 * setSquare, MAJ la case chosi
	 * @param column, x
	 * @param row, y
	 */
	void setSquare(int column, int row, Chessman c) {
		this.chess[row][column].setChessman(c);
	}
	
	/**
	 * moveOk, permet de vérifier si un pion peu bouger
	 * @param move
	 * @return true, false
	 */
	boolean moveOk(Move move) {
		// récupération de la pièce qu'on veut bouger
		Chessman startChessman = this.getSquare(move.getStart().getColumn(), move.getStart().getRow()).getChessman();
		// première condition la case doit être libre et que le movement ne soit pas
		// null
		if (this.getSquare(move.getEnd().getColumn(), move.getEnd().getRow()).isTaken() == false && move.isNul() == false) {

			// on commence le traitement
			if (!(startChessman instanceof Knight)) {

				if (!(startChessman instanceof Pawn)) {

					// vérification que le déplacement est supérieur à un
					if (!(Math.abs(move.getLocationX()) - Math.abs(move.getLocationY()) <= 1
							&& Math.abs(move.getLocationX()) + Math.abs(move.getLocationY()) <= 1)) {

						// jumpX et jumpY repésente le saut
						int jumpX = move.getLocationX() == 0 ? 0
								: (int) (move.getLocationX() / Math.abs(move.getLocationX()));
						int jumpY = move.getLocationY() == 0 ? 0
								: (int) (move.getLocationY() / Math.abs(move.getLocationY()));

						// vérification qu'il n'est pas de pion dans le déplacement prévu
						for (int ctrX = (int) move.getStart().getColumn() + jumpX,
								ctrY = (int) move.getStart().getRow() + jumpY; ctrX != (int) move.getEnd().getColumn()
										| ctrY != (int) move.getEnd().getRow(); ctrX += jumpX, ctrY += jumpY) {
							if (this.chess[ctrX][ctrY].isTaken()) {
								return false;
							}
						}
						return true;
					} else {
						return true;
					}

				} else {
					// cas pion
					return !this.getSquare(move.getEnd().getRow(), move.getEnd().getColumn()).isTaken();
				}

			} else {
				// cas cavalier il peut sauter au dessus des autre
				return true;
			}
		}
		// contient une pièce donc faux
		return false;
	}

	/**
	 * Bouge le pion
	 * 
	 * @param move
	 */
	public String move(Player p, Move move) throws RuntimeException {
		Chessman c = this.getSquare(move.getStart().getColumn(), move.getStart().getRow()).getChessman();
		
		if (!this.moveOk(move)) {
			throw new RuntimeException("Move not ok !");
		}
		
		if (c == null) {
			throw new RuntimeException("Not have chessman in this square !");
		}

		if (!p.getName().equals(c.getPlayer().getName())) {
			throw new RuntimeException("The pawn isn't yours !");
		}

		if (!c.isOk(move)) {
			throw new RuntimeException("Move not allowed to " + c.getName());
		}

		int x = move.getEnd().getColumn();
		int y = move.getEnd().getRow();
		this.setSquare(x, y, c);
		this.cleanSquare(move.getStart().getColumn(), move.getStart().getRow());
		String posX = "a";
		switch ((int) move.getEnd().getColumn()) {
			case 0:
				break;
			case 1:
				posX = "b";
				break;
			case 2:
				posX = "c";
				break;
			case 3:
				posX = "d";
				break;
			case 4:
				posX = "e";
				break;
			case 5:
				posX = "f";
				break;
			case 6:
				posX = "g";
				break;
		}
		return "Player " + c.getName() + " move in (" + posX + "," + ((int) move.getEnd().getRow() + 1) + ")";

	}

	Location createLocation(char posX, int posY) {
		if (posX < 'a' || posX > 'g') {
			throw new IllegalArgumentException("invalid character " + posX);
		}

		if (posY < 1 || posY > 6) {
			throw new IllegalArgumentException("invalid integer " + posY);
		}

		int x = -1;
		switch (posX) {
			case 'a':
				x = 0;
				break;
			case 'b':
				x = 1;
				break;
			case 'c':
				x = 2;
				break;
			case 'd':
				x = 3;
				break;
			case 'e':
				x = 4;
				break;
			case 'f':
				x = 5;
				break;
			case 'g':
				x = 6;
				break;
		}
		return new Location(x, posY - 1);
	}

	public Move createMove(char xs, int ys, char xe, int ye) {
		Location start = this.createLocation(xs, ys);
		Location end = this.createLocation(xe, ye);
		return new Move(start, end);
	}

}
