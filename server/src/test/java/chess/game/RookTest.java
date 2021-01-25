package chess.game;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class RookTest {

	Rook r1;
	Color c1;
	Rook k2;
	Color r2;

	@Before
	public void setUp() throws Exception {
		this.c1 = new Color();
		this.c1.Black();
		this.r1 = new Rook(c1);
		
		this.r2 = new Color();
		this.r2.White();
		this.k2 = new Rook(r2);
	}

	
	@Test
	public void testMoveQ1() {
		Move m1 = new Move(new Location(5,5), new Location(1,4));
		Move m2 = new Move(new Location(5,5), new Location(5,1));
		
		assertEquals (this.r1.isOk(m1), false);
		assertEquals (this.r1.isOk(m2), true);
	}
	
	@Test
	public void testMoveQ2() {
		Move m1 = new Move(new Location(5,5), new Location(1,4));
		Move m2 = new Move(new Location(5,5), new Location(5,1));
		
		assertEquals (this.k2.isOk(m1), false);
		assertEquals (this.k2.isOk(m2), true);
	}

}
