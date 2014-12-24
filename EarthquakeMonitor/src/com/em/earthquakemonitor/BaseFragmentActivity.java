package com.em.earthquakemonitor;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;

import com.em.earthquakemonitor.controller.AppSingleton;
import com.em.earthquakemonitor.controller.FocusWizardService;

public class BaseFragmentActivity extends FragmentActivity {
	private final ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName cn, IBinder service) {
		}

		@Override
		public void onServiceDisconnected(ComponentName cn) {
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		AppSingleton.onActivityResume();
	}

	@Override
	protected void onNewIntent(Intent newIntent) {
		this.setIntent(newIntent);
	}

	@Override
	public void onPause() {
		super.onPause();
		AppSingleton.onActivityPause();
	}

	@Override
	public void onStart() {
		super.onStart();
		bindService(new Intent(this, FocusWizardService.class), mConnection,
				Context.BIND_AUTO_CREATE);
	}

	@Override
	public void onStop() {
		super.onStop();
		unbindService(mConnection);
	}
}
