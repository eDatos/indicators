package es.gobcan.istac.indicators.core.serviceapi.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.joda.time.DateTime;
import org.siemac.metamac.common.test.utils.MetamacAsserts;
import org.siemac.metamac.core.common.dto.InternationalStringDto;

import com.arte.statistic.dataset.repository.dto.AttributeInstanceObservationDto;

import es.gobcan.istac.indicators.core.dto.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.DataSourceVariableDto;
import es.gobcan.istac.indicators.core.dto.DimensionDto;
import es.gobcan.istac.indicators.core.dto.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.dto.GeographicalValueBaseDto;
import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;
import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.IndicatorInstanceDto;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.dto.QuantityDto;
import es.gobcan.istac.indicators.core.dto.QuantityUnitDto;
import es.gobcan.istac.indicators.core.dto.RateDerivationDto;
import es.gobcan.istac.indicators.core.dto.UnitMultiplierDto;
import es.gobcan.istac.indicators.core.serviceimpl.util.ServiceUtils;

/**
 * Asserts to tests
 */
public class IndicatorsAsserts extends MetamacAsserts {

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
        assertEquals(expected.getViewCode(), actual.getViewCode());
        assertEquals(expected.getSubjectCode(), actual.getSubjectCode());
        assertEqualsInternationalString(expected.getSubjectTitle(), actual.getSubjectTitle());
        assertEqualsInternationalString(expected.getTitle(), actual.getTitle());
        assertEqualsInternationalString(expected.getAcronym(), actual.getAcronym());
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
        assertEquals(expected.getBaseLocationUuid(), actual.getBaseLocationUuid());
        assertEquals(expected.getBaseQuantityIndicatorUuid(), actual.getBaseQuantityIndicatorUuid());
    }

    public static void assertEqualsDataSource(DataSourceDto expected, DataSourceDto actual) {
        assertEquals(expected.getQueryUuid(), actual.getQueryUuid());
        assertEquals(expected.getQueryUrn(), actual.getQueryUrn());
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
        assertEqualsInternationalStringDto(expected, actual);
    }

    public static void assertEqualsInternationalString(InternationalStringDto internationalStringDto, String locale1, String label1, String locale2, String label2) {
        assertEqualsInternationalStringDto(internationalStringDto, locale1, label1, locale2, label2);
    }

    public static void assertEqualsDate(String expected, Date actual) {
        assertEquals(expected, (new DateTime(actual)).toString("yyyy-MM-dd HH:mm:ss"));
    }

    public static void assertEqualsIndicatorInstance(IndicatorInstanceDto expected, IndicatorInstanceDto actual) {
        assertEquals(expected.getIndicatorUuid(), actual.getIndicatorUuid());
        assertEqualsInternationalString(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getGeographicalGranularityUuid(), actual.getGeographicalGranularityUuid());
        assertEqualsGeographicalValuesBase(expected.getGeographicalValues(), actual.getGeographicalValues());
        assertEquals(expected.getTimeGranularity(), actual.getTimeGranularity());
        assertStringCollectionEquals(expected.getTimeValues(), actual.getTimeValues());
    }

    public static void assertEqualsCreatedGeographicalValueDto(GeographicalValueDto expected, GeographicalValueDto actual) {
        assertEquals(expected.getCode(), actual.getCode());
        assertEquals(expected.getOrder(), actual.getOrder());
        assertEqualsInternationalString(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getLatitude(), actual.getLatitude());
        assertEquals(expected.getLongitude(), actual.getLongitude());
        assertEquals(expected.getGranularity().getUuid(), actual.getGranularity().getUuid());
    }

    public static void assertEqualsGeographicalValueDto(GeographicalValueDto expected, GeographicalValueDto actual) {
        assertEqualsCreatedGeographicalValueDto(expected, actual);
        assertEquals(expected.getUuid(), actual.getUuid());
        assertEquals(expected.getCreatedDate(), actual.getCreatedDate());
        assertEquals(expected.getCreatedBy(), actual.getCreatedBy());
    }

    public static void assertEqualsCreatedQuantityUnitDto(QuantityUnitDto expected, QuantityUnitDto actual) {
        assertEqualsInternationalString(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getSymbol(), actual.getSymbol());
        assertEquals(expected.getSymbolPosition(), actual.getSymbolPosition());
    }

    public static void assertEqualsQuantityUnitDto(QuantityUnitDto expected, QuantityUnitDto actual) {
        assertEqualsCreatedQuantityUnitDto(expected, actual);
        assertEquals(expected.getUuid(), actual.getUuid());
        assertEquals(expected.getCreatedDate(), actual.getCreatedDate());
        assertEquals(expected.getCreatedBy(), actual.getCreatedBy());
    }

    public static void assertEqualsCreatedUnitMultiplierDto(UnitMultiplierDto expected, UnitMultiplierDto actual) {
        assertEquals(expected.getUnitMultiplier(), actual.getUnitMultiplier());
        assertEqualsInternationalString(expected.getTitle(), actual.getTitle());
    }

    public static void assertEqualsUnitMultiplierDto(UnitMultiplierDto expected, UnitMultiplierDto actual) {
        assertEqualsCreatedUnitMultiplierDto(expected, actual);
        assertEquals(expected.getUuid(), actual.getUuid());
        assertEquals(expected.getCreatedDate(), actual.getCreatedDate());
        assertEquals(expected.getCreatedBy(), actual.getCreatedBy());
    }

    public static void assertEqualsCreatedGeographicalGranularityDto(GeographicalGranularityDto expected, GeographicalGranularityDto actual) {
        assertEquals(expected.getCode(), actual.getCode());
        assertEqualsInternationalString(expected.getTitle(), actual.getTitle());
    }

    public static void assertEqualsGeographicalGranularityDto(GeographicalGranularityDto expected, GeographicalGranularityDto actual) {
        assertEqualsCreatedGeographicalGranularityDto(expected, actual);
        assertEquals(expected.getUuid(), actual.getUuid());
        assertEquals(expected.getCreatedDate(), actual.getCreatedDate());
        assertEquals(expected.getCreatedBy(), actual.getCreatedBy());
    }

    public static void assertEqualsAttributeBasic(AttributeInstanceObservationDto expected, AttributeInstanceObservationDto actual) {
        if (expected == actual) {
            return;
        }
        if (expected == null) {
            fail("Expected is null, actual isn't:" + actual.getValue());
        } else if (actual == null) {
            fail("Expected is not null, actual is");
        }
        assertEquals(expected.getAttributeId(), actual.getAttributeId());
        DatasetRepositoryAsserts.assertEqualsInternationalString(expected.getValue(), actual.getValue());
    }

    private static void assertEqualsGeographicalValuesBase(Collection<GeographicalValueBaseDto> expected, Collection<GeographicalValueBaseDto> actual) {
        if (expected == null) {
            if (actual == null) {
                assertNull(actual);
            } else {
                assertEquals(0, actual.size());
            }
        } else {
            assertNotNull(actual);

            Set<String> uuidsExpected = getGeographicalValueBaseDtoUuids(expected);
            Set<String> uuidsActual = getGeographicalValueBaseDtoUuids(actual);

            assertStringCollectionEquals(uuidsExpected, uuidsActual);
        }
    }

    private static Set<String> getGeographicalValueBaseDtoUuids(Collection<GeographicalValueBaseDto> collection) {
        Set<String> uuids = new HashSet<String>();
        if (collection != null) {
            for (GeographicalValueBaseDto geoValueBase : collection) {
                uuids.add(geoValueBase.getUuid());
            }
        }
        return uuids;
    }

    private static void assertStringCollectionEquals(Collection<String> expected, Collection<String> actual) {
        if (expected == null) {
            assertNull(actual);
        } else {
            assertNotNull(actual);
            assertEquals(expected.size(), actual.size());

            for (String str : expected) {
                assertTrue(actual.contains(str));
            }
        }
    }

}
