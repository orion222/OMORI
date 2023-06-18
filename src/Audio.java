/*
 * This class is for all audio-related purposes for the game.
 * It stores all sound effects and map songs.
 * It also has methods to play these sounds.
 */
import java.util.*;
import java.io.*;
import javax.sound.sampled.*;
public class Audio {
	
	static AudioInputStream[] mapSongs = new AudioInputStream[5];
	static ArrayList<Clip> soundEffects = new ArrayList<Clip>();
	static ArrayList<String> soundEffectNames = new ArrayList<String>();
	static  Clip settingMusic;
	static  Clip soundEffect;
	static int lastPlayedSoundEffect;
	
	// all imports
	public Audio() {
		try {
			mapSongs[0] = AudioSystem.getAudioInputStream(new File("assets/music/Title.wav"));
			mapSongs[1] = AudioSystem.getAudioInputStream(new File("assets/music/WHITESPACE.wav"));
			mapSongs[2] = AudioSystem.getAudioInputStream(new File("assets/music/ForestChillin.wav"));
			mapSongs[3] = AudioSystem.getAudioInputStream(new File("assets/music/BLACKSPACE.wav"));
			mapSongs[4] = AudioSystem.getAudioInputStream(new File("assets/music/win.wav"));
			for (int i = 1; i <= 5; i++) {
				soundEffectNames.add("scare" + i);
			}
			soundEffectNames.add("getItem");
			soundEffectNames.add("heal");
			soundEffectNames.add("selectOption");
			soundEffectNames.add("unlockDoor");
			soundEffectNames.add("lockedDoor");
			soundEffectNames.add("interact");
			soundEffectNames.add("hit");
			for (int i = 0; i < soundEffectNames.size(); i++) {
				soundEffects.add(AudioSystem.getClip());
				soundEffects.get(i).open(AudioSystem.getAudioInputStream(new File("assets/sounds/" + soundEffectNames.get(i) + ".wav")));
			}
			System.out.println(soundEffects.size());

		}
		catch (IOException | UnsupportedAudioFileException | LineUnavailableException e1) {
			e1.printStackTrace();
		}
	}
	
	
	// play the current setting music given the menuState
	public void playSettingMusic(int menuState) throws LineUnavailableException, IOException {
	    // if a song is already running and the method is called to play another one, close the current song
		if (settingMusic != null && settingMusic.isRunning()) {
	        settingMusic.stop();
	        settingMusic.close();
	    }
	    
	    // open, set the song at the beginning, play
	    settingMusic = AudioSystem.getClip();
	    settingMusic.open(mapSongs[menuState]);
	    settingMusic.setFramePosition(0);
	    settingMusic.start();
	    settingMusic.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	// play a sound effect given the index of the sound effect
	public void playSoundEffect(int n) throws LineUnavailableException, IOException {
		System.out.println("BLAH3");
	    Clip cur = soundEffects.get(n);
	    cur.stop();
	    cur.setFramePosition(0);
	    cur.start();
	    
	}



}
