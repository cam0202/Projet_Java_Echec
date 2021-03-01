package chess.cli;

import java.io.IOException;
import java.net.InetAddress;

import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.TextBox;

public class GameCLIPanelJoin extends GameCLIPanel {

    public GameCLIPanelJoin(final GameCLI game) {
        this(game, null);
    }

    public GameCLIPanelJoin(final GameCLI game, final GameCLIPanel previous) {
        super(game, previous);

        this.setLayoutManager(new GridLayout(2));

        this.addComponent(new Button("Go back", new ActionBack(this)));
        this.addComponent(new EmptySpace());

        final TextBox address = new TextBox("localhost");
        this.addComponent(new Label("Address"));
        this.addComponent(address);

        final TextBox port = new TextBox("" + chess.protocol.Server.DEFAULT_PORT);
        this.addComponent(new Label("Port"));
        this.addComponent(port);

        this.addComponent(new EmptySpace());
        this.addComponent(new Button("Join", new ActionJoin(this, address, port)));
    }

    @Override
    public void update() {
        // Nothing to do here
    }

    private class ActionJoin extends GameCLIAction {

        private final TextBox address;
        private final TextBox port;

        public ActionJoin(final GameCLIPanel panel, final TextBox address, final TextBox port) {
            super(panel);
            this.address = address;
            this.port = port;
        }

        @Override
        public void run() {
            try {
                InetAddress address = InetAddress.getByName(this.address.getText());
                int port = Integer.parseInt(this.port.getText());

                this.panel.getGame().getServer().connect(address, port);
                this.panel.getGame().switchPanel(new GameCLIPanelServerLobby(this.panel.getGame(), this.panel));
           
            } catch (NumberFormatException | IOException e) {
                this.panel.getGame().switchPanel(new GameCLIPanelServerErrorConnect(this.panel.getGame(), e.getMessage(), this.panel));
            }
        }

    }

}
