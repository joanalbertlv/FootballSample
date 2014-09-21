package com.joanalbert.footballsample;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

import android.app.Activity;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainActivity extends Activity implements ContactListener {

	private SurfaceView surface;
	private SurfaceHolder surfaceHolder;
	private Canvas c = null;
	private Field field;
	private Handler handler;
	private long time;
	boolean kick = false;
	float fx = 0, fy = 0;
	float x1=0, x2=0, y1=0, y2=0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		surface = new SurfaceView(this);
		surfaceHolder = surface.getHolder();
		this.setContentView(surface);

		field = new Field();
		field.create();

		handler = new Handler();
		handler.post(update);

		this.time = System.currentTimeMillis();
	}

	@Override
	protected void onPause() {
		super.onPause();
		handler.removeCallbacks(update);
	}

	private Runnable update = new Runnable() {
		public void run() {
			long now = System.currentTimeMillis();
			
			field.update(now - time, kick, fx, fy);
			time = now;
			draw();
			handler.postDelayed(update, (long) field.timeStep * 1000);
			kick = false;
			fx = 0;
			fy = 0;
		}
	};

	private void draw() {
		try {
			c = surfaceHolder.lockCanvas();
			if (c == null)
				return;
			synchronized (surfaceHolder) {
				draw(c);
			}
		} catch (Exception e) {
			Log.e("draw()", "Rendering Exception", e);
		} finally {
			if (c != null) {
				surfaceHolder.unlockCanvasAndPost(c);
			}
		}
	}

	private void draw(Canvas canvas) {
		field.draw(canvas);
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			x1 = event.getX();
			y1 = event.getY();
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {

		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			x2 = event.getX();
			y2 = event.getY();
			float dx = x2 - x1;
			float dy = y2 - y1;
			if (Math.abs(dx) < 100 && Math.abs(dy) < 100) {
				kick = true;
			} else {
				if (dy < 0)
					fy = dy * 4;
				else
					fy = 0;
				fx = dx * 4;
			}
		}
		return true;

	}

	@Override
	public void beginContact(Contact arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void endContact(Contact arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

}