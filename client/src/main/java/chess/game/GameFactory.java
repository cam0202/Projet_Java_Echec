package chess.game;

import java.io.IOException;

import chess.gui.GameGUI;

/**
 * This program is intended to be used in a terminal, but we still want to be
 * able to create other variants like a GUI app or a web app. This factory
 * allows to select the type of front-end we want
 */
public class GameFactory {
    private final boolean useGUI = true;
    
    public GameFactory() {

    }

    /**
     * Creates an appropriate front-end for the context
     */
    public Game createGame() throws IOException {
        if (useGUI) {
            return new GameGUI();
        }

        return null;
    }
}
