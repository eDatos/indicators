package es.gobcan.istac.indicators.web.client.widgets;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getCoreMessages;
import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getMessages;

import java.util.LinkedHashMap;
import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomIntegerItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.SearchViewTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.form.validator.CustomValidator;

import es.gobcan.istac.indicators.core.dto.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.IndicatorSummaryDto;
import es.gobcan.istac.indicators.core.dto.QuantityDto;
import es.gobcan.istac.indicators.core.dto.QuantityUnitDto;
import es.gobcan.istac.indicators.core.dto.RateDerivationDto;
import es.gobcan.istac.indicators.core.dto.UnitMultiplierDto;
import es.gobcan.istac.indicators.core.enume.domain.QuantityTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.RateDerivationMethodTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.RateDerivationRoundingEnum;
import es.gobcan.istac.indicators.web.client.enums.IndicatorCalculationTypeEnum;
import es.gobcan.istac.indicators.web.client.enums.RateDerivationTypeEnum;
import es.gobcan.istac.indicators.web.client.indicator.presenter.IndicatorUiHandler;
import es.gobcan.istac.indicators.web.client.model.ds.DataSourceDS;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS;
import es.gobcan.istac.indicators.web.client.utils.CommonUtils;

public class RateDerivationForm extends BaseRateDerivationForm {

    private QuantityTypeEnum       quantityType;
    private RateDerivationTypeEnum rateDerivationTypeEnum;

    private RateDerivationDto      rateDerivationDto;
    private IndicatorDto           indicatorDto;

    private IndicatorsSearchWindow indicatorDenominatorSearchWindow;
    private IndicatorsSearchWindow indicatorNumeratorSearchWindow;

    private boolean                viewMode;                        // If view mode is set, method and method type cannot be edited

