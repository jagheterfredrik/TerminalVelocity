package kth.inda.terminalvelocity;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;

/**
 * @author Fredrik Gustafsson and Viktor Holmberg
 * @version 2009-04-14
 */
public class Bullet implements SpaceObject {
	private float angle;
	private float speed;
	private Shape shape;
	private boolean alive;
	//ParticleSystem ps;
	private int life;

	/**
	 * Creates a new bullet.
	 * @param centerPointX the x-position of the new slug.
	 * @param centerPointY the y-position of the new slug.
	 * @param radius the radius of the new slug.
	 * @param angle the angle of the new slug.
	 * @param speed the speed of the new slug.
	 * @param width the width of the game-window.
	 * @param height the height of the game-window.
	 */
	public Bullet(float centerPointX, float centerPointY, float radius,
			float angle, float speed) {
		shape = new Circle(centerPointX, centerPointY, radius);
		this.angle = angle;
		this.speed = speed;
		this.alive = true;
		this.life = GameConstants.BULLET_LIFETIME;
		/*try {
			ps = ParticleIO.loadConfiguredSystem("bullet.xml");
		} catch (IOException e) {
			System.out.println("missing files. Re-install recommended");
			
		}
		ps.setPosition(shape.getCenterX(), shape.getCenterY());*/
	}
	
	/**
	 * Sets the speed.
	 * @param speed the objects new speed, in pixels per update.
	 */
	public void setSpeed(float speed){
		this.speed = speed;
	}

	/**
	 * Returns the speed.
	 * @return the speed of this object, in pixels per update.
	 */
	public float getSpeed(){
		return speed;
	}
	
	/**
	 * Increases/decreases the speed.
	 * @param speed the change of speed relative to the
	 * current one, in pixels per update.
	 */
	public void accelerate(float newSpeed){
		speed =+ newSpeed;
	}	

	/**
	 * Sets the angle.
	 * @param angle the objects new angle in radians, with 0 meaning straight 
	 * up and pi/2 to the left of the screen.
	 */
	public void setAngle(float angle){
		this.angle = angle;		
	}
	
	/**
	 * Gives the angle.
	 * @return the angle of this object in radians, with 0 meaning straight 
	 * up and pi/2 to the left of the screen.
	 */
	public float getAngle(){
		return angle;
	}
	
	/**
	 * Rotates the object.
	 * @param rotation the new angle relative to the current one in radians,
	 * with negative numbers meaning a counterclockwise rotation. 
	 */
	public void rotate(float rotation){
		angle =+ rotation;
	}
	
	/**
	 * Returns the shapes in this object. (in this class, there is always just one)
	 * @return an iterator of all shapes in this object.
	 */
	public Shape getShape(){
		return shape;		
	}
	
	/**
	 * Does everything the object itself should do every update.
	 * @param delta time since last update in milliseconds.
	 */
	public void update(int delta){
		if (life <= 0){
			alive = false;
			shape = null;
			return;
		}		
		life--;
		shape .setX(shape.getX()+speed*(float)Math.cos(angle));
		shape.setY(shape.getY()+speed*(float)Math.sin(angle));	
		if (shape.getX() > GameConstants.width) {
			shape.setX(-2*shape.getBoundingCircleRadius());			
		}
		if (shape.getY() > GameConstants.height) {
			shape.setY(-2*shape.getBoundingCircleRadius());
		}
		if (shape.getX() < -2*shape.getBoundingCircleRadius()) {
			shape.setX(GameConstants.width);			
		}
		if (shape.getY() < -2*shape.getBoundingCircleRadius()) {
			shape.setY(GameConstants.height);			
		}	

		//ps.update(delta);
	}
	
	/**
	 * Checks whether or not this object is alive.
	 * @return true if this object is alive.
	 */
	public boolean getLife(){
		return alive;
	}	
	
	/**
	 * Renders the bullet on the canvas.
	 */
	public void render(Graphics g) {
		g.setColor(Color.green);
		g.fill(this.shape);
		//ps.render();
	}
	
	/**
	 * @see kth.inda.terminalvelocity.SpaceObject#hits(kth.inda.terminalvelocity.SpaceObject)
	 */
	public boolean hits(SpaceObject o) {
		return shape.intersects(o.getShape());
	}
}