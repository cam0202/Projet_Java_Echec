package chess.app;

import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import chess.server.Server;

public class App {
    private final static Logger LOGGER = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("p", "port", true, "Set the listening port of the server");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            return;
        }

        Server server = null;
        try {
            server = new Server();
            server.start();

            LOGGER.info("Started server on port " + server.getPort());
            server.join();
        } catch (InterruptedException e) {
            System.out.println("Main interrupt");
            if (server != null) server.interrupt();
        }
    }
}
