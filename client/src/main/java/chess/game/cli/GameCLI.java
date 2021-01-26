package chess.game.cli;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Collection;

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
            Collection<ExchangePacket> servers = Server.discover();
            for (ExchangePacket s : servers) {
                LOGGER.debug("[" + s.getAddress() + "]:" + s.getPort() + " -> " + s.getMessage().getData());
            }

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
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
