package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainController {
	
	private Stage stage;
	private Scene scene;
	private Parent root;

	public void SwitchToNewSpellingQuiz(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("newSpellingQuiz.fxml"));
		root = loader.load();
		
		newSpellingQuizController newSpellingQuizController = loader.getController();
		newSpellingQuizController.newSpellingQuiz();
		
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void SwitchToReviewMistakes(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("reviewMistakes.fxml"));
		root = loader.load();
		
		reviewMistakesController reviewMistakesController = loader.getController();
		reviewMistakesController.newSpellingQuiz();
		
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void SwitchToViewStatistics(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewStatistics.fxml"));
		root = loader.load();
		
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	@FXML
	private Label ins;
	
	public void ClearStatistics(ActionEvent event) {
		try {
			String command = "rm faulted failed mastered .wordlist";
			
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);

			Process process = pb.start();

			BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			
			int exitStatus = process.waitFor();
			
			if (exitStatus == 0) {
				String line;
				while ((line = stdout.readLine()) != null) {
					System.err.println(line);
				}
			} else {
				String line;
				while ((line = stderr.readLine()) != null) {
					System.err.println(line);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ins.setText("Cleared statistics");
	}
	
	@FXML
	private Button quitButton;
	@FXML 
	private AnchorPane scenePane;
	
	public void Quit(ActionEvent event) {
			stage = (Stage) scenePane.getScene().getWindow();
			stage.close();	
		}
	}
