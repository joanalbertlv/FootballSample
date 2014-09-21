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

public class Goal {

	private static final int goalColor = Color.WHITE;
	public static final float goalHeight = 30;
	public static final float goalWidth = 12;
	private static final float postThickness = 1;
	private boolean isLeft = true;
	public Body bGoalEnd, bGoalTop;

	public Goal(float x, float y, boolean isLeft, World world) {
		this.isLeft = isLeft;
		PolygonShape groundBox;
		BodyDef bBodyDef = null;

		// Goal end
		bBodyDef = new BodyDef();
		bBodyDef.type = BodyType.STATIC;
		if (isLeft)
			bBodyDef.position.set(new Vec2(x - goalWidth / 2, y));
		else
			bBodyDef.position.set(new Vec2(x + goalWidth / 2, y));
		bGoalEnd = world.createBody(bBodyDef);

		groundBox = new PolygonShape();
		groundBox.setAsBox(postThickness / 2, goalHeight / 2);
		bGoalEnd.createFixture(groundBox, 20.0f);

		// Goal top
		bBodyDef = new BodyDef();
		bBodyDef.type = BodyType.STATIC;
		bBodyDef.position.set(new Vec2(x, y - goalHeight / 2));
		bGoalTop = world.createBody(bBodyDef);

		groundBox = new PolygonShape();
		groundBox.setAsBox(goalWidth / 2, postThickness / 2);
		bGoalTop.createFixture(groundBox, 20.0f);

	}

	public void drawNet(Canvas canvas, Paint paint) {
		float leftX;
		if (isLeft)
			leftX = bGoalEnd.getPosition().x;
		else
			leftX = bGoalEnd.getPosition().x - goalWidth;

		float x = (leftX + 2 - postThickness / 2);
		float yIni = (bGoalEnd.getPosition().y - goalHeight / 2);
		float yEnd = (bGoalEnd.getPosition().y + goalHeight / 2);
		while (x < (bGoalTop.getPosition().x + goalWidth / 2)) {
			canvas.drawLine(x * GameInfo.worldScale, yIni * GameInfo.worldScale, x
					* GameInfo.worldScale, yEnd * GameInfo.worldScale, paint);
			x += 2;
		}
		if (isLeft)
			canvas.drawLine((bGoalTop.getPosition().x + goalWidth / 2) * GameInfo.worldScale,
					yIni * GameInfo.worldScale, (bGoalTop.getPosition().x + goalWidth / 2)
							* GameInfo.worldScale, yEnd * GameInfo.worldScale, paint);
		else
			canvas.drawLine((bGoalTop.getPosition().x - goalWidth / 2) * GameInfo.worldScale,
					yIni * GameInfo.worldScale, (bGoalTop.getPosition().x - goalWidth / 2)
							* GameInfo.worldScale, yEnd * GameInfo.worldScale, paint);
		

		float y = (bGoalEnd.getPosition().y - goalHeight / 2);
		float xIni = leftX;
		float xEnd = (bGoalTop.getPosition().x + goalWidth / 2);
		while (y < (bGoalEnd.getPosition().y + goalHeight / 2)) {
			canvas.drawLine(xIni * GameInfo.worldScale, y * GameInfo.worldScale, xEnd
					* GameInfo.worldScale, y * GameInfo.worldScale, paint);
			y += 2;
		}
		y = (bGoalEnd.getPosition().y + goalHeight / 2);

	}

	public void draw(Canvas canvas, Paint paint) {
		paint.setColor(goalColor);
		paint.setStyle(Paint.Style.FILL);

		// posts
		canvas.drawRect((bGoalEnd.getPosition().x - postThickness / 2)
				* GameInfo.worldScale, (bGoalEnd.getPosition().y - goalHeight / 2)
				* GameInfo.worldScale, (bGoalEnd.getPosition().x + postThickness / 2)
				* GameInfo.worldScale, (bGoalEnd.getPosition().y + goalHeight / 2)
				* GameInfo.worldScale, paint);
		canvas.drawRect((bGoalTop.getPosition().x - goalWidth / 2)
				* GameInfo.worldScale, (bGoalTop.getPosition().y - postThickness / 2)
				* GameInfo.worldScale, (bGoalTop.getPosition().x + goalWidth / 2)
				* GameInfo.worldScale, (bGoalTop.getPosition().y + postThickness / 2)
				* GameInfo.worldScale, paint);
		drawNet(canvas, paint);

	}

}
