package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
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

//This class controls the choose topic screen

public class TopicController extends Controller implements Initializable {

	private String topic;
	@FXML
	private Button playButton;
	@FXML
	private Button practiceButton;
	@FXML
	protected Label topicLabel;
	@FXML
	private ListView<String> topicList;

	public void getTopic(){
		try {
			String command = "ls -1 words | sed -e 's/\\.txt$//'";
			// Sets the bash command
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
			Process process = pb.start();
			// Creates a process with the bash command and starts it
			BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			// Pipelines the stdout and stderror to the variables
			int exitStatus = process.waitFor();
			if (exitStatus == 0) {
				String line;
				while ((line = stdout.readLine()) != null) {
					topicList.getItems().add(line);
					// Adds the current line to the topic list 
				}
			} else {
				String line;
				while ((line = stderr.readLine()) != null) {
					System.err.println(line);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) { // Method which controls the list that the user chooses a topic from
		this.getTopic();
		topicList.getSelectionModel().selectedItemProperty().addListener((ChangeListener<? super String>) new ChangeListener<String>() { // Sets the list to include all elements in the topic list	
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				topic = topicList.getSelectionModel().getSelectedItem();
				// Sets the topic to the topic that the user selected from the list
				topicLabel.setText(topic);
				// Sets the topicLabel to the current topic
			}
		});
	}

	public void practice(ActionEvent event) throws IOException { // Method which controls the play button 
		if (!topicLabel.getText().equals("Choose a topic:")) {
			this.setLoader("Practice2.fxml");
			root = loader.load();
			PracticeController PracticeController = loader.getController();
			PracticeController.setTheme(theme);
			PracticeController.setUp(topic);
			this.showStage(event);
		}
	}

	public void play(ActionEvent event) throws IOException { // Method which controls the play button 
		if (!topicLabel.getText().equals("Choose a topic:")) {
			this.setLoader("Play2.fxml");
			root = loader.load();
			this.showStage(event);
			PlayController PlayController = loader.getController();
			PlayController.setTheme(theme);
			PlayController.setUp(topic);
		}
	}

}