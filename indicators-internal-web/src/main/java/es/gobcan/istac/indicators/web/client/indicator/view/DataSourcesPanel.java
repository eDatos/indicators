package es.gobcan.istac.indicators.web.client.indicator.view;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getMessages;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.utils.LocaleMock;
import org.siemac.metamac.web.common.client.utils.TimeVariableWebUtils;
import org.siemac.metamac.web.common.client.widgets.CustomListGrid;
import org.siemac.metamac.web.common.client.widgets.ListGridToolStrip;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.SearchViewTextItem;
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
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.form.validator.CustomValidator;
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
import es.gobcan.istac.indicators.core.enume.domain.RateDerivationMethodTypeEnum;
import es.gobcan.istac.indicators.web.client.IndicatorsWeb;
import es.gobcan.istac.indicators.web.client.indicator.presenter.IndicatorUiHandler;
import es.gobcan.istac.indicators.web.client.model.DataSourceRecord;
import es.gobcan.istac.indicators.web.client.model.ds.DataSourceDS;
import es.gobcan.istac.indicators.web.client.utils.ClientSecurityUtils;
import es.gobcan.istac.indicators.web.client.utils.CommonUtils;
import es.gobcan.istac.indicators.web.client.utils.RecordUtils;
import es.gobcan.istac.indicators.web.client.widgets.DataDefinitionsSearchWindow;
import es.gobcan.istac.indicators.web.client.widgets.DataSourceMainFormLayout;
import es.gobcan.istac.indicators.web.client.widgets.GeographicalSelectItem;
import es.gobcan.istac.indicators.web.client.widgets.RateDerivationForm;
import es.gobcan.istac.indicators.web.client.widgets.VariableCanvasItem;
import es.gobcan.istac.indicators.web.client.widgets.ViewDataSourceGeneralForm;
import es.gobcan.istac.indicators.web.client.widgets.ViewRateDerivationForm;

public class DataSourcesPanel extends VLayout {

    private IndicatorDto                indicatorDto;
    private IndicatorUiHandler          uiHandlers;

    private CustomListGrid              dataSourcesListGrid;

    private ListGridToolStrip           toolStrip;
    private DataSourceMainFormLayout    mainFormLayout;

    // View Form
    private ViewDataSourceGeneralForm   generalForm;
    private ViewRateDerivationForm      interperiodPuntualRateForm;
    private ViewRateDerivationForm      annualPuntualRateForm;
    private ViewRateDerivationForm      interperiodPercentageRateForm;
    private ViewRateDerivationForm      annualPercentageRateForm;

    // Edition Form
    private GroupDynamicForm            generalEditionForm;
    private ViewDataSourceGeneralForm   generalStaticEditionForm;
    private RateDerivationForm          interperiodPuntualRateEditionForm;
    private RateDerivationForm          annualPuntualRateEditionForm;
    private RateDerivationForm          interperiodPercentageRateEditionForm;
    private RateDerivationForm          annualPercentageRateEditionForm;

    private DataSourceDto               dataSourceDto;
    private DataStructureDto            dataStructureDto;

    private DataDefinitionsSearchWindow dataDefinitionsSearchWindow;
    private List<String>                dataDefinitionsOperationCodes;

