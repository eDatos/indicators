package es.gobcan.istac.indicators.web.client.widgets;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getCoreMessages;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.smartgwt.client.widgets.form.fields.HiddenItem;

import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;
import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.QuantityDto;
import es.gobcan.istac.indicators.web.client.indicator.presenter.IndicatorUiHandler;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS;
import es.gobcan.istac.indicators.web.client.utils.CommonUtils;

public class ViewQuantityForm extends BaseQuantityForm {

    public ViewQuantityForm(String groupTitle) {
        super(groupTitle);

        // Its important to set the value of the QuantityType (no translated name!) in a hidden field. It is required by the validators (see BaseQuantityForm).
        ViewTextItem type = new ViewTextItem(IndicatorDS.QUANTITY_TYPE, getConstants().indicQuantityType());
        type.setVisible(false);
        ViewTextItem typeText = new ViewTextItem(IndicatorDS.QUANTITY_TYPE + "-text", getConstants().indicQuantityType());

        ViewTextItem unitUuid = new ViewTextItem(IndicatorDS.QUANTITY_UNIT_UUID, getConstants().indicQuantityUnit());

        ViewMultiLanguageTextItem unitMultiplier = new ViewMultiLanguageTextItem(IndicatorDS.QUANTITY_UNIT_MULTIPLIER, getConstants().indicQuantityUnitMultiplier());

        ViewTextItem sigDigits = new ViewTextItem(IndicatorDS.QUANTITY_SIGNIFICANT_DIGITS, getConstants().indicQuantitySignificantDigits());

        ViewTextItem decPlaces = new ViewTextItem(IndicatorDS.QUANTITY_DECIMAL_PLACES, getConstants().indicQuantityDecimalPlaces());

        ViewTextItem min = new ViewTextItem(IndicatorDS.QUANTITY_MINIMUM, getConstants().indicQuantityMinimum());
        min.setShowIfCondition(getMinIfFunction());

        ViewTextItem max = new ViewTextItem(IndicatorDS.QUANTITY_MAXIMUM, getConstants().indicQuantityMaximum());
        max.setShowIfCondition(getMaxIfFunction());

        ViewTextItem denominatorUuid = new ViewTextItem(IndicatorDS.QUANTITY_DENOMINATOR_INDICATOR_TEXT, getConstants().indicQuantityDenominatorIndicator());
        denominatorUuid.setShowIfCondition(getDenominatorIfFunction());

        ViewTextItem numeratorUuid = new ViewTextItem(IndicatorDS.QUANTITY_NUMERATOR_INDICATOR_TEXT, getConstants().indicQuantityNumeratorIndicator());
        numeratorUuid.setShowIfCondition(getNumeratorIfFunction());

        ViewTextItem isPercentangeText = new ViewTextItem(IndicatorDS.QUANTITY_IS_PERCENTAGE_TEXT, getConstants().indicQuantityIsPercentage());
        isPercentangeText.setShowIfCondition(getIsPercentageIfFunction());
        HiddenItem isPercentange = new HiddenItem(IndicatorDS.QUANTITY_IS_PERCENTAGE);

        ViewMultiLanguageTextItem percentageOf = new ViewMultiLanguageTextItem(IndicatorDS.QUANTITY_PERCENTAGE_OF, getConstants().indicQuantityPercentageOf());
        percentageOf.setShowIfCondition(getPercentageOfIfFunction());

        ViewTextItem baseValue = new ViewTextItem(IndicatorDS.QUANTITY_BASE_VALUE, getConstants().indicQuantityBaseValue());
        baseValue.setShowIfCondition(getBaseValueIfFunction());

        ViewTextItem indexBaseType = new ViewTextItem(IndicatorDS.QUANTITY_INDEX_BASE_TYPE, getConstants().indicQuantityIndexMetadata());
        indexBaseType.setVisible(false);
        ViewTextItem indexBaseTypeText = new ViewTextItem(IndicatorDS.QUANTITY_INDEX_BASE_TYPE + "-text", getConstants().indicQuantityIndexMetadata());
        indexBaseTypeText.setShowIfCondition(getIndexBaseTypeIfFunction());

        ViewTextItem baseTime = new ViewTextItem(IndicatorDS.QUANTITY_BASE_TIME, getConstants().indicQuantityBaseTime());
        baseTime.setShowIfCondition(getBaseTimeIfFunction());

        ViewTextItem baseLocation = new ViewTextItem(IndicatorDS.QUANTITY_BASE_LOCATION, getConstants().indicQuantityBaseLocation());
        baseLocation.setShowIfCondition(getBaseLocationIfFunction());

        ViewTextItem baseQuantityIndUuid = new ViewTextItem(IndicatorDS.QUANTITY_BASE_QUANTITY_INDICATOR_TEXT, getConstants().indicQuantityBaseQuantityIndicator());
        baseQuantityIndUuid.setShowIfCondition(getBaseQuantityIfFunction());

        setFields(type, typeText, unitUuid, unitMultiplier, sigDigits, decPlaces, min, max, denominatorUuid, numeratorUuid, isPercentangeText, isPercentange, percentageOf, baseValue, indexBaseType,
                indexBaseTypeText, baseTime, baseLocation, baseQuantityIndUuid);
    }

