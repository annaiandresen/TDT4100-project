package blackjack;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;

public class BlackJackSaveHandlerTest {
	
	// Game Objects
	private BlackJack game;
	
	//SaveHandler
	private BlackJackSaveHandler saveHandler;
	
	@BeforeEach
	public void setUp() {
		createGame();
	}
	
	private void createGame() {
		CardHand playerHand = new CardHand(new Card('S', 1), new Card('H', 10));
		CardHand dealerHand = new CardHand(new Card('D', 5), new Card('C', 13));
		
		Player player = new Player("Player", playerHand, 700);
		Player dealer = new Player("Dealer", dealerHand, 50);
		
		CardDeck deck = new CardDeck(
				new Card('S', 1), 
				new Card('H', 10), 
				new Card('D', 5), 
				new Card('C', 13));
		
		game = new BlackJack(player, dealer, deck, 5);
		saveHandler = new BlackJackSaveHandler();
	}
	
	@Test
	public void testSave() {
		try {
			saveHandler.save("test_save", game);
		} catch (FileNotFoundException e) {
			fail("could not save file");
		}
		
		byte[] test = null, newFile = null;
		
		try {
			test = Files.readAllBytes(Path.of("src/main/resources/blackjack/test_save.txt"));
		} catch (IOException e) {
			fail("Test file couldn't load");
		}
		
		try {
			newFile = Files.readAllBytes(Path.of("src/main/resources/blackjack/test_save.txt"));
		} catch(IOException e) {
			fail("Could not load new file");
		}
		
		assertNotNull(test);
		assertNotNull(newFile);
		assertTrue(Arrays.equals(test, newFile));
	}

		
	@Test
	public void testLoad() {
		BlackJack newGame;
		
		try {
			newGame = saveHandler.load("test_save");
		} catch (FileNotFoundException e) {
			fail("Could not load file");
			return;
		}
		assertEquals(game.toString(), newGame.toString());
	}
	
	@Test
	public void testLoadFileDoesNotExist() {
		assertThrows(FileNotFoundException.class,
				() -> game = saveHandler.load("nonexistantFile")
				);
		
	}
	
	/*
	@AfterAll
	public static void eraseSaves() {
		//delete saves
		File testSave = new File("src/main/resources/blackjack/test_save.txt");
		testSave.delete();
	}*/
	
	
	
}
