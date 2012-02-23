package es.gobcan.istac.indicators.web.client.system.view;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.getLocalisedString;

import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;

import es.gobcan.istac.indicators.core.dto.serviceapi.DimensionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorInstanceDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.web.client.system.presenter.SystemPresenter;
import es.gobcan.istac.indicators.web.client.system.presenter.SystemUiHandler;

public class SystemViewImpl extends ViewImpl implements SystemPresenter.SystemView {
	private SystemUiHandler uiHandlers;
	private VLayout panel;
	private Label indSysLabel;
	private TabSet tabSet;
	private Tab generalTab;
	private Tab structureTab;
	
	//Panels
	private SystemStructurePanel structurePanel;
	private SystemGeneralPanel generalPanel;
	
	@Inject
	public SystemViewImpl() {
		this.structurePanel = new SystemStructurePanel();
		this.generalPanel = new SystemGeneralPanel();
		panel = new VLayout();
		tabSet = new TabSet();
		
		indSysLabel = new Label();
		indSysLabel.setAlign(Alignment.LEFT);
		indSysLabel.setOverflow(Overflow.HIDDEN);
		indSysLabel.setHeight(40);
		indSysLabel.setStyleName("sectionTitle");
		
		generalTab = new Tab(getConstants().systemDetailGeneral());
		generalTab.setPane(generalPanel);
		
		structureTab = new Tab(getConstants().systemDetailStructure());
		structureTab.setPane(structurePanel);
		
		tabSet.addTab(generalTab);
		tabSet.addTab(structureTab);
		
		panel.addMember(indSysLabel);
		panel.addMember(tabSet);
		
		bindEvents();
	}
	
	@Override
	public void init() {
		tabSet.selectTab(0);
	}
	
	@Override
	public Widget asWidget() {
		return panel;
	}
	
	private void bindEvents() {
		structureTab.addTabSelectedHandler(new TabSelectedHandler() {
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				uiHandlers.retrieveSystemStructure();
			}
		});
	}
	
	@Override
	public void setIndicatorsSystem(IndicatorsSystemDto indSystem) {
		indSysLabel.setContents(getLocalisedString(indSystem.getTitle()));
		structurePanel.setIndicatorsSystem(indSystem);
		generalPanel.setIndicatorsSystem(indSystem);
	}
	
	@Override
	public void setIndicatorsSystemStructure(IndicatorsSystemDto indicatosSystem, List<DimensionDto> dimensions, List<IndicatorInstanceDto> indicatorInstances) {
		structurePanel.setIndicatorSystemStructure(indicatosSystem, dimensions,indicatorInstances);
	}
	
	@Override
	public void setUiHandlers(SystemUiHandler uiHandlers) {
		this.uiHandlers = uiHandlers;
		this.structurePanel.setUiHandlers(uiHandlers);
	}
	
	@Override
	public void onDimensionSaved(DimensionDto dimension) {
		structurePanel.onDimensionSaved(dimension);
	}
	
	@Override
	public void onIndicatorInstanceSaved(IndicatorInstanceDto instance) {
		structurePanel.onIndicatorInstanceSaved(instance);
	}
	
}

