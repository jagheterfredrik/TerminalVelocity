package kth.inda.terminalvelocity;

import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

/**
 * @author Fredrik Gustafsson
 * 
 */
public class AboutState extends BasicGameState {

	private AngelCodeFont bigFont;
	private AngelCodeFont smallFont;
	private int id;

	public AboutState(int id) {
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
	 * org.newdawn.slick.state.GameState#init(org.newdawn.slick.GameContainer,
	 * org.newdawn.slick.state.StateBasedGame)
	 */
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
		smallFont = new AngelCodeFont("TVfont.fnt", "TVfont_00.tga");
		bigFont = new AngelCodeFont("zephyrBig.fnt", "zephyrBig_00.tga");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.newdawn.slick.state.GameState#render(org.newdawn.slick.GameContainer,
	 * org.newdawn.slick.state.StateBasedGame, org.newdawn.slick.Graphics)
	 */
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics g)
			throws SlickException {
		g.setFont(bigFont);
		g.setColor(Color.white);
		g.drawString("About", 220, 100);
		g.setFont(smallFont);
		g.setColor(Color.lightGray);
		g.drawString("TerminalVelocity, some say it's the ultimate game.", 100,
				250);
		g.drawString("Some say its creators are immortals.", 100, 310);
		g.drawString("Some even say the creators are Gods.", 100, 370);
		g.drawString("All of it.....    is true.", 100, 430);
		g.setColor(Color.pink);
		g.drawString("Fredrik 'jagheterfredrik' Gustafsson", 240, 500);
		g.drawString("Max 'keffis' Westermark", 330, 550);
		g.drawString("Viktor 'smash' Holmberg", 320, 600);
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
		if (container.getInput().isKeyPressed(Input.KEY_ESCAPE)
				|| container.getInput().isKeyPressed(Input.KEY_SPACE)
				|| container.getInput().isKeyPressed(Input.KEY_ENTER)) {
			try {
				game.enterState(GameConstants.MENU, FadeOutTransition.class
						.newInstance(), EmptyTransition.class.newInstance());
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
}
