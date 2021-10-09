package application;

import java.util.ArrayList;
import java.util.List;

public class Score {
	private int score;
	private List<String> time = new ArrayList<String>();
	
	public void incrementScore(String word) {
		score++;
		time.add("correct  : " + word);
	}
	
	public void addWrong(String word) {
		time.add("incorrect: " + word);
	}
	
	public int getScore() {
		return score;
	}
	
	public List<String> getTime(){
		return this.time;
	}
}
