package es.gobcan.istac.indicators.web.client.indicator.view;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.web.common.client.utils.ListGridUtils;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;
import org.siemac.metamac.web.common.client.widgets.PaginatedCheckListGrid;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
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
import es.gobcan.istac.indicators.web.client.indicator.presenter.IndicatorListPresenter;
import es.gobcan.istac.indicators.web.client.indicator.presenter.IndicatorListUiHandler;
import es.gobcan.istac.indicators.web.client.indicator.widgets.NewIndicatorWindow;
import es.gobcan.istac.indicators.web.client.model.IndicatorRecord;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS;
import es.gobcan.istac.indicators.web.client.resources.IndicatorsResources;
import es.gobcan.istac.indicators.web.client.utils.ClientSecurityUtils;
import es.gobcan.istac.indicators.web.client.utils.IndicatorsWebConstants;
import es.gobcan.istac.indicators.web.client.utils.RecordUtils;
import es.gobcan.istac.indicators.web.client.widgets.IndicatorListGrid;
import es.gobcan.istac.indicators.web.client.widgets.IndicatorsSearchSectionStack;
import es.gobcan.istac.indicators.web.shared.criteria.IndicatorCriteria;

public class IndicatorListViewImpl extends ViewWithUiHandlers<IndicatorListUiHandler> implements IndicatorListPresenter.IndicatorListView {

    private VLayout                      panel;

    private ToolStripButton              newIndicatorActor;
    private ToolStripButton              deleteIndicatorActor;
    private ToolStripButton              exportIndicatorsButton;

    private ToolStripButton              enableNotifyPopulationErrors;
    private ToolStripButton              disableNotifyPopulationErrors;

    private IndicatorsSearchSectionStack searchSectionStack;

    private PaginatedCheckListGrid       indicatorList;

    private DeleteConfirmationWindow     deleteConfirmationWindow;

    private NewIndicatorWindow           window;

