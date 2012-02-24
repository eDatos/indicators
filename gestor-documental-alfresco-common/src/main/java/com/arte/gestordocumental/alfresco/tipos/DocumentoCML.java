package com.arte.gestordocumental.alfresco.tipos;

import org.alfresco.webservice.types.Reference;

public class DocumentoCML {
	
	private String idDocumento;
	private String origen;
	private Reference origenReference;
	private String destino;
	
	public DocumentoCML(String idDocumento, String origen, String destino) {
		this.idDocumento = idDocumento;
		this.origen = origen;
		this.destino = destino;
	}
	
	public DocumentoCML(String idDocumento, Reference origenReference, String destino) {
		this.idDocumento = idDocumento;
		this.origenReference = origenReference;
		this.destino = destino;
	}
	
	public String getDestino() {
		return destino;
	}
	public void setDestino(String destino) {
		this.destino = destino;
	}
	public String getIdDocumento() {
		return idDocumento;
	}
	public void setIdDocumento(String idDocumento) {
		this.idDocumento = idDocumento;
	}
	public String getOrigen() {
		return origen;
	}
	public void setOrigen(String origen) {
		this.origen = origen;
	}

	public Reference getOrigenReference() {
		return origenReference;
	}

	public void setOrigenReference(Reference origenReference) {
		this.origenReference = origenReference;
	}
}
