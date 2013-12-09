package kth.inda.terminalvelocity;

import java.io.IOException;

import org.newdawn.slick.AngelCodeFont;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Handles the main menu. It also sets game variables to their default values.
 * 
 * @author Viktor Holmberg and Fredrik Gustafsson
 */
public class Menu extends BasicGameState {
	StateBasedGame parent;
	AngelCodeFont smallFont;
	AngelCodeFont bigFont;
	int x = 100;
	int y = -27;
	int menuChoice = 0;
	int noOfChoices = 4;
	private int id;
	boolean skipIntro = false;

	ParticleSystem ps;
	private int ydiff = 0;

	public Menu(final int id) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.newdawn.slick.state.BasicGameState#keyReleased(int, char)
	 */
	@Override
	public void keyReleased(int key, char c) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.newdawn.slick.state.BasicGameState#getID()
	 */
	@Override
	public int getID() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.newdawn.slick.state.BasicGameState#enter(org.newdawn.slick.GameContainer
	 * , org.newdawn.slick.state.StateBasedGame)
	 */
	public void enter(GameContainer container, StateBasedGame game) {
		SoundController.instance().loop(SoundController.SoundBank.INTRO);
		container.getInput().clearKeyPressedRecord();
		container.getGraphics().clear();
		ydiff = 0;
		// initialize appwide variables to default values.
		GameConstants.lives = GameConstants.STARTLIVES;
		GameConstants.score = 0;
		GameConstants.money = 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.newdawn.slick.state.GameState#init(org.newdawn.slick.GameContainer,
	 * org.newdawn.slick.state.StateBasedGame)
	 */
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		smallFont = new AngelCodeFont("TVfont.fnt", "TVfont_00.tga");
		bigFont = new AngelCodeFont("zephyrBig.fnt", "zephyrBig_00.tga");
		parent = game;
		try {
			ps = ParticleIO.loadConfiguredSystem("bigFire.xml");
		} catch (IOException e) {
			e.printStackTrace();
		}
		ps.setPosition(512, 235);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.newdawn.slick.state.GameState#render(org.newdawn.slick.GameContainer,
	 * org.newdawn.slick.state.StateBasedGame, org.newdawn.slick.Graphics)
	 */
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		g.setColor(Color.white);
		g.setFont(bigFont);
		g.drawString("TERMINAL VELOCITY", 136, 149);
		int horpos = 154;
		int inty = 220;
		if (ydiff < 55) {
			ydiff = ydiff + 2;
		}
		g.setFont(smallFont);
		g.setColor(Color.white);
		if (menuChoice == 0) {
			g.setColor(Color.green);
		}
		g.drawString("Play", horpos, inty + ydiff * 1);
		g.setColor(Color.white);
		if (menuChoice == 1) {
			g.setColor(Color.green);
		}
		g.drawString("Instructions", horpos, inty + ydiff * 2);
		g.setColor(Color.white);
		if (menuChoice == 2) {
			g.setColor(Color.green);
		}
		g.drawString("High Scores", horpos, inty + ydiff * 3);
		g.setColor(Color.white);
		if (menuChoice == 3) {
			g.setColor(Color.green);
		}

		g.drawString("About", horpos, inty + ydiff * 4);
		g.setColor(Color.white);
		if (menuChoice == 4) {
			g.setColor(Color.green);
		}
		g.drawString("Quit", horpos, inty + ydiff * 5);
		ps.render();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.newdawn.slick.state.BasicGameState#leave(org.newdawn.slick.GameContainer
	 * , org.newdawn.slick.state.StateBasedGame)
	 */
	public void leave(GameContainer container, StateBasedGame game) {
		container.getGraphics().clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.newdawn.slick.state.GameState#update(org.newdawn.slick.GameContainer,
	 * org.newdawn.slick.state.StateBasedGame, int)
	 */
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		if (container.getInput().isKeyPressed(Input.KEY_UP)) {
			menuChoice = --menuChoice < 0 ? noOfChoices : menuChoice;
			SoundController.instance().playfx(
					SoundController.FXBank.MENUCHOICE, 0.4f);
		}
		if (container.getInput().isKeyPressed(Input.KEY_DOWN)) {
			menuChoice = (++menuChoice) % (noOfChoices + 1);
			SoundController.instance().playfx(
					SoundController.FXBank.MENUCHOICE, 0.4f);
		}

		if (container.getInput().isKeyDown(Input.KEY_LEFT))
			y--;
		if (container.getInput().isKeyDown(Input.KEY_RIGHT))
			y++;
		if (container.getInput().isKeyPressed(Input.KEY_ENTER)
				|| container.getInput().isKeyPressed(Input.KEY_SPACE)) {
			SoundController.instance().playfx(
					SoundController.FXBank.MENUCHOICE, 0.4f);
			switch (menuChoice) {
			case 0: {
				if (!skipIntro) {
					skipIntro = true;
					game.addState(new SpaceController(GameConstants.FIRSTLEVEL,
							1));
					game.enterState(GameConstants.INTRO);
				} else {
					game.addState(new SpaceController(GameConstants.FIRSTLEVEL,
							1));
					game.enterState(GameConstants.FIRSTLEVEL);
				}
				break;
			}
			case 1:
				game.enterState(GameConstants.INSTRUCTIONS);
				break;
			case 2:
				game.enterState(GameConstants.HIGHSCOREDISPLAY);
				break;
			case 3:
				game.enterState(GameConstants.ABOUT);
				break;
			case 4:
				System.exit(0);
			default:
				menuChoice = 0;
			}
		}
		ps.update(delta);
	}
}
