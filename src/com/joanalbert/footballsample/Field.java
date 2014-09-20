package com.joanalbert.footballsample;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.joanalbert.footballsample.elements.Ball;
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
		walls = new Walls(5.0f, 5.0f, 105.0f, 85.0f, world);
		player = new Player(50.0f, 50.0f, world);
	}
	
	int count=0; 
	
	public void update(long time) {
		count++;
		if (count%50==0){
			player.torso.applyLinearImpulse(new Vec2(200, -500), new Vec2(0,0));
		}
		
		world.step(time / 1000.0f, velocityIterations, positionIterations);
	}
 
	
	public void draw(Canvas canvas) {
		canvas.drawColor(backgroundColor);		
		walls.draw(canvas, paint);
		ball.draw(canvas, paint);
		player.draw(canvas, paint);	
	}
	

}