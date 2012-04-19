package es.gobcan.istac.indicators.rest.mapper;

import java.util.HashMap;
import java.util.Map;

import org.siemac.metamac.schema.common.v1_0.domain.InternationalString;
import org.siemac.metamac.schema.common.v1_0.domain.LocalisedString;

public class MapperUtil {

    public static Map<String, String> getLocalisedLabel(InternationalString internationalString) {
        if (internationalString == null || internationalString.getLocalisedStrings() == null) {
            return null;
        }

        Map<String, String> labels = new HashMap<String, String>();
        for (LocalisedString localisedString : internationalString.getLocalisedStrings().getLocalisedString()) {
            labels.put(localisedString.getLocale(), localisedString.getLabel());
        }
        return labels;
    }
    
    public static Map<String, String> getLocalisedLabel(org.siemac.metamac.core.common.ent.domain.InternationalString internationalString) {
        if (internationalString == null || internationalString.getTexts() == null) {
            return null;
        }

        Map<String, String> labels = new HashMap<String, String>();
        for (org.siemac.metamac.core.common.ent.domain.LocalisedString localisedString : internationalString.getTexts()) {
            labels.put(localisedString.getLocale(), localisedString.getLabel());
        }
        return labels;
    }
}
