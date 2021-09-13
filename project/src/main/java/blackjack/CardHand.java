package blackjack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class CardHand implements Iterable<Card>{
	private List<Card> cardHand;
	
	public CardHand(Card...card) {
		cardHand = new ArrayList<>();
		addCard(card);
	}
	
	public CardHand() {
		cardHand = new ArrayList<>();
	}
	
    public Card getCard(int n) {
        try {
            return cardHand.get(n);
        }
        catch (Exception e) {
            throw new IllegalArgumentException("This card does not exist");
        }
    }
    
	public int getCardCount() {
		return cardHand.size();
	}
	
	public void addCard(Card...card) {
		List<Card> cardList = Arrays.asList(card);
		for (Card c : cardHand) {
			for (int i = 0; i<cardList.size(); i++) {
				if (c.toString().equals(cardList.get(i).toString())) {
					throw new IllegalArgumentException("This card is already in your hand");
				}
			}
		}
		//need to make sure that there are no duplicate cards
		cardList.stream().forEach(c -> cardHand.add(c));
	}
	
	
	public Iterator<Card> iterator() {
		return new CardHandIterator(this);
	}
	
	public List<Card> returnAsList(){
		//Used for tests
		List<Card> newList = cardHand;
		return newList;
	}
	
	@Override
	public String toString() {
		String str = "";
		for (int i = 0; i<cardHand.size(); i++) {
			str += cardHand.get(i)+" ";
		}
		return str;
	}
	
}