package com.em.earthquakemonitor.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

import com.em.earthquakemonitor.controller.AppSingleton;

public class ConnectionUtil {
	// Singleton
	private static ConnectionUtil pConnectionUtil = null;

	// Singleton Instance
	private ConnectionUtil() {
	}

	/**
	 * Checks if we have a valid Internet Connection on the device.
	 * 
	 * @param _context
	 * @return True if device has Internet s
	 * 
	 *         Code from: http://www.androidsnippets.org/snippets/131/
	 */
	public static boolean haveInternet() {
		NetworkInfo info = ((ConnectivityManager) AppSingleton.getAppContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();

		if (info == null || !info.isConnected()) {
			return false;
		}

		/*
		 * if (info.isRoaming()) { // here is the roaming option you can change
		 * it if you want to // disable Internet while roaming, just return
		 * false return false; }
		 */
		return true;
	}

	public int getConectionType() {
		ConnectivityManager cM = (ConnectivityManager) AppSingleton
				.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);

		// mobile
		State mobile = cM.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();

		// wifi
		State wifi = cM.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();

		return State.CONNECTED == mobile ? ConnectivityManager.TYPE_MOBILE
				: State.CONNECTED == wifi ? ConnectivityManager.TYPE_WIFI : -1;
	}

	public static ConnectionUtil getConnectionUtil() {
		if (pConnectionUtil == null)
			pConnectionUtil = new ConnectionUtil();
		return pConnectionUtil;
	}
}
