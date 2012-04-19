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

import es.gobcan.istac.indicators.core.dto.DimensionDto;
import es.gobcan.istac.indicators.core.dto.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;
import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.IndicatorInstanceDto;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemStructureDto;
import es.gobcan.istac.indicators.core.enume.domain.TimeGranularityEnum;
import es.gobcan.istac.indicators.web.client.system.presenter.SystemPresenter;
import es.gobcan.istac.indicators.web.client.system.presenter.SystemUiHandler;
import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemDtoWeb;

public class SystemViewImpl extends ViewImpl implements SystemPresenter.SystemView {

    private SystemUiHandler      uiHandlers;
    private VLayout              panel;
    private Label                indSysLabel;
    private TabSet               tabSet;
    private Tab                  generalTab;
    private Tab                  structureTab;

    // Panels
    private SystemGeneralPanel   generalPanel;
    private SystemStructurePanel structurePanel;

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
        indSysLabel.setStyleName("sectionTitleLeftMargin");

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
    public void setIndicatorsSystem(IndicatorsSystemDtoWeb indSystem) {
        indSysLabel.setContents(getLocalisedString(indSystem.getTitle()));
        generalPanel.setIndicatorsSystem(indSystem);
        structurePanel.setIndicatorsSystem(indSystem);
    }

    @Override
    public void setIndicatorsSystemStructure(IndicatorsSystemDtoWeb indicatosSystem, IndicatorsSystemStructureDto structure) {
        structurePanel.setIndicatorSystemStructure(indicatosSystem, structure);
    }

    @Override
    public void setIndicatorFromIndicatorInstance(IndicatorDto indicator) {
        structurePanel.setIndicatorFromIndicatorInstance(indicator);
    }

    @Override
    public void setIndicators(List<IndicatorDto> indicators) {
        structurePanel.setIndicators(indicators);
    }

    @Override
    public void setUiHandlers(SystemUiHandler uiHandlers) {
        this.uiHandlers = uiHandlers;
        this.structurePanel.setUiHandlers(uiHandlers);
        this.generalPanel.setUiHandlers(uiHandlers);
    }

    @Override
    public void onDimensionSaved(DimensionDto dimension) {
        structurePanel.onDimensionSaved(dimension);
    }

    @Override
    public void onIndicatorInstanceSaved(IndicatorInstanceDto instance) {
        structurePanel.onIndicatorInstanceSaved(instance);
    }

    @Override
    public void setGeographicalGranularitiesForIndicator(List<GeographicalGranularityDto> geographicalGranularityDtos) {
        structurePanel.setGeographicalGranularitiesForIndicator(geographicalGranularityDtos);
    }

    @Override
    public void setGeographicalValuesForIndicator(List<GeographicalValueDto> geographicalValueDtos) {
        structurePanel.setGeographicalValuesForIndicatorForIndicator(geographicalValueDtos);
    }

    @Override
    public void setGeographicalValue(GeographicalValueDto geographicalValueDto) {
        structurePanel.setGeographicalValue(geographicalValueDto);
    }

    @Override
    public void onIndicatorsSystemStatusChanged(IndicatorsSystemDtoWeb indicatorSystem) {
        generalPanel.setIndicatorsSystem(indicatorSystem);
        structurePanel.setIndicatorsSystem(indicatorSystem);
    }

    @Override
    public void onIndicatorDataPopulated(IndicatorDto indicatorDto) {
        structurePanel.onIndicatorDataPopulated(indicatorDto);
    }

    @Override
    public void setTemporalGranularitiesForIndicator(List<TimeGranularityEnum> timeGranularityEnums) {
        structurePanel.setTemporalGranularitiesForIndicator(timeGranularityEnums);
    }

    @Override
    public void setTemporalValuesFormIndicator(List<String> timeValues) {
        structurePanel.setTemporalValuesFormIndicator(timeValues);
    }

}
