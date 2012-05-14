package es.gobcan.istac.indicators.web.client.widgets;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import java.util.LinkedHashMap;
import java.util.List;

import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.widgets.SearchWindow;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredSelectItem;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.events.HasClickHandlers;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

import es.gobcan.istac.indicators.core.dto.DataDefinitionDto;
import es.gobcan.istac.indicators.web.client.model.DataDefinitionRecord;
import es.gobcan.istac.indicators.web.client.model.ds.DataDefinitionDS;
import es.gobcan.istac.indicators.web.client.model.ds.DataSourceDS;
import es.gobcan.istac.indicators.web.client.utils.RecordUtils;

public class DataDefinitionsSearchWindow extends SearchWindow {

    private ButtonItem searchButton;

    public DataDefinitionsSearchWindow(String windowTitle) {
        super(windowTitle);

        // Form

        RequiredSelectItem operationCode = new RequiredSelectItem(DataSourceDS.SOURCE_SURVEY_CODE, getConstants().dataSourceSurveyCode());
        searchButton = new ButtonItem("search-ind", MetamacWebCommon.getConstants().search());
        searchButton.setWidth(180);
        searchButton.setAlign(Alignment.RIGHT);

        form.setNumCols(4);
        form.setFields(operationCode, searchButton);

        // ListGrid

        ListGridField name = new ListGridField(DataDefinitionDS.NAME, getConstants().dataSourceSurveyTitle());
        name.setAlign(Alignment.LEFT);
        ListGridField pxUri = new ListGridField(DataDefinitionDS.PX_URI, getConstants().dataSourcePx());
        listGrid.setFields(name, pxUri);
    }

    public boolean validateForm() {
        return form.validate();
    }

    public HasClickHandlers getSearchButton() {
        return searchButton;
    }

    public String getSelectedOperationCode() {
        return form.getValueAsString(DataSourceDS.SOURCE_SURVEY_CODE);
    }

    public void setDataDefinitionsOperationCodes(List<String> operationCodes) {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        for (String code : operationCodes) {
            valueMap.put(code, code);
        }
        form.getItem(DataSourceDS.SOURCE_SURVEY_CODE).setValueMap(valueMap);
    }

    public void setDataDefinitionList(List<DataDefinitionDto> dataDefinitionDtos) {
        DataDefinitionRecord[] records = new DataDefinitionRecord[dataDefinitionDtos.size()];
        int index = 0;
        for (DataDefinitionDto dataDefinition : dataDefinitionDtos) {
            records[index++] = RecordUtils.getDataDefinitionRecord(dataDefinition);
        }
        listGrid.setData(records);
    }

    public DataDefinitionDto getSelectedDataDefinitionDto() {
        ListGridRecord record = listGrid.getSelectedRecord();
        if (record != null && record.getAttributeAsObject(DataDefinitionDS.DTO) != null) {
            DataDefinitionDto deDataDefinitionDto = (DataDefinitionDto) record.getAttributeAsObject(DataDefinitionDS.DTO);
            return deDataDefinitionDto;
        }
        return new DataDefinitionDto();
    }

}
