package chess.game.cli;

import java.io.IOException;

import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import chess.game.Game;

public class GameCLI extends Game {
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
        /*
        try {
            //this.currentScreen.things();
            
            terminal.enterPrivateMode();
            terminal.setCursorVisible(false);
            terminal.setBackgroundColor(TextColor.ANSI.BLUE);
            terminal.setForegroundColor(TextColor.ANSI.WHITE);
            terminal.setCursorPosition(0, 0);
            
            terminal.clearScreen();
            terminal.putString("Au secours");
            terminal.flush();
            
            terminal.resetColorAndSGR();

            KeyStroke key = terminal.readInput();
            terminal.putString(key.toString());

            terminal.exitPrivateMode();
            terminal.close();
        } catch (IOException e) {

        } catch (InterruptedException e) {

        }
        */
    }
}
