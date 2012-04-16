package es.gobcan.istac.indicators.web.client.widgets;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getCoreMessages;

import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import es.gobcan.istac.indicators.core.dto.QuantityDto;
import es.gobcan.istac.indicators.core.dto.RateDerivationDto;
import es.gobcan.istac.indicators.web.client.model.ds.DataSourceDS;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS;

public class ViewRateDerivationForm extends BaseRateDerivationForm {

    public ViewRateDerivationForm(String groupTitle) {
        super(groupTitle);

        ViewTextItem methodType = new ViewTextItem(DataSourceDS.RATE_DERIVATION_METHOD_TYPE, getConstants().datasourceMethodType());

        ViewTextItem method = new ViewTextItem(DataSourceDS.RATE_DERIVATION_METHOD_VIEW, getConstants().datasourceMethod());

        ViewTextItem rounding = new ViewTextItem(DataSourceDS.RATE_DERIVATION_ROUNDING, getConstants().datasourceRounding());

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

        setFields(methodType, method, rounding, type, typeText, unitUuid, unitMultiplier, sigDigits, decPlaces, min, max, denominatorUuid, numeratorUuid, isPercentange, percentageOf);
    }

    public void setValue(RateDerivationDto rateDerivationDto) {
        clearValues();

        setValue(DataSourceDS.RATE_DERIVATION_METHOD_VIEW, rateDerivationDto.getMethod());
        setValue(DataSourceDS.RATE_DERIVATION_METHOD_TYPE,
                rateDerivationDto.getMethodType() != null ? getCoreMessages().getString(getCoreMessages().rateDerivationMethodTypeEnum() + rateDerivationDto.getMethodType().getName()) : new String());
        setValue(DataSourceDS.RATE_DERIVATION_ROUNDING,
                rateDerivationDto.getRounding() != null ? getCoreMessages().getString(getCoreMessages().rateDerivationRoundingEnum() + rateDerivationDto.getRounding().getName()) : new String());

        QuantityDto quantityDto = rateDerivationDto.getQuantity();

        if (quantityDto != null) {
            setValue(IndicatorDS.QUANTITY_TYPE, quantityDto.getType() != null ? quantityDto.getType().toString() : "");
            setValue(IndicatorDS.QUANTITY_TYPE + "-text", quantityDto.getType() != null ? getCoreMessages().getString(getCoreMessages().quantityTypeEnum() + quantityDto.getType().toString()) : "");
            setValue(IndicatorDS.QUANTITY_UNIT_UUID, getQuantityUnitSymbol(quantityDto.getUnitUuid()));
            setValue(IndicatorDS.QUANTITY_UNIT_MULTIPLIER, quantityDto.getUnitMultiplier());
            setValue(IndicatorDS.QUANTITY_SIGNIFICANT_DIGITS, quantityDto.getSignificantDigits() != null ? quantityDto.getSignificantDigits().toString() : "");
            setValue(IndicatorDS.QUANTITY_DECIMAL_PLACES, quantityDto.getDecimalPlaces() != null ? quantityDto.getDecimalPlaces().toString() : "");
            setValue(IndicatorDS.QUANTITY_MINIMUM, quantityDto.getMinimum() != null ? quantityDto.getMinimum().toString() : "");
            setValue(IndicatorDS.QUANTITY_MAXIMUM, quantityDto.getMaximum() != null ? quantityDto.getMaximum().toString() : "");
            setValue(IndicatorDS.QUANTITY_DENOMINATOR_INDICATOR_UUID, getIndicatorText(quantityDto.getDenominatorIndicatorUuid()));
            setValue(IndicatorDS.QUANTITY_NUMERATOR_INDICATOR_UUID, getIndicatorText(quantityDto.getNumeratorIndicatorUuid()));
            setValue(IndicatorDS.QUANTITY_IS_PERCENTAGE, quantityDto.getIsPercentage() != null ? (quantityDto.getIsPercentage() ? MetamacWebCommon.getConstants().yes() : MetamacWebCommon
                    .getConstants().no()) : "");
            setValue(IndicatorDS.QUANTITY_PERCENTAGE_OF, RecordUtils.getInternationalStringRecord(quantityDto.getPercentageOf()));
        }
    }

}
