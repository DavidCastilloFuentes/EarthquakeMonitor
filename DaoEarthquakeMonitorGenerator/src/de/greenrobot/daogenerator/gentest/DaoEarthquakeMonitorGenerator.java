/*
 * Copyright (C) 2011 Markus Junginger, greenrobot (http://greenrobot.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.greenrobot.daogenerator.gentest;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * Generates entities and DAOs for the example project DaoExample.
 * 
 * Run it as a Java application (not Android).
 * 
 * @author Markus
 */
public class DaoEarthquakeMonitorGenerator {
	public static final int DB_VERSION = 1;

	public static final String TBL_Earthquakes = "Earthquakes";

	public static void main(String[] args) throws Exception {
		Schema schema = new Schema(DB_VERSION,
				"com.em.earthquakemonitor.greendao");
		schema.enableKeepSectionsByDefault();
		schema.enableActiveEntitiesByDefault();

		addEarthquakes(schema);

		new DaoGenerator().generateAll(schema, "../EarthquakeMonitor/src");
	}

	public static void addEarthquakes(Schema schema) {
		Entity earthquakes = schema.addEntity(TBL_Earthquakes);
		earthquakes.addIdProperty().columnName("Z_PK").autoincrement();
		earthquakes.addStringProperty("QId");
		earthquakes.addStringProperty("QPlace");
		earthquakes.addStringProperty("QUrl");
		earthquakes.addStringProperty("Qtitle");
		earthquakes.addStringProperty("QDetails");
		earthquakes.addDoubleProperty("QMagnitude");
		earthquakes.addDoubleProperty("QLatitude");
		earthquakes.addDoubleProperty("QLongitude");
		earthquakes.addDoubleProperty("QDepth");
		earthquakes.addLongProperty("QTime");
	}
}
