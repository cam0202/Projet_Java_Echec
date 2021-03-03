package chess.gui;

import java.util.Arrays;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Window;

import org.apache.log4j.Logger;

public class GameGUIWindow extends BasicWindow {
    private static final Logger LOGGER = Logger.getLogger(GameGUIWindow.class);

    protected GameGUIWindow() {
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

    //public abstract void prepare();

    //public abstract String get();
    //public abstract List<CLIOption> getOptions();
}
