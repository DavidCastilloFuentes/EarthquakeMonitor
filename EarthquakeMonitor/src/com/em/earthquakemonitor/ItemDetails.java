package com.em.earthquakemonitor;

import java.util.Calendar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.em.earthquakemonitor.controller.Config;
import com.em.earthquakemonitor.controller.GlobalEnv;
import com.em.earthquakemonitor.greendao.Earthquakes;
import com.em.earthquakemonitor.interfaces.TaskDone;
import com.em.earthquakemonitor.network.Service;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ItemDetails extends BaseFragmentActivity implements TaskDone {
	TextView magnitude;
	TextView date;
	TextView location;
	Earthquakes earthquake;
	String itemId;
	LatLng earthquakeLoc;
	GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_details);
		itemId = getIntent().getStringExtra(Config.ITEM);
		loadContents();
	}

	protected void loadContents() {
		earthquake = GlobalEnv.getEarthquake(itemId);
		if (earthquake == null) {
			finish();
			return;
		}

		magnitude = (TextView) findViewById(R.id.magnitude);
		date = (TextView) findViewById(R.id.date);
		location = (TextView) findViewById(R.id.location);

		magnitude.setText("Magnitude: "
				+ GlobalEnv.formatter.format(earthquake.getQMagnitude()));
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(earthquake.getQTime());
		date.setText("Date & Time: " + calendar.getTime());
		location.setText("Location: " + earthquake.getQPlace());
		earthquakeLoc = new LatLng(earthquake.getQLatitude(),
				earthquake.getQLongitude());
		map = ((com.google.android.gms.maps.SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.mapView)).getMap();
		CameraPosition cameraPosition = new CameraPosition.Builder()
		/* Sets the location */
		.target(earthquakeLoc)
		/* Sets the zoom */
		.zoom(14)
		/* Sets the orientation of the camera to east */
		.bearing(90)
		/* Sets the tilt of the camera to 30 degrees */
		.tilt(30)
		/* Creates a CameraPosition from the builder */
		.build();

		if (map != null) {
			map.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));
			map.addMarker(new MarkerOptions()
					.position(earthquakeLoc)
					.title(earthquake.getQtitle())
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_launcher)));
		} else {
			Toast.makeText(this,
					"Maps support is not available for your device",
					Toast.LENGTH_LONG).show();
		}
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
}
