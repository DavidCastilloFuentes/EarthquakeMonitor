package com.em.earthquakemonitor;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.em.earthquakemonitor.adapters.EarthquakesAdapter;
import com.em.earthquakemonitor.controller.Config;
import com.em.earthquakemonitor.controller.GlobalEnv;
import com.em.earthquakemonitor.greendao.Earthquakes;
import com.em.earthquakemonitor.interfaces.TaskDone;
import com.em.earthquakemonitor.json.JSONHandler;
import com.em.earthquakemonitor.network.LoadServices;
import com.em.earthquakemonitor.network.Service;

public class Main extends BaseActionBarActivity implements TaskDone {
	ArrayList<Earthquakes> earthquakesList = new ArrayList<Earthquakes>();
	EarthquakesAdapter adapter;
	TextView feed;
	ListView earthquakes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		feed = (TextView) findViewById(R.id.feed);
		earthquakes = (ListView) findViewById(R.id.earthquakes);
		adapter = new EarthquakesAdapter(this, R.layout.item_earthquakes,
				earthquakesList);
		earthquakes.setAdapter(adapter);
		fillEarthquakes();
		loadFeed();
	}

	protected void loadFeed() {
		Service[] services = new Service[1];
		services[0] = new Service();
		services[0].setServiceCode(Config.FEED_CONTENT_CODE);
		services[0].setServiceName(Config.FEED_CONTENT);
		services[0].setServiceType(Config.SERVICE_GET);
		services[0].setTaskDone(this);
		new LoadServices().loadOnExecutor(services);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void taskDone(Service response) {
		switch (response.getServiceCode()) {
		case Config.FEED_CONTENT_CODE:
			JSONHandler.storeEarthquakes((String) response.getResponseObject());
			fillEarthquakes();
			break;
		}
	}

	@Override
	public void globalUpdate() {
	}

	public void updateView() {
	}

	public void fillEarthquakes() {
		earthquakesList.clear();
		ArrayList<Earthquakes> m_lst = GlobalEnv.getEarthquakes();
		if (m_lst != null) {
			earthquakesList.addAll(m_lst);
		}
		updateEarthquakes();

		if (earthquakesList.size() == 0) {
			feed.setVisibility(View.VISIBLE);
			earthquakes.setVisibility(View.GONE);
		} else {
			feed.setVisibility(View.GONE);
			earthquakes.setVisibility(View.VISIBLE);
		}
	}

	public void updateEarthquakes() {
		try {
			adapter.notifyDataSetChanged();
		} catch (Exception e) {
		}
	}
}
