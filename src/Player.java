
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

public class Player extends JPanel implements Runnable, ActionListener{

	
	private int HP;
	private ArrayList<String> attacks = new ArrayList<>();
	private ArrayList<String> items = new ArrayList<>();
	public Timer timer = new Timer(250, this);

	public Main game;
	

	public Player(Main Main) {
		game = Main;
	}

	int key = 0;
	boolean legUp, legDown, legLeft, legRight;
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		run();
	}
	

	@Override
	public void run() {
		
		// TODO Auto-generated method stub
		if(key == 1 && game.yCounter % (50 / game.charSpeed) == 0) {
			if(game.left == false && game.right == false) {
				if(legUp) game.playerIndex = 9;
				else game.playerIndex = 11;
				legUp = !legUp;
			}
		}
		else if(key == 2 && game.xCounter % (50 / game.charSpeed) == 0) {
			if(legLeft) game.playerIndex = 3;
			else game.playerIndex = 5;
			legLeft = !legLeft;
		}
		else if(key == 3 && game.yCounter % (50 / game.charSpeed) == 0) {
			if(game.left == false && game.right == false) {
				if(legDown) game.playerIndex = 0;
				else game.playerIndex = 2;
				legDown = !legDown;
			}
		}
		else if (key == 4 && game.xCounter % (50 / game.charSpeed) == 0){
			if(legRight) game.playerIndex = 6;
			else game.playerIndex = 8;
			legRight = !legRight;
		}
		game.repaint();
	}

}