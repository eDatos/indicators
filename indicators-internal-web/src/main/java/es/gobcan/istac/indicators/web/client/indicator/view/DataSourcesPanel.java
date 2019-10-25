package es.gobcan.istac.indicators.web.client.indicator.view;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getMessages;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.web.common.client.widgets.CustomListGrid;
import org.siemac.metamac.web.common.client.widgets.InformationWindow;
import org.siemac.metamac.web.common.client.widgets.ListGridToolStrip;

import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;

import es.gobcan.istac.indicators.core.dto.DataDefinitionDto;
import es.gobcan.istac.indicators.core.dto.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.DataStructureDto;
import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;
import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.IndicatorSummaryDto;
import es.gobcan.istac.indicators.core.dto.QuantityDto;
import es.gobcan.istac.indicators.core.dto.RateDerivationDto;
import es.gobcan.istac.indicators.core.dto.UnitMultiplierDto;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorProcStatusEnum;
import es.gobcan.istac.indicators.web.client.IndicatorsWeb;
import es.gobcan.istac.indicators.web.client.enums.IndicatorCalculationTypeEnum;
import es.gobcan.istac.indicators.web.client.enums.RateDerivationTypeEnum;
import es.gobcan.istac.indicators.web.client.indicator.presenter.IndicatorUiHandler;
import es.gobcan.istac.indicators.web.client.model.DataSourceRecord;
import es.gobcan.istac.indicators.web.client.model.ds.DataSourceDS;
import es.gobcan.istac.indicators.web.client.utils.ClientSecurityUtils;
import es.gobcan.istac.indicators.web.client.utils.RecordUtils;
import es.gobcan.istac.indicators.web.shared.GetQueriesPaginatedListResult;

public class DataSourcesPanel extends VLayout {

    private IndicatorDto       indicatorDto;
    private IndicatorUiHandler uiHandlers;

    private CustomListGrid     dataSourcesListGrid;

    private ListGridToolStrip  toolStrip;

    private DataSourcePanel    datasourcePanel;

