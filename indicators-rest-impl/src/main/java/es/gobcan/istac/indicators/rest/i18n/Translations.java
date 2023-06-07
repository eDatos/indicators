package es.gobcan.istac.indicators.rest.i18n;

import es.gobcan.istac.indicators.core.conf.MetadataProperties;
import org.apache.commons.lang.LocaleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.util.Locale;

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

    @Autowired
    private MetadataProperties metadataProperties;

    private Locale defaultLocale;

    @PostConstruct
    public void setDefaultLocale() {
        defaultLocale = LocaleUtils.toLocale(metadataProperties.getDefaultInternationalizationLanguage());
    }

    public String get(String code) {
        return messageSource.getMessage(code, null, defaultLocale);
    }

    public String get(String code, Object[] objects) {
        return messageSource.getMessage(code, objects, defaultLocale);
    }
}
