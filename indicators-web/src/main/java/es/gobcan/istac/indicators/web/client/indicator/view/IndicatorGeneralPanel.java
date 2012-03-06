package es.gobcan.istac.indicators.web.client.indicator.view;


import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getCoreMessages;

import org.siemac.metamac.core.common.dto.serviceapi.InternationalStringDto;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.web.client.indicator.presenter.IndicatorUiHandler;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS;

public class IndicatorGeneralPanel extends VLayout {
	
    /* Data */
    private IndicatorDto indicator;
    
    /* UiHandlers */
    private IndicatorUiHandler uiHandlers;
    
	private InternationalMainFormLayout mainFormLayout;
	
	/* View Form */
	private GroupDynamicForm identifiersForm;
	private GroupDynamicForm contentClassifiersForm; 
	
	/* Edit Form*/
	private GroupDynamicForm identifiersEditionForm;
	private GroupDynamicForm contentClassifiersEditionForm; 
	
	
	public IndicatorGeneralPanel() {
		super();
		
		mainFormLayout = new InternationalMainFormLayout();

		createViewForm();
		createEditionForm();
		
		this.addMember(mainFormLayout);
		bindEvents();
	}
	
	private void bindEvents() {
	    mainFormLayout.getTranslateToolStripButton().addClickHandler(new ClickHandler() {
	        @Override
            public void onClick(ClickEvent event) {
	        	boolean translationsShowed =  mainFormLayout.getTranslateToolStripButton().isSelected();
	        	identifiersForm.setTranslationsShowed(translationsShowed);
	        	identifiersEditionForm.setTranslationsShowed(translationsShowed);
	        	contentClassifiersForm.setTranslationsShowed(translationsShowed);
	        	contentClassifiersEditionForm.setTranslationsShowed(translationsShowed);
	        }
        });
        
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
		
		mainFormLayout.addViewCanvas(identifiersForm);
		mainFormLayout.addViewCanvas(contentClassifiersForm);
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
		
		mainFormLayout.addEditionCanvas(identifiersEditionForm);
		mainFormLayout.addEditionCanvas(contentClassifiersEditionForm);
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
	    identifiersForm.setValue(IndicatorDS.TITLE, RecordUtils.getInternationalStringRecord(indicatorDto.getName()));
	    identifiersForm.setValue(IndicatorDS.ACRONYM, RecordUtils.getInternationalStringRecord(indicatorDto.getAcronym()));
	    
	    // Content Classifiers
	    contentClassifiersForm.setValue(IndicatorDS.SUBJECT_CODE, indicatorDto.getSubjectCode());
	    contentClassifiersForm.setValue(IndicatorDS.SUBJECT_TITLE, RecordUtils.getInternationalStringRecord(indicatorDto.getSubjectTitle()));
	}
	
	private void setIndicatorEditionMode(IndicatorDto indicatorDto) {
	    // Identifiers Form
		identifiersEditionForm.setValue(IndicatorDS.CODE, indicatorDto.getCode());
		identifiersEditionForm.setValue(IndicatorDS.UUID, indicatorDto.getUuid());
		identifiersEditionForm.setValue(IndicatorDS.VERSION_NUMBER, indicatorDto.getVersionNumber());
		identifiersEditionForm.setValue(IndicatorDS.PROC_STATUS, getCoreMessages().getString(getCoreMessages().indicatorProcStatusEnum() + indicatorDto.getProcStatus()));
		identifiersEditionForm.setValue(IndicatorDS.TITLE, RecordUtils.getInternationalStringRecord(indicatorDto.getName()));
		identifiersEditionForm.setValue(IndicatorDS.ACRONYM, RecordUtils.getInternationalStringRecord(indicatorDto.getAcronym()));
		
	    // Content Classifiers
        contentClassifiersEditionForm.setValue(IndicatorDS.SUBJECT_CODE, indicatorDto.getSubjectCode());
        contentClassifiersEditionForm.setValue(IndicatorDS.SUBJECT_TITLE, RecordUtils.getInternationalStringRecord(indicatorDto.getSubjectTitle()));
	}
	
    private void saveIndicator() {
        if (identifiersEditionForm.validate() && contentClassifiersEditionForm.validate()) {
            // Identifiers
            indicator.setName((InternationalStringDto)identifiersEditionForm.getValue(IndicatorDS.TITLE));
            indicator.setAcronym((InternationalStringDto)identifiersEditionForm.getValue(IndicatorDS.ACRONYM));
            // Content Classifiers
            indicator.setSubjectCode(contentClassifiersEditionForm.getValueAsString(IndicatorDS.SUBJECT_CODE));
            indicator.setSubjectTitle((InternationalStringDto)contentClassifiersEditionForm.getValue(IndicatorDS.SUBJECT_TITLE));
            
            uiHandlers.saveIndicator(indicator);
        }
    }
    
    public void setUiHandlers(IndicatorUiHandler uiHandlers) {
        this.uiHandlers = uiHandlers;
    }
    
}
