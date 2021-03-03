package chess.game;

import chess.game.chessman.Chessman;

/**
 * 
 * Square Class
 * Sert à représenter une case du jeu
 */
public class Square {
    
    private Chessman chessman;
    
    public Square(Chessman chessman) {
        this.chessman = chessman;
    }
    
    /**
     * getChessman, retourne le chessman de la case 
     * @return Chessman
     */
    public Chessman getChessman() {
        return this.chessman;
    }
    
    /**
     * setChessman, met un chessman sur la case
     * @param chessman
     */
    public void setChessman(Chessman chessman) {
        this.chessman = chessman;
    }
    
    /**
	 * isTaken, sert à savoir si la case est utilisé
	 * @return
	 */
    public boolean isTaken() {
        return this.chessman != null;
    }

    /**
     * clear, vide la case
     */
    public void clear() {
        this.chessman = null;
    }

}
