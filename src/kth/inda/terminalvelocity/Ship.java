package kth.inda.terminalvelocity;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;

/**
 * This class is used to describe a ship flying through
 * space.
 * @author Fredrik Gustafsson and Viktor Holmberg
 * @version 0.1
 */

public class Ship implements SpaceObject {
	private float angle;
	private Shape shape;
	private float xSpeed;
	private float ySpeed;
	
	private float xGravSpeed;
	private float yGravSpeed;
	
	private static final long serialVersionUID = 1L;
	private static final float mass = (float) 0.000000005;
	private int invincibility = GameConstants.INVINSIBILITYTIME;

	/**
	 * Creates a ship.
	 */
	public Ship() {
		shape = new Polygon();
		((Polygon) shape).addPoint(0,0);
		((Polygon) shape).addPoint(5,10);
		((Polygon) shape).addPoint(0,20);
		((Polygon) shape).addPoint(25,10);
		shape.setX(480);
		shape.setY(420);
		xSpeed=0;
		ySpeed=0;
	}

	/* (non-Javadoc)
	 * @see kth.inda.terminalvelocity.SpaceObject#rotate(float)
	 */
	public void rotate(float rotation)
	{
		float tempX = shape.getX();
		float tempY = shape.getY();
		shape.setX(0);
		shape.setY(0);
		angle = (float) (angle%(2f*Math.PI) + rotation);
		Transform fwd_rotate = Transform.createRotateTransform((float)rotation,shape.getCenterX(),shape.getCenterY());
		shape = (Polygon) shape.transform(fwd_rotate);
		shape.setX(tempX);
		shape.setY(tempY);
	}

	/**
	 * Tells the ship to accelerate by a fixed
	 * value GameConstants.SHIP_ACCELERATION.
	 * Cannot accelerate past GameConstants.SHIP_MAXSPEED.
	 */
	public void accelerate() {	
		xSpeed+=GameConstants.SHIP_ACCELERATION* Math.cos(angle);
		ySpeed+=GameConstants.SHIP_ACCELERATION* Math.sin(angle);
		if  ((xSpeed*xSpeed+ySpeed*ySpeed)>GameConstants.SHIP_MAXSPEED*GameConstants.SHIP_MAXSPEED){
			float speedAngle = (float) Math.atan2(ySpeed, xSpeed);
			xSpeed=(float) (Math.cos(speedAngle)*GameConstants.SHIP_MAXSPEED);
			ySpeed=(float) (Math.sin(speedAngle)*GameConstants.SHIP_MAXSPEED);
		}
	}

	/**
	 * Tells the ship to accelerate by a given value.
	 * Cannot accelerate past GameConstants.SHIP_MAXSPEED.
	 * @param speedChange the speed to accelerate with.
	 */
	public void accelerate(float speedChange) {	
		xSpeed+=(speedChange*Math.cos(angle));
		ySpeed+=(speedChange*Math.sin(angle));
		if  ((xSpeed*xSpeed+ySpeed*ySpeed)>GameConstants.SHIP_MAXSPEED*GameConstants.SHIP_MAXSPEED){
			float speedAngle = (float) Math.atan2(ySpeed, xSpeed);
			xSpeed=(float) (Math.cos(speedAngle)*GameConstants.SHIP_MAXSPEED);
			ySpeed=(float) (Math.sin(speedAngle)*GameConstants.SHIP_MAXSPEED);
		}
	}
	
	/**
	 * Tells the ship to accelerate by a given value towards
	 * a asteroid, taking into account its mass and distance.
	 * Cannot accelerate past GameConstants.GRAV_MAXSPEED.
	 * @param obj the spaceobject to accelerate towards.
	 */
	public void gravAccelerate(Asteroid aster) {
		float xLength = (aster.getShape().getCenterX()-shape.getCenterX());
		float yLength = (aster.getShape().getCenterY() - shape.getCenterY() );
		float distance = (float) Math.sqrt(Math.pow(xLength, 2) + Math.pow(yLength, 2));
		if(distance<aster.getRadius()) return;
		float angle = (float) Math.atan2(yLength, xLength);
		float force = (mass*aster.getMass());//(distance*distance);	
		
		xGravSpeed=(float)(force*Math.cos(angle));
		yGravSpeed=(float)(force*Math.sin(angle));
		if  ((xGravSpeed*xGravSpeed+yGravSpeed*yGravSpeed)>GameConstants.GRAV_MAXSPEED*GameConstants.GRAV_MAXSPEED){
			float speedAngle = (float) Math.atan2(yGravSpeed, xGravSpeed);
			xGravSpeed=(float) (Math.cos(speedAngle)*GameConstants.GRAV_MAXSPEED);
			yGravSpeed=(float) (Math.sin(speedAngle)*GameConstants.GRAV_MAXSPEED);
		}
	}
	
