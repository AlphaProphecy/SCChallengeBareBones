import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.nio.file.Path;

public class Interpreter {
	public static int lineCount;
	public static Map<String, Integer> vars;
	
	public static void main(String args[]) throws IOException{
		String[][] splitFile = readFile();
		ArrayList <Integer> loopPoint = new ArrayList<>();
		vars = new HashMap<String, Integer>();
		int lineNum = 0;
		int checkPoint = 0;
		
		System.out.println("Variables are printed alphabetically");
		
		while (lineNum < lineCount) {
			if (splitFile[lineNum][0].equals("while")) {
				if (loopPoint.isEmpty()) {
					loopPoint.add(lineNum);
					
				} else if (!loopPoint.contains(lineNum)) {
					loopPoint.add(lineNum);
				}
				if (vars.get(splitFile[lineNum][1]).equals(Integer.valueOf(splitFile[lineNum][3]))) {
					lineNum = checkPoint + 1;
					loopPoint.remove(loopPoint.size()-1);
					
				} else {
					lineNum++;
					
				}
			} else if (splitFile[lineNum][0].equals("end")){
				checkPoint = lineNum;
				lineNum = loopPoint.get(loopPoint.size()-1);
				
			} else {
				if (splitFile[lineNum][0].equals("clear")) {
					vars.put(splitFile[lineNum][1], 0);
					
				} else if (splitFile[lineNum][0].equals("incr")) {
					vars.put(splitFile[lineNum][1], vars.get(splitFile[lineNum][1]) + 1);
					
				} else if (splitFile[lineNum][0].equals("decr")) {
					vars.put(splitFile[lineNum][1], vars.get(splitFile[lineNum][1]) - 1);
					
				}
				lineNum++;
			}
			System.out.println(vars.values());
		}
	}
	
	// Reads the file and returns a 2d array with line separated as top lists and then each element of those lines as their own elements  
	public static String[][] readFile() throws IOException{
		String fileName = textInput("Enter the name of the file:");
		Path path = Paths.get(".\\"+fileName);
		lineCount = (int) Files.lines(path).count();
		String[][] splitFile = new String[lineCount][];
		BufferedReader file = Files.newBufferedReader(path);
		
		for (int i = 0; i < lineCount; i++) {
			String line = file.readLine();
			
			// Regx removing more then 2 white spaces and ;
			line = line.replaceAll("\\s{2,}|;", "");
		    
		    splitFile[i] = line.split(" ");
		}
		return splitFile;
	}
	
	// Reads the input from command line returning as a string
	public static String textInput(String request) throws IOException{
		System.out.println(request);
		InputStreamReader in = new InputStreamReader(System.in);
		BufferedReader ir = new BufferedReader(in);
		String temp = ir.readLine();
		return temp;
	}
}
