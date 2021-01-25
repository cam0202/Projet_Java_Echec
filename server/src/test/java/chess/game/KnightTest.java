package chess.game;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class KnightTest {

	Knight k1;
	Color c1;
	Knight k2;
	Color c2;

	@Before
	public void setUp() throws Exception {
		this.c1 = new Color();
		this.c1.Black();
		this.k1 = new Knight(c1);
		
		this.c2 = new Color();
		this.c2.White();
		this.k2 = new Knight(c2);
	}

	
	@Test
	public void testMoveQ1() {
		Move m1 = new Move(new Location(3,5), new Location(5,4));
		Move m2 = new Move(new Location(3,6), new Location(2,4));
		
		assertEquals (this.k1.isOk(m1), true);
		assertEquals (this.k1.isOk(m2), true);
	}
	
	@Test
	public void testMoveQ2() {
		Move m1 = new Move(new Location(3,5), new Location(5,4));
		Move m2 = new Move(new Location(3,6), new Location(2,4));
		
		assertEquals (this.k2.isOk(m1), true);
		assertEquals (this.k2.isOk(m2), true);
	}
}
