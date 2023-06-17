
import javax.swing.*;
import javax.swing.Timer;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.util.*;

public class Main extends JPanel implements KeyListener, MouseListener, Runnable{

	boolean up, down, left, right; // movement in a direction
	ArrayList<Image> playerImages = new ArrayList<>(); // arraylist of player images
	int playerIndex = 1; // which image to display of player
	int playerX = 400, playerY = 250;
	int xCounter = 0;
	int yCounter = 0;  // mod 100 
	
	Player Player = new Player(this);
	public static int menuState = 0;
	public static BufferedImage title2;
	public static boolean hoveringSecret = false;
	public static int submenuOption = 0;
	public static BufferedImage optionsMenu;
	public static BufferedImage aboutMenu;
	
	public static Thread thread;
	public static int mapX = 0;
	public static int mapY = 0;	
	public static BufferedImage[] screens = new BufferedImage[5];
	public static BufferedImage[] speechBoxes = new BufferedImage[4];
	public static int mouseX;
	public static int mouseY;
	public static int windowWidth = 900;
	public static int windowHeight = 600;
	
	static boolean speaking = true;
	static int speakingInd = 0;
	static boolean[] scriptRead = new boolean[4];
	static Interactable[] mainScript = new Interactable[4];
	static BufferedImage selector;
	static ArrayList<Interactable>[] interactablesScript = new ArrayList[4];
	static int interactableScript = -1;
	static boolean choosing = false;
	static boolean choice = true;
	
	static int charHeight = 66;
	static int charWidth = 63;
	static int charSpeed = 2;
	public static ArrayList<Rectangle>[] interactables = new ArrayList[4];

	public static ArrayList<Rectangle>[][] bounds = new ArrayList[4][2];
	public static ArrayList<Rectangle>[] entrances = new ArrayList[4];
	
	static Font speakingFont;
	
