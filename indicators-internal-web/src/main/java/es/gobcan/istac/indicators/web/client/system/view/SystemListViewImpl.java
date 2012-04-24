package es.gobcan.istac.indicators.web.client.system.view;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.web.common.client.widgets.BaseCustomListGrid;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

import es.gobcan.istac.indicators.web.client.IndicatorsWeb;
import es.gobcan.istac.indicators.web.client.model.IndicatorSystemRecord;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorsSystemsDS;
import es.gobcan.istac.indicators.web.client.system.presenter.SystemListPresenter;
import es.gobcan.istac.indicators.web.client.system.presenter.SystemListPresenter.SystemListView;
import es.gobcan.istac.indicators.web.client.system.presenter.SystemListUiHandler;
import es.gobcan.istac.indicators.web.client.utils.RecordUtils;
import es.gobcan.istac.indicators.web.client.view.PaginationViewImpl;
import es.gobcan.istac.indicators.web.client.widgets.StatusBar;
import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemDtoWeb;

public class SystemListViewImpl extends PaginationViewImpl<SystemListPresenter> implements SystemListView {

    private SystemListUiHandler      uiHandlers;

    private final BaseCustomListGrid indSystemListGrid;
    private VLayout                  panel;

    @Inject
    public SystemListViewImpl(StatusBar statusBar) {
        super(statusBar);

        indSystemListGrid = new BaseCustomListGrid();
        indSystemListGrid.setHeight(680);
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
        panel.addMember(statusBar);

        bindEvents();

        initStatusBar();
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
    public void setIndSystemList(List<IndicatorsSystemDtoWeb> indicatorsSystemList) {
        IndicatorSystemRecord[] records = new IndicatorSystemRecord[indicatorsSystemList.size()];
        int index = 0;
        for (IndicatorsSystemDtoWeb indSys : indicatorsSystemList) {
            records[index++] = RecordUtils.getIndicatorsSystemRecord(indSys);
        }
        indSystemListGrid.setData(records);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setUiHandlers(SystemListPresenter uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public void refreshStatusBar() {
        // update Selected label e.g "0 of 50 selected"
        String selectedLabel = IndicatorsWeb.getMessages().selected(String.valueOf(getNumberSelected()), String.valueOf(getNumberOfElements()));
        getStatusBar().getSelectedLabel().setContents(selectedLabel);

        // update Page number label e.g "Page 1"
        String pageNumberLabel = IndicatorsWeb.getMessages().page(String.valueOf(getPageNumber()));
        getStatusBar().getPageNumberLabel().setContents(pageNumberLabel);
        getStatusBar().getPageNumberLabel().markForRedraw();
    }

    protected void initStatusBar() {

        // "0 of 50 selected"

        getStatusBar().getResultSetFirstButton().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                if (uiHandlers != null) {
                    uiHandlers.onResultSetFirstButtonClicked();
                }
            }
        });

        getStatusBar().getResultSetPreviousButton().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                if (uiHandlers != null) {
                    uiHandlers.onResultSetPreviousButtonClicked();
                }
            }
        });

        // "Page 1"

        getStatusBar().getResultSetNextButton().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                if (uiHandlers != null) {
                    uiHandlers.onResultSetNextButtonClicked();
                }
            }
        });

        getStatusBar().getResultSetLastButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (uiHandlers != null) {
                    uiHandlers.onResultSetLastButtonClicked();
                }
            }
        });
    }

    /* Util */
    public List<String> getCodesFromSelected() {
        List<String> codes = new ArrayList<String>();
        for (ListGridRecord record : indSystemListGrid.getSelectedRecords()) {
            codes.add(record.getAttribute(IndicatorsSystemsDS.CODE));
        }
        return codes;
    }

    @Override
    public void removeSelectedData() {
        this.indSystemListGrid.removeSelectedData();
    }

}
