package es.gobcan.istac.indicators.web.client.widgets;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getMessages;

import java.util.LinkedHashMap;
import java.util.List;

import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCheckboxItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;

import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.IntegerItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.validator.CustomValidator;
import com.smartgwt.client.widgets.form.validator.RequiredIfFunction;
import com.smartgwt.client.widgets.form.validator.RequiredIfValidator;

import es.gobcan.istac.indicators.core.dto.serviceapi.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.GeographicalValueDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.QuantityDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.QuantityUnitDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.RateDerivationDto;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorProcStatusEnum;
import es.gobcan.istac.indicators.core.enume.domain.QuantityTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.RateDerivationMethodTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.RateDerivationRoundingEnum;
import es.gobcan.istac.indicators.web.client.enums.DataSourceQuantityType;
import es.gobcan.istac.indicators.web.client.indicator.presenter.IndicatorUiHandler;
import es.gobcan.istac.indicators.web.client.model.ds.DataSourceDS;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS;
import es.gobcan.istac.indicators.web.client.utils.CommonUtils;


public class RateDerivationForm extends BaseRateDerivationForm {
    
    private RateDerivationDto rateDerivationDto;
    private IndicatorDto indicatorDto;
    
    
    public RateDerivationForm(String groupTitle, final DataSourceQuantityType dataSourceQuantityType) {
        super(groupTitle, dataSourceQuantityType);
        
        TextItem method = new TextItem(DataSourceDS.RATE_DERIVATION_METHOD, getConstants().datasourceMethod());
        
        SelectItem methodType = new SelectItem(DataSourceDS.RATE_DERIVATION_METHOD_TYPE, getConstants().datasourceMethodType());
        methodType.setValueMap(CommonUtils.getRateDerivationMethodTypeValueMap());
        
        SelectItem rounding = new SelectItem(DataSourceDS.RATE_DERIVATION_ROUNDING, getConstants().datasourceRounding());
        rounding.setValueMap(CommonUtils.getRateDerivationRoundingValueMap());
        
        // QUANTITY

        SelectItem type = new SelectItem(IndicatorDS.QUANTITY_TYPE, getConstants().indicQuantityType());
        type.setValueMap(CommonUtils.getQuantityValueMap());
        type.addChangedHandler(new ChangedHandler() {
            @Override
            public void onChanged(ChangedEvent event) {
                RateDerivationForm.this.markForRedraw();
            }
        });
        type.setValidators(getQuantityRequiredIfValidator());
        
        SelectItem unitUuid = new SelectItem(IndicatorDS.QUANTITY_UNIT_UUID, getConstants().indicQuantityUnit());
        unitUuid.setValidators(getQuantityRequiredIfValidator());
        
        IntegerItem unitMultiplier = new IntegerItem(IndicatorDS.QUANTITY_UNIT_MULTIPLIER, getConstants().indicQuantityUnitMultiplier());
        unitMultiplier.setValidators(getQuantityRequiredIfValidator());
        
        IntegerItem sigDigits = new IntegerItem(IndicatorDS.QUANTITY_SIGNIFICANT_DIGITS, getConstants().indicQuantitySignificantDigits());
        
        IntegerItem decPlaces = new IntegerItem(IndicatorDS.QUANTITY_DECIMAL_PLACES, getConstants().indicQuantityDecimalPlaces());
        decPlaces.setValidators(new RequiredIfValidator(new RequiredIfFunction() {
            @Override
            public boolean execute(FormItem formItem, Object value) {
                return QuantityTypeEnum.FRACTION.toString().equals(RateDerivationForm.this.getValueAsString(IndicatorDS.QUANTITY_TYPE));
            }
        }));
        
        IntegerItem min = new IntegerItem(IndicatorDS.QUANTITY_MINIMUM, getConstants().indicQuantityMinimum());
        min.setShowIfCondition(getMinIfFunction());
        
        IntegerItem max = new IntegerItem(IndicatorDS.QUANTITY_MAXIMUM, getConstants().indicQuantityMaximum());
        max.setShowIfCondition(getMaxIfFunction());
        
        SelectItem denominatorUuid = new SelectItem(IndicatorDS.QUANTITY_DENOMINATOR_INDICATOR_UUID, getConstants().indicQuantityDenominatorIndicator());
        denominatorUuid.setShowIfCondition(getDenominatorIfFunction());
        denominatorUuid.setValidators(getIndicatorSelectedValidator());
        
        SelectItem numeratorUuid = new SelectItem(IndicatorDS.QUANTITY_NUMERATOR_INDICATOR_UUID, getConstants().indicQuantityNumeratorIndicator());
        numeratorUuid.setShowIfCondition(getNumeratorIfFunction());
        numeratorUuid.setValidators(getIndicatorSelectedValidator());
        
        CustomCheckboxItem isPercentange = new CustomCheckboxItem(IndicatorDS.QUANTITY_IS_PERCENTAGE, getConstants().indicQuantityIsPercentage());
        isPercentange.setShowIfCondition(getIsPercentageIfFunction());
        
        MultiLanguageTextItem percentageOf = new MultiLanguageTextItem(IndicatorDS.QUANTITY_PERCENTAGE_OF, getConstants().indicQuantityPercentageOf());
        percentageOf.setShowIfCondition(getPercentageOfIfFunction());
        
        SelectItem indexBaseType = new SelectItem(IndicatorDS.QUANTITY_INDEX_BASE_TYPE, getConstants().indicQuantityIndexMetadata());
        indexBaseType.setValueMap(getQuantityIndexBaseTypeValueMap());
        indexBaseType.setShowIfCondition(getIndexBaseTypeIfFunction());
        indexBaseType.addChangedHandler(new ChangedHandler() {
            @Override
            public void onChanged(ChangedEvent event) {
                RateDerivationForm.this.markForRedraw();
            }
        });
        
        IntegerItem baseValue = new IntegerItem(IndicatorDS.QUANTITY_BASE_VALUE, getConstants().indicQuantityBaseValue());
        baseValue.setRequired(true);
        baseValue.setShowIfCondition(getBaseValueIfFunction());
        
        RequiredTextItem baseTime = new RequiredTextItem(IndicatorDS.QUANTITY_BASE_TIME, getConstants().indicQuantityBaseTime());
        baseTime.setShowIfCondition(getBaseTimeIfFunction());
// TODO       baseTime.setValidators(TimeVariableWebUtils.getTimeCustomValidator());
        
        final GeographicalSelectItem baseLocation = new GeographicalSelectItem(IndicatorDS.QUANTITY_BASE_LOCATION, getConstants().indicQuantityBaseLocation());
        baseLocation.setRequired(true);
        baseLocation.setShowIfCondition(getBaseLocationIfFunction());
        baseLocation.getGeoGranularity().addChangedHandler(new ChangedHandler() {
            @Override
            public void onChanged(ChangedEvent event) {
                // Clear geographical value
                baseLocation.setGeoValuesValueMap(new LinkedHashMap<String, String>());
                baseLocation.setGeoValue(new String());
                // Set values with selected granularity
                if (event.getValue() != null && !event.getValue().toString().isEmpty()) {
                    if (uiHandlers instanceof IndicatorUiHandler) {
                        if (DataSourceQuantityType.INTERPERIOD_RATE.equals(dataSourceQuantityType)) {
                            ((IndicatorUiHandler) uiHandlers).retrieveGeographicalValuesDSInterperiodRate(event.getValue().toString());
                        } else if (DataSourceQuantityType.ANNUAL_RATE.equals(dataSourceQuantityType)) {
                            ((IndicatorUiHandler) uiHandlers).retrieveGeographicalValuesDSAnnualRate(event.getValue().toString());
                        }
                    }
                }
            }
        });
        
        SelectItem baseQuantityIndUuid = new SelectItem(IndicatorDS.QUANTITY_BASE_QUANTITY_INDICATOR_UUID, getConstants().indicQuantityBaseQuantityIndicator());
        baseQuantityIndUuid.setRequired(true);
        baseQuantityIndUuid.setShowIfCondition(getBaseQuantityIfFunction());
        baseQuantityIndUuid.setValidators(getIndicatorSelectedValidator());

        
        setFields(method, methodType, rounding, type, unitUuid, unitMultiplier, sigDigits, decPlaces, min, max, denominatorUuid, numeratorUuid, isPercentange, percentageOf, indexBaseType, baseValue, baseTime, baseLocation, baseQuantityIndUuid);
    }
    
