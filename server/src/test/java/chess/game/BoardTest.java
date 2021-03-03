package chess.game;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import chess.player.Player;

public class BoardTest {
	
	Board board;
	Player player1;
	Player player2;

	@Before
	public void setUp() throws Exception {
		this.player1 = new Player(UUID.randomUUID());
		this.player2 = new Player(UUID.randomUUID());
		this.board = new Board(this.player1, this.player2);
	}

	@Test
	public void construtorTest() {
		assertEquals(this.board.getBlack(), this.player2);
		assertEquals(this.board.getWhite(), this.player1);
		for (int letter = 0; letter < this.board.getBoard().length ; letter++) {
			for (int number = 0; number < this.board.getBoard().length; number++) {
				if(number < 2) {
					assertEquals(this.board.getBoard()[letter][number].isTaken(), true);
				} else if(number > 5) {
					assertEquals(this.board.getBoard()[letter][number].isTaken(), true);
				} else assertEquals(this.board.getBoard()[letter][number].isTaken(), false);
			}
		}
	}
	
	@Test
	public void inTheWayTest() throws BoardException {
		Move m1 = new Move('a','1','a','3');
		Move m2 = new Move('a','2','a','3');
		
		assertEquals(this.board.inTheWay(this.board.getBoard()[0][0].getChessman(), m1), this.board.getBoard()[0][1].getChessman());
		assertEquals(this.board.inTheWay(this.board.getBoard()[0][1].getChessman(), m2), null);
	}
	
	@Test
	public void playTest() throws BoardException {
		Move m1 = new Move('b','8','c','6');
		Move m2 = new Move('d','2','d','4');
		Move m3 = new Move('c','6','d','4');
		Move m4 = new Move('a','2','a','3');
		
		assertTrue(this.board.play(this.player2,m1, null).contains("moved"));
		assertTrue(this.board.play(this.player1,m2, null).contains("moved"));
		assertTrue(this.board.play(this.player2,m3, null).contains("fight"));
		boolean t = true;
		while(t) {
			if(this.board.getC1attack().getLive() > 0 && this.board.getC2attack().getLive() > 0 ) {
				assertTrue(this.board.play(this.player2,null, "Torch").contains("impact"));
				assertTrue(this.board.play(this.player1,null, "Hellos").contains("impact"));
			} else if (this.board.getC1attack().getLive() <= 0 || this.board.getC2attack().getLive() <= 0 ){
				t = false;
			}
		}
		
		assertTrue(this.board.play(this.player1, m4, null).contains("moved"));
		
		if(this.board.getC1attack().getLive() <= 0){
			assertEquals(this.board.getBoard()[3][3].getChessman(), this.board.getC2attack());
		} else {
			assertEquals(this.board.getBoard()[3][3].getChessman(), this.board.getC2attack());
		}
		
		
		
	}
}