    public DataSourcesPanel() {
        super();
        setMargin(15);

        // ToolStrip

        toolStrip = new ListGridToolStrip(getMessages().dataSourcesDeleteTitle(), getMessages().dataSourcesConfirmDelete());
        toolStrip.getNewButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                // Clear all query dependent fields
                clearAllQueryValues();

                dataSourceDto = new DataSourceDto();
                dataSourceDto.setInterperiodPuntualRate(new RateDerivationDto());
                dataSourceDto.setAnnualPuntualRate(new RateDerivationDto());
                dataSourceDto.setInterperiodPercentageRate(new RateDerivationDto());
                dataSourceDto.setAnnualPercentageRate(new RateDerivationDto());

                selectDataSource(dataSourceDto);
            }
        });
        toolStrip.getNewButton().setVisibility(ClientSecurityUtils.canCreateDataSource() ? Visibility.VISIBLE : Visibility.HIDDEN);

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
                        showToolStripDeleteButton();
                    }
                }
            }
        });

        // MainFormLayout

        mainFormLayout = new DataSourceMainFormLayout(ClientSecurityUtils.canEditDataSource());
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
                if (generalEditionForm.isVisible()) {
                    if (generalEditionForm.validate(false) && interperiodPuntualRateEditionForm.validate(false) && annualPuntualRateEditionForm.validate(false)
                            && interperiodPercentageRateEditionForm.validate(false) && annualPercentageRateEditionForm.validate(false)) {
                        uiHandlers.saveDataSource(indicatorDto.getUuid(), getDataSourceDto());
                    }
                } else {
                    if (interperiodPuntualRateEditionForm.validate(false) && annualPuntualRateEditionForm.validate(false) && interperiodPercentageRateEditionForm.validate(false)
                            && annualPercentageRateEditionForm.validate(false)) {
                        uiHandlers.saveDataSource(indicatorDto.getUuid(), getDataSourceDto());
                    }
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
                generalStaticEditionForm.setTranslationsShowed(translationsShowed);
                generalEditionForm.setTranslationsShowed(translationsShowed);
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
                // Clear query form values
                clearAllQueryValues();

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
        // Hide forms
        mainFormLayout.hide();

        DataSourceRecord[] records = new DataSourceRecord[dataSourceDtos.size()];
        int index = 0;
        for (DataSourceDto ds : dataSourceDtos) {
            records[index++] = RecordUtils.getDataSourceRecord(ds);
        }
        dataSourcesListGrid.setData(records);

        // Load data definitions operation codes
        uiHandlers.retrieveDataDefinitionsOperationsCodes();
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
            showToolStripDeleteButton();
            mainFormLayout.setViewMode();
        } else {
            toolStrip.getDeleteButton().hide();
            dataSourcesListGrid.deselectAllRecords();
            mainFormLayout.setEditionMode();
        }

        // Hide label and edit query button when we are going to create a new data source
        mainFormLayout.getLabel().hide();
        mainFormLayout.getEditQueryToolStripButton().hide();

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
        dataStructureDto = null;
        if (dataSourceDto.getDataGpeUuid() != null && !dataSourceDto.getDataGpeUuid().isEmpty()) {
            uiHandlers.retrieveDataStructure(dataSourceDto.getDataGpeUuid());
        }

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

        if (dataSourceDto.getInterperiodPuntualRate() != null) {
            interperiodPuntualRateForm.setValue(dataSourceDto.getInterperiodPuntualRate());
        } else {
            interperiodPuntualRateForm.prepareNewRate(QuantityTypeEnum.AMOUNT);
        }

        if (dataSourceDto.getInterperiodPercentageRate() != null) {
            interperiodPercentageRateForm.setValue(dataSourceDto.getInterperiodPercentageRate());
        } else {
            interperiodPercentageRateForm.prepareNewRate(QuantityTypeEnum.CHANGE_RATE);
        }

        if (dataSourceDto.getAnnualPuntualRate() != null) {
            annualPuntualRateForm.setValue(dataSourceDto.getAnnualPuntualRate());
        } else {
            annualPuntualRateForm.prepareNewRate(QuantityTypeEnum.AMOUNT);
        }

        if (dataSourceDto.getAnnualPercentageRate() != null) {
            annualPercentageRateForm.setValue(dataSourceDto.getAnnualPercentageRate());
        } else {
            annualPercentageRateForm.prepareNewRate(QuantityTypeEnum.CHANGE_RATE);
        }
    }

    private void setDataSourceEditionMode(DataSourceDto dataSourceDto) {

        generalStaticEditionForm.setValue(dataSourceDto);

        // Edition values are not set. When edit query button is clicked, values will be cleared.
        // generalEditionForm.setValue();

        // Some rates can not exist

        if (dataSourceDto.getInterperiodPuntualRate() != null) {
            interperiodPuntualRateEditionForm.setValue(dataSourceDto.getInterperiodPuntualRate());
        } else {
            interperiodPuntualRateEditionForm.prepareNewRate();
        }

        if (dataSourceDto.getInterperiodPercentageRate() != null) {
            interperiodPercentageRateEditionForm.setValue(dataSourceDto.getInterperiodPercentageRate());
        } else {
            interperiodPercentageRateEditionForm.prepareNewRate();
        }

        if (dataSourceDto.getAnnualPuntualRate() != null) {
            annualPuntualRateEditionForm.setValue(dataSourceDto.getAnnualPuntualRate());
        } else {
            annualPuntualRateEditionForm.prepareNewRate();
        }

        if (dataSourceDto.getAnnualPercentageRate() != null) {
            annualPercentageRateEditionForm.setValue(dataSourceDto.getAnnualPercentageRate());
        } else {
            annualPercentageRateEditionForm.prepareNewRate();
        }
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

        // Search data definition (query)
        RequiredTextItem queryUuid = new RequiredTextItem(DataSourceDS.QUERY_UUID, getConstants().dataSourceQuery());
        queryUuid.setShowIfCondition(CommonUtils.getFalseIfFunction());
        SearchViewTextItem query = getQueryItem();

        ViewTextItem surveyCode = new ViewTextItem(DataSourceDS.SOURCE_SURVEY_CODE, getConstants().dataSourceSurveyCode());

        ViewMultiLanguageTextItem surveyTitle = new ViewMultiLanguageTextItem(DataSourceDS.SOURCE_SURVEY_TITLE, getConstants().dataSourceSurveyTitle());

        MultiLanguageTextItem surveyAcronym = new MultiLanguageTextItem(DataSourceDS.SOURCE_SURVEY_ACRONYM, getConstants().dataSourceSurveyAcronym());

        TextItem surveyUrl = new TextItem(DataSourceDS.SOURCE_SURVEY_URL, getConstants().dataSourceSurveyUrl());

        ViewTextItem publishers = new ViewTextItem(DataSourceDS.PUBLISHERS, getConstants().dataSourcePublishers());

        // Showed when contVariable (measure variable) is set
        // ValueMap set in setDataStructure
        SelectItem absoluteMethod = new SelectItem(DataSourceDS.ABSOLUTE_METHOD, getConstants().dataSourceAbsoluteMethod());
        absoluteMethod.setValidateOnChange(true);
        CustomValidator absoluteMethodValidator = new CustomValidator() {

            // If absolute method is not set, at least one rate type must be LOAD
            @Override
            protected boolean condition(Object value) {
                if (value == null || StringUtils.isBlank(value.toString())) {
                    String load = RateDerivationMethodTypeEnum.LOAD.toString();
                    String interperiodPuntualMethodType = interperiodPuntualRateEditionForm.getValueAsString(DataSourceDS.RATE_DERIVATION_METHOD_TYPE);
                    String interperiodPercentageMethodType = interperiodPercentageRateEditionForm.getValueAsString(DataSourceDS.RATE_DERIVATION_METHOD_TYPE);
                    String annualPuntualMethodType = annualPuntualRateEditionForm.getValueAsString(DataSourceDS.RATE_DERIVATION_METHOD_TYPE);
                    String annualPercentageMethodType = annualPercentageRateEditionForm.getValueAsString(DataSourceDS.RATE_DERIVATION_METHOD_TYPE);

                    if (!load.equals(interperiodPuntualMethodType) && !load.equals(interperiodPercentageMethodType) && !load.equals(annualPuntualMethodType)
                            && !load.equals(annualPercentageMethodType)) {
                        return false;
                    }
                }
                return true;
            }
        };
        absoluteMethod.setValidators(absoluteMethodValidator);
        absoluteMethodValidator.setErrorMessage(getMessages().infoDataSourceAbsoluteMethod());

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
        timeValue.setValidators(TimeVariableWebUtils.getTimeCustomValidator());

        ViewTextItem geographicalVariable = new ViewTextItem(DataSourceDS.GEO_VARIABLE, getConstants().dataSourceGeographicalVariable());
        geographicalVariable.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return value != null && !value.toString().isEmpty();
            }
        });

        final GeographicalSelectItem geographicalValue = new GeographicalSelectItem(DataSourceDS.GEO_VALUE, getConstants().dataSourceGeographicalValue());
        geographicalValue.setRequired(true);
        geographicalValue.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !form.getItem(DataSourceDS.GEO_VARIABLE).isVisible();
            }
        });
        geographicalValue.getGeoGranularitySelectItem().addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                // Clear geographical value
                geographicalValue.setGeoValuesValueMap(new LinkedHashMap<String, String>());
                geographicalValue.setGeoValue(new String());
                // Set values with selected granularity
                if (event.getValue() != null && !event.getValue().toString().isEmpty()) {
                    uiHandlers.retrieveGeographicalValues(event.getValue().toString());
                }
            }
        });

        ViewTextItem measureVariable = new ViewTextItem(DataSourceDS.MEASURE_VARIABLE, getConstants().dataSourceMeasureVariable());
        measureVariable.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return value != null && !StringUtils.isBlank(value.toString());
            }
        });

        VariableCanvasItem variables = new VariableCanvasItem(DataSourceDS.OTHER_VARIABLES, getConstants().dataSourceOtherVariables());

        generalEditionForm.setFields(queryUuid, query, surveyCode, surveyTitle, surveyAcronym, surveyUrl, publishers, absoluteMethod, timeVariable, timeValue, geographicalVariable, geographicalValue,
                measureVariable, variables);

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

    public void setDataDefinitionsOperationCodes(List<String> operationCodes) {
        this.dataDefinitionsOperationCodes = operationCodes;
    }

    public void setDataDefinitions(List<DataDefinitionDto> dataDefinitionsDtos) {
        dataDefinitionsSearchWindow.setDataDefinitionList(dataDefinitionsDtos);
    }

    public void setDataDefinition(DataDefinitionDto dataDefinitionDto) {
        generalForm.setValue(DataSourceDS.QUERY_TEXT, dataDefinitionDto.getName());
        generalStaticEditionForm.setValue(DataSourceDS.QUERY_TEXT, dataDefinitionDto.getName());
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

        // Measure variable
        generalForm.setValue(DataSourceDS.MEASURE_VARIABLE, dataStructureDto.getContVariable());
        generalStaticEditionForm.setValue(DataSourceDS.MEASURE_VARIABLE, dataStructureDto.getContVariable());
        generalEditionForm.setValue(DataSourceDS.MEASURE_VARIABLE, dataStructureDto.getContVariable());
        if (!StringUtils.isBlank(dataStructureDto.getContVariable())) {
            // If there is a contVariable (measure variable), set variable values
            ((SelectItem) generalEditionForm.getItem(DataSourceDS.ABSOLUTE_METHOD)).setValueMap(getMeasureVariableValues(dataStructureDto.getContVariable(), dataStructureDto.getValueCodes(),
                    dataStructureDto.getValueLabels()));

            interperiodPuntualRateEditionForm
                    .setMeasureVariableValues(getMeasureVariableValues(dataStructureDto.getContVariable(), dataStructureDto.getValueCodes(), dataStructureDto.getValueLabels()));
            interperiodPercentageRateEditionForm.setMeasureVariableValues(getMeasureVariableValues(dataStructureDto.getContVariable(), dataStructureDto.getValueCodes(),
                    dataStructureDto.getValueLabels()));
            annualPuntualRateEditionForm.setMeasureVariableValues(getMeasureVariableValues(dataStructureDto.getContVariable(), dataStructureDto.getValueCodes(), dataStructureDto.getValueLabels()));
            annualPercentageRateEditionForm.setMeasureVariableValues(getMeasureVariableValues(dataStructureDto.getContVariable(), dataStructureDto.getValueCodes(), dataStructureDto.getValueLabels()));
        } else {
            // If not, set OBS_VALUE value map
            ((SelectItem) generalEditionForm.getItem(DataSourceDS.ABSOLUTE_METHOD)).setValueMap(CommonUtils.getObsValueLValueMap());

            interperiodPuntualRateEditionForm.setValue(DataSourceDS.RATE_DERIVATION_METHOD_LOAD_VIEW, getConstants().dataSourceObsValue());
            interperiodPercentageRateEditionForm.setValue(DataSourceDS.RATE_DERIVATION_METHOD_LOAD_VIEW, getConstants().dataSourceObsValue());
            annualPuntualRateEditionForm.setValue(DataSourceDS.RATE_DERIVATION_METHOD_LOAD_VIEW, getConstants().dataSourceObsValue());
            annualPercentageRateEditionForm.setValue(DataSourceDS.RATE_DERIVATION_METHOD_LOAD_VIEW, getConstants().dataSourceObsValue());
        }

        // Variables and categories
        ((VariableCanvasItem) generalEditionForm.getItem(DataSourceDS.OTHER_VARIABLES)).clearValue();
        ((VariableCanvasItem) generalEditionForm.getItem(DataSourceDS.OTHER_VARIABLES)).setVariablesAndCategories(dataStructureDto);

        redrawForms();
    }

    public void setGeographicalValues(List<GeographicalValueDto> geographicalValueDtos) {
        ((GeographicalSelectItem) generalEditionForm.getItem(DataSourceDS.GEO_VALUE)).setGeoValuesValueMap(CommonUtils.getGeographicalValuesValueMap(geographicalValueDtos));
    }

    public void setGeographicalValue(GeographicalValueDto geographicalValueDto) {
        generalForm.setValue(DataSourceDS.GEO_VALUE, InternationalStringUtils.getLocalisedString(geographicalValueDto.getTitle()));
        generalStaticEditionForm.setValue(DataSourceDS.GEO_VALUE, InternationalStringUtils.getLocalisedString(geographicalValueDto.getTitle()));
    }

    public DataSourceDto getDataSourceDto() {
        // If query form has been touched
        if (generalEditionForm.isVisible()) {
            dataSourceDto.setDataGpeUuid(generalEditionForm.getValueAsString(DataSourceDS.QUERY_UUID));
        }
        dataSourceDto.setPxUri(dataStructureDto.getPxUri());

        dataSourceDto.setSourceSurveyCode(dataStructureDto.getSurveyCode());
        dataSourceDto.setSourceSurveyTitle(InternationalStringUtils.updateInternationalString(LocaleMock.SPANISH, new InternationalStringDto(), dataStructureDto.getSurveyTitle()));
        if (generalEditionForm.isVisible()) {
            dataSourceDto.setSourceSurveyAcronym((InternationalStringDto) generalEditionForm.getValue(DataSourceDS.SOURCE_SURVEY_ACRONYM));
            dataSourceDto.setSourceSurveyUrl(generalEditionForm.getValueAsString(DataSourceDS.SOURCE_SURVEY_URL));
        }

        dataSourceDto.setPublishers(dataStructureDto.getPublishers());

        if (generalEditionForm.isVisible()) {
            dataSourceDto.setAbsoluteMethod(generalEditionForm.getValueAsString(DataSourceDS.ABSOLUTE_METHOD));
        }

        dataSourceDto.setTimeVariable(dataStructureDto.getTemporalVariable());
        dataSourceDto.setGeographicalVariable(dataStructureDto.getSpatialVariable());

        if (generalEditionForm.isVisible()) {
            dataSourceDto.setTimeValue(generalEditionForm.getItem(DataSourceDS.TIME_VALUE).isVisible() ? generalEditionForm.getValueAsString(DataSourceDS.TIME_VALUE) : null);
            dataSourceDto.setGeographicalValueUuid(generalEditionForm.getItem(DataSourceDS.GEO_VALUE).isVisible() ? CommonUtils.getUuidString(((GeographicalSelectItem) generalEditionForm
                    .getItem(DataSourceDS.GEO_VALUE)).getSelectedGeoValue()) : null);
        }

        if (interperiodPuntualRateEditionForm.isRateNotApplicable()) {
            dataSourceDto.setInterperiodPuntualRate(null);
        } else {
            dataSourceDto.setInterperiodPuntualRate(interperiodPuntualRateEditionForm.getValue());
        }

        if (interperiodPercentageRateEditionForm.isRateNotApplicable()) {
            dataSourceDto.setInterperiodPercentageRate(null);
        } else {
            dataSourceDto.setInterperiodPercentageRate(interperiodPercentageRateEditionForm.getValue());
        }

        if (annualPuntualRateEditionForm.isRateNotApplicable()) {
            dataSourceDto.setAnnualPuntualRate(null);
        } else {
            dataSourceDto.setAnnualPuntualRate(annualPuntualRateEditionForm.getValue());
        }

        if (annualPercentageRateEditionForm.isRateNotApplicable()) {
            dataSourceDto.setAnnualPercentageRate(null);
        } else {
            dataSourceDto.setAnnualPercentageRate(annualPercentageRateEditionForm.getValue());
        }

        if (generalEditionForm.isVisible()) {
            dataSourceDto.getOtherVariables().clear();
            dataSourceDto.getOtherVariables().addAll(((VariableCanvasItem) generalEditionForm.getItem(DataSourceDS.OTHER_VARIABLES)).getValue());
        }

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

    public void setGeographicalGranularities(List<GeographicalGranularityDto> geographicalGranularityDtos) {
        LinkedHashMap<String, String> valueMap = CommonUtils.getGeographicalGranularituesValueMap(geographicalGranularityDtos);
        ((GeographicalSelectItem) generalEditionForm.getItem(DataSourceDS.GEO_VALUE)).setGeoGranularitiesValueMap(valueMap);
    }

    private List<String> getSelectedDataSources() {
        List<String> codes = new ArrayList<String>();
        for (ListGridRecord record : dataSourcesListGrid.getSelectedRecords()) {
            codes.add(record.getAttribute(DataSourceDS.UUID));
        }
        return codes;
    }

    /**
     * In edition mode, shows query form in view mode
     */
    private void setViewQueryMode() {
        generalEditionForm.hide();
        generalStaticEditionForm.show();

        interperiodPuntualRateEditionForm.setViewQueryMode();
        annualPuntualRateEditionForm.setViewQueryMode();
        interperiodPercentageRateEditionForm.setViewQueryMode();
        annualPercentageRateEditionForm.setViewQueryMode();

        mainFormLayout.markForRedraw();
    }

    /**
     * In edition mode, shows query form in edition mode
     */
    private void setEditionQueryMode() {
        generalEditionForm.show();
        generalStaticEditionForm.hide();

        interperiodPuntualRateEditionForm.setEditionQueryMode();
        annualPuntualRateEditionForm.setEditionQueryMode();
        interperiodPercentageRateEditionForm.setEditionQueryMode();
        annualPercentageRateEditionForm.setEditionQueryMode();

        mainFormLayout.markForRedraw();
    }

    private LinkedHashMap<String, String> getMeasureVariableValues(String contVariable, Map<String, List<String>> categoryCodes, Map<String, List<String>> categoryLabels) {
        if (categoryCodes.containsKey(contVariable) && categoryLabels.containsKey(contVariable)) {
            return CommonUtils.getVariableCategoriesValueMap(categoryCodes.get(contVariable), categoryLabels.get(contVariable));
        }
        return null;
    }

    private void clearAllQueryValues() {
        ((RequiredTextItem) generalEditionForm.getItem(DataSourceDS.QUERY_UUID)).clearValue();
        ((SearchViewTextItem) generalEditionForm.getItem(DataSourceDS.QUERY_TEXT)).clearValue();
        clearQueryDependentFields();
    }

    private void clearQueryDependentFields() {
        ((ViewTextItem) generalEditionForm.getItem(DataSourceDS.SOURCE_SURVEY_CODE)).clearValue();
        ((ViewMultiLanguageTextItem) generalEditionForm.getItem(DataSourceDS.SOURCE_SURVEY_TITLE)).clearValue();
        ((MultiLanguageTextItem) generalEditionForm.getItem(DataSourceDS.SOURCE_SURVEY_ACRONYM)).clearValue();
        ((TextItem) generalEditionForm.getItem(DataSourceDS.SOURCE_SURVEY_URL)).clearValue();
        ((ViewTextItem) generalEditionForm.getItem(DataSourceDS.PUBLISHERS)).clearValue();
        ((SelectItem) generalEditionForm.getItem(DataSourceDS.ABSOLUTE_METHOD)).clearValue();

        ((ViewTextItem) generalEditionForm.getItem(DataSourceDS.TIME_VARIABLE)).clearValue();
        ((TextItem) generalEditionForm.getItem(DataSourceDS.TIME_VALUE)).clearValue();
        ((ViewTextItem) generalEditionForm.getItem(DataSourceDS.GEO_VARIABLE)).clearValue();
        ((GeographicalSelectItem) generalEditionForm.getItem(DataSourceDS.GEO_VALUE)).clearValue();
        ((ViewTextItem) generalEditionForm.getItem(DataSourceDS.MEASURE_VARIABLE)).clearValue();
        ((VariableCanvasItem) generalEditionForm.getItem(DataSourceDS.OTHER_VARIABLES)).clearValue();

        ((SelectItem) interperiodPuntualRateEditionForm.getItem(DataSourceDS.RATE_DERIVATION_METHOD_LOAD)).clearValue();
        ((SelectItem) interperiodPercentageRateEditionForm.getItem(DataSourceDS.RATE_DERIVATION_METHOD_LOAD)).clearValue();
        ((SelectItem) annualPuntualRateEditionForm.getItem(DataSourceDS.RATE_DERIVATION_METHOD_LOAD)).clearValue();
        ((SelectItem) annualPercentageRateEditionForm.getItem(DataSourceDS.RATE_DERIVATION_METHOD_LOAD)).clearValue();

        ((SelectItem) interperiodPuntualRateEditionForm.getItem(DataSourceDS.RATE_DERIVATION_METHOD_LOAD)).setValueMap(new String());
        ((SelectItem) interperiodPercentageRateEditionForm.getItem(DataSourceDS.RATE_DERIVATION_METHOD_LOAD)).setValueMap(new String());
        ((SelectItem) annualPuntualRateEditionForm.getItem(DataSourceDS.RATE_DERIVATION_METHOD_LOAD)).setValueMap(new String());
        ((SelectItem) annualPercentageRateEditionForm.getItem(DataSourceDS.RATE_DERIVATION_METHOD_LOAD)).setValueMap(new String());

        ((ViewTextItem) interperiodPuntualRateEditionForm.getItem(DataSourceDS.RATE_DERIVATION_METHOD_LOAD_VIEW)).clearValue();
        ((ViewTextItem) interperiodPercentageRateEditionForm.getItem(DataSourceDS.RATE_DERIVATION_METHOD_LOAD_VIEW)).clearValue();
        ((ViewTextItem) annualPuntualRateEditionForm.getItem(DataSourceDS.RATE_DERIVATION_METHOD_LOAD_VIEW)).clearValue();
        ((ViewTextItem) annualPercentageRateEditionForm.getItem(DataSourceDS.RATE_DERIVATION_METHOD_LOAD_VIEW)).clearValue();
    }

    private void redrawForms() {
        generalForm.markForRedraw();
        generalStaticEditionForm.markForRedraw();
        generalEditionForm.markForRedraw();

        interperiodPuntualRateEditionForm.markForRedraw();
        interperiodPercentageRateEditionForm.markForRedraw();
        annualPuntualRateEditionForm.markForRedraw();
        annualPercentageRateEditionForm.markForRedraw();
    }

    private void showToolStripDeleteButton() {
        if (ClientSecurityUtils.canDeleteDataSource()) {
            toolStrip.getDeleteButton().show();
        }
    }

    private SearchViewTextItem getQueryItem() {
        SearchViewTextItem query = new SearchViewTextItem(DataSourceDS.QUERY_TEXT, getConstants().dataSourceQuery());
        query.setRequired(true);
        query.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                dataDefinitionsSearchWindow = new DataDefinitionsSearchWindow(getConstants().dataSourceQuerySelection());
                dataDefinitionsSearchWindow.setDataDefinitionsOperationCodes(dataDefinitionsOperationCodes);
                dataDefinitionsSearchWindow.getSearchButton().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (dataDefinitionsSearchWindow.validateForm()) {
                            uiHandlers.retrieveDataDefinitionsByOperationCode(dataDefinitionsSearchWindow.getSelectedOperationCode());
                        }
                    }
                });
                dataDefinitionsSearchWindow.getAceptButton().addClickHandler(new ClickHandler() {

                    @Override
                    public void onClick(ClickEvent event) {
                        DataDefinitionDto dataDefinitionDto = dataDefinitionsSearchWindow.getSelectedDataDefinitionDto();
                        dataDefinitionsSearchWindow.destroy();
                        generalEditionForm.setValue(DataSourceDS.QUERY_UUID, dataDefinitionDto != null ? dataDefinitionDto.getUuid() : new String());
                        generalEditionForm.setValue(DataSourceDS.QUERY_TEXT, dataDefinitionDto != null ? dataDefinitionDto.getName() : new String());
                        generalEditionForm.getItem(DataSourceDS.QUERY_TEXT).validate();

                        if (!StringUtils.isBlank(dataDefinitionDto.getUuid())) {
                            // Clear query dependent field values
                            clearQueryDependentFields();

                            // Retrieve data structure
                            uiHandlers.retrieveDataStructure(dataDefinitionDto.getUuid());
                        } else {
                            // Clear query dependent field values
                            clearQueryDependentFields();

                            // Clear temporal variable
                            generalEditionForm.setValue(DataSourceDS.TIME_VARIABLE, new String());
                            // Clear spatial variable
                            generalEditionForm.setValue(DataSourceDS.GEO_VARIABLE, new String());
                        }
                    }
                });
            }
        });
        query.setValidators(new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                return generalEditionForm.getItem(DataSourceDS.QUERY_UUID).validate();
            }
        });
        return query;
    }

}
