package es.gobcan.istac.indicators.web.client.system.view;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.web.common.client.widgets.BaseCustomListGrid;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.web.client.model.IndicatorSystemRecord;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorsSystemsDS;
import es.gobcan.istac.indicators.web.client.system.presenter.SystemListPresenter.SystemListView;
import es.gobcan.istac.indicators.web.client.system.presenter.SystemListUiHandler;
import es.gobcan.istac.indicators.web.client.utils.RecordUtils;

public class SystemListViewImpl extends ViewImpl implements SystemListView {

    private SystemListUiHandler      uiHandlers;

    private final BaseCustomListGrid indSystemListGrid;
    private VLayout                  panel;

    @Inject
    public SystemListViewImpl() {
        indSystemListGrid = new BaseCustomListGrid();
        IndicatorsSystemsDS datasource = new IndicatorsSystemsDS();
        indSystemListGrid.setDataSource(datasource);
        indSystemListGrid.setUseAllDataSourceFields(false);

        // List
        ListGridField code = new ListGridField(IndicatorsSystemsDS.CODE, getConstants().systemListHeaderIdentifier());
        code.setAlign(Alignment.LEFT);
        ListGridField title = new ListGridField(IndicatorsSystemsDS.TITLE, getConstants().systemListHeaderTitle());
        ListGridField status = new ListGridField(IndicatorsSystemsDS.PROC_STATUS, getConstants().systemDetailProcStatus());
        indSystemListGrid.setFields(code, title, status);

        IndicatorSystemRecord[] records = new IndicatorSystemRecord[0];

        indSystemListGrid.setData(records);

        panel = new VLayout();
        panel.addMember(indSystemListGrid);

        bindEvents();
    }

    private void bindEvents() {
        indSystemListGrid.addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                String code = event.getRecord().getAttribute(IndicatorsSystemsDS.CODE);
                uiHandlers.goToIndicatorsSystem(code);
            }
        });
    }

    @Override
    public void setIndSystemList(List<IndicatorsSystemDto> indicatorsSystemList) {
        IndicatorSystemRecord[] records = new IndicatorSystemRecord[indicatorsSystemList.size()];
        int index = 0;
        for (IndicatorsSystemDto indSys : indicatorsSystemList) {
            records[index++] = RecordUtils.getIndicatorsSystemRecord(indSys);
        }
        indSystemListGrid.setData(records);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setUiHandlers(SystemListUiHandler uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    /* Util */
    public List<String> getCodesFromSelected() {
        List<String> codes = new ArrayList<String>();
        for (ListGridRecord record : indSystemListGrid.getSelectedRecords()) {
            codes.add(record.getAttribute(IndicatorsSystemsDS.CODE));
        }
        return codes;
    }

}
