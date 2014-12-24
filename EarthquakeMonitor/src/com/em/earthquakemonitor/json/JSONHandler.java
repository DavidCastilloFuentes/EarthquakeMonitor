package com.em.earthquakemonitor.json;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.em.earthquakemonitor.controller.AppSingleton;
import com.em.earthquakemonitor.greendao.Earthquakes;
import com.em.earthquakemonitor.greendao.EarthquakesDao;

public class JSONHandler {

	public static void storeEarthquakes(String input) {
		JSONObject json1;
		JSONObject json2;
		JSONArray jarr;
		Earthquakes earthquake;
		ArrayList<Earthquakes> earthquakes = new ArrayList<Earthquakes>();

		try {
			// Decode Buffer
			jarr = new JSONObject(input).getJSONArray("features");
			for (int i = 0; i < jarr.length(); i++) {
				earthquake = new Earthquakes();

				json1 = jarr.getJSONObject(i);
				earthquake.setQId(json1.getString("id"));

				json2 = json1.getJSONObject("properties");
				earthquake.setQMagnitude((float) json2.getDouble("mag"));
				earthquake.setQPlace(json2.getString("place"));
				earthquake.setQtitle(json2.getString("title"));
				earthquake.setQUrl(json2.getString("url"));
				earthquake.setQDetails(json2.getString("detail"));

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

		} finally {
			json1 = json2 = null;
			jarr = null;
			earthquakes = null;
			earthquake = null;
		}
	}
}
