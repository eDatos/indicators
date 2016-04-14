package es.gobcan.istac.indicators.web.client.widgets;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;

import com.smartgwt.client.widgets.events.HasClickHandlers;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

import es.gobcan.istac.indicators.core.enume.domain.IndicatorProcStatusEnum;
import es.gobcan.istac.indicators.web.client.resources.IndicatorsResources;
import es.gobcan.istac.indicators.web.client.utils.ClientSecurityUtils;

public class IndicatorMainFormLayout extends InternationalMainFormLayout {

    private PublishToolStripButton  productionValidation;
    private PublishToolStripButton  diffusionValidation;
    private PublishToolStripButton  rejectValidation;
    private PublishToolStripButton  publish;
    private PublishToolStripButton  archive;
    private PublishToolStripButton  versioning;
    private ToolStripButton         populateData;
    private ToolStripButton         previewData;
    private ToolStripButton         enableNotifyPopulationErrors;
    private ToolStripButton         disableNotifyPopulationErrors;

    private IndicatorProcStatusEnum status;

    public IndicatorMainFormLayout() {
        super();
        common();
    }

    public IndicatorMainFormLayout(boolean canEdit) {
        super(canEdit);
        common();
    }

    private void common() {
        // Remove handler from edit button
        editHandlerRegistration.removeHandler();

        productionValidation = new PublishToolStripButton(getConstants().indicatorSendToProductionValidation(), IndicatorsResources.RESOURCE.validateProduction().getURL());
        diffusionValidation = new PublishToolStripButton(getConstants().indicatorSendToDiffusionValidation(), IndicatorsResources.RESOURCE.validateDifussion().getURL());
        publish = new PublishToolStripButton(getConstants().indicatorPublish(), IndicatorsResources.RESOURCE.publish().getURL());
        archive = new PublishToolStripButton(getConstants().indicatorArchive(), IndicatorsResources.RESOURCE.archive().getURL());
        rejectValidation = new PublishToolStripButton(getConstants().indicatorRejectValidation(), IndicatorsResources.RESOURCE.reject().getURL());
        versioning = new PublishToolStripButton(getConstants().indicatorVersioning(), IndicatorsResources.RESOURCE.version().getURL());

        populateData = new ToolStripButton(getConstants().indicatorPopulateData(), IndicatorsResources.RESOURCE.populateData().getURL());

        previewData = new ToolStripButton(getConstants().indicatorPreviewData(), IndicatorsResources.RESOURCE.preview().getURL());
        
        enableNotifyPopulationErrors = new ToolStripButton(getConstants().indicatorEnableNotifyPopulationErrors(), IndicatorsResources.RESOURCE.enableNotification().getURL());
        enableNotifyPopulationErrors.hide();
        disableNotifyPopulationErrors = new ToolStripButton(getConstants().indicatorDisableNotifyPopulationErrors(), IndicatorsResources.RESOURCE.disableNotification().getURL());
        disableNotifyPopulationErrors.hide();

        toolStrip.addButton(productionValidation);
        toolStrip.addButton(diffusionValidation);
        toolStrip.addButton(publish);
        toolStrip.addButton(archive);
        toolStrip.addButton(rejectValidation);
        toolStrip.addButton(versioning);
        toolStrip.addButton(populateData);
        toolStrip.addButton(previewData);
        toolStrip.addButton(enableNotifyPopulationErrors);
        toolStrip.addButton(disableNotifyPopulationErrors);
    }

    public void updatePublishSection(IndicatorProcStatusEnum status) {
        this.status = status;
    }

    private void updateVisibility() {
        // Hide all buttons
        hideAllPublishButtons();
        // Show buttons depending on the status
        if (IndicatorProcStatusEnum.DRAFT.equals(status)) {
            showProductionValidationButton();
        } else if (IndicatorProcStatusEnum.PRODUCTION_VALIDATION.equals(status)) {
            showDiffusionValidationButton();
            showRejectValidationButton();
        } else if (IndicatorProcStatusEnum.DIFFUSION_VALIDATION.equals(status)) {
            showPublishButton();
            showRejectValidationButton();
        } else if (IndicatorProcStatusEnum.VALIDATION_REJECTED.equals(status)) {
            showProductionValidationButton();
        } else if (IndicatorProcStatusEnum.PUBLICATION_FAILED.equals(status)) {
            showPublishButton();
        } else if (IndicatorProcStatusEnum.PUBLISHED.equals(status)) {
            showArchiveButton();
            showVersioningButton();
        } else if (IndicatorProcStatusEnum.ARCHIVED.equals(status)) {
            showVersioningButton();
        }
    }

    @Override
    public void setViewMode() {
        super.setViewMode();
        updateVisibility();
        previewData.show();
    }

    @Override
    public void setEditionMode() {
        super.setEditionMode();
        hideAllPublishButtons();
        previewData.hide();
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

    public HasClickHandlers getPopulateData() {
        return populateData;
    }

    public HasClickHandlers getPreviewData() {
        return previewData;
    }
    
    public ToolStripButton getEnableNotifyPopulationErrors() {
        return enableNotifyPopulationErrors;
    }
    
    public ToolStripButton getDisableNotifyPopulationErrors() {
        return disableNotifyPopulationErrors;
    }
    
    private void hideAllPublishButtons() {
        productionValidation.hide();
        diffusionValidation.hide();
        rejectValidation.hide();
        publish.hide();
        archive.hide();
        versioning.hide();
    }

    private void showProductionValidationButton() {
        if (ClientSecurityUtils.canSendIndicatorToProductionValidation()) {
            productionValidation.show();
        }
    }

    private void showDiffusionValidationButton() {
        if (ClientSecurityUtils.canSendIndicatorToDiffusionValidation()) {
            diffusionValidation.show();
        }
    }

    private void showRejectValidationButton() {
        if (IndicatorProcStatusEnum.PRODUCTION_VALIDATION.equals(status)) {
            if (ClientSecurityUtils.canRejectIndicatorProductionValidation()) {
                rejectValidation.show();
            }
        } else if (IndicatorProcStatusEnum.DIFFUSION_VALIDATION.equals(status)) {
            if (ClientSecurityUtils.canRejectIndicatorDiffusionValidation()) {
                rejectValidation.show();
            }
        }
    }

    private void showPublishButton() {
        if (ClientSecurityUtils.canPublishIndicator()) {
            publish.show();
        }
    }

    private void showArchiveButton() {
        if (ClientSecurityUtils.canArchiveIndicator()) {
            archive.show();
        }
    }

    private void showVersioningButton() {
        if (ClientSecurityUtils.canVersioningIndicator()) {
            versioning.show();
        }
    }
    
    private void showEnableNotifyPopulationErrors() {
        if (ClientSecurityUtils.canEnableNotifyPopulationErrors()) {
            enableNotifyPopulationErrors.show();
            disableNotifyPopulationErrors.hide();
        }
    }
    
    private void showDisableNotifyPopulationErrors() {
        if (ClientSecurityUtils.canDisableNotifyPopulationErrors()) {
            disableNotifyPopulationErrors.show();
            enableNotifyPopulationErrors.hide();
        }
    }

    public void updateVisibilityNotifyPopulateErrors(Boolean notifyPopulationErrors) {
        if (notifyPopulationErrors) {
            showDisableNotifyPopulationErrors();
        } else {
            showEnableNotifyPopulationErrors();
        }
        
    }

}
