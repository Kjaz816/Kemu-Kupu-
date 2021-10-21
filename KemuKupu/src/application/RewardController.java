package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

// This class controls the rewards screen after the user finishes a quiz

public class RewardController {

	@FXML
	private Label rewardLabel;
	@FXML
	private Button playAgainButton;
	@FXML
	private Button PracticeAgainButton;
	@FXML
	private Button mainMenuButton;
	@FXML
	private Label topicLabel;
	@FXML
	private Label timeLabel;
	@FXML
	private Label scoreLabel;

	private Stage stage;
	private Scene scene;
	private Parent root;

	private String topic;

	private ArrayList<String> masteredTopics = new ArrayList<String>();

	@FXML
	private ListView<String> scoreBoard;

	private double secElapsed;

	private String theme = "default.css";

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public void practiceAgain(ActionEvent event) { // Method that controls the "Play again" button
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Practice2.fxml"));
			root = loader.load();

			PracticeController PracticeController = loader.getController();
			PracticeController.setTopic(topic);
			PracticeController.setTheme(theme);
			// Starts a new game

			setStage(event);
			// Sets the scene to the new game scene

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void playAgain(ActionEvent event) { // Method that controls the "Play again" button
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Play2.fxml"));
			root = loader.load();

			PlayController PlayController = loader.getController();
			PlayController.setTopic(topic);
			PlayController.setTheme(theme);

			// Starts a new game

			setStage(event);
			// Sets the scene to the new game scene

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setTopic(String topic) { // Method that sets the topic for the next quiz
		// Inputs:
		// topic = the topic chosen by the user
		this.topic = topic;
		// Gets the topic chosen by the user and sets it to the topic variable
		topicLabel.setText("Topic: " + topic);
		// Sets the topicLabel to display the current topic
	}

	public void setTimeElapsed(long nanoTimeElapsed) {
		int secondsElapsed = (int) TimeUnit.SECONDS.convert(nanoTimeElapsed, TimeUnit.NANOSECONDS);
		int minutesElapsed = secondsElapsed / 60;
		secondsElapsed = secondsElapsed % 60;
		timeLabel.setText(" Time Elapsed: " + String.format("%02d:%02d", minutesElapsed, secondsElapsed));
	}

	public void returnToMainMenu(ActionEvent event) { // Method that controls the "Return to Main Menu" button
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Main2.fxml"));
			root = loader.load();

			MainController MainController = loader.getController();
			MainController.setTheme(theme);

			setStage(event);
			// Returns the scene to the default scene

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setScore(Score Score) { // Method to display the score to the screen.
		int score = Score.getScore();
		scoreLabel.setText("Score: " + score);

		// Sets the rewardLabel text that displays to the user after a game based on how
		// well they did
		if (score < 3) {
			rewardLabel.setText("Good try, you scored " + score + ". Play more to master your spelling!");
		} else if (Score.getScore() == 3) {
			rewardLabel.setText(
					"Not bad! You scored " + score + "! A little more practise and you could get a perfect score!");
		} else {
			rewardLabel.setText("Congratulations! You scored " + score + "! Well done");
		}
	}

	public void setScoreBoard(Score Score) { // Method that displays words in the quiz to the screen.
		scoreBoard.getItems().addAll(Score.getWords());
	}

	public void addMastered(Score Score, String topic) {
		if (Score.getScore() == 5 || secElapsed <= 60.00) {
			try {
				this.getMastered();
				if (!masteredTopics.contains(topic)) {
					String command = "echo '" + topic + "' >> data.txt";
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
						}
					} else {
						String line;
						while ((line = stderr.readLine()) != null) {
							System.err.println(line);
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void setStage(ActionEvent event) throws IOException {
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("css/" + theme).toExternalForm());
		stage.setScene(scene);
		stage.show();
		// Sets the scene to the new game scene
	}

	public void getMastered() throws IOException {
		File data = new File("data.txt");
		// Checks if the data.txt
		if (data.exists()) {
			// Writes each line in the file to a new element on the current stat array that
			// corresponds to its name
			BufferedReader in = new BufferedReader(new FileReader(data));
			String line;
			while ((line = in.readLine()) != null) {
				masteredTopics.add(line);
			}
			in.close();
		}
	}

}