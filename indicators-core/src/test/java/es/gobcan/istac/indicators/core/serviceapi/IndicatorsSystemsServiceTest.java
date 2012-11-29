package es.gobcan.istac.indicators.core.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorInstanceProperties;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorVersionProperties;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystem;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemHistory;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorsSystemProcStatusEnum;
import es.gobcan.istac.indicators.core.enume.domain.VersionTypeEnum;
import es.gobcan.istac.indicators.core.serviceapi.utils.IndicatorsAsserts;
import es.gobcan.istac.indicators.core.serviceapi.utils.IndicatorsMocks;

/**
 * Test to IndicatorsSystemService. Testing: indicators systems, dimensions, indicators instances
 * Spring based transactional test with DbUnit support.
 * Only testing properties are not in Dto
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/include/indicators-data-service-mockito.xml","classpath:spring/applicationContext-test.xml"})
@TransactionConfiguration(defaultRollback = true, transactionManager = "txManager")
@Transactional
public class IndicatorsSystemsServiceTest extends IndicatorsBaseTest {

    @Autowired
    protected IndicatorsSystemsService indicatorsSystemService;
    
    @Autowired
    private IndicatorsDataService indicatorsDataService;
    
    @Autowired
    private IndicatorsDataProviderService indicatorsDataProviderService;


    private static String              INDICATORS_SYSTEM_3         = "IndSys-3";
    private static String              INDICATORS_SYSTEM_1         = "IndSys-1";
    private static String              INDICATORS_SYSTEM_3_VERSION = "11.033";
    private static String              INDICATORS_SYSTEM_5         = "IndSys-5";
    private static String              INDICATORS_SYSTEM_6         = "IndSys-6";
    private static String              INDICATORS_SYSTEM_10         = "IndSys-10";
    
    private static String             INDICATOR_1                                      = "Indicator-1";
    private static String             INDICATOR_1_DS_GPE_UUID                          = "Indicator-1-v1-DataSource-1-GPE-GEO-TIME";
    private static String             INDICATOR_1_GPE_JSON_DATA                        = readFile("json/data_temporal_spatials.json");

    private static String             INDICATORS_SYSTEM_1_IINSTANCE_1                                      = "IndSys-1-v1-IInstance-1";
    private static String             INDICATORS_SYSTEM_1_V2_IINSTANCE_1                                      = "IndSys-1-v2-IInstance-1";
    private static String             INDICATORS_SYSTEM_1_V2_IINSTANCE_2                                      = "IndSys-1-v2-IInstance-2";
    private static String             INDICATORS_SYSTEM_1_V2_IINSTANCE_3                                      = "IndSys-1-v2-IInstance-3";
    private static String             INDICATORS_SYSTEM_3_IINSTANCE_1A                                      = "IndSys-3-v1-IInstance-1A";
    private static String             INDICATORS_SYSTEM_3_IINSTANCE_2                                      = "IndSys-3-v1-IInstance-2";
    private static String             INDICATORS_SYSTEM_6_IINSTANCE_1                                      = "IndSys-6-v1-IInstance-1";
    private static String             INDICATORS_SYSTEM_6_IINSTANCE_2                                      = "IndSys-6-v1-IInstance-2";
    private static String             INDICATORS_SYSTEM_10_IINSTANCE_1                                      = "IndSys-10-v1-IInstance-1";
    private static String             INDICATORS_SYSTEM_10_V2_IINSTANCE_1                                      = "IndSys-10-v2-IInstance-1";
    private static String             INDICATORS_SYSTEM_10_IINSTANCE_2                                      = "IndSys-10-v1-IInstance-2";
    private static String             INDICATORS_SYSTEM_10_IINSTANCE_3                                      = "IndSys-10-v1-IInstance-3";
    
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
            IndicatorsSystemVersion indicatorsSystemVersion1 = indicatorsSystemService.retrieveIndicatorsSystem(getServiceContextAdministrador(), uuid, "1.000");
            assertFalse(indicatorsSystemVersion1.getIsLastVersion());
            IndicatorsSystemVersion indicatorsSystemVersion2 = indicatorsSystemService.retrieveIndicatorsSystem(getServiceContextAdministrador(), uuid, "2.000");
            assertTrue(indicatorsSystemVersion2.getIsLastVersion());
        }

        // Delete indicator
        indicatorsSystemService.deleteIndicatorsSystem(getServiceContextAdministrador(), uuid);

        // Validation
        // Retrieve after delete
        {
            IndicatorsSystemVersion indicatorsSystemVersion1 = indicatorsSystemService.retrieveIndicatorsSystem(getServiceContextAdministrador(), uuid, "1.000");
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
    public void testPublishIndicatorsSystem() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR_1_DS_GPE_UUID))).thenReturn(INDICATOR_1_GPE_JSON_DATA);
        
        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR_1);
        
        String uuid = INDICATORS_SYSTEM_5;
        String versionNumber = "1.000";

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
                .withProperty(IndicatorInstanceProperties.lastValuesCache().geographicalValue().code()).eq("ES")
                .orderBy(IndicatorInstanceProperties.uuid()).ascending()
                .build();
    
            PagingParameter paging = PagingParameter.pageAccess(10);
    
            PagedResult<IndicatorInstance> indicatorsInstances = indicatorsSystemService.findIndicatorsInstancesInPublishedIndicatorsSystems(getServiceContextAdministrador(), conditions, paging);
            
            assertEquals(6,indicatorsInstances.getValues().size());
            
            assertEquals(INDICATORS_SYSTEM_1_IINSTANCE_1,indicatorsInstances.getValues().get(0).getUuid());
            assertEquals(INDICATORS_SYSTEM_10_IINSTANCE_2,indicatorsInstances.getValues().get(1).getUuid());
            assertEquals(INDICATORS_SYSTEM_10_IINSTANCE_3,indicatorsInstances.getValues().get(2).getUuid());
            assertEquals(INDICATORS_SYSTEM_3_IINSTANCE_1A,indicatorsInstances.getValues().get(3).getUuid());
            assertEquals(INDICATORS_SYSTEM_3_IINSTANCE_2,indicatorsInstances.getValues().get(4).getUuid());
            assertEquals(INDICATORS_SYSTEM_6_IINSTANCE_2,indicatorsInstances.getValues().get(5).getUuid());
        }
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(IndicatorInstance.class).distinctRoot()
            .withProperty(IndicatorInstanceProperties.lastValuesCache().geographicalValue().code()).eq("ES611")
            .orderBy(IndicatorInstanceProperties.uuid()).ascending()
            .build();
            
            PagingParameter paging = PagingParameter.pageAccess(10);
            
            PagedResult<IndicatorInstance> indicatorsInstances = indicatorsSystemService.findIndicatorsInstancesInPublishedIndicatorsSystems(getServiceContextAdministrador(), conditions, paging);
            
            assertEquals(2,indicatorsInstances.getValues().size());
            
            assertEquals(INDICATORS_SYSTEM_10_IINSTANCE_1,indicatorsInstances.getValues().get(0).getUuid());
            assertEquals(INDICATORS_SYSTEM_6_IINSTANCE_1,indicatorsInstances.getValues().get(1).getUuid());
        }
    }
    
    @Test
    public void testFindIndicatorsInstancesInPublishedIndicatorsSystemsFilteredBySystem() throws Exception {
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(IndicatorInstance.class).distinctRoot()
                .withProperty(IndicatorInstanceProperties.lastValuesCache().geographicalValue().code()).eq("ES")
                .withProperty(IndicatorInstanceProperties.elementLevel().indicatorsSystemVersion().indicatorsSystem().uuid()).eq(INDICATORS_SYSTEM_1)
                .orderBy(IndicatorInstanceProperties.uuid()).ascending()
                .build();
            
            PagingParameter paging = PagingParameter.pageAccess(10);
            
            PagedResult<IndicatorInstance> indicatorsInstances = indicatorsSystemService.findIndicatorsInstancesInPublishedIndicatorsSystems(getServiceContextAdministrador(), conditions, paging);
            
            assertEquals(1,indicatorsInstances.getValues().size());
            
            assertEquals(INDICATORS_SYSTEM_1_IINSTANCE_1,indicatorsInstances.getValues().get(0).getUuid());
        }
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(IndicatorInstance.class).distinctRoot()
                .withProperty(IndicatorInstanceProperties.lastValuesCache().geographicalValue().code()).eq("ES")
                .withProperty(IndicatorInstanceProperties.elementLevel().indicatorsSystemVersion().indicatorsSystem().uuid()).eq(INDICATORS_SYSTEM_3)
                .orderBy(IndicatorInstanceProperties.uuid()).ascending()
                .build();
            
            PagingParameter paging = PagingParameter.pageAccess(10);
            
            PagedResult<IndicatorInstance> indicatorsInstances = indicatorsSystemService.findIndicatorsInstancesInPublishedIndicatorsSystems(getServiceContextAdministrador(), conditions, paging);
            
            assertEquals(2,indicatorsInstances.getValues().size());
            
            assertEquals(INDICATORS_SYSTEM_3_IINSTANCE_1A,indicatorsInstances.getValues().get(0).getUuid());
            assertEquals(INDICATORS_SYSTEM_3_IINSTANCE_2,indicatorsInstances.getValues().get(1).getUuid());
        }
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(IndicatorInstance.class).distinctRoot()
                .withProperty(IndicatorInstanceProperties.lastValuesCache().geographicalValue().code()).eq("ES")
                .withProperty(IndicatorInstanceProperties.elementLevel().indicatorsSystemVersion().indicatorsSystem().uuid()).eq(INDICATORS_SYSTEM_10)
                .orderBy(IndicatorInstanceProperties.uuid()).ascending()
                .build();
            
            PagingParameter paging = PagingParameter.pageAccess(10);
            
            PagedResult<IndicatorInstance> indicatorsInstances = indicatorsSystemService.findIndicatorsInstancesInPublishedIndicatorsSystems(getServiceContextAdministrador(), conditions, paging);
            
            assertEquals(2,indicatorsInstances.getValues().size());
            
            assertEquals(INDICATORS_SYSTEM_10_IINSTANCE_2,indicatorsInstances.getValues().get(0).getUuid());
            assertEquals(INDICATORS_SYSTEM_10_IINSTANCE_3,indicatorsInstances.getValues().get(1).getUuid());
        }
    } 
    
    @Test
    public void testFindIndicatorsInstancesInLastVersionIndicatorsSystemsFilteredBySystem() throws Exception {
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(IndicatorInstance.class).distinctRoot()
            .withProperty(IndicatorInstanceProperties.elementLevel().indicatorsSystemVersion().indicatorsSystem().uuid()).eq(INDICATORS_SYSTEM_1)
            .orderBy(IndicatorInstanceProperties.uuid()).ascending()
            .build();
            
            PagingParameter paging = PagingParameter.pageAccess(10);
            
            PagedResult<IndicatorInstance> indicatorsInstances = indicatorsSystemService.findIndicatorsInstancesInLastVersionIndicatorsSystems(getServiceContextAdministrador(), conditions, paging);
            
            assertEquals(3,indicatorsInstances.getValues().size());
            
            assertEquals(INDICATORS_SYSTEM_1_V2_IINSTANCE_1,indicatorsInstances.getValues().get(0).getUuid());
            assertEquals(INDICATORS_SYSTEM_1_V2_IINSTANCE_2,indicatorsInstances.getValues().get(1).getUuid());
            assertEquals(INDICATORS_SYSTEM_1_V2_IINSTANCE_3,indicatorsInstances.getValues().get(2).getUuid());
        }
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(IndicatorInstance.class).distinctRoot()
            .withProperty(IndicatorInstanceProperties.elementLevel().indicatorsSystemVersion().indicatorsSystem().uuid()).eq(INDICATORS_SYSTEM_3)
            .orderBy(IndicatorInstanceProperties.uuid()).ascending()
            .build();
            
            PagingParameter paging = PagingParameter.pageAccess(10);
            
            PagedResult<IndicatorInstance> indicatorsInstances = indicatorsSystemService.findIndicatorsInstancesInLastVersionIndicatorsSystems(getServiceContextAdministrador(), conditions, paging);
            
            assertEquals(2,indicatorsInstances.getValues().size());
            
            assertEquals(INDICATORS_SYSTEM_3_IINSTANCE_1A,indicatorsInstances.getValues().get(0).getUuid());
            assertEquals(INDICATORS_SYSTEM_3_IINSTANCE_2,indicatorsInstances.getValues().get(1).getUuid());
        }
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(IndicatorInstance.class).distinctRoot()
            .withProperty(IndicatorInstanceProperties.elementLevel().indicatorsSystemVersion().indicatorsSystem().uuid()).eq(INDICATORS_SYSTEM_10)
            .orderBy(IndicatorInstanceProperties.uuid()).ascending()
            .build();
            
            PagingParameter paging = PagingParameter.pageAccess(10);
            
            PagedResult<IndicatorInstance> indicatorsInstances = indicatorsSystemService.findIndicatorsInstancesInLastVersionIndicatorsSystems(getServiceContextAdministrador(), conditions, paging);
            
            assertEquals(1,indicatorsInstances.getValues().size());
            
            assertEquals(INDICATORS_SYSTEM_10_V2_IINSTANCE_1,indicatorsInstances.getValues().get(0).getUuid());
        }
    }       
    
    @Test
    public void testFindIndicatorsSystemHistory() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR_1_DS_GPE_UUID))).thenReturn(INDICATOR_1_GPE_JSON_DATA);
        
        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR_1);
        
        String uuid = INDICATORS_SYSTEM_6;
        {
            //Check not empty history
            List<IndicatorsSystemHistory> history = indicatorsSystemService.findIndicatorsSystemHistory(getServiceContextAdministrador(), uuid, Integer.MAX_VALUE);
            assertNotNull(history);
            assertEquals(3,history.size());
            
            assertEquals(uuid,history.get(0).getIndicatorsSystem().getUuid());
            assertEquals("1.500",history.get(0).getVersionNumber());
            assertEquals(uuid,history.get(1).getIndicatorsSystem().getUuid());
            assertEquals("1.300",history.get(1).getVersionNumber());
            assertEquals(uuid,history.get(2).getIndicatorsSystem().getUuid());
            assertEquals("1.000",history.get(2).getVersionNumber());
        }
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/IndicatorsServiceFacadeIndicatorsSystemsTest.xml";
    }
}
