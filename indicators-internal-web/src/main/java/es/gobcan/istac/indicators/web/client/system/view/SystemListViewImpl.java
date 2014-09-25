package es.gobcan.istac.indicators.web.client.system.view;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static es.gobcan.istac.indicators.web.client.utils.IndicatorsWebConstants.SYSTEMS_LISTGRID_MAX_RESULTS;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.web.common.client.utils.ListGridUtils;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;
import org.siemac.metamac.web.common.client.widgets.PaginatedCheckListGrid;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Overflow;
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

import es.gobcan.istac.indicators.web.client.model.IndicatorSystemRecord;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorsSystemsDS;
import es.gobcan.istac.indicators.web.client.system.presenter.SystemListPresenter;
import es.gobcan.istac.indicators.web.client.system.presenter.SystemListUiHandler;
import es.gobcan.istac.indicators.web.client.utils.ClientSecurityUtils;
import es.gobcan.istac.indicators.web.client.utils.RecordUtils;
import es.gobcan.istac.indicators.web.client.widgets.SystemListGrid;
import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemSummaryDtoWeb;

public class SystemListViewImpl extends ViewWithUiHandlers<SystemListUiHandler> implements SystemListPresenter.SystemListView {

    private PaginatedCheckListGrid   indSystemListGrid;

    private VLayout                  panel;

    private ToolStripButton          deleteSystemActor;

    private DeleteConfirmationWindow deleteConfirmationWindow;

    @Inject
    public SystemListViewImpl() {
        super();

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

        indSystemListGrid = new PaginatedCheckListGrid(SYSTEMS_LISTGRID_MAX_RESULTS, new SystemListGrid(), new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                getUiHandlers().retrieveSystems(firstResult, maxResults);
            }
        });
        ListGridUtils.setCheckBoxSelectionType(indSystemListGrid.getListGrid());

        indSystemListGrid.getListGrid().addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (ClientSecurityUtils.canDeleteIndicatorsSystem()) {
                    if (indSystemListGrid.getListGrid().getSelectedRecords().length > 0) {
                        deleteSystemActor.show();
                    } else {
                        deleteSystemActor.hide();
                    }
                }
            }
        });
        indSystemListGrid.getListGrid().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getFieldNum() != 0) { // Clicking checkBox will be ignored
                    String code = event.getRecord().getAttribute(IndicatorsSystemsDS.CODE);
                    getUiHandlers().goToIndicatorsSystem(code);
                }
            }
        });

        IndicatorSystemRecord[] records = new IndicatorSystemRecord[0];

        indSystemListGrid.getListGrid().setData(records);

        panel = new VLayout();
        panel.setHeight100();

        VLayout subpanel = new VLayout();
        subpanel.setOverflow(Overflow.SCROLL);
        subpanel.addMember(toolStrip);
        subpanel.addMember(indSystemListGrid);

        panel.addMember(subpanel);

        // Delete confirmation window
        deleteConfirmationWindow = new DeleteConfirmationWindow(getConstants().appConfirmDeleteTitle(), getConstants().systemDeleteConfirm());
        deleteConfirmationWindow.setVisibility(Visibility.HIDDEN);
        deleteConfirmationWindow.getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().deleteIndicatorsSystems(getUuidsFromSelectedSystems());
                deleteConfirmationWindow.hide();
            }
        });
    }

    @Override
    public void setIndSystemList(List<IndicatorsSystemSummaryDtoWeb> indicatorsSystemList, int firstResult, int totalResults) {
        IndicatorSystemRecord[] records = new IndicatorSystemRecord[indicatorsSystemList.size()];
        int index = 0;
        for (IndicatorsSystemSummaryDtoWeb indSys : indicatorsSystemList) {
            records[index++] = RecordUtils.getIndicatorsSystemRecord(indSys);
        }
        indSystemListGrid.getListGrid().setData(records);
        indSystemListGrid.refreshPaginationInfo(firstResult, indicatorsSystemList.size(), totalResults);
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

    public List<String> getUuidsFromSelectedSystems() {
        List<String> uuids = new ArrayList<String>();
        for (ListGridRecord record : indSystemListGrid.getListGrid().getSelectedRecords()) {
            if (!StringUtils.isBlank(record.getAttribute(IndicatorsSystemsDS.UUID))) {
                uuids.add(record.getAttribute(IndicatorsSystemsDS.UUID));
            }
        }
        return uuids;
    }
}
