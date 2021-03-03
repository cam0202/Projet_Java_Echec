package chess.game.chessman;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import chess.game.BoardException;
import chess.game.Location;
import chess.game.Move;
import chess.player.Player;

public class RookTest {
	
	Player player;
	Rook rook;

	@Before
	public void setUp() throws Exception {
		this.player = new Player(UUID.randomUUID());
		this.rook = new Rook(this.player);
	}

	@Test
	public void canMoveTest() throws BoardException {
		// dep vertical
		Move m1 = new Move('a','1','a','4');
		Move m3 = new Move('a','1','c','1');
		
		// dep non vertical
		Move m2 = new Move('a','1','c','4');
		
		assertEquals(this.rook.canMove(m1), true);
		assertEquals(this.rook.canMove(m2), false);
		assertEquals(this.rook.canMove(m3), true);
	}

}
