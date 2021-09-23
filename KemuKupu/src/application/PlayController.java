package application;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PlayController {
	
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

	private double voiceSpeed = 1.0;
	
	private List<String> wordList = new ArrayList<String>();
	private String currentWord;
	
	public void repeatWord(ActionEvent event) {
		try {
			festival(currentWord);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void randWords(String topic) {
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
			PrintWriter speechWriter = new PrintWriter("speech.scm");
			speechWriter.println("(Parameter.set 'Duration_Stretch " + voiceSpeed + " )");
			speechWriter.println("(SayText \""  + word + "\")");
			speechWriter.close();
			
			String command = new String("festival -b speech.scm");
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);

			Process process = pb.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
