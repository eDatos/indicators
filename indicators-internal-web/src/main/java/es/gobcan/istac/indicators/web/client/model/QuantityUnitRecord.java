package es.gobcan.istac.indicators.web.client.model;

import com.smartgwt.client.widgets.grid.ListGridRecord;

import es.gobcan.istac.indicators.core.dto.QuantityUnitDto;
import es.gobcan.istac.indicators.web.client.model.ds.QuantityUnitDS;

public class QuantityUnitRecord extends ListGridRecord {

    public QuantityUnitRecord(String uuid, String title, QuantityUnitDto dto) {
        setUuid(uuid);
        setTitle(title);
        setDto(dto);
    }

    public void setUuid(String value) {
        setAttribute(QuantityUnitDS.UUID, value);
    }

    public String getUuid() {
        return getAttributeAsString(QuantityUnitDS.UUID);
    }

    public void setTitle(String value) {
        setAttribute(QuantityUnitDS.TITLE, value);
    }

    public String getTitle() {
        return getAttributeAsString(QuantityUnitDS.TITLE);
    }

    public void setDto(QuantityUnitDto dto) {
        setAttribute(QuantityUnitDS.DTO, dto);
    }

    public QuantityUnitDto getDto() {
        return (QuantityUnitDto) getAttributeAsObject(QuantityUnitDS.DTO);
    }

}
