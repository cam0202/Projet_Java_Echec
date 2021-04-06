package chess.game.chessman;
/**
 * 
 * Classe Attack
 * permet la définition d'une attaque 
 *
 */
public class Attack {
    private final String name;
    private int value;
    
    /**
     * Attack, permet d'initialiser l'attaque par un nom
     * @param name
     */
    public Attack(String name){
        this.name = name;
        this.value = 0;
    }
    
    /**
     * getName, donne le nom d'une attaque
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * getValue, donne la valeur de l'attauqe 
     * @return int 
     */
    public int getValue() {
        return value;
    }
    
    /**
     * setValue, MaJ de la valeur de l'attaque
     * @param c
     */
    public void setValue(Chessman c) {
        this.value = (c.getLive()/2) + (int)(Math.random() * (c.getLive() - (c.getLive()/2)));
        if(c.getLive() == 1) {
        	this.value = 1;
        }
    }

}
