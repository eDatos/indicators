package es.gobcan.istac.indicators.web.client.widgets;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getCoreMessages;

import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.QuantityDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.QuantityUnitDto;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS;


public class ViewQuantityForm extends BaseQuantityForm {
    
    public ViewQuantityForm(String groupTitle) {
        super(groupTitle);
        
        // Its important to set the value of the QuantityType (no translated name!) in a hidden field. It is required by the validators (see BaseQuantityForm).
        ViewTextItem type = new ViewTextItem(IndicatorDS.QUANTITY_TYPE, getConstants().indicQuantityType());
        type.setVisible(false);
        ViewTextItem typeText = new ViewTextItem(IndicatorDS.QUANTITY_TYPE + "-text", getConstants().indicQuantityType());
        
        ViewTextItem unitUuid = new ViewTextItem(IndicatorDS.QUANTITY_UNIT_UUID, getConstants().indicQuantityUnit());
        
        ViewTextItem unitMultiplier = new ViewTextItem(IndicatorDS.QUANTITY_UNIT_MULTIPLIER, getConstants().indicQuantityUnitMultiplier());
        
        ViewTextItem sigDigits = new ViewTextItem(IndicatorDS.QUANTITY_SIGNIFICANT_DIGITS, getConstants().indicQuantitySignificantDigits());
        
        ViewTextItem decPlaces = new ViewTextItem(IndicatorDS.QUANTITY_DECIMAL_PLACES, getConstants().indicQuantityDecimalPlaces());
        
        ViewTextItem min = new ViewTextItem(IndicatorDS.QUANTITY_MINIMUM, getConstants().indicQuantityMinimum());
        min.setShowIfCondition(getMinIfFunction());
        
        ViewTextItem max = new ViewTextItem(IndicatorDS.QUANTITY_MAXIMUM, getConstants().indicQuantityMaximum());
        max.setShowIfCondition(getMaxIfFunction());
        
        ViewTextItem denominatorUuid = new ViewTextItem(IndicatorDS.QUANTITY_DENOMINATOR_INDICATOR_UUID, getConstants().indicQuantityDenominatorIndicator());
        denominatorUuid.setShowIfCondition(getDenominatorIfFunction());
        
        ViewTextItem numeratorUuid = new ViewTextItem(IndicatorDS.QUANTITY_NUMERATOR_INDICATOR_UUID, getConstants().indicQuantityNumeratorIndicator());
        numeratorUuid.setShowIfCondition(getNumeratorIfFunction());
        
        ViewTextItem isPercentange = new ViewTextItem(IndicatorDS.QUANTITY_IS_PERCENTAGE, getConstants().indicQuantityIsPercentage());
        isPercentange.setShowIfCondition(getIsPercentageIfFunction());
        
        ViewTextItem percentageOf = new ViewTextItem(IndicatorDS.QUANTITY_PERCENTAGE_OF, getConstants().indicQuantityPercentageOf());
        percentageOf.setShowIfCondition(getPercentageOfIfFunction());
        
        ViewTextItem indexBaseType = new ViewTextItem(IndicatorDS.QUANTITY_INDEX_BASE_TYPE, getConstants().indicQuantityIndexMetadata());
        indexBaseType.setVisible(false);
        ViewTextItem indexBaseTypeText = new ViewTextItem(IndicatorDS.QUANTITY_INDEX_BASE_TYPE + "-text", getConstants().indicQuantityIndexMetadata());
        indexBaseTypeText.setShowIfCondition(getIndexBaseTypeIfFunction());
        
        ViewTextItem baseValue = new ViewTextItem(IndicatorDS.QUANTITY_BASE_VALUE, getConstants().indicQuantityBaseValue());
        baseValue.setShowIfCondition(getBaseValueIfFunction());
        
        ViewTextItem baseTime = new ViewTextItem(IndicatorDS.QUANTITY_BASE_TIME, getConstants().indicQuantityBaseTime());
        baseTime.setShowIfCondition(getBaseTimeIfFunction());
        
        ViewTextItem baseLocation = new ViewTextItem(IndicatorDS.QUANTITY_BASE_LOCATION, getConstants().indicQuantityBaseLocation());
        baseLocation.setShowIfCondition(getBaseLocationIfFunction());
        
        ViewTextItem baseQuantityIndUuid = new ViewTextItem(IndicatorDS.QUANTITY_BASE_QUANTITY_INDICATOR_UUID, getConstants().indicQuantityBaseQuantityIndicator());
        baseQuantityIndUuid.setShowIfCondition(getBaseQuantityIfFunction());
        
        setFields(type, typeText, unitUuid, unitMultiplier, sigDigits, decPlaces, min, max, denominatorUuid, numeratorUuid, isPercentange, percentageOf, indexBaseType, indexBaseTypeText, baseValue, baseTime, baseLocation, baseQuantityIndUuid);
    }
    
