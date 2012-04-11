package es.gobcan.istac.indicators.web.client.widgets;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;

import com.smartgwt.client.widgets.events.HasClickHandlers;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

import es.gobcan.istac.indicators.web.client.resources.IndicatorsResources;

public class DataSourceMainFormLayout extends InternationalMainFormLayout {

    private ToolStripButton editQueryButton;

    public DataSourceMainFormLayout() {
        super();

        editQueryButton = new ToolStripButton(getConstants().editQuery(), IndicatorsResources.RESOURCE.reset().getURL());

        toolStrip.addSeparator();
        toolStrip.addButton(editQueryButton);
    }

    public HasClickHandlers getEditQueryToolStripButton() {
        return editQueryButton;
    }

}
