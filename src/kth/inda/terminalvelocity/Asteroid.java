package kth.inda.terminalvelocity;

import net.phys2d.math.MathUtil;
import net.phys2d.math.ROVector2f;
import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Circle;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Shape;

/**
 * This class is used to describe an asteroid flying
 * through space.
 * @author Fredrik Gustafsson and Viktor Holmberg
 * @version 2009-04-14
 */

public class Asteroid implements SpaceObject{

	private static final long serialVersionUID = 1L;
	private float angle;
	private float speed;
	private float radius;
	private float mass;
	private World space;
	private Shape shape;
	private Color col;
	private Body body;

	/**
	 * Creates a new asteroid.
	 * @param centerPointX the x-position of the new asteroid.
	 * @param centerPointY the y-position of the new asteroid.
	 * @param radius the radius of the new asteroid. 
	 * @param angle the angle of the new asteroid relative
	 * the x-axis.
	 * @param energy the energy of the new asteroid.
	 * @param mass the mass of this asteroid
	 * @param space 
	 */
	public Asteroid(float centerPointX, float centerPointY, float radius,
			float angle, float energy, float mass, World space) {
		this.body = new Body(new Circle(radius), mass);
		body.setPosition(centerPointX, centerPointY);
		this.mass = mass;
		this.radius = radius;
		this.space = space;
		this.angle = angle;
		body.setFriction(1f);
		body.setGravityEffected(false);
		body.setMoveable(true);
		body.setRestitution(0f);
		body.setForce((float) (Math.cos(angle)*energy), (float)Math.sin(angle)*energy);
		body.setMaxVelocity(200f, 200f);
		space.add(body);
		this.shape = bodyToShape(body);
		col = GameConstants.ASTEROID_COLOR.darker((float) (GameConstants.rand.nextFloat()*0.6));
	}

	/**
	 * Creates a new, standard, asteroid.
	 */
	public Asteroid(float centerPointX, float centerPointY, World space) {
		this(centerPointX,centerPointY,
				GameConstants.ASTEROID_MINSIZE+GameConstants.rand.nextFloat()*(GameConstants.ASTEROID_MAXSIZE-GameConstants.ASTEROID_MINSIZE),
				(float) (GameConstants.rand.nextFloat()*Math.PI*2),
				GameConstants.rand.nextFloat()*GameConstants.ASTEROID_DEFAULTMAXENERGY,
				(GameConstants.ASTEROID_MINSIZE+GameConstants.rand.nextFloat()*(GameConstants.ASTEROID_MAXSIZE-GameConstants.ASTEROID_MINSIZE)/(GameConstants.ASTEROID_MAXSIZE-GameConstants.ASTEROID_MINSIZE))
				*1000000,
				space);
		System.out.println(body.getEnergy());
	}

	/**
	 * Returns the radius of this object.
	 * @return the radius of this object.
	 */
	public float getRadius() {
		return this.radius;
	}

	/**
	 * Makes the asteroid split in two. The asteroids gets reduced in size
	 * and returns another asteroid of the same size but different angle.
	 * @param angle the angle of the object making the asteroid split.
	 * @return the new asteroid created.
	 */
	public Asteroid[] split(float angle) {
		radius /= 2;
		if(radius<GameConstants.ASTEROID_MINSIZE)
			return new Asteroid[0];
		mass /= 2.8;
		float energy = 99999999999999999999999F+body.getEnergy()/2;
		body.setShape(new Circle(radius));
		this.shape = bodyToShape(body);
		this.angle = angle+1f;
		Asteroid[] asteroids = new Asteroid[2];
		asteroids[0] = new Asteroid(body.getPosition().getX(),
				body.getPosition().getY(), radius, angle+3*((GameConstants.rand.nextFloat()-1.5f)), energy, mass, space);
		asteroids[1] = new Asteroid(body.getPosition().getX()+1,
				body.getPosition().getY()+1, radius, angle+3*((GameConstants.rand.nextFloat()-1.5f)), energy, mass, space);
		return asteroids;
	}

