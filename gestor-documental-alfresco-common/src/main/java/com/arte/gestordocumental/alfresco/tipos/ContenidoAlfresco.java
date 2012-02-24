package com.arte.gestordocumental.alfresco.tipos;

public class ContenidoAlfresco {
	
	private String tipoMime;
	private byte[] contenido;
	private String propiedadNodo;
	
	public ContenidoAlfresco(String tipoMime, byte[] contenido, String propiedadNodo) {
		this.tipoMime = tipoMime;
		this.contenido = contenido;
		this.propiedadNodo = propiedadNodo;
	}
	
	public byte[] getContenido() {
		return contenido;
	}
	
	public String getTipoMime() {
		return tipoMime;
	}

	public String getPropiedadNodo() {
		return propiedadNodo;
	}
}
