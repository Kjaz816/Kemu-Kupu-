package application;

import java.util.ArrayList;
import java.util.List;

public class Score {
	private int score;
	private List<String> time = new ArrayList<String>();
	
	public void incrementScore(String word) {
		score++;
		time.add("correct  : " + word);
		// Increments the users current score
	}
	
	public void addWrong(String word) {
		time.add("incorrect: " + word);
		// Adds a word that the user got incorrec to the List for future display
	}
	
	public int getScore() {
		return score;
		// Gets the user's current score
	}
	
	public List<String> getTime(){
		return this.time;
		// Gets the time elapsed
	}
}
