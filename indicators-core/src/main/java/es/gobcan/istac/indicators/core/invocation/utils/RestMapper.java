package es.gobcan.istac.indicators.core.invocation.utils;

import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.siemac.metamac.srm.core.stream.message.InternationalStringAvro;
import org.siemac.metamac.srm.core.stream.message.LocalisedStringAvro;

public class RestMapper {

    private RestMapper() {
        // nothing to do
    }

    public static InternationalString getInternationalStringFromInternationalStringAvro(InternationalStringAvro internationalStringAvro) {
        InternationalString result = new InternationalString();

        for (LocalisedStringAvro localisedStringAvro : internationalStringAvro.getLocalisedStrings()) {
            result.addText(localisedString(localisedStringAvro.getLocale(), localisedStringAvro.getLabel()));
        }
        return result;
    }

    private static LocalisedString localisedString(String locale, String label) {
        LocalisedString localisedStrings = new LocalisedString();
        localisedStrings.setLabel(label);
        localisedStrings.setLocale(locale);
        return localisedStrings;
    }

}
