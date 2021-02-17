package chess.game;

import chess.game.chessman.Attack;
import chess.game.chessman.Chessman;
import chess.player.Player;

/**
 * 
 * Classe Fight
 * Gère un combat 
 *
 */
public class Fight {

    private Chessman c1;
    private Chessman c2;
    private Chessman whoIsNext;
    
    
    /**
     * Figth, constructeur 
     * @param c1
     * @param c2
     * @param whoIsNext
     */
    public Fight(Chessman c1, Chessman c2, Chessman whoIsNext){
        this.c1 = c1;
        this.c2 = c2;
        this.whoIsNext = whoIsNext;
    }

    
    /**
     * chooseAttack, retourne l'attaque choisi
     * @param c
     * @param name
     * @return Attack
     * @throws BoardException
     */
    Attack chooseAttacks(Chessman c, String name) throws BoardException {
        for(int i = 0 ; i < c.getAttacks().length ; i++){
            if(c.getAttacks()[i].getName().toLowerCase().equals(name.toLowerCase())) return c.getAttacks()[i];
        }
        throw new BoardException("Attack does exist !");
    }
    
    /**
     * possibleAttack, donne les attaques d'un chessman
     * @param c
     * @return String 
     */
    public String possibleAttack(Chessman c) {
    	return c.getName() + "can attack with " + c.getAttacks()[0].getName() 
    			+", " + c.getAttacks()[1].getName()
    			+", " + c.getAttacks()[2].getName()+".";
    }
    
    /**
     * possiblePoint, donne le tableau des point possible à gagner
     * @return
     */
    public int[] possiblePoint() {
    	int[] value = {this.c1.getLive(), this.c2.getLive()};
    	return value;
    }
    
    /**
     * toFight, gère le combat
     * @param name
     * @return String
     * @throws BoardException
     */
    public String toFight(String name) throws BoardException {
    	Chessman c= this.whoIsNext.equals(this.c1) ? this.c2 : this.c1;; 
    	// cas du mauvais chessman en attaque
        if(!this.whoIsNext.equals(c)){
            throw new BoardException("it's not your turn");
        }
        // cas de vie = 0 pour le joeur suivant 
        if (this.whoIsNext.getLive() <= 0){
            return c.getPlayer().getName();
        }
        
        // chessman sans vie
        if (c.getLive() <= 0){
            return this.whoIsNext.getPlayer().getName();
        }
        
        // attaque
        Attack attack = this.chooseAttacks(c, name);
        attack.setValue(whoIsNext);
        if(this.whoIsNext.equals(this.c1)) {
        	this.c2.setLive(attack.getValue());
        }else {
        	this.c1.setLive(attack.getValue());
        }
        this.whoIsNext = this.whoIsNext.equals(this.c1) ? this.c2 : this.c1;
        return c.getPlayer().getName() + " impact " + this.whoIsNext.getName() + " with " + attack.getName() + " to "  + attack.getValue();

    }

    
}
