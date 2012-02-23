package es.gobcan.istac.indicators.web.client.model;

import com.smartgwt.client.data.Record;

import es.gobcan.istac.indicators.web.client.model.ds.IndicatorsSystemsDS;

public class IndicatorSystemRecord extends Record {
	
	public IndicatorSystemRecord(String uuid, String code, String title) {
		setUuid(uuid);
		setCode(code);
		setTitle(title);
	}
	
	public void setUuid(String uuid) {
		setAttribute(IndicatorsSystemsDS.FIELD_UUID, uuid);
	}
	
	public void setCode(String code) {
		setAttribute(IndicatorsSystemsDS.FIELD_CODE, code);
	}
	
	public void setTitle(String title) {
		setAttribute(IndicatorsSystemsDS.FIELD_INTERNATIONAL_TITLE,title);
	}

}
