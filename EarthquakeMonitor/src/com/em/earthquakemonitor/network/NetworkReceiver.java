package com.em.earthquakemonitor.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.em.earthquakemonitor.controller.Config;

public class NetworkReceiver extends BroadcastReceiver {
	private static boolean isNetworkAvailable = false;
	String typeName;
	String subtypeName;
	CheckReachabilityTask currentTask;

	@SuppressWarnings("deprecation")
	@Override
	public void onReceive(Context ctx, Intent intent) {
		if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
			NetworkInfo info = intent
					.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);

			if (info == null) {
				return;
			}

			typeName = info.getTypeName();
			subtypeName = info.getSubtypeName();

			boolean tmpAvailable = info.isAvailable();

			Log.i(Config.my_tag, "Network Type: " + typeName + ", subtype: "
					+ subtypeName + ", available: " + isNetworkAvailable);

			if (isNetworkAvailable != tmpAvailable && currentTask == null) {
				isNetworkAvailable = tmpAvailable;
				currentTask = new CheckReachabilityTask();
				currentTask.execute();
			}
		}
	}

	private class CheckReachabilityTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			Reachability.getSingleton().checkReachability();
			currentTask = null;
			return null;
		}
	}

	public static String getNetworkType() {
		return ConnectionUtil.getConnectionUtil().getConectionType() == ConnectivityManager.TYPE_MOBILE ? "Mobile"
				: ConnectionUtil.getConnectionUtil().getConectionType() == ConnectivityManager.TYPE_WIFI ? "Wifi"
						: "Ninguno";
	}
}
