/*
 * ISU4U - CULMINATING PROJECT
 * June 18 2023
 * -- OMORI
 * 
 * Created by Orion Chen and Nicky Lam.
 * 
 * This game is an experience of a depressive episode. 
 * This game is a 2D adventure game where you must explore your surroundings to proceed in the story line.
 * You will be controlling a character who is named SUNNY located in WHITESPACE and need to find a way to start the game. 
 * Once you begin the game, you are now a delusion of SUNNY named OMORI. 
 * 
 * Nicky pulled an all nighter to finish battle.
 * 
 * 
 * 
 */
import javax.swing.*;
import javax.swing.Timer;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.util.*;

public class Main extends JPanel implements KeyListener, MouseListener, Runnable{

	public static JFrame frame;
	public static Container con;
	public static Main panel;
	
	// player variables
	boolean up, down, left, right; // movement in a direction
	ArrayList<Image> playerImages = new ArrayList<>(); // arraylist of player images
	public static Thread thread;
	public static int mapX = 0;
	public static int mapY = 0;	
	int playerIndex = 1; // which image to display of player
	int playerX = 400, playerY = 250;
	int xCounter = 0;
	int yCounter = 0;  // mod 100 
	static int charHeight = 66;
	static int charWidth = 63;
	static int charSpeed = 2;
	
	
	// for the scare animation
	static int scareX = 0;
	static int scareY = 0;
	static boolean showScare = false;
	static int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
	static int direction;
	static int curScare;
	public static BufferedImage[] scareImages = new BufferedImage[5];
	
	
	// menu variables
	Player Player = new Player(this);
	public static int menuState = 0;
	public static BufferedImage title2;
	public static boolean hoveringSecret = false;
	public static int submenuOption = 0;
	public static BufferedImage optionsMenu;
	public static BufferedImage aboutMenu;
	public static BufferedImage inventoryMenu;
	public static boolean showInventory = false;
	
	// images
	public static BufferedImage[] screens = new BufferedImage[5];
	public static BufferedImage[] speechBoxes = new BufferedImage[5];
	public static int mouseX;
	public static int mouseY;
	public static int windowWidth = 900;
	public static int windowHeight = 600;
	
	// script reading
	static boolean speaking;
	static int speakingInd = 0;
	static boolean[] scriptRead = new boolean[5];
	static Interactable[] mainScript = new Interactable[5];
	static BufferedImage selector;
	static ArrayList<Interactable>[] interactablesScript = new ArrayList[5];
	static int interactableScript = -1;
	static boolean choosing = false;
	static boolean choice = true;
	
	// map boundaries / interacts
	public static ArrayList<Rectangle>[] interactables = new ArrayList[5];
	public static ArrayList<Rectangle>[][] bounds = new ArrayList[5][2];
	public static ArrayList<Rectangle>[] entrances = new ArrayList[5];
	
	static Font speakingFont;
	static Font blackspaceFont;
	static Font inventoryFont;
	
	Audio sound = new Audio();
	static boolean[] settingSongPlayed = new boolean[5];
	
	// battle variables
	public static boolean fight = false; // fighting or not
	public static int fightState = 1; // 1 --> fight or run? // 2 --> attack or snack? // 3 --> snacks
	public static int damage = 0;
	public static int appear = 0; // initial animation
	public static boolean turn = true; // initial turn will be player's -- false is boss'
	public static int curPosX = 0; //
	public static int tempX = 400; // animation purposes
	public static int tempY = 250; //
	public static BufferedImage[] fightImages = new BufferedImage[13];
	public static int playerHealth = 125; // player initial hp
	public static int bossHealth = 200; // boss initial hp
	public static String[] bossDialogue = new String[7];
	public static int dialogue = 0;
	public static BufferedImage[] damageText = new BufferedImage[10];
	public static BufferedImage gameOver;
	public static AudioInputStream music, music2;
	public static Clip bossMusic, gameOverMusic; // boss bg music and game over music
	public static boolean test = false; // to replay bg music after defeating boss
	
	static boolean[] healsVisited = new boolean[6];
	static boolean[] doorsVisited = new boolean[10];  
	HashMap<String, Integer> backpack = new HashMap<String, Integer>();
	
