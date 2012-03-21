package es.gobcan.istac.indicators.web.client.indicator.view;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.List;

import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.widgets.CustomListGrid;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

import es.gobcan.istac.indicators.core.dto.serviceapi.DataBasicDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.DataStructureDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.GeographicalValueDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.QuantityUnitDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.RateDerivationDto;
import es.gobcan.istac.indicators.web.client.IndicatorsWeb;
import es.gobcan.istac.indicators.web.client.enums.DataSourceQuantityType;
import es.gobcan.istac.indicators.web.client.indicator.presenter.IndicatorUiHandler;
import es.gobcan.istac.indicators.web.client.model.DataSourceRecord;
import es.gobcan.istac.indicators.web.client.model.ds.DataSourceDS;
import es.gobcan.istac.indicators.web.client.utils.CommonUtils;
import es.gobcan.istac.indicators.web.client.utils.RecordUtils;
import es.gobcan.istac.indicators.web.client.widgets.RateDerivationForm;
import es.gobcan.istac.indicators.web.client.widgets.ViewRateDerivationForm;


public class DataSourcesPanel extends VLayout {

    private IndicatorDto indicatorDto;
    private IndicatorUiHandler uiHandlers;
    
    private ToolStripButton newDataSourceButton;
    private ToolStripButton deleteDatasourceButton;
    private CustomListGrid dataSourcesListGrid;
    
    private InternationalMainFormLayout mainFormLayout;

    // View Form
    private GroupDynamicForm generalForm;
    private ViewRateDerivationForm interperiodRateForm;
    private ViewRateDerivationForm annualRateForm;
    
    // Edition Form
    private GroupDynamicForm generalEditionForm;
    private RateDerivationForm interperiodRateEditionForm;
    private RateDerivationForm annualRateEditionForm;
    
