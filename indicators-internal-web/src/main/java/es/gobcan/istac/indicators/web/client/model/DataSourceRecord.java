package es.gobcan.istac.indicators.web.client.model;

import com.smartgwt.client.widgets.grid.ListGridRecord;

import es.gobcan.istac.indicators.core.dto.DataSourceDto;
import es.gobcan.istac.indicators.web.client.model.ds.DataSourceDS;

public class DataSourceRecord extends ListGridRecord {

    public DataSourceRecord(String uuid, String query, String px, DataSourceDto dataSourceDto) {
        setUuid(uuid);
        setQuery(query);
        setPx(px);
        setDataSourceDto(dataSourceDto);
    }

    public void setUuid(String value) {
        setAttribute(DataSourceDS.UUID, value);
    }

    public String getUuid() {
        return getAttributeAsString(DataSourceDS.UUID);
    }

    public void setQuery(String value) {
        setAttribute(DataSourceDS.QUERY, value);
    }

    public String getQuery() {
        return getAttributeAsString(DataSourceDS.QUERY);
    }

    public void setPx(String value) {
        setAttribute(DataSourceDS.PX, value);
    }

    public String getPx() {
        return getAttributeAsString(DataSourceDS.PX);
    }

    public void setDataSourceDto(DataSourceDto dataSourceDto) {
        setAttribute(DataSourceDS.DTO, dataSourceDto);
    }

    public DataSourceDto getDataSourceDto() {
        return (DataSourceDto) getAttributeAsObject(DataSourceDS.DTO);
    }

}
