package es.gobcan.istac.indicators.web.client.main.view;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.events.HasClickHandlers;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import com.smartgwt.client.widgets.toolbar.ToolStripSeparator;

import es.gobcan.istac.indicators.web.client.main.presenter.ToolStripPresenterWidget;
import es.gobcan.istac.indicators.web.client.utils.ClientSecurityUtils;

public class ToolStripViewImpl implements ToolStripPresenterWidget.ToolStripView {

    private ToolStrip       toolStrip;

    private ToolStripButton systemListButton;
    private ToolStripButton indicatorListButton;
    private ToolStripButton adminButton;

    @Inject
    public ToolStripViewImpl() {
        super();
        toolStrip = new ToolStrip();
        toolStrip.setWidth100();
        toolStrip.setAlign(Alignment.LEFT);

        systemListButton = new ToolStripButton(getConstants().appLinksSystemList());
        indicatorListButton = new ToolStripButton(getConstants().appLinksIndList());
        adminButton = new ToolStripButton(getConstants().appLinksAdmin());
        if (!ClientSecurityUtils.canAdministrate()) {
            adminButton.hide();
        }

        // Add buttons to toolStrip
        toolStrip.addButton(systemListButton);
        toolStrip.addMember(new ToolStripSeparator());
        toolStrip.addButton(indicatorListButton);
        toolStrip.addMember(new ToolStripSeparator());
        toolStrip.addButton(adminButton);
    }

    @Override
    public void addToSlot(Object slot, Widget content) {
    }

    @Override
    public Widget asWidget() {
        return toolStrip;
    }

    @Override
    public void removeFromSlot(Object slot, Widget content) {

    }

    @Override
    public void setInSlot(Object slot, Widget content) {
    }

    @Override
    public HasClickHandlers getIndicatorsSystemsButton() {
        return systemListButton;
    }

    @Override
    public HasClickHandlers getIndicatorsButton() {
        return indicatorListButton;
    }

    @Override
    public HasClickHandlers getAdminButton() {
        return adminButton;
    }

}
