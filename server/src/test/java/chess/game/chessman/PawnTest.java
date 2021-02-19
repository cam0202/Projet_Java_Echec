package chess.game.chessman;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import chess.game.BoardException;
import chess.game.Move;
import chess.player.Player;

public class PawnTest {

	Player player;
	Pawn pawn1;
	Pawn pawn2;

	@Before
	public void setUp() throws Exception {
		this.player = new Player(UUID.randomUUID());
		this.pawn1 = new Pawn(this.player, true);
		this.pawn2 = new Pawn(this.player, false);
	}


	@Test
	public void canMoveTest() throws BoardException {
		// dep 
		Move m1 = new Move('d','2','d','1');
		Move m2 = new Move('d','2','d','3');
		Move m3 = new Move('d','2','d','4');
		Move m4 = new Move('d','3','d','1');
		
		assertEquals(this.pawn1.canMove(m1), false);
		assertEquals(this.pawn1.canMove(m2), true);
		assertEquals(this.pawn1.canMove(m3), true);
		assertEquals(this.pawn2.canMove(m1), true);
		assertEquals(this.pawn2.canMove(m4), true);
		assertEquals(this.pawn2.canMove(m3), false);
	}

}
