package es.gobcan.istac.indicators.web.client.indicator.view;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getMessages;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.utils.LocaleMock;
import org.siemac.metamac.web.common.client.widgets.CustomListGrid;
import org.siemac.metamac.web.common.client.widgets.ListGridToolStrip;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;

import es.gobcan.istac.indicators.core.dto.DataDefinitionDto;
import es.gobcan.istac.indicators.core.dto.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.DataStructureDto;
import es.gobcan.istac.indicators.core.dto.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;
import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.QuantityUnitDto;
import es.gobcan.istac.indicators.core.dto.RateDerivationDto;
import es.gobcan.istac.indicators.core.enume.domain.QuantityTypeEnum;
import es.gobcan.istac.indicators.web.client.IndicatorsWeb;
import es.gobcan.istac.indicators.web.client.indicator.presenter.IndicatorUiHandler;
import es.gobcan.istac.indicators.web.client.model.DataSourceRecord;
import es.gobcan.istac.indicators.web.client.model.ds.DataSourceDS;
import es.gobcan.istac.indicators.web.client.utils.CommonUtils;
import es.gobcan.istac.indicators.web.client.utils.RecordUtils;
import es.gobcan.istac.indicators.web.client.widgets.DataSourceMainFormLayout;
import es.gobcan.istac.indicators.web.client.widgets.RateDerivationForm;
import es.gobcan.istac.indicators.web.client.widgets.VariableCanvasItem;
import es.gobcan.istac.indicators.web.client.widgets.ViewDataSourceGeneralForm;
import es.gobcan.istac.indicators.web.client.widgets.ViewRateDerivationForm;

public class DataSourcesPanel extends VLayout {

    private IndicatorDto              indicatorDto;
    private IndicatorUiHandler        uiHandlers;

    private CustomListGrid            dataSourcesListGrid;

    private ListGridToolStrip         toolStrip;
    private DataSourceMainFormLayout  mainFormLayout;

    // View Form
    private ViewDataSourceGeneralForm generalForm;
    private ViewRateDerivationForm    interperiodPuntualRateForm;
    private ViewRateDerivationForm    annualPuntualRateForm;
    private ViewRateDerivationForm    interperiodPercentageRateForm;
    private ViewRateDerivationForm    annualPercentageRateForm;

    // Edition Form
    private GroupDynamicForm          generalEditionForm;
    private ViewDataSourceGeneralForm generalStaticEditionForm;
    private RateDerivationForm        interperiodPuntualRateEditionForm;
    private RateDerivationForm        annualPuntualRateEditionForm;
    private RateDerivationForm        interperiodPercentageRateEditionForm;
    private RateDerivationForm        annualPercentageRateEditionForm;

    private DataSourceDto             dataSourceDto;
    private DataStructureDto          dataStructureDto;

