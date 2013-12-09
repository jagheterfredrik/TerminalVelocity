package kth.inda.terminalvelocity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.World;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

/**
 * Controls the actual game.
 * @author Viktor Holmberg and Fredrik Gustafsson
 */
public class SpaceController extends BasicGameState {
	ArrayList<Asteroid> asteroids;
	Ship ship;
	ArrayList<Bullet> bullets;
	ArrayList<Wormhole> wormholes;
	ArrayList<Cash> cash;
	ArrayList<ParticleEffect> effects;

	World space;
	StateBasedGame parent;

	Random rand = new Random();

	//Used for the warp effect
	float nextWarpX = GameConstants.width/2;
	float nextWarpY = GameConstants.height/2;
	int shipWarpCounter = 100; 
	float tempVelocity = 0;
	float tempAngle = 0;

	protected int id = 5;
	protected int fireWait;

	AngelCodeFont smallFont;
	AngelCodeFont tinyFont;

	protected boolean countTime = true;
	protected long time = 0;

	protected boolean inputEnabled = true;
	protected boolean resetEnabled = true;

	protected int shotsFired = 0;
	protected int shotsHit = 0;
	protected String accuracy = "0.00%";
	
	//Money and score this level.
	protected int score = 0;
	private int money = 0;
	
	protected int difficulty;
	protected int warpWait = 0;
	//should start counting when all asteroids are destroyed.
	protected int completionCounter = Integer.MIN_VALUE;
	//should start counting when ship is destroyed.
	protected int deadCounter = Integer.MIN_VALUE;

	/**
	 * Constructor
	 * @param id an identifier for this state
	 * @param difficulty the difficulty of this level
	 */
	public SpaceController(int id, int difficulty) {
		this.id=id;
		this.difficulty = difficulty;
		setupSpace();
		try {
			smallFont = new AngelCodeFont("TVfont.fnt","TVfont_00.tga");
			tinyFont = new AngelCodeFont("zephyrTiny.fnt","zephyrTiny_00.tga");
		} catch (SlickException e) {
			System.out.println("missing font files detected. Re-install recommended");
		}
	}

	/**
	 * Gets called automatically by slick when entering this state.
	 */	
	@Override
	public void enter(GameContainer container, StateBasedGame game)  {
		this.parent=game;		
		container.getInput().clearKeyPressedRecord();
		parent.addState(new SpaceController(getID()+1, difficulty+1));
		if(!(((TerminalVelocity) game).getLastStateID() == getID() || difficulty < 0))
			soundInit(game);
	}

	@Override
	public void leave(GameContainer container, StateBasedGame game)  {
		//game.addState(new SpaceController(GameConstants.FIRSTLEVEL, 1));		
	}

	/**
	 * gets called only the first time this class is initialized
	 */
	public void init(GameContainer container, StateBasedGame game)
	throws SlickException {	
	}

	public void soundInit(StateBasedGame game) {
		SoundController sc = SoundController.instance();
		if(difficulty > 7)
		{
			sc.loop(SoundController.SoundBank.MAIN7);
		}
		else if(difficulty == 1 && !(((TerminalVelocity) game).getLastStateID() == 6))
		{
			sc.stopSound(SoundController.SoundBank.INTRO);
			sc.loop(SoundController.SoundBank.values()[1]);
		}
		else if(difficulty > 1 && difficulty <= 7)
			sc.loop(SoundController.SoundBank.values()[difficulty], SoundController.SoundBank.values()[difficulty-1]);

	}

	protected void setupSpace() {
		asteroids = new ArrayList<Asteroid>();
		bullets = new ArrayList<Bullet>();
		wormholes = new ArrayList<Wormhole>();
		effects = new ArrayList<ParticleEffect>();
		cash = new ArrayList<Cash>();
		space = new World(new Vector2f(0,0), 1);

		int asteroidCount = 3*(difficulty)+2;
		for (int i = 0; i<asteroidCount; i++){
			asteroids.add(new Asteroid(0,0,space));
		}
	}

