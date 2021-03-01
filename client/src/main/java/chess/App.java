package chess;

import java.io.IOException;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

import chess.game.Game;
import chess.game.GameFactory;

class App extends Thread {
    private static final Logger LOGGER = Logger.getLogger(App.class);

    @Override
    public void run() {
        Logger loggerRoot = Logger.getRootLogger();
        loggerRoot.setLevel(Level.WARN);
        
        // TODO: log elsewhere, because console is used
        ConsoleAppender loggerConsole = new ConsoleAppender();
        loggerConsole.setName("console");
        loggerConsole.setLayout(new SimpleLayout());
        loggerConsole.activateOptions();
        loggerRoot.addAppender(loggerConsole);
        
        GameFactory factory = new GameFactory();
        
        try {
            Game game = factory.createGame();
            game.loop();
        } catch (IOException e) {
            LOGGER.fatal("Failed to create game", e);
        } catch (RuntimeException e) {
            LOGGER.fatal("Exception occured in game loop", e);
        }

        LOGGER.info("Bye bye");
    }
}
