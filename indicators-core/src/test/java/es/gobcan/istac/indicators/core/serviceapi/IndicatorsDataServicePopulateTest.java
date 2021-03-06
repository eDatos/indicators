package es.gobcan.istac.indicators.core.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceObservationDto;
import es.gobcan.istac.edatos.dataset.repository.service.DatasetRepositoriesServiceFacade;
import es.gobcan.istac.indicators.core.domain.DataGpeRepository;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorVersionRepository;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorDataAttributeTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorDataDimensionTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.MeasureDimensionTypeEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionParameters;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.serviceimpl.IndicatorsDataServiceImpl;
import es.gobcan.istac.indicators.core.util.IndicatorsVersionUtils;

/**
 * Spring based transactional test with DbUnit support.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/include/indicators-data-service-populate-mockito.xml", "classpath:spring/applicationContext-test.xml"})
@TransactionConfiguration(defaultRollback = true, transactionManager = "txManager")
@Transactional
public class IndicatorsDataServicePopulateTest extends IndicatorsDataBaseTest {

    /* Has geographic and time variables */
    private static final String              INDICATOR1_UUID                          = "Indicator-1";
    private static final String              INDICATOR1_DS_GPE_UUID                   = "Indicator-1-v1-DataSource-1-GPE-TIME-GEO";
    private static final String              INDICATOR1_GPE_JSON_DATA                 = readFile("json/data_temporal_spatials.json");
    private static final String              INDICATOR1_VERSION                       = IndicatorsDataBaseTest.INIT_VERSION;

    /* Has geographic variable */
    private static final String              INDICATOR2_UUID                          = "Indicator-2";
    private static final String              INDICATOR2_DS_GPE_UUID                   = "Indicator-2-v1-DataSource-1-GPE-GEO";
    private static final String              INDICATOR2_GPE_JSON_DATA                 = readFile("json/data_spatials.json");
    private static final String              INDICATOR2_VERSION                       = IndicatorsDataBaseTest.INIT_VERSION;

    /* Has time variable */
    private static final String              INDICATOR3_UUID                          = "Indicator-3";
    private static final String              INDICATOR3_DS_UUID                       = "Indicator-3-v1-DataSource-1";
    private static final String              INDICATOR3_DS_GPE_UUID                   = "Indicator-3-v1-DataSource-1-GPE-TIME";
    private static final String              INDICATOR3_GPE_JSON_DATA                 = readFile("json/data_temporals.json");
    private static final String              INDICATOR3_GPE_JSON_DATA_AMBIGUOUS       = readFile("json/data_temporals_ambiguous.json");
    private static final String              INDICATOR3_VERSION                       = IndicatorsDataBaseTest.INIT_VERSION;

    /* Has no geographic and temporal variables */
    private static final String              INDICATOR4_UUID                          = "Indicator-4";
    private static final String              INDICATOR4_DS_GPE_UUID                   = "Indicator-4-v1-DataSource-1-GPE-NOTIME-NOGEO";
    private static final String              INDICATOR4_GPE_JSON_DATA                 = readFile("json/data_fixed.json");
    private static final String              INDICATOR4_VERSION                       = IndicatorsDataBaseTest.INIT_VERSION;

    /* uses contvariable for absolute method */
    private static final String              INDICATOR5_UUID                          = "Indicator-5";
    private static final String              INDICATOR5_DS_GPE_UUID                   = "Indicator-5-v1-DataSource-1-GPE-NOTIME-NOGEO-CONTVARIABLE";
    private static final String              INDICATOR5_GPE_JSON_DATA                 = readFile("json/data_fixed_contvariable.json");
    private static final String              INDICATOR5_VERSION                       = IndicatorsDataBaseTest.INIT_VERSION;

    /* Has "." in data */
    private static final String              INDICATOR6_UUID                          = "Indicator-6";
    private static final String              INDICATOR6_DS_GPE_UUID                   = "Indicator-6-v1-DataSource-1-GPE-DOTS";
    private static final String              INDICATOR6_GPE_JSON_DATA                 = readFile("json/data_dots.json");
    private static final String              INDICATOR6_VERSION                       = IndicatorsDataBaseTest.INIT_VERSION;

    /* Calculates all rates */
    private static final String              INDICATOR7_UUID                          = "Indicator-7";
    private static final String              INDICATOR7_DS_GPE_UUID                   = "Indicator-7-v1-DataSource-1-GPE-TIME";
    private static final String              INDICATOR7_GPE_JSON_DATA_29FEB           = readFile("json/data_temporals_calculate_29feb.json");
    private static final String              INDICATOR7_GPE_JSON_DATA                 = readFile("json/data_temporals_calculate.json");
    private static final String              INDICATOR7_GPE_JSON_DATA_AMBIGUOUS       = readFile("json/data_temporals_calculate_ambiguous.json");
    private static final String              INDICATOR7_VERSION                       = IndicatorsDataBaseTest.INIT_VERSION;

    /* Calculates all rates from two different data sources */
    private static final String              INDICATOR8_UUID                          = "Indicator-8";
    private static final String              INDICATOR8_DS1_GPE_UUID                  = "Indicator-8-v1-DataSource-1-GPE-TIME";
    private static final String              INDICATOR8_DS2_GPE_UUID                  = "Indicator-8-v1-DataSource-2-GPE-TIME";
    private static final String              INDICATOR8_GPE_JSON_DATA1                = readFile("json/data_increase_temporal_part1.json");
    private static final String              INDICATOR8_GPE_JSON_DATA2                = readFile("json/data_increase_temporal_part2.json");
    private static final String              INDICATOR8_GPE_JSON_DECIMALS_DATA1       = readFile("json/data_increase_temporal_decimals_part1.json");
    private static final String              INDICATOR8_GPE_JSON_DECIMALS_DATA2       = readFile("json/data_increase_temporal_decimals_part2.json");
    private static final String              INDICATOR8_VERSION                       = IndicatorsDataBaseTest.INIT_VERSION;

    /* Loads all rates from one data source using contvariable */
    private static final String              INDICATOR9_UUID                          = "Indicator-9";
    private static final String              INDICATOR9_DS_GPE_UUID                   = "Indicator-9-v1-DataSource-1-GPE-TIME";
    private static final String              INDICATOR9_GPE_JSON_DATA                 = readFile("json/data_allrates_contvariable.json");
    private static final String              INDICATOR9_VERSION                       = IndicatorsDataBaseTest.INIT_VERSION;

    /* Loads all rates, all from different data sources, using OBS_VALUE */
    private static final String              INDICATOR10_UUID                         = "Indicator-10";
    private static final String              INDICATOR10_DS1_GPE_UUID                 = "Indicator-10-v1-DataSource-1-GPE-ABSOLUTE";
    private static final String              INDICATOR10_DS2_GPE_UUID                 = "Indicator-10-v1-DataSource-2-GPE-ANNUAL-PERC";
    private static final String              INDICATOR10_DS3_GPE_UUID                 = "Indicator-10-v1-DataSource-3-GPE-ANNUAL-PUNT";
    private static final String              INDICATOR10_DS4_GPE_UUID                 = "Indicator-10-v1-DataSource-4-GPE-INTER-PERC";
    private static final String              INDICATOR10_DS5_GPE_UUID                 = "Indicator-10-v1-DataSource-5-GPE-INTER-PUNT";
    private static final String              INDICATOR10_GPE_JSON_DATA1               = readFile("json/data_rates_obsvalue_split1_absolute.json");
    private static final String              INDICATOR10_GPE_JSON_DATA2               = readFile("json/data_rates_obsvalue_split2_annual_percentage.json");
    private static final String              INDICATOR10_GPE_JSON_DATA3               = readFile("json/data_rates_obsvalue_split3_annual_puntual.json");
    private static final String              INDICATOR10_GPE_JSON_DATA4               = readFile("json/data_rates_obsvalue_split4_inter_percentage.json");
    private static final String              INDICATOR10_GPE_JSON_DATA5               = readFile("json/data_rates_obsvalue_split5_inter_puntual.json");
    private static final String              INDICATOR10_VERSION                      = IndicatorsDataBaseTest.INIT_VERSION;

    /* Error wrong absoluteMethod without contvariable */
    private static final String              INDICATOR11_UUID                         = "Indicator-11";
    private static final String              INDICATOR11_DS_GPE_UUID                  = "Indicator-11-v1-DataSource-1-GPE-TIME-GEO";
    private static final String              INDICATOR11_GPE_JSON_DATA                = readFile("json/data_temporal_spatials.json");

    /* Error wrong absoluteMethod with contvariable */
    private static final String              INDICATOR12_UUID                         = "Indicator-12";
    private static final String              INDICATOR12_DS_GPE_UUID                  = "Indicator-12-v1-DataSource-1-GPE-NOTIME-NOGEO-CONTVARIABLE";
    private static final String              INDICATOR12_GPE_JSON_DATA                = readFile("json/data_fixed_contvariable.json");

    /* Calculates all rates, with some dots values */
    private static final String              INDICATOR13_UUID                         = "Indicator-13";
    private static final String              INDICATOR13_DS_GPE_UUID                  = "Indicator-13-v1-DataSource-1-GPE-TIME";
    private static final String              INDICATOR13_GPE_JSON_DATA                = readFile("json/data_temporals_calculate_dots.json");
    private static final String              INDICATOR13_VERSION                      = IndicatorsDataBaseTest.INIT_VERSION;

    /* Error getting data from json EMPTY */
    private static final String              INDICATOR14_UUID                         = "Indicator-14";
    private static final String              INDICATOR14_DS_UUID                      = "Indicator-14-v1-DataSource-1";
    private static final String              INDICATOR14_DS_GPE_UUID                  = "Indicator-14-v1-DataSource-1-GPE-NOT-EXISTS";
    private static final String              INDICATOR14_GPE_JSON_DATA                = null;

    /* Error getting data from json */
    private static final String              INDICATOR15_UUID                         = "Indicator-15";
    private static final String              INDICATOR15_DS_UUID                      = "Indicator-15-v1-DataSource-1";
    private static final String              INDICATOR15_DS_GPE_UUID                  = "Indicator-15-v1-DataSource-1-GPE-WRONG";
    private static final String              INDICATOR15_GPE_JSON_DATA                = readFile("json/data_temporals_wrong_format.json");

    /* A PUBLISHED VERSION */
    private static final String              INDICATOR16_UUID                         = "Indicator-16";
    private static final String              INDICATOR16_DS_GPE_UUID                  = "Indicator-16-v1-DataSource-1-GPE-TIME";
    private static final String              INDICATOR16_GPE_JSON_DATA                = readFile("json/data_temporals.json");
    private static final String              INDICATOR16_VERSION                      = IndicatorsDataBaseTest.INIT_VERSION;

    /* Indicator with a geographic or time problems depending on the json */
    private static final String              INDICATOR17_UUID                         = "Indicator-17";
    private static final String              INDICATOR17_DS_GPE_UUID                  = "Indicator-17-v1-DataSource-1-GPE-TIME-GEO";
    /* Geographic variable not exist */
    private static final String              INDICATOR17_GPE_JSON_DATA_GEO_NOT_EXIST  = readFile("json/data_temporals.json");
    /* Geographic variable is not geographic */
    private static final String              INDICATOR17_GPE_JSON_DATA_GEO_NOT_GEO    = readFile("json/data_temporals_notspatials.json");
    /* Time variable not exist */
    private static final String              INDICATOR17_GPE_JSON_DATA_TEMP_NOT_EXIST = readFile("json/data_spatials.json");
    /* Time variable is not temporal */
    private static final String              INDICATOR17_GPE_JSON_DATA_TEMP_NOT_TEMP  = readFile("json/data_nottemporals_spatials.json");
    private static final String              INDICATOR17_VERSION                      = IndicatorsDataBaseTest.INIT_VERSION;

    /* Indicator with a compatibility issues depending on the json */
    private static final String              INDICATOR18_UUID                         = "Indicator-18";
    private static final String              INDICATOR18_DS_GPE_UUID                  = "Indicator-18-v1-DataSource-1-GPE-TIMEVAL-GEOVAL";
    /* Geographic variable not exist */
    private static final String              INDICATOR18_GPE_JSON_DATA                = readFile("json/data_temporal_spatials.json");
    private static final String              INDICATOR18_VERSION                      = IndicatorsDataBaseTest.INIT_VERSION;

    /* Indicator with compatibility issues */
    private static final String              INDICATOR19_UUID                         = "Indicator-19";
    private static final String              INDICATOR19_DS_GPE_UUID                  = "Indicator-19-v1-DataSource-1-GPE-TIME-GEO";
    private static final String              INDICATOR19_GPE_JSON_DATA                = readFile("json/data_temporals_spatials_contvariable.json");
    private static final String              INDICATOR19_VERSION                      = IndicatorsDataBaseTest.INIT_VERSION;

    /* Indicator with a compatibility issues depending on the json */
    private static final String              INDICATOR20_UUID                         = "Indicator-20";
    private static final String              INDICATOR20_DS_GPE_UUID                  = "Indicator-20-v1-DataSource-1-GPE-TIME-GEO";
    private static final String              INDICATOR20_GPE_JSON_DATA                = readFile("json/data_temporal_spatials.json");
    private static final String              INDICATOR20_VERSION                      = IndicatorsDataBaseTest.INIT_VERSION;

    /* Indicator with a compatibility issues related to other variables */
    private static final String              INDICATOR21_UUID                         = "Indicator-21";
    private static final String              INDICATOR21_DS_GPE_UUID                  = "Indicator-21-v1-DataSource-1-GPE-TIME-GEO";
    private static final String              INDICATOR21_GPE_JSON_DATA                = readFile("json/data_temporals_spatials_contvariable.json");
    private static final String              INDICATOR21_VERSION                      = IndicatorsDataBaseTest.INIT_VERSION;

    /* Error indicator has no data sources */
    private static final String              INDICATOR23_UUID                         = "Indicator-23";

    /* too many digits in one observation */
    private static final String              INDICATOR24_UUID                         = "Indicator-24";
    private static final String              INDICATOR24_DS_GPE_UUID                  = "Indicator-24-v1-DataSource-1-GPE-TIME";
    private static final String              INDICATOR24_GPE_JSON_DATA                = readFile("json/data_temporals_calculate.json");
    private static final String              INDICATOR24_VERSION                      = IndicatorsDataBaseTest.INIT_VERSION;

    /* DIFFUSION AND PRODUCTION VERSION (consecutive minor) */
    private static final String              INDICATOR25_UUID                         = "Indicator-25";
    private static final String              INDICATOR25_DS_GPE_UUID_V1               = "Indicator-25-v1-DataSource-1-GPE-TIME";
    private static final String              INDICATOR25_DS_GPE_UUID_V2               = "Indicator-25-v2-DataSource-1-GPE-TIME";
    private static final String              INDICATOR25_GPE_JSON_DATA                = readFile("json/data_temporals.json");
    private static final String              INDICATOR25_DIFFUSION_VERSION            = IndicatorsDataBaseTest.INIT_VERSION;
    private static final String              INDICATOR25_PRODUCTION_VERSION           = IndicatorsDataBaseTest.INIT_VERSION_MINOR_INCREMENT;

    /* DIFFUSION AND PRODUCTION VERSION (major) */
    private static final String              INDICATOR26_UUID                         = "Indicator-26";
    private static final String              INDICATOR26_DS_GPE_UUID_V1               = "Indicator-26-v1-DataSource-1-GPE-TIME";
    private static final String              INDICATOR26_DS_GPE_UUID_V2               = "Indicator-26-v2-DataSource-1-GPE-TIME";
    private static final String              INDICATOR26_GPE_JSON_DATA                = readFile("json/data_temporals.json");
    private static final String              INDICATOR26_DIFFUSION_VERSION            = IndicatorsDataBaseTest.INIT_VERSION;
    private static final String              INDICATOR26_PRODUCTION_VERSION           = IndicatorsDataBaseTest.SECOND_VERSION;

    /* DRAFT VERSION (major) */
    private static final String              INDICATOR27_UUID                         = "Indicator-27";

    /* DRAFT VERSION (major) */
    private static final String              INDICATOR28_UUID                         = "Indicator-28";
    private static final String              INDICATOR28_DS_GPE_UUID                  = "Indicator-28-v1-DataSource-1-GPE-TIME";
    private static final String              INDICATOR28_GPE_JSON_DATA                = readFile("json/data_temporals.json");

    /* DRAFT VERSION (major) */
    private static final String              INDICATOR29_UUID                         = "Indicator-29";
    private static final String              INDICATOR29_DS_GPE_UUID                  = "Indicator-29-v1-DataSource-1-GPE-TIME";
    private static final String              INDICATOR29_GPE_JSON_DATA                = readFile("json/data_temporals.json");

    /* INDICATORS SYSTEM */

    /* PRODUCTION & DIFFUSION VERSION (MAJOR DIFFERENCE) */
    private static final String              INDICATORS_SYSTEM1_UUID                  = "IndSys-1";
    private static final String              INDICATORS_SYSTEM1_DIFFUSION_VERSION     = IndicatorsDataBaseTest.INIT_VERSION;
    private static final String              INDICATORS_SYSTEM1_PRODUCTION_VERSION    = IndicatorsDataBaseTest.SECOND_VERSION;

    /* PRODUCTION & DIFFUSION VERSION CONSECUTIVE */
    private static final String              INDICATORS_SYSTEM2_UUID                  = "IndSys-2";
    private static final String              INDICATORS_SYSTEM2_DIFFUSION_VERSION     = IndicatorsDataBaseTest.INIT_VERSION;
    private static final String              INDICATORS_SYSTEM2_PRODUCTION_VERSION    = IndicatorsDataBaseTest.INIT_VERSION_MINOR_INCREMENT;

    /* PRODUCTION VERSION ONLY */
    private static final String              INDICATORS_SYSTEM3_UUID                  = "IndSys-3";
    private static final String              INDICATORS_SYSTEM3_PRODUCTION_VERSION    = IndicatorsDataBaseTest.INIT_VERSION_MINOR_INCREMENT;

    @Autowired
    protected IndicatorsDataService          indicatorsDataService;

    @Autowired
    private IndicatorsDataProviderService    indicatorsDataProviderService;

    @Autowired
    private DataGpeRepository                dataGpeRepository;

    @Autowired
    private DatasetRepositoriesServiceFacade datasetRepositoriesServiceFacade;

    @Autowired
    private IndicatorsService                indicatorsService;

    @Autowired
    private IndicatorsSystemsService         indicatorsSystemsService;

    @Autowired
    private IndicatorVersionRepository       indicatorVersionRepository;

    @Before
    public void initMock() throws MetamacException {
    }

    /*
     * Populate data using geographical and time variables provided by Jaxi
     * @see es.gobcan.istac.indicators.core.serviceapi.IndicatorsDataServiceTestBase#testPopulateIndicatorData()
     * Has been marked as inconsistent data, must be unmarked after populate
     */
    @Test
    public void testPopulateIndicatorData() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        Date beforePopulate = new Date();

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID);

        IndicatorVersion indicatorVersion = getIndicatorsService().retrieveIndicator(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_VERSION);

        // Populate date must be updated
        assertTrue(beforePopulate.before(indicatorVersion.getLastPopulateDate().toDate()));

        Map<String, List<String>> dimensionCodes = new HashMap<String, List<String>>();
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.TIME.name(), Arrays.asList("2011-01", "2010", "2010-12", "2010-11", "2010-10", "2010-09"));
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name(), Arrays.asList("ES", "ES61", "ES611", "ES612", "ES613"));
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.MEASURE.name(), Arrays.asList(MeasureDimensionTypeEnum.ABSOLUTE.name()));

        checkDataDimensions(dimensionCodes, INDICATOR1_UUID, INDICATOR1_VERSION);
        List<String> data = Arrays.asList("3585", "497", "56", "60", "49", "34413", "4546", "422", "487", "410", "2471", "329", "36", "25", "38", "2507", "347", "31", "44", "27", "2036", "297", "20",
                "46", "26", "2156", "321", "41", "29", "19");
        checkDataObservations(dimensionCodes, INDICATOR1_UUID, INDICATOR1_VERSION, data);
    }

    @Test
    public void testPopulateIndicatorDataDraftNoDatasources() throws Exception {
        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR27_UUID);
    }

    @Test
    public void testPopulateIndicatorDataDraftNoDecimalPlaces() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR28_DS_GPE_UUID))).thenReturn(INDICATOR28_GPE_JSON_DATA);
        try {
            indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR28_UUID);
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DATA_POPULATE_NO_DECIMAL_PLACES.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testPopulateIndicatorDataDuplicatedData() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR29_DS_GPE_UUID))).thenReturn(INDICATOR29_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR29_UUID);
    }

    @Test
    public void testPopulateIndicatorDataNoDatasources() throws Exception {
        List<MetamacExceptionItem> errors = indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR23_UUID);
        assertEquals(0, errors.size());
    }

    @Test
    public void testPopulateIndicatorDataUuuidNull() throws Exception {
        try {
            indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), null);
            fail("indicatorUuid should be required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.INDICATOR_UUID, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testPopulateIndicatorDataGPEUuidNotExist() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR14_DS_GPE_UUID))).thenReturn(INDICATOR14_GPE_JSON_DATA);

        List<MetamacExceptionItem> errors = indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR14_UUID);
        assertEquals(1, errors.size());
        assertEquals(ServiceExceptionType.DATA_POPULATE_RETRIEVE_DATA_EMPTY.getCode(), errors.get(0).getCode());
        assertEquals(2, errors.get(0).getMessageParameters().length);
        assertEquals(INDICATOR14_DS_GPE_UUID, errors.get(0).getMessageParameters()[0]);
        assertEquals(INDICATOR14_DS_UUID, errors.get(0).getMessageParameters()[1]);
    }

    @Test
    public void testPopulateIndicatorDataDataGPEError() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR15_DS_GPE_UUID))).thenReturn(INDICATOR15_GPE_JSON_DATA);

        List<MetamacExceptionItem> errors = indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR15_UUID);
        assertEquals(1, errors.size());
        assertEquals(ServiceExceptionType.DATA_POPULATE_RETRIEVE_DATA_ERROR.getCode(), errors.get(0).getCode());
        assertEquals(2, errors.get(0).getMessageParameters().length);
        assertEquals(INDICATOR15_DS_GPE_UUID, errors.get(0).getMessageParameters()[0]);
        assertEquals(INDICATOR15_DS_UUID, errors.get(0).getMessageParameters()[1]);
    }

    /*
     * Populate data error: absolute method can only be OBS_VALUE or a CONT_VARIABLE category
     * In this case there is no contvariable, so only valid option is OBS_VALUE
     */
    @Test
    public void testPopulateIndicatorDataWrongAbsoluteMethod() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR11_DS_GPE_UUID))).thenReturn(INDICATOR11_GPE_JSON_DATA);

        List<MetamacExceptionItem> errors = indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR11_UUID);
        assertNotNull(errors);
        assertEquals(1, errors.size());
        assertEquals(ServiceExceptionType.DATA_COMPATIBILITY_ABSMETHOD_NO_CONTVARIABLE_ILLEGAL.getCode(), errors.get(0).getCode());
    }

    /*
     * Populate data error: absolute method can only be OBS_VALUE or a CONT_VARIABLE category
     * In this case there is contvariable, so only valid option is a CONT_VARIABLE category
     */
    @Test
    public void testPopulateIndicatorDataWrongAbsoluteMethodContVariable() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR12_DS_GPE_UUID))).thenReturn(INDICATOR12_GPE_JSON_DATA);

        List<MetamacExceptionItem> errors = indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR12_UUID);
        assertNotNull(errors);
        assertEquals(1, errors.size());
        assertEquals(ServiceExceptionType.DATA_COMPATIBILITY_ABSMETHOD_CONTVARIABLE_ILLEGAL.getCode(), errors.get(0).getCode());
    }

    /*
     * Populate data with geographical variable provided by Jaxi, and fixed time value
     */
    @Test
    public void testPopulateIndicatorDataSpatialVariableTemporalValue() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR2_DS_GPE_UUID))).thenReturn(INDICATOR2_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR2_UUID);
        Map<String, List<String>> dimensionCodes = new HashMap<String, List<String>>();
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.TIME.name(), Arrays.asList("2010"));
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name(), Arrays.asList("ES", "ES61", "ES611", "ES612", "ES613"));
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.MEASURE.name(), Arrays.asList(MeasureDimensionTypeEnum.ABSOLUTE.name()));

        checkDataDimensions(dimensionCodes, INDICATOR2_UUID, INDICATOR2_VERSION);
        List<String> data = Arrays.asList("3585", "497", "56", "60", "49");
        checkDataObservations(dimensionCodes, INDICATOR2_UUID, INDICATOR2_VERSION, data);
    }

    /*
     * Populate data with time variable provided by Jaxi, and fixed geographical value
     */
    @Test
    public void testPopulateIndicatorDataSpatialValueTemporalVariable() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR3_DS_GPE_UUID))).thenReturn(INDICATOR3_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR3_UUID);
        Map<String, List<String>> dimensionCodes = new HashMap<String, List<String>>();
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.TIME.name(), Arrays.asList("2011-01", "2010", "2010-12", "2010-11", "2010-10", "2010-09"));
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name(), Arrays.asList("ES"));
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.MEASURE.name(), Arrays.asList(MeasureDimensionTypeEnum.ABSOLUTE.name()));

        checkDataDimensions(dimensionCodes, INDICATOR3_UUID, INDICATOR3_VERSION);
        List<String> data = Arrays.asList("3585", "34413", "2471", "2507", "2036", "2156");
        checkDataObservations(dimensionCodes, INDICATOR3_UUID, INDICATOR3_VERSION, data);
    }

    /*
     * Populate data with time variables that represent the same time period (2000 and 2000-A1)
     * This is NOT an error. The last value will prevail and on the dataset we??ll only have one entry
     */
    @Test
    public void testPopulateIndicatorDataSpatialValueTemporalVariableWithAmbiguousYear() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR3_DS_GPE_UUID))).thenReturn(INDICATOR3_GPE_JSON_DATA_AMBIGUOUS);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR3_UUID);
        Map<String, List<String>> dimensionCodes = new HashMap<String, List<String>>();
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.TIME.name(), Arrays.asList("2010"));
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name(), Arrays.asList("ES"));
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.MEASURE.name(), Arrays.asList(MeasureDimensionTypeEnum.ABSOLUTE.name()));

        checkDataDimensions(dimensionCodes, INDICATOR3_UUID, INDICATOR3_VERSION);
        List<String> data = Arrays.asList("34413");
        checkDataObservations(dimensionCodes, INDICATOR3_UUID, INDICATOR3_VERSION, data);
    }

    /*
     * Populate data with both, time and geographical, variables fixed
     */
    @Test
    public void testPopulateIndicatorDataSpatialValueTemporalValue() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR4_DS_GPE_UUID))).thenReturn(INDICATOR4_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR4_UUID);
        Map<String, List<String>> dimensionCodes = new HashMap<String, List<String>>();
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.TIME.name(), Arrays.asList("2010"));
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name(), Arrays.asList("ES"));
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.MEASURE.name(), Arrays.asList(MeasureDimensionTypeEnum.ABSOLUTE.name()));

        checkDataDimensions(dimensionCodes, INDICATOR4_UUID, INDICATOR4_VERSION);
        List<String> data = Arrays.asList("3585");
        checkDataObservations(dimensionCodes, INDICATOR4_UUID, INDICATOR4_VERSION, data);
    }

    /*
     * Populate Data with fixed variables, loading absolute values from cont variable
     */
    @Test
    public void testPopulateIndicatorDataContVariable() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR5_DS_GPE_UUID))).thenReturn(INDICATOR5_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR5_UUID);
        Map<String, List<String>> dimensionCodes = new HashMap<String, List<String>>();
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.TIME.name(), Arrays.asList("2010"));
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name(), Arrays.asList("ES"));
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.MEASURE.name(), Arrays.asList(MeasureDimensionTypeEnum.ABSOLUTE.name()));

        checkDataDimensions(dimensionCodes, INDICATOR5_UUID, INDICATOR5_VERSION);
        List<String> data = Arrays.asList("3585");
        checkDataObservations(dimensionCodes, INDICATOR5_UUID, INDICATOR5_VERSION, data);
    }

    /*
     * Populate Data with all different kind of dots "."
     */
    @Test
    public void testPopulateIndicatorDataDots() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR6_DS_GPE_UUID))).thenReturn(INDICATOR6_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR6_UUID);
        Map<String, List<String>> dimensionCodes = new HashMap<String, List<String>>();
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.TIME.name(), Arrays.asList("2011-01", "2010", "2010-12", "2010-11", "2010-10", "2010-09"));
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name(), Arrays.asList("ES", "ES61", "ES611", "ES612", "ES613"));
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.MEASURE.name(), Arrays.asList(MeasureDimensionTypeEnum.ABSOLUTE.name()));

        checkDataDimensions(dimensionCodes, INDICATOR6_UUID, INDICATOR6_VERSION);
        List<String> data = Arrays.asList("3585", "497", "56", "60", "49", null, null, null, null, null, null, null, "36", "25", "38", "2507", "347", "31", "44", "27", "2036", "297", "20", "46", "26",
                "2156", "321", "41", "29", "19");
        checkDataObservations(dimensionCodes, INDICATOR6_UUID, INDICATOR6_VERSION, data);

        Map<String, AttributeInstanceObservationDto> mapAttributes = new HashMap<String, AttributeInstanceObservationDto>();
        {
            String key = generateObservationUniqueKey("ES", "2010", MeasureDimensionTypeEnum.ABSOLUTE.name());
            mapAttributes.put(key, createAttribute(IndicatorDataAttributeTypeEnum.OBS_CONF.name(), IndicatorsDataServiceImpl.DATASET_REPOSITORY_LOCALE, "No procede"));
        }
        {
            String key = generateObservationUniqueKey("ES61", "2010", MeasureDimensionTypeEnum.ABSOLUTE.name());
            mapAttributes.put(key, createAttribute(IndicatorDataAttributeTypeEnum.OBS_CONF.name(), IndicatorsDataServiceImpl.DATASET_REPOSITORY_LOCALE, "Dato no disponible"));
        }
        {
            String key = generateObservationUniqueKey("ES611", "2010", MeasureDimensionTypeEnum.ABSOLUTE.name());
            mapAttributes.put(key, createAttribute(IndicatorDataAttributeTypeEnum.OBS_CONF.name(), IndicatorsDataServiceImpl.DATASET_REPOSITORY_LOCALE, "Dato oculto por impreciso o baja calidad"));
        }
        {
            String key = generateObservationUniqueKey("ES612", "2010", MeasureDimensionTypeEnum.ABSOLUTE.name());
            mapAttributes.put(key, createAttribute(IndicatorDataAttributeTypeEnum.OBS_CONF.name(), IndicatorsDataServiceImpl.DATASET_REPOSITORY_LOCALE, "Dato oculto por secreto estad??stico"));
        }
        {
            String key = generateObservationUniqueKey("ES613", "2010", MeasureDimensionTypeEnum.ABSOLUTE.name());
            mapAttributes.put(key, createAttribute(IndicatorDataAttributeTypeEnum.OBS_CONF.name(), IndicatorsDataServiceImpl.DATASET_REPOSITORY_LOCALE, "Dato incluido en otra categor??a"));
        }
        {
            String key = generateObservationUniqueKey("ES", "2010-12", MeasureDimensionTypeEnum.ABSOLUTE.name());
            mapAttributes.put(key,
                    createAttribute(IndicatorDataAttributeTypeEnum.OBS_CONF.name(), IndicatorsDataServiceImpl.DATASET_REPOSITORY_LOCALE, "Dato no disponible por vacaciones o festivos"));
        }

        checkDataAttributes(dimensionCodes, INDICATOR6_UUID, INDICATOR6_VERSION, IndicatorDataAttributeTypeEnum.OBS_CONF.name(), mapAttributes);
    }

    /*
     * Populate Data checking that CODE attribute is specified for all values
     */
    @Test
    public void testPopulateIndicatorDataCodeAttribute() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR3_DS_GPE_UUID))).thenReturn(INDICATOR3_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR3_UUID);
        Map<String, List<String>> dimensionCodes = new HashMap<String, List<String>>();

        dimensionCodes.put(IndicatorDataDimensionTypeEnum.TIME.name(), Arrays.asList("2011-01", "2010", "2010-12", "2010-11", "2010-10", "2010-09"));
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name(), Arrays.asList("ES"));
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.MEASURE.name(), Arrays.asList(MeasureDimensionTypeEnum.ABSOLUTE.name()));
        List<String> data = Arrays.asList("3585", "34413", "2471", "2507", "2036", "2156");
        checkDataDimensions(dimensionCodes, INDICATOR3_UUID, INDICATOR3_VERSION);
        checkDataObservations(dimensionCodes, INDICATOR3_UUID, INDICATOR3_VERSION, data);

        Map<String, AttributeInstanceObservationDto> mapAttributes = new HashMap<String, AttributeInstanceObservationDto>();
        // All observations must have CODE attribute
        for (String geoValue : dimensionCodes.get(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name())) {
            for (String timeValue : dimensionCodes.get(IndicatorDataDimensionTypeEnum.TIME.name())) {
                for (String measureValue : dimensionCodes.get(IndicatorDataDimensionTypeEnum.MEASURE.name())) {
                    String key = generateObservationUniqueKey(geoValue, timeValue, measureValue);
                    mapAttributes.put(key, createAttribute(IndicatorDataAttributeTypeEnum.CODE.name(), IndicatorsDataServiceImpl.DATASET_REPOSITORY_LOCALE, INDICATOR3_DS_UUID));
                }
            }
        }

        checkDataAttributes(dimensionCodes, INDICATOR3_UUID, INDICATOR3_VERSION, IndicatorDataAttributeTypeEnum.CODE.name(), mapAttributes);
    }

    /*
     * Populate Data with All rates calculated using only one data source with absolute values
     */
    @Test
    public void testPopulateIndicatorDataCalculate() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR7_DS_GPE_UUID))).thenReturn(INDICATOR7_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR7_UUID);
        Map<String, List<String>> dimensionCodes = new HashMap<String, List<String>>();

        dimensionCodes.put(IndicatorDataDimensionTypeEnum.TIME.name(), Arrays.asList("2010", "2010-12", "2010-11", "2010-10", "2009", "2009-12", "2009-11"));
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name(), Arrays.asList("ES"));
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.MEASURE.name(), Arrays.asList(MeasureDimensionTypeEnum.ABSOLUTE.name(), MeasureDimensionTypeEnum.ANNUAL_PERCENTAGE_RATE.name(),
                MeasureDimensionTypeEnum.INTERPERIOD_PERCENTAGE_RATE.name(), MeasureDimensionTypeEnum.ANNUAL_PUNTUAL_RATE.name(), MeasureDimensionTypeEnum.INTERPERIOD_PUNTUAL_RATE.name()));
        /* ABSOLUTE */
        List<String> absolute = Arrays.asList("34413", "2471", "2507", "2036", "30413", "1952", "1925");
        List<String> annualPercentageRate = Arrays.asList("13.152", "26.588", "30.233", null, null, null, null);
        List<String> interperiodPercentageRate = Arrays.asList("13.15", "-1.43", "23.13", null, null, "1.40", null);
        List<String> annualPuntualRate = Arrays.asList("4000", "519", "582", null, null, null, null);
        List<String> interperiodPuntualRate = Arrays.asList("4000", "-36", "471", null, null, "27", null);

        List<String> data = new ArrayList<String>();
        data.addAll(absolute);
        data.addAll(annualPercentageRate);
        data.addAll(interperiodPercentageRate);
        data.addAll(annualPuntualRate);
        data.addAll(interperiodPuntualRate);

        checkDataDimensions(dimensionCodes, INDICATOR7_UUID, INDICATOR7_VERSION);
        checkDataObservations(dimensionCodes, INDICATOR7_UUID, INDICATOR7_VERSION, data);
    }

    /*
     * Populate Data with All rates calculated using only one data source with absolute values
     */
    @Test
    public void testPopulateIndicatorDataCalculateFebruary29() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR7_DS_GPE_UUID))).thenReturn(INDICATOR7_GPE_JSON_DATA_29FEB);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR7_UUID);
        Map<String, List<String>> dimensionCodes = new HashMap<String, List<String>>();

        dimensionCodes.put(IndicatorDataDimensionTypeEnum.TIME.name(), Arrays.asList("2012-02", "2012-02-29", "2012-02-28", "2012-02-27", "2011-02", "2011-02-28", "2011-02-27"));
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name(), Arrays.asList("ES"));
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.MEASURE.name(), Arrays.asList(MeasureDimensionTypeEnum.ABSOLUTE.name(), MeasureDimensionTypeEnum.ANNUAL_PERCENTAGE_RATE.name(),
                MeasureDimensionTypeEnum.INTERPERIOD_PERCENTAGE_RATE.name(), MeasureDimensionTypeEnum.ANNUAL_PUNTUAL_RATE.name(), MeasureDimensionTypeEnum.INTERPERIOD_PUNTUAL_RATE.name()));
        /* ABSOLUTE */
        List<String> absolute = Arrays.asList("34413", "2471", "2507", "2036", "30413", "1952", "1925");
        List<String> annualPercentageRate = Arrays.asList("13.152", null, "28.432", "5.766", null, null, null);
        List<String> interperiodPercentageRate = Arrays.asList(null, "-1.43", "23.13", null, null, "1.40", null);
        List<String> annualPuntualRate = Arrays.asList("4000", null, "555", "111", null, null, null);
        List<String> interperiodPuntualRate = Arrays.asList(null, "-36", "471", null, null, "27", null);

        List<String> data = new ArrayList<String>();
        data.addAll(absolute);
        data.addAll(annualPercentageRate);
        data.addAll(interperiodPercentageRate);
        data.addAll(annualPuntualRate);
        data.addAll(interperiodPuntualRate);

        checkDataDimensions(dimensionCodes, INDICATOR7_UUID, INDICATOR7_VERSION);
        checkDataObservations(dimensionCodes, INDICATOR7_UUID, INDICATOR7_VERSION, data);
    }

    /*
     * Populate Data with All rates calculated using only one data source with absolute values
     * Temporal values are being normalized, that's why it's able to calculate the interperiodic properly
     */
    @Test
    public void testPopulateIndicatorDataCalculateAmbiguous() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR7_DS_GPE_UUID))).thenReturn(INDICATOR7_GPE_JSON_DATA_AMBIGUOUS);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR7_UUID);
        Map<String, List<String>> dimensionCodes = new HashMap<String, List<String>>();

        dimensionCodes.put(IndicatorDataDimensionTypeEnum.TIME.name(), Arrays.asList("2010", "2010-12", "2010-11", "2010-10", "2009", "2009-12", "2009-11"));
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name(), Arrays.asList("ES"));
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.MEASURE.name(), Arrays.asList(MeasureDimensionTypeEnum.ABSOLUTE.name(), MeasureDimensionTypeEnum.ANNUAL_PERCENTAGE_RATE.name(),
                MeasureDimensionTypeEnum.INTERPERIOD_PERCENTAGE_RATE.name(), MeasureDimensionTypeEnum.ANNUAL_PUNTUAL_RATE.name(), MeasureDimensionTypeEnum.INTERPERIOD_PUNTUAL_RATE.name()));
        /* ABSOLUTE */
        List<String> absolute = Arrays.asList("34413", "2471", "2507", "2036", "30413", "1952", "1925");
        List<String> annualPercentageRate = Arrays.asList("13.152", "26.588", "30.233", null, null, null, null);
        List<String> interperiodPercentageRate = Arrays.asList("13.15", "-1.43", "23.13", null, null, "1.40", null);
        List<String> annualPuntualRate = Arrays.asList("4000", "519", "582", null, null, null, null);
        List<String> interperiodPuntualRate = Arrays.asList("4000", "-36", "471", null, null, "27", null);

        List<String> data = new ArrayList<String>();
        data.addAll(absolute);
        data.addAll(annualPercentageRate);
        data.addAll(interperiodPercentageRate);
        data.addAll(annualPuntualRate);
        data.addAll(interperiodPuntualRate);

        checkDataDimensions(dimensionCodes, INDICATOR7_UUID, INDICATOR7_VERSION);
        checkDataObservations(dimensionCodes, INDICATOR7_UUID, INDICATOR7_VERSION, data);
    }

    /*
     * Populate Data with All rates calculated using only one data source with absolute values
     * some values are dots
     */
    @Test
    public void testPopulateIndicatorDataCalculateDots() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR13_DS_GPE_UUID))).thenReturn(INDICATOR13_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR13_UUID);
        Map<String, List<String>> dimensionCodes = new HashMap<String, List<String>>();

        dimensionCodes.put(IndicatorDataDimensionTypeEnum.TIME.name(), Arrays.asList("2010", "2010-12", "2010-11", "2010-10", "2009", "2009-12", "2009-11", "2009-10"));
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name(), Arrays.asList("ES"));
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.MEASURE.name(), Arrays.asList(MeasureDimensionTypeEnum.ABSOLUTE.name(), MeasureDimensionTypeEnum.INTERPERIOD_PERCENTAGE_RATE.name()));
        /* ABSOLUTE */
        List<String> absolute = Arrays.asList("34413", null/* ... */, null/* . */, "2036", "30413", "1952", null /* .. */, null /* - */);
        List<String> interperiodPercentageRate = Arrays.asList("13.15", null, null, null, null, null, null, null);

        List<String> data = new ArrayList<String>();
        data.addAll(absolute);
        data.addAll(interperiodPercentageRate);

        checkDataDimensions(dimensionCodes, INDICATOR13_UUID, INDICATOR13_VERSION);
        checkDataObservations(dimensionCodes, INDICATOR13_UUID, INDICATOR13_VERSION, data);

        Map<String, AttributeInstanceObservationDto> mapAttributes = new HashMap<String, AttributeInstanceObservationDto>();
        {
            String key = generateObservationUniqueKey("ES", "2010-12", MeasureDimensionTypeEnum.ABSOLUTE.name());
            mapAttributes.put(key, createAttribute(IndicatorDataAttributeTypeEnum.OBS_CONF.name(), IndicatorsDataServiceImpl.DATASET_REPOSITORY_LOCALE, "Dato oculto por impreciso o baja calidad"));
        }
        {
            String key = generateObservationUniqueKey("ES", "2010-11", MeasureDimensionTypeEnum.ABSOLUTE.name());
            mapAttributes.put(key, createAttribute(IndicatorDataAttributeTypeEnum.OBS_CONF.name(), IndicatorsDataServiceImpl.DATASET_REPOSITORY_LOCALE, "No procede"));
        }
        {
            String key = generateObservationUniqueKey("ES", "2009-11", MeasureDimensionTypeEnum.ABSOLUTE.name());
            mapAttributes.put(key, createAttribute(IndicatorDataAttributeTypeEnum.OBS_CONF.name(), IndicatorsDataServiceImpl.DATASET_REPOSITORY_LOCALE, "Dato no disponible"));
        }

        {
            String key = generateObservationUniqueKey("ES", "2010-12", MeasureDimensionTypeEnum.INTERPERIOD_PERCENTAGE_RATE.name());
            mapAttributes.put(key,
                    createAttribute(IndicatorDataAttributeTypeEnum.OBS_CONF.name(), IndicatorsDataServiceImpl.DATASET_REPOSITORY_LOCALE, "Dato oculto por impreciso o baja calidad, No procede"));
        }
        {
            String key = generateObservationUniqueKey("ES", "2010-11", MeasureDimensionTypeEnum.INTERPERIOD_PERCENTAGE_RATE.name());
            mapAttributes.put(key, createAttribute(IndicatorDataAttributeTypeEnum.OBS_CONF.name(), IndicatorsDataServiceImpl.DATASET_REPOSITORY_LOCALE, "No procede"));
        }
        {
            String key = generateObservationUniqueKey("ES", "2010-10", MeasureDimensionTypeEnum.INTERPERIOD_PERCENTAGE_RATE.name());
            mapAttributes.put(key, createAttribute(IndicatorDataAttributeTypeEnum.OBS_CONF.name(), IndicatorsDataServiceImpl.DATASET_REPOSITORY_LOCALE, "Dato no disponible"));
        }
        {
            String key = generateObservationUniqueKey("ES", "2009", MeasureDimensionTypeEnum.INTERPERIOD_PERCENTAGE_RATE.name());
            mapAttributes.put(key, createAttribute(IndicatorDataAttributeTypeEnum.OBS_CONF.name(), IndicatorsDataServiceImpl.DATASET_REPOSITORY_LOCALE, "Dato no disponible"));
        }
        {
            String key = generateObservationUniqueKey("ES", "2009-12", MeasureDimensionTypeEnum.INTERPERIOD_PERCENTAGE_RATE.name());
            mapAttributes.put(key, createAttribute(IndicatorDataAttributeTypeEnum.OBS_CONF.name(), IndicatorsDataServiceImpl.DATASET_REPOSITORY_LOCALE, "Dato no disponible"));
        }
        {
            String key = generateObservationUniqueKey("ES", "2009-11", MeasureDimensionTypeEnum.INTERPERIOD_PERCENTAGE_RATE.name());
            mapAttributes.put(key, createAttribute(IndicatorDataAttributeTypeEnum.OBS_CONF.name(), IndicatorsDataServiceImpl.DATASET_REPOSITORY_LOCALE, "Dato no disponible"));
        }
        {
            String key = generateObservationUniqueKey("ES", "2009-10", MeasureDimensionTypeEnum.INTERPERIOD_PERCENTAGE_RATE.name());
            mapAttributes.put(key, createAttribute(IndicatorDataAttributeTypeEnum.OBS_CONF.name(), IndicatorsDataServiceImpl.DATASET_REPOSITORY_LOCALE, "Dato no disponible"));
        }
        checkDataAttributes(dimensionCodes, INDICATOR13_UUID, INDICATOR13_VERSION, IndicatorDataAttributeTypeEnum.OBS_CONF.name(), mapAttributes);
    }

    /*
     * Populate Data with All rates calculated using two data sources with absolute values, checking that
     * calculated values are correct and were calculated using both data sources
     */
    @Test
    public void testPopulateIndicatorDataMultiDataSource() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR8_DS1_GPE_UUID))).thenReturn(INDICATOR8_GPE_JSON_DATA1);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR8_DS2_GPE_UUID))).thenReturn(INDICATOR8_GPE_JSON_DATA2);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR8_UUID);
        Map<String, List<String>> dimensionCodes = new HashMap<String, List<String>>();

        dimensionCodes.put(IndicatorDataDimensionTypeEnum.TIME.name(), Arrays.asList("2010", "2010-12", "2010-11", "2010-10", "2009", "2009-12", "2009-11", "2009-10"));
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name(), Arrays.asList("ES", "ES61"));
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.MEASURE.name(), Arrays.asList(MeasureDimensionTypeEnum.ABSOLUTE.name(), MeasureDimensionTypeEnum.ANNUAL_PERCENTAGE_RATE.name(),
                MeasureDimensionTypeEnum.INTERPERIOD_PERCENTAGE_RATE.name(), MeasureDimensionTypeEnum.ANNUAL_PUNTUAL_RATE.name(), MeasureDimensionTypeEnum.INTERPERIOD_PUNTUAL_RATE.name()));
        // DATA
        List<String> absolute = Arrays.asList("34413.00", "4546.00", "2471.00", "329.00", "2507.00", "347.00", "2036.00", "297.00", "33413.00", "3546.00", "1471.00", "229.00", "1507.00", "247.00",
                "1036.00", "197.00");
        List<String> annualPercentageRate = Arrays.asList("2.992", "28.200", "67.980", "43.668", "66.357", "40.485", "96.525", "50.761", null, null, null, null, null, null, null, null);
        List<String> interperiodPercentageRate = Arrays.asList("2.99", "28.20", "-1.43", "-5.18", "23.13", "16.83", null, null, null, null, "-2.38", "-7.28", "45.46", "25.38", null, null);
        List<String> annualPuntualRate = Arrays.asList("1000.0", "1000.0", "1000.0", "100.0", "1000.0", "100.0", "1000.0", "100.0", null, null, null, null, null, null, null, null);
        List<String> interperiodPuntualRate = Arrays.asList("1000.0", "1000.0", "-36.0", "-18.0", "471.0", "50.0", null, null, null, null, "-36.0", "-18.0", "471.0", "50.0", null, null);

        List<String> data = new ArrayList<String>();
        data.addAll(absolute);
        data.addAll(annualPercentageRate);
        data.addAll(interperiodPercentageRate);
        data.addAll(annualPuntualRate);
        data.addAll(interperiodPuntualRate);

        checkDataDimensions(dimensionCodes, INDICATOR8_UUID, INDICATOR8_VERSION);
        checkDataObservations(dimensionCodes, INDICATOR8_UUID, INDICATOR8_VERSION, data);
    }

    /*
     * Populate Data with All rates calculated using two data sources with absolute values, checking that
     * calculated values are correct and were calculated using both data sources, and absolute values have decimals
     */
    @Test
    public void testPopulateIndicatorDataMultiDataSourceDecimals() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR8_DS1_GPE_UUID))).thenReturn(INDICATOR8_GPE_JSON_DECIMALS_DATA1);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR8_DS2_GPE_UUID))).thenReturn(INDICATOR8_GPE_JSON_DECIMALS_DATA2);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR8_UUID);
        Map<String, List<String>> dimensionCodes = new HashMap<String, List<String>>();

        dimensionCodes.put(IndicatorDataDimensionTypeEnum.TIME.name(), Arrays.asList("2010", "2010-12", "2010-11", "2009", "2009-12", "2009-11"));
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name(), Arrays.asList("ES", "ES61"));
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.MEASURE.name(), Arrays.asList(MeasureDimensionTypeEnum.ABSOLUTE.name(), MeasureDimensionTypeEnum.ANNUAL_PERCENTAGE_RATE.name(),
                MeasureDimensionTypeEnum.INTERPERIOD_PERCENTAGE_RATE.name(), MeasureDimensionTypeEnum.ANNUAL_PUNTUAL_RATE.name(), MeasureDimensionTypeEnum.INTERPERIOD_PUNTUAL_RATE.name()));
        // DATA
        List<String> absolute = Arrays.asList("34413.25", "4546.36", "2471.26", "329.20", "2507.23", "347.58", "33413.85", "3546.35", "1471.25", "229.22", "1507.73", "247.12");
        List<String> annualPercentageRate = Arrays.asList("2.990", "28.198", "67.970", "43.617", "66.291", "40.652", null, null, null, null, null, null);
        List<String> interperiodPercentageRate = Arrays.asList("2.99", "28.19", "-1.43", "-5.28", null, null, null, null, "-2.41", "-7.24", null, null);
        List<String> annualPuntualRate = Arrays.asList("999.4", "1000.0", "1000.0", "99.9", "999.5", "100.4", null, null, null, null, null, null);
        List<String> interperiodPuntualRate = Arrays.asList("999.4", "1000.0", "-35.9", "-18.3", null, null, null, null, "-36.4", "-17.9", null, null);

        List<String> data = new ArrayList<String>();
        data.addAll(absolute);
        data.addAll(annualPercentageRate);
        data.addAll(interperiodPercentageRate);
        data.addAll(annualPuntualRate);
        data.addAll(interperiodPuntualRate);

        checkDataDimensions(dimensionCodes, INDICATOR8_UUID, INDICATOR8_VERSION);
        checkDataObservations(dimensionCodes, INDICATOR8_UUID, INDICATOR8_VERSION, data);
    }

    /*
     * Populate Data with All rates obtained from contvariable
     */
    @Test
    public void testPopulateIndicatorDataAllRatesContVariable() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR9_DS_GPE_UUID))).thenReturn(INDICATOR9_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR9_UUID);
        Map<String, List<String>> dimensionCodes = new HashMap<String, List<String>>();

        dimensionCodes.put(IndicatorDataDimensionTypeEnum.TIME.name(), Arrays.asList("2010", "2010-12", "2010-11", "2010-10", "2009", "2009-12", "2009-11", "2009-10"));
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name(), Arrays.asList("ES", "ES61"));
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.MEASURE.name(), Arrays.asList(MeasureDimensionTypeEnum.ABSOLUTE.name(), MeasureDimensionTypeEnum.ANNUAL_PERCENTAGE_RATE.name(),
                MeasureDimensionTypeEnum.INTERPERIOD_PERCENTAGE_RATE.name(), MeasureDimensionTypeEnum.ANNUAL_PUNTUAL_RATE.name(), MeasureDimensionTypeEnum.INTERPERIOD_PUNTUAL_RATE.name()));
        // DATA
        List<String> absolute = Arrays.asList("34413", "4546", "2471", "329", "2507", "347", "2036", "297", "33413", "3546", "1471", "229", "1507", "247", "1036", "197");
        List<String> annualPercentageRate = Arrays.asList("2.993", "28.201", "67.981", "43.668", "66.357", "40.486", "96.525", "50.761", null, null, null, null, null, null, null, null);
        List<String> interperiodPercentageRate = Arrays.asList("2.99", "28.20", "-1.44", "-5.19", "23.13", "16.84", null, null, null, null, "-2.39", "-7.29", "45.46", "25.38", null, null);
        List<String> annualPuntualRate = Arrays.asList("1000", "1000", "1000", "100", "1000", "100", "1000", "100", null, null, null, null, null, null, null, null);
        List<String> interperiodPuntualRate = Arrays.asList("1000", "1000", "-36", "-18", "471", "50", null, null, null, null, "-36", "-18", "471", "50", null, null);

        List<String> data = new ArrayList<String>();
        data.addAll(absolute);
        data.addAll(annualPercentageRate);
        data.addAll(interperiodPercentageRate);
        data.addAll(annualPuntualRate);
        data.addAll(interperiodPuntualRate);

        checkDataDimensions(dimensionCodes, INDICATOR9_UUID, INDICATOR9_VERSION);
        checkDataObservations(dimensionCodes, INDICATOR9_UUID, INDICATOR9_VERSION, data);
    }

    /*
     * Populate Data with All rates obtained from different data sources as OBS_VALUE
     */
    @Test
    public void testPopulateIndicatorDataAllRatesObsValue() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR10_DS1_GPE_UUID))).thenReturn(INDICATOR10_GPE_JSON_DATA1);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR10_DS2_GPE_UUID))).thenReturn(INDICATOR10_GPE_JSON_DATA2);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR10_DS3_GPE_UUID))).thenReturn(INDICATOR10_GPE_JSON_DATA3);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR10_DS4_GPE_UUID))).thenReturn(INDICATOR10_GPE_JSON_DATA4);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR10_DS5_GPE_UUID))).thenReturn(INDICATOR10_GPE_JSON_DATA5);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR10_UUID);
        Map<String, List<String>> dimensionCodes = new HashMap<String, List<String>>();

        dimensionCodes.put(IndicatorDataDimensionTypeEnum.TIME.name(), Arrays.asList("2010", "2010-12", "2010-11", "2010-10", "2009", "2009-12", "2009-11", "2009-10"));
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name(), Arrays.asList("ES", "ES61"));
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.MEASURE.name(), Arrays.asList(MeasureDimensionTypeEnum.ABSOLUTE.name(), MeasureDimensionTypeEnum.ANNUAL_PERCENTAGE_RATE.name(),
                MeasureDimensionTypeEnum.INTERPERIOD_PERCENTAGE_RATE.name(), MeasureDimensionTypeEnum.ANNUAL_PUNTUAL_RATE.name(), MeasureDimensionTypeEnum.INTERPERIOD_PUNTUAL_RATE.name()));
        // DATA
        List<String> absolute = Arrays.asList("34413", "4546", "2471", "329", "2507", "347", "2036", "297", "33413", "3546", "1471", "229", "1507", "247", "1036", "197");
        List<String> annualPercentageRate = Arrays.asList("2.993", "28.201", "67.981", "43.668", "66.357", "40.486", "96.525", "50.761", null, null, null, null, null, null, null, null);
        List<String> interperiodPercentageRate = Arrays.asList("2.990", "28.200", "-1.440", "-5.190", "23.130", "16.840", null, null, null, null, "-2.390", "-7.290", "45.460", "25.380", null, null);
        List<String> annualPuntualRate = Arrays.asList("1000", "1000", "1000", "100", "1000", "100", "1000", "100", null, null, null, null, null, null, null, null);
        List<String> interperiodPuntualRate = Arrays.asList("1000", "1000", "-36", "-18", "471", "50", null, null, null, null, "-36", "-18", "471", "50", null, null);

        List<String> data = new ArrayList<String>();
        data.addAll(absolute);
        data.addAll(annualPercentageRate);
        data.addAll(interperiodPercentageRate);
        data.addAll(annualPuntualRate);
        data.addAll(interperiodPuntualRate);

        checkDataDimensions(dimensionCodes, INDICATOR10_UUID, INDICATOR10_VERSION);
        checkDataObservations(dimensionCodes, INDICATOR10_UUID, INDICATOR10_VERSION, data);
    }

    /*
     * Populate data for a published version
     */
    @Test
    public void testPopulateIndicatorDataPublishedVersion() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR16_DS_GPE_UUID))).thenReturn(INDICATOR16_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR16_UUID);

        // After populate, version has changed
        String newVersion = IndicatorsVersionUtils.createNextVersion(getHasVersionNumberMock(INDICATOR16_VERSION), VersionTypeEnum.MINOR);

        Map<String, List<String>> dimensionCodes = new HashMap<String, List<String>>();
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.TIME.name(), Arrays.asList("2011-01", "2010", "2010-12", "2010-11", "2010-10", "2010-09"));
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name(), Arrays.asList("ES"));
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.MEASURE.name(), Arrays.asList(MeasureDimensionTypeEnum.ABSOLUTE.name()));

        checkDataDimensions(dimensionCodes, INDICATOR16_UUID, newVersion);
        List<String> data = Arrays.asList("3585", "34413", "2471", "2507", "2036", "2156");
        checkDataObservations(dimensionCodes, INDICATOR16_UUID, newVersion, data);

        // old version does not longer exist
        try {
            getIndicatorsService().retrieveIndicator(getServiceContextAdministrador(), INDICATOR16_UUID, INDICATOR16_VERSION);
            fail("Should have not found old version");
        } catch (MetamacException e) {
            assertNotNull(e.getExceptionItems());
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_VERSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    /*
     * Indicator has a nonexistent geographic variable
     */
    @Test
    public void testPopulateIndicatorDataRemovedGeographicVariable() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR17_DS_GPE_UUID))).thenReturn(INDICATOR17_GPE_JSON_DATA_GEO_NOT_EXIST);
        List<MetamacExceptionItem> errors = indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR17_UUID);
        assertIndicatorEmptyData(INDICATOR17_UUID, INDICATOR17_VERSION);
        assertNotNull(errors);
        assertEquals(1, errors.size());
        assertEquals(ServiceExceptionType.DATA_COMPATIBILITY_GEOGRAPHIC_VARIABLE_NOT_EXISTS.getCode(), errors.get(0).getCode());
    }

    /*
     * Indicator has a geographic variable that is not geographic
     */
    @Test
    public void testPopulateIndicatorDataIllegalGeographicVariable() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR17_DS_GPE_UUID))).thenReturn(INDICATOR17_GPE_JSON_DATA_GEO_NOT_GEO);
        List<MetamacExceptionItem> errors = indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR17_UUID);
        assertIndicatorEmptyData(INDICATOR17_UUID, INDICATOR17_VERSION);
        assertNotNull(errors);
        assertEquals(2, errors.size());
        assertEquals(ServiceExceptionType.DATA_COMPATIBILITY_GEOGRAPHIC_VARIABLE_NOT_GEOGRAPHIC.getCode(), errors.get(0).getCode());
        assertEquals(ServiceExceptionType.DATA_COMPATIBILITY_OTHER_VARIABLES_UNSPECIFIED_VARIABLES.getCode(), errors.get(1).getCode());
        assertEquals("Provincias por CC.AA.", errors.get(1).getMessageParameters()[1]);
    }

    /*
     * Indicator has a nonexistent time variable
     */
    @Test
    public void testPopulateIndicatorDataRemovedTimeVariable() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR17_DS_GPE_UUID))).thenReturn(INDICATOR17_GPE_JSON_DATA_TEMP_NOT_EXIST);
        List<MetamacExceptionItem> errors = indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR17_UUID);
        assertIndicatorEmptyData(INDICATOR17_UUID, INDICATOR17_VERSION);
        assertNotNull(errors);
        assertEquals(1, errors.size());
        assertEquals(ServiceExceptionType.DATA_COMPATIBILITY_TIME_VARIABLE_NOT_EXISTS.getCode(), errors.get(0).getCode());
    }

    /*
     * Indicator has a time variable that is not temporal
     */
    @Test
    public void testPopulateIndicatorDataIllegalTimeVariable() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR17_DS_GPE_UUID))).thenReturn(INDICATOR17_GPE_JSON_DATA_TEMP_NOT_TEMP);
        List<MetamacExceptionItem> errors = indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR17_UUID);
        assertIndicatorEmptyData(INDICATOR17_UUID, INDICATOR17_VERSION);
        assertNotNull(errors);
        assertEquals(2, errors.size());
        assertEquals(ServiceExceptionType.DATA_COMPATIBILITY_TIME_VARIABLE_NOT_TEMPORAL.getCode(), errors.get(0).getCode());
        assertEquals(ServiceExceptionType.DATA_COMPATIBILITY_OTHER_VARIABLES_UNSPECIFIED_VARIABLES.getCode(), errors.get(1).getCode());
        assertEquals("Periodos", errors.get(1).getMessageParameters()[1]);
    }

    /*
     * Indicator has a geographic value but data has geographic variable
     * has a time value but data has temporal variable
     */
    @Test
    public void testPopulateIndicatorDataIllegalGeographicValueIllegalTimeValue() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR18_DS_GPE_UUID))).thenReturn(INDICATOR18_GPE_JSON_DATA);
        List<MetamacExceptionItem> errors = indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR18_UUID);
        assertIndicatorEmptyData(INDICATOR18_UUID, INDICATOR18_VERSION);
        assertNotNull(errors);
        assertEquals(2, errors.size());
        assertEquals(ServiceExceptionType.DATA_COMPATIBILITY_GEOGRAPHIC_VALUE_ILLEGAL.getCode(), errors.get(0).getCode());
        assertEquals(ServiceExceptionType.DATA_COMPATIBILITY_TIME_VALUE_ILLEGAL.getCode(), errors.get(1).getCode());
    }

    /*
     * Indicator has an unknown AbsoluteMethod and rates methods with contvariable
     */
    @Test
    public void testPopulateIndicatorDataIllegalMethodsContVariable() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR19_DS_GPE_UUID))).thenReturn(INDICATOR19_GPE_JSON_DATA);
        List<MetamacExceptionItem> errors = indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR19_UUID);
        assertIndicatorEmptyData(INDICATOR19_UUID, INDICATOR19_VERSION);
        assertNotNull(errors);
        assertEquals(5, errors.size());
        assertEquals(ServiceExceptionType.DATA_COMPATIBILITY_ABSMETHOD_CONTVARIABLE_ILLEGAL.getCode(), errors.get(0).getCode());
        assertEquals(ServiceExceptionType.DATA_COMPATIBILITY_RATE_METHOD_CONTVARIABLE_ILLEGAL.getCode(), errors.get(1).getCode());
        assertEquals(ServiceExceptionParameters.DATA_SOURCE_ANNUAL_PERCENTAGE_RATE, errors.get(1).getMessageParameters()[1]);
        assertEquals(ServiceExceptionType.DATA_COMPATIBILITY_RATE_METHOD_CONTVARIABLE_ILLEGAL.getCode(), errors.get(2).getCode());
        assertEquals(ServiceExceptionParameters.DATA_SOURCE_ANNUAL_PUNTUAL_RATE, errors.get(2).getMessageParameters()[1]);
        assertEquals(ServiceExceptionType.DATA_COMPATIBILITY_RATE_METHOD_CONTVARIABLE_ILLEGAL.getCode(), errors.get(3).getCode());
        assertEquals(ServiceExceptionParameters.DATA_SOURCE_INTERPERIOD_PERCENTAGE_RATE, errors.get(3).getMessageParameters()[1]);
        assertEquals(ServiceExceptionType.DATA_COMPATIBILITY_RATE_METHOD_CONTVARIABLE_ILLEGAL.getCode(), errors.get(4).getCode());
        assertEquals(ServiceExceptionParameters.DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE, errors.get(4).getMessageParameters()[1]);
    }

    /*
     * Indicator has an unknown AbsoluteMethod and rates methods without contvariable
     */
    @Test
    public void testPopulateIndicatorDataIllegalMethodsNoContVariable() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR20_DS_GPE_UUID))).thenReturn(INDICATOR20_GPE_JSON_DATA);
        List<MetamacExceptionItem> errors = indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR20_UUID);
        assertIndicatorEmptyData(INDICATOR20_UUID, INDICATOR20_VERSION);
        assertNotNull(errors);
        assertEquals(5, errors.size());
        assertEquals(ServiceExceptionType.DATA_COMPATIBILITY_ABSMETHOD_NO_CONTVARIABLE_ILLEGAL.getCode(), errors.get(0).getCode());
        assertEquals(ServiceExceptionType.DATA_COMPATIBILITY_RATE_METHOD_NO_CONTVARIABLE_ILLEGAL.getCode(), errors.get(1).getCode());
        assertEquals(ServiceExceptionParameters.DATA_SOURCE_ANNUAL_PERCENTAGE_RATE, errors.get(1).getMessageParameters()[1]);
        assertEquals(ServiceExceptionType.DATA_COMPATIBILITY_RATE_METHOD_NO_CONTVARIABLE_ILLEGAL.getCode(), errors.get(2).getCode());
        assertEquals(ServiceExceptionParameters.DATA_SOURCE_ANNUAL_PUNTUAL_RATE, errors.get(2).getMessageParameters()[1]);
        assertEquals(ServiceExceptionType.DATA_COMPATIBILITY_RATE_METHOD_NO_CONTVARIABLE_ILLEGAL.getCode(), errors.get(3).getCode());
        assertEquals(ServiceExceptionParameters.DATA_SOURCE_INTERPERIOD_PERCENTAGE_RATE, errors.get(3).getMessageParameters()[1]);
        assertEquals(ServiceExceptionType.DATA_COMPATIBILITY_RATE_METHOD_NO_CONTVARIABLE_ILLEGAL.getCode(), errors.get(4).getCode());
        assertEquals(ServiceExceptionParameters.DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE, errors.get(4).getMessageParameters()[1]);
    }

    /*
     * Indicator has wrong variables in "Other variables"
     * GEOGRAPHIC TIME and CONTVARIABLE are not allowed in other variables
     */
    @Test
    public void testPopulateIndicatorDataWrongOtherVariables() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR21_DS_GPE_UUID))).thenReturn(INDICATOR21_GPE_JSON_DATA);
        List<MetamacExceptionItem> errors = indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR21_UUID);
        assertIndicatorEmptyData(INDICATOR21_UUID, INDICATOR21_VERSION);
        assertNotNull(errors);
        assertEquals(4, errors.size());
        assertEquals(ServiceExceptionType.DATA_COMPATIBILITY_OTHER_VARIABLES_TEMPORAL_INCLUDED.getCode(), errors.get(0).getCode());
        assertEquals(ServiceExceptionType.DATA_COMPATIBILITY_OTHER_VARIABLES_CONTVARIABLE_INCLUDED.getCode(), errors.get(1).getCode());
        assertEquals(ServiceExceptionType.DATA_COMPATIBILITY_OTHER_VARIABLES_UNSPECIFIED_VARIABLES.getCode(), errors.get(2).getCode());
        assertEquals("Naturaleza jur??dica", errors.get(2).getMessageParameters()[1]);
        assertEquals(ServiceExceptionType.DATA_COMPATIBILITY_OTHER_VARIABLES_UNKNOWN_VARIABLES.getCode(), errors.get(3).getCode());
        assertEquals("NOT REAL VARIABLE", errors.get(3).getMessageParameters()[1]);
    }

    /*
     * Indicator has observations with too many digits
     */
    @Test
    public void testPopulateIndicatorDataWrongObservationLength() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR24_DS_GPE_UUID))).thenReturn(INDICATOR24_GPE_JSON_DATA);
        List<MetamacExceptionItem> errors = indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR24_UUID);
        assertIndicatorEmptyData(INDICATOR24_UUID, INDICATOR24_VERSION);
        assertNotNull(errors);
        assertEquals(1, errors.size());
        assertEquals(ServiceExceptionType.DATA_POPULATE_WRONG_OBSERVATION_VALUE_LENGTH.getCode(), errors.get(0).getCode());
    }

    @Test
    public void testDeleteIndicatorData() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID);
        Map<String, List<String>> dimensionCodes = new HashMap<String, List<String>>();
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.TIME.name(), Arrays.asList("2011-01", "2010", "2010-12", "2010-11", "2010-10", "2010-09"));
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name(), Arrays.asList("ES", "ES61", "ES611", "ES612", "ES613"));
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.MEASURE.name(), Arrays.asList(MeasureDimensionTypeEnum.ABSOLUTE.name()));

        checkDataDimensions(dimensionCodes, INDICATOR1_UUID, INDICATOR1_VERSION);
        List<String> data = Arrays.asList("3585", "497", "56", "60", "49", "34413", "4546", "422", "487", "410", "2471", "329", "36", "25", "38", "2507", "347", "31", "44", "27", "2036", "297", "20",
                "46", "26", "2156", "321", "41", "29", "19");
        checkDataObservations(dimensionCodes, INDICATOR1_UUID, INDICATOR1_VERSION, data);

        IndicatorVersion version = indicatorVersionRepository.retrieveIndicatorVersion(INDICATOR1_UUID, INDICATOR1_VERSION);
        // delete
        assertNotNull(version.getDataRepositoryId());
        assertNotNull(version.getDataRepositoryTableName());
        indicatorsDataService.deleteIndicatorVersionData(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_VERSION);

        version = indicatorVersionRepository.retrieveIndicatorVersion(INDICATOR1_UUID, INDICATOR1_VERSION);
        assertNull(version.getDataRepositoryId());
        assertNull(version.getDataRepositoryTableName());
    }

    @Test
    public void testDeleteIndicatorDataNotExists() throws Exception {
        IndicatorVersion version = indicatorVersionRepository.retrieveIndicatorVersion(INDICATOR1_UUID, INDICATOR1_VERSION);
        // delete
        assertNull(version.getDataRepositoryId());
        assertNull(version.getDataRepositoryTableName());
        try {
            indicatorsDataService.deleteIndicatorVersionData(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_VERSION);
            fail("Should throw an exception for empty data");
        } catch (MetamacException e) {
            assertNotNull(e.getExceptionItems());
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_NOT_POPULATED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(INDICATOR1_UUID, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(INDICATOR1_VERSION, e.getExceptionItems().get(0).getMessageParameters()[1]);
        }
    }

    /* Tests for system version change */
    /*
     * Populate data for diffusion and production version, next diffusion version collides with current production version
     */
    @Test
    public void testPopulateIndicatorDataAllVersionsUpdate() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR25_DS_GPE_UUID_V1))).thenReturn(INDICATOR25_GPE_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR25_DS_GPE_UUID_V2))).thenReturn(INDICATOR25_GPE_JSON_DATA);

        Date beforeUpdateDate = new Date();

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR25_UUID);

        // After populate, diffusion and production version have to change
        String newDiffusionVersion = IndicatorsVersionUtils.createNextVersion(getHasVersionNumberMock(INDICATOR25_DIFFUSION_VERSION), VersionTypeEnum.MINOR);
        String newProductionVersion = IndicatorsVersionUtils.createNextVersion(getHasVersionNumberMock(INDICATOR25_PRODUCTION_VERSION), VersionTypeEnum.MINOR);

        // old diffusion version does not longer exist
        try {
            getIndicatorsService().retrieveIndicator(getServiceContextAdministrador(), INDICATOR25_UUID, INDICATOR25_DIFFUSION_VERSION);
            fail("Should have not found old version");
        } catch (MetamacException e) {
            assertNotNull(e.getExceptionItems());
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_VERSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
        }

        Map<String, List<String>> dimensionCodes = new HashMap<String, List<String>>();
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.TIME.name(), Arrays.asList("2011-01", "2010", "2010-12", "2010-11", "2010-10", "2010-09"));
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name(), Arrays.asList("ES"));
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.MEASURE.name(), Arrays.asList(MeasureDimensionTypeEnum.ABSOLUTE.name()));

        // Check new Versions
        {
            IndicatorVersion indicatorVersion = getIndicatorsService().retrieveIndicator(getServiceContextAdministrador(), INDICATOR25_UUID, newDiffusionVersion);
            assertTrue(beforeUpdateDate.before(indicatorVersion.getLastUpdated().toDate())); // last update date, changed
            checkDataDimensions(dimensionCodes, INDICATOR25_UUID, newDiffusionVersion);
            List<String> data = Arrays.asList("3585", "34413", "2471", "2507", "2036", "2156");
            checkDataObservations(dimensionCodes, INDICATOR25_UUID, newDiffusionVersion, data);
        }
        {
            IndicatorVersion indicatorVersion = getIndicatorsService().retrieveIndicator(getServiceContextAdministrador(), INDICATOR25_UUID, newProductionVersion);
            assertTrue(beforeUpdateDate.before(indicatorVersion.getLastUpdated().toDate())); // last update date, changed
            checkDataDimensions(dimensionCodes, INDICATOR25_UUID, newProductionVersion);
            List<String> data = Arrays.asList("3585", "34413", "2471", "2507", "2036", "2156");
            checkDataObservations(dimensionCodes, INDICATOR25_UUID, newProductionVersion, data);
        }
    }

    /*
     * Populate data for a published version
     */
    @Test
    public void testPopulateIndicatorDataOnlyDiffusionVersionUpdates() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR26_DS_GPE_UUID_V1))).thenReturn(INDICATOR26_GPE_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR26_DS_GPE_UUID_V2))).thenReturn(INDICATOR26_GPE_JSON_DATA);

        Date beforeUpdateDate = new Date();

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR26_UUID);

        // After populate, diffusion and production version have to change
        String newDiffusionVersion = IndicatorsVersionUtils.createNextVersion(getHasVersionNumberMock(INDICATOR26_DIFFUSION_VERSION), VersionTypeEnum.MINOR);

        // old diffusion version does not longer exist
        try {
            getIndicatorsService().retrieveIndicator(getServiceContextAdministrador(), INDICATOR26_UUID, INDICATOR26_DIFFUSION_VERSION);
            fail("Should have not found old version");
        } catch (MetamacException e) {
            assertNotNull(e.getExceptionItems());
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_VERSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
        }

        Map<String, List<String>> dimensionCodes = new HashMap<String, List<String>>();
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.TIME.name(), Arrays.asList("2011-01", "2010", "2010-12", "2010-11", "2010-10", "2010-09"));
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name(), Arrays.asList("ES"));
        dimensionCodes.put(IndicatorDataDimensionTypeEnum.MEASURE.name(), Arrays.asList(MeasureDimensionTypeEnum.ABSOLUTE.name()));

        {
            IndicatorVersion indicatorVersion = getIndicatorsService().retrieveIndicator(getServiceContextAdministrador(), INDICATOR26_UUID, newDiffusionVersion);
            assertTrue(beforeUpdateDate.before(indicatorVersion.getLastUpdated().toDate())); // last update date, changed
            checkDataDimensions(dimensionCodes, INDICATOR26_UUID, newDiffusionVersion);
            List<String> data = Arrays.asList("3585", "34413", "2471", "2507", "2036", "2156");
            checkDataObservations(dimensionCodes, INDICATOR26_UUID, newDiffusionVersion, data);
        }
        { // Production version remains the same
            checkDataDimensions(dimensionCodes, INDICATOR26_UUID, INDICATOR26_PRODUCTION_VERSION);
            List<String> data = Arrays.asList("3585", "34413", "2471", "2507", "2036", "2156");
            checkDataObservations(dimensionCodes, INDICATOR26_UUID, INDICATOR26_PRODUCTION_VERSION, data);
        }
    }

    /*
     * Populate published indicator then all published systems will not be updated
     */
    @Test
    public void testPopulateIndicatorDataSystemDiffusionVersionNotUpdated() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR16_DS_GPE_UUID))).thenReturn(INDICATOR16_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR16_UUID);

        { // production version remains the same
            IndicatorsSystemVersion systemVersion = indicatorsSystemsService.retrieveIndicatorsSystem(getServiceContextAdministrador(), INDICATORS_SYSTEM1_UUID, INDICATORS_SYSTEM1_PRODUCTION_VERSION);
            assertNotNull(systemVersion);
        }

        { // diffusion version remains the same
            IndicatorsSystemVersion systemVersion = indicatorsSystemsService.retrieveIndicatorsSystem(getServiceContextAdministrador(), INDICATORS_SYSTEM1_UUID, INDICATORS_SYSTEM1_DIFFUSION_VERSION);
            assertNotNull(systemVersion);
        }
    }

    /*
     * Populate published indicator then all published systems will be updated
     */
    @Test
    public void testPopulateIndicatorDataNotSystemVersionUpdates() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR16_DS_GPE_UUID))).thenReturn(INDICATOR16_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR16_UUID);

        // Systems 2 and 3 only have indicator 16 in production versions, so none of their versions should be changed
        {
            IndicatorsSystemVersion systemVersion = indicatorsSystemsService.retrieveIndicatorsSystem(getServiceContextAdministrador(), INDICATORS_SYSTEM2_UUID, INDICATORS_SYSTEM2_PRODUCTION_VERSION);
            assertNotNull(systemVersion);
        }

        {
            IndicatorsSystemVersion systemVersion = indicatorsSystemsService.retrieveIndicatorsSystem(getServiceContextAdministrador(), INDICATORS_SYSTEM2_UUID, INDICATORS_SYSTEM2_DIFFUSION_VERSION);
            assertNotNull(systemVersion);
        }

        {
            IndicatorsSystemVersion systemVersion = indicatorsSystemsService.retrieveIndicatorsSystem(getServiceContextAdministrador(), INDICATORS_SYSTEM3_UUID, INDICATORS_SYSTEM3_PRODUCTION_VERSION);
            assertNotNull(systemVersion);
        }
    }

    /* TESTS CHECKING POPULATE IS ONLY DONE WHEN IT'S NECESSARY */

    /*
     * A second consecutive "populate" should not populate if no gpe data has changed
     */
    @Test
    // ignore because for INDISTAC-919 we are populating always
    @Ignore
    public void testPopulateIndicatorDataNotNecessaryAfterFirstPopulate() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        Date beforePopulate = new Date();

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID);

        IndicatorVersion indicatorVersion = getIndicatorsService().retrieveIndicator(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_VERSION);
        // Populate date must be updated
        assertTrue(beforePopulate.before(indicatorVersion.getLastPopulateDate().toDate()));
        String lastDataRepoId = indicatorVersion.getDataRepositoryId();
        DateTime lastPopulateDate = indicatorVersion.getLastPopulateDate();

        // Second populate should do nothing
        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID);
        indicatorVersion = getIndicatorsService().retrieveIndicator(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_VERSION);
        assertEquals(lastPopulateDate, indicatorVersion.getLastPopulateDate());
        assertEquals(lastDataRepoId, indicatorVersion.getDataRepositoryId());
    }

    /*
     * A second consecutive "populate" should not populate if no gpe data has changed
     */
    @Test
    // ignore because for INDISTAC-919 we are populating always
    @Ignore
    public void testPopulateIndicatorDataNotNecessary() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);
        // Data gpe updated
        when(dataGpeRepository.filterDataDefinitionsWithDataUpdatedAfter(Matchers.anyListOf(String.class), Matchers.any(Date.class))).thenReturn(Arrays.asList(INDICATOR1_DS_GPE_UUID));

        IndicatorVersion indicatorVersion = getIndicatorsService().retrieveIndicator(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_VERSION);
        indicatorVersion.setLastPopulateDate(new DateTime());
        indicatorVersionRepository.save(indicatorVersion);

        Date beforePopulate = new Date();

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID);

        indicatorVersion = getIndicatorsService().retrieveIndicator(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_VERSION);
        // Populate date must be updated
        assertTrue(beforePopulate.before(indicatorVersion.getLastPopulateDate().toDate()));
        String lastDataRepoId = indicatorVersion.getDataRepositoryId();
        DateTime lastPopulateDate = indicatorVersion.getLastPopulateDate();

        // Second populate should do nothing
        // Data gpe NOT updated
        when(dataGpeRepository.filterDataDefinitionsWithDataUpdatedAfter(Matchers.anyListOf(String.class), Matchers.any(Date.class))).thenReturn(new ArrayList<String>());
        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID);
        indicatorVersion = getIndicatorsService().retrieveIndicator(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_VERSION);
        assertEquals(lastPopulateDate, indicatorVersion.getLastPopulateDate());
        assertEquals(lastDataRepoId, indicatorVersion.getDataRepositoryId());
    }

    /*
     * A second consecutive populate should populate if some changes have been made to data in gpe
     */
    @Test
    public void testPopulateIndicatorDataDataGpeChanged() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);
        Date beforePopulate = new Date();

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID);

        IndicatorVersion indicatorVersion = getIndicatorsService().retrieveIndicator(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_VERSION);
        // Populate date must be updated
        assertTrue(beforePopulate.before(indicatorVersion.getLastPopulateDate().toDate()));
        String lastDataRepoId = indicatorVersion.getDataRepositoryId();
        DateTime lastPopulateDate = indicatorVersion.getLastPopulateDate();

        // Suddenly data in gpe has been updated, so third populate should change data
        when(dataGpeRepository.filterDataDefinitionsWithDataUpdatedAfter(Matchers.anyListOf(String.class), Matchers.any(Date.class))).thenReturn(Arrays.asList(INDICATOR1_DS_GPE_UUID));
        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID);
        indicatorVersion = getIndicatorsService().retrieveIndicator(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_VERSION);
        assertFalse(lastPopulateDate.equals(indicatorVersion.getLastPopulateDate()));
        assertFalse(lastDataRepoId.equals(indicatorVersion.getDataRepositoryId()));
        lastPopulateDate = indicatorVersion.getLastPopulateDate();
        lastDataRepoId = indicatorVersion.getDataRepositoryId();
    }

    /*
     * A second consecutive populate should populate if indicatorVersion NEEDS_UPDATE
     */
    @Test
    public void testPopulateIndicatorDataIndicatorNeedsUpdate() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);
        Date beforePopulate = new Date();

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID);

        IndicatorVersion indicatorVersion = getIndicatorsService().retrieveIndicator(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_VERSION);
        // Populate date must be updated
        assertTrue(beforePopulate.before(indicatorVersion.getLastPopulateDate().toDate()));
        String lastDataRepoId = indicatorVersion.getDataRepositoryId();
        DateTime lastPopulateDate = indicatorVersion.getLastPopulateDate();

        // Suppose last populate failed, so version has needs_update, should populate data
        indicatorVersion.setNeedsUpdate(true);
        indicatorVersionRepository.save(indicatorVersion);
        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID);
        indicatorVersion = getIndicatorsService().retrieveIndicator(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_VERSION);
        assertFalse(lastPopulateDate.equals(indicatorVersion.getLastPopulateDate()));
        assertFalse(lastDataRepoId.equals(indicatorVersion.getDataRepositoryId()));
    }

    /*
     * A second consecutive populate should populate if indicatorVersion could have inconsistent data
     */
    @Test
    public void testPopulateIndicatorDataIndicatorInconsistentData() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);
        Date beforePopulate = new Date();

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID);

        IndicatorVersion indicatorVersion = getIndicatorsService().retrieveIndicator(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_VERSION);
        // Populate date must be updated
        assertTrue(beforePopulate.before(indicatorVersion.getLastPopulateDate().toDate()));
        String lastDataRepoId = indicatorVersion.getDataRepositoryId();
        DateTime lastPopulateDate = indicatorVersion.getLastPopulateDate();

        // Suppose last populate failed, so version has needs_update, should populate data
        indicatorVersion.setNeedsUpdate(true);
        indicatorVersionRepository.save(indicatorVersion);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID);
        indicatorVersion = getIndicatorsService().retrieveIndicator(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_VERSION);
        assertFalse(lastPopulateDate.equals(indicatorVersion.getLastPopulateDate()));
        assertFalse(lastDataRepoId.equals(indicatorVersion.getDataRepositoryId()));
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/IndicatorsDataServiceTest_Populate.xml";
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
