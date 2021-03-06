package chess.gui;

import java.io.EOFException;
import java.io.IOException;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.TextGUIThread;
import com.googlecode.lanterna.gui2.WindowListener;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import org.apache.log4j.Logger;

import chess.game.Game;
import chess.server.Server;

public class GameGUI extends Game {

    private static final Logger LOGGER = Logger.getLogger(GameGUI.class);

    private final DefaultTerminalFactory factory;
    private final GameGUIWindow window;

    private final Server server;

    private GameGUIPanel currentPanel;

    public GameGUI() throws IOException {
        this.factory = new DefaultTerminalFactory();
        this.factory.setPreferTerminalEmulator(false);
        this.window = new GameGUIWindow();

        this.server = new Server();

        this.currentPanel = null;
    }

    public void switchPanel(final GameGUIPanel newPanel) {
        this.currentPanel = newPanel;
        this.currentPanel.setRequireUpdate(true);
        this.window.setComponent(this.currentPanel);
    }

    public GameGUIWindow getWindow() {
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

                this.window.setCloseWindowWithEscape(true); // TODO: temporary

                this.switchPanel(new GameGUIPanelHome(this));

                while (this.window.getTextGUI() != null) {
                    if (this.currentPanel != null && this.currentPanel.getRequireUpdate()) {
                        this.currentPanel.update();
                        this.currentPanel.setRequireUpdate(false);
                    }

                    boolean sleep = true;
                    TextGUIThread guiThread = gui.getGUIThread();
                    if (Thread.currentThread() == guiThread.getThread()) {
                        try {
                            sleep = !guiThread.processEventsAndUpdate();
                        } catch (EOFException ignore) {
                            // The GUI has closed so allow exit
                            break;
                        } catch (IOException e) {
                            throw new RuntimeException("Unexpected IOException while waiting for window to close", e);
                        }
                    }

                    if (sleep) {
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException ignore) {
                        }
                    }
                }

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
