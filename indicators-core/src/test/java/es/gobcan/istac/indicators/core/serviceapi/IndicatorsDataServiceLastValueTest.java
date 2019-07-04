package es.gobcan.istac.indicators.core.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.gobcan.istac.edatos.dataset.repository.dto.ObservationExtendedDto;
import es.gobcan.istac.edatos.dataset.repository.service.DatasetRepositoriesServiceFacade;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorInstanceLastValue;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorVersionLastValue;
import es.gobcan.istac.indicators.core.domain.IndicatorVersionLastValueCacheRepository;
import es.gobcan.istac.indicators.core.domain.IndicatorVersionRepository;
import es.gobcan.istac.indicators.core.domain.LastValue;
import es.gobcan.istac.indicators.core.enume.domain.MeasureDimensionTypeEnum;
import es.gobcan.istac.indicators.core.util.IndicatorsVersionUtils;

/**
 * Spring based transactional test with DbUnit support.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/include/indicators-data-service-mockito.xml", "classpath:spring/applicationContext-test.xml"})
@TransactionConfiguration(defaultRollback = true, transactionManager = "txManager")
@Transactional
public class IndicatorsDataServiceLastValueTest extends IndicatorsDataBaseTest {

    private static final String                      SUBJECT_CODE_EDUCACION            = "EDUCACION";
    private static final String                      SUBJECT_CODE_POLITICA_ECON        = "POLITICA_ECON";

    /* Has geographic and time variables */
    private static final String                      INDICATOR1_UUID                   = "Indicator-1";
    private static final String                      INDICATOR1_CODE                   = "Indicator-1-CODE";
    private static final String                      INDICATOR1_V1_DS_GPE_UUID         = "Indicator-1-v1-DataSource-1-GPE-TIME-GEO";
    private static final String                      INDICATOR1_V2_DS_GPE_UUID         = "Indicator-1-v2-DataSource-1-GPE-TIME-GEO";
    private static final String                      INDICATOR1_GPE_JSON_DATA          = readFile("json/data_temporal_spatials.json");
    private static final String                      INDICATOR1_GPE_JSON_DATA_EXTENDED = readFile("json/data_temporal_spatials_extended.json");
    private static final String                      INDICATOR1_VERSION_DIFFUSION      = IndicatorsDataBaseTest.INIT_VERSION_MINOR_INCREMENT;
    private static final String                      INDICATOR1_VERSION_PRODUCTION     = IndicatorsDataBaseTest.SECOND_VERSION;
    private static final String                      INDICATOR1_INSTANCE_111_UUID      = "IndSys-1-v1-IInstance-1";
    private static final String                      INDICATOR1_INSTANCE_111_CODE      = "IndSys-1-v1-IInstance-1-CODE";

    private static final String                      INDICATOR2_UUID                   = "Indicator-2";
    private static final String                      INDICATOR2_CODE                   = "Indicator-2-CODE";
    private static final String                      INDICATOR2_DS_GPE_UUID            = "Indicator-2-v1-DataSource-1-GPE-TIME-GEO";
    private static final String                      INDICATOR2_GPE_JSON_DATA          = readFile("json/data_temporal_spatials.json");
    private static final String                      INDICATOR2_VERSION                = IndicatorsDataBaseTest.INIT_VERSION;
    private static final String                      INDICATOR2_INSTANCE_112_UUID      = "IndSys-1-v1-IInstance-2";
    private static final String                      INDICATOR2_INSTANCE_112_CODE      = "IndSys-1-v1-IInstance-2-CODE";

    private static final String                      INDICATOR3_UUID                   = "Indicator-3";

    private static final String                      INDICATOR3_DS_GPE_UUID            = "Indicator-3-v1-DataSource-1-GPE-TIME-GEO";
    private static final String                      INDICATOR3_GPE_JSON_DATA          = readFile("json/data_temporal_spatials.json");

    private static final String                      INDICATOR4_UUID                   = "Indicator-4";
    private static final String                      INDICATOR4_DS_GPE_UUID            = "Indicator-4-v1-DataSource-1-GPE-TIME-GEO";
    private static final String                      INDICATOR4_GPE_JSON_DATA          = readFile("json/data_temporal_spatials.json");
    private static final String                      INDICATOR4_VERSION                = IndicatorsDataBaseTest.INIT_VERSION;
    private static final String                      INDICATOR4_INSTANCE_211_UUID      = "IndSys-2-v1-IInstance-1";

    private static final String                      INDICATOR5_UUID                   = "Indicator-5";
    private static final String                      INDICATOR5_DS_GPE_UUID            = "Indicator-5-v1-DataSource-1-GPE-TIME-GEO";
    private static final String                      INDICATOR5_GPE_JSON_DATA          = readFile("json/data_temporal_spatials.json");
    private static final String                      INDICATOR5_VERSION                = IndicatorsDataBaseTest.INIT_VERSION;
    private static final String                      INDICATOR5_INSTANCE_311_UUID      = "IndSys-1-v1-IInstance-3";
    private static final String                      INDICATOR5_INSTANCE_311_CODE      = "IndSys-1-v1-IInstance-3-CODE";

    private static final String                      INDICATORS_SYSTEM1_UUID           = "IndSys-1";
    private static final String                      INDICATORS_SYSTEM1_CODE           = "CODE-1";
    private static final String                      INDICATORS_SYSTEM2_UUID           = "IndSys-2";
    private static final String                      INDICATORS_SYSTEM2_CODE           = "CODE-2";

    @Autowired
    protected IndicatorsDataService                  indicatorsDataService;

    @Autowired
    private IndicatorsDataProviderService            indicatorsDataProviderService;

    @Autowired
    private DatasetRepositoriesServiceFacade         datasetRepositoriesServiceFacade;

    @Autowired
    private IndicatorsService                        indicatorsService;

    @Autowired
    private IndicatorsSystemsService                 indicatorsSystemsService;

    @Autowired
    private IndicatorVersionRepository               indicatorVersionRepository;

    @Autowired
    private IndicatorVersionLastValueCacheRepository indicatorVersionLastValueCacheRepository;

    /*** Indicator Version ***/

    @Test
    public void testFindLastValueIndicatorsVersionWithGeoValueAndSubjectCodeOrderedByLastUpdate() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_V2_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA_EXTENDED);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR2_DS_GPE_UUID))).thenReturn(INDICATOR2_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorVersionData(ctx, INDICATOR1_UUID, INDICATOR1_VERSION_DIFFUSION);
        indicatorsDataService.populateIndicatorVersionData(ctx, INDICATOR2_UUID, INDICATOR2_VERSION); // most recent

        List<MeasureDimensionTypeEnum> measures = Arrays.asList(MeasureDimensionTypeEnum.ABSOLUTE);

        {
            String GEO_CODE = "ES611";
            List<IndicatorVersionLastValue> indicatorsVersionsLatestValues = indicatorsDataService.findLastValueNLastIndicatorsVersionsWithSubjectCodeAndGeoCodeOrderedByLastUpdate(ctx,
                    SUBJECT_CODE_EDUCACION, GEO_CODE, measures, 2);

            assertNotNull(indicatorsVersionsLatestValues);
            assertEquals(2, indicatorsVersionsLatestValues.size());

            GeographicalValue geoValue = indicatorsSystemsService.retrieveGeographicalValueByCode(ctx, GEO_CODE);
            Map<MeasureDimensionTypeEnum, String> obs;

            obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "56");
            checkIndicatorVersionLastValue(indicatorsVersionsLatestValues.get(0), INDICATOR2_UUID, geoValue, "2011M01", obs);

            obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "156");
            checkIndicatorVersionLastValue(indicatorsVersionsLatestValues.get(1), INDICATOR1_UUID, geoValue, "2011M02", obs);
        }
        {
            String GEO_CODE = "ES612";
            List<IndicatorVersionLastValue> indicatorsVersionsLatestValues = indicatorsDataService.findLastValueNLastIndicatorsVersionsWithSubjectCodeAndGeoCodeOrderedByLastUpdate(ctx,
                    SUBJECT_CODE_EDUCACION, GEO_CODE, measures, 2);

            assertNotNull(indicatorsVersionsLatestValues);
            assertEquals(2, indicatorsVersionsLatestValues.size());

            GeographicalValue geoValue = indicatorsSystemsService.retrieveGeographicalValueByCode(ctx, GEO_CODE);
            Map<MeasureDimensionTypeEnum, String> obs;

            obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "60");
            checkIndicatorVersionLastValue(indicatorsVersionsLatestValues.get(0), INDICATOR2_UUID, geoValue, "2011M01", obs);

            obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "160");
            checkIndicatorVersionLastValue(indicatorsVersionsLatestValues.get(1), INDICATOR1_UUID, geoValue, "2011M02", obs);
        }
        {
            String GEO_CODE = "ES613";
            List<IndicatorVersionLastValue> indicatorsVersionsLatestValues = indicatorsDataService.findLastValueNLastIndicatorsVersionsWithSubjectCodeAndGeoCodeOrderedByLastUpdate(ctx,
                    SUBJECT_CODE_EDUCACION, GEO_CODE, measures, 2);

            assertNotNull(indicatorsVersionsLatestValues);
            assertEquals(2, indicatorsVersionsLatestValues.size());

            GeographicalValue geoValue = indicatorsSystemsService.retrieveGeographicalValueByCode(ctx, GEO_CODE);
            Map<MeasureDimensionTypeEnum, String> obs;

            obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "49");
            checkIndicatorVersionLastValue(indicatorsVersionsLatestValues.get(0), INDICATOR2_UUID, geoValue, "2011M01", obs);

            obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "149");
            checkIndicatorVersionLastValue(indicatorsVersionsLatestValues.get(1), INDICATOR1_UUID, geoValue, "2011M02", obs);
        }
    }

    @Test
    public void testFindLastValueForIndicatorsVersionsWithGeoCodeOrderedByLastUpdate() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_V2_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA_EXTENDED);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR2_DS_GPE_UUID))).thenReturn(INDICATOR2_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorVersionData(ctx, INDICATOR1_UUID, INDICATOR1_VERSION_DIFFUSION);
        indicatorsDataService.populateIndicatorVersionData(ctx, INDICATOR2_UUID, INDICATOR2_VERSION); // most recent

        List<MeasureDimensionTypeEnum> measures = Arrays.asList(MeasureDimensionTypeEnum.ABSOLUTE);

        List<String> indicatorsCodes = Arrays.asList(INDICATOR1_CODE, INDICATOR2_CODE);
        {
            String GEO_CODE = "ES611";
            List<IndicatorVersionLastValue> indicatorsVersionsLatestValues = indicatorsDataService.findLastValueForIndicatorsVersionsWithGeoCodeOrderedByLastUpdate(ctx, indicatorsCodes, GEO_CODE,
                    measures);

            assertNotNull(indicatorsVersionsLatestValues);
            assertEquals(2, indicatorsVersionsLatestValues.size());

            GeographicalValue geoValue = indicatorsSystemsService.retrieveGeographicalValueByCode(ctx, GEO_CODE);
            Map<MeasureDimensionTypeEnum, String> obs;

            obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "56");
            checkIndicatorVersionLastValue(indicatorsVersionsLatestValues.get(0), INDICATOR2_UUID, geoValue, "2011M01", obs);

            obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "156");
            checkIndicatorVersionLastValue(indicatorsVersionsLatestValues.get(1), INDICATOR1_UUID, geoValue, "2011M02", obs);
        }
        {
            String GEO_CODE = "ES612";
            List<IndicatorVersionLastValue> indicatorsVersionsLatestValues = indicatorsDataService.findLastValueNLastIndicatorsVersionsWithSubjectCodeAndGeoCodeOrderedByLastUpdate(ctx,
                    SUBJECT_CODE_EDUCACION, GEO_CODE, measures, 2);

            assertNotNull(indicatorsVersionsLatestValues);
            assertEquals(2, indicatorsVersionsLatestValues.size());

            GeographicalValue geoValue = indicatorsSystemsService.retrieveGeographicalValueByCode(ctx, GEO_CODE);
            Map<MeasureDimensionTypeEnum, String> obs;

            obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "60");
            checkIndicatorVersionLastValue(indicatorsVersionsLatestValues.get(0), INDICATOR2_UUID, geoValue, "2011M01", obs);

            obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "160");
            checkIndicatorVersionLastValue(indicatorsVersionsLatestValues.get(1), INDICATOR1_UUID, geoValue, "2011M02", obs);
        }
        {
            String GEO_CODE = "ES613";
            List<IndicatorVersionLastValue> indicatorsVersionsLatestValues = indicatorsDataService.findLastValueNLastIndicatorsVersionsWithSubjectCodeAndGeoCodeOrderedByLastUpdate(ctx,
                    SUBJECT_CODE_EDUCACION, GEO_CODE, measures, 2);

            assertNotNull(indicatorsVersionsLatestValues);
            assertEquals(2, indicatorsVersionsLatestValues.size());

            GeographicalValue geoValue = indicatorsSystemsService.retrieveGeographicalValueByCode(ctx, GEO_CODE);
            Map<MeasureDimensionTypeEnum, String> obs;

            obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "49");
            checkIndicatorVersionLastValue(indicatorsVersionsLatestValues.get(0), INDICATOR2_UUID, geoValue, "2011M01", obs);

            obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "149");
            checkIndicatorVersionLastValue(indicatorsVersionsLatestValues.get(1), INDICATOR1_UUID, geoValue, "2011M02", obs);
        }
    }

    @Test
    public void testFindIndicatorsVersionsPublishedWithSubjectCodeAndGeoCodeOrderedByLastUpdate() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_V2_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR5_DS_GPE_UUID))).thenReturn(INDICATOR5_GPE_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR2_DS_GPE_UUID))).thenReturn(INDICATOR2_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorVersionData(ctx, INDICATOR1_UUID, INDICATOR1_VERSION_DIFFUSION);
        indicatorsDataService.populateIndicatorVersionData(ctx, INDICATOR5_UUID, INDICATOR5_VERSION);
        indicatorsDataService.populateIndicatorVersionData(ctx, INDICATOR2_UUID, INDICATOR2_VERSION); // most recent

        {
            String GEO_CODE = "ES611";
            List<IndicatorVersion> indicatorsVersions = indicatorsDataService.findIndicatorsVersionsPublishedWithSubjectCodeAndGeoCodeOrderedByLastUpdate(ctx, SUBJECT_CODE_EDUCACION, GEO_CODE);

            assertNotNull(indicatorsVersions);
            assertEquals(3, indicatorsVersions.size());

            assertEquals(INDICATOR2_UUID, indicatorsVersions.get(0).getIndicator().getUuid());
            assertEquals(INDICATOR5_UUID, indicatorsVersions.get(1).getIndicator().getUuid());
            assertEquals(INDICATOR1_UUID, indicatorsVersions.get(2).getIndicator().getUuid());
        }
        {
            String GEO_CODE = "ES612";
            List<IndicatorVersion> indicatorsVersions = indicatorsDataService.findIndicatorsVersionsPublishedWithSubjectCodeAndGeoCodeOrderedByLastUpdate(ctx, SUBJECT_CODE_EDUCACION, GEO_CODE);

            assertNotNull(indicatorsVersions);
            assertEquals(3, indicatorsVersions.size());

            assertEquals(INDICATOR2_UUID, indicatorsVersions.get(0).getIndicator().getUuid());
            assertEquals(INDICATOR5_UUID, indicatorsVersions.get(1).getIndicator().getUuid());
            assertEquals(INDICATOR1_UUID, indicatorsVersions.get(2).getIndicator().getUuid());
        }
        {
            String GEO_CODE = "ES613";
            List<IndicatorVersion> indicatorsVersions = indicatorsDataService.findIndicatorsVersionsPublishedWithSubjectCodeAndGeoCodeOrderedByLastUpdate(ctx, SUBJECT_CODE_EDUCACION, GEO_CODE);

            assertNotNull(indicatorsVersions);
            assertEquals(3, indicatorsVersions.size());

            assertEquals(INDICATOR2_UUID, indicatorsVersions.get(0).getIndicator().getUuid());
            assertEquals(INDICATOR5_UUID, indicatorsVersions.get(1).getIndicator().getUuid());
            assertEquals(INDICATOR1_UUID, indicatorsVersions.get(2).getIndicator().getUuid());
        }
    }

    @Test
    public void testFindIndicatorsVersionsPublishedWithSubjectCodeAndGeoCodeOrderedByLastUpdate_Order() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_V2_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR2_DS_GPE_UUID))).thenReturn(INDICATOR2_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorVersionData(ctx, INDICATOR1_UUID, INDICATOR1_VERSION_DIFFUSION);
        indicatorsDataService.populateIndicatorVersionData(ctx, INDICATOR2_UUID, INDICATOR2_VERSION); // most recent

        {
            String GEO_CODE = "ES611";
            List<IndicatorVersion> indicatorsVersions = indicatorsDataService.findIndicatorsVersionsPublishedWithSubjectCodeAndGeoCodeOrderedByLastUpdate(ctx, SUBJECT_CODE_EDUCACION, GEO_CODE);

            assertNotNull(indicatorsVersions);
            assertEquals(2, indicatorsVersions.size());

            assertEquals(INDICATOR2_UUID, indicatorsVersions.get(0).getIndicator().getUuid());
            assertEquals(INDICATOR1_UUID, indicatorsVersions.get(1).getIndicator().getUuid());
        }

        {
            /* mark for update */
            String indicator2VersionNumber = IndicatorsVersionUtils.createNextVersion(INDICATOR2_VERSION, VersionTypeEnum.MINOR, getMockCode()).getValue();
            IndicatorVersion indicatorVersion = indicatorsService.retrieveIndicator(ctx, INDICATOR2_UUID, indicator2VersionNumber);
            indicatorVersion.setNeedsUpdate(true);
            indicatorVersionRepository.save(indicatorVersion);

            String indicator1VersionNumber = IndicatorsVersionUtils.createNextVersion(INDICATOR1_VERSION_DIFFUSION, VersionTypeEnum.MINOR, getMockCode()).getValue();
            indicatorVersion = indicatorsService.retrieveIndicator(ctx, INDICATOR1_UUID, indicator1VersionNumber);
            indicatorVersion.setNeedsUpdate(true);
            indicatorVersionRepository.save(indicatorVersion);

            indicatorsDataService.populateIndicatorVersionData(ctx, INDICATOR2_UUID, indicator2VersionNumber);
            indicatorsDataService.populateIndicatorVersionData(ctx, INDICATOR1_UUID, indicator1VersionNumber); // most recent

            String GEO_CODE = "ES611";
            List<IndicatorVersion> indicatorsVersions = indicatorsDataService.findIndicatorsVersionsPublishedWithSubjectCodeAndGeoCodeOrderedByLastUpdate(ctx, SUBJECT_CODE_EDUCACION, GEO_CODE);

            assertNotNull(indicatorsVersions);
            assertEquals(2, indicatorsVersions.size());

            assertEquals(INDICATOR1_UUID, indicatorsVersions.get(0).getIndicator().getUuid());
            assertEquals(INDICATOR2_UUID, indicatorsVersions.get(1).getIndicator().getUuid());
        }
    }

    /* Create tests */

    @Test
    public void testCreateIndicatorVersionLastValueCacheAfterPublish() throws Exception {
        String indicatorUuid = INDICATOR3_UUID;
        ServiceContext ctx = getServiceContextAdministrador();

        List<MeasureDimensionTypeEnum> measures = Arrays.asList(MeasureDimensionTypeEnum.ABSOLUTE);
        {
            for (String geoCode : Arrays.asList("ES", "ES61", "ES611", "ES612", "ES613")) {
                List<IndicatorVersionLastValue> latestValues = indicatorsDataService.findLastValueNLastIndicatorsVersionsWithSubjectCodeAndGeoCodeOrderedByLastUpdate(ctx, SUBJECT_CODE_POLITICA_ECON,
                        geoCode, measures, 1);
                assertNotNull(latestValues);
                assertEquals(0, latestValues.size());
            }
        }

        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR3_DS_GPE_UUID))).thenReturn(INDICATOR3_GPE_JSON_DATA);
        indicatorsService.publishIndicator(ctx, indicatorUuid);

        {
            String geoCode = "ES";
            GeographicalValue geoValue = indicatorsSystemsService.retrieveGeographicalValueByCode(ctx, geoCode);

            List<IndicatorVersionLastValue> latestValues = indicatorsDataService.findLastValueNLastIndicatorsVersionsWithSubjectCodeAndGeoCodeOrderedByLastUpdate(ctx, SUBJECT_CODE_POLITICA_ECON,
                    geoCode, measures, 1);
            assertNotNull(latestValues);
            assertEquals(1, latestValues.size());

            Map<MeasureDimensionTypeEnum, String> obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "3585");
            checkIndicatorVersionLastValue(latestValues.get(0), indicatorUuid, geoValue, "2011M01", obs);
        }
        {
            String geoCode = "ES61";
            GeographicalValue geoValue = indicatorsSystemsService.retrieveGeographicalValueByCode(ctx, geoCode);

            List<IndicatorVersionLastValue> latestValues = indicatorsDataService.findLastValueNLastIndicatorsVersionsWithSubjectCodeAndGeoCodeOrderedByLastUpdate(ctx, SUBJECT_CODE_POLITICA_ECON,
                    geoCode, measures, 1);
            assertNotNull(latestValues);
            assertEquals(1, latestValues.size());

            Map<MeasureDimensionTypeEnum, String> obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "497");
            checkIndicatorVersionLastValue(latestValues.get(0), indicatorUuid, geoValue, "2011M01", obs);
        }
        {
            String geoCode = "ES611";
            GeographicalValue geoValue = indicatorsSystemsService.retrieveGeographicalValueByCode(ctx, geoCode);

            List<IndicatorVersionLastValue> latestValues = indicatorsDataService.findLastValueNLastIndicatorsVersionsWithSubjectCodeAndGeoCodeOrderedByLastUpdate(ctx, SUBJECT_CODE_POLITICA_ECON,
                    geoCode, measures, 1);
            assertNotNull(latestValues);
            assertEquals(1, latestValues.size());

            Map<MeasureDimensionTypeEnum, String> obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "56");
            checkIndicatorVersionLastValue(latestValues.get(0), indicatorUuid, geoValue, "2011M01", obs);
        }
        {
            String geoCode = "ES612";
            GeographicalValue geoValue = indicatorsSystemsService.retrieveGeographicalValueByCode(ctx, geoCode);

            List<IndicatorVersionLastValue> latestValues = indicatorsDataService.findLastValueNLastIndicatorsVersionsWithSubjectCodeAndGeoCodeOrderedByLastUpdate(ctx, SUBJECT_CODE_POLITICA_ECON,
                    geoCode, measures, 1);
            assertNotNull(latestValues);
            assertEquals(1, latestValues.size());

            Map<MeasureDimensionTypeEnum, String> obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "60");
            checkIndicatorVersionLastValue(latestValues.get(0), indicatorUuid, geoValue, "2011M01", obs);
        }
        {
            String geoCode = "ES613";
            GeographicalValue geoValue = indicatorsSystemsService.retrieveGeographicalValueByCode(ctx, geoCode);

            List<IndicatorVersionLastValue> latestValues = indicatorsDataService.findLastValueNLastIndicatorsVersionsWithSubjectCodeAndGeoCodeOrderedByLastUpdate(ctx, SUBJECT_CODE_POLITICA_ECON,
                    geoCode, measures, 1);
            assertNotNull(latestValues);
            assertEquals(1, latestValues.size());

            Map<MeasureDimensionTypeEnum, String> obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "49");
            checkIndicatorVersionLastValue(latestValues.get(0), indicatorUuid, geoValue, "2011M01", obs);
        }

    }

    @Test
    public void testCreateCacheForNotPublishedIndicatorVersion() throws Exception {
        String indicatorUuid = INDICATOR1_UUID;
        String geoCode = "ES";
        ServiceContext ctx = getServiceContextAdministrador();

        List<MeasureDimensionTypeEnum> measures = Arrays.asList(MeasureDimensionTypeEnum.ABSOLUTE);

        String indicatorVersionNumber = INDICATOR1_VERSION_PRODUCTION;
        {
            when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_V1_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

            indicatorsDataService.populateIndicatorVersionData(ctx, indicatorUuid, indicatorVersionNumber);

            List<IndicatorVersionLastValue> latestValues = indicatorsDataService.findLastValueNLastIndicatorsVersionsWithSubjectCodeAndGeoCodeOrderedByLastUpdate(ctx, SUBJECT_CODE_EDUCACION, geoCode,
                    measures, 1);

            assertNotNull(latestValues);
            assertEquals(1, latestValues.size());
        }
    }

    @Test
    public void testReloadCacheAfterPopulateIndicatorVersion() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();
        String indicatorUuid = INDICATOR1_UUID;
        String geoCode = "ES";
        GeographicalValue geoValue = indicatorsSystemsService.retrieveGeographicalValueByCode(ctx, geoCode);
        List<MeasureDimensionTypeEnum> measures = Arrays.asList(MeasureDimensionTypeEnum.ABSOLUTE);

        String indicatorVersionNumber = INDICATOR1_VERSION_DIFFUSION;
        {
            when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_V2_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);
            indicatorsDataService.populateIndicatorVersionData(ctx, INDICATOR1_UUID, indicatorVersionNumber);
            // populate published indicators changes version
            indicatorVersionNumber = IndicatorsVersionUtils.createNextVersion(indicatorVersionNumber, VersionTypeEnum.MINOR, getMockCode()).getValue();

            List<IndicatorVersionLastValue> latestValues = indicatorsDataService.findLastValueNLastIndicatorsVersionsWithSubjectCodeAndGeoCodeOrderedByLastUpdate(ctx, SUBJECT_CODE_EDUCACION, geoCode,
                    measures, 5);

            assertNotNull(latestValues);
            assertEquals(1, latestValues.size());
            Map<MeasureDimensionTypeEnum, String> obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "3585");
            checkIndicatorVersionLastValue(latestValues.get(0), indicatorUuid, geoValue, "2011M01", obs);

        }
        // DATA CHANGED New time values
        {
            when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_V2_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA_EXTENDED);

            // MARK AS NEEDS UPDATE
            IndicatorVersion indicatorVersion = indicatorsService.retrieveIndicator(ctx, INDICATOR1_UUID, indicatorVersionNumber);
            indicatorVersion.setNeedsUpdate(true);
            indicatorVersionRepository.save(indicatorVersion);

            indicatorsDataService.populateIndicatorVersionData(ctx, INDICATOR1_UUID, indicatorVersionNumber);
            // populate published indicators changes version
            indicatorVersionNumber = IndicatorsVersionUtils.createNextVersion(indicatorVersionNumber, VersionTypeEnum.MINOR, getMockCode()).getValue();

            List<IndicatorVersionLastValue> latestValues = indicatorsDataService.findLastValueNLastIndicatorsVersionsWithSubjectCodeAndGeoCodeOrderedByLastUpdate(ctx, SUBJECT_CODE_EDUCACION, geoCode,
                    measures, 5);

            assertNotNull(latestValues);
            assertEquals(1, latestValues.size());
            Map<MeasureDimensionTypeEnum, String> obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "3685");
            checkIndicatorVersionLastValue(latestValues.get(0), indicatorUuid, geoValue, "2011M02", obs);
        }
    }

    /* Delete tests */
    @Test
    public void testDeleteIndicatorVersionLastValueCacheAfterArchive() throws MetamacException {
        ServiceContext ctx = getServiceContextAdministrador();
        String indicatorUuid = INDICATOR4_UUID;
        String geoCode = "ES";
        GeographicalValue geoValue = indicatorsSystemsService.retrieveGeographicalValueByCode(ctx, geoCode);
        List<MeasureDimensionTypeEnum> measures = Arrays.asList(MeasureDimensionTypeEnum.ABSOLUTE);

        String indicatorVersionNumber = INDICATOR4_VERSION;
        String subjectCode = getIndicatorsService().retrieveIndicator(ctx, indicatorUuid, indicatorVersionNumber).getSubjectCode();
        {
            when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR4_DS_GPE_UUID))).thenReturn(INDICATOR4_GPE_JSON_DATA);
            indicatorsDataService.populateIndicatorVersionData(ctx, indicatorUuid, indicatorVersionNumber);
            // populate published indicators changes version
            indicatorVersionNumber = IndicatorsVersionUtils.createNextVersion(indicatorVersionNumber, VersionTypeEnum.MINOR, getMockCode()).getValue();

            List<IndicatorVersionLastValue> latestValues = indicatorsDataService.findLastValueNLastIndicatorsVersionsWithSubjectCodeAndGeoCodeOrderedByLastUpdate(ctx, subjectCode, geoCode, measures,
                    5);

            assertNotNull(latestValues);
            assertEquals(1, latestValues.size());
            Map<MeasureDimensionTypeEnum, String> obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "3585");
            checkIndicatorVersionLastValue(latestValues.get(0), indicatorUuid, geoValue, "2011M01", obs);

        }
        // Archive
        {
            indicatorsService.archiveIndicator(ctx, indicatorUuid);

            List<IndicatorVersionLastValue> latestValues = indicatorsDataService.findLastValueNLastIndicatorsVersionsWithSubjectCodeAndGeoCodeOrderedByLastUpdate(ctx, subjectCode, geoCode, measures,
                    5);

            assertNotNull(latestValues);
            assertEquals(0, latestValues.size());
        }
    }

    /* FIND TESTS */

    /*** Indicator Instance ***/

    @Test
    public void testFindLastValueIndicatorsInstancesInIndicatorsSystemWithGeoValueOrderedByLastUpdate() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR5_DS_GPE_UUID))).thenReturn(INDICATOR5_GPE_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_V2_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR2_DS_GPE_UUID))).thenReturn(INDICATOR2_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorVersionData(ctx, INDICATOR5_UUID, INDICATOR5_VERSION);
        indicatorsDataService.populateIndicatorVersionData(ctx, INDICATOR1_UUID, INDICATOR1_VERSION_DIFFUSION);
        indicatorsDataService.populateIndicatorVersionData(ctx, INDICATOR2_UUID, INDICATOR2_VERSION); // most recent

        List<MeasureDimensionTypeEnum> measures = Arrays.asList(MeasureDimensionTypeEnum.ABSOLUTE);

        {
            String GEO_CODE = "ES611";
            List<IndicatorInstanceLastValue> indicatorsInstancesLatestValues = indicatorsDataService.findLastValueNLastIndicatorsInstancesInIndicatorsSystemWithGeoCodeOrderedByLastUpdate(ctx,
                    INDICATORS_SYSTEM1_CODE, GEO_CODE, measures, Integer.MAX_VALUE);

            assertNotNull(indicatorsInstancesLatestValues);
            assertEquals(3, indicatorsInstancesLatestValues.size());

            GeographicalValue geoValue = indicatorsSystemsService.retrieveGeographicalValueByCode(ctx, GEO_CODE);
            Map<MeasureDimensionTypeEnum, String> obs;

            obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "422");
            checkIndicatorInstanceLastValue(indicatorsInstancesLatestValues.get(0), INDICATOR2_INSTANCE_112_UUID, geoValue, "2010", obs);

            obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "422");
            checkIndicatorInstanceLastValue(indicatorsInstancesLatestValues.get(1), INDICATOR1_INSTANCE_111_UUID, geoValue, "2010", obs);

            obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "422");
            checkIndicatorInstanceLastValue(indicatorsInstancesLatestValues.get(2), INDICATOR5_INSTANCE_311_UUID, geoValue, "2010", obs);
        }
        {
            String GEO_CODE = "ES612";
            List<IndicatorInstanceLastValue> indicatorsInstancesLatestValues = indicatorsDataService.findLastValueNLastIndicatorsInstancesInIndicatorsSystemWithGeoCodeOrderedByLastUpdate(ctx,
                    INDICATORS_SYSTEM1_CODE, GEO_CODE, measures, Integer.MAX_VALUE);

            assertNotNull(indicatorsInstancesLatestValues);
            assertEquals(3, indicatorsInstancesLatestValues.size());

            GeographicalValue geoValue = indicatorsSystemsService.retrieveGeographicalValueByCode(ctx, GEO_CODE);
            Map<MeasureDimensionTypeEnum, String> obs;

            obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "487");
            checkIndicatorInstanceLastValue(indicatorsInstancesLatestValues.get(0), INDICATOR2_INSTANCE_112_UUID, geoValue, "2010", obs);

            obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "487");
            checkIndicatorInstanceLastValue(indicatorsInstancesLatestValues.get(1), INDICATOR1_INSTANCE_111_UUID, geoValue, "2010", obs);

            obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "487");
            checkIndicatorInstanceLastValue(indicatorsInstancesLatestValues.get(2), INDICATOR5_INSTANCE_311_UUID, geoValue, "2010", obs);
        }
        {
            String GEO_CODE = "ES613";
            List<IndicatorInstanceLastValue> indicatorsInstancesLatestValues = indicatorsDataService.findLastValueNLastIndicatorsInstancesInIndicatorsSystemWithGeoCodeOrderedByLastUpdate(ctx,
                    INDICATORS_SYSTEM1_CODE, GEO_CODE, measures, Integer.MAX_VALUE);

            assertNotNull(indicatorsInstancesLatestValues);
            assertEquals(3, indicatorsInstancesLatestValues.size());

            GeographicalValue geoValue = indicatorsSystemsService.retrieveGeographicalValueByCode(ctx, GEO_CODE);
            Map<MeasureDimensionTypeEnum, String> obs;

            obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "410");
            checkIndicatorInstanceLastValue(indicatorsInstancesLatestValues.get(0), INDICATOR2_INSTANCE_112_UUID, geoValue, "2010", obs);

            obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "410");
            checkIndicatorInstanceLastValue(indicatorsInstancesLatestValues.get(1), INDICATOR1_INSTANCE_111_UUID, geoValue, "2010", obs);

            obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "410");
            checkIndicatorInstanceLastValue(indicatorsInstancesLatestValues.get(2), INDICATOR5_INSTANCE_311_UUID, geoValue, "2010", obs);
        }
    }

    @Test
    public void testFindLastValueIndicatorsInstancesInIndicatorsSystemWithGeoValueOrderedByLastUpdateLimitResults() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();
        String GEO_CODE = "ES611";
        List<MeasureDimensionTypeEnum> measures = Arrays.asList(MeasureDimensionTypeEnum.ABSOLUTE);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_V2_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR2_DS_GPE_UUID))).thenReturn(INDICATOR2_GPE_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR5_DS_GPE_UUID))).thenReturn(INDICATOR5_GPE_JSON_DATA);

        String indicator5VersionNumber = INDICATOR5_VERSION;
        String indicator1VersionNumber = INDICATOR1_VERSION_DIFFUSION;
        String indicator2VersionNumber = INDICATOR2_VERSION;

        indicatorsDataService.populateIndicatorVersionData(ctx, INDICATOR1_UUID, indicator1VersionNumber);
        indicatorsDataService.populateIndicatorVersionData(ctx, INDICATOR2_UUID, indicator2VersionNumber);
        indicatorsDataService.populateIndicatorVersionData(ctx, INDICATOR5_UUID, indicator5VersionNumber); // most recent

        indicator1VersionNumber = IndicatorsVersionUtils.createNextVersion(indicator1VersionNumber, VersionTypeEnum.MINOR, getMockCode()).getValue();
        indicator2VersionNumber = IndicatorsVersionUtils.createNextVersion(indicator2VersionNumber, VersionTypeEnum.MINOR, getMockCode()).getValue();
        indicator5VersionNumber = IndicatorsVersionUtils.createNextVersion(indicator5VersionNumber, VersionTypeEnum.MINOR, getMockCode()).getValue();

        GeographicalValue geoValue = indicatorsSystemsService.retrieveGeographicalValueByCode(ctx, GEO_CODE);
        {
            List<IndicatorInstanceLastValue> indicatorsInstancesLatestValues = indicatorsDataService.findLastValueNLastIndicatorsInstancesInIndicatorsSystemWithGeoCodeOrderedByLastUpdate(ctx,
                    INDICATORS_SYSTEM1_CODE, GEO_CODE, measures, 1);

            assertNotNull(indicatorsInstancesLatestValues);
            assertEquals(1, indicatorsInstancesLatestValues.size());

            Map<MeasureDimensionTypeEnum, String> obs;

            obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "422");
            checkIndicatorInstanceLastValue(indicatorsInstancesLatestValues.get(0), INDICATOR5_INSTANCE_311_UUID, geoValue, "2010", obs);
        }
        {
            List<IndicatorInstanceLastValue> indicatorsInstancesLatestValues = indicatorsDataService.findLastValueNLastIndicatorsInstancesInIndicatorsSystemWithGeoCodeOrderedByLastUpdate(ctx,
                    INDICATORS_SYSTEM1_CODE, GEO_CODE, measures, 2);

            assertNotNull(indicatorsInstancesLatestValues);
            assertEquals(2, indicatorsInstancesLatestValues.size());

            Map<MeasureDimensionTypeEnum, String> obs;

            obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "422");
            checkIndicatorInstanceLastValue(indicatorsInstancesLatestValues.get(0), INDICATOR5_INSTANCE_311_UUID, geoValue, "2010", obs);

            obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "422");
            checkIndicatorInstanceLastValue(indicatorsInstancesLatestValues.get(1), INDICATOR2_INSTANCE_112_UUID, geoValue, "2010", obs);
        }
        {
            List<IndicatorInstanceLastValue> indicatorsInstancesLatestValues = indicatorsDataService.findLastValueNLastIndicatorsInstancesInIndicatorsSystemWithGeoCodeOrderedByLastUpdate(ctx,
                    INDICATORS_SYSTEM1_CODE, GEO_CODE, measures, Integer.MAX_VALUE);

            assertNotNull(indicatorsInstancesLatestValues);
            assertEquals(3, indicatorsInstancesLatestValues.size());

            Map<MeasureDimensionTypeEnum, String> obs;

            obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "422");
            checkIndicatorInstanceLastValue(indicatorsInstancesLatestValues.get(0), INDICATOR5_INSTANCE_311_UUID, geoValue, "2010", obs);

            obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "422");
            checkIndicatorInstanceLastValue(indicatorsInstancesLatestValues.get(1), INDICATOR2_INSTANCE_112_UUID, geoValue, "2010", obs);

            obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "422");
            checkIndicatorInstanceLastValue(indicatorsInstancesLatestValues.get(2), INDICATOR1_INSTANCE_111_UUID, geoValue, "2010", obs);
        }
    }

    @Test
    public void testFindLastValueIndicatorsInstancesOrder() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();
        String GEO_CODE = "ES611";
        List<MeasureDimensionTypeEnum> measures = Arrays.asList(MeasureDimensionTypeEnum.ABSOLUTE);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_V2_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR2_DS_GPE_UUID))).thenReturn(INDICATOR2_GPE_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR5_DS_GPE_UUID))).thenReturn(INDICATOR5_GPE_JSON_DATA);

        String indicator5VersionNumber = INDICATOR5_VERSION;
        String indicator1VersionNumber = INDICATOR1_VERSION_DIFFUSION;
        String indicator2VersionNumber = INDICATOR2_VERSION;
        {
            indicatorsDataService.populateIndicatorVersionData(ctx, INDICATOR1_UUID, indicator1VersionNumber);
            indicatorsDataService.populateIndicatorVersionData(ctx, INDICATOR2_UUID, indicator2VersionNumber);
            indicatorsDataService.populateIndicatorVersionData(ctx, INDICATOR5_UUID, indicator5VersionNumber); // most recent

            indicator1VersionNumber = IndicatorsVersionUtils.createNextVersion(indicator1VersionNumber, VersionTypeEnum.MINOR, getMockCode()).getValue();
            indicator2VersionNumber = IndicatorsVersionUtils.createNextVersion(indicator2VersionNumber, VersionTypeEnum.MINOR, getMockCode()).getValue();
            indicator5VersionNumber = IndicatorsVersionUtils.createNextVersion(indicator5VersionNumber, VersionTypeEnum.MINOR, getMockCode()).getValue();

            GeographicalValue geoValue = indicatorsSystemsService.retrieveGeographicalValueByCode(ctx, GEO_CODE);

            List<IndicatorInstanceLastValue> indicatorsInstancesLatestValues = indicatorsDataService.findLastValueNLastIndicatorsInstancesInIndicatorsSystemWithGeoCodeOrderedByLastUpdate(ctx,
                    INDICATORS_SYSTEM1_CODE, GEO_CODE, measures, Integer.MAX_VALUE);

            assertNotNull(indicatorsInstancesLatestValues);
            assertEquals(3, indicatorsInstancesLatestValues.size());

            Map<MeasureDimensionTypeEnum, String> obs;

            obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "422");
            checkIndicatorInstanceLastValue(indicatorsInstancesLatestValues.get(0), INDICATOR5_INSTANCE_311_UUID, geoValue, "2010", obs);

            obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "422");
            checkIndicatorInstanceLastValue(indicatorsInstancesLatestValues.get(1), INDICATOR2_INSTANCE_112_UUID, geoValue, "2010", obs);

            obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "422");
            checkIndicatorInstanceLastValue(indicatorsInstancesLatestValues.get(2), INDICATOR1_INSTANCE_111_UUID, geoValue, "2010", obs);
        }
        {
            /* mark for update */
            IndicatorVersion indicatorVersion = indicatorsService.retrieveIndicator(ctx, INDICATOR5_UUID, indicator5VersionNumber);
            indicatorVersion.setNeedsUpdate(true);
            indicatorVersionRepository.save(indicatorVersion);

            indicatorVersion = indicatorsService.retrieveIndicator(ctx, INDICATOR1_UUID, indicator1VersionNumber);
            indicatorVersion.setNeedsUpdate(true);
            indicatorVersionRepository.save(indicatorVersion);

            indicatorVersion = indicatorsService.retrieveIndicator(ctx, INDICATOR2_UUID, indicator2VersionNumber);
            indicatorVersion.setNeedsUpdate(true);
            indicatorVersionRepository.save(indicatorVersion);

            indicatorsDataService.populateIndicatorVersionData(ctx, INDICATOR5_UUID, indicator5VersionNumber);
            indicatorsDataService.populateIndicatorVersionData(ctx, INDICATOR1_UUID, indicator1VersionNumber);
            indicatorsDataService.populateIndicatorVersionData(ctx, INDICATOR2_UUID, indicator2VersionNumber); // most recent

            GeographicalValue geoValue = indicatorsSystemsService.retrieveGeographicalValueByCode(ctx, GEO_CODE);

            List<IndicatorInstanceLastValue> indicatorsInstancesLatestValues = indicatorsDataService.findLastValueNLastIndicatorsInstancesInIndicatorsSystemWithGeoCodeOrderedByLastUpdate(ctx,
                    INDICATORS_SYSTEM1_CODE, GEO_CODE, measures, Integer.MAX_VALUE);

            assertNotNull(indicatorsInstancesLatestValues);
            assertEquals(3, indicatorsInstancesLatestValues.size());

            Map<MeasureDimensionTypeEnum, String> obs;

            obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "422");
            checkIndicatorInstanceLastValue(indicatorsInstancesLatestValues.get(0), INDICATOR2_INSTANCE_112_UUID, geoValue, "2010", obs);

            obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "422");
            checkIndicatorInstanceLastValue(indicatorsInstancesLatestValues.get(1), INDICATOR1_INSTANCE_111_UUID, geoValue, "2010", obs);

            obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "422");
            checkIndicatorInstanceLastValue(indicatorsInstancesLatestValues.get(2), INDICATOR5_INSTANCE_311_UUID, geoValue, "2010", obs);
        }
    }

    @Test
    public void testFindLastValueForIndicatorsInstancesWithGeoCodeOrderedByLastUpdate() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR5_DS_GPE_UUID))).thenReturn(INDICATOR5_GPE_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_V2_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR2_DS_GPE_UUID))).thenReturn(INDICATOR2_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorVersionData(ctx, INDICATOR5_UUID, INDICATOR5_VERSION);
        indicatorsDataService.populateIndicatorVersionData(ctx, INDICATOR1_UUID, INDICATOR1_VERSION_DIFFUSION);
        indicatorsDataService.populateIndicatorVersionData(ctx, INDICATOR2_UUID, INDICATOR2_VERSION); // most recent

        List<MeasureDimensionTypeEnum> measures = Arrays.asList(MeasureDimensionTypeEnum.ABSOLUTE);

        List<String> indicatorsInstancesCodes = Arrays.asList(INDICATOR1_INSTANCE_111_CODE, INDICATOR2_INSTANCE_112_CODE, INDICATOR5_INSTANCE_311_CODE);
        {
            String GEO_CODE = "ES611";
            List<IndicatorInstanceLastValue> indicatorsInstancesLatestValues = indicatorsDataService.findLastValueForIndicatorsInstancesWithGeoCodeOrderedByLastUpdate(ctx, INDICATORS_SYSTEM1_CODE,
                    indicatorsInstancesCodes, GEO_CODE, measures);

            assertNotNull(indicatorsInstancesLatestValues);
            assertEquals(3, indicatorsInstancesLatestValues.size());

            GeographicalValue geoValue = indicatorsSystemsService.retrieveGeographicalValueByCode(ctx, GEO_CODE);
            Map<MeasureDimensionTypeEnum, String> obs;

            obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "422");
            checkIndicatorInstanceLastValue(indicatorsInstancesLatestValues.get(0), INDICATOR2_INSTANCE_112_UUID, geoValue, "2010", obs);

            obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "422");
            checkIndicatorInstanceLastValue(indicatorsInstancesLatestValues.get(1), INDICATOR1_INSTANCE_111_UUID, geoValue, "2010", obs);

            obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "422");
            checkIndicatorInstanceLastValue(indicatorsInstancesLatestValues.get(2), INDICATOR5_INSTANCE_311_UUID, geoValue, "2010", obs);
        }
        {
            String GEO_CODE = "ES612";
            List<IndicatorInstanceLastValue> indicatorsInstancesLatestValues = indicatorsDataService.findLastValueForIndicatorsInstancesWithGeoCodeOrderedByLastUpdate(ctx, INDICATORS_SYSTEM1_CODE,
                    indicatorsInstancesCodes, GEO_CODE, measures);

            assertNotNull(indicatorsInstancesLatestValues);
            assertEquals(3, indicatorsInstancesLatestValues.size());

            GeographicalValue geoValue = indicatorsSystemsService.retrieveGeographicalValueByCode(ctx, GEO_CODE);
            Map<MeasureDimensionTypeEnum, String> obs;

            obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "487");
            checkIndicatorInstanceLastValue(indicatorsInstancesLatestValues.get(0), INDICATOR2_INSTANCE_112_UUID, geoValue, "2010", obs);

            obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "487");
            checkIndicatorInstanceLastValue(indicatorsInstancesLatestValues.get(1), INDICATOR1_INSTANCE_111_UUID, geoValue, "2010", obs);

            obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "487");
            checkIndicatorInstanceLastValue(indicatorsInstancesLatestValues.get(2), INDICATOR5_INSTANCE_311_UUID, geoValue, "2010", obs);
        }
        {
            String GEO_CODE = "ES613";
            List<IndicatorInstanceLastValue> indicatorsInstancesLatestValues = indicatorsDataService.findLastValueForIndicatorsInstancesWithGeoCodeOrderedByLastUpdate(ctx, INDICATORS_SYSTEM1_CODE,
                    indicatorsInstancesCodes, GEO_CODE, measures);

            assertNotNull(indicatorsInstancesLatestValues);
            assertEquals(3, indicatorsInstancesLatestValues.size());

            GeographicalValue geoValue = indicatorsSystemsService.retrieveGeographicalValueByCode(ctx, GEO_CODE);
            Map<MeasureDimensionTypeEnum, String> obs;

            obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "410");
            checkIndicatorInstanceLastValue(indicatorsInstancesLatestValues.get(0), INDICATOR2_INSTANCE_112_UUID, geoValue, "2010", obs);

            obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "410");
            checkIndicatorInstanceLastValue(indicatorsInstancesLatestValues.get(1), INDICATOR1_INSTANCE_111_UUID, geoValue, "2010", obs);

            obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "410");
            checkIndicatorInstanceLastValue(indicatorsInstancesLatestValues.get(2), INDICATOR5_INSTANCE_311_UUID, geoValue, "2010", obs);
        }
    }

    @Test
    public void testFindIndicatorsInstancesInPublishedIndicatorsSystemWithGeoCodeOrderedByLastUpdate() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_V2_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR2_DS_GPE_UUID))).thenReturn(INDICATOR2_GPE_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR5_DS_GPE_UUID))).thenReturn(INDICATOR5_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorVersionData(ctx, INDICATOR1_UUID, INDICATOR1_VERSION_DIFFUSION);
        indicatorsDataService.populateIndicatorVersionData(ctx, INDICATOR2_UUID, INDICATOR2_VERSION);
        indicatorsDataService.populateIndicatorVersionData(ctx, INDICATOR5_UUID, INDICATOR5_VERSION); // most recent

        {
            String GEO_CODE = "ES611";

            List<IndicatorInstance> indicatorsInstances = indicatorsDataService.findIndicatorsInstancesInPublishedIndicatorsSystemWithGeoCodeOrderedByLastUpdate(ctx, INDICATORS_SYSTEM1_CODE,
                    GEO_CODE);

            assertNotNull(indicatorsInstances);
            assertEquals(3, indicatorsInstances.size());

            assertEquals(INDICATOR5_INSTANCE_311_UUID, indicatorsInstances.get(0).getUuid());
            assertEquals(INDICATOR2_INSTANCE_112_UUID, indicatorsInstances.get(1).getUuid());
            assertEquals(INDICATOR1_INSTANCE_111_UUID, indicatorsInstances.get(2).getUuid());
        }
        {
            String GEO_CODE = "ES612";

            List<IndicatorInstance> indicatorsInstances = indicatorsDataService.findIndicatorsInstancesInPublishedIndicatorsSystemWithGeoCodeOrderedByLastUpdate(ctx, INDICATORS_SYSTEM1_CODE,
                    GEO_CODE);

            assertNotNull(indicatorsInstances);
            assertEquals(3, indicatorsInstances.size());

            assertEquals(INDICATOR5_INSTANCE_311_UUID, indicatorsInstances.get(0).getUuid());
            assertEquals(INDICATOR2_INSTANCE_112_UUID, indicatorsInstances.get(1).getUuid());
            assertEquals(INDICATOR1_INSTANCE_111_UUID, indicatorsInstances.get(2).getUuid());
        }
        {
            String GEO_CODE = "ES613";

            List<IndicatorInstance> indicatorsInstances = indicatorsDataService.findIndicatorsInstancesInPublishedIndicatorsSystemWithGeoCodeOrderedByLastUpdate(ctx, INDICATORS_SYSTEM1_CODE,
                    GEO_CODE);

            assertNotNull(indicatorsInstances);
            assertEquals(3, indicatorsInstances.size());

            assertEquals(INDICATOR5_INSTANCE_311_UUID, indicatorsInstances.get(0).getUuid());
            assertEquals(INDICATOR2_INSTANCE_112_UUID, indicatorsInstances.get(1).getUuid());
            assertEquals(INDICATOR1_INSTANCE_111_UUID, indicatorsInstances.get(2).getUuid());
        }
    }

    /* Create tests */
    @Test
    public void testCreateIndicatorInstanceLastValueCacheAfterPublishSystem() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();
        String GEO_CODE = "ES613";
        GeographicalValue geoValue = indicatorsSystemsService.retrieveGeographicalValueByCode(ctx, GEO_CODE);
        List<MeasureDimensionTypeEnum> measures = Arrays.asList(MeasureDimensionTypeEnum.ABSOLUTE);

        {
            List<IndicatorInstanceLastValue> indicatorsInstancesLatestValues = indicatorsDataService.findLastValueNLastIndicatorsInstancesInIndicatorsSystemWithGeoCodeOrderedByLastUpdate(ctx,
                    INDICATORS_SYSTEM2_CODE, GEO_CODE, measures, Integer.MAX_VALUE);
            assertNotNull(indicatorsInstancesLatestValues);
            assertEquals(0, indicatorsInstancesLatestValues.size());
        }

        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR4_DS_GPE_UUID))).thenReturn(INDICATOR4_GPE_JSON_DATA);
        indicatorsDataService.populateIndicatorVersionData(ctx, INDICATOR4_UUID, INDICATOR4_VERSION);
        indicatorsSystemsService.publishIndicatorsSystem(ctx, INDICATORS_SYSTEM2_UUID);

        {
            List<IndicatorInstanceLastValue> indicatorsInstancesLatestValues = indicatorsDataService.findLastValueNLastIndicatorsInstancesInIndicatorsSystemWithGeoCodeOrderedByLastUpdate(ctx,
                    INDICATORS_SYSTEM2_CODE, GEO_CODE, measures, 5);

            assertNotNull(indicatorsInstancesLatestValues);
            assertEquals(1, indicatorsInstancesLatestValues.size());

            Map<MeasureDimensionTypeEnum, String> obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "410");
            checkIndicatorInstanceLastValue(indicatorsInstancesLatestValues.get(0), INDICATOR4_INSTANCE_211_UUID, geoValue, "2010", obs);
        }
    }

    /* Delete tests */
    @Test
    public void testDeleteIndicatorInstanceLastValueCacheAfterArchiveSystem() throws MetamacException {
        ServiceContext ctx = getServiceContextAdministrador();
        String GEO_CODE = "ES611";
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_V2_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR2_DS_GPE_UUID))).thenReturn(INDICATOR2_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorVersionData(ctx, INDICATOR1_UUID, INDICATOR1_VERSION_DIFFUSION);
        indicatorsDataService.populateIndicatorVersionData(ctx, INDICATOR2_UUID, INDICATOR2_VERSION); // most recent

        List<MeasureDimensionTypeEnum> measures = Arrays.asList(MeasureDimensionTypeEnum.ABSOLUTE);

        {
            List<IndicatorInstanceLastValue> indicatorsInstancesLatestValues = indicatorsDataService.findLastValueNLastIndicatorsInstancesInIndicatorsSystemWithGeoCodeOrderedByLastUpdate(ctx,
                    INDICATORS_SYSTEM1_CODE, GEO_CODE, measures, 5);

            assertNotNull(indicatorsInstancesLatestValues);
            assertEquals(2, indicatorsInstancesLatestValues.size());

            GeographicalValue geoValue = indicatorsSystemsService.retrieveGeographicalValueByCode(ctx, GEO_CODE);
            Map<MeasureDimensionTypeEnum, String> obs;

            obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "422");
            checkIndicatorInstanceLastValue(indicatorsInstancesLatestValues.get(0), INDICATOR2_INSTANCE_112_UUID, geoValue, "2010", obs);

            obs = new HashMap<MeasureDimensionTypeEnum, String>();
            obs.put(MeasureDimensionTypeEnum.ABSOLUTE, "422");
            checkIndicatorInstanceLastValue(indicatorsInstancesLatestValues.get(1), INDICATOR1_INSTANCE_111_UUID, geoValue, "2010", obs);
        }
        {
            indicatorsSystemsService.archiveIndicatorsSystem(ctx, INDICATORS_SYSTEM1_UUID);

            List<IndicatorInstanceLastValue> indicatorsInstancesLatestValues = indicatorsDataService.findLastValueNLastIndicatorsInstancesInIndicatorsSystemWithGeoCodeOrderedByLastUpdate(ctx,
                    INDICATORS_SYSTEM1_CODE, GEO_CODE, measures, 5);

            assertNotNull(indicatorsInstancesLatestValues);
            assertEquals(0, indicatorsInstancesLatestValues.size());
        }
    }

    private void checkLastValue(LastValue lastValue, GeographicalValue geoValue, String timeValueStr, Map<MeasureDimensionTypeEnum, String> observations) {
        assertNotNull(lastValue);

        assertNotNull(lastValue.getGeographicalValue());
        assertEquals(geoValue, lastValue.getGeographicalValue());

        assertNotNull(lastValue.getTimeValue());
        assertEquals(timeValueStr, lastValue.getTimeValue().getTimeValue());

        assertNotNull(lastValue.getObservations());
        assertEquals(observations.size(), lastValue.getObservations().size());
        for (MeasureDimensionTypeEnum measure : observations.keySet()) {
            String expected = observations.get(measure);
            ObservationExtendedDto obs = lastValue.getObservations().get(measure);
            assertNotNull(obs);
            if (expected != null) {
                assertEquals(expected, obs.getPrimaryMeasure());
            } else {
                assertNull(obs.getPrimaryMeasure());
            }
        }
    }
    private void checkIndicatorVersionLastValue(IndicatorVersionLastValue indicatorVersionLastValue, String indicatorUuid, GeographicalValue geoValue, String timeValueStr,
            Map<MeasureDimensionTypeEnum, String> observations) {
        checkLastValue(indicatorVersionLastValue, geoValue, timeValueStr, observations);

        assertNotNull(indicatorVersionLastValue.getIndicatorVersion());
        assertEquals(indicatorUuid, indicatorVersionLastValue.getIndicatorVersion().getIndicator().getUuid());
    }

    private void checkIndicatorInstanceLastValue(IndicatorInstanceLastValue indicatorInstanceLastValue, String indicatorInstanceUuid, GeographicalValue geoValue, String timeValueStr,
            Map<MeasureDimensionTypeEnum, String> observations) {
        checkLastValue(indicatorInstanceLastValue, geoValue, timeValueStr, observations);

        assertNotNull(indicatorInstanceLastValue.getIndicatorInstance());
        assertEquals(indicatorInstanceUuid, indicatorInstanceLastValue.getIndicatorInstance().getUuid());
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/IndicatorsDataServiceTest_LastValue.xml";
    }

    @Override
    protected String getDataSetDSRepoFile() {
        return "dbunit/IndicatorsDataServiceTest_LastValue_DSRepo.xml";
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
