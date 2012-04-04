package es.gobcan.istac.indicators.web.client.utils;

import static es.gobcan.istac.indicators.core.constants.TimeVariableConstants.END;
import static es.gobcan.istac.indicators.core.constants.TimeVariableConstants.START;
import static es.gobcan.istac.indicators.core.constants.TimeVariableConstants.TIME_VALUE_PATTERN;
import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getMessages;

import com.smartgwt.client.widgets.form.validator.CustomValidator;


public class TimeVariableWebUtils {

    public static Boolean isTimeValue(String value) {
        return value.matches(START + TIME_VALUE_PATTERN + END);
    }
    
    public static CustomValidator getTimeCustomValidator() {
        CustomValidator validator = new CustomValidator() {
            @Override
            protected boolean condition(Object value) {
                return value != null ? isTimeValue(value.toString()) : false;
            }
        };
        validator.setErrorMessage(getMessages().errorTemporalValueFormat());
        return validator;
    }
    
}
