package es.gobcan.istac.indicators.web.client.model;

import org.siemac.metamac.web.common.client.utils.CommonWebUtils;

import com.smartgwt.client.widgets.grid.ListGridRecord;

import es.gobcan.istac.indicators.core.dto.TimeValueDto;

public class TimeValueRecord extends ListGridRecord {

    public static final String TIME_VALUE = "time-value";
    public static final String TITLE      = "time-value-title";
    public static final String DTO        = "time-dto";

    public TimeValueRecord(TimeValueDto timeValueDto) {
        setTimeValue(timeValueDto.getTimeValue());
        setTitle(CommonWebUtils.getElementName(timeValueDto.getTimeValue(), timeValueDto.getTitle()));
        setTimeValueDto(timeValueDto);
    }

    public void setTimeValue(String value) {
        setAttribute(TIME_VALUE, value);
    }

    public String getTimeValue() {
        return getAttributeAsString(TIME_VALUE);
    }

    public void setTitle(String value) {
        setAttribute(TITLE, value);
    }

    public void setTimeValueDto(TimeValueDto timeValueDto) {
        setAttribute(DTO, timeValueDto);
    }

    public TimeValueDto getTimeValueDto() {
        return (TimeValueDto) getAttributeAsObject(DTO);
    }
}
