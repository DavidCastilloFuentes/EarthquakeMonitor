package com.em.earthquakemonitor.network;

import android.os.AsyncTask;

public class ServiceExecutorAsyncTask extends AsyncTask<Service, Void, Result> {
	@Override
	protected void onPostExecute(Result v) {
	}

	@Override
	protected Result doInBackground(Service... services) {
		Result res = new Result();

		if (!isCancelled()) {
		}
		return res;
	}
}
