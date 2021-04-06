package chess.gui;

import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.TextBox;

public class GameGUIPanelSettings extends GameGUIPanel {

    private TextBox username;

    public GameGUIPanelSettings(GameGUI game) {
        this(game, null);
    }

    public GameGUIPanelSettings(GameGUI game, GameGUIPanel previous) {
        super(game, previous);

        this.setLayoutManager(new GridLayout(2));

        this.addComponent(new Button("Go back", new ActionBackWrapper(this)));
        this.addComponent(new EmptySpace());

        this.username = new TextBox("");
        this.addComponent(new Label("Username (empty=auto)"));
        this.addComponent(this.username);
    }

    @Override
    public void update() {
        String username = this.getGame().getServer().getForcedUsername();
        if (username == null) {
            this.username.setText("");
        } else {
            this.username.setText(username);
        }
    }

    private class ActionBackWrapper extends ActionBack {

        public ActionBackWrapper(final GameGUIPanel panel) {
            super(panel);
        }

        @Override
        public void run() {
            String username = ((GameGUIPanelSettings)this.panel).username.getText();
            if (username == null || username.isEmpty()) {
                this.panel.getGame().getServer().setForcedUsername(null);
            }
            else {
                this.panel.getGame().getServer().setForcedUsername(username);
            }

            super.run();
        }
    }

}
