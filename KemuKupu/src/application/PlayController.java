package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PlayController extends PracticeController {
	@Override
	public void check(ActionEvent event) throws IOException, InterruptedException { // Method to check if the word was spelled correctly

		if(userSpelling.getText().toString().equalsIgnoreCase(Word.getWord())) {
			Score.incrementScore(Word.getWord());
			this.showCorrectMessage();
			// Checks if the user input word is the same as the word to be spelled, ignoring case
			if (Word.getWordList().isEmpty()) {
				setStage(event);
				// Progresses the scene if the word list is empty
			}
			else {
				newWord();
			}
		}
		else {
			Score.addWrong(Word.getWord());
			if (Word.getWordList().isEmpty()) {
				setStage(event);
				// Progresses the scene if the word list is empty
			}
			else {
				showEncouragingMessage(); 
				newWord();			
			}
		}
		userSpelling.clear();
	}
	
	public void setStage(ActionEvent event) throws IOException {
		endTime = System.nanoTime();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Reward2.fxml"));
		root = loader.load();
		RewardController RewardController = loader.getController();
		RewardController.setScored(Score);
		RewardController.setTimeElapsed(endTime-startTime);
		RewardController.setTheme(theme);
		RewardController.addMastered(Score, Word.getTopic());
		
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("css/" + theme).toExternalForm());
		stage.setScene(scene);
		stage.show();
	}
	
	public void newWord() {
		Word.newWord();
		festival(Word.getWord());
		defaultWordLabel(Word.getWord()); // Sets the word length display to the length of the new word
	}
}
