package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ThemeController extends Controller implements Initializable {

	@FXML
	private Label myLabel;

	@FXML
	private ChoiceBox<String> myChoiceBox;

	private String chosenTheme;

	private List<String> data = new ArrayList<>();

	@FXML
	ImageView myImageView;

	@FXML
	private Button applyButton;

	@FXML
	private Button mainMenuButton;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.getTopic();
		this.getData();
		myChoiceBox.setOnAction(this::changeTheme);
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public void getTopic(){
		try {
			
			myChoiceBox.getItems().add("Default");
			
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
					myChoiceBox.getItems().add(line);
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

	public void getData(){
		try {
			data.add("Default");
			String command = "cat .theme.txt";
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
					// Adds the current line to the topic list 
					data.add(line);
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

	public void changeTheme (ActionEvent event) {

		String newTheme = myChoiceBox.getValue();
		chosenTheme = newTheme;
		//show picture
		Image myImage = new Image(getClass().getResourceAsStream("./css/" + newTheme + ".jpg"));
		myImageView.setImage(myImage);
		// Changes the background image

		if (data.contains(newTheme)) {
			myLabel.setText("Able to set " + newTheme.toLowerCase() + " theme");
		}
		else {
			myLabel.setText("Master " + newTheme.toLowerCase() + " to unlock this theme!");
		}
	}

	public void apply(ActionEvent event) throws IOException { // Method that controls the "Return to Main Menu" button
		if (data.contains(chosenTheme)){
			theme = chosenTheme + ".css";
			this.overwrite(theme, ".startTheme.txt");
			super.home(event);
		}
	}
	
	public void overwrite(String line, String file) {
		try {
			String command = "echo '" + line + "' > " + file;
			// Sets the bash command
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
			Process process = pb.start();
			// Creates a process with the bash command and starts it
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}