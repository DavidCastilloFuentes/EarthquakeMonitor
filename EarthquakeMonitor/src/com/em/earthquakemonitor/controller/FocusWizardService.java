package com.em.earthquakemonitor.controller;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class FocusWizardService extends Service {
	protected Intent mFocusIntent;
	protected SharedPreferences mPref;
	protected SharedPreferences.Editor mPrefEditor;

	@Override
	public void onCreate() {
		super.onCreate();

		mFocusIntent = new Intent(Config.my_package + "FOCUS_CHANGED_ACTION");
		mPref = PreferenceManager.getDefaultSharedPreferences(this);
		mPrefEditor = mPref.edit();

		onFocus();

		// Register a receiver to receive screen status updates.
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(screenStatusReceiver, filter);
	}

	// onUnbind is only called when all the activities have unbind
	// from the service, which is perfect for what we need to do.
	@Override
	public boolean onUnbind(Intent intent) {
		onUnfocus();
		unregisterReceiver(screenStatusReceiver);
		return false;
	}

	private void onFocus() {
		// We save the status in the shared preferences to
		// retrieve whenever we need it.
		mPrefEditor.putBoolean(Config.my_package + "HAS_FOCUS", true);
		mPrefEditor.commit();
		mFocusIntent.putExtra(Config.my_package + "EXTRA_KEY_HAS_FOCUS", true);
		sendBroadcast(mFocusIntent);
	}

	private void onUnfocus() {
		mPrefEditor.putBoolean(Config.my_package + "HAS_FOCUS", false);
		mPrefEditor.commit();
		mFocusIntent.putExtra(Config.my_package + "EXTRA_KEY_HAS_FOCUS", false);
		sendBroadcast(mFocusIntent);
	}

	private class BReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
				onFocus();
				Log.i(Config.my_tag, "SCREEN_ON");
			} else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
				onUnfocus();
				Log.i(Config.my_tag, "SCREEN_OFF");
			}
		}
	}

	private final BReceiver screenStatusReceiver = new BReceiver();

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	public class LocalBinder extends Binder {
		FocusWizardService getService() {
			return FocusWizardService.this;
		}
	}

	private final IBinder mBinder = new LocalBinder();
}
