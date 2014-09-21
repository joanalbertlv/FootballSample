package com.joanalbert.footballsample;

import java.util.Random;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.joanalbert.footballsample.elements.Ball;
import com.joanalbert.footballsample.elements.Goal;
import com.joanalbert.footballsample.elements.Player;
import com.joanalbert.footballsample.elements.Walls;

// This class contains all the logical and graphical information about the game
public class Field {

	private static final int backgroundColor = Color.BLACK;

	public float timeStep = 1.0f / 60.0f;
	private int velocityIterations = 6;
	private int positionIterations = 2;

	// Elements of the game
	private Ball ball;
	private Walls walls;
	public Player myPlayer1, myPlayer2, pcPlayer1, pcPlayer2;
	private Goal lGoal, rGoal;

	private World world;
	private Paint paint;

	private boolean isGoal = false;

	public Field() {
		paint = new Paint();
	}

	public void create() {
		// Initialize all the elements of the game
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

	// Counters used for long actions (kick, goal animation, etc.)
	int myCount = 1000, pcCount = 1000, goalCount = 1000;

	public void update(long time, boolean kick, float fx, float fy) {
		// Logic of the game
		// If there has been a goal recently
		if (isGoal) {
			goalCount++;
			if (goalCount == 300) {
				// After some cycles showing a goal message, we restart the game
				isGoal = false;
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
		} else {
			// Detect goal
			if ((ball.body.getPosition().x < lGoal.bGoalEnd.getPosition().x
					+ Goal.goalWidth)
					&& (ball.body.getPosition().y > lGoal.bGoalEnd
							.getPosition().y - Goal.goalHeight / 2)) {
				GameInfo.pcGoals++;
				goalCount = 0;
				isGoal = true;
			}
			if ((ball.body.getPosition().x > rGoal.bGoalEnd.getPosition().x
					- Goal.goalWidth)
					&& (ball.body.getPosition().y > rGoal.bGoalEnd
							.getPosition().y - Goal.goalHeight / 2)) {
				GameInfo.myGoals++;
				goalCount = 0;
				isGoal = true;
			}
		}

		// My team
		// In the case of the players controlled by the user, we receive the
		// game actions as parameters of this function, depending on the actions
		// performed in the screen.

		// If the players are touching the floor, we apply the forces according
		// to the movements of the finger in the screen
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

		// If we have a trigger for a kick, we start the kick counter, and we
		// perform the first part of the kick
		if (kick) {
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
			myCount = 0;
		}
		// Some cycles after the beginning of the kick, we perform the second
		// part
		if (myCount == 10) {
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
		// If we still have not finished the kick we increase the counter
		if (myCount < 20)
			myCount++;

		// App team
		// We randomly decide the actions done for the players controlled by the
		// app
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

		// If the players are touching the floor, we apply the forces according
		// to the movements decided before
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
		// If we have a trigger for a kick, we start the kick counter, and we
		// perform the first part of the kick
		if (kickPc) {
			pcCount = 0;
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
		}
		// Some cycles after the beginning of the kick, we perform the second
		// part
		if (pcCount == 10) {
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

		// If we still have not finished the kick we increase the counter
		if (pcCount < 20)
			pcCount++;

		// One step more
		world.step(time / 1000.0f, velocityIterations, positionIterations);
	}

	public void draw(Canvas canvas) {
		canvas.drawColor(backgroundColor);

		// Scoreboard drawn
		paint.setColor(Color.WHITE);
		paint.setTextSize(60);
		canvas.drawText("Team1  " + GameInfo.myGoals + " - " + GameInfo.pcGoals
				+ "  Team2 ", 55 * GameInfo.worldScale,
				15 * GameInfo.worldScale, paint);

		// When there is a goal, we show it in the middle of the screen
		if (isGoal) {
			paint.setTextSize(200);
			canvas.drawText("GOAL!", 56 * GameInfo.worldScale,
					45 * GameInfo.worldScale, paint);

		}

		// We draw each one of the elements
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