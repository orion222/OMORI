
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

public class hi extends JPanel implements KeyListener, MouseListener, Runnable, ActionListener{

	boolean up, down, left, right; // movement in a direction
	ArrayList<Image> playerImages = new ArrayList<>(); // arraylist of player images
	int playerIndex = 1; // which image to display of player
	int playerX = 400, playerY = 250;
	
	ye ye = new ye(this);
	public static int menuState = 0;
	public static Thread thread;
	public static int mapX = 0;
	public static int mapY = 0;	
	public static BufferedImage[] screens = new BufferedImage[5];
	public static BufferedImage[] speechBoxes = new BufferedImage[3];
	public static int mouseX;
	public static int mouseY;
	public static int windowWidth = 900;
	public static int windowHeight = 600;
	
	static boolean speaking = false;
	static int speakingInd = 0;
	static boolean[] scriptRead = new boolean[4];
	static int charHeight = 66;
	static int charWidth = 63;
	static int charSpeed = 2;
	public static ArrayList<Rectangle>[] interactables = new ArrayList[4];
	public static ArrayList<Rectangle>[][] bounds = new ArrayList[4][2];
	
	static Font speakingFont;
	static Timer timer;
	public hi() {
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
			screens[0] = ImageIO.read(new File("assets/gameScreens/titlescreen.png"));
			screens[1] = ImageIO.read(new File("assets/gameScreens/whitespace2.png"));
			screens[2] = ImageIO.read(new File("assets/gameScreens/omorimap2.png"));	
		    
			speechBoxes[1] = ImageIO.read(new File("assets/scripts/sunny.png"));
			speechBoxes[2] = ImageIO.read(new File("assets/scripts/kel.png"));
			speakingFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/OMORI_GAME2.ttf")).deriveFont(50f);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("fail");
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < 4; i++) {
			interactables[i] = new ArrayList<Rectangle>();
			for (int x = 0; x < 2; x++) {
				bounds[i][x] = new ArrayList<Rectangle>();
			}
		}
//		interactables[1].add(new Rectangle(980, 1120, 40, 80)); // the pills
//		interactables[1].add(new Rectangle(1140, 1120, 60, 80)); // the book
//		interactables[1].add(new Rectangle(980, 1260, 60, 70)); // the cat
//		
		bounds[1][0] = interactables[1];
//		bounds[2][1].add(new Rectangle(0, 0, 5000, 5000));
		bounds[2][1].add(new Rectangle(876, 614, 52, 220));
		bounds[2][1].add(new Rectangle(834, 810, 130, 150));
		bounds[2][1].add(new Rectangle(784, 922, 416, 656));
		
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
		bounds[2][1].add(new Rectangle(2588, 970, 372, 92)); // 
		bounds[2][1].add(new Rectangle(2830, 1060, 28, 372));
		bounds[2][1].add(new Rectangle(2664, 1430, 305, 34));
		bounds[2][1].add(new Rectangle(2946, 1462, 24, 114)); //
		bounds[2][1].add(new Rectangle(2968, 1540, 400, 38)); //ee
		bounds[2][1].add(new Rectangle(3354, 1316, 14, 234)); // up
		bounds[2][1].add(new Rectangle(3354, 1316, 130, 28));
		bounds[2][1].add(new Rectangle(3466, 1200, 18, 140));
		bounds[2][1].add(new Rectangle(3466, 1200, 190, 34));
		bounds[2][1].add(new Rectangle(3638, 1232, 18, 772)); //
		
		bounds[2][1].add(new Rectangle(3496, 1942, 144, 62));
		bounds[2][1].add(new Rectangle(3122, 1942, 378, 36)); //
		bounds[2][1].add(new Rectangle(3122, 1826, 14, 118));
		bounds[2][1].add(new Rectangle(2594, 1826, 542, 36)); //
		
		bounds[2][1].add(new Rectangle(3654, 1998, 358, 8));
		bounds[2][1].add(new Rectangle(3910, 1998, 98, 400));
		bounds[2][1].add(new Rectangle(3860, 2176, 100, 100));
		bounds[2][1].add(new Rectangle(3738, 2220, 200, 150));
		bounds[2][1].add(new Rectangle(3502, 2286, 506, 814));
		
		
		bounds[2][1].add(new Rectangle(2594, 1826, 30, 100));
		bounds[2][1].add(new Rectangle(2556, 1882, 68, 690));
		bounds[2][1].add(new Rectangle(2380, 2570, 656, 32)); // adjust this
		
