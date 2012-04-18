package es.gobcan.istac.indicators.core.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.siemac.metamac.core.common.exception.MetamacException;

import com.arte.statistic.dataset.repository.dto.AttributeBasicDto;
import com.arte.statistic.dataset.repository.dto.CodeDimensionDto;
import com.arte.statistic.dataset.repository.dto.ConditionDimensionDto;
import com.arte.statistic.dataset.repository.dto.ConditionObservationDto;
import com.arte.statistic.dataset.repository.dto.InternationalStringDto;
import com.arte.statistic.dataset.repository.dto.LocalisedStringDto;
import com.arte.statistic.dataset.repository.dto.ObservationDto;
import com.arte.statistic.dataset.repository.dto.ObservationExtendedDto;
import com.arte.statistic.dataset.repository.service.DatasetRepositoriesServiceFacade;
import com.arte.statistic.dataset.repository.util.DtoUtils;

import es.gobcan.istac.indicators.core.domain.GeographicalGranularity;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorDataDimensionTypeEnum;
import es.gobcan.istac.indicators.core.serviceapi.utils.IndicatorsAsserts;

public abstract class IndicatorsDataBaseTest extends IndicatorsBaseTest {

    protected static final String ANNUAL_PERCENTAGE_RATE      = "ANNUAL_PERCENTAGE_RATE";
    protected static final String ANNUAL_PUNTUAL_RATE         = "ANNUAL_PUNTUAL_RATE";
    protected static final String INTERPERIOD_PERCENTAGE_RATE = "INTERPERIOD_PERCENTAGE_RATE";
    protected static final String INTERPERIOD_PUNTUAL_RATE    = "INTERPERIOD_PUNTUAL_RATE";

    protected abstract IndicatorsService getIndicatorsService();
    protected abstract DatasetRepositoriesServiceFacade getDatasetRepositoriesServiceFacade();

