package es.gobcan.istac.indicators.web.client.model;

import com.smartgwt.client.widgets.grid.ListGridRecord;

import es.gobcan.istac.indicators.core.dto.DataSourceVariableDto;
import es.gobcan.istac.indicators.web.client.model.ds.DataSourceDS;

public class DataSourceVariableRecord extends ListGridRecord {

    public DataSourceVariableRecord(String name, String category, DataSourceVariableDto dataSourceVariableDto) {
        setVariableName(name);
        setVariableCategory(category);
        setDataSourceVariableDto(dataSourceVariableDto);
    }

    public void setVariableName(String value) {
        setAttribute(DataSourceDS.VARIABLE_NAME, value);
    }

    public String getVariableName() {
        return getAttributeAsString(DataSourceDS.VARIABLE_NAME);
    }

    public void setVariableCategory(String value) {
        setAttribute(DataSourceDS.VARIABLE_CATEGORY, value);
    }

    public String getVariableCategory() {
        return getAttributeAsString(DataSourceDS.VARIABLE_CATEGORY);
    }

    public void setDataSourceVariableDto(DataSourceVariableDto dataSourceVariableDto) {
        setAttribute(DataSourceDS.VARIABLE_DTO, dataSourceVariableDto);
    }

    public DataSourceVariableDto getDataSourceVariableDto() {
        return (DataSourceVariableDto) getAttributeAsObject(DataSourceDS.VARIABLE_DTO);
    }

}
