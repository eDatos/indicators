package es.gobcan.istac.indicators.rest.mapper;

import java.util.LinkedHashMap;
import java.util.Map;

import org.siemac.metamac.schema.common.v1_0.domain.InternationalString;
import org.siemac.metamac.schema.common.v1_0.domain.LocalisedString;

public class MapperUtil {

    private static String DEFAULT          = "__default__";
    private static String DEFAULT_LANGUAGE = "es";

    public static Map<String, String> getDefaultLabel(Object defaultLabel) {
        if (defaultLabel == null) {
            return null;
        }

        Map<String, String> labels = new LinkedHashMap<String, String>();
        labels.put(DEFAULT, defaultLabel.toString());
        return labels;
    }

    public static Map<String, String> getLocalisedLabel(InternationalString internationalString) {
        if (internationalString == null || internationalString.getLocalisedStrings() == null || internationalString.getLocalisedStrings().getLocalisedString().size() == 0) {
            return null;
        }

        Map<String, String> labels = new LinkedHashMap<String, String>(internationalString.getLocalisedStrings().getLocalisedString().size() + 1);
        String defaultLabel = null;
        String defaultLabelLocale = null;
        for (LocalisedString localisedString : internationalString.getLocalisedStrings().getLocalisedString()) {
            labels.put(localisedString.getLocale(), localisedString.getLabel());
            if ((defaultLabel == null) || 
                (defaultLabel != null && localisedString.getLocale().equals(DEFAULT_LANGUAGE)) ||
                (defaultLabel != null && defaultLabelLocale.equals(DEFAULT_LANGUAGE) && localisedString.getLocale().startsWith(DEFAULT_LANGUAGE))) {
                defaultLabel = localisedString.getLabel();
                defaultLabelLocale = localisedString.getLocale();
            }
        }
        labels.put(DEFAULT, defaultLabel);
        return labels;
    }

    public static Map<String, String> getLocalisedLabel(org.siemac.metamac.core.common.ent.domain.InternationalString internationalString) {
        if (internationalString == null || internationalString.getTexts() == null || internationalString.getTexts().size() == 0) {
            return null;
        }

        Map<String, String> labels = new LinkedHashMap<String, String>(internationalString.getTexts().size() + 1);
        String defaultLabel = null;
        String defaultLabelLocale = null;
        for (org.siemac.metamac.core.common.ent.domain.LocalisedString localisedString : internationalString.getTexts()) {
            labels.put(localisedString.getLocale(), localisedString.getLabel());
            if ((defaultLabel == null) || 
                    (defaultLabel != null && localisedString.getLocale().equals(DEFAULT_LANGUAGE)) ||
                    (defaultLabel != null && defaultLabelLocale.equals(DEFAULT_LANGUAGE) && localisedString.getLocale().startsWith(DEFAULT_LANGUAGE))) {
                    defaultLabel = localisedString.getLabel();
                    defaultLabelLocale = localisedString.getLocale();
                }
        }
        labels.put(DEFAULT, defaultLabel);
        return labels;
    }
}
