package com.em.earthquakemonitor;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.em.earthquakemonitor.adapters.EarthquakesAdapter;
import com.em.earthquakemonitor.controller.GlobalEnv;
import com.em.earthquakemonitor.greendao.Earthquakes;
import com.em.earthquakemonitor.interfaces.TaskDone;
import com.em.earthquakemonitor.network.Service;

public class Main extends BaseFragmentActivity implements TaskDone {
	public static boolean isActivityVisible;
	public static FragmentActivity pActivity;
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
		pActivity = this;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void taskDone(Service response) {
		switch (response.getServiceCode()) {
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

	@Override
	public void onResume() {
		super.onResume();
		isActivityVisible = true;
	}

	@Override
	public void onPause() {
		super.onPause();
		isActivityVisible = false;
	}

	@Override
	public void finish() {
		super.finish();
		pActivity = null;
	}
}
