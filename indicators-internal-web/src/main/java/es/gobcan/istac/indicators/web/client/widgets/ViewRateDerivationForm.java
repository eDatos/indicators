package es.gobcan.istac.indicators.web.client.widgets;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getCoreMessages;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.smartgwt.client.widgets.form.fields.HiddenItem;

import es.gobcan.istac.indicators.core.dto.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.QuantityDto;
import es.gobcan.istac.indicators.core.dto.RateDerivationDto;
import es.gobcan.istac.indicators.core.enume.domain.QuantityTypeEnum;
import es.gobcan.istac.indicators.web.client.enums.IndicatorCalculationTypeEnum;
import es.gobcan.istac.indicators.web.client.enums.RateDerivationTypeEnum;
import es.gobcan.istac.indicators.web.client.indicator.presenter.IndicatorUiHandler;
import es.gobcan.istac.indicators.web.client.model.ds.DataSourceDS;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS;
import es.gobcan.istac.indicators.web.client.utils.CommonUtils;

public class ViewRateDerivationForm extends BaseRateDerivationForm {

    private RateDerivationTypeEnum rateDerivationTypeEnum;

    public ViewRateDerivationForm(String groupTitle, RateDerivationTypeEnum rateDerivationTypeEnum) {
        super(groupTitle);

        this.rateDerivationTypeEnum = rateDerivationTypeEnum;

        ViewTextItem methodType = new ViewTextItem(DataSourceDS.RATE_DERIVATION_METHOD_TYPE, getConstants().datasourceMethodType());
        methodType.setVisible(false);

        ViewTextItem methodTypeText = new ViewTextItem(DataSourceDS.RATE_DERIVATION_METHOD_TYPE + "-text", getConstants().datasourceMethodType());

        ViewTextItem method = new ViewTextItem(DataSourceDS.RATE_DERIVATION_METHOD_VIEW, getConstants().datasourceMethod());
        method.setShowIfCondition(getFormItemShowIfApplicable());

        ViewTextItem rounding = new ViewTextItem(DataSourceDS.RATE_DERIVATION_ROUNDING, getConstants().datasourceRounding());
        rounding.setShowIfCondition(getFormItemShowIfApplicable());

        // Its important to set the value of the QuantityType (no translated name!) in a hidden field. It is required by the validators (see BaseQuantityForm).
        ViewTextItem type = new ViewTextItem(IndicatorDS.QUANTITY_TYPE, getConstants().indicQuantityType());
        type.setVisible(false);

        ViewTextItem typeText = new ViewTextItem(IndicatorDS.QUANTITY_TYPE + "-text", getConstants().indicQuantityType());

        ViewTextItem unitUuid = new ViewTextItem(IndicatorDS.QUANTITY_UNIT_UUID, getConstants().indicQuantityUnit());
        unitUuid.setShowIfCondition(getFormItemShowIfApplicable());

        ViewMultiLanguageTextItem unitMultiplier = new ViewMultiLanguageTextItem(IndicatorDS.QUANTITY_UNIT_MULTIPLIER, getConstants().indicQuantityUnitMultiplier());
        unitMultiplier.setShowIfCondition(getFormItemShowIfApplicable());

        ViewTextItem sigDigits = new ViewTextItem(IndicatorDS.QUANTITY_SIGNIFICANT_DIGITS, getConstants().indicQuantitySignificantDigits());
        sigDigits.setShowIfCondition(getFormItemShowIfApplicable());

        ViewTextItem decPlaces = new ViewTextItem(IndicatorDS.QUANTITY_DECIMAL_PLACES, getConstants().indicQuantityDecimalPlaces());
        decPlaces.setShowIfCondition(getFormItemShowIfApplicable());

        ViewTextItem min = new ViewTextItem(IndicatorDS.QUANTITY_MINIMUM, getConstants().indicQuantityMinimum());
        min.setShowIfCondition(getMinIfFunction());

        ViewTextItem max = new ViewTextItem(IndicatorDS.QUANTITY_MAXIMUM, getConstants().indicQuantityMaximum());
        max.setShowIfCondition(getMaxIfFunction());

        ViewTextItem denominatorUuid = new ViewTextItem(IndicatorDS.QUANTITY_DENOMINATOR_INDICATOR_TEXT, getConstants().indicQuantityDenominatorIndicator());
        denominatorUuid.setShowIfCondition(getDenominatorIfFunction());

        ViewTextItem numeratorUuid = new ViewTextItem(IndicatorDS.QUANTITY_NUMERATOR_INDICATOR_TEXT, getConstants().indicQuantityNumeratorIndicator());
        numeratorUuid.setShowIfCondition(getNumeratorIfFunction());

        HiddenItem isPercentange = new HiddenItem(IndicatorDS.QUANTITY_IS_PERCENTAGE);

        ViewTextItem isPercentangeText = new ViewTextItem(IndicatorDS.QUANTITY_IS_PERCENTAGE_TEXT, getConstants().indicQuantityIsPercentage());
        isPercentange.setShowIfCondition(getIsPercentageIfFunction());

        ViewTextItem percentageOf = new ViewTextItem(IndicatorDS.QUANTITY_PERCENTAGE_OF, getConstants().indicQuantityPercentageOf());
        percentageOf.setShowIfCondition(getPercentageOfIfFunction());

        setFields(methodType, methodTypeText, method, rounding, type, typeText, unitUuid, unitMultiplier, sigDigits, decPlaces, min, max, denominatorUuid, numeratorUuid, isPercentange,
                isPercentangeText, percentageOf);
    }

