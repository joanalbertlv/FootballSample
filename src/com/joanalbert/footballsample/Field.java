package com.joanalbert.footballsample;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.joanalbert.footballsample.elements.Ball;
import com.joanalbert.footballsample.elements.Goal;
import com.joanalbert.footballsample.elements.Player;
import com.joanalbert.footballsample.elements.Walls;

public class Field {

	private static final int backgroundColor = Color.BLACK;
		
	public float timeStep = 1.0f / 60.0f;
	private int velocityIterations = 6;
	private int positionIterations = 2;

	private Ball ball;
	private Walls walls;
	private Player player;
	private Goal lGoal, rGoal;
	
	private World world;

	private Paint paint;

	public Field() {
		paint = new Paint();
	}

	public void create() {
		Vec2 gravity = new Vec2(0f, 9.8f);
		boolean doSleep = true;
		world = new World(gravity, doSleep);
		ball = new Ball(80.0f, 40.0f, world);
		walls = new Walls(5.0f, 5.0f, 165.0f, 85.0f, world);
		lGoal = new Goal(20.0f, 68.0f, true, world);
		rGoal = new Goal(150.0f, 68.0f, false, world);
		player = new Player(50.0f, 85.0f, world);
	}
	
	int count=1000; 
	
	public void update(long time, boolean kick,  float fx, float fy) {
		//Logic
		if (kick) count=0;
		player.torso.applyLinearImpulse(new Vec2(fx, fy), player.torso.getPosition());
		player.lLeg.applyLinearImpulse(new Vec2(fx*2, fy*2), player.lLeg.getPosition());
		player.rLeg.applyLinearImpulse(new Vec2(fx*2, fy*2), player.rLeg.getPosition());
		
		if (count==0){
			player.torso.applyLinearImpulse(new Vec2(-3000, -100), player.torso.getPosition());
			player.lLeg.applyLinearImpulse(new Vec2(-5000, -1000), player.lLeg.getPosition());
			player.rLeg.applyLinearImpulse(new Vec2(20000, -1000), player.rLeg.getPosition());			
		}else if (count==10){
			player.torso.applyLinearImpulse(new Vec2(4500, 0), player.torso.getPosition());
			player.lLeg.applyLinearImpulse(new Vec2(8000, 0), player.lLeg.getPosition());
			player.rLeg.applyLinearImpulse(new Vec2(-30000, 10000), player.rLeg.getPosition());	
		}
		if (count<110) count++;

		
		world.step(time / 1000.0f, velocityIterations, positionIterations);
	}
 
	
	public void draw(Canvas canvas) {
		canvas.drawColor(backgroundColor);		
		walls.draw(canvas, paint);
		lGoal.draw(canvas, paint);
		rGoal.draw(canvas, paint);
		ball.draw(canvas, paint);
		player.draw(canvas, paint);	
	}
	

}