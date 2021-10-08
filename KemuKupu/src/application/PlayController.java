package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.*;

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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PlayController implements Initializable {
	
	private Word Word;
	
	private int score = 0;
	private int incorrect = 0;
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML
	private Label scoreLabel;
	@FXML
	private Label currentMessage;
	@FXML
	private Label wordLabel;
	@FXML
	private TextField userSpelling;
	@FXML
	private Button repeatWord;
	@FXML
	private Button check;
	@FXML
	private Button dontKnow;
	@FXML
	private Label speedLabel;
	@FXML
	private Button faster;
	@FXML
	private Button slower;
	@FXML
	private Label topicLabel;
	// Sets up UI Elements

	private double displaySpeed = 1.0; // Variable which will be used to display the current playback speed
	private double voiceSpeed = 1.0; // Variable which will be put into the festival command to control the playback speed
	
	public void repeatWord(ActionEvent event) { // Method that repeats the current word
		try {
			festival(Word.getWord());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void faster(ActionEvent event) { // Method that increases the playback speed of words
		displaySpeed = Math.round((displaySpeed+0.1)*10)/10.0;
		voiceSpeed = Math.round((voiceSpeed-0.1)*10)/10.0;
		// Increments / Decrements "displaySpeed" and "voiceSpeed" variables by 0.1, which will be used by the speedLabel and festival respectively.
		speedLabel.setText("Current Speed: " + displaySpeed);	
		// Sets the speedLabel to display the current speed.
	}
	
	public void slower(ActionEvent event) { // Method that decreases the playback speed of words
		displaySpeed = Math.round((displaySpeed-0.1)*10)/10.0;
		voiceSpeed = Math.round((voiceSpeed+0.1)*10)/10.0;
		// Increments / Decrements "displaySpeed" and "voiceSpeed" variables by 0.1, which will be used by the speedLabel and festival respectively.
		speedLabel.setText("Current Speed: " + displaySpeed);
	}
	
	public void festival(String word) { // Method to speak the current word
		// Inputs: 
		// word = the current word
		try {
			PrintWriter speechWriter = new PrintWriter("speech.scm");
			speechWriter.println("(voice_akl_mi_pk06_cg)");
			speechWriter.println("(Parameter.set 'Duration_Stretch " + voiceSpeed + " )");
			speechWriter.println("(SayText \""  + word + "\")");
			speechWriter.close();
			// Sets the voice pack and playback speed of the festival TTS, and sets the word to be played as the input word
			String command = new String("festival -b speech.scm");
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
			Process process = pb.start();
			// Creates a process to input into bash with the festival settings and the word to be said
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void check(ActionEvent event) throws IOException, InterruptedException { // Method to check if the word was spelled correctly
	
			if(userSpelling.getText().toString().equalsIgnoreCase(Word.getWord())) {
				this.incrementScore();
				this.showCorrectMessage();
				// Checks if the user input word is the same as the word to be spelled, ignoring case
				if (Word.getWordList().isEmpty()) {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("Reward.fxml"));
					root = loader.load();
					
					RewardController RewardController = loader.getController();
					RewardController.setScored(score);
					
					stage = (Stage)((Node)event.getSource()).getScene().getWindow();
					scene = new Scene(root);
					stage.setScene(scene);
					stage.show();
					// Progresses the scene if the word list is empty
				}
				Word.newWord();
				festival(Word.getWord());
				incorrect = 0; // Sets the incorrect count to 0 as the user got the spelling correct
				defaultWordLabel(Word.getWord()); // Sets the word length display to the length of the new word
			}
			else {
				switch (incorrect) { // Does different things based on whether it is the first or second time the user spelled the word incorrectly
				case 0:
					incorrect++; // Increments the incorrect count to 1 so that the switch will change case the next time the user spells the word incorrectly
					showSecondLetter(Word.getWord()); // Shows the second letter of the word to the user as a hint
					this.showTryAgainMessage(); // Shows the try again message
					this.festival(Word.getWord()); // Speaks the word out again using festival TTS
					break;
				case 1:
					if (Word.getWordList().isEmpty()) {
						FXMLLoader loader = new FXMLLoader(getClass().getResource("Reward.fxml"));
						root = loader.load();
						
						RewardController RewardController = loader.getController();
						RewardController.setScored(score);
						RewardController.setTopic(Word.getTopic());
						
						stage = (Stage)((Node)event.getSource()).getScene().getWindow();
						scene = new Scene(root);
						stage.setScene(scene);
						stage.show();
						// Progresses the scene if the word list is empty
					}
					Word.newWord();
					this.festival(Word.getWord());
					// Moves on to the next word as the user has spelled the word incorrectly twice
					showEncouragingMessage(); 
					// Shows a message to encorage the user to try again
					incorrect = 0;
					// Resets the incorrect counter to 0 as the word has progressed
					defaultWordLabel(Word.getWord());
					// Sets the word length display to the length of the new word
					break;
				}
			}
			userSpelling.clear();
	}
	
	public void incrementScore() { // Method that increments the user's score
		if (incorrect == 0) {
			score++; 
			// Increments the "score" variable
			scoreLabel.setText("Score: " + Integer.toString(score));
			// Sets the scoreLabel to display the user's current score
		}
	}
	
	public void setTopic(String topic) { // Method to choose the spelling quiz topic
		// Inputs:
		// topic = the topic that the user chose
		this.Word = new Word(topic);
		// Changes the global topic variable to the chosen topic
		topicLabel.setText("Topic: " + topic);
		// Sets the scoreLabel to display the user's current topic
		this.defaultWordLabel(Word.getWord());
		//Read the first word
		this.festival(Word.getWord());
	}
	
	public void showEncouragingMessage() { // Method to show the user an encouraging message
		currentMessage.setVisible(true);
		// Sets the "currentMessage" label to be visible
		currentMessage.setText("Good try, play more to master this word!");
		// Sets the currentMessage label to an encouraging message for the user
	}
	
	public void hideEncouragingMessage() { // Method to hide the encouraging message
		currentMessage.setVisible(false);
		// Sets the "currentMessage" label to be invisible
		
	}
	
	public void showSecondLetter(String word) { // Method to show the user the second word of the current word to be spelled as a hint
		// Inputs:
		// word = the current word to be spelled
		char secondLetter = word.charAt(1);
		// Finds the second letter of the word and sets it to the "secondLetter" variable
		String dashedLines = "_ " + secondLetter;
		// Sets the first 2 characters of the string to an underscore and then the second letter of the current word
		int stringLength = (word.length() - 2);
		for (int i = 0; i < stringLength; i++) {
			dashedLines = dashedLines + "_ ";
		}
		// Sets the rest of the string to a number of underscores equal to the length of the word - 2, so that the string in total is the same length as the word
		wordLabel.setText(dashedLines);
		// Sets the wordLabel to this string
	}
	
	public void defaultWordLabel(String word) { // Method that changes the wordLabel that shows the second letter to the user back to the default state
		// Inputs:
		// word = the current word to be spelled
		String dashedLines = ""; // Creates the empty string
		int stringLength = word.length();
		for (int i = 0; i < stringLength; i++) {
			dashedLines = dashedLines + "_ ";
		}
		// Sets the string to a number of underscores equal to the word length
		wordLabel.setText(dashedLines);
		// Sets the wordLabel to this string
	}
	
	public void dontKnow(ActionEvent event) throws IOException { // Method that controls the behaviour of the button that is pressed when the user doesn't know the word
		if (Word.getWordList().isEmpty()) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Reward.fxml"));
			root = loader.load();
			
			RewardController RewardController = loader.getController();
			RewardController.setScored(score);
			RewardController.setTopic(Word.getTopic());
			
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
			// Progresses the scene if the word list is empty
		}
		else {
			showEncouragingMessage();
			// Uses TTS to speak "incorrect" and then prints the encouraging message to the screen
			Word.newWord();
			this.defaultWordLabel(Word.getWord());
			this.festival(Word.getWord());
			// Progresses to the next word
			incorrect = 0;
			// Resets the incorrect count as the program is progressing to a new word
		}
	}
	
	public void showTryAgainMessage() { // Method that shows the try again message when the user gets the spelling of the word wrong
		currentMessage.setVisible(true);
		// Sets the "currentMessage" label to be visible
		currentMessage.setText("Incorrect, try once more!");
		// Sets the currentMessage label to a message telling the user to try again
	}
	
	public void showCorrectMessage() {
		currentMessage.setVisible(true);
		// Sets the "currentMessage" label to be visible
		currentMessage.setText("Correct!");
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) { // Method that changes double-vowels into the vowel with a macron
		
		userSpelling.textProperty().addListener((ChangeListener<? super String>) new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.toLowerCase().contains("aa") || newValue.toLowerCase().contains("ee") || newValue.toLowerCase().contains("ii") 
						|| newValue.toLowerCase().contains("oo") || newValue.toLowerCase().contains("uu")) {
						// Checks if the answer field contains a double-vowel
					userSpelling.setText(newValue.replaceAll("Aa|AA", "Ā").replaceAll("aa", "ā").replaceAll("Ee|EE", "Ē").replaceAll("ee", "ē")
							.replaceAll("Ii|II", "Ī").replaceAll("ii", "ī").replaceAll("Oo|OO", "Ō").replaceAll("oo", "ō").replaceAll("Uu|UU", "Ū")
							.replaceAll("uu", "ū"));
							// Replaces the double-vowel with the corresponding vowel with a macron
				}
			}
		});
	}
}
