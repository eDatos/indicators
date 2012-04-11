package es.gobcan.istac.indicators.core.serviceapi.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Date;

import org.joda.time.DateTime;
import org.siemac.metamac.common.test.utils.MetamacAsserts;
import org.siemac.metamac.core.common.dto.InternationalStringDto;

import com.arte.statistic.dataset.repository.dto.AttributeBasicDto;

import es.gobcan.istac.indicators.core.dto.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.DataSourceVariableDto;
import es.gobcan.istac.indicators.core.dto.DimensionDto;
import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.IndicatorInstanceDto;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.dto.QuantityDto;
import es.gobcan.istac.indicators.core.dto.RateDerivationDto;
import es.gobcan.istac.indicators.core.serviceimpl.util.ServiceUtils;

/**
 * Asserts to tests
 */
public class IndicatorsAsserts {
    
    public static void assertEqualsIndicatorsSystem(IndicatorsSystemDto expected, IndicatorsSystemDto actual) {
        assertEquals(expected.getCode(), actual.getCode());
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
        assertEqualsInternationalString(expected.getNotes(), actual.getNotes());
        assertEquals(expected.getNotesUrl(), actual.getNotesUrl());
        assertEqualsInternationalString(expected.getComments(), actual.getComments());
        assertEquals(expected.getCommentsUrl(), actual.getCommentsUrl());
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
        assertEquals(expected.getBaseLocationUuid(), actual.getBaseLocationUuid());
        assertEquals(expected.getBaseQuantityIndicatorUuid(), actual.getBaseQuantityIndicatorUuid());
    }
    
    public static void assertEqualsDataSource(DataSourceDto expected, DataSourceDto actual) {
        assertEquals(expected.getDataGpeUuid(), actual.getDataGpeUuid());
        assertEquals(expected.getPxUri(), actual.getPxUri());
        assertEquals(expected.getTimeVariable(), actual.getTimeVariable());
        assertEquals(expected.getTimeValue(), actual.getTimeValue());
        assertEquals(expected.getGeographicalVariable(), actual.getGeographicalVariable());
        assertEquals(expected.getGeographicalValueUuid(), actual.getGeographicalValueUuid());
        assertEquals(expected.getOtherVariables().size(), actual.getOtherVariables().size());
        for (DataSourceVariableDto expectedDataSourceVariable : expected.getOtherVariables()) {
            assertEquals(expectedDataSourceVariable.getCategory(), actual.getOtherVariable(expectedDataSourceVariable.getVariable()).getCategory());
        }
        assertEqualsRateDerivation(expected.getAnnualPuntualRate(), actual.getAnnualPuntualRate());
        assertEqualsRateDerivation(expected.getAnnualPercentageRate(), actual.getAnnualPercentageRate());
        assertEqualsRateDerivation(expected.getInterperiodPuntualRate(), actual.getInterperiodPuntualRate());
        assertEqualsRateDerivation(expected.getInterperiodPercentageRate(), actual.getInterperiodPercentageRate());
        assertEquals(expected.getSourceSurveyCode(), actual.getSourceSurveyCode());
        assertEqualsInternationalString(expected.getSourceSurveyTitle(), actual.getSourceSurveyTitle());
        assertEqualsInternationalString(expected.getSourceSurveyAcronym(), actual.getSourceSurveyAcronym());
        assertEquals(expected.getSourceSurveyUrl(), actual.getSourceSurveyUrl());
        assertEquals(expected.getPublishers().size(), actual.getPublishers().size());
        assertEquals(ServiceUtils.dtoList2DtoString(expected.getPublishers()), ServiceUtils.dtoList2DtoString(actual.getPublishers()));
    }
    
    private static void assertEqualsRateDerivation(RateDerivationDto expected, RateDerivationDto actual) {
        if (expected == null && actual == null) {
            return;
        } else if (expected != null && actual != null) {
            assertEquals(expected.getMethodType(), actual.getMethodType());
            assertEquals(expected.getMethod(), actual.getMethod());
            assertEquals(expected.getRounding(), actual.getRounding());
            assertEqualsQuantity(expected.getQuantity(), actual.getQuantity());
        } else {
            fail("Rates are different");
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
        assertEquals(expected.getGeographicalGranularityUuid(), actual.getGeographicalGranularityUuid());
        assertEquals(expected.getGeographicalValueUuid(), actual.getGeographicalValueUuid());
        assertEquals(expected.getTimeGranularity(), actual.getTimeGranularity());
        assertEquals(expected.getTimeValue(), actual.getTimeValue());
    }
    
    public static void assertEqualsAttributeBasic(AttributeBasicDto expected, AttributeBasicDto actual) {
        if (expected == actual) {
            return;
        }
        if (expected == null) {
            fail("Expected is null, actual isn't");
        } else if (actual == null) {
            fail("Expected is not null, actual is");
        }
        assertEquals(expected.getAttributeId(),actual.getAttributeId());
        DatasetRepositoryAsserts.assertEqualsInternationalString(expected.getValue(), actual.getValue());
    }
    

}
