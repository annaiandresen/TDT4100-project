package blackjack;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Game.fxml"));
        
        primaryStage.setTitle("Blackjack");
        primaryStage.setScene(new Scene(root));
        
        primaryStage.getScene().getStylesheets().add(this.getClass().getResource("style.css").toString());
        primaryStage.show();
  
	}
	
    public static void main(String[] args) {
        launch(args);
    }

}
