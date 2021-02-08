package chess.game.cli;

import java.net.InetAddress;

import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;

public class GameCLIScreenServerLobby extends GameCLIScreen {

    protected GameCLIScreenServerLobby(GameCLI game, InetAddress address, int port) {
        super(game);

        Panel panel = new Panel();

        panel.addComponent(new Label(address.getHostAddress()));
        panel.addComponent(new Label("" + port));
        panel.addComponent(new Label("TODO")); // TODO

        this.setComponent(panel);
    }
    
}
