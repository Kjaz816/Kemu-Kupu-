package application;	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
/*Kemu Kupu
 * By Owen Eng - David Tran - Kian Jazayeri 
 * 
 * This application allows the user to practise spelling Māori words in several different topics.
 * 
 * 		The user first picks the topic that they want to practice from the 13 different topics.
 * 		The program will then pick 5 random words from that topic, and place them into a list.
 * 		The application will read out the words, one at a time, and the user must input their spelling of the word into the input field. 
 * 		If the spelling is wrong on the first attempt, the user can try once more.
 * 		If the user is wrong on the second attempt the application will move onto the next word.
 * 		The user may also press several buttons on the UI which will do certain things:
 * 
 * "Don't Know" - Skips the word and moves on to the next.
 * "Faster" and "Slower" - Speeds up or slows down the playback of the words.
 * "Repeat Word" - Repeats the current word.

*/
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("Kēmu Kupu");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
