package kth.inda.terminalvelocity;

import java.io.IOException;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

public class Wormhole implements SpaceObject{

	private ParticleSystem ps;
	private float x;
	private float y;
	
	public Wormhole(float x, float y) {
		try {
			ps = ParticleIO.loadConfiguredSystem("wormhole.xml");
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.x = x;
		this.y = y;
		ps.setPosition(x, y);
	}
	
	public void accelerate(float deltaSpeed) {
		return;
	}

	public float getAngle() {
		return 0;
	}

	public Shape getShape() {
		return new Circle(this.x, this.y, 60);
	}

	public float getSpeed() {
		return 0;
	}

	public boolean hits(SpaceObject o) {
		Circle hitCircle = new Circle(this.x, this.y, 60);
		return hitCircle.intersects(o.getShape());
	}

	public void render(Graphics g) {
		ps.render();
	}

	public void rotate(float rotation) {
		return;
	}

	public void setAngle(float angle) {
		return;
	}

	public void setSpeed(float speed) {
		return;
	}

	public void update(int delta) {
		ps.update(delta);
	}

}
