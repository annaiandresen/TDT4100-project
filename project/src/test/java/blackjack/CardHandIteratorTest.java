package blackjack;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Iterator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CardHandIteratorTest {
	private CardHand hand;
	private Card s1, h10, c9;
	private CardHandIterator iterator;
	
	
	@BeforeEach
	public void setUp() {
		s1 = new Card('S', 1);
		h10 = new Card('H', 10);
		c9 = new Card('C', 9);
		hand = new CardHand(s1, c9, h10);
		iterator = new CardHandIterator(hand);
		
	}
	
	@Test
	public void testConstructor() {
		Iterator<Card> expected = Arrays.asList(s1, c9, h10).iterator();
		for (int i = 0; i<hand.getCardCount(); i++) {
			assertEquals(expected.next().toString(), iterator.next().toString());
		}
	}
	
	@Test 
	public void testHasNext() {
		assertTrue(iterator.hasNext());
		iterator.next();
		iterator.next();
		iterator.next();
		assertFalse(iterator.hasNext());
		
	}
	
	@Test 
	public void testNext() {
		assertEquals(s1.toString(), iterator.next().toString());
		
	}
	
	
}
