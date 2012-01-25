package es.gobcan.istac.indicadores.web.shared.db;

import com.google.gwt.user.client.rpc.IsSerializable;


public class IndicatorSystemContent implements IsSerializable {
	
	protected String name;
	
	public IndicatorSystemContent() {
		// TODO Auto-generated constructor stub
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
}
