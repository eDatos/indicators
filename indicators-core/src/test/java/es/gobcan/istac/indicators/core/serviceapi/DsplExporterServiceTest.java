package es.gobcan.istac.indicators.core.serviceapi;

import static org.mockito.Mockito.when;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.arte.statistic.dataset.repository.service.DatasetRepositoriesServiceFacade;

/**
 * Spring based transactional test with DbUnit support.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/include/indicators-data-service-populate-mockito.xml", "classpath:spring/applicationContext-test.xml"})
@TransactionConfiguration(defaultRollback = true, transactionManager = "txManager")
@Transactional
public class DsplExporterServiceTest extends IndicatorsDataBaseTest {

    @Autowired
    private IndicatorsService                indicatorsService;

    @Autowired
    private DatasetRepositoriesServiceFacade datasetRepositoriesServiceFacade;

    @Autowired
    private IndicatorsDataProviderService    indicatorsDataProviderService;

    @Autowired
    private IndicatorsDataService            indicatorsDataService;

    @Autowired
    protected DsplExporterService            dsplExporterService;

    private static final String              INDICATORS_SYSTEM_1       = "IndSys-1";
    private static final String              INDICATORS_SYSTEM_2       = "IndSys-2";

    private static final String              INDICATOR1_UUID           = "Indicator-1";
    private static final String              INDICATOR1_DS_GPE_UUID    = "Indicator-1-v1-DataSource-1-GPE-TIME-GEO";
    private static final String              INDICATOR1_GPE_JSON_DATA  = readFile("json/data_temporal_spatials.json");
    private static final String              INDICATOR1_VERSION        = "1.000";

    private static final String              INDICATOR2_UUID           = "Indicator-2";
    private static final String              INDICATOR2_DS_GPE_UUID    = "Indicator-2-v1-DataSource-1-GPE-TIME-GEO";
    private static final String              INDICATOR2_GPE_JSON_DATA  = readFile("json/data_temporal_spatials_countries.json");
    private static final String              INDICATOR2_VERSION        = "1.000";

    private static final String              INDICATOR3_UUID           = "Indicator-3";
    private static final String              INDICATOR3_DS_GPE_UUID    = "Indicator-3-v1-DataSource-1-GPE-TIME-GEO";
    private static final String              INDICATOR3_GPE_JSON_DATA  = readFile("json/data_temporal_spatials_provinces.json");
    private static final String              INDICATOR3_VERSION        = "1.000";

    private static final String              INDICATOR4_UUID           = "Indicator-4";
    private static final String              INDICATOR4_DS_GPE_UUID    = "Indicator-4-v1-DataSource-1-GPE-TIME-GEO";
    private static final String              INDICATOR4_GPE_JSON_DATA  = readFile("json/data_temporal_spatials_communities.json");
    private static final String              INDICATOR4_VERSION        = "1.000";

    private static final String              INDICATOR5_UUID           = "Indicator-5";
    private static final String              INDICATOR5_DS1_GPE_UUID   = "Indicator-5-v1-DataSource-1-GPE-TIME-GEO";
    private static final String              INDICATOR5_DS2_GPE_UUID   = "Indicator-5-v1-DataSource-2-GPE-TIME-GEO";
    private static final String              INDICATOR5_DS3_GPE_UUID   = "Indicator-5-v1-DataSource-3-GPE-TIME-GEO";
    private static final String              INDICATOR5_GPE1_JSON_DATA = readFile("json/data_temporal_spatials_countries.json");
    private static final String              INDICATOR5_GPE2_JSON_DATA = readFile("json/data_temporal_spatials_communities.json");
    private static final String              INDICATOR5_GPE3_JSON_DATA = readFile("json/data_temporal_spatials_provinces.json");
    private static final String              INDICATOR5_VERSION        = "1.000";

    @Before
    public void prepareData() throws MetamacException {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR2_DS_GPE_UUID))).thenReturn(INDICATOR2_GPE_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR3_DS_GPE_UUID))).thenReturn(INDICATOR3_GPE_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR4_DS_GPE_UUID))).thenReturn(INDICATOR4_GPE_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR5_DS1_GPE_UUID))).thenReturn(INDICATOR5_GPE1_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR5_DS2_GPE_UUID))).thenReturn(INDICATOR5_GPE2_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR5_DS3_GPE_UUID))).thenReturn(INDICATOR5_GPE3_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID);
        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR2_UUID);
        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR3_UUID);
        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR4_UUID);
        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR5_UUID);
    }

    @Test
    public void testExportIndicatorsSystemPublished() throws Exception {

        InternationalString title = createInternationalString("title es", "title en");
        String systemUrl = "http://istac.arte-consultores.com/indicators-web/indicatorsSystems/E30259A";

        System.out.println(dsplExporterService.exportIndicatorsSystemPublishedToDsplFiles(getServiceContextAdministrador(), INDICATORS_SYSTEM_2, title, systemUrl));
    }

    @Test
    public void testExportIndicatorsSystemPublished2() throws Exception {

        InternationalString title = createInternationalString("title es", "title en");
        String systemUrl = "http://istac.arte-consultores.com/indicators-web/indicatorsSystems/E30259A";

        System.out.println(dsplExporterService.exportIndicatorsSystemPublishedToDsplFiles(getServiceContextAdministrador(), INDICATORS_SYSTEM_1, title, systemUrl));
        // List<DsplDataset> datasets = dsplExporterService.exportIndicatorsSystemPublishedToDspl(getServiceContextAdministrador(), INDICATORS_SYSTEM_1, title, systemUrl);
        // System.out.println(datasets);
    }

    private InternationalString createInternationalString(String label_es, String label_en) {
        InternationalString intStr = new InternationalString();

        LocalisedString localisedES = new LocalisedString();
        localisedES.setLocale("es");
        localisedES.setLabel(label_es);

        LocalisedString localisedEN = new LocalisedString();
        localisedEN.setLocale("en");
        localisedEN.setLabel(label_en);

        intStr.addText(localisedES);
        intStr.addText(localisedEN);

        return intStr;
    }

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
        return "dbunit/DsplExporterServiceTest.xml";
    }

    @Override
    protected String getDataSetDSRepoFile() {
        return "dbunit/IndicatorsDataServiceTest_DataSetRepository.xml";
    }
}
