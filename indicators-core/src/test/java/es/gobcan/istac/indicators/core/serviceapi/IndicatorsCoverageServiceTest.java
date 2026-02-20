package es.gobcan.istac.indicators.core.serviceapi;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.siemac.metamac.core.common.enume.domain.IstacTimeGranularityEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.gobcan.istac.edatos.dataset.repository.service.DatasetRepositoriesServiceFacade;
import es.gobcan.istac.indicators.core.domain.GeographicalGranularity;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.MeasureValue;
import es.gobcan.istac.indicators.core.domain.TimeGranularity;
import es.gobcan.istac.indicators.core.domain.TimeValue;
import es.gobcan.istac.indicators.core.enume.domain.MeasureDimensionTypeEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.service.SrmRestInternalService;
import es.gobcan.istac.indicators.core.serviceapi.utils.SrmResourcesMocks;
import es.gobcan.istac.indicators.core.vo.GeographicalValueVO;

/**
 * Spring based transactional test with DbUnit support.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/include/indicators-data-service-mockito.xml", "classpath:spring/include/indicators-srm-service-mockito.xml",
        "classpath:spring/applicationContext-test.xml"})
@TransactionConfiguration(defaultRollback = true, transactionManager = "txManager")
@Transactional
public class IndicatorsCoverageServiceTest extends IndicatorsDataBaseTest {

    private static final String                                                   SUBJECT_CODE_1                              = "1";
    private static final String                                                   SUBJECT_CODE_3                              = "3";

    private static final String                                                   GEO_GRANULARITY_COUNTRIES_UUID              = "1";
    private static final String                                                   GEO_GRANULARITY_COMMUNITIES_UUID            = "2";
    private static final String                                                   GEO_GRANULARITY_PROVINCES_UUID              = "3";

    /* Has geographic and time variables */
    private static final String                                                   INDICATOR1_UUID                             = "Indicator-1";
    private static final String                                                   INDICATOR1_PUBLISHED_DS_GPE_UUID            = "Indicator-1-v1-DataSource-1-GPE-TIME-GEO";
    private static final String                                                   INDICATOR1_DRAFT_DS_GPE_UUID                = "Indicator-1-v2-DataSource-1-GPE-TIME-GEO";
    private static final String                                                   INDICATOR1_GPE_JSON_DATA                    = readFile("json/data_temporal_spatials.json");
    private static final String                                                   INDICATOR1_PUBLISHED_VERSION                = IndicatorsDataBaseTest.INIT_VERSION;
    private static final String                                                   INDICATOR1_PUBLISHED_AFTER_POPULATE_VERSION = IndicatorsDataBaseTest.INIT_VERSION_MINOR_INCREMENT;
    private static final String                                                   INDICATOR1_DRAFT_VERSION                    = IndicatorsDataBaseTest.SECOND_VERSION;

    /* Has geographic and time variables */
    private static final String                                                   INDICATOR2_UUID                             = "Indicator-2";
    private static final String                                                   INDICATOR2_DS_GPE_UUID                      = "Indicator-2-v1-DataSource-1-GPE-TIME-GEO";
    private static final String                                                   INDICATOR2_GPE_JSON_DATA                    = readFile("json/data_temporal_spatials.json");

    /* Has no geographic and temporal variables */
    private static final String                                                   INDICATOR3_UUID                             = "Indicator-3";
    private static final String                                                   INDICATOR3_DS_GPE_UUID                      = "Indicator-3-v1-DataSource-1-GPE-NOTIME-NOGEO";
    private static final String                                                   INDICATOR3_GPE_JSON_DATA                    = readFile("json/data_fixed.json");
    private static final String                                                   INDICATOR3_VERSION                          = IndicatorsDataBaseTest.INIT_VERSION;

    /* Has no geographic and temporal variables */
    private static final String                                                   INDICATOR4_UUID                             = "Indicator-4";
    private static final String                                                   INDICATOR4_DS_GPE_UUID                      = "Indicator-4-v1-DataSource-1-GPE-NOTIME-NOGEO";
    private static final String                                                   INDICATOR4_GPE_JSON_DATA                    = readFile("json/data_fixed.json");
    private static final String                                                   INDICATOR4_VERSION                          = IndicatorsDataBaseTest.INIT_VERSION;

    /* Has no geographic and temporal variables */
    private static final String                                                   INDICATOR5_UUID                             = "Indicator-5";
    private static final String                                                   INDICATOR5_DS_GPE_UUID                      = "Indicator-5-v1-DataSource-1-GPE-NOTIME-NOGEO";
    private static final String                                                   INDICATOR5_GPE_JSON_DATA                    = readFile("json/data_fixed.json");
    private static final String                                                   INDICATOR5_VERSION                          = IndicatorsDataBaseTest.INIT_VERSION;

    /* Has geographic and time variables */
    private static final String                                                   INDICATOR6_UUID                             = "Indicator-6";
    private static final String                                                   INDICATOR6_DS_GPE_UUID                      = "Indicator-6-v1-DataSource-1-GPE-TIME-GEO";
    private static final String                                                   INDICATOR6_GPE_JSON_DATA                    = readFile("json/data_temporals_calculate_ambiguous.json");

    /* Indicator instances */
    /* GEO Granularity provinces, time granularity yearly */
    private static final String                                                   INDICATOR_INSTANCE_11_UUID                  = "IndSys-1-v1-IInstance-11";
    /* NOT geographic restrictions, TIME GRANULARITY monthly */
    private static final String                                                   INDICATOR_INSTANCE_12_UUID                  = "IndSys-1-v1-IInstance-12";
    /* GEO es FIXED */
    private static final String                                                   INDICATOR_INSTANCE_13_UUID                  = "IndSys-1-v1-IInstance-13";
    /* GEO es FIXED time is fixed */
    private static final String                                                   INDICATOR_INSTANCE_14_UUID                  = "IndSys-1-v1-IInstance-14";

    private static final String                                                   INDICATORS_SYSTEM_2_CODE                    = "IndSys-CODE-2";

    List<GeographicalValue>                                                       geographicalValues                          = new ArrayList<GeographicalValue>();

    @Autowired
    protected IndicatorsCoverageService                                           indicatorsCoverageService;

    @Autowired
    protected IndicatorsDataService                                               indicatorsDataService;

    @Autowired
    private IndicatorsDataProviderService                                         indicatorsDataProviderService;

    @Autowired
    private DatasetRepositoriesServiceFacade                                      datasetRepositoriesServiceFacade;

    @Autowired
    private IndicatorsService                                                     indicatorsService;

    @Autowired
    private IndicatorsSystemsService                                              indicatorsSystemsService;

    @Autowired
    private SrmRestInternalService                                                srmRestInternalService;

    @Autowired
    protected es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService configurationService;


    @Test
    public void testRetrieveGeographicalGranularitiesInIndicatorNotPopulated() throws Exception {
        try {
            IndicatorVersion indicatorVersion = indicatorsService.retrieveIndicator(getServiceContextAdministrador(), INDICATOR1_UUID, INDICATOR1_DRAFT_VERSION);
            indicatorsCoverageService.retrieveGeographicalGranularitiesInIndicatorVersion(getServiceContextAdministrador(), indicatorVersion);
            fail("Should fail because indicator has not been populated");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_NOT_POPULATED.getCode(), e.getExceptionItems().get(0).getCode());
        }
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
