
import java.util.*;
import java.io.*;
import javax.sound.sampled.*;
public class Audio {
	
	Main game;
	Test g;
	private static AudioInputStream[] mapSongs = new AudioInputStream[4];
	private static ArrayList<AudioInputStream> soundEffects = new ArrayList<AudioInputStream>();
	private Clip settingMusic;
	private Clip soundEffect;
	public Audio(Main e) {
		game = e;
		try {
			mapSongs[0] = AudioSystem.getAudioInputStream(new File("assets/music/Title.wav"));
			mapSongs[1] = AudioSystem.getAudioInputStream(new File("assets/music/WHITESPACE2.wav"));
			mapSongs[2] = AudioSystem.getAudioInputStream(new File("assets/music/ForestChillin.wav"));
			mapSongs[3] = AudioSystem.getAudioInputStream(new File("assets/music/BLACKSPACE.wav"));
			soundEffects.add(AudioSystem.getAudioInputStream(new File("assets/sounds/scare1.wav")));
			soundEffects.add(AudioSystem.getAudioInputStream(new File("assets/sounds/scare2.wav")));
			soundEffects.add(AudioSystem.getAudioInputStream(new File("assets/sounds/getItem.wav")));
			soundEffects.add(AudioSystem.getAudioInputStream(new File("assets/sounds/heal.wav")));
			soundEffects.add(AudioSystem.getAudioInputStream(new File("assets/sounds/selectOption.wav")));
			soundEffects.add(AudioSystem.getAudioInputStream(new File("assets/sounds/unlockDoor.wav")));
			soundEffects.add(AudioSystem.getAudioInputStream(new File("assets/sounds/lockedDoor.wav")));
			soundEffects.add(AudioSystem.getAudioInputStream(new File("assets/sounds/interact.wav")));

		
		}
		catch (IOException | UnsupportedAudioFileException e1) {
			e1.printStackTrace();
		}
	}

	public void playSettingMusic(int menuState) throws LineUnavailableException, IOException {
	    if (settingMusic != null && settingMusic.isRunning()) {
	        settingMusic.stop();
	        settingMusic.close();
	    }

	    settingMusic = AudioSystem.getClip();
	    settingMusic.open(mapSongs[menuState]);
	    settingMusic.start();
	    settingMusic.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public void playSoundEffect(int n) throws LineUnavailableException, IOException {
		// play the same sound as before
		if (n == game.lastPlayedSoundEffect) {
			System.out.println("BLAH2");
	    	soundEffect.setFramePosition(0);
	    	soundEffect.start();
	    }
		else {
			System.out.println("BLAH3");
		    soundEffect = AudioSystem.getClip();
		    soundEffect.open(soundEffects.get(n));
		    soundEffect.start();
		}


	}
	public void resetSoundEffect() {
		
	}


}
