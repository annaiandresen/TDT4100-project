package blackjack;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CardDeckTest {
	
	private CardDeck deck;
	
	@BeforeEach
	public void setUp() {
		deck = new CardDeck();
	}
	
	@Test
	public void testConstructor() {
		Card s1 = new Card('S', 1);
		Card h11 = new Card('H', 11);
		
		CardDeck filteredDeck = new CardDeck(s1, h11);
		assertFalse(deck.toString().equals(filteredDeck.toString()));
		assertEquals(50, filteredDeck.getCardCount());
		assertFalse(filteredDeck.getDeckAsList().stream()
				.anyMatch(c -> c.toString().equals(s1.toString()) || c.toString().equals(h11.toString())));
		
		//regular deck
		assertEquals(52, deck.getCardCount());
		assertEquals(s1.toString(), deck.getCard(0).toString());
	}
	
	
	@Test
	public void testGetCard() {
		assertThrows(IllegalArgumentException.class,
				() -> deck.getCard(76)
				);
		assertThrows(IllegalArgumentException.class,
				() -> deck.getCard(-1)
				);
	}
	
	@Test
	public void testGetCardCount() {
		assertEquals(52, deck.getCardCount());
	}
	
	@Test
	public void testShuffle() {
		CardDeck shuffledDeck = new CardDeck();
		shuffledDeck.shuffle();
		
		assertNotEquals(deck.toString(), shuffledDeck.toString());
	}
	
	@Test
	public void testDeal() {
		CardHand hand = new CardHand();
		deck.deal(hand, 2);
		assertEquals(hand.getCardCount(), 2);
		assertEquals(deck.getCardCount(), 50);
	}

}