    @Inject
    public IndicatorListViewImpl() {
        super();

        // ToolStrip
        ToolStrip toolStrip = new ToolStrip();
        toolStrip.setWidth100();

        newIndicatorActor = new ToolStripButton(getConstants().indicNew(), RESOURCE.newListGrid().getURL());
        newIndicatorActor.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                window = new NewIndicatorWindow(getConstants().indicCreateTitle());
                getUiHandlers().retrieveSubjectsListForCreateIndicator();
                window.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (window.validateForm()) {
                            getUiHandlers().createIndicator(window.getNewIndicatorDto());
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

        exportIndicatorsButton = new ToolStripButton(getConstants().indicatorsExport(), org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.exportResource().getURL());
        exportIndicatorsButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().exportIndicators(getIndicatorCriteria());
            }
        });

        enableNotifyPopulationErrors = new ToolStripButton(getConstants().indicatorEnableNotifyPopulationErrors(), IndicatorsResources.RESOURCE.enableNotification().getURL());
        enableNotifyPopulationErrors.setVisibility(Visibility.HIDDEN);
        enableNotifyPopulationErrors.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().enableNotifyPopulationErrors(getUuidsFromSelected());
            }
        });

        disableNotifyPopulationErrors = new ToolStripButton(getConstants().indicatorDisableNotifyPopulationErrors(), IndicatorsResources.RESOURCE.disableNotification().getURL());
        disableNotifyPopulationErrors.setVisibility(Visibility.HIDDEN);
        disableNotifyPopulationErrors.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().disableNotifyPopulationErrors(getUuidsFromSelected());
            }
        });

        toolStrip.addButton(newIndicatorActor);
        toolStrip.addButton(deleteIndicatorActor);
        toolStrip.addButton(exportIndicatorsButton);
        toolStrip.addButton(enableNotifyPopulationErrors);
        toolStrip.addButton(disableNotifyPopulationErrors);

        // Search

        searchSectionStack = new IndicatorsSearchSectionStack();

        // List

        createIndicatorList();

        panel = new VLayout();
        panel.addMember(toolStrip);
        panel.addMember(searchSectionStack);
        panel.addMember(indicatorList);

        // Delete confirmation window
        deleteConfirmationWindow = new DeleteConfirmationWindow(getConstants().appConfirmDeleteTitle(), getConstants().indicDeleteConfirm());
        deleteConfirmationWindow.setVisibility(Visibility.HIDDEN);
        deleteConfirmationWindow.getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().deleteIndicators(getUuidsFromSelected());
                deleteConfirmationWindow.hide();
            }
        });
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setInSlot(Object slot, Widget content) {
        if (slot == IndicatorListPresenter.TYPE_SetContextAreaContentToolBar) {
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
    public void setIndicatorList(List<IndicatorSummaryDto> indicators, int firstResult, int totalResults) {
        indicatorList.getListGrid().resetSort();
        IndicatorRecord[] records = new IndicatorRecord[indicators.size()];
        int index = 0;
        for (IndicatorSummaryDto ind : indicators) {
            records[index++] = RecordUtils.getIndicatorRecord(ind);
        }
        indicatorList.getListGrid().setData(records);
        indicatorList.refreshPaginationInfo(firstResult, indicators.size(), totalResults);
    }

    @Override
    public void setUiHandlers(IndicatorListUiHandler uiHandlers) {
        super.setUiHandlers(uiHandlers);
        searchSectionStack.setUiHandlers(uiHandlers);
    }

    public List<String> getUuidsFromSelected() {
        List<String> codes = new ArrayList<String>();
        for (ListGridRecord record : indicatorList.getListGrid().getSelectedRecords()) {
            codes.add(record.getAttribute(IndicatorDS.UUID));
        }
        return codes;
    }

    @Override
    public void setSubjectsForCreateIndicator(List<SubjectDto> subjectDtos) {
        if (window != null) {
            window.setSubjetcs(subjectDtos);
        }
    }

    @Override
    public void setSubjectsForSearchIndicator(List<SubjectDto> subjectDtos) {
        searchSectionStack.setSubjects(subjectDtos);
    }

    @Override
    public void clearSearchSection() {
        searchSectionStack.clearSearchSection();
    }

    @Override
    public IndicatorCriteria getIndicatorCriteria() {
        return searchSectionStack.getIndicatorCriteria();
    }

    private void createIndicatorList() {
        indicatorList = new PaginatedCheckListGrid(IndicatorsWebConstants.LISTGRID_MAX_RESULTS, new IndicatorListGrid(), new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                IndicatorCriteria criteria = getIndicatorCriteria();
                criteria.setFirstResult(firstResult);
                criteria.setMaxResults(maxResults);
                getUiHandlers().retrieveIndicators(criteria);
            }
        });
        ListGridUtils.setCheckBoxSelectionType(indicatorList.getListGrid());

        indicatorList.getListGrid().addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                ListGridRecord[] selectedRecords = indicatorList.getListGrid().getSelectedRecords();

                showDeleteButton(selectedRecords);
                showEnableNotifyPopulationErrorButton(selectedRecords);
                showDisableNotifyPopulationErrorButton(selectedRecords);

            }
        });

        indicatorList.getListGrid().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getFieldNum() != 0) { // Clicking checkBox will be ignored
                    String code = event.getRecord().getAttribute(IndicatorDS.CODE);
                    getUiHandlers().goToIndicator(code);
                }
            }
        });
        indicatorList.setHeight100();
    }

    private void showDisableNotifyPopulationErrorButton(ListGridRecord[] selectedRecords) {
        boolean canBeShownDisableNotifyPopulationErrorButton = Boolean.FALSE;

        if (selectedRecords != null && selectedRecords.length > 0) {
            for (ListGridRecord record : selectedRecords) {
                if (record.getAttributeAsBoolean(IndicatorDS.NOTIFY_POPULATION_ERRORS) && ClientSecurityUtils.canDisableNotifyPopulationErrors(getDtoFromRecord(record))) {
                    canBeShownDisableNotifyPopulationErrorButton = Boolean.TRUE;
                } else {
                    canBeShownDisableNotifyPopulationErrorButton = Boolean.FALSE;
                    break;
                }
            }
        }

        if (canBeShownDisableNotifyPopulationErrorButton) {
            disableNotifyPopulationErrors.show();
        } else {
            disableNotifyPopulationErrors.hide();
        }
    }

    private void showEnableNotifyPopulationErrorButton(ListGridRecord[] selectedRecords) {
        boolean canBeShownEnableNotifyPopulationError = Boolean.FALSE;

        if (selectedRecords != null && selectedRecords.length > 0) {
            for (ListGridRecord record : selectedRecords) {
                if (!record.getAttributeAsBoolean(IndicatorDS.NOTIFY_POPULATION_ERRORS) && ClientSecurityUtils.canEnableNotifyPopulationErrors(getDtoFromRecord(record))) {
                    canBeShownEnableNotifyPopulationError = Boolean.TRUE;
                } else {
                    canBeShownEnableNotifyPopulationError = Boolean.FALSE;
                    break;
                }
            }
        }

        if (canBeShownEnableNotifyPopulationError) {
            enableNotifyPopulationErrors.show();
        } else {
            enableNotifyPopulationErrors.hide();
        }
    }

    private void showDeleteButton(ListGridRecord[] selectedRecords) {
        boolean canBeDeleted = Boolean.FALSE;
        if (selectedRecords != null && selectedRecords.length > 0) {
            for (ListGridRecord record : selectedRecords) {
                if (ClientSecurityUtils.canDeleteIndicator(getDtoFromRecord(record))) {
                    canBeDeleted = Boolean.TRUE;
                } else {
                    canBeDeleted = Boolean.FALSE;
                    break;
                }
            }
        }

        if (canBeDeleted) {
            deleteIndicatorActor.show();
        } else {
            deleteIndicatorActor.hide();
        }
    }

    private IndicatorSummaryDto getDtoFromRecord(ListGridRecord record) {
        IndicatorRecord indicatorRecord = (IndicatorRecord) record;
        return (IndicatorSummaryDto) indicatorRecord.getIndicatorDto();
    }
}
