package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

// This class controls the rewards screen after the user finishes a quiz

public class RewardController implements Initializable {

	@FXML
	private Label rewardLabel;
	@FXML
	private Button againButton;
	@FXML
	private Button mainMenuButton;
	@FXML
	private Label topicLabel;
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	private String topic;
	@FXML
	private ListView<String> scoreBoard;
	
	public void playAgain(ActionEvent event) { // Method that controls the "Play again" button
		try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("Play.fxml"));
				root = loader.load();
				
				PlayController PlayController = loader.getController();
				PlayController.setTopic(topic);
				// Starts a new game
				
				stage = (Stage)((Node)event.getSource()).getScene().getWindow();
				scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
				// Sets the scene to the new game scene
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setTopic(String topic) { // Method that sets the topic for the next quiz
		// Inputs:
		// topic = the topic chosen by the user
		this.topic = topic;
		// Gets the topic chosen by the user and sets it to the topic variable
		topicLabel.setText("Topic: " + topic);
		// Sets the topicLabel to display the current topic
	}
	
	public void returnToMainMenu(ActionEvent event) { // Method that controls the "Return to Main Menu" button
		try {
			root = FXMLLoader.load(getClass().getResource("Main.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
			// Returns the scene to the default scene
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setScored(Score Score) { // Method that dispays the user's score to the screen
		// Inputs:
		scoreBoard.getItems().addAll(Score.getTime());
		// score = the user's score from the last game
        if (Score.getScore() < 3) {
            rewardLabel.setText("Good try, you scored " + Score.getScore() + ". Play more to master your spelling!");
        } else if (Score.getScore() == 3) {
            rewardLabel.setText("Not bad! You scored " + Score.getScore() + "! A little more practise and you could get a perfect score!");
        } else {
            rewardLabel.setText("Congratulations! You scored " + Score.getScore() + "! Well done");
        }
        // Sets the rewardLabel text that displays to the user after a game based on how well they did

}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
}