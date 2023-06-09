
import javax.swing.*;


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
	
	Player Player = new Player(this);
	public static int menuState = 0;
	public static Thread thread;
	public static int mapX = 0;
	public static int mapY = 0;	
	public static BufferedImage[] screens = new BufferedImage[5];
	
	public static int mouseX;
	public static int mouseY;
	public static int windowWidth = 900;
	public static int windowHeight = 600;

	static int charHeight = 66;
	static int charWidth = 63;
	static int charSpeed = 2;
	public static ArrayList<Rectangle>[] interactables = new ArrayList[4];
	public static ArrayList<Rectangle>[] bounds = new ArrayList[4];
	
	static Font speakingFont;
	public Main() {
		setPreferredSize(new Dimension(900, 600));
		setBackground(new Color(200, 0, 0));
		addKeyListener(this);
		addMouseListener(this);
		this.setFocusable(true);
		
		try {
			for(int i = 0; i < 12; i++) {
				Image image = ImageIO.read(new File("runAnimation/" + (i+1) + ".png"));
				Image newImage = image.getScaledInstance(charWidth, charHeight,  java.awt.Image.SCALE_SMOOTH);
				image = newImage;
				
				playerImages.add(image);
			}
			screens[0] = ImageIO.read(new File("assets/gameScreens/titlescreen.png"));
			screens[1] = ImageIO.read(new File("assets/gameScreens/whitespace2.png"));
			
			speakingFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/OMORI_GAME2.ttf")).deriveFont(50f);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < 4; i++) {
			interactables[i] = new ArrayList<Rectangle>();
			bounds[i] = new ArrayList<Rectangle>();
		}
		interactables[1].add(new Rectangle(980, 1120, 40, 80)); // the pills
		interactables[1].add(new Rectangle(1140, 1120, 60, 80)); // the book
		interactables[1].add(new Rectangle(980, 1260, 60, 70)); // the cat
		
		bounds[1].addAll(interactables[1]);
		
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
		if (menuState == 0) {
			g2d.drawImage(screens[0], 0, 0, null);

		}
		else if (menuState > 0) {
			try {
				Thread.sleep(17);
				g2d.drawImage(screens[1], -1 * mapX, -1 * mapY, null);
				g2d.drawImage(playerImages.get(playerIndex), playerX, playerY, null);
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
			int posX = (menuState == 1) ? mapX: playerX;
			int posY = (menuState == 1) ? mapY: playerY;			
			if (menuState > 0) {
				if(up && withinBounds(bounds[menuState], new Point(posX, posY - charSpeed))) {
					Player.key = 1;
					// playerY -= 10;
					mapY -= charSpeed;
					Player.run();

				}
				if(down && withinBounds(bounds[menuState], new Point(posX, posY + charSpeed))) {
					Player.key = 3;
					// playerY += 10;
					mapY += charSpeed;
					Player.run();

				}
				if(left && withinBounds(bounds[menuState], new Point(posX - charSpeed, posY))) {
					Player.key = 2;
					// playerX -= 10;
					mapX -= charSpeed;
					Player.run();

				}
				if(right && withinBounds(bounds[menuState], new Point(posX + charSpeed, posY))) {
					Player.key = 4;
					// playerX += 10;
					mapX += charSpeed;
					Player.run();

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
	public boolean withinBounds(ArrayList<Rectangle> r, Point p) {
		
		for (Rectangle i: r) {
			if (within(i, p)) {
				return false;
			}
		}
		return true;
	}
	

	@Override
	public void mousePressed(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		
		if (menuState == 0) {
			// play game
			if (within(new Rectangle(142, 545, 169, 40), getMousePosition())) {
				menuState = 1;
				System.out.println("clicked");
				mapX = 1050;
				mapY = 1200;
				
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
		}
		repaint();
	}
}
