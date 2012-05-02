package es.gobcan.istac.indicators.web.client.widgets;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getCoreMessages;
import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getMessages;

import java.util.LinkedHashMap;
import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCheckboxItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.IntegerItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.validator.CustomValidator;
import com.smartgwt.client.widgets.form.validator.RequiredIfFunction;
import com.smartgwt.client.widgets.form.validator.RequiredIfValidator;

import es.gobcan.istac.indicators.core.dto.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.QuantityDto;
import es.gobcan.istac.indicators.core.dto.QuantityUnitDto;
import es.gobcan.istac.indicators.core.dto.RateDerivationDto;
import es.gobcan.istac.indicators.core.enume.domain.QuantityTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.RateDerivationMethodTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.RateDerivationRoundingEnum;
import es.gobcan.istac.indicators.web.client.model.ds.DataSourceDS;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS;
import es.gobcan.istac.indicators.web.client.utils.CommonUtils;

public class RateDerivationForm extends BaseRateDerivationForm {


    
    private QuantityTypeEnum  quantityType;

    private RateDerivationDto rateDerivationDto;
    private IndicatorDto      indicatorDto;

    private boolean           viewMode;         // If view mode is set, method and method type cannot be edited

    public RateDerivationForm(String groupTitle, QuantityTypeEnum quantityType) {
        super(groupTitle);

        this.quantityType = quantityType;

        // Method type

        ViewTextItem staticMethodType = new ViewTextItem(DataSourceDS.RATE_DERIVATION_METHOD_TYPE_VIEW, getConstants().datasourceMethodType());
        staticMethodType.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return viewMode;
            }
        });

        RequiredSelectItem methodType = new RequiredSelectItem(DataSourceDS.RATE_DERIVATION_METHOD_TYPE, getConstants().datasourceMethodType());
        methodType.setValueMap(CommonUtils.getRateDerivationMethodTypeValueMap());
        methodType.addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                RateDerivationForm.this.markForRedraw();
            }
        });
        methodType.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !form.getItem(DataSourceDS.RATE_DERIVATION_METHOD_TYPE_VIEW).isVisible();
            }
        });

        // METHOD

        // Showed when viewing
        ViewTextItem viewMethod = new ViewTextItem(DataSourceDS.RATE_DERIVATION_METHOD_VIEW, getConstants().datasourceMethod());
        viewMethod.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return viewMode && showItemIfApplicable(item, value, form);
            }
        });

        // Showed when editing and method type is CALCULATED
        RequiredTextItem methodCalculated = new RequiredTextItem(DataSourceDS.RATE_DERIVATION_METHOD_CALCULATED, getConstants().datasourceMethod());
        methodCalculated.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (!form.getItem(DataSourceDS.RATE_DERIVATION_METHOD_VIEW).isVisible()) {
                    String methodType = form.getValue(DataSourceDS.RATE_DERIVATION_METHOD_TYPE) != null ? (String) form.getValue(DataSourceDS.RATE_DERIVATION_METHOD_TYPE) : null;
                    if (!StringUtils.isBlank(methodType)) {
                        return RateDerivationMethodTypeEnum.CALCULATE.toString().equals(methodType);
                    }
                }
                return false;
            }
        });

        // Showed when editing, method type is LOAD and contVariable (measure variable) is not set
        ViewTextItem viewMethodLoad = new ViewTextItem(DataSourceDS.RATE_DERIVATION_METHOD_LOAD_VIEW, getConstants().datasourceMethod());
        viewMethodLoad.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (!form.getItem(DataSourceDS.RATE_DERIVATION_METHOD_VIEW).isVisible()) {
                    String methodType = form.getValue(DataSourceDS.RATE_DERIVATION_METHOD_TYPE) != null ? (String) form.getValue(DataSourceDS.RATE_DERIVATION_METHOD_TYPE) : null;
                    if (!StringUtils.isBlank(methodType) && RateDerivationMethodTypeEnum.LOAD.toString().equals(methodType)) { // If method type is LOAD
                        if (value != null && !StringUtils.isBlank(value.toString())) { // If contVariable (measure variable) is not set
                            return true;
                        }
                    }
                }
                return false;
            }
        });

        // Showed when editing, method type is LOAD and contVariable (measure variable) is set
        // ValueMap set in setMeasureVariableValues
        RequiredSelectItem methodLoad = new RequiredSelectItem(DataSourceDS.RATE_DERIVATION_METHOD_LOAD, getConstants().datasourceMethod());
        methodLoad.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (!form.getItem(DataSourceDS.RATE_DERIVATION_METHOD_VIEW).isVisible()) {
                    String methodType = form.getValue(DataSourceDS.RATE_DERIVATION_METHOD_TYPE) != null ? (String) form.getValue(DataSourceDS.RATE_DERIVATION_METHOD_TYPE) : null;
                    if (!StringUtils.isBlank(methodType) && RateDerivationMethodTypeEnum.LOAD.toString().equals(methodType)) { // If method type is LOAD
                        if (!form.getItem(DataSourceDS.RATE_DERIVATION_METHOD_LOAD_VIEW).isVisible()) { // If contVariable (measure variable) is set
                            return true;
                        }
                    }
                }
                return false;
            }
        });

        // Rounding

        SelectItem rounding = new SelectItem(DataSourceDS.RATE_DERIVATION_ROUNDING, getConstants().datasourceRounding());
        rounding.setValueMap(CommonUtils.getRateDerivationRoundingValueMap());
        rounding.setShowIfCondition(getFormItemShowIfApplicable());
        rounding.setValidators(new RequiredIfValidator(new RequiredIfFunction() {

            @Override
            public boolean execute(FormItem formItem, Object value) {
                // Required when method type is CALCULATED
                return RateDerivationMethodTypeEnum.CALCULATE.toString().equals(getValueAsString((DataSourceDS.RATE_DERIVATION_METHOD_TYPE)));
            }
        }));

        // QUANTITY

        ViewTextItem type = new ViewTextItem(IndicatorDS.QUANTITY_TYPE, getConstants().indicQuantityType());
        type.setVisible(false);

        ViewTextItem typeText = new ViewTextItem(IndicatorDS.QUANTITY_TYPE_TEXT, getConstants().indicQuantityType());

        RequiredSelectItem unitUuid = new RequiredSelectItem(IndicatorDS.QUANTITY_UNIT_UUID, getConstants().indicQuantityUnit());
        unitUuid.setShowIfCondition(getFormItemShowIfApplicable());

        IntegerItem unitMultiplier = new IntegerItem(IndicatorDS.QUANTITY_UNIT_MULTIPLIER, getConstants().indicQuantityUnitMultiplier());
        unitMultiplier.setRequired(true);
        unitMultiplier.setShowIfCondition(getFormItemShowIfApplicable());

        IntegerItem sigDigits = new IntegerItem(IndicatorDS.QUANTITY_SIGNIFICANT_DIGITS, getConstants().indicQuantitySignificantDigits());
        sigDigits.setShowIfCondition(getFormItemShowIfApplicable());

        IntegerItem decPlaces = new IntegerItem(IndicatorDS.QUANTITY_DECIMAL_PLACES, getConstants().indicQuantityDecimalPlaces());
        decPlaces.setShowIfCondition(getFormItemShowIfApplicable());
        decPlaces.setValidators(new RequiredIfValidator(new RequiredIfFunction() {

            @Override
            public boolean execute(FormItem formItem, Object value) {
                // Required when quantity type is CHANGE_RATE and method type is CALCULATED
                return QuantityTypeEnum.CHANGE_RATE.toString().equals(RateDerivationForm.this.getValueAsString(IndicatorDS.QUANTITY_TYPE))
                        && RateDerivationMethodTypeEnum.CALCULATE.toString().equals(getValueAsString((DataSourceDS.RATE_DERIVATION_METHOD_TYPE)));
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

        ViewTextItem baseQuantityIndUuid = new ViewTextItem(IndicatorDS.QUANTITY_BASE_QUANTITY_INDICATOR_UUID, getConstants().indicQuantityBaseQuantityIndicator());
        baseQuantityIndUuid.setVisible(false);

        setFields(staticMethodType, methodType, viewMethod, methodCalculated, viewMethodLoad, methodLoad, rounding, type, typeText, unitUuid, unitMultiplier, sigDigits, decPlaces, min, max,
                denominatorUuid, numeratorUuid, isPercentange, percentageOf, baseQuantityIndUuid);

        markForRedraw();
    }

    public void setValue(RateDerivationDto rateDerivationDto) {
        this.rateDerivationDto = rateDerivationDto;

        clearValues();
        
        String method = rateDerivationDto.getMethod();
        if (DataSourceDto.isObsValue(rateDerivationDto.getMethod())) {
            method = getConstants().dataSourceObsValue();
        }
        
        setValue(DataSourceDS.RATE_DERIVATION_METHOD_VIEW, method);
        setValue(DataSourceDS.RATE_DERIVATION_METHOD_CALCULATED, rateDerivationDto.getMethod());
        setValue(DataSourceDS.RATE_DERIVATION_METHOD_LOAD, rateDerivationDto.getMethod());
        
        String methodType = null;
        if (rateDerivationDto.getMethodType() != null) {
            methodType = rateDerivationDto.getMethodType().name();
        } else {
            methodType = NOT_APPLICABLE;
        }
        
        setValue(DataSourceDS.RATE_DERIVATION_METHOD_TYPE_VIEW, getCoreMessages().getString(getCoreMessages().rateDerivationMethodTypeEnum() + methodType));
        setValue(DataSourceDS.RATE_DERIVATION_METHOD_TYPE, methodType);
        setValue(DataSourceDS.RATE_DERIVATION_ROUNDING, rateDerivationDto.getRounding() != null ? rateDerivationDto.getRounding().toString() : new String());

        if (rateDerivationDto.getQuantity() == null) {
            rateDerivationDto.setQuantity(new QuantityDto());
        }

        QuantityDto quantityDto = rateDerivationDto.getQuantity();

        setValue(IndicatorDS.QUANTITY_TYPE, quantityType.toString());
        setValue(IndicatorDS.QUANTITY_TYPE_TEXT, getCoreMessages().getString(getCoreMessages().quantityTypeEnum() + quantityType.toString()));
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

        setValue(IndicatorDS.QUANTITY_BASE_QUANTITY_INDICATOR_UUID, quantityDto.getBaseQuantityIndicatorUuid());
        setValue(IndicatorDS.QUANTITY_PERCENTAGE_OF, RecordUtils.getInternationalStringRecord(quantityDto.getPercentageOf()));

    }
    
    public void prepareNewRate() {
        clearValues();
        
        this.rateDerivationDto = new RateDerivationDto();
        setValue(DataSourceDS.RATE_DERIVATION_METHOD_TYPE_VIEW, getCoreMessages().rateDerivationMethodTypeEnumNOT_APPLICABLE());
        setValue(DataSourceDS.RATE_DERIVATION_METHOD_TYPE, NOT_APPLICABLE);
        
        setValue(IndicatorDS.QUANTITY_TYPE, quantityType.toString());
        setValue(IndicatorDS.QUANTITY_TYPE_TEXT, getCoreMessages().getString(getCoreMessages().quantityTypeEnum() + quantityType.toString()));
    }
    
    

    public RateDerivationDto getValue() {
        rateDerivationDto.setMethodType(getValueAsString(DataSourceDS.RATE_DERIVATION_METHOD_TYPE) != null && !getValueAsString(DataSourceDS.RATE_DERIVATION_METHOD_TYPE).isEmpty()
                ? RateDerivationMethodTypeEnum.valueOf(getValueAsString(DataSourceDS.RATE_DERIVATION_METHOD_TYPE))
                : null);

        String method = new String();
        if (getItem(DataSourceDS.RATE_DERIVATION_METHOD_CALCULATED).isVisible()) {
            method = getValueAsString(DataSourceDS.RATE_DERIVATION_METHOD_CALCULATED);
            rateDerivationDto.setMethod(method);
        } else if (getItem(DataSourceDS.RATE_DERIVATION_METHOD_LOAD).isVisible()) {
            method = getValueAsString(DataSourceDS.RATE_DERIVATION_METHOD_LOAD);
            rateDerivationDto.setMethod(method);
        } else if (getItem(DataSourceDS.RATE_DERIVATION_METHOD_LOAD_VIEW).isVisible()) {
            method = DataSourceDto.OBS_VALUE;
            rateDerivationDto.setMethod(method);
        } else {
            //keep old method
        }

        rateDerivationDto.setRounding(getValueAsString(DataSourceDS.RATE_DERIVATION_ROUNDING) != null && !getValueAsString(DataSourceDS.RATE_DERIVATION_ROUNDING).isEmpty()
                ? RateDerivationRoundingEnum.valueOf(getValueAsString(DataSourceDS.RATE_DERIVATION_ROUNDING))
                : null);

        QuantityDto quantityDto = rateDerivationDto.getQuantity();
        if (quantityDto == null) {
            quantityDto = new QuantityDto();
        }

        quantityDto.setType(quantityType);
        quantityDto.setUnitUuid(CommonUtils.getUuidString(getValueAsString(IndicatorDS.QUANTITY_UNIT_UUID)));
        quantityDto.setUnitMultiplier(getValue(IndicatorDS.QUANTITY_UNIT_MULTIPLIER) != null ? (Integer) getValue(IndicatorDS.QUANTITY_UNIT_MULTIPLIER) : null);
        quantityDto.setSignificantDigits(getValue(IndicatorDS.QUANTITY_SIGNIFICANT_DIGITS) != null ? (Integer) getValue(IndicatorDS.QUANTITY_SIGNIFICANT_DIGITS) : null);
        quantityDto.setDecimalPlaces(getValue(IndicatorDS.QUANTITY_DECIMAL_PLACES) != null ? (Integer) getValue(IndicatorDS.QUANTITY_DECIMAL_PLACES) : null);
        // Only set value if item is visible (these item are quantity type dependent)
        quantityDto.setMinimum(getItem(IndicatorDS.QUANTITY_MINIMUM).isVisible() ? (getValue(IndicatorDS.QUANTITY_MINIMUM) != null ? (Integer) getValue(IndicatorDS.QUANTITY_MINIMUM) : null) : null);
        quantityDto.setMaximum(getItem(IndicatorDS.QUANTITY_MAXIMUM).isVisible() ? (getValue(IndicatorDS.QUANTITY_MAXIMUM) != null ? (Integer) getValue(IndicatorDS.QUANTITY_MAXIMUM) : null) : null);
        quantityDto.setDenominatorIndicatorUuid(getItem(IndicatorDS.QUANTITY_DENOMINATOR_INDICATOR_UUID).isVisible() ? CommonUtils
                .getUuidString(getValueAsString(IndicatorDS.QUANTITY_DENOMINATOR_INDICATOR_UUID)) : null);
        quantityDto.setNumeratorIndicatorUuid(getItem(IndicatorDS.QUANTITY_NUMERATOR_INDICATOR_UUID).isVisible() ? CommonUtils
                .getUuidString(getValueAsString(IndicatorDS.QUANTITY_NUMERATOR_INDICATOR_UUID)) : null);
        quantityDto.setIsPercentage(getItem(IndicatorDS.QUANTITY_IS_PERCENTAGE).isVisible() ? (getValue(IndicatorDS.QUANTITY_IS_PERCENTAGE) != null ? Boolean
                .valueOf((Boolean) getValue(IndicatorDS.QUANTITY_IS_PERCENTAGE)) : false) : null);
        quantityDto.setPercentageOf(getItem(IndicatorDS.QUANTITY_PERCENTAGE_OF).isVisible() ? ((MultiLanguageTextItem) getItem(IndicatorDS.QUANTITY_PERCENTAGE_OF)).getValue() : null);

        if (QuantityTypeEnum.CHANGE_RATE.toString().equals(getValueAsString(IndicatorDS.QUANTITY_TYPE))) {
            // If selected type if CHANGE_RATE, set current indicator as base quantity indicator
            quantityDto.setBaseQuantityIndicatorUuid(indicatorDto.getUuid());
        } else {
            quantityDto.setBaseQuantityIndicatorUuid(null);
        }

        rateDerivationDto.setQuantity(quantityDto);

        return rateDerivationDto;
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

    @Override
    public void setQuantityUnits(List<QuantityUnitDto> units) {
        super.setQuantityUnits(units);
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        for (QuantityUnitDto unit : units) {
            valueMap.put(unit.getUuid(), unit.getSymbol());
        }
        ((SelectItem) getItem(IndicatorDS.QUANTITY_UNIT_UUID)).setValueMap(valueMap);
    }

    @Override
    public void setIndicators(List<IndicatorDto> indicators) {
        super.setIndicators(indicators);
        LinkedHashMap<String, String> valueMap = CommonUtils.getIndicatorsValueMap(indicators);
        ((SelectItem) getItem(IndicatorDS.QUANTITY_DENOMINATOR_INDICATOR_UUID)).setValueMap(valueMap);
        ((SelectItem) getItem(IndicatorDS.QUANTITY_NUMERATOR_INDICATOR_UUID)).setValueMap(valueMap);
    }

    /**
     * In edition mode, shows query dependent fields in view mode
     */
    public void setViewQueryMode() {
        this.viewMode = true;
    }

    /**
     * In edition mode, shows query dependent fields in view mode
     */
    public void setEditionQueryMode() {
        this.viewMode = false;
        // TODO clear query dependent values (method and method type)
    }

    public void setMeasureVariableValues(LinkedHashMap<String, String> valueMap) {
        getItem(DataSourceDS.RATE_DERIVATION_METHOD_LOAD).setValueMap(valueMap);
    }

}
