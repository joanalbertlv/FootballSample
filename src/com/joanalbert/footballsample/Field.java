package com.joanalbert.footballsample;

import java.util.Random;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.joanalbert.footballsample.elements.Ball;
import com.joanalbert.footballsample.elements.DrawableElement;
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
	public Player myPlayer1, myPlayer2, pcPlayer1, pcPlayer2;
	private Goal lGoal, rGoal;

	private World world;

	private Paint paint;

	private int myGoals=0, pcGoals=0;
	private boolean isGoal=false;
	
	public Field() {
		paint = new Paint();
	}

	public void create() {
		Vec2 gravity = new Vec2(0f, 9.8f);
		boolean doSleep = true;
		world = new World(gravity, doSleep);
		ball = new Ball(80.0f, 40.0f, world);
		walls = new Walls(10.0f, 5.0f, 160.0f, 85.0f, world);
		lGoal = new Goal(20.0f, 68.0f, true, world);
		rGoal = new Goal(150.0f, 68.0f, false, world);
		myPlayer1 = new Player(50.0f, 85.0f, true, world);
		myPlayer2 = new Player(65.0f, 85.0f, true, world);
		pcPlayer1 = new Player(95.0f, 85.0f, false, world);
		pcPlayer2 = new Player(110.0f, 85.0f, false, world);
	}

	int myCount = 1000, pcCount = 1000, goalCount=1000;

	public void update(long time, boolean kick, float fx, float fy) {
		// Logic
		
		//If there has been a goal recently 
		if (isGoal){
			goalCount++;
			if (goalCount==300){
				isGoal=false;
				Vec2 gravity = new Vec2(0f, 9.8f);
				boolean doSleep = true;
				world = new World(gravity, doSleep);
				ball = new Ball(80.0f, 40.0f, world);
				walls = new Walls(10.0f, 5.0f, 160.0f, 85.0f, world);
				lGoal = new Goal(20.0f, 68.0f, true, world);
				rGoal = new Goal(150.0f, 68.0f, false, world);
				myPlayer1 = new Player(50.0f, 85.0f, true, world);
				myPlayer2 = new Player(65.0f, 85.0f, true, world);
				pcPlayer1 = new Player(95.0f, 85.0f, false, world);
				pcPlayer2 = new Player(110.0f, 85.0f, false, world);
			}
		}else{
			// Detect goal
			if ((ball.body.getPosition().x < lGoal.bGoalEnd.getPosition().x + Goal.goalWidth)
					&& (ball.body.getPosition().y > lGoal.bGoalEnd.getPosition().y
							- Goal.goalHeight / 2)) {
				pcGoals++;
				goalCount=0;
				isGoal=true;
			}
			if ((ball.body.getPosition().x > rGoal.bGoalEnd.getPosition().x - Goal.goalWidth)
					&& (ball.body.getPosition().y > rGoal.bGoalEnd.getPosition().y
							- Goal.goalHeight / 2)) {
				myGoals++;
				goalCount=0;
				isGoal=true;
			}
		}

		boolean kickPc = false;
		float fxPc = 0;
		float fyPc = 0;
		Random r = new Random();
		double d = r.nextFloat();
		if (d < 0.05)
			kickPc = true;
		else if (d < 0.08)
			fxPc = 200;
		else if (d < 0.15)
			fxPc = -200;
		else if (d < 0.2)
			fyPc = -200;

		// My players
		if (kick)
			myCount = 0;

		if (myPlayer1.torso.getPosition().y > 60) {
			myPlayer1.torso.applyLinearImpulse(new Vec2(fx, fy),
					myPlayer1.torso.getPosition());
			myPlayer1.lLeg.applyLinearImpulse(new Vec2(fx * 2, fy * 2),
					myPlayer1.lLeg.getPosition());
			myPlayer1.rLeg.applyLinearImpulse(new Vec2(fx * 2, fy * 2),
					myPlayer1.rLeg.getPosition());
		}
		if (myPlayer2.torso.getPosition().y > 60) {
			myPlayer2.torso.applyLinearImpulse(new Vec2(fx, fy),
					myPlayer2.torso.getPosition());
			myPlayer2.lLeg.applyLinearImpulse(new Vec2(fx * 2, fy * 2),
					myPlayer2.lLeg.getPosition());
			myPlayer2.rLeg.applyLinearImpulse(new Vec2(fx * 2, fy * 2),
					myPlayer2.rLeg.getPosition());
		}

		if (myCount == 0) {
			myPlayer1.torso.applyLinearImpulse(new Vec2(-3000, -100),
					myPlayer1.torso.getPosition());
			myPlayer1.lLeg.applyLinearImpulse(new Vec2(-5000, -1000),
					myPlayer1.lLeg.getPosition());
			myPlayer1.rLeg.applyLinearImpulse(new Vec2(20000, -1000),
					myPlayer1.rLeg.getPosition());
			myPlayer2.torso.applyLinearImpulse(new Vec2(-3000, -100),
					myPlayer2.torso.getPosition());
			myPlayer2.lLeg.applyLinearImpulse(new Vec2(-5000, -1000),
					myPlayer2.lLeg.getPosition());
			myPlayer2.rLeg.applyLinearImpulse(new Vec2(20000, -1000),
					myPlayer2.rLeg.getPosition());
		} else if (myCount == 10) {
			myPlayer1.torso.applyLinearImpulse(new Vec2(4500, 0),
					myPlayer1.torso.getPosition());
			myPlayer1.lLeg.applyLinearImpulse(new Vec2(8000, 0),
					myPlayer1.lLeg.getPosition());
			myPlayer1.rLeg.applyLinearImpulse(new Vec2(-30000, 10000),
					myPlayer1.rLeg.getPosition());
			myPlayer2.torso.applyLinearImpulse(new Vec2(4500, 0),
					myPlayer2.torso.getPosition());
			myPlayer2.lLeg.applyLinearImpulse(new Vec2(8000, 0),
					myPlayer2.lLeg.getPosition());
			myPlayer2.rLeg.applyLinearImpulse(new Vec2(-30000, 10000),
					myPlayer2.rLeg.getPosition());
		}

		if (myCount < 20)
			myCount++;
		// Pc players
		if (kickPc)
			pcCount = 0;
		if (pcPlayer1.torso.getPosition().y > 60) {
			pcPlayer1.torso.applyLinearImpulse(new Vec2(fxPc, fyPc),
					pcPlayer1.torso.getPosition());
			pcPlayer1.lLeg.applyLinearImpulse(new Vec2(fxPc * 2, fyPc * 2),
					pcPlayer1.lLeg.getPosition());
			pcPlayer1.rLeg.applyLinearImpulse(new Vec2(fxPc * 2, fyPc * 2),
					pcPlayer1.rLeg.getPosition());
		}
		if (pcPlayer2.torso.getPosition().y > 60) {
			pcPlayer2.torso.applyLinearImpulse(new Vec2(fxPc, fyPc),
					pcPlayer2.torso.getPosition());
			pcPlayer2.lLeg.applyLinearImpulse(new Vec2(fxPc * 2, fyPc * 2),
					pcPlayer2.lLeg.getPosition());
			pcPlayer2.rLeg.applyLinearImpulse(new Vec2(fxPc * 2, fyPc * 2),
					pcPlayer2.rLeg.getPosition());
		}
		if (pcCount == 0) {
			pcPlayer1.torso.applyLinearImpulse(new Vec2(3000, -100),
					pcPlayer1.torso.getPosition());
			pcPlayer1.lLeg.applyLinearImpulse(new Vec2(5000, -1000),
					pcPlayer1.lLeg.getPosition());
			pcPlayer1.rLeg.applyLinearImpulse(new Vec2(-20000, -1000),
					pcPlayer1.rLeg.getPosition());
			pcPlayer2.torso.applyLinearImpulse(new Vec2(3000, -100),
					pcPlayer2.torso.getPosition());
			pcPlayer2.lLeg.applyLinearImpulse(new Vec2(5000, -1000),
					pcPlayer2.lLeg.getPosition());
			pcPlayer2.rLeg.applyLinearImpulse(new Vec2(-20000, -1000),
					pcPlayer2.rLeg.getPosition());
		} else if (pcCount == 10) {
			pcPlayer1.torso.applyLinearImpulse(new Vec2(-4500, 0),
					pcPlayer1.torso.getPosition());
			pcPlayer1.lLeg.applyLinearImpulse(new Vec2(-8000, 0),
					pcPlayer1.lLeg.getPosition());
			pcPlayer1.rLeg.applyLinearImpulse(new Vec2(30000, 10000),
					pcPlayer1.rLeg.getPosition());
			pcPlayer2.torso.applyLinearImpulse(new Vec2(-4500, 0),
					pcPlayer2.torso.getPosition());
			pcPlayer2.lLeg.applyLinearImpulse(new Vec2(-8000, 0),
					pcPlayer2.lLeg.getPosition());
			pcPlayer2.rLeg.applyLinearImpulse(new Vec2(+30000, 10000),
					pcPlayer2.rLeg.getPosition());
		}

		if (pcCount < 20)
			pcCount++;

		world.step(time / 1000.0f, velocityIterations, positionIterations);
	}

	public void draw(Canvas canvas) {
		canvas.drawColor(backgroundColor);
		
		paint.setColor(Color.WHITE); 
		paint.setTextSize(60); 
		canvas.drawText("Team1  " + myGoals + " - " + pcGoals + "  Team2 " , 55*DrawableElement.WORLD_SCALE, 15*DrawableElement.WORLD_SCALE, paint); 
		
		if (isGoal){
			paint.setTextSize(200); 
			canvas.drawText("GOAL!" , 56*DrawableElement.WORLD_SCALE, 45*DrawableElement.WORLD_SCALE, paint); 
			
		}
		
		walls.draw(canvas, paint);
		lGoal.draw(canvas, paint);
		rGoal.draw(canvas, paint);
		ball.draw(canvas, paint);
		myPlayer1.draw(canvas, paint);
		myPlayer2.draw(canvas, paint);
		pcPlayer1.draw(canvas, paint);
		pcPlayer2.draw(canvas, paint);
	}

}