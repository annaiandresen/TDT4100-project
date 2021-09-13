package blackjack;

import java.io.FileNotFoundException;
import java.util.Arrays;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class GameController {

	// Game Objects
	private BlackJack game;

	// SaveHandler
	private BlackJackSaveHandler saveHandler;

	// PauseTransition for pop-up text
	private PauseTransition visiblePause;

	@FXML
	Button hitButton, standButton, newGameButton, saveButton, betButton, loadButton, dealerButton, nextRoundButton,
			settleButton;

	@FXML
	Text fileNotFoundMessage, currentBet, gameOverText, youWonText;

	@FXML
	Label playerMoneyLabel, roundLabel;

	@FXML
	TextField betTextField;

	@FXML
	Pane gameBoard;

	@FXML
	ImageView playerCardImage[], dealerCardImage[];

	@FXML
	FlowPane playerFlowPane, dealerFlowPane;

	@FXML
	public void initialize() {
		/**
		 * This function is called whenever the game is started,
		 */

		// initialize game variables
		Player player = new Player("Player", new CardHand(), 500);
		Player dealer = new Player("Dealer", new CardHand(), 0);

		game = new BlackJack(player, dealer, new CardDeck());
		saveHandler = new BlackJackSaveHandler();
		visiblePause = new PauseTransition(Duration.seconds(2));

		// Set text
		gameOverText.setVisible(false);
		youWonText.setVisible(false);
		fileNotFoundMessage.setVisible(false);

		// Show cards
		dealerFlowPane.setVisible(true);
		playerFlowPane.setVisible(true);

		playerCardImage = new ImageView[12];
		dealerCardImage = new ImageView[12];

		// Set Labels
		playerMoneyLabel.setText("NOK " + String.valueOf(game.getPlayer().getBalance()));
		roundLabel.setText("Round " + game.getRound());

		// Buttons
		Arrays.asList(hitButton, standButton, dealerButton, settleButton).stream().forEach(b -> b.setDisable(true));
		nextRoundButton.setVisible(false);
		saveButton.setDisable(false);

		betTextField.requestFocus();

		// Shows cards on the board
		updatePlayerFlowPane();
		updateDealerFlowPane(true);

	}

	@FXML
	void btnHit(ActionEvent click) {
		/**
		 * Calls the hit() method from game if the player hasn't busted. If the player
		 * has busts, settleGame() is called.
		 */
		try {
			if (!game.getPlayer().checkBust()) {
				game.hit();
				updatePlayerFlowPane();

				if (game.getPlayer().checkBust()) {
					hitButton.setDisable(true);
					standButton.setDisable(true);
					settleGame();

					if (!game.getGameOver())
						nextRoundButton.setVisible(true);
				}
			}
		} catch (IllegalStateException e) {
			gameOverText.setText("Game is over, cannot hit");
			popUpText(gameOverText);
		} catch (IllegalArgumentException e) {
			gameOverText.setText("Player cannot hit when their card value is above 21");
			popUpText(gameOverText);
		}
	}

	@FXML
	void btnDealer(ActionEvent click) {
		/**
		 * Dealer should only hit if the player hasn't busted this is a button that must
		 * be clicked until dealer's hand is >= 17
		 */

		try {
			if (!game.getPlayer().checkBust()) {
				game.dealerPlay();
				updateDealerFlowPane(false);
			}

			if (game.getDealer().getHandValue() >= 17) {
				dealerButton.setDisable(true);
				settleButton.setDisable(false);
			}
		} catch (IllegalStateException e) {
			gameOverText.setText("Game is over, start new game");
			popUpText(gameOverText);
		}
	}

	@FXML
	void btnStand(ActionEvent click) {
		/**
		 * Should prompt the player to click the dealerButton
		 */
		dealerButton.setDisable(false);

		// disable buttons until round ends
		Arrays.asList(standButton, hitButton).stream().forEach(b -> b.setDisable(true));
	}

	@FXML
	void btnSave(ActionEvent click) {
		try {
			saveHandler.save(getFilename(), game);
			fileNotFoundMessage.setText("Game was saved!");
			popUpText(fileNotFoundMessage);
		} catch (FileNotFoundException e) {
			fileNotFoundMessage.setText("ERROR: File couldn't be saved");
			popUpText(fileNotFoundMessage);
		}
	}

	@FXML
	void btnLoad(ActionEvent click) {
		try {
			game = saveHandler.load(getFilename());
			fileNotFoundMessage.setText("Game loaded");
			popUpText(fileNotFoundMessage);
		} catch (FileNotFoundException e) {
			popUpText(fileNotFoundMessage);

		}

		loadGame();
	}

	@FXML
	void btnBet(ActionEvent click) {
		try {
			game.placeBet(Integer.parseInt(betTextField.getText()));
			playerMoneyLabel.setText("NOK" + String.valueOf(game.getPlayer().getBalance()));
			betTextField.setDisable(true);
			betButton.setDisable(true);
			hitButton.setDisable(false);
			standButton.setDisable(false);

		} catch (NumberFormatException e) {
			// invalid input, prompts the player to try again
			betTextField.setText("Invalid Bet");
			betTextField.selectAll();
			betTextField.requestFocus();

		} catch (IllegalArgumentException e) {
			// if input > players balance
			betTextField.setText("Insufficient balance");
			betTextField.selectAll();
			betTextField.requestFocus();
		} catch (IllegalStateException e) {
			// if gameOver = true
			gameOverText.setText("Game is over, cannot bet. Start new game");
			popUpText(gameOverText);
		}
	}

	@FXML
	void btnNewGame(ActionEvent click) {
		initialize();
		cleanFlowPanes();

		betTextField.setDisable(false);
		betTextField.requestFocus();
		betButton.setDisable(false);
	}

	@FXML
	void btnSettleGame(ActionEvent click) {
		settleButton.setDisable(true);
		settleGame();
		if (!game.getGameOver()) {
			nextRoundButton.setVisible(true);
		}
	}

	@FXML
	void btnNextRound(ActionEvent click) {
		nextRound();
		nextRoundButton.setVisible(false);
		cleanFlowPanes();
	}

	private void settleGame() {
		try {
			game.settleGame();
			playerMoneyLabel.setText("NOK " + String.valueOf(game.getPlayer().getBalance()));
			if (game.getPlayer().getBalance() == 0) {
				game.setGameOver();
				checkWhoWon();
				Arrays.asList(hitButton, standButton, saveButton, betButton).stream().forEach(b -> b.setDisable(true));
				betTextField.setDisable(true);
			} else {
				checkWhoWon();

			}
		} catch (IllegalStateException e) {
			gameOverText.setText("Pot hasn't been initialized, restart game");
			popUpText(gameOverText);

		}

	}

	private void cleanFlowPanes() {
		/**
		 * Cleans flow panes, initializes them again whenever the board needs to be
		 * wiped
		 */
		Arrays.asList(playerFlowPane, dealerFlowPane).stream().forEach(p -> p.getChildren().clear());
		dealerFlowPane.setVisible(true);
		playerFlowPane.setVisible(true);

		playerCardImage = new ImageView[12];
		dealerCardImage = new ImageView[12];
		updatePlayerFlowPane();
		updateDealerFlowPane(true);
	}

	private void nextRound() {
		try {
			game.resetRound();
			betTextField.setDisable(false);
			betTextField.requestFocus();

			roundLabel.setText("Round " + game.getRound());
			betButton.setDisable(false);
		} catch (IllegalStateException e) {
			gameOverText.setText("Game is over, restart game");
			popUpText(gameOverText);
		} catch (IllegalArgumentException e) {
			// if player receives duplicates of a card, which shouldn't happen
			gameOverText.setText("Something went wrong, restart game");
			popUpText(gameOverText);
		}
	}

	private void loadGame() {
		cleanFlowPanes();
		betTextField.setDisable(false);
		betTextField.requestFocus();
		roundLabel.setText("Round " + game.getRound());
		betButton.setDisable(false);
	}

	private void updateDealerFlowPane(boolean displayBack) {
		/**
		 * Adds cards from the dealer's hand to the flowpane. Code inspired by Github
		 * user Nalabrie
		 */

		// Displays card back rather than the first card
		if (displayBack) {
			Image back = new Image(this.getClass().getResource("cards/back.png").toString());
			dealerCardImage[0] = new ImageView();
			dealerCardImage[0].setImage(back);
			dealerCardImage[0].setPreserveRatio(true);
			dealerCardImage[0].setSmooth(true);
			dealerCardImage[0].setCache(true);
			dealerCardImage[0].setFitHeight(120);
			dealerFlowPane.getChildren().add(dealerCardImage[0]);
		}

		else if (dealerCardImage[0] != null) {
			dealerFlowPane.getChildren().clear();
			dealerCardImage = new ImageView[12];
		}

		for (int i = 0; i < game.getDealer().getHand().getCardCount(); i++) {
			if (dealerCardImage[i] == null) {
				String path = this.getClass().getResource("cards/" + game.getDealer().getHand().getCard(i) + ".png")
						.toString();
				Image card = new Image(path);
				dealerCardImage[i] = new ImageView();
				dealerCardImage[i].setImage(card);
				dealerCardImage[i].setSmooth(true);
				dealerCardImage[i].setPreserveRatio(true);
				dealerCardImage[i].setCache(true);
				dealerCardImage[i].setFitHeight(120);
				dealerFlowPane.getChildren().add(dealerCardImage[i]);
			}
		}
	}

	private void updatePlayerFlowPane() {
		/**
		 * Adds cards from the player's hand to the flowpane Code inspired by Github
		 * user Nalabrie
		 */
		playerMoneyLabel.setText("NOK" + String.valueOf(game.getPlayer().getBalance()));
		for (int i = 0; i < game.getPlayer().getHand().getCardCount(); i++) {
			if (playerCardImage[i] == null) {

				String path = this.getClass().getResource("cards/" + game.getPlayer().getHand().getCard(i) + ".png")
						.toString();
				Image card = new Image(path);
				playerCardImage[i] = new ImageView();
				playerCardImage[i].setImage(card);
				playerCardImage[i].setSmooth(true);
				playerCardImage[i].setPreserveRatio(true);
				playerCardImage[i].setCache(true);
				playerCardImage[i].setFitHeight(120);
				playerFlowPane.getChildren().add(playerCardImage[i]);
			}
		}
	}

	private void checkWhoWon() {
		// Displays appropriate text
		if (game.getGameOver()) {
			gameOverText.setText("Game Over!");
			popUpText(gameOverText);
		} else if (game.compare(game.getDealer(), game.getPlayer()) < 0) {
			youWonText.setText("You won!");
			popUpText(youWonText);
		} else if (game.compare(game.getDealer(), game.getPlayer()) > 0) {
			youWonText.setText("You Lost!");
			popUpText(youWonText);
		} else if (game.compare(game.getDealer(), game.getPlayer()) == 0) {
			youWonText.setText("It's a draw!");
			popUpText(youWonText);
		}
	}

	private void popUpText(Text text) {
		// text pops up and is displayed for 2 sec before disappearing
		text.setVisible(true);
		visiblePause.setOnFinished(event -> text.setVisible(false));
		visiblePause.play();
	}

	private String getFilename() {
		return "save_file";
	}

}
