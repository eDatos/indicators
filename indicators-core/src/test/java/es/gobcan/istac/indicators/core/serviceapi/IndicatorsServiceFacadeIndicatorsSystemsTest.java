package es.gobcan.istac.indicators.core.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.siemac.metamac.common.test.utils.DirtyDatabase;
import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaConjunctionRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaDisjunctionRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPaginator;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction.OperationType;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.criteria.shared.MetamacCriteriaOrder;
import org.siemac.metamac.core.common.criteria.shared.MetamacCriteriaOrder.OrderTypeEnum;
import org.siemac.metamac.core.common.enume.domain.IstacTimeGranularityEnum;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.gobcan.istac.indicators.core.criteria.GeographicalValueCriteriaOrderEnum;
import es.gobcan.istac.indicators.core.criteria.GeographicalValueCriteriaPropertyEnum;
import es.gobcan.istac.indicators.core.criteria.IndicatorCriteriaPropertyEnum;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemHistory;
import es.gobcan.istac.indicators.core.dto.DimensionDto;
import es.gobcan.istac.indicators.core.dto.ElementLevelDto;
import es.gobcan.istac.indicators.core.dto.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.dto.GeographicalValueBaseDto;
import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;
import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.IndicatorInstanceDto;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemStructureDto;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemSummaryDto;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorsSystemProcStatusEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionParameters;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.serviceapi.utils.IndicatorsAsserts;
import es.gobcan.istac.indicators.core.serviceapi.utils.IndicatorsMocks;
import es.gobcan.istac.indicators.core.serviceimpl.util.GpeTimeUtils;
import es.gobcan.istac.indicators.core.serviceimpl.util.TimeVariableUtils;

