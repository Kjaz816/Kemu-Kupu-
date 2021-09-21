package application;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class reviewMistakesController extends newSpellingQuizController {
	
	
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

	
	public void remove(String word, String file) {
		try {
			String command = new String ("sed -i /" + word + "/d " + file);
			
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);

			Process process = pb.start();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void newSpellingQuiz() throws IOException {
		switch(this.time) {
		case 0:
			this.randWord(failed);
			this.festival(word);
			break;
		case 1:
			instruction.setText("Spell word 2 of 3:");
			this.randWord(failed);
			this.festival(word);
			break;
		case 2:
			instruction.setText("Spell word 3 of 3:");
			this.randWord(failed);
			this.festival(word);
			break;
		case 3: 
			//this.switchToMain();
			break;
		}
	}
	
	@Override
	public void check(ActionEvent event) throws IOException, InterruptedException {
		if(guestWord.getText().toString().equalsIgnoreCase(this.word)) {
			switch(incorrect) {
			case 0:
				this.festival("Correct");
				TimeUnit.SECONDS.sleep(2);
				this.remove(word, failed);
				time++;
				this.newSpellingQuiz();
				break;
			case 1:
				this.incorrect = 0;
				this.festival("Correct");
				TimeUnit.SECONDS.sleep(2);
				this.remove(word, failed);
				this.writeTo(word, faulted);
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
				time++;
				this.newSpellingQuiz();
				break;
			}
		}
	}
	
	public void SwitchToMain(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
		root = loader.load();
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
}
