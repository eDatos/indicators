package es.gobcan.istac.indicators.web.client.system.view;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.getLocalisedString;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.web.client.model.IndicatorSystemRecord;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorsSystemsDS;
import es.gobcan.istac.indicators.web.client.system.presenter.SystemListPresenter.SystemListView;
import es.gobcan.istac.indicators.web.client.system.presenter.SystemListUiHandler;

public class SystemListViewImpl extends ViewImpl implements SystemListView {

	private SystemListUiHandler uiHandlers;
	
	private final ListGrid indSystemListGrid;
	private VLayout panel;
	
	
	@Inject
	public SystemListViewImpl() {
		indSystemListGrid = new ListGrid();
		IndicatorsSystemsDS datasource = new IndicatorsSystemsDS();
		indSystemListGrid.setDataSource(datasource);
		indSystemListGrid.setUseAllDataSourceFields(false);
		indSystemListGrid.setLeaveScrollbarGap(false);
		
		//List
		ListGridField field1 = new ListGridField(IndicatorsSystemsDS.CODE,getConstants().systemListHeaderIdentifier());
		field1.setAlign(Alignment.LEFT);
		ListGridField field2 = new ListGridField(IndicatorsSystemsDS.TITLE,getConstants().systemListHeaderTitle());
		indSystemListGrid.setFields(field1,field2);
		
		IndicatorSystemRecord[] records = new IndicatorSystemRecord[0];

		indSystemListGrid.setData(records);
		
		panel = new VLayout();
		panel.addMember(indSystemListGrid);
		
		bindEvents();
	}
	
	private void bindEvents() {
		indSystemListGrid.addRecordClickHandler(new RecordClickHandler() {
			@Override
			public void onRecordClick(RecordClickEvent event) {
				String code = event.getRecord().getAttribute(IndicatorsSystemsDS.CODE);
				uiHandlers.goToIndicatorsSystem(code);
			}
		});
	}
	
	@Override
	public void setIndSystemList(List<IndicatorsSystemDto> indicatorsSystemList) {
		IndicatorSystemRecord[] records = new IndicatorSystemRecord[indicatorsSystemList.size()];
		int index = 0;
		for (IndicatorsSystemDto indSys : indicatorsSystemList) {
			records[index++] = new IndicatorSystemRecord(indSys.getUuid(), indSys.getCode(), getLocalisedString(indSys.getTitle()));
		}
		indSystemListGrid.setData(records);
	}
	
	@Override
	public Widget asWidget() {
		return panel;
	}

	@Override
	public void setUiHandlers(SystemListUiHandler uiHandlers) {
		this.uiHandlers = uiHandlers;
	}
	
	/* Util */
	public List<String> getCodesFromSelected() {
		List<String> codes = new ArrayList<String>();
		for (ListGridRecord record : indSystemListGrid.getSelectedRecords()) {
			codes.add(record.getAttribute(IndicatorsSystemsDS.CODE));
		}
		return codes;
	}

}
