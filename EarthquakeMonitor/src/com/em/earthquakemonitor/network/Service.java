package com.em.earthquakemonitor.network;

import com.em.earthquakemonitor.interfaces.TaskDone;

public class Service {
	private String serviceName;
	private String serviceInput;
	private String mensaje;

	private int serviceType;
	private int serviceCode;
	private int serviceResponseCode = -1;

	private Object otherDataObject;
	private Object responseObject;

	private boolean serviceRequiereLogin;
	private boolean fullUrl;
	private boolean noSesgetCall;
	private boolean executedWhenAppCameFromBackground;
	private boolean validService = true;
	private TaskDone taskDone;

	private long initTime;

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceUrl() {
		return serviceName;
	}

	public void setServiceInput(String serviceInput) {
		this.serviceInput = serviceInput;
	}

	public String getServiceInput() {
		return serviceInput;
	}

	public void setServiceType(int serviceType) {
		this.serviceType = serviceType;
	}

	public int getServiceType() {
		return serviceType;
	}

	public void setServiceCode(int serviceCode) {
		this.serviceCode = serviceCode;
	}

	public int getServiceCode() {
		return serviceCode;
	}

	public void setTaskDone(TaskDone fragment2Service) {
		this.taskDone = fragment2Service;
	}

	public TaskDone getTaskDone() {
		return taskDone;
	}

	public void setOtherDataObject(Object otherDataObject) {
		this.otherDataObject = otherDataObject;
	}

	public Object getOtherDataObject() {
		return otherDataObject;
	}

	public void setResponseObject(Object responseObject) {
		this.responseObject = responseObject;
	}

	public Object getResponseObject() {
		return responseObject;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setServiceResponseCode(int serviceResponseCode) {
		this.serviceResponseCode = serviceResponseCode;
	}

	public int getServiceResponseCode() {
		return serviceResponseCode;
	}

	public void setServiceRequiereLogin(boolean serviceRequiereLogin) {
		this.serviceRequiereLogin = serviceRequiereLogin;
	}

	public boolean isServiceRequiereLogin() {
		return serviceRequiereLogin;
	}

	public void setFullUrl(boolean fullUrl) {
		this.fullUrl = fullUrl;
	}

	public boolean isFullUrl() {
		return fullUrl;
	}

	public void setNoSesionNeededGETCall(boolean noSesgetCall) {
		this.noSesgetCall = noSesgetCall;
	}

	public boolean isNoSesionNeededGETCall() {
		return noSesgetCall;
	}

	public boolean isExecutedWhenAppCameFromBackground() {
		return executedWhenAppCameFromBackground;
	}

	public void setExecutedWhenAppCameFromBackground(
			boolean executedWhenAppCameFromBackground) {
		this.executedWhenAppCameFromBackground = executedWhenAppCameFromBackground;
	}

	public long getInitTime() {
		return initTime;
	}

	public void setInitTime(long initTime) {
		this.initTime = initTime;
	}

	public boolean isValidService() {
		return validService;
	}

	public void setValidService(boolean validService) {
		this.validService = validService;
	}
}
