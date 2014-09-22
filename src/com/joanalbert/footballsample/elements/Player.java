package com.joanalbert.footballsample.elements;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import com.joanalbert.footballsample.GameInfo;

//This class represents a football player
public class Player {

	private static final int faceColor = Color.MAGENTA;
	// Colors of team 1
	private static final int shirtColor = Color.BLUE;
	private static final int shirt2Color = Color.RED;
	private static final int shortsColor = Color.BLUE;
	// Colors of team 2
	private static final int shirtColorPc = Color.WHITE;
	private static final int shirt2ColorPc = Color.BLUE;
	private static final int shortsColorPc = Color.BLUE;

	float torsoWidth = 7.0f;
	float torsoHeight = 13.0f;
	float legWidth = 3f;
	float legHeight = 10.0f;
	float armWidth = 2f;
	float armHeight = 7.0f;
	float headRadius = 3.0f;
	boolean isMyPlayer = true; // To distinguish between players of each team

	// Bodies of the torso, left and right legs/arms and head
	public Body torso, lLeg, rLeg, lArm, rArm, head;
	// Joints between torso and legs/arms/head
	public RevoluteJointDef lLRjd, rLRjd, lARjd, rARjd, hRjd;

	public Player(float x, float y, boolean isMyPlayer, World world) {
		this.isMyPlayer = isMyPlayer;

		// Definition of the bodies and their properties
		// Torso
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

		// Left leg
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		if (isMyPlayer)
			bodyDef.position
					.set(new Vec2(x - (torsoWidth / 2), y - torsoHeight));
		else
			bodyDef.position.set(new Vec2(x, y - torsoHeight));
		bodyDef.angularDamping = 10;
		lLeg = world.createBody(bodyDef);

		polygon = new PolygonShape();
		polygon.setAsBox(legWidth / 2, legHeight / 2);
		fDef = new FixtureDef();
		fDef.density = 15;
		fDef.shape = polygon;
		lLeg.createFixture(fDef);

		// Right leg
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		if (isMyPlayer)
			bodyDef.position.set(new Vec2(x, y - torsoHeight));
		else
			bodyDef.position
					.set(new Vec2(x - (torsoWidth / 2), y - torsoHeight));
		bodyDef.angularDamping = 10;
		rLeg = world.createBody(bodyDef);

		polygon = new PolygonShape();
		polygon.setAsBox(legWidth / 2, legHeight / 2);
		fDef = new FixtureDef();
		fDef.density = 15;
		fDef.shape = polygon;
		rLeg.createFixture(fDef);

		// Left arm
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

		// Right arm
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

		// Head
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

		// Torso to legs
		lLRjd = new RevoluteJointDef();
		lLRjd.bodyA = torso;
		lLRjd.bodyB = lLeg;
		lLRjd.collideConnected = false;
		if (isMyPlayer)
			lLRjd.localAnchorA = new Vec2(-(torsoWidth / 2) + (legWidth / 2),
					torsoHeight / 2);
		else
			lLRjd.localAnchorA = new Vec2((torsoWidth / 2) - (legWidth / 2),
					torsoHeight / 2);
		lLRjd.localAnchorB = new Vec2(0, -(legHeight / 2));
		lLRjd.enableLimit = true;
		lLRjd.lowerAngle = Double.valueOf(Math.toRadians(-45)).floatValue();
		lLRjd.upperAngle = Double.valueOf(Math.toRadians(45)).floatValue();
		world.createJoint(lLRjd);

		rLRjd = new RevoluteJointDef();
		rLRjd.bodyA = torso;
		rLRjd.bodyB = rLeg;
		rLRjd.collideConnected = false;
		if (isMyPlayer)
			rLRjd.localAnchorA = new Vec2((torsoWidth / 2) - (legWidth / 2),
					torsoHeight / 2);
		else
			rLRjd.localAnchorA = new Vec2(-(torsoWidth / 2) + (legWidth / 2),
					torsoHeight / 2);
		rLRjd.localAnchorB = new Vec2(0, -(legHeight / 2));
		rLRjd.enableLimit = true;
		rLRjd.lowerAngle = Double.valueOf(Math.toRadians(-90)).floatValue();
		rLRjd.upperAngle = Double.valueOf(Math.toRadians(90)).floatValue();
		world.createJoint(rLRjd);

		// Torso to arms
		lARjd = new RevoluteJointDef();
		lARjd.bodyA = torso;
		lARjd.bodyB = lArm;
		lARjd.collideConnected = false;
		lARjd.localAnchorA = new Vec2(-torsoWidth / 2, -torsoHeight / 2);
		lARjd.localAnchorB = new Vec2(0, -(armHeight / 2));
		lARjd.enableLimit = true;
		lARjd.lowerAngle = Double.valueOf(Math.toRadians(-90)).floatValue();
		lARjd.upperAngle = Double.valueOf(Math.toRadians(90)).floatValue();
		world.createJoint(lARjd);

		rARjd = new RevoluteJointDef();
		rARjd.bodyA = torso;
		rARjd.bodyB = rArm;
		rARjd.collideConnected = false;
		rARjd.localAnchorA = new Vec2(torsoWidth / 2, -torsoHeight / 2);
		rARjd.localAnchorB = new Vec2(0, -(armHeight / 2));
		rARjd.enableLimit = true;
		rARjd.lowerAngle = Double.valueOf(Math.toRadians(-90)).floatValue();
		rARjd.upperAngle = Double.valueOf(Math.toRadians(90)).floatValue();
		world.createJoint(rARjd);

		// Torso to head
		hRjd = new RevoluteJointDef();
		hRjd.bodyA = torso;
		hRjd.bodyB = head;
		hRjd.collideConnected = true;
		hRjd.localAnchorA = new Vec2(0, -torsoHeight / 2);
		hRjd.localAnchorB = new Vec2(0, headRadius);
		hRjd.enableLimit = true;
		hRjd.lowerAngle = Double.valueOf(Math.toRadians(-90)).floatValue();
		hRjd.upperAngle = Double.valueOf(Math.toRadians(90)).floatValue();
		world.createJoint(hRjd);

	}

