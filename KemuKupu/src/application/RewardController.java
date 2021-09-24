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
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	public void playAgain(ActionEvent event) {
		try {
			root = FXMLLoader.load(getClass().getResource("Topic.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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