    public void setValue(RateDerivationDto rateDerivationDto) {
        this.rateDerivationDto = rateDerivationDto;
        
        clearValues();
        
        setValue(DataSourceDS.RATE_DERIVATION_METHOD, rateDerivationDto.getMethod());
        setValue(DataSourceDS.RATE_DERIVATION_METHOD_TYPE, rateDerivationDto.getMethodType() != null ? rateDerivationDto.getMethodType().toString() : new String());
        setValue(DataSourceDS.RATE_DERIVATION_ROUNDING, rateDerivationDto.getRounding() != null ? rateDerivationDto.getRounding().toString() : new String());
        
        QuantityDto quantityDto = rateDerivationDto.getQuantity();
        
        if (quantityDto != null) {
            setValue(IndicatorDS.QUANTITY_TYPE, quantityDto.getType() != null ? quantityDto.getType().toString() : null);
            setValue(IndicatorDS.QUANTITY_UNIT_UUID, quantityDto.getUnitUuid());
            setValue(IndicatorDS.QUANTITY_UNIT_MULTIPLIER, quantityDto.getUnitMultiplier());
            if (quantityDto.getSignificantDigits() != null) {
                setValue(IndicatorDS.QUANTITY_SIGNIFICANT_DIGITS, quantityDto.getSignificantDigits());
            }
            if (quantityDto.getDecimalPlaces() != null) {
                setValue(IndicatorDS.QUANTITY_DECIMAL_PLACES, quantityDto.getDecimalPlaces());
            }
            if (quantityDto.getMinimum() != null) {
                setValue(IndicatorDS.QUANTITY_MINIMUM, quantityDto.getMinimum());
            }
            if (quantityDto.getMaximum() != null) {
                setValue(IndicatorDS.QUANTITY_MAXIMUM, quantityDto.getMaximum());
            }
            setValue(IndicatorDS.QUANTITY_DENOMINATOR_INDICATOR_UUID, quantityDto.getDenominatorIndicatorUuid());
            setValue(IndicatorDS.QUANTITY_NUMERATOR_INDICATOR_UUID, quantityDto.getNumeratorIndicatorUuid());
            setValue(IndicatorDS.QUANTITY_IS_PERCENTAGE, quantityDto.getIsPercentage() != null ? quantityDto.getIsPercentage().booleanValue() : false);
            setValue(IndicatorDS.QUANTITY_INDEX_BASE_TYPE, getIndexBaseTypeEnum(quantityDto) != null  ? getIndexBaseTypeEnum(quantityDto).toString() : "");
            if (quantityDto.getBaseValue() != null) {
                setValue(IndicatorDS.QUANTITY_BASE_VALUE, quantityDto.getBaseValue());
            }
            setValue(IndicatorDS.QUANTITY_BASE_TIME, quantityDto.getBaseTime());

            // Base location granularity set in setGeographicalGranularity method
            ((GeographicalSelectItem)getItem(IndicatorDS.QUANTITY_BASE_LOCATION)).setGeoGranularity(new String());
            ((GeographicalSelectItem)getItem(IndicatorDS.QUANTITY_BASE_LOCATION)).setGeoValue(quantityDto.getBaseLocationUuid());
            
            setValue(IndicatorDS.QUANTITY_BASE_QUANTITY_INDICATOR_UUID, quantityDto.getBaseQuantityIndicatorUuid());
            setValue(IndicatorDS.QUANTITY_PERCENTAGE_OF, RecordUtils.getInternationalStringRecord(quantityDto.getPercentageOf()));
        }
        
    }
    