	/**
	 * Reset all parameters to reset the level.
	 */
	protected void reset() {
		if(!resetEnabled) return;

		countTime = true;
		time = 0;

		inputEnabled = true;
		resetEnabled = true;

		shotsFired = 0;
		shotsHit = 0;
		accuracy = "0.00%";

		score = 0;
		warpWait = 0;
		//should start counting when all asteroids are destroyed.
		completionCounter = Integer.MIN_VALUE;
		//should start counting when ship is destroyed.
		deadCounter = Integer.MIN_VALUE;
	}

	protected void shipAccelerate() {
		if (!(ship==null))
			ship.accelerate();
	}

	protected void shipFire() {
		updateAccuracy(false);

		Bullet bullet = ship.fireBullet();
		bullets.add(bullet);
	}

	protected void shipRotateLeft() {
		if (!(ship==null))
			ship.rotate(-GameConstants.ROTATION_FACTOR);
	}

	protected void shipRotateRight() {
		if (!(ship==null))
			ship.rotate(GameConstants.ROTATION_FACTOR);
	}

	protected void updateAccuracy(boolean shotHit){
		if(shotHit)
			shotsHit++;
		else
			shotsFired++;
		if(shotsFired != 0) {
			accuracy = String.format("%2.2f", 100*(shotsHit/(float)shotsFired))+"%";
		}
	}
	protected void parseCollisions() {
		//Wormhole hit-test
		for(Iterator<Wormhole> iter = wormholes.iterator(); iter.hasNext();) {
			Wormhole wh = iter.next();
			if (!(ship==null)){
				if(wh.hits(ship)) {}
			}
			//Works!
		}

		ArrayList<Asteroid> newAsteroids = new ArrayList<Asteroid>();
		for(Iterator<Asteroid> aIter = asteroids.iterator(); aIter.hasNext();){
			Asteroid asteroid = aIter.next();

			//Asteroid-bullet collisions.
			for(Iterator<Bullet> bIter = bullets.iterator(); bIter.hasNext();){
				Bullet bullet = bIter.next();
				if(asteroid.hits(bullet)) {
					if(rand.nextInt(100) < 20-1)
						cash.add(new Cash(asteroid.getShape().getX(),
								asteroid.getShape().getY(),
								Cash.CashSize.values()[rand.nextInt(Cash.CashSize.values().length)],
								asteroid.getAngle(), asteroid.getSpeed()));
					effects.add(new ParticleEffect("asteroidDestruct.xml",
							bullet.getShape().getX(),
							bullet.getShape().getY()));		
					SoundController.instance().playfx(SoundController.FXBank.BLAST, 1f);
					updateAccuracy(true);

					if(asteroid.getRadius()>=20f)
						score+=20;
					else if(asteroid.getRadius()>=10f)
						score+=10;
					else
						score+=5;

					Asteroid[] asses = asteroid.split(bullet.getAngle());
					space.remove(asteroid.getBody());
					/*Ignores an AbstractList-IllegalStateException
					 * that doesn't seem to have any impact and
					 * the game recovers fully from.
					 * TODO:Found out why!
					 * */
					try {
						aIter.remove();
					} catch(IllegalStateException e) {
						System.err.println(e);
					}
					if(asses.length>0)
						for (int i = 0; i<asses.length; i++)
							newAsteroids.add(asses[i]);
					bIter.remove();	

				}
			}
			//Asteroid-ship collisions.
			if (!(ship==null)){
				if(ship.hits(asteroid)) {
					effects.add(new ParticleEffect("shipdie.xml", ship.getX(),ship.getY()));
					effects.add(new ParticleEffect("shipdiecenter.xml", ship.getX(),ship.getY()));
					SoundController.instance().playfx(SoundController.FXBank.SHIPBLAST, 1f);
					deadCounter = 200;	

					GameConstants.lives--;				
					ship = null;
					inputEnabled = false;
				}
			}
		}
		//Ship-cash collision.
		if(ship!=null)
			for(Iterator<Cash> iter = cash.iterator(); iter.hasNext();) {
				Cash tempcash = iter.next();
				if(tempcash.hits(ship)) {
					effects.add(new ParticleEffect("moneyPickup.xml",
							tempcash.getShape().getX(),
							tempcash.getShape().getY()));
					SoundController.instance().playfx(SoundController.FXBank.CASH, 1f);
					score += tempcash.getMoney()/5;
					iter.remove();
				}
			}

		for(int i = 0; i<newAsteroids.size(); i++)
			asteroids.add(newAsteroids.get(i));
	}


