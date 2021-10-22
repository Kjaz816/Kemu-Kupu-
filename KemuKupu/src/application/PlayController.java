package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PlayController extends PracticeController {

<<<<<<< HEAD
=======
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
		scoreLabel.setText("Score: 0");
		// Changes the global topic variable to the chosen topic
		topicLabel.setText("Topic: " + topic);
		// Sets the scoreLabel to display the user's current topic
		defaultWordLabel(Word.getWord());
		//Read the first word
		festival(Word);
		//Sets the image of the first word
		setWordImage(Word);
		//
		this.setStartTime();
		this.startTiming();
	}

	public void setStartTime() {
		startTime = System.nanoTime();
	}

	public void startTiming(){
		int initialDelay = 1000;
		int period = 1000;
		updateTime = new TimerTask() {
			@Override
			public void run()  {
				javafx.application.Platform.runLater(new Runnable() {
					@Override
					public void run() {
						timePassed++;
						int minutes = timePassed/60;
						int seconds = timePassed%60;
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

>>>>>>> 2e040a69c27f1a2d7f7750301c1bf8a86e7c6851
	@Override
	public void check(ActionEvent event) { // Method to check if the word was spelled correctly

		if(userSpelling.getText().toString().equalsIgnoreCase(Word.getWord())) {
			Word.update("", false);
			Score.addResult(Word);
			Score.updateScore(timePassed);
			this.stopTiming();
			scoreLabel.setText("Score: " + Score.getScore());
			this.showCorrectMessage();
			// Checks if the user input word is the same as the word to be spelled, ignoring case
			if (Word.getWordList().isEmpty()) {
				endTime = System.nanoTime();
				this.showStage(event);
				// Progresses the scene if the word list is empty
			}
			else {
				Word.newWord();
				this.festival(Word);
				this.defaultWordLabel(Word.getWord()); // Sets the word length display to the length of the new word
				this.startTiming();
			}
<<<<<<< HEAD
		} 
		else {
			this.stopTiming();
			Word.update("", false);
			Score.addResult(Word);
			if (Word.getWordList().isEmpty()) {
				endTime = System.nanoTime();
				this.showStage(event);
				// Progresses the scene if the word list is empty
			}
			else {
				this.showEncouragingMessage(); 
				Word.newWord();
				this.festival(Word);
				this.defaultWordLabel(Word.getWord()); // Sets the word length display to the length of the new word
				this.startTiming();		
=======
		} else {
			switch (incorrect) { // Does different things based on whether it is the first or second time the user spelled the word incorrectly
			case 0:
				incorrect++; // Increments the incorrect count to 1 so that the switch will change case the next time the user spells the word incorrectly
				showSecondLetter(Word.getWord()); // Shows the second letter of the word to the user as a hint
				this.showTryAgainMessage(); // Shows the try again message
				this.festival(Word); // Speaks the word out again using festival TTS
				this.setWordImage(Word); // Sets image of the next word
				break;
			case 1:
				this.stopTiming();
				Score.addWrong(Word.getWord());
				if (Word.getWordList().isEmpty()) {
					setStage(event);
					// Progresses the scene if the word list is empty
				}
				else {
					showEncouragingMessage(); 
					newWord();			
				}
				break;
>>>>>>> 2e040a69c27f1a2d7f7750301c1bf8a86e7c6851
			}
		}
		userSpelling.clear();
	}
<<<<<<< HEAD
=======


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
			this.setWordImage(Word);
			// Progresses to the next word
			incorrect = 0;
			// Resets the incorrect count as the program is progressing to a new word
			this.startTiming();
		}
	}

	public void setStage(ActionEvent event) throws IOException {
		endTime = System.nanoTime();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Reward2.fxml"));
		root = loader.load();

		RewardController RewardController = loader.getController();
		RewardController.setScoreBoard(Score);
		RewardController.setScore(Score);
		RewardController.setTimeElapsed(endTime-startTime);
		RewardController.setTheme(theme);
		RewardController.setTopic(Word.getTopic());
		RewardController.addMastered(Score, Word.getTopic());

		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("css/" + theme).toExternalForm());
		stage.setScene(scene);
		stage.show();
		// Progresses the scene if the word list is empty
	}

	public void newWord() {
		Word.newWord();
		festival(Word);
		setWordImage(Word);
		incorrect = 0;
		defaultWordLabel(Word.getWord()); // Sets the word length display to the length of the new word
		this.startTiming();
	}
>>>>>>> 2e040a69c27f1a2d7f7750301c1bf8a86e7c6851
}
