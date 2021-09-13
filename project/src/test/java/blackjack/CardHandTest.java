package blackjack;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CardHandTest {
	
	private CardHand hand;
	private Card s1;
	private Card h10;
	private Card c9;
	
	
	@BeforeEach
	public void setUp() {
		s1 = new Card('S', 1);
		h10 = new Card('H', 10);
		c9 = new Card('C', 9);
		hand = new CardHand();
		
	}
	
	@Test
	public void testConstructor() {
		
		CardHand hand2 = new CardHand(s1, h10);
		System.out.println(hand2);
		assertFalse(hand2.toString().equals(hand.toString()));
		assertEquals(hand2.getCardCount(), 2);
		assertTrue(hand2.returnAsList().stream().anyMatch(card -> card.toString().equals(s1.toString())));
	}
	
	@Test
	public void testAddCard() {
		hand.addCard(s1);
		
		assertEquals(s1, hand.getCard(0));
		hand.addCard(h10, c9);
		
		String mockHand = s1.toString()+" "+h10.toString()+" "+c9.toString()+" ";
		assertEquals(mockHand, hand.toString());
		assertEquals(3, hand.getCardCount());
		
		//test duplicate cards
		assertThrows(IllegalArgumentException.class,
				() -> hand.addCard(s1)
				);
	}
	
	@Test
	public void testGetCard() {
		assertThrows(IllegalArgumentException.class, 
				() -> hand.getCard(0)
				);
		hand.addCard(s1);
		assertEquals(s1, hand.getCard(0));
	}
}