    public RateDerivationDto getValue() {
        rateDerivationDto.setMethod(getValueAsString(DataSourceDS.RATE_DERIVATION_METHOD));
        rateDerivationDto.setMethodType(getValueAsString(DataSourceDS.RATE_DERIVATION_METHOD_TYPE) != null && !getValueAsString(DataSourceDS.RATE_DERIVATION_METHOD_TYPE).isEmpty() ? RateDerivationMethodTypeEnum.valueOf(getValueAsString(DataSourceDS.RATE_DERIVATION_METHOD_TYPE)) : null);
        rateDerivationDto.setRounding(getValueAsString(DataSourceDS.RATE_DERIVATION_ROUNDING) != null && !getValueAsString(DataSourceDS.RATE_DERIVATION_ROUNDING).isEmpty() ? RateDerivationRoundingEnum.valueOf(getValueAsString(DataSourceDS.RATE_DERIVATION_ROUNDING)) : null);
        
        QuantityDto quantityDto = rateDerivationDto.getQuantity();
        if (quantityDto == null) {
            quantityDto = new QuantityDto();
        }
        
        quantityDto.setType((getValueAsString(IndicatorDS.QUANTITY_TYPE) != null && !getValueAsString(IndicatorDS.QUANTITY_TYPE).isEmpty()) ? QuantityTypeEnum.valueOf(getValueAsString(IndicatorDS.QUANTITY_TYPE)) : null);
        quantityDto.setUnitUuid(getValueAsString(IndicatorDS.QUANTITY_UNIT_UUID));
        quantityDto.setUnitMultiplier(getValue(IndicatorDS.QUANTITY_UNIT_MULTIPLIER) != null ? (Integer)getValue(IndicatorDS.QUANTITY_UNIT_MULTIPLIER) : null);
        quantityDto.setSignificantDigits(getValue(IndicatorDS.QUANTITY_SIGNIFICANT_DIGITS) != null ? (Integer)getValue(IndicatorDS.QUANTITY_SIGNIFICANT_DIGITS) : null);
        quantityDto.setDecimalPlaces(getValue(IndicatorDS.QUANTITY_DECIMAL_PLACES) != null ? (Integer)getValue(IndicatorDS.QUANTITY_DECIMAL_PLACES) : null);
        // Only set value if item is visible (these item are quantity type dependent) 
        quantityDto.setMinimum(getItem(IndicatorDS.QUANTITY_MINIMUM).isVisible() ? (getValue(IndicatorDS.QUANTITY_MINIMUM) != null ? (Integer)getValue(IndicatorDS.QUANTITY_MINIMUM) : null) : null);
        quantityDto.setMaximum(getItem(IndicatorDS.QUANTITY_MAXIMUM).isVisible() ? (getValue(IndicatorDS.QUANTITY_MAXIMUM) != null ? (Integer)getValue(IndicatorDS.QUANTITY_MAXIMUM) : null) : null);
        quantityDto.setDenominatorIndicatorUuid(getItem(IndicatorDS.QUANTITY_DENOMINATOR_INDICATOR_UUID).isVisible() ? getValueAsString(IndicatorDS.QUANTITY_DENOMINATOR_INDICATOR_UUID) : null);
        quantityDto.setNumeratorIndicatorUuid(getItem(IndicatorDS.QUANTITY_NUMERATOR_INDICATOR_UUID).isVisible() ? getValueAsString(IndicatorDS.QUANTITY_NUMERATOR_INDICATOR_UUID) : null);
        quantityDto.setIsPercentage(getItem(IndicatorDS.QUANTITY_IS_PERCENTAGE).isVisible() ? (getValue(IndicatorDS.QUANTITY_IS_PERCENTAGE) != null ? Boolean.valueOf((Boolean)getValue(IndicatorDS.QUANTITY_IS_PERCENTAGE)): false) : null);
        quantityDto.setPercentageOf(getItem(IndicatorDS.QUANTITY_PERCENTAGE_OF).isVisible() ? ((MultiLanguageTextItem)getItem(IndicatorDS.QUANTITY_PERCENTAGE_OF)).getValue() : null);
        quantityDto.setBaseValue(getItem(IndicatorDS.QUANTITY_BASE_VALUE).isVisible() ? (getValue(IndicatorDS.QUANTITY_BASE_VALUE) != null ? (Integer)getValue(IndicatorDS.QUANTITY_BASE_VALUE) : null) : null);
        quantityDto.setBaseTime(getItem(IndicatorDS.QUANTITY_BASE_TIME).isVisible() ? getValueAsString(IndicatorDS.QUANTITY_BASE_TIME) : null);
        quantityDto.setBaseLocationUuid(getItem(IndicatorDS.QUANTITY_BASE_LOCATION).isVisible() ? ((GeographicalSelectItem)getItem(IndicatorDS.QUANTITY_BASE_LOCATION)).getSelectedGeoValue() : null);
        quantityDto.setBaseQuantityIndicatorUuid(getItem(IndicatorDS.QUANTITY_BASE_QUANTITY_INDICATOR_UUID).isVisible() ? getValueAsString(IndicatorDS.QUANTITY_BASE_QUANTITY_INDICATOR_UUID) : null);

        rateDerivationDto.setQuantity(quantityDto);
        
        return rateDerivationDto;
    }

