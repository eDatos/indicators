package es.gobcan.istac.indicators.core.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import es.gobcan.istac.edatos.dataset.repository.service.DatasetRepositoriesServiceFacade;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.error.ServiceExceptionParameters;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.service.SrmRestInternalService;
import es.gobcan.istac.indicators.core.serviceapi.utils.SrmResourcesMocks;

/**
 * Spring based transactional test with DbUnit support.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/include/indicators-data-service-populate-mockito.xml", "classpath:spring/include/indicators-srm-service-mockito.xml",
        "classpath:spring/applicationContext-test.xml"})
@TransactionConfiguration(defaultRollback = true, transactionManager = "txManager")
@Transactional
public class DsplExporterServiceTest extends IndicatorsDataBaseTest {

    @Autowired
    private IndicatorsService                                                     indicatorsService;

    @Autowired
    private DatasetRepositoriesServiceFacade                                      datasetRepositoriesServiceFacade;

    @Autowired
    protected DsplExporterService                                                 dsplExporterService;

    @Autowired
    private SrmRestInternalService                                                srmRestInternalService;

    @Autowired
    private IndicatorsSystemsService                                              indicatorsSystemsService;

    @Autowired
    protected es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService configurationService;

    private static final String                                                   INDICATORS_SYSTEM_2       = "IndSys-2";

    List<GeographicalValue>                                                       geographicalValues        = new ArrayList<GeographicalValue>();

    @Before
    public void setUp() throws MetamacException {
        geographicalValues = indicatorsSystemsService.findAllGeographicalValues(getServiceContextAdministrador());
        Map<String, String> geographicalVariableElementsByCode = SrmResourcesMocks.buildVariableElementsIdByCodeOfCodelist(geographicalValues);
        when(srmRestInternalService.retrieveVariableElementsIdByCodesOfCodelists(Matchers.any(String.class))).thenReturn(geographicalVariableElementsByCode);

        when(srmRestInternalService.retrieveCodelistLastVersion(configurationService.retrieveDefaultGeographicalCodeListUrn()))
                .thenReturn(SrmResourcesMocks.buildCodelist("defaultJsonstat", "defaultCodelistJsonstat", "es", "urn:defaultCodelistJsonstat"));

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
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("Sistema de indicadores 2", e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(INDICATORS_SYSTEM_2, e.getExceptionItems().get(0).getMessageParameters()[1]);
        }
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
