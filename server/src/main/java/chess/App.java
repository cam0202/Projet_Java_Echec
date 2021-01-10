package chess;

import java.io.IOException;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

import chess.server.Server;

class App extends Thread {
    private static final Logger LOGGER = Logger.getLogger(App.class);

    @Override
    public void run() {
        Logger loggerRoot = Logger.getRootLogger();
        loggerRoot.setLevel(Level.ALL);
        
        ConsoleAppender loggerConsole = new ConsoleAppender();
        loggerConsole.setName("console");
        loggerConsole.setLayout(new SimpleLayout());
        loggerConsole.activateOptions();
        loggerRoot.addAppender(loggerConsole);

        LOGGER.info("Starting server...");
        
        Server server = new Server(chess.protocol.Server.DEFAULT_PORT);
        server.loop();

        LOGGER.info("Bye bye");
    }
}
