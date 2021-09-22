package application;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class PlayController {
	
	private List<String> wordList = new ArrayList<String>();
	
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
