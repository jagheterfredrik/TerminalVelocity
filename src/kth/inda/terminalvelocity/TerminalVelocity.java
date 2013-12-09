package kth.inda.terminalvelocity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.*;

/**
 * The main-class settings up and starting the game.
 * @author Fredrik Gustafsson
 * @version 2009-04-14
 */

public class TerminalVelocity extends StateBasedGame {
	GameContainer app;
	private int previousState;

	/**
	 * Initilizes the gamescreen.
	 * @see org.newdawn.slick.BasicGame#BasicGame
	 */
	public TerminalVelocity() {
		super("Terminal Velocity");
	}

	/**
	 * Main-function to start the game.
	 * @param args arguments passed to the application.
	 */
	public static void main(String[] args) {
		if (args.length > 0){
			if (args[0].equals("noMenu"))
				System.out.println("noMenu modifier active");
			GameConstants.noMenu = true;
		}
		try {
			AppGameContainer app = new AppGameContainer(new TerminalVelocity());			
			GameConstants.width = 1024;
			GameConstants.height = 768;
			app.setDisplayMode(GameConstants.width, GameConstants.height, GameConstants.FULLSCREEN);
			app.setShowFPS(false);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}		
	}

	/* (non-Javadoc)
	 * @see org.newdawn.slick.state.StateBasedGame#initStatesList(org.newdawn.slick.GameContainer)
	 */
	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		container.setTargetFrameRate(GameConstants.FPS);
		addState(new Menu(GameConstants.MENU));
		addState(new Intro(GameConstants.INTRO));		
		addState(new PauseScreen(GameConstants.PAUSESCREEN));
		addState(new Instructions(GameConstants.INSTRUCTIONS,1));
		addState(new HighscoreState(GameConstants.HIGHSCOREDISPLAY));
		addState(new AboutState(GameConstants.ABOUT));
		addState(new SpaceController(GameConstants.FIRSTLEVEL, 1));
		if (GameConstants.noMenu){
			enterState(GameConstants.FIRSTLEVEL);
		}
	}

	/* (non-Javadoc)
	 * @see org.newdawn.slick.state.StateBasedGame#enterState(int)
	 */
	public void enterState(int id) {
		previousState = getCurrentStateID();
		try {
			enterState(id, FadeOutTransition.class.newInstance(), FadeInTransition.class.newInstance());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Enter the previously loaded state.
	 */
	public void enterLastState(){
		enterState(previousState);
	}
	
	/**
	 * Get the id of the previous state.
	 * @return the id of the previous state.
	 */
	public int getLastStateID(){
		return previousState;
	}
}