package es.gobcan.istac.indicators.web.client.model;

import com.smartgwt.client.data.Record;

import es.gobcan.istac.indicators.web.client.model.ds.IndicatorsSystemsDS;

public class IndicatorSystemRecord extends Record {
	
	public IndicatorSystemRecord(String code, String title) {
		setCode(code);
		setTitle(title);
	}
	
	public void setCode(String code) {
		setAttribute(IndicatorsSystemsDS.FIELD_CODE, code);
	}
	
	public void setTitle(String title) {
		setAttribute(IndicatorsSystemsDS.FIELD_INTERNATIONAL_TITLE,title);
	}

}
