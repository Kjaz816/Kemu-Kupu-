package application;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class TestController {

	@FXML
	private Label voiceSpeed;
	@FXML
	private Button faster;
	@FXML
	private Button slower;
	@FXML
	private Button preview;
	
	public double speed = 1.0;
	
	public void faster(ActionEvent event) {
		speed = speed + 0.1;
		speed = Math.round(speed*10)/10.0;
		voiceSpeed.setText("Current Speed: " + speed);	
	}
	
	public void slower(ActionEvent event) {
		speed = speed - 0.1;
		speed = Math.round(speed*10)/10.0;
		voiceSpeed.setText("Current Speed: " + speed);
	}
	
	public void preview(ActionEvent event) throws IOException {
		PrintWriter speechWriter = new PrintWriter("speech.scm");
		speechWriter.println("(Parameter.set 'Duration_Stretch " + voiceSpeed + " )");
		speechWriter.println("(SayText \"This is how fast I will talk\")");
		speechWriter.close();
		
		String command = new String("festival -b speech.scm");
		ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);

		Process process = pb.start();
	}
}
