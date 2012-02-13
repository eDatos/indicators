package es.gobcan.istac.indicators.web.client.indicator.view;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.getLocalisedString;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.web.client.indicator.presenter.IndicatorPresenter;
import es.gobcan.istac.indicators.web.client.indicator.presenter.IndicatorUiHandler;

public class IndicatorViewImpl extends ViewImpl implements IndicatorPresenter.IndicatorView {

	private VLayout panel;
	private Label indicatorLabel;
	private TabSet tabset;
	private IndicatorGeneralPanel generalPanel;
	
	@Inject
	public IndicatorViewImpl(IndicatorGeneralPanel genPanel) {
		
		this.generalPanel = genPanel;
		
		indicatorLabel = new Label();
		indicatorLabel.setAlign(Alignment.LEFT);
		indicatorLabel.setOverflow(Overflow.HIDDEN);
		indicatorLabel.setHeight(40);
		indicatorLabel.setStyleName("sectionTitle");
		
		Tab generalTab = new Tab(getConstants().indicDetailGeneral());
		generalTab.setPane(generalPanel);
		
		tabset = new TabSet();
		tabset.addTab(generalTab);
		
		panel = new VLayout();
		panel.addMember(indicatorLabel);
		panel.addMember(tabset);
	}
	
	@Override
	public Widget asWidget() {
		return panel;
	}
	
	@Override
	public void setUiHandlers(IndicatorPresenter uiHandlers) {
	    generalPanel.setUiHandlers(uiHandlers);
	}

	@Override
	public void setIndicator(IndicatorDto indicator) {
		this.indicatorLabel.setContents(getLocalisedString(indicator.getName()));
		this.generalPanel.setIndicator(indicator);
	}

}
