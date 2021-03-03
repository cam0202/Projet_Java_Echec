package chess.game.chessman;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import chess.player.Player;

public class AttackTest {
	
	Player player1;
	Bishop bishop;
	
	Player player2;
	Rook rook;

	@Before
	public void setUp() throws Exception {
		this.player1 = new Player(UUID.randomUUID());
		this.bishop = new Bishop(this.player1);
		
		this.player2 = new Player(UUID.randomUUID());
		this.rook = new Rook(this.player2);
	}

	@Test
	public void getNameTest() {
		assertEquals(this.bishop.getAttacks()[0].getName(), "Baffle");
		assertEquals(this.bishop.getAttacks()[1].getName(), "Crush");
		assertEquals(this.bishop.getAttacks()[2].getName(), "Bloom");
	}
	
	@Test
	public void getValue() {
		assertEquals(this.bishop.getAttacks()[0].getValue(),0);
		this.bishop.getAttacks()[0].setValue(rook);
		assertTrue(this.bishop.getAttacks()[0].getValue() > 0);
		assertTrue(this.bishop.getAttacks()[1].getValue() == 0);
		
	}

}
