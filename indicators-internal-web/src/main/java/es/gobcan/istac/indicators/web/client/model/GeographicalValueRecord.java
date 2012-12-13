package es.gobcan.istac.indicators.web.client.model;

import org.siemac.metamac.web.common.client.utils.CommonWebUtils;

import com.smartgwt.client.widgets.grid.ListGridRecord;

import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;

public class GeographicalValueRecord extends ListGridRecord {

    public static final String CODE  = "geo-value";
    public static final String TITLE = "geo-value-title";
    public static final String DTO   = "geo-dto";

    public GeographicalValueRecord(GeographicalValueDto geographicalValueDto) {
        setCode(geographicalValueDto.getCode());
        setTitle(CommonWebUtils.getElementName(geographicalValueDto.getCode(), geographicalValueDto.getTitle()));
        setGeographicalValueDto(geographicalValueDto);
    }

    public void setCode(String value) {
        setAttribute(CODE, value);
    }

    public String getGeographicalValue() {
        return getAttributeAsString(CODE);
    }

    public void setTitle(String value) {
        setAttribute(TITLE, value);
    }

    public void setGeographicalValueDto(GeographicalValueDto geographicalValueDto) {
        setAttribute(DTO, geographicalValueDto);
    }

    public GeographicalValueDto getGeographicalValueDto() {
        return (GeographicalValueDto) getAttributeAsObject(DTO);
    }
}
