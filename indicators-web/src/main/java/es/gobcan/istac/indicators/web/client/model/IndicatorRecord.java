package es.gobcan.istac.indicators.web.client.model;

import com.smartgwt.client.data.Record;

import es.gobcan.istac.indicators.web.client.model.ds.IndicatorsDS;

public class IndicatorRecord extends Record {
	
	public IndicatorRecord(String uuid, String code, String name) {
		setUuid(uuid);
		setName(name);
		setCode(code);
	}
	
	public void setUuid(String uuid) {
		setAttribute(IndicatorsDS.FIELD_UUID, uuid);
	}
	
	public void setName(String name) {
		setAttribute(IndicatorsDS.FIELD_INTERNATIONAL_NAME,name);
	}
	
	public void setCode(String code) {
        setAttribute(IndicatorsDS.FIELD_CODE,code);
    }

	public String getUuid() {
	    return getAttribute(IndicatorsDS.FIELD_UUID);
	}
}
