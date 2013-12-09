/**
 * 
 */
package kth.inda.terminalvelocity;

import java.io.IOException;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

/**
 * Handles a simple particle effect.
 * 
 * @author Viktor Holmberg
 *
 */
public class ParticleEffect {
	ParticleSystem ps;
	boolean alive = true;
	
	/**
	 * a new particle effect.
	 * @param effect the filename of the particle system to load
	 * @param x 
	 * @param y
	 */
	public ParticleEffect(String effect, float x, float y){
			try {
				ps = ParticleIO.loadConfiguredSystem(effect);
			} catch (IOException e) {
				System.out.println("missing files. Re-install recommended");
			}
			ps.setPosition(x, y);		
	}
	
	/**
	 * Updates the particlesystem
	 */
	public void update(int delta) {
		ps.update(delta);
	}
	
	
	/**
	 * Returns if the particle-effect still is active.
	 * @return true if the particle-effect is still
	 * active, false otherwise.
	 */
	public boolean getAlive(){
		//runs until there are no more particles
		return !(ps.getParticleCount()==0);
	}
	
	/**
	 * Renders the object on the given canvas.
	 */
	public void render(Graphics g){
		ps.render();
	}
}
