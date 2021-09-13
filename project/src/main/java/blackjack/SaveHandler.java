package blackjack;

import java.io.FileNotFoundException;

public interface SaveHandler {
	
	void save(String filename, BlackJack game) throws FileNotFoundException;
	
	BlackJack load(String filename) throws FileNotFoundException;
	
	String getFilePath(String filename);
}