    public void setValue(QuantityDto quantityDto) {
        clearValues();
        if (quantityDto != null) {
            setValue(IndicatorDS.QUANTITY_TYPE, quantityDto.getType() != null ? quantityDto.getType().toString() : "");
            setValue(IndicatorDS.QUANTITY_TYPE + "-text", quantityDto.getType() != null ? getCoreMessages().getString(getCoreMessages().quantityTypeEnum() + quantityDto.getType().toString()) : "");
            setValue(IndicatorDS.QUANTITY_UNIT_UUID, getQuantityUnitTitle(quantityDto.getUnitUuid()));
            setValue(IndicatorDS.QUANTITY_UNIT_MULTIPLIER, quantityDto.getUnitMultiplierLabel());
            setValue(IndicatorDS.QUANTITY_SIGNIFICANT_DIGITS, quantityDto.getSignificantDigits() != null ? quantityDto.getSignificantDigits().toString() : "");
            setValue(IndicatorDS.QUANTITY_DECIMAL_PLACES, quantityDto.getDecimalPlaces() != null ? quantityDto.getDecimalPlaces().toString() : "");
            setValue(IndicatorDS.QUANTITY_MINIMUM, quantityDto.getMinimum() != null ? quantityDto.getMinimum().toString() : "");
            setValue(IndicatorDS.QUANTITY_MAXIMUM, quantityDto.getMaximum() != null ? quantityDto.getMaximum().toString() : "");

            setValue(IndicatorDS.QUANTITY_DENOMINATOR_INDICATOR_TEXT, ""); // Value set in setIndicatorQuantityDenominator method
            if (!StringUtils.isBlank(quantityDto.getDenominatorIndicatorUuid())) {
                if (uiHandlers instanceof IndicatorUiHandler) {
                    ((IndicatorUiHandler) uiHandlers).retrieveQuantityDenominatorIndicator(quantityDto.getDenominatorIndicatorUuid());
                }
            }

            setValue(IndicatorDS.QUANTITY_NUMERATOR_INDICATOR_TEXT, ""); // Value set in setIndicatorQuantityNumerator method
            if (!StringUtils.isBlank(quantityDto.getNumeratorIndicatorUuid())) {
                if (uiHandlers instanceof IndicatorUiHandler) {
                    ((IndicatorUiHandler) uiHandlers).retrieveQuantityNumeratorIndicator(quantityDto.getNumeratorIndicatorUuid());
                }
            }

            setValue(IndicatorDS.QUANTITY_IS_PERCENTAGE, quantityDto.getIsPercentage() != null ? quantityDto.getIsPercentage().booleanValue() : false);
            setValue(IndicatorDS.QUANTITY_IS_PERCENTAGE_TEXT, quantityDto.getIsPercentage() != null ? (quantityDto.getIsPercentage() ? MetamacWebCommon.getConstants().yes() : MetamacWebCommon
                    .getConstants().no()) : "");
            setValue(IndicatorDS.QUANTITY_INDEX_BASE_TYPE, getIndexBaseTypeEnum(quantityDto) != null ? getIndexBaseTypeEnum(quantityDto).toString() : "");
            setValue(IndicatorDS.QUANTITY_INDEX_BASE_TYPE + "-text", getIndexBaseType(quantityDto));
            setValue(IndicatorDS.QUANTITY_BASE_VALUE, quantityDto.getBaseValue() != null ? quantityDto.getBaseValue().toString() : "");
            setValue(IndicatorDS.QUANTITY_BASE_TIME, quantityDto.getBaseTime());

            setValue(IndicatorDS.QUANTITY_BASE_LOCATION, ""); // Base location set in setGeographicalValue method
            if (!StringUtils.isBlank(quantityDto.getBaseLocationUuid())) {
                if (uiHandlers instanceof IndicatorUiHandler) {
                    ((IndicatorUiHandler) uiHandlers).retrieveGeographicalValue(quantityDto.getBaseLocationUuid());
                }
            }

            setValue(IndicatorDS.QUANTITY_BASE_QUANTITY_INDICATOR_TEXT, ""); // Value set in setIndicatorQuantityIndicatorBase method
            if (!StringUtils.isBlank(quantityDto.getBaseQuantityIndicatorUuid())) {
                if (uiHandlers instanceof IndicatorUiHandler) {
                    ((IndicatorUiHandler) uiHandlers).retrieveQuantityIndicatorBase(quantityDto.getBaseQuantityIndicatorUuid());
                }
            }

            setValue(IndicatorDS.QUANTITY_PERCENTAGE_OF, quantityDto.getPercentageOf());
        }
    }

    public void setGeographicalValue(GeographicalValueDto geographicalValueDto) {
        setValue(IndicatorDS.QUANTITY_BASE_LOCATION,
                geographicalValueDto != null ? geographicalValueDto.getCode() + " - " + InternationalStringUtils.getLocalisedString(geographicalValueDto.getTitle()) : new String());
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

}
