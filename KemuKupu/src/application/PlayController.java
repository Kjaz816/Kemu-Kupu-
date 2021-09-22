package application;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PlayController {
	
	private List<String> wordList = new ArrayList<String>();
	@FXML
	private Label score;
	@FXML
	private Label encouragingMessage;
	@FXML
	private Label word;
	@FXML
	private TextField userSpelling;
	@FXML
	private Button repeatWord;
	@FXML
	private Button check;
	@FXML
	private Button dontKnow;
	
	public void randWord(String topic) {
		try {
			String command = new String ("shuf -n 5 " + "words/" + topic);
			
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);

			Process process = pb.start();

			BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			
			int exitStatus = process.waitFor();
			
			if (exitStatus == 0) {
				String line;
				while ((line = stdout.readLine()) != null) {
					wordList.add(line);
				}
			} else {
				String line;
				while ((line = stderr.readLine()) != null) {
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void festival(String word) {
		try {
			String command = new String ("echo " + word + " | festival --tts");
			
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);

			Process process = pb.start();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
