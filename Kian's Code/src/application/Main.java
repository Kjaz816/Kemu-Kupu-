package application;
	
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
/*Spelling Quiz
 * By Kian Jazayeri - kjaz816@aucklanduni.ac.nz
 * 
 * This application allows the user to practise spelling commonly misspelled words provided in the "Popular" file.
 * Application Guide:
 * 
 * "New Spelling Quiz" - Starts a new quiz with 3 commonly misspelled words from the "Popular" file.
 * 		The application will read out the words, one at a time, and the user must input their spelling of the word into the input field. 
 * 		If the spelling is wrong on the first attempt, the user can try once more.
 * 		If the user is wrong on the second attempt the application will move onto the next word.
 * 
 * "Review Mistakes" - Starts a new quiz with 3 words that have been misspelled by the user in the past.
 * 		Functionality is otherwise the same as "New Spelling Quiz"
 * 
 * "View Statistics" - Displays statistics which include all words that have been tested in the past, sorted into categories:
 * 		Mastered: Words spelled correctly on the first attempt.
 * 		Faulted: Words spelled correctly on the second attmept but not the first.
 * 		Failed: Words spelled incorrectly on both attempts.
 * 
 * "Clear Statistics" - Clears user statistics.
 * 		The user will be prompted if they are sure before any data is deleted. */

