package es.gobcan.istac.indicators.web.shared.db;

import com.google.gwt.user.client.rpc.IsSerializable;

public class IndicatorSystem implements IsSerializable {
	private Long id;
	private String name;
	private String status;
	private String version;
	
	public IndicatorSystem(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public IndicatorSystem() {
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
}
