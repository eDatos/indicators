package es.gobcan.istac.indicators.core.serviceapi.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Date;

import org.joda.time.DateTime;
import org.siemac.metamac.core.common.dto.serviceapi.InternationalStringDto;
import org.siemac.metamac.core.common.dto.serviceapi.LocalisedStringDto;

import es.gobcan.istac.indicators.core.dto.serviceapi.DimensionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;

/**
 * Asserts to tests
 */
public class IndicatorsAsserts {
    
    public static void assertEqualsIndicatorsSystem(IndicatorsSystemDto expected, IndicatorsSystemDto actual) {
        assertEquals(expected.getCode(), actual.getCode());
        assertEqualsInternationalString(expected.getTitle(), actual.getTitle());
        assertEqualsInternationalString(expected.getAcronym(), actual.getAcronym());
        assertEquals(expected.getUriGopestat(), actual.getUriGopestat());
        assertEqualsInternationalString(expected.getObjetive(), actual.getObjetive());
        assertEqualsInternationalString(expected.getDescription(), actual.getDescription());
    }
    


    public static void assertEqualsDimension(DimensionDto expected, DimensionDto actual) {
        assertEqualsInternationalString(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getParentDimensionUuid(), actual.getParentDimensionUuid());
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
    
    public static void assertEqualsInternationalString(InternationalStringDto internationalStringDto, String locale1, String label1, String locale2, String label2) {
        int count = 0;
        if (locale1 != null) {
            assertEquals(label1, internationalStringDto.getLocalisedLabel(locale1));
            count++;
        } 
        if (locale2 != null) {
            assertEquals(label2, internationalStringDto.getLocalisedLabel(locale2));
            count++;
        }
        assertEquals(count, internationalStringDto.getTexts().size());
    }
    
    public static void assertEqualsDate(String expected, Date actual) {
        assertEquals(expected, (new DateTime(actual)).toString("yyyy-MM-dd HH:mm:ss"));
    }
}
