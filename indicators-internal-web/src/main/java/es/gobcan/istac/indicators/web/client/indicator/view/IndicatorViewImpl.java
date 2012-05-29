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

import es.gobcan.istac.indicators.core.dto.DataDefinitionDto;
import es.gobcan.istac.indicators.core.dto.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.DataStructureDto;
import es.gobcan.istac.indicators.core.dto.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;
import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.IndicatorSummaryDto;
import es.gobcan.istac.indicators.core.dto.QuantityUnitDto;
import es.gobcan.istac.indicators.core.dto.SubjectDto;
import es.gobcan.istac.indicators.web.client.enums.IndicatorCalculationTypeEnum;
import es.gobcan.istac.indicators.web.client.enums.RateDerivationTypeEnum;
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
    public void setDiffusionIndicator(IndicatorDto indicator) {
        generalPanel.setDiffusionIndicator(indicator);
    }

    @Override
    public void setQuantityUnits(List<QuantityUnitDto> units) {
        generalPanel.setQuantityUnits(units);
        dataSourcesPanel.setQuantityUnits(units);
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
        dataSourcesPanel.setGeographicalValues(geographicalValueDtos);
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
    public void setDataStructureView(DataStructureDto dataStructureDto) {
        dataSourcesPanel.setDataStructureView(dataStructureDto);
    }

    @Override
    public void setDataStructureEdition(DataStructureDto dataStructureDto) {
        dataSourcesPanel.setDataStructureEdition(dataStructureDto);
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

    @Override
    public void setIndicatorListQuantityDenominator(List<IndicatorSummaryDto> indicators) {
        generalPanel.setIndicatorListQuantityDenominator(indicators);
    }

    @Override
    public void setIndicatorListQuantityNumerator(List<IndicatorSummaryDto> indicators) {
        generalPanel.setIndicatorListQuantityNumerator(indicators);
    }

    @Override
    public void setIndicatorListQuantityIndicatorBase(List<IndicatorSummaryDto> indicators) {
        generalPanel.setIndicatorListQuantityIndicatorBase(indicators);
    }

    @Override
    public void setIndicatorQuantityDenominator(IndicatorDto indicator) {
        generalPanel.setIndicatorQuantityDenominator(indicator);
    }

    @Override
    public void setIndicatorQuantityNumerator(IndicatorDto indicator) {
        generalPanel.setIndicatorQuantityNumerator(indicator);
    }

    @Override
    public void setIndicatorQuantityIndicatorBase(IndicatorDto indicator) {
        generalPanel.setIndicatorQuantityIndicatorBase(indicator);
    }

    @Override
    public void setDataDefinitionsOperationCodes(List<String> operationCodes) {
        dataSourcesPanel.setDataDefinitionsOperationCodes(operationCodes);
    }

    @Override
    public void setRateIndicators(List<IndicatorSummaryDto> indicatorDtos, RateDerivationTypeEnum rateDerivationTypeEnum, IndicatorCalculationTypeEnum indicatorCalculationTypeEnum) {
        dataSourcesPanel.setRateIndicators(indicatorDtos, rateDerivationTypeEnum, indicatorCalculationTypeEnum);
    }

    @Override
    public void setRateIndicator(IndicatorDto indicatorDto, RateDerivationTypeEnum rateDerivationTypeEnum, IndicatorCalculationTypeEnum indicatorCalculationTypeEnum) {
        dataSourcesPanel.setRateIndicator(indicatorDto, rateDerivationTypeEnum, indicatorCalculationTypeEnum);
    }

}
