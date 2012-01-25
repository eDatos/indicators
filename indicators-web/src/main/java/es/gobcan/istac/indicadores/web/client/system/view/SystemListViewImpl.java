package es.gobcan.istac.indicadores.web.client.system.view;

import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

import es.gobcan.istac.indicadores.web.client.IndicadoresWeb;
import es.gobcan.istac.indicadores.web.client.model.IndicatorSystemRecord;
import es.gobcan.istac.indicadores.web.client.system.presenter.SystemListPresenter;
import es.gobcan.istac.indicadores.web.client.system.presenter.SystemListUiHandler;
import es.gobcan.istac.indicadores.web.shared.db.IndicatorSystem;

public class SystemListViewImpl extends ViewImpl implements SystemListPresenter.SystemListView {

	private SystemListUiHandler uiHandler;
	
	
	private final ListGrid indSystemListGrid;
	private Window newModal;
	private VLayout vLayout;
	
	private ToolStripButton newIndSystemActor;
	
	private DynamicForm newIndSysForm;
	private TextItem nameIndSys;
	private ButtonItem createIndSysButton;

	@Inject
	public SystemListViewImpl() {
		//ToolStrip
		newIndSystemActor = new ToolStripButton(IndicadoresWeb.getMessages().newItem(),"new_listgrid.png");
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setWidth100();
		toolStrip.addButton(newIndSystemActor);
		
		//List
		ListGridField field1 = new ListGridField(IndicatorSystemRecord.IDENTIFIER,"Identificador");
		ListGridField field2 = new ListGridField(IndicatorSystemRecord.NAME,"Nombre");
		
		indSystemListGrid = new ListGrid();
		indSystemListGrid.setFields(field1,field2);
		
		IndicatorSystemRecord[] records = new IndicatorSystemRecord[0];

		indSystemListGrid.setData(records);
		vLayout = new VLayout();
		vLayout.addMember(toolStrip);
		vLayout.addMember(indSystemListGrid);
		
		
		//Modals
		
		newIndSysForm = new DynamicForm();
		newIndSysForm.setWidth100();
		newIndSysForm.setHeight100();
        newIndSysForm.setPadding(5);
        newIndSysForm.setLayoutAlign(VerticalAlignment.BOTTOM);
		
		nameIndSys = new TextItem("name-new-dsd", "Nombre");
		nameIndSys.setRequired(true);
        nameIndSys.setWidth(200);
        createIndSysButton = new ButtonItem("create-new-dsd", "Crear");
        createIndSysButton.setWidth(100);
        
        newIndSysForm.setFields(nameIndSys,createIndSysButton);
        
		bindEvents();
	}
	
	private void bindEvents() {
		//New Indicator System
		newIndSystemActor.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				newModal = new Window();
                newModal.setWidth(380);
                newModal.setHeight(100);
                newModal.setTitle("Nuevo Sistema de Indicadores");
                newModal.setShowMinimizeButton(false);
                newModal.setIsModal(true);
                newModal.setShowModalMask(true);
                newModal.centerInPage();
				newModal.addCloseClickHandler(new CloseClickHandler() {
					public void onCloseClick(CloseClientEvent event) {
						newModal.destroy();
					}
				});
				newModal.addItem(newIndSysForm);
				newModal.show(); 
			}
		});
		
		//Create Indicator System in Modal
		createIndSysButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				uiHandler.createIndicatorSystem(nameIndSys.getValueAsString());
				newModal.hide();
				newModal.destroy();
			}
		});
		
		indSystemListGrid.addRecordClickHandler(new RecordClickHandler() {
			@Override
			public void onRecordClick(RecordClickEvent event) {
				String id = event.getRecord().getAttribute(IndicatorSystemRecord.IDENTIFIER);
				uiHandler.goToIndicatorSystem(id);
			}
		});
	}
	
	@Override
	public void setIndSystemList(List<IndicatorSystem> indSysList) {
		IndicatorSystemRecord[] records = new IndicatorSystemRecord[indSysList.size()];
		int index = 0;
		for (IndicatorSystem indSys : indSysList) {
			records[index++] = new IndicatorSystemRecord(indSys.getId(), indSys.getName());
		}
		indSystemListGrid.setData(records);
	}
	
	@Override
	public Widget asWidget() {
		return vLayout;
	}

	@Override
	public void setUiHandlers(SystemListUiHandler uiHandlers) {
		this.uiHandler = uiHandlers;
	}

}
