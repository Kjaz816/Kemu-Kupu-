package application;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

// This class controls the rewards screen after the user finishes a quiz

public class RewardController {

	@FXML
	private Label rewardLabel;
	@FXML
	private Button againButton;
	@FXML
	private Button mainMenuButton;
	@FXML
	private Label topicLabel;
	@FXML
	private Label timeLabel;
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	private String topic;
	
	public void playAgain(ActionEvent event) { // Method that controls the "Play again" button
		try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("Play.fxml"));
				root = loader.load();
				
				PlayController PlayController = loader.getController();
				PlayController.randWord(topic);
				PlayController.newWord();
				PlayController.setTopic(topic);
				PlayController.defaultWordLabel(PlayController.getWord());
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
	
	public void setTimeElapsed(long nanoTimeElapsed) {
		double millisecElapsed = (double) TimeUnit.MILLISECONDS.convert(nanoTimeElapsed,TimeUnit.NANOSECONDS);
		double secElapsed = millisecElapsed/1000.0;
		secElapsed = Math.round(secElapsed*100.0)/100.0;
		timeLabel.setText("Time Elapsed: " + secElapsed + " seconds");
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
	
	public void setScored(int score) { // Method that dispays the user's score to the screen
		// Inputs:
		// score = the user's score from the last game
        if (score < 3) {
            rewardLabel.setText("Good try, you scored " + score + ". Play more to master your spelling!");
        } else if (score == 3) {
            rewardLabel.setText("Not bad! You scored " + score + "! A little more practise and you could get a perfect score!");
        } else {
            rewardLabel.setText("Congratulations! You scored " + score + "! Well done");
        }
        // Sets the rewardLabel text that displays to the user after a game based on how well they did

}
	
}