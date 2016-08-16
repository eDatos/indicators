package es.gobcan.istac.indicators.core.serviceapi;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
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

import es.gobcan.istac.indicators.core.error.ServiceExceptionParameters;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import static org.mockito.Mockito.when;

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
    private static final String              INDICATORS_SYSTEM_3       = "IndSys-3";

    private static final String              INDICATOR1_UUID           = "Indicator-1";
    private static final String              INDICATOR1_DS_GPE_UUID    = "Indicator-1-v1-DataSource-1-GPE-TIME-GEO";
    private static final String              INDICATOR1_GPE_JSON_DATA  = readFile("json/data_temporal_spatials.json");

    private static final String              INDICATOR2_UUID           = "Indicator-2";
    private static final String              INDICATOR2_DS_GPE_UUID    = "Indicator-2-v1-DataSource-1-GPE-TIME-GEO";
    private static final String              INDICATOR2_GPE_JSON_DATA  = readFile("json/data_temporal_spatials_countries.json");

    private static final String              INDICATOR3_UUID           = "Indicator-3";
    private static final String              INDICATOR3_DS_GPE_UUID    = "Indicator-3-v1-DataSource-1-GPE-TIME-GEO";
    private static final String              INDICATOR3_GPE_JSON_DATA  = readFile("json/data_temporal_spatials_provinces.json");

    private static final String              INDICATOR4_UUID           = "Indicator-4";
    private static final String              INDICATOR4_DS_GPE_UUID    = "Indicator-4-v1-DataSource-1-GPE-TIME-GEO";
    private static final String              INDICATOR4_GPE_JSON_DATA  = readFile("json/data_temporal_spatials_communities.json");

    private static final String              INDICATOR5_UUID           = "Indicator-5";
    private static final String              INDICATOR5_DS1_GPE_UUID   = "Indicator-5-v1-DataSource-1-GPE-TIME-GEO";
    private static final String              INDICATOR5_DS2_GPE_UUID   = "Indicator-5-v1-DataSource-2-GPE-TIME-GEO";
    private static final String              INDICATOR5_DS3_GPE_UUID   = "Indicator-5-v1-DataSource-3-GPE-TIME-GEO";
    private static final String              INDICATOR5_GPE1_JSON_DATA = readFile("json/data_temporal_spatials_countries.json");
    private static final String              INDICATOR5_GPE2_JSON_DATA = readFile("json/data_temporal_spatials_communities.json");
    private static final String              INDICATOR5_GPE3_JSON_DATA = readFile("json/data_temporal_spatials_provinces.json");

    private static final String              INDICATOR6_UUID           = "Indicator-6";
    private static final String              INDICATOR6_DS1_GPE_UUID   = "Indicator-6-v1-DataSource-1-GPE-TIME-GEO";
    private static final String              INDICATOR6_DS2_GPE_UUID   = "Indicator-6-v1-DataSource-2-GPE-TIME-GEO";
    private static final String              INDICATOR6_DS3_GPE_UUID   = "Indicator-6-v1-DataSource-3-GPE-TIME-GEO";
    private static final String              INDICATOR6_GPE1_JSON_DATA = readFile("json/data_temporal_spatials_countries.json");
    private static final String              INDICATOR6_GPE2_JSON_DATA = readFile("json/data_temporal_spatials_communities.json");
    private static final String              INDICATOR6_GPE3_JSON_DATA = readFile("json/data_temporal_spatials_provinces.json");

    private static final String              INDICATOR7_UUID           = "Indicator-7";
    private static final String              INDICATOR7_DS_GPE_UUID    = "Indicator-7-v1-DataSource-1-GPE-TIME-GEO";
    private static final String              INDICATOR7_GPE_JSON_DATA  = readFile("json/data_temporal_spatials_communities.json");

    private static final String              INDICATOR8_UUID           = "Indicator-8";
    private static final String              INDICATOR8_DS_GPE_UUID    = "Indicator-8-v1-DataSource-1-GPE-TIME-GEO";
    private static final String              INDICATOR8_GPE_JSON_DATA  = readFile("json/data_temporal_spatials_communities.json");

    @Test
    public void testExportEmptyDescription() throws Exception {
        populateForIndicatorsSystem2();

        InternationalString title = createInternationalString("Sistema de indicadores 2", "Indicators System 2");
        InternationalString desc = null;

        List<String> files = dsplExporterService.exportIndicatorsSystemPublishedToDsplFiles(getServiceContextAdministrador(), INDICATORS_SYSTEM_2, title, desc, false);
        assertNotNull(files);
        assertTrue(files.size() > 0);
    }

    @Test
    public void testExportEmptyTitle() throws Exception {
        populateForIndicatorsSystem2();

        InternationalString title = null;
        InternationalString desc = createInternationalString("Sistema de indicadores 2", "Indicators System 2");
        try {
            dsplExporterService.exportIndicatorsSystemPublishedToDsplFiles(getServiceContextAdministrador(), INDICATORS_SYSTEM_2, title, desc, false);
            fail("Should not allow empty title");
        } catch (MetamacException e) {
            assertNotNull(e.getExceptionItems());
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertNotNull(e.getExceptionItems().get(0).getMessageParameters());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.DSPL_DATASET_TITLE, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testExportEmptySystemUuid() throws Exception {
        populateForIndicatorsSystem2();

        InternationalString title = createInternationalString("Sistema de indicadores 2", "Indicators System 2");
        InternationalString desc = createInternationalString("Sistema de indicadores 2", "Indicators System 2");
        try {
            dsplExporterService.exportIndicatorsSystemPublishedToDsplFiles(getServiceContextAdministrador(), null, title, desc, false);
            fail("Should not allow empty indicators system uuid");
        } catch (MetamacException e) {
            assertNotNull(e.getExceptionItems());
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertNotNull(e.getExceptionItems().get(0).getMessageParameters());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.INDICATORS_SYSTEM_UUID, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testExportNotPopulatedInstances() throws Exception {

        InternationalString title = createInternationalString("Sistema de indicadores 2", "Indicators System 2");
        InternationalString desc = createInternationalString("Sistema de indicadores 2", "Indicators System 2");
        try {
            dsplExporterService.exportIndicatorsSystemPublishedToDsplFiles(getServiceContextAdministrador(), INDICATORS_SYSTEM_2, title, desc, false);
            fail("Should not allow exports with not populated instances");
        } catch (MetamacException e) {
            assertNotNull(e.getExceptionItems());
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DSPL_STRUCTURE_CREATE_ERROR.getCode(), e.getExceptionItems().get(0).getCode());
            assertNotNull(e.getExceptionItems().get(0).getMessageParameters());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(INDICATORS_SYSTEM_2, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testExportIndicatorsSystemPublishedSimple() throws Exception {
        populateForIndicatorsSystem2();
        InternationalString title = createInternationalString("Sistema de indicadores 2", "Indicators System 2");
        InternationalString desc = createInternationalString("Sistema de indicadores 2", "Indicators System 2");

        List<String> files = dsplExporterService.exportIndicatorsSystemPublishedToDsplFiles(getServiceContextAdministrador(), INDICATORS_SYSTEM_2, title, desc, false);
        assertNotNull(files);
        assertEquals(1, files.size());
    }

    @Test
    public void testExportIndicatorsSystemPublishedMergingTimeGranularitiesSimple() throws Exception {
        populateForIndicatorsSystem2();
        InternationalString title = createInternationalString("Sistema de indicadores 2", "Indicators System 2");
        InternationalString desc = createInternationalString("Sistema de indicadores 2", "Indicators System 2");

        List<String> files = dsplExporterService.exportIndicatorsSystemPublishedToDsplFiles(getServiceContextAdministrador(), INDICATORS_SYSTEM_2, title, desc, true);
        assertNotNull(files);
        assertEquals(1, files.size());
    }

    @Test
    public void testExportInstancesDifferentQuantities() throws Exception {
        populateForIndicatorsSystem3();
        InternationalString title = createInternationalString("Sistema de indicadores 3", "Indicators System 3");
        InternationalString desc = createInternationalString("Sistema de indicadores 3", "Indicators System 3");

        List<String> files = dsplExporterService.exportIndicatorsSystemPublishedToDsplFiles(getServiceContextAdministrador(), INDICATORS_SYSTEM_3, title, desc, false);
        assertNotNull(files);
        assertEquals(3, files.size());
    }

    @Test
    public void testExportInstancesMergingTimeGranularitiesDifferentQuantities() throws Exception {
        populateForIndicatorsSystem3();
        InternationalString title = createInternationalString("Sistema de indicadores 3", "Indicators System 3");
        InternationalString desc = createInternationalString("Sistema de indicadores 3", "Indicators System 3");

        List<String> files = dsplExporterService.exportIndicatorsSystemPublishedToDsplFiles(getServiceContextAdministrador(), INDICATORS_SYSTEM_3, title, desc, true);
        assertNotNull(files);
        assertEquals(1, files.size());
    }

    @Test
    public void testExportIndicatorsSystemPublishedComplex() throws Exception {
        populateForIndicatorsSystem1();

        InternationalString title = createInternationalString("Sistema de indicadores 1", "Indicators System 1");
        InternationalString desc = createInternationalString("Sistema de indicadores 1", "Indicators System 1");

        List<String> files = dsplExporterService.exportIndicatorsSystemPublishedToDsplFiles(getServiceContextAdministrador(), INDICATORS_SYSTEM_1, title, desc, false);
        assertNotNull(files);
        assertEquals(4, files.size());
    }
    @Test
    public void testExportIndicatorsSystemPublishedMergingTimeGranularitiesComplex() throws Exception {
        populateForIndicatorsSystem1();

        InternationalString title = createInternationalString("Sistema de indicadores 1", "Indicators System 1");
        InternationalString desc = createInternationalString("Sistema de indicadores 1", "Indicators System 1");

        List<String> files = dsplExporterService.exportIndicatorsSystemPublishedToDsplFiles(getServiceContextAdministrador(), INDICATORS_SYSTEM_1, title, desc, true);
        assertNotNull(files);
        assertEquals(1, files.size());
    }

    private void populateForIndicatorsSystem1() throws MetamacException {
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

    private void populateForIndicatorsSystem2() throws MetamacException {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR2_DS_GPE_UUID))).thenReturn(INDICATOR2_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR2_UUID);
    }

    private void populateForIndicatorsSystem3() throws MetamacException {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR6_DS1_GPE_UUID))).thenReturn(INDICATOR6_GPE1_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR6_DS2_GPE_UUID))).thenReturn(INDICATOR6_GPE2_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR6_DS3_GPE_UUID))).thenReturn(INDICATOR6_GPE3_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR7_DS_GPE_UUID))).thenReturn(INDICATOR7_GPE_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR8_DS_GPE_UUID))).thenReturn(INDICATOR8_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR1_UUID);
        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR6_UUID);
        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR7_UUID);
        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR8_UUID);
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
