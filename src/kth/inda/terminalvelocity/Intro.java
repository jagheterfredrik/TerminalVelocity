package kth.inda.terminalvelocity;

import java.io.IOException;

import org.newdawn.slick.*;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

/**
 * An flashy intro gameState, telling the player a background story.
 * 
 * @author Viktor Holmberg
 */
public class Intro extends BasicGameState {
	private int id;
	StateBasedGame parent;
	AngelCodeFont smallFont;
	private float textAlpha;
	private int time;
	private int finTime;
	private ParticleSystem ps;

	/**
	 * Constructor
	 * 
	 * @param the
	 *            id of this state
	 */
	public Intro(int id) {
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
	 * org.newdawn.slick.state.GameState#update(org.newdawn.slick.GameContainer,
	 * org.newdawn.slick.state.StateBasedGame, int)
	 */
	@Override
	public void enter(GameContainer container, StateBasedGame game) {
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
		parent = game;
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
		int time1 = 200;
		int time2 = time1 + 250;
		int time3 = time2 + 450;
		int time4 = time3 + 400;
		int time5 = time4 + 50;
		finTime = time5 + 60;
		int ypadding = 60;

		g.resetFont();
		g.setColor(Color.darkGray);
		g.drawString("Press Return to skip intro...", 850, 750);

		g.setFont(smallFont);
		g.setColor(new Color(0, 1, 0, 0));
		if (time < time1) {
			if (textAlpha < 1)
				textAlpha = textAlpha + (float) 0.01;
			if (time > time1 - 50)
				textAlpha = textAlpha - (float) 0.01 * 2 * 2;
			g.setColor(new Color(0, 1, 0, textAlpha));
			g.drawString("The year 2432,", 100, 200);
			g.drawString("7 years after world war 17.", 100, 270);
		}
		if (time == time1)
			textAlpha = 0;
		if (time > time1 && time < time2) {
			if (textAlpha < 1)
				textAlpha = textAlpha + (float) 0.01;
			if (time > time2 - 50)
				textAlpha = textAlpha - (float) 0.01 * 2 * 2;
			g.setColor(new Color(0, 1, 0, textAlpha));
			g.drawString("Rampant consumism and centuries of war ", 100, 200);
			g.drawString("have finally emptied the last resources", 100,
					200 + ypadding);
			g.drawString("of the dying earth.", 100, 200 + ypadding * 2);
		}
		if (time == time2)
			textAlpha = 0;
		if (time > time2 && time < time3) {
			if (textAlpha < 1)
				textAlpha = textAlpha + (float) 0.01;
			if (time > time3 - 50)
				textAlpha = textAlpha - (float) 0.01 * 2 * 2;
			Color textColor = new Color(0, 1, 0, textAlpha);
			g.setColor(textColor);
			g.drawString("The only hope for mankinds further survival ", 100,
					200);
			g.drawString("is out among the stars, ", 100, 200 + ypadding);
			g.drawString("where space-based miners harvest asteroids;", 100,
					200 + ypadding * 2);
			g.drawString("a notoriously difficult and often deadly process...",
					100, 200 + ypadding * 3);
		}
		if (time == time3)
			textAlpha = 0;
		if (time > time3 && time < time4) {
			if (textAlpha < 1)
				textAlpha = textAlpha + (float) 0.01;
			if (time > time4 - 50)
				textAlpha = textAlpha - (float) 0.01 * 2 * 2;
			g.setColor(new Color(0, 1, 0, textAlpha));
			g.drawString("As one of many war veterans your last chance", 100,
					200);
			g.drawString("to survive is the astro-mining business.", 100,
					200 + ypadding);
			g.drawString("Good luck... you'll need it.", 100,
					200 + ypadding * 3);
		}
		if (time > time4) {
			SoundController.instance().fadeOut(SoundController.SoundBank.INTRO,
					500);

			g.setColor(Color.blue);
			g.setColor(new Color(100, 255, 255, 255));
			g.drawString("INITIALIZING HYPERSPACE JUMP", 225, 310);
		}
		if (time == time5) {
			try {
				ps = ParticleIO.loadConfiguredSystem("megaWarp.xml");
			} catch (IOException e) {
				e.printStackTrace();
			}
			ps.setPosition(GameConstants.width / 2, GameConstants.height / 2);
			SoundController.instance().playfx(
					SoundController.FXBank.HYPERSPACE, 1f);
		}
		if (time > time5 + 5) {
			g.setBackground(new Color(180, 243, 255));
		}
		if (ps != null)
			ps.render();
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
		time++;
		if (ps != null)
			ps.update(delta);
		if ((time > finTime)
				|| container.getInput().isKeyPressed(Input.KEY_ENTER)
				|| container.getInput().isKeyPressed(Input.KEY_SPACE)) {
			SoundController.instance().playfx(
					SoundController.FXBank.MENUCHOICE, 0.4f);
			try {
				parent.enterState(GameConstants.FIRSTLEVEL,
						FadeOutTransition.class.newInstance(),
						EmptyTransition.class.newInstance());
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.newdawn.slick.state.BasicGameState#leave(org.newdawn.slick.GameContainer
	 * , org.newdawn.slick.state.StateBasedGame)
	 */
	public void leave(GameContainer container, StateBasedGame game) {
		container.getGraphics().setBackground(Color.black);
	}
}
