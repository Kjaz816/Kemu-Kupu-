package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

// This class controls the rewards screen after the user finishes a quiz

public class RewardController extends controller {

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

	private String topic;

	@FXML
	private ListView<String> scoreBoard;
	
	@FXML
	private TableView<Score> resultTable;

	private double secElapsed;

	public void practiceAgain(ActionEvent event) { // Method that controls the "Play again" button
		try {
			this.setLoader("Practice2.fxml");
			root = loader.load();

			PracticeController PracticeController = loader.getController();
			PracticeController.setTopic(topic);
			PracticeController.setTheme(theme);
			// Starts a new game

			this.showStage(event);
			// Sets the scene to the new game scene

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void playAgain(ActionEvent event) { // Method that controls the "Play again" button
		try {
			this.setLoader("Play2.fxml");
			root = loader.load();

			PlayController PlayController = loader.getController();
			PlayController.setTopic(topic);
			PlayController.setTheme(theme);

			// Starts a new game
			this.showStage(event);
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
		int secondsElapsed = (int) TimeUnit.SECONDS.convert(nanoTimeElapsed,TimeUnit.NANOSECONDS);
		int minutesElapsed = secondsElapsed/60;
		secondsElapsed = secondsElapsed%60;
		timeLabel.setText(" Time Elapsed: " + String.format("%02d:%02d", minutesElapsed,secondsElapsed));
	}

	public void returnToMainMenu(ActionEvent event) { // Method that controls the "Return to Main Menu" button
		try {
			this.setLoader("Main2.fxml");
			root = loader.load();

			MainController MainController = loader.getController();
			MainController.setTheme(theme);

			this.showStage(event);
			// Returns the scene to the default scene

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setScore(Score Score) { //Method to display the score to the screen. 
		int score = Score.getScore();
		scoreLabel.setText("Score: " + score);

		// Sets the rewardLabel text that displays to the user after a game based on how well they did
		if (score < 3) {
			rewardLabel.setText("Good try, you scored " + score + ". Play more to master your spelling!");
		} else if (Score.getScore() == 3) {
			rewardLabel.setText("Not bad! You scored " + score + "! A little more practise and you could get a perfect score!");
		} else {
			rewardLabel.setText("Congratulations! You scored " + score + "! Well done");
		}
	}

	public void addMastered(Score Score, String topic) {
		if (Score.getScore() == 5 || secElapsed <= 60.00) {
			try {
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


			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setUpResults(ObservableList<Score> tableData) {
		// set up table
		resultTable.setEditable(false);
		resultTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		// create columns
		TableColumn<Score, String> word = new TableColumn<>("Word");
		word.setCellValueFactory(new PropertyValueFactory<>("word"));
		TableColumn<Score, String> answer = new TableColumn<>("You Spelled");
		answer.setCellValueFactory(new PropertyValueFactory<>("answer"));

		word.setSortable(false);
		answer.setSortable(false);
		word.setReorderable(false);
		answer.setReorderable(false);

		// add data to table
		resultTable.setItems(tableData);
		resultTable.getColumns().addAll(Arrays.asList(word, answer));

		// color based on isCorrect

	}

}