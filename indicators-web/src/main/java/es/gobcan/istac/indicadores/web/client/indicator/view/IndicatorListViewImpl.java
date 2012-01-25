package es.gobcan.istac.indicadores.web.client.indicator.view;

import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
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

import es.gobcan.istac.indicadores.web.client.NameTokens;
import es.gobcan.istac.indicadores.web.client.PlaceRequestParams;
import es.gobcan.istac.indicadores.web.client.indicator.presenter.IndicatorListPresenter;
import es.gobcan.istac.indicadores.web.client.indicator.presenter.IndicatorListUiHandler;
import es.gobcan.istac.indicadores.web.client.model.IndicatorRecord;
import es.gobcan.istac.indicadores.web.shared.db.Indicator;

public class IndicatorListViewImpl extends ViewImpl implements IndicatorListPresenter.IndicatorListView {

	private PlaceManager placeManager;
	private IndicatorListUiHandler uiHandler;
	
	private VLayout panel;
	
	private ToolStripButton newIndicatorActor;
	
	private ListGrid indicatorList;
	private Window newModal;
	
	private DynamicForm newIndForm;
	private TextItem nameInd;
	private ButtonItem createIndButton;
	
	//toolstrip
	
	@Inject
	public IndicatorListViewImpl(PlaceManager placeManager) {
		super();
		this.placeManager = placeManager;
	
		//Toolstrip
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setWidth100();
		
		newIndicatorActor = new ToolStripButton("Nuevo", "new_listgrid.png");
		toolStrip.addButton(newIndicatorActor);
		
		indicatorList = new ListGrid();
		ListGridField fieldId = new ListGridField(IndicatorRecord.IDENTIFIER, "Identificador");
		ListGridField fieldName = new ListGridField(IndicatorRecord.NAME, "Nombre");
		indicatorList.setFields(fieldId,fieldName);
		
		panel = new VLayout();
		panel.addMember(toolStrip);
		panel.addMember(indicatorList);
		
		//Modal
		newIndForm = new DynamicForm();
		newIndForm.setWidth100();
		newIndForm.setHeight100();
        newIndForm.setPadding(5);
        newIndForm.setLayoutAlign(VerticalAlignment.BOTTOM);
		
		nameInd = new TextItem("name-new-dsd", "Nombre");
		nameInd.setRequired(true);
        nameInd.setWidth(200);
        createIndButton = new ButtonItem("create-new-dsd", "Crear");
        createIndButton.setWidth(100);
        
        newIndForm.setFields(nameInd,createIndButton);
		
		bindEvents();
	}
	
	private void bindEvents() {
		newIndicatorActor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				newModal = new Window();
				newModal.setWidth(380);
	            newModal.setHeight(100);
	            newModal.setTitle("Nuevo Indicador");
	            newModal.setShowMinimizeButton(false);
	            newModal.setIsModal(true);
	            newModal.setShowModalMask(true);
	            newModal.centerInPage();
				newModal.addCloseClickHandler(new CloseClickHandler() {
					public void onCloseClick(CloseClientEvent event) {
						newModal.destroy();
					}
				});
				newModal.addItem(newIndForm);
				newModal.show(); 
			}
		});
		
		createIndButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				uiHandler.createIndicator(nameInd.getValueAsString());
				newModal.hide();
				newModal.destroy();
			}
		});
		
		indicatorList.addRecordClickHandler(new RecordClickHandler() {
			@Override
			public void onRecordClick(RecordClickEvent event) {
				String id = event.getRecord().getAttribute(IndicatorRecord.IDENTIFIER);
				PlaceRequest indicatorDetailRequest = new PlaceRequest(NameTokens.indicatorPage).with(PlaceRequestParams.indicatorParam, id);
				placeManager.revealPlace(indicatorDetailRequest);
			}
		});
	}
	
	@Override
	public Widget asWidget() {
		return panel;
	}
	
	@Override
	public void setIndicatorList(List<Indicator> indicators) {
		IndicatorRecord[] records = new IndicatorRecord[indicators.size()];
		int index = 0;
		for (Indicator ind : indicators) {
			records[index++] = new IndicatorRecord(ind.getId(), ind.getName());
		}
		indicatorList.setData(records);
	}
	
	@Override
	public void setUiHandlers(IndicatorListPresenter uiHandlers) {
		this.uiHandler = uiHandlers;
	}

}
