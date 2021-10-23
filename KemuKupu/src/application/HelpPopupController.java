package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class HelpPopupController {

	@FXML
	private Button ok;
	@FXML
	private GridPane scenePane;
	
	private Stage stage;
	
	
	public void ok(ActionEvent event) { // Method that controls the ok Button
		stage = (Stage) scenePane.getScene().getWindow();
		// Gets the current scene
		stage.close(); 
		// closes the current scene
	}
	
	
}
