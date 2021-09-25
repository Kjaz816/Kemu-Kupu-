package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class RewardController {

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
	
	public void playAgain(ActionEvent event) {
		try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("Play.fxml"));
				root = loader.load();
				
				PlayController PlayController = loader.getController();
				PlayController.randWord(topic);
				PlayController.newWord();
				PlayController.setTopic(topic);
				
				stage = (Stage)((Node)event.getSource()).getScene().getWindow();
				scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setTopic(String topic) {
		this.topic = topic;
		topicLabel.setText("Topic: " + topic);
	}
	
	public void returnToMainMenu(ActionEvent event) {
		try {
			root = FXMLLoader.load(getClass().getResource("Main.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setScored(int score) {
		rewardLabel.setText("Congratulations! You scored " + score + "!");
	}
	
}