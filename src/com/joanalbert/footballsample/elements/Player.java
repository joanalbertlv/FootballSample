package com.joanalbert.footballsample.elements;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

public class Player extends DrawableElement {

	private static final int shirtColor = Color.WHITE;
	private static final int shortsColor = Color.RED;

	float torsoWidth = 8.0f;
	float torsoHeight = 16.0f;

	public Body torso;

	public Player(float x, float y, World world) {
		// TORSO
		BodyDef bodyDef = new BodyDef();
		bodyDef.fixedRotation = false;
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position.set(new Vec2(x, y - 30));
		torso = world.createBody(bodyDef);

		PolygonShape polygon = new PolygonShape();
		polygon.setAsBox(torsoWidth / 2, torsoHeight / 2);

		FixtureDef fDef = new FixtureDef();
		fDef.density = 3f;
		fDef.shape = polygon;
		torso.createFixture(fDef);

	}

	public void draw(Canvas canvas, Paint paint) {
		paint.setColor(shirtColor);
		paint.setStyle(Paint.Style.FILL);
		float xInit = (torso.getPosition().x - (torsoWidth / 2)) * WORLD_SCALE;
		float yInit = (torso.getPosition().y - (torsoHeight / 2)) * WORLD_SCALE;
		float xEnd = (torso.getPosition().x + (torsoWidth / 2)) * WORLD_SCALE;
		float yEnd = (torso.getPosition().y + (torsoHeight / 2)) * WORLD_SCALE;

		float angle = torso.getAngle();

		Vec2 center = new Vec2(xInit + ((xEnd - xInit) / 2), yInit
				+ ((yEnd - yInit) / 2));

		Point upLeft = new Point(
		// x-coordinate
				(int) Math.round(center.x
						+ ((xInit - center.x) * Math.cos(angle))
						- ((yInit - center.y) * Math.sin(angle))),
				// y-coordinate
				(int) Math.round(center.y
						+ ((xInit - center.x) * Math.sin(angle))
						+ ((yInit - center.y) * Math.cos(angle))));
		Point upRight = new Point(
		// x-coordinate
				(int) Math.round(center.x
						+ ((xEnd - center.x) * Math.cos(angle))
						- ((yInit - center.y) * Math.sin(angle))),
				// y-coordinate
				(int) Math.round(center.y
						+ ((xEnd - center.x) * Math.sin(angle))
						+ ((yInit - center.y) * Math.cos(angle))));

		Point downRight = new Point(
		// x-coordinate
				(int) Math.round(center.x
						+ ((xEnd - center.x) * Math.cos(angle))
						- ((yEnd - center.y) * Math.sin(angle))),
				// y-coordinate
				(int) Math.round(center.y
						+ ((xEnd - center.x) * Math.sin(angle))
						+ ((yEnd - center.y) * Math.cos(angle))));

		Point downLeft = new Point(
		// x-coordinate
				(int) Math.round(center.x
						+ ((xInit - center.x) * Math.cos(angle))
						- ((yEnd - center.y) * Math.sin(angle))),
				// y-coordinate
				(int) Math.round(center.y
						+ ((xInit - center.x) * Math.sin(angle))
						+ ((yEnd - center.y) * Math.cos(angle))));

		canvas.drawLine(upLeft.x, upLeft.y, upRight.x, upRight.y, paint);
		canvas.drawLine(upLeft.x, upLeft.y, downLeft.x, downLeft.y, paint);
		canvas.drawLine(downLeft.x, downLeft.y, downRight.x, downRight.y, paint);
		canvas.drawLine(upRight.x, upRight.y, downRight.x, downRight.y, paint);

	}

}
