package es.gobcan.istac.indicators.rest.test.mocks;

import org.apache.commons.lang.RandomStringUtils;
import org.siemac.metamac.schema.common.v1_0.domain.InternationalString;
import org.siemac.metamac.schema.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.schema.common.v1_0.domain.LocalisedStringList;

public class MockUtil {

    private MockUtil() {
    }
    
    public static org.siemac.metamac.core.common.ent.domain.InternationalString createInternationalStringDomain() {
        org.siemac.metamac.core.common.ent.domain.InternationalString internationalString = new org.siemac.metamac.core.common.ent.domain.InternationalString();
        
        org.siemac.metamac.core.common.ent.domain.LocalisedString localisedStringEN = new org.siemac.metamac.core.common.ent.domain.LocalisedString();
        localisedStringEN.setLocale("en");
        localisedStringEN.setLabel(RandomStringUtils.randomAlphabetic(15));
        internationalString.addText(localisedStringEN);

        LocalisedString localisedStringES = new LocalisedString();
        localisedStringES.setLocale("es");
        localisedStringES.setLabel(RandomStringUtils.randomAlphabetic(15));
        internationalString.addText(localisedStringEN);
        return internationalString;
    }
    
    public static InternationalString createInternationalString() {
        LocalisedStringList localisedStringList = new LocalisedStringList();

        LocalisedString localisedStringEN = new LocalisedString();
        localisedStringEN.setLocale("en");
        localisedStringEN.setLabel(RandomStringUtils.randomAlphabetic(15));
        localisedStringList.getLocalisedString().add(localisedStringEN);

        LocalisedString localisedStringES = new LocalisedString();
        localisedStringES.setLocale("es");
        localisedStringES.setLabel(RandomStringUtils.randomAlphabetic(15));
        localisedStringList.getLocalisedString().add(localisedStringES);

        InternationalString internationalString = new InternationalString();
        internationalString.setLocalisedStrings(localisedStringList);
        return internationalString;
    }
}
