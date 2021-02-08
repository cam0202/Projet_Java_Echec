package chess.game.cli;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;

import org.apache.log4j.Logger;

public class GameCLIScreenJoinServer extends GameCLIScreen {
    private static final Logger LOGGER = Logger.getLogger(GameCLIScreenJoinServer.class);

    protected GameCLIScreenJoinServer(GameCLI game) {
        super(game);

        Panel panel = new Panel(new GridLayout(2));

        panel.addComponent(new Button("Go back", new ActionBack(this)));
        panel.addComponent(new EmptySpace());

        panel.addComponent(new Label("Address"));
        final TextBox address = new TextBox("localhost");
        panel.addComponent(address);

        panel.addComponent(new Label("Port"));
        final TextBox port = new TextBox("" + chess.protocol.Server.DEFAULT_PORT);
        panel.addComponent(port);

        panel.addComponent(new EmptySpace());
        panel.addComponent(new Button("Join", new ActionJoin(this, address, port)));

        this.setComponent(panel);
    }

    private class ActionBack implements Runnable {
        private final GameCLIScreen gui;

        public ActionBack(final GameCLIScreen gui) {
            this.gui = gui;
        }

        @Override
        public void run() {
            this.gui.game.setNextGUI(new GameCLIScreenHome(this.gui.game));
            this.gui.close();
        }
    }

    private class ActionJoin implements Runnable {
        private final GameCLIScreen gui;
        private final TextBox address;
        private final TextBox port;

        public ActionJoin(final GameCLIScreen gui, TextBox address, TextBox port) {
            this.gui = gui;
            this.address = address;
            this.port = port;
        }

        @Override
        public void run() {
            try {
                this.gui.game.setNextGUI(new GameCLIScreenServerLobby(this.gui.game,
                        InetAddress.getByName(this.address.getText()), Integer.parseInt(this.port.getText())));
            } catch (NumberFormatException e) {
            } catch (UnknownHostException e) {
            }
            this.gui.close();
        }
    }

}
