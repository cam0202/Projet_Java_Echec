package chess.game.cli;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Scanner;

import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import org.apache.log4j.Logger;

import chess.server.Server;
import chess.game.Game;
import chess.network.ExchangePacket;

public class GameCLI extends Game {
    private static final Logger LOGGER = Logger.getLogger(GameCLI.class);

    private DefaultTerminalFactory factory;
    private Terminal terminal;

    private GameCLIScreen currentScreen;

    public GameCLI() throws IOException {
        this.factory = new DefaultTerminalFactory();
        this.factory.setPreferTerminalEmulator(false);

        this.terminal = this.factory.createTerminal();

        this.currentScreen = new GameCLIScreenHome(null, this.terminal);
    }

    @Override
    public void loop() {
        try {
            Scanner scanner = new Scanner(System.in);
            Server server = null;

            boolean run = true;
            while (run) {
                String command = scanner.next();
                switch (command) {
                    case "stop": {
                        if (server != null) {
                            server.disconnect();
                        }
                        run = false;
                        break;
                    }
                    case "connect": {
                        if (server != null) {
                            System.err.println("Already connected to a server!");
                            break;
                        }

                        InetAddress address = null;
                        int port = 0;
                        Collection<ExchangePacket> servers = null;
                        for (int i = 0; i < 5; i++) {
                            servers = Server.discover();
                            if (servers.size() > 0) {
                                break;
                            }
                        }

                        if (servers == null || servers.isEmpty()) {
                            System.err.println("Failed to discover");
                            break;
                        }

                        for (ExchangePacket s : servers) {
                            System.out.println("Selecting server [" + s.getAddress() + "]:" + s.getPort() + " -> "
                                    + s.getMessage().getData());
                            address = s.getAddress();
                            port = s.getPort();
                            break; // Select the first one for now
                        }

                        if (address == null) {
                            System.err.println("No servers found");
                            break;
                        }

                        server = new Server(address, port);
                        server.connect();
                        break;
                    }

                    case "disconnect": {
                        if (server != null) {
                            server.disconnect();
                            server = null;
                            System.out.println("Disconnecting");
                        } else {
                            System.err.println("Not connected!");
                        }
                        break;
                    }

                    case "move": {
                        if (server == null) {
                            System.err.println("Not connected!");
                            break;
                        }

                        System.out.print("Enter your move: ");
                        command = scanner.next();
                        server.move(command);
                        System.out.println();
                        break;
                    }

                    default: {
                        System.err.println("Unknown command!");
                        break;
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.fatal(e);
        }

        /*
         * ┌──────── Chat ────────┐ 8 ██ ██ ██ ██ │ │ 7 ██ ██ ██ ██ │ │ 6 ██ ██ ██ ██ │
         * │ 5 ██ ██ ██ ██ │ │ 4 ██ ██ ██ ██ │ │ 3 ██ ██ ██ ██ │ │ 2 ██ ██ ██ ██ │ │ 1
         * ██ ██ ██ ██ │ │ a b c d e f g h │ Ceci est un message │
         * └──────────────────────┘ ┌─ Your input ───────────────────────────────┐ └
         */

        /*
         * try { //this.currentScreen.things();
         * 
         * terminal.enterPrivateMode(); terminal.setCursorVisible(false);
         * terminal.setBackgroundColor(TextColor.ANSI.BLUE);
         * terminal.setForegroundColor(TextColor.ANSI.WHITE);
         * terminal.setCursorPosition(0, 0);
         * 
         * terminal.clearScreen(); terminal.putString("Au secours"); terminal.flush();
         * 
         * terminal.resetColorAndSGR();
         * 
         * KeyStroke key = terminal.readInput(); terminal.putString(key.toString());
         * 
         * terminal.exitPrivateMode(); terminal.close(); } catch (IOException e) {
         * 
         * } catch (InterruptedException e) {
         * 
         * }
         */
    }
}
