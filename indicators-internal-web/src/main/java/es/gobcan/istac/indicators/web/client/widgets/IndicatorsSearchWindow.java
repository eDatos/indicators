package es.gobcan.istac.indicators.web.client.widgets;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import java.util.List;

import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.widgets.SearchWindow;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.HasClickHandlers;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.web.client.model.IndicatorRecord;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS;
import es.gobcan.istac.indicators.web.client.utils.RecordUtils;
import es.gobcan.istac.indicators.web.shared.criteria.IndicatorCriteria;

public class IndicatorsSearchWindow extends SearchWindow {

    private ButtonItem searchButton;

    public IndicatorsSearchWindow(String windowTitle) {
        super(windowTitle);

        // Form

        TextItem code = new TextItem(IndicatorDS.CODE, getConstants().indicDetailIdentifier());
        code.setWidth(180);
        TextItem title = new TextItem(IndicatorDS.TITLE, getConstants().indicDetailTitle());
        title.setWidth(180);
        searchButton = new ButtonItem("search-ind", MetamacWebCommon.getConstants().search());
        searchButton.setAlign(Alignment.RIGHT);
        form.setFields(code, title, searchButton);

        // ListGrid

        ListGridField fieldCode = new ListGridField(IndicatorDS.CODE, getConstants().indicListHeaderIdentifier());
        fieldCode.setAlign(Alignment.LEFT);
        ListGridField fieldName = new ListGridField(IndicatorDS.TITLE, getConstants().indicListHeaderName());
        ListGridField status = new ListGridField(IndicatorDS.PROC_STATUS, getConstants().indicDetailProcStatus());
        ListGridField updated = new ListGridField(IndicatorDS.NEEDS_UPDATE, getConstants().indicatorUpdateStatus());
        updated.setWidth(140);
        updated.setType(ListGridFieldType.IMAGE);
        updated.setAlign(Alignment.CENTER);
        listGrid.setFields(fieldCode, fieldName, status, updated);
    }

    public HasClickHandlers getSearchButton() {
        return searchButton;
    }

    public IndicatorCriteria getSearchCriteria() {
        IndicatorCriteria criteria = new IndicatorCriteria();
        criteria.setCode(form.getValueAsString(IndicatorDS.CODE));
        criteria.setTitle(form.getValueAsString(IndicatorDS.TITLE));
        return criteria;
    }

    public void setIndicatorList(List<IndicatorDto> indicators) {
        IndicatorRecord[] records = new IndicatorRecord[indicators.size()];
        int index = 0;
        for (IndicatorDto ind : indicators) {
            records[index++] = RecordUtils.getIndicatorRecord(ind);
        }
        listGrid.setData(records);
    }

    public IndicatorDto getSelectedIndicator() {
        ListGridRecord record = listGrid.getSelectedRecord();
        if (record != null && record.getAttributeAsObject(IndicatorDS.DTO) != null) {
            IndicatorDto indicatorDto = (IndicatorDto) record.getAttributeAsObject(IndicatorDS.DTO);
            return indicatorDto;
        }
        return new IndicatorDto();
    }

}