    public DataSourcesPanel() {
        super();
        setMargin(15);

        // ToolStrip

        toolStrip = new ListGridToolStrip(getMessages().dataSourcesDeleteTitle(), getMessages().dataSourcesConfirmDelete());
        toolStrip.getNewButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                DataSourceDto dataSourceDto = new DataSourceDto();
                dataSourceDto.setInterperiodPuntualRate(new RateDerivationDto());
                dataSourceDto.setAnnualPuntualRate(new RateDerivationDto());
                dataSourceDto.setInterperiodPercentageRate(new RateDerivationDto());
                dataSourceDto.setAnnualPercentageRate(new RateDerivationDto());
                selectDataSource(dataSourceDto);
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
        dataSourcesListGrid.setFields(uuidField, pxField);
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
                        toolStrip.getDeleteButton().show();
                    }
                }
            }
        });

        // MainFormLayout

        mainFormLayout = new DataSourceMainFormLayout();
        mainFormLayout.setTitleLabelContents(getConstants().dataSource());
        mainFormLayout.setVisibility(Visibility.HIDDEN);

        mainFormLayout.getEditToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                setViewQueryMode();
            }
        });

        mainFormLayout.getSave().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (generalEditionForm.validate(false) && interperiodPuntualRateEditionForm.validate(false) && annualPuntualRateEditionForm.validate(false)
                        && interperiodPercentageRateEditionForm.validate(false) && annualPercentageRateEditionForm.validate(false)) {
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
                boolean translationsShowed = mainFormLayout.getTranslateToolStripButton().isSelected();
                generalForm.setTranslationsShowed(translationsShowed);
                generalEditionForm.setTranslationsShowed(translationsShowed);
                generalStaticEditionForm.setTranslationsShowed(translationsShowed);
                interperiodPuntualRateForm.setTranslationsShowed(translationsShowed);
                interperiodPuntualRateEditionForm.setTranslationsShowed(translationsShowed);
                interperiodPercentageRateForm.setTranslationsShowed(translationsShowed);
                interperiodPercentageRateEditionForm.setTranslationsShowed(translationsShowed);
                annualPuntualRateForm.setTranslationsShowed(translationsShowed);
                annualPuntualRateEditionForm.setTranslationsShowed(translationsShowed);
                annualPercentageRateForm.setTranslationsShowed(translationsShowed);
                annualPercentageRateEditionForm.setTranslationsShowed(translationsShowed);
            }
        });

        mainFormLayout.getEditQueryToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                setEditionQueryMode();
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
        interperiodPuntualRateEditionForm.setIndicator(indicatorDto);
        interperiodPercentageRateEditionForm.setIndicator(indicatorDto);
        annualPuntualRateEditionForm.setIndicator(indicatorDto);
        annualPercentageRateEditionForm.setIndicator(indicatorDto);
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
        generalForm.setUiHandlers(uiHandlers);
        generalStaticEditionForm.setUiHandlers(uiHandlers);
        interperiodPuntualRateForm.setUiHandlers(uiHandlers);
        interperiodPuntualRateEditionForm.setUiHandlers(uiHandlers);
        interperiodPercentageRateForm.setUiHandlers(uiHandlers);
        interperiodPercentageRateEditionForm.setUiHandlers(uiHandlers);
        annualPuntualRateForm.setUiHandlers(uiHandlers);
        annualPuntualRateEditionForm.setUiHandlers(uiHandlers);
        annualPercentageRateForm.setUiHandlers(uiHandlers);
        annualPercentageRateEditionForm.setUiHandlers(uiHandlers);
    }

    private void selectDataSource(DataSourceDto dataSourceDto) {
        if (dataSourceDto.getUuid() != null) {
            toolStrip.getDeleteButton().show();
            mainFormLayout.setViewMode();
        } else {
            toolStrip.getDeleteButton().hide();
            dataSourcesListGrid.deselectAllRecords();
            mainFormLayout.setEditionMode();
        }

        mainFormLayout.show();
        setDataSource(dataSourceDto);
    }

    private void deselectDatasource() {
        toolStrip.getDeleteButton().hide();
        mainFormLayout.hide();
    }

    private void setDataSource(DataSourceDto dataSourceDto) {
        this.dataSourceDto = dataSourceDto;

        // Update dataSource title
        mainFormLayout.setTitleLabelContents(getConstants().dataSource() + (dataSourceDto.getUuid() != null ? " " + dataSourceDto.getUuid() : new String()));

        // Clear and load data structure
        // dataStructureDto = null;
        // if (dataSourceDto.getDataGpeUuid() != null && !dataSourceDto.getDataGpeUuid().isEmpty()) {
        // uiHandlers.retrieveDataStructure(dataSourceDto.getDataGpeUuid());
        // }

        // Set query form visibility
        if (dataSourceDto.getUuid() == null) {
            setEditionQueryMode();
        } else {
            setViewQueryMode();
        }

        setDataSourceViewMode(dataSourceDto);
        setDataSourceEditionMode(dataSourceDto);
    }

    private void setDataSourceViewMode(DataSourceDto dataSourceDto) {
        generalForm.setValue(dataSourceDto);

        interperiodPuntualRateForm.setValue(dataSourceDto.getInterperiodPuntualRate());

        interperiodPercentageRateForm.setValue(dataSourceDto.getInterperiodPercentageRate());

        annualPuntualRateForm.setValue(dataSourceDto.getAnnualPuntualRate());

        annualPercentageRateForm.setValue(dataSourceDto.getAnnualPercentageRate());
    }

    private void setDataSourceEditionMode(DataSourceDto dataSourceDto) {
        // Clear query values
        generalEditionForm.clearValues();

        generalStaticEditionForm.setValue(dataSourceDto);

        interperiodPuntualRateEditionForm.setValue(dataSourceDto.getInterperiodPuntualRate());

        interperiodPercentageRateEditionForm.setValue(dataSourceDto.getInterperiodPercentageRate());

        annualPuntualRateEditionForm.setValue(dataSourceDto.getAnnualPuntualRate());

        annualPercentageRateEditionForm.setValue(dataSourceDto.getAnnualPercentageRate());
    }

    private void createViewForm() {
        generalForm = new ViewDataSourceGeneralForm(getConstants().datasourceGeneral());

        interperiodPuntualRateForm = new ViewRateDerivationForm(getConstants().dataSourceInterperiodPuntualRate());

        interperiodPercentageRateForm = new ViewRateDerivationForm(getConstants().dataSourceInterperiodPercentageRate());

        annualPuntualRateForm = new ViewRateDerivationForm(getConstants().dataSourceAnnualPuntualRate());

        annualPercentageRateForm = new ViewRateDerivationForm(getConstants().dataSourceAnnualPercentageRate());

        mainFormLayout.addViewCanvas(generalForm);
        mainFormLayout.addViewCanvas(interperiodPuntualRateForm);
        mainFormLayout.addViewCanvas(interperiodPercentageRateForm);
        mainFormLayout.addViewCanvas(annualPuntualRateForm);
        mainFormLayout.addViewCanvas(annualPercentageRateForm);
    }

    private void createEditionForm() {
        generalStaticEditionForm = new ViewDataSourceGeneralForm(getConstants().datasourceGeneral());

        generalEditionForm = new GroupDynamicForm(getConstants().datasourceGeneral());

        RequiredSelectItem query = new RequiredSelectItem(DataSourceDS.QUERY, getConstants().dataSourceQuery());
        query.addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                if (event.getValue() != null && !event.getValue().toString().isEmpty()) {
                    // Clear values
                    ((ViewTextItem) generalEditionForm.getItem(DataSourceDS.SOURCE_SURVEY_CODE)).clearValue();
                    ((ViewMultiLanguageTextItem) generalEditionForm.getItem(DataSourceDS.SOURCE_SURVEY_TITLE)).clearValue();
                    ((MultiLanguageTextItem) generalEditionForm.getItem(DataSourceDS.SOURCE_SURVEY_ACRONYM)).clearValue();
                    ((TextItem) generalEditionForm.getItem(DataSourceDS.SOURCE_SURVEY_URL)).clearValue();
                    ((ViewTextItem) generalEditionForm.getItem(DataSourceDS.PUBLISHERS)).clearValue();

                    ((ViewTextItem) generalEditionForm.getItem(DataSourceDS.TIME_VARIABLE)).clearValue();
                    ((TextItem) generalEditionForm.getItem(DataSourceDS.TIME_VALUE)).clearValue();
                    ((ViewTextItem) generalEditionForm.getItem(DataSourceDS.GEO_VARIABLE)).clearValue();
                    ((SelectItem) generalEditionForm.getItem(DataSourceDS.GEO_VALUE)).clearValue();
                    ((VariableCanvasItem) generalEditionForm.getItem(DataSourceDS.OTHER_VARIABLES)).clearValue();

                    // Retrieve data structure
                    uiHandlers.retrieveDataStructure(event.getValue().toString());
                } else {
                    // Clear temporal variable
                    generalEditionForm.setValue(DataSourceDS.TIME_VARIABLE, new String());
                    // Clear spatial variable
                    generalEditionForm.setValue(DataSourceDS.GEO_VARIABLE, new String());
                }
            }
        });

        ViewTextItem surveyCode = new ViewTextItem(DataSourceDS.SOURCE_SURVEY_CODE, getConstants().dataSourceSurveyCode());

        ViewMultiLanguageTextItem surveyTitle = new ViewMultiLanguageTextItem(DataSourceDS.SOURCE_SURVEY_TITLE, getConstants().dataSourceSurveyTitle());

        MultiLanguageTextItem surveyAcronym = new MultiLanguageTextItem(DataSourceDS.SOURCE_SURVEY_ACRONYM, getConstants().dataSourceSurveyAcronym());

        TextItem surveyUrl = new TextItem(DataSourceDS.SOURCE_SURVEY_URL, getConstants().dataSourceSurveyUrl());

        ViewTextItem publishers = new ViewTextItem(DataSourceDS.PUBLISHERS, getConstants().dataSourcePublishers());

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

        VariableCanvasItem variables = new VariableCanvasItem(DataSourceDS.OTHER_VARIABLES, getConstants().dataSourceOtherVariables());

        generalEditionForm.setFields(query, surveyCode, surveyTitle, surveyAcronym, surveyUrl, publishers, timeVariable, timeValue, geographicalVariable, geographicalValue, variables);

        interperiodPuntualRateEditionForm = new RateDerivationForm(getConstants().dataSourceInterperiodPuntualRate(), QuantityTypeEnum.AMOUNT);

        interperiodPercentageRateEditionForm = new RateDerivationForm(getConstants().dataSourceInterperiodPercentageRate(), QuantityTypeEnum.CHANGE_RATE);

        annualPuntualRateEditionForm = new RateDerivationForm(getConstants().dataSourceAnnualPuntualRate(), QuantityTypeEnum.AMOUNT);

        annualPercentageRateEditionForm = new RateDerivationForm(getConstants().dataSourceAnnualPercentageRate(), QuantityTypeEnum.CHANGE_RATE);

        mainFormLayout.addEditionCanvas(generalEditionForm);
        mainFormLayout.addEditionCanvas(generalStaticEditionForm);
        mainFormLayout.addEditionCanvas(interperiodPuntualRateEditionForm);
        mainFormLayout.addEditionCanvas(interperiodPercentageRateEditionForm);
        mainFormLayout.addEditionCanvas(annualPuntualRateEditionForm);
        mainFormLayout.addEditionCanvas(annualPercentageRateEditionForm);
    }

    public void setDataDefinitions(List<DataDefinitionDto> dataDefinitionsDtos) {
        ((SelectItem) generalEditionForm.getItem(DataSourceDS.QUERY)).setValueMap(CommonUtils.getDataBasicValueMap(dataDefinitionsDtos));
    }

    public void setDataDefinition(DataDefinitionDto dataDefinitionDto) {
        generalForm.setValue(DataSourceDS.QUERY, dataDefinitionDto.getName());
        generalStaticEditionForm.setValue(DataSourceDS.QUERY, dataDefinitionDto.getName());
    }

    public void setDataStructure(DataStructureDto dataStructureDto) {
        this.dataStructureDto = dataStructureDto;

        // Source survey code
        generalEditionForm.setValue(DataSourceDS.SOURCE_SURVEY_CODE, dataStructureDto.getSurveyCode());

        // Source survey title
        InternationalStringDto internationalStringDto = InternationalStringUtils.updateInternationalString(LocaleMock.SPANISH, new InternationalStringDto(), dataStructureDto.getSurveyTitle());
        generalEditionForm.setValue(DataSourceDS.SOURCE_SURVEY_TITLE, org.siemac.metamac.web.common.client.utils.RecordUtils.getInternationalStringRecord(internationalStringDto));

        // Publishers
        generalEditionForm.setValue(DataSourceDS.PUBLISHERS, CommonWebUtils.getStringListToString(dataStructureDto.getPublishers()));

        // Temporal variable
        generalEditionForm.setValue(DataSourceDS.TIME_VARIABLE, dataStructureDto.getTemporalVariable());

        // Spatial variable
        generalEditionForm.setValue(DataSourceDS.GEO_VARIABLE, dataStructureDto.getSpatialVariable());

        // Variables and categories
        ((VariableCanvasItem) generalEditionForm.getItem(DataSourceDS.OTHER_VARIABLES)).clearValue();

        List<String> variables = dataStructureDto.getVariables();
        Map<String, List<String>> categoryCodes = dataStructureDto.getValueCodes();
        Map<String, List<String>> categoryLabels = dataStructureDto.getValueLabels();
        ((VariableCanvasItem) generalEditionForm.getItem(DataSourceDS.OTHER_VARIABLES)).setVariablesAndCategories(variables, categoryCodes, categoryLabels);

        generalEditionForm.markForRedraw();
    }

    public void setGeographicalValues(List<GeographicalValueDto> geographicalValueDtos) {
        ((SelectItem) generalEditionForm.getItem(DataSourceDS.GEO_VALUE)).setValueMap(CommonUtils.getGeographicalValuesValueMap(geographicalValueDtos));
    }

    public void setGeographicalValue(GeographicalValueDto geographicalValueDto) {
        generalForm.setValue(DataSourceDS.GEO_VALUE, InternationalStringUtils.getLocalisedString(geographicalValueDto.getTitle()));
        generalStaticEditionForm.setValue(DataSourceDS.GEO_VALUE, InternationalStringUtils.getLocalisedString(geographicalValueDto.getTitle()));
    }

    public DataSourceDto getDataSourceDto() {
        dataSourceDto.setDataGpeUuid(generalEditionForm.getValueAsString(DataSourceDS.QUERY));
        dataSourceDto.setPxUri(dataStructureDto.getPxUri());

        dataSourceDto.setSourceSurveyCode(dataStructureDto.getSurveyCode());
        dataSourceDto.setSourceSurveyTitle(InternationalStringUtils.updateInternationalString(LocaleMock.SPANISH, new InternationalStringDto(), dataStructureDto.getSurveyTitle()));
        dataSourceDto.setSourceSurveyAcronym((InternationalStringDto) generalEditionForm.getValue(DataSourceDS.SOURCE_SURVEY_ACRONYM));
        dataSourceDto.setSourceSurveyUrl(generalEditionForm.getValueAsString(DataSourceDS.SOURCE_SURVEY_URL));

        dataSourceDto.setPublishers(dataStructureDto.getPublishers());

        dataSourceDto.setTimeVariable(dataStructureDto.getTemporalVariable());
        dataSourceDto.setTimeValue(generalEditionForm.getItem(DataSourceDS.TIME_VALUE).isVisible() ? generalEditionForm.getValueAsString(DataSourceDS.TIME_VALUE) : null);
        dataSourceDto.setGeographicalVariable(dataStructureDto.getSpatialVariable());
        dataSourceDto.setGeographicalValueUuid(generalEditionForm.getItem(DataSourceDS.GEO_VALUE).isVisible()
                ? CommonUtils.getUuidString(generalEditionForm.getValueAsString(DataSourceDS.GEO_VALUE))
                : null);

        dataSourceDto.setInterperiodPuntualRate(interperiodPuntualRateEditionForm.getValue());
        dataSourceDto.setInterperiodPercentageRate(interperiodPercentageRateEditionForm.getValue());
        dataSourceDto.setAnnualPuntualRate(annualPuntualRateEditionForm.getValue());
        dataSourceDto.setAnnualPercentageRate(annualPercentageRateEditionForm.getValue());

        dataSourceDto.getOtherVariables().clear();
        dataSourceDto.getOtherVariables().addAll(((VariableCanvasItem) generalEditionForm.getItem(DataSourceDS.OTHER_VARIABLES)).getValue());

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
        interperiodPuntualRateForm.setQuantityUnits(units);
        interperiodPuntualRateEditionForm.setQuantityUnits(units);
        interperiodPercentageRateForm.setQuantityUnits(units);
        interperiodPercentageRateEditionForm.setQuantityUnits(units);
        annualPuntualRateForm.setQuantityUnits(units);
        annualPuntualRateEditionForm.setQuantityUnits(units);
        annualPercentageRateForm.setQuantityUnits(units);
        annualPercentageRateEditionForm.setQuantityUnits(units);
    }

    public void setIndicatorList(List<IndicatorDto> indicatorDtos) {
        interperiodPuntualRateEditionForm.setIndicators(indicatorDtos);
        interperiodPuntualRateForm.setIndicators(indicatorDtos);
        interperiodPercentageRateEditionForm.setIndicators(indicatorDtos);
        interperiodPercentageRateForm.setIndicators(indicatorDtos);
        annualPuntualRateEditionForm.setIndicators(indicatorDtos);
        annualPuntualRateForm.setIndicators(indicatorDtos);
        annualPercentageRateEditionForm.setIndicators(indicatorDtos);
        annualPercentageRateForm.setIndicators(indicatorDtos);
    }

    public void setGeographicalGranularities(List<GeographicalGranularityDto> geographicalGranularityDtos) {
        // TODO
    }

    private List<String> getSelectedDataSources() {
        List<String> codes = new ArrayList<String>();
        for (ListGridRecord record : dataSourcesListGrid.getSelectedRecords()) {
            codes.add(record.getAttribute(DataSourceDS.UUID));
        }
        return codes;
    }

    private void setViewQueryMode() {
        generalEditionForm.hide();
        generalStaticEditionForm.show();
        mainFormLayout.markForRedraw();
    }

    private void setEditionQueryMode() {
        generalEditionForm.show();
        generalStaticEditionForm.hide();
        mainFormLayout.markForRedraw();
    }

}
