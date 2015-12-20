package fr.mimus.canvas.render;

import java.awt.event.KeyEvent;

import fr.mimus.canvas.InputHandler;
import fr.mimus.canvas.utils.Vec3;

public class Game extends Bitmap3D
{
	private final double rot90 = Math.toRadians(90);
	private final double speed = 0.05;
	Vec3 cam;
	double rot;
	
	public Game(int width, int height)
	{
		super(width, height);
		
		cam = new Vec3();
		rot = 0.0;
	}
	
	public void render()
	{
		clear();
		translate(-cam.x, -cam.y, -cam.z);
		rotate(rot);
		super.render();
		renderFog();
	}
	
	public void update(InputHandler input) {
		if (input.keys[KeyEvent.VK_LEFT])
			rot += 0.025;
		if (input.keys[KeyEvent.VK_RIGHT])
			rot -= 0.025;
		if (input.keys[KeyEvent.VK_UP] || input.keys[KeyEvent.VK_Z])
		{
			double cosY = Math.cos(rot - rot90);
			double sinY = Math.sin(rot - rot90);
			Vec3 forward = new Vec3(cosY, 0, sinY);
			
			forward.normalize();
			forward.mul(speed);
			cam.x += forward.x;
			cam.z += forward.z;
		} 
		if (input.keys[KeyEvent.VK_DOWN] || input.keys[KeyEvent.VK_S])
		{
			double cosY = Math.cos(rot - rot90);
			double sinY = Math.sin(rot - rot90);
			Vec3 forward = new Vec3(cosY, 0, sinY);
			
			forward.normalize();
			forward.mul(speed);
			cam.x -= forward.x;
			cam.z -= forward.z;
		}
		if (input.keys[KeyEvent.VK_D])
		{
			double cosY = Math.cos(rot);
			double sinY = Math.sin(rot);
			Vec3 side = new Vec3(cosY, 0, sinY);
			
			side.normalize();
			side.mul(speed);
			cam.x -= side.x;
			cam.z -= side.z;
		}
		if (input.keys[KeyEvent.VK_Q])
		{
			double cosY = Math.cos(rot);
			double sinY = Math.sin(rot);
			Vec3 side = new Vec3(cosY, 0, sinY);
			
			side.normalize();
			side.mul(speed);
			cam.x += side.x;
			cam.z += side.z;
		}
	}
}
