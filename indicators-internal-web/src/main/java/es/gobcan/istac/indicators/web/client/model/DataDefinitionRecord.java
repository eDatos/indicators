package es.gobcan.istac.indicators.web.client.model;

import com.smartgwt.client.data.Record;

import es.gobcan.istac.indicators.core.dto.DataDefinitionDto;
import es.gobcan.istac.indicators.web.client.model.ds.DataDefinitionDS;

public class DataDefinitionRecord extends Record {

    public DataDefinitionRecord(String uuid, String name, String pxUri, DataDefinitionDto dataDefinitionDto) {
        setUuid(uuid);
        setName(name);
        setPxUri(pxUri);
        setDataDefinitionDto(dataDefinitionDto);
    }
    public void setUuid(String value) {
        setAttribute(DataDefinitionDS.UUID, value);
    }

    public String getUuid() {
        return getAttributeAsString(DataDefinitionDS.UUID);
    }

    public void setName(String value) {
        setAttribute(DataDefinitionDS.NAME, value);
    }

    public String getName() {
        return getAttributeAsString(DataDefinitionDS.NAME);
    }

    public void setPxUri(String value) {
        setAttribute(DataDefinitionDS.PX_URI, value);
    }

    public String getPxUri() {
        return getAttributeAsString(DataDefinitionDS.PX_URI);
    }

    public void setDataDefinitionDto(DataDefinitionDto dataDefinitionDto) {
        setAttribute(DataDefinitionDS.DTO, dataDefinitionDto);
    }

    public DataDefinitionDto getDataDefinitionDto() {
        return (DataDefinitionDto) getAttributeAsObject(DataDefinitionDS.DTO);
    }

}
