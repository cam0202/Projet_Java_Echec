package chess.game.cli;

import java.io.IOException;
import java.util.ArrayList;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.DefaultWindowDecorationRenderer;
import com.googlecode.lanterna.gui2.DefaultWindowManager;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.WindowDecorationRenderer;
import com.googlecode.lanterna.gui2.WindowManager;
import com.googlecode.lanterna.terminal.Terminal;

public class GameCLIScreenHome extends GameCLIScreen {

    public GameCLIScreenHome(final Terminal terminal, final GameCLIScreen previous) throws IOException {
        super(terminal, previous);

        TextGraphics test = this.screen.newTextGraphics();
        test.putString(0, 0, "aled");
    }

    @Override
    public void render() throws IOException {
        screen.startScreen();

        // Create panel to hold components
        Panel panel = new Panel();
        panel.setLayoutManager(new GridLayout(2));

        panel.addComponent(new Label("Forename"));
        panel.addComponent(new TextBox());

        panel.addComponent(new Label("Surname"));
        panel.addComponent(new TextBox());

        panel.addComponent(new EmptySpace(new TerminalSize(0,0))); // Empty space underneath labels
        panel.addComponent(new Button("Submit"));

        // Create window to hold the panel
        BasicWindow window = new BasicWindow();
        ArrayList<Window.Hint> hints = new ArrayList<>();
        hints.add(Window.Hint.FULL_SCREEN);
        hints.add(Window.Hint.NO_POST_RENDERING);
        window.setHints(hints);
        window.setTheme(SimpleTheme.makeTheme(
            true,
            TextColor.ANSI.WHITE,
            TextColor.ANSI.BLACK, 
            TextColor.ANSI.RED, 
            TextColor.ANSI.GREEN, 
            TextColor.ANSI.BLUE, 
            TextColor.ANSI.YELLOW, 
            TextColor.ANSI.MAGENTA
        ));
        window.setTheme(SimpleTheme.makeTheme(
            true,
            TextColor.ANSI.WHITE,
            TextColor.ANSI.BLACK, 
            TextColor.ANSI.BLACK, 
            TextColor.ANSI.WHITE, 
            TextColor.ANSI.BLACK, 
            TextColor.ANSI.WHITE, 
            TextColor.ANSI.MAGENTA
        ));

        window.setComponent(panel);

        // Create gui and start gui
        WindowDecorationRenderer decorationRenderer = new DefaultWindowDecorationRenderer();
        WindowManager manager = new DefaultWindowManager(decorationRenderer, screen.getTerminalSize());
        MultiWindowTextGUI gui = new MultiWindowTextGUI(screen, manager, new EmptySpace(TextColor.ANSI.BLUE));
        gui.addWindowAndWait(window);
    }
    
}
