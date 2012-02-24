package com.arte.gestordocumental.util.concurrencia;


/**
 * @author Arte Consultores
 * Wrapper con datos de configuracion para el WebServiceFactory
 */
public class WebServiceFactoryConfig {

	private String endPointAddress = null;
	private String endPointWebapp = null;
	private int timeoutMilliseconds;
	private String user = null;
	private String password = null;

	public WebServiceFactoryConfig(String endPointAddress, String endPointWebapp, int timeoutMilliseconds, String user, String password) {
		this.endPointAddress = endPointAddress;
		this.endPointWebapp = endPointWebapp;
		this.timeoutMilliseconds = timeoutMilliseconds;
		this.user = user;
		this.password = password;
	}
	
	public String getEndPointAddress() {
		return endPointAddress;
	}

	public String getEndPointWebapp() {
		return endPointWebapp;
	}

	public int getTimeoutMilliseconds() {
		return timeoutMilliseconds;
	}

	public void setEndPointAddress(String endPointAddress) {
		this.endPointAddress = endPointAddress;
	}

	public void setEndPointWebapp(String endPointWebapp) {
		this.endPointWebapp = endPointWebapp;
	}

	public void setTimeoutMilliseconds(int timeoutMilliseconds) {
		this.timeoutMilliseconds = timeoutMilliseconds;
	}
	
	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}

