package com.joanalbert.footballsample.elements;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.joanalbert.footballsample.GameInfo;

//This class represents the limits of the field
public class Walls {

	private static final int backColor1 = 0xFF00DDFF;
	private static final int backColor2 = 0xFF00FF88;
	private static final float wallThickness = 2;
	float xIni, yIni, xEnd, yEnd;

	public Walls(float xIni, float yIni, float xEnd, float yEnd, World world) {
		this.xIni = xIni;
		this.yIni = yIni;
		this.xEnd = xEnd;
		this.yEnd = yEnd;
		BodyDef bBodyDef;
		Body bBody;
		PolygonShape groundBox;

		// Definition of the bodies and their properties
		// Down
		bBodyDef = new BodyDef();
		bBodyDef.type = BodyType.STATIC;
		bBodyDef.position.set(new Vec2(xIni, yEnd));
		bBody = world.createBody(bBodyDef);

		groundBox = new PolygonShape();
		groundBox.setAsBox(xEnd - xIni, wallThickness);
		bBody.createFixture(groundBox, 20.0f);

		// Left
		bBodyDef = new BodyDef();
		bBodyDef.type = BodyType.STATIC;
		bBodyDef.position.set(new Vec2(xIni, yIni));
		bBody = world.createBody(bBodyDef);

		groundBox = new PolygonShape();
		groundBox.setAsBox(wallThickness, yEnd - yIni);
		bBody.createFixture(groundBox, 20.0f);

		// Top
		bBodyDef = new BodyDef();
		bBodyDef.type = BodyType.STATIC;
		bBodyDef.position.set(new Vec2(xIni, yIni));
		bBody = world.createBody(bBodyDef);

		groundBox = new PolygonShape();
		groundBox.setAsBox(xEnd - xIni, wallThickness);
		bBody.createFixture(groundBox, 20.0f);

		// Right
		bBodyDef = new BodyDef();
		bBodyDef.type = BodyType.STATIC;
		bBodyDef.position.set(new Vec2(xEnd, yIni));
		bBody = world.createBody(bBodyDef);

		groundBox = new PolygonShape();
		groundBox.setAsBox(wallThickness, yEnd - yIni);
		bBody.createFixture(groundBox, 20.0f);
	}

	// Method to draw the limits of the field in the specified canvas
	public void draw(Canvas canvas, Paint paint) {
		paint.setColor(backColor1);
		paint.setStyle(Paint.Style.FILL);
		// We do not draw the bodies but a rectangle
		canvas.drawRect((xIni + 1) * GameInfo.worldScale, (yIni + 1)
				* GameInfo.worldScale, (xEnd - 1) * GameInfo.worldScale,
				(yEnd - 1) * GameInfo.worldScale, paint);
		paint.setColor(backColor2);
		canvas.drawRect((xIni + 1) * GameInfo.worldScale, (yEnd - 1)
				* GameInfo.worldScale, (xEnd - 1) * GameInfo.worldScale,
				(yEnd +10) * GameInfo.worldScale, paint);
	}

}
