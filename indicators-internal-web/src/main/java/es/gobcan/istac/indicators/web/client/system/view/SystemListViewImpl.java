package es.gobcan.istac.indicators.web.client.system.view;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.web.common.client.widgets.BaseCustomListGrid;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.HeaderSpan;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridFieldIfFunction;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

import es.gobcan.istac.indicators.web.client.IndicatorsWeb;
import es.gobcan.istac.indicators.web.client.model.IndicatorSystemRecord;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorsSystemsDS;
import es.gobcan.istac.indicators.web.client.system.presenter.SystemListPresenter;
import es.gobcan.istac.indicators.web.client.system.presenter.SystemListPresenter.SystemListView;
import es.gobcan.istac.indicators.web.client.system.presenter.SystemListUiHandler;
import es.gobcan.istac.indicators.web.client.utils.ClientSecurityUtils;
import es.gobcan.istac.indicators.web.client.utils.RecordUtils;
import es.gobcan.istac.indicators.web.client.view.PaginationViewImpl;
import es.gobcan.istac.indicators.web.client.widgets.StatusBar;
import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemSummaryDtoWeb;

public class SystemListViewImpl extends PaginationViewImpl<SystemListPresenter> implements SystemListView {

    private SystemListUiHandler      uiHandlers;

    private final BaseCustomListGrid indSystemListGrid;

    private VLayout                  panel;

    private ToolStripButton          deleteSystemActor;

    private DeleteConfirmationWindow deleteConfirmationWindow;

