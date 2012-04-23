package es.gobcan.istac.indicators.core.serviceapi;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.oracle.OracleDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import com.arte.statistic.dataset.repository.service.DatasetRepositoriesServiceFacade;

import es.gobcan.istac.indicators.core.domain.DataGpeRepository;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorVersionRepository;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorDataDimensionTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.MeasureDimensionTypeEnum;

/**
 * Spring based transactional test with DbUnit support.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/include/indicators-data-service-batchupdate-mockito.xml", "classpath:spring/applicationContext-test.xml"})
@TransactionConfiguration(defaultRollback=false,transactionManager="txManager")
@Transactional
public class IndicatorsDataServiceBatchUpdateTest extends IndicatorsDataBaseTest {

    /* Using one datasource */
    private static final String              INDICATOR1_UUID           = "Indicator-1";
    private static final String              INDICATOR1_DS_GPE_UUID    = "Indicator-1-v1-DataSource-1-GPE-TIME";
    private static final String              INDICATOR1_GPE_JSON_DATA  = readFile("json/data_temporals_batch_update.json");
    private static final String              INDICATOR1_VERSION        = "1.000";

    /* Using multiple datasources */
    private static final String              INDICATOR2_UUID           = "Indicator-2";
    private static final String              INDICATOR2_DS1_GPE_UUID   = "Indicator-2-v1-DataSource-1-GPE-TIME";
    private static final String              INDICATOR2_DS2_GPE_UUID   = "Indicator-2-v1-DataSource-2-GPE-TIME";
    private static final String              INDICATOR2_GPE_JSON_DATA1 = readFile("json/data_batchupdate_multids_part1.json");
    private static final String              INDICATOR2_GPE_JSON_DATA2 = readFile("json/data_batchupdate_multids_part2.json");
    private static final String              INDICATOR2_VERSION        = "1.000";

    /* Wrong DataGpeUuid this indicator will fail */
    private static final String              INDICATOR3_UUID           = "Indicator-3";
    private static final String              INDICATOR3_DS_GPE_UUID    = "Indicator-3-v1-DataSource-1-GPE-NOT-EXIST";
    private static final String              INDICATOR3_GPE_JSON_DATA  = null;
    private static final String              INDICATOR3_VERSION        = "1.000";

    /* Will be marked as pending */
    private static final String              INDICATOR4_UUID           = "Indicator-4";
    private static final String              INDICATOR4_DS_GPE_UUID    = "Indicator-4-v1-DataSource-1-GPE-TIME";
    private static final String              INDICATOR4_GPE_JSON_DATA  = readFile("json/data_temporals_batch_update.json");
    private static final String              INDICATOR4_VERSION        = "1.000";

    /* Will be marked as pending */
    private static final String              INDICATOR5_UUID           = "Indicator-5";
    private static final String              INDICATOR5_DS_GPE_UUID    = "Indicator-5-v1-DataSource-1-GPE-TIME";
    private static final String              INDICATOR5_GPE_JSON_DATA  = readFile("json/data_temporals_batch_update.json");
    private static final String              INDICATOR5_VERSION        = "1.000";

    /* Using one datasource */
    private static final String              INDICATOR6_UUID           = "Indicator-6";
    private static final String              INDICATOR6_DS_GPE_UUID    = "Indicator-6-v1-DataSource-1-GPE-TIME";
    private static final String              INDICATOR6_GPE_JSON_DATA  = readFile("json/data_temporals_batch_update.json");
    private static final String              INDICATOR6_VERSION        = "1.000";

    /* Using one datasource */
    private static final String              INDICATOR7_UUID           = "Indicator-7";
    private static final String              INDICATOR7_DS_GPE_UUID    = "Indicator-7-v1-DataSource-1-GPE-TIME";
    private static final String              INDICATOR7_GPE_JSON_DATA  = readFile("json/data_temporals_batch_update.json");
    private static final String              INDICATOR7_VERSION        = "1.000";

    @Autowired
    protected IndicatorsDataService          indicatorsDataService;

    @Autowired
    protected IndicatorsConfigurationService indicatorsConfigurationService;

    @Autowired
    private IndicatorsDataProviderService    indicatorsDataProviderService;

    @Autowired
    private DataGpeRepository                dataGpeRepository;

    @Autowired
    private DatasetRepositoriesServiceFacade datasetRepositoriesServiceFacade;

    @Autowired
    private IndicatorsService                indicatorsService;

    @Autowired
    private IndicatorVersionRepository       indicatorVersionRepository;

    @Autowired
    private JpaTransactionManager            txManager;

    /*
     * Indicator with a single datasource
     */
    @Test
    public void testUpdateIndicatorsDataSingleDatasource() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);
        Date lastUpdateDate = createDate(2012, 05, 03);
        List<String> indicatorsToUpdate = Arrays.asList(INDICATOR1_DS_GPE_UUID);
        when(indicatorsConfigurationService.retrieveLastSuccessfulGpeQueryDate(Matchers.any(ServiceContext.class))).thenReturn(lastUpdateDate);
        when(dataGpeRepository.findDataDefinitionsWithDataUpdatedAfter(Matchers.eq(lastUpdateDate))).thenReturn(indicatorsToUpdate);

        indicatorsDataService.updateIndicatorsData(getServiceContextAdministrador());

        checkDataContentForIndicator(INDICATOR1_UUID, INDICATOR1_VERSION);

        IndicatorVersion indVersion = indicatorVersionRepository.retrieveIndicatorVersion(INDICATOR1_UUID,INDICATOR1_VERSION);
        assertFalse(indVersion.getNeedsUpdate());
    }

    /*
     * Indicator with a multiple datasources
     */
    @Test
    public void testUpdateIndicatorsDataMultiDatasource() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR2_DS1_GPE_UUID))).thenReturn(INDICATOR2_GPE_JSON_DATA1);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR2_DS2_GPE_UUID))).thenReturn(INDICATOR2_GPE_JSON_DATA2);
        Date lastUpdateDate = createDate(2012, 05, 03);
        List<String> indicatorsToUpdate = Arrays.asList(INDICATOR2_DS2_GPE_UUID);
        when(indicatorsConfigurationService.retrieveLastSuccessfulGpeQueryDate(Matchers.any(ServiceContext.class))).thenReturn(lastUpdateDate);
        when(dataGpeRepository.findDataDefinitionsWithDataUpdatedAfter(Matchers.eq(lastUpdateDate))).thenReturn(indicatorsToUpdate);

        indicatorsDataService.updateIndicatorsData(getServiceContextAdministrador());

        checkDataContentForIndicator(INDICATOR2_UUID, INDICATOR2_VERSION);

        IndicatorVersion indVersion = indicatorVersionRepository.retrieveIndicatorVersion(INDICATOR2_UUID,INDICATOR2_VERSION);
        assertFalse(indVersion.getNeedsUpdate());
    }

    /*
     * Indicator with a single datasource
     * data source refers to a nonexistent query in JAXI
     */
    @Test
    public void testUpdateIndicatorsDataPopulateRetrieveDataError() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR3_DS_GPE_UUID))).thenReturn(INDICATOR3_GPE_JSON_DATA);
        Date lastUpdateDate = createDate(2012, 05, 03);
        List<String> indicatorsToUpdate = Arrays.asList(INDICATOR3_DS_GPE_UUID);
        when(indicatorsConfigurationService.retrieveLastSuccessfulGpeQueryDate(Matchers.any(ServiceContext.class))).thenReturn(lastUpdateDate);
        when(dataGpeRepository.findDataDefinitionsWithDataUpdatedAfter(Matchers.eq(lastUpdateDate))).thenReturn(indicatorsToUpdate);

        indicatorsDataService.updateIndicatorsData(getServiceContextAdministrador());

        IndicatorVersion indicatorVersion = indicatorsService.retrieveIndicatorPublished(getServiceContextAdministrador(), INDICATOR3_UUID);
        // Could not be populated, data should be null
        assertNull(indicatorVersion.getDataRepositoryId());
        // populate failed should be marked as needs update
        assertTrue(indicatorVersion.getNeedsUpdate());
    }

    /*
     * Multiple indicators
     */
    @Test
    public void testUpdateIndicatorsDataMultiIndicator() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR4_DS_GPE_UUID))).thenReturn(INDICATOR4_GPE_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR5_DS_GPE_UUID))).thenReturn(INDICATOR5_GPE_JSON_DATA);

        Date lastUpdateDate = createDate(2012, 05, 03);
        List<String> indicatorsToUpdate = Arrays.asList(INDICATOR4_DS_GPE_UUID, INDICATOR5_DS_GPE_UUID);

        when(indicatorsConfigurationService.retrieveLastSuccessfulGpeQueryDate(Matchers.any(ServiceContext.class))).thenReturn(lastUpdateDate);
        when(dataGpeRepository.findDataDefinitionsWithDataUpdatedAfter(Matchers.eq(lastUpdateDate))).thenReturn(indicatorsToUpdate);

        indicatorsDataService.updateIndicatorsData(getServiceContextAdministrador());

        checkDataContentForIndicator(INDICATOR4_UUID, INDICATOR4_VERSION);
        checkDataContentForIndicator(INDICATOR5_UUID, INDICATOR5_VERSION);

        IndicatorVersion indVersion4 = indicatorVersionRepository.retrieveIndicatorVersion(INDICATOR4_UUID,INDICATOR4_VERSION);
        IndicatorVersion indVersion5 = indicatorVersionRepository.retrieveIndicatorVersion(INDICATOR5_UUID,INDICATOR5_VERSION);
        assertFalse(indVersion4.getNeedsUpdate());
        assertFalse(indVersion5.getNeedsUpdate());
    }

    /*
     * Multiple indicators, one previously needed Update
     */
    @Test
    public void testUpdateIndicatorsDataMultiIndicatorWithNeedsUpdate() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR4_DS_GPE_UUID))).thenReturn(INDICATOR4_GPE_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR5_DS_GPE_UUID))).thenReturn(INDICATOR5_GPE_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR6_DS_GPE_UUID))).thenReturn(INDICATOR6_GPE_JSON_DATA);

        setNeedsUpdateTransaction(INDICATOR6_UUID, INDICATOR6_VERSION);

        Date lastUpdateDate = createDate(2012, 05, 03);
        List<String> indicatorsToUpdate = Arrays.asList(INDICATOR4_DS_GPE_UUID, INDICATOR5_DS_GPE_UUID);

        when(indicatorsConfigurationService.retrieveLastSuccessfulGpeQueryDate(Matchers.any(ServiceContext.class))).thenReturn(lastUpdateDate);
        when(dataGpeRepository.findDataDefinitionsWithDataUpdatedAfter(Matchers.eq(lastUpdateDate))).thenReturn(indicatorsToUpdate);

        indicatorsDataService.updateIndicatorsData(getServiceContextAdministrador());

        checkDataContentForIndicator(INDICATOR4_UUID, INDICATOR4_VERSION);
        checkDataContentForIndicator(INDICATOR5_UUID, INDICATOR5_VERSION);
        checkDataContentForIndicator(INDICATOR6_UUID, INDICATOR6_VERSION);

        IndicatorVersion indVersion4 = indicatorVersionRepository.retrieveIndicatorVersion(INDICATOR4_UUID,INDICATOR4_VERSION);
        IndicatorVersion indVersion5 = indicatorVersionRepository.retrieveIndicatorVersion(INDICATOR5_UUID,INDICATOR5_VERSION);
        IndicatorVersion indVersion6 = indicatorVersionRepository.retrieveIndicatorVersion(INDICATOR6_UUID,INDICATOR6_VERSION);
        assertFalse(indVersion4.getNeedsUpdate());
        assertFalse(indVersion5.getNeedsUpdate());
        assertFalse(indVersion6.getNeedsUpdate());
    }

    /*
     * Multiple indicators, only previous indicators needs Update
     */
    @Test
    public void testUpdateIndicatorsDataMultiIndicatorNotNewUpdateOnlyNeedsUpdate() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR6_DS_GPE_UUID))).thenReturn(INDICATOR6_GPE_JSON_DATA);

        setNeedsUpdateTransaction(INDICATOR6_UUID, INDICATOR6_VERSION);

        Date lastUpdateDate = createDate(2012, 05, 03);
        List<String> indicatorsToUpdate = Arrays.asList();

        when(indicatorsConfigurationService.retrieveLastSuccessfulGpeQueryDate(Matchers.any(ServiceContext.class))).thenReturn(lastUpdateDate);
        when(dataGpeRepository.findDataDefinitionsWithDataUpdatedAfter(Matchers.eq(lastUpdateDate))).thenReturn(indicatorsToUpdate);

        indicatorsDataService.updateIndicatorsData(getServiceContextAdministrador());

        checkDataContentForIndicator(INDICATOR6_UUID, INDICATOR6_VERSION);

        IndicatorVersion indVersion = indicatorVersionRepository.retrieveIndicatorVersion(INDICATOR6_UUID,INDICATOR6_VERSION);
        assertFalse(indVersion.getNeedsUpdate());
    }

    private void setNeedsUpdateTransaction(String indicatorUuid, String indicatorVersionNumber) {
        TransactionStatus status = txManager.getTransaction(null);
        IndicatorVersion indicatorVersion = indicatorVersionRepository.retrieveIndicatorVersion(indicatorUuid,indicatorVersionNumber);
        indicatorVersion.setNeedsUpdate(Boolean.TRUE);
        indicatorVersion = indicatorVersionRepository.save(indicatorVersion);
        txManager.commit(status);
    }

    private void checkDataContentForIndicator(String indicatorUuid, String indicatorVersion) throws Exception {
        // Dimensions
        Map<String, List<String>> dimensionCodes = getTestDimensionsForIndicator(indicatorUuid);
        // DATA
        List<String> data = getTestDataForIndicator(indicatorUuid);

        checkDataDimensions(dimensionCodes, indicatorUuid, indicatorVersion);
        checkDataObservations(dimensionCodes, indicatorUuid, indicatorVersion, data);
    }

    /* Indicators use only two different kind of data so testing will be easier */
    private static List<String> getTestDataForIndicator(String indicatorUuid) {
        if (INDICATOR1_UUID.equals(indicatorUuid) || INDICATOR4_UUID.equals(indicatorUuid) || INDICATOR5_UUID.equals(indicatorUuid) || INDICATOR6_UUID.equals(indicatorUuid)
                || INDICATOR7_UUID.equals(indicatorUuid)) {
            return Arrays.asList("3.585", "34.413", "2.471", "2.507", "2.036", "2.156");
        } else if (INDICATOR2_UUID.equals(indicatorUuid)) {
            List<String> absolute = Arrays.asList("34.413", "4.546", "2.471", "329", "2.507", "347", "2.036", "297", "33.413", "3.546", "1.471", "229", "1.507", "247", "1.036", "197");
            List<String> annualPercentageRate = Arrays.asList("2,992", "28,200", "67,980", "43,668", "66,357", "40,485", "96,525", "50,761", null, null, null, null, null, null, null, null);
            List<String> interperiodPercentageRate = Arrays.asList("2,99", "28,20", "-1,43", "-5,18", "23,13", "16,83", null, null, null, null, "-2,38", "-7,28", "45,46", "25,38", null, null);
            List<String> annualPuntualRate = Arrays.asList("1.000", "1.000", "1.000", "100", "1.000", "100", "1.000", "100", null, null, null, null, null, null, null, null);
            List<String> interperiodPuntualRate = Arrays.asList("1.000", "1.000", "-36", "-18", "471", "50", null, null, null, null, "-36", "-18", "471", "50", null, null);

            List<String> data = new ArrayList<String>();
            data.addAll(absolute);
            data.addAll(annualPercentageRate);
            data.addAll(interperiodPercentageRate);
            data.addAll(annualPuntualRate);
            data.addAll(interperiodPuntualRate);
            return data;
        }
        return null;
    }

    /* Indicators use only two different kind of data so testing will be easy */
    private static Map<String, List<String>> getTestDimensionsForIndicator(String indicatorUuid) {
        Map<String, List<String>> dimensionCodes = new HashMap<String, List<String>>();
        if (INDICATOR1_UUID.equals(indicatorUuid) || INDICATOR4_UUID.equals(indicatorUuid) || INDICATOR5_UUID.equals(indicatorUuid) || INDICATOR6_UUID.equals(indicatorUuid)
                || INDICATOR7_UUID.equals(indicatorUuid)) {
            dimensionCodes.put(IndicatorDataDimensionTypeEnum.TIME.name(), Arrays.asList("2011M01", "2010", "2010M12", "2010M11", "2010M10", "2010M09"));
            dimensionCodes.put(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name(), Arrays.asList("ES"));
            dimensionCodes.put(IndicatorDataDimensionTypeEnum.MEASURE.name(), Arrays.asList(MeasureDimensionTypeEnum.ABSOLUTE.name()));

        } else if (INDICATOR2_UUID.equals(indicatorUuid)) {
            dimensionCodes.put(IndicatorDataDimensionTypeEnum.TIME.name(), Arrays.asList("2010", "2010M12", "2010M11", "2010M10", "2009", "2009M12", "2009M11", "2009M10"));
            dimensionCodes.put(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name(), Arrays.asList("ES", "ES61"));
            dimensionCodes.put(IndicatorDataDimensionTypeEnum.MEASURE.name(), Arrays.asList(MeasureDimensionTypeEnum.ABSOLUTE.name(), MeasureDimensionTypeEnum.ANNUAL_PERCENTAGE_RATE.name(),
                    MeasureDimensionTypeEnum.INTERPERIOD_PERCENTAGE_RATE.name(), MeasureDimensionTypeEnum.ANNUAL_PUNTUAL_RATE.name(), MeasureDimensionTypeEnum.INTERPERIOD_PUNTUAL_RATE.name()));
        }
        return dimensionCodes;
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

    @Override
    protected String getDataSetFile() {
        return "dbunit/IndicatorsDataServiceTest_BatchUpdate.xml";
    }

    protected String getDataSetDSRepoFile() {
        return "dbunit/IndicatorsDataServiceTest_DataSetRepository.xml";
    }

}
