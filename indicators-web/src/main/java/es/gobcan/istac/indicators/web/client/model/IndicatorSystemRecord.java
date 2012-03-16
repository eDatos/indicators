package es.gobcan.istac.indicators.web.client.model;

import com.smartgwt.client.data.Record;

import es.gobcan.istac.indicators.web.client.model.ds.IndicatorsSystemsDS;

public class IndicatorSystemRecord extends Record {
	
	public IndicatorSystemRecord(String uuid, String code, String title, String status) {
		setUuid(uuid);
		setCode(code);
		setTitle(title);
		setProcStatus(status);
	}
	
	public void setUuid(String uuid) {
		setAttribute(IndicatorsSystemsDS.UUID, uuid);
	}
	
	public void setCode(String code) {
		setAttribute(IndicatorsSystemsDS.CODE, code);
	}
	
	public void setTitle(String title) {
		setAttribute(IndicatorsSystemsDS.TITLE,title);
	}

	public void setProcStatus(String value) {
	    setAttribute(IndicatorsSystemsDS.PROC_STATUS, value);
	}
	
}
