package es.gobcan.istac.indicators.web.client.indicator.view;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.widgets.CustomListGrid;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
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
import es.gobcan.istac.indicators.web.client.widgets.VariableListItem;
import es.gobcan.istac.indicators.web.client.widgets.ViewRateDerivationForm;


public class DataSourcesPanel extends VLayout {

    private Logger logger = Logger.getLogger(DataSourcesPanel.class.getName());
    
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
        
        // MainFormLayout
        
        mainFormLayout = new InternationalMainFormLayout();
        mainFormLayout.setTitleLabelContents(getConstants().dataSource());
        mainFormLayout.setVisibility(Visibility.HIDDEN);
        mainFormLayout.getSave().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (generalEditionForm.validate(false) && interperiodRateEditionForm.validate(false) && annualRateEditionForm.validate(false)) {
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
        if (dataSourceDto.getUuid() != null) {
            mainFormLayout.setViewMode();
        } else {
            mainFormLayout.setEditionMode();
        }
        mainFormLayout.show();
        setDataSource(dataSourceDto);
    }
    
    private void deselectDatasource() {
        mainFormLayout.hide();
    }
    
    private void setDataSource(DataSourceDto dataSourceDto) {
        this.dataSourceDto = dataSourceDto;
        // Clear and load data structure
        dataStructureDto = null;
        if (dataSourceDto.getQueryGpe() != null && !dataSourceDto.getQueryGpe().isEmpty()) {
            uiHandlers.retrieveDataStructure(dataSourceDto.getQueryGpe());
        }
        setDataSourceViewMode(dataSourceDto);
        setDataSourceEditionMode(dataSourceDto);
    }
    
    private void setDataSourceViewMode(DataSourceDto dataSourceDto) {
        generalForm.setValue(DataSourceDS.QUERY, ""); // Set in method setDataDefinition
        if (!StringUtils.isBlank(dataSourceDto.getQueryGpe())) {
            uiHandlers.retrieveDataDefinition(dataSourceDto.getQueryGpe());
        }
        
        generalForm.setValue(DataSourceDS.TIME_VARIABLE, dataSourceDto.getTimeVariable());
        generalForm.setValue(DataSourceDS.TIME_VALUE, dataSourceDto.getTimeValue());
        generalForm.setValue(DataSourceDS.GEO_VARIABLE, dataSourceDto.getGeographicalVariable());
        generalForm.setValue(DataSourceDS.GEO_VALUE, ""); // Set in method setGeographicalValue
        if (dataSourceDto.getGeographicalValueUuid() != null && !dataSourceDto.getGeographicalValueUuid().isEmpty()) {
            uiHandlers.retrieveGeographicalValueDS(dataSourceDto.getGeographicalValueUuid());
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
        
        ((VariableListItem)generalEditionForm.getItem(DataSourceDS.OTHER_VARIABLES)).setValue(dataSourceDto.getOtherVariables());
        
        interperiodRateEditionForm.setValue(dataSourceDto.getInterperiodRate());
        
        annualRateEditionForm.setValue(dataSourceDto.getAnnualRate());
    }
    
    private void createViewForm() {
        generalForm = new GroupDynamicForm(getConstants().datasourceGeneral());
        
        ViewTextItem query = new ViewTextItem(DataSourceDS.QUERY, getConstants().dataSourceQuery());
        
        ViewTextItem timeVariable = new ViewTextItem(DataSourceDS.TIME_VARIABLE, getConstants().dataSourceTimeVariable());
        timeVariable.setShowIfCondition(new FormItemIfFunction() {
            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return value != null && !value.toString().isEmpty();
            }
        });
        
        ViewTextItem timeValue = new ViewTextItem(DataSourceDS.TIME_VALUE, getConstants().dataSourceTimeValue());
        timeValue.setShowIfCondition(new FormItemIfFunction() {
            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !form.getItem(DataSourceDS.TIME_VARIABLE).isVisible();
            }
        });
        