    public DataSourcesPanel() {
        super();
        setMargin(15);

        // ToolStrip

        toolStrip = new ListGridToolStrip(getMessages().dataSourcesDeleteTitle(), getMessages().dataSourcesConfirmDelete());
        toolStrip.getNewButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (IndicatorProcStatusEnum.PUBLISHED.equals(DataSourcesPanel.this.indicatorDto.getProcStatus())
                        || IndicatorProcStatusEnum.ARCHIVED.equals(DataSourcesPanel.this.indicatorDto.getProcStatus())) {
                    // Create a new version of the indicator
                    final InformationWindow window = new InformationWindow(getMessages().indicatorEditionInfo(), getMessages().indicatorEditionInfoDetailedMessage());
                    window.show();
                } else {
                    // Default behavior

                    // Clear all query dependent fields
                    datasourcePanel.clearAllQueryValues();

                    DataSourceDto dataSourceDto = createEmptyDataSource();

                    selectDataSource(dataSourceDto);
                }
            }

        });

        toolStrip.getDeleteHandlerRegistration().removeHandler();
        toolStrip.getDeleteButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (IndicatorProcStatusEnum.PUBLISHED.equals(DataSourcesPanel.this.indicatorDto.getProcStatus())
                        || IndicatorProcStatusEnum.ARCHIVED.equals(DataSourcesPanel.this.indicatorDto.getProcStatus())) {
                    // Create a new version of the indicator
                    final InformationWindow window = new InformationWindow(getMessages().indicatorEditionInfo(), getMessages().indicatorEditionInfoDetailedMessage());
                    window.show();
                } else {
                    // Default behavior
                    toolStrip.getDeleteConfirmationWindow().show();
                }
            }
        });

        toolStrip.getDeleteConfirmationWindow().getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.deleteDataSource(getSelectedDataSources());
            }
        });

        // ListGrid

        dataSourcesListGrid = new CustomListGrid();
        dataSourcesListGrid.setHeight(150);
        ListGridField uuidField = new ListGridField(DataSourceDS.UUID, IndicatorsWeb.getConstants().dataSourceUuid());
        ListGridField pxField = new ListGridField(DataSourceDS.PX, IndicatorsWeb.getConstants().dataSourcePx());
        ListGridField dataGpeUuidField = new ListGridField(DataSourceDS.QUERY_UUID, IndicatorsWeb.getConstants().dataSourceDataGpeUuid());
        dataSourcesListGrid.setFields(uuidField, dataGpeUuidField, pxField);
        // Show data source details when record clicked
        dataSourcesListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (dataSourcesListGrid.getSelectedRecords() != null && dataSourcesListGrid.getSelectedRecords().length == 1) {
                    DataSourceRecord record = (DataSourceRecord) dataSourcesListGrid.getSelectedRecord();
                    DataSourceDto dataSourceSelected = record.getDataSourceDto();
                    selectDataSource(dataSourceSelected);
                } else {
                    // No record selected
                    deselectDatasource();
                    if (dataSourcesListGrid.getSelectedRecords().length > 1) {
                        // Delete more than one dimension with one click
                        showToolStripDeleteButton();
                    }
                }
            }
        });

        // Datasource panel
        datasourcePanel = new DataSourcePanel();

        addMember(toolStrip);
        addMember(dataSourcesListGrid);
        addMember(datasourcePanel);
    }

    protected DataSourceDto createEmptyDataSource() {
        DataSourceDto dataSourceDto = new DataSourceDto();

        RateDerivationDto interperiodPuntualRate = new RateDerivationDto();
        interperiodPuntualRate.setQuantity(new QuantityDto());
        interperiodPuntualRate.getQuantity().setIsPercentage(false);
        dataSourceDto.setInterperiodPuntualRate(interperiodPuntualRate);

        RateDerivationDto annualPuntualRate = new RateDerivationDto();
        annualPuntualRate.setQuantity(new QuantityDto());
        annualPuntualRate.getQuantity().setIsPercentage(false);
        dataSourceDto.setAnnualPuntualRate(annualPuntualRate);

        RateDerivationDto interperiodPercentageRate = new RateDerivationDto();
        interperiodPercentageRate.setQuantity(new QuantityDto());
        interperiodPercentageRate.getQuantity().setIsPercentage(true);
        dataSourceDto.setInterperiodPercentageRate(interperiodPercentageRate);

        RateDerivationDto annualPercentageRate = new RateDerivationDto();
        annualPercentageRate.setQuantity(new QuantityDto());
        annualPercentageRate.getQuantity().setIsPercentage(true);
        dataSourceDto.setAnnualPercentageRate(annualPercentageRate);
        return dataSourceDto;
    }

    public void setDataSources(List<DataSourceDto> dataSourceDtos) {
        // Hide forms
        datasourcePanel.hide();

        DataSourceRecord[] records = new DataSourceRecord[dataSourceDtos.size()];
        int index = 0;
        for (DataSourceDto ds : dataSourceDtos) {
            records[index++] = RecordUtils.getDataSourceRecord(ds);
        }
        dataSourcesListGrid.setData(records);

        // Load data definitions operation codes
        uiHandlers.retrieveDataDefinitionsOperationsCodes();
    }

    private void selectDataSource(DataSourceDto dataSourceDto) {
        if (dataSourceDto.getUuid() != null) {
            showToolStripDeleteButton();
        } else {
            toolStrip.getDeleteButton().hide();
            dataSourcesListGrid.deselectAllRecords();
        }

        datasourcePanel.setDataSource(dataSourceDto);
    }

    private void deselectDatasource() {
        toolStrip.getDeleteButton().hide();
        datasourcePanel.hide();
    }

    public void onDataSourceSaved(DataSourceDto dataSourceDto) {
        selectDataSource(dataSourceDto);

        dataSourcesListGrid.removeSelectedData();
        DataSourceRecord record = RecordUtils.getDataSourceRecord(dataSourceDto);
        dataSourcesListGrid.addData(record);
        dataSourcesListGrid.selectRecord(record);
    }

    private List<String> getSelectedDataSources() {
        List<String> codes = new ArrayList<String>();
        for (ListGridRecord record : dataSourcesListGrid.getSelectedRecords()) {
            codes.add(record.getAttribute(DataSourceDS.UUID));
        }
        return codes;
    }

    private void showToolStripDeleteButton() {
        if (ClientSecurityUtils.canDeleteDataSource(indicatorDto)) {
            toolStrip.getDeleteButton().show();
        }
    }

    // SETTING

    public void setIndicator(IndicatorDto indicatorDto) {
        this.indicatorDto = indicatorDto;
        datasourcePanel.setIndicator(indicatorDto);
        setCanEdit(indicatorDto);
    }

    private void setCanEdit(IndicatorDto indicatorDto) {
        toolStrip.getNewButton().setVisibility(ClientSecurityUtils.canCreateDataSource(indicatorDto) ? Visibility.VISIBLE : Visibility.HIDDEN);
        toolStrip.markForRedraw();
    }

    public void setUiHandlers(IndicatorUiHandler uiHandlers) {
        this.uiHandlers = uiHandlers;
        datasourcePanel.setUiHandlers(uiHandlers);

    }

    // DELEGATES

    public void setDataDefinitionsOperationCodes(List<String> operationCodes) {
        datasourcePanel.setDataDefinitionsOperationCodes(operationCodes);
    }

    public void setDataDefinitions(List<DataDefinitionDto> dataDefinitionsDtos) {
        datasourcePanel.setDataDefinitions(dataDefinitionsDtos);
    }

    public void setDataDefinition(DataDefinitionDto dataDefinitionDto) {
        datasourcePanel.setDataDefinition(dataDefinitionDto);
    }

    public void setStatisticalOperations(List<ExternalItemDto> operationsList) {
        datasourcePanel.setStatisticalOperations(operationsList);
    }

    public void setQueries(GetQueriesPaginatedListResult result) {
        List<ExternalItemDto> queriesList = result.getQueriesList();
        datasourcePanel.setQueries(queriesList, result.getFirstResultOut(), queriesList.size(), result.getTotalResults());
    }

    public void setUnitMultipliers(List<UnitMultiplierDto> unitMultiplierDtos) {
        datasourcePanel.setUnitMultipliers(unitMultiplierDtos);
    }

    public void setDataStructure(DataStructureDto dataStructureDto) {
        datasourcePanel.setDataStructureView(dataStructureDto);
        datasourcePanel.setDataStructureForEdition(dataStructureDto);
    }

    public void setDataStructureForEdition(DataStructureDto dataStructureDto) {
        datasourcePanel.setDataStructureForEdition(dataStructureDto);
    }

    public void setGeographicalValues(List<GeographicalValueDto> geographicalValueDtos) {
        datasourcePanel.setGeographicalValues(geographicalValueDtos);
    }

    public void setGeographicalValue(GeographicalValueDto geographicalValueDto) {
        datasourcePanel.setGeographicalValue(geographicalValueDto);
    }

    public DataSourceDto getDataSourceDto() {
        return datasourcePanel.getDataSourceDto();
    }

    public void setRateIndicator(IndicatorDto indicatorDto, RateDerivationTypeEnum rateDerivationTypeEnum, IndicatorCalculationTypeEnum indicatorCalculationTypeEnum) {
        datasourcePanel.setRateIndicator(indicatorDto, rateDerivationTypeEnum, indicatorCalculationTypeEnum);
    }

    public void setRateIndicators(List<IndicatorSummaryDto> indicatorDtos, RateDerivationTypeEnum rateDerivationTypeEnum, IndicatorCalculationTypeEnum indicatorCalculationTypeEnum) {
        datasourcePanel.setRateIndicators(indicatorDtos, rateDerivationTypeEnum, indicatorCalculationTypeEnum);
    }
}