    protected static Date createDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        return cal.getTime();
    }

    protected void checkElementsInCollection(String[] expected, Collection<List<String>> collection) {
        List<String> values = new ArrayList<String>();
        for (List<String> vals : collection) {
            values.addAll(vals);
        }
        checkElementsInCollection(expected, values);
    }
    protected void checkElementsInCollection(String[] expected, List<String> collection) {
        for (String elem : expected) {
            assertTrue("Element "+elem+" not in collection",collection.contains(elem));
        }
        assertEquals(expected.length, collection.size());
    }

    protected void assertIndicatorEmptyData(String indicatorUuid, String indicatorVersionNumber) throws MetamacException {
        IndicatorVersion indicatorVersion = getIndicatorsService().retrieveIndicator(getServiceContext(), indicatorUuid, indicatorVersionNumber);
        assertNotNull(indicatorVersion);
        assertNull(indicatorVersion.getDataRepositoryId());
    }

    protected void checkDataDimensions(Map<String, List<String>> dimCodes, String indicatorUuid, String indicatorVersion) throws Exception {
        IndicatorVersion indicator = getIndicatorsService().retrieveIndicator(getServiceContext(), indicatorUuid, indicatorVersion);
        assertNotNull(indicator);
        assertNotNull(indicator.getDataRepositoryId());
        List<ConditionObservationDto> conditionObservationsList = getDatasetRepositoriesServiceFacade().findCodeDimensions(indicator.getDataRepositoryId());
        // Retrieve all dataset's code dimensions
        Map<String, List<String>> repoDimCodes = new HashMap<String, List<String>>();
        for (ConditionObservationDto condObs : conditionObservationsList) {
            for (CodeDimensionDto codeDim : condObs.getCodesDimension()) {
                List<String> codes = repoDimCodes.get(codeDim.getDimensionId());
                if (codes == null) {
                    codes = new ArrayList<String>();
                }
                codes.add(codeDim.getCodeDimensionId());
                repoDimCodes.put(codeDim.getDimensionId(), codes);
            }
        }
        // Compare with expected code dimensions
        for (String dimension : dimCodes.keySet()) {
            List<String> dataSetCodes = repoDimCodes.get(dimension);
            assertNotNull(dataSetCodes);
            assertNotNull(dimCodes.get(dimension));
            Set<String> dimCodesSet = new HashSet<String>(dimCodes.get(dimension));
            Set<String> dataSetCodesSet = new HashSet<String>(dataSetCodes);
            assertEquals(dimCodesSet, dataSetCodesSet);
        }

        assertEquals(dimCodes.keySet().size(), repoDimCodes.keySet().size());
    }

    protected void checkDataObservations(Map<String, List<String>> dimCodes, String indicatorUuid, String indicatorVersion, List<String> plainData) throws Exception {
        IndicatorVersion indicator = getIndicatorsService().retrieveIndicator(getServiceContext(), indicatorUuid, indicatorVersion);
        assertNotNull(indicator);
        assertNotNull(indicator.getDataRepositoryId());
        int index = 0;
        for (String measureCode : dimCodes.get(IndicatorDataDimensionTypeEnum.MEASURE.name())) {
            for (String timeCode : dimCodes.get(IndicatorDataDimensionTypeEnum.TIME.name())) {
                for (String geoCode : dimCodes.get(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name())) {
                    ConditionObservationDto condition = new ConditionObservationDto();
                    condition.addCodesDimension(new CodeDimensionDto(IndicatorDataDimensionTypeEnum.TIME.name(), timeCode));
                    condition.addCodesDimension(new CodeDimensionDto(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name(), geoCode));
                    condition.addCodesDimension(new CodeDimensionDto(IndicatorDataDimensionTypeEnum.MEASURE.name(), measureCode));

                    Map<String, ObservationDto> observations = getDatasetRepositoriesServiceFacade().findObservations(indicator.getDataRepositoryId(), Arrays.asList(condition));
                    assertNotNull(observations);
                    assertEquals(1, observations.keySet().size());
                    List<ObservationDto> observationsList = new ArrayList<ObservationDto>(observations.values());
                    assertEquals(plainData.get(index), observationsList.get(0).getPrimaryMeasure());
                    index++;
                }
            }
        }
    }

    protected void checkDataAttributes(Map<String, List<String>> dimCodes, String indicatorUuid, String indicatorVersion, String attrId, Map<String, AttributeBasicDto> expectedAttributes)
            throws Exception {
        IndicatorVersion indicator = getIndicatorsService().retrieveIndicator(getServiceContext(), indicatorUuid, indicatorVersion);
        assertNotNull(indicator);
        assertNotNull(indicator.getDataRepositoryId());

        ConditionDimensionDto geoCondition = createCondition(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name(), dimCodes.get(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name()));
        ConditionDimensionDto timeCondition = createCondition(IndicatorDataDimensionTypeEnum.TIME.name(), dimCodes.get(IndicatorDataDimensionTypeEnum.TIME.name()));
        ConditionDimensionDto measureCondition = createCondition(IndicatorDataDimensionTypeEnum.MEASURE.name(), dimCodes.get(IndicatorDataDimensionTypeEnum.MEASURE.name()));

        Map<String, ObservationExtendedDto> observations = getDatasetRepositoriesServiceFacade().findObservationsExtendedByDimensions(indicator.getDataRepositoryId(),
                Arrays.asList(geoCondition, timeCondition, measureCondition));

        for (Entry<String, ObservationExtendedDto> entry : observations.entrySet()) {
            String key = entry.getKey();
            ObservationExtendedDto obs = entry.getValue();

            AttributeBasicDto expectedAttr = expectedAttributes.get(key);
            AttributeBasicDto actualAttr = findAttribute(obs, attrId);

            IndicatorsAsserts.assertEqualsAttributeBasic(expectedAttr, actualAttr);
        }
    }

    protected AttributeBasicDto findAttribute(ObservationExtendedDto observation, String attrId) {
        for (AttributeBasicDto attr : observation.getAttributes()) {
            if (attrId.equals(attr.getAttributeId())) {
                return attr;
            }
        }
        return null;
    }

    /*
     * Helper for generate id for observation maps
     */
    protected String generateObservationUniqueKey(String geoValue, String timeValue, String measureValue) {
        CodeDimensionDto geoDimCode = new CodeDimensionDto(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name(), geoValue);
        CodeDimensionDto timeDimCode = new CodeDimensionDto(IndicatorDataDimensionTypeEnum.TIME.name(), timeValue);
        CodeDimensionDto measureDimCode = new CodeDimensionDto(IndicatorDataDimensionTypeEnum.MEASURE.name(), measureValue);
        return DtoUtils.generateUniqueKey(Arrays.asList(geoDimCode, timeDimCode, measureDimCode));
    }

    /*
     * Helper for attributes
     */
    protected AttributeBasicDto createAttribute(String id, String locale, String value) {
        AttributeBasicDto attributeBasicDto = new AttributeBasicDto();
        attributeBasicDto.setAttributeId(id);
        InternationalStringDto intStr = new InternationalStringDto();
        LocalisedStringDto locStr = new LocalisedStringDto();
        locStr.setLocale(locale);
        locStr.setLabel(value);
        intStr.addText(locStr);
        attributeBasicDto.setValue(intStr);
        return attributeBasicDto;
    }

    /*
     * Helper for ConditionDimension
     */
    protected ConditionDimensionDto createCondition(String dimensionId, List<String> codeDimensions) {
        ConditionDimensionDto condition = new ConditionDimensionDto();
        condition.setDimensionId(dimensionId);
        condition.setCodesDimension(codeDimensions);
        return condition;
    }
    
    protected List<String> getGeographicalGranularitiesCodes(List<GeographicalGranularity> granularities) {
        if (granularities == null) {
            return null;
        }
        List<String> codes = new ArrayList<String>();
        for (GeographicalGranularity granularity : granularities) {
            codes.add(granularity.getCode());
        }
        return codes;
    }
    
    protected List<String> getGeographicalValuesCodes(List<GeographicalValue> geoValues) {
        if (geoValues == null) {
            return null;
        }
        List<String> codes = new ArrayList<String>();
        for (GeographicalValue geoValue: geoValues) {
            codes.add(geoValue.getCode());
        }
        return codes;
    }

}
