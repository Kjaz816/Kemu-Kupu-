package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

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
	
	public void quit(ActionEvent event) {
		stage = (Stage) scenePane.getScene().getWindow();
		stage.close();	
	}
}

