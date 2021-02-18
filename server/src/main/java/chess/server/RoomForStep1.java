package chess.server;

import java.util.Arrays;

import org.apache.log4j.Logger;

import chess.game.Board;
import chess.game.BoardException;
import chess.game.Move;
import chess.player.Player;

public class RoomForStep1 {
    private final static Logger LOGGER = Logger.getLogger(RoomForStep1.class);

    private final Board board;

    private final Player player1;
    private final Player player2;

    public RoomForStep1(final Player player1, final Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.board = new Board(player1, player2);
    }

    public void doCommand(Player player, String command) {
    	char[] data = command.toLowerCase().toCharArray();
        LOGGER.debug("PLAY command " + Arrays.toString(data));
        
        // cas du déplacement 
        if (command.length() == 4) {
        	  try {
                  Move move = new Move(data[0], data[1], data[2], data[3]);
                  this.board.play(player, move, null);
                  LOGGER.debug("Move " + Arrays.toString(data));
              } catch (BoardException e) {
              	System.err.println(e.getMessage());
              }
        } else {
        	// cas de l'attaque
            
            String attack = "";
            for(int i = 0; i < data.length; i++) {
            	attack += data[i];
            }
            try {
    			this.board.play(player, null, attack);
    			LOGGER.debug("Attack " + attack);
    		} catch (BoardException e) {
    			System.err.println(e.getMessage());
    		}
        }
        

        /*
         * Move move = this.board.getMove(command);
         * 
         * if (player.getUUID().equals(player1.getUUID())) {
         * this.board.move(player1.color, move); } else if
         * (player.getUUID().equals(player1.getUUID())) { this.board.move(player2.color,
         * move); } else { throw new RuntimeException("failed to do command..."); }
         */
    }
}
