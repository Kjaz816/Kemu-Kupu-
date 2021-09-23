package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PlayController {
	
	private int score;
	private String word;
	private int incorrect;
	
	@FXML
	private Label scoreLabel;
	@FXML
	private Label encouragingMessage;
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

	private double voiceSpeed = 1.0;
	private Stack<String> wordList = new Stack<String>();
	private String currentWord;
	
	public void repeatWord(ActionEvent event) {
		try {
			festival(currentWord);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void randWord(String topic) {
		try {
			String command = new String ("shuf -n 5 " + "word/" + topic + ".txt");
			
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);

			Process process = pb.start();

			BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			
			int exitStatus = process.waitFor();
			
			if (exitStatus == 0) {
				String line;
				while ((line = stdout.readLine()) != null) {
					wordList.add(line);
				}
				while ((line = stderr.readLine()) != null) {
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void festival(String word) {
		try {
			PrintWriter speechWriter = new PrintWriter("speech.scm");
			speechWriter.println("(Parameter.set 'Duration_Stretch " + voiceSpeed + " )");
			speechWriter.println("(SayText \""  + word + "\")");
			speechWriter.close();
			
			String command = new String("festival -b speech.scm");
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);

			Process process = pb.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void check(ActionEvent event) throws IOException, InterruptedException {
		if (wordList.isEmpty()) {
			//show results
		}
		else {
			if(userSpelling.getText().toString().equalsIgnoreCase(this.word)) {
				this.festival("correct");
				this.incrementScore();
				this.newWord();
				incorrect = 0;
			}
			else {
				switch (incorrect) {
				case 0:
					this.festival("Incorrect, try once more. " + this.word +  " "  +  this.word);
					incorrect++;
					break;
				case 1:
					this.festival("Incorrect");
					this.newWord();
					incorrect = 0;
					break;
				}
			}
		}
	}
	
	public void newWord() {
		word = wordList.pop();
		System.out.println(word);
		this.festival(word);
	}
	
	public void incrementScore() {
		if (incorrect == 0) {
			score++;
			scoreLabel.setText("Score: " + Integer.toString(score));
		}
	}
}
