package com.joanalbert.footballsample;

import java.util.Random;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.Log;

import com.joanalbert.footballsample.elements.Ball;
import com.joanalbert.footballsample.elements.Goal;
import com.joanalbert.footballsample.elements.Player;
import com.joanalbert.footballsample.elements.Walls;

//This class represents the field in which the game takes place.
//It contains all the logics and it draws all the elements.
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

	private void initialize() {
		// Initialize all the elements of the game
		Vec2 gravity = new Vec2(0f, 20f); // Gravity value chosen after several
											// tests
		boolean doSleep = true;
		world = new World(gravity, doSleep);
		ball = new Ball(world);
		walls = new Walls(2.0f, 2.0f, GameInfo.worldWidth,
				GameInfo.worldHeight, world);
		lGoal = new Goal(true, world);
		rGoal = new Goal(false, world);

		myPlayer1 = new Player((GameInfo.worldWidth / 2) - 20.0f,
				(GameInfo.worldHeight / 2) + 10, true, world);
		myPlayer2 = new Player((GameInfo.worldWidth / 2) - 10.0f,
				(GameInfo.worldHeight / 2) + 10, true, world);
		pcPlayer1 = new Player((GameInfo.worldWidth / 2) + 10.0f,
				(GameInfo.worldHeight / 2) + 10, false, world);
		pcPlayer2 = new Player((GameInfo.worldWidth / 2) + 20.0f,
				(GameInfo.worldHeight / 2) + 10, false, world);
	}

	public void create() {
		initialize();
	}

	// Counters used for long actions (kick, goal animation, etc.)
	int myCount = 1000, pcCount = 1000, goalCount = 1000;

	// Depending on the angle of the torso and the position of the head it
	// decides if a player has fallen
	public boolean playerHasFallen(Player p) {
		float headNormalPosition = (GameInfo.worldHeight - Player
				.getPlayerHeadHeight());

		return ((Math.abs(p.head.getPosition().x - p.lLeg.getPosition().x) > 20) || (p.head
				.getPosition().y) > headNormalPosition);
	}

	// Logic of the game
	public void update(long time, boolean kick, float fx, float fy) {
		// If there has been a goal recently
		if (isGoal) {
			goalCount++;
			if (goalCount == 300) {
				// After some cycles showing a goal message, we restart the game
				isGoal = false;
				initialize();
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
		if (myPlayer1.torso.getPosition().y > (0.72 * GameInfo.worldHeight)) {
			// Detect if the player has fallen
			if (playerHasFallen(myPlayer1)) {
				// Try to recover position
				myPlayer1.torso.applyLinearImpulse(new Vec2(0, -10000
						* GameInfo.forceRate), myPlayer1.torso.getPosition());
			}
			myPlayer1.torso.applyLinearImpulse(new Vec2(
					fx * GameInfo.forceRate, fy * GameInfo.forceRate),
					myPlayer1.torso.getPosition());
			myPlayer1.lLeg.applyLinearImpulse(new Vec2(fx * 2
					* GameInfo.forceRate, fy * 2 * GameInfo.forceRate),
					myPlayer1.lLeg.getPosition());
			myPlayer1.rLeg.applyLinearImpulse(new Vec2(fx * 2
					* GameInfo.forceRate, fy * 2 * GameInfo.forceRate),
					myPlayer1.rLeg.getPosition());
		}
		if (myPlayer2.torso.getPosition().y > (0.72 * GameInfo.worldHeight)) {
			// Detect if the player has fallen
			if (playerHasFallen(myPlayer2)) {
				// Try to recover position
				myPlayer2.torso.applyLinearImpulse(new Vec2(
						0 * GameInfo.forceRate, -10000 * GameInfo.forceRate),
						myPlayer2.torso.getPosition());
			}
			myPlayer2.torso.applyLinearImpulse(new Vec2(
					fx * GameInfo.forceRate, fy * GameInfo.forceRate),
					myPlayer2.torso.getPosition());
			myPlayer2.lLeg.applyLinearImpulse(new Vec2(fx * 2
					* GameInfo.forceRate, fy * 2 * GameInfo.forceRate),
					myPlayer2.lLeg.getPosition());
			myPlayer2.rLeg.applyLinearImpulse(new Vec2(fx * 2
					* GameInfo.forceRate, fy * 2 * GameInfo.forceRate),
					myPlayer2.rLeg.getPosition());
		}

		// If we have a trigger for a kick, we start the kick counter, and we
		// perform the first part of the kick
		float rateOfKick = 0.5f; // Strength of the kick
		if (kick) {
			myPlayer1.torso.applyLinearImpulse(new Vec2(-3000 * rateOfKick
					* GameInfo.forceRate, -100 * rateOfKick
					* GameInfo.forceRate), myPlayer1.torso.getPosition());
			myPlayer1.lLeg.applyLinearImpulse(new Vec2(-5000 * rateOfKick
					* GameInfo.forceRate, -1000 * rateOfKick
					* GameInfo.forceRate), myPlayer1.lLeg.getPosition());
			myPlayer1.rLeg.applyLinearImpulse(new Vec2(20000 * rateOfKick
					* GameInfo.forceRate, -1000 * rateOfKick
					* GameInfo.forceRate), myPlayer1.rLeg.getPosition());
			myPlayer2.torso.applyLinearImpulse(new Vec2(-3000 * rateOfKick
					* GameInfo.forceRate, -100 * rateOfKick
					* GameInfo.forceRate), myPlayer2.torso.getPosition());
			myPlayer2.lLeg.applyLinearImpulse(new Vec2(-5000 * rateOfKick
					* GameInfo.forceRate, -1000 * rateOfKick
					* GameInfo.forceRate), myPlayer2.lLeg.getPosition());
			myPlayer2.rLeg.applyLinearImpulse(new Vec2(20000 * rateOfKick
					* GameInfo.forceRate, -1000 * rateOfKick
					* GameInfo.forceRate), myPlayer2.rLeg.getPosition());
			myCount = 0;
		}
		// Some cycles after the beginning of the kick, we perform the second
		// part
		if (myCount == 10) {
			myPlayer1.torso.applyLinearImpulse(new Vec2(4500 * rateOfKick
					* GameInfo.forceRate, 0), myPlayer1.torso.getPosition());
			myPlayer1.lLeg.applyLinearImpulse(new Vec2(8000 * rateOfKick
					* GameInfo.forceRate, 0), myPlayer1.lLeg.getPosition());
			myPlayer1.rLeg.applyLinearImpulse(new Vec2(-30000 * rateOfKick
					* GameInfo.forceRate, 10000 * rateOfKick
					* GameInfo.forceRate), myPlayer1.rLeg.getPosition());
			myPlayer2.torso.applyLinearImpulse(new Vec2(4500 * rateOfKick
					* GameInfo.forceRate, 0), myPlayer2.torso.getPosition());
			myPlayer2.lLeg.applyLinearImpulse(new Vec2(8000 * rateOfKick
					* GameInfo.forceRate, 0), myPlayer2.lLeg.getPosition());
			myPlayer2.rLeg.applyLinearImpulse(new Vec2(-30000 * rateOfKick
					* GameInfo.forceRate, 10000 * rateOfKick
					* GameInfo.forceRate), myPlayer2.rLeg.getPosition());
		}
		// If we still have not finished the kick we increase the counter
		if (myCount < 20)
			myCount++;

		// App team
		// We randomly decide the actions done by the players controlled by the
		// app, considering some intelligent decisions
		double probKick = 0.05, probRight = 0.1, probLeft = 0.1, probJump = 0.15;
		boolean kickPc = false;
		float fxPc = 0;
		float fyPc = 0;
		Random r = new Random();
		double d = r.nextFloat();
		// If the ball is on the left of one of the players, we will try to
		// score
		if (ball.body.getPosition().x < pcPlayer1.torso.getPosition().x
				|| ball.body.getPosition().x < pcPlayer2.torso.getPosition().x) {
			// If the ball is accessible to one of the players, we increase the
			// probability to go left and kick in order to score
			if (ball.body.getPosition().y > pcPlayer1.head.getPosition().y
					|| ball.body.getPosition().y > pcPlayer2.head.getPosition().y) {
				probKick = 0.1;
				probRight = 0.0;
				probLeft = 0.5;
				probJump = 0.0;
			}
			// If not accessible to the players, we increase the probability to
			// wait
			else {
				probKick = 0.1;
				probRight = 0.08;
				probLeft = 0.1;
				probJump = 0.08;
			}
			// Otherwise, if the ball is on the right of both players, we will
			// try to save the goal
		} else if (ball.body.getPosition().x > pcPlayer1.torso.getPosition().x
				&& ball.body.getPosition().x > pcPlayer2.torso.getPosition().x) {
			// If the ball is accessible to one of the players, we increase the
			// probability to wait because we do not want to score in our own goal
			if (ball.body.getPosition().y > pcPlayer1.head.getPosition().y
					|| ball.body.getPosition().y > pcPlayer2.head.getPosition().y) {
				probKick = 0.05;
				probRight = 0.0;
				probLeft = 0.05;
				probJump = 0.05;
			}
			// If not accessible to the players, we increase the probability to
			// go right in order to be between the ball and our goal
			else {
				probKick = 0.01;
				probRight = 0.5;
				probLeft = 0.0;
				probJump = 0.0;
			}

		}

		// In any case, if the ball is over one of the players, we increase the
		// probability
		// to jump
		if ((Math.abs(ball.body.getPosition().x
				- pcPlayer1.torso.getPosition().x) < 10 && ball.body
				.getPosition().y < pcPlayer1.torso.getPosition().y)
				|| (Math.abs(ball.body.getPosition().x
						- pcPlayer2.torso.getPosition().x) < 10 && ball.body
						.getPosition().y < pcPlayer2.torso.getPosition().y)) {
			probKick = 0.05;
			probRight = 0.1;
			probLeft = 0.1;
			probJump = 0.4;
		}

		if (d < probKick)
			kickPc = true;
		else if (d < probKick + probRight)
			fxPc = 200;
		else if (d < probKick + probRight + probLeft)
			fxPc = -200;
		else if (d < probKick + probRight + probLeft + probJump)
			fyPc = -200;
		else {
			fx = 0;
			fy = 0;
		}

		// If the players are touching the floor, we apply the forces according
		// to the movements decided before
		if (pcPlayer1.torso.getPosition().y > (0.72 * GameInfo.worldHeight)) {
			// Detect if the player has fallen
			if (playerHasFallen(pcPlayer1)) {
				// Try to recover position
				pcPlayer1.torso.applyLinearImpulse(new Vec2(0, -10000),
						pcPlayer1.torso.getPosition());
			}
			pcPlayer1.torso.applyLinearImpulse(new Vec2(fxPc, fyPc),
					pcPlayer1.torso.getPosition());
			pcPlayer1.lLeg.applyLinearImpulse(new Vec2(fxPc * 2, fyPc * 2),
					pcPlayer1.lLeg.getPosition());
			pcPlayer1.rLeg.applyLinearImpulse(new Vec2(fxPc * 2, fyPc * 2),
					pcPlayer1.rLeg.getPosition());
		}
		if (pcPlayer2.torso.getPosition().y > (0.72 * GameInfo.worldHeight)) {
			// Detect if the player has fallen
			if (playerHasFallen(pcPlayer2)) {
				// Try to recover position
				pcPlayer2.torso.applyLinearImpulse(new Vec2(0, -10000),
						pcPlayer2.torso.getPosition());
			}
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
			pcPlayer1.torso.applyLinearImpulse(new Vec2(3000 * rateOfKick, -100
					* rateOfKick), pcPlayer1.torso.getPosition());
			pcPlayer1.lLeg.applyLinearImpulse(new Vec2(5000 * rateOfKick, -1000
					* rateOfKick), pcPlayer1.lLeg.getPosition());
			pcPlayer1.rLeg.applyLinearImpulse(new Vec2(-20000 * rateOfKick,
					-1000 * rateOfKick), pcPlayer1.rLeg.getPosition());
			pcPlayer2.torso.applyLinearImpulse(new Vec2(3000 * rateOfKick, -100
					* rateOfKick), pcPlayer2.torso.getPosition());
			pcPlayer2.lLeg.applyLinearImpulse(new Vec2(5000 * rateOfKick, -1000
					* rateOfKick), pcPlayer2.lLeg.getPosition());
			pcPlayer2.rLeg.applyLinearImpulse(new Vec2(-20000 * rateOfKick,
					-1000 * rateOfKick), pcPlayer2.rLeg.getPosition());
		}
		// Some cycles after the beginning of the kick, we perform the second
		// part
		if (pcCount == 10) {
			pcPlayer1.torso.applyLinearImpulse(new Vec2(-4500 * rateOfKick, 0),
					pcPlayer1.torso.getPosition());
			pcPlayer1.lLeg.applyLinearImpulse(new Vec2(-8000 * rateOfKick, 0),
					pcPlayer1.lLeg.getPosition());
			pcPlayer1.rLeg.applyLinearImpulse(new Vec2(30000 * rateOfKick,
					10000 * rateOfKick), pcPlayer1.rLeg.getPosition());
			pcPlayer2.torso.applyLinearImpulse(new Vec2(-4500 * rateOfKick, 0),
					pcPlayer2.torso.getPosition());
			pcPlayer2.lLeg.applyLinearImpulse(new Vec2(-8000 * rateOfKick, 0),
					pcPlayer2.lLeg.getPosition());
			pcPlayer2.rLeg.applyLinearImpulse(new Vec2(+30000 * rateOfKick,
					10000 * rateOfKick), pcPlayer2.rLeg.getPosition());
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
		paint.setTextAlign(Align.CENTER);
		paint.setTextSize(40);
		canvas.drawText("Team1  " + GameInfo.myGoals + " - " + GameInfo.pcGoals
				+ "  Team2 ", (GameInfo.worldWidth / 2) * GameInfo.worldScale,
				15 * GameInfo.worldScale, paint);

		// When there is a goal, we show it in the middle of the screen
		if (isGoal) {
			paint.setTextSize(150);
			canvas.drawText("GOAL!", (GameInfo.worldWidth / 2)
					* GameInfo.worldScale, 45 * GameInfo.worldScale, paint);

		}

		// We draw each one of the elements
		walls.draw(canvas, paint);
		ball.draw(canvas, paint);
		myPlayer1.draw(canvas, paint);
		myPlayer2.draw(canvas, paint);
		pcPlayer1.draw(canvas, paint);
		pcPlayer2.draw(canvas, paint);
		lGoal.draw(canvas, paint);
		rGoal.draw(canvas, paint);
	}

}