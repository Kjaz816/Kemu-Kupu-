package application;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Stack;

public class Word {

	private String topic;
	private Stack<String> wordList = new Stack<String>();
	private String word;
	
	public Word(String topic) {
		this.topic = topic;
		this.randWord(topic);
		this.word = this.wordList.pop();
	}

	public void randWord(String topic) { // Method that fetches the random words from the chosen word list
		// Inputs: 
		// topic = title of the chosen word list
		try {
			String command = new String ("shuf -n 5 " + "words/" + topic + ".txt");
			// Sets up the bash command to shuffle the words in the word list file
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
			Process process = pb.start();
			// Creates a process with the bash command and starts it

			BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			// Pipelines the stdout and stderror to the variables

			int exitStatus = process.waitFor();

			if (exitStatus == 0) {
				String line;
				while ((line = stdout.readLine()) != null) {
					this.wordList.add(line); 
					// Adds the current line to the word list 
				}
				while ((line = stderr.readLine()) != null) {
					System.out.println(line);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void newWord() { // Method that moves the game on to the next word in the list and speaks it using TTS
		if (!wordList.isEmpty()) {
			word = wordList.pop();
			// Pops the current word from the word list and inputs it into the "word"		}
		}
	}
	
	public String getWord() { // Method that returns the current word
		return this.word; 
	}
	
	public Stack<String> getWordList(){
		return wordList;
	}
	
	public String getTopic() { // Method that returns the current word
		return this.topic; 
	}
}

