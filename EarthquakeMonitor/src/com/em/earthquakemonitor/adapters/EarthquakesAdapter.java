package com.em.earthquakemonitor.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.em.earthquakemonitor.ItemDetails;
import com.em.earthquakemonitor.R;
import com.em.earthquakemonitor.controller.Config;
import com.em.earthquakemonitor.controller.GlobalEnv;
import com.em.earthquakemonitor.greendao.Earthquakes;

public class EarthquakesAdapter extends ArrayAdapter<Earthquakes> implements
		OnClickListener {
	ArrayList<Earthquakes> elementos;
	Earthquakes marker;
	LayoutInflater li;
	Holder holder;

	public EarthquakesAdapter(Context ctx, int textViewResourceId,
			ArrayList<Earthquakes> e) {
		super(ctx, textViewResourceId, e);
		elementos = e;
		li = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	private class Holder {
		TextView magnitude;
		TextView location;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {

		if (view == null || !(view.getTag() instanceof Holder)) {
			view = li.inflate(R.layout.item_earthquakes, parent, false);

			holder = new Holder();
			holder.magnitude = (TextView) view.findViewById(R.id.magnitude);
			holder.location = (TextView) view.findViewById(R.id.location);
			view.setTag(holder);
		} else {
			holder = (Holder) view.getTag();
		}
		marker = elementos.get(position);
		holder.magnitude.setText(GlobalEnv.formatter.format(marker
				.getQMagnitude()));
		holder.magnitude.setTextColor(getColor(marker.getQMagnitude()));
		holder.location.setText(marker.getQPlace());
		view.setOnClickListener(this);
		view.setTag(marker);
		return view;
	}

	private int getColor(float magnitude) {
		if (magnitude <= 0.9) {
			return 0xff669900;
		} else if (magnitude > 0.9 && magnitude <= 9.0) {
			return 0xffff8800;
		} else {
			return 0xffcc0000;
		}
	}

	@Override
	public void onClick(View view) {
		if (view != null && view.getTag() instanceof Earthquakes) {
			Intent itemDetails = new Intent(getContext(), ItemDetails.class);
			itemDetails.putExtra(Config.ITEM,
					((Earthquakes) view.getTag()).getQId());
			if (getContext() instanceof ActionBarActivity) {
				((ActionBarActivity) getContext()).startActivity(itemDetails);
			}
		}
	}
}
