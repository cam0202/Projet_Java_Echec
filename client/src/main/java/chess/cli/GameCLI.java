package chess.cli;

import java.io.IOException;
import java.util.Collection;
import java.util.Scanner;

import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import chess.game.Game;
import chess.network.MessagePacket;
import chess.server.Server;

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
        // All of this is temporary, and is just to make a somewhat functional 0.1.0
        // version
        System.out.println("Welcome!");
        try (Scanner scanner = new Scanner(System.in)) {
            Server server = new Server();

            boolean run = true;
            while (run) {
                String command = scanner.next();
                switch (command) {
                    case "stop": {
                        if (server.isConnected()) {
                            server.disconnect();
                        }
                        run = false;
                        break;
                    }
                    case "connect": {
                        if (server.isConnected()) {
                            System.err.println("Already connected to a server!");
                            break;
                        }

                        MessagePacket serverData = null;
                        Collection<MessagePacket> servers = server.discover();

                        if (servers.isEmpty()) {
                            System.err.println("Failed to discover servers!");
                            break;
                        }

                        for (MessagePacket s : servers) {
                            System.out.println("Selecting server [" + s.getAddress() + "]:" + s.getPort());
                            serverData = s;
                            break; // Select the first one for now
                        }

                        if (serverData == null) {
                            System.err.println("No servers found!");
                            break;
                        }

                        server.connect(serverData.getAddress(), serverData.getPort());
                        {
                            JSONObject root = new JSONObject(serverData.getMessage().getData());
                            System.out.println("---------- CONNECTED ----------");
                            System.out.println(String.format("%s (%d/%d)", root.getString("name"),
                                    root.getInt("online_players"), root.getInt("max_online_players")));
                            System.out.println(root.getString("description"));
                        }

                        break;
                    }

                    case "disconnect": {
                        if (server.isConnected()) {
                            server.disconnect();
                            System.out.println("---------- DISCONNECTED ----------");
                        } else {
                            System.err.println("Not connected!");
                        }
                        break;
                    }

                    case "play": {
                        if (!server.isConnected()) {
                            System.err.println("Not connected!");
                            break;
                        }

                        System.out.print("Enter your play: ");
                        command = scanner.next();
                        System.out.println();
                        try {
                            System.out.println("Sending play '" + command + "'");
                            server.post(command);
                        } catch (IOException e) {
                            System.err.println("Play failed: " + e.getMessage());
                        }
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
         * â”Œâ”€â”€â”€â”€â”€â”€â”€â”€ Chat â”€â”€â”€â”€â”€â”€â”€â”€â”� 8 â–ˆâ–ˆ â–ˆâ–ˆ â–ˆâ–ˆ â–ˆâ–ˆ â”‚ â”‚ 7 â–ˆâ–ˆ â–ˆâ–ˆ â–ˆâ–ˆ â–ˆâ–ˆ â”‚ â”‚ 6 â–ˆâ–ˆ â–ˆâ–ˆ â–ˆâ–ˆ â–ˆâ–ˆ â”‚
         * â”‚ 5 â–ˆâ–ˆ â–ˆâ–ˆ â–ˆâ–ˆ â–ˆâ–ˆ â”‚ â”‚ 4 â–ˆâ–ˆ â–ˆâ–ˆ â–ˆâ–ˆ â–ˆâ–ˆ â”‚ â”‚ 3 â–ˆâ–ˆ â–ˆâ–ˆ â–ˆâ–ˆ â–ˆâ–ˆ â”‚ â”‚ 2 â–ˆâ–ˆ â–ˆâ–ˆ â–ˆâ–ˆ â–ˆâ–ˆ â”‚ â”‚ 1
         * â–ˆâ–ˆ â–ˆâ–ˆ â–ˆâ–ˆ â–ˆâ–ˆ â”‚ â”‚ a b c d e f g h â”‚ Ceci est un message â”‚
         * â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜ â”Œâ”€ Your input â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”� â””
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
