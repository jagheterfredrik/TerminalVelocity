package kth.inda.terminalvelocity;

import java.util.Random;

import org.newdawn.slick.Color;

/**
 * Keeps track of app-wide variables and constants.
 * @author Fredrik Gustafson, Viktor Holmberg
 *
 */

public class GameConstants {
	
	/** APPWIDE STATIC VARIABLES **/
	/**
	 * The users current score.
	 */
	public static int score;
	/**
	 * The users current amount of money.
	 */
	public static int money;
	/**
	 * The users current amount of lives.
	 */
	public static int lives;
	
	/**
	 * The game-canvas height in pixels.
	 */
	public static int height;
	/**
	 * The game-canvas width in pixels.
	 */
	public static int width;
	public static boolean debug = false;
	public static boolean noMenu = false;
	public static boolean paused = false;
	public static Random rand = new Random();
	
	/** REAL CONSTANTS **/	
	/**
	 * the time to be invincible at warp-in time.
	 */
	public static final int INVINSIBILITYTIME = 110;
	/**
	 * The framerate to be used.
	 */
	public final static int FPS = 50;
	/**
	 * The amount of rotation applied in each frame.
	 */
	public final static float ROTATION_FACTOR = 0.15f;
	/**
	 * The acceleration of the ship.
	 */
	public final static float SHIP_ACCELERATION = 0.20f;
	/**
	 * The max speed a ship can travel in.
	 */
	public final static float SHIP_MAXSPEED = 6f;
	public static final int RELOADTIME = 6;
	public static final float GRAV_MAXSPEED = 2f;
	public static final float TOTAL_MAXSPEED = 5f;
	
	/**
	 * The lifetime of a bullet.
	 */
	public final static int BULLET_LIFETIME = 100;
	/**
	 * The time it takes to hyperspace-jump.
	 */
	public final static int HYPERSPACETIME = 30;
	/**
	 * The amount of time that you'll have to wait to
	 * hyperspace again after another hyperspace-jump.
	 */
	public final static int HYPERWAIT = 50*12;
	/**
	 * The minimum size for asteroids. When the asteroids
	 * divides to below this size it'll vanish.
	 */
	public final static float ASTEROID_MINSIZE = 13;
	public static final float ASTEROID_DEFAULTMAXENERGY = 99999999999999999999F;
	public static final float ASTEROID_MAXSIZE = 70;
	/**
	 * The amount of time that money floats around.
	 * Ex. 400*(1/50) = 8 seconds.
	 */
	public static final int MONEY_LIFETIME = 400;
	/**
	 * AES-128-key for highscore-crypting.
	 */
	public static final String HIGHSCORE_CRYPTO_KEY = "K3s@,m_[l/,1H;-)";
	/**
	 * The filename to save the highscore.
	 */
	public static final String HIGHSCORE_FILE_NAME = "highscore.tv";
	/**
	 * The number of scores to save. After this limit is reached the
	 * lowest highscore will be discarded when a higher is submitted.
	 */
	public static final int HIGHSCORE_SCORE_COUNT = 10;	
	public static final Color ASTEROID_COLOR = new Color(215,76,0);
	/**
	 * The number of lives to start with.
	 */
	public static final int STARTLIVES = 6;
	
	public static final boolean FULLSCREEN = false;
	
	//States
	public static final int	MENU = 0;
	public static final int INTRO = 1;
	public static final int	INSTRUCTIONS = 2;	
	public static final int HIGHSCOREENTRY = 3;
	public static final int HIGHSCOREDISPLAY = 4;
	public static final int ABOUT = 5;
	public static final int PAUSESCREEN = 6;
	public static final int FIRSTLEVEL = 7;



	
}
