package chess.game.cli;

import java.io.IOException;
import java.util.List;

import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.Separator;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.menu.MenuItem;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import chess.network.ExchangePacket;
import chess.server.Server;

public class GameCLIScreenHome extends GameCLIScreen {
    private static final Logger LOGGER = Logger.getLogger(GameCLIScreenHome.class);

    public GameCLIScreenHome(final GameCLI game) {
        super(game);

        Panel leftPanel = new Panel();
        leftPanel.addComponent(new EmptySpace());
        leftPanel.addComponent(new MenuItem("Join server", new ActionJoin(this)));
        leftPanel.addComponent(new MenuItem("Settings", new ActionSettings(this)));
        leftPanel.addComponent(new MenuItem("Quit", new ActionQuit(this)));

        Panel rightPanel = new Panel();
        try {
            List<ExchangePacket> servers = Server.discover();
            if (servers.size() == 0) {
                rightPanel.addComponent(new Label(" - No server found -"));
            } else {
                for (int i = 0; i < servers.size(); i++) {
                    if (i > 0) {
                        rightPanel.addComponent(new Separator(Direction.HORIZONTAL));
                    }

                    ExchangePacket packet = servers.get(i);
                    JSONObject root = new JSONObject(packet.getMessage().getData());
                    Panel server = new Panel();
                    server.addComponent(new Button(String.format("%s (%d/%d)", root.getString("name"),
                            root.getInt("online_players"), root.getInt("max_online_players")), new Runnable() {
                                @Override
                                public void run() {
                                }
                            }));
                    server.addComponent(new Label(root.getString("description")));
                    rightPanel.addComponent(server);
                }
            }
        } catch (IOException e) {
            LOGGER.error(e);
        }

        Panel panel = new Panel();
        panel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
        panel.addComponent(leftPanel);
        panel.addComponent(rightPanel.withBorder(Borders.singleLine(" Available servers ")));

        this.setComponent(panel);
    }

    private class ActionJoin implements Runnable {
        private final GameCLIScreen gui;

        public ActionJoin(final GameCLIScreen gui) {
            this.gui = gui;
        }

        @Override
        public void run() {
            this.gui.game.setNextGUI(new GameCLIScreenJoinServer(this.gui.game));
            this.gui.close();
        }
    }

    private class ActionSettings implements Runnable {
        private final GameCLIScreen gui;

        public ActionSettings(final GameCLIScreen gui) {
            this.gui = gui;
        }

        @Override
        public void run() {
            this.gui.game.setNextGUI(new GameCLIScreenSettings(this.gui.game));
            this.gui.close();
        }
    }

    private class ActionQuit implements Runnable {
        private final GameCLIScreen gui;

        public ActionQuit(final GameCLIScreen gui) {
            this.gui = gui;
        }

        @Override
        public void run() {
            this.gui.close();
        }
    }
}
