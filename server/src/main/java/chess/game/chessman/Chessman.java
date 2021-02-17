package chess.game.chessman;

import chess.game.Move;
import chess.player.Player;

/**
 * Chessman Abstract Class
 * Point de départ de toute pièce du jeu
 */
public abstract class Chessman {

    private final String name;
    private final Player player;
    private int live;
    private Attack[] attacks;

    /**
     * Constructeur d'une pièce
     * @param name
     * @param player
     * @param live
     * @param attacks
     */
    public Chessman(String name, Player player, int live, Attack[] attacks) {
        this.name = name;
        this.player = player;
        this.live = live;
        this.attacks = attacks;
    }
    
    /**
     * getName, donne le nom de la pièce
     * @return String
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * getPlayer, donne le joueur à qui appartient la pièce
     * @return Player
     */
    public Player getPlayer() {
        return this.player;
    }
    
    /**
     * getLive, donne les points de vie de la pièce
     * @return int
     */
    public int getLive() {
        return this.live;
    }
    /**
     * getAttacks, donne la liste des attaques de la pièce
     * @return Attack[]
     */
    public Attack[] getAttacks() {
        return attacks;
    }
    
    /**
     * setLive, MaJ de la vie de la pièce
     * @param impact
     */
    public void setLive(int impact) {
        this.live = this.live - impact;
    }

    public abstract boolean canMove(Move move);

}
