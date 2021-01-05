package chess.game.cli;

import java.io.IOException;

import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.Terminal;

public abstract class GameCLIScreen {
    protected GameCLIScreen previous;

    protected Terminal terminal;
    protected TerminalScreen screen;

    protected GameCLIScreen(GameCLIScreen previous, Terminal terminal) throws IOException {
        this.previous = previous;
        
        this.terminal = terminal;
        this.screen = new TerminalScreen(this.terminal);
    }

    //public abstract String get();
    //public abstract List<CLIOption> getOptions();
}
