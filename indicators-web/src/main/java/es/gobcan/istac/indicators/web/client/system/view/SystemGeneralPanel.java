package es.gobcan.istac.indicators.web.client.system.view;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorsSystemsDS;

public class SystemGeneralPanel extends VLayout {

	private InternationalMainFormLayout mainFormLayout;
	
	/* VIEW FORM */
	private GroupDynamicForm identifiersForm;
	private GroupDynamicForm generalForm;
	private GroupDynamicForm statusForm;
	
	/* EDIT FORM */
	private GroupDynamicForm identifiersEditForm;
	private GroupDynamicForm generalEditForm;
	private GroupDynamicForm statusEditForm;
	
	public SystemGeneralPanel() {
		super();
		
		mainFormLayout = new InternationalMainFormLayout();
		
		mainFormLayout.getTranslateToolStripButton().addClickHandler(new ClickHandler() {
		    @Override
            public void onClick(ClickEvent event) {
                boolean translationsShowed = mainFormLayout.getTranslateToolStripButton().isSelected();
                generalForm.setTranslationsShowed(translationsShowed);
                generalEditForm.setTranslationsShowed(translationsShowed);
            }
        });
		
		createViewForm();
		createEditionForm();
		
		this.addMember(mainFormLayout);
	}
	
	
	/**
	 * Creates and returns the view layout
	 * 
	 * @return
	 */
	private void createViewForm() {
		
		ViewTextItem codeField = new ViewTextItem(IndicatorsSystemsDS.FIELD_CODE,getConstants().systemDetailIdentifier());
		ViewTextItem uuidField = new ViewTextItem(IndicatorsSystemsDS.FIELD_UUID, getConstants().systemDetailUuid());
		ViewTextItem versionField = new ViewTextItem(IndicatorsSystemsDS.FIELD_VERSION, getConstants().systemDetailVersion());

		// Identifiers Form
		identifiersForm = new GroupDynamicForm(getConstants().systemDetailIdentifiers());
		identifiersForm.setFields(codeField, uuidField, versionField);

		// General Form
		ViewMultiLanguageTextItem titleField = new ViewMultiLanguageTextItem(IndicatorsSystemsDS.FIELD_INTERNATIONAL_TITLE, getConstants().systemDetailTitle());
		generalForm = new GroupDynamicForm(getConstants().systemDetailDetails());
		generalForm.setFields(titleField);

		// Status Form
		statusForm = new GroupDynamicForm(getConstants().systemDetailStatus());
//		statusForm.setRedrawOnResize(true);
//		statusForm.setFields(staticFinalItem, staticStartDateItem, staticEndDateItem);
		
		mainFormLayout.addViewCanvas(identifiersForm);
		mainFormLayout.addViewCanvas(generalForm);
		mainFormLayout.addViewCanvas(statusForm);
	}
	
	
	/**
	 * Creates and returns the edition layout
	 * 
	 * @return
	 */
	private void createEditionForm() {
		
		ViewTextItem codeField = new ViewTextItem(IndicatorsSystemsDS.FIELD_CODE, getConstants().systemDetailIdentifier());
		ViewTextItem uuidField = new ViewTextItem(IndicatorsSystemsDS.FIELD_UUID, getConstants().systemDetailUuid());
		ViewTextItem versionField = new ViewTextItem(IndicatorsSystemsDS.FIELD_VERSION, getConstants().systemDetailVersion());

		// Identifiers Form
		identifiersEditForm = new GroupDynamicForm(getConstants().systemDetailIdentifiers());
		identifiersEditForm.setFields(codeField, uuidField, versionField);
		
		// General Form
		MultiLanguageTextItem titleField = new MultiLanguageTextItem(IndicatorsSystemsDS.FIELD_INTERNATIONAL_TITLE, getConstants().systemDetailTitle());
		generalEditForm = new GroupDynamicForm(getConstants().systemDetailDetails());
		generalEditForm.setFields(titleField);
				
		// Status Form
		statusEditForm = new GroupDynamicForm(getConstants().systemDetailStatus());
		//statusForm.setFields(staticFinalItemEdit, staticStartDateItemEdit, staticEndDateItemEdit);
		
		mainFormLayout.addEditionCanvas(identifiersEditForm);
		mainFormLayout.addEditionCanvas(generalEditForm);
		mainFormLayout.addEditionCanvas(statusEditForm);
	}
	
	public void setIndicatorsSystem(IndicatorsSystemDto indSystem) {
		mainFormLayout.setViewMode();
		
		setIndicatorsSystemViewMode(indSystem); 
		setIndicatorsSystemEditionMode(indSystem);
		
		// Clear errors
		identifiersEditForm.clearErrors(true);
		generalEditForm.clearErrors(true);
	}
	
	private void setIndicatorsSystemViewMode(IndicatorsSystemDto indicatorSystem) {
		/*Actualizamos campos con solo informacion estatica */
		identifiersForm.setValue(IndicatorsSystemsDS.FIELD_CODE, indicatorSystem.getCode());
		identifiersForm.setValue(IndicatorsSystemsDS.FIELD_UUID, indicatorSystem.getUuid());
		identifiersForm.setValue(IndicatorsSystemsDS.FIELD_VERSION, indicatorSystem.getVersionNumber());
		
		generalForm.setValue(IndicatorsSystemsDS.FIELD_INTERNATIONAL_TITLE, RecordUtils.getInternationalStringRecord(indicatorSystem.getTitle()));
	}
	
	
	private void setIndicatorsSystemEditionMode(IndicatorsSystemDto indicatorSystem) {
		/*Actualizamos campos de edicion*/
		identifiersEditForm.setValue(IndicatorsSystemsDS.FIELD_CODE, indicatorSystem.getCode());
		identifiersEditForm.setValue(IndicatorsSystemsDS.FIELD_UUID, indicatorSystem.getUuid());
		identifiersEditForm.setValue(IndicatorsSystemsDS.FIELD_VERSION, indicatorSystem.getVersionNumber());
		
		generalEditForm.setValue(IndicatorsSystemsDS.FIELD_INTERNATIONAL_TITLE, RecordUtils.getInternationalStringRecord(indicatorSystem.getTitle()));
	}
	
}
