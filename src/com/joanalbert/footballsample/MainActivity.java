package com.joanalbert.footballsample;
import android.app.Activity;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainActivity extends Activity {

	private SurfaceView surface;
	private SurfaceHolder surfaceHolder;
	private Canvas c = null;
	private Field physicsWorld;
	private Handler handler;
	private long time;
	
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
			physicsWorld.update(now - time);
			time = now;
			draw();
			handler.postDelayed(update, (long)physicsWorld.timeStep * 1000);
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

}