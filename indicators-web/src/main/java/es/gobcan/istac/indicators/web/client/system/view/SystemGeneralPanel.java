package es.gobcan.istac.indicators.web.client.system.view;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

import es.gobcan.istac.indicators.web.client.widgets.form.GroupDynamicForm;
import es.gobcan.istac.indicators.web.client.widgets.form.MainFormLayout;
import es.gobcan.istac.indicators.web.client.widgets.form.fields.ViewTextItem;
import es.gobcan.istac.indicators.web.shared.db.IndicatorSystem;

public class SystemGeneralPanel extends VLayout {

	private MainFormLayout mainFormLayout;
	
	/* VIEW FORM */
	private ViewTextItem staticIdLogic;
	private ViewTextItem staticVersion;
	
	/* EDIT FORM */
	private ViewTextItem editIdLogic;
	private ViewTextItem editVersion;
	
	public SystemGeneralPanel() {
		super();
		
		mainFormLayout = new MainFormLayout();
		mainFormLayout.getTranslateToolStripButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				/*setTranslationsShowed(mainFormLayout.getTranslateToolStripButton().isSelected());
				generalForm.markForRedraw();
				generalEditionForm.markForRedraw();*/
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
		
		staticIdLogic = new ViewTextItem("id-indSys-view", "Identificador");
		staticVersion = new ViewTextItem("version-indSys-view", "Versión");

		// Identifiers Form
		GroupDynamicForm identifiersForm = new GroupDynamicForm("Identificadores del Sistema");
		identifiersForm.setFields(staticIdLogic, staticVersion);

		// General Form
		GroupDynamicForm generalForm = new GroupDynamicForm("Detalles del Sistema");
//		generalForm.setFields(staticInternationalName, staticNameItem, staticInternationalDescription, staticDescriptionItem, staticAgency);

		// Status Form
		GroupDynamicForm statusForm = new GroupDynamicForm("Estado del Sistema");
//		statusForm.setRedrawOnResize(true);
//		statusForm.setFields(staticFinalItem, staticStartDateItem, staticEndDateItem);
		
		mainFormLayout.addViewForm(identifiersForm);
		mainFormLayout.addViewForm(generalForm);
		mainFormLayout.addViewForm(statusForm);
	}
	
	
	/**
	 * Creates and returns the edition layout
	 * 
	 * @return
	 */
	private void createEditionForm() {
		
		editIdLogic = new ViewTextItem("id-indSys-edit", "Identificador");
		editVersion = new ViewTextItem("version-indSys-edit", "Versión");

		// Identifiers Form
		GroupDynamicForm identifiersEditionForm = new GroupDynamicForm("Identificadores del Sistema");
		identifiersEditionForm.setFields(editIdLogic, editVersion);
		
		// General Form
		GroupDynamicForm generalEditionForm = new GroupDynamicForm("Detalles del Sistema");
		//generalEditionForm.setFields(internationalName, nameItem, internationalDescription, descriptionItem, staticAgencyEdit);
				
		// Status Form
		GroupDynamicForm statusForm = new GroupDynamicForm("Estado del Sistema");
		//statusForm.setFields(staticFinalItemEdit, staticStartDateItemEdit, staticEndDateItemEdit);
		
		mainFormLayout.addEditionForm(identifiersEditionForm);
		mainFormLayout.addEditionForm(generalEditionForm);
		mainFormLayout.addEditionForm(statusForm);
	}
	
	public void setIndicatorSystem(IndicatorSystem indSystem) {
		mainFormLayout.setViewMode();
		
		setIndicatorSystemViewMode(indSystem);
		setIndicatorSystemEditionMode(indSystem);
		
		// Clear errors
		/*identifiersEditionForm.clearErrors(true);
		generalEditionForm.clearErrors(true);*/
	}
	
	private void setIndicatorSystemViewMode(IndicatorSystem indicatorSystem) {
		/*Actualizamos campos con solo informacion estatica */
		staticIdLogic.setValue(indicatorSystem.getId());
		staticVersion.setValue(indicatorSystem.getVersion());
	}
	
	
	private void setIndicatorSystemEditionMode(IndicatorSystem indicatorSystem) {
		/*Actualizamos campos de edicion*/
		editIdLogic.setValue(indicatorSystem.getId());
		editVersion.setValue(indicatorSystem.getVersion());
	}

}