    private DataSourceDto dataSourceDto;
    private DataBasicDto dataBasicDto;
    private DataStructureDto dataStructureDto;
    
    
    public DataSourcesPanel() {
        super();
        setMargin(15);
        
        // ToolStrip
        
        ToolStrip toolStrip = new ToolStrip();
        toolStrip.setWidth100();
        newDataSourceButton = new ToolStripButton(getConstants().indicNew(), RESOURCE.newListGrid().getURL());
        newDataSourceButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                DataSourceDto dataSourceDto = new DataSourceDto();
                dataSourceDto.setInterperiodRate(new RateDerivationDto());
                dataSourceDto.setAnnualRate(new RateDerivationDto());
                selectDataSource(dataSourceDto);
            }
        });

        deleteDatasourceButton = new ToolStripButton(getConstants().indicDelete(), RESOURCE.deleteListGrid().getURL());
        deleteDatasourceButton.setVisibility(Visibility.HIDDEN);
        deleteDatasourceButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // TODO Delete DataSource
            }
        });
        
        toolStrip.addButton(newDataSourceButton);
        toolStrip.addButton(deleteDatasourceButton);
        
        // ListGrid
        
        dataSourcesListGrid = new CustomListGrid();
        dataSourcesListGrid.setHeight(150);
        ListGridField queryField = new ListGridField(DataSourceDS.QUERY, IndicatorsWeb.getConstants().dataSourceQuery());
        dataSourcesListGrid.setFields(queryField);
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
                        deleteDatasourceButton.show();
                    }
                }
            }
        });
        dataSourcesListGrid.addRecordClickHandler(new RecordClickHandler() {
            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getFieldNum() != 0) { // CheckBox is not clicked
                    dataSourcesListGrid.deselectAllRecords();
                    dataSourcesListGrid.selectRecord(event.getRecord());
                }
            }
        });
        
        // MainFormLayout
        
        mainFormLayout = new InternationalMainFormLayout();
        mainFormLayout.setTitleLabelContents(getConstants().dataSource());
        mainFormLayout.setVisibility(Visibility.HIDDEN);
        mainFormLayout.getSave().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (generalForm.validate(false)) {
                    uiHandlers.saveDataSource(indicatorDto.getUuid(), getDataSourceDto());
                }
            }
        });
        mainFormLayout.getCancelToolStripButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (dataSourceDto.getUuid() == null || (dataSourceDto.getUuid() != null && dataSourceDto.getUuid().isEmpty())) {
                    mainFormLayout.hide();
                }
            }
        });
        mainFormLayout.getTranslateToolStripButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                boolean translationsShowed =  mainFormLayout.getTranslateToolStripButton().isSelected();
                generalForm.setTranslationsShowed(translationsShowed);
                generalEditionForm.setTranslationsShowed(translationsShowed);
                interperiodRateForm.setTranslationsShowed(translationsShowed);
                interperiodRateEditionForm.setTranslationsShowed(translationsShowed);
                annualRateForm.setTranslationsShowed(translationsShowed);
                annualRateEditionForm.setTranslationsShowed(translationsShowed);
            }
        });
        
        createViewForm();
        createEditionForm();
        
        addMember(toolStrip);
        addMember(dataSourcesListGrid);
        addMember(mainFormLayout);
    }
    
    public void setIndicator(IndicatorDto indicatorDto) {
        this.indicatorDto = indicatorDto;
        interperiodRateEditionForm.setIndicator(indicatorDto);
        annualRateEditionForm.setIndicator(indicatorDto);
    }
    
    public void setDataSources(List<DataSourceDto> dataSourceDtos) {
        DataSourceRecord[] records = new DataSourceRecord[dataSourceDtos.size()];
        int index = 0;
        for (DataSourceDto ds : dataSourceDtos) {
            records[index++] = RecordUtils.getDataSourceRecord(ds);
        }
        dataSourcesListGrid.setData(records);
        
        // Load Data Definitions
        uiHandlers.retrieveDataDefinitions();
        
        // Load Geographical Values
        uiHandlers.retrieveGeographicalValuesDS();
    }
    
    public void setUiHandlers(IndicatorUiHandler uiHandlers) {
        this.uiHandlers = uiHandlers;
        interperiodRateForm.setUiHandlers(uiHandlers);
        interperiodRateEditionForm.setUiHandlers(uiHandlers);
        annualRateForm.setUiHandlers(uiHandlers);
        annualRateEditionForm.setUiHandlers(uiHandlers);
    }
    
    private void selectDataSource(DataSourceDto dataSourceDto) {
        mainFormLayout.show();
        mainFormLayout.setViewMode();
        setDataSource(dataSourceDto);
    }
    
    private void deselectDatasource() {
        mainFormLayout.hide();
    }
    
    private void setDataSource(DataSourceDto dataSourceDto) {
        this.dataSourceDto = dataSourceDto;
        setDataSourceViewMode(dataSourceDto);
        setDataSourceEditionMode(dataSourceDto);
        
        // Load data structure
        if (dataSourceDto.getQueryGpe() != null && !dataSourceDto.getQueryGpe().isEmpty()) {
            uiHandlers.retrieveDataStructure(dataSourceDto.getQueryGpe());
        }
    }
    
    private void setDataSourceViewMode(DataSourceDto dataSourceDto) {
        generalForm.setValue(DataSourceDS.QUERY, ""); // Set in method setDataDefinition
        generalForm.setValue(DataSourceDS.TIME_VARIABLE, dataSourceDto.getTimeVariable());
        generalForm.setValue(DataSourceDS.TIME_VALUE, dataSourceDto.getTimeValue());
        generalForm.setValue(DataSourceDS.GEO_VARIABLE, dataSourceDto.getGeographicalVariable());
        generalForm.setValue(DataSourceDS.GEO_VALUE, ""); // Set in method setGeographicalValue
        if (dataSourceDto.getGeographicalValueUuid() != null && !dataSourceDto.getGeographicalValueUuid().isEmpty()) {
            uiHandlers.retrieveGeographicalValue(dataSourceDto.getGeographicalValueUuid());
        }
        
        interperiodRateForm.setValue(dataSourceDto.getInterperiodRate());
        
        annualRateForm.setValue(dataSourceDto.getAnnualRate());
    }
    
    private void setDataSourceEditionMode(DataSourceDto dataSourceDto) {
        generalEditionForm.setValue(DataSourceDS.QUERY, dataSourceDto.getQueryGpe());
        generalEditionForm.setValue(DataSourceDS.TIME_VARIABLE, dataSourceDto.getTimeVariable());
        generalEditionForm.setValue(DataSourceDS.TIME_VALUE, dataSourceDto.getTimeValue());
        generalEditionForm.setValue(DataSourceDS.GEO_VARIABLE, dataSourceDto.getGeographicalVariable());
        generalEditionForm.setValue(DataSourceDS.GEO_VALUE, dataSourceDto.getGeographicalValueUuid());
        
        interperiodRateEditionForm.setValue(dataSourceDto.getInterperiodRate());
        
        annualRateEditionForm.setValue(dataSourceDto.getAnnualRate());
    }
    
    private void createViewForm() {
        generalForm = new GroupDynamicForm(getConstants().datasourceGeneral());
        ViewTextItem query = new ViewTextItem(DataSourceDS.QUERY, getConstants().dataSourceQuery());
        ViewTextItem timeVariable = new ViewTextItem(DataSourceDS.TIME_VARIABLE, getConstants().dataSourceTimeVariable());
        ViewTextItem timeValue = new ViewTextItem(DataSourceDS.TIME_VALUE, getConstants().dataSourceTimeValue());
        ViewTextItem geographicalVariable = new ViewTextItem(DataSourceDS.GEO_VARIABLE, getConstants().dataSourceGeographicalVariable());
        ViewTextItem geographicalValue = new ViewTextItem(DataSourceDS.GEO_VALUE, getConstants().dataSourceGeographicalValue());
        generalForm.setFields(query, timeVariable, timeVariable, timeValue, geographicalVariable, geographicalValue);
        
        interperiodRateForm = new ViewRateDerivationForm(getConstants().dataSourceInterperiodRate(), DataSourceQuantityType.INTERPERIOD_RATE);
        
        annualRateForm = new ViewRateDerivationForm(getConstants().dataSourceAnnualRate(), DataSourceQuantityType.ANNUAL_RATE);
        
        mainFormLayout.addViewCanvas(generalForm);
        mainFormLayout.addViewCanvas(interperiodRateForm);
        mainFormLayout.addViewCanvas(annualRateForm);
    }
    
    private void createEditionForm() {
        generalEditionForm = new GroupDynamicForm(getConstants().datasourceGeneral());
        RequiredSelectItem query = new RequiredSelectItem(DataSourceDS.QUERY, getConstants().dataSourceQuery());
        query.addChangedHandler(new ChangedHandler() {
            @Override
            public void onChanged(ChangedEvent event) {
                if (event.getValue() != null && !event.getValue().toString().isEmpty()) {
                    uiHandlers.retrieveDataStructure(event.getValue().toString());
                } else {
                    // TODO Clear all values related?
                }
            }
        });
        ViewTextItem timeVariable = new ViewTextItem(DataSourceDS.TIME_VARIABLE, getConstants().dataSourceTimeVariable());
        TextItem timeValue = new TextItem(DataSourceDS.TIME_VALUE, getConstants().dataSourceTimeValue());
        ViewTextItem geographicalVariable = new ViewTextItem(DataSourceDS.GEO_VARIABLE, getConstants().dataSourceGeographicalVariable());
        SelectItem geographicalValue = new SelectItem(DataSourceDS.GEO_VALUE, getConstants().dataSourceGeographicalValue());
        generalEditionForm.setFields(query, timeVariable, timeValue, geographicalVariable, geographicalValue);
        
        interperiodRateEditionForm = new RateDerivationForm(getConstants().dataSourceInterperiodRate(), DataSourceQuantityType.INTERPERIOD_RATE);
        
        annualRateEditionForm = new RateDerivationForm(getConstants().dataSourceAnnualRate(), DataSourceQuantityType.ANNUAL_RATE);
        
        mainFormLayout.addEditionCanvas(generalEditionForm);
        mainFormLayout.addEditionCanvas(interperiodRateEditionForm);
        mainFormLayout.addEditionCanvas(annualRateEditionForm);
    }
    
    public void setDataDefinitions(List<DataBasicDto> dataBasicDtos) {
        ((SelectItem)generalEditionForm.getItem(DataSourceDS.QUERY)).setValueMap(CommonUtils.getDataBasicValueMap(dataBasicDtos));
    }
    
    public void setDataDefinition(DataBasicDto dataBasicDto) {
        this.dataBasicDto = dataBasicDto;
        generalForm.setValue(DataSourceDS.QUERY, dataBasicDto.getName());
    }
    
    public void setDataStructure(DataStructureDto dataStructureDto) {
        this.dataStructureDto = dataStructureDto;
    }
    
    public void setGeographicalValues(List<GeographicalValueDto> geographicalValueDtos) {
        ((SelectItem)generalEditionForm.getItem(DataSourceDS.GEO_VALUE)).setValueMap(CommonUtils.getGeographicalValuesValueMap(geographicalValueDtos));
    }
    
    public void setGeographicalValue(GeographicalValueDto geographicalValueDto) {
        generalForm.setValue(DataSourceDS.GEO_VALUE, InternationalStringUtils.getLocalisedString(geographicalValueDto.getTitle()));
    }
    
    public DataSourceDto getDataSourceDto() {
        dataSourceDto.setPx(dataStructureDto.getPxUri());
        dataSourceDto.setTimeVariable(dataStructureDto.getTemporalVariable());
        dataSourceDto.setTimeValue(generalEditionForm.getValueAsString(DataSourceDS.TIME_VALUE));
        dataSourceDto.setGeographicalVariable(dataStructureDto.getSpatialVariable());
        dataSourceDto.setGeographicalValueUuid(generalEditionForm.getValueAsString(DataSourceDS.GEO_VALUE));
        return dataSourceDto;
    }
    
    public void onDataSourceSaved(DataSourceDto dataSourceDto) {
        selectDataSource(dataSourceDto);
    }
    
    public void setGeographicalValuesDSInterperiodRate(List<GeographicalValueDto> geographicalValueDtos) {
        interperiodRateEditionForm.setGeographicalValues(geographicalValueDtos);
    }

    public void setGeographicalValuesDSAnnualRate(List<GeographicalValueDto> geographicalValueDtos) {
        annualRateEditionForm.setGeographicalValues(geographicalValueDtos);
    }
    
    public void setGeographicalValueDSInterperiodRate(GeographicalValueDto geographicalValueDto) {
        interperiodRateEditionForm.setGeographicalValue(geographicalValueDto);
    }

    public void setGeographicalValueDSAnnualRate(GeographicalValueDto geographicalValueDto) {
        annualRateEditionForm.setGeographicalValue(geographicalValueDto);
    }
    
    public void setQuantityUnits(List<QuantityUnitDto> units) {
        interperiodRateForm.setQuantityUnits(units);
        interperiodRateEditionForm.setQuantityUnits(units);
        annualRateForm.setQuantityUnits(units);
        annualRateEditionForm.setQuantityUnits(units);
    }
    
    public void setIndicatorList(List<IndicatorDto> indicatorDtos) {
        interperiodRateEditionForm.setIndicators(indicatorDtos);
        interperiodRateForm.setIndicators(indicatorDtos);
        annualRateEditionForm.setIndicators(indicatorDtos);
        annualRateForm.setIndicators(indicatorDtos);
    }
    
    public void setGeographicalGranularities(List<GeographicalGranularityDto> geographicalGranularityDtos) {
        interperiodRateEditionForm.setGeographicalGranularities(geographicalGranularityDtos);
        annualRateEditionForm.setGeographicalGranularities(geographicalGranularityDtos);
    }
    
}