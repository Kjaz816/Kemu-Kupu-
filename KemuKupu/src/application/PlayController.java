package application;

import java.io.IOException;

import javafx.event.ActionEvent;

public class PlayController extends PracticeController {
	
	@Override
	public void switchToReward(ActionEvent event) {
		this.setLoader("fxml/Reward.fxml");
		try {
			root = loader.load();
			RewardController RewardController = loader.getController();
			RewardController.setTheme(theme);
			RewardController.setUpFromPlay(Word.getTopic(), Score, endTime - startTime);
			RewardController.getTimeLabel().setVisible(false);
			this.showStage(event);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void check(ActionEvent event) throws IOException, InterruptedException {
		incorrect = true;
		super.check(event);
	}
}
