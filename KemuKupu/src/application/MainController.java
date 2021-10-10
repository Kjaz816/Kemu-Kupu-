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

public class MainController extends Main {
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
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
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Topic2.fxml"));
			root =  loader.load();
			
			TopicController TopicController = loader.getController();
			TopicController.setTheme(this.theme);
			
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("css/" + theme).toExternalForm());
			
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void switchToTheme(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Theme.fxml"));
			root =  loader.load();
			
			ThemeController ThemeController = loader.getController();
			ThemeController.setTheme(theme);

			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("css/" + theme).toExternalForm());
			stage.setScene(scene);
			stage.show();
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

