package chess.cli;

import java.io.IOException;

import com.googlecode.lanterna.terminal.Terminal;

public class GameCLIScreenHome extends GameCLIScreen {

    public GameCLIScreenHome(GameCLIScreen previous, Terminal terminal) throws IOException {
        super(previous, terminal);
    }
    
}
