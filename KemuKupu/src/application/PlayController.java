package application;

import java.io.IOException;

import javafx.event.ActionEvent;

public class PlayController extends PracticeController {
	@Override
	public void check(ActionEvent event) throws IOException, InterruptedException {
		incorrect = true;
		super.check(event);
	}
}
