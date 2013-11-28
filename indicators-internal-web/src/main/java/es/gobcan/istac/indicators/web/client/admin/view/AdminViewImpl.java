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

import es.gobcan.istac.indicators.web.client.admin.presenter.AdminPresenter;
import es.gobcan.istac.indicators.web.client.admin.presenter.AdminQuantityUnitsTabPresenter.AdminQuantityUnitsTabView;
import es.gobcan.istac.indicators.web.client.admin.view.handlers.AdminUiHandlers;
import es.gobcan.istac.indicators.web.client.indicator.presenter.IndicatorPresenter;

public class AdminViewImpl extends ViewWithUiHandlers<AdminUiHandlers> implements AdminPresenter.AdminView {

    private VLayout    panel;
    private TitleLabel adminLabel;
    private TabSet     tabset;

    private Tab        quantityUnitsTab;

    @Inject
    public AdminViewImpl(AdminQuantityUnitsTabView quantityUnitsView) {

        adminLabel = new TitleLabel();
        adminLabel.setStyleName("sectionTitleLeftMargin");

        quantityUnitsTab = new Tab(getConstants().adminQuantityUnits());
        quantityUnitsTab.setPane((Canvas) quantityUnitsView.asWidget());

        tabset = new TabSet();
        tabset.addTab(quantityUnitsTab);

        panel = new VLayout();
        panel.addMember(adminLabel);
        panel.addMember(tabset);
    }

    @Override
    public void selectQuantityUnitsTab() {
        tabset.selectTab(quantityUnitsTab);
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

}
