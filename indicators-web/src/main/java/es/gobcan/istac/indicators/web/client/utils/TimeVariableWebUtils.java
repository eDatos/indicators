package es.gobcan.istac.indicators.web.client.utils;

import static es.gobcan.istac.indicators.core.constants.TimeVariableConstants.END;
import static es.gobcan.istac.indicators.core.constants.TimeVariableConstants.START;
import static es.gobcan.istac.indicators.core.constants.TimeVariableConstants.TIME_VALUE_PATTERN;
import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getMessages;

import com.google.gwt.regexp.shared.RegExp;
import com.smartgwt.client.widgets.form.validator.CustomValidator;


public class TimeVariableWebUtils {

    private static RegExp patternTimeValue     = RegExp.compile(START + TIME_VALUE_PATTERN + END);


    /**
     * Checks if a time value is valid
     */
    public static Boolean isTimeValue(String value) {
        boolean matchFound = patternTimeValue.test(value);
        return Boolean.valueOf(matchFound);
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
