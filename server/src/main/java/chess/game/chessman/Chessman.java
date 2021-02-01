package chess.game.chessman;

import chess.game.Move;
import chess.player.Player;

public abstract class Chessman {

    private final String name;
    private final Player player;

    public Chessman(String name, Player player) {
        this.name = name;
        this.player = player;
    }

    public String getName() {
        return this.name;
    }

    public Player getPlayer() {
        return this.player;
    }

    public abstract boolean canMove(Move move);

}
