package com.joanalbert.footballsample.elements;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.DistanceJointDef;
import org.jbox2d.dynamics.joints.JointDef;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

public class Player extends DrawableElement {

	private static final int shirtColor = Color.WHITE;
	private static final int shortsColor = Color.RED;

	float torsoWidth = 7.0f;
	float torsoHeight = 13.0f;
	float legWidth = 3f;
	float legHeight = 10.0f;
	float armWidth = 2f;
	float armHeight = 7.0f;
	float headRadius = 3.0f;
	
	public Body torso, lLeg, rLeg, lArm, rArm, head;
	public RevoluteJointDef lLRjd, rLRjd, lARjd, rARjd, hRjd;
	
	public Player(float x, float y, World world) {
		// TORSO
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position.set(new Vec2(x, y - 30));
		bodyDef.angularDamping = 10;
		torso = world.createBody(bodyDef);

		PolygonShape polygon = new PolygonShape();
		polygon.setAsBox(torsoWidth / 2, torsoHeight / 2);

		FixtureDef fDef = new FixtureDef();
		fDef.density = 0.5f;
		fDef.shape = polygon;
		torso.createFixture(fDef);

		// LEFT LEG
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position.set(new Vec2(x - (torsoWidth / 2), y - torsoHeight));
		bodyDef.angularDamping=10;
		lLeg = world.createBody(bodyDef);

		polygon = new PolygonShape();
		polygon.setAsBox(legWidth / 2, legHeight / 2);
		fDef = new FixtureDef();
		fDef.density = 15;
		fDef.shape = polygon;
		lLeg.createFixture(fDef);

		// RIGHT LEG
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position.set(new Vec2(x, y - torsoHeight));
		bodyDef.angularDamping = 10;
		rLeg = world.createBody(bodyDef);

		polygon = new PolygonShape();
		polygon.setAsBox(legWidth / 2, legHeight / 2);
		fDef = new FixtureDef();
		fDef.density = 15;
		fDef.shape = polygon;
		rLeg.createFixture(fDef);

		// LEFT ARM
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position.set(new Vec2(x - (torsoWidth / 2), y - torsoHeight));
		lArm = world.createBody(bodyDef);

		polygon = new PolygonShape();
		polygon.setAsBox(armWidth / 2, armHeight / 2);
		fDef = new FixtureDef();
		fDef.density = 0.5f;
		fDef.shape = polygon;
		lArm.createFixture(fDef);

		// RIGHT ARM
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position.set(new Vec2(x, y - torsoHeight));
		rArm = world.createBody(bodyDef);

		polygon = new PolygonShape();
		polygon.setAsBox(armWidth / 2, armHeight / 2);
		fDef = new FixtureDef();
		fDef.density = 0.5f;
		fDef.shape = polygon;
		rArm.createFixture(fDef);
				
		// HEAD
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position.set(new Vec2(x - (torsoWidth / 2), y - torsoHeight));
		head = world.createBody(bodyDef);

		CircleShape circle = new CircleShape();
		circle.m_radius = headRadius;

		fDef = new FixtureDef();
		fDef.density = 0.5f;
		fDef.shape = circle;
		head.createFixture(fDef);
				
		//Torso to legs
		lLRjd = new RevoluteJointDef();
		lLRjd.bodyA = torso;
		lLRjd.bodyB = lLeg;
		lLRjd.collideConnected = false;
		lLRjd.localAnchorA = new Vec2(-(torsoWidth / 2)+(legWidth/2), torsoHeight / 2);
		lLRjd.localAnchorB = new Vec2(0, -(legHeight / 2));
		lLRjd.enableLimit = true;
		lLRjd.lowerAngle = new Double(Math.toRadians(-45)).floatValue();
		lLRjd.upperAngle = new Double(Math.toRadians(45)).floatValue();
		world.createJoint(lLRjd);

		rLRjd = new RevoluteJointDef();
		rLRjd.bodyA = torso;
		rLRjd.bodyB = rLeg;
		rLRjd.collideConnected = false;
		rLRjd.localAnchorA = new Vec2((torsoWidth / 2)-(legWidth/2), torsoHeight / 2);
		rLRjd.localAnchorB = new Vec2(0, -(legHeight / 2));
		rLRjd.enableLimit = true;
		rLRjd.lowerAngle = new Double(Math.toRadians(-90)).floatValue();
		rLRjd.upperAngle = new Double(Math.toRadians(90)).floatValue();
		world.createJoint(rLRjd);
		
		//Torso to arms
		lARjd = new RevoluteJointDef();
		lARjd.bodyA = torso;
		lARjd.bodyB = lArm;
		lARjd.collideConnected = false;
		lARjd.localAnchorA = new Vec2(-torsoWidth / 2, -torsoHeight / 2);
		lARjd.localAnchorB = new Vec2(0, -(armHeight / 2));
		lARjd.enableLimit = true;
		lARjd.lowerAngle = new Double(Math.toRadians(-90)).floatValue();
		lARjd.upperAngle = new Double(Math.toRadians(90)).floatValue();
		world.createJoint(lARjd);
		
		rARjd = new RevoluteJointDef();
		rARjd.bodyA = torso;
		rARjd.bodyB = rArm;
		rARjd.collideConnected = false;
		rARjd.localAnchorA = new Vec2(torsoWidth / 2, -torsoHeight / 2);
		rARjd.localAnchorB = new Vec2(0, -(armHeight / 2));
		rARjd.enableLimit = true;
		rARjd.lowerAngle = new Double(Math.toRadians(-90)).floatValue();
		rARjd.upperAngle = new Double(Math.toRadians(90)).floatValue();
		world.createJoint(rARjd);
		
		//Torso to head
		hRjd = new RevoluteJointDef();
		hRjd.bodyA = torso;
		hRjd.bodyB = head;
		hRjd.collideConnected = true;
		hRjd.localAnchorA = new Vec2(0, -torsoHeight / 2);
		hRjd.localAnchorB = new Vec2(0, headRadius);
		hRjd.enableLimit = true;
		hRjd.lowerAngle = new Double(Math.toRadians(-90)).floatValue();
		hRjd.upperAngle = new Double(Math.toRadians(90)).floatValue();
		world.createJoint(hRjd);		

	}


	public void draw(Canvas canvas, Paint paint) {
		paint.setColor(shirtColor);
		paint.setStyle(Paint.Style.STROKE);

		canvas.drawCircle(head.getPosition().x * WORLD_SCALE, head.getPosition().y * WORLD_SCALE,
				headRadius * WORLD_SCALE, paint);
		
		drawRectangle(canvas, paint, torso, torsoWidth, torsoHeight);
		drawRectangle(canvas, paint, lArm, armWidth, armHeight);
		drawRectangle(canvas, paint, rArm, armWidth, armHeight);
		
		paint.setColor(shortsColor);
		drawRectangle(canvas, paint, lLeg, legWidth, legHeight);
		drawRectangle(canvas, paint, rLeg, legWidth, legHeight);
	}
	
	public void drawRectangle(Canvas canvas, Paint paint, Body b, float width,
			float height) {
		float xInit = (b.getPosition().x - (width / 2)) * WORLD_SCALE;
		float yInit = (b.getPosition().y - (height / 2)) * WORLD_SCALE;
		float xEnd = (b.getPosition().x + (width / 2)) * WORLD_SCALE;
		float yEnd = (b.getPosition().y + (height / 2)) * WORLD_SCALE;

		float angle = b.getAngle();

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