    public RateDerivationForm(String groupTitle, QuantityTypeEnum quantityType, RateDerivationTypeEnum rateDerivationTypeEnum) {
        super(groupTitle);

        this.quantityType = quantityType;
        this.rateDerivationTypeEnum = rateDerivationTypeEnum;

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

        RequiredSelectItem rounding = new RequiredSelectItem(DataSourceDS.RATE_DERIVATION_ROUNDING, getConstants().datasourceRounding());
        rounding.setValueMap(CommonUtils.getRateDerivationRoundingValueMap());
        rounding.setShowIfCondition(getFormItemShowIfApplicable());

        // QUANTITY

        ViewTextItem type = new ViewTextItem(IndicatorDS.QUANTITY_TYPE, getConstants().indicQuantityType());
        type.setVisible(false);

        ViewTextItem typeText = new ViewTextItem(IndicatorDS.QUANTITY_TYPE_TEXT, getConstants().indicQuantityType());

        RequiredSelectItem unitUuid = new RequiredSelectItem(IndicatorDS.QUANTITY_UNIT_UUID, getConstants().indicQuantityUnit());
        unitUuid.setShowIfCondition(getFormItemShowIfApplicable());

        RequiredSelectItem unitMultiplier = new RequiredSelectItem(IndicatorDS.QUANTITY_UNIT_MULTIPLIER, getConstants().indicQuantityUnitMultiplier());
        unitMultiplier.setShowIfCondition(getFormItemShowIfApplicable());

        CustomIntegerItem sigDigits = new CustomIntegerItem(IndicatorDS.QUANTITY_SIGNIFICANT_DIGITS, getConstants().indicQuantitySignificantDigits());
        sigDigits.setShowIfCondition(getFormItemShowIfApplicable());

        CustomIntegerItem decPlaces = new CustomIntegerItem(IndicatorDS.QUANTITY_DECIMAL_PLACES, getConstants().indicQuantityDecimalPlaces());
        decPlaces.setRequired(true);
        decPlaces.setShowIfCondition(getFormItemShowIfApplicable());
        decPlaces.setValidators(getDecimalPlacesValidator());

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

        ViewTextItem isPercentange = new ViewTextItem(IndicatorDS.QUANTITY_IS_PERCENTAGE, getConstants().indicQuantityIsPercentage());
        isPercentange.setShowIfCondition(CommonUtils.getFalseIfFunction());

        ViewTextItem isPercentangeText = new ViewTextItem(IndicatorDS.QUANTITY_IS_PERCENTAGE_TEXT, getConstants().indicQuantityIsPercentage());
        isPercentangeText.setShowIfCondition(getIsPercentageIfFunction());

        MultiLanguageTextItem percentageOf = new MultiLanguageTextItem(IndicatorDS.QUANTITY_PERCENTAGE_OF, getConstants().indicQuantityPercentageOf());
        percentageOf.setShowIfCondition(getPercentageOfIfFunction());

        ViewTextItem baseQuantityIndUuid = new ViewTextItem(IndicatorDS.QUANTITY_BASE_QUANTITY_INDICATOR_UUID, getConstants().indicQuantityBaseQuantityIndicator());
        baseQuantityIndUuid.setVisible(false);

        setFields(staticMethodType, methodType, viewMethod, methodCalculated, viewMethodLoad, methodLoad, rounding, type, typeText, unitUuid, unitMultiplier, sigDigits, decPlaces, min, max,
                searchDenominatorUuid, searchDenominatorText, searchNumeratorUuid, searchNumeratorText, isPercentange, isPercentangeText, percentageOf, baseQuantityIndUuid);

        markForRedraw();
    }
    public void setMethodChangedHandler(ChangedHandler changedHandler) {
        getItem(DataSourceDS.RATE_DERIVATION_METHOD_LOAD).addChangedHandler(changedHandler);
        getItem(DataSourceDS.RATE_DERIVATION_METHOD_LOAD_VIEW).addChangedHandler(changedHandler);
        getItem(DataSourceDS.RATE_DERIVATION_METHOD_CALCULATED).addChangedHandler(changedHandler);
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
        setValue(DataSourceDS.RATE_DERIVATION_ROUNDING, rateDerivationDto.getRounding() != null ? rateDerivationDto.getRounding().toString() : RateDerivationRoundingEnum.UPWARD.toString());

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

        // Amount rates (PUNTUALS) have 0 decimal places by default. The others rates have 2 decimal places.
        Integer defaultDecimalPlaces = (RateDerivationTypeEnum.ANNUAL_PUNTUAL_RATE_TYPE.equals(this.rateDerivationTypeEnum) || RateDerivationTypeEnum.INTERPERIOD_PUNTUAL_RATE_TYPE
                .equals(this.rateDerivationTypeEnum)) ? 0 : 2;
        setValue(IndicatorDS.QUANTITY_DECIMAL_PLACES, quantityDto.getDecimalPlaces() != null ? quantityDto.getDecimalPlaces() : defaultDecimalPlaces);

        if (quantityDto.getMinimum() != null) {
            setValue(IndicatorDS.QUANTITY_MINIMUM, quantityDto.getMinimum());
        }
        if (quantityDto.getMaximum() != null) {
            setValue(IndicatorDS.QUANTITY_MAXIMUM, quantityDto.getMaximum());
        }

        setValue(IndicatorDS.QUANTITY_DENOMINATOR_INDICATOR_UUID, quantityDto.getDenominatorIndicatorUuid());
        setValue(IndicatorDS.QUANTITY_DENOMINATOR_INDICATOR_TEXT, quantityDto.getDenominatorIndicatorUuid()); // Value set in setDenominatorIndicator method

        setValue(IndicatorDS.QUANTITY_NUMERATOR_INDICATOR_UUID, quantityDto.getNumeratorIndicatorUuid());
        setValue(IndicatorDS.QUANTITY_NUMERATOR_INDICATOR_TEXT, quantityDto.getNumeratorIndicatorUuid()); // Value set in setNumeratorIndicator method

        setValue(IndicatorDS.QUANTITY_IS_PERCENTAGE, quantityDto.getIsPercentage() != null ? quantityDto.getIsPercentage().booleanValue() : false);
        setValue(IndicatorDS.QUANTITY_IS_PERCENTAGE_TEXT, quantityDto.getIsPercentage() != null ? (quantityDto.getIsPercentage().booleanValue()
                ? MetamacWebCommon.getConstants().yes()
                : MetamacWebCommon.getConstants().no()) : MetamacWebCommon.getConstants().no());

        setValue(IndicatorDS.QUANTITY_BASE_QUANTITY_INDICATOR_UUID, quantityDto.getBaseQuantityIndicatorUuid());
        setValue(IndicatorDS.QUANTITY_PERCENTAGE_OF, RecordUtils.getInternationalStringRecord(quantityDto.getPercentageOf()));
    }
    public void setNumeratorIndicator(IndicatorDto indicatorDto) {
        setValue(IndicatorDS.QUANTITY_NUMERATOR_INDICATOR_TEXT, CommonUtils.getIndicatorText(indicatorDto.getCode(), indicatorDto.getTitle()));
    }

    public void setDenominatorIndicator(IndicatorDto indicatorDto) {
        setValue(IndicatorDS.QUANTITY_DENOMINATOR_INDICATOR_TEXT, CommonUtils.getIndicatorText(indicatorDto.getCode(), indicatorDto.getTitle()));
    }

    public void setNumeratorIndicators(List<IndicatorSummaryDto> indicators) {
        if (indicatorNumeratorSearchWindow != null) {
            indicatorNumeratorSearchWindow.setIndicatorList(indicators);
        }
    }

