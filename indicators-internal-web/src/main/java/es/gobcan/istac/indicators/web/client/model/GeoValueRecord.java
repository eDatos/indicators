package es.gobcan.istac.indicators.web.client.model;

import com.smartgwt.client.widgets.grid.ListGridRecord;

import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;
import es.gobcan.istac.indicators.web.client.model.ds.GeoValueDS;

public class GeoValueRecord extends ListGridRecord {

    public GeoValueRecord(String uuid, String title, String code, String granularityTitle, String order, GeographicalValueDto dto) {
        setUuid(uuid);
        setCode(code);
        setTitle(title);
        setOrder(order);
        setGranularityTitle(granularityTitle);
        setDto(dto);
    }

    public void setUuid(String value) {
        setAttribute(GeoValueDS.UUID, value);
    }

    public String getUuid() {
        return getAttributeAsString(GeoValueDS.UUID);
    }

    public void setCode(String value) {
        setAttribute(GeoValueDS.CODE, value);
    }

    public String getCode() {
        return getAttributeAsString(GeoValueDS.CODE);
    }

    public void setOrder(String value) {
        setAttribute(GeoValueDS.ORDER, value);
    }

    public String getOrder() {
        return getAttributeAsString(GeoValueDS.ORDER);
    }

    public void setTitle(String value) {
        setAttribute(GeoValueDS.TITLE, value);
    }

    public String getTitle() {
        return getAttributeAsString(GeoValueDS.TITLE);
    }

    public void setGranularityTitle(String value) {
        setAttribute(GeoValueDS.GRANULARITY_TITLE, value);
    }

    public String getGranularityTitle() {
        return getAttributeAsString(GeoValueDS.GRANULARITY_TITLE);
    }

    public void setDto(GeographicalValueDto dto) {
        setAttribute(GeoValueDS.DTO, dto);
    }

    public GeographicalValueDto getDto() {
        return (GeographicalValueDto) getAttributeAsObject(GeoValueDS.DTO);
    }
}
