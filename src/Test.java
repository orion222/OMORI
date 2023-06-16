import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.sound.sampled.*;

public class Test {
    static Audio sound = new Audio();
    static int lastPlayedSound = -1;

    public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        Scanner scanner = new Scanner(System.in);

        int response = 0;
        while (response != -1) {
            System.out.print("Enter a sound effect number: ");
            response = scanner.nextInt();
            if (response == lastPlayedSound) {
                sound.resetSoundEffect();
            }
            sound.playSoundEffect(response);
            lastPlayedSound = response;
        }

        System.out.println("Bye!");
    }
}