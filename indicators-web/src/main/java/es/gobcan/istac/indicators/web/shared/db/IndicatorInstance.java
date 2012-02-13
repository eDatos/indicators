package es.gobcan.istac.indicators.web.shared.db;

import com.google.gwt.user.client.rpc.IsSerializable;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;

public class IndicatorInstance extends IndicatorSystemContent implements IsSerializable {
	private Long id;
	private IndicatorDto indicator;
	/* TODO: Query must be specified as a set of parameters and values:
	 * Granularity (Spatial and temporal)
	 * Values for spatial and temporal dim
	 */
	
	public IndicatorInstance() {
	}
	
	public IndicatorInstance(Long id, String name, IndicatorDto indicator) {
		super();
		this.id = id;
		this.name = name;
		this.indicator = indicator;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public IndicatorDto getIndicator() {
		return indicator;
	}
	
	public void setIndicator(IndicatorDto indicator) {
		this.indicator = indicator;
	}

}
