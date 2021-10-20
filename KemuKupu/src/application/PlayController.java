package application;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class PlayController extends PracticeController {
	
	@FXML
	private Label timeElapsed;
	
	private long startTime;
	private long endTime;
	
	private Timer timer;
	private TimerTask updateTime;
	
	private int timePassed = 0;
	
	@Override
	public void setTopic(String topic) { // Method to choose the spelling quiz topic
		// Inputs:
		// topic = the topic that the user chose
		this.Word = new Word(topic);
		this.Score = new Score();
		// Changes the global topic variable to the chosen topic
		topicLabel.setText("Topic: " + topic);
		// Sets the scoreLabel to display the user's current topic
		defaultWordLabel(Word.getWord());
		//Read the first word
		festival(Word);
		//
		this.setStartTime();
		this.startTiming();
	}
	
	public void setStartTime() {
		startTime = System.nanoTime();
	}
	
	public void startTiming(){
		int initialDelay = 10;
		int period = 10;
		updateTime = new TimerTask() {
			@Override
			public void run()  {
				javafx.application.Platform.runLater(new Runnable() {
					@Override
					public void run() {
						timePassed++;
						int minutes = timePassed/60000;
						int seconds = (timePassed%60000)/100;
						timeElapsed.setText("Time Elapsed:" + String.format("%02d:%02d",minutes,seconds));
					}
			});
			};
		};
		timePassed = 0;
		timer = new Timer();
		timer.scheduleAtFixedRate(updateTime, initialDelay, period);
	}
	
	public void stopTiming() {
		updateTime.cancel();
		timer.cancel();
		timer.purge();
	}
	
	@Override
	public void check(ActionEvent event) throws IOException, InterruptedException { // Method to check if the word was spelled correctly

		if(userSpelling.getText().toString().equalsIgnoreCase(Word.getWord())) {
			this.stopTiming();
			Score.incrementScore(Word.getWord());
			scoreLabel.setText("Score: " + Score.getScore());
			this.showCorrectMessage();
			// Checks if the user input word is the same as the word to be spelled, ignoring case
			if (Word.getWordList().isEmpty()) {
				endTime = System.nanoTime();
				FXMLLoader loader = new FXMLLoader(getClass().getResource("Reward2.fxml"));
				root = loader.load();

				RewardController RewardController = loader.getController();
				RewardController.setScoreBoard(Score);
				RewardController.setScore(Score);
				RewardController.setTimeElapsed(endTime-startTime);
				RewardController.setTheme(theme);
				RewardController.addMastered(Score, Word.getTopic());
				
				stage = (Stage)((Node)event.getSource()).getScene().getWindow();
				scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("css/" + theme).toExternalForm());
				stage.setScene(scene);
				stage.show();
				// Progresses the scene if the word list is empty
			}
			else {
				Word.newWord();
				festival(Word);
				incorrect = 0;
				defaultWordLabel(Word.getWord()); // Sets the word length display to the length of the new word
				this.startTiming();
			}
		} else {
		switch (incorrect) { // Does different things based on whether it is the first or second time the user spelled the word incorrectly
		case 0:
			incorrect++; // Increments the incorrect count to 1 so that the switch will change case the next time the user spells the word incorrectly
			showSecondLetter(Word.getWord()); // Shows the second letter of the word to the user as a hint
			this.showTryAgainMessage(); // Shows the try again message
			this.festival(Word); // Speaks the word out again using festival TTS
			break;
		case 1:
			this.stopTiming();
			Score.addWrong(Word.getWord());
			if (Word.getWordList().isEmpty()) {
				endTime = System.nanoTime();
				FXMLLoader loader = new FXMLLoader(getClass().getResource("Reward2.fxml"));
				root = loader.load();

				RewardController RewardController = loader.getController();
				RewardController.setScoreBoard(Score);
				RewardController.setScore(Score);
				RewardController.setTimeElapsed(endTime-startTime);
				RewardController.setTopic(Word.getTopic());
				RewardController.setTheme(theme);
				// Shows the users score and the words that the user got current or incorrect

				stage = (Stage)((Node)event.getSource()).getScene().getWindow();
				scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("css/" + theme).toExternalForm());
				stage.setScene(scene);
				stage.show();
				// Progresses the scene if the word list is empty
			}
			else {
				Word.newWord();
				this.festival(Word);
				// Moves on to the next word as the user has spelled the word incorrectly twice
				showEncouragingMessage(); 
				// Shows a message to encourage the user to try again
				incorrect = 0;
				// Resets the incorrect counter to 0 as the word has progressed
				defaultWordLabel(Word.getWord());
				// Sets the word length display to the length of the new word
				this.startTiming();
			}
			break;
		}
	}
	userSpelling.clear();
}

	
	@Override
	public void dontKnow(ActionEvent event) throws IOException {
		// Method that controls the behaviour of the button that is pressed when the user doesn't know the word
		this.stopTiming();
		Score.addWrong(Word.getWord());
		if (Word.getWordList().isEmpty()) {
			endTime = System.nanoTime();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Reward2.fxml"));
			root = loader.load();

			RewardController RewardController = loader.getController();
			RewardController.setScoreBoard(Score);
			RewardController.setScore(Score);
			RewardController.setTimeElapsed(endTime-startTime);
			RewardController.setTopic(Word.getTopic());
			RewardController.setTheme(theme);

			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("css/" + theme).toExternalForm());
			stage.setScene(scene);
			stage.show();
			// Progresses the scene if the word list is empty
		}
		else {
			showEncouragingMessage();
			// Uses TTS to speak "incorrect" and then prints the encouraging message to the screen
			Word.newWord();
			this.defaultWordLabel(Word.getWord());
			this.festival(Word);
			// Progresses to the next word
			incorrect = 0;
			// Resets the incorrect count as the program is progressing to a new word
		}
	}
}
