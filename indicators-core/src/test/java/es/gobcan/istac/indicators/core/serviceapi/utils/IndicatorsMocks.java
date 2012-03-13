package es.gobcan.istac.indicators.core.serviceapi.utils;

import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.core.common.dto.serviceapi.InternationalStringDto;
import org.siemac.metamac.core.common.dto.serviceapi.LocalisedStringDto;

/**
 * Mocks
 */
public class IndicatorsMocks {

    /**
     * Mock a simple String
     */
    public static String mockString(int count) {
        return MetamacMocks.mockString(count);
    }
    
    /**
     * Mock a InternationalString with locales "en" and "es"
     */
    public static InternationalStringDto mockInternationalString() {
        return MetamacMocks.mockInternationalString();
    }
    
    /**
     * Mock a InternationalString with one locale
     */
    public static InternationalStringDto mockInternationalString(String locale, String label) {
        InternationalStringDto target = new InternationalStringDto();
        LocalisedStringDto localisedStringDto = new LocalisedStringDto();
        localisedStringDto.setLocale(locale);
        localisedStringDto.setLabel(label);
        target.addText(localisedStringDto);
        return target;
    }
}
