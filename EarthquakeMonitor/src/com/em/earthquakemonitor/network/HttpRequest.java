package com.em.earthquakemonitor.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.ConnectionClosedException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.annotation.SuppressLint;
import android.util.Log;

import com.em.earthquakemonitor.controller.Config;

public class HttpRequest {
	private DefaultHttpClient httpClient;
	private static HttpRequest pSingleton;
	public static final int MAX_TOTAL_CONNECTIONS = 20;
	public static final int MAX_CONNECTIONS_PER_ROUTE = 20;
	public static final int TIMEOUT_CONNECT = 15000;
	public static final int TIMEOUT_READ = 15000;

	final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		@Override
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};

	@SuppressLint("TrulyRandom")
	private static void trustAllHosts() {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}

			@Override
			public void checkClientTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}
		} };

		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private HttpRequest() {
		buildNewEmptyDefaultHttpClient();
	}

	private void buildNewEmptyDefaultHttpClient() {
		X509HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
		SSLSocketFactory httpsSocketFactory = SSLSocketFactory
				.getSocketFactory();
		httpsSocketFactory.setHostnameVerifier(hostnameVerifier);

		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		schemeRegistry.register(new Scheme("https", httpsSocketFactory, 443));

		HttpParams connManagerParams = new BasicHttpParams();
		ConnManagerParams.setMaxTotalConnections(connManagerParams,
				MAX_TOTAL_CONNECTIONS);
		ConnManagerParams.setMaxConnectionsPerRoute(connManagerParams,
				new ConnPerRouteBean(MAX_CONNECTIONS_PER_ROUTE));

		HttpConnectionParams.setConnectionTimeout(connManagerParams,
				TIMEOUT_CONNECT);
		HttpConnectionParams.setSoTimeout(connManagerParams, TIMEOUT_READ);

		ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(
				new BasicHttpParams(), schemeRegistry);

		httpClient = new DefaultHttpClient(cm, new BasicHttpParams());
		HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
	}

	public static synchronized HttpRequest getSingleton() {
		if (pSingleton == null)
			pSingleton = new HttpRequest();
		return pSingleton;
	}

	public DefaultHttpClient getDefaultHttpClient() {
		return httpClient;
	}

	synchronized public String getHttpStream1(String uri) {
		StringBuilder response = new StringBuilder();
		try {
			HttpGet get = new HttpGet();
			get.setURI(new URI(uri));
			get.setHeader("Accept", "application/json");
			get.setHeader("Content-Type", "application/json");
			get.setHeader("Cache-Control", "no-cache");
			HttpResponse httpResponse = httpClient.execute(get);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity messageEntity = httpResponse.getEntity();
				InputStream is = messageEntity.getContent();
				BufferedReader br = new BufferedReader(
						new InputStreamReader(is));
				String line;
				while ((line = br.readLine()) != null) {
					response.append(line);
				}
			} else {
				Log.e(Config.my_tag, "URL: " + uri
						+ httpResponse.getStatusLine().getStatusCode()
						+ " Error: "
						+ httpResponse.getStatusLine().getReasonPhrase());
			}
			httpResponse.getEntity().consumeContent();
		} catch (UnknownHostException e) {
			Log.i(Config.my_tag, "Error: " + e);
		} catch (ConnectTimeoutException e) {
			Log.i(Config.my_tag, "Error: " + e);
		} catch (IOException e) {
			Log.i(Config.my_tag, "Error: " + e);
		} catch (Exception e) {
			Log.i(Config.my_tag, "Error: " + e);
		}
		return response.toString();
	}

	@SuppressLint("DefaultLocale")
	public String getHttpStream2(String m_url) {
		InputStream in = null;
		int response = -1;
		StringBuilder result = new StringBuilder();
		HttpURLConnection httpConn;

		try {
			URL url = new URL(m_url);
			if (url.getProtocol().toLowerCase().equals("https")) {
				trustAllHosts();
				HttpsURLConnection https = (HttpsURLConnection) url
						.openConnection();
				https.setHostnameVerifier(DO_NOT_VERIFY);
				httpConn = https;
			} else {
				httpConn = (HttpURLConnection) url.openConnection();
			}
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.connect();
			response = httpConn.getResponseCode();
			if (response == HttpURLConnection.HTTP_OK) {
				in = httpConn.getInputStream();
				BufferedReader br = new BufferedReader(
						new InputStreamReader(in));
				String line;
				while ((line = br.readLine()) != null) {
					result.append(line);
				}
			} else {

			}
			httpConn.disconnect();
		} catch (UnknownHostException e) {
			Log.i("ERROR: ", e.getMessage());

		} catch (ConnectionClosedException e) {

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
		}
		return result.toString();
	}

	public String sendPostRequest(String url, String data) {
		StringBuilder response = new StringBuilder();
		HttpResponse httpResponse;
		try {
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-Type", "application/json");
			httpPost.setEntity(new StringEntity(data));
			httpResponse = httpClient.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity messageEntity = httpResponse.getEntity();
				InputStream is = messageEntity.getContent();
				BufferedReader br = new BufferedReader(
						new InputStreamReader(is));
				String line;
				while ((line = br.readLine()) != null) {
					response.append(line);
				}
			} else {
				Log.e(Config.my_tag, "URL: " + url + " Body: " + data
						+ " HTTP POST status code is: "
						+ httpResponse.getStatusLine().getStatusCode()
						+ " Error: "
						+ httpResponse.getStatusLine().getReasonPhrase());
			}
			httpResponse.getEntity().consumeContent();
		} catch (UnknownHostException e) {
			Log.i("ERROR: ", e.getMessage());

		} catch (ConnectTimeoutException e) {

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
		}
		return response.toString();
	}
}
