package blackjack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class BlackJackSaveHandler implements SaveHandler{
	
	final static String folder = "src/main/resources/blackjack/";
	

	@Override
	public void save(String filename, BlackJack game) throws FileNotFoundException {
		/**
		 * Writes player, dealer and round to .txt-file
		 */
		try (PrintWriter writer = new PrintWriter(getFilePath(filename))) {
			writer.println(game.getPlayer()+"\n");
			writer.println(game.getDealer()+"\n");
			writer.println(game.getRound());
			//Don't need to close writer when it is inside a try/catch
		}
		
	}

	@Override
	public BlackJack load(String filename) throws FileNotFoundException {
		try (Scanner scanner = new Scanner(new File(getFilePath(filename)))){
			
			//player
			String name = scanner.next();
			int balance = scanner.nextInt();
			String card1 = scanner.next();
			String card2 = scanner.next();
			boolean aceSwitch = scanner.nextBoolean();
			
			Card firstCard = convertFromString(card1);
			Card secondCard = convertFromString(card2);
			
			CardHand hand = new CardHand(firstCard, secondCard);
			
			Player player = new Player(name, hand, balance);
			player.setAceSwitch(aceSwitch);
			
			
			//dealer
			String dealerName = scanner.next();
			int dealerBalance = scanner.nextInt();
			String dealerCard1 = scanner.next();
			String dealerCard2 = scanner.next();
			boolean dealerAceSwitch = scanner.nextBoolean();
			
			Card dealerFirst = convertFromString(dealerCard1);
			Card dealerSecond = convertFromString(dealerCard2);
			CardHand dealerHand = new CardHand(dealerFirst, dealerSecond);
			Player dealer = new Player(dealerName, dealerHand, dealerBalance);
			dealer.setAceSwitch(dealerAceSwitch);
			
			//creates a new deck without the cards on hand
			CardDeck deck = new CardDeck(firstCard, secondCard, dealerFirst, dealerSecond);
			
			//round
			int round = scanner.nextInt();
			return new BlackJack(player, dealer, deck, round);

		}
	}

	@Override
	public String getFilePath(String filename) {
		return folder + filename + ".txt";
	}
	
	private Card convertFromString(String card) {
		//should add a try catch
		char suit = card.charAt(0);
		if (card.length() == 2) {
			String face = Character.toString(card.charAt(1));
			int faceInt = Integer.parseInt(face);
			return new Card(suit, faceInt);
		
		} else if (card.length() == 3) {
			String face1 = Character.toString(card.charAt(1));
			String face2 = Character.toString(card.charAt(2));
			int face = Integer.parseInt(face1+face2);
			return new Card(suit, face);
		}
		
		//shouldn't get this far
		return null;
	}

}
