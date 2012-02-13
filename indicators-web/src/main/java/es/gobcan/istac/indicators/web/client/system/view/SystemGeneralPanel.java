package es.gobcan.istac.indicators.web.client.system.view;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;

public class SystemGeneralPanel extends VLayout {

	private InternationalMainFormLayout mainFormLayout;
	
	/* VIEW FORM */
	private ViewTextItem staticIdLogic;
	private ViewTextItem staticVersion;
	
	/* EDIT FORM */
	private ViewTextItem editIdLogic;
	private ViewTextItem editVersion;
	
	public SystemGeneralPanel() {
		super();
		
		mainFormLayout = new InternationalMainFormLayout();
		
		mainFormLayout.getTranslateToolStripButton().addClickHandler(new ClickHandler() {
		    @Override
            public void onClick(ClickEvent event) {
                setTranslationsShowed(mainFormLayout.getTranslateToolStripButton().isSelected());
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
		
		staticIdLogic = new ViewTextItem("id-indSys-view", getConstants().systemDetailIdentifier());
		staticVersion = new ViewTextItem("version-indSys-view", getConstants().systemDetailVersion());

		// Identifiers Form
		GroupDynamicForm identifiersForm = new GroupDynamicForm(getConstants().systemDetailIdentifiers());
		identifiersForm.setFields(staticIdLogic, staticVersion);

		// General Form
		GroupDynamicForm generalForm = new GroupDynamicForm(getConstants().systemDetailDetails());
//		generalForm.setFields(staticInternationalName, staticNameItem, staticInternationalDescription, staticDescriptionItem, staticAgency);

		// Status Form
		GroupDynamicForm statusForm = new GroupDynamicForm(getConstants().systemDetailStatus());
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
		
		editIdLogic = new ViewTextItem("id-indSys-edit", getConstants().systemDetailIdentifier());
		editVersion = new ViewTextItem("version-indSys-edit", getConstants().systemDetailVersion());

		// Identifiers Form
		GroupDynamicForm identifiersEditionForm = new GroupDynamicForm(getConstants().systemDetailIdentifiers());
		identifiersEditionForm.setFields(editIdLogic, editVersion);
		
		// General Form
		GroupDynamicForm generalEditionForm = new GroupDynamicForm(getConstants().systemDetailDetails());
		//generalEditionForm.setFields(internationalName, nameItem, internationalDescription, descriptionItem, staticAgencyEdit);
				
		// Status Form
		GroupDynamicForm statusForm = new GroupDynamicForm(getConstants().systemDetailStatus());
		//statusForm.setFields(staticFinalItemEdit, staticStartDateItemEdit, staticEndDateItemEdit);
		
		mainFormLayout.addEditionCanvas(identifiersEditionForm);
		mainFormLayout.addEditionCanvas(generalEditionForm);
		mainFormLayout.addEditionCanvas(statusForm);
	}
	
	public void setIndicatorsSystem(IndicatorsSystemDto indSystem) {
		mainFormLayout.setViewMode();
		
		setIndicatorsSystemViewMode(indSystem); 
		setIndicatorsSystemEditionMode(indSystem);
		
		// Clear errors
		/*identifiersEditionForm.clearErrors(true);
		generalEditionForm.clearErrors(true);*/
	}
	
	private void setIndicatorsSystemViewMode(IndicatorsSystemDto indicatorSystem) {
		/*Actualizamos campos con solo informacion estatica */
		staticIdLogic.setValue(indicatorSystem.getId());
		staticVersion.setValue(indicatorSystem.getVersion());
	}
	
	
	private void setIndicatorsSystemEditionMode(IndicatorsSystemDto indicatorSystem) {
		/*Actualizamos campos de edicion*/
		editIdLogic.setValue(indicatorSystem.getId());
		editVersion.setValue(indicatorSystem.getVersion());
	}
	
	public void setTranslationsShowed(boolean translationsShowed) {
    }

}
