package chess.game.cli;

import java.util.Arrays;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Window;

import org.apache.log4j.Logger;

public abstract class GameCLIScreen extends BasicWindow {
    private static final Logger LOGGER = Logger.getLogger(GameCLIScreen.class);

    protected final GameCLI game;

    protected GameCLIScreen(final GameCLI game) {
        this.game = game;

        this.setTitle(" Chess Game ");
        this.setHints(Arrays.asList(
            Window.Hint.FULL_SCREEN,
            Window.Hint.NO_POST_RENDERING
        ));
        this.setTheme(SimpleTheme.makeTheme(
            true, 
            TextColor.ANSI.WHITE,
            TextColor.ANSI.BLACK, 
            TextColor.ANSI.RED, 
            TextColor.ANSI.GREEN, 
            TextColor.ANSI.BLUE, 
            TextColor.ANSI.YELLOW, 
            TextColor.ANSI.MAGENTA
        ));
    }

    //public abstract String get();
    //public abstract List<CLIOption> getOptions();
}
