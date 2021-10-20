package application;

import java.util.ArrayList;
import java.util.List;

public class Score {
	private int score;
	private List<String> words = new ArrayList<String>();
	
	public void incrementScore(String word) {
		score++;
		words.add("correct: " + word);
		// Increments the users current score
	}
	
	public void addWrong(String word) {
		words.add("incorrect: " + word);
		// Adds a word that the user got incorrect to the List for future display
	}
	
	public int getScore() {
		return score;
		// Gets the user's current score
	}
	
	public List<String> getWords(){
		return this.words;
		// Gets the words tested
	}
}