        ViewTextItem geographicalVariable = new ViewTextItem(DataSourceDS.GEO_VARIABLE, getConstants().dataSourceGeographicalVariable());
        geographicalVariable.setShowIfCondition(new FormItemIfFunction() {
            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return value != null && !value.toString().isEmpty();
            }
        });
        
        ViewTextItem geographicalValue = new ViewTextItem(DataSourceDS.GEO_VALUE, getConstants().dataSourceGeographicalValue());
        geographicalValue.setShowIfCondition(new FormItemIfFunction() {
            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !form.getItem(DataSourceDS.GEO_VARIABLE).isVisible();
            }
        });
        
        generalForm.setFields(query, timeVariable, timeValue, geographicalVariable, geographicalValue);
        
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
                    // Clear temporal variable
                    generalEditionForm.setValue(DataSourceDS.TIME_VARIABLE, new String());
                    // Clear spatial variable
                    generalEditionForm.setValue(DataSourceDS.GEO_VARIABLE, new String());
                }
            }
        });
        
        ViewTextItem timeVariable = new ViewTextItem(DataSourceDS.TIME_VARIABLE, getConstants().dataSourceTimeVariable());
        timeVariable.setShowIfCondition(new FormItemIfFunction() {
            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return value != null && !value.toString().isEmpty();
            }
        });
        
        RequiredTextItem timeValue = new RequiredTextItem(DataSourceDS.TIME_VALUE, getConstants().dataSourceTimeValue());
        timeValue.setShowIfCondition(new FormItemIfFunction() {
            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !form.getItem(DataSourceDS.TIME_VARIABLE).isVisible();
            }
        });
        
        ViewTextItem geographicalVariable = new ViewTextItem(DataSourceDS.GEO_VARIABLE, getConstants().dataSourceGeographicalVariable());
        geographicalVariable.setShowIfCondition(new FormItemIfFunction() {
            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return value != null && !value.toString().isEmpty();
            }
        });
        
        RequiredSelectItem geographicalValue = new RequiredSelectItem(DataSourceDS.GEO_VALUE, getConstants().dataSourceGeographicalValue());
        geographicalValue.setShowIfCondition(new FormItemIfFunction() {
            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !form.getItem(DataSourceDS.GEO_VARIABLE).isVisible();
            }
        });
        
        VariableListItem variables = new VariableListItem(DataSourceDS.OTHER_VARIABLES, getConstants().dataSourceOtherVariables());
        
        generalEditionForm.setFields(query, timeVariable, timeValue, geographicalVariable, geographicalValue, variables);
        
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
        generalForm.setValue(DataSourceDS.QUERY, dataBasicDto.getName());
    }
    
    public void setDataStructure(DataStructureDto dataStructureDto) {
        this.dataStructureDto = dataStructureDto;
        
        // Temporal variable
        generalEditionForm.setValue(DataSourceDS.TIME_VARIABLE, dataStructureDto.getTemporalVariable());
        
        // Spatial variable
        generalEditionForm.setValue(DataSourceDS.GEO_VARIABLE, dataStructureDto.getSpatialVariable());
        
        // Variables and categories
        List<String> variables = dataStructureDto.getVariables();
        Map<String, List<String>> categoryCodes = dataStructureDto.getValueCodes();
        Map<String, List<String>> categoryLabels = dataStructureDto.getValueLabels();
        for (String var : variables) {
            if (categoryCodes.containsKey(var) && categoryLabels.containsKey(var)) {
                ((VariableListItem)generalEditionForm.getItem(DataSourceDS.OTHER_VARIABLES)).addVariableAndCategories(var, categoryCodes.get(var), categoryLabels.get(var));
            } else {
                logger.log(Level.SEVERE, "Something wrong with variables and its category labels and codes");
            }
        }
        
        generalEditionForm.markForRedraw();
    }
    
    public void setGeographicalValues(List<GeographicalValueDto> geographicalValueDtos) {
        ((SelectItem)generalEditionForm.getItem(DataSourceDS.GEO_VALUE)).setValueMap(CommonUtils.getGeographicalValuesValueMap(geographicalValueDtos));
    }
    
    public void setGeographicalValue(GeographicalValueDto geographicalValueDto) {
        generalForm.setValue(DataSourceDS.GEO_VALUE, InternationalStringUtils.getLocalisedString(geographicalValueDto.getTitle()));
    }
    
    public DataSourceDto getDataSourceDto() {
        dataSourceDto.setQueryGpe(generalEditionForm.getValueAsString(DataSourceDS.QUERY));
        dataSourceDto.setPx(dataStructureDto.getPxUri());
        dataSourceDto.setTimeVariable(dataStructureDto.getTemporalVariable());
        dataSourceDto.setTimeValue(generalEditionForm.getItem(DataSourceDS.TIME_VALUE).isVisible() ? generalEditionForm.getValueAsString(DataSourceDS.TIME_VALUE) : null);
        dataSourceDto.setGeographicalVariable(dataStructureDto.getSpatialVariable());
        dataSourceDto.setGeographicalValueUuid(generalEditionForm.getItem(DataSourceDS.GEO_VALUE).isVisible() ?  CommonUtils.getUuidString(generalEditionForm.getValueAsString(DataSourceDS.GEO_VALUE)) : null);
        dataSourceDto.setInterperiodRate(interperiodRateEditionForm.getValue());
        dataSourceDto.setAnnualRate(annualRateEditionForm.getValue());
        return dataSourceDto;
    }
    
    public void onDataSourceSaved(DataSourceDto dataSourceDto) {
        this.dataSourceDto = dataSourceDto;
        selectDataSource(dataSourceDto);

        dataSourcesListGrid.removeSelectedData();
        DataSourceRecord record = RecordUtils.getDataSourceRecord(dataSourceDto);
        dataSourcesListGrid.addData(record);
        dataSourcesListGrid.selectRecord(record);
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
        // TODO
    }
    
}