    public void setValue(RateDerivationDto rateDerivationDto) {
        clearValues();

        String methodType = null;
        if (rateDerivationDto.getMethodType() != null) {
            methodType = rateDerivationDto.getMethodType().name();
        } else {
            methodType = NOT_APPLICABLE;
        }

        setValue(DataSourceDS.RATE_DERIVATION_METHOD_TYPE, methodType);

        setValue(DataSourceDS.RATE_DERIVATION_METHOD_TYPE + "-text", getCoreMessages().getString(getCoreMessages().rateDerivationMethodTypeEnum() + methodType));

        if (DataSourceDto.isObsValue(rateDerivationDto.getMethod())) {
            setValue(DataSourceDS.RATE_DERIVATION_METHOD_VIEW, getConstants().dataSourceObsValue());
        } else {
            setValue(DataSourceDS.RATE_DERIVATION_METHOD_VIEW, rateDerivationDto.getMethod());
        }

        setValue(DataSourceDS.RATE_DERIVATION_ROUNDING,
                rateDerivationDto.getRounding() != null ? getCoreMessages().getString(getCoreMessages().rateDerivationRoundingEnum() + rateDerivationDto.getRounding().getName()) : new String());

        QuantityDto quantityDto = rateDerivationDto.getQuantity();

        if (quantityDto != null) {
            setValue(IndicatorDS.QUANTITY_TYPE, quantityDto.getType() != null ? quantityDto.getType().toString() : "");
            setValue(IndicatorDS.QUANTITY_TYPE + "-text", quantityDto.getType() != null ? getCoreMessages().getString(getCoreMessages().quantityTypeEnum() + quantityDto.getType().toString()) : "");
            setValue(IndicatorDS.QUANTITY_UNIT_UUID, getQuantityUnitTitle(quantityDto.getUnitUuid()));
            setValue(IndicatorDS.QUANTITY_UNIT_MULTIPLIER, RecordUtils.getInternationalStringRecord(quantityDto.getUnitMultiplierLabel()));
            setValue(IndicatorDS.QUANTITY_SIGNIFICANT_DIGITS, quantityDto.getSignificantDigits() != null ? quantityDto.getSignificantDigits().toString() : "");
            setValue(IndicatorDS.QUANTITY_DECIMAL_PLACES, quantityDto.getDecimalPlaces() != null ? quantityDto.getDecimalPlaces().toString() : "");
            setValue(IndicatorDS.QUANTITY_MINIMUM, quantityDto.getMinimum() != null ? quantityDto.getMinimum().toString() : "");
            setValue(IndicatorDS.QUANTITY_MAXIMUM, quantityDto.getMaximum() != null ? quantityDto.getMaximum().toString() : "");

            setValue(IndicatorDS.QUANTITY_DENOMINATOR_INDICATOR_TEXT, ""); // Value set in setDenominatorIndicator method
            if (!StringUtils.isBlank(quantityDto.getDenominatorIndicatorUuid())) {
                if (uiHandlers instanceof IndicatorUiHandler) {
                    ((IndicatorUiHandler) uiHandlers).retrieveRateIndicator(quantityDto.getDenominatorIndicatorUuid(), rateDerivationTypeEnum, IndicatorCalculationTypeEnum.DENOMINATOR);
                }
            }

            setValue(IndicatorDS.QUANTITY_NUMERATOR_INDICATOR_TEXT, ""); // Value set in setNumeratorIndicator method
            if (!StringUtils.isBlank(quantityDto.getNumeratorIndicatorUuid())) {
                if (uiHandlers instanceof IndicatorUiHandler) {
                    ((IndicatorUiHandler) uiHandlers).retrieveRateIndicator(quantityDto.getNumeratorIndicatorUuid(), rateDerivationTypeEnum, IndicatorCalculationTypeEnum.NUMERATOR);
                }
            }

            setValue(IndicatorDS.QUANTITY_IS_PERCENTAGE, quantityDto.getIsPercentage() != null ? quantityDto.getIsPercentage() : false);

            setValue(IndicatorDS.QUANTITY_IS_PERCENTAGE_TEXT, quantityDto.getIsPercentage() != null ? (quantityDto.getIsPercentage() ? MetamacWebCommon.getConstants().yes() : MetamacWebCommon
                    .getConstants().no()) : "");
            setValue(IndicatorDS.QUANTITY_PERCENTAGE_OF, RecordUtils.getInternationalStringRecord(quantityDto.getPercentageOf()));
        }
    }

    public void prepareNewRate(QuantityTypeEnum quantityType) {
        setValue(DataSourceDS.RATE_DERIVATION_METHOD_TYPE, NOT_APPLICABLE);
        setValue(DataSourceDS.RATE_DERIVATION_METHOD_TYPE + "-text", getCoreMessages().rateDerivationMethodTypeEnumNOT_APPLICABLE());
        setValue(IndicatorDS.QUANTITY_TYPE + "-text", quantityType != null ? getCoreMessages().getString(getCoreMessages().quantityTypeEnum() + quantityType.toString()) : "");
    }

    public void setNumeratorIndicator(IndicatorDto indicatorDto) {
        setValue(IndicatorDS.QUANTITY_NUMERATOR_INDICATOR_TEXT, CommonUtils.getIndicatorText(indicatorDto.getCode(), indicatorDto.getTitle()));
    }

    public void setDenominatorIndicator(IndicatorDto indicatorDto) {
        setValue(IndicatorDS.QUANTITY_DENOMINATOR_INDICATOR_TEXT, CommonUtils.getIndicatorText(indicatorDto.getCode(), indicatorDto.getTitle()));
    }

}
