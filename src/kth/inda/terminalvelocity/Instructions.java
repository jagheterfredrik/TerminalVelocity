package kth.inda.terminalvelocity;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

/**
 * Handles the instructions state, giving information about how to play.
 * @author Viktor Holmberg
 */
public class Instructions extends SpaceController {

	public Instructions(int id, int difficulty) {
		super(id, -1);
		super.asteroids.clear();
		super.asteroids.add(new Asteroid(800,200,super.space));
	}
	
	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		if (GameConstants.debug)
			System.out.println(x + ", " + y);
	}
	
	/**
	 * Renders all visual elements. Gets called automatically
	 * by Slick.
	 * @see org.newdawn.slick.BasicGame#render
	 */
	public void render(GameContainer container, StateBasedGame game, Graphics g)
	throws SlickException {
		inputEnabled = true;
		for(int i = 0; i<bullets.size(); i++) {
			bullets.get(i).render(g);
		}
		for(int i = 0; i<cash.size(); i++) {
			cash.get(i).render(g);
		}
		for(int i = 0; i<asteroids.size(); i++) {
			g.setColor(Color.orange);
			asteroids.get(i).render(g);
			g.setColor(Color.white);
		}
		if (!(ship == null))
			ship.render(g);
		for(int i = 0; i<wormholes.size(); i++) {
			wormholes.get(i).render(g);
		}
		for(int i = 0; i<effects.size(); i++) {
			effects.get(i).render(g);
		}

		//draws the HUD
		int verpad = 1;
		g.setFont(tinyFont);
		g.setColor(Color.blue);
		if ((GameConstants.HYPERWAIT-warpWait)/GameConstants.HYPERWAIT==1){
			g.setColor(new Color(255,(GameConstants.HYPERWAIT-warpWait)
					/GameConstants.HYPERWAIT,100));
		}
		else
			g.setColor(Color.blue);		
		g.setColor(new Color(
				255*(1-(GameConstants.HYPERWAIT/GameConstants.HYPERWAIT-warpWait)),
				255*(GameConstants.HYPERWAIT-warpWait)
				/GameConstants.HYPERWAIT,0));
		g.fill(new Rectangle(1024-130,verpad+6,120*(GameConstants.HYPERWAIT-warpWait)
				/GameConstants.HYPERWAIT,22));
		g.setColor(Color.white);

		g.drawString("Score: "+(GameConstants.score+this.score) +
				"            Lives: " + GameConstants.lives,10,verpad);

		//DEATH
		if (deadCounter!=Integer.MIN_VALUE){	

			if (GameConstants.lives>0){
				g.setFont(smallFont);
				g.drawString("YOU CRASHED!", 350, 300);
				g.setFont(tinyFont);
				g.drawString(GameConstants.lives + " lives left.", 350, 360);
			}
			else{		
				g.setFont(smallFont);
				g.drawString("GAME OVER!", 400, 300);
			}
		}

		if(asteroids.size()==0) {
			g.setFont(smallFont);
			g.drawString("Training complete!", 360, 370);		
		}
		drawInfo(g);

	}

	/**
	 * Updates the game logic. Gets called automatically
	 * by Slick.
	 * @see org.newdawn.slick.BasicGame#update
	 */
	public void update(GameContainer container, StateBasedGame game, int delta)
	throws SlickException {		

		if (deadCounter!=Integer.MIN_VALUE){
			deadCounter=Integer.MIN_VALUE;
		ship=new Ship();

		}
		if(countTime)
			time+=delta;
		//Updates the ships location
		updateUserInput(container);
		//Updates locations
		moveObjects(delta);
		//Collision-checking
		parseCollisions();
		//Parse gravity
		parseGravity();
		if (warpWait>0)
			warpWait--;
		//Level completion
		if (asteroids.size()==0 && completionCounter == Integer.MIN_VALUE) {
			resetEnabled = false;

			GameConstants.score += this.score;
			this.score = 0;
			completionCounter = 200;
		}
		if (asteroids.size()==0)
			completionCounter--;
		if (completionCounter == 100){
			hyperJump();
			shipWarpCounter = Integer.MAX_VALUE;
		}
		else if (completionCounter == 0){
			try {
				parent.enterState(GameConstants.MENU,FadeOutTransition.class.newInstance(), FadeInTransition.class.newInstance());
			} catch (Exception e) {			
				System.out.println("Internal error!");
			}  
		}
	}

	private void drawInfo(Graphics g){
		g.setFont(tinyFont);
		String[] info = {
		"Mission: Destroy every asteroid without getting destroyed yourself",
		"Watch out for the asteroids, their gravity pulls you!",
		"Collect the crystals for extra points:",
		"Green: 20 points, Blue: 40 points, Red: 100 points.",
		"Controls:",
		"Keypad left/right: rotate ship",
		"Keypad up: accelerate",
		"Space: Fire weapon",
		"Enter: Hyperspace jump",
		"Pause/break or P: Pause game",
		"Esc = quit to menu, without saving your score.",
		"Shoot the asteroid to return to the menu"};
		for (int i = 0; (i<info.length);i++ ){
			g.drawString(info[i], 33, 26*(i+1));
		}
	}
}
