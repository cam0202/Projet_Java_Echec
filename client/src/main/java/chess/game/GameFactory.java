package chess.game;

import java.io.IOException;

import chess.cli.GameCLI;

/**
 * This program is intended to be used in a terminal, but we still want to be
 * able to create other variants like a GUI app or a web app. This factory
 * allows to select the type of front-end we want
 */
public class GameFactory {
    public GameFactory() {

    }

    /**
     * Creates an appropriate front-end for the context
     */
    public Game createGame() throws IOException {
        return new GameCLI();
    }
}
