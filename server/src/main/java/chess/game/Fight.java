package chess.game;

import chess.game.chessman.Attack;
import chess.game.chessman.Chessman;
import chess.player.Player;

public class Fight {

    private Chessman c1;
    private Chessman c2;
    private Chessman whoIsNext;

    public Fight(Chessman c1, Chessman c2){
        this.c1 = c1;
        this.c2 = c2;
        this.whoIsNext = c2;
    }

    

    Attack chooseAttacks(Chessman c, String name) throws BoardException {
        for(int i = 0 ; i < c.getAttacks().length ; i++){
            if(c.getAttacks()[i].getName().toLowerCase().equals(name.toLowerCase())) return c.getAttacks()[i];
        }
        throw new BoardException("Attack does exist !");
    }

    public String toFight(Chessman c, String name) throws BoardException {
        
        if(!this.whoIsNext.equals(c)){
            throw new BoardException("it's not your turn");
        }

        if (this.whoIsNext.getLive() <= 0){
            return "You have win the fight";
        }

        if (c.getLive() <= 0){
            return "You loose the fight";
        }

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
