package chess.game.cli;

import java.io.IOException;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.screen.TabBehaviour;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.Terminal;

public abstract class GameCLIScreen {
    protected final Terminal terminal;
    protected final TerminalScreen screen;
    
    protected final GameCLIScreen previous;

    protected GameCLIScreen(final Terminal terminal, final GameCLIScreen previous) throws IOException {
        this.terminal = terminal;
        this.screen = new TerminalScreen(this.terminal);
        this.screen.setTabBehaviour(TabBehaviour.IGNORE);
        this.screen.setCursorPosition(TerminalPosition.TOP_LEFT_CORNER);
        
        this.previous = previous;
    }

    public abstract void render() throws IOException;

    //public abstract String get();
    //public abstract List<CLIOption> getOptions();
}
