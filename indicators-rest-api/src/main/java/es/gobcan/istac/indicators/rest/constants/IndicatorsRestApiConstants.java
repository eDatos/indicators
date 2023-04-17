package es.gobcan.istac.indicators.rest.constants;

import java.util.Locale;

import org.apache.commons.lang.LocaleUtils;

public class IndicatorsRestApiConstants {

    private IndicatorsRestApiConstants() {

    }

    public static final String PROP_ATTRIBUTE_OBS_CONF = "OBS_CONF";

    public static final String DEFAULT                 = "__default__";
    public static final String DEFAULT_LANGUAGE        = "es"; // TODO sigue teniendo sentido esta constante?
    public static final Locale DEFAULT_LOCALE          = LocaleUtils.toLocale(DEFAULT_LANGUAGE); // TODO sigue teniendo sentido esta constante?
}
