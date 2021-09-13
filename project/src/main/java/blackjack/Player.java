package blackjack;

public class Player{
	
	//player needs a betting amount
	private int balance;
	
	private String name;
	private CardHand hand;
	
	private boolean aceSwitch;


	public Player(String name, CardHand hand, int balance) {
		if (balance < 0) {
			throw new IllegalArgumentException("Balance cannot be negative");
		}
		this.name = name;
		this.hand = hand;
		this.balance = balance;

		aceSwitch = true; // false = 1, true = 11
	}
	
	public String getName() {
		return name;
	}

	public CardHand getHand() {
		return hand;
	}
	
	public int getBalance() {
		return balance;
	}
	
	public boolean getAceSwitch() {
		return aceSwitch;
	}
	
	public void resetHand() {
		//Resets the hand
		CardHand newHand = new CardHand();
		this.hand = newHand;
	}
	
	public void addCardToHand(Card...card) {
		//delegation
		hand.addCard(card);
	}

	public boolean hasBlackJack() {
		return getHandValue() == 21;
	}
	
	public int getHandValue() {
		int value = 0;
		for (Card card: hand) {
			if (card.getFace() > 10) {
				value += 10;
			} else if (aceSwitch && card.getFace() == 1) {
				value+=11;
			} else {
				value+=card.getFace();
			}
		} 
		return value;
	}
	
	public void setAceSwitch(boolean yes) {
		aceSwitch = yes;
	}
	
	public boolean checkBust() {
		return getHandValue() > 21;
	}
	
	public void bet(int amount) {
		if (amount > balance) {
			throw new IllegalArgumentException(name+" don't have enough money in their bank");
		}
		balance = balance - amount;
	}
	
	public void settleBet(int amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("Amount needs to be above 0");
		}
		
		balance = balance + amount;
	}
	
	@Override
	public String toString() {
		//should return name + balance + aceSwitch + hand
		return name +"\n" +Integer.toString(balance)+"\n"+hand+"\n"+String.valueOf(aceSwitch);
	}
	
}
