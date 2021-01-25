package chess.game;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ColorTest extends Color {

	Color c; 
	@Before
	public void setUp() {
		this.c = new Color();
	}
	
	@Test
	public void testInit() {
		assertEquals(this.c.getColor(), null);
	}
	
	@Test
	public void testChooseColor() {
		this.c.Black();
		assertEquals(this.c.getColor(), "black");
		this.c.White();
		assertEquals(this.c.getColor(), "white");
		this.c.Blue();
		assertEquals(this.c.getColor(), "blue");
		this.c.Green();
		assertEquals(this.c.getColor(), "green");
		this.c.Orange();
		assertEquals(this.c.getColor(), "orange");
		this.c.Red();
		assertEquals(this.c.getColor(), "red");
		this.c.Violet();
		assertEquals(this.c.getColor(), "violet");
		this.c.Yellow();
		assertEquals(this.c.getColor(), "yellow");
	}

}
