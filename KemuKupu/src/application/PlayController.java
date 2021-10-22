package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PlayController extends PracticeController {

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
			}
		}
		userSpelling.clear();
	}
}
