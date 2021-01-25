package chess.game;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SquareTest {
	
	Square s1;
	Square s2;
	Square s3;
	King k;
	Color c;

	@Before
	public void setUp() throws Exception {
		this.c = new Color();
		this.c.Black();
		this.k = new King(c);
		this.s1 = new Square(k);
		this.s2 = new Square();
		this.s3 = new Square();
		this.s3.setChessman(k);
	}

	@Test
	public void testS2() {
		
		assertEquals(this.s2.getChessman(), null);
		assertEquals(this.s2.isTaken(), false);
		assertEquals(this.s2.isTaken("black"), false);
	}
	
	@Test
	public void testS1() {
		
		assertEquals(this.s1.getChessman(), k);
		assertEquals(this.s1.isTaken(), true);
		assertEquals(this.s1.isTaken("black"), true);
		assertEquals(this.s1.isTaken("white"), false);
	}
	
	@Test
	public void testS3() {
		
		assertEquals(this.s3.getChessman(), k);
		assertEquals(this.s3.isTaken(), true);
	}

}
