package es.gobcan.istac.indicators.core.serviceapi;

import static org.mockito.Mockito.when;
import static junit.framework.Assert.*;

import java.util.List;
import java.util.Map;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.arte.statistic.dataset.repository.service.DatasetRepositoriesServiceFacade;

import es.gobcan.istac.indicators.core.domain.GeographicalGranularity;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.MeasureValue;
import es.gobcan.istac.indicators.core.domain.TimeGranularity;
import es.gobcan.istac.indicators.core.domain.TimeValue;
import es.gobcan.istac.indicators.core.enume.domain.MeasureDimensionTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.TimeGranularityEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;

/**
 * Spring based transactional test with DbUnit support.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/include/indicators-data-service-mockito.xml", "classpath:spring/applicationContext-test.xml"})
@TransactionConfiguration(defaultRollback = true, transactionManager = "txManager")
@Transactional
public class IndicatorsDataServiceRetrieveGeoTimeTest extends IndicatorsDataBaseTest {

    private static final String              SUBJECT_CODE_1                   = "1";
    private static final String              SUBJECT_CODE_3                   = "3";

    private static final String              GEO_GRANULARITY_COUNTRIES_UUID        = "1";
    private static final String              GEO_GRANULARITY_COMMUNITIES_UUID      = "2";
    private static final String              GEO_GRANULARITY_PROVINCES_UUID        = "3";

    /* Has geographic and time variables */
    private static final String              INDICATOR1_UUID                  = "Indicator-1";
    private static final String              INDICATOR1_PUBLISHED_DS_GPE_UUID = "Indicator-1-v1-DataSource-1-GPE-TIME-GEO";
    private static final String              INDICATOR1_DRAFT_DS_GPE_UUID     = "Indicator-1-v2-DataSource-1-GPE-TIME-GEO";
    private static final String              INDICATOR1_GPE_JSON_DATA         = readFile("json/data_temporal_spatials.json");
    private static final String              INDICATOR1_PUBLISHED_VERSION     = "1.000";
    private static final String              INDICATOR1_DRAFT_VERSION         = "2.000";

    /* Has geographic and time variables */
    private static final String              INDICATOR2_UUID                  = "Indicator-2";
    private static final String              INDICATOR2_DS_GPE_UUID           = "Indicator-2-v1-DataSource-1-GPE-TIME-GEO";
    private static final String              INDICATOR2_GPE_JSON_DATA         = readFile("json/data_temporal_spatials.json");
    private static final String              INDICATOR2_VERSION               = "1.000";

    /* Has no geographic and temporal variables */
    private static final String              INDICATOR3_UUID                  = "Indicator-3";
    private static final String              INDICATOR3_DS_GPE_UUID           = "Indicator-3-v1-DataSource-1-GPE-NOTIME-NOGEO";
    private static final String              INDICATOR3_GPE_JSON_DATA         = readFile("json/data_fixed.json");
    private static final String              INDICATOR3_VERSION               = "1.000";

    /* Has no geographic and temporal variables */
    private static final String              INDICATOR4_UUID                  = "Indicator-4";
    private static final String              INDICATOR4_DS_GPE_UUID           = "Indicator-4-v1-DataSource-1-GPE-NOTIME-NOGEO";
    private static final String              INDICATOR4_GPE_JSON_DATA         = readFile("json/data_fixed.json");
    private static final String              INDICATOR4_VERSION               = "1.000";

    /* Has no geographic and temporal variables */
    private static final String              INDICATOR5_UUID                  = "Indicator-5";
    private static final String              INDICATOR5_DS_GPE_UUID           = "Indicator-5-v1-DataSource-1-GPE-NOTIME-NOGEO";
    private static final String              INDICATOR5_GPE_JSON_DATA         = readFile("json/data_fixed.json");
    private static final String              INDICATOR5_VERSION               = "1.000";

    /* Indicator instances */
    /* GEO Granularity provinces, time granularity yearly */
    private static final String              INDICATOR_INSTANCE_11_UUID       = "IndSys-1-v1-IInstance-11";
    /* NOT geographic restrictions, TIME GRANULARITY monthly */
    private static final String              INDICATOR_INSTANCE_12_UUID       = "IndSys-1-v1-IInstance-12";
    /* GEO es FIXED */
    private static final String              INDICATOR_INSTANCE_13_UUID       = "IndSys-1-v1-IInstance-13";

    private static final String              INDICATORS_SYSTEM_1_CODE         = "IndSys-CODE-1";
    private static final String              INDICATORS_SYSTEM_2_CODE         = "IndSys-CODE-2";

    @Autowired
    protected IndicatorsDataService          indicatorsDataService;

    @Autowired
    private IndicatorsDataProviderService    indicatorsDataProviderService;

    @Autowired
    private DatasetRepositoriesServiceFacade datasetRepositoriesServiceFacade;

    @Autowired
    private IndicatorsService                indicatorsService;

    /* GEOGRAPHICAL GRANULARITIES */

    @Test
    public void testRetrieveGeographicalGranularitiesInIndicator() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_DRAFT_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_PUBLISHED_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID);

        List<GeographicalGranularity> granularities = indicatorsDataService.retrieveGeographicalGranularitiesInIndicator(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_DRAFT_VERSION);
        List<String> codes = getGeographicalGranularitiesCodes(granularities);
        String[] expectedCodes = new String[]{"COUNTRIES", "COMMUNITIES", "PROVINCES"};
        checkElementsInCollection(expectedCodes, codes);
    }

    @Test
    public void testRetrieveGeographicalGranularitiesInIndicatorNotPopulated() throws Exception {
        try {
            List<GeographicalGranularity> granularities = indicatorsDataService.retrieveGeographicalGranularitiesInIndicator(getServiceContextAdministrador(), INDICATOR1_UUID,
                    INDICATOR1_DRAFT_VERSION);
            fail("Should fail because indicator has not been populated");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_NOT_POPULATED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testRetrieveGeographicalGranularitiesInIndicatorFixedValue() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR4_DS_GPE_UUID))).thenReturn(INDICATOR4_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR4_UUID);

        List<GeographicalGranularity> granularities = indicatorsDataService.retrieveGeographicalGranularitiesInIndicator(getServiceContextAdministrador(), INDICATOR4_UUID, INDICATOR4_VERSION);
        List<String> codes = getGeographicalGranularitiesCodes(granularities);
        String[] expectedCodes = new String[]{"COUNTRIES"};
        checkElementsInCollection(expectedCodes, codes);
    }

    @Test
    public void testRetrieveGeographicalGranularitiesInIndicatorPublished() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_PUBLISHED_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID);

        List<GeographicalGranularity> granularities = indicatorsDataService.retrieveGeographicalGranularitiesInIndicatorPublished(getServiceContextAdministrador(), INDICATOR1_UUID);
        List<String> codes = getGeographicalGranularitiesCodes(granularities);
        String[] expectedCodes = new String[]{"COUNTRIES", "COMMUNITIES", "PROVINCES"};
        checkElementsInCollection(expectedCodes, codes);
    }

    @Test
    public void testRetrieveGeographicalGranularitiesInIndicatorsPublishedWithSubjectCode() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_PUBLISHED_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorVersionData(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_PUBLISHED_VERSION);

        List<GeographicalGranularity> granularities = indicatorsDataService.retrieveGeographicalGranularitiesInIndicatorsPublishedWithSubjectCode(getServiceContextAdministrador(), SUBJECT_CODE_1);
        List<String> codes = getGeographicalGranularitiesCodes(granularities);
        String[] expectedCodes = new String[]{"COUNTRIES", "COMMUNITIES", "PROVINCES"};
        checkElementsInCollection(expectedCodes, codes);
    }

    @Test
    public void testRetrieveGeographicalGranularitiesInIndicatorsPublishedWithSubjectCodeFixedGeo() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR3_DS_GPE_UUID))).thenReturn(INDICATOR3_GPE_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR5_DS_GPE_UUID))).thenReturn(INDICATOR5_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorVersionData(getServiceContextAdministrador(), INDICATOR3_UUID, INDICATOR3_VERSION);
        indicatorsDataService.populateIndicatorVersionData(getServiceContextAdministrador(), INDICATOR5_UUID, INDICATOR5_VERSION);

        List<GeographicalGranularity> granularities = indicatorsDataService.retrieveGeographicalGranularitiesInIndicatorsPublishedWithSubjectCode(getServiceContextAdministrador(), SUBJECT_CODE_3);
        List<String> codes = getGeographicalGranularitiesCodes(granularities);
        String[] expectedCodes = new String[]{"COMMUNITIES", "PROVINCES"};
        checkElementsInCollection(expectedCodes, codes);
    }

    @Test
    public void testRetrieveGeographicalGranularitiesInIndicatorInstance() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_PUBLISHED_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID);

        List<GeographicalGranularity> granularities = indicatorsDataService.retrieveGeographicalGranularitiesInIndicatorInstanceWithPublishedIndicator(getServiceContextAdministrador(), INDICATOR_INSTANCE_11_UUID);
        List<String> codes = getGeographicalGranularitiesCodes(granularities);
        String[] expectedCodes = new String[]{"PROVINCES"};
        checkElementsInCollection(expectedCodes, codes);
    }

    @Test
    public void testRetrieveGeographicalGranularitiesInIndicatorInstanceNotPopulated() throws Exception {
        try {
            List<GeographicalGranularity> granularities = indicatorsDataService.retrieveGeographicalGranularitiesInIndicatorInstanceWithPublishedIndicator(getServiceContextAdministrador(), INDICATOR_INSTANCE_11_UUID);
            fail("Should fail because indicator has not been populated");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_NOT_POPULATED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testRetrieveGeographicalGranularitiesInIndicatorInstanceFixedGeoValue() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_PUBLISHED_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorVersionData(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_PUBLISHED_VERSION);

        List<GeographicalGranularity> granularities = indicatorsDataService.retrieveGeographicalGranularitiesInIndicatorInstanceWithPublishedIndicator(getServiceContextAdministrador(), INDICATOR_INSTANCE_13_UUID);
        List<String> codes = getGeographicalGranularitiesCodes(granularities);
        String[] expectedCodes = new String[]{"COUNTRIES"};
        checkElementsInCollection(expectedCodes, codes);
    }

    @Test
    public void testRetrieveGeographicalGranularitiesInIndicatorInstanceNotGeoFilter() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_PUBLISHED_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID);

        List<GeographicalGranularity> granularities = indicatorsDataService.retrieveGeographicalGranularitiesInIndicatorInstanceWithPublishedIndicator(getServiceContextAdministrador(), INDICATOR_INSTANCE_12_UUID);
        List<String> codes = getGeographicalGranularitiesCodes(granularities);
        String[] expectedCodes = new String[]{"COUNTRIES", "COMMUNITIES", "PROVINCES"};
        checkElementsInCollection(expectedCodes, codes);
    }

    @Test
    public void testRetrieveGeographicalGranularitiesInIndicatorsInstanceInPublishedIndicatorsSystem() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR3_DS_GPE_UUID))).thenReturn(INDICATOR3_GPE_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR2_DS_GPE_UUID))).thenReturn(INDICATOR2_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR3_UUID);
        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR2_UUID);

        List<GeographicalGranularity> granularities = indicatorsDataService.retrieveGeographicalGranularitiesInIndicatorsInstanceInPublishedIndicatorsSystem(getServiceContextAdministrador(),
                INDICATORS_SYSTEM_2_CODE);
        List<String> codes = getGeographicalGranularitiesCodes(granularities);
        String[] expectedCodes = new String[]{"COUNTRIES", "COMMUNITIES", "PROVINCES"};
        checkElementsInCollection(expectedCodes, codes);
    }

    /* GEOGRAPHICAL VALUES BY GRANULARITY */

    @Test
    public void testRetrieveGeographicalValuesByGranularityInIndicator() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_DRAFT_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID);

        List<GeographicalValue> geoValues = indicatorsDataService.retrieveGeographicalValuesByGranularityInIndicator(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_DRAFT_VERSION,
                GEO_GRANULARITY_COUNTRIES_UUID);
        List<String> countryCodes = getGeographicalValuesCodes(geoValues);
        String[] expectedCountryCodes = new String[]{"ES"};
        checkElementsOrder(expectedCountryCodes, countryCodes);

        geoValues = indicatorsDataService.retrieveGeographicalValuesByGranularityInIndicator(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_DRAFT_VERSION, GEO_GRANULARITY_COMMUNITIES_UUID);
        List<String> communityCodes = getGeographicalValuesCodes(geoValues);
        String[] expectedCommunityCodes = new String[]{"ES61"};
        checkElementsOrder(expectedCommunityCodes, communityCodes);

        geoValues = indicatorsDataService.retrieveGeographicalValuesByGranularityInIndicator(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_DRAFT_VERSION, GEO_GRANULARITY_PROVINCES_UUID);
        List<String> provinceCodes = getGeographicalValuesCodes(geoValues);
        String[] expectedProvinceCodes = new String[]{"ES611", "ES612", "ES613"};
        checkElementsOrder(expectedProvinceCodes, provinceCodes);
    }

    @Test
    public void testRetrieveGeographicalValuesByGranularityInIndicatorFixedValue() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR4_DS_GPE_UUID))).thenReturn(INDICATOR4_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR4_UUID);

        List<GeographicalValue> geoValues = indicatorsDataService.retrieveGeographicalValuesByGranularityInIndicator(getServiceContextAdministrador(), INDICATOR4_UUID, INDICATOR4_VERSION,
                GEO_GRANULARITY_COUNTRIES_UUID);
        List<String> countryCodes = getGeographicalValuesCodes(geoValues);
        String[] expectedCountryCodes = new String[]{"ES"};
        checkElementsOrder(expectedCountryCodes, countryCodes);

        geoValues = indicatorsDataService.retrieveGeographicalValuesByGranularityInIndicator(getServiceContextAdministrador(), INDICATOR4_UUID, INDICATOR4_VERSION, GEO_GRANULARITY_COMMUNITIES_UUID);
        List<String> communityCodes = getGeographicalValuesCodes(geoValues);
        String[] expectedCommunityCodes = new String[]{};
        checkElementsOrder(expectedCommunityCodes, communityCodes);

        geoValues = indicatorsDataService.retrieveGeographicalValuesByGranularityInIndicator(getServiceContextAdministrador(), INDICATOR4_UUID, INDICATOR4_VERSION, GEO_GRANULARITY_PROVINCES_UUID);
        List<String> provinceCodes = getGeographicalValuesCodes(geoValues);
        String[] expectedProvinceCodes = new String[]{};
        checkElementsOrder(expectedProvinceCodes, provinceCodes);
    }

    @Test
    public void testRetrieveGeographicalValuesByGranularityInIndicatorPublished() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_PUBLISHED_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID);

        List<GeographicalValue> geoValues = indicatorsDataService.retrieveGeographicalValuesByGranularityInIndicatorPublished(getServiceContextAdministrador(), INDICATOR1_UUID,
                GEO_GRANULARITY_COUNTRIES_UUID);
        List<String> countryCodes = getGeographicalValuesCodes(geoValues);
        String[] expectedCountryCodes = new String[]{"ES"};
        checkElementsOrder(expectedCountryCodes, countryCodes);

        geoValues = indicatorsDataService.retrieveGeographicalValuesByGranularityInIndicatorPublished(getServiceContextAdministrador(), INDICATOR1_UUID, GEO_GRANULARITY_COMMUNITIES_UUID);
        List<String> communityCodes = getGeographicalValuesCodes(geoValues);
        String[] expectedCommunityCodes = new String[]{"ES61"};
        checkElementsOrder(expectedCommunityCodes, communityCodes);

        geoValues = indicatorsDataService.retrieveGeographicalValuesByGranularityInIndicatorPublished(getServiceContextAdministrador(), INDICATOR1_UUID, GEO_GRANULARITY_PROVINCES_UUID);
        List<String> provinceCodes = getGeographicalValuesCodes(geoValues);
        String[] expectedProvinceCodes = new String[]{"ES611", "ES612", "ES613"};
        checkElementsOrder(expectedProvinceCodes, provinceCodes);
    }
    
    @Test
    public void testRetrieveGeographicalValuesByGranularityInIndicatorPublishedWithSubjectCode() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_PUBLISHED_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);
        
        indicatorsDataService.populateIndicatorVersionData(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_PUBLISHED_VERSION);
        
        List<GeographicalValue> geoValues = indicatorsDataService.retrieveGeographicalValuesByGranularityInIndicatorPublishedWithSubjectCode(getServiceContextAdministrador(), SUBJECT_CODE_1, GEO_GRANULARITY_COUNTRIES_UUID);
        List<String> countryCodes = getGeographicalValuesCodes(geoValues);
        String[] expectedCountryCodes = new String[]{"ES"};
        checkElementsOrder(expectedCountryCodes, countryCodes);
        
        geoValues = indicatorsDataService.retrieveGeographicalValuesByGranularityInIndicatorPublishedWithSubjectCode(getServiceContextAdministrador(), SUBJECT_CODE_1, GEO_GRANULARITY_COMMUNITIES_UUID);
        List<String> communityCodes = getGeographicalValuesCodes(geoValues);
        String[] expectedCommunityCodes = new String[]{"ES61"};
        checkElementsOrder(expectedCommunityCodes, communityCodes);
        
        geoValues = indicatorsDataService.retrieveGeographicalValuesByGranularityInIndicatorPublishedWithSubjectCode(getServiceContextAdministrador(), SUBJECT_CODE_1, GEO_GRANULARITY_PROVINCES_UUID);
        List<String> provinceCodes = getGeographicalValuesCodes(geoValues);
        String[] expectedProvinceCodes = new String[]{"ES611", "ES612", "ES613"};
        checkElementsOrder(expectedProvinceCodes, provinceCodes);
    }
    
    
    @Test
    public void testRetrieveGeographicalValuesByGranularityInIndicatorPublishedWithSubjectCodeMultiFixed() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR3_DS_GPE_UUID))).thenReturn(INDICATOR3_GPE_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR5_DS_GPE_UUID))).thenReturn(INDICATOR5_GPE_JSON_DATA);
        
        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR3_UUID);
        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR5_UUID);
        
        List<GeographicalValue> geoValues = indicatorsDataService.retrieveGeographicalValuesByGranularityInIndicatorPublishedWithSubjectCode(getServiceContextAdministrador(), SUBJECT_CODE_3, GEO_GRANULARITY_COUNTRIES_UUID);
        List<String> countryCodes = getGeographicalValuesCodes(geoValues);
        String[] expectedCountryCodes = new String[]{};
        checkElementsOrder(expectedCountryCodes, countryCodes);
        
        geoValues = indicatorsDataService.retrieveGeographicalValuesByGranularityInIndicatorPublishedWithSubjectCode(getServiceContextAdministrador(), SUBJECT_CODE_3, GEO_GRANULARITY_COMMUNITIES_UUID);
        List<String> communityCodes = getGeographicalValuesCodes(geoValues);
        String[] expectedCommunityCodes = new String[]{"ES61"};
        checkElementsOrder(expectedCommunityCodes, communityCodes);
        
        geoValues = indicatorsDataService.retrieveGeographicalValuesByGranularityInIndicatorPublishedWithSubjectCode(getServiceContextAdministrador(), SUBJECT_CODE_3, GEO_GRANULARITY_PROVINCES_UUID);
        List<String> provinceCodes = getGeographicalValuesCodes(geoValues);
        String[] expectedProvinceCodes = new String[]{"ES611"};
        checkElementsOrder(expectedProvinceCodes, provinceCodes);
    }
    
    @Test
    public void testRetrieveGeographicalValuesByGranularityInIndicatorInstanceDifferentGranularity() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_PUBLISHED_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorVersionData(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_PUBLISHED_VERSION);

        List<GeographicalValue> geoValues = indicatorsDataService.retrieveGeographicalValuesByGranularityInIndicatorInstance(getServiceContextAdministrador(), INDICATOR_INSTANCE_11_UUID,
                GEO_GRANULARITY_COUNTRIES_UUID);
        assertNotNull(geoValues);
        assertEquals(0, geoValues.size());
    }

    @Test
    public void testRetrieveGeographicalValuesByGranularityInIndicatorInstanceSameGranularity() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_PUBLISHED_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorVersionData(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_PUBLISHED_VERSION);

        List<GeographicalValue> geoValues = indicatorsDataService.retrieveGeographicalValuesByGranularityInIndicatorInstance(getServiceContextAdministrador(), INDICATOR_INSTANCE_11_UUID,
                GEO_GRANULARITY_PROVINCES_UUID);
        assertNotNull(geoValues);
        assertEquals(3, geoValues.size());

        List<String> provinceCodes = getGeographicalValuesCodes(geoValues);
        String[] expectedProvinceCodes = new String[]{"ES611", "ES612", "ES613"};
        checkElementsOrder(expectedProvinceCodes, provinceCodes);
    }

    @Test
    public void testRetrieveGeographicalValuesByGranularityInIndicatorInstanceNotGeoFixed() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_PUBLISHED_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorVersionData(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_PUBLISHED_VERSION);
        {
            List<GeographicalValue> geoValues = indicatorsDataService.retrieveGeographicalValuesByGranularityInIndicatorInstance(getServiceContextAdministrador(), INDICATOR_INSTANCE_12_UUID,
                    GEO_GRANULARITY_COUNTRIES_UUID);
            assertNotNull(geoValues);
            assertEquals(1, geoValues.size());

            List<String> countryCodes = getGeographicalValuesCodes(geoValues);
            String[] expectedCountryCodes = new String[]{"ES"};
            checkElementsOrder(expectedCountryCodes, countryCodes);
        }
        {
            List<GeographicalValue> geoValues = indicatorsDataService.retrieveGeographicalValuesByGranularityInIndicatorInstance(getServiceContextAdministrador(), INDICATOR_INSTANCE_12_UUID,
                    GEO_GRANULARITY_COMMUNITIES_UUID);
            assertNotNull(geoValues);
            assertEquals(1, geoValues.size());

            List<String> communityCodes = getGeographicalValuesCodes(geoValues);
            String[] expectedCommunityCodes = new String[]{"ES61"};
            checkElementsOrder(expectedCommunityCodes, communityCodes);
        }
        {
            List<GeographicalValue> geoValues = indicatorsDataService.retrieveGeographicalValuesByGranularityInIndicatorInstance(getServiceContextAdministrador(), INDICATOR_INSTANCE_12_UUID,
                    GEO_GRANULARITY_PROVINCES_UUID);
            assertNotNull(geoValues);
            assertEquals(3, geoValues.size());

            List<String> provinceCodes = getGeographicalValuesCodes(geoValues);
            String[] expectedProvinceCodes = new String[]{"ES611", "ES612", "ES613"};
            checkElementsOrder(expectedProvinceCodes, provinceCodes);
        }
    }

    @Test
    public void testRetrieveGeographicalValuesByGranularityInIndicatorInstanceGeoValueFixed() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_PUBLISHED_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorVersionData(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_PUBLISHED_VERSION);
        {
            List<GeographicalValue> geoValues = indicatorsDataService.retrieveGeographicalValuesByGranularityInIndicatorInstance(getServiceContextAdministrador(), INDICATOR_INSTANCE_13_UUID,
                    GEO_GRANULARITY_COUNTRIES_UUID);
            assertNotNull(geoValues);
            assertEquals(1, geoValues.size());

            List<String> countryCodes = getGeographicalValuesCodes(geoValues);
            String[] expectedCountryCodes = new String[]{"ES"};
            checkElementsOrder(expectedCountryCodes, countryCodes);
        }
        {
            List<GeographicalValue> geoValues = indicatorsDataService.retrieveGeographicalValuesByGranularityInIndicatorInstance(getServiceContextAdministrador(), INDICATOR_INSTANCE_13_UUID,
                    GEO_GRANULARITY_COMMUNITIES_UUID);
            assertNotNull(geoValues);
            assertEquals(0, geoValues.size());
        }
        {
            List<GeographicalValue> geoValues = indicatorsDataService.retrieveGeographicalValuesByGranularityInIndicatorInstance(getServiceContextAdministrador(), INDICATOR_INSTANCE_13_UUID,
                    GEO_GRANULARITY_PROVINCES_UUID);
            assertNotNull(geoValues);
            assertEquals(0, geoValues.size());
        }
    }
    
    @Test
    public void testRetrieveGeographicalValuesByGranularityInIndicatorsInstancesInPublishedIndicatorsSystem() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR2_DS_GPE_UUID))).thenReturn(INDICATOR2_GPE_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR3_DS_GPE_UUID))).thenReturn(INDICATOR3_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR2_UUID);
        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR3_UUID);
        {
            List<GeographicalValue> geoValues = indicatorsDataService.retrieveGeographicalValuesByGranularityInIndicatorsInstancesInPublishedIndicatorsSystem(getServiceContextAdministrador(), INDICATORS_SYSTEM_2_CODE, GEO_GRANULARITY_COUNTRIES_UUID);
            assertNotNull(geoValues);
            List<String> countryCodes = getGeographicalValuesCodes(geoValues);
            String[] expectedCountryCodes = new String[]{"ES"};
            checkElementsOrder(expectedCountryCodes, countryCodes);
        }
        {
            List<GeographicalValue> geoValues = indicatorsDataService.retrieveGeographicalValuesByGranularityInIndicatorsInstancesInPublishedIndicatorsSystem(getServiceContextAdministrador(), INDICATORS_SYSTEM_2_CODE, GEO_GRANULARITY_COMMUNITIES_UUID);
            assertNotNull(geoValues);
            List<String> communityCodes = getGeographicalValuesCodes(geoValues);
            String[] expectedCommunityCodes = new String[]{"ES61"};
            checkElementsOrder(expectedCommunityCodes, communityCodes);
        }
        {
            List<GeographicalValue> geoValues = indicatorsDataService.retrieveGeographicalValuesByGranularityInIndicatorsInstancesInPublishedIndicatorsSystem(getServiceContextAdministrador(), INDICATORS_SYSTEM_2_CODE,GEO_GRANULARITY_PROVINCES_UUID);
            assertNotNull(geoValues);
            List<String> provinceCodes = getGeographicalValuesCodes(geoValues);
            String[] expectedProvinceCodes = new String[]{"ES611"};
            checkElementsOrder(expectedProvinceCodes, provinceCodes);
        }
    }
    

    /* GEOGRAPHICAL VALUES */

    @Test
    public void testRetrieveGeographicalValuesInIndicator() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_DRAFT_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID);

        List<GeographicalValue> geoValues = indicatorsDataService.retrieveGeographicalValuesInIndicator(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_DRAFT_VERSION);
        List<String> countryCodes = getGeographicalValuesCodes(geoValues);
        String[] expectedCountryCodes = new String[]{"ES", "ES61", "ES611", "ES612", "ES613"};
        checkElementsOrder(expectedCountryCodes, countryCodes);
    }

    @Test
    public void testRetrieveGeographicalValuesInIndicatorFixedValue() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR4_DS_GPE_UUID))).thenReturn(INDICATOR4_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR4_UUID);

        List<GeographicalValue> geoValues = indicatorsDataService.retrieveGeographicalValuesInIndicator(getServiceContextAdministrador(), INDICATOR4_UUID, INDICATOR4_VERSION);
        List<String> countryCodes = getGeographicalValuesCodes(geoValues);
        String[] expectedCountryCodes = new String[]{"ES"};
        checkElementsOrder(expectedCountryCodes, countryCodes);
    }

    @Test
    public void testRetrieveGeographicalValuesInIndicatorPublished() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_PUBLISHED_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID);

        List<GeographicalValue> geoValues = indicatorsDataService.retrieveGeographicalValuesInIndicatorPublished(getServiceContextAdministrador(), INDICATOR1_UUID);
        List<String> countryCodes = getGeographicalValuesCodes(geoValues);
        String[] expectedCountryCodes = new String[]{"ES", "ES61", "ES611", "ES612", "ES613"};
        checkElementsOrder(expectedCountryCodes, countryCodes);
    }
    
    @Test
    public void testRetrieveGeographicalValuesInPublishedIndicatorsWithSubjectCode() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_PUBLISHED_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);
        
        indicatorsDataService.populateIndicatorVersionData(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_PUBLISHED_VERSION);
        
        List<GeographicalValue> geoValues = indicatorsDataService.retrieveGeographicalValuesInPublishedIndicatorsWithSubjectCode(getServiceContextAdministrador(), SUBJECT_CODE_1);
        List<String> actualCodes = getGeographicalValuesCodes(geoValues);
        String[] expectedCodes = new String[]{"ES","ES61","ES611","ES612","ES613"};
        checkElementsOrder(expectedCodes, actualCodes);
    }
    
    @Test
    public void testRetrieveGeographicalValuesInPublishedIndicatorsWithSubjectCodeMultiFixed() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR3_DS_GPE_UUID))).thenReturn(INDICATOR3_GPE_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR5_DS_GPE_UUID))).thenReturn(INDICATOR5_GPE_JSON_DATA);
        
        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR3_UUID);
        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR5_UUID);
        
        List<GeographicalValue> geoValues = indicatorsDataService.retrieveGeographicalValuesInPublishedIndicatorsWithSubjectCode(getServiceContextAdministrador(), SUBJECT_CODE_3);
        List<String> actualCodes = getGeographicalValuesCodes(geoValues);
        String[] expectedCodes = new String[]{"ES61","ES611"};
        checkElementsOrder(expectedCodes, actualCodes);
    }

    @Test
    public void testRetrieveGeographicalValuesInIndicatorInstance() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_PUBLISHED_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID);

        List<GeographicalValue> geoValues = indicatorsDataService.retrieveGeographicalValuesInIndicatorInstanceWithPublishedIndicator(getServiceContextAdministrador(), INDICATOR_INSTANCE_11_UUID);
        List<String> geoCodes = getGeographicalValuesCodes(geoValues);
        String[] expectedCodes = new String[]{"ES611", "ES612", "ES613"};
        checkElementsOrder(expectedCodes, geoCodes);
    }

    @Test
    public void testRetrieveGeographicalValuesInIndicatorInstanceNotGeo() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_PUBLISHED_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_DRAFT_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID);

        List<GeographicalValue> geoValues = indicatorsDataService.retrieveGeographicalValuesInIndicatorInstanceWithPublishedIndicator(getServiceContextAdministrador(), INDICATOR_INSTANCE_12_UUID);
        List<String> geoCodes = getGeographicalValuesCodes(geoValues);
        String[] expectedCodes = new String[]{"ES", "ES61", "ES611", "ES612", "ES613"};
        checkElementsOrder(expectedCodes, geoCodes);
    }
    
    @Test
    public void testRetrieveGeographicalValuesInIndicatorsInstancesInPublishedIndicatorsSystem() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR2_DS_GPE_UUID))).thenReturn(INDICATOR2_GPE_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR3_DS_GPE_UUID))).thenReturn(INDICATOR3_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR2_UUID);
        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR3_UUID);
        {
            List<GeographicalValue> geoValues = indicatorsDataService.retrieveGeographicalValuesInIndicatorsInstancesInPublishedIndicatorsSystem(getServiceContextAdministrador(), INDICATORS_SYSTEM_2_CODE);
            assertNotNull(geoValues);
            List<String> actualCodes = getGeographicalValuesCodes(geoValues);
            String[] expectedCodes = new String[]{"ES","ES61","ES611"};
            checkElementsOrder(expectedCodes, actualCodes);
        }
    }

    /* TIME GRANULARITIES */

    @Test
    public void testRetrieveTimeGranularitiesInIndicator() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_DRAFT_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID);

        List<TimeGranularity> timeGranularities = indicatorsDataService.retrieveTimeGranularitiesInIndicator(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_DRAFT_VERSION);
        List<String> granularitiesCodes = getTimeGranularitiesNames(timeGranularities);
        String[] expectedGranularities = new String[]{TimeGranularityEnum.MONTHLY.name(), TimeGranularityEnum.YEARLY.name()};
        checkElementsInCollection(expectedGranularities, granularitiesCodes);
    }

    @Test
    public void testRetrieveTimeGranularitiesInIndicatorFixedValue() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR4_DS_GPE_UUID))).thenReturn(INDICATOR4_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR4_UUID);

        List<TimeGranularity> timeGranularities = indicatorsDataService.retrieveTimeGranularitiesInIndicator(getServiceContextAdministrador(), INDICATOR4_UUID, INDICATOR4_VERSION);
        List<String> granularitiesCodes = getTimeGranularitiesNames(timeGranularities);
        String[] expectedGranularities = new String[]{TimeGranularityEnum.YEARLY.name()};
        checkElementsInCollection(expectedGranularities, granularitiesCodes);
    }

    @Test
    public void testRetrieveTimeGranularitiesInIndicatorPublished() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_PUBLISHED_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_DRAFT_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID);

        List<TimeGranularity> timeGranularities = indicatorsDataService.retrieveTimeGranularitiesInIndicatorPublished(getServiceContextAdministrador(), INDICATOR1_UUID);
        List<String> granularitiesCodes = getTimeGranularitiesNames(timeGranularities);
        String[] expectedGranularities = new String[]{TimeGranularityEnum.MONTHLY.name(), TimeGranularityEnum.YEARLY.name()};
        checkElementsInCollection(expectedGranularities, granularitiesCodes);
    }

    @Test
    public void testRetrieveTimeGranularitiesInIndicatorInstance() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_PUBLISHED_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID);

        List<TimeGranularity> timeGranularities = indicatorsDataService.retrieveTimeGranularitiesInIndicatorInstanceWithPublishedIndicator(getServiceContextAdministrador(), INDICATOR_INSTANCE_12_UUID);
        List<String> granularitiesCodes = getTimeGranularitiesNames(timeGranularities);
        String[] expectedCodes = new String[]{"MONTHLY"};
        checkElementsInCollection(expectedCodes, granularitiesCodes);
    }

    /* TIME VALUES BY GRANULARITY */
    @Test
    public void testRetrieveTimeValuesByGranularityInIndicator() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_DRAFT_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID);

        List<TimeValue> yearTimeValues = indicatorsDataService.retrieveTimeValuesByGranularityInIndicator(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_DRAFT_VERSION,
                TimeGranularityEnum.YEARLY);
        List<String> yearValues = getTimeValuesCodes(yearTimeValues);
        String[] expectedYearValues = new String[]{"2010"};
        checkElementsInCollection(expectedYearValues, yearValues);

        List<TimeValue> monthTimeValues = indicatorsDataService.retrieveTimeValuesByGranularityInIndicator(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_DRAFT_VERSION,
                TimeGranularityEnum.MONTHLY);
        List<String> monthValues = getTimeValuesCodes(monthTimeValues);
        String[] expectedMonthValues = new String[]{"2011M01", "2010M12", "2010M11", "2010M10", "2010M09"};
        checkElementsOrder(expectedMonthValues, monthValues);

        // NOT EXIST
        List<TimeValue> dayTimeValues = indicatorsDataService.retrieveTimeValuesByGranularityInIndicator(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_DRAFT_VERSION,
                TimeGranularityEnum.DAILY);
        List<String> dayValues = getTimeValuesCodes(dayTimeValues);
        String[] expectedDayValues = new String[]{};
        checkElementsInCollection(expectedDayValues, dayValues);
    }

    @Test
    public void testRetrieveTimeValuesByGranularityInIndicatorFixedValue() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR4_DS_GPE_UUID))).thenReturn(INDICATOR4_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR4_UUID);

        List<TimeValue> yearTimeValues = indicatorsDataService.retrieveTimeValuesByGranularityInIndicator(getServiceContextAdministrador(), INDICATOR4_UUID, INDICATOR4_VERSION,
                TimeGranularityEnum.YEARLY);
        List<String> yearValues = getTimeValuesCodes(yearTimeValues);
        String[] expectedYearValues = new String[]{"2010"};
        checkElementsInCollection(expectedYearValues, yearValues);

        // NOT EXIST
        List<TimeValue> monthTimeValues = indicatorsDataService.retrieveTimeValuesByGranularityInIndicator(getServiceContextAdministrador(), INDICATOR4_UUID, INDICATOR4_VERSION,
                TimeGranularityEnum.MONTHLY);
        List<String> monthValues = getTimeValuesCodes(monthTimeValues);
        String[] expectedMonthValues = new String[]{};
        checkElementsInCollection(expectedMonthValues, monthValues);

        // NOT EXIST
        List<TimeValue> dayTimeValues = indicatorsDataService.retrieveTimeValuesByGranularityInIndicator(getServiceContextAdministrador(), INDICATOR4_UUID, INDICATOR4_VERSION,
                TimeGranularityEnum.DAILY);
        List<String> dayValues = getTimeValuesCodes(dayTimeValues);
        String[] expectedDayValues = new String[]{};
        checkElementsInCollection(expectedDayValues, dayValues);
    }

    @Test
    public void testRetrieveTimeValuesByGranularityInIndicatorPublished() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_PUBLISHED_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID);

        List<TimeValue> yearTimeValues = indicatorsDataService.retrieveTimeValuesByGranularityInIndicatorPublished(getServiceContextAdministrador(), INDICATOR1_UUID, TimeGranularityEnum.YEARLY);
        List<String> yearValues = getTimeValuesCodes(yearTimeValues);
        String[] expectedYearValues = new String[]{"2010"};
        checkElementsInCollection(expectedYearValues, yearValues);

        List<TimeValue> monthTimeValues = indicatorsDataService.retrieveTimeValuesByGranularityInIndicatorPublished(getServiceContextAdministrador(), INDICATOR1_UUID, TimeGranularityEnum.MONTHLY);
        List<String> monthValues = getTimeValuesCodes(monthTimeValues);
        String[] expectedMonthValues = new String[]{"2011M01", "2010M12", "2010M11", "2010M10", "2010M09"};
        checkElementsOrder(expectedMonthValues, monthValues);

        // NOT EXIST
        List<TimeValue> dayTimeValues = indicatorsDataService.retrieveTimeValuesByGranularityInIndicatorPublished(getServiceContextAdministrador(), INDICATOR1_UUID, TimeGranularityEnum.DAILY);
        List<String> dayValues = getTimeValuesCodes(dayTimeValues);
        String[] expectedDayValues = new String[]{};
        checkElementsInCollection(expectedDayValues, dayValues);
    }

    /* TIME VALUES */

    @Test
    public void testRetrieveTimeValuesInIndicator() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_DRAFT_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID);

        List<TimeValue> timeValues = indicatorsDataService.retrieveTimeValuesInIndicator(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_DRAFT_VERSION);
        List<String> timeCodes = getTimeValuesCodes(timeValues);
        String[] expectedValues = new String[]{"2011M01", "2010", "2010M12", "2010M11", "2010M10", "2010M09"};
        checkElementsInCollection(expectedValues, timeCodes);
    }

    @Test
    public void testRetrieveTimeValuesInIndicatorFixedValue() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR4_DS_GPE_UUID))).thenReturn(INDICATOR4_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR4_UUID);

        List<TimeValue> timeValues = indicatorsDataService.retrieveTimeValuesInIndicator(getServiceContextAdministrador(), INDICATOR4_UUID, INDICATOR4_VERSION);
        List<String> timeCodes = getTimeValuesCodes(timeValues);
        String[] expectedValues = new String[]{"2010"};
        checkElementsInCollection(expectedValues, timeCodes);
    }

    @Test
    public void testRetrieveTimeValuesInIndicatorPublished() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_PUBLISHED_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID);

        List<TimeValue> timeValues = indicatorsDataService.retrieveTimeValuesInIndicatorPublished(getServiceContextAdministrador(), INDICATOR1_UUID);
        List<String> timeCodes = getTimeValuesCodes(timeValues);
        String[] expectedValues = new String[]{"2011M01", "2010", "2010M12", "2010M11", "2010M10", "2010M09"};
        checkElementsInCollection(expectedValues, timeCodes);
    }

    @Test
    public void testRetrieveTimeValuesInIndicatorInstance() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_PUBLISHED_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID);

        List<TimeValue> timeValues = indicatorsDataService.retrieveTimeValuesInIndicatorInstanceWithPublishedIndicator(getServiceContextAdministrador(), INDICATOR_INSTANCE_12_UUID);
        List<String> timeCodes = getTimeValuesCodes(timeValues);
        String[] expectedCodes = new String[]{"2011M01", "2010M12", "2010M11", "2010M10", "2010M09"};
        checkElementsOrder(expectedCodes, timeCodes);
    }

    /* MEASURE VALUES */

    @Test
    public void testRetrieveMeasureValuesInIndicator() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_DRAFT_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID);

        List<MeasureValue> measures = indicatorsDataService.retrieveMeasureValuesInIndicator(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_DRAFT_VERSION);
        List<String> measuresNames = getMeasureNames(measures);
        String[] expectedMeasures = new String[]{MeasureDimensionTypeEnum.ABSOLUTE.name()};
        checkElementsInCollection(expectedMeasures, measuresNames);
    }

    @Test
    public void testRetrieveMeasureValuesInIndicatorWithRates() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR4_DS_GPE_UUID))).thenReturn(INDICATOR4_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR4_UUID);

        List<MeasureValue> measures = indicatorsDataService.retrieveMeasureValuesInIndicator(getServiceContextAdministrador(), INDICATOR4_UUID, INDICATOR4_VERSION);
        List<String> measuresNames = getMeasureNames(measures);
        String[] expectedMeasures = new String[]{MeasureDimensionTypeEnum.ABSOLUTE.name(), MeasureDimensionTypeEnum.ANNUAL_PERCENTAGE_RATE.name()};
        checkElementsInCollection(expectedMeasures, measuresNames);
    }

    @Test
    public void testRetrieveMeasureValuesInIndicatorPublished() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_PUBLISHED_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID);

        List<MeasureValue> measures = indicatorsDataService.retrieveMeasureValuesInIndicatorPublished(getServiceContextAdministrador(), INDICATOR1_UUID);
        List<String> measuresNames = getMeasureNames(measures);
        String[] expectedMeasures = new String[]{MeasureDimensionTypeEnum.ABSOLUTE.name()};
        checkElementsInCollection(expectedMeasures, measuresNames);
    }

    @Test
    public void testRetrieveMeasureValuesInIndicatorInstance() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_PUBLISHED_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID);

        List<MeasureValue> measureValues = indicatorsDataService.retrieveMeasureValuesInIndicatorInstanceWithPublishedIndicator(getServiceContextAdministrador(), INDICATOR_INSTANCE_12_UUID);
        List<String> measuresNames = getMeasureNames(measureValues);
        String[] expectedMeasures = new String[]{MeasureDimensionTypeEnum.ABSOLUTE.name()};
        checkElementsInCollection(expectedMeasures, measuresNames);
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/IndicatorsDataServiceTest_RetrieveGeoTime.xml";
    }

    @Override
    protected String getDataSetDSRepoFile() {
        return "dbunit/IndicatorsDataServiceTest_DataSetRepository.xml";
    }

    /* Utils for indicators base data */
    @Override
    protected IndicatorsService getIndicatorsService() {
        return indicatorsService;
    }

    @Override
    protected DatasetRepositoriesServiceFacade getDatasetRepositoriesServiceFacade() {
        return datasetRepositoriesServiceFacade;
    }
}
