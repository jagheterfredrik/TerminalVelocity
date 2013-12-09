package kth.inda.terminalvelocity;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

/**
 * Handles all music.
 * @author Fredrik Gustafsson
 * @version v0.1 early beta
 */

public class SoundController {
	Music[] sounds;
	Sound[] fx;
	static private SoundController _instance = null;

	/**
	 * Available sounds.
	 */
	public static enum SoundBank {
		INTRO, MAIN1, MAIN2, MAIN3, MAIN4,
		MAIN5, MAIN6, MAIN7;
	}

	/**
	 * Available soundeffects.
	 */
	public static enum FXBank {
		MENUCHOICE, LASER, SHIPBLAST, BLAST, HYPERSPACE, CASH;
	}

	/**
	 * A hack to make the class static.
	 * @return the static SoundController class.
	 */
	public static SoundController instance() {
		if(null == _instance) {
			_instance = new SoundController();
		}
		return _instance;
	}

	/**
	 * Sets up a new SoundController if it doesn't already exist.
	 */
	protected SoundController() {
		sounds = new Music[SoundBank.values().length];
		fx = new Sound[FXBank.values().length];
		try {
			sounds[SoundBank.INTRO.ordinal()] = new Music("TVintro.ogg");
			sounds[SoundBank.MAIN1.ordinal()] = new Music("main1.ogg");
			sounds[SoundBank.MAIN2.ordinal()] = new Music("main2.ogg");
			sounds[SoundBank.MAIN3.ordinal()] = new Music("main3.ogg");
			sounds[SoundBank.MAIN4.ordinal()] = new Music("main4.ogg");
			sounds[SoundBank.MAIN5.ordinal()] = new Music("main5.ogg");
			sounds[SoundBank.MAIN6.ordinal()] = new Music("main6.ogg");
			sounds[SoundBank.MAIN7.ordinal()] = new Music("main7.ogg");
			fx[FXBank.MENUCHOICE.ordinal()] = new Sound("menuChoice.ogg");
			fx[FXBank.LASER.ordinal()] = new Sound("laser.ogg");
			fx[FXBank.SHIPBLAST.ordinal()] = new Sound("shipBlast.ogg");
			fx[FXBank.BLAST.ordinal()] = new Sound("asteroidBlast.ogg");
			fx[FXBank.HYPERSPACE.ordinal()] = new Sound("hyperspace.ogg");
			fx[FXBank.CASH.ordinal()] = new Sound("cashreg.ogg");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Fades the sound.
	 * @param sound the sound to fade.
	 * @param duration the fadetime in milliseconds.
	 */
	public void fadeOut(SoundBank sound, int duration) {
		sounds[sound.ordinal()].fade(duration, 0f, true);
	}
	
	/**
	 * Stops the sound.
	 * @param sound the sound to stop.
	 */
	public void stopSound(SoundBank sound) {
		sounds[sound.ordinal()].stop();
	}

	/**
	 * Loops a sounds.
	 * @param sound the sound to loop.
	 */
	public void loop(SoundBank sound) {
		sounds[sound.ordinal()].loop(1f,0.1f);
	}
	
	/**
	 * Loops a sounds. newSound must be longer than prevSound (very specific for this game).
	 * @param sound the sound to loop.
	 */
	public void loop(SoundBank newSound, SoundBank prevSound) {
		float time = 0;
		if(sounds[prevSound.ordinal()].playing())
			time = sounds[prevSound.ordinal()].getPosition();
		sounds[prevSound.ordinal()].stop();
		sounds[newSound.ordinal()].loop(1f,0.1f);
		sounds[newSound.ordinal()].setPosition(time);
		
	}

	/**
	 * Plays an effect.
	 * @param sound the effect to play.
	 * @param volume the volume of the soundeffect.
	 */
	public void playfx(FXBank sound, float volume) {
		fx[sound.ordinal()].play(1f, 0.2f);
	}

	/**
	 * Set the volume of a sound.
	 * @param sound the sound to change the volume of.
	 * @param volume the new volume.
	 */
	public void setVolume(SoundBank sound, float volume) {
		sounds[sound.ordinal()].setVolume(volume);
	}


}