    public void setValue(QuantityDto quantityDto) {
        clearValues();
        if (quantityDto != null) {
            setValue(IndicatorDS.QUANTITY_TYPE, quantityDto.getType() != null ? quantityDto.getType().toString() : "");
            setValue(IndicatorDS.QUANTITY_TYPE + "-text", quantityDto.getType() != null ? getCoreMessages().getString(getCoreMessages().quantityTypeEnum() + quantityDto.getType().toString()) : "");
            setValue(IndicatorDS.QUANTITY_UNIT_UUID, getQuantityUnitSymbol(quantityDto.getUnitUuid()));
            setValue(IndicatorDS.QUANTITY_UNIT_MULTIPLIER, quantityDto.getUnitMultiplier());
            setValue(IndicatorDS.QUANTITY_SIGNIFICANT_DIGITS, quantityDto.getSignificantDigits() != null ? quantityDto.getSignificantDigits().toString() : "");
            setValue(IndicatorDS.QUANTITY_DECIMAL_PLACES, quantityDto.getDecimalPlaces() != null ? quantityDto.getDecimalPlaces().toString() : "");
            setValue(IndicatorDS.QUANTITY_MINIMUM, quantityDto.getMinimum() != null ? quantityDto.getMinimum().toString() : "");
            setValue(IndicatorDS.QUANTITY_MAXIMUM, quantityDto.getMaximum() != null ? quantityDto.getMaximum().toString() : "");
            setValue(IndicatorDS.QUANTITY_DENOMINATOR_INDICATOR_UUID, getIndicatorCode(quantityDto.getDenominatorIndicatorUuid()));
            setValue(IndicatorDS.QUANTITY_NUMERATOR_INDICATOR_UUID, getIndicatorCode(quantityDto.getNumeratorIndicatorUuid()));
            setValue(IndicatorDS.QUANTITY_IS_PERCENTAGE, quantityDto.getIsPercentage() != null ? (quantityDto.getIsPercentage() ? MetamacWebCommon.getConstants().yes() : MetamacWebCommon.getConstants().no()) : "");
            setValue(IndicatorDS.QUANTITY_INDEX_BASE_TYPE, getIndexBaseTypeEnum(quantityDto) != null  ? getIndexBaseTypeEnum(quantityDto).toString() : "");
            setValue(IndicatorDS.QUANTITY_INDEX_BASE_TYPE + "-text", getIndexBaseType(quantityDto));
            setValue(IndicatorDS.QUANTITY_BASE_VALUE, quantityDto.getBaseValue() != null ? quantityDto.getBaseValue().toString() : "");
            setValue(IndicatorDS.QUANTITY_BASE_TIME, quantityDto.getBaseTime());
            setValue(IndicatorDS.QUANTITY_BASE_LOCATION, quantityDto.getBaseLocationUuid());
            setValue(IndicatorDS.QUANTITY_BASE_QUANTITY_INDICATOR_UUID, getIndicatorCode(quantityDto.getBaseQuantityIndicatorUuid()));
            setValue(IndicatorDS.QUANTITY_PERCENTAGE_OF, RecordUtils.getInternationalStringRecord(quantityDto.getPercentageOf()));
        }
    }
    
    private String getQuantityUnitSymbol(String unitUuid) {
        if (unitUuid != null) {
            for (QuantityUnitDto unit : quantityUnitDtos) {
                if (unitUuid.equals(unit.getUuid())) {
                    return unit.getSymbol();
                }
            }
        }
        return new String();
    }
    
    private String getIndicatorCode(String indicatorUuid) {
        if (indicatorUuid != null) {
            for (IndicatorDto ind : indicatorDtos) {
                if (indicatorUuid.equals(ind.getUuid())) {
                    return ind.getCode();
                }
            }
        }
        return new String();
    }
    
}
