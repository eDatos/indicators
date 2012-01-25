package es.gobcan.istac.indicators.web.client.system.view;

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

import es.gobcan.istac.indicators.web.client.system.presenter.SystemPresenter;
import es.gobcan.istac.indicators.web.client.system.presenter.SystemUiHandler;
import es.gobcan.istac.indicators.web.shared.db.IndicatorSystem;
import es.gobcan.istac.indicators.web.shared.db.IndicatorSystemContent;

public class SystemViewImpl extends ViewImpl implements SystemPresenter.SystemView {
	private SystemUiHandler uiHandler;
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
		
		generalTab = new Tab("General");
		generalTab.setPane(generalPanel);
		
		structureTab = new Tab("Estructura");
		structureTab.setPane(structurePanel);
		
		tabSet.addTab(generalTab);
		tabSet.addTab(structureTab);
		
		panel.addMember(indSysLabel);
		panel.addMember(tabSet);
		
		bindEvents();
	}
	
	@Override
	public void init() {
		tabSet.setSelectedTab(0);
	}
	
	@Override
	public Widget asWidget() {
		return panel;
	}
	
	private void bindEvents() {
		structureTab.addTabSelectedHandler(new TabSelectedHandler() {
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				uiHandler.retrieveSystemStructure();
			}
		});
	}
	
	@Override
	public void setIndicatorSystem(IndicatorSystem indSystem) {
		indSysLabel.setContents(indSystem.getName());
		structurePanel.setIndicatorSystem(indSystem);
		generalPanel.setIndicatorSystem(indSystem);
	}
	
	@Override
	public void setIndicatorSystemStructure(List<IndicatorSystemContent> content) {
		structurePanel.setIndicatorSystemStructure(content);
	}
	
	@Override
	public void setUiHandlers(SystemUiHandler uiHandlers) {
		this.uiHandler = uiHandlers;
	}
	
}

