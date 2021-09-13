package blackjack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CardDeck{
	private List<Card> cardDeck;

	
	public CardDeck(Card...card) {
		/**
		 * creates a deck without the cards specified
		 */
		List<Card> cardsInPlay = Arrays.asList(card);
		cardDeck = new ArrayList<>();
		for (char suit : Card.suits) {
			for (int i = 1; i < 14; i++) {
				Card newCard = new Card(suit, i);
				if(!cardsInPlay.stream().anyMatch(c -> c.toString().equals(newCard.toString()))) {
					cardDeck.add(newCard);
				}
			}
		}
	}
	
	public CardDeck() {
		cardDeck = new ArrayList<>();
		for (char suit : Card.suits) {
			for (int i = 1; i < 14; i++) {
				cardDeck.add(new Card(suit, i));
			}
		}
	}
	
	public List<Card> getDeckAsList(){
		//used for tests
		List<Card> newList = cardDeck;
		return newList;
	}
	

	// Getters
	public Card getCard(int position) {
		if (position < 0 || position > cardDeck.size()) {
			throw new IllegalArgumentException("Invalid position");
		}
		return cardDeck.get(position);
	}

	public int getCardCount() {
		return cardDeck.size();
	}

	public void shuffle() {
		Collections.shuffle(cardDeck);
	}

	public void deal(CardHand hand, int n) {
		int count = this.getCardCount();
		for (int i = count - 1; i > count - n - 1; i--) {
			hand.addCard(this.getCard(i));
			this.cardDeck.remove(this.getCard(i));
		}
	}
	

	@Override
	public String toString() {
		String str = "";
		for (int i = 0; i < cardDeck.size(); i++) {
			str += cardDeck.get(i) + " ";
		}
		return str;
	}

}