package com.em.earthquakemonitor.controller;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import com.em.earthquakemonitor.greendao.DaoMaster;
import com.em.earthquakemonitor.greendao.DaoSession;
import com.em.earthquakemonitor.interfaces.TaskDone;
import com.em.earthquakemonitor.network.Reachability;
import com.em.earthquakemonitor.network.Reachability.ReachabilityCallback;
import com.em.earthquakemonitor.network.Reachability.ReachabilityState;
import com.em.earthquakemonitor.network.Service;

public class AppSingleton extends Application implements ReachabilityCallback,
		TaskDone {
	static boolean activityVisible;
	static Context globalAppContext;
	static AppSingleton theApplication;
	static Reachability pReachability;
	SQLiteDatabase database;
	DaoMaster daoMaster;
	DaoSession daoSession;
	SharedPreferences mPref;

	@Override
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public void onCreate() {
		super.onCreate();
		theApplication = this;
		globalAppContext = getApplicationContext();
		Reachability.startReachabilityWithHost("https://www.google.com.mx/",
				theApplication);
		mPref = PreferenceManager.getDefaultSharedPreferences(this);
	}

	public static Context getAppContext() {
		return globalAppContext;
	}

	public static boolean isActivityVisible() {
		return activityVisible;
	}

	public static void onActivityResume() {
		activityVisible = true;
		boolean fromBackground = !theApplication.mPref.getBoolean(
				Config.my_package + "HAS_FOCUS", false);
		if (fromBackground) {
			Log.d(Config.my_tag, "Coming from BACKGROUND");
			theApplication.loadData();
		}
	}

	public static void onActivityPause() {
		activityVisible = false;
	}

	@Override
	public void reachabilityChanged(ReachabilityState newState) {
		Log.d(Config.my_tag, "Reachability Changed: " + newState);
		boolean isInBackground = !theApplication.mPref.getBoolean(
				Config.my_package + "HAS_FOCUS", false);
		if (Reachability.isReachable() && !isInBackground) {
			Log.d(Config.my_tag, "*** LOADING ALL ***");
			loadData();
		}
	}

	public void loadData() {
		openSQLiteDatabase();
		loadServices();
	}

	public SQLiteDatabase openSQLiteDatabase() throws SQLiteException {
		if (database == null) {
			database = new DaoMaster.DevOpenHelper(this, Config.DATABASE_NAME,
					null).getWritableDatabase();
		} else if (!database.isOpen()) {
			database = new DaoMaster.DevOpenHelper(this, Config.DATABASE_NAME,
					null).getWritableDatabase();
		}
		return database;
	}

	public DaoSession getDAOSession() {
		DaoMaster dm = getDAOMaster();
		if (daoSession == null)
			daoSession = dm.newSession();
		return daoSession;
	}

	public DaoMaster getDAOMaster() {
		SQLiteDatabase d = openSQLiteDatabase();
		if (daoMaster == null)
			daoMaster = new DaoMaster(d);
		else if (daoMaster.getDatabase() != d)
			daoMaster = new DaoMaster(d);
		return daoMaster;
	}

	public SQLiteDatabase getSQLiteDatabase() {
		return openSQLiteDatabase();
	}

	public void closeSQLiteDatabase() {
		try {
			if (database != null && database.isOpen()) {
				database.close();
			}
		} catch (Exception e) {
		}
		database = null;
		daoMaster = null;
		daoSession = null;
	}

	public void loadServices() {
	}

	@Override
	public void taskDone(Service serviceResponse) {
	}

	@Override
	public void globalUpdate() {
	}

	public static AppSingleton getTheApplication() {
		return theApplication;
	}
}
