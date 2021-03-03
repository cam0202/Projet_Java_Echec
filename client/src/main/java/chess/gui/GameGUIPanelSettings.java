package chess.gui;

import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Label;

public class GameGUIPanelSettings extends GameGUIPanel {

    public GameGUIPanelSettings(GameGUI game) {
        this(game, null);
    }

    public GameGUIPanelSettings(GameGUI game, GameGUIPanel previous) {
        super(game, previous);

        this.addComponent(new Button("Go back", new ActionBack(this)));
        this.addComponent(new Label("TODO")); // TODO
    }

    @Override
    public void update() {
        // Nothing to do here
    }

}
