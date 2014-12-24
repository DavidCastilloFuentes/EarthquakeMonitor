package com.em.earthquakemonitor.interfaces;

import com.em.earthquakemonitor.network.Service;

public interface TaskDone {
	public void taskDone(Service serviceResponse);

	public void globalUpdate();
}
