package es.gobcan.istac.indicators.web.client.indicator.view;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.web.common.client.widgets.BaseCustomListGrid;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.HeaderSpan;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

import es.gobcan.istac.indicators.core.dto.IndicatorSummaryDto;
import es.gobcan.istac.indicators.core.dto.SubjectDto;
import es.gobcan.istac.indicators.web.client.IndicatorsWeb;
import es.gobcan.istac.indicators.web.client.indicator.presenter.IndicatorListPresenter;
import es.gobcan.istac.indicators.web.client.indicator.presenter.IndicatorListUiHandler;
import es.gobcan.istac.indicators.web.client.indicator.widgets.NewIndicatorWindow;
import es.gobcan.istac.indicators.web.client.model.IndicatorRecord;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS;
import es.gobcan.istac.indicators.web.client.utils.ClientSecurityUtils;
import es.gobcan.istac.indicators.web.client.utils.RecordUtils;
import es.gobcan.istac.indicators.web.client.view.PaginationViewImpl;
import es.gobcan.istac.indicators.web.client.widgets.StatusBar;

public class IndicatorListViewImpl extends PaginationViewImpl<IndicatorListPresenter> implements IndicatorListPresenter.IndicatorListView {

    private IndicatorListUiHandler   uiHandlers;

    private VLayout                  panel;

    private ToolStripButton          newIndicatorActor;
    private ToolStripButton          deleteIndicatorActor;

    private BaseCustomListGrid       indicatorList;

    private DeleteConfirmationWindow deleteConfirmationWindow;

    private NewIndicatorWindow       window;

