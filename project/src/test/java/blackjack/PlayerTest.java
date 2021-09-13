package blackjack;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class PlayerTest {

	private Player player2;
	private Player player;	
	private Player dealer;
	
	private CardHand dealerHand;
	
	private Card s1;
	private Card h10;
	private Card c9;
	private Card d12;
	private Card d1;
	
	@BeforeEach
	public void setUp() {
		s1 = new Card('S', 1);
		h10 = new Card('H', 10);
		c9 = new Card('C', 9);
		d12 = new Card('D', 12);
		d1 = new Card('D', 1);
		CardHand player2Hand = new CardHand(s1, h10, d12);
		CardHand playerHand = new CardHand(c9, d1);
		
		player = new Player("Player", playerHand, 500);
		player2 = new Player("Player2", player2Hand, 0);
	}
	
	@Test
	public void testConstructor() {
		assertThrows(IllegalArgumentException.class,
				() -> dealer = new Player("Test", new CardHand(), -45)
				);
		dealerHand= new CardHand(s1, h10, d12);
		dealer = new Player("Dealer", dealerHand,0);
		assertTrue(dealer.getName().equals("Dealer"));
		assertEquals(0, dealer.getBalance());
		
		CardHand hand = new CardHand(s1, h10, d12);
		assertEquals(hand.toString(), dealer.getHand().toString());
	}
	
	@Test
	public void testResetHand() {
		CardHand emptyHand = new CardHand();
		player.resetHand();
		assertEquals(emptyHand.toString(), player.getHand().toString());
	}
	
	@Test
	@DisplayName("Tests checkBlackJack, Player's hand value and ace switch")
	public void testGetHandValue() {
		assertEquals(20, player.getHandValue());
		assertTrue(player.getAceSwitch());
		
		Player testPlayer = new Player("test", new CardHand(s1, h10, d1), 500);
		assertEquals(32, testPlayer.getHandValue());
		testPlayer.setAceSwitch(false);
		assertEquals(12, testPlayer.getHandValue());
		
		assertFalse(player2.hasBlackJack());
		player2.addCardToHand(c9);
		assertTrue(player2.checkBust());
		
		Player blackJackPlayer = new Player("test2", new CardHand(s1, h10), 500);
		assertTrue(blackJackPlayer.hasBlackJack());
		
	}
	
	@Test
	public void testBet() {
		assertThrows(IllegalArgumentException.class,
				() -> player.bet(700)
				);
		player.bet(400);
		assertEquals(100, player.getBalance());
		
	}
	
	@Test
	public void settleBet() {
		assertThrows(IllegalArgumentException.class,
				() -> player.settleBet(-4)
				);
		player.settleBet(50);
		assertEquals(550, player.getBalance());
	}
}