	/* (non-Javadoc)
	 * @see kth.inda.terminalvelocity.SpaceObject#update(int)
	 */
	public void update(int delta) {
		if(body==null) return;
		float x = body.getPosition().getX();
		float y = body.getPosition().getY();

		//Check the screen bounds for wrap-around.
		float width = body.getShape().getBounds().getWidth();
		float height  = body.getShape().getBounds().getHeight();
		if (x > GameConstants.width+width) {
			x = -width;			
		}
		if (y > GameConstants.height+height) {
			y = -height;
		}
		if (x < -width) {
			x = GameConstants.width+width;			
		}
		if (y < -height) {
			y = GameConstants.height+height;			
		}
		body.setPosition(x, y);
		this.shape = bodyToShape(body);
	}

	/**
	 * Accelerates the body due to gravitation.
	 * @param a the asteroid to accelerate towards.
	 */
	public void accelerateTowards(Asteroid a) {
		ROVector2f toVector = a.getBody().getPosition();
		ROVector2f thisVector = body.getPosition();
		float distance = thisVector.distance(toVector);
		float distance2;
		distance2 = distance+20;
		if (!(distance<50))
			distance2 = thisVector.distanceSquared(toVector);

		if(distance>a.getRadius()+this.radius+1) {
			//A derivative of Newton's gravity-formula.			
			float force = 0.01f*(mass*a.getMass())/(distance2);
			//Gets the vector towards the other object.
			Vector2f forceVector = MathUtil.sub(toVector, thisVector);
			//Normalize the vector.
			forceVector.normalise();
			//Scale the vector to our calculated force.
			forceVector = MathUtil.scale(forceVector, force);
			body.addForce(forceVector);
		}
	}

	/**
	 * Removes all physics to get ready for deletion.
	 */
	public void removePhysics() {
		//space.remove(body);
		//body = null;
	}

	/**
	 * Returns the mass of the asteroid.
	 */
	public float getMass() {
		return this.mass;
	}

	/**
	 * Returns the body of the asteroid.
	 */
	public Body getBody() {
		return this.body;
	}

	/* (non-Javadoc)
	 * @see kth.inda.terminalvelocity.SpaceObject#getAngle()
	 */
	public float getAngle() {
		return this.angle;
	}

	/* (non-Javadoc)
	 * @see kth.inda.terminalvelocity.SpaceObject#setSpeed(float)
	 */
	public void setSpeed(float speed){
		this.speed = speed;
	}

	/* (non-Javadoc)
	 * @see kth.inda.terminalvelocity.SpaceObject#getSpeed()
	 */
	public float getSpeed(){
		return body.getAngularVelocity();
	}

	/* (non-Javadoc)
	 * @see kth.inda.terminalvelocity.SpaceObject#accelerate(float)
	 */
	public void accelerate(float newSpeed){
		this.speed += newSpeed;
	}	

	/* (non-Javadoc)
	 * @see kth.inda.terminalvelocity.SpaceObject#setAngle(float)
	 */
	public void setAngle(float angle){
		this.angle = angle;
	}

	/* (non-Javadoc)
	 * @see kth.inda.terminalvelocity.SpaceObject#rotate(float)
	 */
	public void rotate(float rotation){
		this.angle = rotation;
	}

	/**
	 * Converts the body to a slick-renderable shape.
	 * @param body the body to convert.
	 * @return the shape corresponding to the body.
	 */
	private Shape bodyToShape(Body body) {
		if(body==null) return null;
		float x = body.getPosition().getX();
		float y = body.getPosition().getY();
		return new org.newdawn.slick.geom.Circle(x,y,radius);
	}

	/* (non-Javadoc)
	 * @see kth.inda.terminalvelocity.SpaceObject#getShape()
	 */
	public Shape getShape(){
		return this.shape;
	}

	/**
	 * Renders the asteroid.
	 */
	public void render(Graphics g) {
		if(shape==null) return;
		g.setColor(col);
		g.fill(shape);
		g.setColor(Color.white);
		if(GameConstants.debug)
			g.drawString(""+getMass(),getShape().getCenterX(),getShape().getCenterY());
	}

	/* (non-Javadoc)
	 * @see kth.inda.terminalvelocity.SpaceObject#hits(kth.inda.terminalvelocity.SpaceObject)
	 */
	public boolean hits(SpaceObject o) {
		if(shape==null) return false;
		return shape.intersects(o.getShape());
	}
}
