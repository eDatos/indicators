package es.gobcan.istac.indicators.web.client.widgets;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getMessages;

import java.util.LinkedHashMap;
import java.util.List;

import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCheckboxItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;

import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.IntegerItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.validator.CustomValidator;
import com.smartgwt.client.widgets.form.validator.RequiredIfFunction;
import com.smartgwt.client.widgets.form.validator.RequiredIfValidator;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.QuantityDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.QuantityUnitDto;
import es.gobcan.istac.indicators.core.enume.domain.QuantityTypeEnum;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS;
import es.gobcan.istac.indicators.web.client.utils.CommonUtils;


public class QuantityForm extends BaseQuantityForm {
    
    private QuantityDto quantityDto;
    private IndicatorDto indicatorDto;
    
    
    public QuantityForm(String groupTitle) {
        super(groupTitle);
        
        RequiredSelectItem type = new RequiredSelectItem(IndicatorDS.QUANTITY_TYPE, getConstants().indicQuantityType());
        type.setValueMap(CommonUtils.getQuantityValueMap());
        type.addChangedHandler(new ChangedHandler() {
            @Override
            public void onChanged(ChangedEvent event) {
                QuantityForm.this.markForRedraw();
            }
        });
        
        RequiredSelectItem unitUuid = new RequiredSelectItem(IndicatorDS.QUANTITY_UNIT_UUID, getConstants().indicQuantityUnit());
        
        IntegerItem unitMultiplier = new IntegerItem(IndicatorDS.QUANTITY_UNIT_MULTIPLIER, getConstants().indicQuantityUnitMultiplier());
        unitMultiplier.setRequired(true);

        IntegerItem sigDigits = new IntegerItem(IndicatorDS.QUANTITY_SIGNIFICANT_DIGITS, getConstants().indicQuantitySignificantDigits());
        
        IntegerItem decPlaces = new IntegerItem(IndicatorDS.QUANTITY_DECIMAL_PLACES, getConstants().indicQuantityDecimalPlaces());
        decPlaces.setValidators(new RequiredIfValidator(new RequiredIfFunction() {
            @Override
            public boolean execute(FormItem formItem, Object value) {
                return QuantityTypeEnum.FRACTION.toString().equals(QuantityForm.this.getValueAsString(IndicatorDS.QUANTITY_TYPE));
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
                QuantityForm.this.markForRedraw();
            }
        });
        
        IntegerItem baseValue = new IntegerItem(IndicatorDS.QUANTITY_BASE_VALUE, getConstants().indicQuantityBaseValue());
        baseValue.setRequired(true);
        baseValue.setShowIfCondition(getBaseValueIfFunction());
        
        RequiredTextItem baseTime = new RequiredTextItem(IndicatorDS.QUANTITY_BASE_TIME, getConstants().indicQuantityBaseTime());
        baseTime.setShowIfCondition(getBaseTimeIfFunction());
        
        RequiredTextItem baseLocation = new RequiredTextItem(IndicatorDS.QUANTITY_BASE_LOCATION, getConstants().indicQuantityBaseLocation());
        baseLocation.setShowIfCondition(getBaseLocationIfFunction());
        
        SelectItem baseQuantityIndUuid = new SelectItem(IndicatorDS.QUANTITY_BASE_QUANTITY_INDICATOR_UUID, getConstants().indicQuantityBaseQuantityIndicator());
        baseQuantityIndUuid.setRequired(true);
        baseQuantityIndUuid.setShowIfCondition(getBaseQuantityIfFunction());
        baseQuantityIndUuid.setValidators(getIndicatorSelectedValidator());
        
        
        setFields(type, unitUuid, unitMultiplier, sigDigits, decPlaces, min, max, denominatorUuid, numeratorUuid, isPercentange, percentageOf, indexBaseType, baseValue, baseTime, baseLocation, baseQuantityIndUuid);
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
            setValue(IndicatorDS.QUANTITY_BASE_LOCATION, quantityDto.getBaseLocationUuid());
            setValue(IndicatorDS.QUANTITY_BASE_QUANTITY_INDICATOR_UUID, quantityDto.getBaseQuantityIndicatorUuid());
            setValue(IndicatorDS.QUANTITY_PERCENTAGE_OF, RecordUtils.getInternationalStringRecord(quantityDto.getPercentageOf()));
        }
    }

    public QuantityDto getValue() {
        quantityDto.setType(QuantityTypeEnum.valueOf(getValueAsString(IndicatorDS.QUANTITY_TYPE)));
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
        quantityDto.setBaseLocationUuid(getItem(IndicatorDS.QUANTITY_BASE_LOCATION).isVisible() ? getValueAsString(IndicatorDS.QUANTITY_BASE_LOCATION) : null);
        quantityDto.setBaseQuantityIndicatorUuid(getItem(IndicatorDS.QUANTITY_BASE_QUANTITY_INDICATOR_UUID).isVisible() ? getValueAsString(IndicatorDS.QUANTITY_BASE_QUANTITY_INDICATOR_UUID) : null);
        return quantityDto;
    }
    
    @Override
    public void setIndicators(List<IndicatorDto> indicators) {
        super.setIndicators(indicators);
        LinkedHashMap<String, String> valueMap = CommonUtils.getIndicatorsValueMap(indicators);
        ((SelectItem)getItem(IndicatorDS.QUANTITY_DENOMINATOR_INDICATOR_UUID)).setValueMap(valueMap);
        ((SelectItem)getItem(IndicatorDS.QUANTITY_NUMERATOR_INDICATOR_UUID)).setValueMap(valueMap);
        ((SelectItem)getItem(IndicatorDS.QUANTITY_BASE_QUANTITY_INDICATOR_UUID)).setValueMap(valueMap);
    }
    
    @Override
    public void setQuantityUnits(List<QuantityUnitDto> units) {
        super.setQuantityUnits(units);
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        for (QuantityUnitDto unit : units) {
            valueMap.put(unit.getUuid(), unit.getSymbol());
        }
        ((RequiredSelectItem)getItem(IndicatorDS.QUANTITY_UNIT_UUID)).setValueMap(valueMap);
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
    
}
