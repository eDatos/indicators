package es.gobcan.istac.indicators.core.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.gobcan.istac.edatos.dataset.repository.service.DatasetRepositoriesServiceFacade;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorVersionRepository;
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
public class IndicatorsDataServicePopulateTest extends IndicatorsDataBaseTest {

    /* Has geographic and time variables */
    private static final String                                                   INDICATOR1_UUID                       = "Indicator-1";

    private static final String                                                   INDICATOR1_VERSION                    = IndicatorsDataBaseTest.INIT_VERSION;

    /* Error indicator has no data sources */
    private static final String                                                   INDICATOR23_UUID                      = "Indicator-23";

    /* DRAFT VERSION (major) */
    private static final String                                                   INDICATOR27_UUID                      = "Indicator-27";


    List<GeographicalValue>                                                       geographicalValues                    = new ArrayList<GeographicalValue>();

    @Autowired
    protected IndicatorsDataService                                               indicatorsDataService;

    @Autowired
    private DatasetRepositoriesServiceFacade                                      datasetRepositoriesServiceFacade;

    @Autowired
    private IndicatorsService                                                     indicatorsService;

    @Autowired
    private IndicatorsSystemsService                                              indicatorsSystemsService;

    @Autowired
    private IndicatorVersionRepository                                            indicatorVersionRepository;

    @Autowired
    private SrmRestInternalService                                                srmRestInternalService;

    @Autowired
    protected es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService configurationService;

    @Before
    public void initMock() throws MetamacException {
        geographicalValues = indicatorsSystemsService.findAllGeographicalValues(getServiceContextAdministrador());
        Map<String, String> geographicalVariableElementsByCode = SrmResourcesMocks.buildVariableElementsIdByCodeOfCodelist(geographicalValues);
        when(srmRestInternalService.retrieveVariableElementsIdByCodesOfCodelists(Matchers.any(String.class))).thenReturn(geographicalVariableElementsByCode);

        when(srmRestInternalService.retrieveCodelistLastVersion(configurationService.retrieveDefaultGeographicalCodeListUrn()))
                .thenReturn(SrmResourcesMocks.buildCodelist("defaultJsonstat", "defaultCodelistJsonstat", "es", "urn:defaultCodelistJsonstat"));

    }

    @Test
    public void testPopulateIndicatorDataDraftNoDatasources() throws Exception {
        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR27_UUID);
    }

    @Test
    public void testPopulateIndicatorDataNoDatasources() throws Exception {
        List<MetamacExceptionItem> errors = indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR23_UUID);
        assertEquals(1, errors.size());
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
