package es.gobcan.istac.indicators.web.client.admin.view;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import org.siemac.metamac.web.common.client.widgets.TitleLabel;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;

import es.gobcan.istac.indicators.core.navigation.shared.NameTokens;
import es.gobcan.istac.indicators.web.client.admin.presenter.AdminGeoGranularitiesTabPresenter.AdminGeoGranularitiesTabView;
import es.gobcan.istac.indicators.web.client.admin.presenter.AdminGeoValuesTabPresenter.AdminGeoValuesTabView;
import es.gobcan.istac.indicators.web.client.admin.presenter.AdminPresenter;
import es.gobcan.istac.indicators.web.client.admin.presenter.AdminUnitMultipliersTabPresenter.AdminUnitMultipliersTabView;
import es.gobcan.istac.indicators.web.client.admin.view.handlers.AdminUiHandlers;

public class AdminViewImpl extends ViewWithUiHandlers<AdminUiHandlers> implements AdminPresenter.AdminView {

    private VLayout    panel;
    private TitleLabel adminLabel;
    private TabSet     tabset;

    private Tab        geoGranularitiesTab;
    private Tab        unitMultipliersTab;
    private Tab        geoValuesTab;

    @Inject
    public AdminViewImpl(AdminGeoGranularitiesTabView geoGranularitiesView, AdminUnitMultipliersTabView unitMultipliersTabView, AdminGeoValuesTabView geoValuesTabView) {

        adminLabel = new TitleLabel();
        adminLabel.setStyleName("sectionTitleLeftMargin");

        unitMultipliersTab = new Tab(getConstants().adminUnitMultipliers());
        unitMultipliersTab.setPane((Canvas) unitMultipliersTabView.asWidget());

        geoGranularitiesTab = new Tab(getConstants().adminGeoGranularities());
        geoGranularitiesTab.setPane((Canvas) geoGranularitiesView.asWidget());

        geoValuesTab = new Tab(getConstants().adminGeoValues());
        geoValuesTab.setPane((Canvas) geoValuesTabView.asWidget());

        tabset = new TabSet();
        tabset.addTab(unitMultipliersTab);
        tabset.addTab(geoGranularitiesTab);
        tabset.addTab(geoValuesTab);

        panel = new VLayout();
        panel.addMember(adminLabel);
        panel.addMember(tabset);

        bindEvents();
    }

    private void bindEvents() {
        geoGranularitiesTab.addTabSelectedHandler(new TabSelectedHandler() {

            @Override
            public void onTabSelected(TabSelectedEvent event) {
                getUiHandlers().goToGeoGranularitiesTab();
            }
        });

        geoValuesTab.addTabSelectedHandler(new TabSelectedHandler() {

            @Override
            public void onTabSelected(TabSelectedEvent event) {
                getUiHandlers().goToGeoValuesTab();
            }
        });

        unitMultipliersTab.addTabSelectedHandler(new TabSelectedHandler() {

            @Override
            public void onTabSelected(TabSelectedEvent event) {
                getUiHandlers().goToUnitMultipliersTab();
            }
        });
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setInSlot(Object slot, Widget content) {
        if (slot == AdminPresenter.TYPE_SetContextAreaContentToolBar) {
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
    public void selectTab(String tabName, String geoValue) {
        if (NameTokens.adminGeoValuesPage.equals(tabName)) {
            tabset.selectTab(geoValuesTab);
            getUiHandlers().goToGeoValuesTab(geoValue);

        } else if (NameTokens.adminUnitMultipliersPage.equals(tabName)) {
            tabset.selectTab(unitMultipliersTab);
        }
    }
}
