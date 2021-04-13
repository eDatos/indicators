package es.gobcan.istac.indicators.web.client.indicator.view;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getMessages;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.web.common.client.utils.ApplicationEditionLanguages;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.utils.TimeVariableWebUtils;
import org.siemac.metamac.web.common.client.widgets.InformationWindow;
import org.siemac.metamac.web.common.client.widgets.actions.search.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.SearchViewTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.external.SearchExternalItemLinkItem;

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.HiddenItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.form.validator.CustomValidator;
import com.smartgwt.client.widgets.layout.VLayout;

import es.gobcan.istac.indicators.core.dto.DataDefinitionDto;
import es.gobcan.istac.indicators.core.dto.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.DataStructureDto;
import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;
import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.IndicatorSummaryDto;
import es.gobcan.istac.indicators.core.dto.UnitMultiplierDto;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorProcStatusEnum;
import es.gobcan.istac.indicators.core.enume.domain.QuantityTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.QueryEnvironmentEnum;
import es.gobcan.istac.indicators.core.enume.domain.RateDerivationMethodTypeEnum;
import es.gobcan.istac.indicators.web.client.IndicatorsValues;
import es.gobcan.istac.indicators.web.client.enums.IndicatorCalculationTypeEnum;
import es.gobcan.istac.indicators.web.client.enums.RateDerivationTypeEnum;
import es.gobcan.istac.indicators.web.client.indicator.presenter.IndicatorUiHandler;
import es.gobcan.istac.indicators.web.client.model.ds.DataSourceDS;
import es.gobcan.istac.indicators.web.client.utils.ClientSecurityUtils;
import es.gobcan.istac.indicators.web.client.utils.CommonUtils;
import es.gobcan.istac.indicators.web.client.utils.IndicatorsWebConstants;
import es.gobcan.istac.indicators.web.client.widgets.DataDefinitionsSearchWindow;
import es.gobcan.istac.indicators.web.client.widgets.DataSourceMainFormLayout;
import es.gobcan.istac.indicators.web.client.widgets.GeographicalSelectItem;
import es.gobcan.istac.indicators.web.client.widgets.RateDerivationForm;
import es.gobcan.istac.indicators.web.client.widgets.SearchSingleQueryPaginatedWindow;
import es.gobcan.istac.indicators.web.client.widgets.VariableCanvasItem;
import es.gobcan.istac.indicators.web.client.widgets.ViewDataSourceGeneralForm;
import es.gobcan.istac.indicators.web.client.widgets.ViewRateDerivationForm;
import es.gobcan.istac.indicators.web.client.widgets.ViewVariableCanvasItem;
import es.gobcan.istac.indicators.web.shared.criteria.QueryWebCriteria;

public class DataSourcePanel extends VLayout {

    // View Form
    private ViewDataSourceGeneralForm        generalForm;
    private GroupDynamicForm                 dataForm;
    private ViewRateDerivationForm           interperiodPuntualRateForm;
    private ViewRateDerivationForm           annualPuntualRateForm;
    private ViewRateDerivationForm           interperiodPercentageRateForm;
    private ViewRateDerivationForm           annualPercentageRateForm;

    // Edition Form
    private GroupDynamicForm                 generalEditionForm;
    private ViewDataSourceGeneralForm        generalStaticEditionForm;
    private GroupDynamicForm                 dataEditionForm;
    private RateDerivationForm               interperiodPuntualRateEditionForm;
    private RateDerivationForm               annualPuntualRateEditionForm;
    private RateDerivationForm               interperiodPercentageRateEditionForm;
    private RateDerivationForm               annualPercentageRateEditionForm;

    private DataDefinitionsSearchWindow      dataDefinitionsSearchWindow;
    private List<String>                     dataDefinitionsOperationCodes;
    private DataSourceMainFormLayout         mainFormLayout;

    // When we are editing the form, but query dependent fields are in view mode
    private boolean                          queryEditionViewMode;

    private DataSourceDto                    dataSourceDto;
    private DataStructureDto                 dataStructureDtoEdition;
    private IndicatorDto                     indicatorDto;
    private IndicatorUiHandler               uiHandlers;

    private SearchSingleQueryPaginatedWindow searchQueryMetamacWindow;
    private List<String>                     editionLanguages;

