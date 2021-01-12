package game;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class LocationTest {

	Location p; 
	@Before
	public void setUp() {
		this.p = new Location(1,1);
	}
	
	@Test
	public void testLocVal() {
		assertEquals (this.p.getRow(), 1);
		assertEquals (this.p.getColumn(), 1);
	}
	
	@Test
	public void testChangeLoc() {
		this.p.setColumn(3);
		this.p.setRow(2);
		
		assertEquals (this.p.getRow(), 2);
		assertEquals (this.p.getColumn(), 3);
	}

}