	/* (non-Javadoc)
	 * @see kth.inda.terminalvelocity.SpaceObject#update(int)
	 */
	public void update(int delta) {
		if (invincibility>0)
			invincibility--;
		//Updates the location and checks to ensure ship does
		//not go off screen down or right
		xSpeed += xGravSpeed;
		ySpeed += yGravSpeed;
		if  ((xSpeed*xSpeed+ySpeed*ySpeed)>GameConstants.TOTAL_MAXSPEED*GameConstants.TOTAL_MAXSPEED){
			float speedAngle = (float) Math.atan2(ySpeed, xSpeed);
			xSpeed=(float) (Math.cos(speedAngle)*GameConstants.TOTAL_MAXSPEED);
			ySpeed=(float) (Math.sin(speedAngle)*GameConstants.TOTAL_MAXSPEED);
		}
		shape.setLocation(((shape.getX()+xSpeed)%GameConstants.width),
				(shape.getY()+ySpeed)%GameConstants.height);

		//Check that the ship does not go off screen up or left
		if(shape.getX()<0) shape.setX(GameConstants.width);
		if(shape.getY()<0) shape.setY(GameConstants.height);
	}

	/* (non-Javadoc)
	 * @see kth.inda.terminalvelocity.SpaceObject#setAngle(float)
	 */
	public void setAngle(float angle) {
		this.angle = angle;
	}

	/* (non-Javadoc)
	 * @see kth.inda.terminalvelocity.SpaceObject#getAngle()
	 */
	public float getAngle() {
		return this.angle;
	}

	/**
	 * Creates a new bullet based on the ships position
	 * and angle.
	 * @return a new bullet.
	 */
	public Bullet fireBullet() {
		return new Bullet(shape.getCenterX(), shape.getCenterY(), 2,
				angle, 8f);

	}

	/* (non-Javadoc)
	 * @see kth.inda.terminalvelocity.SpaceObject#getShape()
	 */
	public Shape getShape() {
		return this.shape;
	}

	/* (non-Javadoc)
	 * @see kth.inda.terminalvelocity.SpaceObject#getSpeed()
	 */
	public float getSpeed() {
		return (float) (Math.sqrt((xSpeed*xSpeed+ySpeed*ySpeed)));
	}

	/**
	 * Sets the polygon that is the ship.
	 * @param newShape the new polygon.
	 */
	public void setShape(Shape newShape) {
		this.shape = newShape;
	}

	/* (non-Javadoc)
	 * @see kth.inda.terminalvelocity.SpaceObject#setSpeed(float)
	 */
	public void setSpeed(float speed) {
		xSpeed=speed*(float) (Math.cos(angle));
		ySpeed=speed*(float) (Math.sin(angle));
	}

	/**
	 * Renders the shape.
	 * @see kth.inda.terminalvelocity.SpaceObject#render(org.newdawn.slick.Graphics)
	 */
	public void render(Graphics g) {
		if (invincibility>0)
			g.setColor(Color.cyan);
		else
			g.setColor(Color.white);
		g.fill(this.shape);
		g.setColor(Color.white);
	}

	/**
	 * Checks if the ship hits another object in space.
	 * @see kth.inda.terminalvelocity.SpaceObject#hits(kth.inda.terminalvelocity.SpaceObject)
	 */
	public boolean hits(SpaceObject o) {
		if (invincibility>0)
			return false;
		else
			return shape.intersects(o.getShape());
	}

	/**
	 * Returns the ships x-position.
	 */
	public float getX(){		
		return shape.getCenterX();
	}

	/**
	 * Returns the ships y-position.
	 */
	public float getY(){		
		return shape.getCenterY();
	}
	/**
	 * Sets new coordinates for the ship. The new coordinates must be 
	 * in the range of 0-GameConstants.width/height.
	 * @return true if the change of coordinates was successful.
	 */
	public Boolean setPosition(float x, float y){
		if ((x < GameConstants.width)&&(x>0)&&(y < GameConstants.height)&&(y>0)){
			shape.setLocation(x, y);
			return true;
		}
		else{
			return false;
		}		
	}
}
