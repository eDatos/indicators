package es.gobcan.istac.indicators.core.serviceapi;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.arte.statistic.dataset.repository.service.DatasetRepositoriesServiceFacade;

import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.QuantityUnitRepository;
import es.gobcan.istac.indicators.core.enume.domain.VersionTypeEnum;

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
    protected IndicatorsService           indicatorService;

    @Autowired
    protected QuantityUnitRepository      quantityUnitRepository;

    @Autowired
    private IndicatorsDataProviderService indicatorsDataProviderService;

    @Autowired
    private IndicatorsDataService         indicatorsDataService;

    /* Using one datasource */
    private static final String           INDICATOR1_UUID          = "Indicator-1";
    private static final String           INDICATOR1_DS_GPE_UUID   = "Indicator-1-v1-DataSource-1-GPE-TIME";
    private static final String           INDICATOR1_GPE_JSON_DATA = readFile("json/data_temporals_batch_update.json");
    private static final String           INDICATOR1_VERSION       = "1.001";

    @Test
    public void testVersioningIndicator() throws Exception {

        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR1_DS_GPE_UUID))).thenReturn(INDICATOR1_GPE_JSON_DATA);

        String uuid = INDICATOR1_UUID;

        // Retrieve before versioning
        {
            indicatorsDataService.populateIndicatorVersionData(getServiceContextAdministrador(), uuid, "1.000");
            IndicatorVersion indicatorVersion1 = indicatorService.retrieveIndicator(getServiceContextAdministrador(), uuid, INDICATOR1_VERSION);
            assertTrue(indicatorVersion1.getIsLastVersion());
            assertTrue(StringUtils.isNotEmpty(indicatorVersion1.getDataRepositoryId()));
            assertFalse(indicatorVersion1.getNeedsUpdate());
            assertFalse(indicatorVersion1.getInconsistentData());
        }

        // Versioning
        IndicatorVersion newVersion = indicatorService.versioningIndicator(getServiceContextAdministrador(), uuid, VersionTypeEnum.MAJOR);

        // Retrieve after delete
        {
            IndicatorVersion indicatorVersion1 = indicatorService.retrieveIndicator(getServiceContextAdministrador(), uuid, INDICATOR1_VERSION);
            assertFalse(indicatorVersion1.getIsLastVersion());
            assertTrue(StringUtils.isNotEmpty(indicatorVersion1.getDataRepositoryId()));
            assertFalse(indicatorVersion1.getNeedsUpdate());
            assertFalse(indicatorVersion1.getInconsistentData());

            IndicatorVersion indicatorVersion2 = indicatorService.retrieveIndicator(getServiceContextAdministrador(), uuid, newVersion.getVersionNumber());
            assertTrue(indicatorVersion2.getIsLastVersion());
            assertTrue(StringUtils.isNotEmpty(indicatorVersion2.getDataRepositoryId()));
            assertFalse(indicatorVersion2.getNeedsUpdate());
            assertFalse(indicatorVersion2.getInconsistentData());

            assertNotSame(indicatorVersion1.getDataRepositoryId(), indicatorVersion2.getDataRepositoryId());
        }
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