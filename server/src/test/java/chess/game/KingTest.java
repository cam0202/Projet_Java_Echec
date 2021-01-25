package chess.game;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class KingTest {

	King k1;
	Color c1;
	King k2;
	Color c2;

	@Before
	public void setUp() throws Exception {
		this.c1 = new Color();
		this.c1.Black();
		this.k1 = new King(c1);
		
		this.c2 = new Color();
		this.c2.White();
		this.k2 = new King(c2);
	}

	
	@Test
	public void testMoveQ1() {
		Move m1 = new Move(new Location(3,4), new Location(4,5));
		Move m2 = new Move(new Location(3,2), new Location(6,2));
		
		assertEquals (this.k1.isOk(m1), true);
		assertEquals (this.k1.isOk(m2), false);
	}
	
	@Test
	public void testMoveQ2() {
		Move m1 = new Move(new Location(4,5), new Location(3,4));
		Move m2 = new Move(new Location(6,2), new Location(3,2));
		
		assertEquals (this.k2.isOk(m1), true);
		assertEquals (this.k2.isOk(m2), false);
	}

}
