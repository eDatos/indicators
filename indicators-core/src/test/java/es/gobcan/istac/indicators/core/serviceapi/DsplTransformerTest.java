package es.gobcan.istac.indicators.core.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.List;

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

import es.gobcan.istac.indicators.core.dspl.DsplDataset;
import es.gobcan.istac.indicators.core.dspl.DsplNode;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.serviceimpl.util.DsplTransformer;

/**
 * Spring based transactional test with DbUnit support.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/include/indicators-data-service-populate-mockito.xml", "classpath:spring/applicationContext-test.xml"})
@TransactionConfiguration(defaultRollback = true, transactionManager = "txManager")
@Transactional
public class DsplTransformerTest extends IndicatorsDataBaseTest {

    @Autowired
    private IndicatorsService                                                   indicatorsService;

    @Autowired
    private IndicatorsSystemsService                                            indicatorsSystemsService;

    @Autowired
    private DatasetRepositoriesServiceFacade                                    datasetRepositoriesServiceFacade;

    @Autowired
    private IndicatorsDataProviderService                                       indicatorsDataProviderService;

    @Autowired
    private IndicatorsDataService                                               indicatorsDataService;

    @Autowired
    private IndicatorsCoverageService                                           indicatorsCoverageService;

    @Autowired
    private es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService configurationService;

    private DsplTransformer                                                     dsplTransformer;

    private static final String                                                 INDICATORS_SYSTEM_1       = "IndSys-1";
    private static final String                                                 INDICATORS_SYSTEM_2       = "IndSys-2";
    private static final String                                                 INDICATORS_SYSTEM_3       = "IndSys-3";

    private static final String                                                 INDICATOR1_UUID           = "Indicator-1";
    private static final String                                                 INDICATOR1_DS_GPE_UUID    = "Indicator-1-v1-DataSource-1-GPE-TIME-GEO";
    private static final String                                                 INDICATOR1_GPE_JSON_DATA  = readFile("json/data_temporal_spatials.json");
    private static final String                                                 INDICATOR1_VERSION        = "1.000";

    private static final String                                                 INDICATOR2_UUID           = "Indicator-2";
    private static final String                                                 INDICATOR2_DS_GPE_UUID    = "Indicator-2-v1-DataSource-1-GPE-TIME-GEO";
    private static final String                                                 INDICATOR2_GPE_JSON_DATA  = readFile("json/data_temporal_spatials_countries.json");
    private static final String                                                 INDICATOR2_VERSION        = "1.000";

    private static final String                                                 INDICATOR3_UUID           = "Indicator-3";
    private static final String                                                 INDICATOR3_DS_GPE_UUID    = "Indicator-3-v1-DataSource-1-GPE-TIME-GEO";
    private static final String                                                 INDICATOR3_GPE_JSON_DATA  = readFile("json/data_temporal_spatials_provinces.json");
    private static final String                                                 INDICATOR3_VERSION        = "1.000";

    private static final String                                                 INDICATOR4_UUID           = "Indicator-4";
    private static final String                                                 INDICATOR4_DS_GPE_UUID    = "Indicator-4-v1-DataSource-1-GPE-TIME-GEO";
    private static final String                                                 INDICATOR4_GPE_JSON_DATA  = readFile("json/data_temporal_spatials_communities.json");
    private static final String                                                 INDICATOR4_VERSION        = "1.000";

    private static final String                                                 INDICATOR5_UUID           = "Indicator-5";
    private static final String                                                 INDICATOR5_DS1_GPE_UUID   = "Indicator-5-v1-DataSource-1-GPE-TIME-GEO";
    private static final String                                                 INDICATOR5_DS2_GPE_UUID   = "Indicator-5-v1-DataSource-2-GPE-TIME-GEO";
    private static final String                                                 INDICATOR5_DS3_GPE_UUID   = "Indicator-5-v1-DataSource-3-GPE-TIME-GEO";
    private static final String                                                 INDICATOR5_GPE1_JSON_DATA = readFile("json/data_temporal_spatials_countries.json");
    private static final String                                                 INDICATOR5_GPE2_JSON_DATA = readFile("json/data_temporal_spatials_communities.json");
    private static final String                                                 INDICATOR5_GPE3_JSON_DATA = readFile("json/data_temporal_spatials_provinces.json");
    private static final String                                                 INDICATOR5_VERSION        = "1.000";

    private static final String                                                 INDICATOR6_UUID           = "Indicator-6";
    private static final String                                                 INDICATOR6_DS1_GPE_UUID   = "Indicator-6-v1-DataSource-1-GPE-TIME-GEO";
    private static final String                                                 INDICATOR6_DS2_GPE_UUID   = "Indicator-6-v1-DataSource-2-GPE-TIME-GEO";
    private static final String                                                 INDICATOR6_DS3_GPE_UUID   = "Indicator-6-v1-DataSource-3-GPE-TIME-GEO";
    private static final String                                                 INDICATOR6_GPE1_JSON_DATA = readFile("json/data_temporal_spatials_countries.json");
    private static final String                                                 INDICATOR6_GPE2_JSON_DATA = readFile("json/data_temporal_spatials_communities.json");
    private static final String                                                 INDICATOR6_GPE3_JSON_DATA = readFile("json/data_temporal_spatials_provinces.json");
    private static final String                                                 INDICATOR6_VERSION        = "1.000";

    private static final String                                                 INDICATOR7_UUID           = "Indicator-7";
    private static final String                                                 INDICATOR7_DS_GPE_UUID    = "Indicator-7-v1-DataSource-1-GPE-TIME-GEO";
    private static final String                                                 INDICATOR7_GPE_JSON_DATA  = readFile("json/data_temporal_spatials_communities.json");
    private static final String                                                 INDICATOR7_VERSION        = "1.000";

    private static final String                                                 INDICATOR8_UUID           = "Indicator-8";
    private static final String                                                 INDICATOR8_DS_GPE_UUID    = "Indicator-8-v1-DataSource-1-GPE-TIME-GEO";
    private static final String                                                 INDICATOR8_GPE_JSON_DATA  = readFile("json/data_temporal_spatials_communities.json");
    private static final String                                                 INDICATOR8_VERSION        = "1.000";

    @Before
    public void createTransformer() {
        dsplTransformer = new DsplTransformer(indicatorsSystemsService, indicatorsDataService, indicatorsCoverageService, indicatorsService, configurationService);
    }

    @Test
    public void testTransformNotPopulatedInstances() throws Exception {

        InternationalString title = createInternationalString("Sistema de indicadores 2", "Indicators System 2");
        InternationalString desc = createInternationalString("Sistema de indicadores 2", "Indicators System 2");
        try {
            dsplTransformer.transformIndicatorsSystem(getServiceContextAdministrador(), INDICATORS_SYSTEM_2, title, desc);
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

        List<DsplDataset> datasets = dsplTransformer.transformIndicatorsSystem(getServiceContextAdministrador(), INDICATORS_SYSTEM_2, title, desc);
        assertNotNull(datasets);
        assertEquals(1, datasets.size());

        DsplDataset dataset = datasets.get(0);

        // Topics
        assertNotNull(dataset.getTopics());
        assertEquals(0, dataset.getTopics().size());

        // Concepts
        assertNotNull(dataset.getConcepts());
        assertEquals(3, dataset.getConcepts().size());
        assertNotNull(findNode(getGeoConceptId("countries"), dataset.getConcepts()));
        assertNotNull(findNode(getUnitConceptId("unit-2"), dataset.getConcepts()));
        assertNotNull(findNode(getIndicatorConceptId(INDICATOR2_UUID), dataset.getConcepts()));

        // Slices
        assertNotNull(dataset.getSlices());
        assertEquals(1, dataset.getSlices().size());
        assertNotNull(findNode(getSliceId("countries", "monthly"), dataset.getSlices()));

        // Tables
        assertNotNull(dataset.getTables());
        assertEquals(3, dataset.getTables().size());
        assertNotNull(findNode(getGeoTableId("countries"), dataset.getTables()));
        assertNotNull(findNode(getUnitTableId("unit-2"), dataset.getTables()));
        assertNotNull(findNode(getSliceTableId("countries", "monthly"), dataset.getTables()));

    }

    @Test
    public void testExportInstancesDifferentQuantities() throws Exception {
        populateForIndicatorsSystem3();
        InternationalString title = createInternationalString("Sistema de indicadores 3", "Indicators System 3");
        InternationalString desc = createInternationalString("Sistema de indicadores 3", "Indicators System 3");

        List<DsplDataset> datasets = dsplTransformer.transformIndicatorsSystem(getServiceContextAdministrador(), INDICATORS_SYSTEM_3, title, desc);
        assertNotNull(datasets);
        assertEquals(3, datasets.size());
    }

    @Test
    public void testExportIndicatorsSystemPublishedComplex() throws Exception {
        populateForIndicatorsSystem1();

        InternationalString title = createInternationalString("Sistema de indicadores 1", "Indicators System 1");
        InternationalString desc = createInternationalString("Sistema de indicadores 1", "Indicators System 1");

        List<DsplDataset> datasets = dsplTransformer.transformIndicatorsSystem(getServiceContextAdministrador(), INDICATORS_SYSTEM_1, title, desc);
        assertNotNull(datasets);
        assertEquals(4, datasets.size());
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

    private <T extends DsplNode> T findNode(String id, Collection<T> nodes) {
        for (T node : nodes) {
            if (node.getId().equals(id)) {
                return node;
            }
        }
        return null;
    }

    private String getGeoConceptId(String geoGranularity) {
        return "geo_" + geoGranularity;
    }

    private String getUnitConceptId(String unitUuid) {
        return "unit_" + unitUuid;
    }

    private String getIndicatorConceptId(String indicatorUuid) {
        return "quantity_" + indicatorUuid;
    }

    private String getSliceId(String geoGranularity, String timeGranularity) {
        return "slice_" + geoGranularity + "_" + timeGranularity;
    }

    private String getGeoTableId(String geoGranularity) {
        return getGeoConceptId(geoGranularity) + "_table";
    }

    private String getUnitTableId(String unit_uuid) {
        return getUnitConceptId(unit_uuid) + "_table";
    }

    private String getSliceTableId(String geoGranularity, String timeGranularity) {
        return getSliceId(geoGranularity, timeGranularity) + "_table";
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
