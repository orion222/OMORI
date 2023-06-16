
import java.util.*;
import java.io.*;
import javax.sound.sampled.*;
public class Audio {
	
	public Main game;
	AudioInputStream[] mapSongs = new AudioInputStream[4];
	ArrayList<AudioInputStream> interactSounds = new ArrayList<AudioInputStream>();
	public static Clip settingMusic;
	public Audio(Main e) {
		game = e;
		try {
			mapSongs[0] = AudioSystem.getAudioInputStream(new File("assets/music/01. Title.wav"));
			mapSongs[1] = AudioSystem.getAudioInputStream(new File("assets/music/02.  WHITE SPACE.wav"));
			mapSongs[2] = AudioSystem.getAudioInputStream(new File("assets/music/17. Forest Chillin'.wav"));
		}
		catch (IOException | UnsupportedAudioFileException e1) {
			e1.printStackTrace();
		}
	}
	

	public void playSettingMusic(int menuState) throws LineUnavailableException, IOException {
		settingMusic = AudioSystem.getClip();
		settingMusic.open(mapSongs[menuState]);
		settingMusic.start();
		settingMusic.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public void playInteractableSound(int n) {
		
	}


}
