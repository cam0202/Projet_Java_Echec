package chess.game;

import java.io.IOException;

import chess.game.cli.GameCLI;

public class GameFactory {
    public GameFactory() {

    }

    public Game createGame() throws IOException {
        // TODO: return something else maybe?
        return new GameCLI();
    }
}
