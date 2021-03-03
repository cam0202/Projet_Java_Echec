package chess.game.chessman;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import chess.game.BoardException;
import chess.game.Move;
import chess.player.Player;

public class QueenTest {

	Player player;
	Queen queen;

	@Before
	public void setUp() throws Exception {
		this.player = new Player(UUID.randomUUID());
		this.queen = new Queen(this.player);
	}


	@Test
	public void canMoveTest() throws BoardException {
		// dep ok
		Move m1 = new Move('b','2','a','1');
		Move m2 = new Move('b','2','d','4');
		Move m3 = new Move('b','2','a','3');
		Move m4 = new Move('d','4','b','2');
				
		// dep  non ok
		Move m5 = new Move('a','1','b','3');
		
		assertEquals(this.queen.canMove(m1), true);
		assertEquals(this.queen.canMove(m2), true);
		assertEquals(this.queen.canMove(m3), true);
		assertEquals(this.queen.canMove(m4), true);
		assertEquals(this.queen.canMove(m5), false);
	}

}
