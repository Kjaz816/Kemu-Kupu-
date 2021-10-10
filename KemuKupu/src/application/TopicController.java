package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

//This class controls the choose topic screen

public class TopicController implements Initializable {
	
	@FXML
	private Button playButton;
	
	@FXML
	private Button practiceButton;
	
	@FXML
	private Label topicLabel;
	
	@FXML
	private ListView<String> topicList;
	
	private String topic;
	
	private String theme = "default.css";
	
	public void setTheme(String theme) {
		this.theme = theme;
	}
	
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
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	public void practice(ActionEvent event) throws IOException { // Method which controls the play button 
		if (!topicLabel.getText().equals("Choose a topic:")) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Practice.fxml"));
			root = loader.load();
		
			PracticeController PracticeController = loader.getController();
			PracticeController.setTopic(topic);
			PracticeController.setTheme(this.theme);
			// Starts a new game
		
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("css/" + theme).toExternalForm());
			stage.setScene(scene);
			stage.show();
			// Sets the scene to the new game scene
		}
	}
	
	public void play(ActionEvent event) throws IOException { // Method which controls the play button 
		if (!topicLabel.getText().equals("Choose a topic:")) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Play.fxml"));
			root = loader.load();
		
			PlayController PlayController = loader.getController();
			PlayController.setTopic(topic);
			PlayController.setTheme(theme);
			// Starts a new game
		
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("css/" + theme).toExternalForm());
			stage.setScene(scene);
			stage.show();
			// Sets the scene to the new game scene
		}
	}
	
}