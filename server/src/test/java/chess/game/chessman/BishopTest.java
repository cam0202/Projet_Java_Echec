package chess.game.chessman;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import chess.game.BoardException;
import chess.game.Move;
import chess.player.Player;

public class BishopTest {

	Player player;
	Bishop bishop;

	@Before
	public void setUp() throws Exception {
		this.player = new Player(UUID.randomUUID());
		this.bishop = new Bishop(this.player);
	}


	@Test
	public void canMoveTest() throws BoardException {
		// dep vertical
		Move m1 = new Move('b','2','a','1');
		Move m2 = new Move('b','2','d','4');
		Move m3 = new Move('b','2','a','3');
		Move m4 = new Move('d','4','b','2');
				
		// dep non vertical
		Move m5 = new Move('a','1','a','4');
		
		assertEquals(this.bishop.canMove(m1), true);
		assertEquals(this.bishop.canMove(m2), true);
		assertEquals(this.bishop.canMove(m3), true);
		assertEquals(this.bishop.canMove(m4), true);
		assertEquals(this.bishop.canMove(m5), false);
	}

}