    public DataSourcePanel() {
        // MainFormLayout

        mainFormLayout = new DataSourceMainFormLayout();
        mainFormLayout.setTitleLabelContents(getConstants().dataSource());

        // Edit: Add a custom handler to check indicator status before start editing
        mainFormLayout.getEditToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (IndicatorProcStatusEnum.PUBLISHED.equals(indicatorDto.getProcStatus()) || IndicatorProcStatusEnum.ARCHIVED.equals(indicatorDto.getProcStatus())) {
                    // Create a new version of the indicator
                    final InformationWindow window = new InformationWindow(getMessages().indicatorEditionInfo(), getMessages().indicatorEditionInfoDetailedMessage());
                    window.show();
                } else {
                    // Default behavior
                    mainFormLayout.setEditionMode();
                }
            }
        });

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
                    if (generalEditionForm.validate(false) && dataEditionForm.validate(false) && interperiodPuntualRateEditionForm.validate(false) && annualPuntualRateEditionForm.validate(false)
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
                    hide();
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

        addMember(mainFormLayout);
    }

    public void setGeographicalValue(GeographicalValueDto geographicalValueDto) {
        generalForm.setValue(DataSourceDS.GEO_VALUE, InternationalStringUtils.getLocalisedString(geographicalValueDto.getTitle()));
        generalStaticEditionForm.setValue(DataSourceDS.GEO_VALUE, InternationalStringUtils.getLocalisedString(geographicalValueDto.getTitle()));
    }

    public void setGeographicalValues(List<GeographicalValueDto> geographicalValueDtos) {
        ((GeographicalSelectItem) generalEditionForm.getItem(DataSourceDS.GEO_VALUE)).setGeoValuesValueMap(CommonUtils.getGeographicalValuesValueMap(geographicalValueDtos));
    }

    public void setRateIndicators(List<IndicatorSummaryDto> indicatorDtos, RateDerivationTypeEnum rateDerivationTypeEnum, IndicatorCalculationTypeEnum indicatorCalculationTypeEnum) {
        // Only percentage rates have denominator and numerator indicators
        if (RateDerivationTypeEnum.INTERPERIOD_PERCENTAGE_RATE_TYPE.equals(rateDerivationTypeEnum)) {
            if (IndicatorCalculationTypeEnum.NUMERATOR.equals(indicatorCalculationTypeEnum)) {
                interperiodPercentageRateEditionForm.setNumeratorIndicators(indicatorDtos);
            } else {
                interperiodPercentageRateEditionForm.setDenominatorIndicators(indicatorDtos);
            }
        } else if (RateDerivationTypeEnum.ANNUAL_PERCENTAGE_RATE_TYPE.equals(rateDerivationTypeEnum)) {
            if (IndicatorCalculationTypeEnum.NUMERATOR.equals(indicatorCalculationTypeEnum)) {
                annualPercentageRateEditionForm.setNumeratorIndicators(indicatorDtos);
            } else {
                annualPercentageRateEditionForm.setDenominatorIndicators(indicatorDtos);
            }
        }
    }

    public void setRateIndicator(IndicatorDto indicatorDto, RateDerivationTypeEnum rateDerivationTypeEnum, IndicatorCalculationTypeEnum indicatorCalculationTypeEnum) {
        // Only percentage rates have denominator and numerator indicators
        if (RateDerivationTypeEnum.INTERPERIOD_PERCENTAGE_RATE_TYPE.equals(rateDerivationTypeEnum)) {
            if (IndicatorCalculationTypeEnum.NUMERATOR.equals(indicatorCalculationTypeEnum)) {
                interperiodPercentageRateForm.setNumeratorIndicator(indicatorDto);
                interperiodPercentageRateEditionForm.setNumeratorIndicator(indicatorDto);
            } else {
                interperiodPercentageRateForm.setDenominatorIndicator(indicatorDto);
                interperiodPercentageRateEditionForm.setDenominatorIndicator(indicatorDto);
            }
        } else if (RateDerivationTypeEnum.ANNUAL_PERCENTAGE_RATE_TYPE.equals(rateDerivationTypeEnum)) {
            if (IndicatorCalculationTypeEnum.NUMERATOR.equals(indicatorCalculationTypeEnum)) {
                annualPercentageRateForm.setNumeratorIndicator(indicatorDto);
                annualPercentageRateEditionForm.setNumeratorIndicator(indicatorDto);
            } else {
                annualPercentageRateForm.setDenominatorIndicator(indicatorDto);
                annualPercentageRateEditionForm.setDenominatorIndicator(indicatorDto);
            }
        }
    }

    public DataSourceDto getDataSourceDto() {
        // If query form has been touched

        String queryEnvironmentAsString = generalEditionForm.getValueAsString(DataSourceDS.QUERY_ENVIRONMENT);
        if (!StringUtils.isEmpty(queryEnvironmentAsString)) {
            dataSourceDto.setQueryEnvironment(QueryEnvironmentEnum.valueOf(queryEnvironmentAsString));
        }

        dataSourceDto.setStatResource(generalEditionForm.getValueAsExternalItemDto(DataSourceDS.QUERY_METAMAC));
        dataSourceDto.setQueryUrn(dataStructureDtoEdition.getQueryUrn());

        if (generalEditionForm.isVisible()) {
            if (QueryEnvironmentEnum.METAMAC.equals(dataSourceDto.getQueryEnvironment())) {
                dataSourceDto.setQueryUuid(dataStructureDtoEdition.getQueryUrn());
            } else {
                dataSourceDto.setQueryUuid(generalEditionForm.getValueAsString(DataSourceDS.QUERY_UUID));
            }
        }

        dataSourceDto.setSourceSurveyCode(dataStructureDtoEdition.getSurveyCode());
        dataSourceDto.setSourceSurveyTitle(InternationalStringUtils.updateInternationalString(editionLanguages.get(0), new InternationalStringDto(), dataStructureDtoEdition.getSurveyTitle()));
        if (generalEditionForm.isVisible()) {
            dataSourceDto.setSourceSurveyAcronym(generalEditionForm.getValueAsInternationalStringDto(DataSourceDS.SOURCE_SURVEY_ACRONYM));
            dataSourceDto.setSourceSurveyUrl(generalEditionForm.getValueAsString(DataSourceDS.SOURCE_SURVEY_URL));
        }

        dataSourceDto.setPublishers(dataStructureDtoEdition.getPublishers());

        if (generalEditionForm.isVisible()) {
            dataSourceDto.setAbsoluteMethod(dataEditionForm.getValueAsString(DataSourceDS.ABSOLUTE_METHOD));
        }

        if (generalEditionForm.isVisible()) {
            dataSourceDto.setTimeVariable(dataStructureDtoEdition.getTemporalVariable());
        }

        if (generalEditionForm.isVisible()) {
            if (generalEditionForm.getItem(DataSourceDS.GEO_VARIABLE).isVisible()) {
                dataSourceDto.setGeographicalVariable(generalEditionForm.getValueAsString(DataSourceDS.GEO_VARIABLE));
            } else {
                dataSourceDto.setGeographicalVariable(null);
            }
        }

        if (generalEditionForm.isVisible()) {
            if (QueryEnvironmentEnum.METAMAC.equals(dataSourceDto.getQueryEnvironment())) {
                dataSourceDto.setTimeValue(generalEditionForm.getItem(DataSourceDS.TIME_VALUE_METAMAC).isVisible() ? generalEditionForm.getValueAsString(DataSourceDS.TIME_VALUE_METAMAC) : null);

                dataSourceDto.setGeographicalValueUuid(generalEditionForm.getItem(DataSourceDS.GEO_VALUE_TEXT_METAMAC).isVisible()
                        ? CommonUtils.getUuidString(generalEditionForm.getValueAsString(DataSourceDS.GEO_VALUE_UUID_METAMAC))
                        : null);

            } else {
                dataSourceDto.setTimeValue(generalEditionForm.getItem(DataSourceDS.TIME_VALUE).isVisible() ? generalEditionForm.getValueAsString(DataSourceDS.TIME_VALUE) : null);

                dataSourceDto.setGeographicalValueUuid(generalEditionForm.getItem(DataSourceDS.GEO_VALUE).isVisible()
                        ? CommonUtils.getUuidString(((GeographicalSelectItem) generalEditionForm.getItem(DataSourceDS.GEO_VALUE)).getSelectedGeoValue())
                        : null);
            }
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

    public void setDataStructureForEdition(DataStructureDto dataStructureDto) {
        dataStructureDtoEdition = dataStructureDto;

        // Source survey code
        generalEditionForm.setValue(DataSourceDS.SOURCE_SURVEY_CODE, dataStructureDto.getSurveyCode());

        // Source survey title
        InternationalStringDto internationalStringDto = InternationalStringUtils.updateInternationalString(ApplicationEditionLanguages.SPANISH, new InternationalStringDto(),
                dataStructureDto.getSurveyTitle());
        generalEditionForm.setValue(DataSourceDS.SOURCE_SURVEY_TITLE, internationalStringDto);

        // Publishers
        generalEditionForm.setValue(DataSourceDS.PUBLISHERS, CommonWebUtils.getStringListToString(dataStructureDto.getPublishers()));

        // Temporal variable
        generalEditionForm.setValue(DataSourceDS.TIME_VARIABLE, dataStructureDto.getTemporalVariable());

        // Temporal value
        generalEditionForm.setValue(DataSourceDS.TIME_VALUE, dataStructureDto.getTemporalValue());
        generalEditionForm.setValue(DataSourceDS.TIME_VALUE_METAMAC, dataStructureDto.getTemporalValue());

        // Spatial Value (metamac)
        if (dataStructureDto.getGeographicalValueDto() != null) {
            generalEditionForm.setValue(DataSourceDS.GEO_VALUE_TEXT_METAMAC, InternationalStringUtils.getLocalisedString(dataStructureDto.getGeographicalValueDto().getTitle()));
            generalEditionForm.setValue(DataSourceDS.GEO_VALUE_UUID_METAMAC, dataStructureDto.getGeographicalValueDto().getUuid());
        }

        // Spatial variable
        SelectItem geoVariableItem = (SelectItem) generalEditionForm.getItem(DataSourceDS.GEO_VARIABLE);
        if (dataStructureDto.getSpatialVariables() != null && !dataStructureDto.getSpatialVariables().isEmpty()) {
            geoVariableItem.setValueMap(dataStructureDto.getSpatialVariables().toArray(new String[dataStructureDto.getSpatialVariables().size()]));
        } else {
            geoVariableItem.setValue((Object) null);
        }

        // Measure variable
        generalStaticEditionForm.setValue(DataSourceDS.MEASURE_VARIABLE, dataStructureDto.getContVariable());
        generalEditionForm.setValue(DataSourceDS.MEASURE_VARIABLE, dataStructureDto.getContVariable());
        if (!StringUtils.isBlank(dataStructureDto.getContVariable())) {
            // If there is a contVariable (measure variable), set variable values
            ((SelectItem) dataEditionForm.getItem(DataSourceDS.ABSOLUTE_METHOD))
                    .setValueMap(getMeasureVariableValues(dataStructureDto.getContVariable(), dataStructureDto.getValueCodes(), dataStructureDto.getValueLabels()));

            interperiodPuntualRateEditionForm
                    .setMeasureVariableValues(getMeasureVariableValues(dataStructureDto.getContVariable(), dataStructureDto.getValueCodes(), dataStructureDto.getValueLabels()));
            interperiodPercentageRateEditionForm
                    .setMeasureVariableValues(getMeasureVariableValues(dataStructureDto.getContVariable(), dataStructureDto.getValueCodes(), dataStructureDto.getValueLabels()));
            annualPuntualRateEditionForm.setMeasureVariableValues(getMeasureVariableValues(dataStructureDto.getContVariable(), dataStructureDto.getValueCodes(), dataStructureDto.getValueLabels()));
            annualPercentageRateEditionForm.setMeasureVariableValues(getMeasureVariableValues(dataStructureDto.getContVariable(), dataStructureDto.getValueCodes(), dataStructureDto.getValueLabels()));
        } else {
            // If not, set OBS_VALUE value map
            ((SelectItem) dataEditionForm.getItem(DataSourceDS.ABSOLUTE_METHOD)).setValueMap(CommonUtils.getObsValueLValueMap());

            interperiodPuntualRateEditionForm.setValue(DataSourceDS.RATE_DERIVATION_METHOD_LOAD_VIEW, getConstants().dataSourceObsValue());
            interperiodPercentageRateEditionForm.setValue(DataSourceDS.RATE_DERIVATION_METHOD_LOAD_VIEW, getConstants().dataSourceObsValue());
            annualPuntualRateEditionForm.setValue(DataSourceDS.RATE_DERIVATION_METHOD_LOAD_VIEW, getConstants().dataSourceObsValue());
            annualPercentageRateEditionForm.setValue(DataSourceDS.RATE_DERIVATION_METHOD_LOAD_VIEW, getConstants().dataSourceObsValue());
        }

        // Static Absolute method
        if (DataSourceDto.OBS_VALUE.equals(dataSourceDto.getAbsoluteMethod())) {
            dataEditionForm.getItem(DataSourceDS.ABSOLUTE_METHOD_VIEW).setValue(getConstants().dataSourceObsValue());
        } else {
            List<String> codes = dataStructureDto.getValueCodes().get(dataStructureDto.getContVariable());
            List<String> labels = dataStructureDto.getValueLabels().get(dataStructureDto.getContVariable());
            if (codes != null) {
                for (int i = 0; i < codes.size(); i++) {
                    if (codes.get(i).equals(dataSourceDto.getAbsoluteMethod())) {
                        dataEditionForm.getItem(DataSourceDS.ABSOLUTE_METHOD_VIEW).setValue(dataSourceDto.getAbsoluteMethod() + " - " + labels.get(i));
                        break;
                    }
                }
            }
        }

        // Variables and categories
        ((VariableCanvasItem) generalEditionForm.getItem(DataSourceDS.OTHER_VARIABLES)).clearValue();
        ((VariableCanvasItem) generalEditionForm.getItem(DataSourceDS.OTHER_VARIABLES)).setVariablesAndCategories(dataStructureDto);
        if (dataStructureDto.getSpatialVariables() != null && dataStructureDto.getSpatialVariables().size() == 1) {
            generalEditionForm.setValue(DataSourceDS.GEO_VARIABLE, dataStructureDto.getSpatialVariables().get(0));
            ((VariableCanvasItem) generalEditionForm.getItem(DataSourceDS.OTHER_VARIABLES)).restartVisibility(dataStructureDto.getSpatialVariables().get(0));
        }
        redrawForms();
    }

    public void setDataStructureView(DataStructureDto dataStructureDto) {
        // Absolute method
        if (DataSourceDto.OBS_VALUE.equals(dataSourceDto.getAbsoluteMethod())) {
            dataForm.getItem(DataSourceDS.ABSOLUTE_METHOD).setValue(getConstants().dataSourceObsValue());
        } else {
            List<String> codes = dataStructureDto.getValueCodes().get(dataStructureDto.getContVariable());
            List<String> labels = dataStructureDto.getValueLabels().get(dataStructureDto.getContVariable());
            if (codes != null) {
                for (int i = 0; i < codes.size(); i++) {
                    if (codes.get(i).equals(dataSourceDto.getAbsoluteMethod())) {
                        dataForm.getItem(DataSourceDS.ABSOLUTE_METHOD).setValue(dataSourceDto.getAbsoluteMethod() + " - " + labels.get(i));
                        break;
                    }
                }
            }
        }

        // Measure variable
        generalForm.setValue(DataSourceDS.MEASURE_VARIABLE, dataStructureDto.getContVariable());

        // Variables and categories
        ((ViewVariableCanvasItem) generalForm.getItem(DataSourceDS.OTHER_VARIABLES)).clearValue();
        ((ViewVariableCanvasItem) generalForm.getItem(DataSourceDS.OTHER_VARIABLES)).setVariablesAndCategories(dataSourceDto.getOtherVariables(), dataStructureDto);

        redrawForms();
    }

    public void setUnitMultipliers(List<UnitMultiplierDto> unitMultiplierDtos) {
        interperiodPuntualRateEditionForm.setUnitMultipliers(unitMultiplierDtos);
        interperiodPercentageRateEditionForm.setUnitMultipliers(unitMultiplierDtos);
        annualPuntualRateEditionForm.setUnitMultipliers(unitMultiplierDtos);
        annualPercentageRateEditionForm.setUnitMultipliers(unitMultiplierDtos);
    }

    public void setDataDefinition(DataDefinitionDto dataDefinitionDto) {
        generalForm.setValue(DataSourceDS.QUERY_TEXT, dataDefinitionDto.getName());
        generalStaticEditionForm.setValue(DataSourceDS.QUERY_TEXT, dataDefinitionDto.getName());
    }

    private void setSelectedRelatedQueryInEditionForm(ExternalItemDto relatedDsdDto) {
        generalEditionForm.setValue(DataSourceDS.QUERY_METAMAC, relatedDsdDto);
    }

    public void setDataSource(DataSourceDto dataSourceDto) {
        this.dataSourceDto = dataSourceDto;
        if (dataSourceDto.getUuid() != null) {
            mainFormLayout.setViewMode();
        } else {
            mainFormLayout.setEditionMode();
        }

        // Hide label and edit query button when we are going to create a new data source
        mainFormLayout.getLabel().hide();
        mainFormLayout.getEditQueryToolStripButton().hide();

        show();

        // Update dataSource title
        mainFormLayout.setTitleLabelContents(getConstants().dataSource() + (dataSourceDto.getUuid() != null ? " " + dataSourceDto.getUuid() : new String()));

        // Set query form visibility
        if (dataSourceDto.getUuid() == null) {
            setEditionQueryMode();
        } else {
            setViewQueryMode();
        }

        // Load data structure (common)
        if (dataSourceDto.getQueryUuid() != null && !dataSourceDto.getQueryUuid().isEmpty()) {
            uiHandlers.retrieveDataStructure(dataSourceDto.getQueryUuid());
        }

        setDataSourceViewMode(dataSourceDto);
        setDataSourceEditionMode(dataSourceDto);
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

    public void setIndicator(IndicatorDto indicatorDto) {
        this.indicatorDto = indicatorDto;
        interperiodPuntualRateEditionForm.setIndicator(indicatorDto);
        interperiodPercentageRateEditionForm.setIndicator(indicatorDto);
        annualPuntualRateEditionForm.setIndicator(indicatorDto);
        annualPercentageRateEditionForm.setIndicator(indicatorDto);
        setCanEdit(indicatorDto);
    }

    private void setCanEdit(IndicatorDto indicatorDto) {
        mainFormLayout.setCanEdit(ClientSecurityUtils.canEditDataSource(indicatorDto));
    }

    private void createViewForm() {
        generalForm = new ViewDataSourceGeneralForm(getConstants().datasourceGeneral());

        interperiodPuntualRateForm = new ViewRateDerivationForm(getConstants().dataSourceInterperiodVariation(), RateDerivationTypeEnum.INTERPERIOD_PUNTUAL_RATE_TYPE);

        dataForm = new GroupDynamicForm(getConstants().dataSourceData());
        ViewTextItem absoluteMethod = new ViewTextItem(DataSourceDS.ABSOLUTE_METHOD, getConstants().dataSourceDataSelection());
        dataForm.setFields(absoluteMethod);

        interperiodPercentageRateForm = new ViewRateDerivationForm(getConstants().dataSourceInterperiodVariationRate(), RateDerivationTypeEnum.INTERPERIOD_PERCENTAGE_RATE_TYPE);

        annualPuntualRateForm = new ViewRateDerivationForm(getConstants().dataSourceAnnualVariation(), RateDerivationTypeEnum.ANNUAL_PUNTUAL_RATE_TYPE);

        annualPercentageRateForm = new ViewRateDerivationForm(getConstants().dataSourceAnnualVariationRate(), RateDerivationTypeEnum.ANNUAL_PERCENTAGE_RATE_TYPE);

        mainFormLayout.addViewCanvas(generalForm);
        mainFormLayout.addViewCanvas(dataForm);
        mainFormLayout.addViewCanvas(interperiodPuntualRateForm);
        mainFormLayout.addViewCanvas(interperiodPercentageRateForm);
        mainFormLayout.addViewCanvas(annualPuntualRateForm);
        mainFormLayout.addViewCanvas(annualPercentageRateForm);
    }

    private void createEditionForm() {
        generalStaticEditionForm = new ViewDataSourceGeneralForm(getConstants().datasourceGeneral());

        generalEditionForm = new GroupDynamicForm(getConstants().datasourceGeneral());

        // Search data definition (query)
        RequiredSelectItem dataSourceQueryEnvironment = new RequiredSelectItem(DataSourceDS.QUERY_ENVIRONMENT, getConstants().dataSourceQueryEnvironment());
        dataSourceQueryEnvironment.setValueMap(CommonUtils.getQueryEnvironmentEnumValueMap());
        dataSourceQueryEnvironment.addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                clearAllQueryValues();
                generalEditionForm.markForRedraw();
            }
        });
        dataSourceQueryEnvironment.setValue(QueryEnvironmentEnum.METAMAC.toString());

        // Query from GPE
        RequiredTextItem queryUuid = new RequiredTextItem(DataSourceDS.QUERY_UUID, getConstants().dataSourceQuery());
        queryUuid.setShowIfCondition(CommonUtils.getFalseIfFunction());
        SearchViewTextItem query = getQueryItem();
        query.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return isEnvironmentSelected(form, QueryEnvironmentEnum.GPE);
            }
        });

        // Query from METAMAC
        SearchExternalItemLinkItem queryMetamacItem = getQueryMetamacItem();
        queryMetamacItem.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return isEnvironmentSelected(form, QueryEnvironmentEnum.METAMAC);
            }

        });

        ViewTextItem surveyCode = new ViewTextItem(DataSourceDS.SOURCE_SURVEY_CODE, getConstants().dataSourceSurveyCode());

        ViewMultiLanguageTextItem surveyTitle = new ViewMultiLanguageTextItem(DataSourceDS.SOURCE_SURVEY_TITLE, getConstants().dataSourceSurveyTitle());

        MultiLanguageTextItem surveyAcronym = new MultiLanguageTextItem(DataSourceDS.SOURCE_SURVEY_ACRONYM, getConstants().dataSourceSurveyAcronym());

        CustomTextItem surveyUrl = new CustomTextItem(DataSourceDS.SOURCE_SURVEY_URL, getConstants().dataSourceSurveyUrl());

        ViewTextItem publishers = new ViewTextItem(DataSourceDS.PUBLISHERS, getConstants().dataSourcePublishers());

        ViewTextItem timeVariable = new ViewTextItem(DataSourceDS.TIME_VARIABLE, getConstants().dataSourceTimeVariable());
        timeVariable.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return dataStructureHasTimeVariable();
            }
        });

        RequiredTextItem timeValue = new RequiredTextItem(DataSourceDS.TIME_VALUE, getConstants().dataSourceTimeValue());
        timeValue.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return isEnvironmentSelected(form, QueryEnvironmentEnum.GPE) && dataStructureHasTimeValue();
            }
        });
        timeValue.setValidators(TimeVariableWebUtils.getTimeCustomValidator());

        ViewTextItem timeValueMetamac = new ViewTextItem(DataSourceDS.TIME_VALUE_METAMAC, getConstants().dataSourceTimeValue());
        timeValueMetamac.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return isEnvironmentSelected(form, QueryEnvironmentEnum.METAMAC) && dataStructureHasTimeValue();
            }
        });

        SelectItem geographicalVariable = new SelectItem(DataSourceDS.GEO_VARIABLE, getConstants().dataSourceGeographicalVariable());
        geographicalVariable.setRequired(true);
        geographicalVariable.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return dataStructureHasGeoVariable();
            }
        });

        geographicalVariable.addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                VariableCanvasItem variablesItem = (VariableCanvasItem) generalEditionForm.getItem(DataSourceDS.OTHER_VARIABLES);
                if (event.getValue() != null) {
                    variablesItem.restartVisibility(event.getValue().toString());
                } else {
                    variablesItem.restartVisibility();
                }
                event.getForm().markForRedraw();
            }
        });

        final GeographicalSelectItem geographicalValueMulti = new GeographicalSelectItem(DataSourceDS.GEO_VALUE, getConstants().dataSourceGeographicalValue());
        geographicalValueMulti.setGeoGranularitiesValueMap(CommonUtils.getGeographicalGranularituesValueMap(IndicatorsValues.getGeographicalGranularities()));
        geographicalValueMulti.setRequired(true);
        geographicalValueMulti.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return isEnvironmentSelected(form, QueryEnvironmentEnum.GPE) && dataStructureHasGeoValue();
            }
        });
        geographicalValueMulti.getGeoGranularitySelectItem().addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                // Clear geographical value
                geographicalValueMulti.setGeoValuesValueMap(new LinkedHashMap<String, String>());
                geographicalValueMulti.setGeoValue(new String());
                // Set values with selected granularity
                if (event.getValue() != null && !event.getValue().toString().isEmpty()) {
                    uiHandlers.retrieveGeographicalValuesByGranularity(event.getValue().toString());
                }
            }
        });

        ViewTextItem geographicalValueMetamac = new ViewTextItem(DataSourceDS.GEO_VALUE_TEXT_METAMAC, getConstants().dataSourceGeographicalValue());
        geographicalValueMetamac.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return isEnvironmentSelected(form, QueryEnvironmentEnum.METAMAC) && dataStructureHasGeoValue();
            }
        });

        HiddenItem geographicalValueUUIDMetamac = new HiddenItem(DataSourceDS.GEO_VALUE_UUID_METAMAC);

        ViewTextItem measureVariable = new ViewTextItem(DataSourceDS.MEASURE_VARIABLE, getConstants().dataSourceMeasureVariable());
        measureVariable.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return dataStructureHasMeasureVariable();
            }
        });

        VariableCanvasItem variables = new VariableCanvasItem(DataSourceDS.OTHER_VARIABLES, getConstants().dataSourceOtherVariables());
        variables.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (dataStructureHasGeoValue()) {
                    return true;
                } else if (dataStructureHasGeoVariable()) {
                    return form.getItem(DataSourceDS.GEO_VARIABLE).getValue() != null;
                }
                return false;
            }
        });

        generalEditionForm.setFields(queryUuid, dataSourceQueryEnvironment, query, queryMetamacItem, surveyCode, surveyTitle, surveyAcronym, surveyUrl, publishers, timeVariable, timeValue,
                timeValueMetamac, geographicalVariable, geographicalValueMulti, geographicalValueMetamac, geographicalValueUUIDMetamac, measureVariable, variables);

        dataEditionForm = new GroupDynamicForm(getConstants().dataSourceData());
        ViewTextItem staticAbsoluteMethod = new ViewTextItem(DataSourceDS.ABSOLUTE_METHOD_VIEW, getConstants().dataSourceDataSelection());
        staticAbsoluteMethod.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return queryEditionViewMode;
            }
        });

        // ValueMap set in setDataStructure
        CustomSelectItem absoluteMethod = new CustomSelectItem(DataSourceDS.ABSOLUTE_METHOD, getConstants().dataSourceDataSelection());
        absoluteMethod.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !queryEditionViewMode;
            }
        });
        absoluteMethod.setValidateOnChange(true);
        CustomValidator emptyAbsoluteMethodValidator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                if (value == null || StringUtils.isBlank(value.toString())) {
                    // If absolute method is not set, at least one rate type must be LOAD
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
        emptyAbsoluteMethodValidator.setErrorMessage(getMessages().infoDataSourceEmptyData());
        CustomValidator filledAbsoluteMethodValidator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                if (value != null && !StringUtils.isBlank(value.toString())) {
                    // If absolute method is set, value selected cannot be the same as one of the rate methods
                    String absoluteMethod = (String) value;

                    String interperiodPuntualMethod = interperiodPuntualRateEditionForm.getItem(DataSourceDS.RATE_DERIVATION_METHOD_LOAD).isVisible()
                            ? interperiodPuntualRateEditionForm.getValueAsString(DataSourceDS.RATE_DERIVATION_METHOD_LOAD)
                            : (interperiodPuntualRateEditionForm.getItem(DataSourceDS.RATE_DERIVATION_METHOD_LOAD_VIEW).isVisible() ? DataSourceDto.OBS_VALUE : null);

                    String interperiodPercentageMethod = interperiodPercentageRateEditionForm.getItem(DataSourceDS.RATE_DERIVATION_METHOD_LOAD).isVisible()
                            ? interperiodPercentageRateEditionForm.getValueAsString(DataSourceDS.RATE_DERIVATION_METHOD_LOAD)
                            : (interperiodPercentageRateEditionForm.getItem(DataSourceDS.RATE_DERIVATION_METHOD_LOAD_VIEW).isVisible() ? DataSourceDto.OBS_VALUE : null);

                    String annualPuntualMethod = annualPuntualRateEditionForm.getItem(DataSourceDS.RATE_DERIVATION_METHOD_LOAD).isVisible()
                            ? annualPuntualRateEditionForm.getValueAsString(DataSourceDS.RATE_DERIVATION_METHOD_LOAD)
                            : (annualPuntualRateEditionForm.getItem(DataSourceDS.RATE_DERIVATION_METHOD_LOAD_VIEW).isVisible() ? DataSourceDto.OBS_VALUE : null);

                    String annualPercentageMethod = annualPercentageRateEditionForm.getItem(DataSourceDS.RATE_DERIVATION_METHOD_LOAD).isVisible()
                            ? annualPercentageRateEditionForm.getValueAsString(DataSourceDS.RATE_DERIVATION_METHOD_LOAD)
                            : (annualPercentageRateEditionForm.getItem(DataSourceDS.RATE_DERIVATION_METHOD_LOAD_VIEW).isVisible() ? DataSourceDto.OBS_VALUE : null);

                    if (absoluteMethod.equals(interperiodPuntualMethod) || absoluteMethod.equals(interperiodPercentageMethod) || absoluteMethod.equals(annualPuntualMethod)
                            || absoluteMethod.equals(annualPercentageMethod)) {
                        return false;
                    }
                }
                return true;
            }
        };
        filledAbsoluteMethodValidator.setErrorMessage(getMessages().infoDataSourceFilledData());
        absoluteMethod.setValidators(emptyAbsoluteMethodValidator, filledAbsoluteMethodValidator);
        dataEditionForm.setFields(staticAbsoluteMethod, absoluteMethod);

        interperiodPuntualRateEditionForm = new RateDerivationForm(getConstants().dataSourceInterperiodVariation(), QuantityTypeEnum.AMOUNT, RateDerivationTypeEnum.INTERPERIOD_PUNTUAL_RATE_TYPE);
        interperiodPuntualRateEditionForm.setMethodChangedHandler(getRateDerivationMethodChangeHandler());

        interperiodPercentageRateEditionForm = new RateDerivationForm(getConstants().dataSourceInterperiodVariationRate(), QuantityTypeEnum.CHANGE_RATE,
                RateDerivationTypeEnum.INTERPERIOD_PERCENTAGE_RATE_TYPE);
        interperiodPercentageRateEditionForm.setMethodChangedHandler(getRateDerivationMethodChangeHandler());

        annualPuntualRateEditionForm = new RateDerivationForm(getConstants().dataSourceAnnualVariation(), QuantityTypeEnum.AMOUNT, RateDerivationTypeEnum.ANNUAL_PUNTUAL_RATE_TYPE);
        annualPuntualRateEditionForm.setMethodChangedHandler(getRateDerivationMethodChangeHandler());

        annualPercentageRateEditionForm = new RateDerivationForm(getConstants().dataSourceAnnualVariationRate(), QuantityTypeEnum.CHANGE_RATE, RateDerivationTypeEnum.ANNUAL_PERCENTAGE_RATE_TYPE);
        annualPercentageRateEditionForm.setMethodChangedHandler(getRateDerivationMethodChangeHandler());

        mainFormLayout.addEditionCanvas(generalEditionForm);
        mainFormLayout.addEditionCanvas(generalStaticEditionForm);
        mainFormLayout.addEditionCanvas(dataEditionForm);
        mainFormLayout.addEditionCanvas(interperiodPuntualRateEditionForm);
        mainFormLayout.addEditionCanvas(interperiodPercentageRateEditionForm);
        mainFormLayout.addEditionCanvas(annualPuntualRateEditionForm);
        mainFormLayout.addEditionCanvas(annualPercentageRateEditionForm);
    }

    private ChangedHandler getRateDerivationMethodChangeHandler() {
        return new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                dataEditionForm.validate(); // Absolute method validation
            }
        };
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
                dataDefinitionsSearchWindow.getAcceptButton().addClickHandler(new ClickHandler() {

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
                            uiHandlers.retrieveDataStructureEdition(dataDefinitionDto.getUuid());
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

    private SearchExternalItemLinkItem getQueryMetamacItem() {
        final SearchExternalItemLinkItem item = new SearchExternalItemLinkItem(DataSourceDS.QUERY_METAMAC, getConstants().dataSourceQuerySelection()) {

            @Override
            public void onSearch() {

                searchQueryMetamacWindow = new SearchSingleQueryPaginatedWindow(getConstants().resourceSelection(), IndicatorsWebConstants.FORM_LIST_MAX_RESULTS,
                        new SearchPaginatedAction<QueryWebCriteria>() {

                            @Override
                            public void retrieveResultSet(int firstResult, int maxResults, QueryWebCriteria criteria) {
                                retrieveResourcesForRelatedQuery(firstResult, maxResults, criteria);
                            }
                        });

                // Load resources (to populate the selection window)
                retrieveStatisticalOperationsForQuerySelection();

                searchQueryMetamacWindow.setSaveAction(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        ExternalItemDto selectedResource = searchQueryMetamacWindow.getSelectedResource();
                        retrieveQueryForRelatedQuery(selectedResource.getUrn());

                        searchQueryMetamacWindow.markForDestroy();
                        // Set selected resource in form
                        setSelectedRelatedQueryInEditionForm(selectedResource);
                        generalForm.validate(false);
                    }
                });
            }
        };

        return item;
    }

    public void retrieveStatisticalOperationsForQuerySelection() {
        uiHandlers.retrieveStatisticalOperationsForQuerySelection();
    }

    public void retrieveResourcesForRelatedQuery(int firstResult, int maxResults, QueryWebCriteria criteria) {
        uiHandlers.retrieveQueriesForRelatedQuery(firstResult, maxResults, criteria);
    }

    public void retrieveQueryForRelatedQuery(String queryUrn) {
        uiHandlers.retrieveDataStructureEdition(queryUrn);
    }

    /**
     * In edition mode, shows query form in edition mode
     */
    private void setEditionQueryMode() {
        queryEditionViewMode = false;

        generalEditionForm.show();
        generalStaticEditionForm.hide();

        interperiodPuntualRateEditionForm.setEditionQueryMode();
        annualPuntualRateEditionForm.setEditionQueryMode();
        interperiodPercentageRateEditionForm.setEditionQueryMode();
        annualPercentageRateEditionForm.setEditionQueryMode();

        mainFormLayout.markForRedraw();
    }

    /**
     * In edition mode, shows query form in view mode
     */
    private void setViewQueryMode() {
        queryEditionViewMode = true;

        generalEditionForm.hide();
        generalStaticEditionForm.show();

        interperiodPuntualRateEditionForm.setViewQueryMode();
        annualPuntualRateEditionForm.setViewQueryMode();
        interperiodPercentageRateEditionForm.setViewQueryMode();
        annualPercentageRateEditionForm.setViewQueryMode();

        mainFormLayout.markForRedraw();
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
        dataStructureDtoEdition = null;

        generalStaticEditionForm.setValue(dataSourceDto);

        // Edition values are not set. When edit query button is clicked, values will be cleared.
        // generalEditionForm.setValue();

        // Some rates may not exist

        if (dataSourceDto.getInterperiodPuntualRate() != null) {
            interperiodPuntualRateEditionForm.setValue(dataSourceDto.getInterperiodPuntualRate());
        } else {
            interperiodPuntualRateEditionForm.prepareNewRate();
        }
        interperiodPuntualRateEditionForm.setIsPercentage(false);

        if (dataSourceDto.getInterperiodPercentageRate() != null) {
            interperiodPercentageRateEditionForm.setValue(dataSourceDto.getInterperiodPercentageRate());
        } else {
            interperiodPercentageRateEditionForm.prepareNewRate();
        }
        interperiodPercentageRateEditionForm.setIsPercentage(true);

        if (dataSourceDto.getAnnualPuntualRate() != null) {
            annualPuntualRateEditionForm.setValue(dataSourceDto.getAnnualPuntualRate());
        } else {
            annualPuntualRateEditionForm.prepareNewRate();
        }
        annualPuntualRateEditionForm.setIsPercentage(false);

        if (dataSourceDto.getAnnualPercentageRate() != null) {
            annualPercentageRateEditionForm.setValue(dataSourceDto.getAnnualPercentageRate());
        } else {
            annualPercentageRateEditionForm.prepareNewRate();
        }
        annualPercentageRateEditionForm.setIsPercentage(true);
    }

    void clearAllQueryValues() {
        ((RequiredTextItem) generalEditionForm.getItem(DataSourceDS.QUERY_UUID)).clearValue();
        ((SearchViewTextItem) generalEditionForm.getItem(DataSourceDS.QUERY_TEXT)).clearValue();
        ((SearchExternalItemLinkItem) generalEditionForm.getItem(DataSourceDS.QUERY_METAMAC)).clearValue();
        clearQueryDependentFields();
    }

    private void clearQueryDependentFields() {
        ((ViewTextItem) generalEditionForm.getItem(DataSourceDS.SOURCE_SURVEY_CODE)).clearValue();
        generalEditionForm.clearValue(DataSourceDS.SOURCE_SURVEY_TITLE);
        generalEditionForm.clearValue(DataSourceDS.SOURCE_SURVEY_ACRONYM);
        ((TextItem) generalEditionForm.getItem(DataSourceDS.SOURCE_SURVEY_URL)).clearValue();
        ((ViewTextItem) generalEditionForm.getItem(DataSourceDS.PUBLISHERS)).clearValue();
        ((SelectItem) dataEditionForm.getItem(DataSourceDS.ABSOLUTE_METHOD)).clearValue();

        ((ViewTextItem) generalEditionForm.getItem(DataSourceDS.TIME_VARIABLE)).clearValue();
        ((TextItem) generalEditionForm.getItem(DataSourceDS.TIME_VALUE)).clearValue();
        ((ViewTextItem) generalEditionForm.getItem(DataSourceDS.TIME_VALUE_METAMAC)).clearValue();
        ((SelectItem) generalEditionForm.getItem(DataSourceDS.GEO_VARIABLE)).clearValue();
        ((SelectItem) generalEditionForm.getItem(DataSourceDS.GEO_VARIABLE)).setValueMap();
        ((GeographicalSelectItem) generalEditionForm.getItem(DataSourceDS.GEO_VALUE)).clearValue();
        ((ViewTextItem) generalEditionForm.getItem(DataSourceDS.GEO_VALUE_TEXT_METAMAC)).clearValue();
        ((HiddenItem) generalEditionForm.getItem(DataSourceDS.GEO_VALUE_UUID_METAMAC)).clearValue();
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

    // LISTS

    private LinkedHashMap<String, String> getMeasureVariableValues(String contVariable, Map<String, List<String>> categoryCodes, Map<String, List<String>> categoryLabels) {
        if (categoryCodes.containsKey(contVariable) && categoryLabels.containsKey(contVariable)) {
            return CommonUtils.getVariableCategoriesValueMap(categoryCodes.get(contVariable), categoryLabels.get(contVariable));
        }
        return null;
    }

    public void setDataDefinitions(List<DataDefinitionDto> dataDefinitionsDtos) {
        dataDefinitionsSearchWindow.setDataDefinitionList(dataDefinitionsDtos);
    }

    public void setDataDefinitionsOperationCodes(List<String> operationCodes) {
        dataDefinitionsOperationCodes = operationCodes;
    }

    public void setStatisticalOperations(List<ExternalItemDto> statisticalOperations) {
        if (searchQueryMetamacWindow != null) {
            searchQueryMetamacWindow.setStatisticalOperations(statisticalOperations);
            // searchQueryMetamacWindow.setSelectedStatisticalOperation(statisticalOperation);
            searchQueryMetamacWindow.setFixedQueryCode(null);
            searchQueryMetamacWindow.setOnlyLastVersion(true);
        }
    }

    public void setQueries(List<ExternalItemDto> queriesDtos, int firstResult, int elementsInPage, int totalResults) {
        if (searchQueryMetamacWindow != null) {
            searchQueryMetamacWindow.setResources(queriesDtos);
            searchQueryMetamacWindow.refreshSourcePaginationInfo(firstResult, elementsInPage, totalResults);
        }
    }

    // UTILS

    private boolean dataStructureHasGeoVariable() {
        if (dataStructureDtoEdition != null) {
            return dataStructureDtoEdition.getSpatialVariables() != null && !dataStructureDtoEdition.getSpatialVariables().isEmpty();
        }
        return false;
    }

    private boolean dataStructureHasGeoValue() {
        if (dataStructureDtoEdition != null) {
            return dataStructureDtoEdition.getSpatialVariables() == null || dataStructureDtoEdition.getSpatialVariables().isEmpty();
        }
        return false;
    }

    private boolean dataStructureHasTimeVariable() {
        if (dataStructureDtoEdition != null) {
            return !StringUtils.isEmpty(dataStructureDtoEdition.getTemporalVariable());
        }
        return false;
    }

    private boolean dataStructureHasTimeValue() {
        if (dataStructureDtoEdition != null) {
            return StringUtils.isEmpty(dataStructureDtoEdition.getTemporalVariable());
        }
        return false;
    }

    private boolean dataStructureHasMeasureVariable() {
        if (dataStructureDtoEdition != null) {
            return !StringUtils.isEmpty(dataStructureDtoEdition.getContVariable());
        }
        return false;
    }

    private boolean isEnvironmentSelected(DynamicForm form, QueryEnvironmentEnum environment) {
        String valueAsString = form.getValueAsString(DataSourceDS.QUERY_ENVIRONMENT);
        if (!StringUtils.isEmpty(valueAsString)) {
            QueryEnvironmentEnum queryEnvironmentEnum = QueryEnvironmentEnum.valueOf(valueAsString);
            if (environment.equals(queryEnvironmentEnum)) {
                return true;
            }
        }
        return false;
    }

    public void setEditionLanguages(List<String> languages) {
        editionLanguages = languages;
    }

}