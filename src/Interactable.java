import java.util.*;
import java.io.*;
public class Interactable {
	
	private ArrayList<String[]> slides = new ArrayList<String[]>();
	private boolean choice = false;
	private int health;
	private String name;
	
	
	public Interactable(BufferedReader file, boolean option) {
		try {
			this.choice = option;
			String s;
			StringBuilder line = new StringBuilder("");
			
			String[] slide = new String[] {"", "", ""};
			int ind = 0;
			int lineInd = 0;
			while ((s = file.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(s);
				while (st.hasMoreTokens()) {
					String cur = st.nextToken();
					if (ind + cur.length() <= 50) {
						line.append(cur + " ");
						ind = line.length();
					}
					else {
						slide[lineInd] = line.toString();
						line.delete(0, line.length());
						line.append(cur + " ");
						ind = line.length();
						lineInd++;
						// the slide is complete
						if (lineInd == 3) {
							lineInd = 0;
							slides.add(slide);
							slide = new String[] {"", "", ""};
						}
					}
				}
			}
			slide[lineInd] = line.toString();
			slides.add(slide);

		}
		catch (FileNotFoundException e) {
			System.out.println("file not found");
		}
		catch (IOException e) {
			System.out.println("io exception");
		}
	}
	public Interactable(String s, boolean option, String name, int health) {
		this.choice = option;
		this.name = name;
		this.health = health;
		StringBuilder line = new StringBuilder("");
		String[] slide = new String[] {"", "", ""};
		int ind = 0;
		int lineInd = 0;
		StringTokenizer st = new StringTokenizer(s);
		while (st.hasMoreTokens()) {
			String cur = st.nextToken();
			if (ind + cur.length() <= 50) {
				line.append(cur + " ");
				ind = line.length();
			}
			else {
				slide[lineInd] = line.toString();
				line.delete(0, line.length());
				line.append(cur + " ");
				ind = line.length();
				lineInd++;
				// the slide is complete
				if (lineInd == 3) {
					lineInd = 0;
					slides.add(slide);
					slide = new String[] {"", "", ""};
				}
			}
		}
		slide[lineInd] = line.toString();
		if (option) {
			slide[2] = "                  YES                  NO";
		}
		slides.add(slide);
	}
	
	
	// getter
	public ArrayList<String[]> getSlides(){
		return slides;
	}
	public int getSlidesSize() {
		return slides.size();
	}
	public boolean getChoice() {
		return choice;
	}
	public String getName() {
		return name;
	}
	public int getHealth() {
		return health;
	}
	public String toString() {
		return slides.toString();
	}
}
