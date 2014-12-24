package com.em.earthquakemonitor;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class Splash extends BaseActionBarActivity {
	private static final long SPLASH_SCREEN_DELAY = 5000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);

		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				if (isFinishing()) {
					return;
				}
				Intent mainIntent = new Intent().setClass(Splash.this,
						Main.class);
				startActivity(mainIntent);
				finish();
			}
		};
		Timer timer = new Timer();
		timer.schedule(task, SPLASH_SCREEN_DELAY);
	}
}
