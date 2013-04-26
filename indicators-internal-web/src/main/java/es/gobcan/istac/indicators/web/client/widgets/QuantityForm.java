package es.gobcan.istac.indicators.web.client.widgets;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getMessages;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.utils.TimeVariableWebUtils;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCheckboxItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomIntegerItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.SearchViewTextItem;

import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.form.validator.CustomValidator;
import com.smartgwt.client.widgets.form.validator.RequiredIfFunction;
import com.smartgwt.client.widgets.form.validator.RequiredIfValidator;

import es.gobcan.istac.indicators.core.dto.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;
import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.IndicatorSummaryDto;
import es.gobcan.istac.indicators.core.dto.QuantityDto;
import es.gobcan.istac.indicators.core.dto.QuantityUnitDto;
import es.gobcan.istac.indicators.core.dto.UnitMultiplierDto;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorProcStatusEnum;
import es.gobcan.istac.indicators.core.enume.domain.QuantityTypeEnum;
import es.gobcan.istac.indicators.web.client.indicator.presenter.IndicatorUiHandler;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS;
import es.gobcan.istac.indicators.web.client.utils.CommonUtils;

public class QuantityForm extends BaseQuantityForm {

    private QuantityDto            quantityDto;
    private IndicatorDto           indicatorDto;

    private IndicatorsSearchWindow indicatorDenominatorSearchWindow;
    private IndicatorsSearchWindow indicatorNumeratorSearchWindow;
    private IndicatorsSearchWindow indicatorBaseSearchWindow;

