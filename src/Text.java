import java.util.*;
import java.io.*;
public class Text {
	private ArrayList<String> slides = new ArrayList<String>();
	
	public Text(String fileName) {
		try {
			BufferedReader sc = new BufferedReader(new FileReader(fileName));
			String s;
			while ((s = sc.readLine()) != null) {
				
			}
		}
		catch (FileNotFoundException e) {
			System.out.println("file not found");
		}
		catch (IOException e) {
			System.out.println("io exception");
		}
	}
}
