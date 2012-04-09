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

import es.gobcan.istac.indicators.core.dto.serviceapi.DataDefinitionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.DataStructureDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.GeographicalValueDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.QuantityUnitDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.SubjectDto;
import es.gobcan.istac.indicators.web.client.indicator.presenter.IndicatorPresenter;

public class IndicatorViewImpl extends ViewImpl implements IndicatorPresenter.IndicatorView {

    private VLayout               panel;
    private TitleLabel            indicatorLabel;
    private TabSet                tabset;
    private IndicatorGeneralPanel generalPanel;
    private DataSourcesPanel      dataSourcesPanel;

    @Inject
    public IndicatorViewImpl(IndicatorGeneralPanel genPanel, DataSourcesPanel dataSourcesPanel) {
        this.generalPanel = genPanel;
        this.dataSourcesPanel = dataSourcesPanel;

        indicatorLabel = new TitleLabel();
        indicatorLabel.setStyleName("sectionTitleLeftMargin");

        Tab generalTab = new Tab(getConstants().indicDetailGeneral());
        generalTab.setPane(generalPanel);

        Tab dataSourcesTab = new Tab(getConstants().indicDetailDataSources());
        dataSourcesTab.setPane(dataSourcesPanel);

        tabset = new TabSet();
        tabset.addTab(generalTab);
        tabset.addTab(dataSourcesTab);

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
        dataSourcesPanel.setUiHandlers(uiHandlers);
    }

    @Override
    public void setIndicator(IndicatorDto indicator) {
        indicatorLabel.setContents(getLocalisedString(indicator.getTitle()));
        generalPanel.setIndicator(indicator);
        dataSourcesPanel.setIndicator(indicator);
    }

    @Override
    public void setQuantityUnits(List<QuantityUnitDto> units) {
        generalPanel.setQuantityUnits(units);
        dataSourcesPanel.setQuantityUnits(units);
    }

    @Override
    public void setIndicatorList(List<IndicatorDto> indicators) {
        generalPanel.setIndicatorList(indicators);
        dataSourcesPanel.setIndicatorList(indicators);
    }

    @Override
    public void setSubjectsList(List<SubjectDto> subjectDtos) {
        generalPanel.setSubjectsList(subjectDtos);
    }

    @Override
    public void setGeographicalGranularities(List<GeographicalGranularityDto> geographicalGranularityDtos) {
        generalPanel.setGeographicalGranularities(geographicalGranularityDtos);
        dataSourcesPanel.setGeographicalGranularities(geographicalGranularityDtos);
    }

    @Override
    public void setGeographicalValues(List<GeographicalValueDto> geographicalValueDtos) {
        generalPanel.setGeographicalValues(geographicalValueDtos);
    }

    @Override
    public void setGeographicalValue(GeographicalValueDto geographicalValueDto) {
        generalPanel.setGeographicalValue(geographicalValueDto);
    }

    @Override
    public void setIndicatorDataSources(List<DataSourceDto> dataSourceDtos) {
        dataSourcesPanel.setDataSources(dataSourceDtos);
    }

    @Override
    public void setDataDefinitions(List<DataDefinitionDto> dataDefinitionDtos) {
        dataSourcesPanel.setDataDefinitions(dataDefinitionDtos);
    }

    @Override
    public void setDataStructure(DataStructureDto dataStructureDto) {
        dataSourcesPanel.setDataStructure(dataStructureDto);
    }

    @Override
    public void setDataDefinition(DataDefinitionDto datDefinitionDto) {
        dataSourcesPanel.setDataDefinition(datDefinitionDto);
    }

    @Override
    public void setGeographicalValuesDS(List<GeographicalValueDto> geographicalValueDtos) {
        dataSourcesPanel.setGeographicalValues(geographicalValueDtos);
    }

    @Override
    public void setGeographicalValueDS(GeographicalValueDto geographicalValueDto) {
        dataSourcesPanel.setGeographicalValue(geographicalValueDto);
    }

    @Override
    public void onDataSourceSaved(DataSourceDto dataSourceDto) {
        dataSourcesPanel.onDataSourceSaved(dataSourceDto);
    }

}
