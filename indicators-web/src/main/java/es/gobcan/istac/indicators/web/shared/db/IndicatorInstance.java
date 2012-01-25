package es.gobcan.istac.indicators.web.shared.db;

import com.google.gwt.user.client.rpc.IsSerializable;

public class IndicatorInstance extends IndicatorSystemContent implements IsSerializable {
	private Long id;
	private Indicator indicator;
	/* TODO: Query must be specified as a set of parameters and values:
	 * Granularity (Spatial and temporal)
	 * Values for spatial and temporal dim
	 */
	
	public IndicatorInstance() {
		// TODO Auto-generated constructor stub
	}
	
	public IndicatorInstance(Long id, String name, Indicator ind) {
		super();
		this.id = id;
		this.name = name;
		this.indicator = ind;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Indicator getIndicator() {
		return indicator;
	}
	
	public void setIndicator(Indicator indicator) {
		this.indicator = indicator;
	}

}
