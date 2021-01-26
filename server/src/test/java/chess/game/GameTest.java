package chess.game;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class GameTest {
	
	Game g;
	Color c1;
	Color c2;

	@Before
	public void setUp() throws Exception {
		this.c1 = new Color();
		this.c1.Black();
		this.c2 = new Color();
		this.c2.White();
		this.g = new Game(c1, c2);
	}
	
	@Test
	public void testIni() {
		for(int row = 0; row <= 7; row++){
			for(int column = 0; column <= 7 ; column++) {
				assertEquals(this.g.getSquare(column,row).isTaken(),false);
			}
		}
	}
	
	@Test 
	public void testStart() {
		this.g.startGame();
		for(int row = 0; row <= 7; row++){
			for(int column = 0; column <= 7 ; column++) {
				if(row <= 1) {
					assertEquals(this.g.getSquare(column,row).isTaken(),true);
				} else if(row >= 6) {
					assertEquals(this.g.getSquare(column,row).isTaken(),true);
				} else {
					assertEquals(this.g.getSquare(column,row).isTaken(),false);
				}
			}
		}	
	}
	
	@Test 
	public void testClearSquare() {
		this.g.startGame();
		assertEquals(this.g.getSquare(7,1).isTaken(),true);
		this.g.cleanSquare(7, 1);
		assertEquals(this.g.getSquare(7,1).isTaken(),false);
		
	}
	
	
	@Test 
	public void testSetSquare() {
		assertEquals(this.g.getSquare(4,4).isTaken(),false);
		this.g.setSquare(4, 4, new Pawn(this.c1));
		assertEquals(this.g.getSquare(4,4).isTaken(),true);
		
	}
	
	@Test 
	public void testMoveOk() {
		this.g.startGame();
		
		
		// Pawn
		Move m1 = new Move(new Location(3,1), new Location(3,3));
		assertEquals(this.g.moveOk(m1), true);
		
		
		// Rock
		m1 = new Move(new Location(7,0), new Location(7,4));
		assertEquals(this.g.moveOk(m1), false);
		this.g.cleanSquare(7, 1);
		assertEquals(this.g.moveOk(m1), true);
		
		
		// Knight
		m1 = new Move(new Location(6,0), new Location(6,5));
		System.out.print(this.g.getSquare(6, 0).getChessman().getName());
		assertEquals(this.g.moveOk(m1), true);
		
		// Bishop
		m1 = new Move(new Location(5,0), new Location(7,2));
		assertEquals(this.g.moveOk(m1), false);
		this.g.cleanSquare(6, 1);
		assertEquals(this.g.moveOk(m1), true);
		
		// Queen 
		m1 = new Move(new Location(3,0), new Location(3,4));
		assertEquals(this.g.moveOk(m1), false);
		this.g.cleanSquare(3, 1);
		assertEquals(this.g.moveOk(m1), true);
		
		// King
		m1 = new Move(new Location(4,0), new Location(4,1));
		assertEquals(this.g.moveOk(m1), false);
		this.g.cleanSquare(4, 1);
		assertEquals(this.g.moveOk(m1), true);
		
	}
	
	@Test
	public void testMove() {
		this.g.startGame();
		// Pawn
		Move m1 = new Move(new Location(3,1), new Location(3,3));
		assertEquals(this.g.getSquare(3,1).isTaken(),true);
		assertEquals(this.g.getSquare(3,3).isTaken(),false);
		this.g.move(this.c1,m1);
		assertEquals(this.g.getSquare(3,1).isTaken(),false);
		assertEquals(this.g.getSquare(3,3).isTaken(),true);
	}
	
	

}
