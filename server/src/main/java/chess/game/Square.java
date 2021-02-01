package chess.game;

import chess.game.chessman.Chessman;

public class Square {
    
    private Chessman chessman;

    public Square(Chessman chessman) {
        this.chessman = chessman;
    }

    public Chessman getChessman() {
        return this.chessman;
    }

    public void setChessman(Chessman chessman) {
        this.chessman = chessman;
    }

    public boolean isTaken() {
        return this.chessman != null;
    }

    public void clear() {
        this.chessman = null;
    }

}
