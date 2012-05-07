package es.gobcan.istac.indicators.web.client.widgets;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;

import es.gobcan.istac.indicators.web.client.model.ds.DataSourceDS;

public class BaseRateDerivationForm extends BaseQuantityForm {

    public static final String NOT_APPLICABLE = "NOT_APPLICABLE";

    public BaseRateDerivationForm(String groupTitle) {
        super(groupTitle);

    }

    public boolean isRateNotApplicable() {
        String methodType = getValueAsString((DataSourceDS.RATE_DERIVATION_METHOD_TYPE));
        return NOT_APPLICABLE.equals(methodType);
    }

    protected FormItemIfFunction getFormItemShowIfApplicable() {
        FormItemIfFunction function = new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return showItemIfApplicable(item, value, form);
            }
        };
        return function;
    }

    protected boolean showItemIfApplicable(FormItem item, Object value, DynamicForm form) {
        String methodType = getValueAsString((DataSourceDS.RATE_DERIVATION_METHOD_TYPE));
        return !NOT_APPLICABLE.equals(methodType);
    }

    protected FormItemIfFunction getMinIfFunction() {
        FormItemIfFunction function = new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (showItemIfApplicable(item, value, form)) {
                    return showMinFunction(item, value, form);
                }
                return false;
            }
        };
        return function;
    }

    @Override
    protected FormItemIfFunction getMaxIfFunction() {
        FormItemIfFunction function = new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (showItemIfApplicable(item, value, form)) {
                    return showMaxFunction(item, value, form);
                }
                return false;
            }
        };
        return function;
    }

    @Override
    protected FormItemIfFunction getDenominatorIfFunction() {
        FormItemIfFunction function = new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (showItemIfApplicable(item, value, form)) {
                    return showDenominatorFunction(item, value, form);
                }
                return false;
            }
        };
        return function;
    }

    @Override
    protected FormItemIfFunction getNumeratorIfFunction() {
        FormItemIfFunction function = new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (showItemIfApplicable(item, value, form)) {
                    return showNumeratorFunction(item, value, form);
                }
                return false;
            }
        };
        return function;
    }

    @Override
    protected FormItemIfFunction getIsPercentageIfFunction() {
        FormItemIfFunction function = new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (showItemIfApplicable(item, value, form)) {
                    return showIsPercentageFunction(item, value, form);
                }
                return false;
            }
        };
        return function;
    }

    @Override
    protected FormItemIfFunction getPercentageOfIfFunction() {
        FormItemIfFunction function = new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (showItemIfApplicable(item, value, form)) {
                    return showPercentageOfFunction(item, value, form);
                }
                return false;
            }
        };
        return function;
    }

}
