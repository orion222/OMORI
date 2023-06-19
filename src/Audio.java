/*
 * This class is for all audio-related purposes for the game.
 * It stores all sound effects and map songs.
 * It also has methods to play these sounds.
 */
import java.util.*;
import java.io.*;
import javax.sound.sampled.*;
public class Audio {
	
	static ArrayList<Clip> mapSongs = new ArrayList<Clip>();
	static ArrayList<String> mapSongNames = new ArrayList<String>();
	static ArrayList<Clip> soundEffects = new ArrayList<Clip>();
	static ArrayList<String> soundEffectNames = new ArrayList<String>();
	static  Clip settingMusic;

	
	// all imports
	public Audio() {
		try {
			
			mapSongNames.add("Title");
			mapSongNames.add("WHITESPACE");
			mapSongNames.add("ForestChillin");
			mapSongNames.add("BLACKSPACE");
			mapSongNames.add("win");
			
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
			
			for (int i = 0; i < mapSongNames.size(); i++) {
				mapSongs.add(AudioSystem.getClip());
				mapSongs.get(i).open(AudioSystem.getAudioInputStream(new File("assets/music/" + mapSongNames.get(i) +  ".wav")));
			}
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
	        
	        // this line will actually NOT LET the song be played again once its closed.
	        // settingMusic.close();
	    }
	    
	    // open, set the song at the beginning, play
	    settingMusic = mapSongs.get(menuState);
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
