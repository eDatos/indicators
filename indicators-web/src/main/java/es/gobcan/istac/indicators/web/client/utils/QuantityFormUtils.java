package es.gobcan.istac.indicators.web.client.utils;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;

import es.gobcan.istac.indicators.core.enume.domain.QuantityTypeEnum;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS;


public class QuantityFormUtils {

    // 
    // FormItemIfFunctions
    //
    
    public static FormItemIfFunction getMinIfFunction() {
        return new FormItemIfFunction() {
            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (form.getValueAsString(IndicatorDS.QUANTITY_TYPE) != null) {
                    QuantityTypeEnum type = QuantityTypeEnum.valueOf(form.getValueAsString(IndicatorDS.QUANTITY_TYPE));
                    if (QuantityTypeEnum.MAGNITUDE.equals(type) ||
                            QuantityTypeEnum.FRACTION.equals(type) ||
                            QuantityTypeEnum.RATIO.equals(type) ||
                            QuantityTypeEnum.RATE.equals(type) ||
                            QuantityTypeEnum.INDEX.equals(type) ||
                            QuantityTypeEnum.CHANGE_RATE.equals(type)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }
    
    public static FormItemIfFunction getMaxIfFunction() {
        return new FormItemIfFunction() {
            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (form.getValueAsString(IndicatorDS.QUANTITY_TYPE) != null) {
                    QuantityTypeEnum type = QuantityTypeEnum.valueOf(form.getValueAsString(IndicatorDS.QUANTITY_TYPE));
                    if (QuantityTypeEnum.MAGNITUDE.equals(type) ||
                            QuantityTypeEnum.FRACTION.equals(type) ||
                            QuantityTypeEnum.RATIO.equals(type) ||
                            QuantityTypeEnum.RATE.equals(type) ||
                            QuantityTypeEnum.INDEX.equals(type) ||
                            QuantityTypeEnum.CHANGE_RATE.equals(type)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }
    
    public static FormItemIfFunction getDenominatorIfFunction() {
        return new FormItemIfFunction() {
            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (form.getValueAsString(IndicatorDS.QUANTITY_TYPE) != null) {
                    QuantityTypeEnum type = QuantityTypeEnum.valueOf(form.getValueAsString(IndicatorDS.QUANTITY_TYPE));
                    if (QuantityTypeEnum.FRACTION.equals(type) ||
                            QuantityTypeEnum.RATIO.equals(type) ||
                            QuantityTypeEnum.RATE.equals(type) ||
                            QuantityTypeEnum.INDEX.equals(type) ||
                            QuantityTypeEnum.CHANGE_RATE.equals(type)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }
    
    public static FormItemIfFunction getNumeratorIfFunction() {
        return new FormItemIfFunction() {
            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (form.getValueAsString(IndicatorDS.QUANTITY_TYPE) != null) {
                    QuantityTypeEnum type = QuantityTypeEnum.valueOf(form.getValueAsString(IndicatorDS.QUANTITY_TYPE));
                    if (QuantityTypeEnum.FRACTION.equals(type) ||
                            QuantityTypeEnum.RATIO.equals(type) ||
                            QuantityTypeEnum.RATE.equals(type) ||
                            QuantityTypeEnum.INDEX.equals(type) ||
                            QuantityTypeEnum.CHANGE_RATE.equals(type)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }
    
    public static FormItemIfFunction getIsPercentageIfFunction() {
        return new FormItemIfFunction() {
            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (form.getValueAsString(IndicatorDS.QUANTITY_TYPE) != null) {
                    QuantityTypeEnum type = QuantityTypeEnum.valueOf(form.getValueAsString(IndicatorDS.QUANTITY_TYPE));
                    if (QuantityTypeEnum.RATIO.equals(type) ||
                            QuantityTypeEnum.RATE.equals(type) ||
                            QuantityTypeEnum.INDEX.equals(type) ||
                            QuantityTypeEnum.CHANGE_RATE.equals(type)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }
    
    public static FormItemIfFunction getPercentageOfFunction() {
        return new FormItemIfFunction() {
            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (form.getValueAsString(IndicatorDS.QUANTITY_TYPE) != null) {
                    QuantityTypeEnum type = QuantityTypeEnum.valueOf(form.getValueAsString(IndicatorDS.QUANTITY_TYPE));
                    if (QuantityTypeEnum.RATIO.equals(type) ||
                            QuantityTypeEnum.RATE.equals(type) ||
                            QuantityTypeEnum.INDEX.equals(type) ||
                            QuantityTypeEnum.CHANGE_RATE.equals(type)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }
    
    public static FormItemIfFunction getBaseValueFunction() {
        return new FormItemIfFunction() {
            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (form.getValueAsString(IndicatorDS.QUANTITY_TYPE) != null) {
                    QuantityTypeEnum type = QuantityTypeEnum.valueOf(form.getValueAsString(IndicatorDS.QUANTITY_TYPE));
                    if (QuantityTypeEnum.INDEX.equals(type) ||
                            QuantityTypeEnum.CHANGE_RATE.equals(type)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }
    
    public static FormItemIfFunction getBaseTimeFunction() {
        return new FormItemIfFunction() {
            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (form.getValueAsString(IndicatorDS.QUANTITY_TYPE) != null) {
                    QuantityTypeEnum type = QuantityTypeEnum.valueOf(form.getValueAsString(IndicatorDS.QUANTITY_TYPE));
                    if (QuantityTypeEnum.INDEX.equals(type) ||
                            QuantityTypeEnum.CHANGE_RATE.equals(type)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }
    
    public static FormItemIfFunction getBaseLocationFunction() {
        return new FormItemIfFunction() {
            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (form.getValueAsString(IndicatorDS.QUANTITY_TYPE) != null) {
                    QuantityTypeEnum type = QuantityTypeEnum.valueOf(form.getValueAsString(IndicatorDS.QUANTITY_TYPE));
                    if (QuantityTypeEnum.INDEX.equals(type) ||
                            QuantityTypeEnum.CHANGE_RATE.equals(type)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }
    
    public static FormItemIfFunction getBaseQuantityFunction() {
        return new FormItemIfFunction() {
            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (form.getValueAsString(IndicatorDS.QUANTITY_TYPE) != null) {
                    QuantityTypeEnum type = QuantityTypeEnum.valueOf(form.getValueAsString(IndicatorDS.QUANTITY_TYPE));
                    if (QuantityTypeEnum.CHANGE_RATE.equals(type)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }
    
}