		bounds[2][1].add(new Rectangle(2552, 2600, 14, 702));
		bounds[2][1].add(new Rectangle(2552, 3300, 156, 204));
		bounds[2][1].add(new Rectangle(2408, 3356, 146, 148));
		
		bounds[2][1].add(new Rectangle(2542, 3448, 32, 376));
		bounds[2][1].add(new Rectangle(2482, 3594, 144, 134));
		bounds[2][1].add(new Rectangle(2498, 3816, 122, 464));
		
		bounds[2][1].add(new Rectangle(1530, 956, 60, 16));
		// 1532 958 -- 1588 958 // teleport gate thing above fence
		
		
		thread = new Thread(this);
		thread.start();
		timer = new Timer(250, this);
	}
	
	public static void main(String[] args) {
		System.out.println("bop");
		JFrame frame = new JFrame("OMORI");
		hi panel = new hi();
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);

	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setFont(speakingFont);
		if (menuState == 0) {
			g2d.drawImage(screens[0], 0, 0, null);

		}
		else if (menuState == 1) {
			g2d.drawImage(screens[1], -1 * mapX, -1 * mapY, null);
			g2d.drawImage(playerImages.get(playerIndex), playerX, playerY, null);
			
			if (!scriptRead[menuState]) {
				g2d.drawImage(speechBoxes[1], 0, 260, null);
			}
		}
		else if(menuState == 2) {
			g2d.drawImage(screens[2], -1 * mapX, -1 * mapY, null);
			g2d.drawImage(playerImages.get(playerIndex), playerX, playerY, null);
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
			int posX = (menuState == 1 || menuState == 2) ? mapX: playerX;
			int posY = (menuState == 1 || menuState == 2) ? mapY: playerY;			
			if (menuState > 0) {
				if(up && withinBounds(bounds[menuState], new Point(posX, posY - charSpeed))) {
					ye.key = 1;
					// playerY -= 10;
					mapY -= charSpeed;
					ye.timer.start();


				}
				if(down && withinBounds(bounds[menuState], new Point(posX, posY + charSpeed))) {
					ye.key = 3;
					// playerY += 10;
					mapY += charSpeed;
					ye.timer.start();



				}
				if(left && withinBounds(bounds[menuState], new Point(posX - charSpeed, posY))) {
					ye.key = 2;
					// playerX -= 10;
					mapX -= charSpeed;
					ye.timer.start();



				}
				if(right && withinBounds(bounds[menuState], new Point(posX + charSpeed, posY))) {
					ye.key = 4;
					// playerX += 10;
					mapX += charSpeed;
					ye.timer.start();

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
				
			}
		}
	}

	public boolean within(Rectangle r, Point p) {
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
		if (r.x <= p.x && p.x <= r.x + r.width) {
			if (r.y <= p.y && p.y <= r.y + r.height) {
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
	public void mousePressed(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		
		if (menuState == 0) {
			// play game
			if (within(new Rectangle(142, 545, 169, 40), getMousePosition())) {
				menuState = 2; // for map
				System.out.println("clicked");
				mapX = 902;
				mapY = 644;
				
				// 902
				// 644
				
			}			
		}
		repaint();	
		
		
	}
	
	
	
	
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
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
		System.out.println(mapX + " " + mapY);
		int key = e.getKeyCode();
		if (menuState > 0) {
			// w
			if(key == 38) {
				up = true;
			}
			// a
			else if(key == 37) {
				left = true;

			}
			// s
			else if(key == 40) {
				down = true;

			}
			// d
			else if(key == 39) {
				right = true;

			}
			else if (key == 90) {
				int a = (menuState == 1) ? mapX: playerX;
				int b = (menuState == 1) ? mapY: playerY;
				for (Rectangle i: interactables[menuState]) {
					if (interact(i, new Point(a, b))) {
						System.out.println("Interacted");
					}
				}
			}
		}
		repaint();
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		int key = e.getKeyCode();
		if (menuState > 0) {
			// w
			if(key == 38) {
				System.out.println("w releaes");
				up = false;
				playerIndex = 10;
			}
			// a
			else if(key == 37) {
				System.out.println("a relaes");
				left = false;
				playerIndex = 4;
			}
			// s
			else if(key == 40) {
				System.out.println("s release");
				down = false;
				playerIndex = 1;
			}
			// d
			else if(key == 39) {
				System.out.println("d release");
				right = false;
				playerIndex = 7;
			}
			ye.timer.stop();
		}
		repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}


}
