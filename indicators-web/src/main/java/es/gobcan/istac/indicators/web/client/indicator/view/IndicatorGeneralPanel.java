package es.gobcan.istac.indicadores.web.client.indicator.view;


import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

import es.gobcan.istac.indicadores.web.client.widgets.form.GroupDynamicForm;
import es.gobcan.istac.indicadores.web.client.widgets.form.MainFormLayout;
import es.gobcan.istac.indicadores.web.client.widgets.form.fields.ViewTextItem;
import es.gobcan.istac.indicadores.web.shared.db.Indicator;

public class IndicatorGeneralPanel extends VLayout {
	
	private MainFormLayout mainFormLayout;
	
	/* View Form */
	private ViewTextItem staticIdLogic;
	private ViewTextItem staticVersion;
	
	/* Edit Form*/
	private ViewTextItem editIdLogic;
	private ViewTextItem editVersion;
	
	
	public IndicatorGeneralPanel() {
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
		staticIdLogic = new ViewTextItem("id-ind-view", "Identificador");
		staticVersion = new ViewTextItem("version-ind-view", "Versión");
		// Identifiers Form
		GroupDynamicForm identifiersForm = new GroupDynamicForm("Identificadores del Indicador");
		identifiersForm.setFields(staticIdLogic, staticVersion);

		// General Form
		GroupDynamicForm generalForm = new GroupDynamicForm("Detalles del Indicador");
//		generalForm.setFields(staticInternationalName, staticNameItem, staticInternationalDescription, staticDescriptionItem, staticAgency);

		// Status Form
		GroupDynamicForm statusForm = new GroupDynamicForm("Estado del Indicador");
//		statusForm.setRedrawOnResize(true);
//		statusForm.setFields(staticFinalItem, staticStartDateItem, staticEndDateItem);
		
		mainFormLayout.addViewForm(identifiersForm);
		mainFormLayout.addViewForm(generalForm);
		mainFormLayout.addViewForm(statusForm);
	}
	
	
	private void createEditionForm() {
		editIdLogic = new ViewTextItem("id-ind-edit", "Identificador");
		editVersion = new ViewTextItem("version-ind-edit", "Versión");
		
		// Identifiers Form
		GroupDynamicForm identifiersEditionForm = new GroupDynamicForm("Identificadores del Indicador");
		identifiersEditionForm.setFields(editIdLogic, editVersion);
		
		// General Form
		GroupDynamicForm generalEditionForm = new GroupDynamicForm("Detalles del Indicador");
		//generalEditionForm.setFields(internationalName, nameItem, internationalDescription, descriptionItem, staticAgencyEdit);
				
		// Status Form
		GroupDynamicForm statusForm = new GroupDynamicForm("Estado del Indicador");
		//statusForm.setFields(staticFinalItemEdit, staticStartDateItemEdit, staticEndDateItemEdit);
		
		mainFormLayout.addEditionForm(identifiersEditionForm);
		mainFormLayout.addEditionForm(generalEditionForm);
		mainFormLayout.addEditionForm(statusForm);
	}
	
	public void setIndicator(Indicator indicator) {
		mainFormLayout.setViewMode();
		
		setIndicatorSystemViewMode(indicator);
		setIndicatorSystemEditionMode(indicator);
		
		// Clear errors
		/*identifiersEditionForm.clearErrors(true);
		generalEditionForm.clearErrors(true);*/
	}
	
	private void setIndicatorSystemViewMode(Indicator indicator) {
		/*Actualizamos campos con solo informacion estatica */
		staticIdLogic.setValue(indicator.getId());
		staticVersion.setValue(indicator.getVersion());
	}
	
	
	private void setIndicatorSystemEditionMode(Indicator indicator) {
		/*Actualizamos campos de edicion*/
		editIdLogic.setValue(indicator.getId());
		editVersion.setValue(indicator.getVersion());
	}
}