	protected void moveObjects(int delta) {
		//Move ship
		if (!(ship==null))
			ship.update(delta);

		//Move bullets
		for(Iterator<Bullet> iter = bullets.iterator(); iter.hasNext();) {
			Bullet bullet = iter.next();
			bullet.update(delta);
			//TODOeffects.add(new ParticleEffect("bullet.xml",bullet.getShape().getCenterX(),bullet.getShape().getCenterY()));
			if (!bullet.getLife()){
				iter.remove();
			}
		}

		//Move cash
		if(cash!=null)
			for(Iterator<Cash> iter = cash.iterator(); iter.hasNext();) {
				Cash cash = iter.next();
				cash.update(delta);
				if (!cash.getLife()){
					iter.remove();
				}
			}

		//Move asteroids
		for(Iterator<Asteroid> iter = asteroids.iterator(); iter.hasNext();)
			iter.next().update(delta);

		//Update wormholes
		for(Iterator<Wormhole> iter = wormholes.iterator(); iter.hasNext();) {
			Wormhole wh = iter.next();
			wh.update(delta);
		}
		//Update effects and removes finished ones
		for(Iterator<ParticleEffect> iter = effects.iterator(); iter.hasNext();) {
			ParticleEffect effect = iter.next();
			effect.update(delta);
			if (!effect.getAlive()){
				iter.remove();
			}
		}
		//Update the physics
		space.step();
		//hyperspace
		jumpBack();

	}

	/**
	 * checks if the ship should jump back after hyperspacing and if so does.
	 */
	protected void jumpBack(){
		if ((ship==null)&&deadCounter==Integer.MIN_VALUE){
			shipWarpCounter--;
			if (shipWarpCounter <= 0){
				Boolean isNextWarpSafe = false;	
				float radius = 200;
				int nrwarp = 1;
				while (!isNextWarpSafe){					
					radius = radius-0.5f;
					if (radius<0)
						radius = 0;
					nrwarp++;

					Circle testCircle = new Circle(nextWarpX, nextWarpX, radius);	
					isNextWarpSafe = true;
					//System.out.println("warpnr"+nrwarp+" warp radius:"+radius);
					for(Iterator<Asteroid> aIter = asteroids.iterator(); aIter.hasNext();){
						Asteroid asteroid = aIter.next();
						if (asteroid.getShape().intersects(testCircle))
							isNextWarpSafe = false;
					}
					if (!isNextWarpSafe){
						nextWarpX = rand.nextFloat()*GameConstants.width;
						nextWarpY = rand.nextFloat()*GameConstants.height;
					}
				}
				effects.add(new ParticleEffect("warp.xml",nextWarpX+32,nextWarpY+40));
				SoundController.instance().playfx(SoundController.FXBank.HYPERSPACE, 1f);
				ship = new Ship();
				ship.setPosition(nextWarpX, nextWarpY);		
				ship.rotate(tempAngle);
				ship.setSpeed(tempVelocity);
				warpWait = GameConstants.HYPERWAIT;
			}
		}
	}

	protected void parseGravity() {
		for(int i=0;i<asteroids.size();i++) {
			if(ship!=null)
				ship.gravAccelerate(asteroids.get(i));
			for(int j = 0; j<asteroids.size(); j++)
				if(i!=j)
					asteroids.get(i).accelerateTowards(asteroids.get(j));
		}
	}

