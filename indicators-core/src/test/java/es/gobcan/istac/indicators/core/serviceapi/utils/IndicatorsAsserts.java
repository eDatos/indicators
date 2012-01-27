package es.gobcan.istac.indicators.core.serviceapi.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.siemac.metamac.core.common.dto.serviceapi.InternationalStringDto;
import org.siemac.metamac.core.common.dto.serviceapi.LocalisedStringDto;

import es.gobcan.istac.indicators.core.serviceapi.IndicatorSystemDto;

/**
 * Asserts to tests
 */
public class IndicatorsAsserts {
    
    public static void assertEqualsIndicatorSystem(IndicatorSystemDto expected, IndicatorSystemDto actual) {
        assertEquals(expected.getCode(), actual.getCode());
        assertEqualsInternationalString(expected.getTitle(), actual.getTitle());
        assertEqualsInternationalString(expected.getAcronym(), actual.getAcronym());
        assertEquals(expected.getUri(), actual.getUri());
        assertEqualsInternationalString(expected.getObjetive(), actual.getObjetive());
        assertEqualsInternationalString(expected.getDescription(), actual.getDescription());
    }
    
    public static void assertEqualsInternationalString(InternationalStringDto expected, InternationalStringDto actual) {
        if (actual == null && expected == null) {
            return;
        } else if ((actual != null && expected == null) || (actual == null && expected != null)) {
            fail();
        }        
        assertEquals(expected.getTexts().size(), actual.getTexts().size());
        for (LocalisedStringDto localisedStringDtoExpected : expected.getTexts()) {
            assertEquals(localisedStringDtoExpected.getLabel(), actual.getLocalisedLabel(localisedStringDtoExpected.getLocale()));
        }
    }
}
