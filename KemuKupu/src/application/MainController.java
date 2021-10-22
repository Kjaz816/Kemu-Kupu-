package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class MainController extends controller {

	@FXML
	private Button gameButton;
	
	@FXML
	private Button quitButton;

	@FXML 
	private BorderPane scenePane;

	@FXML
	private Label title;

	public void switchToTopic(ActionEvent event) {
		try {
			this.setLoader("Topic2.fxml");
			root =  loader.load();

			TopicController TopicController = loader.getController();
			TopicController.setTheme(this.theme);
			// Sets the theme of the topic controller to the currently selected theme

			this.showStage(event);
			// Sets the theme of the application to the selected one

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void switchToTheme(ActionEvent event) {
		try {
			this.setLoader("Theme.fxml");
			root =  loader.load();

			ThemeController ThemeController = loader.getController();
			ThemeController.setTheme(theme);
			
			this.showStage(event);
			// Sets the theme of the application to the selected one

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void switchToScoreBoard(ActionEvent event) {
		try {
			this.setLoader("scoreBoard.fxml");
			root =  loader.load();

			ThemeController ThemeController = loader.getController();
			ThemeController.setTheme(theme);

			this.showStage(event);
			// Sets the theme of the application to the selected one

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void quit(ActionEvent event) { // Method that controls the Quit Button
		stage = (Stage) scenePane.getScene().getWindow();
		// Gets the current scene
		stage.close(); 
		// closes the current scene
	}
}

