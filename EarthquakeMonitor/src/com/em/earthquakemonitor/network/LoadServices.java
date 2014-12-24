package com.em.earthquakemonitor.network;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Build;
import android.util.Log;

import com.em.earthquakemonitor.controller.Config;

public class LoadServices extends ServiceExecutorAsyncTask {
	private static final int corePoolSize = 5;
	private static final int maximumPoolSize = 10;
	private static final int keepAliveTime = 10;
	private static Executor threadPoolExecutor;
	private static BlockingQueue<Runnable> workQueue;
	private Service[] services;

	public static BlockingQueue<Runnable> getWorkQueue() {
		if (workQueue == null) {
			workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
		}
		return workQueue;
	}

	public static Executor getThreadPoolExecutor() {
		if (threadPoolExecutor == null) {
			threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
					maximumPoolSize, keepAliveTime, TimeUnit.SECONDS,
					getWorkQueue());
		}
		return threadPoolExecutor;
	}

	@Override
	protected void onPostExecute(Result result) {
		for (Service service : this.services) {
			if (service != null && service.getTaskDone() != null
					&& service.isValidService()) {
				service.getTaskDone().taskDone(service);
				service = null;
			}
		}
		this.services = null;
	}

	@Override
	protected Result doInBackground(Service... _services) {
		this.services = _services;
		String out;
		int i = 0;
		JSONObject jsonAux;
		JSONArray arrayAux;
		String message;
		int codeMessage;
		HttpRequest httpRequest = HttpRequest.getSingleton();

		while (i < this.services.length) {
			if (this.services[i] == null) {
				++i;
				continue;
			}

			Log.d(Config.my_tag, services[i].getServiceUrl()
					+ " with request:  " + services[i].getServiceInput());

			out = this.services[i].getServiceType() == Config.SERVICE_GET ? httpRequest
					.getHttpStream1(this.services[i].getServiceUrl())
					: httpRequest.sendPostRequest(
							this.services[i].getServiceUrl(),
							this.services[i].getServiceInput());

			Log.i(Config.my_tag, this.services[i].getServiceUrl()
					+ ", Response: " + out);

			this.services[i].setResponseObject(out);
			codeMessage = -1;
			message = "Error";

			try {
				jsonAux = new JSONObject(
						(String) this.services[i].getResponseObject());

				codeMessage = jsonAux.getInt("codeMessage");
				message = jsonAux.getString("message");
				jsonAux = null;
			} catch (Exception e) {
				arrayAux = null;
				try {
					arrayAux = new JSONArray(
							(String) this.services[i].getResponseObject());
					codeMessage = 0;
					message = "Llamada exitosa";
				} catch (JSONException e1) {
					Log.i(Config.my_tag,
							"Failed Service Call on service: "
									+ services[i].getServiceUrl()
									+ " With response: \""
									+ this.services[i].getResponseObject()
									+ "\" Error Message: " + e1.getMessage());
				}
				if (arrayAux == null) {
					Log.i(Config.my_tag,
							"Failed Service Call on service: "
									+ services[i].getServiceUrl()
									+ " With response: \""
									+ this.services[i].getResponseObject()
									+ "\" Error Message: " + e.getMessage());
				}
				arrayAux = null;
			}

			this.services[i].setServiceResponseCode(codeMessage);
			this.services[i].setMensaje(message);

			out = null;
			message = null;

			i++;
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public void loadOnExecutor(Service... services) {
		if (services.length == 0) {
			return;
		}

		if (services.length == 1
				|| Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			try {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					this.executeOnExecutor(
							LoadServices.getThreadPoolExecutor(), services);
				} else {
					this.execute(services);
				}
			} catch (Exception e) {
				Log.i(Config.my_tag, "Error on THREAD Executor");
			}
		} else {
			LoadServices[] lServices = new LoadServices[services.length];
			Service[][] tmpServices = new Service[services.length][];
			Service[] service;

			for (int i = 0; i < services.length; i++) {
				service = new Service[1];
				service[0] = services[i];
				tmpServices[i] = service;
				if (i > 0) {
					lServices[i] = new LoadServices();
				} else {
					lServices[0] = this;
				}
			}

			for (int i = 0; i < lServices.length; i++) {
				lServices[i].loadOnExecutor(tmpServices[i]);
			}

			lServices = null;
			tmpServices = null;
		}
	}
}
