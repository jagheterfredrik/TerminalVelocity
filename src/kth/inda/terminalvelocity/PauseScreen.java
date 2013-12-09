/**
 * 
 */
package kth.inda.terminalvelocity;

import java.io.IOException;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Handles the pause screen.
 * 
 * @author Viktor Holmberg
 * 
 */
public class PauseScreen extends BasicGameState {

	private AngelCodeFont bigFont;
	private AngelCodeFont smallFont;
	private ParticleSystem ps;
	private int id;

	public PauseScreen(int id) {
		this.id = id;
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
	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		container.getInput().clearKeyPressedRecord();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.newdawn.slick.state.GameState#init(org.newdawn.slick.GameContainer,
	 * org.newdawn.slick.state.StateBasedGame)
	 */
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
		smallFont = new AngelCodeFont("TVfont.fnt", "TVfont_00.tga");
		bigFont = new AngelCodeFont("zephyrBig.fnt", "zephyrBig_00.tga");
		try {
			ps = ParticleIO.loadConfiguredSystem("pause.xml");
		} catch (IOException e) {
			System.out.println("missing files. Re-install recommended");
		}
		ps.setPosition(GameConstants.width / 2, GameConstants.height / 2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.newdawn.slick.state.GameState#render(org.newdawn.slick.GameContainer,
	 * org.newdawn.slick.state.StateBasedGame, org.newdawn.slick.Graphics)
	 */
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2)
			throws SlickException {
		ps.render();
		arg2.setFont(bigFont);
		arg2.drawString("GAME PAUSED", 220, 200);
		arg2.setFont(smallFont);
		arg2.drawString("press space to continue", 220, 310);
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
		ps.update(delta);
		if (container.getInput().isKeyPressed(Input.KEY_P)
				|| container.getInput().isKeyPressed(Input.KEY_PAUSE)
				|| container.getInput().isKeyPressed(Input.KEY_SPACE)) {
			((TerminalVelocity) game).enterLastState();
		}
	}

}
