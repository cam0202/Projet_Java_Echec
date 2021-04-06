package chess.game;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

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
    private final static Logger LOGGER = Logger.getLogger(Board.class);

    // Joueurs
    private Player white;
    private Player black;

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
     * 
     * @param white
     * @param black
     */
    public Board(final Player white, Player black) {

        // init joueur
        this.white = white;
        this.black = black;

        // init tableau de score
        this.score = new int[2];

        // variable qui sert à savoir quel joueur doit jouer
        this.whoIsNext = this.white;

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

    public Square[][] getBoard() {
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
     * 
     * @param chessman
     * @param move
     * @return String
     * @throws BoardException
     */
    public String move(Chessman chessman, Move move) throws BoardException {

        // changement de joueur
        this.whoIsNext = this.whoIsNext.equals(this.white) ? this.black : this.white;

        // vérification du déplacement
        Chessman inTheWay = this.inTheWay(chessman, move);
        if (inTheWay != null) {

            this.c1attack = chessman;
            this.c2attack = inTheWay;
            this.whoIsNextAttack = chessman;
            this.locationFigth[0] = move.getEnd();
            this.locationFigth[1] = move.getStart();

            this.value = this.possiblePoint();
            if(!chessman.getName().equals(inTheWay.getName())) {
            	return " Go to fight. ! \n" +
                		this.possibleAttack(chessman) + " have "+ chessman.getLive() + "HP and can attack with " + this.possibleAttack(chessman) + "\n"+
                		this.possibleAttack(inTheWay) + " have "+ inTheWay.getLive() + "HP and can attack with " + this.possibleAttack(inTheWay);

            } else {
            	return " Go to fight. " +
            			this.possibleAttack(chessman) + " have "+ chessman.getLive() + "HP and can attack with " + this.possibleAttack(chessman);
            }
            
        }

        // déplacement
        this.board[move.getEnd().getRow()][move.getEnd().getCol()].setChessman(chessman);
        this.board[move.getStart().getRow()][move.getStart().getCol()].clear();

        return chessman.getPlayer().getName() + " moved " + chessman.getName() + " to " + move.getEnd().toString();
    }

    /**
     * inTheWay, permet de savoir si suite à un déplacement un pion est présent sur
     * le movemment ou à l'arrivé
     * 
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
     * 
     * @param name
     * @return String
     * @throws BoardException
     */
    private String fight(Player player, String name) throws BoardException {
        Chessman c = this.whoIsNextAttack.equals(this.c1attack) ? this.c2attack : this.c1attack;
        String result;
        // attaque
        Attack attack = this.chooseAttacks(this.whoIsNextAttack, name);
       
        attack.setValue(c);
        c.setLive(attack.getValue());
        
        result = whoIsNextAttack.getName() + " have an impact to "+ attack.getValue() + ".\n"
    	+ ". ("+this.c1attack.getPlayer().getName() + " : " + this.c1attack.getLive()
    	+ " and " + this.c2attack.getPlayer().getName() + " : " + this.c2attack.getLive();
        
        if(c.getLive() <= 0) {
        	this.whoIsNextAttack = null;
        	
        	// vide les case
            this.board[this.locationFigth[0].getRow()][this.locationFigth[0].getCol()].clear();
            this.board[this.locationFigth[1].getRow()][this.locationFigth[1].getCol()].clear();
        	
            // vide les case
        	if(this.whoIsNextAttack == c1attack) {
        		this.score[0] += this.value[1];
        		this.board[this.locationFigth[0].getRow()][this.locationFigth[0].getCol()]
                        .setChessman(this.c2attack);
        	} else {
        		this.score[1] += this.value[0];
        		this.board[this.locationFigth[0].getRow()][this.locationFigth[0].getCol()]
                        .setChessman(this.c1attack);
        	}
        	
        
        	 
        } else {

            this.whoIsNextAttack = this.whoIsNextAttack.equals(this.c1attack) ? this.c2attack : this.c1attack;
           
        }
        System.out.println(result);
        return result;
    }

    /**
     * fonction intermédiaire pour l'attaque
     */

    /**
     * chooseAttack, retourne l'attaque choisi
     * 
     * @param c
     * @param name
     * @return Attack
     * @throws BoardException
     */
    Attack chooseAttacks(Chessman c, String name) throws BoardException {
        for (int i = 0; i < c.getAttacks().length; i++) {
            if (c.getAttacks()[i].getName().toLowerCase().equals(name.toLowerCase()))
                return c.getAttacks()[i];
        }
        throw new BoardException("Attack does exist !");
    }

    /**
     * possibleAttack, donne les attaques d'un chessman
     * 
     * @param c
     * @return String
     */
    public String possibleAttack(Chessman c) {
        return c.getName() + "can attack with " + c.getAttacks()[0].getName() + ", " + c.getAttacks()[1].getName()
                + ", " + c.getAttacks()[2].getName() + ".";
    }

    /**
     * possiblePoint, donne le tableau des point possible à gagner
     * 
     * @return
     */
    public int[] possiblePoint() {
        int[] value = { this.c1attack.getLive(), this.c2attack.getLive() };
        return value;
    }

    /**
     * jeu
     */

    /**
     * play, fonction permettant de faire un mouvement ou un combat
     * 
     * @param player
     * @param move
     * @param nameAttack
     * @return String
     * @throws BoardException
     */
    public String play(Player player, Move move, String nameAttack) throws BoardException {
    	
    	// Verification que le score maximun n'est pas atteind pour chaque joueur 
        if (this.score[0] == 890) {
            return this.white.getName() + " have win the game !";
        }
        if (this.score[1] == 890) {
            return this.black.getName() + " have win the game !";
        }
        
        // Absence de combat 
        if(this.whoIsNextAttack == null && !move.isNull()) {
        	
        	// Combat fini 
        	if (this.whoIsNextAttack != null) {
        		throw new BoardException("Sorry you can't move, have fight in the game."); 
        	}
        	
        	// ce n'est pas le bon joueur qui joue
            if (!this.whoIsNext.equals(player)) {
                throw new BoardException("it's not your turn");
            }
            
            // movement nul
            if (move.isNull()) {
                throw new BoardException("this move is null");
            }

            Chessman chessman = this.board[move.getStart().getRow()][move.getStart().getCol()].getChessman();
            
            // on ne bouge pas un pièce
            if (chessman == null) {
                throw new BoardException("there is no chessman on " + move.getStart());
            }

            // La pièce n'appartient pas au joeur 
            if (!player.equals(chessman.getPlayer())) {
                throw new BoardException(chessman.getName() + " at " + move.getStart() + " does not belong to you");
            }
            
            // move non autoriser 
            if (!chessman.canMove(move)) {
                throw new BoardException(chessman.getName() + " cannot move to " + move.getEnd());
            }

            return this.move(chessman, move);
        	
        } 
        
        if(nameAttack != null ){
        
        	if(!this.whoIsNextAttack.getPlayer().equals(player)) {
        		throw new BoardException("It'snt your turn to attack !");
        	}
        	if (this.c1attack.getLive() <= 0 || this.c2attack.getLive() <= 0) {
                // demande d'attaque alors qu'il n'y a rien 
                throw new BoardException("Not have a fight !");
            }
            // execution d'une attaque 
            return this.fight(player, nameAttack);
        } 
        
        throw new BoardException("not execute move or attack !");
       
    }

    // Persitance
    public String serialize() {

        JSONObject root = new JSONObject();
        root.put("white", this.white.getName());
        root.put("black", this.black.getName());
        root.put("whoIsNext", this.whoIsNext.getName());
        if(this.whoIsNextAttack == null) {
        	root.put("whoIsNextAttack", (JSONObject)null);
        }else {
        	root.put("whoIsNextAttack", this.whoIsNextAttack.getPlayer().getName());
        }
        root.put("score", this.score);

        // board
        JSONArray board = new JSONArray();
        for (int letter = 0; letter < this.board.length; letter++) {
            JSONArray squareLine = new JSONArray();
            for (int number = 0; number < this.board.length; number++) {
                JSONObject square = new JSONObject();
                if (this.board[letter][number].isTaken()) {
                    Chessman c = this.board[letter][number].getChessman();
                    if (c.getName().equals("pawn") && c.getPlayer().equals(this.white)) {
                        square.put("boolean", true);
                    }
                    if (c.getName().equals("pawn") && c.getPlayer().equals(this.black)) {
                        square.put("boolean", false);
                    }
                    square.put("chessman", c.getName());
                    square.put("player", c.getPlayer().getName());
                    squareLine.put(square);
                } else {
                    squareLine.put((JSONObject) null);
                }
            }
            board.put(squareLine);
        }

        root.put("board", board);

        return root.toString();

    }

    public void deserialize(String game) {

        JSONObject root = new JSONObject(game);

        // verification place des joueurs
        if (!this.white.getName().equals(root.getString("white"))) {
            Player role = this.white;
            this.white = this.black;
            this.black = role;
        }

        // remise du score
        for (int i = 0; i < root.getJSONArray("score").length(); i++) {
            this.score[i] = root.getJSONArray("score").getInt(i);
        }

        // tour du joueur
        this.whoIsNext = root.get("whoIsNext").equals(this.white.getName()) ? this.white : this.black;

        // board
        JSONArray board = root.getJSONArray("board");

        for (int letter = 0; letter < board.length(); letter++) {
            JSONArray squareLine = board.getJSONArray(letter);
            for (int number = 0; number < squareLine.length(); number++) {
                JSONObject square = squareLine.optJSONObject(number);
                if (square != null) {
                    Player p = square.getString("player").equals(this.white.getName()) ? this.white : this.black;
                    switch (square.getString("chessman")) {
                    case "pawn":
                        this.board[letter][number].setChessman(new Pawn(p, square.getBoolean("boolean")));
                        break;
                    case "bishop":
                        this.board[letter][number].setChessman(new Bishop(p));
                        break;
                    case "king":
                        this.board[letter][number].setChessman(new King(p));
                        break;
                    case "knight":
                        this.board[letter][number].setChessman(new Knight(p));
                        break;
                    case "rook":
                        this.board[letter][number].setChessman(new Rook(p));
                        break;
                    case "queen":
                        this.board[letter][number].setChessman(new Queen(p));
                        break;
                    }
                } else {
                    this.board[letter][number] = new Square(null);
                }
            }
        }

    }

}
