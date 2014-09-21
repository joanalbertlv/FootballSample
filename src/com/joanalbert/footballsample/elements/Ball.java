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

public class Ball extends DrawableElement{
	
	private static final int ballColor = Color.WHITE;
	private static final float ballRadius = 3;
	
	public Body body;
	
	public Ball(float x, float y, World world){
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position.set(x, y);
		bodyDef.allowSleep = true;
		body = world.createBody(bodyDef);

		CircleShape circle = new CircleShape();
		circle.m_radius = ballRadius;

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = 0.1f;
		fixtureDef.friction = 0.7f;
		fixtureDef.restitution = 0.8f;

		body.createFixture(fixtureDef);		
	}	
	
	public void draw(Canvas canvas, Paint paint) {
		paint.setColor(ballColor);
		paint.setStyle(Paint.Style.FILL);
		canvas.drawCircle(body.getPosition().x * WORLD_SCALE, body.getPosition().y * WORLD_SCALE,
				ballRadius * WORLD_SCALE, paint);
	}	
	
}
