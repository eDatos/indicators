package es.gobcan.istac.indicators.web.client.model;

import com.smartgwt.client.data.Record;

import es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS;

public class IndicatorRecord extends Record {

    public IndicatorRecord(String uuid, String code, String name, String status) {
        setUuid(uuid);
        setName(name);
        setCode(code);
        setProcStatus(status);
    }

    public void setUuid(String uuid) {
        setAttribute(IndicatorDS.UUID, uuid);
    }

    public void setName(String name) {
        setAttribute(IndicatorDS.TITLE, name);
    }

    public void setCode(String code) {
        setAttribute(IndicatorDS.CODE, code);
    }

    public String getUuid() {
        return getAttribute(IndicatorDS.UUID);
    }

    public void setProcStatus(String value) {
        setAttribute(IndicatorDS.PROC_STATUS, value);
    }

}
