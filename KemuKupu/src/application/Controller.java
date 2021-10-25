package application;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Controller extends Main {
	
	protected Stage stage;
	protected Scene scene;
	protected Parent root;
	
	protected FXMLLoader loader;
	
	@FXML
	protected BorderPane scenePane;
	
	public void setLoader(String fxml) {
		loader = new FXMLLoader(getClass().getResource(fxml));
	}
	
	public void showStage(ActionEvent buttonEvent) {
		stage = (Stage) ((Node) buttonEvent.getSource()).getScene().getWindow();
		scene = new Scene(root,scenePane.getWidth(),scenePane.getHeight());
		scene.getStylesheets().add(getClass().getResource("css/" + theme).toExternalForm());		
		stage.setScene(scene);
		stage.show();
		// Method to show the selected stage
		
		stage.setOnCloseRequest(event -> {
			event.consume();
			logout(stage);	
		});
	}
	
	@FXML
	protected Button home;
	@FXML
	protected Button help;
	
	public void home(ActionEvent event) throws IOException {
		this.setLoader("fxml/Main.fxml");
		root = loader.load();
		MainController MainController = loader.getController();
		MainController.setTheme(theme);
		this.showStage(event);
		// Controls the button that returns the user to the main menu
	}
	
	public void help(ActionEvent event) throws IOException {
		loader = new FXMLLoader(getClass().getResource("fxml/Help.fxml"));
		root = (Parent) loader.load();
		scene = new Scene(root);
		stage = new Stage();
		stage.setResizable(false);
		stage.setScene(scene);
		// Controls the button that shows the user help messages.

		// disable underlying window
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
	}
}