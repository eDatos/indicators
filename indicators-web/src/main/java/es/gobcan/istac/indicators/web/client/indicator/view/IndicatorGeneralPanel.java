package es.gobcan.istac.indicators.web.client.indicator.view;


import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getCoreMessages;

import java.util.List;

import org.siemac.metamac.core.common.dto.serviceapi.InternationalStringDto;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextAndUrlItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextAndUrlItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.QuantityUnitDto;
import es.gobcan.istac.indicators.web.client.indicator.presenter.IndicatorUiHandler;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS;
import es.gobcan.istac.indicators.web.client.widgets.QuantityForm;
import es.gobcan.istac.indicators.web.client.widgets.ViewQuantityForm;

public class IndicatorGeneralPanel extends VLayout {
	
    /* Data */
    private IndicatorDto indicator;
    
    /* UiHandlers */
    private IndicatorUiHandler uiHandlers;
    
	private InternationalMainFormLayout mainFormLayout;
	
	/* View Form */
	private GroupDynamicForm identifiersForm;
	private GroupDynamicForm contentClassifiersForm; 
	private GroupDynamicForm contentDescriptorsForm;
	private GroupDynamicForm productionDescriptorsForm;
	private ViewQuantityForm quantityForm;
	private GroupDynamicForm diffusionDescriptorsForm;
	private GroupDynamicForm publicationDescriptorsForm;
	private GroupDynamicForm annotationsForm;
	
	/* Edit Form*/
	private GroupDynamicForm identifiersEditionForm;
	private GroupDynamicForm contentClassifiersEditionForm; 
	private GroupDynamicForm contentDescriptorsEditionForm;
	private GroupDynamicForm productionDescriptorsEditionForm;
	private QuantityForm quantityEditionForm;
	private GroupDynamicForm diffusionDescriptorsEditionForm;
	private GroupDynamicForm publicationDescriptorsEditionForm;
	private GroupDynamicForm annotationsEditionForm;
	
	
	public IndicatorGeneralPanel() {
		super();
		
		mainFormLayout = new InternationalMainFormLayout();

		createViewForm();
		createEditionForm();
		
		this.addMember(mainFormLayout);
		bindEvents();
	}
	