    public void setIndicator(IndicatorDto indicatorDto) {
        this.indicatorDto = indicatorDto;
    }
    
    private RequiredIfValidator getQuantityRequiredIfValidator() {
        return new RequiredIfValidator(new RequiredIfFunction() {
            @Override
            public boolean execute(FormItem formItem, Object value) {
                return !IndicatorProcStatusEnum.DRAFT.equals(indicatorDto.getProcStatus());
            }
        });
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
    
    public void setGeographicalGranularities(List<GeographicalGranularityDto> granularityDtos) {
        ((GeographicalSelectItem)getItem(IndicatorDS.QUANTITY_BASE_LOCATION)).setGeoGranularitiesValueMap(CommonUtils.getGeographicalGranularituesValueMap(granularityDtos));
    }
    
    public void setGeographicalValues(List<GeographicalValueDto> geographicalValueDtos) {
        ((GeographicalSelectItem)getItem(IndicatorDS.QUANTITY_BASE_LOCATION)).setGeoValuesValueMap(CommonUtils.getGeographicalValuesValueMap(geographicalValueDtos));
    }
    
    public void setGeographicalValue(GeographicalValueDto geographicalValueDto) {
        if (geographicalValueDto != null) {
            ((GeographicalSelectItem)getItem(IndicatorDS.QUANTITY_BASE_LOCATION)).setGeoGranularity(geographicalValueDto.getGranularityUuid());
            // Make sure value map is set properly
            if (uiHandlers instanceof IndicatorUiHandler) {
                if (DataSourceQuantityType.INTERPERIOD_RATE.equals(dataSourceQuantityType)) {
                    ((IndicatorUiHandler) uiHandlers).retrieveGeographicalValuesDSInterperiodRate(geographicalValueDto.getGranularityUuid());
                } else if (DataSourceQuantityType.ANNUAL_RATE.equals(dataSourceQuantityType)) {
                    ((IndicatorUiHandler) uiHandlers).retrieveGeographicalValuesDSAnnualRate(geographicalValueDto.getGranularityUuid());
                }
            }
        }
    }
    
    @Override
    public void setQuantityUnits(List<QuantityUnitDto> units) {
        super.setQuantityUnits(units);
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        for (QuantityUnitDto unit : units) {
            valueMap.put(unit.getUuid(), unit.getSymbol());
        }
        ((SelectItem)getItem(IndicatorDS.QUANTITY_UNIT_UUID)).setValueMap(valueMap);
    }
    
    @Override
    public void setIndicators(List<IndicatorDto> indicators) {
        super.setIndicators(indicators);
        LinkedHashMap<String, String> valueMap = CommonUtils.getIndicatorsValueMap(indicators);
//        ((SelectItem)getItem(IndicatorDS.QUANTITY_DENOMINATOR_INDICATOR_UUID)).setValueMap(valueMap);
//        ((SelectItem)getItem(IndicatorDS.QUANTITY_NUMERATOR_INDICATOR_UUID)).setValueMap(valueMap);
//        ((SelectItem)getItem(IndicatorDS.QUANTITY_BASE_QUANTITY_INDICATOR_UUID)).setValueMap(valueMap);
    }
    
}
