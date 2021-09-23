package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class MainController {
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML
	private Button gameButton;

	@FXML
	private Button quitButton;
	
	@FXML 
	private AnchorPane scenePane;
	
	public void switchToTopic(ActionEvent event) {
		try {
			root = FXMLLoader.load(getClass().getResource("Topic.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void quit(ActionEvent event) {
		stage = (Stage) scenePane.getScene().getWindow();
		stage.close();	
	}
}

