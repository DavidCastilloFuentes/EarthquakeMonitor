package com.em.earthquakemonitor.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.em.earthquakemonitor.greendao.Earthquakes;
import com.em.earthquakemonitor.greendao.EarthquakesDao;

public class GlobalEnv {
	public static DecimalFormat formatter = new DecimalFormat("#.##");

	public static ArrayList<Earthquakes> getEarthquakes() {
		ArrayList<Earthquakes> f = null;
		EarthquakesDao dao = AppSingleton.getTheApplication().getDAOSession()
				.getEarthquakesDao();

		List<Earthquakes> lst = dao.loadAll();
		if (lst != null && lst.size() > 0) {
			f = new ArrayList<Earthquakes>();
			f.addAll(lst);
			lst = null;
		}

		return f;
	}
}
