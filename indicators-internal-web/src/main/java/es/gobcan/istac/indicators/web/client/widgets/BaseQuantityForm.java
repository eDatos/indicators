package es.gobcan.istac.indicators.web.client.widgets;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getCoreMessages;

import java.util.LinkedHashMap;

import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;

import com.gwtplatform.mvp.client.UiHandlers;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;

import es.gobcan.istac.indicators.core.dto.QuantityDto;
import es.gobcan.istac.indicators.core.dto.QuantityUnitDto;
import es.gobcan.istac.indicators.core.enume.domain.QuantityTypeEnum;
import es.gobcan.istac.indicators.core.util.shared.IndicatorUtils;
import es.gobcan.istac.indicators.web.client.IndicatorsValues;
import es.gobcan.istac.indicators.web.client.enums.QuantityIndexBaseTypeEnum;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS;

public class BaseQuantityForm extends GroupDynamicForm {

    protected UiHandlers uiHandlers;

    public BaseQuantityForm(String groupTitle) {
        super(groupTitle);
    }

    public void setUiHandlers(UiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    protected QuantityIndexBaseTypeEnum getIndexBaseTypeEnum(QuantityDto quantityDto) {
        String baseTime = quantityDto.getBaseTime();
        String baseLocation = quantityDto.getBaseLocationUuid();
        if (baseTime != null && !baseTime.isEmpty()) {
            return QuantityIndexBaseTypeEnum.BASE_TIME;
        } else if (baseLocation != null && !baseLocation.isEmpty()) {
            return QuantityIndexBaseTypeEnum.BASE_LOCATION;
        }
        return null;
    }

    protected String getIndexBaseType(QuantityDto quantityDto) {
        QuantityIndexBaseTypeEnum basetype = getIndexBaseTypeEnum(quantityDto);
        if (basetype != null) {
            return getCoreMessages().getString(getCoreMessages().quantityIndexBaseTypeEnum() + basetype.getName());
        }
        return "";
    }

    protected LinkedHashMap<String, String> getQuantityTypeValueMap() {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        for (QuantityTypeEnum type : QuantityTypeEnum.values()) {
            valueMap.put(type.toString(), getCoreMessages().getString(getCoreMessages().quantityTypeEnum() + type.getName()));
        }
        return valueMap;
    }

    protected LinkedHashMap<String, String> getQuantityIndexBaseTypeValueMap() {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(QuantityIndexBaseTypeEnum.BASE_LOCATION.toString(), getCoreMessages().quantityIndexBaseTypeEnumBASE_LOCATION());
        valueMap.put(QuantityIndexBaseTypeEnum.BASE_TIME.toString(), getCoreMessages().quantityIndexBaseTypeEnumBASE_TIME());
        return valueMap;
    }

    protected FormItemIfFunction getMinIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return showMinFunction(item, value, form);
            }
        };
    }

    protected boolean showMinFunction(FormItem item, Object value, DynamicForm form) {
        if (form.getValueAsString(IndicatorDS.QUANTITY_TYPE) != null && !form.getValueAsString(IndicatorDS.QUANTITY_TYPE).isEmpty()) {
            QuantityTypeEnum type = QuantityTypeEnum.valueOf(form.getValueAsString(IndicatorDS.QUANTITY_TYPE));
            if (IndicatorUtils.isMagnitudeOrExtension(type)) {
                return true;
            }
        }
        return false;
    }

    protected FormItemIfFunction getMaxIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return showMaxFunction(item, value, form);
            }
        };
    }

    protected boolean showMaxFunction(FormItem item, Object value, DynamicForm form) {
        if (form.getValueAsString(IndicatorDS.QUANTITY_TYPE) != null && !form.getValueAsString(IndicatorDS.QUANTITY_TYPE).isEmpty()) {
            QuantityTypeEnum type = QuantityTypeEnum.valueOf(form.getValueAsString(IndicatorDS.QUANTITY_TYPE));
            if (IndicatorUtils.isMagnitudeOrExtension(type)) {
                return true;
            }
        }
        return false;
    }

    protected FormItemIfFunction getDenominatorIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return showDenominatorFunction(item, value, form);
            }
        };
    }

    protected boolean showDenominatorFunction(FormItem item, Object value, DynamicForm form) {
        if (form.getValueAsString(IndicatorDS.QUANTITY_TYPE) != null && !form.getValueAsString(IndicatorDS.QUANTITY_TYPE).isEmpty()) {
            QuantityTypeEnum type = QuantityTypeEnum.valueOf(form.getValueAsString(IndicatorDS.QUANTITY_TYPE));
            if (IndicatorUtils.isFractionOrExtension(type)) {
                return true;
            }
        }
        return false;
    }

    protected FormItemIfFunction getNumeratorIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return showNumeratorFunction(item, value, form);
            }
        };
    }

    protected boolean showNumeratorFunction(FormItem item, Object value, DynamicForm form) {
        if (form.getValueAsString(IndicatorDS.QUANTITY_TYPE) != null && !form.getValueAsString(IndicatorDS.QUANTITY_TYPE).isEmpty()) {
            QuantityTypeEnum type = QuantityTypeEnum.valueOf(form.getValueAsString(IndicatorDS.QUANTITY_TYPE));
            if (IndicatorUtils.isFractionOrExtension(type)) {
                return true;
            }
        }
        return false;
    }

    protected FormItemIfFunction getIsPercentageIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return showIsPercentageFunction(item, value, form);
            }
        };
    }

    protected boolean showIsPercentageFunction(FormItem item, Object value, DynamicForm form) {
        if (form.getValueAsString(IndicatorDS.QUANTITY_TYPE) != null && !form.getValueAsString(IndicatorDS.QUANTITY_TYPE).isEmpty()) {
            QuantityTypeEnum type = QuantityTypeEnum.valueOf(form.getValueAsString(IndicatorDS.QUANTITY_TYPE));
            if (IndicatorUtils.isRatioOrExtension(type)) {
                return true;
            }
        }
        return false;
    }

    protected FormItemIfFunction getPercentageOfIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return showPercentageOfFunction(item, value, form);
            }
        };
    }

    protected boolean showPercentageOfFunction(FormItem item, Object value, DynamicForm form) {
        if (form.getValueAsString(IndicatorDS.QUANTITY_TYPE) != null && !form.getValueAsString(IndicatorDS.QUANTITY_TYPE).isEmpty()) {
            QuantityTypeEnum type = QuantityTypeEnum.valueOf(form.getValueAsString(IndicatorDS.QUANTITY_TYPE));
            if (IndicatorUtils.isRatioOrExtension(type)) {
                boolean isPercentage = (Boolean) form.getValue(IndicatorDS.QUANTITY_IS_PERCENTAGE);
                if (isPercentage) {
                    return true;
                }
            }
        }
        return false;
    }

    protected FormItemIfFunction getIndexBaseTypeIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (form.getValueAsString(IndicatorDS.QUANTITY_TYPE) != null && !form.getValueAsString(IndicatorDS.QUANTITY_TYPE).isEmpty()) {
                    QuantityTypeEnum type = QuantityTypeEnum.valueOf(form.getValueAsString(IndicatorDS.QUANTITY_TYPE));
                    if (IndicatorUtils.isIndexOrExtension(type)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    protected FormItemIfFunction getBaseValueIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (form.getValueAsString(IndicatorDS.QUANTITY_TYPE) != null && !form.getValueAsString(IndicatorDS.QUANTITY_TYPE).isEmpty()) {
                    QuantityTypeEnum type = QuantityTypeEnum.valueOf(form.getValueAsString(IndicatorDS.QUANTITY_TYPE));
                    if (IndicatorUtils.isIndexOrExtension(type)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    protected FormItemIfFunction getBaseTimeIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (form.getValueAsString(IndicatorDS.QUANTITY_TYPE) != null && !form.getValueAsString(IndicatorDS.QUANTITY_TYPE).isEmpty()) {
                    QuantityTypeEnum type = QuantityTypeEnum.valueOf(form.getValueAsString(IndicatorDS.QUANTITY_TYPE));
                    if (IndicatorUtils.isIndexOrExtension(type)) {
                        if (QuantityIndexBaseTypeEnum.BASE_TIME.toString().equals(BaseQuantityForm.this.getValueAsString(IndicatorDS.QUANTITY_INDEX_BASE_TYPE))) {
                            return true;
                        }
                    }
                }
                return false;
            }
        };
    }

    protected FormItemIfFunction getBaseLocationIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (form.getValueAsString(IndicatorDS.QUANTITY_TYPE) != null && !form.getValueAsString(IndicatorDS.QUANTITY_TYPE).isEmpty()) {
                    QuantityTypeEnum type = QuantityTypeEnum.valueOf(form.getValueAsString(IndicatorDS.QUANTITY_TYPE));
                    if (IndicatorUtils.isIndexOrExtension(type)) {
                        if (QuantityIndexBaseTypeEnum.BASE_LOCATION.toString().equals(BaseQuantityForm.this.getValueAsString(IndicatorDS.QUANTITY_INDEX_BASE_TYPE))) {
                            return true;
                        }
                    }
                }
                return false;
            }
        };
    }

    protected FormItemIfFunction getBaseQuantityIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (form.getValueAsString(IndicatorDS.QUANTITY_TYPE) != null && !form.getValueAsString(IndicatorDS.QUANTITY_TYPE).isEmpty()) {
                    QuantityTypeEnum type = QuantityTypeEnum.valueOf(form.getValueAsString(IndicatorDS.QUANTITY_TYPE));
                    if (IndicatorUtils.isChangeRateOrExtension(type)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    protected String getQuantityUnitTitle(String unitUuid) {
        if (unitUuid != null) {
            for (QuantityUnitDto unit : IndicatorsValues.getQuantityUnits()) {
                if (unitUuid.equals(unit.getUuid())) {
                    return InternationalStringUtils.getLocalisedString(unit.getTitle());
                }
            }
        }
        return new String();
    }

}
