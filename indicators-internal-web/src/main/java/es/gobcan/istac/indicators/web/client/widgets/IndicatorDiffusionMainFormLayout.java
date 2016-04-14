package es.gobcan.istac.indicators.web.client.widgets;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import org.siemac.metamac.web.common.client.widgets.form.InternationalViewMainFormLayout;

import com.smartgwt.client.widgets.toolbar.ToolStripButton;

import es.gobcan.istac.indicators.web.client.resources.IndicatorsResources;


public class IndicatorDiffusionMainFormLayout extends InternationalViewMainFormLayout {
    
    ToolStripButton previewDataDiffusionToolStripButton;
    
    public IndicatorDiffusionMainFormLayout() {
        super();
        previewDataDiffusionToolStripButton =  new ToolStripButton(getConstants().indicatorPreviewData(), IndicatorsResources.RESOURCE.preview().getURL());
        getToolStrip().addButton(previewDataDiffusionToolStripButton);
    }

    public ToolStripButton getPreviewDataDiffusion() {
        return previewDataDiffusionToolStripButton;
    }

}