    @Inject
    public IndicatorListViewImpl(StatusBar statusBar) {
        super(statusBar);

        // ToolStrip
        ToolStrip toolStrip = new ToolStrip();
        toolStrip.setWidth100();

        newIndicatorActor = new ToolStripButton(getConstants().indicNew(), RESOURCE.newListGrid().getURL());
        newIndicatorActor.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                window = new NewIndicatorWindow(getConstants().indicCreateTitle());
                uiHandlers.retrieveSubjectsList();
                window.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (window.validateForm()) {
                            uiHandlers.createIndicator(window.getNewIndicatorDto());
                            window.destroy();
                        }
                    }
                });
            }
        });
        newIndicatorActor.setVisibility(ClientSecurityUtils.canCreateIndicator() ? Visibility.VISIBLE : Visibility.HIDDEN);

        deleteIndicatorActor = new ToolStripButton(getConstants().indicDelete(), RESOURCE.deleteListGrid().getURL());
        deleteIndicatorActor.setVisibility(Visibility.HIDDEN);
        deleteIndicatorActor.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                deleteConfirmationWindow.show();
            }
        });

        toolStrip.addButton(newIndicatorActor);
        toolStrip.addButton(deleteIndicatorActor);

        indicatorList = new BaseCustomListGrid();
        indicatorList.setHeight(680);
        indicatorList.setHeaderHeight(40);
        indicatorList.setDataSource(new IndicatorDS());
        indicatorList.setUseAllDataSourceFields(false);
        indicatorList.setSelectionAppearance(SelectionAppearance.CHECKBOX);
        indicatorList.addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (ClientSecurityUtils.canDeleteIndicator()) {
                    if (indicatorList.getSelectedRecords().length > 0) {
                        deleteIndicatorActor.show();
                    } else {
                        deleteIndicatorActor.hide();
                    }
                }

                ListGridRecord[] records = event.getSelection();

                setNumberSelected(records.length);

                String selectedLabel = IndicatorsWeb.getMessages().selected(String.valueOf(getNumberSelected()), String.valueOf(getNumberOfElements()));
                IndicatorListViewImpl.this.statusBar.getSelectedLabel().setContents(selectedLabel);

            }
        });
        indicatorList.addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getFieldNum() != 0) { // Clicking checkBox will be ignored
                    String code = event.getRecord().getAttribute(IndicatorDS.CODE);
                    uiHandlers.goToIndicator(code);
                }
            }
        });

        ListGridField fieldCode = new ListGridField(IndicatorDS.CODE, getConstants().indicListHeaderIdentifier());
        fieldCode.setAlign(Alignment.LEFT);
        ListGridField fieldName = new ListGridField(IndicatorDS.TITLE, getConstants().indicListHeaderName());
        ListGridField version = new ListGridField(IndicatorDS.VERSION_NUMBER, getConstants().indicDetailVersion());
        ListGridField status = new ListGridField(IndicatorDS.PROC_STATUS, getConstants().indicDetailProcStatus());
        ListGridField needsUpdate = new ListGridField(IndicatorDS.NEEDS_UPDATE, getConstants().indicatorUpdateStatus());
        needsUpdate.setWidth(140);
        needsUpdate.setType(ListGridFieldType.IMAGE);
        needsUpdate.setAlign(Alignment.CENTER);
        ListGridField diffusionVersion = new ListGridField(IndicatorDS.VERSION_NUMBER_DIFF, getConstants().indicDetailVersion());
        ListGridField diffusionStatus = new ListGridField(IndicatorDS.PROC_STATUS_DIFF, getConstants().indicDetailProcStatus());
        ListGridField diffusionNeedsUpdate = new ListGridField(IndicatorDS.NEEDS_UPDATE_DIFF, getConstants().indicatorUpdateStatus());
        diffusionNeedsUpdate.setWidth(140);
        diffusionNeedsUpdate.setType(ListGridFieldType.IMAGE);
        diffusionNeedsUpdate.setAlign(Alignment.CENTER);
        indicatorList.setFields(fieldCode, fieldName, version, status, needsUpdate, diffusionVersion, diffusionStatus, diffusionNeedsUpdate);
        indicatorList.setHeaderSpans(new HeaderSpan(getConstants().indicator(), new String[]{IndicatorDS.CODE, IndicatorDS.TITLE}), new HeaderSpan(getConstants().indicatorProductionEnvironment(),
                new String[]{IndicatorDS.VERSION_NUMBER, IndicatorDS.PROC_STATUS, IndicatorDS.NEEDS_UPDATE}), new HeaderSpan(getConstants().indicatorDiffusionEnvironment(), new String[]{
                IndicatorDS.VERSION_NUMBER_DIFF, IndicatorDS.PROC_STATUS_DIFF, IndicatorDS.NEEDS_UPDATE_DIFF}));

        panel = new VLayout();
        panel.addMember(toolStrip);
        panel.addMember(indicatorList);
        panel.addMember(statusBar);

        // Delete confirmation window
        deleteConfirmationWindow = new DeleteConfirmationWindow(getConstants().appConfirmDeleteTitle(), getConstants().indicDeleteConfirm());
        deleteConfirmationWindow.setVisibility(Visibility.HIDDEN);
        deleteConfirmationWindow.getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.deleteIndicators(getUuidsFromSelected());
                deleteConfirmationWindow.hide();
            }
        });

        initStatusBar();

    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setIndicatorList(List<IndicatorSummaryDto> indicators) {
        IndicatorRecord[] records = new IndicatorRecord[indicators.size()];
        int index = 0;
        for (IndicatorSummaryDto ind : indicators) {
            records[index++] = RecordUtils.getIndicatorRecord(ind);
        }
        indicatorList.setData(records);
    }

    @Override
    public void setUiHandlers(IndicatorListPresenter uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public List<String> getUuidsFromSelected() {
        List<String> codes = new ArrayList<String>();
        for (ListGridRecord record : indicatorList.getSelectedRecords()) {
            codes.add(record.getAttribute(IndicatorDS.UUID));
        }
        return codes;
    }

    @Override
    public void setSubjects(List<SubjectDto> subjectDtos) {
        if (window != null) {
            window.setSubjetcs(subjectDtos);
        }
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

    @Override
    public void removeSelectedData() {
        this.indicatorList.removeSelectedData();
    }

}
