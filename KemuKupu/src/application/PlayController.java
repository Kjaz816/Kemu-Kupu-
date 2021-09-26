package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.*;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PlayController implements Initializable {
	
	private int score = 0;
	private String word;
	private int incorrect = 0;
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML
	private Label scoreLabel;
	@FXML
	private Label encouragingMessage;
	@FXML
	private Label wordLabel;
	@FXML
	private TextField userSpelling;
	@FXML
	private Button repeatWord;
	@FXML
	private Button check;
	@FXML
	private Button dontKnow;
	@FXML
	private Label speedLabel;
	@FXML
	private Button faster;
	@FXML
	private Button slower;
	@FXML
	private Label topicLabel;

	private double displaySpeed = 1.0;
	private double voiceSpeed = 1.0;
	
	private Stack<String> wordList = new Stack<String>();
	
	private String topic;
	
	public void repeatWord(ActionEvent event) {
		try {
			festival(word);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void faster(ActionEvent event) {
		displaySpeed = Math.round((displaySpeed+0.1)*10)/10.0;
		voiceSpeed = Math.round((voiceSpeed-0.1)*10)/10.0;
		speedLabel.setText("Current Speed: " + displaySpeed);	
	}
	
	public void slower(ActionEvent event) {
		displaySpeed = Math.round((displaySpeed-0.1)*10)/10.0;
		voiceSpeed = Math.round((voiceSpeed+0.1)*10)/10.0;
		speedLabel.setText("Current Speed: " + displaySpeed);
	}
	
	
	public void randWord(String topic) {
		try {
			String command = new String ("shuf -n 5 " + "words/" + topic + ".txt");
			
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
				while ((line = stderr.readLine()) != null) {
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		word = wordList.peek();
	}
	
	public void festival(String word) {
		try {
			PrintWriter speechWriter = new PrintWriter("speech.scm");
			speechWriter.println("(voice_akl_mi_pk06_cg)");
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
	
	public void check(ActionEvent event) throws IOException, InterruptedException {
		
		
			if(userSpelling.getText().toString().equalsIgnoreCase(this.word)) {
				this.festival("correct");
				this.incrementScore();
				this.newWord();
				incorrect = 0;
				defaultWordLabel(word);
			}
			else {
				switch (incorrect) {
				case 0:
					this.festival("Incorrect, try once more. " + this.word +  " "  +  this.word);
					incorrect++;
					showSecondLetter(word);
					break;
				case 1:
					this.festival("Incorrect");
					this.newWord();
					showEncouragingMessage();
					incorrect = 0;
					defaultWordLabel(word);
					break;
				}
			}
			
			if (wordList.isEmpty()) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("Reward.fxml"));
				root = loader.load();
				
				RewardController RewardController = loader.getController();
				RewardController.setScored(score);
				RewardController.setTopic(topic);
				
				stage = (Stage)((Node)event.getSource()).getScene().getWindow();
				scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
			}
	}
	
	public void newWord() {
		word = wordList.pop();
		this.festival(word);
	}
	
	public void incrementScore() {
		if (incorrect == 0) {
			score++;
			scoreLabel.setText("Score: " + Integer.toString(score));
		}
	}
	
	public void setTopic(String topic) {
		this.topic = topic;
		topicLabel.setText("Topic: " + topic);
	}
	
	public void showEncouragingMessage() {
		encouragingMessage.setText("Good try, play more to master this word!");
	}
	
	public void hideEncouragingMessage() {
		encouragingMessage.setText("");
		
	}
	
	public void showSecondLetter(String word) {
		char secondLetter = word.charAt(1);
		String dashedLines = "_ " + secondLetter;
		int stringLength = (word.length() - 2);
		for (int i = 0; i < stringLength; i++) {
			dashedLines = dashedLines + "_ ";
		}
		wordLabel.setText(dashedLines);
	}
	
	public void defaultWordLabel(String word) {
		String dashedLines = "";
		int stringLength = word.length();
		for (int i = 0; i < stringLength; i++) {
			dashedLines = dashedLines + "_ ";
		}
		wordLabel.setText(dashedLines);
	}
	
	public void dontKnow(ActionEvent event) throws IOException {
		if (wordList.isEmpty()) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Reward.fxml"));
			root = loader.load();
			
			RewardController RewardController = loader.getController();
			RewardController.setScored(score);
			RewardController.setTopic(topic);
			
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}
		else {
			this.festival("Incorrect");
			showEncouragingMessage();
			this.newWord();
			incorrect = 0;
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		userSpelling.textProperty().addListener((ChangeListener<? super String>) new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub 
				if (newValue.toLowerCase().contains("aa") || newValue.toLowerCase().contains("ee") || newValue.toLowerCase().contains("ii") 
						|| newValue.toLowerCase().contains("oo") || newValue.toLowerCase().contains("uu")) {
					
					userSpelling.setText(newValue.replaceAll("Aa|AA", "Ā").replaceAll("aa", "ā").replaceAll("Ee|EE", "Ē").replaceAll("ee", "ē")
							.replaceAll("Ii|II", "Ī").replaceAll("ii", "ī").replaceAll("Oo|OO", "Ō").replaceAll("oo", "ō").replaceAll("Uu|UU", "Ū")
							.replaceAll("uu", "ū"));
				}
			}
		});
	}
}
