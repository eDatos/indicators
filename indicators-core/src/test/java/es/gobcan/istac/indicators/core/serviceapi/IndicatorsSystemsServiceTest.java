package es.gobcan.istac.indicators.core.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorInstanceProperties;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystem;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemHistory;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.serviceapi.utils.IndicatorsMocks;

/**
 * Test to IndicatorsSystemService. Testing: indicators systems, dimensions, indicators instances
 * Spring based transactional test with DbUnit support.
 * Only testing properties are not in Dto
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/include/indicators-notices-service-mockito.xml", "classpath:spring/include/indicators-data-service-mockito.xml",
        "classpath:spring/applicationContext-test.xml"})
@TransactionConfiguration(defaultRollback = true, transactionManager = "txManager")
@Transactional
public class IndicatorsSystemsServiceTest extends IndicatorsBaseTest {

    @Autowired
    protected IndicatorsSystemsService    indicatorsSystemService;

    @Autowired
    private IndicatorsDataService         indicatorsDataService;

    @Autowired
    private IndicatorsDataProviderService indicatorsDataProviderService;

    @Test
    public void testCreateIndicatorsSystem() throws Exception {

        IndicatorsSystemVersion indicatorsSystemVersion = new IndicatorsSystemVersion();
        indicatorsSystemVersion.setIndicatorsSystem(new IndicatorsSystem());
        indicatorsSystemVersion.getIndicatorsSystem().setCode(IndicatorsMocks.mockString(10));

        // Create
        IndicatorsSystemVersion indicatorsSystemVersionCreated = indicatorsSystemService.createIndicatorsSystem(getServiceContextAdministrador(), indicatorsSystemVersion);
        assertEquals(getServiceContextAdministrador().getUserId(), indicatorsSystemVersionCreated.getCreatedBy());
        assertEquals(getServiceContextAdministrador().getUserId(), indicatorsSystemVersionCreated.getLastUpdatedBy());

        // Validate properties are not in Dto
        String uuid = indicatorsSystemVersionCreated.getIndicatorsSystem().getUuid();
        String version = indicatorsSystemVersionCreated.getVersionNumber();
        IndicatorsSystemVersion indicatorsSystemCreated = indicatorsSystemService.retrieveIndicatorsSystem(getServiceContextAdministrador(), uuid, version);
        assertFalse(indicatorsSystemCreated.getIndicatorsSystem().getIsPublished());
        assertTrue(indicatorsSystemVersionCreated.getIsLastVersion());
        assertNull(indicatorsSystemCreated.getIndicatorsSystem().getDiffusionVersion());
    }

    @Test
    public void testDeleteIndicatorWithPublishedAndDraft() throws Exception {

        String uuid = INDICATORS_SYSTEM_1;

        // Retrieve before delete
        {
            IndicatorsSystemVersion indicatorsSystemVersion1 = indicatorsSystemService.retrieveIndicatorsSystem(getServiceContextAdministrador(), uuid, IndicatorsDataBaseTest.INIT_VERSION);
            assertFalse(indicatorsSystemVersion1.getIsLastVersion());
            IndicatorsSystemVersion indicatorsSystemVersion2 = indicatorsSystemService.retrieveIndicatorsSystem(getServiceContextAdministrador(), uuid, IndicatorsDataBaseTest.SECOND_VERSION);
            assertTrue(indicatorsSystemVersion2.getIsLastVersion());
        }

        // Delete indicator
        indicatorsSystemService.deleteIndicatorsSystem(getServiceContextAdministrador(), uuid);

        // Validation
        // Retrieve after delete
        {
            IndicatorsSystemVersion indicatorsSystemVersion1 = indicatorsSystemService.retrieveIndicatorsSystem(getServiceContextAdministrador(), uuid, IndicatorsDataBaseTest.INIT_VERSION);
            assertTrue(indicatorsSystemVersion1.getIsLastVersion());
            assertNull(indicatorsSystemVersion1.getIndicatorsSystem().getProductionVersion());
        }
    }

    @Test
    public void testVersioningIndicatorsSystem() throws Exception {

        String uuid = INDICATORS_SYSTEM_3;

        // Retrieve before versioning
        {
            IndicatorsSystemVersion indicatorsSystemVersion1 = indicatorsSystemService.retrieveIndicatorsSystem(getServiceContextAdministrador(), uuid, INDICATORS_SYSTEM_3_VERSION);
            assertTrue(indicatorsSystemVersion1.getIsLastVersion());
        }

        // Versioning
        IndicatorsSystemVersion newVersion = indicatorsSystemService.versioningIndicatorsSystem(getServiceContextAdministrador(), uuid, VersionTypeEnum.MAJOR);

        // Retrieve after delete
        {
            IndicatorsSystemVersion indicatorsSystemVersion1 = indicatorsSystemService.retrieveIndicatorsSystem(getServiceContextAdministrador(), uuid, INDICATORS_SYSTEM_3_VERSION);
            assertFalse(indicatorsSystemVersion1.getIsLastVersion());
            IndicatorsSystemVersion indicatorsSystemVersion2 = indicatorsSystemService.retrieveIndicatorsSystem(getServiceContextAdministrador(), uuid, newVersion.getVersionNumber());
            assertTrue(indicatorsSystemVersion2.getIsLastVersion());
        }
    }

    @Test
    public void testVersioningIndicatorsSystemMaximumMinorVersionReached() throws Exception {

        String uuid = INDICATORS_SYSTEM_11;

        // Retrieve before versioning
        {
            IndicatorsSystemVersion indicatorsSystemVersion1 = indicatorsSystemService.retrieveIndicatorsSystem(getServiceContextAdministrador(), uuid, INDICATORS_SYSTEM_11_VERSION);
            assertEquals(IndicatorsDataBaseTest.INIT_VERSION_MAXIMUM_MINOR_VERSION, indicatorsSystemVersion1.getVersionNumber());
        }

        // Versioning
        indicatorsSystemService.versioningIndicatorsSystem(getServiceContextAdministrador(), uuid, VersionTypeEnum.MINOR);

        // Retrieve after delete
        {
            IndicatorsSystemVersion indicatorsSystemVersion1 = indicatorsSystemService.retrieveIndicatorsSystem(getServiceContextAdministrador(), uuid, INDICATORS_SYSTEM_11_VERSION);
            assertEquals(IndicatorsDataBaseTest.INIT_VERSION_MAXIMUM_MINOR_VERSION, indicatorsSystemVersion1.getVersionNumber());
            IndicatorsSystemVersion indicatorsSystemVersion2 = indicatorsSystemService.retrieveIndicatorsSystem(getServiceContextAdministrador(), uuid, IndicatorsDataBaseTest.SECOND_VERSION);
            assertEquals(IndicatorsDataBaseTest.SECOND_VERSION, indicatorsSystemVersion2.getVersionNumber());
        }
    }

    @Test
    public void testVersioningIndicatorsSystemMinorVersionLimitReachedError() throws Exception {

        String uuid = INDICATORS_SYSTEM_12;

        // Retrieve before versioning
        {
            IndicatorsSystemVersion indicatorsSystemVersion1 = indicatorsSystemService.retrieveIndicatorsSystem(getServiceContextAdministrador(), uuid, INDICATORS_SYSTEM_12_VERSION);
            assertEquals(IndicatorsDataBaseTest.MAXIMUM_LIMIT_VERSION, indicatorsSystemVersion1.getVersionNumber());
        }

        // Minor versioning
        expectedMetamacException(MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.RESOURCE_MAXIMUM_VERSION_REACHED)
                .withMessageParameters(VersionTypeEnum.MINOR, INDICATORS_SYSTEM_12_VERSION).build());

        indicatorsSystemService.versioningIndicatorsSystem(getServiceContextAdministrador(), uuid, VersionTypeEnum.MINOR);
        fail("Should not allow minor versioning when the indicator system version number rearches 99999.9");
    }

    @Test
    public void testVersioningIndicatorsSystemMajorVersionLimitReachedError() throws Exception {

        String uuid = INDICATORS_SYSTEM_12;

        // Retrieve before versioning
        {
            IndicatorsSystemVersion indicatorsSystemVersion1 = indicatorsSystemService.retrieveIndicatorsSystem(getServiceContextAdministrador(), uuid, INDICATORS_SYSTEM_12_VERSION);
            assertEquals(IndicatorsDataBaseTest.MAXIMUM_LIMIT_VERSION, indicatorsSystemVersion1.getVersionNumber());
        }

        // Major versioning
        expectedMetamacException(MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.RESOURCE_MAXIMUM_VERSION_REACHED)
                .withMessageParameters(VersionTypeEnum.MAJOR, INDICATORS_SYSTEM_12_VERSION).build());

        indicatorsSystemService.versioningIndicatorsSystem(getServiceContextAdministrador(), uuid, VersionTypeEnum.MAJOR);
        fail("Should not allow major versioning when the indicator system version number rearches 99999.9");
    }

    @Test
    public void testPublishIndicatorsSystem() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR_1_DS_GPE_UUID))).thenReturn(INDICATOR_1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR_1);

        String uuid = INDICATORS_SYSTEM_5;
        String versionNumber = IndicatorsDataBaseTest.INIT_VERSION;

        // Publish
        indicatorsSystemService.publishIndicatorsSystem(getServiceContextAdministrador(), uuid);

        // Validate properties are not in Dto
        IndicatorsSystemVersion indicatorsSystemCreated = indicatorsSystemService.retrieveIndicatorsSystem(getServiceContextAdministrador(), uuid, versionNumber);
        assertTrue(indicatorsSystemCreated.getIndicatorsSystem().getIsPublished());
    }

    @Test
    public void testArchiveIndicatorsSystem() throws Exception {

        String uuid = INDICATORS_SYSTEM_3;
        String versionNumber = INDICATORS_SYSTEM_3_VERSION;

        // Archive
        indicatorsSystemService.archiveIndicatorsSystem(getServiceContextAdministrador(), uuid);

        // Validate properties are not in Dto
        IndicatorsSystemVersion indicatorsSystemCreated = indicatorsSystemService.retrieveIndicatorsSystem(getServiceContextAdministrador(), uuid, versionNumber);
        assertFalse(indicatorsSystemCreated.getIndicatorsSystem().getIsPublished());
    }

    @Test
    public void testFindIndicatorsInstancesInPublishedIndicatorsSystems() throws Exception {
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(IndicatorInstance.class).distinctRoot()
                    .withProperty(IndicatorInstanceProperties.lastValuesCache().geographicalCode()).eq("ES").orderBy(IndicatorInstanceProperties.uuid()).ascending().build();

            PagingParameter paging = PagingParameter.pageAccess(10);

            PagedResult<IndicatorInstance> indicatorsInstances = indicatorsSystemService.findIndicatorsInstancesInPublishedIndicatorsSystems(getServiceContextAdministrador(), conditions, paging);

            assertEquals(6, indicatorsInstances.getValues().size());

            assertTrue(contains(indicatorsInstances.getValues(), INDICATORS_SYSTEM_1_IINSTANCE_1));
            assertTrue(contains(indicatorsInstances.getValues(), INDICATORS_SYSTEM_10_IINSTANCE_2));
            assertTrue(contains(indicatorsInstances.getValues(), INDICATORS_SYSTEM_10_IINSTANCE_3));
            assertTrue(contains(indicatorsInstances.getValues(), INDICATORS_SYSTEM_3_IINSTANCE_1A));
            assertTrue(contains(indicatorsInstances.getValues(), INDICATORS_SYSTEM_3_IINSTANCE_2));
            assertTrue(contains(indicatorsInstances.getValues(), INDICATORS_SYSTEM_6_IINSTANCE_2));
        }
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(IndicatorInstance.class).distinctRoot()
                    .withProperty(IndicatorInstanceProperties.lastValuesCache().geographicalCode()).eq("ES611").orderBy(IndicatorInstanceProperties.uuid()).ascending().build();

            PagingParameter paging = PagingParameter.pageAccess(10);

            PagedResult<IndicatorInstance> indicatorsInstances = indicatorsSystemService.findIndicatorsInstancesInPublishedIndicatorsSystems(getServiceContextAdministrador(), conditions, paging);

            assertEquals(2, indicatorsInstances.getValues().size());

            assertTrue(contains(indicatorsInstances.getValues(), INDICATORS_SYSTEM_10_IINSTANCE_1));
            assertTrue(contains(indicatorsInstances.getValues(), INDICATORS_SYSTEM_6_IINSTANCE_1));
        }
    }

    @Test
    public void testFindIndicatorsInstancesInPublishedIndicatorsSystemsFilteredBySystem() throws Exception {
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(IndicatorInstance.class).distinctRoot()
                    .withProperty(IndicatorInstanceProperties.lastValuesCache().geographicalCode()).eq("ES")
                    .withProperty(IndicatorInstanceProperties.elementLevel().indicatorsSystemVersion().indicatorsSystem().uuid()).eq(INDICATORS_SYSTEM_1).orderBy(IndicatorInstanceProperties.uuid())
                    .ascending().build();

            PagingParameter paging = PagingParameter.pageAccess(10);

            PagedResult<IndicatorInstance> indicatorsInstances = indicatorsSystemService.findIndicatorsInstancesInPublishedIndicatorsSystems(getServiceContextAdministrador(), conditions, paging);

            assertEquals(1, indicatorsInstances.getValues().size());

            assertEquals(INDICATORS_SYSTEM_1_IINSTANCE_1, indicatorsInstances.getValues().get(0).getUuid());
        }
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(IndicatorInstance.class).distinctRoot()
                    .withProperty(IndicatorInstanceProperties.lastValuesCache().geographicalCode()).eq("ES")
                    .withProperty(IndicatorInstanceProperties.elementLevel().indicatorsSystemVersion().indicatorsSystem().uuid()).eq(INDICATORS_SYSTEM_3).orderBy(IndicatorInstanceProperties.uuid())
                    .ascending().build();

            PagingParameter paging = PagingParameter.pageAccess(10);

            PagedResult<IndicatorInstance> indicatorsInstances = indicatorsSystemService.findIndicatorsInstancesInPublishedIndicatorsSystems(getServiceContextAdministrador(), conditions, paging);

            assertEquals(2, indicatorsInstances.getValues().size());

            assertEquals(INDICATORS_SYSTEM_3_IINSTANCE_1A, indicatorsInstances.getValues().get(0).getUuid());
            assertEquals(INDICATORS_SYSTEM_3_IINSTANCE_2, indicatorsInstances.getValues().get(1).getUuid());
        }
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(IndicatorInstance.class).distinctRoot()
                    .withProperty(IndicatorInstanceProperties.lastValuesCache().geographicalCode()).eq("ES")
                    .withProperty(IndicatorInstanceProperties.elementLevel().indicatorsSystemVersion().indicatorsSystem().uuid()).eq(INDICATORS_SYSTEM_10).orderBy(IndicatorInstanceProperties.uuid())
                    .ascending().build();

            PagingParameter paging = PagingParameter.pageAccess(10);

            PagedResult<IndicatorInstance> indicatorsInstances = indicatorsSystemService.findIndicatorsInstancesInPublishedIndicatorsSystems(getServiceContextAdministrador(), conditions, paging);

            assertEquals(2, indicatorsInstances.getValues().size());

            assertEquals(INDICATORS_SYSTEM_10_IINSTANCE_2, indicatorsInstances.getValues().get(0).getUuid());
            assertEquals(INDICATORS_SYSTEM_10_IINSTANCE_3, indicatorsInstances.getValues().get(1).getUuid());
        }
    }

    @Test
    public void testFindIndicatorsInstancesInLastVersionIndicatorsSystemsFilteredBySystem() throws Exception {
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(IndicatorInstance.class).distinctRoot()
                    .withProperty(IndicatorInstanceProperties.elementLevel().indicatorsSystemVersion().indicatorsSystem().uuid()).eq(INDICATORS_SYSTEM_1).orderBy(IndicatorInstanceProperties.uuid())
                    .ascending().build();

            PagingParameter paging = PagingParameter.pageAccess(10);

            PagedResult<IndicatorInstance> indicatorsInstances = indicatorsSystemService.findIndicatorsInstancesInLastVersionIndicatorsSystems(getServiceContextAdministrador(), conditions, paging);

            assertEquals(3, indicatorsInstances.getValues().size());

            assertEquals(INDICATORS_SYSTEM_1_V2_IINSTANCE_1, indicatorsInstances.getValues().get(0).getUuid());
            assertEquals(INDICATORS_SYSTEM_1_V2_IINSTANCE_2, indicatorsInstances.getValues().get(1).getUuid());
            assertEquals(INDICATORS_SYSTEM_1_V2_IINSTANCE_3, indicatorsInstances.getValues().get(2).getUuid());
        }
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(IndicatorInstance.class).distinctRoot()
                    .withProperty(IndicatorInstanceProperties.elementLevel().indicatorsSystemVersion().indicatorsSystem().uuid()).eq(INDICATORS_SYSTEM_3).orderBy(IndicatorInstanceProperties.uuid())
                    .ascending().build();

            PagingParameter paging = PagingParameter.pageAccess(10);

            PagedResult<IndicatorInstance> indicatorsInstances = indicatorsSystemService.findIndicatorsInstancesInLastVersionIndicatorsSystems(getServiceContextAdministrador(), conditions, paging);

            assertEquals(2, indicatorsInstances.getValues().size());

            assertEquals(INDICATORS_SYSTEM_3_IINSTANCE_1A, indicatorsInstances.getValues().get(0).getUuid());
            assertEquals(INDICATORS_SYSTEM_3_IINSTANCE_2, indicatorsInstances.getValues().get(1).getUuid());
        }
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(IndicatorInstance.class).distinctRoot()
                    .withProperty(IndicatorInstanceProperties.elementLevel().indicatorsSystemVersion().indicatorsSystem().uuid()).eq(INDICATORS_SYSTEM_10).orderBy(IndicatorInstanceProperties.uuid())
                    .ascending().build();

            PagingParameter paging = PagingParameter.pageAccess(10);

            PagedResult<IndicatorInstance> indicatorsInstances = indicatorsSystemService.findIndicatorsInstancesInLastVersionIndicatorsSystems(getServiceContextAdministrador(), conditions, paging);

            assertEquals(1, indicatorsInstances.getValues().size());

            assertEquals(INDICATORS_SYSTEM_10_V2_IINSTANCE_1, indicatorsInstances.getValues().get(0).getUuid());
        }
    }

    @Test
    public void testFindIndicatorsSystemHistory() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR_1_DS_GPE_UUID))).thenReturn(INDICATOR_1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR_1);

        String uuid = INDICATORS_SYSTEM_6;
        {
            // Check not empty history
            List<IndicatorsSystemHistory> history = indicatorsSystemService.findIndicatorsSystemHistory(getServiceContextAdministrador(), uuid, Integer.MAX_VALUE);
            assertNotNull(history);
            assertEquals(3, history.size());

            assertEquals(uuid, history.get(0).getIndicatorsSystem().getUuid());
            assertEquals(IndicatorsDataBaseTest.INIT_VERSION_ANOTHER_HUGE_INCREMENT, history.get(0).getVersionNumber());
            assertEquals(uuid, history.get(1).getIndicatorsSystem().getUuid());
            assertEquals(IndicatorsDataBaseTest.INIT_VERSION_HUGE_INCREMENT, history.get(1).getVersionNumber());
            assertEquals(uuid, history.get(2).getIndicatorsSystem().getUuid());
            assertEquals(IndicatorsDataBaseTest.INIT_VERSION, history.get(2).getVersionNumber());
        }
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/IndicatorsServiceFacadeIndicatorsSystemsTest.xml";
    }

    private boolean contains(List<IndicatorInstance> instances, String uuid) {
        for (IndicatorInstance instance : instances) {
            if (uuid.equals(instance.getUuid())) {
                return true;
            }
        }
        return false;
    }
}
