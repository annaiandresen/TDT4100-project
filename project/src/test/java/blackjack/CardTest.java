package blackjack;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class CardTest {

	private boolean checkSuitAndFace(Card card, char suit, int face) {
		return card.getSuit() == suit && card.getFace() == face;
	}
	
	@Test
	public void testConstructor() {
		assertThrows(IllegalArgumentException.class, ()-> new Card('F', 1));
		assertThrows(IllegalArgumentException.class, ()-> new Card('D', 16));
		
		assertTrue(checkSuitAndFace(new Card('S', 1), 'S', 1));
		assertTrue(checkSuitAndFace(new Card('H', 11), 'H', 11));
	}
	
	@Test
	public void testToString() {
		Card s1 = new Card('S', 1);
		Card h11 = new Card('H', 11);
		assertEquals("S1", s1.toString());
		assertEquals("H11", h11.toString());
	}

}
