package es.gobcan.istac.indicators.web.client.indicator.view;


import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.getLocalisedString;

import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.InternationalTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.layout.VLayout;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.web.client.indicator.presenter.IndicatorUiHandler;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorsDS;

public class IndicatorGeneralPanel extends VLayout {
	
    /* Data */
    private IndicatorDto indicator;
    
    /* UiHandlers */
    private IndicatorUiHandler uiHandlers;
    
	private InternationalMainFormLayout mainFormLayout;
	
	/* View Form */
	private ViewTextItem staticNameItem;
	private InternationalTextItem staticInternationalName;
	private GroupDynamicForm generalForm; 
	GroupDynamicForm identifiersForm;
	
	/* Edit Form*/
	private ViewTextItem idLogicItem;
	private ViewTextItem versionItem;
	private RequiredTextItem nameItem;
	private InternationalTextItem internationalName;
	GroupDynamicForm generalEditionForm;
	
	private boolean translationsShowed;
	
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
                setTranslationsShowed(mainFormLayout.getTranslateToolStripButton().isSelected());
                identifiersForm.setCanEdit(false);
                identifiersForm.redraw();
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
	    ViewTextItem staticVersion = new ViewTextItem(IndicatorDS.FIELD_VERSION, getConstants().indicDetailVersion());
		
		// Identifiers Form
		identifiersForm = new GroupDynamicForm(getConstants().indicDetailIdentifiers());
		identifiersForm.setFields(staticCode, staticUuid, staticVersion);

		staticNameItem = new ViewTextItem("name-ind-view", getConstants().indicDetailName());
		staticNameItem.setShowIfCondition(new FormItemIfFunction() {
		    @Override
		    public boolean execute(FormItem item, Object value, DynamicForm form) {
		        return !translationsShowed;
		    }
		});
		
		staticInternationalName = new InternationalTextItem("nameint-ind-view", getConstants().indicDetailName(), true, true);
		staticInternationalName.setShowIfCondition(new FormItemIfFunction() {
		    @Override
		    public boolean execute(FormItem item, Object value, DynamicForm form) {
		        return translationsShowed;
		    }
		});
		// General Form
		generalForm = new GroupDynamicForm(getConstants().indicDetailDetails());
		generalForm.setFields(staticNameItem, staticInternationalName);

		// Status Form
		GroupDynamicForm statusForm = new GroupDynamicForm(getConstants().indicDetailStatus());
//		statusForm.setRedrawOnResize(true);
//		statusForm.setFields(staticFinalItem, staticStartDateItem, staticEndDateItem);
		
		mainFormLayout.addViewCanvas(identifiersForm);
		mainFormLayout.addViewCanvas(generalForm);
		mainFormLayout.addViewCanvas(statusForm);
	}
	
	
	private void createEditionForm() {
		idLogicItem = new ViewTextItem("id-ind-edit", getConstants().indicDetailIdentifier());
		versionItem = new ViewTextItem("version-ind-edit", getConstants().indicDetailVersion());
		
		// Identifiers Form
		GroupDynamicForm identifiersEditionForm = new GroupDynamicForm(getConstants().indicDetailIdentifiers());
		identifiersEditionForm.setFields(idLogicItem, versionItem);
		
		
		nameItem = new RequiredTextItem("name-ind-edit", getConstants().indicDetailName());
        nameItem.setShowIfCondition(new FormItemIfFunction() {
            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !translationsShowed;
            }
        });
        
        internationalName = new InternationalTextItem("nameint-ind-edit", getConstants().indicDetailName(), false, true);
        internationalName.setShowIfCondition(new FormItemIfFunction() {
            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return translationsShowed;
            }
        });
		// General Form
		generalEditionForm = new GroupDynamicForm(getConstants().indicDetailDetails());
		generalEditionForm.setFields(internationalName, nameItem);
				
		// Status Form
		GroupDynamicForm statusForm = new GroupDynamicForm(getConstants().indicDetailStatus());
		//statusForm.setFields(staticFinalItemEdit, staticStartDateItemEdit, staticEndDateItemEdit);
		
		mainFormLayout.addEditionCanvas(identifiersEditionForm);
		mainFormLayout.addEditionCanvas(generalEditionForm);
		mainFormLayout.addEditionCanvas(statusForm);
	}
	
	public void setIndicator(IndicatorDto indicator) {
	    this.indicator = indicator;
		mainFormLayout.setViewMode();
		
		setIndicatorSystemViewMode(indicator);
		setIndicatorSystemEditionMode(indicator);
		
		// Clear errors
		/*identifiersEditionForm.clearErrors(true);
		generalEditionForm.clearErrors(true);*/
	}
	
	private void setIndicatorSystemViewMode(IndicatorDto indicator) {
		/*Actualizamos campos con solo informacion estatica */
   		/*staticIdLogic.setValue(indicator.getId());
		staticVersion.setValue(indicator.getVersionNumber());*/
	    identifiersForm.setValue(IndicatorsDS.FIELD_CODE, indicator.getId());
	    identifiersForm.setValue(IndicatorsDS.FIELD_UUID, indicator.getUuid());
	    identifiersForm.setValue(IndicatorsDS.FIELD_VERSION, indicator.getVersionNumber());
		staticNameItem.setValue(getLocalisedString(indicator.getName()));
		staticInternationalName.setValue(indicator.getName());
	}
	
	
	private void setIndicatorSystemEditionMode(IndicatorDto indicator) {
		/*Actualizamos campos de edicion*/
		idLogicItem.setValue(indicator.getId());
		versionItem.setValue(indicator.getVersionNumber());
	    nameItem.setValue(getLocalisedString(indicator.getName()));
	    internationalName.setValue(indicator.getName());
	}
	
    public void setTranslationsShowed(boolean translationsShowed) {
        this.translationsShowed = translationsShowed;
        // If forms are marked for redraw after show/hide translations in annotations panel, the annotations are not showed properly ¿?
       generalForm.markForRedraw();
       generalEditionForm.markForRedraw();
/*        generalEditionForm.markForRedraw();
        viewAnnotationsPanel.setTranslationsShowed(translationsShowed);
        editionAnnotationsPanel.setTranslationsShowed(translationsShowed);*/
    }
    
    private void saveIndicator() {
        if (generalEditionForm.validate()) {
            //TODO: solo si se muestra
            indicator.setName(internationalName.getValue(indicator.getName()));
            uiHandlers.saveIndicator(indicator);
        }
    }
    
    public void setUiHandlers(IndicatorUiHandler uiHandlers) {
        this.uiHandlers = uiHandlers;
    }
}
