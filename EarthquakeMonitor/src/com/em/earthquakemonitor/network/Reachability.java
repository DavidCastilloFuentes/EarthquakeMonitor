package com.em.earthquakemonitor.network;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.util.Log;

import com.em.earthquakemonitor.controller.AppSingleton;
import com.em.earthquakemonitor.controller.Config;

public class Reachability {
	public enum ReachabilityState {
		ReachableWifi, ReachableMobile, NotReachable,
	}

	private static Reachability singleton;
	private String host;
	private ReachabilityState currentState;
	private ReachabilityCallback delegate;

	private Reachability() {
	}

	public static Reachability getSingleton() {
		if (singleton == null) {
			singleton = new Reachability();
		}
		return singleton;
	}

	public void setDelegate(ReachabilityCallback del) {
		this.delegate = del;
	}

	public void checkReachability() {
		ConnectivityManager connMgr = ((ConnectivityManager) AppSingleton
				.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE));
		NetworkInfo netInfo = connMgr.getActiveNetworkInfo();
		boolean isReachable = false;
		ReachabilityState tempState;

		if (netInfo != null && netInfo.isConnected()) {
			// Some sort of connection is open, check if server is reachable
			try {
				URL url = new URL(host);
				HttpURLConnection urlc = (HttpURLConnection) url
						.openConnection();
				urlc.setRequestProperty("User-Agent", "Android Application");
				urlc.setRequestProperty("Connection", "close");
				urlc.setConnectTimeout(30 * 1000);
				urlc.connect();
				isReachable = (urlc.getResponseCode() == 200);
				urlc.disconnect();
			} catch (IOException e) {
				Log.e("Error", e.getMessage());
			}
		}

		if (isReachable) {
			State mobile = null;
			State wifi = null;

			netInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (netInfo != null) {
				mobile = netInfo.getState();
			}

			netInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (netInfo != null) {
				wifi = netInfo.getState();
			}

			if (mobile == State.CONNECTED) {
				tempState = ReachabilityState.ReachableMobile;
				Log.d(Config.my_tag, "***** MOBILE NETWORK CONNECTED *****");
			} else if (wifi == State.CONNECTED) {
				tempState = ReachabilityState.ReachableWifi;
				Log.d(Config.my_tag, "***** WIFI NETWORK CONNECT *****");
			} else {
				tempState = ReachabilityState.NotReachable;
				Log.d(Config.my_tag, "***** NO NETWORK AVAILALE *****");
			}
		} else {
			tempState = ReachabilityState.NotReachable;
			Log.d(Config.my_tag, "***** NO NETWORK AVAILALE *****");
		}

		if (currentState != tempState && delegate != null) {
			currentState = tempState;
			delegate.reachabilityChanged(currentState);
		}
		Log.d(Config.my_tag, "THREAD DE MONITOREO MUERTO");
	}

	public static ReachabilityState getNetWorkType() {
		ReachabilityState type = ReachabilityState.NotReachable;

		if (AppSingleton.getAppContext() == null) {
			return type;
		}

		if (AppSingleton.getAppContext().getSystemService(
				Context.CONNECTIVITY_SERVICE) == null) {
			return type;
		}

		ConnectivityManager cm = (ConnectivityManager) AppSingleton
				.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);

		if (cm != null) {
			NetworkInfo[] netInfo = cm.getAllNetworkInfo();
			for (NetworkInfo ni : netInfo) {
				if (ni.isConnected() && ni.isAvailable()) {
					if (ni.getTypeName().equalsIgnoreCase("WIFI")) {
						type = ReachabilityState.ReachableWifi;
					} else if (ni.getTypeName().equalsIgnoreCase("MOBILE")) {
						type = ReachabilityState.ReachableMobile;
					}
				}
			}
		}
		return type;
	}

	public static boolean isReachable() {
		return getSingleton().currentState == ReachabilityState.ReachableMobile
				|| getSingleton().currentState == ReachabilityState.ReachableWifi;
	}

	public static ReachabilityState getCurrentReachabilityState() {
		return singleton.currentState;
	}

	public static void startReachabilityWithHost(String host,
			ReachabilityCallback delegate) {
		getSingleton().setHost(host);
		getSingleton().setDelegate(delegate);
	}

	public static void stopReachability() {
		getSingleton().setDelegate(null);
	}

	public interface ReachabilityCallback {
		public void reachabilityChanged(ReachabilityState newState);
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}
}