    @Inject
    public SystemListViewImpl(StatusBar statusBar) {
        super(statusBar);

        // ToolStrip
        ToolStrip toolStrip = new ToolStrip();
        toolStrip.setWidth100();

        deleteSystemActor = new ToolStripButton(getConstants().systemDeleteRelatedData(), RESOURCE.deleteListGrid().getURL());
        deleteSystemActor.setVisibility(Visibility.HIDDEN);
        deleteSystemActor.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                deleteConfirmationWindow.show();
            }
        });

        toolStrip.addButton(deleteSystemActor);

        indSystemListGrid = new BaseCustomListGrid();
        indSystemListGrid.setHeight(680);
        indSystemListGrid.setHeaderHeight(40);
        indSystemListGrid.setDataSource(new IndicatorsSystemsDS());
        indSystemListGrid.setUseAllDataSourceFields(false);
        indSystemListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
        indSystemListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (ClientSecurityUtils.canDeleteIndicatorsSystem()) {
                    if (indSystemListGrid.getSelectedRecords().length > 0) {
                        deleteSystemActor.show();
                    } else {
                        deleteSystemActor.hide();
                    }
                }

                ListGridRecord[] records = event.getSelection();

                setNumberSelected(records.length);

                String selectedLabel = IndicatorsWeb.getMessages().selected(String.valueOf(getNumberSelected()), String.valueOf(getNumberOfElements()));
                SystemListViewImpl.this.statusBar.getSelectedLabel().setContents(selectedLabel);

            }
        });
        indSystemListGrid.addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getFieldNum() != 0) { // Clicking checkBox will be ignored
                    String code = event.getRecord().getAttribute(IndicatorsSystemsDS.CODE);
                    uiHandlers.goToIndicatorsSystem(code);
                }
            }
        });

        // List
        ListGridField uuid = new ListGridField(IndicatorsSystemsDS.UUID, "UUID");
        uuid.setShowIfCondition(new ListGridFieldIfFunction() {

            @Override
            public boolean execute(ListGrid grid, ListGridField field, int fieldNum) {
                return false;
            }
        });
        ListGridField code = new ListGridField(IndicatorsSystemsDS.CODE, getConstants().systemListHeaderIdentifier());
        code.setAlign(Alignment.LEFT);
        ListGridField title = new ListGridField(IndicatorsSystemsDS.TITLE, getConstants().systemListHeaderTitle());
        ListGridField version = new ListGridField(IndicatorsSystemsDS.VERSION, getConstants().systemDetailVersion());
        ListGridField status = new ListGridField(IndicatorsSystemsDS.PROC_STATUS, getConstants().systemDetailProcStatus());
        ListGridField diffusionVersion = new ListGridField(IndicatorsSystemsDS.VERSION_DIFF, getConstants().systemDetailVersion());
        ListGridField diffusionStatus = new ListGridField(IndicatorsSystemsDS.PROC_STATUS_DIFF, getConstants().systemDetailProcStatus());
        indSystemListGrid.setFields(uuid, code, title, version, status, diffusionVersion, diffusionStatus);
        indSystemListGrid.setHeaderSpans(new HeaderSpan(getConstants().system(), new String[]{IndicatorsSystemsDS.CODE, IndicatorsSystemsDS.TITLE}), new HeaderSpan(getConstants()
                .systemProductionEnvironment(), new String[]{IndicatorsSystemsDS.VERSION, IndicatorsSystemsDS.PROC_STATUS}), new HeaderSpan(getConstants().systemDiffusionEnvironment(), new String[]{
                IndicatorsSystemsDS.VERSION_DIFF, IndicatorsSystemsDS.PROC_STATUS_DIFF}));

        IndicatorSystemRecord[] records = new IndicatorSystemRecord[0];

        indSystemListGrid.setData(records);

        panel = new VLayout();
        panel.addMember(toolStrip);
        panel.addMember(indSystemListGrid);
        panel.addMember(statusBar);

        // Delete confirmation window
        deleteConfirmationWindow = new DeleteConfirmationWindow(getConstants().appConfirmDeleteTitle(), getConstants().systemDeleteConfirm());
        deleteConfirmationWindow.setVisibility(Visibility.HIDDEN);
        deleteConfirmationWindow.getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.deleteIndicatorsSystems(getUuidsFromSelectedSystems());
                deleteConfirmationWindow.hide();
            }
        });

        initStatusBar();
    }

    @Override
    public void setIndSystemList(List<IndicatorsSystemSummaryDtoWeb> indicatorsSystemList) {
        IndicatorSystemRecord[] records = new IndicatorSystemRecord[indicatorsSystemList.size()];
        int index = 0;
        for (IndicatorsSystemSummaryDtoWeb indSys : indicatorsSystemList) {
            records[index++] = RecordUtils.getIndicatorsSystemRecord(indSys);
        }
        indSystemListGrid.setData(records);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setInSlot(Object slot, Widget content) {
        if (slot == SystemListPresenter.TYPE_SetContextAreaContentToolBar) {
            if (content != null) {
                panel.addMember(content, 0);
            }
        } else {
            // To support inheritance in your views it is good practice to call super.setInSlot when you can't handle the call.
            // Who knows, maybe the parent class knows what to do with this slot.
            super.setInSlot(slot, content);
        }
    }

    @Override
    public void setUiHandlers(SystemListPresenter uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    @Override
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

            @Override
            public void onClick(ClickEvent event) {
                if (uiHandlers != null) {
                    uiHandlers.onResultSetFirstButtonClicked();
                }
            }
        });

        getStatusBar().getResultSetPreviousButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (uiHandlers != null) {
                    uiHandlers.onResultSetPreviousButtonClicked();
                }
            }
        });

        // "Page 1"

        getStatusBar().getResultSetNextButton().addClickHandler(new ClickHandler() {

            @Override
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

    public List<String> getUuidsFromSelectedSystems() {
        List<String> uuids = new ArrayList<String>();
        for (ListGridRecord record : indSystemListGrid.getSelectedRecords()) {
            if (!StringUtils.isBlank(record.getAttribute(IndicatorsSystemsDS.UUID))) {
                uuids.add(record.getAttribute(IndicatorsSystemsDS.UUID));
            }
        }
        return uuids;
    }

    @Override
    public void removeSelectedData() {
        this.indSystemListGrid.removeSelectedData();
    }

    @Override
    public void onIndicatorsSystemsDeleted() {
        indSystemListGrid.deselectAllRecords();
    }
}
