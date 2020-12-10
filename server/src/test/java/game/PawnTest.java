package game;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PawnTest {
	
	Pawn p1;
	Color c1;
	Pawn p2;
	Color c2;
	

	@Before
	public void setUp() throws Exception {
		this.c1 = new Color();
		this.c1.Black();
		this.p1 = new Pawn(c1);
		
		this.c2= new Color();
		this.c2.White();
		this.p2 = new Pawn(c2);
	}

	@Test
	public void testGet() {
		assertEquals (this.p1.getName(), "Pawn");
		assertEquals (this.p1.getColor(), "black");
		
		assertEquals (this.p2.getName(), "Pawn");
		assertEquals (this.p2.getColor(), "white");
	}
	
	@Test
	public void testSet() {
		this.p1.setName("P");
		assertEquals (this.p1.getName(), "P");
		
		this.p2.setName("P");
		assertEquals (this.p2.getName(), "P");
	}

	@Test
	public void testMoveP1() {
		Move m1 = new Move(new Location(1,1), new Location(3,3));
		Move m2 = new Move(new Location(1,1), new Location(3,1));
		Move m3 = new Move(new Location(0,1), new Location(0,2));
		Move m4 = new Move(new Location(3,1), new Location(3,2));
		Move m5 = new Move(new Location(3,1), new Location(3,3));
		
		assertEquals (this.p1.isOk(m1), false);
		assertEquals (this.p1.isOk(m2), false);
		assertEquals (this.p1.isOk(m3), true);
		assertEquals (this.p1.isOk(m4), true);
		assertEquals (this.p1.isOk(m5), true);	
	
	}
	
	@Test
	public void testMoveP2() {
		Move m1 = new Move(new Location(3,3), new Location(1,1));
		Move m2 = new Move(new Location(3,1), new Location(1,1));
		Move m3 = new Move(new Location(0,2), new Location(0,1));
		Move m4 = new Move(new Location(3,2), new Location(3,1));
		Move m5 = new Move(new Location(3,3), new Location(3,1));
		
		assertEquals (this.p2.isOk(m1), false);
		assertEquals (this.p2.isOk(m2), false);
		assertEquals (this.p2.isOk(m3), true);
		assertEquals (this.p2.isOk(m4), true);
		assertEquals (this.p2.isOk(m5), true);
			
	}
}
