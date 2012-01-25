package es.gobcan.istac.indicadores.web.client.model;

import com.smartgwt.client.data.Record;

public class IndicatorRecord extends Record {
	public static final String IDENTIFIER = "identifier";
	public static final String NAME = "name";
	
	public IndicatorRecord(Long identifier, String name) {
		setIdentifier(identifier);
		setName(name);
	}
	
	public void setIdentifier(Long id) {
		setAttribute(IDENTIFIER, id);
	}
	
	public void setName(String name) {
		setAttribute(NAME,name);
	}

}