/**
 * Test to IndicatorsServiceFacade. Testing: indicators systems, dimensions, indicators instances
 * Spring based transactional test with DbUnit support.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/include/indicators-data-service-mockito.xml", "classpath:spring/applicationContext-test.xml"})
@TransactionConfiguration(defaultRollback = true, transactionManager = "txManager")
public class IndicatorsServiceFacadeIndicatorsSystemsTest extends IndicatorsBaseTest {

    @Autowired
    protected IndicatorsServiceFacade     indicatorsServiceFacade;

    @Autowired
    protected IndicatorsSystemsService    indicatorsSystemService;

    @Autowired
    private IndicatorsDataService         indicatorsDataService;

    @Autowired
    private IndicatorsDataProviderService indicatorsDataProviderService;

    @Test
    @Transactional
    public void testRetrieveIndicatorsSystemByCode() throws Exception {

        String code = "CODE-1";
        String versionNumber = IndicatorsDataBaseTest.INIT_VERSION;
        IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, versionNumber);

        assertNotNull(indicatorsSystemDto);
        assertEquals(INDICATORS_SYSTEM_1, indicatorsSystemDto.getUuid());
        assertEquals(IndicatorsDataBaseTest.INIT_VERSION, indicatorsSystemDto.getVersionNumber());
        assertEquals(IndicatorsDataBaseTest.SECOND_VERSION, indicatorsSystemDto.getProductionVersion());
        assertEquals(IndicatorsDataBaseTest.INIT_VERSION, indicatorsSystemDto.getPublishedVersion());
        assertEquals(null, indicatorsSystemDto.getArchivedVersion());
        assertEquals("CODE-1", indicatorsSystemDto.getCode());
    }

    @Test
    @Transactional
    public void testRetrieveIndicatorsSystemByCodeLastVersion() throws Exception {

        String code = "CODE-1";
        String versionNumber = null;
        IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, versionNumber);

        assertNotNull(indicatorsSystemDto);
        assertEquals(INDICATORS_SYSTEM_1, indicatorsSystemDto.getUuid());
        assertEquals(indicatorsSystemDto.getProductionVersion(), indicatorsSystemDto.getVersionNumber());
        assertEquals(IndicatorsDataBaseTest.SECOND_VERSION, indicatorsSystemDto.getProductionVersion());
        assertEquals(IndicatorsDataBaseTest.INIT_VERSION, indicatorsSystemDto.getPublishedVersion());
        assertEquals(null, indicatorsSystemDto.getArchivedVersion());
        assertEquals("CODE-1", indicatorsSystemDto.getCode());
    }

    @Test
    @Transactional
    public void testRetrieveIndicatorsSystemByCodeErrorNotExists() throws Exception {

        String code = "CODE_NOT_EXISTS";

        try {
            indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, null);
            fail("No exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND_WITH_CODE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(code, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testRetrieveIndicatorsSystemStructure() throws Exception {

        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContextAdministrador(), INDICATORS_SYSTEM_1,
                IndicatorsDataBaseTest.SECOND_VERSION);

        assertEquals(INDICATORS_SYSTEM_1, indicatorsSystemStructureDto.getUuid());
        assertEquals(IndicatorsDataBaseTest.SECOND_VERSION, indicatorsSystemStructureDto.getVersionNumber());

        assertEquals(4, indicatorsSystemStructureDto.getElements().size());

        {
            ElementLevelDto elementLevelDto = indicatorsSystemStructureDto.getElements().get(0);
            assertEquals(INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2, elementLevelDto.getIndicatorInstance().getUuid());
            assertEquals(Long.valueOf(1), elementLevelDto.getOrderInLevel());
            assertEquals(0, elementLevelDto.getSubelements().size());
        }

        {
            ElementLevelDto elementLevelDto = indicatorsSystemStructureDto.getElements().get(1);
            assertEquals(DIMENSION_1_INDICATORS_SYSTEM_1_V2, elementLevelDto.getDimension().getUuid());
            assertEquals(Long.valueOf(2), elementLevelDto.getOrderInLevel());
            assertEquals(2, elementLevelDto.getSubelements().size());

            // Children
            {
                ElementLevelDto elementLevelChildDto = elementLevelDto.getSubelements().get(0);
                assertEquals(DIMENSION_1A_INDICATORS_SYSTEM_1_V2, elementLevelChildDto.getDimension().getUuid());
                assertEquals(Long.valueOf(1), elementLevelChildDto.getOrderInLevel());
                assertEquals(0, elementLevelChildDto.getSubelements().size());
            }
            {
                ElementLevelDto elementLevelChildDto = elementLevelDto.getSubelements().get(1);
                assertEquals(DIMENSION_1B_INDICATORS_SYSTEM_1_V2, elementLevelChildDto.getDimension().getUuid());
                assertEquals(Long.valueOf(2), elementLevelChildDto.getOrderInLevel());
                assertEquals(2, elementLevelChildDto.getSubelements().size());
                {
                    ElementLevelDto elementLevelChildChildDto = elementLevelChildDto.getSubelements().get(0);
                    assertEquals(DIMENSION_1BA_INDICATORS_SYSTEM_1_V2, elementLevelChildChildDto.getDimension().getUuid());
                    assertEquals(Long.valueOf(1), elementLevelChildChildDto.getOrderInLevel());
                    assertEquals(0, elementLevelChildChildDto.getSubelements().size());
                }
                {
                    ElementLevelDto elementLevelChildChildDto = elementLevelChildDto.getSubelements().get(1);
                    assertEquals(INDICATOR_INSTANCE_3_INDICATORS_SYSTEM_1_V2, elementLevelChildChildDto.getIndicatorInstance().getUuid());
                    assertEquals(Long.valueOf(2), elementLevelChildChildDto.getOrderInLevel());
                    assertEquals(0, elementLevelChildChildDto.getSubelements().size());
                }
            }
        }
        {
            ElementLevelDto elementLevelDto = indicatorsSystemStructureDto.getElements().get(2);
            assertEquals(INDICATOR_INSTANCE_2_INDICATORS_SYSTEM_1_V2, elementLevelDto.getIndicatorInstance().getUuid());
            assertEquals(Long.valueOf(3), elementLevelDto.getOrderInLevel());
            assertEquals(0, elementLevelDto.getSubelements().size());
        }
        {
            ElementLevelDto elementLevelDto = indicatorsSystemStructureDto.getElements().get(3);
            assertEquals(DIMENSION_2_INDICATORS_SYSTEM_1_V2, elementLevelDto.getDimension().getUuid());
            assertEquals(Long.valueOf(4), elementLevelDto.getOrderInLevel());
            assertEquals(0, elementLevelDto.getSubelements().size());
        }
    }

    @Test
    @Transactional
    public void testCreateIndicatorsSystem() throws Exception {

        IndicatorsSystemDto indicatorsSystemDto = new IndicatorsSystemDto();
        indicatorsSystemDto.setCode(IndicatorsMocks.mockString(10));

        // Create
        IndicatorsSystemDto indicatorsSystemDtoCreated = indicatorsServiceFacade.createIndicatorsSystem(getServiceContextAdministrador(), indicatorsSystemDto);

        // Validate
        assertNotNull(indicatorsSystemDtoCreated);
        assertNotNull(indicatorsSystemDtoCreated.getUuid());
        assertNotNull(indicatorsSystemDtoCreated.getVersionNumber());
        IndicatorsSystemDto indicatorsSystemDtoRetrieved = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), indicatorsSystemDtoCreated.getCode(),
                indicatorsSystemDtoCreated.getVersionNumber());
        assertEquals(IndicatorsSystemProcStatusEnum.DRAFT, indicatorsSystemDtoCreated.getProcStatus());
        assertEquals(IndicatorsSystemProcStatusEnum.DRAFT, indicatorsSystemDtoRetrieved.getProcStatus());
        assertEquals(IndicatorsDataBaseTest.INIT_VERSION, indicatorsSystemDtoRetrieved.getProductionVersion());
        assertNull(indicatorsSystemDtoRetrieved.getPublishedVersion());
        assertNull(indicatorsSystemDtoRetrieved.getArchivedVersion());
        assertNull(indicatorsSystemDtoRetrieved.getProductionValidationDate());
        assertNull(indicatorsSystemDtoRetrieved.getProductionValidationUser());
        assertNull(indicatorsSystemDtoRetrieved.getDiffusionValidationDate());
        assertNull(indicatorsSystemDtoRetrieved.getDiffusionValidationUser());
        assertNull(indicatorsSystemDtoRetrieved.getPublicationDate());
        assertNull(indicatorsSystemDtoRetrieved.getPublicationUser());
        assertNull(indicatorsSystemDtoRetrieved.getArchiveDate());
        assertNull(indicatorsSystemDtoRetrieved.getArchiveUser());

        IndicatorsAsserts.assertEqualsIndicatorsSystem(indicatorsSystemDto, indicatorsSystemDtoRetrieved);

        // Validate audit
        assertEquals(getServiceContextAdministrador().getUserId(), indicatorsSystemDtoCreated.getCreatedBy());
        assertTrue(DateUtils.isSameDay(new Date(), indicatorsSystemDtoCreated.getCreatedDate()));
        assertTrue(DateUtils.isSameDay(new Date(), indicatorsSystemDtoCreated.getLastUpdated()));
        assertEquals(getServiceContextAdministrador().getUserId(), indicatorsSystemDtoCreated.getLastUpdatedBy());
    }

    @Test
    @Transactional
    public void testCreateIndicatorsSystemErrorOperationNotAllowed() throws Exception {

        ServiceContext serviceContext = getServiceContextTecnicoProduccion();

        IndicatorsSystemDto indicatorsSystemDto = new IndicatorsSystemDto();
        indicatorsSystemDto.setCode(IndicatorsMocks.mockString(10));

        try {
            indicatorsServiceFacade.createIndicatorsSystem(serviceContext, indicatorsSystemDto);
            fail("operation not allowed");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());

            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(serviceContext.getUserId(), e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testCreateIndicatorsSystemParametersRequired() throws Exception {

        IndicatorsSystemDto indicatorsSystemDto = new IndicatorsSystemDto();
        indicatorsSystemDto.setCode(null);

        try {
            indicatorsServiceFacade.createIndicatorsSystem(getServiceContextAdministrador(), indicatorsSystemDto);
            fail("parameters required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.INDICATORS_SYSTEM_CODE, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testCreateIndicatorsSystemCodeDuplicated() throws Exception {

        IndicatorsSystemDto indicatorsSystemDto = new IndicatorsSystemDto();
        indicatorsSystemDto.setCode("CoDe-1");

        try {
            indicatorsServiceFacade.createIndicatorsSystem(getServiceContextAdministrador(), indicatorsSystemDto);
            fail("code duplicated");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_ALREADY_EXIST_CODE_DUPLICATED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(indicatorsSystemDto.getCode(), e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testCreateIndicatorsSystemCodeDuplicatedInsensitive() throws Exception {

        IndicatorsSystemDto indicatorsSystemDto = new IndicatorsSystemDto();
        indicatorsSystemDto.setCode("CoDe-1");

        try {
            indicatorsServiceFacade.createIndicatorsSystem(getServiceContextAdministrador(), indicatorsSystemDto);
            fail("code duplicated");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_ALREADY_EXIST_CODE_DUPLICATED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(indicatorsSystemDto.getCode(), e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testDeleteIndicatorsSystem() throws Exception {

        String uuid = INDICATORS_SYSTEM_2;
        String code = INDICATORS_SYSTEM_2_CODE;

        // Delete indicators system only in draft
        indicatorsServiceFacade.deleteIndicatorsSystem(getServiceContextAdministrador(), uuid);

        // Validation
        try {
            indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, null);
            fail("Indicators system deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND_WITH_CODE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(code, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testDeleteIndicatorsSystemWithPublishedAndDraft() throws Exception {

        String uuid = INDICATORS_SYSTEM_1;
        String code = INDICATORS_SYSTEM_1_CODE;

        // Retrieve: version 1 is diffusion; version 2 is in production
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, IndicatorsDataBaseTest.INIT_VERSION);
            assertEquals(uuid, indicatorsSystemDto.getUuid());
            assertEquals(IndicatorsDataBaseTest.INIT_VERSION, indicatorsSystemDto.getVersionNumber());
            assertEquals(IndicatorsDataBaseTest.SECOND_VERSION, indicatorsSystemDto.getProductionVersion());
            assertEquals(IndicatorsDataBaseTest.INIT_VERSION, indicatorsSystemDto.getPublishedVersion());
            assertEquals(null, indicatorsSystemDto.getArchivedVersion());
        }

        // Retrieve dimensions to check will be deleted
        List<String> dimensionsUuid = new ArrayList<String>();
        dimensionsUuid.add(DIMENSION_1_INDICATORS_SYSTEM_1_V2);
        dimensionsUuid.add(DIMENSION_1A_INDICATORS_SYSTEM_1_V2);
        dimensionsUuid.add(DIMENSION_1B_INDICATORS_SYSTEM_1_V2);
        dimensionsUuid.add(DIMENSION_1BA_INDICATORS_SYSTEM_1_V2);
        dimensionsUuid.add(DIMENSION_2_INDICATORS_SYSTEM_1_V2);
        for (String dimensionUuid : dimensionsUuid) {
            DimensionDto dimensionDto = indicatorsServiceFacade.retrieveDimension(getServiceContextAdministrador(), dimensionUuid);
            assertNotNull(dimensionDto);
        }
        // Retrieve indicators instances to check will be deleted
        List<String> indicatorsInstancesUuid = new ArrayList<String>();
        indicatorsInstancesUuid.add(INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2);
        indicatorsInstancesUuid.add(INDICATOR_INSTANCE_2_INDICATORS_SYSTEM_1_V2);
        indicatorsInstancesUuid.add(INDICATOR_INSTANCE_3_INDICATORS_SYSTEM_1_V2);
        for (String indicatorInstanceUuid : indicatorsInstancesUuid) {
            IndicatorInstanceDto indicatorInstanceDto = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContextAdministrador(), indicatorInstanceUuid);
            assertNotNull(indicatorInstanceDto);
        }

        // Delete indicators system
        indicatorsServiceFacade.deleteIndicatorsSystem(getServiceContextAdministrador(), uuid);

        // Validation
        // Version 1 exists
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, IndicatorsDataBaseTest.INIT_VERSION);
            assertEquals(uuid, indicatorsSystemDto.getUuid());
            assertEquals(IndicatorsDataBaseTest.INIT_VERSION, indicatorsSystemDto.getVersionNumber());
            assertEquals(null, indicatorsSystemDto.getProductionVersion());
            assertEquals(IndicatorsDataBaseTest.INIT_VERSION, indicatorsSystemDto.getPublishedVersion());
            assertEquals(null, indicatorsSystemDto.getArchivedVersion());
        }
        // Version 2 not exists
        try {
            indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, IndicatorsDataBaseTest.SECOND_VERSION);
            fail("Indicators system version deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND_WITH_CODE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(code, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }

        // Check dimensions deleted
        for (String dimensionUuid : dimensionsUuid) {
            try {
                indicatorsServiceFacade.retrieveDimension(getServiceContextAdministrador(), dimensionUuid);
                fail("dimension deleted");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.DIMENSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
                assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
                assertEquals(dimensionUuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            }
        }
        // Check indicators instances deleted
        for (String indicatorInstanceUuid : indicatorsInstancesUuid) {
            try {
                indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContextAdministrador(), indicatorInstanceUuid);
                fail("indicators instances deleted");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.INDICATOR_INSTANCE_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
                assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
                assertEquals(indicatorInstanceUuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            }
        }
    }

    @Test
    @Transactional
    public void testDeleteIndicatorsSystemErrorInDiffusion() throws Exception {

        String uuid = INDICATORS_SYSTEM_3;

        // Validation
        try {
            indicatorsServiceFacade.deleteIndicatorsSystem(getServiceContextAdministrador(), uuid);
            fail("Indicators system is not in draft");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_IN_PRODUCTION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testDeleteIndicatorsSystemErrorNotExists() throws Exception {

        String uuid = NOT_EXISTS;

        // Validation
        try {
            indicatorsServiceFacade.deleteIndicatorsSystem(getServiceContextAdministrador(), uuid);
            fail("Indicator system not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testSendIndicatorsSystemToProductionValidation() throws Exception {

        String uuid = INDICATORS_SYSTEM_1;
        String code = INDICATORS_SYSTEM_1_CODE;
        String diffusionVersion = IndicatorsDataBaseTest.INIT_VERSION;
        String productionVersion = IndicatorsDataBaseTest.SECOND_VERSION;

        {
            IndicatorsSystemDto indicatorsSystemDtoV1 = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, diffusionVersion);
            IndicatorsSystemDto indicatorsSystemDtoV2 = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, productionVersion);
            assertEquals(diffusionVersion, indicatorsSystemDtoV1.getPublishedVersion());
            assertEquals(productionVersion, indicatorsSystemDtoV2.getProductionVersion());
            assertEquals(null, indicatorsSystemDtoV1.getArchivedVersion());
            assertEquals(null, indicatorsSystemDtoV2.getArchivedVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemDtoV1.getProcStatus());
            assertEquals(IndicatorsSystemProcStatusEnum.DRAFT, indicatorsSystemDtoV2.getProcStatus());
        }

        // Sends to production validation
        IndicatorsSystemDto indicatorsSystemDtoV2Updated = indicatorsServiceFacade.sendIndicatorsSystemToProductionValidation(getServiceContextAdministrador2(), uuid);

        // Validation
        {
            assertEquals(diffusionVersion, indicatorsSystemDtoV2Updated.getPublishedVersion());
            assertEquals(null, indicatorsSystemDtoV2Updated.getArchivedVersion());
            assertEquals(productionVersion, indicatorsSystemDtoV2Updated.getProductionVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.PRODUCTION_VALIDATION, indicatorsSystemDtoV2Updated.getProcStatus());
            assertTrue(DateUtils.isSameDay(new Date(), indicatorsSystemDtoV2Updated.getProductionValidationDate()));
            assertEquals(getServiceContextAdministrador2().getUserId(), indicatorsSystemDtoV2Updated.getProductionValidationUser());
        }
        {
            IndicatorsSystemDto indicatorsSystemDtoV1 = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, diffusionVersion);
            IndicatorsSystemDto indicatorsSystemDtoV2 = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, productionVersion);
            assertEquals(productionVersion, indicatorsSystemDtoV2.getProductionVersion());
            assertEquals(diffusionVersion, indicatorsSystemDtoV1.getPublishedVersion());
            assertEquals(null, indicatorsSystemDtoV1.getArchivedVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemDtoV1.getProcStatus());
            assertEquals(IndicatorsSystemProcStatusEnum.PRODUCTION_VALIDATION, indicatorsSystemDtoV2.getProcStatus());

            assertTrue(DateUtils.isSameDay(new Date(), indicatorsSystemDtoV2.getProductionValidationDate()));
            assertEquals(getServiceContextAdministrador2().getUserId(), indicatorsSystemDtoV2.getProductionValidationUser());
            assertNull(indicatorsSystemDtoV2.getDiffusionValidationDate());
            assertNull(indicatorsSystemDtoV2.getDiffusionValidationUser());
            assertNull(indicatorsSystemDtoV2.getPublicationDate());
            assertNull(indicatorsSystemDtoV2.getPublicationUser());
            assertNull(indicatorsSystemDtoV2.getArchiveDate());
            assertNull(indicatorsSystemDtoV2.getArchiveUser());
        }
    }

    @Test
    @Transactional
    public void testSendIndicatorsSystemToProductionValidationInProcStatusRejected() throws Exception {

        String uuid = INDICATORS_SYSTEM_9;
        String code = INDICATORS_SYSTEM_9_CODE;
        String productionVersion = IndicatorsDataBaseTest.INIT_VERSION;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, productionVersion);
            assertEquals(productionVersion, indicatorsSystemDto.getProductionVersion());
            assertNull(indicatorsSystemDto.getPublishedVersion());
            assertNull(indicatorsSystemDto.getArchivedVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.VALIDATION_REJECTED, indicatorsSystemDto.getProcStatus());
        }

        // Sends to production validation
        indicatorsServiceFacade.sendIndicatorsSystemToProductionValidation(getServiceContextAdministrador2(), uuid);

        // Validation
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, productionVersion);
            assertEquals(productionVersion, indicatorsSystemDto.getProductionVersion());
            assertNull(indicatorsSystemDto.getPublishedVersion());
            assertNull(indicatorsSystemDto.getArchivedVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.PRODUCTION_VALIDATION, indicatorsSystemDto.getProcStatus());
        }
    }

    @Test
    @Transactional
    public void testSendIndicatorsSystemToProductionValidationErrorWithoutIndicatorInstance() throws Exception {
        try {
            indicatorsServiceFacade.sendIndicatorsSystemToProductionValidation(getServiceContextAdministrador(), INDICATORS_SYSTEM_2);
            fail("Indicators system has not any indicator instance");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_MUST_HAVE_INDICATOR_INSTANCE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(INDICATORS_SYSTEM_2, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testSendIndicatorsSystemToProductionValidationErrorNotExists() throws Exception {

        try {
            indicatorsServiceFacade.sendIndicatorsSystemToProductionValidation(getServiceContextAdministrador(), NOT_EXISTS);
            fail("Indicators system not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testSendIndicatorsSystemToProductionValidationErrorWrongProcStatus() throws Exception {

        String uuid = INDICATORS_SYSTEM_3;
        String code = INDICATORS_SYSTEM_3_CODE;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, INDICATORS_SYSTEM_3_VERSION);
            assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemDto.getProcStatus());
            assertNull(indicatorsSystemDto.getProductionVersion());
        }

        try {
            indicatorsServiceFacade.sendIndicatorsSystemToProductionValidation(getServiceContextAdministrador(), uuid);
            fail("Indicators system is not draft");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_DRAFT, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_VALIDATION_REJECTED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);

        }
    }

    @Test
    @Transactional
    public void testSendIndicatorsSystemToDiffusionValidation() throws Exception {

        String uuid = INDICATORS_SYSTEM_4;
        String code = INDICATORS_SYSTEM_4_CODE;
        String versionNumber = IndicatorsDataBaseTest.INIT_VERSION;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, versionNumber);
            assertNull(indicatorsSystemDto.getPublishedVersion());
            assertNull(indicatorsSystemDto.getArchivedVersion());
            assertEquals(IndicatorsDataBaseTest.INIT_VERSION, indicatorsSystemDto.getProductionVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.PRODUCTION_VALIDATION, indicatorsSystemDto.getProcStatus());
        }

        // Sends to diffusion validation
        IndicatorsSystemDto indicatorsSystemDtoV1Updated = indicatorsServiceFacade.sendIndicatorsSystemToDiffusionValidation(getServiceContextAdministrador(), uuid);

        // Validation
        {
            assertEquals(IndicatorsSystemProcStatusEnum.DIFFUSION_VALIDATION, indicatorsSystemDtoV1Updated.getProcStatus());
            IndicatorsAsserts.assertEqualsDate("2011-04-04 01:02:04", indicatorsSystemDtoV1Updated.getProductionValidationDate());
            assertEquals("user1", indicatorsSystemDtoV1Updated.getProductionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), indicatorsSystemDtoV1Updated.getDiffusionValidationDate()));
            assertEquals(getServiceContextAdministrador().getUserId(), indicatorsSystemDtoV1Updated.getDiffusionValidationUser());

        }
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, versionNumber);
            assertNull(indicatorsSystemDto.getPublishedVersion());
            assertNull(indicatorsSystemDto.getArchivedVersion());
            assertEquals(IndicatorsDataBaseTest.INIT_VERSION, indicatorsSystemDto.getProductionVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.DIFFUSION_VALIDATION, indicatorsSystemDto.getProcStatus());

            IndicatorsAsserts.assertEqualsDate("2011-04-04 01:02:04", indicatorsSystemDto.getProductionValidationDate());
            assertEquals("user1", indicatorsSystemDto.getProductionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), indicatorsSystemDto.getDiffusionValidationDate()));
            assertEquals(getServiceContextAdministrador().getUserId(), indicatorsSystemDto.getDiffusionValidationUser());
            assertNull(indicatorsSystemDto.getPublicationDate());
            assertNull(indicatorsSystemDto.getPublicationUser());
            assertNull(indicatorsSystemDto.getArchiveDate());
            assertNull(indicatorsSystemDto.getArchiveUser());
        }
    }

    @Test
    @Transactional
    public void testSendIndicatorsSystemToDiffusionValidationErrorNotExists() throws Exception {

        try {
            indicatorsServiceFacade.sendIndicatorsSystemToDiffusionValidation(getServiceContextAdministrador(), NOT_EXISTS);
            fail("Indicators system not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testSendIndicatorsSystemToDiffusionValidationErrorWrongProcStatusDraft() throws Exception {

        String uuid = INDICATORS_SYSTEM_2;
        String code = INDICATORS_SYSTEM_2_CODE;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, IndicatorsDataBaseTest.INIT_VERSION);
            assertEquals(IndicatorsSystemProcStatusEnum.DRAFT, indicatorsSystemDto.getProcStatus());
            assertEquals(IndicatorsDataBaseTest.INIT_VERSION, indicatorsSystemDto.getProductionVersion());
            assertNull(indicatorsSystemDto.getPublishedVersion());
            assertNull(indicatorsSystemDto.getArchivedVersion());
        }

        try {
            indicatorsServiceFacade.sendIndicatorsSystemToDiffusionValidation(getServiceContextAdministrador(), uuid);
            fail("Indicators system is not draft");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_PRODUCTION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);

        }
    }

    @Test
    @Transactional
    public void testSendIndicatorsSystemToDiffusionValidationErrorWrongProcStatusPublished() throws Exception {

        String uuid = INDICATORS_SYSTEM_3;
        String code = INDICATORS_SYSTEM_3_CODE;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, INDICATORS_SYSTEM_3_VERSION);
            assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemDto.getProcStatus());
            assertEquals(null, indicatorsSystemDto.getProductionVersion());
            assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDto.getPublishedVersion());
            assertNull(indicatorsSystemDto.getArchivedVersion());
        }

        try {
            indicatorsServiceFacade.sendIndicatorsSystemToDiffusionValidation(getServiceContextAdministrador(), uuid);
            fail("Indicators system is not production validation");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_PRODUCTION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);

        }
    }

    @Test
    @Transactional
    public void testRejectIndicatorsSystemProductionValidation() throws Exception {

        String uuid = INDICATORS_SYSTEM_4;
        String code = INDICATORS_SYSTEM_4_CODE;
        String versionNumber = IndicatorsDataBaseTest.INIT_VERSION;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, versionNumber);
            assertNull(indicatorsSystemDto.getPublishedVersion());
            assertNull(indicatorsSystemDto.getArchivedVersion());
            assertEquals(IndicatorsDataBaseTest.INIT_VERSION, indicatorsSystemDto.getProductionVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.PRODUCTION_VALIDATION, indicatorsSystemDto.getProcStatus());
        }

        // Rejects validation
        IndicatorsSystemDto indicatorsSystemDtoV1Updated = indicatorsServiceFacade.rejectIndicatorsSystemProductionValidation(getServiceContextAdministrador(), uuid);

        // Validation
        {
            assertEquals(IndicatorsSystemProcStatusEnum.VALIDATION_REJECTED, indicatorsSystemDtoV1Updated.getProcStatus());

            assertNull(indicatorsSystemDtoV1Updated.getProductionValidationDate());
            assertNull(indicatorsSystemDtoV1Updated.getProductionValidationUser());
            assertNull(indicatorsSystemDtoV1Updated.getDiffusionValidationDate());
            assertNull(indicatorsSystemDtoV1Updated.getDiffusionValidationUser());
            assertNull(indicatorsSystemDtoV1Updated.getPublicationDate());
            assertNull(indicatorsSystemDtoV1Updated.getPublicationUser());
            assertNull(indicatorsSystemDtoV1Updated.getArchiveDate());
            assertNull(indicatorsSystemDtoV1Updated.getArchiveUser());
        }
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, versionNumber);
            assertNull(indicatorsSystemDto.getPublishedVersion());
            assertNull(indicatorsSystemDto.getArchivedVersion());
            assertEquals(IndicatorsDataBaseTest.INIT_VERSION, indicatorsSystemDto.getProductionVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.VALIDATION_REJECTED, indicatorsSystemDto.getProcStatus());

            assertNull(indicatorsSystemDto.getProductionValidationDate());
            assertNull(indicatorsSystemDto.getProductionValidationUser());
            assertNull(indicatorsSystemDto.getDiffusionValidationDate());
            assertNull(indicatorsSystemDto.getDiffusionValidationUser());
            assertNull(indicatorsSystemDto.getPublicationDate());
            assertNull(indicatorsSystemDto.getPublicationUser());
            assertNull(indicatorsSystemDto.getArchiveDate());
            assertNull(indicatorsSystemDto.getArchiveUser());
        }
    }

    @Test
    @Transactional
    public void testRejectIndicatorsSystemProductionValidationErrorNotExists() throws Exception {

        try {
            indicatorsServiceFacade.rejectIndicatorsSystemProductionValidation(getServiceContextAdministrador(), NOT_EXISTS);
            fail("Indicators system not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testRejectIndicatorsSystemProductionValidationErrorWrongProcStatus() throws Exception {

        String uuid = INDICATORS_SYSTEM_2;
        String code = INDICATORS_SYSTEM_2_CODE;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, IndicatorsDataBaseTest.INIT_VERSION);
            assertEquals(IndicatorsSystemProcStatusEnum.DRAFT, indicatorsSystemDto.getProcStatus());
            assertEquals(IndicatorsDataBaseTest.INIT_VERSION, indicatorsSystemDto.getProductionVersion());
            assertNull(indicatorsSystemDto.getPublishedVersion());
            assertNull(indicatorsSystemDto.getArchivedVersion());
        }

        try {
            indicatorsServiceFacade.rejectIndicatorsSystemProductionValidation(getServiceContextAdministrador(), uuid);
            fail("Indicators system is not in validation");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_PRODUCTION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Test
    @Transactional
    public void testRejectIndicatorsSystemDiffusionValidation() throws Exception {

        String uuid = INDICATORS_SYSTEM_5;
        String code = INDICATORS_SYSTEM_5_CODE;
        String versionNumber = IndicatorsDataBaseTest.INIT_VERSION;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, versionNumber);
            assertNull(indicatorsSystemDto.getPublishedVersion());
            assertNull(indicatorsSystemDto.getArchivedVersion());
            assertEquals(IndicatorsDataBaseTest.INIT_VERSION, indicatorsSystemDto.getProductionVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.DIFFUSION_VALIDATION, indicatorsSystemDto.getProcStatus());
        }

        // Rejects validation
        indicatorsServiceFacade.rejectIndicatorsSystemDiffusionValidation(getServiceContextAdministrador(), uuid);

        // Validation
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, versionNumber);
            assertNull(indicatorsSystemDto.getPublishedVersion());
            assertNull(indicatorsSystemDto.getArchivedVersion());
            assertEquals(IndicatorsDataBaseTest.INIT_VERSION, indicatorsSystemDto.getProductionVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.VALIDATION_REJECTED, indicatorsSystemDto.getProcStatus());
        }
    }

    @Test
    @Transactional
    public void testRejectIndicatorsSystemDiffusionValidationErrorNotExists() throws Exception {

        try {
            indicatorsServiceFacade.rejectIndicatorsSystemDiffusionValidation(getServiceContextAdministrador(), NOT_EXISTS);
            fail("Indicators system not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testRejectIndicatorsSystemDiffusionValidationErrorWrongProcStatus() throws Exception {

        String uuid = INDICATORS_SYSTEM_2;
        String code = INDICATORS_SYSTEM_2_CODE;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, IndicatorsDataBaseTest.INIT_VERSION);
            assertEquals(IndicatorsSystemProcStatusEnum.DRAFT, indicatorsSystemDto.getProcStatus());
            assertEquals(IndicatorsDataBaseTest.INIT_VERSION, indicatorsSystemDto.getProductionVersion());
            assertNull(indicatorsSystemDto.getPublishedVersion());
            assertNull(indicatorsSystemDto.getArchivedVersion());
        }

        try {
            indicatorsServiceFacade.rejectIndicatorsSystemDiffusionValidation(getServiceContextAdministrador(), uuid);
            fail("Indicators system is not in validation");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_DIFFUSION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Test
    @Transactional
    public void testRejectIndicatorsSystemDiffusionValidationErrorWrongProcStatusDiffusion() throws Exception {

        String uuid = INDICATORS_SYSTEM_3;
        String code = INDICATORS_SYSTEM_3_CODE;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, INDICATORS_SYSTEM_3_VERSION);
            assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemDto.getProcStatus());
            assertEquals(null, indicatorsSystemDto.getProductionVersion());
            assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDto.getPublishedVersion());
            assertNull(indicatorsSystemDto.getArchivedVersion());
        }

        try {
            indicatorsServiceFacade.rejectIndicatorsSystemDiffusionValidation(getServiceContextAdministrador(), uuid);
            fail("Indicators system is not in validation");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_DIFFUSION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Test
    @Transactional
    public void testPublishIndicatorsSystem() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR_1_DS_GPE_UUID))).thenReturn(INDICATOR_1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR_1);

        String uuid = INDICATORS_SYSTEM_5;
        String code = INDICATORS_SYSTEM_5_CODE;
        String versionNumber = IndicatorsDataBaseTest.INIT_VERSION;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, versionNumber);
            assertEquals(versionNumber, indicatorsSystemDto.getProductionVersion());
            assertNull(indicatorsSystemDto.getPublishedVersion());
            assertNull(indicatorsSystemDto.getArchivedVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.DIFFUSION_VALIDATION, indicatorsSystemDto.getProcStatus());

            // Check empty history
            List<IndicatorsSystemHistory> history = indicatorsSystemService.findIndicatorsSystemHistory(getServiceContextAdministrador(), uuid, Integer.MAX_VALUE);
            assertNotNull(history);
            assertEquals(0, history.size());
        }

        // Publish
        IndicatorsSystemDto indicatorsSystemDto1Updated = indicatorsServiceFacade.publishIndicatorsSystem(getServiceContextAdministrador(), uuid);

        // Validation
        {
            assertEquals(null, indicatorsSystemDto1Updated.getProductionVersion());
            assertEquals(versionNumber, indicatorsSystemDto1Updated.getPublishedVersion());
            assertEquals(null, indicatorsSystemDto1Updated.getArchivedVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemDto1Updated.getProcStatus());
            IndicatorsAsserts.assertEqualsDate("2011-06-06 01:02:04", indicatorsSystemDto1Updated.getProductionValidationDate());
            assertEquals("user1", indicatorsSystemDto1Updated.getProductionValidationUser());
            IndicatorsAsserts.assertEqualsDate("2011-07-07 03:02:04", indicatorsSystemDto1Updated.getDiffusionValidationDate());
            assertEquals("user2", indicatorsSystemDto1Updated.getDiffusionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), indicatorsSystemDto1Updated.getPublicationDate()));
            assertEquals(getServiceContextAdministrador().getUserId(), indicatorsSystemDto1Updated.getPublicationUser());
            assertNull(indicatorsSystemDto1Updated.getArchiveDate());
            assertNull(indicatorsSystemDto1Updated.getArchiveUser());
        }
        {
            // Check history
            List<IndicatorsSystemHistory> history = indicatorsSystemService.findIndicatorsSystemHistory(getServiceContextAdministrador(), uuid, Integer.MAX_VALUE);
            assertNotNull(history);
            assertEquals(1, history.size());
            assertEquals(indicatorsSystemDto1Updated.getPublishedVersion(), history.get(0).getVersionNumber());
        }
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, versionNumber);
            assertEquals(null, indicatorsSystemDto.getProductionVersion());
            assertEquals(versionNumber, indicatorsSystemDto.getPublishedVersion());
            assertEquals(null, indicatorsSystemDto.getArchivedVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemDto.getProcStatus());

            IndicatorsAsserts.assertEqualsDate("2011-06-06 01:02:04", indicatorsSystemDto.getProductionValidationDate());
            assertEquals("user1", indicatorsSystemDto.getProductionValidationUser());
            IndicatorsAsserts.assertEqualsDate("2011-07-07 03:02:04", indicatorsSystemDto.getDiffusionValidationDate());
            assertEquals("user2", indicatorsSystemDto.getDiffusionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), indicatorsSystemDto.getPublicationDate()));
            assertEquals(getServiceContextAdministrador().getUserId(), indicatorsSystemDto.getPublicationUser());
            assertNull(indicatorsSystemDto.getArchiveDate());
            assertNull(indicatorsSystemDto.getArchiveUser());
        }
    }

    @Test
    @Transactional
    public void testPublishIndicatorsSystemExistingHistory() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR_1_DS_GPE_UUID))).thenReturn(INDICATOR_1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR_1);

        String uuid = INDICATORS_SYSTEM_6;
        String code = INDICATORS_SYSTEM_6_CODE;
        String previousPublishedversionNumber = IndicatorsDataBaseTest.INIT_VERSION_ANOTHER_HUGE_INCREMENT;
        String versionNumber = IndicatorsDataBaseTest.SECOND_VERSION;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, versionNumber);
            assertEquals(versionNumber, indicatorsSystemDto.getProductionVersion());
            assertNotNull(indicatorsSystemDto.getPublishedVersion());
            assertNull(indicatorsSystemDto.getArchivedVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.DIFFUSION_VALIDATION, indicatorsSystemDto.getProcStatus());

            // Check not empty history
            List<IndicatorsSystemHistory> history = indicatorsSystemService.findIndicatorsSystemHistory(getServiceContextAdministrador(), uuid, Integer.MAX_VALUE);
            assertNotNull(history);
            assertEquals(3, history.size());

            assertEquals(uuid, history.get(0).getIndicatorsSystem().getUuid());
            assertEquals(previousPublishedversionNumber, history.get(0).getVersionNumber());
        }

        // Publish
        IndicatorsSystemDto indicatorsSystemDto1Updated = indicatorsServiceFacade.publishIndicatorsSystem(getServiceContextAdministrador(), uuid);

        // Validation
        {
            assertEquals(null, indicatorsSystemDto1Updated.getProductionVersion());
            assertEquals(versionNumber, indicatorsSystemDto1Updated.getPublishedVersion());
            assertEquals(null, indicatorsSystemDto1Updated.getArchivedVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemDto1Updated.getProcStatus());
            IndicatorsAsserts.assertEqualsDate("2011-06-06 01:02:04", indicatorsSystemDto1Updated.getProductionValidationDate());
            assertEquals("user1", indicatorsSystemDto1Updated.getProductionValidationUser());
            IndicatorsAsserts.assertEqualsDate("2011-07-07 03:02:04", indicatorsSystemDto1Updated.getDiffusionValidationDate());
            assertEquals("user2", indicatorsSystemDto1Updated.getDiffusionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), indicatorsSystemDto1Updated.getPublicationDate()));
            assertEquals(getServiceContextAdministrador().getUserId(), indicatorsSystemDto1Updated.getPublicationUser());
            assertNull(indicatorsSystemDto1Updated.getArchiveDate());
            assertNull(indicatorsSystemDto1Updated.getArchiveUser());
        }
        {
            // Check history
            List<IndicatorsSystemHistory> history = indicatorsSystemService.findIndicatorsSystemHistory(getServiceContextAdministrador(), uuid, Integer.MAX_VALUE);
            assertNotNull(history);
            assertEquals(4, history.size());

            assertEquals(indicatorsSystemDto1Updated.getUuid(), history.get(0).getIndicatorsSystem().getUuid());
            assertEquals(indicatorsSystemDto1Updated.getPublishedVersion(), history.get(0).getVersionNumber());

            assertEquals(indicatorsSystemDto1Updated.getUuid(), history.get(1).getIndicatorsSystem().getUuid());
            assertEquals(previousPublishedversionNumber, history.get(1).getVersionNumber());
        }
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, versionNumber);
            assertEquals(null, indicatorsSystemDto.getProductionVersion());
            assertEquals(versionNumber, indicatorsSystemDto.getPublishedVersion());
            assertEquals(null, indicatorsSystemDto.getArchivedVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemDto.getProcStatus());

            IndicatorsAsserts.assertEqualsDate("2011-06-06 01:02:04", indicatorsSystemDto.getProductionValidationDate());
            assertEquals("user1", indicatorsSystemDto.getProductionValidationUser());
            IndicatorsAsserts.assertEqualsDate("2011-07-07 03:02:04", indicatorsSystemDto.getDiffusionValidationDate());
            assertEquals("user2", indicatorsSystemDto.getDiffusionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), indicatorsSystemDto.getPublicationDate()));
            assertEquals(getServiceContextAdministrador().getUserId(), indicatorsSystemDto.getPublicationUser());
            assertNull(indicatorsSystemDto.getArchiveDate());
            assertNull(indicatorsSystemDto.getArchiveUser());
        }
    }

    @Test
    @Transactional
    public void testPublishIndicatorsSystemWithPublishedVersion() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR_1_DS_GPE_UUID))).thenReturn(INDICATOR_1_GPE_JSON_DATA);
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR_3_DS_GPE_UUID))).thenReturn(INDICATOR_3_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR_1);
        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR_3);

        String uuid = INDICATORS_SYSTEM_6;
        String code = INDICATORS_SYSTEM_6_CODE;
        String diffusionVersionBefore = IndicatorsDataBaseTest.INIT_VERSION_ANOTHER_HUGE_INCREMENT_WITH_MINOR_INCREMENT; // WAS 1.0, but it was changed to 1.1 after populating linked indicator 1
        String productionVersionBefore = IndicatorsDataBaseTest.SECOND_VERSION; // will be published

        {
            IndicatorsSystemDto indicatorsSystemDtoV1 = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, diffusionVersionBefore);
            IndicatorsSystemDto indicatorsSystemDtoV2 = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, productionVersionBefore);
            assertEquals(productionVersionBefore, indicatorsSystemDtoV1.getProductionVersion());
            assertEquals(diffusionVersionBefore, indicatorsSystemDtoV1.getPublishedVersion());
            assertEquals(null, indicatorsSystemDtoV1.getArchivedVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemDtoV1.getProcStatus());
            assertEquals(IndicatorsSystemProcStatusEnum.DIFFUSION_VALIDATION, indicatorsSystemDtoV2.getProcStatus());
        }

        // Publish
        indicatorsServiceFacade.publishIndicatorsSystem(getServiceContextAdministrador(), uuid);

        // Validation
        {
            // Version 1 already not exists
            try {
                indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, diffusionVersionBefore);
                fail("Indicators system version not exists");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND_WITH_CODE.getCode(), e.getExceptionItems().get(0).getCode());
                assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
                assertEquals(code, e.getExceptionItems().get(0).getMessageParameters()[0]);
            }

            // Actual version in diffusion is version 2
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, productionVersionBefore);
            assertEquals(null, indicatorsSystemDto.getProductionVersion());
            assertEquals(IndicatorsDataBaseTest.SECOND_VERSION, indicatorsSystemDto.getPublishedVersion());
            assertEquals(null, indicatorsSystemDto.getArchivedVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemDto.getProcStatus());
        }
    }

    @Test
    @Transactional
    public void testPublishIndicatorsSystemWithArchivedVersion() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR_1_DS_GPE_UUID))).thenReturn(INDICATOR_1_GPE_JSON_DATA);

        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR_1);

        String uuid = INDICATORS_SYSTEM_7;
        String code = INDICATORS_SYSTEM_7_CODE;
        String archivedVersionBefore = IndicatorsDataBaseTest.INIT_VERSION; // will be deleted when publish current version in diffusion validation
        String productionVersionBefore = IndicatorsDataBaseTest.SECOND_VERSION; // will be published

        {
            IndicatorsSystemDto indicatorsSystemDtoV1 = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, archivedVersionBefore);
            IndicatorsSystemDto indicatorsSystemDtoV2 = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, productionVersionBefore);
            assertEquals(productionVersionBefore, indicatorsSystemDtoV1.getProductionVersion());
            assertEquals(null, indicatorsSystemDtoV1.getPublishedVersion());
            assertEquals(null, indicatorsSystemDtoV1.getPublishedVersion());
            assertEquals(archivedVersionBefore, indicatorsSystemDtoV1.getArchivedVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.ARCHIVED, indicatorsSystemDtoV1.getProcStatus());
            assertEquals(IndicatorsSystemProcStatusEnum.DIFFUSION_VALIDATION, indicatorsSystemDtoV2.getProcStatus());
        }

        // Publish
        indicatorsServiceFacade.publishIndicatorsSystem(getServiceContextAdministrador(), uuid);

        // Validation
        {
            // Version 1 already not exists
            try {
                indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, archivedVersionBefore);
                fail("Indicators system version not exists");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND_WITH_CODE.getCode(), e.getExceptionItems().get(0).getCode());
                assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
                assertEquals(code, e.getExceptionItems().get(0).getMessageParameters()[0]);
            }

            // Actual version in diffusion is version 2
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, productionVersionBefore);
            assertEquals(null, indicatorsSystemDto.getProductionVersion());
            assertEquals(IndicatorsDataBaseTest.SECOND_VERSION, indicatorsSystemDto.getPublishedVersion());
            assertEquals(null, indicatorsSystemDto.getArchivedVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemDto.getProcStatus());
        }
    }

    @Test
    @Transactional
    public void testPublishIndicatorsSystemErrorNotExists() throws Exception {

        try {
            indicatorsServiceFacade.publishIndicatorsSystem(getServiceContextAdministrador(), NOT_EXISTS);
            fail("Indicators system not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testPublishIndicatorsSystemErrorWrongProcStatusProduction() throws Exception {

        String uuid = INDICATORS_SYSTEM_2;
        String code = INDICATORS_SYSTEM_2_CODE;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, IndicatorsDataBaseTest.INIT_VERSION);
            assertEquals(IndicatorsSystemProcStatusEnum.DRAFT, indicatorsSystemDto.getProcStatus());
            assertEquals(IndicatorsDataBaseTest.INIT_VERSION, indicatorsSystemDto.getProductionVersion());
            assertNull(indicatorsSystemDto.getPublishedVersion());
            assertNull(indicatorsSystemDto.getArchivedVersion());
        }

        try {
            indicatorsServiceFacade.publishIndicatorsSystem(getServiceContextAdministrador(), uuid);
            fail("Indicators system is not diffusion validation");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_DIFFUSION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Test
    @Transactional
    public void testPublishIndicatorsSystemErrorWrongProcStatusDiffusion() throws Exception {

        String uuid = INDICATORS_SYSTEM_3;
        String code = INDICATORS_SYSTEM_3_CODE;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, INDICATORS_SYSTEM_3_VERSION);
            assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemDto.getProcStatus());
            assertEquals(null, indicatorsSystemDto.getProductionVersion());
            assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDto.getPublishedVersion());
            assertNull(indicatorsSystemDto.getArchivedVersion());
        }

        try {
            indicatorsServiceFacade.publishIndicatorsSystem(getServiceContextAdministrador(), uuid);
            fail("Indicators system is not diffusion validation");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_DIFFUSION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Test
    @Transactional
    public void testPublishIndicatorsSystemErrorIndicatorNotPublished() throws Exception {

        String uuid = INDICATORS_SYSTEM_5;
        String indicatorUuid2 = INDICATOR_2;
        String indicatorUuid3 = INDICATOR_3;

        GeographicalValueBaseDto geoValue = new GeographicalValueBaseDto();
        geoValue.setUuid(GEOGRAPHICAL_VALUE_1);

        // Create indicators instances with indicator not published
        {

            // Indicator 2
            IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
            indicatorInstanceDto.setIndicatorUuid(indicatorUuid2);
            indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
            indicatorInstanceDto.setParentUuid(null);
            indicatorInstanceDto.setOrderInLevel(Long.valueOf(2));
            indicatorInstanceDto.setGeographicalValues(Arrays.asList(geoValue));
            indicatorInstanceDto.setTimeValues(Arrays.asList("2012"));
            indicatorsServiceFacade.createIndicatorInstance(getServiceContextAdministrador(), uuid, indicatorInstanceDto);
        }
        {
            // Indicator 3
            IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
            indicatorInstanceDto.setIndicatorUuid(indicatorUuid3);
            indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
            indicatorInstanceDto.setParentUuid(null);
            indicatorInstanceDto.setOrderInLevel(Long.valueOf(3));
            indicatorInstanceDto.setGeographicalValues(Arrays.asList(geoValue));
            indicatorInstanceDto.setTimeValues(Arrays.asList("2012"));
            indicatorsServiceFacade.createIndicatorInstance(getServiceContextAdministrador(), uuid, indicatorInstanceDto);
        }

        // Publish
        try {
            indicatorsServiceFacade.publishIndicatorsSystem(getServiceContextAdministrador(), uuid);
            fail("Indicators not published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_MUST_HAVE_ALL_INDICATORS_PUBLISHED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(2, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1]).length);
            boolean indicator2 = false;
            boolean indicator3 = false;
            for (int i = 0; i < ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1]).length; i++) {
                if (indicatorUuid2.equals(((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[i])) {
                    indicator2 = true;
                }
                if (indicatorUuid3.equals(((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[i])) {
                    indicator3 = true;
                }
            }
            assertTrue(indicator2);
            assertTrue(indicator3);
        }
    }

    @Test
    @Transactional
    public void testArchiveIndicatorsSystem() throws Exception {

        String uuid = INDICATORS_SYSTEM_3;
        String code = INDICATORS_SYSTEM_3_CODE;
        String versionNumber = INDICATORS_SYSTEM_3_VERSION;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, versionNumber);
            assertEquals(null, indicatorsSystemDto.getProductionVersion());
            assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDto.getPublishedVersion());
            assertNull(indicatorsSystemDto.getArchivedVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemDto.getProcStatus());
        }

        // Archive
        IndicatorsSystemDto indicatorsSystemDtoUpdated = indicatorsServiceFacade.archiveIndicatorsSystem(getServiceContextAdministrador(), uuid);

        // Validation
        {
            assertEquals(null, indicatorsSystemDtoUpdated.getProductionVersion());
            assertEquals(null, indicatorsSystemDtoUpdated.getPublishedVersion());
            assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDtoUpdated.getArchivedVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.ARCHIVED, indicatorsSystemDtoUpdated.getProcStatus());

            IndicatorsAsserts.assertEqualsDate("2011-03-03 01:02:04", indicatorsSystemDtoUpdated.getProductionValidationDate());
            assertEquals("user1", indicatorsSystemDtoUpdated.getProductionValidationUser());
            IndicatorsAsserts.assertEqualsDate("2011-04-04 03:02:04", indicatorsSystemDtoUpdated.getDiffusionValidationDate());
            assertEquals("user2", indicatorsSystemDtoUpdated.getDiffusionValidationUser());
            IndicatorsAsserts.assertEqualsDate("2011-05-05 04:02:04", indicatorsSystemDtoUpdated.getPublicationDate());
            assertEquals("user3", indicatorsSystemDtoUpdated.getPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), indicatorsSystemDtoUpdated.getArchiveDate()));
            assertEquals(getServiceContextAdministrador().getUserId(), indicatorsSystemDtoUpdated.getArchiveUser());
        }
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, versionNumber);
            assertEquals(null, indicatorsSystemDto.getProductionVersion());
            assertEquals(null, indicatorsSystemDto.getPublishedVersion());
            assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDto.getArchivedVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.ARCHIVED, indicatorsSystemDto.getProcStatus());

            IndicatorsAsserts.assertEqualsDate("2011-03-03 01:02:04", indicatorsSystemDto.getProductionValidationDate());
            assertEquals("user1", indicatorsSystemDto.getProductionValidationUser());
            IndicatorsAsserts.assertEqualsDate("2011-04-04 03:02:04", indicatorsSystemDto.getDiffusionValidationDate());
            assertEquals("user2", indicatorsSystemDto.getDiffusionValidationUser());
            IndicatorsAsserts.assertEqualsDate("2011-05-05 04:02:04", indicatorsSystemDto.getPublicationDate());
            assertEquals("user3", indicatorsSystemDto.getPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), indicatorsSystemDto.getArchiveDate()));
            assertEquals(getServiceContextAdministrador().getUserId(), indicatorsSystemDto.getArchiveUser());
        }
    }

    @Test
    @Transactional
    public void testArchiveIndicatorsSystemWithProductionVersion() throws Exception {

        String uuid = INDICATORS_SYSTEM_1;
        String code = INDICATORS_SYSTEM_1_CODE;
        String diffusionVersion = IndicatorsDataBaseTest.INIT_VERSION;
        String productionVersion = IndicatorsDataBaseTest.SECOND_VERSION;

        {
            IndicatorsSystemDto indicatorsSystemDtoV1 = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, diffusionVersion);
            IndicatorsSystemDto indicatorsSystemDtoV2 = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, productionVersion);
            assertEquals(productionVersion, indicatorsSystemDtoV1.getProductionVersion());
            assertEquals(diffusionVersion, indicatorsSystemDtoV1.getPublishedVersion());
            assertEquals(null, indicatorsSystemDtoV1.getArchivedVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemDtoV1.getProcStatus());
            assertEquals(IndicatorsSystemProcStatusEnum.DRAFT, indicatorsSystemDtoV2.getProcStatus());
        }

        // Archive
        indicatorsServiceFacade.archiveIndicatorsSystem(getServiceContextAdministrador(), uuid);

        // Validation
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, diffusionVersion);
            assertEquals(productionVersion, indicatorsSystemDto.getProductionVersion());
            assertEquals(null, indicatorsSystemDto.getPublishedVersion());
            assertEquals(diffusionVersion, indicatorsSystemDto.getArchivedVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.ARCHIVED, indicatorsSystemDto.getProcStatus());
        }
    }

    @Test
    @Transactional
    public void testArchiveIndicatorsSystemErrorNotExists() throws Exception {

        try {
            indicatorsServiceFacade.archiveIndicatorsSystem(getServiceContextAdministrador(), NOT_EXISTS);
            fail("Indicators system not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testArchiveIndicatorsSystemErrorWrongProcStatusProduction() throws Exception {

        String uuid = INDICATORS_SYSTEM_2;
        String code = INDICATORS_SYSTEM_2_CODE;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, IndicatorsDataBaseTest.INIT_VERSION);
            assertEquals(IndicatorsSystemProcStatusEnum.DRAFT, indicatorsSystemDto.getProcStatus());
            assertEquals(IndicatorsDataBaseTest.INIT_VERSION, indicatorsSystemDto.getProductionVersion());
            assertNull(indicatorsSystemDto.getPublishedVersion());
            assertNull(indicatorsSystemDto.getArchivedVersion());
        }

        try {
            indicatorsServiceFacade.archiveIndicatorsSystem(getServiceContextAdministrador(), uuid);
            fail("Indicators system is not published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_PUBLISHED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);

        }
    }

    @Test
    @Transactional
    public void testArchiveIndicatorsSystemErrorWrongProcStatusDiffusion() throws Exception {

        String uuid = INDICATORS_SYSTEM_8;
        String code = INDICATORS_SYSTEM_8_CODE;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, IndicatorsDataBaseTest.INIT_VERSION);
            assertEquals(IndicatorsSystemProcStatusEnum.ARCHIVED, indicatorsSystemDto.getProcStatus());
            assertEquals(null, indicatorsSystemDto.getProductionVersion());
            assertEquals(null, indicatorsSystemDto.getPublishedVersion());
            assertEquals(IndicatorsDataBaseTest.INIT_VERSION, indicatorsSystemDto.getArchivedVersion());
        }

        try {
            indicatorsServiceFacade.archiveIndicatorsSystem(getServiceContextAdministrador(), uuid);
            fail("Indicators system is not published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_PUBLISHED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Test
    @Transactional
    public void testVersioningIndicatorsSystem() throws Exception {

        String uuid = INDICATORS_SYSTEM_3;
        String code = INDICATORS_SYSTEM_3_CODE;
        String newVersionExpected = IndicatorsDataBaseTest.ANOTHER_NOT_INITIAL_VERSION;

        IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, null);
        assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDto.getVersionNumber());
        assertEquals(null, indicatorsSystemDto.getProductionVersion());
        assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDto.getPublishedVersion());
        assertNull(indicatorsSystemDto.getArchivedVersion());

        IndicatorsSystemDto indicatorsSystemDtoVersioned = indicatorsServiceFacade.versioningIndicatorsSystem(getServiceContextAdministrador(), uuid, VersionTypeEnum.MAJOR);

        // Validate
        assertEquals(newVersionExpected, indicatorsSystemDtoVersioned.getVersionNumber());
        assertEquals(newVersionExpected, indicatorsSystemDtoVersioned.getProductionVersion());
        assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDtoVersioned.getPublishedVersion());
        assertEquals(null, indicatorsSystemDtoVersioned.getArchivedVersion());
        IndicatorsAsserts.assertEqualsIndicatorsSystem(indicatorsSystemDto, indicatorsSystemDtoVersioned);

        IndicatorsSystemDto indicatorsSystemDtoProduction = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, newVersionExpected);
        IndicatorsSystemDto indicatorsSystemDtoDiffusion = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, INDICATORS_SYSTEM_3_VERSION);

        IndicatorsAsserts.assertEqualsIndicatorsSystem(indicatorsSystemDtoDiffusion, indicatorsSystemDtoProduction);

        assertEquals(IndicatorsSystemProcStatusEnum.DRAFT, indicatorsSystemDtoProduction.getProcStatus());
        assertEquals(newVersionExpected, indicatorsSystemDtoProduction.getVersionNumber());
        assertEquals(newVersionExpected, indicatorsSystemDtoProduction.getProductionVersion());
        assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDtoProduction.getPublishedVersion());
        assertEquals(null, indicatorsSystemDtoProduction.getArchivedVersion());

        assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemDtoDiffusion.getProcStatus());
        assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDtoDiffusion.getVersionNumber());
        assertEquals(newVersionExpected, indicatorsSystemDtoDiffusion.getProductionVersion());
        assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDtoDiffusion.getPublishedVersion());
        assertEquals(null, indicatorsSystemDtoDiffusion.getArchivedVersion());

        // Validate structure
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContextAdministrador(), uuid, newVersionExpected);
        assertEquals(2, indicatorsSystemStructureDto.getElements().size());

        {
            ElementLevelDto elementLevelDto = indicatorsSystemStructureDto.getElements().get(0);
            IndicatorsAsserts.assertEqualsInternationalString(elementLevelDto.getDimension().getTitle(), "es", "Título IndSys-3-v1-Dimension-1", "en", "Title IndSys-3-v1-Dimension-1");
            assertEquals(Long.valueOf(1), elementLevelDto.getOrderInLevel());
            assertEquals(2, elementLevelDto.getSubelements().size());

            // Children
            {
                ElementLevelDto elementLevelChildDto = elementLevelDto.getSubelements().get(0);
                assertEquals("IndSys-3-v1-IInstance-1A-CODE", elementLevelChildDto.getIndicatorInstance().getCode());
                IndicatorsAsserts.assertEqualsInternationalString(elementLevelChildDto.getIndicatorInstance().getTitle(), "es", "Título IndSys-3-v1-IInstance-1A", "en",
                        "Title IndSys-3-v1-IInstance-1A");
                assertEquals(Long.valueOf(1), elementLevelChildDto.getOrderInLevel());
                assertEquals(0, elementLevelChildDto.getSubelements().size());
            }
            {
                ElementLevelDto elementLevelChildDto = elementLevelDto.getSubelements().get(1);
                IndicatorsAsserts.assertEqualsInternationalString(elementLevelChildDto.getDimension().getTitle(), "es", "Título IndSys-3-v1-Dimension-1A", "en", "Title IndSys-3-v1-Dimension-1A");
                assertEquals(Long.valueOf(2), elementLevelChildDto.getOrderInLevel());
                assertEquals(0, elementLevelChildDto.getSubelements().size());
            }
        }
        {
            ElementLevelDto elementLevelDto = indicatorsSystemStructureDto.getElements().get(1);
            assertEquals("IndSys-3-v1-IInstance-2-CODE", elementLevelDto.getIndicatorInstance().getCode());
            IndicatorsAsserts.assertEqualsInternationalString(elementLevelDto.getIndicatorInstance().getTitle(), "es", "Título IndSys-3-v1-IInstance-2", "en", "Title IndSys-3-v1-IInstance-2");
            assertEquals(Long.valueOf(2), elementLevelDto.getOrderInLevel());
            assertEquals(0, elementLevelDto.getSubelements().size());
        }
    }

    @Test
    @Transactional
    public void testVersioningIndicatorsSystemVersionMinor() throws Exception {

        String uuid = INDICATORS_SYSTEM_3;
        String code = INDICATORS_SYSTEM_3_CODE;
        String newVersionExpected = IndicatorsDataBaseTest.NOT_INITIAL_VERSION_WITH_MINOR_INCREMENT;

        IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, null);
        assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDto.getVersionNumber());
        assertEquals(null, indicatorsSystemDto.getProductionVersion());
        assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDto.getPublishedVersion());
        assertNull(indicatorsSystemDto.getArchivedVersion());

        IndicatorsSystemDto indicatorsSystemDtoVersioned = indicatorsServiceFacade.versioningIndicatorsSystem(getServiceContextAdministrador(), uuid, VersionTypeEnum.MINOR);

        // Validate
        assertEquals(newVersionExpected, indicatorsSystemDtoVersioned.getVersionNumber());
        assertEquals(newVersionExpected, indicatorsSystemDtoVersioned.getProductionVersion());
        assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDtoVersioned.getPublishedVersion());
        assertEquals(null, indicatorsSystemDtoVersioned.getArchivedVersion());
        IndicatorsAsserts.assertEqualsIndicatorsSystem(indicatorsSystemDto, indicatorsSystemDtoVersioned);

        {
            IndicatorsSystemDto indicatorsSystemDtoProduction = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, newVersionExpected);
            IndicatorsSystemDto indicatorsSystemDtoDiffusion = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), code, INDICATORS_SYSTEM_3_VERSION);

            IndicatorsAsserts.assertEqualsIndicatorsSystem(indicatorsSystemDtoDiffusion, indicatorsSystemDtoProduction);

            assertEquals(IndicatorsSystemProcStatusEnum.DRAFT, indicatorsSystemDtoProduction.getProcStatus());
            assertEquals(newVersionExpected, indicatorsSystemDtoProduction.getVersionNumber());
            assertEquals(newVersionExpected, indicatorsSystemDtoProduction.getProductionVersion());
            assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDtoProduction.getPublishedVersion());
            assertEquals(null, indicatorsSystemDtoProduction.getArchivedVersion());

            assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemDtoDiffusion.getProcStatus());
            assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDtoDiffusion.getVersionNumber());
            assertEquals(newVersionExpected, indicatorsSystemDtoDiffusion.getProductionVersion());
            assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDtoDiffusion.getPublishedVersion());
            assertEquals(null, indicatorsSystemDtoDiffusion.getArchivedVersion());
        }
    }

    @Test
    @Transactional
    public void testVersioningIndicatorsSystemErrorNotExists() throws Exception {

        try {
            indicatorsServiceFacade.versioningIndicatorsSystem(getServiceContextAdministrador(), NOT_EXISTS, VersionTypeEnum.MINOR);
            fail("Indicators system not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testVersioningIndicatorsSystemErrorAlreadyExistsProduction() throws Exception {

        String uuid = INDICATORS_SYSTEM_2;

        try {
            indicatorsServiceFacade.versioningIndicatorsSystem(getServiceContextAdministrador(), uuid, VersionTypeEnum.MINOR);
            fail("Indicators system already exists in production");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_PUBLISHED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_ARCHIVED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
        }
    }

    @Test
    @Transactional
    public void testFindIndicatorsSystems() throws Exception {

        // Retrieve last versions...
        MetamacCriteriaResult<IndicatorsSystemSummaryDto> result = indicatorsServiceFacade.findIndicatorsSystems(getServiceContextTecnicoProduccion(), null);
        assertEquals(10, result.getResults().size());
        List<IndicatorsSystemSummaryDto> indicatorsSystemsDto = result.getResults();

        {
            IndicatorsSystemSummaryDto indicatorsSystemSummaryDto = indicatorsSystemsDto.get(0);
            assertEquals(INDICATORS_SYSTEM_1, indicatorsSystemSummaryDto.getUuid());
            assertEquals("CODE-1", indicatorsSystemSummaryDto.getCode());

            assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemSummaryDto.getDiffusionVersion().getProcStatus());
            assertEquals(IndicatorsDataBaseTest.INIT_VERSION, indicatorsSystemSummaryDto.getDiffusionVersion().getVersionNumber());

            assertEquals(IndicatorsSystemProcStatusEnum.DRAFT, indicatorsSystemSummaryDto.getProductionVersion().getProcStatus());
            assertEquals(IndicatorsDataBaseTest.SECOND_VERSION, indicatorsSystemSummaryDto.getProductionVersion().getVersionNumber());
        }
        {
            IndicatorsSystemSummaryDto indicatorsSystemSummaryDto = indicatorsSystemsDto.get(1);
            assertEquals(INDICATORS_SYSTEM_2, indicatorsSystemSummaryDto.getUuid());
            assertEquals("CODE-2", indicatorsSystemSummaryDto.getCode());

            assertNull(indicatorsSystemSummaryDto.getDiffusionVersion());

            assertEquals(IndicatorsSystemProcStatusEnum.DRAFT, indicatorsSystemSummaryDto.getProductionVersion().getProcStatus());
            assertEquals(IndicatorsDataBaseTest.INIT_VERSION, indicatorsSystemSummaryDto.getProductionVersion().getVersionNumber());
        }
        {
            IndicatorsSystemSummaryDto indicatorsSystemSummaryDto = indicatorsSystemsDto.get(2);
            assertEquals(INDICATORS_SYSTEM_3, indicatorsSystemSummaryDto.getUuid());
            assertEquals("CODE-3", indicatorsSystemSummaryDto.getCode());

            assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemSummaryDto.getDiffusionVersion().getProcStatus());
            assertEquals(IndicatorsDataBaseTest.NOT_INITIAL_VERSION, indicatorsSystemSummaryDto.getDiffusionVersion().getVersionNumber());

            assertNull(indicatorsSystemSummaryDto.getProductionVersion());
        }
        {
            IndicatorsSystemSummaryDto indicatorsSystemSummaryDto = indicatorsSystemsDto.get(3);
            assertEquals(INDICATORS_SYSTEM_4, indicatorsSystemSummaryDto.getUuid());
            assertEquals("CODE-4", indicatorsSystemSummaryDto.getCode());

            assertNull(indicatorsSystemSummaryDto.getDiffusionVersion());

            assertEquals(IndicatorsSystemProcStatusEnum.PRODUCTION_VALIDATION, indicatorsSystemSummaryDto.getProductionVersion().getProcStatus());
            assertEquals(IndicatorsDataBaseTest.INIT_VERSION, indicatorsSystemSummaryDto.getProductionVersion().getVersionNumber());
        }
        {
            IndicatorsSystemSummaryDto indicatorsSystemSummaryDto = indicatorsSystemsDto.get(4);
            assertEquals(INDICATORS_SYSTEM_5, indicatorsSystemSummaryDto.getUuid());
            assertEquals("CODE-5", indicatorsSystemSummaryDto.getCode());

            assertNull(indicatorsSystemSummaryDto.getDiffusionVersion());

            assertEquals(IndicatorsSystemProcStatusEnum.DIFFUSION_VALIDATION, indicatorsSystemSummaryDto.getProductionVersion().getProcStatus());
            assertEquals(IndicatorsDataBaseTest.INIT_VERSION, indicatorsSystemSummaryDto.getProductionVersion().getVersionNumber());
        }
        {
            IndicatorsSystemSummaryDto indicatorsSystemSummaryDto = indicatorsSystemsDto.get(5);
            assertEquals(INDICATORS_SYSTEM_6, indicatorsSystemSummaryDto.getUuid());
            assertEquals("CODE-6", indicatorsSystemSummaryDto.getCode());

            assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemSummaryDto.getDiffusionVersion().getProcStatus());
            assertEquals(IndicatorsDataBaseTest.INIT_VERSION_ANOTHER_HUGE_INCREMENT, indicatorsSystemSummaryDto.getDiffusionVersion().getVersionNumber());

            assertEquals(IndicatorsSystemProcStatusEnum.DIFFUSION_VALIDATION, indicatorsSystemSummaryDto.getProductionVersion().getProcStatus());
            assertEquals(IndicatorsDataBaseTest.SECOND_VERSION, indicatorsSystemSummaryDto.getProductionVersion().getVersionNumber());
        }
        {
            IndicatorsSystemSummaryDto indicatorsSystemSummaryDto = indicatorsSystemsDto.get(6);
            assertEquals(INDICATORS_SYSTEM_7, indicatorsSystemSummaryDto.getUuid());
            assertEquals("CODE-7", indicatorsSystemSummaryDto.getCode());

            assertEquals(IndicatorsSystemProcStatusEnum.ARCHIVED, indicatorsSystemSummaryDto.getDiffusionVersion().getProcStatus());
            assertEquals(IndicatorsDataBaseTest.INIT_VERSION, indicatorsSystemSummaryDto.getDiffusionVersion().getVersionNumber());

            assertEquals(IndicatorsSystemProcStatusEnum.DIFFUSION_VALIDATION, indicatorsSystemSummaryDto.getProductionVersion().getProcStatus());
            assertEquals(IndicatorsDataBaseTest.SECOND_VERSION, indicatorsSystemSummaryDto.getProductionVersion().getVersionNumber());
        }
        {
            IndicatorsSystemSummaryDto indicatorsSystemSummaryDto = indicatorsSystemsDto.get(7);
            assertEquals(INDICATORS_SYSTEM_8, indicatorsSystemSummaryDto.getUuid());
            assertEquals("CODE-8", indicatorsSystemSummaryDto.getCode());

            assertEquals(IndicatorsSystemProcStatusEnum.ARCHIVED, indicatorsSystemSummaryDto.getDiffusionVersion().getProcStatus());
            assertEquals(IndicatorsDataBaseTest.INIT_VERSION, indicatorsSystemSummaryDto.getDiffusionVersion().getVersionNumber());

            assertNull(indicatorsSystemSummaryDto.getProductionVersion());
        }
        {
            IndicatorsSystemSummaryDto indicatorsSystemSummaryDto = indicatorsSystemsDto.get(8);
            assertEquals(INDICATORS_SYSTEM_9, indicatorsSystemSummaryDto.getUuid());
            assertEquals("CODE-9", indicatorsSystemSummaryDto.getCode());

            assertNull(indicatorsSystemSummaryDto.getDiffusionVersion());

            assertEquals(IndicatorsSystemProcStatusEnum.VALIDATION_REJECTED, indicatorsSystemSummaryDto.getProductionVersion().getProcStatus());
            assertEquals(IndicatorsDataBaseTest.INIT_VERSION, indicatorsSystemSummaryDto.getProductionVersion().getVersionNumber());
        }
        {
            IndicatorsSystemSummaryDto indicatorsSystemSummaryDto = indicatorsSystemsDto.get(9);
            assertEquals(INDICATORS_SYSTEM_10, indicatorsSystemSummaryDto.getUuid());
            assertEquals("CODE-10", indicatorsSystemSummaryDto.getCode());

            assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemSummaryDto.getDiffusionVersion().getProcStatus());
            assertEquals(IndicatorsDataBaseTest.INIT_VERSION, indicatorsSystemSummaryDto.getDiffusionVersion().getVersionNumber());

            assertEquals(IndicatorsSystemProcStatusEnum.DRAFT, indicatorsSystemSummaryDto.getProductionVersion().getProcStatus());
            assertEquals(IndicatorsDataBaseTest.INIT_VERSION_MINOR_INCREMENT, indicatorsSystemSummaryDto.getProductionVersion().getVersionNumber());
        }
    }

    @Test
    @Transactional
    public void testFindIndicatorsSystemsByCriteria() throws Exception {

        // Retrieve code = y or z
        MetamacCriteria criteria = new MetamacCriteria();
        MetamacCriteriaDisjunctionRestriction disjunction = new MetamacCriteriaDisjunctionRestriction();
        disjunction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(IndicatorCriteriaPropertyEnum.CODE.name(), "CODE-3", OperationType.EQ));
        disjunction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(IndicatorCriteriaPropertyEnum.CODE.name(), "CODE-6", OperationType.EQ));
        disjunction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(IndicatorCriteriaPropertyEnum.CODE.name(), "CODE-9", OperationType.EQ));
        criteria.setRestriction(disjunction);

        MetamacCriteriaResult<IndicatorsSystemSummaryDto> result = indicatorsServiceFacade.findIndicatorsSystems(getServiceContextAdministrador(), criteria);
        assertEquals(3, result.getResults().size());
        List<IndicatorsSystemSummaryDto> indicatorsSystemsDto = result.getResults();
        assertEquals(INDICATORS_SYSTEM_3, indicatorsSystemsDto.get(0).getUuid());
        assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemsDto.get(0).getDiffusionVersion().getProcStatus());
        assertNull(indicatorsSystemsDto.get(0).getProductionVersion());

        assertEquals(INDICATORS_SYSTEM_6, indicatorsSystemsDto.get(1).getUuid());
        assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemsDto.get(1).getDiffusionVersion().getProcStatus());
        assertEquals(IndicatorsSystemProcStatusEnum.DIFFUSION_VALIDATION, indicatorsSystemsDto.get(1).getProductionVersion().getProcStatus());

        assertEquals(INDICATORS_SYSTEM_9, indicatorsSystemsDto.get(2).getUuid());
        assertNull(indicatorsSystemsDto.get(2).getDiffusionVersion());
        assertEquals(IndicatorsSystemProcStatusEnum.VALIDATION_REJECTED, indicatorsSystemsDto.get(2).getProductionVersion().getProcStatus());
    }

    @Test
    @Transactional
    public void testFindIndicatorsSystemsByCriteriaPaginated() throws Exception {

        // Retrieve code = y or z, paginated
        MetamacCriteria criteria = new MetamacCriteria();
        MetamacCriteriaDisjunctionRestriction disjunction = new MetamacCriteriaDisjunctionRestriction();
        disjunction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(IndicatorCriteriaPropertyEnum.CODE.name(), "CODE-3", OperationType.EQ));
        disjunction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(IndicatorCriteriaPropertyEnum.CODE.name(), "CODE-6", OperationType.EQ));
        disjunction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(IndicatorCriteriaPropertyEnum.CODE.name(), "CODE-9", OperationType.EQ));
        criteria.setRestriction(disjunction);

        MetamacCriteriaPaginator paginator = new MetamacCriteriaPaginator();
        paginator.setCountTotalResults(Boolean.TRUE);
        paginator.setMaximumResultSize(Integer.valueOf(2));
        criteria.setPaginator(paginator);

        {
            // Page 1
            criteria.getPaginator().setFirstResult(Integer.valueOf(0));

            MetamacCriteriaResult<IndicatorsSystemSummaryDto> result = indicatorsServiceFacade.findIndicatorsSystems(getServiceContextAdministrador(), criteria);
            assertEquals(2, result.getResults().size());
            assertEquals(Integer.valueOf(3), result.getPaginatorResult().getTotalResults());
            assertEquals(Integer.valueOf(0), result.getPaginatorResult().getFirstResult());
            // assertEquals(Integer.valueOf(2), result.getMaximumResultSize());
            List<IndicatorsSystemSummaryDto> indicatorsSystemsDto = result.getResults();

            assertEquals(INDICATORS_SYSTEM_3, indicatorsSystemsDto.get(0).getUuid());
            assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemsDto.get(0).getDiffusionVersion().getProcStatus());
            assertNull(indicatorsSystemsDto.get(0).getProductionVersion());

            assertEquals(INDICATORS_SYSTEM_6, indicatorsSystemsDto.get(1).getUuid());
            assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemsDto.get(1).getDiffusionVersion().getProcStatus());
            assertEquals(IndicatorsSystemProcStatusEnum.DIFFUSION_VALIDATION, indicatorsSystemsDto.get(1).getProductionVersion().getProcStatus());
        }
        {
            // Page 2
            criteria.getPaginator().setFirstResult(Integer.valueOf(2));

            MetamacCriteriaResult<IndicatorsSystemSummaryDto> result = indicatorsServiceFacade.findIndicatorsSystems(getServiceContextAdministrador(), criteria);
            assertEquals(1, result.getResults().size());
            assertEquals(Integer.valueOf(3), result.getPaginatorResult().getTotalResults());
            assertEquals(Integer.valueOf(2), result.getPaginatorResult().getFirstResult());
            assertEquals(Integer.valueOf(2), result.getPaginatorResult().getMaximumResultSize());
            List<IndicatorsSystemSummaryDto> indicatorsSystemsDto = result.getResults();

            assertEquals(INDICATORS_SYSTEM_9, indicatorsSystemsDto.get(0).getUuid());
            assertNull(indicatorsSystemsDto.get(0).getDiffusionVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.VALIDATION_REJECTED, indicatorsSystemsDto.get(0).getProductionVersion().getProcStatus());
        }
    }

    @Test
    @Transactional
    public void testRetrieveDimension() throws Exception {

        DimensionDto dimensionDto = indicatorsServiceFacade.retrieveDimension(getServiceContextAdministrador(), DIMENSION_1_INDICATORS_SYSTEM_1_V2);

        assertNotNull(dimensionDto);
        assertEquals(DIMENSION_1_INDICATORS_SYSTEM_1_V2, dimensionDto.getUuid());
        IndicatorsAsserts.assertEqualsInternationalString(dimensionDto.getTitle(), "es", "Título IndSys-1-v2-Dimension-1", "en", "Title IndSys-1-v2-Dimension-1");
        assertNull(dimensionDto.getParentUuid());

        IndicatorsAsserts.assertEqualsDate("2011-03-03 01:02:04", dimensionDto.getCreatedDate());
        assertEquals("user1", dimensionDto.getCreatedBy());
        IndicatorsAsserts.assertEqualsDate("2011-03-04 05:06:07", dimensionDto.getLastUpdated());
        assertEquals("user2", dimensionDto.getLastUpdatedBy());
    }

    @Test
    @Transactional
    public void testRetrieveDimensionErrorParameterRequired() throws Exception {

        String uuid = null;

        try {
            indicatorsServiceFacade.retrieveDimension(getServiceContextAdministrador(), uuid);
            fail("parameter required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.UUID, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testRetrieveDimensionErrorNotExists() throws Exception {

        String uuid = NOT_EXISTS;

        try {
            indicatorsServiceFacade.retrieveDimension(getServiceContextAdministrador(), uuid);
            fail("No exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DIMENSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testCreateDimension() throws Exception {

        // Create dimension
        DimensionDto dimensionDto = new DimensionDto();
        dimensionDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        dimensionDto.setParentUuid(null);
        dimensionDto.setOrderInLevel(Long.valueOf(5));

        String uuidIndicatorsSystem = INDICATORS_SYSTEM_1;
        DimensionDto dimensionDtoCreated = indicatorsServiceFacade.createDimension(getServiceContextAdministrador(), uuidIndicatorsSystem, dimensionDto);
        assertNotNull(dimensionDtoCreated.getUuid());

        // Retrieve dimension
        DimensionDto dimensionDtoRetrieved = indicatorsServiceFacade.retrieveDimension(getServiceContextAdministrador(), dimensionDtoCreated.getUuid());
        IndicatorsAsserts.assertEqualsDimension(dimensionDto, dimensionDtoRetrieved);

        // Validate new structure
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContextAdministrador(), INDICATORS_SYSTEM_1,
                IndicatorsDataBaseTest.SECOND_VERSION);
        assertEquals(5, indicatorsSystemStructureDto.getElements().size());
        assertEquals(INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(0).getIndicatorInstance().getUuid());
        assertEquals(Long.valueOf(1), indicatorsSystemStructureDto.getElements().get(0).getOrderInLevel());
        assertEquals(DIMENSION_1_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(1).getDimension().getUuid());
        assertEquals(Long.valueOf(2), indicatorsSystemStructureDto.getElements().get(1).getOrderInLevel());
        assertEquals(INDICATOR_INSTANCE_2_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(2).getIndicatorInstance().getUuid());
        assertEquals(Long.valueOf(3), indicatorsSystemStructureDto.getElements().get(2).getOrderInLevel());
        assertEquals(DIMENSION_2_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(3).getDimension().getUuid());
        assertEquals(Long.valueOf(4), indicatorsSystemStructureDto.getElements().get(3).getOrderInLevel());
        assertEquals(dimensionDtoCreated.getUuid(), indicatorsSystemStructureDto.getElements().get(4).getDimension().getUuid());
        assertEquals(Long.valueOf(5), indicatorsSystemStructureDto.getElements().get(4).getOrderInLevel());
        assertEquals(0, indicatorsSystemStructureDto.getElements().get(4).getSubelements().size());
    }

    @Test
    @DirtyDatabase
    public void testCreateDimensionWithOrderInMiddle() throws Exception {

        // Create dimension
        DimensionDto dimensionDto = new DimensionDto();
        dimensionDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        dimensionDto.setParentUuid(null);
        dimensionDto.setOrderInLevel(Long.valueOf(2));

        String uuidIndicatorsSystem = INDICATORS_SYSTEM_1;
        DimensionDto dimensionDtoCreated = indicatorsServiceFacade.createDimension(getServiceContextAdministrador(), uuidIndicatorsSystem, dimensionDto);
        assertNotNull(dimensionDtoCreated.getUuid());

        // Retrieve dimension
        DimensionDto dimensionDtoRetrieved = indicatorsServiceFacade.retrieveDimension(getServiceContextAdministrador(), dimensionDtoCreated.getUuid());
        IndicatorsAsserts.assertEqualsDimension(dimensionDto, dimensionDtoRetrieved);

        // Validate new structure
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContextAdministrador(), INDICATORS_SYSTEM_1,
                IndicatorsDataBaseTest.SECOND_VERSION);
        assertEquals(5, indicatorsSystemStructureDto.getElements().size());
        assertEquals(INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(0).getIndicatorInstance().getUuid());
        assertEquals(Long.valueOf(1), indicatorsSystemStructureDto.getElements().get(0).getOrderInLevel());
        assertEquals(dimensionDtoCreated.getUuid(), indicatorsSystemStructureDto.getElements().get(1).getDimension().getUuid());
        assertEquals(Long.valueOf(2), indicatorsSystemStructureDto.getElements().get(1).getOrderInLevel());
        assertEquals(0, indicatorsSystemStructureDto.getElements().get(1).getSubelements().size());
        assertEquals(DIMENSION_1_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(2).getDimension().getUuid());
        assertEquals(Long.valueOf(3), indicatorsSystemStructureDto.getElements().get(2).getOrderInLevel());
        assertEquals(INDICATOR_INSTANCE_2_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(3).getIndicatorInstance().getUuid());
        assertEquals(Long.valueOf(4), indicatorsSystemStructureDto.getElements().get(3).getOrderInLevel());
        assertEquals(DIMENSION_2_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(4).getDimension().getUuid());
        assertEquals(Long.valueOf(5), indicatorsSystemStructureDto.getElements().get(4).getOrderInLevel());
    }

    @Test
    @Transactional()
    public void testCreateDimensionSubdimension() throws Exception {

        String parentUuid = DIMENSION_1_INDICATORS_SYSTEM_1_V2;

        // Create dimension
        DimensionDto dimensionDto = new DimensionDto();
        dimensionDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        dimensionDto.setParentUuid(DIMENSION_1_INDICATORS_SYSTEM_1_V2);
        dimensionDto.setOrderInLevel(Long.valueOf(3));
        String uuidIndicatorsSystem = INDICATORS_SYSTEM_1;
        DimensionDto dimensionDtoCreated = indicatorsServiceFacade.createDimension(getServiceContextAdministrador(), uuidIndicatorsSystem, dimensionDto);
        assertNotNull(dimensionDtoCreated.getUuid());

        // Retrieve dimension
        DimensionDto dimensionDtoRetrieved = indicatorsServiceFacade.retrieveDimension(getServiceContextAdministrador(), dimensionDtoCreated.getUuid());
        IndicatorsAsserts.assertEqualsDimension(dimensionDto, dimensionDtoRetrieved);
        assertEquals(parentUuid, dimensionDtoRetrieved.getParentUuid());

        // Validate new structure
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContextAdministrador(), INDICATORS_SYSTEM_1,
                IndicatorsDataBaseTest.SECOND_VERSION);
        ElementLevelDto elementLevelDto = indicatorsSystemStructureDto.getElements().get(1);
        assertEquals(parentUuid, elementLevelDto.getDimension().getUuid());
        assertEquals(3, elementLevelDto.getSubelements().size());
        assertEquals(DIMENSION_1A_INDICATORS_SYSTEM_1_V2, elementLevelDto.getSubelements().get(0).getDimension().getUuid());
        assertEquals(Long.valueOf(1), elementLevelDto.getSubelements().get(0).getOrderInLevel());
        assertEquals(DIMENSION_1B_INDICATORS_SYSTEM_1_V2, elementLevelDto.getSubelements().get(1).getDimension().getUuid());
        assertEquals(Long.valueOf(2), elementLevelDto.getSubelements().get(1).getOrderInLevel());
        assertEquals(dimensionDtoCreated.getUuid(), elementLevelDto.getSubelements().get(2).getDimension().getUuid());
        assertEquals(Long.valueOf(3), elementLevelDto.getSubelements().get(2).getOrderInLevel());
    }

    @Test
    @DirtyDatabase
    public void testCreateDimensionSubdimensionOrderInMiddle() throws Exception {

        String parentUuid = DIMENSION_1_INDICATORS_SYSTEM_1_V2;

        // Create dimension
        DimensionDto dimensionDto = new DimensionDto();
        dimensionDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        dimensionDto.setParentUuid(DIMENSION_1_INDICATORS_SYSTEM_1_V2);
        dimensionDto.setOrderInLevel(Long.valueOf(2));
        String uuidIndicatorsSystem = INDICATORS_SYSTEM_1;
        DimensionDto dimensionDtoCreated = indicatorsServiceFacade.createDimension(getServiceContextAdministrador(), uuidIndicatorsSystem, dimensionDto);
        assertNotNull(dimensionDtoCreated.getUuid());

        // Retrieve dimension
        DimensionDto dimensionDtoRetrieved = indicatorsServiceFacade.retrieveDimension(getServiceContextAdministrador(), dimensionDtoCreated.getUuid());
        IndicatorsAsserts.assertEqualsDimension(dimensionDto, dimensionDtoRetrieved);
        assertEquals(parentUuid, dimensionDtoRetrieved.getParentUuid());

        // Validate new structure
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContextAdministrador(), INDICATORS_SYSTEM_1,
                IndicatorsDataBaseTest.SECOND_VERSION);
        ElementLevelDto elementLevelDto = indicatorsSystemStructureDto.getElements().get(1);
        assertEquals(parentUuid, elementLevelDto.getDimension().getUuid());
        assertEquals(3, elementLevelDto.getSubelements().size());
        assertEquals(DIMENSION_1A_INDICATORS_SYSTEM_1_V2, elementLevelDto.getSubelements().get(0).getDimension().getUuid());
        assertEquals(Long.valueOf(1), elementLevelDto.getSubelements().get(0).getOrderInLevel());
        assertEquals(dimensionDtoCreated.getUuid(), elementLevelDto.getSubelements().get(1).getDimension().getUuid());
        assertEquals(Long.valueOf(2), elementLevelDto.getSubelements().get(1).getOrderInLevel());
        assertEquals(DIMENSION_1B_INDICATORS_SYSTEM_1_V2, elementLevelDto.getSubelements().get(2).getDimension().getUuid());
        assertEquals(Long.valueOf(3), elementLevelDto.getSubelements().get(2).getOrderInLevel());
    }

    @Test
    @Transactional
    public void testCreateDimensionSubSubdimension() throws Exception {

        String parentUuid = DIMENSION_1A_INDICATORS_SYSTEM_1_V2;

        // Create subdimension
        DimensionDto dimensionDto = new DimensionDto();
        dimensionDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        dimensionDto.setParentUuid(parentUuid);
        dimensionDto.setOrderInLevel(Long.valueOf(1));
        String uuidIndicatorsSystem = INDICATORS_SYSTEM_1;
        DimensionDto dimensionDtoCreated = indicatorsServiceFacade.createDimension(getServiceContextAdministrador(), uuidIndicatorsSystem, dimensionDto);
        assertNotNull(dimensionDtoCreated.getUuid());

        // Validate
        DimensionDto dimensionDtoRetrieved = indicatorsServiceFacade.retrieveDimension(getServiceContextAdministrador(), dimensionDtoCreated.getUuid());
        IndicatorsAsserts.assertEqualsDimension(dimensionDtoCreated, dimensionDtoRetrieved);
        assertEquals(parentUuid, dimensionDtoRetrieved.getParentUuid());

        // Validate new structure
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContextAdministrador(), INDICATORS_SYSTEM_1,
                IndicatorsDataBaseTest.SECOND_VERSION);
        ElementLevelDto elementLevelDto = indicatorsSystemStructureDto.getElements().get(1).getSubelements().get(0);
        assertEquals(parentUuid, elementLevelDto.getDimension().getUuid());
        assertEquals(1, elementLevelDto.getSubelements().size());
        assertEquals(dimensionDtoCreated.getUuid(), elementLevelDto.getSubelements().get(0).getDimension().getUuid());
        assertEquals(Long.valueOf(1), elementLevelDto.getSubelements().get(0).getOrderInLevel());
    }

    @Test
    @Transactional
    public void testCreateDimensionErrorParametersRequired() throws Exception {

        DimensionDto dimensionDto = new DimensionDto();
        dimensionDto.setTitle(null);
        dimensionDto.setOrderInLevel(null);
        try {
            indicatorsServiceFacade.createDimension(getServiceContextAdministrador(), INDICATORS_SYSTEM_1, dimensionDto);
            fail("parameters required");
        } catch (MetamacException e) {
            assertEquals(2, e.getExceptionItems().size());

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.DIMENSION_TITLE, e.getExceptionItems().get(0).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(1).getCode());
            assertEquals(1, e.getExceptionItems().get(1).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.DIMENSION_ORDER_IN_LEVEL, e.getExceptionItems().get(1).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testCreateDimensionErrorOrderIncorrect() throws Exception {

        // Create dimension
        DimensionDto dimensionDto = new DimensionDto();
        dimensionDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        dimensionDto.setOrderInLevel(Long.MAX_VALUE);

        try {
            indicatorsServiceFacade.createDimension(getServiceContextAdministrador(), INDICATORS_SYSTEM_1, dimensionDto);
            fail("order incorrect");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.DIMENSION_ORDER_IN_LEVEL, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testCreateDimensionErrorOrderIncorrectNegative() throws Exception {

        // Create dimension
        DimensionDto dimensionDto = new DimensionDto();
        dimensionDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        dimensionDto.setOrderInLevel(Long.MIN_VALUE);

        try {
            indicatorsServiceFacade.createDimension(getServiceContextAdministrador(), INDICATORS_SYSTEM_1, dimensionDto);
            fail("order incorrect");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.DIMENSION_ORDER_IN_LEVEL, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testCreateDimensionErrorIndicatorsSystemNotExists() throws Exception {

        DimensionDto dimensionDto = new DimensionDto();
        dimensionDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        dimensionDto.setOrderInLevel(Long.valueOf(1));
        try {
            indicatorsServiceFacade.createDimension(getServiceContextAdministrador(), NOT_EXISTS, dimensionDto);
            fail("Indicators system not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testCreateDimensionErrorIndicatorsSystemHasNotVersionInProduction() throws Exception {

        String indicatorsSystemUuid = INDICATORS_SYSTEM_3;
        DimensionDto dimensionDto = new DimensionDto();
        dimensionDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        dimensionDto.setOrderInLevel(Long.valueOf(1));

        try {
            indicatorsServiceFacade.createDimension(getServiceContextAdministrador(), indicatorsSystemUuid, dimensionDto);
            fail("Indicators system not in production");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_IN_PRODUCTION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(indicatorsSystemUuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testCreateDimensionSubdimensionErrorDimensionNotExists() throws Exception {

        DimensionDto dimensionDto = new DimensionDto();
        dimensionDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        dimensionDto.setParentUuid(DIMENSION_NOT_EXISTS);
        dimensionDto.setOrderInLevel(Long.valueOf(1));

        try {
            indicatorsServiceFacade.createDimension(getServiceContextAdministrador(), INDICATORS_SYSTEM_1, dimensionDto);
            fail("dimension id not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DIMENSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(DIMENSION_NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testCreateDimensionSubdimensionErrorDimensionNotExistsInIndicatorsSystem() throws Exception {

        DimensionDto dimensionDto = new DimensionDto();
        dimensionDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        dimensionDto.setParentUuid(DIMENSION_1_INDICATORS_SYSTEM_1_V2);
        dimensionDto.setOrderInLevel(Long.valueOf(1));

        try {
            indicatorsServiceFacade.createDimension(getServiceContextAdministrador(), INDICATORS_SYSTEM_2, dimensionDto);
            fail("dimension id not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DIMENSION_NOT_FOUND_IN_INDICATORS_SYSTEM.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(DIMENSION_1_INDICATORS_SYSTEM_1_V2, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(INDICATORS_SYSTEM_2, e.getExceptionItems().get(0).getMessageParameters()[1]);
        }
    }

    @Test
    @Transactional
    public void testDeleteDimension() throws Exception {

        String uuid = DIMENSION_1_INDICATORS_SYSTEM_1_V2;

        // Retrieve
        DimensionDto dimensionDto = indicatorsServiceFacade.retrieveDimension(getServiceContextAdministrador(), uuid);
        assertEquals(Long.valueOf(2), dimensionDto.getOrderInLevel());

        // Delete dimension
        indicatorsServiceFacade.deleteDimension(getServiceContextAdministrador(), uuid);

        // Validation
        try {
            indicatorsServiceFacade.retrieveDimension(getServiceContextAdministrador(), uuid);
            fail("Dimension deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DIMENSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }

        // Validate new structure
        // Checks orders of other elements are updated in same level
        // Subdimensions and indicators instances are deleted too
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContextAdministrador(), INDICATORS_SYSTEM_1,
                IndicatorsDataBaseTest.SECOND_VERSION);
        assertEquals(3, indicatorsSystemStructureDto.getElements().size());
        assertEquals(INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(0).getIndicatorInstance().getUuid());
        assertEquals(Long.valueOf(1), indicatorsSystemStructureDto.getElements().get(0).getOrderInLevel());
        assertEquals(INDICATOR_INSTANCE_2_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(1).getIndicatorInstance().getUuid());
        assertEquals(Long.valueOf(2), indicatorsSystemStructureDto.getElements().get(1).getOrderInLevel());
        assertEquals(DIMENSION_2_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(2).getDimension().getUuid());
        assertEquals(Long.valueOf(3), indicatorsSystemStructureDto.getElements().get(2).getOrderInLevel());

        // Retrieve at least one subdimension to checks it is deleted
        try {
            indicatorsServiceFacade.retrieveDimension(getServiceContextAdministrador(), DIMENSION_1A_INDICATORS_SYSTEM_1_V2);
            fail("Subimension deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DIMENSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(DIMENSION_1A_INDICATORS_SYSTEM_1_V2, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testDeleteDimensionSubdimension() throws Exception {

        String uuid = DIMENSION_1B_INDICATORS_SYSTEM_1_V2;

        // Delete dimension
        indicatorsServiceFacade.deleteDimension(getServiceContextAdministrador(), uuid);

        // Validation
        try {
            indicatorsServiceFacade.retrieveDimension(getServiceContextAdministrador(), uuid);
            fail("Dimension deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DIMENSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }

        // Validate new structure
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContextAdministrador(), INDICATORS_SYSTEM_1,
                IndicatorsDataBaseTest.SECOND_VERSION);
        assertEquals(4, indicatorsSystemStructureDto.getElements().size());
        assertEquals(INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(0).getIndicatorInstance().getUuid());
        assertEquals(Long.valueOf(1), indicatorsSystemStructureDto.getElements().get(0).getOrderInLevel());
        assertEquals(DIMENSION_1_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(1).getDimension().getUuid());
        assertEquals(Long.valueOf(2), indicatorsSystemStructureDto.getElements().get(1).getOrderInLevel());
        assertEquals(1, indicatorsSystemStructureDto.getElements().get(1).getSubelements().size());
        assertEquals(DIMENSION_1A_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(1).getSubelements().get(0).getDimension().getUuid());
        assertEquals(Long.valueOf(1), indicatorsSystemStructureDto.getElements().get(1).getSubelements().get(0).getDimension().getOrderInLevel());
        assertEquals(INDICATOR_INSTANCE_2_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(2).getIndicatorInstance().getUuid());
        assertEquals(Long.valueOf(3), indicatorsSystemStructureDto.getElements().get(2).getOrderInLevel());
        assertEquals(DIMENSION_2_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(3).getDimension().getUuid());
        assertEquals(Long.valueOf(4), indicatorsSystemStructureDto.getElements().get(3).getOrderInLevel());
    }

    @Test
    @Transactional
    public void testDeleteDimensionErrorIndicatorsSystemVersionPublished() throws Exception {

        try {
            indicatorsServiceFacade.deleteDimension(getServiceContextAdministrador(), DIMENSION_1_INDICATORS_SYSTEM_3);
            fail("Indicators system not in production");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(INDICATORS_SYSTEM_3, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_DRAFT, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_VALIDATION_REJECTED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
            assertEquals(ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_PRODUCTION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[2]);
            assertEquals(ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_DIFFUSION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[3]);
        }
    }

    @Test
    @Transactional
    public void testDeleteDimensionErrorNotExists() throws Exception {

        String uuid = NOT_EXISTS;

        // Validation
        try {
            indicatorsServiceFacade.deleteDimension(getServiceContextAdministrador(), uuid);
            fail("Dimension not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DIMENSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testUpdateDimension() throws Exception {

        String uuid = DIMENSION_1_INDICATORS_SYSTEM_1_V2;
        DimensionDto dimensionDto = indicatorsServiceFacade.retrieveDimension(getServiceContextAdministrador(), uuid);
        dimensionDto.setTitle(IndicatorsMocks.mockInternationalStringDto());

        // Update
        DimensionDto dimensionDtoUpdated = indicatorsServiceFacade.updateDimension(getServiceContextAdministrador(), dimensionDto);

        // Validation
        IndicatorsAsserts.assertEqualsDimension(dimensionDto, dimensionDtoUpdated);
        dimensionDtoUpdated = indicatorsServiceFacade.retrieveDimension(getServiceContextAdministrador(), uuid);
        IndicatorsAsserts.assertEqualsDimension(dimensionDto, dimensionDtoUpdated);
    }

    @Test
    @Transactional
    public void testUpdateDimensionErrorChangeParentDimensionAndOrder() throws Exception {

        DimensionDto dimensionDto = indicatorsServiceFacade.retrieveDimension(getServiceContextAdministrador(), DIMENSION_1A_INDICATORS_SYSTEM_1_V2);
        assertEquals(DIMENSION_1_INDICATORS_SYSTEM_1_V2, dimensionDto.getParentUuid());
        assertEquals(Long.valueOf(1), dimensionDto.getOrderInLevel());
        dimensionDto.setParentUuid(DIMENSION_1BA_INDICATORS_SYSTEM_1_V2);
        dimensionDto.setOrderInLevel(Long.valueOf(2));

        try {
            indicatorsServiceFacade.updateDimension(getServiceContextAdministrador(), dimensionDto);
            fail("Parent and order changed");
        } catch (MetamacException e) {
            assertEquals(2, e.getExceptionItems().size());

            assertEquals(ServiceExceptionType.METADATA_UNMODIFIABLE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.DIMENSION_PARENT_UUID, e.getExceptionItems().get(0).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_UNMODIFIABLE.getCode(), e.getExceptionItems().get(1).getCode());
            assertEquals(1, e.getExceptionItems().get(1).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.DIMENSION_ORDER_IN_LEVEL, e.getExceptionItems().get(1).getMessageParameters()[0]);
        }

        // Put parent null
        dimensionDto.setParentUuid(null);
        dimensionDto.setOrderInLevel(Long.valueOf(1));

        try {
            indicatorsServiceFacade.updateDimension(getServiceContextAdministrador(), dimensionDto);
            fail("Parent changed");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_UNMODIFIABLE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.DIMENSION_PARENT_UUID, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testUpdateDimensionErrorIndicatorsSystemPublished() throws Exception {

        DimensionDto dimensionDto = indicatorsServiceFacade.retrieveDimension(getServiceContextAdministrador(), DIMENSION_1_INDICATORS_SYSTEM_1_V1);

        try {
            indicatorsServiceFacade.updateDimension(getServiceContextAdministrador(), dimensionDto);
            fail("Indicators system published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(INDICATORS_SYSTEM_1, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_DRAFT, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_VALIDATION_REJECTED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
            assertEquals(ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_PRODUCTION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[2]);
            assertEquals(ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_DIFFUSION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[3]);
        }
    }

    @Test
    @Transactional
    public void testUpdateDimensionErrorNotExists() throws Exception {

        DimensionDto dimensionDto = new DimensionDto();
        dimensionDto.setUuid(NOT_EXISTS);
        dimensionDto.setTitle(IndicatorsMocks.mockInternationalStringDto());

        try {
            indicatorsServiceFacade.updateDimension(getServiceContextAdministrador(), dimensionDto);
            fail("Dimension not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DIMENSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testUpdateDimensionErrorOptimisticLocking() throws Exception {

        String uuid = DIMENSION_1_INDICATORS_SYSTEM_1_V2;

        DimensionDto dimensionDtoSession1 = indicatorsServiceFacade.retrieveDimension(getServiceContextAdministrador(), uuid);
        assertEquals(Long.valueOf(1), dimensionDtoSession1.getVersionOptimisticLocking());
        dimensionDtoSession1.setTitle(IndicatorsMocks.mockInternationalStringDto());

        DimensionDto dimensionDtoSession2 = indicatorsServiceFacade.retrieveDimension(getServiceContextAdministrador(), uuid);
        assertEquals(Long.valueOf(1), dimensionDtoSession2.getVersionOptimisticLocking());
        dimensionDtoSession2.setTitle(IndicatorsMocks.mockInternationalStringDto());

        // Update by session 1
        DimensionDto dimensionDtoSession1AfterUpdate = indicatorsServiceFacade.updateDimension(getServiceContextAdministrador(), dimensionDtoSession1);
        assertTrue(dimensionDtoSession1AfterUpdate.getVersionOptimisticLocking() > dimensionDtoSession1.getVersionOptimisticLocking());
        IndicatorsAsserts.assertEqualsDimension(dimensionDtoSession1, dimensionDtoSession1AfterUpdate);

        // Fails when is updated by session 2
        try {
            indicatorsServiceFacade.updateDimension(getServiceContextAdministrador(), dimensionDtoSession2);
            fail("Optimistic locking");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.OPTIMISTIC_LOCKING.getCode(), e.getExceptionItems().get(0).getCode());
            assertNull(e.getExceptionItems().get(0).getMessageParameters());
        }

        // Session 1 can modify because has last version
        dimensionDtoSession1AfterUpdate.setTitle(IndicatorsMocks.mockInternationalStringDto());
        DimensionDto dimensionDtoSession1AfterUpdate2 = indicatorsServiceFacade.updateDimension(getServiceContextAdministrador(), dimensionDtoSession1AfterUpdate);
        assertTrue(dimensionDtoSession1AfterUpdate2.getVersionOptimisticLocking() > dimensionDtoSession1AfterUpdate.getVersionOptimisticLocking());
        IndicatorsAsserts.assertEqualsDimension(dimensionDtoSession1AfterUpdate, dimensionDtoSession1AfterUpdate2);
    }

    @Test
    @Transactional
    public void testUpdateDimensionLocation() throws Exception {
        // In other test testUpdateDimensionLocation*
    }

    @Test
    @DirtyDatabase
    public void testUpdateDimensionLocationActualWithoutParentTargetWithParent() throws Exception {

        String uuid = DIMENSION_1_INDICATORS_SYSTEM_1_V2;
        String parentBeforeUuid = null;
        String parentTargetUuid = DIMENSION_2_INDICATORS_SYSTEM_1_V2;

        DimensionDto dimensionDto = indicatorsServiceFacade.retrieveDimension(getServiceContextAdministrador(), uuid);
        assertEquals(parentBeforeUuid, dimensionDto.getParentUuid());

        // Update location
        DimensionDto dimensionDtoChanged = indicatorsServiceFacade.updateDimensionLocation(getServiceContextAdministrador(), uuid, parentTargetUuid, Long.valueOf(1));

        // Validate dimension
        assertEquals(parentTargetUuid, dimensionDtoChanged.getParentUuid());
        dimensionDtoChanged = indicatorsServiceFacade.retrieveDimension(getServiceContextAdministrador(), uuid);
        assertEquals(parentTargetUuid, dimensionDtoChanged.getParentUuid());

        // Validate source
        {
            IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContextAdministrador(), INDICATORS_SYSTEM_1,
                    IndicatorsDataBaseTest.SECOND_VERSION);
            assertEquals(3, indicatorsSystemStructureDto.getElements().size());
            assertEquals(INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(0).getIndicatorInstance().getUuid());
            assertEquals(Long.valueOf(1), indicatorsSystemStructureDto.getElements().get(0).getOrderInLevel());
            assertEquals(INDICATOR_INSTANCE_2_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(1).getIndicatorInstance().getUuid());
            assertEquals(Long.valueOf(2), indicatorsSystemStructureDto.getElements().get(1).getOrderInLevel());
            assertEquals(DIMENSION_2_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(2).getDimension().getUuid());
            assertEquals(Long.valueOf(3), indicatorsSystemStructureDto.getElements().get(2).getOrderInLevel());
        }

        // Validate target
        {
            IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContextAdministrador(), INDICATORS_SYSTEM_1,
                    IndicatorsDataBaseTest.SECOND_VERSION);
            ElementLevelDto elementLevelDto = indicatorsSystemStructureDto.getElements().get(2);
            assertEquals(parentTargetUuid, elementLevelDto.getDimension().getUuid());
            assertEquals(1, elementLevelDto.getSubelements().size());
            assertEquals(uuid, elementLevelDto.getSubelements().get(0).getDimension().getUuid());
            assertEquals(Long.valueOf(1), indicatorsSystemStructureDto.getElements().get(0).getOrderInLevel());
        }
    }

    @Test
    @DirtyDatabase
    public void testUpdateDimensionLocationActualWithParentTargetWithoutParent() throws Exception {

        String uuid = DIMENSION_1BA_INDICATORS_SYSTEM_1_V2;
        String parentBeforeUuid = DIMENSION_1B_INDICATORS_SYSTEM_1_V2;
        String parentTargetUuid = null;

        // Retrieve actual
        DimensionDto dimensionDto = indicatorsServiceFacade.retrieveDimension(getServiceContextAdministrador(), uuid);
        assertEquals(parentBeforeUuid, dimensionDto.getParentUuid());

        // Update location
        indicatorsServiceFacade.updateDimensionLocation(getServiceContextAdministrador(), uuid, parentTargetUuid, Long.valueOf(2));

        // Validate parent is changed in dimension
        DimensionDto dimensionDtoChanged = indicatorsServiceFacade.retrieveDimension(getServiceContextAdministrador(), uuid);
        assertNull(dimensionDtoChanged.getParentUuid());

        // Validate source
        {
            IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContextAdministrador(), INDICATORS_SYSTEM_1,
                    IndicatorsDataBaseTest.SECOND_VERSION);
            ElementLevelDto elementLevelDto = indicatorsSystemStructureDto.getElements().get(2).getSubelements().get(1);
            assertEquals(parentBeforeUuid, elementLevelDto.getDimension().getUuid());
            assertEquals(1, elementLevelDto.getSubelements().size());
        }

        // Validate target
        {
            IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContextAdministrador(), INDICATORS_SYSTEM_1,
                    IndicatorsDataBaseTest.SECOND_VERSION);
            assertEquals(5, indicatorsSystemStructureDto.getElements().size());
            assertEquals(INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(0).getIndicatorInstance().getUuid());
            assertEquals(Long.valueOf(1), indicatorsSystemStructureDto.getElements().get(0).getOrderInLevel());
            assertEquals(uuid, indicatorsSystemStructureDto.getElements().get(1).getDimension().getUuid());
            assertEquals(Long.valueOf(2), indicatorsSystemStructureDto.getElements().get(1).getOrderInLevel());
            assertEquals(DIMENSION_1_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(2).getDimension().getUuid());
            assertEquals(Long.valueOf(3), indicatorsSystemStructureDto.getElements().get(2).getOrderInLevel());
            assertEquals(INDICATOR_INSTANCE_2_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(3).getIndicatorInstance().getUuid());
            assertEquals(Long.valueOf(4), indicatorsSystemStructureDto.getElements().get(3).getOrderInLevel());
            assertEquals(DIMENSION_2_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(4).getDimension().getUuid());
            assertEquals(Long.valueOf(5), indicatorsSystemStructureDto.getElements().get(4).getOrderInLevel());
        }
    }

    @Test
    @DirtyDatabase
    public void testUpdateDimensionLocationChangingDimensionParent() throws Exception {

        String uuid = DIMENSION_1BA_INDICATORS_SYSTEM_1_V2;
        String parentBeforeUuid = DIMENSION_1B_INDICATORS_SYSTEM_1_V2;
        String parentTargetUuid = DIMENSION_1A_INDICATORS_SYSTEM_1_V2;

        // Retrieve actual
        DimensionDto dimensionDto = indicatorsServiceFacade.retrieveDimension(getServiceContextAdministrador(), uuid);
        assertEquals(parentBeforeUuid, dimensionDto.getParentUuid());

        // Update location
        DimensionDto dimensionDtoChanged = indicatorsServiceFacade.updateDimensionLocation(getServiceContextAdministrador(), uuid, parentTargetUuid, Long.valueOf(1));

        // Validate dimension
        assertEquals(parentTargetUuid, dimensionDtoChanged.getParentUuid());
        dimensionDtoChanged = indicatorsServiceFacade.retrieveDimension(getServiceContextAdministrador(), uuid);
        assertEquals(parentTargetUuid, dimensionDtoChanged.getParentUuid());

        // Validate source
        {
            IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContextAdministrador(), INDICATORS_SYSTEM_1,
                    IndicatorsDataBaseTest.SECOND_VERSION);
            ElementLevelDto elementLevelDto = indicatorsSystemStructureDto.getElements().get(1).getSubelements().get(1);
            assertEquals(parentBeforeUuid, elementLevelDto.getDimension().getUuid());
            assertEquals(1, elementLevelDto.getSubelements().size());
        }

        // Validate target
        {
            IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContextAdministrador(), INDICATORS_SYSTEM_1,
                    IndicatorsDataBaseTest.SECOND_VERSION);
            ElementLevelDto elementLevelDto = indicatorsSystemStructureDto.getElements().get(1).getSubelements().get(0);
            assertEquals(parentTargetUuid, elementLevelDto.getDimension().getUuid());
            assertEquals(1, elementLevelDto.getSubelements().size());
            assertEquals(uuid, elementLevelDto.getSubelements().get(0).getDimension().getUuid());
        }
    }

    @Test
    @DirtyDatabase
    public void testUpdateDimensionLocationActualSameParentOnlyChangeOrderWithoutParent() throws Exception {

        String uuid = DIMENSION_1_INDICATORS_SYSTEM_1_V2;

        // Retrieve actual
        DimensionDto dimensionDto = indicatorsServiceFacade.retrieveDimension(getServiceContextAdministrador(), uuid);
        assertNull(dimensionDto.getParentUuid());
        assertEquals(Long.valueOf(2), dimensionDto.getOrderInLevel());

        // Update location
        DimensionDto dimensionDtoChanged = indicatorsServiceFacade.updateDimensionLocation(getServiceContextAdministrador(), uuid, dimensionDto.getParentUuid(), Long.valueOf(4));

        // Validate dimension
        assertNull(dimensionDtoChanged.getParentUuid());
        assertEquals(Long.valueOf(4), dimensionDtoChanged.getOrderInLevel());
        dimensionDtoChanged = indicatorsServiceFacade.retrieveDimension(getServiceContextAdministrador(), uuid);
        assertNull(dimensionDtoChanged.getParentUuid());
        assertEquals(Long.valueOf(4), dimensionDtoChanged.getOrderInLevel());

        // Validate source and target
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContextAdministrador(), INDICATORS_SYSTEM_1,
                IndicatorsDataBaseTest.SECOND_VERSION);
        assertEquals(4, indicatorsSystemStructureDto.getElements().size());
        assertEquals(INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(0).getIndicatorInstance().getUuid());
        assertEquals(Long.valueOf(1), indicatorsSystemStructureDto.getElements().get(0).getOrderInLevel());
        assertEquals(INDICATOR_INSTANCE_2_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(1).getIndicatorInstance().getUuid());
        assertEquals(Long.valueOf(2), indicatorsSystemStructureDto.getElements().get(1).getOrderInLevel());
        assertEquals(DIMENSION_2_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(2).getDimension().getUuid());
        assertEquals(Long.valueOf(3), indicatorsSystemStructureDto.getElements().get(2).getOrderInLevel());
        assertEquals(DIMENSION_1_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(3).getDimension().getUuid());
        assertEquals(Long.valueOf(4), indicatorsSystemStructureDto.getElements().get(3).getOrderInLevel());
    }

    @Test
    @DirtyDatabase
    public void testUpdateDimensionLocationActualSameParentOnlyChangeOrderWithParent() throws Exception {

        String uuid = DIMENSION_1BA_INDICATORS_SYSTEM_1_V2;
        String parentUuid = DIMENSION_1B_INDICATORS_SYSTEM_1_V2;

        // Retrieve actual
        DimensionDto dimensionDto = indicatorsServiceFacade.retrieveDimension(getServiceContextAdministrador(), uuid);
        assertEquals(parentUuid, dimensionDto.getParentUuid());
        assertEquals(Long.valueOf(1), dimensionDto.getOrderInLevel());

        // Update location
        indicatorsServiceFacade.updateDimensionLocation(getServiceContextAdministrador(), uuid, dimensionDto.getParentUuid(), Long.valueOf(2));

        // Validate dimension
        DimensionDto dimensionDtoChanged = indicatorsServiceFacade.retrieveDimension(getServiceContextAdministrador(), uuid);
        assertEquals(parentUuid, dimensionDtoChanged.getParentUuid());
        assertEquals(Long.valueOf(2), dimensionDtoChanged.getOrderInLevel());

        // Validate source and target
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContextAdministrador(), INDICATORS_SYSTEM_1,
                IndicatorsDataBaseTest.SECOND_VERSION);
        ElementLevelDto elementLevelDto = indicatorsSystemStructureDto.getElements().get(1).getSubelements().get(1);
        assertEquals(parentUuid, elementLevelDto.getDimension().getUuid());
        assertEquals(2, elementLevelDto.getSubelements().size());
        assertEquals(INDICATOR_INSTANCE_3_INDICATORS_SYSTEM_1_V2, elementLevelDto.getSubelements().get(0).getIndicatorInstance().getUuid());
        assertEquals(Long.valueOf(1), elementLevelDto.getSubelements().get(0).getIndicatorInstance().getOrderInLevel());
        assertEquals(uuid, elementLevelDto.getSubelements().get(1).getDimension().getUuid());
        assertEquals(Long.valueOf(2), elementLevelDto.getSubelements().get(1).getDimension().getOrderInLevel());
    }

    @Test
    @Transactional
    public void testUpdateDimensionLocationErrorParentIsChild() throws Exception {

        String uuid = DIMENSION_1_INDICATORS_SYSTEM_1_V2;
        String parentTargetUuid = DIMENSION_1BA_INDICATORS_SYSTEM_1_V2;

        try {
            indicatorsServiceFacade.updateDimensionLocation(getServiceContextAdministrador(), uuid, parentTargetUuid, Long.valueOf(1));
            fail("It is child");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.DIMENSION_PARENT_UUID, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testUpdateDimensionLocationErrorNotExists() throws Exception {

        String uuid = NOT_EXISTS;

        // Validation
        try {
            indicatorsServiceFacade.updateDimensionLocation(getServiceContextAdministrador(), uuid, null, Long.valueOf(1));
            fail("Dimension not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DIMENSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testUpdateDimensionLocationErrorParentNotExists() throws Exception {
        String uuid = DIMENSION_1_INDICATORS_SYSTEM_1_V2;
        String parentTargetUuid = NOT_EXISTS;

        // Validation
        try {
            indicatorsServiceFacade.updateDimensionLocation(getServiceContextAdministrador(), uuid, parentTargetUuid, Long.valueOf(1));
            fail("Dimension not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DIMENSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(parentTargetUuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testUpdateDimensionLocationErrorParentNotExistsInIndicatorSystemVersion() throws Exception {
        String uuid = DIMENSION_1_INDICATORS_SYSTEM_1_V2;
        String parentTargetUuid = DIMENSION_1_INDICATORS_SYSTEM_1_V1;

        // Validation
        try {
            indicatorsServiceFacade.updateDimensionLocation(getServiceContextAdministrador(), uuid, parentTargetUuid, Long.valueOf(1));
            fail("Dimension not exists in indicator system version");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DIMENSION_NOT_FOUND_IN_INDICATORS_SYSTEM.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(parentTargetUuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(INDICATORS_SYSTEM_1_V2, e.getExceptionItems().get(0).getMessageParameters()[1]);
        }
    }

    @Test
    @Transactional
    public void testUpdateDimensionLocationErrorOrderIncorrect() throws Exception {

        try {
            indicatorsServiceFacade.updateDimensionLocation(getServiceContextAdministrador(), DIMENSION_1_INDICATORS_SYSTEM_1_V2, null, Long.MAX_VALUE);
            fail("order incorrect");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.DIMENSION_ORDER_IN_LEVEL, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testRetrieveIndicatorInstance() throws Exception {

        IndicatorInstanceDto indicatorInstanceDto = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContextAdministrador(), INDICATOR_INSTANCE_3_INDICATORS_SYSTEM_1_V2);

        assertNotNull(indicatorInstanceDto);
        assertEquals(INDICATOR_INSTANCE_3_INDICATORS_SYSTEM_1_V2, indicatorInstanceDto.getUuid());
        assertEquals("IndSys-1-v2-IInstance-3-code", indicatorInstanceDto.getCode());
        assertEquals(INDICATOR_2, indicatorInstanceDto.getIndicatorUuid());
        IndicatorsAsserts.assertEqualsInternationalString(indicatorInstanceDto.getTitle(), "es", "Título IndSys-1-v2-IInstance-3", "en", "Title IndSys-1-v2-IInstance-3");
        assertEquals(DIMENSION_1B_INDICATORS_SYSTEM_1_V2, indicatorInstanceDto.getParentUuid());
        assertEquals(IstacTimeGranularityEnum.YEARLY, indicatorInstanceDto.getTimeGranularity());
        assertEquals(0, indicatorInstanceDto.getTimeValues().size());
        assertEquals(GEOGRAPHICAL_GRANULARITY_1, indicatorInstanceDto.getGeographicalGranularityUuid());
        assertEquals(0, indicatorInstanceDto.getGeographicalValues().size());
        IndicatorsAsserts.assertEqualsDate("2011-05-05 01:02:04", indicatorInstanceDto.getCreatedDate());
        assertEquals("user1", indicatorInstanceDto.getCreatedBy());
        IndicatorsAsserts.assertEqualsDate("2011-06-06 05:06:07", indicatorInstanceDto.getLastUpdated());
        assertEquals("user2", indicatorInstanceDto.getLastUpdatedBy());
    }

    @Test
    @Transactional
    public void testRetrieveIndicatorInstanceErrorParameterRequired() throws Exception {

        String uuid = null;

        try {
            indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContextAdministrador(), uuid);
            fail("parameter required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.UUID, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testRetrieveIndicatorInstanceErrorNotExists() throws Exception {

        String uuid = NOT_EXISTS;

        try {
            indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContextAdministrador(), uuid);
            fail("No exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_INSTANCE_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testCreateIndicatorInstance() throws Exception {
        GeographicalValueBaseDto geoValue = new GeographicalValueBaseDto();
        geoValue.setUuid(GEOGRAPHICAL_VALUE_1);

        // Create indicator instance
        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setIndicatorUuid(INDICATOR_2);
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorInstanceDto.setParentUuid(null);
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(5));
        indicatorInstanceDto.setGeographicalValues(Arrays.asList(geoValue));
        indicatorInstanceDto.setTimeValues(Arrays.asList("2012"));

        String uuidIndicatorsSystem = INDICATORS_SYSTEM_1;
        IndicatorInstanceDto indicatorInstanceDtoCreated = indicatorsServiceFacade.createIndicatorInstance(getServiceContextAdministrador(), uuidIndicatorsSystem, indicatorInstanceDto);
        assertNotNull(indicatorInstanceDtoCreated.getUuid());
        assertNotNull(indicatorInstanceDtoCreated.getCode());

        // Retrieve indicator instance
        IndicatorInstanceDto indicatorInstanceDtoRetrieved = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContextAdministrador(), indicatorInstanceDtoCreated.getUuid());
        IndicatorsAsserts.assertEqualsIndicatorInstance(indicatorInstanceDto, indicatorInstanceDtoRetrieved);

        // Validate new structure
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContextAdministrador(), INDICATORS_SYSTEM_1,
                IndicatorsDataBaseTest.SECOND_VERSION);
        assertEquals(5, indicatorsSystemStructureDto.getElements().size());
        assertEquals(INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(0).getIndicatorInstance().getUuid());
        assertEquals(Long.valueOf(1), indicatorsSystemStructureDto.getElements().get(0).getOrderInLevel());
        assertEquals(DIMENSION_1_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(1).getDimension().getUuid());
        assertEquals(Long.valueOf(2), indicatorsSystemStructureDto.getElements().get(1).getOrderInLevel());
        assertEquals(INDICATOR_INSTANCE_2_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(2).getIndicatorInstance().getUuid());
        assertEquals(Long.valueOf(3), indicatorsSystemStructureDto.getElements().get(2).getOrderInLevel());
        assertEquals(DIMENSION_2_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(3).getDimension().getUuid());
        assertEquals(Long.valueOf(4), indicatorsSystemStructureDto.getElements().get(3).getOrderInLevel());
        assertEquals(indicatorInstanceDtoCreated.getUuid(), indicatorsSystemStructureDto.getElements().get(4).getIndicatorInstance().getUuid());
        assertEquals(Long.valueOf(5), indicatorsSystemStructureDto.getElements().get(4).getOrderInLevel());
    }

    @Test
    @Transactional
    public void testCreateIndicatorInstanceMultiGeo() throws Exception {
        GeographicalValueBaseDto geoValue1 = new GeographicalValueBaseDto();
        geoValue1.setUuid(GEOGRAPHICAL_VALUE_1);

        GeographicalValueBaseDto geoValue2 = new GeographicalValueBaseDto();
        geoValue2.setUuid(GEOGRAPHICAL_VALUE_2);

        // Create indicator instance
        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setIndicatorUuid(INDICATOR_2);
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorInstanceDto.setParentUuid(null);
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(5));
        indicatorInstanceDto.setGeographicalValues(Arrays.asList(geoValue1, geoValue2));
        indicatorInstanceDto.setTimeValues(Arrays.asList("2012"));

        String uuidIndicatorsSystem = INDICATORS_SYSTEM_1;
        IndicatorInstanceDto indicatorInstanceDtoCreated = indicatorsServiceFacade.createIndicatorInstance(getServiceContextAdministrador(), uuidIndicatorsSystem, indicatorInstanceDto);
        assertNotNull(indicatorInstanceDtoCreated.getUuid());
        assertNotNull(indicatorInstanceDtoCreated.getCode());

        // Retrieve indicator instance
        IndicatorInstanceDto indicatorInstanceDtoRetrieved = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContextAdministrador(), indicatorInstanceDtoCreated.getUuid());
        IndicatorsAsserts.assertEqualsIndicatorInstance(indicatorInstanceDto, indicatorInstanceDtoRetrieved);

        // Validate new structure
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContextAdministrador(), INDICATORS_SYSTEM_1,
                IndicatorsDataBaseTest.SECOND_VERSION);
        assertEquals(5, indicatorsSystemStructureDto.getElements().size());
        assertEquals(INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(0).getIndicatorInstance().getUuid());
        assertEquals(Long.valueOf(1), indicatorsSystemStructureDto.getElements().get(0).getOrderInLevel());
        assertEquals(DIMENSION_1_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(1).getDimension().getUuid());
        assertEquals(Long.valueOf(2), indicatorsSystemStructureDto.getElements().get(1).getOrderInLevel());
        assertEquals(INDICATOR_INSTANCE_2_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(2).getIndicatorInstance().getUuid());
        assertNotNull(indicatorsSystemStructureDto.getElements().get(2).getIndicatorInstance().getGeographicalValues());
        assertEquals(Long.valueOf(3), indicatorsSystemStructureDto.getElements().get(2).getOrderInLevel());
        assertEquals(DIMENSION_2_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(3).getDimension().getUuid());
        assertEquals(Long.valueOf(4), indicatorsSystemStructureDto.getElements().get(3).getOrderInLevel());
        assertEquals(indicatorInstanceDtoCreated.getUuid(), indicatorsSystemStructureDto.getElements().get(4).getIndicatorInstance().getUuid());
        assertEquals(Long.valueOf(5), indicatorsSystemStructureDto.getElements().get(4).getOrderInLevel());
    }

    @Test
    @Transactional
    public void testCreateIndicatorInstanceMultiTimeValues() throws Exception {
        GeographicalValueBaseDto geoValue1 = new GeographicalValueBaseDto();
        geoValue1.setUuid(GEOGRAPHICAL_VALUE_1);

        // Create indicator instance
        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setIndicatorUuid(INDICATOR_2);
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorInstanceDto.setParentUuid(null);
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(5));
        indicatorInstanceDto.setGeographicalValues(Arrays.asList(geoValue1));
        indicatorInstanceDto.setTimeValues(Arrays.asList("2012", "2011", "2010"));

        String uuidIndicatorsSystem = INDICATORS_SYSTEM_1;
        IndicatorInstanceDto indicatorInstanceDtoCreated = indicatorsServiceFacade.createIndicatorInstance(getServiceContextAdministrador(), uuidIndicatorsSystem, indicatorInstanceDto);
        assertNotNull(indicatorInstanceDtoCreated.getUuid());
        assertNotNull(indicatorInstanceDtoCreated.getCode());

        // Retrieve indicator instance
        IndicatorInstanceDto indicatorInstanceDtoRetrieved = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContextAdministrador(), indicatorInstanceDtoCreated.getUuid());
        IndicatorsAsserts.assertEqualsIndicatorInstance(indicatorInstanceDto, indicatorInstanceDtoRetrieved);

        // Validate new structure
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContextAdministrador(), INDICATORS_SYSTEM_1,
                IndicatorsDataBaseTest.SECOND_VERSION);
        assertEquals(5, indicatorsSystemStructureDto.getElements().size());
        assertEquals(INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(0).getIndicatorInstance().getUuid());
        assertEquals(Long.valueOf(1), indicatorsSystemStructureDto.getElements().get(0).getOrderInLevel());
        assertEquals(DIMENSION_1_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(1).getDimension().getUuid());
        assertEquals(Long.valueOf(2), indicatorsSystemStructureDto.getElements().get(1).getOrderInLevel());
        assertEquals(INDICATOR_INSTANCE_2_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(2).getIndicatorInstance().getUuid());
        assertNotNull(indicatorsSystemStructureDto.getElements().get(2).getIndicatorInstance().getGeographicalValues());
        assertEquals(Long.valueOf(3), indicatorsSystemStructureDto.getElements().get(2).getOrderInLevel());
        assertEquals(DIMENSION_2_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(3).getDimension().getUuid());
        assertEquals(Long.valueOf(4), indicatorsSystemStructureDto.getElements().get(3).getOrderInLevel());
        assertEquals(indicatorInstanceDtoCreated.getUuid(), indicatorsSystemStructureDto.getElements().get(4).getIndicatorInstance().getUuid());
        assertEquals(Long.valueOf(5), indicatorsSystemStructureDto.getElements().get(4).getOrderInLevel());
    }

    @Test
    @Transactional
    public void testCreateIndicatorInstanceMultiGeoDuplicated() throws Exception {
        GeographicalValueBaseDto geoValue1 = new GeographicalValueBaseDto();
        geoValue1.setUuid(GEOGRAPHICAL_VALUE_1);

        GeographicalValueBaseDto geoValue2 = new GeographicalValueBaseDto();
        geoValue2.setUuid(GEOGRAPHICAL_VALUE_1);

        // Create indicator instance
        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setIndicatorUuid(INDICATOR_2);
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorInstanceDto.setParentUuid(null);
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(5));
        indicatorInstanceDto.setGeographicalValues(Arrays.asList(geoValue1, geoValue2));
        indicatorInstanceDto.setTimeValues(Arrays.asList("2012"));

        String uuidIndicatorsSystem = INDICATORS_SYSTEM_1;
        IndicatorInstanceDto indicatorInstanceDtoCreated = indicatorsServiceFacade.createIndicatorInstance(getServiceContextAdministrador(), uuidIndicatorsSystem, indicatorInstanceDto);
        assertNotNull(indicatorInstanceDtoCreated.getUuid());
        assertNotNull(indicatorInstanceDtoCreated.getCode());

        // Retrieve indicator instance
        IndicatorInstanceDto indicatorInstanceDtoRetrieved = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContextAdministrador(), indicatorInstanceDtoCreated.getUuid());
        IndicatorsAsserts.assertEqualsIndicatorInstance(indicatorInstanceDto, indicatorInstanceDtoRetrieved);
        assertEquals(1, indicatorInstanceDtoRetrieved.getGeographicalValues().size());

        // Validate new structure
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContextAdministrador(), INDICATORS_SYSTEM_1,
                IndicatorsDataBaseTest.SECOND_VERSION);
        assertEquals(5, indicatorsSystemStructureDto.getElements().size());
        assertEquals(INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(0).getIndicatorInstance().getUuid());
        assertEquals(Long.valueOf(1), indicatorsSystemStructureDto.getElements().get(0).getOrderInLevel());
        assertEquals(DIMENSION_1_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(1).getDimension().getUuid());
        assertEquals(Long.valueOf(2), indicatorsSystemStructureDto.getElements().get(1).getOrderInLevel());
        assertEquals(INDICATOR_INSTANCE_2_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(2).getIndicatorInstance().getUuid());
        assertNotNull(indicatorsSystemStructureDto.getElements().get(2).getIndicatorInstance().getGeographicalValues());
        assertEquals(Long.valueOf(3), indicatorsSystemStructureDto.getElements().get(2).getOrderInLevel());
        assertEquals(DIMENSION_2_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(3).getDimension().getUuid());
        assertEquals(Long.valueOf(4), indicatorsSystemStructureDto.getElements().get(3).getOrderInLevel());
        assertEquals(indicatorInstanceDtoCreated.getUuid(), indicatorsSystemStructureDto.getElements().get(4).getIndicatorInstance().getUuid());
        assertEquals(Long.valueOf(5), indicatorsSystemStructureDto.getElements().get(4).getOrderInLevel());
    }

    @Test
    @DirtyDatabase
    public void testCreateIndicatorInstanceWithOrderInMiddle() throws Exception {

        // Create indicator instance
        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setIndicatorUuid(INDICATOR_2);
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorInstanceDto.setParentUuid(null);
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(3));
        indicatorInstanceDto.setGeographicalGranularityUuid(GEOGRAPHICAL_GRANULARITY_1);
        indicatorInstanceDto.setTimeGranularity(IstacTimeGranularityEnum.DAILY);

        String uuidIndicatorsSystem = INDICATORS_SYSTEM_1;
        IndicatorInstanceDto indicatorInstanceDtoCreated = indicatorsServiceFacade.createIndicatorInstance(getServiceContextAdministrador(), uuidIndicatorsSystem, indicatorInstanceDto);
        assertNotNull(indicatorInstanceDtoCreated.getUuid());

        // Retrieve indicator instance
        IndicatorInstanceDto indicatorInstanceDtoRetrieved = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContextAdministrador(), indicatorInstanceDtoCreated.getUuid());
        IndicatorsAsserts.assertEqualsIndicatorInstance(indicatorInstanceDto, indicatorInstanceDtoRetrieved);

        // Validate new structure
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContextAdministrador(), INDICATORS_SYSTEM_1,
                IndicatorsDataBaseTest.SECOND_VERSION);
        assertEquals(5, indicatorsSystemStructureDto.getElements().size());
        assertEquals(INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(0).getIndicatorInstance().getUuid());
        assertEquals(Long.valueOf(1), indicatorsSystemStructureDto.getElements().get(0).getOrderInLevel());
        assertEquals(DIMENSION_1_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(1).getDimension().getUuid());
        assertEquals(Long.valueOf(2), indicatorsSystemStructureDto.getElements().get(1).getOrderInLevel());
        assertEquals(indicatorInstanceDtoCreated.getUuid(), indicatorsSystemStructureDto.getElements().get(2).getIndicatorInstance().getUuid());
        assertEquals(Long.valueOf(3), indicatorsSystemStructureDto.getElements().get(2).getOrderInLevel());
        assertEquals(INDICATOR_INSTANCE_2_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(3).getIndicatorInstance().getUuid());
        assertEquals(Long.valueOf(4), indicatorsSystemStructureDto.getElements().get(3).getOrderInLevel());
        assertEquals(DIMENSION_2_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(4).getDimension().getUuid());
        assertEquals(Long.valueOf(5), indicatorsSystemStructureDto.getElements().get(4).getOrderInLevel());

    }

    @Test
    @Transactional
    public void testCreateIndicatorInstanceInDimension() throws Exception {
        GeographicalValueBaseDto geoValue = new GeographicalValueBaseDto();
        geoValue.setUuid(GEOGRAPHICAL_VALUE_1);

        String parentUuid = DIMENSION_1_INDICATORS_SYSTEM_1_V2;

        // Create indicator instance
        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setIndicatorUuid("Indicator-1");
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorInstanceDto.setParentUuid(parentUuid);
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(3));
        indicatorInstanceDto.setGeographicalValues(Arrays.asList(geoValue));
        indicatorInstanceDto.setTimeValues(Arrays.asList("2012"));

        String uuidIndicatorsSystem = INDICATORS_SYSTEM_1;
        IndicatorInstanceDto indicatorInstanceDtoCreated = indicatorsServiceFacade.createIndicatorInstance(getServiceContextAdministrador(), uuidIndicatorsSystem, indicatorInstanceDto);
        assertNotNull(indicatorInstanceDtoCreated.getUuid());

        // Retrieve indicatorInstance
        IndicatorInstanceDto indicatorInstanceDtoRetrieved = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContextAdministrador(), indicatorInstanceDtoCreated.getUuid());
        IndicatorsAsserts.assertEqualsIndicatorInstance(indicatorInstanceDto, indicatorInstanceDtoRetrieved);
        assertEquals(parentUuid, indicatorInstanceDtoRetrieved.getParentUuid());

        // Validate new structure
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContextAdministrador(), INDICATORS_SYSTEM_1,
                IndicatorsDataBaseTest.SECOND_VERSION);
        assertEquals(4, indicatorsSystemStructureDto.getElements().size());
        ElementLevelDto elementLevelDto = indicatorsSystemStructureDto.getElements().get(1);
        assertEquals(parentUuid, elementLevelDto.getDimension().getUuid());
        assertEquals(3, elementLevelDto.getSubelements().size());
        // Children (new indicator instance is created as children)
        assertEquals(DIMENSION_1A_INDICATORS_SYSTEM_1_V2, elementLevelDto.getSubelements().get(0).getDimension().getUuid());
        assertEquals(Long.valueOf(1), elementLevelDto.getSubelements().get(0).getOrderInLevel());
        assertEquals(DIMENSION_1B_INDICATORS_SYSTEM_1_V2, elementLevelDto.getSubelements().get(1).getDimension().getUuid());
        assertEquals(Long.valueOf(2), elementLevelDto.getSubelements().get(1).getOrderInLevel());
        assertEquals(indicatorInstanceDtoCreated.getUuid(), elementLevelDto.getSubelements().get(2).getIndicatorInstance().getUuid());
        assertEquals(Long.valueOf(3), elementLevelDto.getSubelements().get(2).getOrderInLevel());
    }

    @Test
    @DirtyDatabase
    public void testCreateIndicatorInstanceIndicatorDuplicatedInSameRootLevel() throws Exception {
        GeographicalValueBaseDto geoValue = new GeographicalValueBaseDto();
        geoValue.setUuid(GEOGRAPHICAL_VALUE_1);

        // Create indicator instance
        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setIndicatorUuid("Indicator-1");
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorInstanceDto.setParentUuid(null);
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(3));
        indicatorInstanceDto.setGeographicalValues(Arrays.asList(geoValue));
        indicatorInstanceDto.setTimeValues(Arrays.asList("2012"));

        // Root level
        IndicatorInstanceDto indicatorInstanceDtoCreated = indicatorsServiceFacade.createIndicatorInstance(getServiceContextAdministrador(), INDICATORS_SYSTEM_1, indicatorInstanceDto);
        assertNotNull(indicatorInstanceDtoCreated.getUuid());

        // Retrieve indicatorInstance
        IndicatorInstanceDto indicatorInstanceDtoRetrieved = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContextAdministrador(), indicatorInstanceDtoCreated.getUuid());
        IndicatorsAsserts.assertEqualsIndicatorInstance(indicatorInstanceDto, indicatorInstanceDtoRetrieved);
        assertNull(indicatorInstanceDtoRetrieved.getParentUuid());

        // Validate new structure
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContextAdministrador(), INDICATORS_SYSTEM_1,
                IndicatorsDataBaseTest.SECOND_VERSION);
        assertEquals(5, indicatorsSystemStructureDto.getElements().size());

        {
            ElementLevelDto elementLevelDto = indicatorsSystemStructureDto.getElements().get(0);
            assertNull(elementLevelDto.getParentUuid());
            assertNotNull(elementLevelDto.getIndicatorInstance());
            assertEquals(INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2, elementLevelDto.getIndicatorInstance().getUuid());
            assertEquals(Long.valueOf(1), elementLevelDto.getOrderInLevel());

        }
        {
            ElementLevelDto elementLevelDto = indicatorsSystemStructureDto.getElements().get(1);
            assertNull(elementLevelDto.getParentUuid());
            assertNotNull(elementLevelDto.getDimension());
            assertEquals(DIMENSION_1_INDICATORS_SYSTEM_1_V2, elementLevelDto.getDimension().getUuid());
            assertEquals(Long.valueOf(2), elementLevelDto.getOrderInLevel());
        }
        {
            ElementLevelDto elementLevelDto = indicatorsSystemStructureDto.getElements().get(2);
            assertNull(elementLevelDto.getParentUuid());
            assertNotNull(elementLevelDto.getIndicatorInstance());
            assertEquals(indicatorInstanceDtoCreated.getUuid(), elementLevelDto.getIndicatorInstance().getUuid());
            assertEquals(Long.valueOf(3), elementLevelDto.getOrderInLevel());
        }
        {
            ElementLevelDto elementLevelDto = indicatorsSystemStructureDto.getElements().get(3);
            assertNull(elementLevelDto.getParentUuid());
            assertNotNull(elementLevelDto.getIndicatorInstance());
            assertEquals(INDICATOR_INSTANCE_2_INDICATORS_SYSTEM_1_V2, elementLevelDto.getIndicatorInstance().getUuid());
            assertEquals(Long.valueOf(4), elementLevelDto.getOrderInLevel());
        }
        {
            ElementLevelDto elementLevelDto = indicatorsSystemStructureDto.getElements().get(4);
            assertNull(elementLevelDto.getParentUuid());
            assertNotNull(elementLevelDto.getDimension());
            assertEquals(DIMENSION_2_INDICATORS_SYSTEM_1_V2, elementLevelDto.getDimension().getUuid());
            assertEquals(Long.valueOf(5), elementLevelDto.getOrderInLevel());
        }
    }

    @Test
    @DirtyDatabase
    public void testCreateIndicatorInstanceIndicatorDuplicatedInSameDimensionLevel() throws Exception {
        GeographicalValueBaseDto geoValue = new GeographicalValueBaseDto();
        geoValue.setUuid(GEOGRAPHICAL_VALUE_1);

        // Create indicator instance
        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setIndicatorUuid("Indicator-1");
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorInstanceDto.setParentUuid(DIMENSION_2_INDICATORS_SYSTEM_1_V2);
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(1));
        indicatorInstanceDto.setGeographicalValues(Arrays.asList(geoValue));
        indicatorInstanceDto.setTimeValues(Arrays.asList("2012"));

        // Root level
        IndicatorInstanceDto indicatorInstanceDtoCreated = indicatorsServiceFacade.createIndicatorInstance(getServiceContextAdministrador(), INDICATORS_SYSTEM_1, indicatorInstanceDto);
        assertNotNull(indicatorInstanceDtoCreated.getUuid());

        // Retrieve indicatorInstance
        IndicatorInstanceDto indicatorInstanceDtoRetrieved = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContextAdministrador(), indicatorInstanceDtoCreated.getUuid());
        IndicatorsAsserts.assertEqualsIndicatorInstance(indicatorInstanceDto, indicatorInstanceDtoRetrieved);
        assertEquals(DIMENSION_2_INDICATORS_SYSTEM_1_V2, indicatorInstanceDtoRetrieved.getParentUuid());

        // Validate new structure
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContextAdministrador(), INDICATORS_SYSTEM_1,
                IndicatorsDataBaseTest.SECOND_VERSION);
        assertEquals(4, indicatorsSystemStructureDto.getElements().size());

        {
            ElementLevelDto elementLevelDto = indicatorsSystemStructureDto.getElements().get(3);
            assertNull(elementLevelDto.getParentUuid());
            assertNotNull(elementLevelDto.getDimension());
            assertEquals(DIMENSION_2_INDICATORS_SYSTEM_1_V2, elementLevelDto.getDimension().getUuid());

            assertEquals(1, elementLevelDto.getSubelements().size());
            ElementLevelDto subElementLevelDto = elementLevelDto.getSubelements().get(0);
            assertEquals(elementLevelDto.getDimension().getUuid(), subElementLevelDto.getParentUuid());
            assertNotNull(subElementLevelDto.getIndicatorInstance());
            assertEquals(indicatorInstanceDtoCreated.getUuid(), subElementLevelDto.getIndicatorInstance().getUuid());
        }
    }

    @Test
    @DirtyDatabase
    public void testCreateIndicatorInstanceInDimensionInMiddle() throws Exception {

        String parentUuid = DIMENSION_1_INDICATORS_SYSTEM_1_V2;

        // Create indicator instance
        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setIndicatorUuid("Indicator-1");
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorInstanceDto.setParentUuid(parentUuid);
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(2));
        indicatorInstanceDto.setTimeValues(Arrays.asList("2012"));

        String uuidIndicatorsSystem = INDICATORS_SYSTEM_1;
        IndicatorInstanceDto indicatorInstanceDtoCreated = indicatorsServiceFacade.createIndicatorInstance(getServiceContextAdministrador(), uuidIndicatorsSystem, indicatorInstanceDto);
        assertNotNull(indicatorInstanceDtoCreated.getUuid());

        // Retrieve indicatorInstance
        IndicatorInstanceDto indicatorInstanceDtoRetrieved = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContextAdministrador(), indicatorInstanceDtoCreated.getUuid());
        IndicatorsAsserts.assertEqualsIndicatorInstance(indicatorInstanceDto, indicatorInstanceDtoRetrieved);
        assertEquals(parentUuid, indicatorInstanceDtoRetrieved.getParentUuid());

        // Validate new structure
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContextAdministrador(), INDICATORS_SYSTEM_1,
                IndicatorsDataBaseTest.SECOND_VERSION);
        assertEquals(4, indicatorsSystemStructureDto.getElements().size());
        ElementLevelDto elementLevelDto = indicatorsSystemStructureDto.getElements().get(1);
        assertEquals(parentUuid, elementLevelDto.getDimension().getUuid());
        assertEquals(3, elementLevelDto.getSubelements().size());
        // Children (new indicator instance is created as children)
        assertEquals(DIMENSION_1A_INDICATORS_SYSTEM_1_V2, elementLevelDto.getSubelements().get(0).getDimension().getUuid());
        assertEquals(Long.valueOf(1), elementLevelDto.getSubelements().get(0).getOrderInLevel());
        assertEquals(indicatorInstanceDtoCreated.getUuid(), elementLevelDto.getSubelements().get(1).getIndicatorInstance().getUuid());
        assertEquals(Long.valueOf(2), elementLevelDto.getSubelements().get(1).getOrderInLevel());
        assertEquals(DIMENSION_1B_INDICATORS_SYSTEM_1_V2, elementLevelDto.getSubelements().get(2).getDimension().getUuid());
        assertEquals(Long.valueOf(3), elementLevelDto.getSubelements().get(2).getOrderInLevel());

    }

    @Test
    @DirtyDatabase
    public void testCreateIndicatorInstanceInSubDimension() throws Exception {

        String parentUuid = DIMENSION_1B_INDICATORS_SYSTEM_1_V2;

        // Create indicator instance
        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setIndicatorUuid("Indicator-1");
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorInstanceDto.setParentUuid(parentUuid);
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(2));
        indicatorInstanceDto.setTimeValues(Arrays.asList("2012"));

        String uuidIndicatorsSystem = INDICATORS_SYSTEM_1;
        IndicatorInstanceDto indicatorInstanceDtoCreated = indicatorsServiceFacade.createIndicatorInstance(getServiceContextAdministrador(), uuidIndicatorsSystem, indicatorInstanceDto);
        assertNotNull(indicatorInstanceDtoCreated.getUuid());

        // Retrieve indicatorInstance
        IndicatorInstanceDto indicatorInstanceDtoRetrieved = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContextAdministrador(), indicatorInstanceDtoCreated.getUuid());
        IndicatorsAsserts.assertEqualsIndicatorInstance(indicatorInstanceDto, indicatorInstanceDtoRetrieved);
        assertEquals(parentUuid, indicatorInstanceDtoRetrieved.getParentUuid());

        // Validate new structure
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContextAdministrador(), INDICATORS_SYSTEM_1,
                IndicatorsDataBaseTest.SECOND_VERSION);
        assertEquals(4, indicatorsSystemStructureDto.getElements().size());
        ElementLevelDto elementLevelDto = indicatorsSystemStructureDto.getElements().get(1).getSubelements().get(1);
        assertEquals(DIMENSION_1B_INDICATORS_SYSTEM_1_V2, elementLevelDto.getDimension().getUuid());
        assertEquals(3, elementLevelDto.getSubelements().size());
        assertEquals(DIMENSION_1BA_INDICATORS_SYSTEM_1_V2, elementLevelDto.getSubelements().get(0).getDimension().getUuid());
        assertEquals(indicatorInstanceDtoCreated.getUuid(), elementLevelDto.getSubelements().get(1).getIndicatorInstance().getUuid());
        assertEquals(INDICATOR_INSTANCE_3_INDICATORS_SYSTEM_1_V2, elementLevelDto.getSubelements().get(2).getIndicatorInstance().getUuid());

    }

    @Test
    @Transactional
    public void testCreateIndicatorInstanceErrorParametersRequired() throws Exception {

        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setTitle(null);
        indicatorInstanceDto.setIndicatorUuid(null);
        indicatorInstanceDto.setParentUuid(null);
        indicatorInstanceDto.setOrderInLevel(null);
        indicatorInstanceDto.setTimeGranularity(null);
        try {
            indicatorsServiceFacade.createIndicatorInstance(getServiceContextAdministrador(), INDICATORS_SYSTEM_1, indicatorInstanceDto);
            fail("parameters required");
        } catch (MetamacException e) {
            assertEquals(4, e.getExceptionItems().size());

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.INDICATOR_INSTANCE_TITLE, e.getExceptionItems().get(0).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(1).getCode());
            assertEquals(1, e.getExceptionItems().get(1).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.INDICATOR_INSTANCE_INDICATOR_UUID, e.getExceptionItems().get(1).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(2).getCode());
            assertEquals(2, e.getExceptionItems().get(2).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.INDICATOR_INSTANCE_TIME_GRANULARITY, e.getExceptionItems().get(2).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.INDICATOR_INSTANCE_TIME_VALUES, e.getExceptionItems().get(2).getMessageParameters()[1]);

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(3).getCode());
            assertEquals(1, e.getExceptionItems().get(3).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.INDICATOR_INSTANCE_ORDER_IN_LEVEL, e.getExceptionItems().get(3).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testCreateIndicatorInstanceErrorOrderIncorrect() throws Exception {
        GeographicalValueBaseDto geoValue = new GeographicalValueBaseDto();
        geoValue.setUuid(GEOGRAPHICAL_VALUE_1);

        // Create indicatorInstance
        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setIndicatorUuid(INDICATOR_2);
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorInstanceDto.setGeographicalValues(Arrays.asList(geoValue));
        indicatorInstanceDto.setTimeValues(Arrays.asList("2012"));
        indicatorInstanceDto.setParentUuid(null);
        indicatorInstanceDto.setOrderInLevel(Long.MAX_VALUE);

        try {
            indicatorsServiceFacade.createIndicatorInstance(getServiceContextAdministrador(), INDICATORS_SYSTEM_1, indicatorInstanceDto);
            fail("order incorrect");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.INDICATOR_INSTANCE_ORDER_IN_LEVEL, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testCreateIndicatorInstanceErrorOrderIncorrectNegative() throws Exception {
        GeographicalValueBaseDto geoValue = new GeographicalValueBaseDto();
        geoValue.setUuid(GEOGRAPHICAL_VALUE_1);

        // Create indicatorInstance
        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setIndicatorUuid("Indicator-1");
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorInstanceDto.setGeographicalValues(Arrays.asList(geoValue));
        indicatorInstanceDto.setTimeValues(Arrays.asList("2012"));
        indicatorInstanceDto.setParentUuid(null);
        indicatorInstanceDto.setOrderInLevel(Long.MIN_VALUE);

        try {
            indicatorsServiceFacade.createIndicatorInstance(getServiceContextAdministrador(), INDICATORS_SYSTEM_1, indicatorInstanceDto);
            fail("order incorrect");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.INDICATOR_INSTANCE_ORDER_IN_LEVEL, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testCreateIndicatorInstanceErrorTimeValueIncorrect() throws Exception {
        GeographicalValueBaseDto geoValue = new GeographicalValueBaseDto();
        geoValue.setUuid(GEOGRAPHICAL_VALUE_1);

        // Create indicatorInstance
        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setIndicatorUuid(INDICATOR_2);
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorInstanceDto.setGeographicalValues(Arrays.asList(geoValue));
        indicatorInstanceDto.setParentUuid(null);
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(1));

        indicatorInstanceDto.setTimeValues(Arrays.asList("2012a"));

        try {
            indicatorsServiceFacade.createIndicatorInstance(getServiceContextAdministrador(), INDICATORS_SYSTEM_1, indicatorInstanceDto);
            fail("Time value incorrect");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.INDICATOR_INSTANCE_TIME_VALUES, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testCreateIndicatorInstanceErrorGeographicValueNotExists() throws Exception {
        GeographicalValueBaseDto geoValue = new GeographicalValueBaseDto();
        geoValue.setUuid(NOT_EXISTS);

        // Create indicatorInstance
        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setIndicatorUuid(INDICATOR_2);
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorInstanceDto.setTimeValues(Arrays.asList("2012"));
        indicatorInstanceDto.setGeographicalValues(Arrays.asList(geoValue));
        indicatorInstanceDto.setParentUuid(null);
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(1));

        try {
            indicatorsServiceFacade.createIndicatorInstance(getServiceContextAdministrador(), INDICATORS_SYSTEM_1, indicatorInstanceDto);
            fail("Geographic value not exits");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.GEOGRAPHICAL_VALUE_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(indicatorInstanceDto.getGeographicalValues().get(0).getUuid(), e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testCreateIndicatorInstanceErrorIndicatorNotExists() throws Exception {
        GeographicalValueBaseDto geoValue = new GeographicalValueBaseDto();
        geoValue.setUuid(GEOGRAPHICAL_VALUE_1);

        // Create indicator instance
        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setIndicatorUuid(NOT_EXISTS);
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorInstanceDto.setParentUuid(null);
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(1));
        indicatorInstanceDto.setGeographicalValues(Arrays.asList(geoValue));
        indicatorInstanceDto.setTimeValues(Arrays.asList("2012"));

        // Root level
        try {
            indicatorsServiceFacade.createIndicatorInstance(getServiceContextAdministrador(), INDICATORS_SYSTEM_1, indicatorInstanceDto);
            fail("Indicator not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(indicatorInstanceDto.getIndicatorUuid(), e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testCreateIndicatorInstanceErrorIndicatorsSystemNotExists() throws Exception {
        GeographicalValueBaseDto geoValue = new GeographicalValueBaseDto();
        geoValue.setUuid(GEOGRAPHICAL_VALUE_1);

        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setIndicatorUuid("Indicator-1");
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorInstanceDto.setGeographicalValues(Arrays.asList(geoValue));
        indicatorInstanceDto.setTimeValues(Arrays.asList("2012"));
        indicatorInstanceDto.setParentUuid(null);
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(1));
        try {
            indicatorsServiceFacade.createIndicatorInstance(getServiceContextAdministrador(), NOT_EXISTS, indicatorInstanceDto);
            fail("Indicators system not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testCreateIndicatorInstanceErrorIndicatorsSystemHasNotVersionInProduction() throws Exception {
        GeographicalValueBaseDto geoValue = new GeographicalValueBaseDto();
        geoValue.setUuid(GEOGRAPHICAL_VALUE_1);

        String indicatorsSystemUuid = INDICATORS_SYSTEM_3;
        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setIndicatorUuid("Indicator-1");
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorInstanceDto.setGeographicalValues(Arrays.asList(geoValue));
        indicatorInstanceDto.setTimeValues(Arrays.asList("2012"));
        indicatorInstanceDto.setParentUuid(null);
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(1));

        try {
            indicatorsServiceFacade.createIndicatorInstance(getServiceContextAdministrador(), indicatorsSystemUuid, indicatorInstanceDto);
            fail("Indicators system not in production");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_IN_PRODUCTION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(indicatorsSystemUuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testCreateIndicatorInstanceErrorDimensionNotExists() throws Exception {
        GeographicalValueBaseDto geoValue = new GeographicalValueBaseDto();
        geoValue.setUuid(GEOGRAPHICAL_VALUE_1);

        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setIndicatorUuid("Indicator-1");
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorInstanceDto.setGeographicalValues(Arrays.asList(geoValue));
        indicatorInstanceDto.setTimeValues(Arrays.asList("2012"));
        indicatorInstanceDto.setParentUuid(DIMENSION_NOT_EXISTS);
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(1));

        try {
            indicatorsServiceFacade.createIndicatorInstance(getServiceContextAdministrador(), INDICATORS_SYSTEM_1, indicatorInstanceDto);
            fail("Dimension not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DIMENSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(DIMENSION_NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testCreateIndicatorInstanceErrorDimensionNotExistsInIndicatorsSystem() throws Exception {
        GeographicalValueBaseDto geoValue = new GeographicalValueBaseDto();
        geoValue.setUuid(GEOGRAPHICAL_VALUE_1);

        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setIndicatorUuid("Indicator-1");
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorInstanceDto.setGeographicalValues(Arrays.asList(geoValue));
        indicatorInstanceDto.setTimeValues(Arrays.asList("2012"));
        indicatorInstanceDto.setParentUuid(DIMENSION_1_INDICATORS_SYSTEM_1_V2);
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(1));

        try {
            indicatorsServiceFacade.createIndicatorInstance(getServiceContextAdministrador(), INDICATORS_SYSTEM_2, indicatorInstanceDto);
            fail("dimension not exists in indicators system");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DIMENSION_NOT_FOUND_IN_INDICATORS_SYSTEM.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(DIMENSION_1_INDICATORS_SYSTEM_1_V2, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(INDICATORS_SYSTEM_2, e.getExceptionItems().get(0).getMessageParameters()[1]);
        }
    }

    @Test
    @Transactional
    public void testDeleteIndicatorInstance() throws Exception {

        String uuid = INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2;

        // Retrieve
        IndicatorInstanceDto indicatorInstanceDto = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContextAdministrador(), uuid);
        assertEquals(Long.valueOf(1), indicatorInstanceDto.getOrderInLevel());

        // Delete indicatorInstance
        indicatorsServiceFacade.deleteIndicatorInstance(getServiceContextAdministrador(), uuid);

        // Validation
        try {
            indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContextAdministrador(), uuid);
            fail("Indicator instance deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_INSTANCE_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }

        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), indicatorInstanceDto.getIndicatorUuid(), null);
        assertNotNull(indicatorDto);

        // Checks orders of other elements are updated
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContextAdministrador(), INDICATORS_SYSTEM_1,
                IndicatorsDataBaseTest.SECOND_VERSION);
        assertEquals(3, indicatorsSystemStructureDto.getElements().size());
        assertEquals(DIMENSION_1_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(0).getDimension().getUuid());
        assertEquals(Long.valueOf(1), indicatorsSystemStructureDto.getElements().get(0).getOrderInLevel());
        assertEquals(INDICATOR_INSTANCE_2_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(1).getIndicatorInstance().getUuid());
        assertEquals(Long.valueOf(2), indicatorsSystemStructureDto.getElements().get(1).getOrderInLevel());
        assertEquals(DIMENSION_2_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(2).getDimension().getUuid());
        assertEquals(Long.valueOf(3), indicatorsSystemStructureDto.getElements().get(2).getOrderInLevel());
    }

    @Test
    @Transactional
    public void testDeleteIndicatorInstanceErrorIndicatorsSystemVersionPublished() throws Exception {

        try {
            indicatorsServiceFacade.deleteIndicatorInstance(getServiceContextAdministrador(), INDICATOR_INSTANCE_2_INDICATORS_SYSTEM_3_V1);
            fail("Indicators system not in production");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(INDICATORS_SYSTEM_3, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_DRAFT, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_VALIDATION_REJECTED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
            assertEquals(ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_PRODUCTION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[2]);
            assertEquals(ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_DIFFUSION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[3]);
        }
    }

    @Test
    @Transactional
    public void testDeleteIndicatorInstanceErrorNotExists() throws Exception {

        String uuid = NOT_EXISTS;

        // Validation
        try {
            indicatorsServiceFacade.deleteIndicatorInstance(getServiceContextAdministrador(), uuid);
            fail("IndicatorInstance not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_INSTANCE_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testUpdateIndicatorInstance() throws Exception {

        String uuid = INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2;
        IndicatorInstanceDto indicatorInstanceDto = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContextAdministrador(), uuid);
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorInstanceDto.setGeographicalGranularityUuid(GEOGRAPHICAL_GRANULARITY_1);
        indicatorInstanceDto.setGeographicalValues(null);

        // Update
        IndicatorInstanceDto indicatorInstanceDtoUpdated = indicatorsServiceFacade.updateIndicatorInstance(getServiceContextAdministrador(), indicatorInstanceDto);

        // Validation
        IndicatorsAsserts.assertEqualsIndicatorInstance(indicatorInstanceDto, indicatorInstanceDtoUpdated);
        indicatorInstanceDtoUpdated = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContextAdministrador(), uuid);
        IndicatorsAsserts.assertEqualsIndicatorInstance(indicatorInstanceDto, indicatorInstanceDtoUpdated);
    }

    @Test
    @Transactional
    public void testUpdateIndicatorInstanceErrorChangeMetadataUnmodifiable() throws Exception {

        IndicatorInstanceDto indicatorInstanceDto = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContextAdministrador(), INDICATOR_INSTANCE_3_INDICATORS_SYSTEM_1_V2);
        assertEquals(DIMENSION_1B_INDICATORS_SYSTEM_1_V2, indicatorInstanceDto.getParentUuid());
        indicatorInstanceDto.setParentUuid(DIMENSION_1A_INDICATORS_SYSTEM_1_V2);
        assertEquals(Long.valueOf(2), indicatorInstanceDto.getOrderInLevel());
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(3));
        assertEquals(INDICATOR_2, indicatorInstanceDto.getIndicatorUuid());
        indicatorInstanceDto.setIndicatorUuid("newIndicator");
        indicatorInstanceDto.setCode("new");

        try {
            indicatorsServiceFacade.updateIndicatorInstance(getServiceContextAdministrador(), indicatorInstanceDto);
            fail("Unmodifiable attributes changed");
        } catch (MetamacException e) {
            assertEquals(4, e.getExceptionItems().size());

            assertEquals(ServiceExceptionType.METADATA_UNMODIFIABLE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.INDICATOR_INSTANCE_CODE, e.getExceptionItems().get(0).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_UNMODIFIABLE.getCode(), e.getExceptionItems().get(1).getCode());
            assertEquals(1, e.getExceptionItems().get(1).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.INDICATOR_INSTANCE_INDICATOR_UUID, e.getExceptionItems().get(1).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_UNMODIFIABLE.getCode(), e.getExceptionItems().get(2).getCode());
            assertEquals(1, e.getExceptionItems().get(2).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.INDICATOR_INSTANCE_PARENT_UUID, e.getExceptionItems().get(2).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_UNMODIFIABLE.getCode(), e.getExceptionItems().get(3).getCode());
            assertEquals(1, e.getExceptionItems().get(3).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.INDICATOR_INSTANCE_ORDER_IN_LEVEL, e.getExceptionItems().get(3).getMessageParameters()[0]);
        }

        // Put parent null
        indicatorInstanceDto.setParentUuid(null);
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(2));
        indicatorInstanceDto.setIndicatorUuid(INDICATOR_2);
        indicatorInstanceDto.setCode(INDICATOR_INSTANCE_3_INDICATORS_SYSTEM_1_V2_CODE);

        try {
            indicatorsServiceFacade.updateIndicatorInstance(getServiceContextAdministrador(), indicatorInstanceDto);
            fail("Parent changed");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_UNMODIFIABLE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.INDICATOR_INSTANCE_PARENT_UUID, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testUpdateIndicatorInstanceErrorIndicatorsSystemPublished() throws Exception {

        IndicatorInstanceDto indicatorInstanceDto = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContextAdministrador(), INDICATOR_INSTANCE_2_INDICATORS_SYSTEM_3_V1);

        try {
            indicatorsServiceFacade.updateIndicatorInstance(getServiceContextAdministrador(), indicatorInstanceDto);
            fail("Indicators system published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(INDICATORS_SYSTEM_3, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_DRAFT, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_VALIDATION_REJECTED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
            assertEquals(ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_PRODUCTION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[2]);
            assertEquals(ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_DIFFUSION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[3]);
        }
    }

    @Test
    @Transactional
    public void testUpdateIndicatorInstanceErrorNotExists() throws Exception {

        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setUuid(NOT_EXISTS);
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalStringDto());

        try {
            indicatorsServiceFacade.updateIndicatorInstance(getServiceContextAdministrador(), indicatorInstanceDto);
            fail("Indicator instance not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_INSTANCE_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testUpdateIndicatorInstanceErrorOptimisticLocking() throws Exception {

        String uuid = INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2;

        IndicatorInstanceDto indicatorInstanceDtoSession1 = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContextAdministrador(), uuid);
        assertEquals(Long.valueOf(1), indicatorInstanceDtoSession1.getVersionOptimisticLocking());
        indicatorInstanceDtoSession1.setGeographicalGranularityUuid(GEOGRAPHICAL_GRANULARITY_2);

        IndicatorInstanceDto indicatorInstanceDtoSession2 = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContextAdministrador(), uuid);
        assertEquals(Long.valueOf(1), indicatorInstanceDtoSession2.getVersionOptimisticLocking());
        indicatorInstanceDtoSession2.setTitle(IndicatorsMocks.mockInternationalStringDto());

        // Update by session 1
        IndicatorInstanceDto indicatorInstanceDtoSession1AfterUpdate = indicatorsServiceFacade.updateIndicatorInstance(getServiceContextAdministrador(), indicatorInstanceDtoSession1);
        assertTrue(indicatorInstanceDtoSession1AfterUpdate.getVersionOptimisticLocking() > indicatorInstanceDtoSession1.getVersionOptimisticLocking());
        IndicatorsAsserts.assertEqualsIndicatorInstance(indicatorInstanceDtoSession1, indicatorInstanceDtoSession1AfterUpdate);

        // Fails when is updated by session 2
        try {
            indicatorsServiceFacade.updateIndicatorInstance(getServiceContextAdministrador(), indicatorInstanceDtoSession2);
            fail("Optimistic locking");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.OPTIMISTIC_LOCKING.getCode(), e.getExceptionItems().get(0).getCode());
            assertNull(e.getExceptionItems().get(0).getMessageParameters());
        }

        // Session 1 can modify because has last version
        indicatorInstanceDtoSession1AfterUpdate.setTitle(IndicatorsMocks.mockInternationalStringDto());
        IndicatorInstanceDto indicatorInstanceDtoSession1AfterUpdate2 = indicatorsServiceFacade.updateIndicatorInstance(getServiceContextAdministrador(), indicatorInstanceDtoSession1AfterUpdate);
        assertTrue(indicatorInstanceDtoSession1AfterUpdate2.getVersionOptimisticLocking() > indicatorInstanceDtoSession1AfterUpdate.getVersionOptimisticLocking());
        IndicatorsAsserts.assertEqualsIndicatorInstance(indicatorInstanceDtoSession1AfterUpdate, indicatorInstanceDtoSession1AfterUpdate2);
    }

    @Test
    @Transactional
    public void testUpdateIndicatorInstanceLocation() throws Exception {
        // In other test testUpdateIndicatorInstanceLocation*
    }

    @Test
    @DirtyDatabase
    public void testUpdateIndicatorInstanceLocationActualWithoutParentTargetWithParent() throws Exception {

        String uuid = INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2;
        String parentBeforeUuid = null;
        String parentTargetUuid = DIMENSION_2_INDICATORS_SYSTEM_1_V2;

        IndicatorInstanceDto indicatorInstanceDto = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContextAdministrador(), uuid);
        assertEquals(parentBeforeUuid, indicatorInstanceDto.getParentUuid());

        // Update location
        IndicatorInstanceDto indicatorInstanceDtoChanged = indicatorsServiceFacade.updateIndicatorInstanceLocation(getServiceContextAdministrador(), uuid, parentTargetUuid, Long.valueOf(1));

        // Validate indicatorInstance
        assertEquals(parentTargetUuid, indicatorInstanceDtoChanged.getParentUuid());
        indicatorInstanceDtoChanged = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContextAdministrador(), uuid);
        assertEquals(parentTargetUuid, indicatorInstanceDtoChanged.getParentUuid());

        // Validate source
        {
            IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContextAdministrador(), INDICATORS_SYSTEM_1,
                    IndicatorsDataBaseTest.SECOND_VERSION);
            assertEquals(3, indicatorsSystemStructureDto.getElements().size());
            assertEquals(DIMENSION_1_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(0).getDimension().getUuid());
            assertEquals(Long.valueOf(1), indicatorsSystemStructureDto.getElements().get(0).getOrderInLevel());
            assertEquals(INDICATOR_INSTANCE_2_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(1).getIndicatorInstance().getUuid());
            assertEquals(Long.valueOf(2), indicatorsSystemStructureDto.getElements().get(1).getOrderInLevel());
            assertEquals(DIMENSION_2_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(2).getDimension().getUuid());
            assertEquals(Long.valueOf(3), indicatorsSystemStructureDto.getElements().get(2).getOrderInLevel());
        }

        // Validate target
        {
            IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContextAdministrador(), INDICATORS_SYSTEM_1,
                    IndicatorsDataBaseTest.SECOND_VERSION);
            ElementLevelDto elementLevelDto = indicatorsSystemStructureDto.getElements().get(2);
            assertEquals(parentTargetUuid, elementLevelDto.getDimension().getUuid());
            assertEquals(1, elementLevelDto.getSubelements().size());
            assertEquals(uuid, elementLevelDto.getSubelements().get(0).getIndicatorInstance().getUuid());
            assertEquals(Long.valueOf(1), indicatorsSystemStructureDto.getElements().get(0).getOrderInLevel());
        }
    }

    @Test
    @DirtyDatabase
    public void testUpdateIndicatorInstanceLocationActualWithParentTargetWithoutParent() throws Exception {

        String uuid = INDICATOR_INSTANCE_3_INDICATORS_SYSTEM_1_V2;
        String parentBeforeUuid = DIMENSION_1B_INDICATORS_SYSTEM_1_V2;
        String parentTargetUuid = null;

        // Retrieve actual
        IndicatorInstanceDto indicatorInstanceDto = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContextAdministrador(), uuid);
        assertEquals(parentBeforeUuid, indicatorInstanceDto.getParentUuid());

        // Update location
        indicatorsServiceFacade.updateIndicatorInstanceLocation(getServiceContextAdministrador(), uuid, parentTargetUuid, Long.valueOf(2));

        // Validate parent is changed in indicatorInstance
        IndicatorInstanceDto indicatorInstanceDtoChanged = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContextAdministrador(), uuid);
        assertNull(indicatorInstanceDtoChanged.getParentUuid());

        // Validate source
        {
            IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContextAdministrador(), INDICATORS_SYSTEM_1,
                    IndicatorsDataBaseTest.SECOND_VERSION);
            ElementLevelDto elementLevelDto = indicatorsSystemStructureDto.getElements().get(2).getSubelements().get(1);
            assertEquals(parentBeforeUuid, elementLevelDto.getDimension().getUuid());
            assertEquals(1, elementLevelDto.getSubelements().size());
        }

        // Validate target
        {
            IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContextAdministrador(), INDICATORS_SYSTEM_1,
                    IndicatorsDataBaseTest.SECOND_VERSION);
            assertEquals(5, indicatorsSystemStructureDto.getElements().size());
            assertEquals(INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(0).getIndicatorInstance().getUuid());
            assertEquals(Long.valueOf(1), indicatorsSystemStructureDto.getElements().get(0).getOrderInLevel());
            assertEquals(uuid, indicatorsSystemStructureDto.getElements().get(1).getIndicatorInstance().getUuid());
            assertEquals(Long.valueOf(2), indicatorsSystemStructureDto.getElements().get(1).getOrderInLevel());
            assertEquals(DIMENSION_1_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(2).getDimension().getUuid());
            assertEquals(Long.valueOf(3), indicatorsSystemStructureDto.getElements().get(2).getOrderInLevel());
            assertEquals(INDICATOR_INSTANCE_2_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(3).getIndicatorInstance().getUuid());
            assertEquals(Long.valueOf(4), indicatorsSystemStructureDto.getElements().get(3).getOrderInLevel());
            assertEquals(DIMENSION_2_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(4).getDimension().getUuid());
            assertEquals(Long.valueOf(5), indicatorsSystemStructureDto.getElements().get(4).getOrderInLevel());
        }
    }

    @Test
    @DirtyDatabase
    public void testUpdateIndicatorInstanceLocationChangingDimensionParent() throws Exception {

        String uuid = INDICATOR_INSTANCE_3_INDICATORS_SYSTEM_1_V2;
        String parentBeforeUuid = DIMENSION_1B_INDICATORS_SYSTEM_1_V2;
        String parentTargetUuid = DIMENSION_1A_INDICATORS_SYSTEM_1_V2;

        // Retrieve actual
        IndicatorInstanceDto indicatorInstanceDto = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContextAdministrador(), uuid);
        assertEquals(parentBeforeUuid, indicatorInstanceDto.getParentUuid());

        // Update location
        indicatorsServiceFacade.updateIndicatorInstanceLocation(getServiceContextAdministrador(), uuid, parentTargetUuid, Long.valueOf(1));

        // Validate indicatorInstance
        IndicatorInstanceDto indicatorInstanceDtoChanged = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContextAdministrador(), uuid);
        assertEquals(parentTargetUuid, indicatorInstanceDtoChanged.getParentUuid());

        // Validate source
        {
            IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContextAdministrador(), INDICATORS_SYSTEM_1,
                    IndicatorsDataBaseTest.SECOND_VERSION);
            ElementLevelDto elementLevelDto = indicatorsSystemStructureDto.getElements().get(1).getSubelements().get(1);
            assertEquals(parentBeforeUuid, elementLevelDto.getDimension().getUuid());
            assertEquals(1, elementLevelDto.getSubelements().size());
        }

        // Validate target
        {
            IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContextAdministrador(), INDICATORS_SYSTEM_1,
                    IndicatorsDataBaseTest.SECOND_VERSION);
            ElementLevelDto elementLevelDto = indicatorsSystemStructureDto.getElements().get(1).getSubelements().get(0);
            assertEquals(parentTargetUuid, elementLevelDto.getDimension().getUuid());
            assertEquals(1, elementLevelDto.getSubelements().size());
            assertEquals(uuid, elementLevelDto.getSubelements().get(0).getIndicatorInstance().getUuid());
        }
    }

    @Test
    @DirtyDatabase
    public void testUpdateIndicatorInstanceLocationActualSameParentOnlyChangeOrderWithoutParent() throws Exception {

        String uuid = INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2;

        // Retrieve actual
        IndicatorInstanceDto indicatorInstanceDto = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContextAdministrador(), uuid);
        assertNull(indicatorInstanceDto.getParentUuid());
        assertEquals(Long.valueOf(1), indicatorInstanceDto.getOrderInLevel());

        // Update location
        indicatorsServiceFacade.updateIndicatorInstanceLocation(getServiceContextAdministrador(), uuid, indicatorInstanceDto.getParentUuid(), Long.valueOf(4));

        // Validate indicatorInstance
        IndicatorInstanceDto indicatorInstanceDtoChanged = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContextAdministrador(), uuid);
        assertNull(indicatorInstanceDtoChanged.getParentUuid());
        assertEquals(Long.valueOf(4), indicatorInstanceDtoChanged.getOrderInLevel());

        // Validate source and target
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContextAdministrador(), INDICATORS_SYSTEM_1,
                IndicatorsDataBaseTest.SECOND_VERSION);
        assertEquals(4, indicatorsSystemStructureDto.getElements().size());
        assertEquals(DIMENSION_1_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(0).getDimension().getUuid());
        assertEquals(Long.valueOf(1), indicatorsSystemStructureDto.getElements().get(0).getOrderInLevel());
        assertEquals(INDICATOR_INSTANCE_2_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(1).getIndicatorInstance().getUuid());
        assertEquals(Long.valueOf(2), indicatorsSystemStructureDto.getElements().get(1).getOrderInLevel());
        assertEquals(DIMENSION_2_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(2).getDimension().getUuid());
        assertEquals(Long.valueOf(3), indicatorsSystemStructureDto.getElements().get(2).getOrderInLevel());
        assertEquals(INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(3).getIndicatorInstance().getUuid());
        assertEquals(Long.valueOf(4), indicatorsSystemStructureDto.getElements().get(3).getOrderInLevel());
    }

    @Test
    @DirtyDatabase
    public void testUpdateIndicatorInstanceLocationActualSameParentOnlyChangeOrderWithParent() throws Exception {

        String uuid = INDICATOR_INSTANCE_3_INDICATORS_SYSTEM_1_V2;
        String parentUuid = DIMENSION_1B_INDICATORS_SYSTEM_1_V2;

        // Retrieve actual
        IndicatorInstanceDto indicatorInstanceDto = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContextAdministrador(), uuid);
        assertEquals(parentUuid, indicatorInstanceDto.getParentUuid());
        assertEquals(Long.valueOf(2), indicatorInstanceDto.getOrderInLevel());

        // Update location
        IndicatorInstanceDto indicatorInstanceDtoChanged = indicatorsServiceFacade.updateIndicatorInstanceLocation(getServiceContextAdministrador(), uuid, indicatorInstanceDto.getParentUuid(),
                Long.valueOf(1));

        // Validate indicatorInstance
        assertEquals(parentUuid, indicatorInstanceDtoChanged.getParentUuid());
        assertEquals(Long.valueOf(1), indicatorInstanceDtoChanged.getOrderInLevel());
        indicatorInstanceDtoChanged = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContextAdministrador(), uuid);
        assertEquals(parentUuid, indicatorInstanceDtoChanged.getParentUuid());
        assertEquals(Long.valueOf(1), indicatorInstanceDtoChanged.getOrderInLevel());

        // Validate source and target
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContextAdministrador(), INDICATORS_SYSTEM_1,
                IndicatorsDataBaseTest.SECOND_VERSION);
        ElementLevelDto elementLevelDto = indicatorsSystemStructureDto.getElements().get(1).getSubelements().get(1);
        assertEquals(parentUuid, elementLevelDto.getDimension().getUuid());
        assertEquals(2, elementLevelDto.getSubelements().size());
        assertEquals(uuid, elementLevelDto.getSubelements().get(0).getIndicatorInstance().getUuid());
        assertEquals(Long.valueOf(1), elementLevelDto.getSubelements().get(0).getIndicatorInstance().getOrderInLevel());
        assertEquals(DIMENSION_1BA_INDICATORS_SYSTEM_1_V2, elementLevelDto.getSubelements().get(1).getDimension().getUuid());
        assertEquals(Long.valueOf(2), elementLevelDto.getSubelements().get(1).getDimension().getOrderInLevel());

    }

    @Test
    @Transactional
    public void testUpdateIndicatorInstanceLocationErrorNotExists() throws Exception {

        String uuid = NOT_EXISTS;

        // Validation
        try {
            indicatorsServiceFacade.updateIndicatorInstanceLocation(getServiceContextAdministrador(), uuid, null, Long.valueOf(1));
            fail("IndicatorInstance not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_INSTANCE_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testUpdateIndicatorInstanceLocationErrorOrderIncorrect() throws Exception {

        try {
            indicatorsServiceFacade.updateIndicatorInstanceLocation(getServiceContextAdministrador(), INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2, null, Long.MAX_VALUE);
            fail("order incorrect");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.INDICATOR_INSTANCE_ORDER_IN_LEVEL, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testValidateTimeGranularities() throws Exception {

        // Valid
        for (int i = 0; i <= 1999; i++) {
            // Yearly
            String year = String.valueOf(1000 + i);
            assertTrue(year, TimeVariableUtils.isTimeValue(year));

            // Biyearly
            for (int j = 1; j <= 2; j++) {
                String biyear = year + "H" + String.valueOf(j);
                assertTrue(biyear, TimeVariableUtils.isTimeValue(biyear));
            }
            // Quarterly
            for (int j = 1; j <= 4; j++) {
                String quarter = year + "Q" + String.valueOf(j);
                assertTrue(quarter, TimeVariableUtils.isTimeValue(quarter));
            }
            for (int j = 1; j <= 12; j++) {
                String month = StringUtils.leftPad(String.valueOf(j), 2, "0");
                String monthly = year + "M" + month;
                // Monthly
                assertTrue(monthly, TimeVariableUtils.isTimeValue(monthly));
                // Daily
                for (int k = 1; k <= 31; k++) {
                    String day = year + month + StringUtils.leftPad(String.valueOf(k), 2, "0");
                    assertTrue(day, TimeVariableUtils.isTimeValue(day));
                }
            }
            // Weekly
            for (int j = 1; j <= 53; j++) {
                String week = year + "W" + StringUtils.leftPad(String.valueOf(j), 2, "0");
                assertTrue(week, TimeVariableUtils.isTimeValue(week));
            }
        }

        // Invalid
        assertFalse(GpeTimeUtils.isTimeValue("912"));
        assertFalse(GpeTimeUtils.isTimeValue("3000"));
        assertFalse(GpeTimeUtils.isTimeValue("2012q1"));
        assertFalse(GpeTimeUtils.isTimeValue("2012Q00"));
        assertFalse(GpeTimeUtils.isTimeValue("2012Q0"));
        assertFalse(GpeTimeUtils.isTimeValue("2012Q5"));
        assertFalse(GpeTimeUtils.isTimeValue("2012m1"));
        assertFalse(GpeTimeUtils.isTimeValue("2012M111"));
        assertFalse(GpeTimeUtils.isTimeValue("2012M1"));
        assertFalse(GpeTimeUtils.isTimeValue("2012M14"));
        assertFalse(GpeTimeUtils.isTimeValue("2012w1"));
        assertFalse(GpeTimeUtils.isTimeValue("2012W111"));
        assertFalse(GpeTimeUtils.isTimeValue("2012W1"));
        assertFalse(GpeTimeUtils.isTimeValue("2012W54"));
        assertFalse(GpeTimeUtils.isTimeValue("2012W60"));
        assertFalse(GpeTimeUtils.isTimeValue("20121"));
        assertFalse(GpeTimeUtils.isTimeValue("201212"));
        assertFalse(GpeTimeUtils.isTimeValue("2012121"));
        assertFalse(GpeTimeUtils.isTimeValue("201211223"));
        assertFalse(GpeTimeUtils.isTimeValue("afas"));
        assertFalse(GpeTimeUtils.isTimeValue("201 2"));
    }

    @Test
    @Transactional
    public void testCompareTimeGranularities() throws Exception {

        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2012", "2012") == 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2012H2", "2012H2") == 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2012Q1", "2012Q1") == 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2012M02", "2012M02") == 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("20120102", "20120102") == 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2012W51", "2012W51") == 0);

        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011", "2012") > 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2012H1", "2012H2") > 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011H2", "2012H2") > 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2012Q2", "2012Q3") > 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011Q1", "2012Q1") > 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2012M01", "2012M02") > 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011M01", "2012M02") > 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("20120102", "20120202") > 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("20120102", "20120103") > 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2012W01", "2012W51") > 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011W51", "2012W51") > 0);

        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2013", "2012") < 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2013H1", "2012H2") < 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("20130102", "20120103") < 0);
    }

    @Test
    @Transactional
    public void testCompareTimeGranularitiesErrorTimeValueIncorrect() throws Exception {
        try {
            TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2010xx", "2010H2");
            fail("time incorrect");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("2010xx", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testCompareTimeGranularitiesDifferentGranularities() throws Exception {
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011", "2011H2") > 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011", "2011Q4") > 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011", "2011M12") > 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011", "2011W52") > 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011", "20111231") > 0);

        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011H1", "2011Q2") > 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011H1", "2011M06") > 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011H2", "2011") < 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011H2", "2011Q4") > 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011H2", "2011M12") > 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011H2", "2011W52") > 0);

        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011Q1", "2011M03") > 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011Q1", "20110331") > 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011Q2", "2011H1") < 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011Q2", "2011M06") > 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011Q2", "20110630") > 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011Q3", "2011M09") > 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011Q3", "20110930") > 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011Q4", "2011H2") < 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011Q4", "2011M12") > 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011Q4", "2011W52") > 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011Q4", "20111231") > 0);

        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011M01", "20110131") > 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011M02", "20110228") > 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011M03", "20110331") > 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011M04", "2011W17") > 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011M04", "20110430") > 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011M05", "20110531") > 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011M06", "20110630") > 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011M07", "20110731") > 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011M08", "20110831") > 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011M09", "20110930") > 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011M10", "20111031") > 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011M11", "20111130") > 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011M12", "2011W52") > 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011M12", "20111231") > 0);

        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011W17", "2011M04") < 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011W17", "20110501") > 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011W52", "2011") < 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011W52", "2011H2") < 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011W52", "2011Q4") < 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011W52", "2011M12") < 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("2011W52", "20120101") > 0);

        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("20110131", "2011M01") < 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("20110228", "2011M02") < 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("20110331", "2011Q1") < 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("20110331", "2011M03") < 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("20110430", "2011M04") < 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("20110501", "2011W17") < 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("20110531", "2011M05") < 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("20110630", "2011H1") < 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("20110630", "2011Q2") < 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("20110630", "2011M06") < 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("20110731", "2011M07") < 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("20110831", "2011M08") < 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("20110930", "2011Q3") < 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("20110930", "2011M09") < 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("20111031", "2011M10") < 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("20111130", "2011M11") < 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("20111231", "2011") < 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("20111231", "2011H2") < 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("20111231", "2011Q4") < 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("20111231", "2011M12") < 0);
        assertTrue(TimeVariableUtils.compareToMostRecentFirstLowestGranularityMostRecent("20120101", "2011W52") < 0);
    }

    @Test
    @Transactional
    public void testGuessTimeGranularity() throws Exception {

        assertEquals(IstacTimeGranularityEnum.YEARLY, TimeVariableUtils.guessTimeGranularity("2012"));
        assertEquals(IstacTimeGranularityEnum.BIYEARLY, TimeVariableUtils.guessTimeGranularity("2012H2"));
        assertEquals(IstacTimeGranularityEnum.QUARTERLY, TimeVariableUtils.guessTimeGranularity("2012Q1"));
        assertEquals(IstacTimeGranularityEnum.MONTHLY, TimeVariableUtils.guessTimeGranularity("2012M02"));
        assertEquals(IstacTimeGranularityEnum.DAILY, TimeVariableUtils.guessTimeGranularity("20120102"));
        assertEquals(IstacTimeGranularityEnum.WEEKLY, TimeVariableUtils.guessTimeGranularity("2012W51"));
    }

    @Test
    @Transactional
    public void testGuessTimeGranularityErrorTimeValueIncorrect() throws Exception {
        try {
            TimeVariableUtils.guessTimeGranularity("2012W51xx");
            fail("time incorrect");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("2012W51xx", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testCalculatePreviousTimeValue() throws Exception {

        // Yearly
        assertEquals("2010", TimeVariableUtils.calculatePreviousTimeValue("2011"));
        assertEquals("1999", TimeVariableUtils.calculatePreviousTimeValue("2000"));
        // Biyearly
        assertEquals("2012H1", TimeVariableUtils.calculatePreviousTimeValue("2012H2"));
        assertEquals("2011H2", TimeVariableUtils.calculatePreviousTimeValue("2012H1"));
        // Quaterly
        assertEquals("2012Q3", TimeVariableUtils.calculatePreviousTimeValue("2012Q4"));
        assertEquals("2012Q2", TimeVariableUtils.calculatePreviousTimeValue("2012Q3"));
        assertEquals("2012Q1", TimeVariableUtils.calculatePreviousTimeValue("2012Q2"));
        assertEquals("2011Q4", TimeVariableUtils.calculatePreviousTimeValue("2012Q1"));
        // Monthly
        assertEquals("2012M11", TimeVariableUtils.calculatePreviousTimeValue("2012M12"));
        assertEquals("2012M10", TimeVariableUtils.calculatePreviousTimeValue("2012M11"));
        assertEquals("2012M09", TimeVariableUtils.calculatePreviousTimeValue("2012M10"));
        assertEquals("2012M08", TimeVariableUtils.calculatePreviousTimeValue("2012M09"));
        assertEquals("2012M07", TimeVariableUtils.calculatePreviousTimeValue("2012M08"));
        assertEquals("2012M06", TimeVariableUtils.calculatePreviousTimeValue("2012M07"));
        assertEquals("2012M05", TimeVariableUtils.calculatePreviousTimeValue("2012M06"));
        assertEquals("2012M04", TimeVariableUtils.calculatePreviousTimeValue("2012M05"));
        assertEquals("2012M03", TimeVariableUtils.calculatePreviousTimeValue("2012M04"));
        assertEquals("2012M02", TimeVariableUtils.calculatePreviousTimeValue("2012M03"));
        assertEquals("2012M01", TimeVariableUtils.calculatePreviousTimeValue("2012M02"));
        assertEquals("2011M12", TimeVariableUtils.calculatePreviousTimeValue("2012M01"));
        // Weekly
        assertEquals("2012W51", TimeVariableUtils.calculatePreviousTimeValue("2012W52"));
        assertEquals("2012W50", TimeVariableUtils.calculatePreviousTimeValue("2012W51"));
        assertEquals("2012W01", TimeVariableUtils.calculatePreviousTimeValue("2012W02"));
        assertEquals("2011W52", TimeVariableUtils.calculatePreviousTimeValue("2012W01"));
        // Daily
        assertEquals("20121230", TimeVariableUtils.calculatePreviousTimeValue("20121231"));
        assertEquals("20121229", TimeVariableUtils.calculatePreviousTimeValue("20121230"));
        assertEquals("20121207", TimeVariableUtils.calculatePreviousTimeValue("20121208"));
        assertEquals("20121130", TimeVariableUtils.calculatePreviousTimeValue("20121201"));
        assertEquals("20120930", TimeVariableUtils.calculatePreviousTimeValue("20121001"));
        assertEquals("20120505", TimeVariableUtils.calculatePreviousTimeValue("20120506"));
        assertEquals("20111231", TimeVariableUtils.calculatePreviousTimeValue("20120101"));
    }

    @Test
    @Transactional
    public void testCalculatePreviousYearTimeValue() throws Exception {

        // Yearly
        assertEquals("2010", TimeVariableUtils.calculatePreviousYearTimeValue("2011"));
        assertEquals("1999", TimeVariableUtils.calculatePreviousYearTimeValue("2000"));
        // Biyearly
        assertEquals("2011H2", TimeVariableUtils.calculatePreviousYearTimeValue("2012H2"));
        assertEquals("2011H1", TimeVariableUtils.calculatePreviousYearTimeValue("2012H1"));
        // Quaterly
        assertEquals("2011Q4", TimeVariableUtils.calculatePreviousYearTimeValue("2012Q4"));
        assertEquals("2011Q3", TimeVariableUtils.calculatePreviousYearTimeValue("2012Q3"));
        assertEquals("2011Q2", TimeVariableUtils.calculatePreviousYearTimeValue("2012Q2"));
        assertEquals("2011Q1", TimeVariableUtils.calculatePreviousYearTimeValue("2012Q1"));
        // Monthly
        assertEquals("2011M12", TimeVariableUtils.calculatePreviousYearTimeValue("2012M12"));
        assertEquals("2011M11", TimeVariableUtils.calculatePreviousYearTimeValue("2012M11"));
        assertEquals("2011M10", TimeVariableUtils.calculatePreviousYearTimeValue("2012M10"));
        assertEquals("2011M09", TimeVariableUtils.calculatePreviousYearTimeValue("2012M09"));
        assertEquals("2011M08", TimeVariableUtils.calculatePreviousYearTimeValue("2012M08"));
        assertEquals("2011M07", TimeVariableUtils.calculatePreviousYearTimeValue("2012M07"));
        assertEquals("2011M06", TimeVariableUtils.calculatePreviousYearTimeValue("2012M06"));
        assertEquals("2011M05", TimeVariableUtils.calculatePreviousYearTimeValue("2012M05"));
        assertEquals("2011M04", TimeVariableUtils.calculatePreviousYearTimeValue("2012M04"));
        assertEquals("2011M03", TimeVariableUtils.calculatePreviousYearTimeValue("2012M03"));
        assertEquals("2011M02", TimeVariableUtils.calculatePreviousYearTimeValue("2012M02"));
        assertEquals("2011M01", TimeVariableUtils.calculatePreviousYearTimeValue("2012M01"));
        // Weekly
        assertEquals("2011W52", TimeVariableUtils.calculatePreviousYearTimeValue("2012W52"));
        assertEquals("2011W51", TimeVariableUtils.calculatePreviousYearTimeValue("2012W51"));
        assertEquals("2011W02", TimeVariableUtils.calculatePreviousYearTimeValue("2012W02"));
        assertEquals("2011W01", TimeVariableUtils.calculatePreviousYearTimeValue("2012W01"));
        // Daily
        assertEquals("20111231", TimeVariableUtils.calculatePreviousYearTimeValue("20121231"));
        assertEquals("20111230", TimeVariableUtils.calculatePreviousYearTimeValue("20121230"));
        assertEquals("20111208", TimeVariableUtils.calculatePreviousYearTimeValue("20121208"));
        assertEquals("20111201", TimeVariableUtils.calculatePreviousYearTimeValue("20121201"));
        assertEquals("20111001", TimeVariableUtils.calculatePreviousYearTimeValue("20121001"));
        assertEquals("20110506", TimeVariableUtils.calculatePreviousYearTimeValue("20120506"));
        assertEquals("20110101", TimeVariableUtils.calculatePreviousYearTimeValue("20120101"));
        assertEquals(null, TimeVariableUtils.calculatePreviousYearTimeValue("20120229"));
    }

    @Test
    @Transactional
    public void testRetrieveGeographicalValue() throws Exception {

        String uuid = GEOGRAPHICAL_VALUE_1;
        GeographicalValueDto geographicalValueDto = indicatorsServiceFacade.retrieveGeographicalValue(getServiceContextAdministrador(), uuid);

        assertNotNull(geographicalValueDto);
        assertEquals(uuid, geographicalValueDto.getUuid());
        assertEquals("ES", geographicalValueDto.getCode());
        assertEquals(GEOGRAPHICAL_GRANULARITY_1, geographicalValueDto.getGranularity().getUuid());
        assertEquals(Double.valueOf(-40.689061), geographicalValueDto.getLatitude());
        assertEquals("-40.689061", geographicalValueDto.getLatitude().toString());
        assertEquals(Double.valueOf(368987.22), geographicalValueDto.getLongitude());
        assertEquals("368987.22", geographicalValueDto.getLongitude().toString());
        assertEquals("ES", geographicalValueDto.getOrder());
        IndicatorsAsserts.assertEqualsInternationalString(geographicalValueDto.getTitle(), "es", "España", "en", "Spain");
    }

    @Test
    @Transactional
    public void testRetrieveGeographicalValueErrorParameterRequired() throws Exception {

        String uuid = null;

        try {
            indicatorsServiceFacade.retrieveGeographicalValue(getServiceContextAdministrador(), uuid);
            fail("parameter required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.UUID, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testRetrieveGeographicalValueErrorNotExists() throws Exception {

        String uuid = NOT_EXISTS;

        try {
            indicatorsServiceFacade.retrieveGeographicalValue(getServiceContextAdministrador(), uuid);
            fail("No exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.GEOGRAPHICAL_VALUE_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testFindGeographicalValues() throws Exception {

        // IMPORTANT: Database have 9 geographicalValues but only two have the title correctly filled.
        // The findGeographicalValues service only retrieve values with title in the default language.
        // The real problem is in dbUnit file because title is required for this entities.
        // We don't want to change dbunit so, we assume that at most two results are returned:
        // - VALUE 1, with granularity 1
        // - VALUE 2, with granularity 4

        // All
        {
            MetamacCriteriaResult<GeographicalValueDto> geographicalValuesResult = indicatorsServiceFacade.findGeographicalValues(getServiceContextAdministrador(), null);
            assertEquals(Integer.valueOf(0), geographicalValuesResult.getPaginatorResult().getFirstResult());
            assertEquals(Integer.valueOf(25), geographicalValuesResult.getPaginatorResult().getMaximumResultSize());
            assertEquals(Integer.valueOf(2), geographicalValuesResult.getPaginatorResult().getTotalResults());
            assertEquals(2, geographicalValuesResult.getResults().size());

            List<GeographicalValueDto> geographicalValues = geographicalValuesResult.getResults();
            assertEquals(GEOGRAPHICAL_VALUE_1, geographicalValues.get(0).getUuid());
            assertEquals("ES", geographicalValues.get(0).getCode());
            assertEquals(GEOGRAPHICAL_VALUE_2, geographicalValues.get(1).getUuid());
            assertEquals("EN-LN", geographicalValues.get(1).getCode());
        }

        // All, only 1 results
        {
            MetamacCriteria criteria = new MetamacCriteria();
            criteria.setPaginator(new MetamacCriteriaPaginator());
            criteria.getPaginator().setMaximumResultSize(Integer.valueOf(1));
            criteria.getPaginator().setCountTotalResults(Boolean.TRUE);
            MetamacCriteriaResult<GeographicalValueDto> geographicalValuesResult = indicatorsServiceFacade.findGeographicalValues(getServiceContextAdministrador(), criteria);
            assertEquals(Integer.valueOf(0), geographicalValuesResult.getPaginatorResult().getFirstResult());
            assertEquals(Integer.valueOf(1), geographicalValuesResult.getPaginatorResult().getMaximumResultSize());
            assertEquals(Integer.valueOf(2), geographicalValuesResult.getPaginatorResult().getTotalResults());
            assertEquals(1, geographicalValuesResult.getResults().size());

            List<GeographicalValueDto> geographicalValues = geographicalValuesResult.getResults();
            assertEquals(GEOGRAPHICAL_VALUE_1, geographicalValues.get(0).getUuid());
            assertEquals("ES", geographicalValues.get(0).getCode());
        }

        // All, only 1 result second page
        {
            MetamacCriteria criteria = new MetamacCriteria();
            criteria.setPaginator(new MetamacCriteriaPaginator());
            criteria.getPaginator().setMaximumResultSize(Integer.valueOf(1));
            criteria.getPaginator().setFirstResult(Integer.valueOf(1));
            criteria.getPaginator().setCountTotalResults(Boolean.TRUE);
            MetamacCriteriaResult<GeographicalValueDto> geographicalValuesResult = indicatorsServiceFacade.findGeographicalValues(getServiceContextAdministrador(), criteria);
            assertEquals(Integer.valueOf(1), geographicalValuesResult.getPaginatorResult().getFirstResult());
            assertEquals(Integer.valueOf(1), geographicalValuesResult.getPaginatorResult().getMaximumResultSize());
            assertEquals(Integer.valueOf(2), geographicalValuesResult.getPaginatorResult().getTotalResults());
            assertEquals(1, geographicalValuesResult.getResults().size());

            List<GeographicalValueDto> geographicalValues = geographicalValuesResult.getResults();
            assertEquals(GEOGRAPHICAL_VALUE_2, geographicalValues.get(0).getUuid());
            assertEquals("EN-LN", geographicalValues.get(0).getCode());
        }

        // By granularity, with order default
        {
            MetamacCriteria criteria = new MetamacCriteria();
            MetamacCriteriaConjunctionRestriction conjuction = new MetamacCriteriaConjunctionRestriction();
            conjuction.getRestrictions()
                    .add(new MetamacCriteriaPropertyRestriction(GeographicalValueCriteriaPropertyEnum.GEOGRAPHICAL_GRANULARITY_UUID.name(), GEOGRAPHICAL_GRANULARITY_1, OperationType.EQ));
            criteria.setRestriction(conjuction);

            MetamacCriteriaResult<GeographicalValueDto> geographicalValuesResult = indicatorsServiceFacade.findGeographicalValues(getServiceContextAdministrador(), criteria);
            assertEquals(Integer.valueOf(0), geographicalValuesResult.getPaginatorResult().getFirstResult());
            assertEquals(Integer.valueOf(25), geographicalValuesResult.getPaginatorResult().getMaximumResultSize());
            assertEquals(Integer.valueOf(1), geographicalValuesResult.getPaginatorResult().getTotalResults());
            assertEquals(1, geographicalValuesResult.getResults().size());

            List<GeographicalValueDto> geographicalValues = geographicalValuesResult.getResults();
            assertEquals(GEOGRAPHICAL_VALUE_1, geographicalValues.get(0).getUuid());
            assertEquals("ES", geographicalValues.get(0).getCode());
        }
        // By granularity order by "order" desc
        {
            MetamacCriteria criteria = new MetamacCriteria();
            MetamacCriteriaConjunctionRestriction conjuction = new MetamacCriteriaConjunctionRestriction();
            conjuction.getRestrictions()
                    .add(new MetamacCriteriaPropertyRestriction(GeographicalValueCriteriaPropertyEnum.GEOGRAPHICAL_GRANULARITY_UUID.name(), GEOGRAPHICAL_GRANULARITY_1, OperationType.EQ));
            criteria.setRestriction(conjuction);
            criteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());
            MetamacCriteriaOrder metamacCriteriaOrder = new MetamacCriteriaOrder();
            metamacCriteriaOrder.setPropertyName(GeographicalValueCriteriaOrderEnum.ORDER.name());
            metamacCriteriaOrder.setType(OrderTypeEnum.DESC);
            criteria.getOrdersBy().add(metamacCriteriaOrder);

            MetamacCriteriaResult<GeographicalValueDto> geographicalValuesResult = indicatorsServiceFacade.findGeographicalValues(getServiceContextAdministrador(), criteria);
            assertEquals(Integer.valueOf(0), geographicalValuesResult.getPaginatorResult().getFirstResult());
            assertEquals(Integer.valueOf(25), geographicalValuesResult.getPaginatorResult().getMaximumResultSize());
            assertEquals(Integer.valueOf(1), geographicalValuesResult.getPaginatorResult().getTotalResults());
            assertEquals(1, geographicalValuesResult.getResults().size());

            List<GeographicalValueDto> geographicalValues = geographicalValuesResult.getResults();
            assertEquals(GEOGRAPHICAL_VALUE_1, geographicalValues.get(0).getUuid());
            assertEquals("ES", geographicalValues.get(0).getCode());
            assertEquals("ES", geographicalValues.get(0).getOrder());
        }
        // By granularity order by "order" asc
        {
            MetamacCriteria criteria = new MetamacCriteria();
            MetamacCriteriaConjunctionRestriction conjuction = new MetamacCriteriaConjunctionRestriction();
            conjuction.getRestrictions()
                    .add(new MetamacCriteriaPropertyRestriction(GeographicalValueCriteriaPropertyEnum.GEOGRAPHICAL_GRANULARITY_UUID.name(), GEOGRAPHICAL_GRANULARITY_1, OperationType.EQ));
            criteria.setRestriction(conjuction);
            criteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());
            MetamacCriteriaOrder metamacCriteriaOrder = new MetamacCriteriaOrder();
            metamacCriteriaOrder.setPropertyName(GeographicalValueCriteriaOrderEnum.ORDER.name());
            metamacCriteriaOrder.setType(OrderTypeEnum.ASC);
            criteria.getOrdersBy().add(metamacCriteriaOrder);

            MetamacCriteriaResult<GeographicalValueDto> geographicalValuesResult = indicatorsServiceFacade.findGeographicalValues(getServiceContextAdministrador(), criteria);
            assertEquals(Integer.valueOf(0), geographicalValuesResult.getPaginatorResult().getFirstResult());
            assertEquals(Integer.valueOf(25), geographicalValuesResult.getPaginatorResult().getMaximumResultSize());
            assertEquals(Integer.valueOf(1), geographicalValuesResult.getPaginatorResult().getTotalResults());
            assertEquals(1, geographicalValuesResult.getResults().size());

            List<GeographicalValueDto> geographicalValues = geographicalValuesResult.getResults();
            assertEquals(GEOGRAPHICAL_VALUE_1, geographicalValues.get(0).getUuid());
            assertEquals("ES", geographicalValues.get(0).getCode());
            assertEquals("ES", geographicalValues.get(0).getOrder());
        }
    }

    @Test
    @Transactional
    public void testCreateGeographicalValue() throws Exception {
        GeographicalValueDto geographicalValueDto = IndicatorsMocks.mockGeographicalValue("CANARIAS", "CANARIAS", GEOGRAPHICAL_GRANULARITY_2);

        // Create
        GeographicalValueDto geographicalValueDtoCreated = indicatorsServiceFacade.createGeographicalValue(getServiceContextAdministrador(), geographicalValueDto);

        // Validate
        assertNotNull(geographicalValueDtoCreated);
        assertNotNull(geographicalValueDtoCreated.getUuid());
        assertNotNull(geographicalValueDtoCreated.getOptimisticLockingVersion());

        IndicatorsAsserts.assertEqualsCreatedGeographicalValueDto(geographicalValueDto, geographicalValueDtoCreated);

        // Audit validations
        assertNotNull(geographicalValueDtoCreated.getCreatedBy());
        assertNotNull(geographicalValueDtoCreated.getCreatedDate());
        assertNotNull(geographicalValueDtoCreated.getLastUpdated());
        assertNotNull(geographicalValueDtoCreated.getLastUpdatedBy());
        assertEquals(getServiceContextAdministrador().getUserId(), geographicalValueDtoCreated.getCreatedBy());
        assertTrue(DateUtils.isSameDay(new Date(), geographicalValueDtoCreated.getCreatedDate()));
        assertTrue(DateUtils.isSameDay(new Date(), geographicalValueDtoCreated.getLastUpdated()));
        assertEquals(getServiceContextAdministrador().getUserId(), geographicalValueDtoCreated.getLastUpdatedBy());
    }

    @Test
    @Transactional
    public void testCreateGeographicalValueErrorGeographicalValueRequired() throws Exception {
        try {
            indicatorsServiceFacade.createGeographicalValue(getServiceContextAdministrador(), null);
            fail("parameter required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.GEOGRAPHICAL_VALUE, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testCreateGeographicalValueErrorCodeRequired() throws Exception {
        GeographicalValueDto geographicalValueDto = IndicatorsMocks.mockGeographicalValue(null, "CANARIAS", GEOGRAPHICAL_GRANULARITY_2);
        try {
            indicatorsServiceFacade.createGeographicalValue(getServiceContextAdministrador(), geographicalValueDto);
            fail("metadata required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.GEOGRAPHICAL_VALUE_CODE, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testCreateGeographicalValueErrorCodeRequiredEmpty() throws Exception {
        GeographicalValueDto geographicalValueDto = IndicatorsMocks.mockGeographicalValue(StringUtils.EMPTY, "CANARIAS", GEOGRAPHICAL_GRANULARITY_2);
        try {
            indicatorsServiceFacade.createGeographicalValue(getServiceContextAdministrador(), geographicalValueDto);
            fail("metadata required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.GEOGRAPHICAL_VALUE_CODE, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testCreateGeographicalValueErrorOrderRequired() throws Exception {
        GeographicalValueDto geographicalValueDto = IndicatorsMocks.mockGeographicalValue("CANARIAS", null, GEOGRAPHICAL_GRANULARITY_2);
        try {
            indicatorsServiceFacade.createGeographicalValue(getServiceContextAdministrador(), geographicalValueDto);
            fail("metadata required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.GEOGRAPHICAL_VALUE_ORDER, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testCreateGeographicalValueErrorOrderRequiredEmpty() throws Exception {
        GeographicalValueDto geographicalValueDto = IndicatorsMocks.mockGeographicalValue("CANARIAS", StringUtils.EMPTY, GEOGRAPHICAL_GRANULARITY_2);
        try {
            indicatorsServiceFacade.createGeographicalValue(getServiceContextAdministrador(), geographicalValueDto);
            fail("metadata required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.GEOGRAPHICAL_VALUE_ORDER, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testCreateGeographicalValueErrorGranularityRequired() throws Exception {
        GeographicalValueDto geographicalValueDto = IndicatorsMocks.mockGeographicalValue("CANARIAS", "CANARIAS", null);
        try {
            indicatorsServiceFacade.createGeographicalValue(getServiceContextAdministrador(), geographicalValueDto);
            fail("metadata required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.GEOGRAPHICAL_VALUE_GRANULARITY, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testCreateGeographicalValueErrorGranularityRequiredEmpty() throws Exception {
        GeographicalValueDto geographicalValueDto = IndicatorsMocks.mockGeographicalValue("CANARIAS", "CANARIAS", StringUtils.EMPTY);
        try {
            indicatorsServiceFacade.createGeographicalValue(getServiceContextAdministrador(), geographicalValueDto);
            fail("metadata required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.GEOGRAPHICAL_VALUE_GRANULARITY, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testCreateGeographicalValueErrorCodeDuplicated() throws Exception {
        String code = "ES";
        GeographicalValueDto geographicalValueDto = IndicatorsMocks.mockGeographicalValue(code, "SPAIN", GEOGRAPHICAL_GRANULARITY_1);
        try {
            indicatorsServiceFacade.createGeographicalValue(getServiceContextAdministrador(), geographicalValueDto);
            fail("code duplicated");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.GEOGRAPHICAL_VALUE_ALREADY_EXISTS_CODE_DUPLICATED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(code, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testCreateGeographicalValueErrorCodeDuplicatedInsensitive() throws Exception {
        String code = "es";
        GeographicalValueDto geographicalValueDto = IndicatorsMocks.mockGeographicalValue(code, "SPAIN", GEOGRAPHICAL_GRANULARITY_1);
        try {
            indicatorsServiceFacade.createGeographicalValue(getServiceContextAdministrador(), geographicalValueDto);
            fail("code duplicated");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.GEOGRAPHICAL_VALUE_ALREADY_EXISTS_CODE_DUPLICATED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(code, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testCreateGeographicalValueErrorOrderDuplicated() throws Exception {
        String order = "ES";
        GeographicalValueDto geographicalValueDto = IndicatorsMocks.mockGeographicalValue("SPAIN", order, GEOGRAPHICAL_GRANULARITY_1);
        try {
            indicatorsServiceFacade.createGeographicalValue(getServiceContextAdministrador(), geographicalValueDto);
            fail("oder duplicated");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.GEOGRAPHICAL_VALUE_ALREADY_EXISTS_ORDER_DUPLICATED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(order, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testCreateGeographicalValueErrorOrderDuplicatedInsensitive() throws Exception {
        String order = "es";
        GeographicalValueDto geographicalValueDto = IndicatorsMocks.mockGeographicalValue("SPAIN", order, GEOGRAPHICAL_GRANULARITY_1);
        try {
            indicatorsServiceFacade.createGeographicalValue(getServiceContextAdministrador(), geographicalValueDto);
            fail("oder duplicated");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.GEOGRAPHICAL_VALUE_ALREADY_EXISTS_ORDER_DUPLICATED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(order, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testUpdateGeographicalValue() throws Exception {
        String uuid = GEOGRAPHICAL_VALUE_1;

        GeographicalValueDto geographicalValueDto = indicatorsServiceFacade.retrieveGeographicalValue(getServiceContextAdministrador(), uuid);

        geographicalValueDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        geographicalValueDto.setCode(IndicatorsMocks.mockString(5));
        GeographicalGranularityDto geographicalGranularityDto = new GeographicalGranularityDto();
        geographicalGranularityDto.setUuid(GEOGRAPHICAL_GRANULARITY_4);
        geographicalValueDto.setGranularity(geographicalGranularityDto);
        geographicalValueDto.setLatitude(22.232511);
        geographicalValueDto.setLongitude(41232.254112);
        geographicalValueDto.setOrder(IndicatorsMocks.mockString(5));

        // Update
        GeographicalValueDto geographicalValueDtoUpdated = indicatorsServiceFacade.updateGeographicalValue(getServiceContextAdministrador(), geographicalValueDto);

        // Validations
        IndicatorsAsserts.assertEqualsGeographicalValueDto(geographicalValueDto, geographicalValueDtoUpdated);
        assertTrue(geographicalValueDtoUpdated.getLastUpdated().after(geographicalValueDtoUpdated.getCreatedDate()));
        assertTrue(geographicalValueDtoUpdated.getLastUpdated().after(geographicalValueDto.getLastUpdated()));
    }

    @Test
    @Transactional
    public void testUpdateGeographicalValueNotExists() throws Exception {
        GeographicalValueDto geographicalValueDto = indicatorsServiceFacade.retrieveGeographicalValue(getServiceContextAdministrador(), GEOGRAPHICAL_VALUE_1);
        geographicalValueDto.setUuid(NOT_EXISTS);
        try {
            indicatorsServiceFacade.updateGeographicalValue(getServiceContextAdministrador(), geographicalValueDto);
            fail("geographical value not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.GEOGRAPHICAL_VALUE_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testUpdateGeographicalValueCodeDuplicated() throws Exception {
        String code = "FR";
        GeographicalValueDto geographicalValueDto = indicatorsServiceFacade.retrieveGeographicalValue(getServiceContextAdministrador(), GEOGRAPHICAL_VALUE_1);
        geographicalValueDto.setCode(code);
        try {
            indicatorsServiceFacade.updateGeographicalValue(getServiceContextAdministrador(), geographicalValueDto);
            fail("geographical value code duplicated");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.GEOGRAPHICAL_VALUE_ALREADY_EXISTS_CODE_DUPLICATED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(code, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testUpdateGeographicalValueOrderDuplicated() throws Exception {
        String order = "FR";
        GeographicalValueDto geographicalValueDto = indicatorsServiceFacade.retrieveGeographicalValue(getServiceContextAdministrador(), GEOGRAPHICAL_VALUE_1);
        geographicalValueDto.setOrder(order);
        try {
            indicatorsServiceFacade.updateGeographicalValue(getServiceContextAdministrador(), geographicalValueDto);
            fail("geographical value order duplicated");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.GEOGRAPHICAL_VALUE_ALREADY_EXISTS_ORDER_DUPLICATED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(order, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testUpdateGeographicalValueErrorOptimisticLocking() throws Exception {
        String uuid = GEOGRAPHICAL_VALUE_1;

        GeographicalValueDto geographicalValueDtoSession1 = indicatorsServiceFacade.retrieveGeographicalValue(getServiceContextAdministrador(), uuid);
        assertEquals(Long.valueOf(1), geographicalValueDtoSession1.getOptimisticLockingVersion());

        GeographicalValueDto geographicalValueDtoSession2 = indicatorsServiceFacade.retrieveGeographicalValue(getServiceContextAdministrador(), uuid);
        assertEquals(Long.valueOf(1), geographicalValueDtoSession2.getOptimisticLockingVersion());

        // Update by session 1
        geographicalValueDtoSession1.setTitle(IndicatorsMocks.mockInternationalStringDto());
        GeographicalValueDto geographicalValueDtoSession1AfterUpdate = indicatorsServiceFacade.updateGeographicalValue(getServiceContextAdministrador(), geographicalValueDtoSession1);
        IndicatorsAsserts.assertEqualsGeographicalValueDto(geographicalValueDtoSession1, geographicalValueDtoSession1AfterUpdate);
        assertEquals(Long.valueOf(2), geographicalValueDtoSession1AfterUpdate.getOptimisticLockingVersion());

        // Fails when is updated by session 2
        try {
            indicatorsServiceFacade.updateGeographicalValue(getServiceContextAdministrador(), geographicalValueDtoSession2);
            fail("Optimistic locking");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.OPTIMISTIC_LOCKING.getCode(), e.getExceptionItems().get(0).getCode());
            assertNull(e.getExceptionItems().get(0).getMessageParameters());
        }

        // Session 1 can modify because has last version
        geographicalValueDtoSession1AfterUpdate.setTitle(IndicatorsMocks.mockInternationalStringDto());
        GeographicalValueDto geographicalValueDtoSession1AfterUpdate2 = indicatorsServiceFacade.updateGeographicalValue(getServiceContextAdministrador(), geographicalValueDtoSession1AfterUpdate);
        assertEquals(Long.valueOf(3), geographicalValueDtoSession1AfterUpdate2.getOptimisticLockingVersion());
        IndicatorsAsserts.assertEqualsGeographicalValueDto(geographicalValueDtoSession1AfterUpdate, geographicalValueDtoSession1AfterUpdate2);
    }

    @Test
    @Transactional
    public void testDeleteGeographicalValue() throws Exception {
        String uuid = GEOGRAPHICAL_VALUE_2;

        // Delete
        indicatorsServiceFacade.deleteGeographicalValue(getServiceContextAdministrador(), uuid);

        // Validation
        try {
            indicatorsServiceFacade.retrieveGeographicalValue(getServiceContextAdministrador(), uuid);
            fail("Geographical value deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.GEOGRAPHICAL_VALUE_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testDeleteGeographicalValueBeingUsed() throws Exception {
        when(indicatorsDataProviderService.retrieveDataJson(Matchers.any(ServiceContext.class), Matchers.eq(INDICATOR_1_DS_GPE_UUID))).thenReturn(INDICATOR_1_GPE_JSON_DATA);

        String uuid = GEOGRAPHICAL_VALUE_1;
        indicatorsDataService.populateIndicatorData(getServiceContextAdministrador(), INDICATOR_1);
        try {
            indicatorsServiceFacade.deleteGeographicalValue(getServiceContextAdministrador(), uuid);
            fail("Geographical value being used");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.GEOGRAPHICAL_VALUE_CAN_NOT_BE_REMOVED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testDeleteGeographicalValueNotExists() throws Exception {
        String uuid = NOT_EXISTS;

        try {
            indicatorsServiceFacade.deleteGeographicalValue(getServiceContextAdministrador(), uuid);
            fail("Geographical value not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.GEOGRAPHICAL_VALUE_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testCreateGeographicalGranularity() throws Exception {
        GeographicalGranularityDto geographicalGranularityDto = IndicatorsMocks.mockGeographicalGranularity("NUTS2");

        // Create
        GeographicalGranularityDto geographicalGranularityDtoCreated = indicatorsServiceFacade.createGeographicalGranularity(getServiceContextAdministrador(), geographicalGranularityDto);

        // Validate
        assertNotNull(geographicalGranularityDtoCreated);
        assertNotNull(geographicalGranularityDtoCreated.getUuid());
        assertNotNull(geographicalGranularityDtoCreated.getOptimisticLockingVersion());

        IndicatorsAsserts.assertEqualsCreatedGeographicalGranularityDto(geographicalGranularityDto, geographicalGranularityDtoCreated);

        // Audit validations
        assertNotNull(geographicalGranularityDtoCreated.getCreatedBy());
        assertNotNull(geographicalGranularityDtoCreated.getCreatedDate());
        assertNotNull(geographicalGranularityDtoCreated.getLastUpdated());
        assertNotNull(geographicalGranularityDtoCreated.getLastUpdatedBy());
        assertEquals(getServiceContextAdministrador().getUserId(), geographicalGranularityDtoCreated.getCreatedBy());
        assertTrue(DateUtils.isSameDay(new Date(), geographicalGranularityDtoCreated.getCreatedDate()));
        assertTrue(DateUtils.isSameDay(new Date(), geographicalGranularityDtoCreated.getLastUpdated()));
        assertEquals(getServiceContextAdministrador().getUserId(), geographicalGranularityDtoCreated.getLastUpdatedBy());
    }

    @Test
    @Transactional
    public void testCreateGeographicalGranularityErrorGeographicalGranularityRequired() throws Exception {
        try {
            indicatorsServiceFacade.createGeographicalGranularity(getServiceContextAdministrador(), null);
            fail("parameter required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.GEOGRAPHICAL_GRANULARITY, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testCreateGeographicalGranularityErrorCodeRequired() throws Exception {
        GeographicalGranularityDto geographicalGranularityDto = IndicatorsMocks.mockGeographicalGranularity(null);
        try {
            indicatorsServiceFacade.createGeographicalGranularity(getServiceContextAdministrador(), geographicalGranularityDto);
            fail("metadata required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.GEOGRAPHICAL_GRANULARITY_CODE, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testCreateGeographicalGranularityErrorCodeRequiredEmpty() throws Exception {
        GeographicalGranularityDto geographicalGranularityDto = IndicatorsMocks.mockGeographicalGranularity(StringUtils.EMPTY);
        try {
            indicatorsServiceFacade.createGeographicalGranularity(getServiceContextAdministrador(), geographicalGranularityDto);
            fail("metadata required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.GEOGRAPHICAL_GRANULARITY_CODE, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testCreateGeographicalGranularityErrorCodeDuplicated() throws Exception {
        String code = "COUNTRIES";
        GeographicalGranularityDto geographicalGranularityDto = IndicatorsMocks.mockGeographicalGranularity(code);
        try {
            indicatorsServiceFacade.createGeographicalGranularity(getServiceContextAdministrador(), geographicalGranularityDto);
            fail("code duplicated");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.GEOGRAPHICAL_GRANULARITY_ALREADY_EXISTS_CODE_DUPLICATED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(code, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testCreateGeographicalGranularityErrorCodeDuplicatedInsensitive() throws Exception {
        String code = "COuNTrIEs";
        GeographicalGranularityDto geographicalGranularityDto = IndicatorsMocks.mockGeographicalGranularity(code);
        try {
            indicatorsServiceFacade.createGeographicalGranularity(getServiceContextAdministrador(), geographicalGranularityDto);
            fail("code duplicated");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.GEOGRAPHICAL_GRANULARITY_ALREADY_EXISTS_CODE_DUPLICATED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(code, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testUpdateGeographicalGranularity() throws Exception {
        String uuid = GEOGRAPHICAL_GRANULARITY_1;

        GeographicalGranularityDto geographicalGranularityDto = indicatorsServiceFacade.retrieveGeographicalGranularity(getServiceContextAdministrador(), uuid);

        geographicalGranularityDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        geographicalGranularityDto.setCode(IndicatorsMocks.mockString(5));

        // Update
        GeographicalGranularityDto geographicalGranularityDtoUpdated = indicatorsServiceFacade.updateGeographicalGranularity(getServiceContextAdministrador(), geographicalGranularityDto);

        // Validations
        IndicatorsAsserts.assertEqualsGeographicalGranularityDto(geographicalGranularityDto, geographicalGranularityDtoUpdated);
        assertTrue(geographicalGranularityDtoUpdated.getLastUpdated().after(geographicalGranularityDtoUpdated.getCreatedDate()));
        assertTrue(geographicalGranularityDtoUpdated.getLastUpdated().after(geographicalGranularityDto.getLastUpdated()));
    }

    @Test
    @Transactional
    public void testUpdateGeographicalGranularityNotExists() throws Exception {
        GeographicalGranularityDto geographicalGranularityDto = indicatorsServiceFacade.retrieveGeographicalGranularity(getServiceContextAdministrador(), GEOGRAPHICAL_GRANULARITY_1);
        geographicalGranularityDto.setUuid(NOT_EXISTS);
        try {
            indicatorsServiceFacade.updateGeographicalGranularity(getServiceContextAdministrador(), geographicalGranularityDto);
            fail("geographical granularity not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.GEOGRAPHICAL_GRANULARITY_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testUpdateGeographicalGranularityCodeDuplicated() throws Exception {
        String code = "COMMUNITIES";
        GeographicalGranularityDto geographicalGranularityDto = indicatorsServiceFacade.retrieveGeographicalGranularity(getServiceContextAdministrador(), GEOGRAPHICAL_GRANULARITY_1);
        geographicalGranularityDto.setCode(code);
        try {
            indicatorsServiceFacade.updateGeographicalGranularity(getServiceContextAdministrador(), geographicalGranularityDto);
            fail("geographical granularity code duplicated");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.GEOGRAPHICAL_GRANULARITY_ALREADY_EXISTS_CODE_DUPLICATED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(code, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testUpdateGeographicalGranularityErrorOptimisticLocking() throws Exception {
        String uuid = GEOGRAPHICAL_GRANULARITY_1;

        GeographicalGranularityDto geographicalGranularityDtoSession1 = indicatorsServiceFacade.retrieveGeographicalGranularity(getServiceContextAdministrador(), uuid);
        assertEquals(Long.valueOf(1), geographicalGranularityDtoSession1.getOptimisticLockingVersion());

        GeographicalGranularityDto geographicalGranularityDtoSession2 = indicatorsServiceFacade.retrieveGeographicalGranularity(getServiceContextAdministrador(), uuid);
        assertEquals(Long.valueOf(1), geographicalGranularityDtoSession2.getOptimisticLockingVersion());

        // Update by session 1
        geographicalGranularityDtoSession1.setTitle(IndicatorsMocks.mockInternationalStringDto());
        GeographicalGranularityDto geographicalGranularityDtoSession1AfterUpdate = indicatorsServiceFacade.updateGeographicalGranularity(getServiceContextAdministrador(),
                geographicalGranularityDtoSession1);
        IndicatorsAsserts.assertEqualsGeographicalGranularityDto(geographicalGranularityDtoSession1, geographicalGranularityDtoSession1AfterUpdate);
        assertEquals(Long.valueOf(2), geographicalGranularityDtoSession1AfterUpdate.getOptimisticLockingVersion());

        // Fails when is updated by session 2
        try {
            indicatorsServiceFacade.updateGeographicalGranularity(getServiceContextAdministrador(), geographicalGranularityDtoSession2);
            fail("Optimistic locking");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.OPTIMISTIC_LOCKING.getCode(), e.getExceptionItems().get(0).getCode());
            assertNull(e.getExceptionItems().get(0).getMessageParameters());
        }

        // Session 1 can modify because has last version
        geographicalGranularityDtoSession1AfterUpdate.setTitle(IndicatorsMocks.mockInternationalStringDto());
        GeographicalGranularityDto geographicalGranularityDtoSession1AfterUpdate2 = indicatorsServiceFacade.updateGeographicalGranularity(getServiceContextAdministrador(),
                geographicalGranularityDtoSession1AfterUpdate);
        assertEquals(Long.valueOf(3), geographicalGranularityDtoSession1AfterUpdate2.getOptimisticLockingVersion());
        IndicatorsAsserts.assertEqualsGeographicalGranularityDto(geographicalGranularityDtoSession1AfterUpdate, geographicalGranularityDtoSession1AfterUpdate2);
    }

    @Test
    @Transactional
    public void testDeleteGeographicalGranularity() throws Exception {
        String uuid = GEOGRAPHICAL_GRANULARITY_5;

        // Delete
        indicatorsServiceFacade.deleteGeographicalGranularity(getServiceContextAdministrador(), uuid);

        // Validation
        try {
            indicatorsServiceFacade.retrieveGeographicalGranularity(getServiceContextAdministrador(), uuid);
            fail("Geographical value deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.GEOGRAPHICAL_GRANULARITY_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testDeleteGeographicalGranularityBeingUsed() throws Exception {
        String uuid = GEOGRAPHICAL_GRANULARITY_1;

        try {
            indicatorsServiceFacade.deleteGeographicalGranularity(getServiceContextAdministrador(), uuid);
            fail("Geographical value being used");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.GEOGRAPHICAL_GRANULARITY_CAN_NOT_BE_REMOVED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testDeleteGeographicalGranularityNotExists() throws Exception {
        String uuid = NOT_EXISTS;

        try {
            indicatorsServiceFacade.deleteGeographicalGranularity(getServiceContextAdministrador(), uuid);
            fail("Geographical value not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.GEOGRAPHICAL_GRANULARITY_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testFindGeographicalGranularities() throws Exception {
        // All
        {
            MetamacCriteriaResult<GeographicalGranularityDto> geographicalGranularitiesResult = indicatorsServiceFacade.findGeographicalGranularities(getServiceContextAdministrador(), null);
            assertEquals(Integer.valueOf(0), geographicalGranularitiesResult.getPaginatorResult().getFirstResult());
            assertEquals(Integer.valueOf(25), geographicalGranularitiesResult.getPaginatorResult().getMaximumResultSize());
            assertEquals(Integer.valueOf(5), geographicalGranularitiesResult.getPaginatorResult().getTotalResults());
            assertEquals(5, geographicalGranularitiesResult.getResults().size());

            List<GeographicalGranularityDto> geographicalGranularities = geographicalGranularitiesResult.getResults();
            assertEquals(GEOGRAPHICAL_GRANULARITY_1, geographicalGranularities.get(0).getUuid());
            assertEquals("COUNTRIES", geographicalGranularities.get(0).getCode());
            assertEquals(GEOGRAPHICAL_GRANULARITY_2, geographicalGranularities.get(1).getUuid());
            assertEquals("COMMUNITIES", geographicalGranularities.get(1).getCode());
            assertEquals(GEOGRAPHICAL_GRANULARITY_3, geographicalGranularities.get(2).getUuid());
            assertEquals("PROVINCES", geographicalGranularities.get(2).getCode());
            assertEquals(GEOGRAPHICAL_GRANULARITY_4, geographicalGranularities.get(3).getUuid());
            assertEquals("MUNICIPALITIES", geographicalGranularities.get(3).getCode());
            assertEquals(GEOGRAPHICAL_GRANULARITY_5, geographicalGranularities.get(4).getUuid());
            assertEquals("ISLANDS", geographicalGranularities.get(4).getCode());
        }

        // All, only 3 results
        {
            MetamacCriteria criteria = new MetamacCriteria();
            criteria.setPaginator(new MetamacCriteriaPaginator());
            criteria.getPaginator().setMaximumResultSize(Integer.valueOf(3));
            criteria.getPaginator().setCountTotalResults(Boolean.TRUE);
            MetamacCriteriaResult<GeographicalGranularityDto> geographicalGranularitiesResult = indicatorsServiceFacade.findGeographicalGranularities(getServiceContextAdministrador(), criteria);
            assertEquals(Integer.valueOf(0), geographicalGranularitiesResult.getPaginatorResult().getFirstResult());
            assertEquals(Integer.valueOf(3), geographicalGranularitiesResult.getPaginatorResult().getMaximumResultSize());
            assertEquals(Integer.valueOf(5), geographicalGranularitiesResult.getPaginatorResult().getTotalResults());
            assertEquals(3, geographicalGranularitiesResult.getResults().size());

            List<GeographicalGranularityDto> geographicalGranularities = geographicalGranularitiesResult.getResults();
            assertEquals(GEOGRAPHICAL_GRANULARITY_1, geographicalGranularities.get(0).getUuid());
            assertEquals("COUNTRIES", geographicalGranularities.get(0).getCode());
            assertEquals(GEOGRAPHICAL_GRANULARITY_2, geographicalGranularities.get(1).getUuid());
            assertEquals("COMMUNITIES", geographicalGranularities.get(1).getCode());
            assertEquals(GEOGRAPHICAL_GRANULARITY_3, geographicalGranularities.get(2).getUuid());
            assertEquals("PROVINCES", geographicalGranularities.get(2).getCode());
        }

        // All, only 2 results second page
        {
            MetamacCriteria criteria = new MetamacCriteria();
            criteria.setPaginator(new MetamacCriteriaPaginator());
            criteria.getPaginator().setMaximumResultSize(Integer.valueOf(3));
            criteria.getPaginator().setFirstResult(Integer.valueOf(3));
            criteria.getPaginator().setCountTotalResults(Boolean.TRUE);
            MetamacCriteriaResult<GeographicalGranularityDto> geographicalGranularitiesResult = indicatorsServiceFacade.findGeographicalGranularities(getServiceContextAdministrador(), criteria);
            assertEquals(Integer.valueOf(3), geographicalGranularitiesResult.getPaginatorResult().getFirstResult());
            assertEquals(Integer.valueOf(3), geographicalGranularitiesResult.getPaginatorResult().getMaximumResultSize());
            assertEquals(Integer.valueOf(5), geographicalGranularitiesResult.getPaginatorResult().getTotalResults());
            assertEquals(2, geographicalGranularitiesResult.getResults().size());

            List<GeographicalGranularityDto> geographicalGranularities = geographicalGranularitiesResult.getResults();
            assertEquals(GEOGRAPHICAL_GRANULARITY_4, geographicalGranularities.get(0).getUuid());
            assertEquals("MUNICIPALITIES", geographicalGranularities.get(0).getCode());
            assertEquals(GEOGRAPHICAL_GRANULARITY_5, geographicalGranularities.get(1).getUuid());
            assertEquals("ISLANDS", geographicalGranularities.get(1).getCode());
        }
    }

    @Test
    @Transactional
    public void testRetrieveGeographicalGranularity() throws Exception {

        String uuid = GEOGRAPHICAL_GRANULARITY_2;
        GeographicalGranularityDto geographicalGranularityDto = indicatorsServiceFacade.retrieveGeographicalGranularity(getServiceContextAdministrador(), uuid);

        assertNotNull(geographicalGranularityDto);
        assertEquals(uuid, geographicalGranularityDto.getUuid());
        assertEquals("COMMUNITIES", geographicalGranularityDto.getCode());
        IndicatorsAsserts.assertEqualsInternationalString(geographicalGranularityDto.getTitle(), "es", "Comunidades", "en", "Communities");
    }

    @Test
    @Transactional
    public void testRetrieveGeographicalGranularityErrorParameterRequired() throws Exception {

        String uuid = null;

        try {
            indicatorsServiceFacade.retrieveGeographicalGranularity(getServiceContextAdministrador(), uuid);
            fail("parameter required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.UUID, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testRetrieveGeographicalGranularityErrorNotExists() throws Exception {

        String uuid = NOT_EXISTS;

        try {
            indicatorsServiceFacade.retrieveGeographicalGranularity(getServiceContextAdministrador(), uuid);
            fail("No exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.GEOGRAPHICAL_GRANULARITY_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Transactional
    public void testRetrieveGeographicalGranularities() throws Exception {

        List<GeographicalGranularityDto> geographicalGranularities = indicatorsServiceFacade.retrieveGeographicalGranularities(getServiceContextAdministrador());
        assertEquals(5, geographicalGranularities.size());

        Map<String, String> granularitiesExpected = new HashMap<String, String>();
        granularitiesExpected.put(GEOGRAPHICAL_GRANULARITY_1, "COUNTRIES");
        granularitiesExpected.put(GEOGRAPHICAL_GRANULARITY_2, "COMMUNITIES");
        granularitiesExpected.put(GEOGRAPHICAL_GRANULARITY_3, "PROVINCES");
        granularitiesExpected.put(GEOGRAPHICAL_GRANULARITY_4, "MUNICIPALITIES");
        granularitiesExpected.put(GEOGRAPHICAL_GRANULARITY_5, "ISLANDS");

        checkGranularitiesInCollection(granularitiesExpected, geographicalGranularities);
    }

    private void checkGranularitiesInCollection(Map<String, String> granularitiesExpected, Collection<GeographicalGranularityDto> geographicalGranularities) {
        assertEquals(granularitiesExpected.size(), geographicalGranularities.size());

        for (GeographicalGranularityDto granularity : geographicalGranularities) {
            String code = granularitiesExpected.get(granularity.getUuid());
            assertEquals(code, granularity.getCode());
        }
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/IndicatorsServiceFacadeIndicatorsSystemsTest.xml";
    }
}
