package es.gobcan.istac.indicators.web.client.widgets;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import org.siemac.metamac.web.common.client.widgets.form.InternationalViewMainFormLayout;

import com.smartgwt.client.widgets.events.HasClickHandlers;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

import es.gobcan.istac.indicators.core.enume.domain.IndicatorsSystemProcStatusEnum;
import es.gobcan.istac.indicators.web.client.resources.IndicatorsResources;

public class SystemDiffusionMainFormLayout extends InternationalViewMainFormLayout {

    private ToolStripButton                exportDspl;

    private IndicatorsSystemProcStatusEnum status;

    public SystemDiffusionMainFormLayout() {
        exportDspl = new ToolStripButton(getConstants().systemExportDspl(), IndicatorsResources.RESOURCE.export().getURL());

        toolStrip.addButton(exportDspl);
    }

    public void updatePublishSection(IndicatorsSystemProcStatusEnum status) {
        this.status = status;
    }

    private void updateVisibility() {
        // Hide all buttons
        hideAllPublishButtons();
        // Show buttons depending on the status
        if (IndicatorsSystemProcStatusEnum.PUBLISHED.equals(status)) {
            showExportDsplButton();
        }
    }

    @Override
    public void setViewMode() {
        super.setViewMode();
        updateVisibility();
    }

    public HasClickHandlers getExportDspl() {
        return exportDspl;
    }

    private void hideAllPublishButtons() {
        exportDspl.hide();
    }

    private void showExportDsplButton() {
        exportDspl.show();
    }

}
