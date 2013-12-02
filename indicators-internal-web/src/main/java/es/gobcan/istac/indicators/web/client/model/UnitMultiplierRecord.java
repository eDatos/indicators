package es.gobcan.istac.indicators.web.client.model;

import com.smartgwt.client.widgets.grid.ListGridRecord;

import es.gobcan.istac.indicators.core.dto.UnitMultiplierDto;
import es.gobcan.istac.indicators.web.client.model.ds.UnitMultiplierDS;

public class UnitMultiplierRecord extends ListGridRecord {

    public UnitMultiplierRecord(String uuid, String title, Integer multiplier, UnitMultiplierDto dto) {
        setUuid(uuid);
        setTitle(title);
        setMultiplier(multiplier);
        setDto(dto);
    }

    public void setUuid(String value) {
        setAttribute(UnitMultiplierDS.UUID, value);
    }

    public String getUuid() {
        return getAttributeAsString(UnitMultiplierDS.UUID);
    }

    public void setTitle(String value) {
        setAttribute(UnitMultiplierDS.TITLE, value);
    }

    public String getTitle() {
        return getAttributeAsString(UnitMultiplierDS.TITLE);
    }

    public void setMultiplier(Integer multiplier) {
        setAttribute(UnitMultiplierDS.MULTIPLIER, multiplier);
    }

    public Integer getMultiplier() {
        return getAttributeAsInt(UnitMultiplierDS.MULTIPLIER);
    }

    public void setDto(UnitMultiplierDto dto) {
        setAttribute(UnitMultiplierDS.DTO, dto);
    }

    public UnitMultiplierDto getDto() {
        return (UnitMultiplierDto) getAttributeAsObject(UnitMultiplierDS.DTO);
    }

}
