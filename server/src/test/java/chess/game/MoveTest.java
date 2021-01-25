package chess.game;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MoveTest {
	
	Move m;
	Location start;
	Location end;

	@Before
	public void setUp() throws Exception {
		this.start = new Location(1,1);
		this.end = new Location(1,1);
		this.m = new Move(start, end);
	}

	@Test
	public void testGetLocationX() {
		assertEquals ((int)this.m.getLocationX(), 0);
	}
	
	@Test
	public void testGetLocationY() {
		assertEquals ((int)this.m.getLocationY(), 0);
	}
	
	@Test
	public void testGetStart() {
		assertEquals (this.m.getStart(), this.start);
	}
	
	@Test
	public void testGetEnd() {
		assertEquals (this.m.getEnd(), this.end);
	}
	
	@Test
	public void testIsNul() {
		assertEquals (this.m.isNul(), true);
		Move m2 = new Move(this.start, new Location(1,3));
		assertEquals (m2.isNul(), false);
		m2 = new Move(this.start, new Location(3,1));
		assertEquals (m2.isNul(), false);
	}

}
