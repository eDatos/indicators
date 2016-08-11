package es.gobcan.istac.indicators.web.client.model;

import com.smartgwt.client.widgets.grid.ListGridRecord;

import es.gobcan.istac.indicators.core.dto.GeographicalGranularityDto;
import es.gobcan.istac.indicators.web.client.model.ds.GeoGranularityDS;

public class GeoGranularityRecord extends ListGridRecord {

    public GeoGranularityRecord(String uuid, String title, String code, GeographicalGranularityDto dto) {
        setUuid(uuid);
        setTitle(title);
        setCode(code);
        setDto(dto);
    }

    public void setUuid(String value) {
        setAttribute(GeoGranularityDS.UUID, value);
    }

    public String getUuid() {
        return getAttributeAsString(GeoGranularityDS.UUID);
    }

    public void setCode(String value) {
        setAttribute(GeoGranularityDS.CODE, value);
    }

    public String getCode() {
        return getAttributeAsString(GeoGranularityDS.CODE);
    }

    public void setTitle(String value) {
        setAttribute(GeoGranularityDS.TITLE, value);
    }

    public String getTitle() {
        return getAttributeAsString(GeoGranularityDS.TITLE);
    }

    public void setDto(GeographicalGranularityDto dto) {
        setAttribute(GeoGranularityDS.DTO, dto);
    }

    public GeographicalGranularityDto getDto() {
        return (GeographicalGranularityDto) getAttributeAsObject(GeoGranularityDS.DTO);
    }
}
