package chess.game;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import chess.game.chessman.Bishop;
import chess.player.Player;

public class SquareTest {
	
	Player player;
	Bishop bishop;
	Square square;

	@Before
	public void setUp() throws Exception {
		this.player = new Player(UUID.randomUUID());
		this.bishop = new Bishop(this.player);
		
		this.square = new Square(this.bishop);
	}

	@Test
	public void getChessmanTest() {
		assertEquals(this.square.getChessman(), this.bishop);
	}
	
	@Test
	public void setChessmanTest() {
		this.square = new Square(null);
		assertEquals(this.square.getChessman(), null);
		this.square.setChessman(this.bishop);
		assertEquals(this.square.getChessman(), this.bishop);
	}
	
	@Test
	public void clearTest() {
		this.square.clear();
		assertEquals(this.square.getChessman(), null);
	}
	
	@Test
	public void isTakenTest() {
		assertEquals(this.square.isTaken(), true);
		this.square.clear();
		assertEquals(this.square.isTaken(), false);
	}

}
