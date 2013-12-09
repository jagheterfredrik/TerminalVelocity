package kth.inda.terminalvelocity;

import java.util.Iterator;

import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

/**
 * Describes the state where the highscore is
 * shown and entered.
 * @author Viktor Holmberg and Fredrik Gustafsson
 */
public class HighscoreState extends BasicGameState {

	private AngelCodeFont bigFont;
	private AngelCodeFont smallFont;
	private int id;

	private int qualifiedIndex = 0;
	private String enteredName = "";
	private StateBasedGame parent;

	public HighscoreState(int id){
		this.id=id;
	}

	/* (non-Javadoc)
	 * @see org.newdawn.slick.state.BasicGameState#getID()
	 */
	@Override
	public int getID() {
		return id;
	}

	/* (non-Javadoc)
	 * @see org.newdawn.slick.state.GameState#init(org.newdawn.slick.GameContainer, org.newdawn.slick.state.StateBasedGame)
	 */
	public void init(GameContainer arg0, StateBasedGame arg1)
	throws SlickException {
		smallFont = new AngelCodeFont("TVfont.fnt","TVfont_00.tga");	
		bigFont = new AngelCodeFont("zephyrBig.fnt", "zephyrBig_00.tga");
		parent = arg1;
	}

	/* (non-Javadoc)
	 * @see org.newdawn.slick.state.BasicGameState#enter(org.newdawn.slick.GameContainer, org.newdawn.slick.state.StateBasedGame)
	 */
	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		if(GameConstants.score>0) {
			GameConstants.lives=GameConstants.STARTLIVES;
			enteredName = "";
			qualifiedIndex = Highscore.instance().qualify(GameConstants.score);
		}
	}

	/* (non-Javadoc)
	 * @see org.newdawn.slick.state.GameState#render(org.newdawn.slick.GameContainer, org.newdawn.slick.state.StateBasedGame, org.newdawn.slick.Graphics)
	 */
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics g)
	throws SlickException {
		g.setFont(bigFont);
		g.drawString("Highscores", 220, 100);
		g.setFont(smallFont);
		g.drawString("#", 250, 240);
		g.drawString("Name", 320, 240);
		g.drawString("Score", 650, 240);
		Iterator<HighscoreEntry> hsit = Highscore.instance().elementIterator();
		HighscoreEntry hse;
		for (int i = 1; i<=10; i++){

			if(qualifiedIndex == i) {
				g.setColor(Color.green);
				g.drawString(""+i, 250, 240+i*40);				
				g.drawString(enteredName.toString()+"_", 320, 240+i*40);
				g.drawString(""+GameConstants.score, 650, 240+i*40);
				g.setColor(Color.white);
			} else if (hsit.hasNext()){
				hse = hsit.next();
				g.drawString(""+i, 250, 240+i*40);
				g.drawString(hse.getName(), 320, 240+i*40);
				g.drawString(""+hse.getScore(), 650, 240+i*40);
			}
			else{
				g.drawString(""+i, 250, 240+i*40);
				g.drawString("Empty", 320, 240+i*40);
				g.drawString("0", 650, 240+i*40);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.newdawn.slick.state.BasicGameState#keyPressed(int, char)
	 */
	public void keyPressed(int key, char c)
	{
		if(qualifiedIndex!=0) {
			if(key == Input.KEY_BACK) {
				if(enteredName.length()>0)
					enteredName = enteredName.substring(0,enteredName.length()-1);
			} else if(key == Input.KEY_RETURN) {
				qualifiedIndex = 0;
				Highscore.instance().submitHighscore(enteredName.toString(), GameConstants.score);
				GameConstants.score = 0;
				enteredName = null;
				return;
			} else
				enteredName = enteredName+c;
			while(enteredName.length()>12)
				enteredName = enteredName.substring(0,enteredName.length()-1);
		}
		else if(key == Input.KEY_RETURN || key == Input.KEY_SPACE || key == Input.KEY_ESCAPE)
			try {
				parent.enterState(GameConstants.MENU,FadeOutTransition.class.newInstance(), EmptyTransition.class.newInstance());
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
	}

	/* (non-Javadoc)
	 * @see org.newdawn.slick.state.GameState#update(org.newdawn.slick.GameContainer, org.newdawn.slick.state.StateBasedGame, int)
	 */
	public void update(GameContainer container, StateBasedGame game, int delta)
	throws SlickException {		
	}

}