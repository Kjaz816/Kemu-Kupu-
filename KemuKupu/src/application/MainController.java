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

public class MainController extends Controller {

	@FXML
	private Button gameButton;
	@FXML
	private Button scoreboardButton;
	@FXML
	private Button quitButton;
	@FXML
	private Label title;

	public void switchToTopic(ActionEvent event) {
		try {
			this.setLoader("fxml/Topic.fxml");
			root =  loader.load();
			TopicController TopicController = loader.getController();
			TopicController.setTheme(theme);
			this.showStage(event);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void switchToTheme(ActionEvent event) {
		try {
			this.setLoader("fxml/Theme.fxml");
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
			this.setLoader("fxml/ScoreBoard.fxml");
			root =  loader.load();
			ScoreBoardController ScoreBoardController = loader.getController();
			ScoreBoardController.setTheme(theme);
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

