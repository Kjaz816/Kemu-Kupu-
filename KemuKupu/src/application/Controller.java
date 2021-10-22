package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Controller extends Main {
	
	protected Stage stage;
	protected Scene scene;
	protected Parent root;
	
	protected FXMLLoader loader;
	
	public void setLoader(String fxml) {
		loader = new FXMLLoader(getClass().getResource(fxml));
	}
	
	public void showStage(ActionEvent buttonEvent) {
		stage = (Stage) ((Node) buttonEvent.getSource()).getScene().getWindow();
		scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("css/" + theme).toExternalForm());		
		stage.setScene(scene);
		stage.show();
		
		stage.setOnCloseRequest(event -> {
			event.consume();
			logout(stage);	
		});
	}
}