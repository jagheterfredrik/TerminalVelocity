package kth.inda.terminalvelocity;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Shape;

/**
 * An interface for all the "actors" in the game, such as asteroids or the ship.
 * @author Viktor Holmberg
 * @version 0.11
 *
 */
public interface SpaceObject{	
	
	/**
	 * Sets the speed.
	 * @param speed the objects new speed, in pixels per update.
	 */
	public void setSpeed(float speed);

	/**
	 * Gives the speed.
	 * @return the speed of this object, in pixels per update.
	 */
	public float getSpeed();	
	
	/**
	 * Increases/decreases the speed.
	 * @param speed the change of speed relative to the
	 * current one, in pixels per update.
	 */
	public void accelerate(float deltaSpeed);

	/**
	 * Sets the angle.
	 * @param angle the objects new angle in radians, with 0 meaning straight 
	 * up and pi/2 to the left of the screen.
	 */
	public void setAngle(float angle);
	
	/**
	 * Gives the angle.
	 * @return the objects angle in radians, with 0 meaning straight 
	 * up and pi/2 to the left of the screen.
	 */
	public float getAngle();
	
	/**
	 * Rotates the object.
	 * @param rotation the new direction relative to the current one in radians,
	 * with negative numbers meaning a counterclockwise rotation. 
	 */
	public void rotate(float rotation);

	/**
	 * Does everything the object itself should do every update.
	 * @param delta time in milliseconds since the last update.
	 */
	public void update(int delta);
	
	/**
	 * Returns the shape in this object.
	 * @return the shape in this object.
	 */
	public Shape getShape();
	
	/**
	 * Renders the object on the given canvas.
	 */
	public void render(Graphics g);
	
	/**
	 * Returns true if this objects is colliding with another object.
	 */
	public boolean hits(SpaceObject o);
}
