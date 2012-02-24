package com.arte.gestordocumental.uri;

public class UriDocumento {
	
	private String uriCompleta;
	private String idYVersion;
	private String idDocumento;
	private String version;
	
	public UriDocumento(String uriCompleta, String idYVersion, String idDocumento, String version) {
		this.uriCompleta = uriCompleta;
		this.idYVersion = idYVersion;
		this.idDocumento = idDocumento;
		this.version = version;
	}
	
	public String getIdDocumento() {
		return idDocumento;
	}
	public void setIdDocumento(String idDocumento) {
		this.idDocumento = idDocumento;
	}
	
	public String getIdYVersion() {
		return idYVersion;
	}

	public void setIdYVersion(String idYVersion) {
		this.idYVersion = idYVersion;
	}

	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}

	public String getUriCompleta() {
		return uriCompleta;
	}

	public void setUriCompleta(String uriCompleta) {
		this.uriCompleta = uriCompleta;
	}
}
