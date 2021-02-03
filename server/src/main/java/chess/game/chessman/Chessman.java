package chess.game.chessman;

import chess.game.Move;
import chess.player.Player;

public abstract class Chessman {

    private final String name;
    private final Player player;
    private int live;
    private Attack[] attacks;

    public Chessman(String name, Player player, int live, Attack[] attacks) {
        this.name = name;
        this.player = player;
        this.live = live;
        this.attacks = attacks;
    }

    public String getName() {
        return this.name;
    }

    public Player getPlayer() {
        return this.player;
    }

    public int getLive() {
        return this.live;
    }

    public Attack[] getAttacks() {
        return attacks;
    }

    public void setLive(int impact) {
        this.live = this.live - impact;
    }

    public abstract boolean canMove(Move move);

}
