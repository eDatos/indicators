package es.gobcan.istac.indicators.web.client.indicator.view;


import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import org.siemac.metamac.core.common.dto.serviceapi.InternationalStringDto;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.web.client.indicator.presenter.IndicatorUiHandler;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorsDS;

public class IndicatorGeneralPanel extends VLayout {
	
    /* Data */
    private IndicatorDto indicator;
    
    /* UiHandlers */
    private IndicatorUiHandler uiHandlers;
    
	private InternationalMainFormLayout mainFormLayout;
	
	/* View Form */
	private GroupDynamicForm generalForm; 
	private GroupDynamicForm identifiersForm;
	private GroupDynamicForm statusForm; 
	
	/* Edit Form*/
	private GroupDynamicForm identifiersEditionForm;
	private GroupDynamicForm generalEditionForm;
	private GroupDynamicForm statusEditionForm; 
	
	
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
	        	generalForm.setTranslationsShowed(translationsShowed);
	        	generalEditionForm.setTranslationsShowed(translationsShowed);
                generalForm.markForRedraw();
                generalEditionForm.markForRedraw();
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
	    ViewTextItem staticCode = new ViewTextItem(IndicatorsDS.FIELD_CODE, getConstants().indicDetailIdentifier());
	    ViewTextItem staticUuid = new ViewTextItem(IndicatorsDS.FIELD_UUID, getConstants().indicDetailUuid());
	    ViewTextItem staticVersion = new ViewTextItem(IndicatorsDS.FIELD_VERSION, getConstants().indicDetailVersion());
	    
	    ViewTextItem staticState = new ViewTextItem(IndicatorsDS.FIELD_STATE, getConstants().indicDetailState());
	    ViewTextItem staticCreatedDate = new ViewTextItem(IndicatorsDS.FIELD_CREATED_DATE, getConstants().indicDetailCreatedDate());
		
		// Identifiers Form
		identifiersForm = new GroupDynamicForm(getConstants().indicDetailIdentifiers());
		identifiersForm.setFields(staticCode, staticUuid, staticVersion);

		ViewMultiLanguageTextItem staticInternationalName = new ViewMultiLanguageTextItem(IndicatorsDS.FIELD_INTERNATIONAL_NAME, getConstants().indicDetailName());
		// General Form
		generalForm = new GroupDynamicForm(getConstants().indicDetailDetails());
		generalForm.setFields(staticInternationalName);

		// Status Form
		statusForm = new GroupDynamicForm(getConstants().indicDetailStatus());
//		statusForm.setRedrawOnResize(true);
		statusForm.setFields(staticState, staticCreatedDate);
		
		mainFormLayout.addViewCanvas(identifiersForm);
		mainFormLayout.addViewCanvas(generalForm);
		mainFormLayout.addViewCanvas(statusForm);
	}
	
	
	private void createEditionForm() {
	    ViewTextItem staticCode = new ViewTextItem(IndicatorsDS.FIELD_CODE, getConstants().indicDetailIdentifier());
	    ViewTextItem staticUuid = new ViewTextItem(IndicatorsDS.FIELD_UUID, getConstants().indicDetailUuid());
	    ViewTextItem staticVersion = new ViewTextItem(IndicatorsDS.FIELD_VERSION, getConstants().indicDetailVersion());
	    ViewTextItem staticState = new ViewTextItem(IndicatorsDS.FIELD_STATE, getConstants().indicDetailState());
        ViewTextItem staticCreatedDate = new ViewTextItem(IndicatorsDS.FIELD_CREATED_DATE, getConstants().indicDetailCreatedDate());
		
		// Identifiers Form
		identifiersEditionForm = new GroupDynamicForm(getConstants().indicDetailIdentifiers());
		identifiersEditionForm.setFields(staticCode, staticUuid, staticVersion);
		
		
		MultiLanguageTextItem internationalName = new MultiLanguageTextItem(IndicatorsDS.FIELD_INTERNATIONAL_NAME, getConstants().indicDetailName());
		internationalName.setRequired(true);
		// General Form
		generalEditionForm = new GroupDynamicForm(getConstants().indicDetailDetails());
		generalEditionForm.setFields(internationalName);
				
		// Status Form
		statusEditionForm = new GroupDynamicForm(getConstants().indicDetailStatus());
		statusEditionForm.setFields(staticState,staticCreatedDate);
		
		mainFormLayout.addEditionCanvas(identifiersEditionForm);
		mainFormLayout.addEditionCanvas(generalEditionForm);
		mainFormLayout.addEditionCanvas(statusEditionForm);
	}
	
	public void setIndicator(IndicatorDto indicator) {
	    this.indicator = indicator;
		mainFormLayout.setViewMode();
		
		setIndicatorViewMode(indicator);
		setIndicatorEditionMode(indicator);
		
		// Clear errors
		identifiersEditionForm.clearErrors(true);
		generalEditionForm.clearErrors(true);
	}
	
	private void setIndicatorViewMode(IndicatorDto indicator) {
		/*Updating static fields only*/
	    identifiersForm.setValue(IndicatorsDS.FIELD_CODE, indicator.getCode());
	    identifiersForm.setValue(IndicatorsDS.FIELD_UUID, indicator.getUuid());
	    identifiersForm.setValue(IndicatorsDS.FIELD_VERSION, indicator.getVersionNumber());
	    
	    generalForm.setValue(IndicatorsDS.FIELD_INTERNATIONAL_NAME, RecordUtils.getInternationalStringRecord(indicator.getName()));
	    //TODO: SHow localized string for Enums
	    statusForm.setValue(IndicatorsDS.FIELD_STATE, indicator.getState().getName());
	    statusForm.setValue(IndicatorsDS.FIELD_CREATED_DATE, indicator.getCreatedDate());
	}
	
	
	private void setIndicatorEditionMode(IndicatorDto indicator) {
		/*Actualizamos campos de edicion*/
		identifiersEditionForm.setValue(IndicatorsDS.FIELD_CODE, indicator.getCode());
		identifiersEditionForm.setValue(IndicatorsDS.FIELD_UUID, indicator.getUuid());
		identifiersEditionForm.setValue(IndicatorsDS.FIELD_VERSION, indicator.getVersionNumber());
		
	    generalEditionForm.setValue(IndicatorsDS.FIELD_INTERNATIONAL_NAME, RecordUtils.getInternationalStringRecord(indicator.getName()));
	    
	    statusEditionForm.setValue(IndicatorsDS.FIELD_STATE, indicator.getState().getName());
	    statusEditionForm.setValue(IndicatorsDS.FIELD_CREATED_DATE, indicator.getCreatedDate());
	}
	
    private void saveIndicator() {
        if (generalEditionForm.validate()) {
            indicator.setName((InternationalStringDto)generalEditionForm.getValue(IndicatorsDS.FIELD_INTERNATIONAL_NAME));
            uiHandlers.saveIndicator(indicator);
        }
    }
    
    public void setUiHandlers(IndicatorUiHandler uiHandlers) {
        this.uiHandlers = uiHandlers;
    }
}
