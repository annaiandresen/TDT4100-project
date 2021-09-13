package blackjack;

import java.util.Iterator;

public class CardHandIterator implements Iterator<Card> {
	private CardHand hand;
	private int index;
	
	
	public CardHandIterator(CardHand hand) {
		this.hand = hand;
		index = 0;
	}
	
	
	@Override
	public boolean hasNext() {
		//returns true whenever there is another card in the list.
		return index < hand.getCardCount();
	}

	@Override
	public Card next() {
		return hand.getCard(index++);
	}

}