    public QuantityForm(String groupTitle) {
        super(groupTitle);

        CustomSelectItem type = new CustomSelectItem(IndicatorDS.QUANTITY_TYPE, getConstants().indicQuantityType());
        type.setValueMap(CommonUtils.getQuantityTypeValueMap());
        type.addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                QuantityForm.this.markForRedraw();
            }
        });
        type.setValidators(getQuantityRequiredIfValidator());

        CustomSelectItem unitUuid = new CustomSelectItem(IndicatorDS.QUANTITY_UNIT_UUID, getConstants().indicQuantityUnit());
        unitUuid.setValidators(getQuantityRequiredIfValidator());

        CustomSelectItem unitMultiplier = new CustomSelectItem(IndicatorDS.QUANTITY_UNIT_MULTIPLIER, getConstants().indicQuantityUnitMultiplier());
        unitMultiplier.setValidators(getQuantityRequiredIfValidator());

        CustomIntegerItem sigDigits = new CustomIntegerItem(IndicatorDS.QUANTITY_SIGNIFICANT_DIGITS, getConstants().indicQuantitySignificantDigits());

        CustomIntegerItem decPlaces = new CustomIntegerItem(IndicatorDS.QUANTITY_DECIMAL_PLACES, getConstants().indicQuantityDecimalPlaces());
        decPlaces.setRequired(true);

        CustomIntegerItem min = new CustomIntegerItem(IndicatorDS.QUANTITY_MINIMUM, getConstants().indicQuantityMinimum());
        min.setShowIfCondition(getMinIfFunction());

        CustomIntegerItem max = new CustomIntegerItem(IndicatorDS.QUANTITY_MAXIMUM, getConstants().indicQuantityMaximum());
        max.setShowIfCondition(getMaxIfFunction());

        // Search denominator indicator
        CustomTextItem searchDenominatorUuid = new CustomTextItem(IndicatorDS.QUANTITY_DENOMINATOR_INDICATOR_UUID, getConstants().indicQuantityDenominatorIndicator());
        searchDenominatorUuid.setShowIfCondition(CommonUtils.getFalseIfFunction());
        searchDenominatorUuid.setValidators(getIndicatorSelectedValidator());
        SearchViewTextItem searchDenominatorText = getSearchDenominatorTextItem();

        // Search numerator indicator
        CustomTextItem searchNumeratorUuid = new CustomTextItem(IndicatorDS.QUANTITY_NUMERATOR_INDICATOR_UUID, getConstants().indicQuantityNumeratorIndicator());
        searchNumeratorUuid.setShowIfCondition(CommonUtils.getFalseIfFunction());
        searchNumeratorUuid.setValidators(getIndicatorSelectedValidator());
        SearchViewTextItem searchNumeratorText = getSearchNumeratorTextItem();

        CustomCheckboxItem isPercentange = new CustomCheckboxItem(IndicatorDS.QUANTITY_IS_PERCENTAGE, getConstants().indicQuantityIsPercentage());
        isPercentange.setShowIfCondition(getIsPercentageIfFunction());

        MultiLanguageTextItem percentageOf = new MultiLanguageTextItem(IndicatorDS.QUANTITY_PERCENTAGE_OF, getConstants().indicQuantityPercentageOf());
        percentageOf.setShowIfCondition(getPercentageOfIfFunction());

        CustomSelectItem indexBaseType = new CustomSelectItem(IndicatorDS.QUANTITY_INDEX_BASE_TYPE, getConstants().indicQuantityIndexMetadata());
        indexBaseType.setValueMap(getQuantityIndexBaseTypeValueMap());
        indexBaseType.setShowIfCondition(getIndexBaseTypeIfFunction());
        indexBaseType.addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                QuantityForm.this.markForRedraw();
            }
        });

        CustomIntegerItem baseValue = new CustomIntegerItem(IndicatorDS.QUANTITY_BASE_VALUE, getConstants().indicQuantityBaseValue());
        baseValue.setRequired(true);
        baseValue.setShowIfCondition(getBaseValueIfFunction());

        RequiredTextItem baseTime = new RequiredTextItem(IndicatorDS.QUANTITY_BASE_TIME, getConstants().indicQuantityBaseTime());
        baseTime.setShowIfCondition(getBaseTimeIfFunction());
        baseTime.setValidators(TimeVariableWebUtils.getTimeCustomValidator());

        final GeographicalSelectItem baseLocation = new GeographicalSelectItem(IndicatorDS.QUANTITY_BASE_LOCATION, getConstants().indicQuantityBaseLocation());
        baseLocation.setRequired(true);
        baseLocation.setShowIfCondition(getBaseLocationIfFunction());
        baseLocation.getGeoGranularitySelectItem().addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                // Clear geographical value
                baseLocation.setGeoValuesValueMap(new LinkedHashMap<String, String>());
                baseLocation.setGeoValue(new String());
                // Set values with selected granularity
                if (event.getValue() != null && !event.getValue().toString().isEmpty()) {
                    if (uiHandlers instanceof IndicatorUiHandler) {
                        ((IndicatorUiHandler) uiHandlers).retrieveGeographicalValues(event.getValue().toString());
                    }
                }
            }
        });

        // Search indicator base
        TextItem searchIndicatorBaseUuid = new TextItem(IndicatorDS.QUANTITY_BASE_QUANTITY_INDICATOR_UUID, getConstants().indicQuantityBaseQuantityIndicator());
        searchIndicatorBaseUuid.setRequired(true);
        searchIndicatorBaseUuid.setShowIfCondition(CommonUtils.getFalseIfFunction());
        searchIndicatorBaseUuid.setValidators(getIndicatorSelectedValidator());
        SearchViewTextItem searchIndicatorBaseText = getSearchIndicatorBaseTextItem();

        setFields(type, unitUuid, unitMultiplier, sigDigits, decPlaces, min, max, searchDenominatorUuid, searchDenominatorText, searchNumeratorUuid, searchNumeratorText, isPercentange, percentageOf,
                indexBaseType, baseValue, baseTime, baseLocation, searchIndicatorBaseUuid, searchIndicatorBaseText);
    }

    public void setValue(QuantityDto quantityDto) {
        this.quantityDto = quantityDto;
        clearValues();
        if (quantityDto != null) {
            setValue(IndicatorDS.QUANTITY_TYPE, quantityDto.getType() != null ? quantityDto.getType().toString() : null);
            setValue(IndicatorDS.QUANTITY_UNIT_UUID, quantityDto.getUnitUuid());
            setValue(IndicatorDS.QUANTITY_UNIT_MULTIPLIER, quantityDto.getUnitMultiplier());
            if (quantityDto.getSignificantDigits() != null) {
                setValue(IndicatorDS.QUANTITY_SIGNIFICANT_DIGITS, quantityDto.getSignificantDigits());
            }
            setValue(IndicatorDS.QUANTITY_DECIMAL_PLACES, quantityDto.getDecimalPlaces() != null ? quantityDto.getDecimalPlaces() : 2);
            if (quantityDto.getMinimum() != null) {
                setValue(IndicatorDS.QUANTITY_MINIMUM, quantityDto.getMinimum());
            }
            if (quantityDto.getMaximum() != null) {
                setValue(IndicatorDS.QUANTITY_MAXIMUM, quantityDto.getMaximum());
            }

            setValue(IndicatorDS.QUANTITY_DENOMINATOR_INDICATOR_UUID, quantityDto.getDenominatorIndicatorUuid());
            setValue(IndicatorDS.QUANTITY_DENOMINATOR_INDICATOR_TEXT, quantityDto.getDenominatorIndicatorUuid()); // Value set in setIndicatorQuantityDenominator method

            setValue(IndicatorDS.QUANTITY_NUMERATOR_INDICATOR_UUID, quantityDto.getNumeratorIndicatorUuid());
            setValue(IndicatorDS.QUANTITY_NUMERATOR_INDICATOR_TEXT, quantityDto.getNumeratorIndicatorUuid()); // Value set in setIndicatorQuantityNumerator method

            setValue(IndicatorDS.QUANTITY_IS_PERCENTAGE, quantityDto.getIsPercentage() != null ? quantityDto.getIsPercentage().booleanValue() : false);
            setValue(IndicatorDS.QUANTITY_INDEX_BASE_TYPE, getIndexBaseTypeEnum(quantityDto) != null ? getIndexBaseTypeEnum(quantityDto).toString() : "");
            if (quantityDto.getBaseValue() != null) {
                setValue(IndicatorDS.QUANTITY_BASE_VALUE, quantityDto.getBaseValue());
            }
            setValue(IndicatorDS.QUANTITY_BASE_TIME, quantityDto.getBaseTime());

            // Base location granularity set in setGeographicalGranularity method
            ((GeographicalSelectItem) getItem(IndicatorDS.QUANTITY_BASE_LOCATION)).setGeoGranularity(new String());
            ((GeographicalSelectItem) getItem(IndicatorDS.QUANTITY_BASE_LOCATION)).setGeoValue(quantityDto.getBaseLocationUuid());

            setValue(IndicatorDS.QUANTITY_BASE_QUANTITY_INDICATOR_UUID, quantityDto.getBaseQuantityIndicatorUuid());
            setValue(IndicatorDS.QUANTITY_BASE_QUANTITY_INDICATOR_TEXT, quantityDto.getBaseQuantityIndicatorUuid()); // Value set in setIndicatorQuantityIndicatorBase method

            setValue(IndicatorDS.QUANTITY_PERCENTAGE_OF, RecordUtils.getInternationalStringRecord(quantityDto.getPercentageOf()));
        }
    }

    public void setIndicatorQuantityDenominator(IndicatorDto indicator) {
        setValue(IndicatorDS.QUANTITY_DENOMINATOR_INDICATOR_TEXT, CommonUtils.getIndicatorText(indicator.getCode(), indicator.getTitle()));
    }

    public void setIndicatorQuantityNumerator(IndicatorDto indicator) {
        setValue(IndicatorDS.QUANTITY_NUMERATOR_INDICATOR_TEXT, CommonUtils.getIndicatorText(indicator.getCode(), indicator.getTitle()));
    }

    public void setIndicatorQuantityIndicatorBase(IndicatorDto indicator) {
        setValue(IndicatorDS.QUANTITY_BASE_QUANTITY_INDICATOR_TEXT, CommonUtils.getIndicatorText(indicator.getCode(), indicator.getTitle()));
    }

    public QuantityDto getValue() {
        quantityDto.setType((getValueAsString(IndicatorDS.QUANTITY_TYPE) != null && !getValueAsString(IndicatorDS.QUANTITY_TYPE).isEmpty()) ? QuantityTypeEnum
                .valueOf(getValueAsString(IndicatorDS.QUANTITY_TYPE)) : null);
        quantityDto.setUnitUuid(CommonUtils.getUuidString(getValueAsString(IndicatorDS.QUANTITY_UNIT_UUID)));
        quantityDto.setUnitMultiplier(getValue(IndicatorDS.QUANTITY_UNIT_MULTIPLIER) != null ? Integer.valueOf(getValueAsString(IndicatorDS.QUANTITY_UNIT_MULTIPLIER)) : null);
        quantityDto.setSignificantDigits(getValue(IndicatorDS.QUANTITY_SIGNIFICANT_DIGITS) != null ? (Integer) getValue(IndicatorDS.QUANTITY_SIGNIFICANT_DIGITS) : null);
        quantityDto.setDecimalPlaces(getValue(IndicatorDS.QUANTITY_DECIMAL_PLACES) != null ? (Integer) getValue(IndicatorDS.QUANTITY_DECIMAL_PLACES) : null);
        // Only set value if item is visible (these item are quantity type dependent)
        quantityDto.setMinimum(getItem(IndicatorDS.QUANTITY_MINIMUM).isVisible() ? (getValue(IndicatorDS.QUANTITY_MINIMUM) != null ? (Integer) getValue(IndicatorDS.QUANTITY_MINIMUM) : null) : null);
        quantityDto.setMaximum(getItem(IndicatorDS.QUANTITY_MAXIMUM).isVisible() ? (getValue(IndicatorDS.QUANTITY_MAXIMUM) != null ? (Integer) getValue(IndicatorDS.QUANTITY_MAXIMUM) : null) : null);
        quantityDto.setDenominatorIndicatorUuid(getItem(IndicatorDS.QUANTITY_DENOMINATOR_INDICATOR_TEXT).isVisible() ? CommonUtils
                .getUuidString(getValueAsString(IndicatorDS.QUANTITY_DENOMINATOR_INDICATOR_UUID)) : null);
        quantityDto.setNumeratorIndicatorUuid(getItem(IndicatorDS.QUANTITY_NUMERATOR_INDICATOR_TEXT).isVisible() ? CommonUtils
                .getUuidString(getValueAsString(IndicatorDS.QUANTITY_NUMERATOR_INDICATOR_UUID)) : null);
        quantityDto.setIsPercentage(getItem(IndicatorDS.QUANTITY_IS_PERCENTAGE).isVisible() ? (getValue(IndicatorDS.QUANTITY_IS_PERCENTAGE) != null ? Boolean
                .valueOf((Boolean) getValue(IndicatorDS.QUANTITY_IS_PERCENTAGE)) : false) : null);
        quantityDto.setPercentageOf(getItem(IndicatorDS.QUANTITY_PERCENTAGE_OF).isVisible() ? ((MultiLanguageTextItem) getItem(IndicatorDS.QUANTITY_PERCENTAGE_OF)).getValue() : null);
        quantityDto.setBaseValue(getItem(IndicatorDS.QUANTITY_BASE_VALUE).isVisible()
                ? (getValue(IndicatorDS.QUANTITY_BASE_VALUE) != null ? (Integer) getValue(IndicatorDS.QUANTITY_BASE_VALUE) : null)
                : null);
        quantityDto.setBaseTime(getItem(IndicatorDS.QUANTITY_BASE_TIME).isVisible() ? getValueAsString(IndicatorDS.QUANTITY_BASE_TIME) : null);
        quantityDto.setBaseLocationUuid(getItem(IndicatorDS.QUANTITY_BASE_LOCATION).isVisible() ? CommonUtils.getUuidString(((GeographicalSelectItem) getItem(IndicatorDS.QUANTITY_BASE_LOCATION))
                .getSelectedGeoValue()) : null);
        quantityDto.setBaseQuantityIndicatorUuid(getItem(IndicatorDS.QUANTITY_BASE_QUANTITY_INDICATOR_TEXT).isVisible() ? CommonUtils
                .getUuidString(getValueAsString(IndicatorDS.QUANTITY_BASE_QUANTITY_INDICATOR_UUID)) : null);
        return quantityDto;
    }

    public void setIndicatorListQuantityDenominator(List<IndicatorSummaryDto> indicators) {
        if (indicatorDenominatorSearchWindow != null) {
            indicatorDenominatorSearchWindow.setIndicatorList(indicators);
        }
    }

    public void setIndicatorListQuantityNumerator(List<IndicatorSummaryDto> indicators) {
        if (indicatorNumeratorSearchWindow != null) {
            indicatorNumeratorSearchWindow.setIndicatorList(indicators);
        }
    }

    public void setIndicatorListQuantityIndicatorBase(List<IndicatorSummaryDto> indicators) {
        if (indicatorBaseSearchWindow != null) {
            indicatorBaseSearchWindow.setIndicatorList(indicators);
        }
    }

    @Override
    public void setQuantityUnits(List<QuantityUnitDto> units) {
        super.setQuantityUnits(units);
        LinkedHashMap<String, String> valueMap = CommonUtils.getQuantityUnitsValueMap(units);
        ((SelectItem) getItem(IndicatorDS.QUANTITY_UNIT_UUID)).setValueMap(valueMap);
    }
    


    public void setIndicator(IndicatorDto indicatorDto) {
        this.indicatorDto = indicatorDto;
    }

    private CustomValidator getIndicatorSelectedValidator() {
        CustomValidator validator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                return (value != null && value.equals(indicatorDto.getUuid())) ? false : true;
            }
        };
        validator.setErrorMessage(getMessages().validatorErrorIndicatorSelected());
        return validator;
    }

    public void setUnitMultipliers(List<UnitMultiplierDto> unitMultiplierDtos) {
        ((CustomSelectItem) getItem(IndicatorDS.QUANTITY_UNIT_MULTIPLIER)).setValueMap(CommonUtils.getUnitMultiplierValueMap(unitMultiplierDtos));
    }

    public void setGeographicalGranularities(List<GeographicalGranularityDto> granularityDtos) {
        ((GeographicalSelectItem) getItem(IndicatorDS.QUANTITY_BASE_LOCATION)).setGeoGranularitiesValueMap(CommonUtils.getGeographicalGranularituesValueMap(granularityDtos));
    }

    public void setGeographicalValues(List<GeographicalValueDto> geographicalValueDtos) {
        ((GeographicalSelectItem) getItem(IndicatorDS.QUANTITY_BASE_LOCATION)).setGeoValuesValueMap(CommonUtils.getGeographicalValuesValueMap(geographicalValueDtos));
    }

    public void setGeographicalValue(GeographicalValueDto geographicalValueDto) {
        if (geographicalValueDto != null) {
            ((GeographicalSelectItem) getItem(IndicatorDS.QUANTITY_BASE_LOCATION)).setGeoGranularity(geographicalValueDto.getGranularityUuid());
            // Make sure value map is set properly
            if (uiHandlers instanceof IndicatorUiHandler) {
                ((IndicatorUiHandler) uiHandlers).retrieveGeographicalValues(geographicalValueDto.getGranularityUuid());
            }
        }
    }

    public RequiredIfValidator getQuantityRequiredIfValidator() {
        return new RequiredIfValidator(new RequiredIfFunction() {

            @Override
            public boolean execute(FormItem formItem, Object value) {
                return !IndicatorProcStatusEnum.DRAFT.equals(indicatorDto.getProcStatus());
            }
        });
    }

    // ITEMS

    private SearchViewTextItem getSearchDenominatorTextItem() {
        SearchViewTextItem searchDenominatorText = new SearchViewTextItem(IndicatorDS.QUANTITY_DENOMINATOR_INDICATOR_TEXT, getConstants().indicQuantityDenominatorIndicator());
        searchDenominatorText.setShowIfCondition(getDenominatorIfFunction());
        searchDenominatorText.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                indicatorDenominatorSearchWindow = new IndicatorsSearchWindow(getConstants().indicatorSelection());
                indicatorDenominatorSearchWindow.getSearchButton().addClickHandler(new ClickHandler() {

                    @Override
                    public void onClick(ClickEvent event) {
                        if (uiHandlers instanceof IndicatorUiHandler) {
                            ((IndicatorUiHandler) uiHandlers).searchIndicatorsQuantityDenominator(indicatorDenominatorSearchWindow.getSearchCriteria());
                        }
                    }
                });

                indicatorDenominatorSearchWindow.getAceptButton().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                        IndicatorSummaryDto indicatorSummaryDto = indicatorDenominatorSearchWindow.getSelectedIndicator();
                        indicatorDenominatorSearchWindow.destroy();
                        QuantityForm.this.setValue(IndicatorDS.QUANTITY_DENOMINATOR_INDICATOR_UUID, indicatorSummaryDto != null ? indicatorSummaryDto.getUuid() : new String());
                        QuantityForm.this.setValue(IndicatorDS.QUANTITY_DENOMINATOR_INDICATOR_TEXT,
                                indicatorSummaryDto != null ? CommonUtils.getIndicatorText(indicatorSummaryDto.getCode(), CommonUtils.getIndicatorTitle(indicatorSummaryDto)) : new String());
                        QuantityForm.this.getItem(IndicatorDS.QUANTITY_DENOMINATOR_INDICATOR_TEXT).validate();
                    }
                });
            }
        });
        CustomValidator validator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                return QuantityForm.this.getItem(IndicatorDS.QUANTITY_DENOMINATOR_INDICATOR_UUID).validate();
            }
        };
        validator.setErrorMessage(getMessages().validatorErrorIndicatorSelected());
        searchDenominatorText.setValidators(validator);

        return searchDenominatorText;
    }

    private SearchViewTextItem getSearchNumeratorTextItem() {
        SearchViewTextItem searchNumeratorText = new SearchViewTextItem(IndicatorDS.QUANTITY_NUMERATOR_INDICATOR_TEXT, getConstants().indicQuantityNumeratorIndicator());
        searchNumeratorText.setShowIfCondition(getNumeratorIfFunction());
        searchNumeratorText.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                indicatorNumeratorSearchWindow = new IndicatorsSearchWindow(getConstants().indicatorSelection());
                indicatorNumeratorSearchWindow.getSearchButton().addClickHandler(new ClickHandler() {

                    @Override
                    public void onClick(ClickEvent event) {
                        if (uiHandlers instanceof IndicatorUiHandler) {
                            ((IndicatorUiHandler) uiHandlers).searchIndicatorsQuantityNumerator(indicatorNumeratorSearchWindow.getSearchCriteria());
                        }
                    }
                });

                indicatorNumeratorSearchWindow.getAceptButton().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                        IndicatorSummaryDto indicatorSummaryDto = indicatorNumeratorSearchWindow.getSelectedIndicator();
                        indicatorNumeratorSearchWindow.destroy();
                        QuantityForm.this.setValue(IndicatorDS.QUANTITY_NUMERATOR_INDICATOR_UUID, indicatorSummaryDto != null ? indicatorSummaryDto.getUuid() : new String());
                        QuantityForm.this.setValue(IndicatorDS.QUANTITY_NUMERATOR_INDICATOR_TEXT,
                                indicatorSummaryDto != null ? CommonUtils.getIndicatorText(indicatorSummaryDto.getCode(), CommonUtils.getIndicatorTitle(indicatorSummaryDto)) : new String());
                        QuantityForm.this.getItem(IndicatorDS.QUANTITY_NUMERATOR_INDICATOR_TEXT).validate();
                    }
                });
            }
        });
        CustomValidator validator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                return QuantityForm.this.getItem(IndicatorDS.QUANTITY_NUMERATOR_INDICATOR_UUID).validate();
            }
        };
        validator.setErrorMessage(getMessages().validatorErrorIndicatorSelected());
        searchNumeratorText.setValidators(validator);

        return searchNumeratorText;
    }

    private SearchViewTextItem getSearchIndicatorBaseTextItem() {
        SearchViewTextItem searchIndicatorBaseText = new SearchViewTextItem(IndicatorDS.QUANTITY_BASE_QUANTITY_INDICATOR_TEXT, getConstants().indicQuantityBaseQuantityIndicator());
        searchIndicatorBaseText.setRequired(true);
        searchIndicatorBaseText.setShowIfCondition(getBaseQuantityIfFunction());
        searchIndicatorBaseText.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                indicatorBaseSearchWindow = new IndicatorsSearchWindow(getConstants().indicatorSelection());
                indicatorBaseSearchWindow.getSearchButton().addClickHandler(new ClickHandler() {

                    @Override
                    public void onClick(ClickEvent event) {
                        if (uiHandlers instanceof IndicatorUiHandler) {
                            ((IndicatorUiHandler) uiHandlers).searchIndicatorsQuantityIndicatorBase(indicatorBaseSearchWindow.getSearchCriteria());
                        }
                    }
                });

                indicatorBaseSearchWindow.getAceptButton().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                        IndicatorSummaryDto indicatorDto = indicatorBaseSearchWindow.getSelectedIndicator();
                        indicatorBaseSearchWindow.destroy();
                        QuantityForm.this.setValue(IndicatorDS.QUANTITY_BASE_QUANTITY_INDICATOR_UUID, indicatorDto != null ? indicatorDto.getUuid() : new String());
                        QuantityForm.this.setValue(IndicatorDS.QUANTITY_BASE_QUANTITY_INDICATOR_TEXT,
                                indicatorDto != null ? CommonUtils.getIndicatorText(indicatorDto.getCode(), CommonUtils.getIndicatorTitle(indicatorDto)) : new String());
                        QuantityForm.this.getItem(IndicatorDS.QUANTITY_BASE_QUANTITY_INDICATOR_TEXT).validate();
                    }
                });
            }
        });
        CustomValidator validator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                return QuantityForm.this.getItem(IndicatorDS.QUANTITY_BASE_QUANTITY_INDICATOR_UUID).validate();
            }
        };
        validator.setErrorMessage(getMessages().validatorErrorIndicatorSelected());
        searchIndicatorBaseText.setValidators(validator);

        return searchIndicatorBaseText;
    }

}
