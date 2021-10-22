package application;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;

public class Score {
	private int score;
	private ObservableList<Word> result;

	public void addResult(Word Word) {
		result.add(Word);
	}

	public int getScore() {
		return score;
		// Gets the user's current score
	}

	public void updateScore(int TimePassed) {

		int TimeToBeat = 20;
		double timeMultiplier;
		if (TimePassed < TimeToBeat) {
			timeMultiplier = 1.0 - ((double) TimePassed/TimeToBeat) ;
			score += timeMultiplier * 100 + 100;
		} else {
			score += 100;
		}
	}
}
