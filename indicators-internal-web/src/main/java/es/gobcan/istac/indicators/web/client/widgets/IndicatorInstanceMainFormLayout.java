package es.gobcan.istac.indicators.web.client.widgets;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;

import com.smartgwt.client.widgets.events.HasClickHandlers;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

import es.gobcan.istac.indicators.web.client.resources.IndicatorsResources;

public class IndicatorInstanceMainFormLayout extends InternationalMainFormLayout {

    private ToolStripButton previewData;

    public IndicatorInstanceMainFormLayout() {
        super();
        common();
    }

    public IndicatorInstanceMainFormLayout(boolean canEdit) {
        super(canEdit);
        common();
    }

    public HasClickHandlers getPreviewData() {
        return previewData;
    }

    @Override
    public void setViewMode() {
        super.setViewMode();
        previewData.show();
    }

    @Override
    public void setEditionMode() {
        super.setEditionMode();
        previewData.hide();
    }

    private void common() {
        previewData = new ToolStripButton(getConstants().instancePreviewData(), IndicatorsResources.RESOURCE.preview().getURL());
        toolStrip.addButton(previewData);
    }
}
