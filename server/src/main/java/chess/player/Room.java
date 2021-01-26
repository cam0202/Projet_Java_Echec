package chess.player;

import chess.game.Color;
import chess.game.Game;

public class Room {
    private final Game board;

    public Room(final Player player1, final Player player2) {
        Color c1 = new Color();
        Color c2 = new Color();
        c1.White();
        c2.Black();
        player1.color = c1;
        player1.color = c2;
        this.board = new Game(c1, c2);
    }
}
