package es.gobcan.istac.indicators.rest.i18n;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import static es.gobcan.istac.indicators.rest.constants.IndicatorsRestApiConstants.DEFAULT_LOCALE;

@Component
public class Translations {
    public static final String DIMENSIONS_GEOGRAPHIC_NAME = "indicators.rest.dimensions.name.geographic";
    public static final String DIMENSIONS_MEASURE_NAME = "indicators.rest.dimensions.name.measure";
    public static final String DIMENSIONS_TIME_NAME = "indicators.rest.dimensions.name.time";

    public static final String MEASURE_ABSOLUTE = "indicators.rest.measure.absolute";
    public static final String MEASURE_ANNUAL_PUNTUAL_RATE = "indicators.rest.measure.annual_rate";
    public static final String MEASURE_ANNUAL_PERCENTAGE_RATE = "indicators.rest.measure.annual_percentage_rate";
    public static final String MEASURE_INTERPERIOD_PUNTUAL_RATE = "indicators.rest.measure.interperiod_rate";
    public static final String MEASURE_INTERPERIOD_PERCENTAGE_RATE = "indicators.rest.measure.interperiod_percentage_rate";

    @Autowired
    private MessageSource messageSource;

    public String get(String code) {
        return messageSource.getMessage(code, null, DEFAULT_LOCALE);
    }

    public String get(String code, Object[] objects) {
        return messageSource.getMessage(code, objects, DEFAULT_LOCALE);
    }
}
