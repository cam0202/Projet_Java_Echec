package game;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class QueenTest {
	
	Queen q1;
	Color c1;
	Queen q2;
	Color c2;

	@Before
	public void setUp() throws Exception {
		this.c1 = new Color();
		this.c1.Black();
		this.q1 = new Queen(c1);
		
		this.c2 = new Color();
		this.c2.White();
		this.q2 = new Queen(c2);
	}

	
	@Test
	public void testMoveQ1() {
		Move m1 = new Move(new Location(5,7), new Location(7,5));
		Move m2 = new Move(new Location(4,7), new Location(4,0));
		Move m3 = new Move(new Location(4,7), new Location(0,3));
		
		assertEquals (this.q1.isOk(m1), true);
		assertEquals (this.q1.isOk(m2), true);
		assertEquals (this.q1.isOk(m3), true);
	}
	
	@Test
	public void testMoveQ2() {
		Move m1 = new Move(new Location(5,7), new Location(7,5));
		Move m2 = new Move(new Location(4,7), new Location(4,0));
		Move m3 = new Move(new Location(4,7), new Location(0,3));
		
		assertEquals (this.q2.isOk(m1), true);
		assertEquals (this.q2.isOk(m2), true);
		assertEquals (this.q2.isOk(m3), true);
	}
	

}
