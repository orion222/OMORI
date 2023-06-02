
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
	int playerX = 100, playerY = 100;
	
	Player Player = new Player(this);
	public static int menuState = 0;
	public static Thread thread;
	public static int mapX = 0;
	public static int mapY = 0;	
	public static BufferedImage[] screens = new BufferedImage[5];
	
	public static int mouseX;
	public static int mouseY;

	
	public Main() {
		setPreferredSize(new Dimension(900, 600));
		setBackground(new Color(200, 0, 0));
		addKeyListener(this);
		addMouseListener(this);
		this.setFocusable(true);
		
		try {
			for(int i = 0; i < 12; i++) {
				Image image = ImageIO.read(new File("runAnimation/" + (i+1) + ".png"));
				if(i >= 3 && i <= 8) {
					Image newImage = image.getScaledInstance(125, 132,  java.awt.Image.SCALE_SMOOTH);
					image = newImage;
				}
				playerImages.add(image);
			}
			screens[0] = ImageIO.read(new File("assets/gameScreens/titlescreen.png"));

		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
		if (menuState == 0) {
			g2d.drawImage(screens[0], mapX, mapY, null);

		}
		else if (menuState > 0) {
			try {
				Thread.sleep(60);
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
				Thread.sleep(1000/20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (menuState > 0) {
				
				if(up) {
					Player.key = 1;
					playerY -= 10;
					Player.run();
				}
				if(down) {
					Player.key = 3;
					playerY += 10;
					Player.run();
				}
				if(left) {
					Player.key = 2;
					playerX -= 10;
					Player.run();
				}
				if(right) {
					Player.key = 4;
					playerX += 10;
					Player.run();
				}
		
				
			
			}
		}
			
	}

	public boolean clickedWithin(Rectangle r) {
		return r.contains(getMousePosition());
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		
		if (menuState == 0) {
			// play game
			if (clickedWithin(new Rectangle(142, 545, 169, 40))) {
				menuState = 1;
				System.out.println("clicked");
				
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
			if(key == 87) {
				System.out.println("w");
				up = true;
			}
			// a
			else if(key == 65) {
				System.out.println("a");
				left = true;
			}
			// s
			else if(key == 83) {
				System.out.println("s");
				down = true;
			}
			// d
			else if(key == 68) {
				System.out.println("d");
				right = true;
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
			if(key == 87) {
				System.out.println("w releaes");
				up = false;
				playerIndex = 10;
			}
			// a
			else if(key == 65) {
				System.out.println("a relaes");
				left = false;
				playerIndex = 4;
			}
			// s
			else if(key == 83) {
				System.out.println("s release");
				down = false;
				playerIndex = 1;
			}
			// d
			else if(key == 68) {
				System.out.println("d release");
				right = false;
				playerIndex = 7;
			}
		}
		repaint();
	}
}