    public void setDenominatorIndicators(List<IndicatorSummaryDto> indicators) {
        if (indicatorDenominatorSearchWindow != null) {
            indicatorDenominatorSearchWindow.setIndicatorList(indicators);
        }
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
            // keep old method
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

        quantityDto.setIsPercentage(getItem(IndicatorDS.QUANTITY_IS_PERCENTAGE_TEXT).isVisible() ? (getValue(IndicatorDS.QUANTITY_IS_PERCENTAGE) != null ? Boolean
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

    private CustomValidator getDecimalPlacesValidator() {
        CustomValidator validator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                if (value != null && value instanceof Integer) {
                    return ((Integer) value) <= 10;
                }
                return true;
            }
        };
        validator.setErrorMessage(getMessages().validatorErrorDecimalPlaces(String.valueOf(10)));
        return validator;
    }

    @Override
    public void setQuantityUnits(List<QuantityUnitDto> units) {
        super.setQuantityUnits(units);
        LinkedHashMap<String, String> valueMap = CommonUtils.getQuantityUnitsValueMap(units);
        ((SelectItem) getItem(IndicatorDS.QUANTITY_UNIT_UUID)).setValueMap(valueMap);
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
        // NOTE clear query dependent values (method and method type)
    }

    public void setMeasureVariableValues(LinkedHashMap<String, String> valueMap) {
        getItem(DataSourceDS.RATE_DERIVATION_METHOD_LOAD).setValueMap(valueMap);
    }

    public void setIsPercentage(boolean isPercentage) {
        setValue(IndicatorDS.QUANTITY_IS_PERCENTAGE, isPercentage ? true : false);
        setValue(IndicatorDS.QUANTITY_IS_PERCENTAGE_TEXT, isPercentage ? MetamacWebCommon.getConstants().yes() : MetamacWebCommon.getConstants().no());
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
                            ((IndicatorUiHandler) uiHandlers).searchRateIndicators(indicatorDenominatorSearchWindow.getSearchCriteria(), RateDerivationForm.this.rateDerivationTypeEnum,
                                    IndicatorCalculationTypeEnum.DENOMINATOR);
                        }
                    }
                });

                indicatorDenominatorSearchWindow.getAcceptButton().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                        IndicatorSummaryDto indicatorSummaryDto = indicatorDenominatorSearchWindow.getSelectedIndicator();
                        indicatorDenominatorSearchWindow.destroy();
                        RateDerivationForm.this.setValue(IndicatorDS.QUANTITY_DENOMINATOR_INDICATOR_UUID, indicatorSummaryDto != null ? indicatorSummaryDto.getUuid() : new String());
                        RateDerivationForm.this.setValue(IndicatorDS.QUANTITY_DENOMINATOR_INDICATOR_TEXT,
                                indicatorSummaryDto != null ? CommonUtils.getIndicatorText(indicatorSummaryDto.getCode(), CommonUtils.getIndicatorTitle(indicatorSummaryDto)) : new String());
                        RateDerivationForm.this.getItem(IndicatorDS.QUANTITY_DENOMINATOR_INDICATOR_TEXT).validate();
                    }
                });
            }
        });
        CustomValidator validator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                return RateDerivationForm.this.getItem(IndicatorDS.QUANTITY_DENOMINATOR_INDICATOR_UUID).validate();
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
                            ((IndicatorUiHandler) uiHandlers).searchRateIndicators(indicatorNumeratorSearchWindow.getSearchCriteria(), RateDerivationForm.this.rateDerivationTypeEnum,
                                    IndicatorCalculationTypeEnum.NUMERATOR);
                        }
                    }
                });

                indicatorNumeratorSearchWindow.getAcceptButton().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                        IndicatorSummaryDto indicatorSummaryDto = indicatorNumeratorSearchWindow.getSelectedIndicator();
                        indicatorNumeratorSearchWindow.destroy();
                        RateDerivationForm.this.setValue(IndicatorDS.QUANTITY_NUMERATOR_INDICATOR_UUID, indicatorSummaryDto != null ? indicatorSummaryDto.getUuid() : new String());
                        RateDerivationForm.this.setValue(IndicatorDS.QUANTITY_NUMERATOR_INDICATOR_TEXT,
                                indicatorSummaryDto != null ? CommonUtils.getIndicatorText(indicatorSummaryDto.getCode(), CommonUtils.getIndicatorTitle(indicatorSummaryDto)) : new String());
                        RateDerivationForm.this.getItem(IndicatorDS.QUANTITY_NUMERATOR_INDICATOR_TEXT).validate();
                    }
                });
            }
        });
        CustomValidator validator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                return RateDerivationForm.this.getItem(IndicatorDS.QUANTITY_NUMERATOR_INDICATOR_UUID).validate();
            }
        };
        validator.setErrorMessage(getMessages().validatorErrorIndicatorSelected());
        searchNumeratorText.setValidators(validator);

        return searchNumeratorText;
    }

    public void setUnitMultipliers(List<UnitMultiplierDto> unitMultiplierDtos) {
        ((RequiredSelectItem) getItem(IndicatorDS.QUANTITY_UNIT_MULTIPLIER)).setValueMap(CommonUtils.getUnitMultiplierValueMap(unitMultiplierDtos));
    }
}
