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
import es.gobcan.istac.indicators.core.dto.serviceapi.QuantityDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.RateDerivationDto;

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
        assertEqualsInternationalString(expected.getSubjectTitle(), actual.getSubjectTitle());
        assertEqualsInternationalString(expected.getTitle(), actual.getTitle());
        assertEqualsInternationalString(expected.getAcronym(), actual.getAcronym());
        assertEquals(expected.getNotesUrl(), actual.getNotesUrl());
        assertEqualsInternationalString(expected.getNotes(), actual.getNotes());
        assertEqualsInternationalString(expected.getComments(), actual.getComments());
        assertEqualsInternationalString(expected.getConceptDescription(), actual.getConceptDescription());
        assertEqualsQuantity(expected.getQuantity(), actual.getQuantity());
    }
    
    public static void assertEqualsQuantity(QuantityDto expected, QuantityDto actual) {
        assertEquals(expected.getType(), actual.getType());
        assertEquals(expected.getUnitUuid(), actual.getUnitUuid());
        assertEquals(expected.getUnitMultiplier(), actual.getUnitMultiplier());
        assertEquals(expected.getSignificantDigits(), actual.getSignificantDigits());
        assertEquals(expected.getDecimalPlaces(), actual.getDecimalPlaces());
        assertEquals(expected.getMinimum(), actual.getMinimum());
        assertEquals(expected.getMaximum(), actual.getMaximum());
        assertEquals(expected.getNumeratorIndicatorUuid(), actual.getNumeratorIndicatorUuid());
        assertEquals(expected.getDenominatorIndicatorUuid(), actual.getDenominatorIndicatorUuid());
        assertEquals(expected.getIsPercentage(), actual.getIsPercentage());
        assertEqualsInternationalString(expected.getPercentageOf(), actual.getPercentageOf());
        assertEquals(expected.getBaseValue(), actual.getBaseValue());
        assertEquals(expected.getBaseTime(), actual.getBaseTime());
        assertEquals(expected.getBaseLocation(), actual.getBaseLocation());
        assertEquals(expected.getBaseQuantityIndicatorUuid(), actual.getBaseQuantityIndicatorUuid());
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
        assertEqualsRateDerivation(expected.getInterperiodRate(), actual.getInterperiodRate());
        assertEqualsRateDerivation(expected.getAnnualRate(), actual.getAnnualRate());
    }
    
    private static void assertEqualsRateDerivation(RateDerivationDto expected, RateDerivationDto actual) {
        assertEquals(expected.getMethodType(), actual.getMethodType());
        assertEquals(expected.getMethod(), actual.getMethod());
        assertEquals(expected.getRounding(), actual.getRounding());
        assertEqualsQuantity(expected.getQuantity(), actual.getQuantity());
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
        assertEquals(expected.getGeographicGranularityId(), actual.getGeographicGranularityId());
        assertEquals(expected.getGeographicValue(), actual.getGeographicValue());
        assertEquals(expected.getTemporaryGranularityId(), actual.getTemporaryGranularityId());
        assertEquals(expected.getTemporaryValue(), actual.getTemporaryValue());
    }
}