	/**
	 * Updates the game logic. Gets called automatically
	 * by Slick.
	 * @see org.newdawn.slick.BasicGame#update
	 */
	public void update(GameContainer container, StateBasedGame game, int delta)
	throws SlickException {
		if (deadCounter!=Integer.MIN_VALUE)
			deadCounter--;
		//death
		if (deadCounter==0){

			if(GameConstants.lives<=0) {

				GameConstants.score += this.score;
				parent.enterState(GameConstants.HIGHSCOREDISPLAY);
			}
			else{
				parent.addState(new SpaceController(getID(), difficulty));
				parent.enterState(getID());
			}
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
		//level completion:
		if (asteroids.size()==0 && completionCounter == Integer.MIN_VALUE) {
			resetEnabled = false;

			GameConstants.money += this.money;
			this.money = 0;

			GameConstants.score += this.score;
			this.score = 0;
			completionCounter = 200;
		}
		if (asteroids.size()==0)
			//countTime = false;
			//inputEnabled = false;
			completionCounter--;
		if (completionCounter == 100){
			hyperJump();
			shipWarpCounter = Integer.MAX_VALUE;
		}
		else if (completionCounter == 0){
			try {
				parent.enterState(getID()+1,FadeOutTransition.class.newInstance(), FadeInTransition.class.newInstance());
			} catch (Exception e) {			
				System.out.println("Internal error!");
			}  
		}
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

		//level start message:
		if(time<=1000){
			g.setFont(smallFont);
			g.drawString("Now entering level "+difficulty+".", 300, 300);
		}
		if(asteroids.size()==0) {
			g.setFont(smallFont);
			g.drawString("Mission complete!", 300, 300);		
		}

	}

	protected void hyperJump() {		
		if (!(ship==null)&&warpWait==0){
			nextWarpX = rand.nextFloat()*GameConstants.width;
			nextWarpY = rand.nextFloat()*GameConstants.height;
			effects.add(new ParticleEffect("warpout.xml",ship.getX(),ship.getY()));
			tempVelocity = ship.getSpeed();
			tempAngle = ship.getAngle();
			ship = null;
			shipWarpCounter = GameConstants.HYPERSPACETIME;
			SoundController.instance().playfx(SoundController.FXBank.HYPERSPACE, 1f);
		}
	}

	/**
	 * Listen for key-presses.
	 * @see org.newdawn.slick.BasicGame#keyPressed
	 */
	public void keyPressed(int key, char c)
	{
		if(key == Input.KEY_ESCAPE) {
			try {
				parent.enterState(GameConstants.MENU, FadeOutTransition.class.newInstance(), EmptyTransition.class.newInstance());
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Internal error!");
			}  
		} else if(key == Input.KEY_D)
			GameConstants.debug = !GameConstants.debug;
		else if(key == Input.KEY_R)
			reset();
		else if((key == Input.KEY_PAUSE || key == Input.KEY_P) && (deadCounter==Integer.MIN_VALUE)){
			//This MUST be without transitions!
			SoundController sc = SoundController.instance();
			if(difficulty > 7)
				sc.loop(SoundController.SoundBank.values()[1],(SoundController.SoundBank.values()[7]));
			else
				sc.loop(SoundController.SoundBank.values()[1],(SoundController.SoundBank.values()[difficulty]));
			parent.enterState(GameConstants.PAUSESCREEN);
		}
		else if(key == Input.KEY_K && GameConstants.debug)
			asteroids.clear();
		else if(key == Input.KEY_E && GameConstants.debug)
			GameConstants.lives = 0;
		//if(key == Input.KEY_S)
		//SoundController.instance().setEnabled(!SoundController.instance().getEnabled());
		if(inputEnabled)
			if(key == Input.KEY_RETURN){
				hyperJump();
				if (asteroids.size()==0){
					completionCounter = shipWarpCounter;
					shipWarpCounter = Integer.MAX_VALUE;
				}
			}
	}
	/**
	 * Listens for key-releases.
	 * @see org.newdawn.slick.BasicGame#keyReleased
	 */
	public void keyReleased(int key, char c)
	{
	}

	protected void updateUserInput(GameContainer container) {
		if(!inputEnabled)
			return;
		//Accelerate the ship
		if(container.getInput().isKeyDown(Input.KEY_UP)){
			if (!(ship==null)){		
				shipAccelerate();
			}
		}

		//Fire a bullet       
		if(container.getInput().isKeyDown(Input.KEY_SPACE))
			if (fireWait==0) {
				if(ship!=null) {
					SoundController.instance().playfx(SoundController.FXBank.LASER, 0.6f);
					shipFire();
					fireWait = GameConstants.RELOADTIME;
				}
			}
			else{
				fireWait--;
			}

		//Rotate left
		if(container.getInput().isKeyDown(Input.KEY_LEFT))
			shipRotateLeft();
		//Rotate right
		if(container.getInput().isKeyDown(Input.KEY_RIGHT))
			shipRotateRight();
	}

	@Override
	public int getID() {
		return id;
	}

}
