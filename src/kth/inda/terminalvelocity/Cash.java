package kth.inda.terminalvelocity;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;

/**
 * Class that describes money, can come out of asteroids
 * upon destruction.
 * @author Fredrik
 * @version 2008-04-22
 */

public class Cash implements SpaceObject {
	private float angle;
	private float speed;
	private Shape shape;
	private boolean alive;
	private int life;
	private CashSize size;

	public enum CashSize {
		ONE_HUNDRED, TWO_HUNDRED, FIVE_HUNDRED;
	}

	/**
	 * Creates new cash.
	 * @param x the x-position of the new cash.
	 * @param y the y-position of the new cash.
	 * @param size the descriptor of how much money.
	 * @param angle the angle of the new cash.
	 * @param speed the speed of the new cash.
	 */
	public Cash(float x, float y, CashSize size,
			float angle, float speed) {
		//shape = new Rectangle(x, y, 30, 15);
		Polygon poly = new Polygon();
		poly.addPoint(10, 0);
		poly.addPoint(0, 10);
		poly.addPoint(10, 30);
		poly.addPoint(20, 10);
		poly.setX(x);
		poly.setY(y);
		this.shape = poly;
		this.angle = angle;
		this.speed = speed;
		this.alive = true;
		this.size = size;
		this.life = GameConstants.MONEY_LIFETIME;
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
	}

	/**
	 * Checks whether or not this object is alive.
	 * @return true if this object is alive.
	 */
	public boolean getLife(){
		return alive;
	}	

	/**
	 * Renders the cash on the canvas.
	 */
	public void render(Graphics g) {
		switch(this.size) {
		case ONE_HUNDRED:g.setColor(Color.green); break;
		case TWO_HUNDRED:g.setColor(Color.blue); break;
		case FIVE_HUNDRED:g.setColor(Color.red); break;
		default:break;
		}
		Color temp = g.getColor();
		temp.a=150;
		g.setColor(temp);
		g.fill(this.shape);
		g.setColor(Color.white);
	}

	/**
	 * Returns how much money contained in this Cash.
	 */
	public int getMoney() {
		switch(this.size) {
		case ONE_HUNDRED:return 100;
		case TWO_HUNDRED:return 200;
		case FIVE_HUNDRED:return 500;
		default: return 0;
		}
	}

	/**
	 * @see kth.inda.terminalvelocity.SpaceObject#hits(kth.inda.terminalvelocity.SpaceObject)
	 */
	public boolean hits(SpaceObject o) {
		return shape.intersects(o.getShape());
	}
}
