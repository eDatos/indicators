package es.gobcan.istac.indicators.web.client.indicator.view;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.getLocalisedString;

import java.util.List;

import org.siemac.metamac.web.common.client.widgets.TitleLabel;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

import es.gobcan.istac.indicators.core.dto.serviceapi.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.GeographicalValueDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.QuantityUnitDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.SubjectDto;
import es.gobcan.istac.indicators.web.client.indicator.presenter.IndicatorPresenter;

public class IndicatorViewImpl extends ViewImpl implements IndicatorPresenter.IndicatorView {

	private VLayout panel;
	private TitleLabel indicatorLabel;
	private TabSet tabset;
	private IndicatorGeneralPanel generalPanel;
	
	@Inject
	public IndicatorViewImpl(IndicatorGeneralPanel genPanel) {
		this.generalPanel = genPanel;
		
		indicatorLabel = new TitleLabel();
		
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
		indicatorLabel.setContents(getLocalisedString(indicator.getTitle()));
		generalPanel.setIndicator(indicator);
	}

    @Override
    public void setQuantityUnits(List<QuantityUnitDto> units) {
        generalPanel.setQuantityUnits(units);
    }

    @Override
    public void setIndicatorList(List<IndicatorDto> indicators) {
        generalPanel.setIndicatorList(indicators);
    }

    @Override
    public void setSubjectsList(List<SubjectDto> subjectDtos) {
        generalPanel.setSubjectsList(subjectDtos);
    }

    @Override
    public void setGeographicalGranularities(List<GeographicalGranularityDto> geographicalGranularityDtos) {
        generalPanel.setGeographicalGranularities(geographicalGranularityDtos);
    }

    @Override
    public void setGeographicalValues(List<GeographicalValueDto> geographicalValueDtos) {
        generalPanel.setGeographicalValues(geographicalValueDtos);
    }

    @Override
    public void setGeographicalValue(GeographicalValueDto geographicalValueDto) {
        generalPanel.setGeographicalValue(geographicalValueDto);
    }

}
