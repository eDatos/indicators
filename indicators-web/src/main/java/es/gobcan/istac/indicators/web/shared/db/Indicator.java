package es.gobcan.istac.indicators.web.shared.db;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Indicator implements IsSerializable{
	private Long id;
	private String name;
	private String version;
	
	public Indicator() {
	}
	
	public Indicator(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
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
	
	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	/*TODO: Suggested calculated methods: (USEFUL FOR QUERY BUILDING)
	 * - Get code dimensions for temporal dimension
	 * - Get code dimensions for temporal dimension given a granularity specification
	 * - Same as two above but using spatial instead
	 * - Get all granularity for temporal and spatial dimensions. 
	 */
	
	 
}
