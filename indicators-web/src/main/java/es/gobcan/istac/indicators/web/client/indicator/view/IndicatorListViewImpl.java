package es.gobcan.istac.indicators.web.client.indicator.view;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;
import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.getLocalisedString;

import java.util.List;

import org.siemac.metamac.core.common.dto.serviceapi.InternationalStringDto;
import org.siemac.metamac.core.common.dto.serviceapi.InternationalStringDtoBase;
import org.siemac.metamac.core.common.dto.serviceapi.LocalisedStringDto;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
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

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.web.client.NameTokens;
import es.gobcan.istac.indicators.web.client.PlaceRequestParams;
import es.gobcan.istac.indicators.web.client.indicator.presenter.IndicatorListPresenter;
import es.gobcan.istac.indicators.web.client.indicator.presenter.IndicatorListUiHandler;
import es.gobcan.istac.indicators.web.client.model.IndicatorRecord;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorsDS;

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
		
		newIndicatorActor = new ToolStripButton(getConstants().indicNew(), RESOURCE.newListGrid().getURL());
		toolStrip.addButton(newIndicatorActor);
		
		indicatorList = new ListGrid();
		IndicatorsDS indicatorsDS = new IndicatorsDS();
		indicatorList.setDataSource(indicatorsDS);
		indicatorList.setLeaveScrollbarGap(false);
		indicatorList.setUseAllDataSourceFields(false);
		
		ListGridField fieldCode = new ListGridField(IndicatorsDS.FIELD_CODE, getConstants().indicListHeaderIdentifier());
		fieldCode.setAlign(Alignment.LEFT);
		ListGridField fieldName = new ListGridField(IndicatorsDS.FIELD_INTERNATIONAL_NAME, getConstants().indicListHeaderName());
		indicatorList.setFields(fieldCode, fieldName);
		
		panel = new VLayout();
		panel.addMember(toolStrip);
		panel.addMember(indicatorList);
		
		//Modal
		newIndForm = new DynamicForm();
		newIndForm.setWidth100();
		newIndForm.setHeight100();
        newIndForm.setPadding(5);
        newIndForm.setLayoutAlign(VerticalAlignment.BOTTOM);
		
		nameInd = new TextItem("name-new-dsd", getConstants().indicNewName());
		nameInd.setRequired(true);
        nameInd.setWidth(200);
        createIndButton = new ButtonItem("create-new-dsd", getConstants().indicNewCreate());
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
	            newModal.setTitle(getConstants().indicNewTitle());
	            newModal.setShowMinimizeButton(false);
	            newModal.setIsModal(true);
	            newModal.setShowModalMask(true);
	            newModal.centerInPage();
				newModal.addCloseClickHandler(new CloseClickHandler() {
                    @Override
                    public void onCloseClick(CloseClickEvent event) {
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
			    IndicatorDto indicator = new IndicatorDto();
			    indicator.setName(createDefaultIntString(nameInd.getValueAsString()));
				uiHandler.createIndicator(indicator);
				newModal.hide();
				newModal.destroy();
			}
		});
		
		indicatorList.addRecordClickHandler(new RecordClickHandler() {
			@Override
			public void onRecordClick(RecordClickEvent event) {
			    Record record = event.getRecord();
			    String uuid = record.getAttribute(IndicatorsDS.FIELD_UUID);
				PlaceRequest indicatorDetailRequest = new PlaceRequest(NameTokens.indicatorPage).with(PlaceRequestParams.indicatorParam, uuid);
				placeManager.revealPlace(indicatorDetailRequest);
			}
		});
	}
	
	@Override
	public Widget asWidget() {
		return panel;
	}
	
	@Override
	public void setIndicatorList(List<IndicatorDto> indicators) {
		IndicatorRecord[] records = new IndicatorRecord[indicators.size()];
		int index = 0;
		for (IndicatorDto ind : indicators) {
			records[index++] = new IndicatorRecord(ind.getUuid(), ind.getCode(), getLocalisedString(ind.getName()));
		}
		indicatorList.setData(records);
	}
	
	@Override
	public void setUiHandlers(IndicatorListPresenter uiHandlers) {
		this.uiHandler = uiHandlers;
	}
	

	private InternationalStringDto createDefaultIntString(String text) {
	    InternationalStringDto intString = new InternationalStringDto();
	    LocalisedStringDto locString = new LocalisedStringDto();
	    locString.setLocale(InternationalStringUtils.getCurrentLocale());
	    locString.setLabel(text);
	    intString.addText(locString);
	    return intString;
	}
}
