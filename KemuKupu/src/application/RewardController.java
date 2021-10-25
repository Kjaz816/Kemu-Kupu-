package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class RewardController extends Controller {

	private String topic;
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
	@FXML
	private TableView<Word> resultTable;
	
	private ArrayList<String> masteredTopics = new ArrayList<String> ();
	
	public void practiceAgain(ActionEvent event) { // Method that controls the "Play again" button
		try {
			this.setLoader("fxml/Practice.fxml");
			root = loader.load();
			PracticeController PracticeController = loader.getController();
			PracticeController.setTheme(theme);
			PracticeController.setUp(topic);
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
			this.setLoader("fxml/Play.fxml");
			root = loader.load();
			PlayController PlayController = loader.getController();
			PlayController.setTheme(theme);
			PlayController.setUp(topic);
			// Starts a new game
			this.showStage(event);
			// Sets the scene to the new game scene
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setTimeElapsed(long nanoTimeElapsed) {
		int secondsElapsed = (int) TimeUnit.SECONDS.convert(nanoTimeElapsed, TimeUnit.NANOSECONDS);
		int minutesElapsed = secondsElapsed / 60;
		secondsElapsed = secondsElapsed % 60;
		timeLabel.setText(" Time Elapsed: " + String.format("%02d:%02d", minutesElapsed, secondsElapsed));
	}

	public void returnToMainMenu(ActionEvent event) { // Method that controls the "Return to Main Menu" button
		try {
			this.setLoader("fxml/Main.fxml");
			root = loader.load();
			MainController MainController = loader.getController();
			MainController.setTheme(theme);
			this.showStage(event);
			// Returns the scene to the default scene
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showScore(Score Score) { // Method to display the score to the screen.
		int score = Score.getScore();
		scoreLabel.setText("Score: " + score);
		if (score <= 300) {
			rewardLabel.setText("Good try, you scored " + score + ". Play more to master your spelling!");
		} else if (Score.getScore() <= 600) {
			rewardLabel.setText(
					"Not bad! You scored " + score + "! A little more practise and you could get a perfect score!");
		} else {
			rewardLabel.setText("Congratulations! You scored " + score + "! Well done");
		}
	}
	
	public void setTopic(String topic) {
		this.topic = topic;
		topicLabel.setText("Topic: " + topic);
	}
	
	public void setUp(String topic, Score Score, long nanoTimeElapsed) {
		this.setTopic(topic);
		this.setUpResults(Score.getResult());
		this.showScore(Score);
		this.setTimeElapsed(nanoTimeElapsed);
		this.setUpScoreboardData(Score.getScore(), topic);
		this.unlockTheme(Score.getScore(), topic);
	}
	
	public void setUpResults(ObservableList<Word> tableData) {
		resultTable.setEditable(false);
		resultTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		TableColumn<Word, String> word = new TableColumn<>("Word");
		word.setCellValueFactory(new PropertyValueFactory<>("word"));
		TableColumn<Word, String> answer = new TableColumn<>("You Spelled");
		answer.setCellValueFactory(new PropertyValueFactory<>("answer"));
		word.setSortable(false);
		answer.setSortable(false);
		word.setReorderable(false);
		answer.setReorderable(false);
		resultTable.setItems(tableData);
		resultTable.getColumns().addAll(Arrays.asList(word, answer));
		resultTable.setRowFactory(tv -> new TableRow<Word>() {
			@Override
			protected void updateItem(Word word, boolean empty) {
				super.updateItem(word, empty);
				if (word == null) {
					setStyle("");
				} else if (word.isCorrect())
					this.setId("rightWord");
				else
					this.setId("wrongWord");
			}
		});
	}
	
	public void setUpScoreboardData(int score, String topic) {
		this.addLineToFile(String.valueOf(score), ".history.txt");
		this.addLineToFile(topic, ".history.txt");
		String date = new SimpleDateFormat("dd MMMM yyyy").format(new java.util.Date());
		this.addLineToFile(date, ".history.txt");
	}
	
	public void unlockTheme(int score, String topic) {
		if(score >= 850) {
			try {
				this.getMastered();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(!masteredTopics.contains(topic)) {
			this.addLineToFile(topic , ".theme.txt");
			}
		}
	}
	
	public void addLineToFile(String line, String file) {
		try {
			String command = "echo '" + line + "' >> " + file;
			// Sets the bash command
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
			Process process = pb.start();
			// Creates a process with the bash command and starts it
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getMastered() throws IOException {
		File data = new File("theme.txt");
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
	
	public Label getTimeLabel() {
		return timeLabel;
	}
	
	
}