package chess.cli;

import java.io.IOException;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import org.apache.log4j.Logger;

import chess.game.Game;
import chess.server.Server;

public class GameCLI extends Game {
    
    private static final Logger LOGGER = Logger.getLogger(GameCLI.class);

    private final DefaultTerminalFactory factory;
    private final GameCLIWindow window;

    private final Server server;

    public GameCLI() throws IOException {
        this.factory = new DefaultTerminalFactory();
        this.factory.setPreferTerminalEmulator(false);
        this.window = new GameCLIWindow();

        this.server = new Server();
    }

    public void switchPanel(final GameCLIPanel newPanel) {
        if (newPanel != null) {
            newPanel.update();
        }
        
        this.window.setComponent(newPanel);
    }

    public GameCLIWindow getWindow() {
        return this.window;
    }

    public Server getServer() {
        return this.server;
    }

    @Override
    public void loop() {
        try (Terminal terminal = this.factory.createTerminal()) {
            terminal.setCursorVisible(false);

            try (Screen screen = new TerminalScreen(terminal)) {
                screen.startScreen();

                MultiWindowTextGUI gui = new MultiWindowTextGUI(screen);
                gui.addWindow(this.window);

                GameCLIPanel panel = new GameCLIPanelHome(this);
                Thread t = new Thread(new Runnable(){
                    @Override
                    public void run() {
                        panel.update();
                    }
                });
                t.start();
                
                this.window.setCloseWindowWithEscape(true); // TODO: temporary
                this.window.setComponent(panel);

                // TODO: SIGINT seems to not be handled correctly here...
                gui.waitForWindowToClose(this.window);

            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }

        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }

        /*
         * // All of this is temporary, and is just to make a somewhat functional 0.1.0
         * // version try (Scanner scanner = new Scanner(System.in)) { Server server =
         * null;
         * 
         * boolean run = true; while (run) { String command = scanner.next(); switch
         * (command) { case "stop": { if (server != null) { server.disconnect(); } run =
         * false; break; } case "connect": { if (server != null) {
         * System.err.println("Already connected to a server!"); break; }
         * 
         * ExchangePacket serverData = null; Collection<ExchangePacket> servers =
         * Server.discover();
         * 
         * if (servers == null || servers.isEmpty()) {
         * System.err.println("Failed to discover servers!"); break; }
         * 
         * for (ExchangePacket s : servers) { System.out.println("Selecting server [" +
         * s.getAddress() + "]:" + s.getPort()); serverData = s; break; // Select the
         * first one for now }
         * 
         * if (serverData == null) { System.err.println("No servers found!"); break; }
         * 
         * server = new Server(serverData.getAddress(), serverData.getPort());
         * server.connect(); { JSONObject root = new
         * JSONObject(serverData.getMessage().getData());
         * System.out.println("---------- CONNECTED ----------");
         * System.out.println(String.format("%s (%d/%d)", root.getString("name"),
         * root.getInt("online_players"), root.getInt("max_online_players")));
         * System.out.println(root.getString("description")); }
         * 
         * break; }
         * 
         * case "disconnect": { if (server != null) { server.disconnect(); server =
         * null; System.out.println("---------- DISCONNECTED ----------"); } else {
         * System.err.println("Not connected!"); } break; }
         * 
         * case "move": { if (server == null) { System.err.println("Not connected!");
         * break; }
         * 
         * System.out.print("Enter your move: "); command = scanner.next();
         * System.out.println(); try { server.move(command);
         * System.out.println("Executed move '" + command + "'"); } catch (IOException
         * e) { System.err.println("Move failed: " + e.getMessage()); } break; }
         * 
         * default: { System.err.println("Unknown command!"); break; } } } } catch
         * (IOException e) { LOGGER.fatal(e); }
         */

        // @formatter:off
        /*
        *
        *                       
        *            CHESS           │        AVAILABLE SERVERS                        
        *                            │                     
        *  > Join a server           │  [The Main Server] 2/10                              
        *  > Settings                │   This is the main server                              
        *                            │                                 
        *                            │  [Another server] 19/20                              
        *                            │   Come play with us, we have                               
        *                            │   cookies!                              
        *                            │                                 
        *                            │      .....searching.....                      
        *                            │                                 
        */
        // @formatter:on

        // @formatter:off
        /*
        *
        *        CHESS          ┌────────────────────────────────┐ 
        *  Server: Server name  │                                │
        *    Room: #1           │                                │
        *                       │                                │
        *                       │                                │
        *  8 ██  ██  ██  ██     │                                │ 
        *  7   ██  ██  ██  ██   │                                │ 
        *  6 ██  ██  ██  ██     │                                │ 
        *  5   ██  ██  ██  ██   │                                │ 
        *  4 ██  ██  ██  ██     │                                │ 
        *  3   ██  ██  ██  ██   │ <Hugo> Ceci est un message     │ 
        *  2 ██  ██  ██  ██     │ de joueur                      │ 
        *  1   ██  ██  ██  ██   │ >>> Ceci est un message        │
        *    a b c d e f g h    │ système                        │
        *                       └────────────────────────────────┘ 
        * ┌─ Your input ─────────────────────────────────────────┐ 
        * └
        */
        // @formatter:on

        /*
         * ████ ████ try { //this.currentScreen.things();
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
