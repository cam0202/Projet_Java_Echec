package game;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BishopTest {

	Bishop b1;
	Color c1;
	Bishop k2;
	Color b2;

	@Before
	public void setUp() throws Exception {
		this.c1 = new Color();
		this.c1.Black();
		this.b1 = new Bishop(c1);
		
		this.b2 = new Color();
		this.b2.White();
		this.k2 = new Bishop(b2);
	}

	
	@Test
	public void testMoveQ1() {
		Move m1 = new Move(new Location(1,1), new Location(3,3));
		Move m2 = new Move(new Location(1,1), new Location(2,1));
		
		assertEquals (this.b1.isOk(m1), true);
		assertEquals (this.b1.isOk(m2), false);
	}
	
	@Test
	public void testMoveQ2() {
		Move m1 = new Move(new Location(1,4), new Location(3,6));
		Move m2 = new Move(new Location(2,2), new Location(1,4));
		
		assertEquals (this.k2.isOk(m1), true);
		assertEquals (this.k2.isOk(m2), false);
	}

}
