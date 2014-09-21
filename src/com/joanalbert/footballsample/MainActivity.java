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

// This is the main activity which also detects screen contacts
public class MainActivity extends Activity implements ContactListener {

	private SurfaceView surface;
	private SurfaceHolder surfaceHolder;
	private Canvas c = null;
	private Field field;
	private Handler handler;
	private long time;
	boolean kick = false;
	float fx = 0, fy = 0;
	float x1 = 0, x2 = 0, y1 = 0, y2 = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// We place a SurfaceView in the activity in order to draw on it
		surface = new SurfaceView(this);
		surfaceHolder = surface.getHolder();
		this.setContentView(surface);

		// Field of the game created
		field = new Field();
		field.create();

		// Handler to manage the thread to update the game
		handler = new Handler();
		handler.post(update);

		// Current time
		this.time = System.currentTimeMillis();
	}

	@Override
	protected void onPause() {
		super.onPause();
		handler.removeCallbacks(update);
	}

	// Thread that updates the game
	private Runnable update = new Runnable() {
		public void run() {
			// Current time
			long now = System.currentTimeMillis();

			// The field is updated
			field.update(now - time, kick, fx, fy);
			time = now;
			// Draw the current state of the game
			draw();

			// The runnable update will be executed after the needed time
			handler.postDelayed(update, (long) field.timeStep * 1000);

			// There is no kick, jump, etc. requested
			kick = false;
			fx = 0;
			fy = 0;
		}
	};

	private void draw() {
		// The game is drawn in a synchronized way
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
			// If the user touches the screen we store the position
			x1 = event.getX();
			y1 = event.getY();
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {

		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			// When the user releases the finger we store the new position
			x2 = event.getX();
			y2 = event.getY();
			float dx = x2 - x1;
			float dy = y2 - y1;
			// If the difference between old and new position is very low, then
			// it is a kick action
			if (Math.abs(dx) < 100 && Math.abs(dy) < 100) {
				kick = true;
			} else {
				// If the user has dragged the finger, then we store two forces
				// x and y that will be applied to the players. Positive Y-force
				// not allowed.
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