package es.gobcan.istac.indicators.core.serviceapi;

import static org.mockito.Mockito.when;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
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

/**
 * Spring based transactional test with DbUnit support.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/include/indicators-data-service-mockito.xml", "classpath:spring/applicationContext-test.xml"})
@TransactionConfiguration(defaultRollback=false,transactionManager="txManager")
@Transactional
public class IndicatorsDataServiceRetrieveGeoTimeTest extends IndicatorsDataBaseTest {

    private static final String              GEO_GRANULARITY_COUNTRIES        = "1";
    private static final String              GEO_GRANULARITY_COMMUNITIES      = "2";
    private static final String              GEO_GRANULARITY_PROVINCES        = "3";

    /* Has geographic and time variables */
    private static final String              INDICATOR1_UUID                  = "Indicator-1";
    private static final String              INDICATOR1_PUBLISHED_DS_GPE_UUID = "Indicator-1-v1-DataSource-1-GPE-TIME-GEO";
    private static final String              INDICATOR1_DRAFT_DS_GPE_UUID     = "Indicator-1-v2-DataSource-1-GPE-TIME-GEO";
    private static final String              INDICATOR1_GPE_JSON_DATA         = readFile("json/data_temporal_spatials.json");
    private static final String              INDICATOR1_PUBLISHED_VERSION     = "1.000";
    private static final String              INDICATOR1_DRAFT_VERSION         = "2.000";

    /* Has no geographic and temporal variables */
    private static final String              INDICATOR4_UUID                  = "Indicator-4";
    private static final String              INDICATOR4_DS_GPE_UUID           = "Indicator-4-v1-DataSource-1-GPE-NOTIME-NOGEO";
    private static final String              INDICATOR4_GPE_JSON_DATA         = readFile("json/data_fixed.json");
    private static final String              INDICATOR4_VERSION               = "1.000";

    /* Indicator instances */
    /* GEO Granularity provinces, time granularity yearly*/
    private static final String              INDICATOR_INSTANCE_11_UUID       = "IndSys-1-v1-IInstance-11";
    
    /* NOT geographic restrictions, TIME GRANULARITY monthly */
    private static final String              INDICATOR_INSTANCE_12_UUID       = "IndSys-1-v1-IInstance-12";

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

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_DRAFT_VERSION);

        List<GeographicalGranularity> granularities = indicatorsDataService.retrieveGeographicalGranularitiesInIndicator(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_DRAFT_VERSION);
        List<String> codes = getGeographicalGranularitiesCodes(granularities);
        String[] expectedCodes = new String[]{"COUNTRIES", "COMMUNITIES", "PROVINCES"};
        checkElementsInCollection(expectedCodes, codes);
    }

    @Test
    public void testRetrieveGeographicalGranularitiesInIndicatorFixedValue() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR4_DS_GPE_UUID))).thenReturn(INDICATOR4_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR4_UUID, INDICATOR4_VERSION);

        List<GeographicalGranularity> granularities = indicatorsDataService.retrieveGeographicalGranularitiesInIndicator(getServiceContextAdministrador(), INDICATOR4_UUID, INDICATOR4_VERSION);
        List<String> codes = getGeographicalGranularitiesCodes(granularities);
        String[] expectedCodes = new String[]{"COUNTRIES"};
        checkElementsInCollection(expectedCodes, codes);
    }

    @Test
    public void testRetrieveGeographicalGranularitiesInIndicatorPublished() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_PUBLISHED_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_PUBLISHED_VERSION);

        List<GeographicalGranularity> granularities = indicatorsDataService.retrieveGeographicalGranularitiesInIndicatorPublished(getServiceContextAdministrador(), INDICATOR1_UUID);
        List<String> codes = getGeographicalGranularitiesCodes(granularities);
        String[] expectedCodes = new String[]{"COUNTRIES", "COMMUNITIES", "PROVINCES"};
        checkElementsInCollection(expectedCodes, codes);
    }

    @Test
    public void testRetrieveGeographicalGranularitiesInIndicatorInstance() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_PUBLISHED_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_PUBLISHED_VERSION);

        List<GeographicalGranularity> granularities = indicatorsDataService.retrieveGeographicalGranularitiesInIndicatorInstance(getServiceContextAdministrador(), INDICATOR_INSTANCE_11_UUID);
        List<String> codes = getGeographicalGranularitiesCodes(granularities);
        String[] expectedCodes = new String[]{"PROVINCES"};
        checkElementsInCollection(expectedCodes, codes);
    }
    
    @Test
    public void testRetrieveGeographicalGranularitiesInIndicatorInstanceNotGeoFilter() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_PUBLISHED_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_PUBLISHED_VERSION);
        
        List<GeographicalGranularity> granularities = indicatorsDataService.retrieveGeographicalGranularitiesInIndicatorInstance(getServiceContextAdministrador(), INDICATOR_INSTANCE_12_UUID);
        List<String> codes = getGeographicalGranularitiesCodes(granularities);
        String[] expectedCodes = new String[]{"COUNTRIES", "COMMUNITIES", "PROVINCES"};
        checkElementsInCollection(expectedCodes, codes);
    }
    

    /* GEOGRAPHICAL VALUES BY GRANULARITY */

    @Test
    public void testRetrieveGeographicalValuesByGranularityInIndicator() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_DRAFT_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_DRAFT_VERSION);

        List<GeographicalValue> geoValues = indicatorsDataService.retrieveGeographicalValuesByGranularityInIndicator(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_DRAFT_VERSION,
                GEO_GRANULARITY_COUNTRIES);
        List<String> countryCodes = getGeographicalValuesCodes(geoValues);
        String[] expectedCountryCodes = new String[]{"ES"};
        checkElementsInCollection(expectedCountryCodes, countryCodes);

        geoValues = indicatorsDataService.retrieveGeographicalValuesByGranularityInIndicator(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_DRAFT_VERSION, GEO_GRANULARITY_COMMUNITIES);
        List<String> communityCodes = getGeographicalValuesCodes(geoValues);
        String[] expectedCommunityCodes = new String[]{"ES61"};
        checkElementsInCollection(expectedCommunityCodes, communityCodes);

        geoValues = indicatorsDataService.retrieveGeographicalValuesByGranularityInIndicator(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_DRAFT_VERSION, GEO_GRANULARITY_PROVINCES);
        List<String> provinceCodes = getGeographicalValuesCodes(geoValues);
        String[] expectedProvinceCodes = new String[]{"ES611", "ES612", "ES613"};
        checkElementsInCollection(expectedProvinceCodes, provinceCodes);
    }

    @Test
    public void testRetrieveGeographicalValuesByGranularityInIndicatorFixedValue() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR4_DS_GPE_UUID))).thenReturn(INDICATOR4_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR4_UUID, INDICATOR4_VERSION);

        List<GeographicalValue> geoValues = indicatorsDataService.retrieveGeographicalValuesByGranularityInIndicator(getServiceContextAdministrador(), INDICATOR4_UUID, INDICATOR4_VERSION,
                GEO_GRANULARITY_COUNTRIES);
        List<String> countryCodes = getGeographicalValuesCodes(geoValues);
        String[] expectedCountryCodes = new String[]{"ES"};
        checkElementsInCollection(expectedCountryCodes, countryCodes);

        geoValues = indicatorsDataService.retrieveGeographicalValuesByGranularityInIndicator(getServiceContextAdministrador(), INDICATOR4_UUID, INDICATOR4_VERSION, GEO_GRANULARITY_COMMUNITIES);
        List<String> communityCodes = getGeographicalValuesCodes(geoValues);
        String[] expectedCommunityCodes = new String[]{};
        checkElementsInCollection(expectedCommunityCodes, communityCodes);

        geoValues = indicatorsDataService.retrieveGeographicalValuesByGranularityInIndicator(getServiceContextAdministrador(), INDICATOR4_UUID, INDICATOR4_VERSION, GEO_GRANULARITY_PROVINCES);
        List<String> provinceCodes = getGeographicalValuesCodes(geoValues);
        String[] expectedProvinceCodes = new String[]{};
        checkElementsInCollection(expectedProvinceCodes, provinceCodes);
    }

    @Test
    public void testRetrieveGeographicalValuesByGranularityInIndicatorPublished() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_PUBLISHED_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_PUBLISHED_VERSION);

        List<GeographicalValue> geoValues = indicatorsDataService.retrieveGeographicalValuesByGranularityInIndicatorPublished(getServiceContextAdministrador(), INDICATOR1_UUID, GEO_GRANULARITY_COUNTRIES);
        List<String> countryCodes = getGeographicalValuesCodes(geoValues);
        String[] expectedCountryCodes = new String[]{"ES"};
        checkElementsInCollection(expectedCountryCodes, countryCodes);

        geoValues = indicatorsDataService.retrieveGeographicalValuesByGranularityInIndicatorPublished(getServiceContextAdministrador(), INDICATOR1_UUID, GEO_GRANULARITY_COMMUNITIES);
        List<String> communityCodes = getGeographicalValuesCodes(geoValues);
        String[] expectedCommunityCodes = new String[]{"ES61"};
        checkElementsInCollection(expectedCommunityCodes, communityCodes);

        geoValues = indicatorsDataService.retrieveGeographicalValuesByGranularityInIndicatorPublished(getServiceContextAdministrador(), INDICATOR1_UUID, GEO_GRANULARITY_PROVINCES);
        List<String> provinceCodes = getGeographicalValuesCodes(geoValues);
        String[] expectedProvinceCodes = new String[]{"ES611", "ES612", "ES613"};
        checkElementsInCollection(expectedProvinceCodes, provinceCodes);
    }

    /* GEOGRAPHICAL VALUES */

    @Test
    public void testRetrieveGeographicalValuesInIndicator() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_DRAFT_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_DRAFT_VERSION);

        List<GeographicalValue> geoValues = indicatorsDataService.retrieveGeographicalValuesInIndicator(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_DRAFT_VERSION);
        List<String> countryCodes = getGeographicalValuesCodes(geoValues);
        String[] expectedCountryCodes = new String[]{"ES", "ES61", "ES611", "ES612", "ES613"};
        checkElementsInCollection(expectedCountryCodes, countryCodes);
    }

    @Test
    public void testRetrieveGeographicalValuesInIndicatorFixedValue() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR4_DS_GPE_UUID))).thenReturn(INDICATOR4_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR4_UUID, INDICATOR4_VERSION);

        List<GeographicalValue> geoValues = indicatorsDataService.retrieveGeographicalValuesInIndicator(getServiceContextAdministrador(), INDICATOR4_UUID, INDICATOR4_VERSION);
        List<String> countryCodes = getGeographicalValuesCodes(geoValues);
        String[] expectedCountryCodes = new String[]{"ES"};
        checkElementsInCollection(expectedCountryCodes, countryCodes);
    }

    @Test
    public void testRetrieveGeographicalValuesInIndicatorPublished() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_PUBLISHED_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_PUBLISHED_VERSION);

        List<GeographicalValue> geoValues = indicatorsDataService.retrieveGeographicalValuesInIndicatorPublished(getServiceContextAdministrador(), INDICATOR1_UUID);
        List<String> countryCodes = getGeographicalValuesCodes(geoValues);
        String[] expectedCountryCodes = new String[]{"ES", "ES61", "ES611", "ES612", "ES613"};
        checkElementsInCollection(expectedCountryCodes, countryCodes);
    }

    @Test
    public void testRetrieveGeographicalValuesInIndicatorInstance() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_PUBLISHED_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_PUBLISHED_VERSION);
        
        List<GeographicalValue> geoValues = indicatorsDataService.retrieveGeographicalValuesInIndicatorInstance(getServiceContextAdministrador(), INDICATOR_INSTANCE_11_UUID);
        List<String> geoCodes = getGeographicalValuesCodes(geoValues);
        String[] expectedCodes = new String[]{"ES611", "ES612", "ES613"};
        checkElementsInCollection(expectedCodes, geoCodes);
    }
    
    @Test
    public void testRetrieveGeographicalValuesInIndicatorInstanceNotGeo() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_PUBLISHED_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);
        
        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_PUBLISHED_VERSION);
        
        List<GeographicalValue> geoValues = indicatorsDataService.retrieveGeographicalValuesInIndicatorInstance(getServiceContextAdministrador(), INDICATOR_INSTANCE_12_UUID);
        List<String> geoCodes = getGeographicalValuesCodes(geoValues);
        String[] expectedCodes = new String[]{"ES", "ES61","ES611", "ES612", "ES613"};
        checkElementsInCollection(expectedCodes, geoCodes);
    }
    

    /* TIME GRANULARITIES */
    
    @Test
    public void testRetrieveTimeGranularitiesInIndicator() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_DRAFT_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_DRAFT_VERSION);

        List<TimeGranularity> timeGranularities = indicatorsDataService.retrieveTimeGranularitiesInIndicator(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_DRAFT_VERSION);
        List<String> granularitiesCodes = getTimeGranularitiesNames(timeGranularities);
        String[] expectedGranularities = new String[]{TimeGranularityEnum.MONTHLY.name(), TimeGranularityEnum.YEARLY.name()};
        checkElementsInCollection(expectedGranularities, granularitiesCodes);
    }

    @Test
    public void testRetrieveTimeGranularitiesInIndicatorFixedValue() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR4_DS_GPE_UUID))).thenReturn(INDICATOR4_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR4_UUID, INDICATOR4_VERSION);

        List<TimeGranularity> timeGranularities = indicatorsDataService.retrieveTimeGranularitiesInIndicator(getServiceContextAdministrador(), INDICATOR4_UUID, INDICATOR4_VERSION);
        List<String> granularitiesCodes = getTimeGranularitiesNames(timeGranularities);
        String[] expectedGranularities = new String[]{TimeGranularityEnum.YEARLY.name()};
        checkElementsInCollection(expectedGranularities, granularitiesCodes);
    }

    @Test
    public void testRetrieveTimeGranularitiesInIndicatorPublished() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_PUBLISHED_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_PUBLISHED_VERSION);

        List<TimeGranularity> timeGranularities = indicatorsDataService.retrieveTimeGranularitiesInIndicatorPublished(getServiceContextAdministrador(), INDICATOR1_UUID);
        List<String> granularitiesCodes = getTimeGranularitiesNames(timeGranularities);
        String[] expectedGranularities = new String[]{TimeGranularityEnum.MONTHLY.name(), TimeGranularityEnum.YEARLY.name()};
        checkElementsInCollection(expectedGranularities, granularitiesCodes);
    }

    @Test
    public void testRetrieveTimeGranularitiesInIndicatorInstance() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_PUBLISHED_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);
        
        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_PUBLISHED_VERSION);
        
        List<TimeGranularity> timeGranularities = indicatorsDataService.retrieveTimeGranularitiesInIndicatorInstance(getServiceContextAdministrador(), INDICATOR_INSTANCE_12_UUID);
        List<String> granularitiesCodes = getTimeGranularitiesNames(timeGranularities);
        String[] expectedCodes = new String[] {"MONTHLY"};
        checkElementsInCollection(expectedCodes, granularitiesCodes);    
    }
    

    /* TIME VALUES BY GRANULARITY */
    @Test
    public void testRetrieveTimeValuesByGranularityInIndicator() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_DRAFT_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_DRAFT_VERSION);

        List<TimeValue> yearTimeValues = indicatorsDataService.retrieveTimeValuesByGranularityInIndicator(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_DRAFT_VERSION, TimeGranularityEnum.YEARLY);
        List<String> yearValues = getTimeValuesCodes(yearTimeValues);
        String[] expectedYearValues = new String[]{"2010"};
        checkElementsInCollection(expectedYearValues, yearValues);

        List<TimeValue> monthTimeValues = indicatorsDataService.retrieveTimeValuesByGranularityInIndicator(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_DRAFT_VERSION, TimeGranularityEnum.MONTHLY);
        List<String> monthValues = getTimeValuesCodes(monthTimeValues);
        String[] expectedMonthValues = new String[]{"2011M01", "2010M12", "2010M11", "2010M10", "2010M09"};
        checkElementsInCollection(expectedMonthValues, monthValues);

        // NOT EXIST
        List<TimeValue> dayTimeValues = indicatorsDataService.retrieveTimeValuesByGranularityInIndicator(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_DRAFT_VERSION, TimeGranularityEnum.DAILY);
        List<String> dayValues = getTimeValuesCodes(dayTimeValues);
        String[] expectedDayValues = new String[]{};
        checkElementsInCollection(expectedDayValues, dayValues);
    }

    @Test
    public void testRetrieveTimeValuesByGranularityInIndicatorFixedValue() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR4_DS_GPE_UUID))).thenReturn(INDICATOR4_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR4_UUID, INDICATOR4_VERSION);

        List<TimeValue> yearTimeValues = indicatorsDataService.retrieveTimeValuesByGranularityInIndicator(getServiceContextAdministrador(), INDICATOR4_UUID, INDICATOR4_VERSION, TimeGranularityEnum.YEARLY);
        List<String> yearValues = getTimeValuesCodes(yearTimeValues);
        String[] expectedYearValues = new String[]{"2010"};
        checkElementsInCollection(expectedYearValues, yearValues);

        // NOT EXIST
        List<TimeValue> monthTimeValues = indicatorsDataService.retrieveTimeValuesByGranularityInIndicator(getServiceContextAdministrador(), INDICATOR4_UUID, INDICATOR4_VERSION, TimeGranularityEnum.MONTHLY);
        List<String> monthValues = getTimeValuesCodes(monthTimeValues);
        String[] expectedMonthValues = new String[]{};
        checkElementsInCollection(expectedMonthValues, monthValues);

        // NOT EXIST
        List<TimeValue> dayTimeValues = indicatorsDataService.retrieveTimeValuesByGranularityInIndicator(getServiceContextAdministrador(), INDICATOR4_UUID, INDICATOR4_VERSION, TimeGranularityEnum.DAILY);
        List<String> dayValues = getTimeValuesCodes(dayTimeValues);
        String[] expectedDayValues = new String[]{};
        checkElementsInCollection(expectedDayValues, dayValues);
    }

    @Test
    public void testRetrieveTimeValuesByGranularityInIndicatorPublished() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_PUBLISHED_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_PUBLISHED_VERSION);

        List<TimeValue> yearTimeValues = indicatorsDataService.retrieveTimeValuesByGranularityInIndicatorPublished(getServiceContextAdministrador(), INDICATOR1_UUID, TimeGranularityEnum.YEARLY);
        List<String> yearValues = getTimeValuesCodes(yearTimeValues);
        String[] expectedYearValues = new String[]{"2010"};
        checkElementsInCollection(expectedYearValues, yearValues);

        List<TimeValue> monthTimeValues = indicatorsDataService.retrieveTimeValuesByGranularityInIndicatorPublished(getServiceContextAdministrador(), INDICATOR1_UUID, TimeGranularityEnum.MONTHLY);
        List<String> monthValues = getTimeValuesCodes(monthTimeValues);
        String[] expectedMonthValues = new String[]{"2011M01", "2010M12", "2010M11", "2010M10", "2010M09"};
        checkElementsInCollection(expectedMonthValues, monthValues);

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

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_DRAFT_VERSION);

        List<TimeValue> timeValues = indicatorsDataService.retrieveTimeValuesInIndicator(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_DRAFT_VERSION);
        List<String> timeCodes = getTimeValuesCodes(timeValues);
        String[] expectedValues = new String[]{"2011M01", "2010", "2010M12", "2010M11", "2010M10", "2010M09"};
        checkElementsInCollection(expectedValues, timeCodes);
    }

    @Test
    public void testRetrieveTimeValuesInIndicatorFixedValue() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR4_DS_GPE_UUID))).thenReturn(INDICATOR4_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR4_UUID, INDICATOR4_VERSION);

        List<TimeValue> timeValues = indicatorsDataService.retrieveTimeValuesInIndicator(getServiceContextAdministrador(), INDICATOR4_UUID, INDICATOR4_VERSION);
        List<String> timeCodes = getTimeValuesCodes(timeValues);
        String[] expectedValues = new String[]{"2010"};
        checkElementsInCollection(expectedValues, timeCodes);
    }

    @Test
    public void testRetrieveTimeValuesInIndicatorPublished() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_PUBLISHED_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_PUBLISHED_VERSION);

        List<TimeValue> timeValues = indicatorsDataService.retrieveTimeValuesInIndicatorPublished(getServiceContextAdministrador(), INDICATOR1_UUID);
        List<String> timeCodes = getTimeValuesCodes(timeValues);
        String[] expectedValues = new String[]{"2011M01", "2010", "2010M12", "2010M11", "2010M10", "2010M09"};
        checkElementsInCollection(expectedValues, timeCodes);
    }

    @Test
    public void testRetrieveTimeValuesInIndicatorInstance() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_PUBLISHED_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);
        
        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_PUBLISHED_VERSION);
        
        List<TimeValue> timeValues = indicatorsDataService.retrieveTimeValuesInIndicatorInstance(getServiceContextAdministrador(), INDICATOR_INSTANCE_12_UUID);
        List<String> timeCodes = getTimeValuesCodes(timeValues);
        String[] expectedCodes = new String[] {"2011M01", "2010M12", "2010M11", "2010M10", "2010M09"};
        checkElementsInCollection(expectedCodes, timeCodes);    
    }

    /* MEASURE VALUES */

    @Test
    public void testRetrieveMeasureValuesInIndicator() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_DRAFT_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_DRAFT_VERSION);

        List<MeasureValue> measures = indicatorsDataService.retrieveMeasureValuesInIndicator(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_DRAFT_VERSION);
        List<String> measuresNames = getMeasureNames(measures);
        String[] expectedMeasures = new String[]{MeasureDimensionTypeEnum.ABSOLUTE.name()};
        checkElementsInCollection(expectedMeasures, measuresNames);
    }

    @Test
    public void testRetrieveMeasureValuesInIndicatorWithRates() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR4_DS_GPE_UUID))).thenReturn(INDICATOR4_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR4_UUID, INDICATOR4_VERSION);

        List<MeasureValue> measures = indicatorsDataService.retrieveMeasureValuesInIndicator(getServiceContextAdministrador(), INDICATOR4_UUID, INDICATOR4_VERSION);
        List<String> measuresNames = getMeasureNames(measures);
        String[] expectedMeasures = new String[]{MeasureDimensionTypeEnum.ABSOLUTE.name(), MeasureDimensionTypeEnum.ANNUAL_PERCENTAGE_RATE.name()};
        checkElementsInCollection(expectedMeasures, measuresNames);
    }

    @Test
    public void testRetrieveMeasureValuesInIndicatorPublished() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_PUBLISHED_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_PUBLISHED_VERSION);

        List<MeasureValue> measures = indicatorsDataService.retrieveMeasureValuesInIndicatorPublished(getServiceContextAdministrador(), INDICATOR1_UUID);
        List<String> measuresNames = getMeasureNames(measures);
        String[] expectedMeasures = new String[]{MeasureDimensionTypeEnum.ABSOLUTE.name()};
        checkElementsInCollection(expectedMeasures, measuresNames);
    }

    @Test
    public void testRetrieveMeasureValuesInIndicatorInstance() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_PUBLISHED_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);
        
        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_PUBLISHED_VERSION);
        
        List<MeasureValue> measureValues = indicatorsDataService.retrieveMeasureValuesInIndicatorInstance(getServiceContextAdministrador(), INDICATOR_INSTANCE_12_UUID);
        List<String> measuresNames = getMeasureNames(measureValues);
        String[] expectedMeasures = new String[]{MeasureDimensionTypeEnum.ABSOLUTE.name()};
        checkElementsInCollection(expectedMeasures, measuresNames);   
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/IndicatorsDataServiceTest_RetrieveGeoTime.xml";
    }

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
