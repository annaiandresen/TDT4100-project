package blackjack;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BlackJackTest {

	private BlackJack blackJack;
	private CardDeck deck;

	private Player dealer;
	private Player player;

	private CardHand playerHand;
	private CardHand dealerHand;

	Card s1 = new Card('S', 1);
	Card d6 = new Card('D', 6);
	Card h11 = new Card('H', 11);
	Card c1 = new Card('C', 1);

	@BeforeEach
	public void setUp() {
		c1 = new Card('C', 1);
		h11 = new Card('H', 11);
		d6 = new Card('D', 6);
		s1 = new Card('S', 1);

		playerHand = new CardHand(s1, h11);
		dealerHand = new CardHand(d6, c1);

		deck = new CardDeck();

		player = new Player("Player", playerHand, 500);
		dealer = new Player("Dealer", dealerHand, 0);

		blackJack = new BlackJack(player, dealer, deck);
	}

	@Test
	public void testConstructor() {
		assertEquals(1, blackJack.getRound());
		assertEquals(player, blackJack.getPlayer());
		assertEquals(dealer, blackJack.getDealer());

		BlackJack newGame = new BlackJack(player, dealer, deck);
		assertEquals(newGame.toString(), blackJack.toString());

		BlackJack testGame = new BlackJack(player, dealer, deck, 7);
		assertEquals(7, testGame.getRound());
	}

	@Test
	public void testHit() {
		BlackJack testGame = new BlackJack(player, dealer, deck, 7);
		testGame.setGameOver();
		assertThrows(IllegalStateException.class, () -> testGame.hit());

		Card s10 = new Card('S', 10);
		Player newPlayer = new Player("test", new CardHand(h11, d6, s10), 500);
		CardDeck testDeck = new CardDeck(h11, d6, s10, s1, c1);
		BlackJack testGame2 = new BlackJack(newPlayer, dealer, testDeck);
		assertThrows(IllegalArgumentException.class, () -> testGame2.hit());

		assertTrue(blackJack.getPlayer().getAceSwitch());
		blackJack.hit(); // might fail gotta check
		assertFalse(blackJack.getPlayer().getAceSwitch());
		assertEquals(3, player.getHand().getCardCount());

	}

	@Test
	public void testResetRound() {
		blackJack.placeBet(200);
		assertEquals(200, blackJack.getPot());
		assertNotNull(blackJack.getPlayer().getHand());

		Player newPlayer = new Player("test", new CardHand(s1, h11), 500);
		BlackJack newGame = new BlackJack(newPlayer, dealer, deck);
		blackJack.resetRound();
		assertEquals(2, blackJack.getRound());
		assertEquals(0, blackJack.getPot());

		// the test below might fail if the player is accidentally given the same cards
		assertTrue(
				Collections.disjoint(blackJack.getPlayer().getHand().returnAsList(),
						newGame.getPlayer().getHand().returnAsList()),
				"c1: " + blackJack.getPlayer().getHand().toString() + "\nc2: "
						+ newGame.getPlayer().getHand().toString());
	}

	@Test
	public void testPlaceBet() {
		BlackJack newGame = new BlackJack(player, dealer, deck);
		newGame.setGameOver();
		assertThrows(IllegalStateException.class, () -> newGame.placeBet(200));
		assertThrows(IllegalArgumentException.class, () -> blackJack.placeBet(900));
		blackJack.placeBet(400);
		assertEquals(100, player.getBalance());
		assertEquals(400, blackJack.getPot());

	}

	@Test
	public void testDealerPlay() {
		Card h5 = new Card('H', 5);
		Player testDealer = new Player("Dealer", new CardHand(h5, h11), 0);
		BlackJack newGame = new BlackJack(player, testDealer, deck);
		newGame.setGameOver();
		assertThrows(IllegalStateException.class, () -> newGame.dealerPlay());
		Player sameDealer = new Player("Dealer", new CardHand(d6, c1), 0);

		blackJack.dealerPlay(); // nothing should happen as dealer's hand is 17
		assertEquals(sameDealer.getHand().toString(), blackJack.getDealer().getHand().toString());

		Player testDealer2 = new Player("Dealer", new CardHand(h5, c1), 0);
		BlackJack testGame2 = new BlackJack(player, testDealer2, deck);

		testGame2.dealerPlay();
		assertEquals(3, testGame2.getDealer().getHand().getCardCount());
	}

	@Test
	public void testSetAceValue() {
		assertEquals(21, blackJack.getPlayer().getHandValue());
		assertTrue(blackJack.getPlayer().getAceSwitch());

		blackJack.getPlayer().addCardToHand(d6);
		blackJack.setAceValue(player);
		assertFalse(blackJack.getPlayer().getAceSwitch());
		assertEquals(17, blackJack.getPlayer().getHandValue());
	}

	@Test
	public void testSettleGame() {
		blackJack.placeBet(500);
		blackJack.settleGame();
		assertEquals(750, blackJack.getPlayer().getBalance());

		Player newPlayer = new Player("Player1", new CardHand(d6, c1), 500);
		BlackJack newGame = new BlackJack(newPlayer, dealer, deck);

		assertThrows(IllegalStateException.class, () -> newGame.settleGame());
		newGame.placeBet(500);
		newGame.settleGame();
		assertEquals(500, newGame.getPot());
		assertEquals(500, newGame.getPlayer().getBalance());

	}

	@Test
	public void testCompareCards() {
		Card h5 = new Card('H', 5);
		Player test = new Player("test", new CardHand(h5, d6), 500);
		Player test2 = new Player("test2", new CardHand(c1, d6), 500);
		assertEquals(-1, blackJack.compare(dealer, player));
		assertEquals(1, blackJack.compare(dealer, test));
		assertEquals(0, blackJack.compare(dealer, test2));

		blackJack.getPlayer().addCardToHand(d6);
		assertEquals(1, blackJack.compare(dealer, player));
		
		Card c10 = new Card('C', 10);
		Card s9 = new Card('S', 9);
		Card d1 = new Card('D', 1);
		Card d9 = new Card('D', 9);
		
		Player dealer2 = new Player("Dealer2", new CardHand(c10, s9), 0);
		Player player2 = new Player("Player2", new CardHand(d1, d9), 500);
		BlackJack game2 = new BlackJack(player2, dealer2, new CardDeck(c10, s9, d1, d9));
		assertEquals(-1, game2.compare(dealer2, player2));
	}
}
