package chess.game.chessman;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import chess.game.BoardException;
import chess.game.Move;
import chess.player.Player;

public class KingTest {

	Player player;
	King king;

	@Before
	public void setUp() throws Exception {
		this.player = new Player(UUID.randomUUID());
		this.king = new King(this.player);
	}


	@Test
	public void canMoveTest() throws BoardException {
		// dep vertical
		Move m1 = new Move('d','2','d','1');
		Move m2 = new Move('d','2','d','3');
		Move m3 = new Move('d','2','c','2');
		Move m4 = new Move('d','4','c','3');
				
		// dep non vertical
		Move m5 = new Move('a','1','a','4');
		
		assertEquals(this.king.canMove(m1), true);
		assertEquals(this.king.canMove(m2), true);
		assertEquals(this.king.canMove(m3), true);
		assertEquals(this.king.canMove(m4), true);
		assertEquals(this.king.canMove(m5), false);
	}
}
