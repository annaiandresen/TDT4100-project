package blackjack;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class BlackJack implements Comparator<Player> {

	private Player player;
	private Player dealer;
	private CardDeck deck;
	
	private boolean gameOver;
	
	private int pot;
	private int round;


	public BlackJack(Player player, Player dealer, CardDeck deck) {
		this.player = player;
		this.dealer = dealer;
		this.deck = deck;
		deck.shuffle();
		
		if (player.getHand().getCardCount() == 0) {
			deck.deal(player.getHand(), 2);
			deck.deal(dealer.getHand(), 2);
		}
		
		Arrays.asList(dealer, player).stream().forEach(p -> setAceValue(p));
		round = 1;
		
		gameOver = false;
	}
	
	public BlackJack(Player player, Player dealer, CardDeck deck, int round) {
		this.player = player;
		this.dealer = dealer;
		this.deck = deck;
		deck.shuffle();
		
		if (player.getHand().getCardCount() == 0 && dealer.getHand().getCardCount() == 0) {
			deck.deal(player.getHand(), 2);
			deck.deal(dealer.getHand(), 2);
		}
		this.round = round;
		
		Arrays.asList(dealer, player).stream().forEach(p -> setAceValue(p));
		
		gameOver = false;
	}

	// Getters

	public Player getPlayer() {
		return player;
	}

	public Player getDealer() {
		return dealer;
	}

	public int getPot() {
		return pot;
	}

	public int getRound() {
		return round;
	}
	
	public boolean getGameOver() {
		return gameOver;
	}
	
	//Aetters
	public void setGameOver() {
		gameOver = true;
	}

	public void hit() {
		/**
		 * The player hits whenever they want another card in their hand. 
		 * The player cannot hit when the card value is above 21
		 */
		checkGameOver();
		if (player.checkBust()) {
			throw new IllegalArgumentException("Player cannot hit when their card value is above 21");
		}
		deck.deal(player.getHand(), 1);
		setAceValue(player);
	}

	public void settleGame() {
		/**
		 * Compares the dealer's and player's card value
		 */
		if (pot <= 0) {
			throw new IllegalStateException("Pot is 0");
		}
		if (compare(dealer, player) > 0) {
			System.out.println("Dealer won!");
			dealer.settleBet((int) (pot * 3 / 2));
		} else if (compare(dealer, player) < 0) {
			System.out.println("Player won!");
			player.settleBet((int) (pot * 3 / 2));
		} else if (compare(dealer, player) == 0) {
			System.out.println("Dealer and player have equal hands");
			player.settleBet(pot);
		}
	}

	public void resetRound() {
		/**
		 * Resets the dealer's and player's hand and pot
		 * Makes a new deck, shuffles, then deals 2 cards
		 */
		checkGameOver();
		List<Player> list = Arrays.asList(dealer, player);
		list.stream().forEach(p -> p.resetHand());
		pot = 0;
		deck = new CardDeck();
		deck.shuffle();
		list.stream().forEach(p -> deck.deal(p.getHand(), 2));
		list.stream().forEach(p -> setAceValue(p));
		round++;
	}


	public void placeBet(int amount) {
		/**
		 * Let the player place their bet 
		 * They will first see their first two cards
		 * Happens at the start of each round
		 */
		checkGameOver();
		player.bet(amount);
		pot = amount;
	}

	public void dealerPlay() {
		/**
		 * The dealer plays until 16 and stands at 17
		 */
		if (dealer.getHandValue() < 17) {
			checkGameOver();
			deck.deal(dealer.getHand(), 1);
			setAceValue(dealer);
		}
	}

	private boolean checkIfAce(CardHand hand) {
		for (Card card : hand) {
			if (card.getFace() == 1) {
				return true;
			}
		}
		return false;
	}

	public void setAceValue(Player player) {
		/**
		 * Sets ace value automatically to 11 if possible, else to 1. 
		 */
		if (checkIfAce(player.getHand())) {
			player.setAceSwitch(true);
			if (player.checkBust() && !player.hasBlackJack()) {
				player.setAceSwitch(false);
			}
		}
	}
	
	private void checkGameOver() {
		if (gameOver) {
			throw new IllegalStateException("Game is over!");
		}
	}

	@Override
	public int compare(Player dealer, Player player) {
		/**
		 * Compares the player and dealer's hand values. 
		 */
		if (dealer.checkBust() && player.checkBust()) {
			return 0;
		} else if (player.checkBust()) {
			return 1;
		} else if (dealer.checkBust())
			return -1;

		if (dealer.getHandValue() == player.getHandValue()) {
			return 0;
		} else if (dealer.getHandValue() < player.getHandValue()) {
			return -1;
		} else if (dealer.getHandValue() > player.getHandValue()) {
			return 1;
		}
		//Shouldn't get this far
		return 0;
	}
	
	@Override
	public String toString() {
		return player.toString()+"\n"+dealer.toString()+"\n"+round;
	}
}
