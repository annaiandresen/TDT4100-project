package blackjack;

import java.util.Arrays;
import java.util.List;

public class Card{
	private char suit;
	private int face;
	public static final List<Character> suits = Arrays.asList(
			'S', 'H', 'D', 'C');
	
	public Card(char suit, int face) {
		if (face<1 || face>13) {
			throw new IllegalArgumentException("Invalid face value");
		} else if (!suits.contains(suit)) {
			throw new IllegalArgumentException("Invalid suit");
		}
		this.suit = suit;
		this.face = face;
	}

	public char getSuit() {
		return suit;
	}

	public int getFace() {
		return face;
	}
	
	@Override
	public String toString() {
		String number = Integer.toString(face);
		String suit = String.valueOf(this.suit);
		return suit+number;
	} 
	


}