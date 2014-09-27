package com.joanalbert.footballsample;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

// This is the main activity which also detects screen contacts
public class MainActivity extends Activity {

	private SurfaceView surface;
	private SurfaceHolder surfaceHolder;
	private Canvas c = null;
	private Field field;
	private Handler handler;
	private long time;
	boolean kick = false;
	float fx = 0, fy = 0;
	float x1 = 0, x2 = 0, y1 = 0, y2 = 0;
	
	public static SharedPreferences pref; 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		pref = PreferenceManager
				.getDefaultSharedPreferences(this);
		
		// We place a SurfaceView in the activity in order to draw on it
		surface = new SurfaceView(this);
		surfaceHolder = surface.getHolder();
		this.setContentView(surface);

		// Get the size of the screen
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);

		// The scale to transform the virtual world into the real device screen
		// is increased as much as possible
		GameInfo.deviceScreenWidth = size.x;
		GameInfo.deviceScreenHeight = size.y;
		while (GameInfo.deviceScreenWidth > (GameInfo.worldScale + 1)
				* GameInfo.worldWidth
				&& GameInfo.deviceScreenHeight > (GameInfo.worldScale + 1)
						* GameInfo.worldHeight)
			GameInfo.worldScale++;

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
	
	@Override
	protected void onResume() {
		super.onResume();
		handler = new Handler();
		handler.post(update);

		// Current time
		this.time = System.currentTimeMillis();
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
		int action = MotionEventCompat.getActionMasked(event);
		switch (action) {
		case (MotionEvent.ACTION_DOWN):
			// If the user touches the screen we store the position
			x1 = event.getX();
			y1 = event.getY();
			return true;
		case (MotionEvent.ACTION_UP):
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
				if (Math.abs(dx) < Math.abs(dy)) {
					fy = -2000 * GameInfo.movementRate;
					fx = 0;
				} else {
					fy = 0;
					if (dx > 0)
						fx = 2000 * GameInfo.movementRate;
					else
						fx = -2000 * GameInfo.movementRate;
				}
			}
			return true;
		default:
			return super.onTouchEvent(event);
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.preferencemenu:
			showPreferences();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/***
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	public void showPreferences() {
		Intent i = new Intent(this, SetPreferenceActivity.class);
		startActivity(i);
	}

}