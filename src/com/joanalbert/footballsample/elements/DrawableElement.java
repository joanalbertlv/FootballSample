package com.joanalbert.footballsample.elements;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class DrawableElement {
	
	public static final float WORLD_SCALE = 10;
		
	public abstract void draw(Canvas canvas, Paint paint);
	
}
