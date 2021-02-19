package chess.game.chessman;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import chess.game.BoardException;
import chess.game.Move;
import chess.player.Player;

public class KnightTest {
	Player player;
	Knight knight;

	@Before
	public void setUp() throws Exception {
		this.player = new Player(UUID.randomUUID());
		this.knight = new Knight(this.player);
	}


	@Test
	public void canMoveTest() throws BoardException {
		// dep non en L
		Move m1 = new Move('b','2','a','1');
		Move m2 = new Move('b','2','d','4');
		Move m3 = new Move('b','2','a','3');
		Move m4 = new Move('d','4','b','2');
				
		// dep  L
		Move m5 = new Move('a','1','b','3');
		
		assertEquals(this.knight.canMove(m1), false);
		assertEquals(this.knight.canMove(m2), false);
		assertEquals(this.knight.canMove(m3), false);
		assertEquals(this.knight.canMove(m4), false);
		assertEquals(this.knight.canMove(m5), true);
	}

}
