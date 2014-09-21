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
	private Field physicsWorld;
	private Handler handler;
	private long time;
	boolean kick=false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		surface = new SurfaceView(this);
		surfaceHolder = surface.getHolder();
		this.setContentView(surface);

		physicsWorld = new Field();
		physicsWorld.create();

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
			physicsWorld.update(now - time, kick);
			time = now;
			draw();
			handler.postDelayed(update, (long) physicsWorld.timeStep * 1000);
			kick=false;
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
		physicsWorld.draw(canvas);
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			kick=true;
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			

		}
		return true;

	}

	@Override
	public void beginContact(Contact arg0) {
		// TODO Auto-generated method stub
		Log.v("MOTION","b");

	}

	@Override
	public void endContact(Contact arg0) {
		// TODO Auto-generated method stub
		Log.v("MOTION","e");
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