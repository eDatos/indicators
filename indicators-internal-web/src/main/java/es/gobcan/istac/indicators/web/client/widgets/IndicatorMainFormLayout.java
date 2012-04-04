package es.gobcan.istac.indicators.web.client.widgets;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;

import com.smartgwt.client.widgets.events.HasClickHandlers;

import es.gobcan.istac.indicators.core.enume.domain.IndicatorProcStatusEnum;
import es.gobcan.istac.indicators.web.client.resources.IndicatorsResources;


public class IndicatorMainFormLayout extends InternationalMainFormLayout {

    private PublishToolStripButton productionValidation;
    private PublishToolStripButton diffusionValidation;
    private PublishToolStripButton rejectValidation;
    private PublishToolStripButton publish;
    private PublishToolStripButton archive;
    private PublishToolStripButton versioning;
    
    private IndicatorProcStatusEnum status;
    
    
    public IndicatorMainFormLayout() {
        
        // Remove handler from edit button
        editHandlerRegistration.removeHandler();
        
        productionValidation = new PublishToolStripButton(getConstants().indicatorSendToProductionValidation(), IndicatorsResources.RESOURCE.validateProduction().getURL());
        diffusionValidation = new PublishToolStripButton(getConstants().indicatorSendToDiffusionValidation(), IndicatorsResources.RESOURCE.validateDifussion().getURL());
        publish = new PublishToolStripButton(getConstants().indicatorPublish(), IndicatorsResources.RESOURCE.publish().getURL());
        archive = new PublishToolStripButton(getConstants().indicatorArchive(), IndicatorsResources.RESOURCE.archive().getURL());
        rejectValidation = new PublishToolStripButton(getConstants().indicatorRejectValidation(), IndicatorsResources.RESOURCE.reject().getURL());
        versioning = new PublishToolStripButton(getConstants().indicatorVersioning(), IndicatorsResources.RESOURCE.version().getURL());
        
        toolStrip.addButton(productionValidation);
        toolStrip.addButton(diffusionValidation);
        toolStrip.addButton(publish);
        toolStrip.addButton(archive);
        toolStrip.addButton(rejectValidation);
        toolStrip.addButton(versioning);
    }
    
    public void updatePublishSection(IndicatorProcStatusEnum status) {
        this.status = status;
    }
    
    private void updateVisibility() {
        // Hide all buttons
        hideAllPublishButtons();
        // Show buttons depending on the status
        if (IndicatorProcStatusEnum.DRAFT.equals(status)) {
            productionValidation.show();
        } else if (IndicatorProcStatusEnum.PRODUCTION_VALIDATION.equals(status)) {
            diffusionValidation.show();
            rejectValidation.show();
        } else if (IndicatorProcStatusEnum.DIFFUSION_VALIDATION.equals(status)) {
            publish.show();
            rejectValidation.show();
        } else if (IndicatorProcStatusEnum.VALIDATION_REJECTED.equals(status)) {
            productionValidation.show();
        } else if (IndicatorProcStatusEnum.PUBLICATION_FAILED.equals(status)) {
            publish.show();
        } else if (IndicatorProcStatusEnum.PUBLISHED.equals(status)) {
            archive.show();
            versioning.show();
        } else if (IndicatorProcStatusEnum.ARCHIVED.equals(status)) {
            versioning.show();
        }
    }
    
    @Override
    public void setViewMode() {
        super.setViewMode();
        updateVisibility();
    }
    
    @Override
    public void setEditionMode() {
        super.setEditionMode();
        hideAllPublishButtons();
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
    
    private void hideAllPublishButtons() {
        productionValidation.hide();
        diffusionValidation.hide();
        rejectValidation.hide();
        publish.hide();
        archive.hide();
        versioning.hide();
    }

}
