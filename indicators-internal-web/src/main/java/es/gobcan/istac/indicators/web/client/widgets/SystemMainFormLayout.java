package es.gobcan.istac.indicators.web.client.widgets;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import org.siemac.metamac.web.common.client.widgets.form.InternationalViewMainFormLayout;

import com.smartgwt.client.widgets.events.HasClickHandlers;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

import es.gobcan.istac.indicators.core.enume.domain.IndicatorsSystemProcStatusEnum;
import es.gobcan.istac.indicators.web.client.resources.IndicatorsResources;
import es.gobcan.istac.indicators.web.client.utils.ClientSecurityUtils;

public class SystemMainFormLayout extends InternationalViewMainFormLayout {

    private PublishToolStripButton         productionValidation;
    private PublishToolStripButton         diffusionValidation;
    private PublishToolStripButton         rejectValidation;
    private PublishToolStripButton         publish;
    private PublishToolStripButton         archive;
    private PublishToolStripButton         versioning;
    private ToolStripButton                exportDspl;

    private IndicatorsSystemProcStatusEnum status;

    private String                         indicatorsSystemCode;

    public SystemMainFormLayout() {
        productionValidation = new PublishToolStripButton(getConstants().indicatorSendToProductionValidation(), IndicatorsResources.RESOURCE.validateProduction().getURL());
        diffusionValidation = new PublishToolStripButton(getConstants().indicatorSendToDiffusionValidation(), IndicatorsResources.RESOURCE.validateDifussion().getURL());
        publish = new PublishToolStripButton(getConstants().indicatorPublish(), IndicatorsResources.RESOURCE.publish().getURL());
        archive = new PublishToolStripButton(getConstants().indicatorArchive(), IndicatorsResources.RESOURCE.archive().getURL());
        rejectValidation = new PublishToolStripButton(getConstants().indicatorRejectValidation(), IndicatorsResources.RESOURCE.reject().getURL());
        versioning = new PublishToolStripButton(getConstants().systemVersioning(), IndicatorsResources.RESOURCE.version().getURL());
        exportDspl = new ToolStripButton(getConstants().systemExportDspl(), IndicatorsResources.RESOURCE.export().getURL());

        toolStrip.addButton(productionValidation);
        toolStrip.addButton(diffusionValidation);
        toolStrip.addButton(publish);
        toolStrip.addButton(archive);
        toolStrip.addButton(rejectValidation);
        toolStrip.addButton(versioning);
        toolStrip.addButton(exportDspl);
    }

    public void updatePublishSection(IndicatorsSystemProcStatusEnum status) {
        this.status = status;
    }

    private void updateVisibility() {
        // Hide all buttons
        hideAllPublishButtons();
        // Show buttons depending on the status
        if (IndicatorsSystemProcStatusEnum.DRAFT.equals(status)) {
            showProductionValidationButton();
        } else if (IndicatorsSystemProcStatusEnum.PRODUCTION_VALIDATION.equals(status)) {
            showDiffusionValidationButton();
            showRejectValidationButton();
        } else if (IndicatorsSystemProcStatusEnum.DIFFUSION_VALIDATION.equals(status)) {
            showPublishButton();
            showRejectValidationButton();
        } else if (IndicatorsSystemProcStatusEnum.VALIDATION_REJECTED.equals(status)) {
            showProductionValidationButton();
        } else if (IndicatorsSystemProcStatusEnum.PUBLISHED.equals(status)) {
            showArchiveButton();
            showVersioningButton();
            showExportDsplButton();
        } else if (IndicatorsSystemProcStatusEnum.ARCHIVED.equals(status)) {
            showVersioningButton();
        }
    }

    @Override
    public void setViewMode() {
        super.setViewMode();
        updateVisibility();
    }

    public void setIndicatorsSytemCode(String code) {
        this.indicatorsSystemCode = code;
    }

    public HasClickHandlers getProductionValidation() {
        return productionValidation;
    }

    public HasClickHandlers getDiffusionValidation() {
        return diffusionValidation;
    }

    public HasClickHandlers getRejectValidation() {
        return rejectValidation;
    }

    public HasClickHandlers getPublish() {
        return publish;
    }

    public HasClickHandlers getArchive() {
        return archive;
    }

    public HasClickHandlers getVersioning() {
        return versioning;
    }

    public HasClickHandlers getExportDspl() {
        return exportDspl;
    }

    private void hideAllPublishButtons() {
        productionValidation.hide();
        diffusionValidation.hide();
        rejectValidation.hide();
        publish.hide();
        archive.hide();
        versioning.hide();
        exportDspl.hide();
    }

    private void showProductionValidationButton() {
        if (ClientSecurityUtils.canSendIndicatorsSystemToProductionValidation(indicatorsSystemCode)) {
            productionValidation.show();
        }
    }

    private void showDiffusionValidationButton() {
        if (ClientSecurityUtils.canSendIndicatorsSystemToDiffusionValidation(indicatorsSystemCode)) {
            diffusionValidation.show();
        }
    }

    private void showRejectValidationButton() {
        if (IndicatorsSystemProcStatusEnum.PRODUCTION_VALIDATION.equals(status)) {
            if (ClientSecurityUtils.canRejectIndicatorsSystemProductionValidation(indicatorsSystemCode)) {
                rejectValidation.show();
            }
        } else if (IndicatorsSystemProcStatusEnum.DIFFUSION_VALIDATION.equals(status)) {
            if (ClientSecurityUtils.canRejectIndicatorsSystemDiffusionValidation(indicatorsSystemCode)) {
                rejectValidation.show();
            }
        }
    }

    private void showPublishButton() {
        if (ClientSecurityUtils.canPublishIndicatorsSystem(indicatorsSystemCode)) {
            publish.show();
        }
    }

    private void showArchiveButton() {
        if (ClientSecurityUtils.canArchiveIndicatorsSystem(indicatorsSystemCode)) {
            archive.show();
        }
    }

    private void showVersioningButton() {
        if (ClientSecurityUtils.canVersioningIndicatorsSystem(indicatorsSystemCode)) {
            versioning.show();
        }
    }

    private void showExportDsplButton() {
        exportDspl.show();
    }

}
