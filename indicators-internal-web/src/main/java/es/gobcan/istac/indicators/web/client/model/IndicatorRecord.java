package es.gobcan.istac.indicators.web.client.model;

import org.siemac.metamac.web.common.client.resources.GlobalResources;

import com.smartgwt.client.data.Record;

import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS;

public class IndicatorRecord extends Record {

    public IndicatorRecord(String uuid, String code, String name, String status, Boolean needsUpdate, IndicatorDto indicatorDto) {
        setUuid(uuid);
        setName(name);
        setCode(code);
        setProcStatus(status);
        setNeedsUpdate(needsUpdate);
        setIndicatorDto(indicatorDto);
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

    public void setNeedsUpdate(Boolean value) {
        String imageURL = new String();
        if (value != null && value) {
            // Needs to be updated update
            imageURL = GlobalResources.RESOURCE.errorSmart().getURL();
        } else {
            // Does not need to be updated
            imageURL = GlobalResources.RESOURCE.success().getURL();
        }
        setAttribute(IndicatorDS.NEEDS_UPDATE, imageURL);
    }

    public void setIndicatorDto(IndicatorDto indicatorDto) {
        setAttribute(IndicatorDS.DTO, indicatorDto);
    }

    public IndicatorDto getIndicatorDto() {
        return (IndicatorDto) getAttributeAsObject(IndicatorDS.DTO);
    }

}
