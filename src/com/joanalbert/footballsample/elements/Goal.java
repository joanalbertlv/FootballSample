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
import android.util.Log;

import com.joanalbert.footballsample.GameInfo;

//This class represents a goal
public class Goal {

	private static final int goalColor = Color.WHITE;
	public static float goalHeight;
	public static float goalWidth;
	private static float postThickness;
	private boolean isLeft = true; // To distinguish between the two goals
	public Body bGoalEnd; // The end part of the goal
	public Body bGoalTop; // The top part of the goal

	public Goal(boolean isLeft, World world) {
		goalHeight = GameInfo.worldHeight/2.5f;
		goalWidth = GameInfo.worldWidth/13f;
		postThickness = GameInfo.worldWidth/174f;
	
		float x=0; float y=0;
		this.isLeft = isLeft;
		if (isLeft){
			x=3+(goalWidth/2)+postThickness;
			y=GameInfo.worldHeight - (goalHeight/2) - postThickness; 
		}else{
			x=GameInfo.worldWidth - (goalWidth/2) - postThickness-1;
			y=GameInfo.worldHeight - (goalHeight/2) - postThickness;
		}
		
		PolygonShape groundBox;
		BodyDef bBodyDef = null;
		// Definition of the bodies and their properties
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
	
	// Detects if the ball is just over the goal	
	public boolean isItOverTheGoal(Vec2 position){
		if (position.y < bGoalTop.getPosition().y && position.y > (bGoalTop.getPosition().y-Ball.getRadius()*2)){
			if (isLeft){
				return (position.x<(bGoalEnd.getPosition().x+goalWidth));
			}else{
				return (position.x>(bGoalEnd.getPosition().x-goalWidth));
			}
		}else return false;		
	}

	// Method to draw a goal in the specified canvas
	public void draw(Canvas canvas, Paint paint) {
		paint.setColor(goalColor);
		paint.setStyle(Paint.Style.FILL);

		// Draw posts
		canvas.drawRect((bGoalEnd.getPosition().x - postThickness / 2)
				* GameInfo.worldScale,
				(bGoalEnd.getPosition().y - goalHeight / 2)
						* GameInfo.worldScale,
				(bGoalEnd.getPosition().x + postThickness / 2)
						* GameInfo.worldScale,
				(bGoalEnd.getPosition().y + goalHeight / 2)
						* GameInfo.worldScale, paint);
		canvas.drawRect((bGoalTop.getPosition().x - goalWidth / 2)
				* GameInfo.worldScale,
				(bGoalTop.getPosition().y - postThickness / 2)
						* GameInfo.worldScale,
				(bGoalTop.getPosition().x + goalWidth / 2)
						* GameInfo.worldScale,
				(bGoalTop.getPosition().y + postThickness / 2)
						* GameInfo.worldScale, paint);
		// Draw net
		drawNet(canvas, paint);
	}
	
	// Method to draw the net of the goal. The net is not body.
	public void drawNet(Canvas canvas, Paint paint) {
		float leftX;
		if (isLeft)
			leftX = bGoalEnd.getPosition().x;
		else
			leftX = bGoalEnd.getPosition().x - goalWidth;

		// Vertical strings	
		float x = (leftX + 2 - postThickness / 2);
		float yIni = (bGoalEnd.getPosition().y - goalHeight / 2);
		float yEnd = (bGoalEnd.getPosition().y + goalHeight / 2);
		while (x < (bGoalTop.getPosition().x + goalWidth / 2)) {
			canvas.drawLine(x * GameInfo.worldScale,
					yIni * GameInfo.worldScale, x * GameInfo.worldScale, yEnd
							* GameInfo.worldScale, paint);
			x += 2;
		}
		if (isLeft)
			canvas.drawLine((bGoalTop.getPosition().x + goalWidth / 2)
					* GameInfo.worldScale, yIni * GameInfo.worldScale,
					(bGoalTop.getPosition().x + goalWidth / 2)
							* GameInfo.worldScale, yEnd * GameInfo.worldScale,
					paint);
		else
			canvas.drawLine((bGoalTop.getPosition().x - goalWidth / 2)
					* GameInfo.worldScale, yIni * GameInfo.worldScale,
					(bGoalTop.getPosition().x - goalWidth / 2)
							* GameInfo.worldScale, yEnd * GameInfo.worldScale,
					paint);

		// Horizontal strings
		float y = (bGoalEnd.getPosition().y - goalHeight / 2);
		float xIni = leftX;
		float xEnd = (bGoalTop.getPosition().x + goalWidth / 2);
		while (y < (bGoalEnd.getPosition().y + goalHeight / 2)) {
			canvas.drawLine(xIni * GameInfo.worldScale,
					y * GameInfo.worldScale, xEnd * GameInfo.worldScale, y
							* GameInfo.worldScale, paint);
			y += 2;
		}
		y = (bGoalEnd.getPosition().y + goalHeight / 2);

	}

}