	public Main() throws UnsupportedAudioFileException, LineUnavailableException {
		setPreferredSize(new Dimension(windowWidth, windowHeight));
		setBackground(new Color(200, 0, 0));
		addKeyListener(this);
		addMouseListener(this);
		this.setFocusable(true);
		
		// all imports
		try {
			for(int i = 0; i < 12; i++) {
				Image image = ImageIO.read(new File("assets/runAnimation/" + (i+1) + ".png"));
				Image newImage = image.getScaledInstance(charWidth, charHeight,  java.awt.Image.SCALE_SMOOTH);
				image = newImage;
				playerImages.add(image);
			}

			for (int i = 0; i < 5; i++) {
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
			inventoryMenu = ImageIO.read(new File("assets/gameScreens/inventoryMenu.png"));
			screens[0] = ImageIO.read(new File("assets/gameScreens/titleScreen.png"));
			screens[1] = ImageIO.read(new File("assets/gameScreens/whitespace2.png"));
			screens[2] = ImageIO.read(new File("assets/gameScreens/mapwithboss.png"));
			screens[3] = ImageIO.read(new File("assets/gameScreens/blackspace.png"));
			screens[4] = ImageIO.read(new File("assets/gameScreens/winScreen.png"));
			speechBoxes[0] = ImageIO.read(new File("assets/scripts/speechBox.png"));
			speechBoxes[1] = ImageIO.read(new File("assets/scripts/sunny.png"));
			speechBoxes[2] = ImageIO.read(new File("assets/scripts/kel.png"));
			speechBoxes[3] = ImageIO.read(new File("assets/scripts/sunnyScared.png"));
			selector = ImageIO.read(new File("assets/scripts/select.png"));
			speakingFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/OMORI_GAME2.ttf")).deriveFont(50f);
			inventoryFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/OMORI_GAME2.ttf")).deriveFont(30f);
			blackspaceFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/OMORI_GAME.ttf")).deriveFont(50f);

			fightImages[0] = ImageIO.read(new File("assets/boss/bossDialogue.png"));
			fightImages[1] = ImageIO.read(new File("assets/boss/fightorrun.png"));	
			fightImages[2] = ImageIO.read(new File("assets/boss/fightselected.png"));	
			fightImages[3] = ImageIO.read(new File("assets/boss/runSelected.png"));	
			fightImages[4] = ImageIO.read(new File("assets/boss/attackorsnack.png"));	
			fightImages[5] = ImageIO.read(new File("assets/boss/attackSelected.png"));	
			fightImages[6] = ImageIO.read(new File("assets/boss/snackSelected.png"));	
			fightImages[7] = ImageIO.read(new File("assets/boss/snackChoices.png"));	
			fightImages[8] = ImageIO.read(new File("assets/boss/selectGreen.png"));
			fightImages[9] = ImageIO.read(new File("assets/boss/selectBlue.png"));
			fightImages[10] = ImageIO.read(new File("assets/boss/selectPicnic.png"));
			fightImages[11] = ImageIO.read(new File("assets/boss/selectChicken.png"));
			fightImages[12] = ImageIO.read(new File("assets/boss/selectBack.png"));
			
			bossDialogue[0] = "GWAHAHAHAHAHAHA!!!";
			bossDialogue[1] = "Hmph... you need to do better.";
			bossDialogue[2] = "I'll take you out!";
			bossDialogue[3] = "Hmph... you lack training.";
			bossDialogue[4] = "I won't go down so easily!";
			bossDialogue[5] = "You got lucky...";
			bossDialogue[6] = "You never had a chance!";
			
			gameOver = ImageIO.read(new File("assets/gameScreens/gameOver.png"));
			
			music = AudioSystem.getAudioInputStream(new File("assets/music/bossMusic.wav"));
			bossMusic = AudioSystem.getClip();
		    bossMusic.open(music);
		    
		    music2 = AudioSystem.getAudioInputStream(new File("assets/music/over.wav"));
			gameOverMusic = AudioSystem.getClip();
		    gameOverMusic.open(music2);
		    
			
			
			for(int i = 0; i < 10; i++) {
				damageText[i] = ImageIO.read(new File("assets/boss/numbers/" + i + ".png"));
			}
			
			for (int i = 1; i <= 5; i++) {
				scareImages[i - 1] = ImageIO.read(new File("assets/scareImages/scare" + i + ".png"));	

			}
			
			// script reading
			for (int i = 1; i < 4; i++) {
				mainScript[i] = new Interactable(new BufferedReader(new FileReader("assets/scripts/script" + i + ".txt")), false);
			}
			interactablesScript[1].add(new Interactable("Unknown pills. They're labelled HIGHLY POISONOUS. Take them?", true, "pills", 0));
			interactablesScript[1].add(new Interactable(new BufferedReader(new FileReader("assets/scripts/journal.txt")), false));
			interactablesScript[1].get(1).getSlides().add(0, new String[] {"SUNNY'S JOURNAL", "", ""});
			interactablesScript[1].add(new Interactable("meow.", false, "cat", 0));
			interactablesScript[2].add(new Interactable("Grab a piece of green melon? By the way, toggle C to view inventory!", true, "Green melon", 20));
			interactablesScript[2].add(new Interactable("Grab a piece of green melon?", true, "Green melon", 20));
			interactablesScript[2].add(new Interactable("Grab the picnic basket?", true, "Picnic", 40));
			interactablesScript[2].add(new Interactable("Grab a slice of chicken?", true, "Chicken", 50));
			interactablesScript[2].add(new Interactable("Grab a piece of blue melon?", true, "Blue melon", 30));
			interactablesScript[2].add(new Interactable("You encounter a strange, unidentifiable... creature? Fight?", true, "boss", 0));
			
			for (int i = 0; i < 6; i++) {
				interactablesScript[3].add(null);

			}
			interactablesScript[3].add(new Interactable("The pill bottle is empty", false, "Pills", 0));
			interactablesScript[3].add(new Interactable("ONE OF THESE DOORS HAVE AN EXIT. FIND IT", false, "Book", 0));
			interactablesScript[3].add(new Interactable("meow", false, "Cat", 0));

			backpack.put("Green melon", 0);
			backpack.put("Blue melon", 0);
			backpack.put("Picnic", 0);
			backpack.put("Chicken", 0);



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

		interactables[2].add(new Rectangle(1156, 860, 60, 52)); // green melon
		interactables[2].add(new Rectangle(650, 1696, 72, 44)); // green melon
		interactables[2].add(new Rectangle(2810, 2150, 72, 72)); // picnic basket
		interactables[2].add(new Rectangle(2916, 2268, 74, 68)); // chicken
		interactables[2].add(new Rectangle(3722, 2604, 68, 68)); // blue melon
		interactables[2].add(new Rectangle(2490, 3946, 150, 10)); // boss
		
		// doors, starting from one on the left. clockwise fashion
		interactables[3].add(new Rectangle(1248, 1624, 82, 80));
		interactables[3].add(new Rectangle(1444, 1432, 82, 80));
		interactables[3].add(new Rectangle(1778, 1480, 82, 80));
		interactables[3].add(new Rectangle(1854, 1748, 82, 80));
		interactables[3].add(new Rectangle(1654, 1926, 82, 80));
		interactables[3].add(new Rectangle(1400, 1890, 82, 80));
		interactables[3].add(new Rectangle(1448, 1594, 56, 74)); // the pills
		interactables[3].add(new Rectangle(1672, 1596, 70, 80)); // the book
		interactables[3].add(new Rectangle(1450, 1782, 90, 70)); // the cat
		
		bounds[1][0] = interactables[1];
		bounds[1][1].add(new Rectangle(0, 0, 3000, 3000));
		
		// map bounds
		
		bounds[2][0] = interactables[2];
		
//		bounds[2][1].add(new Rectangle(0,0,5000,5000)); // for testing purposes
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
	
	public static void main(String[] args) throws UnsupportedAudioFileException, LineUnavailableException {
		System.out.println("bop");
		frame = new JFrame("OMORI");
		panel = new Main();
		panel.setLayout(new FlowLayout());
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);

	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		if (menuState == 3) g2d.setFont(blackspaceFont);
		else g2d.setFont(speakingFont);
		g2d.setColor(new Color(255, 255, 255));
		
		
		
		
	
		// play the setting's theme
		if (!settingSongPlayed[menuState]) {
			try {
				sound.playSettingMusic(menuState);
			} catch (LineUnavailableException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			settingSongPlayed[menuState] = true;
		} 
		
		// main menu 
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
		
		// in game
		else if (menuState > 0 && menuState < 4) {
		
			// when user is fighting boss
			if(fight) {
			    
				// when user is defeated
				if(playerHealth <= 0) {
					playerHealth = 0;
					fightState = 0;
					if (bossMusic != null && bossMusic.isRunning()) {
				        bossMusic.stop();
				    }
				
				}
				// when boss is defeated
				if(bossHealth <= 0) {
					fightState = 0;
					bossHealth = 0;
					if (bossMusic != null && bossMusic.isRunning()) {
				        bossMusic.stop();
				    }
					try {
						screens[2] = ImageIO.read(new File("assets/gameScreens/omorimap2.png"));
					} catch (IOException e) {
						e.printStackTrace();
					}
					if(bounds[2][0].size() == 6) bounds[2][0].remove(5);
					
					
				}
				
				// display boss fight images
				g2d.drawImage(fightImages[fightState], -100, 0, null);
				
				// displaying health of user and boss
				try {
					Font healthFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/OMORI_GAME2.ttf")).deriveFont(30f);
					g2d.setFont(healthFont);
					g2d.setColor(Color.black);
					g2d.drawString(playerHealth + " / 125" , 95, 203);
				} catch (FontFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				g2d.setFont(speakingFont);
				g2d.setColor(Color.white);
				
				g2d.drawString(bossHealth + " / 200", 358, 90);
				
				// when snack menu is pulled up
				if(fightState >= 7) {
					g2d.drawString("x " + backpack.get("Green melon"), 239, 500);
					g2d.drawString("x " + backpack.get("Blue melon"), 400, 500);
					g2d.drawString("x " + backpack.get("Picnic"), 561, 500);
					g2d.drawString("x " + backpack.get("Chicken"), 715, 500);
				}
				
				// when its the boss' turn
				if(!turn) {
					// dialogue if user wins/loses
					if(bossHealth == 0) dialogue = 5;
					else if(playerHealth == 0) dialogue = 6;
					
					// draw the dialogue when the image of boss with dialogue box is displayed
					if(fightState == 0) {
						g2d.drawString(bossDialogue[dialogue], 200, 420);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					// if damage has been dealt, display the damage
					if(damage > 0) {
						String x = damage + "";
						System.out.println("DAMAGE: " + x);
						g2d.drawImage(damageText[Integer.parseInt(x.charAt(0) + "")], tempX, tempY, null);
						g2d.drawImage(damageText[Integer.parseInt(x.charAt(1) + "")], tempX + 25, tempY, null);
						
						damage = 0;
						
						
					}
				}
				
				
			}
			// when player loses, display game over screen and play the music
			else if(playerHealth <= 0) {
				g2d.drawImage(gameOver, 0, -100, null);
				bossMusic.stop();
				bossMusic.close();
				if(!gameOverMusic.isRunning()) {
					gameOverMusic.setFramePosition(0);
					gameOverMusic.start();
				    gameOverMusic.loop(Clip.LOOP_CONTINUOUSLY);
				}
			}
			// when player is not fighting, normal gameplay
			else {
				try {
					
					Thread.sleep(10);
					g2d.drawImage(screens[menuState], -1 * mapX, -1 * mapY, null);
					
					// appear = 1 is animation for boss appearance
					if(appear == 1) {
						System.out.println("CHANGE: " + mapX + " " + mapY + " PLAYER: " + tempX + " " + tempY);
						// tempx and tempy is used to pan the camera towards the boss
						g2d.drawImage(playerImages.get(playerIndex), tempX, tempY, null);
					}
					// display character normally
					else {
						g2d.drawImage(playerImages.get(playerIndex), playerX, playerY, null);
					}
					
					// when user goes pass 3498 on the second map, play the animation
					if(menuState == 2 && mapY >= 3498 && appear == 0) {
						up = false;
						down = false;
						left = false;
						right = false;
						
						appear = 1;
						// curPosX is how far the user is from the x value of 2558
						curPosX = 2558 - mapX;
						System.out.println("boss appear: X: " + curPosX);
	
					}
					// jumpscare animation
					if (showScare) {
						g2d.drawImage(scareImages[curScare], scareX, scareY, null);
					}
					
					// we are in a dialogue frame
					if (speaking) {
						
						Interactable cur = null;
						ArrayList<String[]> curSlides = null; 
						up = false;
						down = false;
						right = false;
						left = false;
						// play the opening script on entry
						if (!scriptRead[menuState]) {
							g2d.drawImage(speechBoxes[menuState], 0, 260, null);
							cur = mainScript[menuState];
							curSlides = cur.getSlides();
						}
						// play the interactable script
						else {
							g2d.drawImage(speechBoxes[0], 0, 434, null);
							cur = interactablesScript[menuState].get(interactableScript);
							curSlides = cur.getSlides();
						}
						// script is finished
						if (speakingInd == cur.getSlidesSize()) {
							choosing = false;
							speakingInd = 0;
							speaking = false;
							
							// if we said yes to take the pills
							// so we transition to menuState 2
							if (menuState == 1 && interactableScript == 0 && choice) {
								menuState = 2;
								System.out.println("new world");
								mapX = 902;
								mapY = 644;
								playerIndex = 1;
								speaking = true;
							}
							// if we said yes to fight the boss
							else if (menuState == 2 && choice && scriptRead[menuState]) {
								if(interactableScript == 5) {
									fight = true;
									sound.settingMusic.stop();
								
									bossMusic.setFramePosition(0);
									bossMusic.start();
								    bossMusic.loop(Clip.LOOP_CONTINUOUSLY);
								    
									System.out.println("start fight");
								}
								
								// otherwise we said yes to grabbing items
								else {
									try {
										sound.playSoundEffect(5);
										String curItem = interactablesScript[menuState].get(interactableScript).getName();
										
										// keep track of items in our backpack which is a hashmap that stores
										// backpack = new HashMap [name of item, frequency]
										backpack.put(curItem, backpack.get(curItem) + 1);
										healsVisited[interactableScript] = true;
										System.out.println("bagged");
									} catch (LineUnavailableException | IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								
							}
							// exit script
							else {
								scriptRead[menuState] = true;
								choice = true;
							}
							

						}
						else {
							// draw the actual text from script on screen
							g2d.drawString(curSlides.get(speakingInd)[0], 25, 480);
							g2d.drawString(curSlides.get(speakingInd)[1], 25, 520);
							g2d.drawString(curSlides.get(speakingInd)[2], 25, 560);			
							
							// if the dialogue allows yes / no choice
							if (speakingInd == cur.getSlidesSize() - 1 && choosing) {			
								g2d.drawImage(selector, (choice) ? 200: 525, 540, null);
							
							}
						}
					}
					
					// show inventory
					else if (showInventory) {
						g2d.setFont(inventoryFont);
						g2d.drawImage(inventoryMenu, 0, 349, null);
						g2d.drawString("x " + backpack.get("Green melon"), 239, 505);
						g2d.drawString("x " + backpack.get("Blue melon"), 400, 505);
						g2d.drawString("x " + backpack.get("Picnic"), 561, 505);
						g2d.drawString("x " + backpack.get("Chicken"), 715, 505);

					}

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		// draw the winning screen
		else {
			g2d.drawImage(screens[4], 0, 0, null);
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
			
			
			// check if hovering on secret area
			if (menuState == 0 && submenuOption == 0) {
				if (within(new Rectangle(416, 93, 63, 49), getMousePosition()) || within(new Rectangle(424, 59, 47, 33), getMousePosition()) && !hoveringSecret) {
					hoveringSecret = true;
				}
				else {
					hoveringSecret = false;
				}
				repaint();
			}
			else if (menuState > 0) {
				// animation of boss
				if(appear == 1) {
					
					// pan camera towards the x-value of 2558
					for(int i = 0; i < (int)Math.abs(curPosX); i++) {
//						System.out.println("CHANGE: " + mapX + " " + mapY + " PLAYER: " + tempX + " " + tempY);
						if(curPosX > 0) {
							mapX += 1; 
							tempX -= 1;
							System.out.println("pos");
						}
						else if(curPosX < 0) {
							mapX -= 1; 
							tempX += 1; System.out.println("negative");
						}
						try {Thread.sleep(20);} 
						catch (InterruptedException e) {e.printStackTrace();}		
						repaint();
					}
					
					// panel camera towards the y-value of the boss
					for(int i = 0; i < 59; i ++) {
						mapY += 10;
						tempY -= 10;
						try {Thread.sleep(20);} 
						catch (InterruptedException e) {e.printStackTrace();}
						repaint();
					}
					
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					// reset camera back to original position
					if(curPosX > 0) {
						mapX -= (int)Math.abs(curPosX);
						mapY -= 590;
					}
					else {
						mapX += (int)Math.abs(curPosX); 
						mapY -= 590;
					}
					
					repaint();
					
					appear = 2;
//					System.out.println(mapX);
				}
				else {
					
					// scare animation
					if (showScare) {
						scareX += directions[direction][0] * 4;
						scareY += directions[direction][1] * 4;
						System.out.println(scareX +  " " + scareY);
						BufferedImage curImage = scareImages[curScare];
						int a = curImage.getWidth();
						int b = curImage.getHeight();
						if (!within(new Rectangle(-1 * a, -1 * b, windowWidth + 2 * a, windowHeight + 2 * b), new Point(scareX, scareY))) {
							System.out.println();
							System.out.println("exit");
							showScare = false;
						}
						repaint();
					}
					
					
					// character movement
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
					
					// if they have left 1 quadrant of whitespace
					// redraw that one quadrant
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
						// if player or boss die, delay
						if(fight && (bossHealth <= 0 || playerHealth <= 0)) {
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							fight = false;
	
						}
						if(fight == false && (bossHealth <= 0 || playerHealth <= 0)) {
							repaint();
							
							if(!test) {
								test = true;
								try {
									sound.playSettingMusic(menuState);
								} catch (IOException | LineUnavailableException e) {
									e.printStackTrace();
								}
								
							}
						}
						if(!turn && bossHealth > 0 && fight) {
							
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							fightState = 0;
							
							// boss' attack
							damage = (int) (Math.random() * (30 - 15 + 1) + 15);
							playerHealth -= damage;
							// 15 - 30 damage - max - min
							
							// for user getting hit .. 220,150 + 225, 150
							tempX = 220;
							tempY = 150;
							try {
								sound.playSoundEffect(11);
							} catch (LineUnavailableException | IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							
							repaint();
							
							try {
								Thread.sleep(800);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							// set turn back to user and change screen to fight/run
							turn = true;
							fightState = 1;
							
							// change dialogue for next occurence
							dialogue++;
							if(dialogue >= 5) dialogue = 0;
							repaint();
							
						}
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
							speaking = true;
						}
					}
					
					
					
					// if they have left 1 quadrant of blackspace
					// redraw that one quadrant
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
	}
	

	
	/*
	 * This method checks if a point is within a rectangle.
	 * Used for checking boundaries and clicking buttons on the menu.
	 */
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
	
	// -1 because the character moves at speed = 2, which is an even number. 
	// if we had boundaries at an odd number we wouldn't be able to interact with them
	// because within() wouldn't let us in the rectangle, so we would always be 1 short of 
	// meeting the perimeter of the rectangle. 
	public boolean interact(Rectangle r, Point p) {
		if (r.x - 1<= p.x && p.x <= r.x + r.width + 1) {
			if (r.y - 1 <= p.y && p.y <= r.y + r.height + 1) {
				return true;
			}
		}
		return false;
	}
	
	/*
	 * checks if the player is within bounds of the map.
	 * Player can only move through defined spaces and NOT through interactables
	 */
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
	/*
	 * all mouse stuff is for the main menu
	 */
	public void mousePressed(MouseEvent e){
		mouseX = e.getX();
		mouseY = e.getY();
		try {
			if (menuState == 0) {
				if (submenuOption == 0) {
					
					// play game
					if (within(new Rectangle(142, 545, 169, 40), getMousePosition())) {
						menuState = 1; // 1
						System.out.println("clicked");
						mapX = 1050; // 1050
						mapY = 1200; // 1200
						speaking = true;
						sound.playSoundEffect(7);
					}
					// options
					else if (within(new Rectangle(381, 545, 155, 40), getMousePosition())) {
						submenuOption = 1;
						sound.playSoundEffect(7);
					}		
					
					// quit
					else if (within(new Rectangle(616, 545, 139, 40), getMousePosition())) {
						sound.playSoundEffect(7);
						System.exit(0);
					}
					else if (hoveringSecret) {
						submenuOption = 2;
						sound.playSoundEffect(7);
					}
				}
				// back
				else if (submenuOption == 1 || submenuOption == 2) {
					if (within(new Rectangle(45, 548, 169, 40), getMousePosition())) {
						submenuOption = 0;
						sound.playSoundEffect(7);
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
		
		try {
			if(fight && turn && damage == 0) {
				// user can select between fighting or running
				if(fightState < 4) {
					if(key == 38) {
						
						sound.playSoundEffect(10);
						fightState = 2; // fight selected
					}
					else if(key == 40) {
						sound.playSoundEffect(10);
						fightState = 3; // run selected
					}
	
					if(key == 88) {
						if(fightState == 2) { // user chooses to fight
							fightState = 4; 
						}
						else if(fightState == 3) { // user chooses to run
							if (bossMusic != null && bossMusic.isRunning()) {
						        bossMusic.stop();
						    }

							sound.mapSongs[2] = AudioSystem.getAudioInputStream(new File("assets/music/ForestChillin.wav"));
							sound.playSettingMusic(menuState);

							fight = false;
							fightState = 1;
						}
					}
				}
				// user can choose between attacking and snacking / gain hp
				else if(fightState >= 4 && fightState <= 6) {
					if(key == 37) {
						sound.playSoundEffect(10);
						fightState = 5; // atack selected
					}
					else if(key == 39) {
						sound.playSoundEffect(10);
						fightState = 6; // snack selected
					}
					
					if(key == 88) {
						if(fightState == 5) {
							// run attack method or soething
							damage = (int) (Math.random() * (40 - 20 + 1) + 20);
							bossHealth -= damage;
							// 20 - 40 damage // max - min + 1 + min
							
							turn = false;
							tempX = 600;
							tempY = 100;
							
							sound.playSoundEffect(11);
							
						}
						else if(fightState == 6) {
							fightState = 7; // open snack menu
							
						}
					}
				}
				// user can select which snack to eat
				else if(fightState >= 7) {
					if(fightState == 7 && (key == 40 || key == 39)) {
						sound.playSoundEffect(10);
						fightState = 8; // green melon
					}
					else if(fightState == 12) {
						if(key == 40) { // go bck down to melon
							sound.playSoundEffect(10);
							fightState = 8;
						}
						else if(key == 88) { // select back
							System.out.println("back");
							fightState = 4;
						}
					}
					
					else if(fightState >= 8) {
						if(key == 37) { // choices move to the left
							sound.playSoundEffect(10);
							fightState--;
							if(fightState <= 7) fightState = 8;
						}
						else if(key == 39) { // choices move to the right
							sound.playSoundEffect(10);
							fightState++;
							if(fightState > 11) fightState = 11;
						}
						else if(key == 38) {
							sound.playSoundEffect(10);
							fightState = 12;
							System.out.println("back");
						}
						else if(key == 88 && playerHealth < 125) {
							boolean valid = false;
							if(fightState == 8) { // select green melon
								System.out.println("green");
								if(backpack.get("Green melon") > 0) {
									playerHealth += 20;
									backpack.put("Green melon", backpack.get("Green melon") - 1);
									valid = true;
								}
							}
							else if(fightState == 9) { // select blue melon
								System.out.println("blue");
								if(backpack.get("Blue melon") > 0) {
									playerHealth += 30;
									backpack.put("Blue melon", backpack.get("Blue melon") - 1);
									valid = true;
								}
							}
							else if(fightState == 10) { // select picnic
								System.out.println("picnic");
								if(backpack.get("Picnic") > 0) {
									playerHealth += 40;
									backpack.put("Picnic", backpack.get("Picnic") - 1);

									valid = true;
								}
							}
							else if(fightState == 11) { // select chicken
								System.out.println("chicken");
								if(backpack.get("Chicken") > 0) {
									playerHealth += 50;
									backpack.put("Chicken", backpack.get("Chicken") - 1);
									valid = true;
								}
							}
							if(valid) {
								sound.playSoundEffect(6);
								turn = false;
								fightState = 0;
							}
							
						}
					}
				}
				
			}
			
			// movement keys
			else if (menuState < 4 && menuState > 0 && appear != 1) {
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
					
					// go through all interactable areas of the menuState and see 
					// if we are actually on top of one
					for (int i = 0; i < interactables[menuState].size(); i++) {
						if (interact(interactables[menuState].get(i), new Point(a, b))) {
							
							// if we visited a heal then don't allow user to get it again
							if (menuState == 2 && healsVisited[i]) continue;
							
							try {
								// the doors will play a lock / unlock sound in BLACKSPACE
								if (menuState == 3 && i < 6) {
									if (!doorsVisited[i]) {
										sound.playSoundEffect(8);
										doorsVisited[i] = true;
										
										// picked the right door --> win the game
										if (i == 5) {
											System.out.println("win");
											menuState = 4;
										}
										// picked the wrong door --> scare
										else {
											scare(i);
											curScare = i;
										}
									}
									// we already visited this door --> play lockedDoor sound
									else sound.playSoundEffect(9);
								}
								// default interact sound
								else {
									sound.playSoundEffect(10);
	
								}
							} catch (LineUnavailableException | IOException e1) { e1.printStackTrace();}
							
							if(menuState != 4) {
								Interactable cur = interactablesScript[menuState].get(i);
								// if we have a script for this interactable, then set the appropriate variables
								if (cur != null) {
									speaking = true;
									interactableScript = i;
									System.out.println("yay");
									
									// if the interactable prompts the user to make a choice
									if (cur.getChoice()) {
										choosing = true;
									}
								}
							}

						}
						
					}
					
				}
				else if (key == 88) {
					
					// x flip page on script 
					if (speaking) {
						speakingInd++;
						System.out.println('x');
					}
					// on win screen. go back to main menu
					if (menuState == 4) {
						menuState = 0;
					}
				}
				// run faster by holding shift
				else if (key == 16 && !speaking) {
					charSpeed = 4;
				}
				// inventory
				else if (key == 67 && !speaking) {
					showInventory ^= true;
				}
			}
		}
		catch (LineUnavailableException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedAudioFileException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		repaint();
		
	}
	
	/*
	 * This method is used in BLACKSPACE, where the user must try to exit the game by using doors.
	 * It loads the scare image by having it travel up, down, left, or right through the screen.
	 * It also plays a scary sound.
	 */
	public void scare(int n) {

		try {
			sound.playSoundEffect(n);
			showScare = true;
			direction = n % 4;
			// right
			if (direction == 0) {
				scareX = -1 *  scareImages[n].getWidth();
				scareY = windowHeight / 2 - scareImages[n].getHeight() / 2;
			}
			// left
			else if (direction == 1) {
				scareX = windowWidth + scareImages[n].getWidth();
				scareY = windowHeight / 2 - scareImages[n].getHeight() / 2;
			}
			// down
			else if (direction == 2) {
				scareX = windowWidth / 2 - scareImages[n].getWidth() / 2;
				scareY = -1 * scareImages[n].getHeight();
			}
			// up 
			else if (direction == 3) {
				scareX = windowWidth / 2 - scareImages[n].getWidth() / 2;
				scareY = windowHeight + scareImages[n].getHeight();
			}
		} catch (LineUnavailableException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		int key = e.getKeyCode();
		if (menuState > 0 && !speaking) {
			
			// all movement. 
			// once a key is released, go back to default standing position
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
			
			// release shift key, go back to walking pace
			else if (key == 16) {
				charSpeed = 2;
			}
		}
		repaint();
	}



}
