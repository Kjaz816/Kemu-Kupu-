package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.concurrent.TimeUnit;

public class newSpellingQuizController {
	
	protected String word = "There is nothing to practice";
	private String file = "words/*";
	
	@FXML
	private Label instruction;
	@FXML
	private TextField guestWord;
	
	int incorrect = 0;
	int time = 0;
	
	private String wordList = ".wordList";
	private String mastered = "mastered";
	private String faulted = "faulted";
	private String failed = "failed";
	
	
	public void newSpellingQuiz() throws IOException {
		switch(this.time) {
		case 0:
			this.randWord(file);
			this.festival(word);
			break;
		case 1:
			instruction.setText("Spell word 2 of 3:");
			this.randWord(file);
			this.festival(word);
			break;
		case 2:
			instruction.setText("Spell word 3 of 3:");
			this.randWord(file);
			this.festival(word);
			break;
		default:
			break;
		}
	}
	
	public void randWord(String file) {
		try {
			String command = new String ("shuf -n 1 " + file);
			
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);

			Process process = pb.start();

			BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			
			int exitStatus = process.waitFor();
			
			if (exitStatus == 0) {
				String line;
				while ((line = stdout.readLine()) != null) {
					this.word = line;
				}
			} else {
				String line;
				while ((line = stderr.readLine()) != null) {
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void festival(String word) {
		try {
			String command = new String ("echo " + word + " | festival --tts");
			
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);

			Process process = pb.start();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	public void check(ActionEvent event) throws IOException, InterruptedException {
		if(guestWord.getText().toString().equalsIgnoreCase(this.word)) {
			switch(incorrect) {
			case 0:
				this.festival("Correct");
				TimeUnit.SECONDS.sleep(2);
				this.writeTo(word, mastered);
				this.writeTo(word, wordList);
				time++;
				this.newSpellingQuiz();
				break;
			case 1:
				this.incorrect = 0;
				this.festival("Correct");
				TimeUnit.SECONDS.sleep(2);
				this.writeTo(word, faulted);
				this.writeTo(word, wordList);
				time++;
				this.newSpellingQuiz();
				break;
			}
		}
		else {
			switch (incorrect) {
			case 0:
				this.incorrect++;
				instruction.setText("Incorrect, try once more");
				this.festival("Incorrect, try once more. " + this.word +  " "  +  this.word);
				break;
			case 1:
				this.incorrect = 0;
				this.festival("Incorrect");
				TimeUnit.SECONDS.sleep(2);
				this.writeTo(word, failed);
				this.writeTo(word, wordList);
				time++;
				this.newSpellingQuiz();
				break;
			}
		}
	}
	
	public void writeTo (String word, String file) {
		try {
			String command = new String ("echo " + word + " >> " + file);
			
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);

			Process process = pb.start();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	protected Stage stage;
	protected Scene scene;
	protected Parent root;
	
	public void SwitchToMain(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
		root = loader.load();
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
