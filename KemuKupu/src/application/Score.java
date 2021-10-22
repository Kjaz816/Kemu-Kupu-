package application;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;

public class Score {
	private int score;
	private ObservableList<Word> result;
	
	public void addResult(Word word) {
		result.add(word);
	}

	public int getScore() {
		return score;
		// Gets the user's current score
	}

	public void updateScore(String word, int TimePassed, int firstTry) {

		int TimeToBeat = 20;
		double timeMultiplier;

		if (firstTry == 0) {
			if (TimePassed < TimeToBeat) {
				timeMultiplier = 1.0 - ((double) TimePassed/TimeToBeat) ;
				score += timeMultiplier * 100 + 100;
			} else {
				score += 100;
			}
		} else {
			if (TimePassed < TimeToBeat) {
				timeMultiplier = 1.0 - ((double) TimePassed/TimeToBeat);
				score += timeMultiplier * 50 + 50;
			} else {
				score += 50;
			}
		}
	}
}