public class Main extends Application {
	int wordCount = 0; // integer used to track which word the spelling quiz is on
	int wordsToReview = 2; // integer used to track how many words the spell quiz will use
	String testType; // String used to check whether the quiz is a normal spelling quiz or a review quiz
	List<String> words; // List of words to use for the spelling quiz
	
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,1200,1000);
			root.setStyle("-fx-background-color: white"); // Sets the scene background to white
			primaryStage.setTitle("Spelling Test");
			primaryStage.setScene(scene);
			primaryStage.show();
			
			// Menu Component Setups 
			Text text            	= new Text("Spelling Test");
			Button newQuizBtn    	= new Button("New Spelling Quiz");
			Button reviewQuizBtn 	= new Button("Review Mistakes");
			Button statsBtn      	= new Button("View Statistics");
			Button clearStatsBtn 	= new Button("Clear Statistics");
			Button enterBtn      	= new Button("Check Spelling");
			Button continueBtn   	= new Button("Continue");
			Button tryAgainBtn   	= new Button("Check Again");
			Label enterWord      	= new Label();
			TextField textField  	= new TextField ();
			Button closeStatsBtn    = new Button("Close Stats");
			Text statistics         = new Text("Statistics (Count followed by Word)");
			Text masteredStats      = new Text();
			Text faultedStats       = new Text();
			Text failedStats        = new Text();
			Text areYouSure         = new Text("Are you sure you want to clear stats?");
			Button clearConfirmBtn  = new Button("I am sure");
			Button clearCancelBtn	= new Button("I changed my mind");
			Text statsDeleted  		= new Text("Statistics have been reset");
			Button returnToMenu		= new Button("Return to menu");
			
			// Menu Component sizing
			text.setFont(new Font(30));
			newQuizBtn.setMinSize(400, 75);
			reviewQuizBtn.setMinSize(400, 75);
			statsBtn.setMinSize(400, 75);
			clearStatsBtn.setMinSize(400, 75);
			enterBtn.setMaxSize(200, 500);
			continueBtn.setMaxSize(200, 500);
			tryAgainBtn.setMaxSize(200, 500);
			returnToMenu.setMaxSize(200, 500);
			
			// HBox containing the input field and label for spelling quizzes
			HBox inputField = new HBox();
			inputField.getChildren().addAll(enterWord, textField);
			inputField.setSpacing(10);
			inputField.setAlignment(Pos.CENTER);
			inputField.setPadding(new Insets(30, 0, 0, 0));
			
			// VBox containing the main menu
			VBox menuElements= new VBox();
			menuElements.setPadding(new Insets(15, 12, 15, 12));
			menuElements.setSpacing(10);
			menuElements.getChildren().addAll(text, newQuizBtn, reviewQuizBtn, statsBtn, clearStatsBtn, inputField, enterBtn, tryAgainBtn, continueBtn);
			menuElements.setAlignment(Pos.CENTER);
			root.setCenter(menuElements); // Sets the Main Menu to the middle of the screen
			
			// HBox initializing the word statistics data layout for the statistics popup
			HBox statsTitles = new HBox();
			statsTitles.getChildren().addAll(masteredStats, faultedStats, failedStats);
			statsTitles.setSpacing(100);
			statsTitles.setAlignment(Pos.CENTER);
			statsTitles.setPadding(new Insets(30, 0, 0, 0));
			
			// VBox that contains the content for the statistics popup
			VBox statsPopupContent = new VBox();
			statsPopupContent.getChildren().addAll(closeStatsBtn, statistics, statsTitles);
			statsPopupContent.setSpacing(10);
			statsPopupContent.setAlignment(Pos.CENTER);
			statsPopupContent.setPadding(new Insets(0, 0, 0, 0));
			statsPopupContent.setStyle("-fx-background-color:white;"); // Sets the popup background to white
			
			// Initializes the popup which contains the statistics data and labels
			Popup statisticsPopup = new Popup();
			statisticsPopup.getContent().add(statsPopupContent);
			statisticsPopup.hide(); // Hides the popup until needed
			
			// HBox initializing the layout of the buttons used to clear statistics
			HBox clearButtons = new HBox();
			clearButtons.getChildren().addAll(clearConfirmBtn, clearCancelBtn);
			clearButtons.setSpacing(10);
			clearButtons.setAlignment(Pos.CENTER);
			clearButtons.setPadding(new Insets(0, 0, 0, 0));
			
			// VBox that contains the content for the clear statistics popup
			VBox clearPopupContent = new VBox();
			clearPopupContent.getChildren().addAll(areYouSure, clearButtons, statsDeleted, returnToMenu);
			clearPopupContent.setSpacing(10);
			clearPopupContent.setAlignment(Pos.CENTER);
			clearPopupContent.setPadding(new Insets(0, 0, 0, 0));  
			clearPopupContent.setStyle("-fx-background-color:white;"); // Sets the popup background to white
			
			// Initializes the popup which is used to clear statistics
			Popup clearStatsConfirm = new Popup();
			clearStatsConfirm.getContent().add(clearPopupContent);
	        clearStatsConfirm.hide(); // Hides the popup until needed
	        statsDeleted.setVisible(false);
	        returnToMenu.setVisible(false);
				
			
			// Hides the components of the spell check until needed
			inputField.setVisible(false);
			enterBtn.setVisible(false);
			continueBtn.setVisible(false);
			tryAgainBtn.setVisible(false);
			
			// Action when the "New Spelling Quiz" button is pressed
			newQuizBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					try {
						words = findWords("popular"); // Randomises the misspelled words file and creates a new word list with this data. 
						testType = "Test"; 
						wordCount = 0;
						wordsToReview = 2;
						enterWord.setText("Spell word " + (wordCount+1) + " of " + (wordsToReview+1)); // Sets the text of the input field label
						
						// Disables the main menu while the spelling quiz is ongoing.
						newQuizBtn.setDisable(true);
						reviewQuizBtn.setDisable(true);
						statsBtn.setDisable(true);
						clearStatsBtn.setDisable(true);
						
						// Enables the spelling quiz components
						enterBtn.setVisible(true);
						inputField.setVisible(true);
						inputField.setDisable(false);
						enterBtn.setDisable(false);
						
						
						// Uses festival to speak the current word
						speak(words.get(wordCount));
		
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			// Action when the "Enter" button is pressed after the user makes an attempt at spelling a word
			enterBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					try {
						
						boolean firstAttempt = spellTest(words.get(wordCount), textField.getText(), 0); // Checks if the user spelled the word right and returns true or false
						
						if (firstAttempt == true) { 
							appendToFile(words.get(wordCount), "/home/student/eclipse-workspace/SoftEng206A2/src/timesMastered"); // Adds the word to the Mastered words statistics file 
							
						if (testType.equals("Review")) {
							removeString(words.get(wordCount), "/home/student/eclipse-workspace/SoftEng206A2/src/failed"); 
							// Removes the word from the "Review Mistakes" word pool as they successfully spelled it
							
						}
							inputField.setDisable(true);
							enterBtn.setDisable(true);
							continueBtn.setVisible(true);
							// Disables the spelling quiz components until they are needed again
						} else {
							enterBtn.setDisable(true);
							tryAgainBtn.setVisible(true);	
							// Shows the "Try Again" button if the user spelled the word wrong
							
						}
						textField.clear(); // Clears the input field for the next word
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			// Action when the "Try again" button is pressed after the user spells a word wrong
			tryAgainBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					try {
						
						boolean secondAttempt = spellTest(words.get(wordCount), textField.getText(), 1); // Checks if the user spelled the word right and returns true or false
						
						if (secondAttempt == true) {
							appendToFile(words.get(wordCount), "/home/student/eclipse-workspace/SoftEng206A2/src/timesFaulted"); // Adds the word to the Faulted words statistics file 
							
						} else {
							appendToFile(words.get(wordCount), "/home/student/eclipse-workspace/SoftEng206A2/src/timesFailed"); // Adds the word to the Failed words statistics file 
							if (testType.equals("Test")) {
							appendToFile(words.get(wordCount), "/home/student/eclipse-workspace/SoftEng206A2/src/failed"); // Adds the word to the "Review Mistakes" word pool
							}
						}
						
						textField.clear();
						tryAgainBtn.setVisible(false);
						continueBtn.setVisible(true);
						// Disables the spelling quiz components until they are needed again
						
					} catch (IOException | InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
			
			// Action when the "Continue" button is pressed when the user wants to move to the next word
			continueBtn.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent arg0) {
						try {
							
							inputField.setDisable(false);
							continueBtn.setVisible(false);
							enterBtn.setDisable(false);
							
								
							if (wordCount < wordsToReview) { // Checks if the quiz has played the required number of words, and increments the word count by 1 if not
								wordCount++;
								speak(words.get(wordCount));
								enterWord.setText("Spell word " + (wordCount+1) + " of " + (wordsToReview+1)); // Sets the input field label to the new word count					
							} else {	
								inputField.setVisible(false);
								enterBtn.setVisible(false);
								continueBtn.setVisible(false);
								tryAgainBtn.setVisible(false);
								
								newQuizBtn.setDisable(false);
								reviewQuizBtn.setDisable(false);
								statsBtn.setDisable(false);
								clearStatsBtn.setDisable(false);
								speak(testType + " Finished");
								// Returns the user to the main menu and lets the user know the quiz is finished
										
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
			});
			
			// Action when the "Review Mistakes button is pressed" 
			reviewQuizBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					try {
						
						words = findWords("failed"); // Creates a new word list based on previously failed words
						wordsToReview = words.size()-1; // Sets the word count to the number of words in the word pool
						testType = "Review";
						wordCount = 0;
						
						if (words.isEmpty()) {
							speak("No words to review");
							
						} else {
							if (wordsToReview > 2){
							wordsToReview = 2; // Sets the maximum number of words to be quizzed to 3
							}
							enterWord.setText("Spell word " + (wordCount+1) + " of " + (wordsToReview+1)); 
							
							// Disables the main menu while the spelling quiz is ongoing
							newQuizBtn.setDisable(true);
							reviewQuizBtn.setDisable(true);
							statsBtn.setDisable(true);
							clearStatsBtn.setDisable(true);
							
							// Enables the spelling quiz components
							enterBtn.setVisible(true);
							inputField.setVisible(true);
							inputField.setDisable(false);
							enterBtn.setDisable(false);
							
							speak(words.get(wordCount));
						}
						
						} catch (Exception e) {
							e.printStackTrace();
						}
				}
			});
			
			// Action when the "View Statistics" button is pressed 
			statsBtn.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					
					menuElements.setVisible(false);	// Makes the main menu invisible
					statisticsPopup.show(primaryStage); // Shows the statistics data popups
					try {
						// Reads the 3 statistics files and prints the output to the popup
						masteredStats.setText("Mastered Words" + printStats("/home/student/eclipse-workspace/SoftEng206A2/src/timesMastered"));
						faultedStats.setText("Faulted Words" + printStats("/home/student/eclipse-workspace/SoftEng206A2/src/timesFaulted"));
						failedStats.setText("Failed Words" + printStats("/home/student/eclipse-workspace/SoftEng206A2/src/timesFailed"));
						
					} catch (IOException | InterruptedException e) {
						e.printStackTrace();
					}
					//Print Stats to Screen
				}
				
			});
			
			// Action when the "Close Statistics" button is pressed when the user wants to return to the main menu after viewing statistics
			closeStatsBtn.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
			
					menuElements.setVisible(true);  // Makes the main menu visible again
					statisticsPopup.hide(); // Hides the statistics data popup
					
				}
				
			});
			
			// Action when the "Clear Statistics" button is pressed
			clearStatsBtn.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					clearStatsConfirm.show(primaryStage); // Shows the clear statistics popup
					menuElements.setVisible(false);	// Makes the main menu invisible	
				}
				
			});
			
			// Action when the "I am sure" button is pressed after the user is prompted if they are sure they want to clear statistics
			clearConfirmBtn.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					statsDeleted.setVisible(true);
					clearButtons.setDisable(true);
					returnToMenu.setVisible(true);
					try {
						clearStats("/home/student/eclipse-workspace/SoftEng206A2/src/timesFaulted");
						clearStats("/home/student/eclipse-workspace/SoftEng206A2/src/timesFailed");
						clearStats("/home/student/eclipse-workspace/SoftEng206A2/src/timesMastered");
						clearStats("/home/student/eclipse-workspace/SoftEng206A2/src/failed");
						// Clears all of the user statistics stored in files.
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			// Action when the "I changed my mind" button is pressed after the user is prompted if they are sure they want to clear statistics
			clearCancelBtn.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					clearStatsConfirm.hide(); // Hides the clear statistics popup
					menuElements.setVisible(true); // Makes the main menu visible again
				}
			});
			
			// Action when the "Return to menu" button is pressed when the user wants to return to the main menu after clearing statistics
			returnToMenu.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					clearStatsConfirm.hide();
					menuElements.setVisible(true);	
					statsDeleted.setVisible(false);
					clearButtons.setDisable(false);
					returnToMenu.setVisible(false);
					// Returns the user to the menu 
				}
			});
		
			} catch(Exception e) {
			e.printStackTrace();
			}
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void speak(String word) throws IOException {
		ProcessBuilder festivalSpeech = new ProcessBuilder("bash", "-c", "echo " + word + "| festival --tts");
		festivalSpeech.start();
		// Uses festival to speak the input string
	}
	
	public List<String> findWords(String wordType) throws IOException{
		
		List<String> words = new ArrayList<>(); // Initializes the word list 
		
		BufferedReader reader = new BufferedReader(new FileReader("/home/student/eclipse-workspace/SoftEng206A2/src/" + wordType)); // reads the file that corresponds with the input word type
        String line;
        
        while ((line = reader.readLine()) != null) {
        	words.add(line);
        	// Adds each word to the word list
        }
        
        Collections.shuffle(words); // Shuffles the word list
        reader.close();
        return words; // Returns the word lsit
	}
	
	public boolean spellTest(String word1, String word2, int attemptCount) throws IOException, InterruptedException {
		// Lets the user know if they are correct or incorrect
		if (word1.equalsIgnoreCase(word2)) {		
			speak("Correct. "); 
				return true;
				
		} else {
			if (attemptCount == 0) {
				speak("Incorrect, fix spelling and then click check again");
				Thread.sleep(4000);
				speak(word1);
				Thread.sleep(2000);
				speak(word1);
				
			} else {
				speak("Incorrect. ");
				
			}
			return false;
		}
		
	}
	
	public void appendToFile(String word, String fileName) throws IOException {
		ProcessBuilder appendToFile = new ProcessBuilder("bash", "-c", "echo " + word + " >> " + fileName); // Adds the input word to the input file
		appendToFile.start();
	}
	
	public void removeString(String word, String fileName) throws IOException {
		ProcessBuilder removeString = new ProcessBuilder("bash", "-c", "sed -i " + "\"" + File.separator + word + File.separator + "d" + "\"" + " " + fileName);
		// Removes the input word from the input file
		removeString.start();
	}	
	
	public String printStats(String fileName) throws IOException, InterruptedException {
		
		ProcessBuilder printStats = new ProcessBuilder("uniq", "-c", fileName);
		Process output = printStats.start();
		BufferedReader stdout = new BufferedReader(new InputStreamReader(output.getInputStream()));
		String outputString = new String();
		String line;
		
			while ((line = stdout.readLine()) != null) {
				outputString = outputString +  "\n" + line; // Adds each word and the count to the output string
			}
			
		return outputString;
	}
	
	public void clearStats(String fileName) throws IOException {
		ProcessBuilder clearStats = new ProcessBuilder("bash", "-c", ":>" + fileName); // clears the input file
		clearStats.start();
	}
}