	Audio sound = new Audio();
	static boolean[] settingSongPlayed = new boolean[4];
	
	
	static boolean[] healsVisited = new boolean[6];
	HashMap<String, Integer> backpack = new HashMap<String, Integer>();
	public Main() {
		setPreferredSize(new Dimension(900, 600));
		setBackground(new Color(200, 0, 0));
		addKeyListener(this);
		addMouseListener(this);
		this.setFocusable(true);
		
		try {
			for(int i = 0; i < 12; i++) {
				Image image = ImageIO.read(new File("assets/runAnimation/" + (i+1) + ".png"));
				Image newImage = image.getScaledInstance(charWidth, charHeight,  java.awt.Image.SCALE_SMOOTH);
				image = newImage;
				
				playerImages.add(image);
			}

			for (int i = 0; i < 4; i++) {
				interactables[i] = new ArrayList<Rectangle>();
				interactablesScript[i] = new ArrayList<Interactable>();
				entrances[i] = new ArrayList<Rectangle>();
				for (int x = 0; x < 2; x++) {
					bounds[i][x] = new ArrayList<Rectangle>();
				}
			}
			title2 = ImageIO.read(new File("assets/gameScreens/titleScreen2.png"));
			optionsMenu = ImageIO.read(new File("assets/gameScreens/optionsMenu.png"));
			aboutMenu = ImageIO.read(new File("assets/gameScreens/aboutMenu.png"));
			screens[0] = ImageIO.read(new File("assets/gameScreens/titleScreen.png"));
			screens[1] = ImageIO.read(new File("assets/gameScreens/whitespace2.png"));
			screens[2] = ImageIO.read(new File("assets/gameScreens/omorimap2.png"));
			screens[3] = ImageIO.read(new File("assets/gameScreens/blackspace.png"));
			speechBoxes[0] = ImageIO.read(new File("assets/scripts/speechBox.png"));
			speechBoxes[1] = ImageIO.read(new File("assets/scripts/sunny.png"));
			speechBoxes[2] = ImageIO.read(new File("assets/scripts/kel.png"));
			speechBoxes[3] = ImageIO.read(new File("assets/scripts/sunny.png"));
			selector = ImageIO.read(new File("assets/scripts/select.png"));
			speakingFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/OMORI_GAME2.ttf")).deriveFont(50f);
			
			// script reading
			for (int i = 1; i < 4; i++) {
				mainScript[i] = new Interactable(new BufferedReader(new FileReader("assets/scripts/script" + i + ".txt")), false);
			}
			interactablesScript[1].add(new Interactable("Unknown pills. They're labelled HIGHLY POISONOUS. Take them?", true, "pills", 0));
			interactablesScript[1].add(new Interactable(new BufferedReader(new FileReader("assets/scripts/journal.txt")), false));
			interactablesScript[1].get(1).getSlides().add(0, new String[] {"SUNNY'S JOURNAL", "", ""});
			interactablesScript[1].add(new Interactable("meow.", false, "cat", 0));
			interactablesScript[2].add(new Interactable("Grab a piece of green melon?", true, "Green melon", 10));
			interactablesScript[2].add(new Interactable("Grab a piece of green melon?", true, "Green melon", 10));
			interactablesScript[2].add(new Interactable("Grab an apple out of the picnic box?", true, "picnic", 30));
			interactablesScript[2].add(new Interactable("Grab a slice of chicken?", true, "chicken", 30));
			interactablesScript[2].add(new Interactable("Grab a piece of blue melon?", true, "Blue melon", 15));
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

		interactables[1].add(new Rectangle(981, 1120, 40, 80)); // the pills
		interactables[1].add(new Rectangle(1140, 1120, 60, 80)); // the book
		interactables[1].add(new Rectangle(980, 1260, 60, 70)); // the cat

		interactables[3].add(new Rectangle(1464, 1598, 40, 80)); // the pills
		interactables[3].add(new Rectangle(1686, 1716, 60, 80)); // the book
		interactables[3].add(new Rectangle(1450, 1782, 90, 70)); // the cat
		
		// doors, starting from one on the left. clockwise fashion
		interactables[3].add(new Rectangle(1248, 1624, 82, 80));
		interactables[3].add(new Rectangle(1444, 1432, 82, 80));
		interactables[3].add(new Rectangle(1778, 1480, 82, 80));
		interactables[3].add(new Rectangle(1854, 1748, 82, 80));
		interactables[3].add(new Rectangle(1654, 1926, 82, 80));
		interactables[3].add(new Rectangle(1400, 1890, 82, 80));


		
		bounds[1][0] = interactables[1];
		bounds[1][1].add(new Rectangle(0, 0, 3000, 3000));
		
		// map bounds
		
		interactables[2].add(new Rectangle(1156, 860, 60, 52));
		interactables[2].add(new Rectangle(650, 1696, 72, 44));
		interactables[2].add(new Rectangle(2810, 2150, 72, 72));
		interactables[2].add(new Rectangle(2916, 2268, 74, 68));
		interactables[2].add(new Rectangle(3722, 2604, 68, 68));
		bounds[2][0] = interactables[2];
		
		bounds[2][1].add(new Rectangle(876, 614, 52, 220));
		bounds[2][1].add(new Rectangle(834, 810, 130, 150));
		bounds[2][1].add(new Rectangle(784, 910, 416, 668));
		
		bounds[2][1].add(new Rectangle(1000, 1558, 324, 144));
		bounds[2][1].add(new Rectangle(1176, 1700, 78, 56));
		bounds[2][1].add(new Rectangle(832, 1558, 346, 212));
		bounds[2][1].add(new Rectangle(720, 1718, 114, 52));
		
		bounds[2][1].add(new Rectangle(1320, 1622, 338, 74));
		bounds[2][1].add(new Rectangle(1452, 1668, 96, 102));
		bounds[2][1].add(new Rectangle(1658, 1552, 300, 24));
		bounds[2][1].add(new Rectangle(1664, 1574, 112, 62));
		bounds[2][1].add(new Rectangle(1510, 970, 156, 686));
		bounds[2][1].add(new Rectangle(1270, 1286, 186, 26));
		bounds[2][1].add(new Rectangle(1450, 1286, 62, 12));
		bounds[2][1].add(new Rectangle(1272, 976, 44, 52));
		bounds[2][1].add(new Rectangle(1314, 1020, 212, 10));
		bounds[2][1].add(new Rectangle(1660, 970, 290, 272));
		bounds[2][1].add(new Rectangle(1660, 1240, 116, 54));
		
		bounds[2][1].add(new Rectangle(1948, 970, 84, 48));
		bounds[2][1].add(new Rectangle(2008, 970, 528, 60));
		bounds[2][1].add(new Rectangle(2530, 970, 62, 48)); 
		bounds[2][1].add(new Rectangle(2588, 970, 372, 92)); 
		bounds[2][1].add(new Rectangle(2830, 1060, 28, 372));
		bounds[2][1].add(new Rectangle(2664, 1430, 305, 34));
		bounds[2][1].add(new Rectangle(2946, 1462, 24, 114)); 
		bounds[2][1].add(new Rectangle(2968, 1540, 400, 38)); 
		bounds[2][1].add(new Rectangle(3354, 1316, 14, 234)); 
		bounds[2][1].add(new Rectangle(3354, 1316, 130, 28));
		bounds[2][1].add(new Rectangle(3466, 1200, 18, 140));
		bounds[2][1].add(new Rectangle(3466, 1200, 190, 34));
		bounds[2][1].add(new Rectangle(3638, 1232, 18, 772)); 
		
		bounds[2][1].add(new Rectangle(3496, 1942, 144, 62));
		bounds[2][1].add(new Rectangle(3122, 1942, 378, 36)); 
		bounds[2][1].add(new Rectangle(3122, 1826, 14, 118));
		bounds[2][1].add(new Rectangle(2594, 1826, 542, 36)); 
		
		bounds[2][1].add(new Rectangle(3654, 1998, 358, 8));
		bounds[2][1].add(new Rectangle(3910, 1998, 98, 400));
		bounds[2][1].add(new Rectangle(3860, 2176, 100, 100));
		bounds[2][1].add(new Rectangle(3738, 2220, 200, 150));
		bounds[2][1].add(new Rectangle(3502, 2286, 506, 400));
		
		bounds[2][1].add(new Rectangle(2594, 1826, 30, 100));
		bounds[2][1].add(new Rectangle(2556, 1882, 68, 690));
		bounds[2][1].add(new Rectangle(2380, 2570, 656, 32)); 
		
		bounds[2][1].add(new Rectangle(2552, 2600, 14, 702));
		bounds[2][1].add(new Rectangle(2552, 3300, 156, 204));
		bounds[2][1].add(new Rectangle(2408, 3356, 146, 148));
		
		bounds[2][1].add(new Rectangle(2542, 3448, 32, 376));
		bounds[2][1].add(new Rectangle(2482, 3594, 144, 134));
		bounds[2][1].add(new Rectangle(2498, 3816, 122, 464));
		
		bounds[2][1].add(new Rectangle(1530, 956, 60, 16)); // above first gate
		// area after being teleported
		bounds[2][1].add(new Rectangle(2786, 2094, 60, 16));
		bounds[2][1].add(new Rectangle(2716, 2108, 210, 80));
		bounds[2][1].add(new Rectangle(2662, 2166, 422, 134));
		bounds[2][1].add(new Rectangle(2662, 2222, 764, 224));
		
		// entrances
		entrances[2].add(new Rectangle(1532, 950, 56, 10)); // teleport box thing 1
		entrances[2].add(new Rectangle(2786, 2094, 60, 5)); // teleport box thing 2 *on top of the metal boxes area*
		entrances[2].add(new Rectangle(2498, 4274, 122, 10)); // exit from map into black space
		
		bounds[3][0] = interactables[3];
		bounds[3][1].add(new Rectangle(0, 0, 4000, 4000));
		// end map coordinates should be mapX = 1580, mapY = 1690
		
		thread = new Thread(this);
		thread.start();
	}
	
	public static void main(String[] args) {
		System.out.println("bop");
		JFrame frame = new JFrame("OMORI");
		Main panel = new Main();
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);

	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setFont(speakingFont);
		g2d.setColor(new Color(255, 255, 255));
		
		// play song
		if (!settingSongPlayed[menuState]) {

			try {
				sound.playSettingMusic(menuState);
			} catch (LineUnavailableException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			settingSongPlayed[menuState] = true;

		} 
		
		if (menuState == 0) {
			if (submenuOption == 0) {
				if (hoveringSecret) {
					g2d.drawImage(title2, 0, 0, null);
				}
				else {
					g2d.drawImage(screens[0], 0, 0, null);
				}
			}
			else if (submenuOption == 1) {
				g2d.drawImage(optionsMenu, 0, 0, null);
			}
			else if (submenuOption == 2) {
				g2d.drawImage(aboutMenu, 0, 0, null);
			}
		}
		else if (menuState > 0) {
			try {
				
				Thread.sleep(10);
				g2d.drawImage(screens[menuState], -1 * mapX, -1 * mapY, null);
				g2d.drawImage(playerImages.get(playerIndex), playerX, playerY, null);
				if (speaking) {
					
					Interactable cur = null;
					ArrayList<String[]> curSlides = null; 
					up = false;
					down = false;
					right = false;
					left = false;
					if (!scriptRead[menuState]) {
						g2d.drawImage(speechBoxes[menuState], 0, 260, null);
						cur = mainScript[menuState];
						curSlides = cur.getSlides();
					}
					else {
						g2d.drawImage(speechBoxes[0], 0, 434, null);
						cur = interactablesScript[menuState].get(interactableScript);
						curSlides = cur.getSlides();
					}
					if (speakingInd == cur.getSlidesSize()) {
						choosing = false;
						speakingInd = 0;
						speaking = false;

						if (menuState == 1 && interactableScript == 0 && choice) {
							menuState = 2;
							System.out.println("new world");
							mapX = 902;
							mapY = 644;
							playerIndex = 1;
							speaking = true;
						}
						else if (menuState == 2 && choice && scriptRead[menuState]) {
							try {
								sound.playSoundEffect(2);
								String curItem = interactablesScript[menuState].get(interactableScript).getName();
								backpack.putIfAbsent(curItem, 0);
								backpack.put(curItem, backpack.get(curItem) + 1);
								System.out.println("bagged");
							} catch (LineUnavailableException | IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						else {
							scriptRead[menuState] = true;
							choice = true;
						}
						

					}
					else {
						g2d.drawString(curSlides.get(speakingInd)[0], 25, 480);
						g2d.drawString(curSlides.get(speakingInd)[1], 25, 520);
						g2d.drawString(curSlides.get(speakingInd)[2], 25, 560);			
						System.out.println(choosing);
						if (speakingInd == cur.getSlidesSize() - 1 && choosing) {			
							System.out.println("wagnan");
							int posx = (choice) ? 200: 525;
							g2d.drawImage(selector, posx, 540, null);
						
						}
					}
				}

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void run() {
		while(true) {
			try {
				Thread.sleep(17);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int posX = mapX;
			int posY = mapY;
			if (menuState == 0 && submenuOption == 0) {
				if (within(new Rectangle(416, 93, 63, 49), getMousePosition()) || within(new Rectangle(424, 59, 47, 33), getMousePosition()) && !hoveringSecret) {
					System.out.println("hovering");
					hoveringSecret = true;
				}
				else {
					hoveringSecret = false;
				}
				repaint();
			}
			else if (menuState > 0) {
				if(up && withinBounds(bounds[menuState], new Point(posX, posY - charSpeed))) {
					Player.key = 1;
					// playerY -= 10;
					mapY -= charSpeed;
					Player.run();
					System.out.println(mapX + " " + mapY);
					yCounter = (yCounter - 1) % 1000;


				}
				if(down && withinBounds(bounds[menuState], new Point(posX, posY + charSpeed))) {
					Player.key = 3;
					// playerY += 10;
					mapY += charSpeed;
					Player.run();
					System.out.println(mapX + " " + mapY);
					yCounter = (yCounter + 1) % 1000;


				}
				if(left && withinBounds(bounds[menuState], new Point(posX - charSpeed, posY))) {
					Player.key = 2;
					// playerX -= 10;
					mapX -= charSpeed;
					Player.run();
					System.out.println(mapX + " " + mapY);
					xCounter = (xCounter - 1) % 1000;

				}
				if(right && withinBounds(bounds[menuState], new Point(posX + charSpeed, posY))) {
					Player.key = 4;
					// playerX += 10;
					mapX += charSpeed;
					Player.run();
					System.out.println(mapX + " " + mapY);
					xCounter = (xCounter + 1) % 1000;

				}
				
				// if they have left 1 quadrant of the whitespace
				if (menuState == 1) {
					if (mapY < 800) {
						mapY = 1600;
					}
					if (mapY > 1600) {
						mapY = 800;
					}
					if (mapX < 450) {
						mapX = 1650;
					}
					if (mapX > 1650) {
						mapX = 450;
					}
					
				}
				
				// map
				if(menuState == 2) {
					// teleport thing 1
					if(within(entrances[2].get(0), new Point(mapX, mapY))) {
						System.out.println("in gate");
						up = false;
						down = false;
						right = false;
						left = false;
						mapX = 2816;
						mapY = 2128;
						
					}
					// teleport thing 2
					else if(within(entrances[2].get(1), new Point(mapX, mapY))) {
						System.out.println("in other gate 2");
						up = false;
						down = false;
						right = false;
						left = false;
						mapX = 1560;
						mapY = 984;
						
					}
					else if(within(entrances[2].get(2), new Point(mapX, mapY))) {
						System.out.println("exit map into black space");
						
						up = false;
						down = false;
						right = false;
						left = false;
						mapX = 1580;
						mapY = 1690;
						menuState = 3;
					}
				}
				
				// black space
				if (menuState == 3) {
					if (mapY < 1000) {
						mapY = 2394;
					}
					if (mapY > 2394) {
						mapY = 1000;
					}
					if (mapX < 700) {
						mapX = 2700;
					}
					if (mapX > 2700) {
						mapX = 700;
					}
				}
				
			}
		}
	}

	public boolean within(Rectangle r, Point p) {
		if (p == null) return false;
		if (r.x < p.x && p.x < r.x + r.width) {
			if (r.y < p.y && p.y < r.y + r.height) {
				return true;
			}
		}
		return false;
	}
	
	// this looks the same BUT there is equals.
	// we need this because within() wouldn't work without the equals.
	// the character doesn't move into obstacles, but they can
	// go right up to the side as defined by their coordinates in the constructor.
	// we need interact() to see if the character is at the side
	
	
	public boolean interact(Rectangle r, Point p) {
		if (r.x - 1<= p.x && p.x <= r.x + r.width + 1) {
			if (r.y - 1 <= p.y && p.y <= r.y + r.height + 1) {
				return true;
			}
		}
		return false;
	}
	public boolean withinBounds(ArrayList<Rectangle>[] r, Point p) {
		
		// if we are on top of an interactable object
		for (Rectangle i: r[0]) {
			if (within(i, p)) {
				return false;
			}
		}
		// if we are within the boundaries
		for (Rectangle i: r[1]) {
			if (within(i, p)) {
				return true;
			}
		}
		// otherwise we must be out of bounds
		return false;
	}
	

	@Override
	public void mousePressed(MouseEvent e){
		mouseX = e.getX();
		mouseY = e.getY();
		try {
			if (menuState == 0) {
				if (submenuOption == 0) {
					
					// play game
					if (within(new Rectangle(142, 545, 169, 40), getMousePosition())) {
						menuState = 1;
						System.out.println("clicked");
						mapX = 1050;
						mapY = 1200;
						sound.playSoundEffect(4);
					}
					// options
					else if (within(new Rectangle(381, 545, 155, 40), getMousePosition())) {
						submenuOption = 1;
						sound.playSoundEffect(4);
					}		
					
					// quit
					else if (within(new Rectangle(616, 545, 139, 40), getMousePosition())) {
						sound.playSoundEffect(4);
						System.exit(0);
					}
					else if (hoveringSecret) {
						submenuOption = 2;
						sound.playSoundEffect(4);
					}
				}
				else if (submenuOption == 1 || submenuOption == 2) {
					if (within(new Rectangle(45, 548, 169, 40), getMousePosition())) {
						submenuOption = 0;
						sound.playSoundEffect(4);
					}
				}
				
			}
			repaint();	
		}
		catch (LineUnavailableException | IOException e1) {
			e1.printStackTrace();
		}
		
		
		
	}
	
	
	
	
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) throws NullPointerException{
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub 
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		if (menuState > 0) {
			// w
			if(key == 38 && !speaking) {
				up = true;
			}
			// a
			else if(key == 37) {
				
				if (speaking) choice = true;
				else left = true;
			}
			// s
			else if(key == 40 && !speaking) {
				down = true;

			}
			// d
			else if(key == 39) {
				if (speaking) choice = false;
				else right = true;

			}
			// z interact
			if (key == 90 && !speaking) {
				int a = mapX;
				int b = mapY;
				for (int i = 0; i < interactables[menuState].size(); i++) {
					if (interact(interactables[menuState].get(i), new Point(a, b))) {
						System.out.println("Interacted");
						try {
							sound.playSoundEffect(7);
						} catch (LineUnavailableException | IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						if (interactablesScript[menuState].get(i) != null) {
							speaking = true;
							interactableScript = i;
							System.out.println("yay");
						
							// if the interactable prompts the user to make a choice
							if (interactablesScript[menuState].get(i).getChoice()) {
								choosing = true;
							}
						}
					}
				}
			}
			// x flip page / script
			else if (key == 88) {
				if (speaking) {
					speakingInd++;
					System.out.println('x');
				}
			}
			else if (key == 16 && !speaking) {
				charSpeed = 4;
			}
		}
		repaint();
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		int key = e.getKeyCode();
		if (menuState > 0 && !speaking) {
			// w
			if(key == 38) {
				System.out.println("w releaes");
				up = false;
				if(!up && !down && !left && !right) playerIndex = 10;
				yCounter = 0;
			}
			// a
			else if(key == 37) {
				
				System.out.println("a relaes");
				left = false;
				if(!up && !down && !left && !right) playerIndex = 4;
				xCounter = 0;
			}
			// s
			else if(key == 40) {
				System.out.println("s release");
				down = false;
				if(!up && !down && !left && !right) playerIndex = 1;
				yCounter = 0;
			}
			// d
			else if(key == 39) {
				System.out.println("d release");
				right = false;
				if(!up && !down && !left && !right) playerIndex = 7;
				xCounter = 0;
			}
			else if (key == 16) {
				charSpeed = 2;
			}
		}
		repaint();
	}



}
