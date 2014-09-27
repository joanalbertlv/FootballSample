package com.joanalbert.footballsample.elements;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.joanalbert.footballsample.GameInfo;

//This class represents the ball
public class Ball {

	private static final int ballColor = Color.WHITE;
	private static float ballRadius;

	public Body body;

	public Ball(World world) {
		float x = GameInfo.worldWidth/2;
		float y = GameInfo.worldHeight/2;
		ballRadius = GameInfo.worldWidth / 58;

		// Definition of the body and its properties
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position.set(x, y);
		bodyDef.allowSleep = true;
		body = world.createBody(bodyDef);

		CircleShape circle = new CircleShape();
		circle.m_radius = ballRadius;

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = 0.1f*GameInfo.densityRate;
		fixtureDef.friction = 0.7f;
		fixtureDef.restitution = 0.8f;

		body.createFixture(fixtureDef);
	}
	
	public static float getRadius(){
		return ballRadius;
	}

	// Method to draw a ball in the specified canvas
	public void draw(Canvas canvas, Paint paint) {
		paint.setColor(ballColor);
		paint.setStyle(Paint.Style.FILL);
		canvas.drawCircle(body.getPosition().x * GameInfo.worldScale,
				body.getPosition().y * GameInfo.worldScale, ballRadius
						* GameInfo.worldScale, paint);
	}

}
