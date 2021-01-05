package chess;

import java.io.IOException;

import chess.game.Game;
import chess.game.GameFactory;

class App extends Thread {
    @Override
    public void run() {
        try {
            GameFactory factory = new GameFactory();
            Game game;
            game = factory.createGame();
            game.loop();
        } catch (IOException e) {
            // TODO
            e.printStackTrace();
        }

        System.out.println("Bye bye.");
    }
}
