import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.nio.file.Path;

public class Interpreter {
	public static Map<String, Float> vars;
	
	public static void main(String args[]) throws IOException{
		ArrayList<ArrayList<String>> splitFile = new ArrayList<ArrayList<String>>();
		
		splitFile = readFile();
		
		ArrayList <Integer> loopPoint = new ArrayList<>();
		vars = new HashMap<String, Float>();
		int lineNum = 0;
		int checkPoint = 0;
		
		Pattern fFinder = Pattern.compile("[^a-zA-Z]");
		Matcher fMatcher;
		
		/*for (int i=0; i<splitFile.size();i++) {
			for (int ii = 0; ii < splitFile.get(i).size(); ii++) {
				System.out.println(splitFile.get(i).get(ii).toString());
			}
		}*/
		
		ArrayList <String> operators = new ArrayList<String>();
		
		operators.add("+");
		operators.add("-");
		operators.add("*");
		operators.add("/");
		
		
		System.out.println("Variables are printed alphabetically");
		
		
		while (lineNum < splitFile.size()) {
			// Collates operations into their results first
			// Operators are calculated from left to right
			if (!Collections.disjoint(splitFile.get(lineNum), operators)) {
				int i =0;
				while (!Collections.disjoint(splitFile.get(lineNum), operators)) {
					if (operators.contains(splitFile.get(lineNum).get(i))){
						
						float value = 0;
						float firstVal;
						float secVal;
						
						fMatcher = fFinder.matcher(splitFile.get(lineNum).get(i-1));
						if (fMatcher.find()) {
							firstVal = Float.parseFloat(splitFile.get(lineNum).get(i-1));
						} else {
							firstVal = vars.get(splitFile.get(lineNum).get(i-1));
						}
						
						fMatcher = fFinder.matcher(splitFile.get(lineNum).get(i+1));
						if (fMatcher.find()) {
							secVal = Float.parseFloat(splitFile.get(lineNum).get(i+1));
						} else {
							secVal = vars.get(splitFile.get(lineNum).get(i+1));
						}
						
						
						if (splitFile.get(lineNum).get(i).equals("+")){
							value = firstVal + secVal;
						} else if (splitFile.get(lineNum).get(i).equals("-")){
							value = firstVal - secVal;
						} else if (splitFile.get(lineNum).get(i).equals("*")) {
							value = firstVal * secVal;
						} else if (splitFile.get(lineNum).get(i).equals("/")) {
							value = firstVal / secVal;
						}
						
						splitFile.get(lineNum).set(i, String.valueOf(value));
						splitFile.get(lineNum).remove(i-1);
						splitFile.get(lineNum).remove(i);
						i = 0;
						
					}
					i++;
					
				}
			}
			
			if (splitFile.get(lineNum).get(0).equals("while")) {
				if (loopPoint.isEmpty()) {
					loopPoint.add(lineNum);
					
				} else if (!loopPoint.contains(lineNum)) {
					loopPoint.add(lineNum);
				}
				
				if (vars.get(splitFile.get(lineNum).get(1)).equals(Float.parseFloat(splitFile.get(lineNum).get(3)))) {
					lineNum = checkPoint + 1;
					loopPoint.remove(loopPoint.size()-1);
					
				} else {
					lineNum++;
					
				}
				
			} else if (splitFile.get(lineNum).get(0).equals("end")){
				checkPoint = lineNum;
				lineNum = loopPoint.get(loopPoint.size()-1);
				
			} else {
				if (splitFile.get(lineNum).get(0).equals("clear")) {
					vars.put(splitFile.get(lineNum).get(1), 0f);
					
				} else if (splitFile.get(lineNum).get(0).equals("incr")) {
					vars.put(splitFile.get(lineNum).get(1), vars.get(splitFile.get(lineNum).get(1)) + 1);
					
				} else if (splitFile.get(lineNum).get(0).equals("decr")) {
					vars.put(splitFile.get(lineNum).get(1), vars.get(splitFile.get(lineNum).get(1)) - 1);
					
				} else if (splitFile.get(lineNum).contains("=")) {
					fMatcher = fFinder.matcher(splitFile.get(lineNum).get(2));
					if (fMatcher.find()) {
						vars.put(splitFile.get(lineNum).get(0), Float.valueOf(splitFile.get(lineNum).get(2)));
					} else {
						vars.put(splitFile.get(lineNum).get(0), vars.get(splitFile.get(lineNum).get(2)));
					}
				}
				lineNum++;
			}
			System.out.println(vars.values());
		}
		
	}
	
	// Reads the file and returns a 2d array with line separated as top lists and then each element of those lines as their own
	// elements while removing blank lines and comments
	private static ArrayList<ArrayList <String>> readFile() throws IOException{
		String fileName = textInput("Enter the name of the file:");
		Path path = Paths.get(".\\"+fileName);
		int lineCount = (int) Files.lines(path).count();
		
		BufferedReader file = Files.newBufferedReader(path);
		
		ArrayList <String> temp = new ArrayList<String>();
		
		
		Pattern pMain = Pattern.compile("\\S+");
		Pattern pMLCI = Pattern.compile("/{3,}");
		
		ArrayList <String> operators = new ArrayList<String>();
		
		operators.add("\\+");
		operators.add("-");
		operators.add("\\*");
		operators.add("/");
		operators.add("=");
		
		boolean cMode = false;
		
		for (int i = 0; i < lineCount; i++) {
			String line = file.readLine();
			
			
		    Matcher MLCI = pMLCI.matcher(line);
		    
		    if (MLCI.find()) {
		    	cMode = !cMode;
		    }
		    
			// Regx removing indentation whitespace, ; and trailing whitespace
			line = line.replaceAll("^\\s{2,}|;|//.*|[ \\t]+$", "");
			
			for (String p:operators) {
				line = line.replaceAll(p, " " + p + " ");
			}
			
			
			line = line.replaceAll("\\s+", " ");
			
		    Matcher main = pMain.matcher(line);
		    
		    if (main.find() && !cMode) {
		    	temp.add(line);
		    	
		    }
		}
		
		ArrayList <ArrayList<String>> splitFile = new ArrayList<ArrayList<String>>();
		
		
		for (String s:temp) {
			splitFile.add(new ArrayList<String>(Arrays.asList(s.split(" "))));
		}
		
		
		return splitFile;
	}
	
	// Reads the input from command line returning as a string
	private static String textInput(String request) throws IOException{
		System.out.println(request);
		InputStreamReader in = new InputStreamReader(System.in);
		BufferedReader ir = new BufferedReader(in);
		String temp = ir.readLine();
		return temp;
	}
}