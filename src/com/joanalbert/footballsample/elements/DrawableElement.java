package com.joanalbert.footballsample.elements;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class DrawableElement {
	
	protected static final float WORLD_SCALE = 10;
	protected Body body; 
	
	public Body getBody() {
		return body;
	}
	
	public final Vec2 getPosition() {
		return body.getPosition();
	}
	
	public abstract void draw(Canvas canvas, Paint paint);
	
}
