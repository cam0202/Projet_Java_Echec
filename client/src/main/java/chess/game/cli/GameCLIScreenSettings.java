package chess.game.cli;

import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;

public class GameCLIScreenSettings extends GameCLIScreen {

    protected GameCLIScreenSettings(GameCLI game) {
        super(game);

        Panel panel = new Panel();
        
        panel.addComponent(new Button("Go back", new ActionBack(this)));

        panel.addComponent(new Label("TODO")); // TODO

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
    
}