	// Method to draw a player in the specified canvas
	public void draw(Canvas canvas, Paint paint) {

		paint.setColor(faceColor);
		paint.setStyle(Paint.Style.FILL);

		// Head
		canvas.drawCircle(head.getPosition().x * GameInfo.worldScale,
				head.getPosition().y * GameInfo.worldScale, headRadius
						* GameInfo.worldScale, paint);

		// Torso and arms
		if (isMyPlayer)
			paint.setColor(shirtColor);
		else
			paint.setColor(shirtColorPc);
		drawRectangle(canvas, paint, torso, torsoWidth, torsoHeight);
		drawRectangle(canvas, paint, lArm, armWidth, armHeight);
		drawRectangle(canvas, paint, rArm, armWidth, armHeight);

		// Legs
		if (isMyPlayer)
			paint.setColor(shortsColor);
		else
			paint.setColor(shortsColorPc);
		drawRectangle(canvas, paint, lLeg, legWidth, legHeight);
		drawRectangle(canvas, paint, rLeg, legWidth, legHeight);

		// Color 2 of the shirt (no body)
		if (isMyPlayer)
			paint.setColor(shirt2Color);
		else
			paint.setColor(shirt2ColorPc);
		drawRectangle(canvas, paint, torso, torsoWidth - 4, torsoHeight);
	}

	// Method to draw a filled rectangle in the specified canvas
		// It considers the angle of the rectangle
	public void drawRectangle(Canvas canvas, Paint paint, Body b, float width,
			float height) {
		float xInit = (b.getPosition().x - (width / 2)) * GameInfo.worldScale;
		float yInit = (b.getPosition().y - (height / 2)) * GameInfo.worldScale;
		float xEnd = (b.getPosition().x + (width / 2)) * GameInfo.worldScale;
		float yEnd = (b.getPosition().y + (height / 2)) * GameInfo.worldScale;

		float angle = b.getAngle();

		Vec2 center = new Vec2(xInit + ((xEnd - xInit) / 2), yInit
				+ ((yEnd - yInit) / 2));

		// Calculate the four points of the rectangle depending on the angle
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

		// Join the four points
		Path wallpath = new Path();
		wallpath.reset(); 
		wallpath.moveTo(upLeft.x, upLeft.y); 
		wallpath.lineTo(upRight.x, upRight.y);
		wallpath.lineTo(downRight.x, downRight.y);
		wallpath.lineTo(downLeft.x, downLeft.y);
		wallpath.lineTo(upLeft.x, upLeft.y); 
		canvas.drawPath(wallpath, paint);

	}

}
