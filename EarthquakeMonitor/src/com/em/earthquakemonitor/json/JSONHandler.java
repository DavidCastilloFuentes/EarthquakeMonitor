package com.em.earthquakemonitor.json;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.em.earthquakemonitor.controller.AppSingleton;
import com.em.earthquakemonitor.controller.Config;
import com.em.earthquakemonitor.greendao.Earthquakes;
import com.em.earthquakemonitor.greendao.EarthquakesDao;

public class JSONHandler {

	public static void storeEarthquakes(String input) {
		JSONObject json1;
		JSONObject json2;
		JSONArray jarr1;
		JSONArray jarr2;
		Earthquakes earthquake;
		ArrayList<Earthquakes> earthquakes = new ArrayList<Earthquakes>();

		try {
			// Decode Buffer
			jarr1 = new JSONObject(input).getJSONArray("features");
			for (int i = 0; i < jarr1.length(); i++) {
				earthquake = new Earthquakes();

				json1 = jarr1.getJSONObject(i);
				earthquake.setQId(json1.getString("id"));

				json2 = json1.getJSONObject("properties");
				earthquake.setQMagnitude(json2.getDouble("mag"));
				earthquake.setQPlace(json2.getString("place"));
				earthquake.setQtitle(json2.getString("title"));
				earthquake.setQUrl(json2.getString("url"));
				earthquake.setQDetails(json2.getString("detail"));
				earthquake.setQTime(json2.getLong("time"));

				jarr2 = json1.getJSONObject("geometry").getJSONArray(
						"coordinates");
				earthquake.setQLongitude(jarr2.getDouble(0));
				earthquake.setQLatitude(jarr2.getDouble(1));
				earthquake.setQDepth(jarr2.getDouble(2));

				earthquakes.add(earthquake);
			}

			// Store Buffer
			EarthquakesDao dao = AppSingleton.getTheApplication()
					.getDAOSession().getEarthquakesDao();

			// Cleanup
			dao.deleteAll();

			// Push
			dao.insertInTx(earthquakes);
		} catch (Exception e) {
			Log.i(Config.my_tag, "Error: " + e);
		} finally {
			json1 = json2 = null;
			jarr1 = jarr2 = null;
			earthquakes = null;
			earthquake = null;
		}
	}
}
