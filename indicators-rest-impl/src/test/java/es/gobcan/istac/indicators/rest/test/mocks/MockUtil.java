package es.gobcan.istac.indicators.rest.test.mocks;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;

public class MockUtil {

    private MockUtil() {
    }
    
    public static org.siemac.metamac.core.common.ent.domain.InternationalString createInternationalStringDomain() {
        org.siemac.metamac.core.common.ent.domain.InternationalString internationalString = new org.siemac.metamac.core.common.ent.domain.InternationalString();
        
        org.siemac.metamac.core.common.ent.domain.LocalisedString localisedStringEN = new org.siemac.metamac.core.common.ent.domain.LocalisedString();
        localisedStringEN.setLocale("en");
        localisedStringEN.setLabel(RandomStringUtils.randomAlphabetic(15));
        internationalString.addText(localisedStringEN);

        org.siemac.metamac.core.common.ent.domain.LocalisedString localisedStringES = new org.siemac.metamac.core.common.ent.domain.LocalisedString();
        localisedStringES.setLocale("es");
        localisedStringES.setLabel(RandomStringUtils.randomAlphabetic(15));
        internationalString.addText(localisedStringES);
        return internationalString;
    }
    
    public static InternationalString createInternationalString() {
        InternationalString internationalString = new InternationalString();

        LocalisedString localisedStringEN = new LocalisedString();
        localisedStringEN.setLang("en");
        localisedStringEN.setValue(RandomStringUtils.randomAlphabetic(15));
        internationalString.getTexts().add(localisedStringEN);

        LocalisedString localisedStringES = new LocalisedString();
        localisedStringES.setLang("es");
        localisedStringES.setValue(RandomStringUtils.randomAlphabetic(15));
        internationalString.getTexts().add(localisedStringES);

        return internationalString;
    }
    
    public static Map<String,String> createInternationalStringMap() {
        Map<String,String> internationalMap = new HashMap<String, String>();

        internationalMap.put("en", RandomStringUtils.randomAlphabetic(15));
        internationalMap.put("es",RandomStringUtils.randomAlphabetic(15));
        
        return internationalMap;
    }
}
