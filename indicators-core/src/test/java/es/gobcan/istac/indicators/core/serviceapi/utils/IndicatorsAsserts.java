package es.gobcan.istac.indicators.core.serviceapi.utils;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.joda.time.DateTime;
import org.siemac.metamac.common.test.utils.MetamacAsserts;
import org.siemac.metamac.core.common.dto.serviceapi.InternationalStringDto;

import es.gobcan.istac.indicators.core.dto.serviceapi.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.DataSourceVariableDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.DimensionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorInstanceDto;
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
        assertEquals(expected.getParentUuid(), actual.getParentUuid());
        assertEquals(expected.getOrderInLevel(), actual.getOrderInLevel());
    }
    
    public static void assertEqualsIndicator(IndicatorDto expected, IndicatorDto actual) {
        assertEquals(expected.getCode(), actual.getCode());
        assertEquals(expected.getSubjectCode(), actual.getSubjectCode());
        assertEqualsInternationalString(expected.getName(), actual.getName());
        assertEqualsInternationalString(expected.getAcronym(), actual.getAcronym());
        assertEquals(expected.getNoteUrl(), actual.getNoteUrl());
        assertEqualsInternationalString(expected.getNotes(), actual.getNotes());
        assertEqualsInternationalString(expected.getCommentary(), actual.getCommentary());
    }
    
    public static void assertEqualsDataSource(DataSourceDto expected, DataSourceDto actual) {
        assertEquals(expected.getQueryGpe(), actual.getQueryGpe());
        assertEquals(expected.getPx(), actual.getPx());
        assertEquals(expected.getTemporaryVariable(), actual.getTemporaryVariable());
        assertEquals(expected.getGeographicVariable(), actual.getGeographicVariable());
        assertEquals(expected.getOtherVariables().size(), actual.getOtherVariables().size());
        
        for (DataSourceVariableDto expectedDataSourceVariable : expected.getOtherVariables()) {
            assertEquals(expectedDataSourceVariable.getCategory(), actual.getOtherVariable(expectedDataSourceVariable.getVariable()).getCategory());
        }
    }
    
    public static void assertEqualsInternationalString(InternationalStringDto expected, InternationalStringDto actual) {
        MetamacAsserts.assertEqualsInternationalString(expected, actual);
    }
    
    public static void assertEqualsInternationalString(InternationalStringDto internationalStringDto, String locale1, String label1, String locale2, String label2) {
        MetamacAsserts.assertEqualsInternationalString(internationalStringDto, locale1, label1, locale2, label2);
    }
    
    public static void assertEqualsDate(String expected, Date actual) {
        assertEquals(expected, (new DateTime(actual)).toString("yyyy-MM-dd HH:mm:ss"));
    }

    public static void assertEqualsIndicatorInstance(IndicatorInstanceDto expected, IndicatorInstanceDto actual) {
        assertEquals(expected.getIndicatorUuid(), actual.getIndicatorUuid());
        assertEqualsInternationalString(expected.getTitle(), actual.getTitle());
    }
}
