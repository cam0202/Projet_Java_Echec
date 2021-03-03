package chess.game;

import chess.game.chessman.Attack;
import chess.game.chessman.Bishop;
import chess.game.chessman.Chessman;
import chess.game.chessman.King;
import chess.game.chessman.Knight;
import chess.game.chessman.Pawn;
import chess.game.chessman.Queen;
import chess.game.chessman.Rook;
import chess.player.Player;

/**
 * Class Board permet de réprésenter le jeux.
 *
 */
public class Board {
	
	// Joueurs
	private final Player white;
	private final Player black;
	
	// Tour
	private Player whoIsNext;
	private Chessman whoIsNextAttack;
	
	// Attaque
	private Chessman c1attack;
    private Chessman c2attack;
    private Location[] locationFigth;
    
    // Score 
    private int[] score;
    private int[] value;
    
    // Jeu
    private Square[][] board;
    
    /**
     * Constructeur 
     * @param white
     * @param black
     */
    public Board(final Player white, Player black) {
    	// init joueur 
        this.white = white;
        this.black = black;
        
        // init tableau de score
        this.score = new int[2];
        
        // init joueur suivant dep
        this.whoIsNext = black;
        
        // init attaque 
        this.whoIsNextAttack = null;
        this.locationFigth = new Location[2];
        
        // init des 64 case de l'échequier
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
    
    /**
     * get fonctions 
     */
    
    public Player getWhite() {
        return this.white;
    }
    
    
    public Player getBlack() {
        return this.black;
    }
    
    public Square[][] getBoard(){
    	return this.board;
    }
    
    public Chessman getC1attack() {
    	return this.c1attack;
    }
    
    public Chessman getC2attack() {
    	return this.c2attack;
    }
    
    public int[] getScore() {
    	return this.score;
    }
    
    /**
     * fonction intermédiaire de jeu
     */
    
    /**
     * move, movement sur le plateau 
     * @param chessman
     * @param move
     * @return String 
     * @throws BoardException
     */
    public String move(Chessman chessman, Move move) throws BoardException {
    	
    	// changement de joueur
    	this.whoIsNext = this.whoIsNext.equals(this.white) ? this.black : this.white;
    	
        //vérification du déplacement 
        Chessman inTheWay = this.inTheWay(chessman, move);
        if (inTheWay != null) {
        	
        	this.c1attack = inTheWay;
        	this.c2attack = chessman;
        	this.whoIsNextAttack = inTheWay;
        	this.locationFigth[0] = move.getEnd();
        	this.locationFigth[1] = move.getStart();
        	
            this.value = this.possiblePoint();
            return " Go to fight. " + this.possibleAttack(chessman) + " " + this.possibleAttack(inTheWay);
            
        }
        
        // déplacement  
        this.board[move.getEnd().getRow()][move.getEnd().getCol()].setChessman(chessman);
        this.board[move.getStart().getRow()][move.getStart().getCol()].clear();
        

        return chessman.getPlayer().getName() + " moved " + chessman.getName() + " to " + move.getEnd().toString();
    }
    
    /**
     * inTheWay, permet de savoir si suite à un déplacement un pion est présent sur le movemment ou à l'arrivé
     * @param of
     * @param move
     * @return Chessman
     */
    public Chessman inTheWay(Chessman of, Move move) {
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
    
    /**
     * fight, combat dans le jeu
     * @param name
     * @return String 
     * @throws BoardException
     */
    private String fight(Player player, String name) throws BoardException {
    	Chessman c = this.whoIsNextAttack.equals(this.c1attack) ? this.c2attack : this.c1attack; 
    	// cas du mauvais chessman en attaque
        if(this.whoIsNextAttack.getPlayer().equals(player)){
            throw new BoardException("it's not your turn");
        }
 
        
        // attaque
        Attack attack = this.chooseAttacks(c, name);
        if(this.whoIsNextAttack.equals(this.c1attack)) {
        	attack.setValue(this.c1attack);
        	this.c2attack.setLive(attack.getValue());
        }else {
        	attack.setValue(this.c2attack);
        	this.c1attack.setLive(attack.getValue());
        }
        this.whoIsNextAttack = this.whoIsNextAttack.equals(this.c1attack) ? this.c2attack : this.c1attack;
        return c.getPlayer().getName() + " impact " + this.whoIsNextAttack.getName() + " with " + attack.getName() + " to "  + attack.getValue();

    }
    
    /**
     * fonction intermédiaire pour l'attaque 
     */
    
    /**
     * chooseAttack, retourne l'attaque choisi
     * @param c
     * @param name
     * @return Attack
     * @throws BoardException
     */
    Attack chooseAttacks(Chessman c, String name) throws BoardException {
        for(int i = 0 ; i < c.getAttacks().length ; i++){
            if(c.getAttacks()[i].getName().toLowerCase().equals(name.toLowerCase())) return c.getAttacks()[i];
        }
        throw new BoardException("Attack does exist !");
    }
    
    /**
     * possibleAttack, donne les attaques d'un chessman
     * @param c
     * @return String 
     */
    public String possibleAttack(Chessman c) {
    	return c.getName() + "can attack with " + c.getAttacks()[0].getName() 
    			+", " + c.getAttacks()[1].getName()
    			+", " + c.getAttacks()[2].getName()+".";
    }
    
    /**
     * possiblePoint, donne le tableau des point possible à gagner
     * @return
     */
    public int[] possiblePoint() {
    	int[] value = {this.c1attack.getLive(), this.c2attack.getLive()};
    	return value;
    }
    
    
    /**
     * jeu
     */
    
    /**
     * play, fonction permettant de faire un mouvement ou un combat 
     * @param player
     * @param move
     * @param nameAttack
     * @return String 
     * @throws BoardException
     */
    public String play(Player player, Move move, String nameAttack) throws BoardException{
    	
    	if(this.score[0] == 890 ) {
    		return this.white.getName() + " have win the game !";
    	}
    	if(this.score[1] == 890 ) {
    		return this.black.getName() + " have win the game !";
    	}
    	
        
    	if(nameAttack == null && !move.isNull()) {
    		// error
    		if(this.whoIsNextAttack != null) {
    	    	// chessman sans vie
    	        if (this.c1attack.getLive() <= 0 || this.c2attack.getLive() <= 0){
    	        	// fin combat 
    	        	this.whoIsNextAttack = null;
    	        	// vide les case
    	        	this.board[this.locationFigth[0].getRow()][this.locationFigth[0].getCol()].clear();
    	        	this.board[this.locationFigth[1].getRow()][this.locationFigth[1].getCol()].clear();
    	        	//on met le bon chessman dans la case
    	            if(this.c2attack.getLive() <= 0) {
    	            	this.score[0] += this.value[1];
    	            	this.board[this.locationFigth[0].getRow()][this.locationFigth[0].getCol()].setChessman(this.c1attack);
    	            } else {
    	            	this.score[1] += this.value[0];
    	            	this.board[this.locationFigth[0].getRow()][this.locationFigth[0].getCol()].setChessman(this.c2attack);
    	            }
    	        } else {
    	        	throw new BoardException("Sorry you can't move, have fight in the game.");
    	        }
        		
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
    		
    		return this.move(chessman, move);
    	} else if ((nameAttack != null) && move == null) {
    		// error
    		if(this.c1attack.getLive() <= 0 || this.c2attack.getLive() <= 0) {
        		throw new BoardException("Not have a fight !");
        	}
    		return this.fight(player, nameAttack);
    	} else {
    		return "not execute move or attack !";
    	}
    }
}

