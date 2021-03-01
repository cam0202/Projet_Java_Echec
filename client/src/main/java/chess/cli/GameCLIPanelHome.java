package chess.cli;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.Separator;
import com.googlecode.lanterna.gui2.menu.MenuItem;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import chess.network.MessagePacket;
import chess.server.Server;

public class GameCLIPanelHome extends GameCLIPanel {

    private static final Logger LOGGER = Logger.getLogger(GameCLIPanelHome.class);

    private final Panel left; // Option selector
    private final Panel right; // Available servers

    public GameCLIPanelHome(final GameCLI game) {
        this(game, null);
    }

    public GameCLIPanelHome(final GameCLI game, final GameCLIPanel previous) {
        super(game, previous);

        this.left = new Panel();
        this.left.addComponent(new EmptySpace());
        this.left.addComponent(new MenuItem("Join server", new ActionJoinManual(this)));
        this.left.addComponent(new MenuItem("Settings", new ActionSettings(this)));
        this.left.addComponent(new MenuItem("Quit", new ActionQuit(this)));

        this.right = new Panel();

        this.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
    }

    @Override
    public void update() {
        // Discover available servers and add them
        // TODO: not in the main thread
        this.right.removeAllComponents();

        List<MessagePacket> servers = new ArrayList<>();
        try {
            servers = Server.discover();
        } catch (IOException e) {
            LOGGER.error("Failed to discover local servers", e);
        }

        if (servers.size() <= 0) {
            this.right.addComponent(new Label(" - No servers found - "));
        }

        for (int i = 0; i < servers.size(); i++) {
            // Add a separator when multiple servers are available
            if (i > 0) {
                this.right.addComponent(new Separator(Direction.HORIZONTAL));
            }

            // Format returned server data
            MessagePacket packet = servers.get(i);
            try {
                JSONObject root = new JSONObject(packet.getMessage().getData());
                Panel server = new Panel();
                server.addComponent(new Button(
                        String.format("%s (%d/%d)", root.getString("name"), root.getInt("online_players"),
                                root.getInt("max_online_players")),
                        new ActionJoinAuto(this, packet.getAddress(), packet.getPort())));
                server.addComponent(new Label(root.getString("description")));
                this.right.addComponent(server);

            } catch (JSONException ignore) {
                LOGGER.debug(String.format("Server [{}]:{} returned bad discovery data", packet.getAddress(),
                        packet.getPort()));
            }
        }

        this.removeAllComponents();
        this.addComponent(this.left);
        this.addComponent(this.right.withBorder(Borders.singleLineBevel(" Available servers ")));
    }

    private class ActionJoinAuto extends GameCLIAction {

        private final InetAddress address;
        private final int port;

        public ActionJoinAuto(final GameCLIPanel panel, final InetAddress address, final int port) {
            super(panel);
            this.address = address;
            this.port = port;
        }

        @Override
        public void run() {
            try {
                this.panel.getGame().getServer().connect(this.address, this.port);
                this.panel.getGame().switchPanel(new GameCLIPanelServerLobby(this.panel.getGame(), this.panel));
            } catch (IOException e) {
                this.panel.getGame().switchPanel(new GameCLIPanelServerErrorConnect(this.panel.getGame(), e.getMessage(), this.panel));
            }
        }

    }

    private class ActionJoinManual extends GameCLIAction {

        public ActionJoinManual(final GameCLIPanel panel) {
            super(panel);
        }

        @Override
        public void run() {
            this.panel.getGame().switchPanel(new GameCLIPanelJoin(this.panel.getGame(), this.panel));
        }

    }

    private class ActionSettings extends GameCLIAction {

        public ActionSettings(final GameCLIPanel panel) {
            super(panel);
        }

        @Override
        public void run() {
            this.panel.getGame().switchPanel(new GameCLIPanelSettings(this.panel.getGame(), this.panel));
        }

    }

    private class ActionQuit extends GameCLIAction {

        public ActionQuit(final GameCLIPanel panel) {
            super(panel);
        }

        @Override
        public void run() {
            this.panel.getGame().getWindow().close();
        }

    }

}
