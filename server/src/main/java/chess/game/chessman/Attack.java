package chess.game.chessman;

public class Attack {
    private final String name;
    private int value;

    public Attack(String name){
        this.name = name;
        this.value = 0;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(Chessman c) {
        this.value = (c.getLive()/2) + (int)(Math.random() * (c.getLive() - (c.getLive()/2)));
    }

}
