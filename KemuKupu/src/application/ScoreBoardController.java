package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ScoreBoardController extends Controller implements Initializable {

	@FXML
	private TableView<Quiz> scoreboard;
	private ObservableList<Quiz> tableData = FXCollections.observableArrayList();

	@FXML
	private Button clear;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		// set up table
		scoreboard.setEditable(false);
		scoreboard.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		// create columns
		TableColumn<Quiz, Integer> score = new TableColumn<>("Score");
		score.setCellValueFactory(new PropertyValueFactory<>("Score"));
		TableColumn<Quiz, String> topic = new TableColumn<>("Topic");
		topic.setCellValueFactory(new PropertyValueFactory<>("Topic"));
		TableColumn<Quiz, String> date = new TableColumn<>("Date");
		date.setCellValueFactory(new PropertyValueFactory<>("Date"));
		score.setSortable(true);
		topic.setSortable(false);
		date.setSortable(false);
		score.setReorderable(false);
		topic.setReorderable(false);
		date.setReorderable(false);
		try {
			File resultsFile = new File(".history.txt");
			if (!resultsFile.exists()) {
				resultsFile.createNewFile();
			}

			BufferedReader pb = new BufferedReader(new FileReader(resultsFile));
			String line;
			while ((line = pb.readLine()) != null) {
				tableData.add(new Quiz(Integer.valueOf(line), pb.readLine(), pb.readLine()));
			}
			pb.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// add data to table
		scoreboard.setItems(tableData);
		scoreboard.getColumns().addAll(Arrays.asList(score, topic, date));

		// sort the table entries by score
		score.setSortType(TableColumn.SortType.DESCENDING);
		scoreboard.getSortOrder().add(score);
		scoreboard.sort();
		score.setSortable(false);
	}

	public void clear(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("Are you sure to delete history ?");

		if (alert.showAndWait().get() == ButtonType.OK) {
			File resultsFile = new File(".history.txt");
			if (resultsFile.exists()) {
				resultsFile.delete();
			}
			tableData.clear();
		}
	}
}