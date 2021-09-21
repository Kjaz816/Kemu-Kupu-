package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;

public class viewStatisticsController implements Initializable {
	
	@FXML
	private Label label;
	
	@FXML
	private ListView<String> myListView;
	
	private String count;
	
	private String nMastered, nFaulted, nFailed;
	
	private static String currentWord;
		
	public void sortUniq(){
			
			try {
				String command = "sort .wordlist | uniq";
				
				ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);

				Process process = pb.start();

				BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
				BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
				
				int exitStatus = process.waitFor();
				
				if (exitStatus == 0) {
					String line;
					while ((line = stdout.readLine()) != null) {
						myListView.getItems().add(line);
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
	}
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		this.sortUniq();
		
		myListView.getSelectionModel().selectedItemProperty().addListener((ChangeListener<? super String>) new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				// TODO Auto-generated method stub
				currentWord = myListView.getSelectionModel().getSelectedItem();
				wordCount(currentWord, "mastered");
				nMastered = count;
				wordCount(currentWord, "faulted");
				nFaulted = count;
				wordCount(currentWord, "failed");
				nFailed = count;
				label.setText("Mastered: "+ nMastered + ", faulted: " + nFaulted + ", failed: " + nFailed);
			}
			
		});
	}
	
	
	public void wordCount(String word, String file) {
		
		try {
			String command = "grep " + word + " " + file + " | wc -l";
			
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);

			Process process = pb.start();

			BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			
			int exitStatus = process.waitFor();
			
			if (exitStatus == 0) {
				String line;
				while ((line = stdout.readLine()) != null) {
					count = line;
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
	}
	
	protected Stage stage;
	protected Scene scene;
	protected Parent root;
	
	public void SwitchToMain(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
		root = loader.load();
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}



























