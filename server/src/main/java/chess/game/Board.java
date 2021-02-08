package chess.game;

import chess.game.chessman.*;
import chess.player.Player;

public class Board {

    private final Player white;
    private final Player black;
    private int[] score;
    private int[] value;

    private Player whoIsNext;

    private Square[][] board;
    private Fight fightPlayer;

    public Board(final Player white, Player black) {
        this.white = white;
        this.black = black;
        this.score = new int[2];

        this.whoIsNext = black;

        this.board = new Square[8][8];

        for (int letter = 0; letter < this.board.length; letter++) {
            for (int number = 0; number < this.board.length; number++) {
                this.board[letter][number] = new Square(null);
            }
        }

        // Init Whites
        (this.board[0][0]).setChessman(new Rook(this.white));
        (this.board[1][0]).setChessman(new Knight(this.white));
        (this.board[2][0]).setChessman(new Bishop(this.white));
        (this.board[3][0]).setChessman(new Queen(this.white));
        (this.board[4][0]).setChessman(new King(this.white));
        (this.board[5][0]).setChessman(new Bishop(this.white));
        (this.board[6][0]).setChessman(new Knight(this.white));
        (this.board[7][0]).setChessman(new Rook(this.white));

        for (int letter = 0; letter < this.board.length; letter++) {
            (this.board[letter][1]).setChessman(new Pawn(this.white, true));
        }

        // Init Blacks
        (this.board[0][7]).setChessman(new Rook(this.black));
        (this.board[1][7]).setChessman(new Knight(this.black));
        (this.board[2][7]).setChessman(new Bishop(this.black));
        (this.board[3][7]).setChessman(new Queen(this.black));
        (this.board[4][7]).setChessman(new King(this.black));
        (this.board[5][7]).setChessman(new Bishop(this.black));
        (this.board[6][7]).setChessman(new Knight(this.black));
        (this.board[7][7]).setChessman(new Rook(this.black));

        for (int letter = 0; letter < this.board.length; letter++) {
            (this.board[letter][6]).setChessman(new Pawn(this.black, false));
        }
    }

    public Player getWhite() {
        return this.white;
    }

    public Player getBlack() {
        return this.black;
    }

    public String move(Player player, Move move) throws BoardException {
        
    	if(this.fightPlayer != null) {
    		throw new BoardException("Sorry you can't move, have fight in the game.");
    	}
    	
    	if (!this.whoIsNext.equals(player)) {
            throw new BoardException("it's not your turn");
        }

        if (move.isNull()) {
            throw new BoardException("this move is null");
        }

        Chessman chessman = this.board[move.getStart().getRow()][move.getStart().getCol()].getChessman();

        if (chessman == null) {
            throw new BoardException("there is no chessman on " + move.getStart());
        }

        if (!player.equals(chessman.getPlayer())) {
            throw new BoardException(chessman.getName() + " at " + move.getStart() + " does not belong to you");
        }

        if (!chessman.canMove(move)) {
            throw new BoardException(chessman.getName() + " cannot move to " + move.getEnd());
        }

        Chessman inTheWay = this.inTheWay(chessman, move);
        if (inTheWay != null) {
        	if(inTheWay.getPlayer().equals(this.white)) {
        		this.fightPlayer = new Fight(inTheWay, chessman, inTheWay);
        	}else {
        		 this.fightPlayer = new Fight(chessman, inTheWay, chessman);
        	}
            this.value = this.fightPlayer.possiblePoint();
            return " Go to fight. " + this.fightPlayer.possibleAttack(chessman) + " " + this.fightPlayer.possibleAttack(inTheWay);
        }

        this.board[move.getEnd().getRow()][move.getEnd().getCol()].setChessman(chessman);
        this.board[move.getStart().getRow()][move.getStart().getCol()].clear();
        this.whoIsNext = this.whoIsNext.equals(this.white) ? this.black : this.white;

        return chessman.getPlayer().getName() + " moved " + chessman.getName() + " to " + move.getEnd().toString();
    }

    private Chessman inTheWay(Chessman of, Move move) {
        if (!(of instanceof Knight || of instanceof King)) {
            int row = move.getDirectionRow() != 0 ? move.getDirectionRow() / Math.abs(move.getDirectionRow()) : 0;
            int col = move.getDirectionCol() != 0 ? move.getDirectionCol() / Math.abs(move.getDirectionCol()) : 0;

            for (int ctrRow = move.getStart().getRow() + row,
                    ctrCol = move.getStart().getCol() + col; ctrRow != move.getEnd().getRow()
                            | ctrCol != move.getEnd().getCol(); ctrRow += row, ctrCol += col) {
                if (this.board[ctrRow][ctrCol].isTaken())
                    return this.board[ctrRow][ctrCol].getChessman();
            }
        }

        return this.board[move.getEnd().getRow()][move.getEnd().getCol()].getChessman();
    }
    
    
    public String fight(Chessman of, String name) throws BoardException {
    	if(this.fightPlayer == null) {
    		throw new BoardException("Not have a fight !");
    	}
    	String result = this.fight(of, name);
    	if(!result.contains("impact")) {
    		if(result.equals(this.white.getName())) {
    			this.score[0] += this.value[0];
    			this.fightPlayer = null;
    			return this.white.getName() + " win " + this.score[0] + " points and "+ this.black.getName() +" loose.";
    		} else {
    			this.score[1] += this.value[1];
    			this.fightPlayer = null;
    			return this.black.getName() + " win " + this.score[1] + " points and "+ this.white.getName() +" loose.";
    		}
    	}
    	return result;
    }

}
