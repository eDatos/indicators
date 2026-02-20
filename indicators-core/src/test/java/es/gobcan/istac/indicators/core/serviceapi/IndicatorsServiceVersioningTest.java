package es.gobcan.istac.indicators.core.serviceapi;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.gobcan.istac.edatos.dataset.repository.service.DatasetRepositoriesServiceFacade;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.service.SrmRestInternalService;
import es.gobcan.istac.indicators.core.serviceapi.utils.SrmResourcesMocks;
import es.gobcan.istac.indicators.core.util.IndicatorsVersionUtils;

/**
 * Test to IndicatorService. Testing: indicators, data sources...
 * Spring based transactional test with DbUnit support.
 * Only testing properties are not in Dto
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/include/indicators-data-service-batchupdate-mockito.xml", "classpath:spring/applicationContext-test.xml"})
@TransactionConfiguration(defaultRollback = true, transactionManager = "txManager")
@Transactional
public class IndicatorsServiceVersioningTest extends IndicatorsDataBaseTest {

    @Autowired
    protected IndicatorsService                                                 indicatorService;

    @Autowired
    private IndicatorsDataProviderService                                       indicatorsDataProviderService;

    @Autowired
    private IndicatorsDataService                                               indicatorsDataService;

    @Autowired
    private SrmRestInternalService                                              srmRestInternalService;

    @Autowired
    private es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService configurationService;

    @Before
    public void setUp() throws MetamacException {

        when(srmRestInternalService.retrieveCodelistLastVersion(configurationService.retrieveDefaultGeographicalCodeListUrn()))
                .thenReturn(SrmResourcesMocks.buildCodelist("defaultJsonstat", "defaultCodelistJsonstat", "es", "urn:defaultCodelistJsonstat"));

    }

    /*
     * This test tries to ensure that the version constants used in tests have the same format than the version constant used in the application. If the format of this constant changes, the test fails
     * and it will be necessary to change the format of the test constants
     */
    @Test
    public void testInitialVersion() {
        assertTrue(StringUtils.equals(IndicatorsDataBaseTest.INIT_VERSION, IndicatorsVersionUtils.INITIAL_VERSION));
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/IndicatorsDataServiceTest_BatchUpdate.xml";
    }

    @Override
    protected String getDataSetDSRepoFile() {
        return "dbunit/IndicatorsDataServiceTest_DataSetRepository.xml";
    }

    @Override
    protected IndicatorsService getIndicatorsService() {
        return null;
    }

    @Override
    protected DatasetRepositoriesServiceFacade getDatasetRepositoriesServiceFacade() {
        return null;
    }
}
