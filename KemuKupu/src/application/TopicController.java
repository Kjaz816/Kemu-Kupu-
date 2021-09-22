package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class TopicController implements Initializable {
	
	@FXML
	private Button playButton;
	
	@FXML
	private Label topicLabel;
	
	@FXML
	private ListView<String> topicList;
	
	private String topic;
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		//this.sortUniq();
		topicList.getSelectionModel().selectedItemProperty().addListener((ChangeListener<? super String>) new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				// TODO Auto-generated method stub
				topic = topicList.getSelectionModel().getSelectedItem();
				topicLabel.setText(topic);
			}
			
		});
	}
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	public void play(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("newSpellingQuiz.fxml"));
		root = loader.load();
		
		//newSpellingQuizController newSpellingQuizController = loader.getController();
		//newSpellingQuizController.newSpellingQuiz();
		
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
}