	private void bindEvents() {
	    // Show/Hide Translations
	    mainFormLayout.getTranslateToolStripButton().addClickHandler(new ClickHandler() {
	        @Override
            public void onClick(ClickEvent event) {
	        	boolean translationsShowed =  mainFormLayout.getTranslateToolStripButton().isSelected();
	        	identifiersForm.setTranslationsShowed(translationsShowed);
	        	identifiersEditionForm.setTranslationsShowed(translationsShowed);
	        	contentClassifiersForm.setTranslationsShowed(translationsShowed);
	        	contentClassifiersEditionForm.setTranslationsShowed(translationsShowed);
	        	contentDescriptorsForm.setTranslationsShowed(translationsShowed);
	        	contentDescriptorsEditionForm.setTranslationsShowed(translationsShowed);
	        	productionDescriptorsForm.setTranslationsShowed(translationsShowed);
	        	productionDescriptorsEditionForm.setTranslationsShowed(translationsShowed);
	        	quantityForm.setTranslationsShowed(translationsShowed);
	        	quantityEditionForm.setTranslationsShowed(translationsShowed);
	        	diffusionDescriptorsForm.setTranslationsShowed(translationsShowed);
	        	diffusionDescriptorsEditionForm.setTranslationsShowed(translationsShowed);
	        	publicationDescriptorsForm.setTranslationsShowed(translationsShowed);
	        	publicationDescriptorsEditionForm.setTranslationsShowed(translationsShowed);
	        	annotationsForm.setTranslationsShowed(translationsShowed);
	        	annotationsEditionForm.setTranslationsShowed(translationsShowed);
	        }
        });
        
	    // Save
        mainFormLayout.getSave().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                saveIndicator();
            }
        });
	}
	
	/**
	 * Creates and returns the view layout
	 * 
	 * @return
	 */
	private void createViewForm() {
	    // Identifiers Form
	    identifiersForm = new GroupDynamicForm(getConstants().indicDetailIdentifiers());
	    ViewTextItem code = new ViewTextItem(IndicatorDS.CODE, getConstants().indicDetailIdentifier());
	    ViewTextItem uuid = new ViewTextItem(IndicatorDS.UUID, getConstants().indicDetailUuid());
	    ViewTextItem version = new ViewTextItem(IndicatorDS.VERSION_NUMBER, getConstants().indicDetailVersion());
	    ViewTextItem procStatus = new ViewTextItem(IndicatorDS.PROC_STATUS, getConstants().indicDetailProcStatus());
	    ViewMultiLanguageTextItem title = new ViewMultiLanguageTextItem(IndicatorDS.TITLE, getConstants().indicDetailTitle());
	    ViewMultiLanguageTextItem acronym = new ViewMultiLanguageTextItem(IndicatorDS.ACRONYM, getConstants().indicDetailAcronym());
		identifiersForm.setFields(code, uuid, version, procStatus, title, acronym);

		// Content Classifiers Form
		contentClassifiersForm = new GroupDynamicForm(getConstants().indicDetailContentClassifiers());
		ViewTextItem subjectCode = new ViewTextItem(IndicatorDS.SUBJECT_CODE, getConstants().indicDetailSubjectCode());
		ViewMultiLanguageTextItem subjectTitle = new ViewMultiLanguageTextItem(IndicatorDS.SUBJECT_TITLE, getConstants().indicDetailSubjectTitle());
		contentClassifiersForm.setFields(subjectCode, subjectTitle);
		
		// Content Descriptors Form
		contentDescriptorsForm = new GroupDynamicForm(getConstants().indicDetailContentDescriptors());
		ViewMultiLanguageTextItem conceptDescription = new ViewMultiLanguageTextItem(IndicatorDS.CONCEPT_DESCRIPTION, getConstants().indicDetailConceptDescription());
		contentDescriptorsForm.setFields(conceptDescription);
		
		// Production Descriptors Form
		productionDescriptorsForm = new GroupDynamicForm(getConstants().indicDetailProductionDescriptors());
		ViewTextItem prodVersion = new ViewTextItem(IndicatorDS.PRODUCTION_VERSION, getConstants().indicDetailProductionVersion());
		ViewTextItem prodValDate = new ViewTextItem(IndicatorDS.PRODUCTION_VALIDATION_DATE, getConstants().indicDetailProductionValidationDate());
		ViewTextItem prodValUser = new ViewTextItem(IndicatorDS.PRODUCTION_VALIDATION_USER, getConstants().indicDetailProductionValidationUser());
		productionDescriptorsForm.setFields(prodVersion, prodValDate, prodValUser);
		
		// Quantity Form
		quantityForm = new ViewQuantityForm(getConstants().indicDetailQuantity());
		
		// Diffusion Descriptors Form
        diffusionDescriptorsForm = new GroupDynamicForm(getConstants().indicDetailDiffusionDescriptors());
        ViewTextItem diffVersion = new ViewTextItem(IndicatorDS.DIFFUSION_VERSION, getConstants().indicDetailDiffusionVersion());
        ViewTextItem diffValDate = new ViewTextItem(IndicatorDS.DIFFUSION_VALIDATION_DATE, getConstants().indicDetailDiffusionValidationDate());
        ViewTextItem diffValUser = new ViewTextItem(IndicatorDS.DIFFUSION_VALIDATION_USER, getConstants().indicDetailDiffusionValidationUser());
        diffusionDescriptorsForm.setFields(diffVersion, diffValDate, diffValUser);
		
        // Publication Descriptors Form
        publicationDescriptorsForm = new GroupDynamicForm(getConstants().indicDetailPublicationDescriptors());
        ViewTextItem pubFailedDate = new ViewTextItem(IndicatorDS.PUBLICATION_FAILED_DATE, getConstants().indicDetailPublicationFailedDate());
        ViewTextItem pubFailedUser = new ViewTextItem(IndicatorDS.PUBLICATION_FAILED_USER, getConstants().indicDetailPublicationFailedUser());
        ViewTextItem pubDate = new ViewTextItem(IndicatorDS.PUBLICATION_DATE, getConstants().indicDetailPublicationDate());
        ViewTextItem pubUser = new ViewTextItem(IndicatorDS.PUBLICATION_USER, getConstants().indicDetailPublicationUser());
        ViewTextItem archDate = new ViewTextItem(IndicatorDS.ARCHIVED_DATE, getConstants().indicDetailArchivedDate());
        ViewTextItem archUser = new ViewTextItem(IndicatorDS.ARCHIVED_USER, getConstants().indicDetailArchivedUser());
        publicationDescriptorsForm.setFields(pubFailedDate, pubFailedUser, pubDate, pubUser, archDate, archUser);
        
        // Annotations Form
        annotationsForm = new GroupDynamicForm(getConstants().indicDetailAnnotations());
        ViewMultiLanguageTextAndUrlItem notes = new ViewMultiLanguageTextAndUrlItem(IndicatorDS.NOTES, getConstants().indicDetailNotes());
        ViewMultiLanguageTextAndUrlItem comments = new ViewMultiLanguageTextAndUrlItem(IndicatorDS.COMMENTS, getConstants().indicDetailComments());
        annotationsForm.setFields(notes, comments);
        
		mainFormLayout.addViewCanvas(identifiersForm);
		mainFormLayout.addViewCanvas(contentClassifiersForm);
		mainFormLayout.addViewCanvas(contentDescriptorsForm);
		mainFormLayout.addViewCanvas(productionDescriptorsForm);
		mainFormLayout.addViewCanvas(quantityForm);
		mainFormLayout.addViewCanvas(diffusionDescriptorsForm);
		mainFormLayout.addViewCanvas(publicationDescriptorsForm);
		mainFormLayout.addViewCanvas(annotationsForm);
	}
	
	
	private void createEditionForm() {
	    // Identifiers Form
        identifiersEditionForm = new GroupDynamicForm(getConstants().indicDetailIdentifiers());
	    ViewTextItem code = new ViewTextItem(IndicatorDS.CODE, getConstants().indicDetailIdentifier());
	    ViewTextItem uuid = new ViewTextItem(IndicatorDS.UUID, getConstants().indicDetailUuid());
	    ViewTextItem version = new ViewTextItem(IndicatorDS.VERSION_NUMBER, getConstants().indicDetailVersion());
	    ViewTextItem procStatus = new ViewTextItem(IndicatorDS.PROC_STATUS, getConstants().indicDetailProcStatus());
	    MultiLanguageTextItem title = new MultiLanguageTextItem(IndicatorDS.TITLE, getConstants().indicDetailTitle());
	    title.setRequired(true);
	    MultiLanguageTextItem acronym = new MultiLanguageTextItem(IndicatorDS.ACRONYM, getConstants().indicDetailAcronym());
		identifiersEditionForm.setFields(code, uuid, version, procStatus, title, acronym);
				
		// Status Form
		contentClassifiersEditionForm = new GroupDynamicForm(getConstants().indicDetailContentClassifiers());
		RequiredTextItem subjectCode = new RequiredTextItem(IndicatorDS.SUBJECT_CODE, getConstants().indicDetailSubjectCode());
		subjectCode.setRequired(true);
        MultiLanguageTextItem subjectTitle = new MultiLanguageTextItem(IndicatorDS.SUBJECT_TITLE, getConstants().indicDetailSubjectTitle());
        subjectTitle.setRequired(true);
        contentClassifiersEditionForm.setFields(subjectCode, subjectTitle);
		
        // Content Descriptors Form
        contentDescriptorsEditionForm = new GroupDynamicForm(getConstants().indicDetailContentDescriptors());
        MultiLanguageTextItem conceptDescription = new MultiLanguageTextItem(IndicatorDS.CONCEPT_DESCRIPTION, getConstants().indicDetailConceptDescription());
        contentDescriptorsEditionForm.setFields(conceptDescription);
        
        // Production Descriptors Form
        productionDescriptorsEditionForm = new GroupDynamicForm(getConstants().indicDetailProductionDescriptors());
        ViewTextItem prodVersion = new ViewTextItem(IndicatorDS.PRODUCTION_VERSION, getConstants().indicDetailProductionVersion());
        ViewTextItem prodValDate = new ViewTextItem(IndicatorDS.PRODUCTION_VALIDATION_DATE, getConstants().indicDetailProductionValidationDate());
        ViewTextItem prodValUser = new ViewTextItem(IndicatorDS.PRODUCTION_VALIDATION_USER, getConstants().indicDetailProductionValidationUser());
        productionDescriptorsEditionForm.setFields(prodVersion, prodValDate, prodValUser);
        
        // Quantity Form
        quantityEditionForm = new QuantityForm(getConstants().indicDetailQuantity());
        
        // Diffusion Descriptors Form
        diffusionDescriptorsEditionForm = new GroupDynamicForm(getConstants().indicDetailDiffusionDescriptors());
        ViewTextItem diffVersion = new ViewTextItem(IndicatorDS.DIFFUSION_VERSION, getConstants().indicDetailDiffusionVersion());
        ViewTextItem diffValDate = new ViewTextItem(IndicatorDS.DIFFUSION_VALIDATION_DATE, getConstants().indicDetailDiffusionValidationDate());
        ViewTextItem diffValUser = new ViewTextItem(IndicatorDS.DIFFUSION_VALIDATION_USER, getConstants().indicDetailDiffusionValidationUser());
        diffusionDescriptorsEditionForm.setFields(diffVersion, diffValDate, diffValUser);
        
        // Publication Descriptors
        publicationDescriptorsEditionForm = new GroupDynamicForm(getConstants().indicDetailPublicationDescriptors());
        ViewTextItem pubFailedDate = new ViewTextItem(IndicatorDS.PUBLICATION_FAILED_DATE, getConstants().indicDetailPublicationFailedDate());
        ViewTextItem pubFailedUser = new ViewTextItem(IndicatorDS.PUBLICATION_FAILED_USER, getConstants().indicDetailPublicationFailedUser());
        ViewTextItem pubDate = new ViewTextItem(IndicatorDS.PUBLICATION_DATE, getConstants().indicDetailPublicationDate());
        ViewTextItem pubUser = new ViewTextItem(IndicatorDS.PUBLICATION_USER, getConstants().indicDetailPublicationUser());
        ViewTextItem archDate = new ViewTextItem(IndicatorDS.ARCHIVED_DATE, getConstants().indicDetailArchivedDate());
        ViewTextItem archUser = new ViewTextItem(IndicatorDS.ARCHIVED_USER, getConstants().indicDetailArchivedUser());
        publicationDescriptorsEditionForm.setFields(pubFailedDate, pubFailedUser, pubDate, pubUser, archDate, archUser);
        
        // Annotations Form
        annotationsEditionForm = new GroupDynamicForm(getConstants().indicDetailAnnotations());
        MultiLanguageTextAndUrlItem notes = new MultiLanguageTextAndUrlItem(IndicatorDS.NOTES, getConstants().indicDetailNotes());
        MultiLanguageTextAndUrlItem comments = new MultiLanguageTextAndUrlItem(IndicatorDS.COMMENTS, getConstants().indicDetailComments());
        annotationsEditionForm.setFields(notes, comments);
        
		mainFormLayout.addEditionCanvas(identifiersEditionForm);
		mainFormLayout.addEditionCanvas(contentClassifiersEditionForm);
		mainFormLayout.addEditionCanvas(contentDescriptorsEditionForm);
		mainFormLayout.addEditionCanvas(productionDescriptorsEditionForm);
		mainFormLayout.addEditionCanvas(quantityEditionForm);
		mainFormLayout.addEditionCanvas(diffusionDescriptorsEditionForm);
		mainFormLayout.addEditionCanvas(publicationDescriptorsEditionForm);
		mainFormLayout.addEditionCanvas(annotationsEditionForm);
	}
	
	public void setIndicator(IndicatorDto indicatorDto) {
	    this.indicator = indicatorDto;
	    
		mainFormLayout.setViewMode();
		
		setIndicatorViewMode(indicatorDto);
		setIndicatorEditionMode(indicatorDto);
		
		// Clear errors
		identifiersEditionForm.clearErrors(true);
	}
	
	private void setIndicatorViewMode(IndicatorDto indicatorDto) {
	    // Identifiers Form
	    identifiersForm.setValue(IndicatorDS.CODE, indicatorDto.getCode());
	    identifiersForm.setValue(IndicatorDS.UUID, indicatorDto.getUuid());
	    identifiersForm.setValue(IndicatorDS.VERSION_NUMBER, indicatorDto.getVersionNumber());
	    identifiersForm.setValue(IndicatorDS.PROC_STATUS, getCoreMessages().getString(getCoreMessages().indicatorProcStatusEnum() + indicatorDto.getProcStatus()));
	    identifiersForm.setValue(IndicatorDS.TITLE, RecordUtils.getInternationalStringRecord(indicatorDto.getTitle()));
	    identifiersForm.setValue(IndicatorDS.ACRONYM, RecordUtils.getInternationalStringRecord(indicatorDto.getAcronym()));
	    
	    // Content Classifiers
	    contentClassifiersForm.setValue(IndicatorDS.SUBJECT_CODE, indicatorDto.getSubjectCode());
	    contentClassifiersForm.setValue(IndicatorDS.SUBJECT_TITLE, RecordUtils.getInternationalStringRecord(indicatorDto.getSubjectTitle()));
	    
	    // Content Descriptors
	    contentDescriptorsForm.setValue(IndicatorDS.CONCEPT_DESCRIPTION, RecordUtils.getInternationalStringRecord(indicatorDto.getConceptDescription()));
	    
	    // Production Descriptors
	    productionDescriptorsForm.setValue(IndicatorDS.PRODUCTION_VERSION, indicatorDto.getProductionVersion());
	    productionDescriptorsForm.setValue(IndicatorDS.PRODUCTION_VALIDATION_DATE, indicatorDto.getProductionValidationDate() != null ? indicatorDto.getProductionValidationDate().toString() : "");
	    productionDescriptorsForm.setValue(IndicatorDS.PRODUCTION_VALIDATION_USER, indicatorDto.getProductionValidationUser());
	    
	    // Quantity
	    quantityForm.setValue(indicatorDto.getQuantity());
	    
	    // Diffusion Descriptors
	    diffusionDescriptorsForm.setValue(IndicatorDS.DIFFUSION_VERSION, indicatorDto.getDiffusionVersion());
	    diffusionDescriptorsForm.setValue(IndicatorDS.DIFFUSION_VALIDATION_DATE, indicatorDto.getDiffusionValidationDate() != null ? indicatorDto.getDiffusionValidationDate().toString() : "");
	    diffusionDescriptorsForm.setValue(IndicatorDS.DIFFUSION_VALIDATION_USER, indicatorDto.getDiffusionValidationUser());
	    
	    // Publication Descriptors
	    publicationDescriptorsForm.setValue(IndicatorDS.PUBLICATION_FAILED_DATE, indicatorDto.getPublicationFailedDate() != null ? indicatorDto.getPublicationFailedDate().toString() : "");
	    publicationDescriptorsForm.setValue(IndicatorDS.PUBLICATION_FAILED_USER, indicatorDto.getPublicationFailedUser());
	    publicationDescriptorsForm.setValue(IndicatorDS.PUBLICATION_DATE, indicatorDto.getPublicationDate() != null ? indicatorDto.getPublicationDate().toString() : "");
	    publicationDescriptorsForm.setValue(IndicatorDS.PUBLICATION_USER, indicatorDto.getPublicationUser());
	    publicationDescriptorsForm.setValue(IndicatorDS.ARCHIVED_DATE, indicatorDto.getArchiveDate() != null ? indicatorDto.getArchiveDate().toString() : "");
	    publicationDescriptorsForm.setValue(IndicatorDS.ARCHIVED_USER, indicatorDto.getArchiveUser());
	    
	    // Annotations
	    ((ViewMultiLanguageTextAndUrlItem)annotationsForm.getItem(IndicatorDS.NOTES)).setValue(indicatorDto.getNotes(), indicatorDto.getNotesUrl());
	    ((ViewMultiLanguageTextAndUrlItem)annotationsForm.getItem(IndicatorDS.COMMENTS)).setValue(indicatorDto.getComments(), indicatorDto.getCommentsUrl()); 
	    
	}
	
	private void setIndicatorEditionMode(IndicatorDto indicatorDto) {
	    // Identifiers Form
		identifiersEditionForm.setValue(IndicatorDS.CODE, indicatorDto.getCode());
		identifiersEditionForm.setValue(IndicatorDS.UUID, indicatorDto.getUuid());
		identifiersEditionForm.setValue(IndicatorDS.VERSION_NUMBER, indicatorDto.getVersionNumber());
		identifiersEditionForm.setValue(IndicatorDS.PROC_STATUS, getCoreMessages().getString(getCoreMessages().indicatorProcStatusEnum() + indicatorDto.getProcStatus()));
		identifiersEditionForm.setValue(IndicatorDS.TITLE, RecordUtils.getInternationalStringRecord(indicatorDto.getTitle()));
		identifiersEditionForm.setValue(IndicatorDS.ACRONYM, RecordUtils.getInternationalStringRecord(indicatorDto.getAcronym()));
		
	    // Content Classifiers
        contentClassifiersEditionForm.setValue(IndicatorDS.SUBJECT_CODE, indicatorDto.getSubjectCode());
        contentClassifiersEditionForm.setValue(IndicatorDS.SUBJECT_TITLE, RecordUtils.getInternationalStringRecord(indicatorDto.getSubjectTitle()));
        
        // Content Descriptors
        contentDescriptorsEditionForm.setValue(IndicatorDS.CONCEPT_DESCRIPTION, RecordUtils.getInternationalStringRecord(indicatorDto.getConceptDescription()));
        
        // Production Descriptors
        productionDescriptorsEditionForm.setValue(IndicatorDS.PRODUCTION_VERSION, indicatorDto.getProductionVersion());
        productionDescriptorsEditionForm.setValue(IndicatorDS.PRODUCTION_VALIDATION_DATE, indicatorDto.getProductionValidationDate() != null ? indicatorDto.getProductionValidationDate().toString() : "");
        productionDescriptorsEditionForm.setValue(IndicatorDS.PRODUCTION_VALIDATION_USER, indicatorDto.getProductionValidationUser());
        
        // Quantity
        quantityEditionForm.setIndicator(indicatorDto);
        quantityEditionForm.setValue(indicatorDto.getQuantity());
        
        // Diffusion Descriptors
        diffusionDescriptorsEditionForm.setValue(IndicatorDS.DIFFUSION_VERSION, indicatorDto.getDiffusionVersion());
        diffusionDescriptorsEditionForm.setValue(IndicatorDS.DIFFUSION_VALIDATION_DATE, indicatorDto.getDiffusionValidationDate() != null ? indicatorDto.getDiffusionValidationDate().toString() : "");
        diffusionDescriptorsEditionForm.setValue(IndicatorDS.DIFFUSION_VALIDATION_USER, indicatorDto.getDiffusionValidationUser());
        
        // Publication Descriptors
        publicationDescriptorsEditionForm.setValue(IndicatorDS.PUBLICATION_FAILED_DATE, indicatorDto.getPublicationFailedDate() != null ? indicatorDto.getPublicationFailedDate().toString() : "");
        publicationDescriptorsEditionForm.setValue(IndicatorDS.PUBLICATION_FAILED_USER, indicatorDto.getPublicationFailedUser());
        publicationDescriptorsEditionForm.setValue(IndicatorDS.PUBLICATION_DATE, indicatorDto.getPublicationDate() != null ? indicatorDto.getPublicationDate().toString() : "");
        publicationDescriptorsEditionForm.setValue(IndicatorDS.PUBLICATION_USER, indicatorDto.getPublicationUser());
        publicationDescriptorsEditionForm.setValue(IndicatorDS.ARCHIVED_DATE, indicatorDto.getArchiveDate() != null ? indicatorDto.getArchiveDate().toString() : "");
        publicationDescriptorsEditionForm.setValue(IndicatorDS.ARCHIVED_USER, indicatorDto.getArchiveUser());
        
        // Annotations
        ((MultiLanguageTextAndUrlItem)annotationsEditionForm.getItem(IndicatorDS.NOTES)).setValue(indicatorDto.getNotes(), indicatorDto.getNotesUrl());
        ((MultiLanguageTextAndUrlItem)annotationsEditionForm.getItem(IndicatorDS.COMMENTS)).setValue(indicatorDto.getComments(), indicatorDto.getCommentsUrl());

	}
	
    private void saveIndicator() {
        if (identifiersEditionForm.validate(false) && contentClassifiersEditionForm.validate(false) && contentDescriptorsEditionForm.validate(false) && quantityEditionForm.validate(false)) {
            // Identifiers
            indicator.setTitle((InternationalStringDto)identifiersEditionForm.getValue(IndicatorDS.TITLE));
            indicator.setAcronym((InternationalStringDto)identifiersEditionForm.getValue(IndicatorDS.ACRONYM));
            // Content Classifiers
            indicator.setSubjectCode(contentClassifiersEditionForm.getValueAsString(IndicatorDS.SUBJECT_CODE));
            indicator.setSubjectTitle((InternationalStringDto)contentClassifiersEditionForm.getValue(IndicatorDS.SUBJECT_TITLE));
            // Content Descriptors
            indicator.setConceptDescription((InternationalStringDto)contentDescriptorsEditionForm.getValue(IndicatorDS.CONCEPT_DESCRIPTION));
            // Quantity
            indicator.setQuantity(quantityEditionForm.getValue());
            // Annotations
            indicator.setNotes(((MultiLanguageTextAndUrlItem)annotationsEditionForm.getItem(IndicatorDS.NOTES)).getTextValue());
            indicator.setNotesUrl(((MultiLanguageTextAndUrlItem)annotationsEditionForm.getItem(IndicatorDS.NOTES)).getUrlValue());
            indicator.setComments(((MultiLanguageTextAndUrlItem)annotationsEditionForm.getItem(IndicatorDS.COMMENTS)).getTextValue());
            indicator.setCommentsUrl(((MultiLanguageTextAndUrlItem)annotationsEditionForm.getItem(IndicatorDS.COMMENTS)).getUrlValue());
            
            uiHandlers.saveIndicator(indicator);
        }
    }
    
    public void setUiHandlers(IndicatorUiHandler uiHandlers) {
        this.uiHandlers = uiHandlers;
    }
    
    public void setQuantityUnits(List<QuantityUnitDto> units) {
        quantityForm.setQuantityUnits(units);
        quantityEditionForm.setQuantityUnits(units);
    }
    
    public void setIndicatorList(List<IndicatorDto> indicatorDtos) {
        quantityForm.setIndicators(indicatorDtos);
        quantityEditionForm.setIndicators(indicatorDtos);
    }
    
}
