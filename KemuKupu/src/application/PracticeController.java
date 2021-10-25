package application;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class PracticeController extends Controller implements Initializable {

	protected Word Word;
	protected Score Score;
	
	protected double voiceSpeed = 1.0;
	protected double displaySpeed = 1.0;
	
	protected long startTime;
	protected long endTime;
	
	protected Timer timer;
	protected TimerTask updateTime;
	protected int timePassed = 0;
	
	protected boolean incorrect = false;
	
	@FXML
	protected Label scoreLabel;
	@FXML
	private Label currentMessage;
	@FXML
	private Label wordLabel;
	@FXML
	protected TextField userSpelling;
	@FXML
	private Button repeatWord;
	@FXML
	private Button check;
	@FXML
	private Button dontKnow;
	@FXML
	protected Label speedLabel;
	@FXML
	protected Label topicLabel;
	@FXML
	private ImageView spellingImage;
	@FXML
	protected Slider speedSlider = new Slider(0.5,1.5,1);
	@FXML
	protected Label timeElapsed;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) { // Method that changes double-vowels into the vowel with a macron
		userSpelling.textProperty().addListener((ChangeListener<? super String>) new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				Platform.runLater(()-> {
					userSpelling.setText(newValue.replaceAll("~A|`A", "Ā").replaceAll("~a|`a", "ā").replaceAll("~E|`E", "Ē").replaceAll("~e|`e", "ē")
							.replaceAll("~I|`I", "Ī").replaceAll("~i|`i", "ī").replaceAll("~O|`O", "Ō").replaceAll("~o|`o", "ō").replaceAll("~U|`U", "Ū")
							.replaceAll("~u|`u", "ū"));
					userSpelling.positionCaret(newValue.length());
					// Replaces the double-vowel with the corresponding vowel with a macron
				});
			}
		});
		speedSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				displaySpeed = speedSlider.getValue();
				voiceSpeed = 1.0/speedSlider.getValue();
				speedLabel.setText(String.format("Current Speed: %.2f", displaySpeed));
			}
		});
	}

	public void setUp(String topic) { // Method to choose the spelling quiz topic
		// Inputs:
		// topic = the topic that the user chose
		this.Word = new Word(topic);
		this.Score = new Score();
		scoreLabel.setText("Score: 0");	
		topicLabel.setText("Topic: " + topic);
		this.defaultWordLabel(Word.getWord());
		this.festival(Word);
		this.setWordImage(Word);
		this.setStartTime();
		this.startTiming();
	}

	public void festival(Word word) { // Method to speak the current word
		// Inputs: 
		// word = the current word
		try {
			PrintWriter speechWriter = new PrintWriter("speech.scm");
			speechWriter.println("(voice_akl_mi_pk06_cg)");
			speechWriter.println("(Parameter.set 'Duration_Stretch " + voiceSpeed + " )");
			speechWriter.println("(SayText \""  + word.removeHyphen() + "\")");
			speechWriter.close();
			// Sets the voice pack and playback speed of the festival TTS, and sets the word to be played as the input word

			String command = new String("festival -b speech.scm");
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
			Process process = pb.start();
			this.disableAllButtonFor(Word.getWord().length()/5*voiceSpeed + 2);
			// Creates a process to input into bash with the festival settings and the word to be said
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void disableAllButtonFor(double time) {
		dontKnow.setDisable(true);
		check.setDisable(true);
		repeatWord.setDisable(true);
		home.setDisable(true);
		help.setDisable(true);
		final Timeline animation = new Timeline(
				new KeyFrame(Duration.seconds(time),
						new EventHandler<ActionEvent>() {
					@Override public void handle(ActionEvent actionEvent) {
						dontKnow.setDisable(false);
						check.setDisable(false);
						repeatWord.setDisable(false);
						home.setDisable(false);
						help.setDisable(false);
					}
				}));
		animation.setCycleCount(1);
		animation.play();
	}

	public void setWordImage(Word word) {
		String currentUrl = ("Pictures" + File.separator + word.getTopic() + File.separator + word.getWord() + ".png");

		Image image = new Image(currentUrl);
		spellingImage.setImage(image);
		// Opens the image that corresponds to the current word
	}

	public void check(ActionEvent event) throws IOException, InterruptedException { // Method to check if the word was spelled correctly
		if(userSpelling.getText().toString().equalsIgnoreCase(Word.getWord())) {
			this.stopTiming();
			Word.update(userSpelling.getText(), true);
			Score.addResult(new Word(Word));
			Score.updateScore(timePassed);
			scoreLabel.setText("Score: " + Score.getScore());
			this.showCorrectMessage();
			if (Word.getWordList().isEmpty()) {
				endTime = System.nanoTime();
				this.switchToReward(event);
			}
			else {
				this.nextWord();
			}
		}
		else {
			if (!incorrect) {
				incorrect = true; // Increments the incorrect count to 1 so that the switch will change case the next time the user spells the word incorrectly
				showSecondLetter(Word.getWord()); // Shows the second letter of the word to the user as a hint
				this.showTryAgainMessage(); // Shows the try again message
				this.festival(Word); // Speaks the word out again using festival TTS
			}
			else {
				this.stopTiming();
				Word.update(userSpelling.getText(), false);
				Score.addResult(new Word(Word));
				this.showIncorrectMessage();
				if (Word.getWordList().isEmpty()) {
					endTime = System.nanoTime();
					this.switchToReward(event);
				}
				else {
					incorrect = false;
					this.nextWord();
				}
			}
		}
		userSpelling.clear();
	}

	public void switchToReward(ActionEvent event) {
		this.setLoader("fxml/Reward.fxml");
		try {
			root = loader.load();
			RewardController RewardController = loader.getController();
			RewardController.setTheme(theme);
			RewardController.setUp(Word.getTopic(), Score, endTime - startTime);
			RewardController.getTimeLabel().setVisible(false);
			this.showStage(event);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void nextWord() {
		Word.newWord();
		this.festival(Word);
		this.setWordImage(Word);
		this.defaultWordLabel(Word.getWord());
		this.startTiming();
	}

	public void showEncouragingMessage() { // Method to show the user an encouraging message
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText("Try to guess the word!");
		alert.showAndWait();
	}

	public void showSecondLetter(String word) { // Method to show the user the second word of the current word to be spelled as a hint
		// Inputs:
		// word = the current word to be spelled
		char secondLetter = word.charAt(1);
		// Finds the second letter of the word and sets it to the "secondLetter" variable
		String dashedLines = word.replaceAll(".", "_ ").trim();
		// Sets the first 2 characters of the string to an underscore and then the second letter of the current word
		StringBuilder DashedLines = new StringBuilder(dashedLines);
		DashedLines.setCharAt(2, secondLetter);
		// Sets the rest of the string to a number of underscores equal to the length of the word - 2, so that the string in total is the same length as the word
		wordLabel.setText(DashedLines.toString());
		// Sets the wordLabel to this string
	}

	public void defaultWordLabel(String word) { // Method that changes the wordLabel that shows the second letter to the user back to the default state
		// Inputs:
		// word = the current word to be spelled
		String dashedLines = word.replaceAll(".", "_ ").trim(); // Creates the empty string
		// Sets the string to a number of underscores equal to the word length
		wordLabel.setText(dashedLines);
		// Sets the wordLabel to this string
	}

	public void dontKnow(ActionEvent event) throws IOException {
		// Method that controls the behaviour of the button that is pressed when the user doesn't know the word
		this.stopTiming();
		Word.update(userSpelling.getText(), false);
		Score.addResult(new Word(Word));
		if (Word.getWordList().isEmpty()) {
			endTime = System.nanoTime();
			this.switchToReward(event);
		}
		else {
			incorrect = false;
			this.showEncouragingMessage();
			this.nextWord();
		}
	}

	public void showTryAgainMessage() { // Method that shows the try again message when the user gets the spelling of the word wrong
		currentMessage.setVisible(true);
		currentMessage.setText("Incorrect, try once more!");
	}

	public void showCorrectMessage() {
		currentMessage.setVisible(true);
		currentMessage.setText("Correct!");
	}

	public void showIncorrectMessage() {
		currentMessage.setVisible(true);
		currentMessage.setText("Incorrect! Keep trying to master it!");
	}

	public void repeatWord(ActionEvent event) { // Method that repeats the current word
		try {
			festival(Word);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	
	
}
