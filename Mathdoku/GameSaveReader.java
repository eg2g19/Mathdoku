import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class GameSaveReader {
	
	BufferedReader reader;
	
	
	GameSaveReader(String path) {
		
		try {
		reader = new BufferedReader( new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	public int countLines() {
		
		int lines = 0;
		try {
			while (reader.readLine() != null) lines++;
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;
		
	}
	
	public String getLine() {
		
		String currentLine;
		
		try {
			if ((currentLine = reader.readLine()) != null) {
				return currentLine;
			}
		} catch (IOException e) {
			System.out.println("error reading from file");
			e.printStackTrace();
		}
		return "";
	}
	
	public void gameSaveReaderFromText(String fileName) {
		
	}
}
