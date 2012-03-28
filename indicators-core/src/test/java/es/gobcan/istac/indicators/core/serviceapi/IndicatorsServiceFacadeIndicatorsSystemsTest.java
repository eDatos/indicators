package es.gobcan.istac.indicators.core.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.gobcan.istac.indicators.core.criteria.GeographicalValueCriteriaPropertyEnum;
import es.gobcan.istac.indicators.core.criteria.IndicatorsCriteria;
import es.gobcan.istac.indicators.core.criteria.IndicatorsCriteriaConjunctionRestriction;
import es.gobcan.istac.indicators.core.criteria.IndicatorsCriteriaPropertyRestriction;
import es.gobcan.istac.indicators.core.dto.serviceapi.DimensionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.ElementLevelDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.GeographicalValueDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorInstanceDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemStructureDto;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorsSystemProcStatusEnum;
import es.gobcan.istac.indicators.core.enume.domain.TimeGranularityEnum;
import es.gobcan.istac.indicators.core.enume.domain.VersionTypeEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.serviceapi.utils.IndicatorsAsserts;
import es.gobcan.istac.indicators.core.serviceapi.utils.IndicatorsMocks;
import es.gobcan.istac.indicators.core.serviceimpl.util.TimeVariableUtils;

/**
 * Test to IndicatorsServiceFacade. Testing: indicators systems, dimensions, indicators instances
 * Spring based transactional test with DbUnit support.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext-test.xml"})
public class IndicatorsServiceFacadeIndicatorsSystemsTest extends IndicatorsBaseTest {

    @Autowired
    protected IndicatorsServiceFacade indicatorsServiceFacade;

    private static String             NOT_EXISTS                                  = "not-exists";

    // Indicators systems
    private static String             INDICATORS_SYSTEM_1                         = "IndSys-1";
    private static String             INDICATORS_SYSTEM_2                         = "IndSys-2";
    private static String             INDICATORS_SYSTEM_3                         = "IndSys-3";
    private static String             INDICATORS_SYSTEM_3_VERSION                 = "11.033";
    private static String             INDICATORS_SYSTEM_4                         = "IndSys-4";
    private static String             INDICATORS_SYSTEM_5                         = "IndSys-5";
    private static String             INDICATORS_SYSTEM_6                         = "IndSys-6";
    private static String             INDICATORS_SYSTEM_7                         = "IndSys-7";
    private static String             INDICATORS_SYSTEM_8                         = "IndSys-8";
    private static String             INDICATORS_SYSTEM_9                         = "IndSys-9";

    // Dimensions
    private static String             DIMENSION_NOT_EXISTS                        = "Dim-not-exists";
    private static String             DIMENSION_1_INDICATORS_SYSTEM_1_V1          = "IndSys-1-v1-Dimension-1";
    private static String             DIMENSION_1_INDICATORS_SYSTEM_1_V2          = "IndSys-1-v2-Dimension-1";
    private static String             DIMENSION_1A_INDICATORS_SYSTEM_1_V2         = "IndSys-1-v2-Dimension-1A";
    private static String             DIMENSION_1B_INDICATORS_SYSTEM_1_V2         = "IndSys-1-v2-Dimension-1B";
    private static String             DIMENSION_1BA_INDICATORS_SYSTEM_1_V2        = "IndSys-1-v2-Dimension-1BA";
    private static String             DIMENSION_2_INDICATORS_SYSTEM_1_V2          = "IndSys-1-v2-Dimension-2";
    private static String             DIMENSION_1_INDICATORS_SYSTEM_3             = "IndSys-3-v1-Dimension-1";

    // Indicator instances
    private static String             INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V1 = "IndSys-1-v1-IInstance-1";
    private static String             INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2 = "IndSys-1-v2-IInstance-1";
    private static String             INDICATOR_INSTANCE_2_INDICATORS_SYSTEM_1_V2 = "IndSys-1-v2-IInstance-2";
    private static String             INDICATOR_INSTANCE_3_INDICATORS_SYSTEM_1_V2 = "IndSys-1-v2-IInstance-3";
    private static String             INDICATOR_INSTANCE_2_INDICATORS_SYSTEM_3_V1 = "IndSys-3-v1-IInstance-2";

    // Geographical values
    private static String             GEOGRAPHICAL_VALUE_1                          = "1";
    private static String             GEOGRAPHICAL_VALUE_2                          = "2";
    private static String             GEOGRAPHICAL_VALUE_3                          = "3";
    private static String             GEOGRAPHICAL_VALUE_4                          = "4";
    
    // Geographical granularities
    private static String             GEOGRAPHICAL_GRANULARITY_1                    = "1";
    private static String             GEOGRAPHICAL_GRANULARITY_2                    = "2";

    // Indicators
    private static String             INDICATOR_1                                 = "Indicator-1";
    private static String             INDICATOR_2                                 = "Indicator-2";
    private static String             INDICATOR_3                                 = "Indicator-3";

    @Test
    public void testRetrieveIndicatorsSystem() throws Exception {

        String uuid = INDICATORS_SYSTEM_1;
        String versionNumber = "1.000";
        IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);

        assertNotNull(indicatorsSystemDto);
        assertEquals(uuid, indicatorsSystemDto.getUuid());
        assertEquals(versionNumber, indicatorsSystemDto.getVersionNumber());
        assertEquals("1.000", indicatorsSystemDto.getDiffusionVersion());
        assertEquals("2.000", indicatorsSystemDto.getProductionVersion());
        assertEquals("CODE-1", indicatorsSystemDto.getCode());
        assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemDto.getProcStatus());
        IndicatorsAsserts.assertEqualsDate("2011-01-01 01:02:04", indicatorsSystemDto.getCreatedDate());
        IndicatorsAsserts.assertEqualsDate("2011-01-02 02:02:04", indicatorsSystemDto.getProductionValidationDate());
        IndicatorsAsserts.assertEqualsDate("2011-01-03 03:02:04", indicatorsSystemDto.getDiffusionValidationDate());
        IndicatorsAsserts.assertEqualsDate("2011-01-04 04:02:04", indicatorsSystemDto.getPublicationDate());
        assertNull(indicatorsSystemDto.getArchiveDate());
    }

    @Test
    public void testRetrieveIndicatorsSystemWithAndWithoutVersion() throws Exception {

        String uuid = INDICATORS_SYSTEM_1;
        String versionNumberProduction = "2.000";
        String versionNumberDiffusion = "1.000";

        // Without version (retrieve last)
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, null);
            assertEquals(uuid, indicatorsSystemDto.getUuid());
            assertEquals(versionNumberProduction, indicatorsSystemDto.getVersionNumber());
            assertEquals(versionNumberDiffusion, indicatorsSystemDto.getDiffusionVersion());
            assertEquals(versionNumberProduction, indicatorsSystemDto.getProductionVersion());
        }

        // With version 1
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumberDiffusion);
            assertEquals(uuid, indicatorsSystemDto.getUuid());
            assertEquals(versionNumberDiffusion, indicatorsSystemDto.getVersionNumber());
            assertEquals(versionNumberDiffusion, indicatorsSystemDto.getDiffusionVersion());
            assertEquals(versionNumberProduction, indicatorsSystemDto.getProductionVersion());
        }

        // With version 2
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumberProduction);
            assertEquals(uuid, indicatorsSystemDto.getUuid());
            assertEquals(versionNumberProduction, indicatorsSystemDto.getVersionNumber());
            assertEquals(versionNumberDiffusion, indicatorsSystemDto.getDiffusionVersion());
            assertEquals(versionNumberProduction, indicatorsSystemDto.getProductionVersion());
        }
    }

    @Test
    public void testRetrieveIndicatorsSystemErrorParameterRequired() throws Exception {

        String uuid = null;

        try {
            indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, null);
            fail("parameter required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("UUID", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRetrieveIndicatorsSystemErrorNotExists() throws Exception {

        String uuid = NOT_EXISTS;

        try {
            indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, null);
            fail("No exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRetrieveIndicatorsSystemErrorVersionNotExists() throws Exception {

        String uuid = INDICATORS_SYSTEM_2;
        String versionNotExists = String.valueOf(99);

        try {
            indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNotExists);
            fail("No exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_VERSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(versionNotExists, e.getExceptionItems().get(0).getMessageParameters()[1]);
        }
    }

    @Test
    public void testRetrieveIndicatorsSystemPublished() throws Exception {

        String uuid = INDICATORS_SYSTEM_3;

        IndicatorsSystemDto indicatorsSystemDtoPublished = indicatorsServiceFacade.retrieveIndicatorsSystemPublished(getServiceContext(), uuid);
        assertEquals(uuid, indicatorsSystemDtoPublished.getUuid());
        assertEquals("11.033", indicatorsSystemDtoPublished.getVersionNumber());
        assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemDtoPublished.getProcStatus());
    }

    @Test
    public void testRetrieveIndicatorsSystemPublishedWhenSystemHasVersionProduction() throws Exception {

        String uuid = INDICATORS_SYSTEM_1;

        IndicatorsSystemDto indicatorsSystemDtoPublished = indicatorsServiceFacade.retrieveIndicatorsSystemPublished(getServiceContext(), uuid);
        assertEquals(uuid, indicatorsSystemDtoPublished.getUuid());
        assertEquals("1.000", indicatorsSystemDtoPublished.getVersionNumber());
        assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemDtoPublished.getProcStatus());
    }

    @Test
    public void testRetrieveIndicatorsSystemPublishedErrorOnlyProduction() throws Exception {

        String uuid = INDICATORS_SYSTEM_2;

        try {
            indicatorsServiceFacade.retrieveIndicatorsSystemPublished(getServiceContext(), uuid);
            fail("No published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, ((IndicatorsSystemProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Test
    public void testRetrieveIndicatorsSystemPublishedErrorParameterRequired() throws Exception {

        String uuid = null;

        try {
            indicatorsServiceFacade.retrieveIndicatorsSystemPublished(getServiceContext(), uuid);
            fail("parameter required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("UUID", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRetrieveIndicatorsSystemPublishedErrorNotExists() throws Exception {

        String uuid = NOT_EXISTS;

        try {
            indicatorsServiceFacade.retrieveIndicatorsSystemPublished(getServiceContext(), uuid);
            fail("No exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRetrieveIndicatorsSystemByCode() throws Exception {

        String code = "CODE-1";
        String versionNumber = "1.000";
        IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContext(), code, versionNumber);

        assertNotNull(indicatorsSystemDto);
        assertEquals(INDICATORS_SYSTEM_1, indicatorsSystemDto.getUuid());
        assertEquals("1.000", indicatorsSystemDto.getVersionNumber());
        assertEquals("1.000", indicatorsSystemDto.getDiffusionVersion());
        assertEquals("2.000", indicatorsSystemDto.getProductionVersion());
        assertEquals("CODE-1", indicatorsSystemDto.getCode());
    }

    @Test
    public void testRetrieveIndicatorsSystemByCodeLastVersion() throws Exception {

        String code = "CODE-1";
        String versionNumber = null;
        IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContext(), code, versionNumber);

        assertNotNull(indicatorsSystemDto);
        assertEquals(INDICATORS_SYSTEM_1, indicatorsSystemDto.getUuid());
        assertEquals(indicatorsSystemDto.getProductionVersion(), indicatorsSystemDto.getVersionNumber());
        assertEquals("1.000", indicatorsSystemDto.getDiffusionVersion());
        assertEquals("2.000", indicatorsSystemDto.getProductionVersion());
        assertEquals("CODE-1", indicatorsSystemDto.getCode());
    }

    @Test
    public void testRetrieveIndicatorsSystemByCodeErrorNotExists() throws Exception {

        String code = "CODE_NOT_EXISTS";

        try {
            indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContext(), code, null);
            fail("No exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND_WITH_CODE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(code, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRetrieveIndicatorsSystemPublishedByCode() throws Exception {

        String code = "CODE-1";
        IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystemPublishedByCode(getServiceContext(), code);

        assertNotNull(indicatorsSystemDto);
        assertEquals(INDICATORS_SYSTEM_1, indicatorsSystemDto.getUuid());
        assertEquals(indicatorsSystemDto.getDiffusionVersion(), indicatorsSystemDto.getVersionNumber());
        assertEquals("1.000", indicatorsSystemDto.getDiffusionVersion());
        assertEquals("2.000", indicatorsSystemDto.getProductionVersion());
        assertEquals("CODE-1", indicatorsSystemDto.getCode());
    }

    @Test
    public void testRetrieveIndicatorsSystemPublishedByCodeErrorNotExists() throws Exception {

        String code = "CODE_NOT_EXISTS";

        try {
            indicatorsServiceFacade.retrieveIndicatorsSystemPublishedByCode(getServiceContext(), code);
            fail("No exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND_WITH_CODE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(code, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRetrieveIndicatorsSystemPublishedByCodeErrorNotExistsInDiffusion() throws Exception {

        String code = "CODE-2";

        try {
            indicatorsServiceFacade.retrieveIndicatorsSystemPublishedByCode(getServiceContext(), code);
            fail("No exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(INDICATORS_SYSTEM_2, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, ((IndicatorsSystemProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Test
    public void testRetrieveIndicatorsSystemStructure() throws Exception {

        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContext(), INDICATORS_SYSTEM_1, "2.000");

        assertEquals(INDICATORS_SYSTEM_1, indicatorsSystemStructureDto.getUuid());
        assertEquals("2.000", indicatorsSystemStructureDto.getVersionNumber());

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
    public void testCreateIndicatorsSystem() throws Exception {

        IndicatorsSystemDto indicatorsSystemDto = new IndicatorsSystemDto();
        indicatorsSystemDto.setCode(IndicatorsMocks.mockString(10));

        // Create
        IndicatorsSystemDto indicatorsSystemDtoCreated = indicatorsServiceFacade.createIndicatorsSystem(getServiceContext(), indicatorsSystemDto);

        // Validate
        assertNotNull(indicatorsSystemDtoCreated);
        assertNotNull(indicatorsSystemDtoCreated.getUuid());
        assertNotNull(indicatorsSystemDtoCreated.getVersionNumber());
        IndicatorsSystemDto indicatorsSystemDtoRetrieved = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), indicatorsSystemDtoCreated.getUuid(),
                indicatorsSystemDtoCreated.getVersionNumber());
        assertEquals(IndicatorsSystemProcStatusEnum.DRAFT, indicatorsSystemDtoCreated.getProcStatus());
        assertEquals(IndicatorsSystemProcStatusEnum.DRAFT, indicatorsSystemDtoRetrieved.getProcStatus());
        assertEquals("1.000", indicatorsSystemDtoRetrieved.getProductionVersion());
        assertNull(indicatorsSystemDtoRetrieved.getDiffusionVersion());
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
        assertEquals(getServiceContext().getUserId(), indicatorsSystemDtoCreated.getCreatedBy());
        assertTrue(DateUtils.isSameDay(new Date(), indicatorsSystemDtoCreated.getCreatedDate()));
        assertTrue(DateUtils.isSameDay(new Date(), indicatorsSystemDtoCreated.getLastUpdated()));
        assertEquals(getServiceContext().getUserId(), indicatorsSystemDtoCreated.getLastUpdatedBy());
    }

    @Test
    public void testCreateIndicatorsSystemParametersRequired() throws Exception {

        IndicatorsSystemDto indicatorsSystemDto = new IndicatorsSystemDto();
        indicatorsSystemDto.setCode(null);

        try {
            indicatorsServiceFacade.createIndicatorsSystem(getServiceContext(), indicatorsSystemDto);
            fail("parameters required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("INDICATORS_SYSTEM.CODE", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorsSystemCodeDuplicated() throws Exception {

        IndicatorsSystemDto indicatorsSystemDto = new IndicatorsSystemDto();
        indicatorsSystemDto.setCode("CoDe-1");

        try {
            indicatorsServiceFacade.createIndicatorsSystem(getServiceContext(), indicatorsSystemDto);
            fail("code duplicated");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_ALREADY_EXIST_CODE_DUPLICATED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(indicatorsSystemDto.getCode(), e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorsSystemCodeDuplicatedInsensitive() throws Exception {

        IndicatorsSystemDto indicatorsSystemDto = new IndicatorsSystemDto();
        indicatorsSystemDto.setCode("CoDe-1");

        try {
            indicatorsServiceFacade.createIndicatorsSystem(getServiceContext(), indicatorsSystemDto);
            fail("code duplicated");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_ALREADY_EXIST_CODE_DUPLICATED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(indicatorsSystemDto.getCode(), e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testDeleteIndicatorsSystem() throws Exception {

        String uuid = INDICATORS_SYSTEM_2;

        // Delete indicators system only in draft
        indicatorsServiceFacade.deleteIndicatorsSystem(getServiceContext(), uuid);

        // Validation
        try {
            indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, null);
            fail("Indicators system deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testDeleteIndicatorsSystemWithPublishedAndDraft() throws Exception {

        String uuid = INDICATORS_SYSTEM_1;

        // Retrieve: version 1 is diffusion; version 2 is in production
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, "1.000");
            assertEquals(uuid, indicatorsSystemDto.getUuid());
            assertEquals("1.000", indicatorsSystemDto.getVersionNumber());
            assertEquals("1.000", indicatorsSystemDto.getDiffusionVersion());
            assertEquals("2.000", indicatorsSystemDto.getProductionVersion());
        }

        // Retrieve dimensions to check will be deleted
        List<String> dimensionsUuid = new ArrayList<String>();
        dimensionsUuid.add(DIMENSION_1_INDICATORS_SYSTEM_1_V2);
        dimensionsUuid.add(DIMENSION_1A_INDICATORS_SYSTEM_1_V2);
        dimensionsUuid.add(DIMENSION_1B_INDICATORS_SYSTEM_1_V2);
        dimensionsUuid.add(DIMENSION_1BA_INDICATORS_SYSTEM_1_V2);
        dimensionsUuid.add(DIMENSION_2_INDICATORS_SYSTEM_1_V2);
        for (String dimensionUuid : dimensionsUuid) {
            DimensionDto dimensionDto = indicatorsServiceFacade.retrieveDimension(getServiceContext(), dimensionUuid);
            assertNotNull(dimensionDto);
        }
        // Retrieve indicators instances to check will be deleted
        List<String> indicatorsInstancesUuid = new ArrayList<String>();
        indicatorsInstancesUuid.add(INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2);
        indicatorsInstancesUuid.add(INDICATOR_INSTANCE_2_INDICATORS_SYSTEM_1_V2);
        indicatorsInstancesUuid.add(INDICATOR_INSTANCE_3_INDICATORS_SYSTEM_1_V2);
        for (String indicatorInstanceUuid : indicatorsInstancesUuid) {
            IndicatorInstanceDto indicatorInstanceDto = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContext(), indicatorInstanceUuid);
            assertNotNull(indicatorInstanceDto);
        }

        // Delete indicators system
        indicatorsServiceFacade.deleteIndicatorsSystem(getServiceContext(), uuid);

        // Validation
        // Version 1 exists
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, "1.000");
            assertEquals(uuid, indicatorsSystemDto.getUuid());
            assertEquals("1.000", indicatorsSystemDto.getVersionNumber());
            assertEquals("1.000", indicatorsSystemDto.getDiffusionVersion());
            assertEquals(null, indicatorsSystemDto.getProductionVersion());
        }
        // Version 2 not exists
        try {
            indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, "2.000");
            fail("Indicators system version deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_VERSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals("2.000", e.getExceptionItems().get(0).getMessageParameters()[1]);
        }

        // Check dimensions deleted
        for (String dimensionUuid : dimensionsUuid) {
            try {
                indicatorsServiceFacade.retrieveDimension(getServiceContext(), dimensionUuid);
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
                indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContext(), indicatorInstanceUuid);
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
    public void testDeleteIndicatorsSystemErrorInDiffusion() throws Exception {

        String uuid = INDICATORS_SYSTEM_3;

        // Validation
        try {
            indicatorsServiceFacade.deleteIndicatorsSystem(getServiceContext(), uuid);
            fail("Indicators system is not in draft");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_IN_PRODUCTION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testDeleteIndicatorsSystemErrorNotExists() throws Exception {

        String uuid = NOT_EXISTS;

        // Validation
        try {
            indicatorsServiceFacade.deleteIndicatorsSystem(getServiceContext(), uuid);
            fail("Indicator system not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testSendIndicatorsSystemToProductionValidation() throws Exception {

        String uuid = INDICATORS_SYSTEM_1;
        String diffusionVersion = "1.000";
        String productionVersion = "2.000";

        {
            IndicatorsSystemDto indicatorsSystemDtoV1 = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, diffusionVersion);
            IndicatorsSystemDto indicatorsSystemDtoV2 = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, productionVersion);
            assertEquals(diffusionVersion, indicatorsSystemDtoV1.getDiffusionVersion());
            assertEquals(productionVersion, indicatorsSystemDtoV2.getProductionVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemDtoV1.getProcStatus());
            assertEquals(IndicatorsSystemProcStatusEnum.DRAFT, indicatorsSystemDtoV2.getProcStatus());
        }

        // Sends to production validation
        IndicatorsSystemDto indicatorsSystemDtoV2Updated = indicatorsServiceFacade.sendIndicatorsSystemToProductionValidation(getServiceContext2(), uuid);

        // Validation
        {
            assertEquals(diffusionVersion, indicatorsSystemDtoV2Updated.getDiffusionVersion());
            assertEquals(productionVersion, indicatorsSystemDtoV2Updated.getProductionVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.PRODUCTION_VALIDATION, indicatorsSystemDtoV2Updated.getProcStatus());
            assertTrue(DateUtils.isSameDay(new Date(), indicatorsSystemDtoV2Updated.getProductionValidationDate()));
            assertEquals(getServiceContext2().getUserId(), indicatorsSystemDtoV2Updated.getProductionValidationUser());
        }
        {
            IndicatorsSystemDto indicatorsSystemDtoV1 = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, diffusionVersion);
            IndicatorsSystemDto indicatorsSystemDtoV2 = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, productionVersion);
            assertEquals(diffusionVersion, indicatorsSystemDtoV1.getDiffusionVersion());
            assertEquals(productionVersion, indicatorsSystemDtoV2.getProductionVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemDtoV1.getProcStatus());
            assertEquals(IndicatorsSystemProcStatusEnum.PRODUCTION_VALIDATION, indicatorsSystemDtoV2.getProcStatus());

            assertTrue(DateUtils.isSameDay(new Date(), indicatorsSystemDtoV2.getProductionValidationDate()));
            assertEquals(getServiceContext2().getUserId(), indicatorsSystemDtoV2.getProductionValidationUser());
            assertNull(indicatorsSystemDtoV2.getDiffusionValidationDate());
            assertNull(indicatorsSystemDtoV2.getDiffusionValidationUser());
            assertNull(indicatorsSystemDtoV2.getPublicationDate());
            assertNull(indicatorsSystemDtoV2.getPublicationUser());
            assertNull(indicatorsSystemDtoV2.getArchiveDate());
            assertNull(indicatorsSystemDtoV2.getArchiveUser());
        }
    }

    @Test
    public void testSendIndicatorsSystemToProductionValidationInProcStatusRejected() throws Exception {

        String uuid = INDICATORS_SYSTEM_9;
        String productionVersion = "1.000";

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, productionVersion);
            assertEquals(productionVersion, indicatorsSystemDto.getProductionVersion());
            assertNull(indicatorsSystemDto.getDiffusionVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.VALIDATION_REJECTED, indicatorsSystemDto.getProcStatus());
        }

        // Sends to production validation
        indicatorsServiceFacade.sendIndicatorsSystemToProductionValidation(getServiceContext2(), uuid);

        // Validation
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, productionVersion);
            assertEquals(productionVersion, indicatorsSystemDto.getProductionVersion());
            assertNull(indicatorsSystemDto.getDiffusionVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.PRODUCTION_VALIDATION, indicatorsSystemDto.getProcStatus());
        }
    }

    @Test
    public void testSendIndicatorsSystemToProductionValidationErrorWithoutIndicatorInstance() throws Exception {
        try {
            indicatorsServiceFacade.sendIndicatorsSystemToProductionValidation(getServiceContext(), INDICATORS_SYSTEM_2);
            fail("Indicators system has not any indicator instance");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_MUST_HAVE_INDICATOR_INSTANCE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(INDICATORS_SYSTEM_2, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testSendIndicatorsSystemToProductionValidationErrorNotExists() throws Exception {

        try {
            indicatorsServiceFacade.sendIndicatorsSystemToProductionValidation(getServiceContext(), NOT_EXISTS);
            fail("Indicators system not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testSendIndicatorsSystemToProductionValidationErrorWrongProcStatus() throws Exception {

        String uuid = INDICATORS_SYSTEM_3;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, INDICATORS_SYSTEM_3_VERSION);
            assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemDto.getProcStatus());
            assertNull(indicatorsSystemDto.getProductionVersion());
        }

        try {
            indicatorsServiceFacade.sendIndicatorsSystemToProductionValidation(getServiceContext(), uuid);
            fail("Indicators system is not draft");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorsSystemProcStatusEnum.DRAFT, ((IndicatorsSystemProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(IndicatorsSystemProcStatusEnum.VALIDATION_REJECTED, ((IndicatorsSystemProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);

        }
    }

    @Test
    public void testSendIndicatorsSystemToDiffusionValidation() throws Exception {

        String uuid = INDICATORS_SYSTEM_4;
        String versionNumber = "1.000";

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
            assertEquals(null, indicatorsSystemDto.getDiffusionVersion());
            assertEquals("1.000", indicatorsSystemDto.getProductionVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.PRODUCTION_VALIDATION, indicatorsSystemDto.getProcStatus());
        }

        // Sends to diffusion validation
        IndicatorsSystemDto indicatorsSystemDtoV1Updated = indicatorsServiceFacade.sendIndicatorsSystemToDiffusionValidation(getServiceContext(), uuid);

        // Validation
        {
            assertEquals(IndicatorsSystemProcStatusEnum.DIFFUSION_VALIDATION, indicatorsSystemDtoV1Updated.getProcStatus());
            IndicatorsAsserts.assertEqualsDate("2011-04-04 01:02:04", indicatorsSystemDtoV1Updated.getProductionValidationDate());
            assertEquals("user1", indicatorsSystemDtoV1Updated.getProductionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), indicatorsSystemDtoV1Updated.getDiffusionValidationDate()));
            assertEquals(getServiceContext().getUserId(), indicatorsSystemDtoV1Updated.getDiffusionValidationUser());

        }
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
            assertEquals(null, indicatorsSystemDto.getDiffusionVersion());
            assertEquals("1.000", indicatorsSystemDto.getProductionVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.DIFFUSION_VALIDATION, indicatorsSystemDto.getProcStatus());

            IndicatorsAsserts.assertEqualsDate("2011-04-04 01:02:04", indicatorsSystemDto.getProductionValidationDate());
            assertEquals("user1", indicatorsSystemDto.getProductionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), indicatorsSystemDto.getDiffusionValidationDate()));
            assertEquals(getServiceContext().getUserId(), indicatorsSystemDto.getDiffusionValidationUser());
            assertNull(indicatorsSystemDto.getPublicationDate());
            assertNull(indicatorsSystemDto.getPublicationUser());
            assertNull(indicatorsSystemDto.getArchiveDate());
            assertNull(indicatorsSystemDto.getArchiveUser());
        }
    }

    @Test
    public void testSendIndicatorsSystemToDiffusionValidationErrorNotExists() throws Exception {

        try {
            indicatorsServiceFacade.sendIndicatorsSystemToDiffusionValidation(getServiceContext(), NOT_EXISTS);
            fail("Indicators system not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testSendIndicatorsSystemToDiffusionValidationErrorWrongProcStatusDraft() throws Exception {

        String uuid = INDICATORS_SYSTEM_2;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, "1.000");
            assertEquals(IndicatorsSystemProcStatusEnum.DRAFT, indicatorsSystemDto.getProcStatus());
            assertEquals("1.000", indicatorsSystemDto.getProductionVersion());
            assertEquals(null, indicatorsSystemDto.getDiffusionVersion());
        }

        try {
            indicatorsServiceFacade.sendIndicatorsSystemToDiffusionValidation(getServiceContext(), uuid);
            fail("Indicators system is not draft");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorsSystemProcStatusEnum.PRODUCTION_VALIDATION, ((IndicatorsSystemProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);

        }
    }

    @Test
    public void testSendIndicatorsSystemToDiffusionValidationErrorWrongProcStatusPublished() throws Exception {

        String uuid = INDICATORS_SYSTEM_3;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, INDICATORS_SYSTEM_3_VERSION);
            assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemDto.getProcStatus());
            assertEquals(null, indicatorsSystemDto.getProductionVersion());
            assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDto.getDiffusionVersion());
        }

        try {
            indicatorsServiceFacade.sendIndicatorsSystemToDiffusionValidation(getServiceContext(), uuid);
            fail("Indicators system is not production validation");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorsSystemProcStatusEnum.PRODUCTION_VALIDATION, ((IndicatorsSystemProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);

        }
    }

    @Test
    public void testRejectIndicatorsSystemValidation() throws Exception {

        String uuid = INDICATORS_SYSTEM_4;
        String versionNumber = "1.000";

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
            assertEquals(null, indicatorsSystemDto.getDiffusionVersion());
            assertEquals("1.000", indicatorsSystemDto.getProductionVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.PRODUCTION_VALIDATION, indicatorsSystemDto.getProcStatus());
        }

        // Rejects validation
        IndicatorsSystemDto indicatorsSystemDtoV1Updated = indicatorsServiceFacade.rejectIndicatorsSystemValidation(getServiceContext(), uuid);

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
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
            assertEquals(null, indicatorsSystemDto.getDiffusionVersion());
            assertEquals("1.000", indicatorsSystemDto.getProductionVersion());
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
    public void testRejectIndicatorsSystemValidationInDiffusionValidation() throws Exception {

        String uuid = INDICATORS_SYSTEM_5;
        String versionNumber = "1.000";

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
            assertEquals(null, indicatorsSystemDto.getDiffusionVersion());
            assertEquals("1.000", indicatorsSystemDto.getProductionVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.DIFFUSION_VALIDATION, indicatorsSystemDto.getProcStatus());
        }

        // Rejects validation
        indicatorsServiceFacade.rejectIndicatorsSystemValidation(getServiceContext(), uuid);

        // Validation
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
            assertEquals(null, indicatorsSystemDto.getDiffusionVersion());
            assertEquals("1.000", indicatorsSystemDto.getProductionVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.VALIDATION_REJECTED, indicatorsSystemDto.getProcStatus());
        }
    }

    @Test
    public void testRejectIndicatorsSystemValidationErrorNotExists() throws Exception {

        try {
            indicatorsServiceFacade.rejectIndicatorsSystemValidation(getServiceContext(), NOT_EXISTS);
            fail("Indicators system not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRejectIndicatorsSystemValidationErrorWrongProcStatusProduction() throws Exception {

        String uuid = INDICATORS_SYSTEM_2;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, "1.000");
            assertEquals(IndicatorsSystemProcStatusEnum.DRAFT, indicatorsSystemDto.getProcStatus());
            assertEquals("1.000", indicatorsSystemDto.getProductionVersion());
            assertEquals(null, indicatorsSystemDto.getDiffusionVersion());
        }

        try {
            indicatorsServiceFacade.rejectIndicatorsSystemValidation(getServiceContext(), uuid);
            fail("Indicators system is not in validation");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorsSystemProcStatusEnum.PRODUCTION_VALIDATION, ((IndicatorsSystemProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(IndicatorsSystemProcStatusEnum.DIFFUSION_VALIDATION, ((IndicatorsSystemProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
        }
    }

    @Test
    public void testRejectIndicatorsSystemValidationErrorWrongProcStatusDiffusion() throws Exception {

        String uuid = INDICATORS_SYSTEM_3;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, INDICATORS_SYSTEM_3_VERSION);
            assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemDto.getProcStatus());
            assertEquals(null, indicatorsSystemDto.getProductionVersion());
            assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDto.getDiffusionVersion());
        }

        try {
            indicatorsServiceFacade.rejectIndicatorsSystemValidation(getServiceContext(), uuid);
            fail("Indicators system is not in validation");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorsSystemProcStatusEnum.PRODUCTION_VALIDATION, ((IndicatorsSystemProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(IndicatorsSystemProcStatusEnum.DIFFUSION_VALIDATION, ((IndicatorsSystemProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
        }
    }

    @Test
    public void testPublishIndicatorsSystem() throws Exception {

        String uuid = INDICATORS_SYSTEM_5;
        String versionNumber = "1.000";

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
            assertEquals(versionNumber, indicatorsSystemDto.getProductionVersion());
            assertEquals(null, indicatorsSystemDto.getDiffusionVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.DIFFUSION_VALIDATION, indicatorsSystemDto.getProcStatus());
        }

        // Publish
        IndicatorsSystemDto indicatorsSystemDto1Updated = indicatorsServiceFacade.publishIndicatorsSystem(getServiceContext(), uuid);

        // Validation
        {
            assertEquals(null, indicatorsSystemDto1Updated.getProductionVersion());
            assertEquals(versionNumber, indicatorsSystemDto1Updated.getDiffusionVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemDto1Updated.getProcStatus());
            IndicatorsAsserts.assertEqualsDate("2011-06-06 01:02:04", indicatorsSystemDto1Updated.getProductionValidationDate());
            assertEquals("user1", indicatorsSystemDto1Updated.getProductionValidationUser());
            IndicatorsAsserts.assertEqualsDate("2011-07-07 03:02:04", indicatorsSystemDto1Updated.getDiffusionValidationDate());
            assertEquals("user2", indicatorsSystemDto1Updated.getDiffusionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), indicatorsSystemDto1Updated.getPublicationDate()));
            assertEquals(getServiceContext().getUserId(), indicatorsSystemDto1Updated.getPublicationUser());
            assertNull(indicatorsSystemDto1Updated.getArchiveDate());
            assertNull(indicatorsSystemDto1Updated.getArchiveUser());
        }
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
            assertEquals(null, indicatorsSystemDto.getProductionVersion());
            assertEquals(versionNumber, indicatorsSystemDto.getDiffusionVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemDto.getProcStatus());

            IndicatorsAsserts.assertEqualsDate("2011-06-06 01:02:04", indicatorsSystemDto.getProductionValidationDate());
            assertEquals("user1", indicatorsSystemDto.getProductionValidationUser());
            IndicatorsAsserts.assertEqualsDate("2011-07-07 03:02:04", indicatorsSystemDto.getDiffusionValidationDate());
            assertEquals("user2", indicatorsSystemDto.getDiffusionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), indicatorsSystemDto.getPublicationDate()));
            assertEquals(getServiceContext().getUserId(), indicatorsSystemDto.getPublicationUser());
            assertNull(indicatorsSystemDto.getArchiveDate());
            assertNull(indicatorsSystemDto.getArchiveUser());
        }
    }

    @Test
    public void testPublishIndicatorsSystemWithPublishedVersion() throws Exception {

        String uuid = INDICATORS_SYSTEM_6;
        String diffusionVersionBefore = "1.000"; // will be deleted when publish current version in diffusion validation
        String productionVersionBefore = "2.000"; // will be published

        {
            IndicatorsSystemDto indicatorsSystemDtoV1 = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, diffusionVersionBefore);
            IndicatorsSystemDto indicatorsSystemDtoV2 = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, productionVersionBefore);
            assertEquals(productionVersionBefore, indicatorsSystemDtoV1.getProductionVersion());
            assertEquals(diffusionVersionBefore, indicatorsSystemDtoV1.getDiffusionVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemDtoV1.getProcStatus());
            assertEquals(IndicatorsSystemProcStatusEnum.DIFFUSION_VALIDATION, indicatorsSystemDtoV2.getProcStatus());
        }

        // Publish
        indicatorsServiceFacade.publishIndicatorsSystem(getServiceContext(), uuid);

        // Validation
        {
            // Version 1 already not exists
            try {
                indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, diffusionVersionBefore);
                fail("Indicators system version not exists");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_VERSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
                assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
                assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
                assertEquals(diffusionVersionBefore, e.getExceptionItems().get(0).getMessageParameters()[1]);
            }

            // Actual version in diffusion is version 2
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, productionVersionBefore);
            assertEquals(null, indicatorsSystemDto.getProductionVersion());
            assertEquals("2.000", indicatorsSystemDto.getDiffusionVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemDto.getProcStatus());
        }
    }

    @Test
    public void testPublishIndicatorsSystemWithArchivedVersion() throws Exception {

        String uuid = INDICATORS_SYSTEM_7;
        String diffusionVersionBefore = "1.000"; // will be deleted when publish current version in diffusion validation
        String productionVersionBefore = "2.000"; // will be published

        {
            IndicatorsSystemDto indicatorsSystemDtoV1 = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, diffusionVersionBefore);
            IndicatorsSystemDto indicatorsSystemDtoV2 = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, productionVersionBefore);
            assertEquals(productionVersionBefore, indicatorsSystemDtoV1.getProductionVersion());
            assertEquals(diffusionVersionBefore, indicatorsSystemDtoV1.getDiffusionVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.ARCHIVED, indicatorsSystemDtoV1.getProcStatus());
            assertEquals(IndicatorsSystemProcStatusEnum.DIFFUSION_VALIDATION, indicatorsSystemDtoV2.getProcStatus());
        }

        // Publish
        indicatorsServiceFacade.publishIndicatorsSystem(getServiceContext(), uuid);

        // Validation
        {
            // Version 1 already not exists
            try {
                indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, diffusionVersionBefore);
                fail("Indicators system version not exists");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_VERSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
                assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
                assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
                assertEquals(diffusionVersionBefore, e.getExceptionItems().get(0).getMessageParameters()[1]);
            }

            // Actual version in diffusion is version 2
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, productionVersionBefore);
            assertEquals(null, indicatorsSystemDto.getProductionVersion());
            assertEquals("2.000", indicatorsSystemDto.getDiffusionVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemDto.getProcStatus());
        }
    }

    @Test
    public void testPublishIndicatorsSystemErrorNotExists() throws Exception {

        try {
            indicatorsServiceFacade.publishIndicatorsSystem(getServiceContext(), NOT_EXISTS);
            fail("Indicators system not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testPublishIndicatorsSystemErrorWrongProcStatusProduction() throws Exception {

        String uuid = INDICATORS_SYSTEM_2;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, "1.000");
            assertEquals(IndicatorsSystemProcStatusEnum.DRAFT, indicatorsSystemDto.getProcStatus());
            assertEquals("1.000", indicatorsSystemDto.getProductionVersion());
            assertEquals(null, indicatorsSystemDto.getDiffusionVersion());
        }

        try {
            indicatorsServiceFacade.publishIndicatorsSystem(getServiceContext(), uuid);
            fail("Indicators system is not diffusion validation");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorsSystemProcStatusEnum.DIFFUSION_VALIDATION, ((IndicatorsSystemProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Test
    public void testPublishIndicatorsSystemErrorWrongProcStatusDiffusion() throws Exception {

        String uuid = INDICATORS_SYSTEM_3;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, INDICATORS_SYSTEM_3_VERSION);
            assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemDto.getProcStatus());
            assertEquals(null, indicatorsSystemDto.getProductionVersion());
            assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDto.getDiffusionVersion());
        }

        try {
            indicatorsServiceFacade.publishIndicatorsSystem(getServiceContext(), uuid);
            fail("Indicators system is not diffusion validation");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorsSystemProcStatusEnum.DIFFUSION_VALIDATION, ((IndicatorsSystemProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Test
    public void testPublishIndicatorsSystemErrorIndicatorNotPublished() throws Exception {

        String uuid = INDICATORS_SYSTEM_5;
        String indicatorUuid2 = INDICATOR_2;
        String indicatorUuid3 = INDICATOR_3;

        // Create indicators instances with indicator not published
        {
            // Indicator 2
            IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
            indicatorInstanceDto.setIndicatorUuid(indicatorUuid2);
            indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalString());
            indicatorInstanceDto.setParentUuid(null);
            indicatorInstanceDto.setOrderInLevel(Long.valueOf(2));
            indicatorInstanceDto.setGeographicalValueUuid(GEOGRAPHICAL_VALUE_1);
            indicatorInstanceDto.setTimeValue("2012");
            indicatorsServiceFacade.createIndicatorInstance(getServiceContext(), uuid, indicatorInstanceDto);
        }
        {
            // Indicator 3
            IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
            indicatorInstanceDto.setIndicatorUuid(indicatorUuid3);
            indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalString());
            indicatorInstanceDto.setParentUuid(null);
            indicatorInstanceDto.setOrderInLevel(Long.valueOf(3));
            indicatorInstanceDto.setGeographicalValueUuid(GEOGRAPHICAL_VALUE_1);
            indicatorInstanceDto.setTimeValue("2012");
            indicatorsServiceFacade.createIndicatorInstance(getServiceContext(), uuid, indicatorInstanceDto);
        }

        // Publish
        try {
            indicatorsServiceFacade.publishIndicatorsSystem(getServiceContext(), uuid);
            fail("Indicators not published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_MUST_HAVE_ALL_INDICATORS_PUBLISHED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(2, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1]).length);
            assertEquals(indicatorUuid2, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(indicatorUuid3, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
        }
    }

    @Test
    public void testArchiveIndicatorsSystem() throws Exception {

        String uuid = INDICATORS_SYSTEM_3;
        String versionNumber = INDICATORS_SYSTEM_3_VERSION;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
            assertEquals(null, indicatorsSystemDto.getProductionVersion());
            assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDto.getDiffusionVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemDto.getProcStatus());
        }

        // Archive
        IndicatorsSystemDto indicatorsSystemDtoUpdated = indicatorsServiceFacade.archiveIndicatorsSystem(getServiceContext(), uuid);

        // Validation
        {
            assertEquals(null, indicatorsSystemDtoUpdated.getProductionVersion());
            assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDtoUpdated.getDiffusionVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.ARCHIVED, indicatorsSystemDtoUpdated.getProcStatus());

            IndicatorsAsserts.assertEqualsDate("2011-03-03 01:02:04", indicatorsSystemDtoUpdated.getProductionValidationDate());
            assertEquals("user1", indicatorsSystemDtoUpdated.getProductionValidationUser());
            IndicatorsAsserts.assertEqualsDate("2011-04-04 03:02:04", indicatorsSystemDtoUpdated.getDiffusionValidationDate());
            assertEquals("user2", indicatorsSystemDtoUpdated.getDiffusionValidationUser());
            IndicatorsAsserts.assertEqualsDate("2011-05-05 04:02:04", indicatorsSystemDtoUpdated.getPublicationDate());
            assertEquals("user3", indicatorsSystemDtoUpdated.getPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), indicatorsSystemDtoUpdated.getArchiveDate()));
            assertEquals(getServiceContext().getUserId(), indicatorsSystemDtoUpdated.getArchiveUser());
        }
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
            assertEquals(null, indicatorsSystemDto.getProductionVersion());
            assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDto.getDiffusionVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.ARCHIVED, indicatorsSystemDto.getProcStatus());

            IndicatorsAsserts.assertEqualsDate("2011-03-03 01:02:04", indicatorsSystemDto.getProductionValidationDate());
            assertEquals("user1", indicatorsSystemDto.getProductionValidationUser());
            IndicatorsAsserts.assertEqualsDate("2011-04-04 03:02:04", indicatorsSystemDto.getDiffusionValidationDate());
            assertEquals("user2", indicatorsSystemDto.getDiffusionValidationUser());
            IndicatorsAsserts.assertEqualsDate("2011-05-05 04:02:04", indicatorsSystemDto.getPublicationDate());
            assertEquals("user3", indicatorsSystemDto.getPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), indicatorsSystemDto.getArchiveDate()));
            assertEquals(getServiceContext().getUserId(), indicatorsSystemDto.getArchiveUser());
        }
    }

    @Test
    public void testArchiveIndicatorsSystemWithProductionVersion() throws Exception {

        String uuid = INDICATORS_SYSTEM_1;
        String diffusionVersion = "1.000";
        String productionVersion = "2.000";

        {
            IndicatorsSystemDto indicatorsSystemDtoV1 = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, diffusionVersion);
            IndicatorsSystemDto indicatorsSystemDtoV2 = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, productionVersion);
            assertEquals(productionVersion, indicatorsSystemDtoV1.getProductionVersion());
            assertEquals(diffusionVersion, indicatorsSystemDtoV1.getDiffusionVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemDtoV1.getProcStatus());
            assertEquals(IndicatorsSystemProcStatusEnum.DRAFT, indicatorsSystemDtoV2.getProcStatus());
        }

        // Archive
        indicatorsServiceFacade.archiveIndicatorsSystem(getServiceContext(), uuid);

        // Validation
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, diffusionVersion);
            assertEquals(productionVersion, indicatorsSystemDto.getProductionVersion());
            assertEquals(diffusionVersion, indicatorsSystemDto.getDiffusionVersion());
            assertEquals(IndicatorsSystemProcStatusEnum.ARCHIVED, indicatorsSystemDto.getProcStatus());
        }
    }

    @Test
    public void testArchiveIndicatorsSystemErrorNotExists() throws Exception {

        try {
            indicatorsServiceFacade.archiveIndicatorsSystem(getServiceContext(), NOT_EXISTS);
            fail("Indicators system not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testArchiveIndicatorsSystemErrorWrongProcStatusProduction() throws Exception {

        String uuid = INDICATORS_SYSTEM_2;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, "1.000");
            assertEquals(IndicatorsSystemProcStatusEnum.DRAFT, indicatorsSystemDto.getProcStatus());
            assertEquals("1.000", indicatorsSystemDto.getProductionVersion());
            assertEquals(null, indicatorsSystemDto.getDiffusionVersion());
        }

        try {
            indicatorsServiceFacade.archiveIndicatorsSystem(getServiceContext(), uuid);
            fail("Indicators system is not published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, ((IndicatorsSystemProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);

        }
    }

    @Test
    public void testArchiveIndicatorsSystemErrorWrongProcStatusDiffusion() throws Exception {

        String uuid = INDICATORS_SYSTEM_8;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, "1.000");
            assertEquals(IndicatorsSystemProcStatusEnum.ARCHIVED, indicatorsSystemDto.getProcStatus());
            assertEquals(null, indicatorsSystemDto.getProductionVersion());
            assertEquals("1.000", indicatorsSystemDto.getDiffusionVersion());
        }

        try {
            indicatorsServiceFacade.archiveIndicatorsSystem(getServiceContext(), uuid);
            fail("Indicators system is not published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, ((IndicatorsSystemProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Test
    public void testVersioningIndicatorsSystem() throws Exception {

        String uuid = INDICATORS_SYSTEM_3;
        String newVersionExpected = "12.000";

        IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, null);
        assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDto.getVersionNumber());
        assertEquals(null, indicatorsSystemDto.getProductionVersion());
        assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDto.getDiffusionVersion());

        IndicatorsSystemDto indicatorsSystemDtoVersioned = indicatorsServiceFacade.versioningIndicatorsSystem(getServiceContext(), uuid, VersionTypeEnum.MAJOR);

        // Validate
        assertEquals(newVersionExpected, indicatorsSystemDtoVersioned.getVersionNumber());
        assertEquals(newVersionExpected, indicatorsSystemDtoVersioned.getProductionVersion());
        assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDtoVersioned.getDiffusionVersion());
        IndicatorsAsserts.assertEqualsIndicatorsSystem(indicatorsSystemDto, indicatorsSystemDtoVersioned);

        IndicatorsSystemDto indicatorsSystemDtoProduction = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, newVersionExpected);
        IndicatorsSystemDto indicatorsSystemDtoDiffusion = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, INDICATORS_SYSTEM_3_VERSION);

        IndicatorsAsserts.assertEqualsIndicatorsSystem(indicatorsSystemDtoDiffusion, indicatorsSystemDtoProduction);

        assertEquals(IndicatorsSystemProcStatusEnum.DRAFT, indicatorsSystemDtoProduction.getProcStatus());
        assertEquals(newVersionExpected, indicatorsSystemDtoProduction.getVersionNumber());
        assertEquals(newVersionExpected, indicatorsSystemDtoProduction.getProductionVersion());
        assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDtoProduction.getDiffusionVersion());

        assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemDtoDiffusion.getProcStatus());
        assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDtoDiffusion.getVersionNumber());
        assertEquals(newVersionExpected, indicatorsSystemDtoDiffusion.getProductionVersion());
        assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDtoDiffusion.getDiffusionVersion());

        // Validate structure
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContext(), uuid, newVersionExpected);
        assertEquals(2, indicatorsSystemStructureDto.getElements().size());

        {
            ElementLevelDto elementLevelDto = indicatorsSystemStructureDto.getElements().get(0);
            IndicatorsAsserts.assertEqualsInternationalString(elementLevelDto.getDimension().getTitle(), "es", "Título IndSys-3-v1-Dimension-1", "en", "Title IndSys-3-v1-Dimension-1");
            assertEquals(Long.valueOf(1), elementLevelDto.getOrderInLevel());
            assertEquals(2, elementLevelDto.getSubelements().size());

            // Children
            {
                ElementLevelDto elementLevelChildDto = elementLevelDto.getSubelements().get(0);
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
            IndicatorsAsserts.assertEqualsInternationalString(elementLevelDto.getIndicatorInstance().getTitle(), "es", "Título IndSys-3-v1-IInstance-2", "en", "Title IndSys-3-v1-IInstance-2");
            assertEquals(Long.valueOf(2), elementLevelDto.getOrderInLevel());
            assertEquals(0, elementLevelDto.getSubelements().size());
        }
    }

    @Test
    public void testVersioningIndicatorsSystemVersionMinor() throws Exception {

        String uuid = INDICATORS_SYSTEM_3;
        String newVersionExpected = "11.034";

        IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, null);
        assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDto.getVersionNumber());
        assertEquals(null, indicatorsSystemDto.getProductionVersion());
        assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDto.getDiffusionVersion());

        IndicatorsSystemDto indicatorsSystemDtoVersioned = indicatorsServiceFacade.versioningIndicatorsSystem(getServiceContext(), uuid, VersionTypeEnum.MINOR);

        // Validate
        assertEquals(newVersionExpected, indicatorsSystemDtoVersioned.getVersionNumber());
        assertEquals(newVersionExpected, indicatorsSystemDtoVersioned.getProductionVersion());
        assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDtoVersioned.getDiffusionVersion());
        IndicatorsAsserts.assertEqualsIndicatorsSystem(indicatorsSystemDto, indicatorsSystemDtoVersioned);

        {
            IndicatorsSystemDto indicatorsSystemDtoProduction = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, newVersionExpected);
            IndicatorsSystemDto indicatorsSystemDtoDiffusion = indicatorsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, INDICATORS_SYSTEM_3_VERSION);

            IndicatorsAsserts.assertEqualsIndicatorsSystem(indicatorsSystemDtoDiffusion, indicatorsSystemDtoProduction);

            assertEquals(IndicatorsSystemProcStatusEnum.DRAFT, indicatorsSystemDtoProduction.getProcStatus());
            assertEquals(newVersionExpected, indicatorsSystemDtoProduction.getVersionNumber());
            assertEquals(newVersionExpected, indicatorsSystemDtoProduction.getProductionVersion());
            assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDtoProduction.getDiffusionVersion());

            assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemDtoDiffusion.getProcStatus());
            assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDtoDiffusion.getVersionNumber());
            assertEquals(newVersionExpected, indicatorsSystemDtoDiffusion.getProductionVersion());
            assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDtoDiffusion.getDiffusionVersion());
        }
    }

    @Test
    public void testVersioningIndicatorsSystemErrorNotExists() throws Exception {

        try {
            indicatorsServiceFacade.versioningIndicatorsSystem(getServiceContext(), NOT_EXISTS, VersionTypeEnum.MINOR);
            fail("Indicators system not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testVersioningIndicatorsSystemErrorAlreadyExistsProduction() throws Exception {

        String uuid = INDICATORS_SYSTEM_2;

        try {
            indicatorsServiceFacade.versioningIndicatorsSystem(getServiceContext(), uuid, VersionTypeEnum.MINOR);
            fail("Indicators system already exists in production");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, ((IndicatorsSystemProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(IndicatorsSystemProcStatusEnum.ARCHIVED, ((IndicatorsSystemProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
        }
    }

    @Test
    public void testFindIndicatorsSystems() throws Exception {

        // Retrieve last versions...
        List<IndicatorsSystemDto> indicatorsSystemsDto = indicatorsServiceFacade.findIndicatorsSystems(getServiceContext(), null);
        assertEquals(9, indicatorsSystemsDto.size());

        assertEquals(INDICATORS_SYSTEM_1, indicatorsSystemsDto.get(0).getUuid());
        assertEquals(IndicatorsSystemProcStatusEnum.DRAFT, indicatorsSystemsDto.get(0).getProcStatus());

        assertEquals(INDICATORS_SYSTEM_2, indicatorsSystemsDto.get(1).getUuid());
        assertEquals(IndicatorsSystemProcStatusEnum.DRAFT, indicatorsSystemsDto.get(1).getProcStatus());

        assertEquals(INDICATORS_SYSTEM_3, indicatorsSystemsDto.get(2).getUuid());
        assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemsDto.get(2).getProcStatus());

        assertEquals(INDICATORS_SYSTEM_4, indicatorsSystemsDto.get(3).getUuid());
        assertEquals(IndicatorsSystemProcStatusEnum.PRODUCTION_VALIDATION, indicatorsSystemsDto.get(3).getProcStatus());

        assertEquals(INDICATORS_SYSTEM_5, indicatorsSystemsDto.get(4).getUuid());
        assertEquals(IndicatorsSystemProcStatusEnum.DIFFUSION_VALIDATION, indicatorsSystemsDto.get(4).getProcStatus());

        assertEquals(INDICATORS_SYSTEM_6, indicatorsSystemsDto.get(5).getUuid());
        assertEquals(IndicatorsSystemProcStatusEnum.DIFFUSION_VALIDATION, indicatorsSystemsDto.get(5).getProcStatus());

        assertEquals(INDICATORS_SYSTEM_7, indicatorsSystemsDto.get(6).getUuid());
        assertEquals(IndicatorsSystemProcStatusEnum.DIFFUSION_VALIDATION, indicatorsSystemsDto.get(6).getProcStatus());

        assertEquals(INDICATORS_SYSTEM_8, indicatorsSystemsDto.get(7).getUuid());
        assertEquals(IndicatorsSystemProcStatusEnum.ARCHIVED, indicatorsSystemsDto.get(7).getProcStatus());

        assertEquals(INDICATORS_SYSTEM_9, indicatorsSystemsDto.get(8).getUuid());
        assertEquals(IndicatorsSystemProcStatusEnum.VALIDATION_REJECTED, indicatorsSystemsDto.get(8).getProcStatus());
    }

    @Test
    public void testFindIndicatorsSystemsPublished() throws Exception {

        List<IndicatorsSystemDto> indicatorsSystemsDto = indicatorsServiceFacade.findIndicatorsSystemsPublished(getServiceContext(), null);
        assertEquals(3, indicatorsSystemsDto.size());

        assertEquals(INDICATORS_SYSTEM_1, indicatorsSystemsDto.get(0).getUuid());
        assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemsDto.get(0).getProcStatus());

        assertEquals(INDICATORS_SYSTEM_3, indicatorsSystemsDto.get(1).getUuid());
        assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemsDto.get(1).getProcStatus());

        assertEquals(INDICATORS_SYSTEM_6, indicatorsSystemsDto.get(2).getUuid());
        assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemsDto.get(2).getProcStatus());
    }
    
    @Test
    public void testRetrieveIndicatorsSystemsPublishedWithIndicator() throws Exception {

        {
            // Indicator 1
            List<IndicatorsSystemDto> indicatorsSystemsDto = indicatorsServiceFacade.retrieveIndicatorsSystemPublishedForIndicator(getServiceContext(), INDICATOR_1);
            assertEquals(3, indicatorsSystemsDto.size());
    
            assertEquals(INDICATORS_SYSTEM_1, indicatorsSystemsDto.get(0).getUuid());
            assertEquals("1.000", indicatorsSystemsDto.get(0).getVersionNumber());
            assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemsDto.get(0).getProcStatus());
            
            assertEquals(INDICATORS_SYSTEM_3, indicatorsSystemsDto.get(1).getUuid());
            assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemsDto.get(1).getVersionNumber());
            assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemsDto.get(1).getProcStatus());

            assertEquals(INDICATORS_SYSTEM_6, indicatorsSystemsDto.get(2).getUuid());
            assertEquals("1.000", indicatorsSystemsDto.get(2).getVersionNumber());
            assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemsDto.get(2).getProcStatus());
        }
        {
            // Indicator 2
            List<IndicatorsSystemDto> indicatorsSystemsDto = indicatorsServiceFacade.retrieveIndicatorsSystemPublishedForIndicator(getServiceContext(), INDICATOR_2);
            assertEquals(0, indicatorsSystemsDto.size());
        }
        {
            // Indicator 3
            List<IndicatorsSystemDto> indicatorsSystemsDto = indicatorsServiceFacade.retrieveIndicatorsSystemPublishedForIndicator(getServiceContext(), INDICATOR_3);
            assertEquals(1, indicatorsSystemsDto.size());
            
            assertEquals(INDICATORS_SYSTEM_6, indicatorsSystemsDto.get(0).getUuid());
            assertEquals("1.000", indicatorsSystemsDto.get(0).getVersionNumber());
            assertEquals(IndicatorsSystemProcStatusEnum.PUBLISHED, indicatorsSystemsDto.get(0).getProcStatus());
        }
    }

    @Test
    public void testRetrieveDimension() throws Exception {

        DimensionDto dimensionDto = indicatorsServiceFacade.retrieveDimension(getServiceContext(), DIMENSION_1_INDICATORS_SYSTEM_1_V2);

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
    public void testRetrieveDimensionErrorParameterRequired() throws Exception {

        String uuid = null;

        try {
            indicatorsServiceFacade.retrieveDimension(getServiceContext(), uuid);
            fail("parameter required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("UUID", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRetrieveDimensionErrorNotExists() throws Exception {

        String uuid = NOT_EXISTS;

        try {
            indicatorsServiceFacade.retrieveDimension(getServiceContext(), uuid);
            fail("No exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DIMENSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateDimension() throws Exception {

        // Create dimension
        DimensionDto dimensionDto = new DimensionDto();
        dimensionDto.setTitle(IndicatorsMocks.mockInternationalString());
        dimensionDto.setParentUuid(null);
        dimensionDto.setOrderInLevel(Long.valueOf(5));

        String uuidIndicatorsSystem = INDICATORS_SYSTEM_1;
        DimensionDto dimensionDtoCreated = indicatorsServiceFacade.createDimension(getServiceContext(), uuidIndicatorsSystem, dimensionDto);
        assertNotNull(dimensionDtoCreated.getUuid());

        // Retrieve dimension
        DimensionDto dimensionDtoRetrieved = indicatorsServiceFacade.retrieveDimension(getServiceContext(), dimensionDtoCreated.getUuid());
        IndicatorsAsserts.assertEqualsDimension(dimensionDto, dimensionDtoRetrieved);

        // Validate new structure
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContext(), INDICATORS_SYSTEM_1, "2.000");
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
    public void testCreateDimensionWithOrderInMiddle() throws Exception {

        // Create dimension
        DimensionDto dimensionDto = new DimensionDto();
        dimensionDto.setTitle(IndicatorsMocks.mockInternationalString());
        dimensionDto.setParentUuid(null);
        dimensionDto.setOrderInLevel(Long.valueOf(2));

        String uuidIndicatorsSystem = INDICATORS_SYSTEM_1;
        DimensionDto dimensionDtoCreated = indicatorsServiceFacade.createDimension(getServiceContext(), uuidIndicatorsSystem, dimensionDto);
        assertNotNull(dimensionDtoCreated.getUuid());

        // Retrieve dimension
        DimensionDto dimensionDtoRetrieved = indicatorsServiceFacade.retrieveDimension(getServiceContext(), dimensionDtoCreated.getUuid());
        IndicatorsAsserts.assertEqualsDimension(dimensionDto, dimensionDtoRetrieved);

        // Validate new structure
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContext(), INDICATORS_SYSTEM_1, "2.000");
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
    public void testCreateDimensionSubdimension() throws Exception {

        String parentUuid = DIMENSION_1_INDICATORS_SYSTEM_1_V2;

        // Create dimension
        DimensionDto dimensionDto = new DimensionDto();
        dimensionDto.setTitle(IndicatorsMocks.mockInternationalString());
        dimensionDto.setParentUuid(DIMENSION_1_INDICATORS_SYSTEM_1_V2);
        dimensionDto.setOrderInLevel(Long.valueOf(3));
        String uuidIndicatorsSystem = INDICATORS_SYSTEM_1;
        DimensionDto dimensionDtoCreated = indicatorsServiceFacade.createDimension(getServiceContext(), uuidIndicatorsSystem, dimensionDto);
        assertNotNull(dimensionDtoCreated.getUuid());

        // Retrieve dimension
        DimensionDto dimensionDtoRetrieved = indicatorsServiceFacade.retrieveDimension(getServiceContext(), dimensionDtoCreated.getUuid());
        IndicatorsAsserts.assertEqualsDimension(dimensionDto, dimensionDtoRetrieved);
        assertEquals(parentUuid, dimensionDtoRetrieved.getParentUuid());

        // Validate new structure
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContext(), INDICATORS_SYSTEM_1, "2.000");
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
    public void testCreateDimensionSubdimensionOrderInMiddle() throws Exception {

        String parentUuid = DIMENSION_1_INDICATORS_SYSTEM_1_V2;

        // Create dimension
        DimensionDto dimensionDto = new DimensionDto();
        dimensionDto.setTitle(IndicatorsMocks.mockInternationalString());
        dimensionDto.setParentUuid(DIMENSION_1_INDICATORS_SYSTEM_1_V2);
        dimensionDto.setOrderInLevel(Long.valueOf(2));
        String uuidIndicatorsSystem = INDICATORS_SYSTEM_1;
        DimensionDto dimensionDtoCreated = indicatorsServiceFacade.createDimension(getServiceContext(), uuidIndicatorsSystem, dimensionDto);
        assertNotNull(dimensionDtoCreated.getUuid());

        // Retrieve dimension
        DimensionDto dimensionDtoRetrieved = indicatorsServiceFacade.retrieveDimension(getServiceContext(), dimensionDtoCreated.getUuid());
        IndicatorsAsserts.assertEqualsDimension(dimensionDto, dimensionDtoRetrieved);
        assertEquals(parentUuid, dimensionDtoRetrieved.getParentUuid());

        // Validate new structure
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContext(), INDICATORS_SYSTEM_1, "2.000");
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
    public void testCreateDimensionSubSubdimension() throws Exception {

        String parentUuid = DIMENSION_1A_INDICATORS_SYSTEM_1_V2;

        // Create subdimension
        DimensionDto dimensionDto = new DimensionDto();
        dimensionDto.setTitle(IndicatorsMocks.mockInternationalString());
        dimensionDto.setParentUuid(parentUuid);
        dimensionDto.setOrderInLevel(Long.valueOf(1));
        String uuidIndicatorsSystem = INDICATORS_SYSTEM_1;
        DimensionDto dimensionDtoCreated = indicatorsServiceFacade.createDimension(getServiceContext(), uuidIndicatorsSystem, dimensionDto);
        assertNotNull(dimensionDtoCreated.getUuid());

        // Validate
        DimensionDto dimensionDtoRetrieved = indicatorsServiceFacade.retrieveDimension(getServiceContext(), dimensionDtoCreated.getUuid());
        IndicatorsAsserts.assertEqualsDimension(dimensionDtoCreated, dimensionDtoRetrieved);
        assertEquals(parentUuid, dimensionDtoRetrieved.getParentUuid());

        // Validate new structure
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContext(), INDICATORS_SYSTEM_1, "2.000");
        ElementLevelDto elementLevelDto = indicatorsSystemStructureDto.getElements().get(1).getSubelements().get(0);
        assertEquals(parentUuid, elementLevelDto.getDimension().getUuid());
        assertEquals(1, elementLevelDto.getSubelements().size());
        assertEquals(dimensionDtoCreated.getUuid(), elementLevelDto.getSubelements().get(0).getDimension().getUuid());
        assertEquals(Long.valueOf(1), elementLevelDto.getSubelements().get(0).getOrderInLevel());
    }

    @Test
    public void testCreateDimensionErrorParametersRequired() throws Exception {

        DimensionDto dimensionDto = new DimensionDto();
        dimensionDto.setTitle(null);
        dimensionDto.setOrderInLevel(null);
        try {
            indicatorsServiceFacade.createDimension(getServiceContext(), INDICATORS_SYSTEM_1, dimensionDto);
            fail("parameters required");
        } catch (MetamacException e) {
            assertEquals(2, e.getExceptionItems().size());

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("DIMENSION.TITLE", e.getExceptionItems().get(0).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(1).getCode());
            assertEquals(1, e.getExceptionItems().get(1).getMessageParameters().length);
            assertEquals("DIMENSION.ORDER_IN_LEVEL", e.getExceptionItems().get(1).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateDimensionErrorOrderIncorrect() throws Exception {

        // Create dimension
        DimensionDto dimensionDto = new DimensionDto();
        dimensionDto.setTitle(IndicatorsMocks.mockInternationalString());
        dimensionDto.setOrderInLevel(Long.MAX_VALUE);

        try {
            indicatorsServiceFacade.createDimension(getServiceContext(), INDICATORS_SYSTEM_1, dimensionDto);
            fail("order incorrect");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("ORDER_IN_LEVEL", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateDimensionErrorOrderIncorrectNegative() throws Exception {

        // Create dimension
        DimensionDto dimensionDto = new DimensionDto();
        dimensionDto.setTitle(IndicatorsMocks.mockInternationalString());
        dimensionDto.setOrderInLevel(Long.MIN_VALUE);

        try {
            indicatorsServiceFacade.createDimension(getServiceContext(), INDICATORS_SYSTEM_1, dimensionDto);
            fail("order incorrect");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("DIMENSION.ORDER_IN_LEVEL", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateDimensionErrorIndicatorsSystemNotExists() throws Exception {

        DimensionDto dimensionDto = new DimensionDto();
        dimensionDto.setTitle(IndicatorsMocks.mockInternationalString());
        dimensionDto.setOrderInLevel(Long.valueOf(1));
        try {
            indicatorsServiceFacade.createDimension(getServiceContext(), NOT_EXISTS, dimensionDto);
            fail("Indicators system not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateDimensionErrorIndicatorsSystemHasNotVersionInProduction() throws Exception {

        String indicatorsSystemUuid = INDICATORS_SYSTEM_3;
        DimensionDto dimensionDto = new DimensionDto();
        dimensionDto.setTitle(IndicatorsMocks.mockInternationalString());
        dimensionDto.setOrderInLevel(Long.valueOf(1));

        try {
            indicatorsServiceFacade.createDimension(getServiceContext(), indicatorsSystemUuid, dimensionDto);
            fail("Indicators system not in production");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_IN_PRODUCTION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(indicatorsSystemUuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateDimensionSubdimensionErrorDimensionNotExists() throws Exception {

        DimensionDto dimensionDto = new DimensionDto();
        dimensionDto.setTitle(IndicatorsMocks.mockInternationalString());
        dimensionDto.setParentUuid(DIMENSION_NOT_EXISTS);
        dimensionDto.setOrderInLevel(Long.valueOf(1));

        try {
            indicatorsServiceFacade.createDimension(getServiceContext(), INDICATORS_SYSTEM_1, dimensionDto);
            fail("dimension id not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DIMENSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(DIMENSION_NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateDimensionSubdimensionErrorDimensionNotExistsInIndicatorsSystem() throws Exception {

        DimensionDto dimensionDto = new DimensionDto();
        dimensionDto.setTitle(IndicatorsMocks.mockInternationalString());
        dimensionDto.setParentUuid(DIMENSION_1_INDICATORS_SYSTEM_1_V2);
        dimensionDto.setOrderInLevel(Long.valueOf(1));

        try {
            indicatorsServiceFacade.createDimension(getServiceContext(), INDICATORS_SYSTEM_2, dimensionDto);
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
    public void testDeleteDimension() throws Exception {

        String uuid = DIMENSION_1_INDICATORS_SYSTEM_1_V2;

        // Retrieve
        DimensionDto dimensionDto = indicatorsServiceFacade.retrieveDimension(getServiceContext(), uuid);
        assertEquals(Long.valueOf(2), dimensionDto.getOrderInLevel());

        // Delete dimension
        indicatorsServiceFacade.deleteDimension(getServiceContext(), uuid);

        // Validation
        try {
            indicatorsServiceFacade.retrieveDimension(getServiceContext(), uuid);
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
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContext(), INDICATORS_SYSTEM_1, "2.000");
        assertEquals(3, indicatorsSystemStructureDto.getElements().size());
        assertEquals(INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(0).getIndicatorInstance().getUuid());
        assertEquals(Long.valueOf(1), indicatorsSystemStructureDto.getElements().get(0).getOrderInLevel());
        assertEquals(INDICATOR_INSTANCE_2_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(1).getIndicatorInstance().getUuid());
        assertEquals(Long.valueOf(2), indicatorsSystemStructureDto.getElements().get(1).getOrderInLevel());
        assertEquals(DIMENSION_2_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(2).getDimension().getUuid());
        assertEquals(Long.valueOf(3), indicatorsSystemStructureDto.getElements().get(2).getOrderInLevel());

        // Retrieve at least one subdimension to checks it is deleted
        try {
            indicatorsServiceFacade.retrieveDimension(getServiceContext(), DIMENSION_1A_INDICATORS_SYSTEM_1_V2);
            fail("Subimension deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DIMENSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(DIMENSION_1A_INDICATORS_SYSTEM_1_V2, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testDeleteDimensionSubdimension() throws Exception {

        String uuid = DIMENSION_1B_INDICATORS_SYSTEM_1_V2;

        // Delete dimension
        indicatorsServiceFacade.deleteDimension(getServiceContext(), uuid);

        // Validation
        try {
            indicatorsServiceFacade.retrieveDimension(getServiceContext(), uuid);
            fail("Dimension deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DIMENSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }

        // Validate new structure
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContext(), INDICATORS_SYSTEM_1, "2.000");
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
    public void testDeleteDimensionErrorIndicatorsSystemVersionPublished() throws Exception {

        try {
            indicatorsServiceFacade.deleteDimension(getServiceContext(), DIMENSION_1_INDICATORS_SYSTEM_3);
            fail("Indicators system not in production");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(INDICATORS_SYSTEM_3, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorsSystemProcStatusEnum.DRAFT, ((IndicatorsSystemProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(IndicatorsSystemProcStatusEnum.VALIDATION_REJECTED, ((IndicatorsSystemProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
            assertEquals(IndicatorsSystemProcStatusEnum.PRODUCTION_VALIDATION, ((IndicatorsSystemProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[2]);
            assertEquals(IndicatorsSystemProcStatusEnum.DIFFUSION_VALIDATION, ((IndicatorsSystemProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[3]);
        }
    }

    @Test
    public void testDeleteDimensionErrorNotExists() throws Exception {

        String uuid = NOT_EXISTS;

        // Validation
        try {
            indicatorsServiceFacade.deleteDimension(getServiceContext(), uuid);
            fail("Dimension not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DIMENSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testretrieveDimensionsByIndicatorsSystem() throws Exception {

        String uuidIndicatorsSystem = INDICATORS_SYSTEM_1;

        // Version 1.000
        {
            List<DimensionDto> dimensionsDto = indicatorsServiceFacade.retrieveDimensionsByIndicatorsSystem(getServiceContext(), uuidIndicatorsSystem, "1.000");
            assertEquals(1, dimensionsDto.size());
            assertEquals(DIMENSION_1_INDICATORS_SYSTEM_1_V1, dimensionsDto.get(0).getUuid());
        }

        // Version 2.000
        {
            List<DimensionDto> dimensionsDto = indicatorsServiceFacade.retrieveDimensionsByIndicatorsSystem(getServiceContext(), uuidIndicatorsSystem, "2.000");
            assertEquals(5, dimensionsDto.size());

            assertEquals(DIMENSION_1A_INDICATORS_SYSTEM_1_V2, dimensionsDto.get(0).getUuid());
            assertEquals(DIMENSION_1B_INDICATORS_SYSTEM_1_V2, dimensionsDto.get(1).getUuid());
            assertEquals(DIMENSION_1BA_INDICATORS_SYSTEM_1_V2, dimensionsDto.get(2).getUuid());
            assertEquals(DIMENSION_1_INDICATORS_SYSTEM_1_V2, dimensionsDto.get(3).getUuid());
            assertEquals(DIMENSION_2_INDICATORS_SYSTEM_1_V2, dimensionsDto.get(4).getUuid());
        }
    }

    @Test
    public void testretrieveDimensionsByIndicatorsSystemErrorNotExists() throws Exception {

        String uuid = NOT_EXISTS;

        // Validation
        try {
            indicatorsServiceFacade.retrieveDimensionsByIndicatorsSystem(getServiceContext(), uuid, "1.000");
            fail("Indicators system not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_VERSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals("1.000", e.getExceptionItems().get(0).getMessageParameters()[1]);
        }
    }

    @Test
    public void testUpdateDimension() throws Exception {

        String uuid = DIMENSION_1_INDICATORS_SYSTEM_1_V2;
        DimensionDto dimensionDto = indicatorsServiceFacade.retrieveDimension(getServiceContext(), uuid);
        dimensionDto.setTitle(IndicatorsMocks.mockInternationalString());

        // Update
        DimensionDto dimensionDtoUpdated = indicatorsServiceFacade.updateDimension(getServiceContext(), dimensionDto);

        // Validation
        IndicatorsAsserts.assertEqualsDimension(dimensionDto, dimensionDtoUpdated);
        dimensionDtoUpdated = indicatorsServiceFacade.retrieveDimension(getServiceContext(), uuid);
        IndicatorsAsserts.assertEqualsDimension(dimensionDto, dimensionDtoUpdated);
    }

    @Test
    public void testUpdateDimensionErrorChangeParentDimensionAndOrder() throws Exception {

        DimensionDto dimensionDto = indicatorsServiceFacade.retrieveDimension(getServiceContext(), DIMENSION_1A_INDICATORS_SYSTEM_1_V2);
        assertEquals(DIMENSION_1_INDICATORS_SYSTEM_1_V2, dimensionDto.getParentUuid());
        assertEquals(Long.valueOf(1), dimensionDto.getOrderInLevel());
        dimensionDto.setParentUuid(DIMENSION_1BA_INDICATORS_SYSTEM_1_V2);
        dimensionDto.setOrderInLevel(Long.valueOf(2));

        try {
            indicatorsServiceFacade.updateDimension(getServiceContext(), dimensionDto);
            fail("Parent and order changed");
        } catch (MetamacException e) {
            assertEquals(2, e.getExceptionItems().size());

            assertEquals(ServiceExceptionType.METADATA_UNMODIFIABLE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("DIMENSION.PARENT_UUID", e.getExceptionItems().get(0).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_UNMODIFIABLE.getCode(), e.getExceptionItems().get(1).getCode());
            assertEquals(1, e.getExceptionItems().get(1).getMessageParameters().length);
            assertEquals("DIMENSION.ORDER_IN_LEVEL", e.getExceptionItems().get(1).getMessageParameters()[0]);
        }

        // Put parent null
        dimensionDto.setParentUuid(null);
        dimensionDto.setOrderInLevel(Long.valueOf(1));

        try {
            indicatorsServiceFacade.updateDimension(getServiceContext(), dimensionDto);
            fail("Parent changed");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_UNMODIFIABLE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("DIMENSION.PARENT_UUID", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateDimensionErrorIndicatorsSystemPublished() throws Exception {

        DimensionDto dimensionDto = indicatorsServiceFacade.retrieveDimension(getServiceContext(), DIMENSION_1_INDICATORS_SYSTEM_1_V1);

        try {
            indicatorsServiceFacade.updateDimension(getServiceContext(), dimensionDto);
            fail("Indicators system published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(INDICATORS_SYSTEM_1, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorsSystemProcStatusEnum.DRAFT, ((IndicatorsSystemProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(IndicatorsSystemProcStatusEnum.VALIDATION_REJECTED, ((IndicatorsSystemProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
            assertEquals(IndicatorsSystemProcStatusEnum.PRODUCTION_VALIDATION, ((IndicatorsSystemProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[2]);
            assertEquals(IndicatorsSystemProcStatusEnum.DIFFUSION_VALIDATION, ((IndicatorsSystemProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[3]);
        }
    }

    @Test
    public void testUpdateDimensionErrorNotExists() throws Exception {

        DimensionDto dimensionDto = new DimensionDto();
        dimensionDto.setUuid(NOT_EXISTS);
        dimensionDto.setTitle(IndicatorsMocks.mockInternationalString());

        try {
            indicatorsServiceFacade.updateDimension(getServiceContext(), dimensionDto);
            fail("Dimension not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DIMENSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateDimensionLocation() throws Exception {
        // In other test testUpdateDimensionLocation*
    }

    @Test
    public void testUpdateDimensionLocationActualWithoutParentTargetWithParent() throws Exception {

        String uuid = DIMENSION_1_INDICATORS_SYSTEM_1_V2;
        String parentBeforeUuid = null;
        String parentTargetUuid = DIMENSION_2_INDICATORS_SYSTEM_1_V2;

        DimensionDto dimensionDto = indicatorsServiceFacade.retrieveDimension(getServiceContext(), uuid);
        assertEquals(parentBeforeUuid, dimensionDto.getParentUuid());

        // Update location
        DimensionDto dimensionDtoChanged = indicatorsServiceFacade.updateDimensionLocation(getServiceContext(), uuid, parentTargetUuid, Long.valueOf(1));

        // Validate dimension
        assertEquals(parentTargetUuid, dimensionDtoChanged.getParentUuid());
        dimensionDtoChanged = indicatorsServiceFacade.retrieveDimension(getServiceContext(), uuid);
        assertEquals(parentTargetUuid, dimensionDtoChanged.getParentUuid());

        // Validate source
        {
            IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContext(), INDICATORS_SYSTEM_1, "2.000");
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
            IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContext(), INDICATORS_SYSTEM_1, "2.000");
            ElementLevelDto elementLevelDto = indicatorsSystemStructureDto.getElements().get(2);
            assertEquals(parentTargetUuid, elementLevelDto.getDimension().getUuid());
            assertEquals(1, elementLevelDto.getSubelements().size());
            assertEquals(uuid, elementLevelDto.getSubelements().get(0).getDimension().getUuid());
            assertEquals(Long.valueOf(1), indicatorsSystemStructureDto.getElements().get(0).getOrderInLevel());
        }
    }

    @Test
    public void testUpdateDimensionLocationActualWithParentTargetWithoutParent() throws Exception {

        String uuid = DIMENSION_1BA_INDICATORS_SYSTEM_1_V2;
        String parentBeforeUuid = DIMENSION_1B_INDICATORS_SYSTEM_1_V2;
        String parentTargetUuid = null;

        // Retrieve actual
        DimensionDto dimensionDto = indicatorsServiceFacade.retrieveDimension(getServiceContext(), uuid);
        assertEquals(parentBeforeUuid, dimensionDto.getParentUuid());

        // Update location
        indicatorsServiceFacade.updateDimensionLocation(getServiceContext(), uuid, parentTargetUuid, Long.valueOf(2));

        // Validate parent is changed in dimension
        DimensionDto dimensionDtoChanged = indicatorsServiceFacade.retrieveDimension(getServiceContext(), uuid);
        assertNull(dimensionDtoChanged.getParentUuid());

        // Validate source
        {
            IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContext(), INDICATORS_SYSTEM_1, "2.000");
            ElementLevelDto elementLevelDto = indicatorsSystemStructureDto.getElements().get(2).getSubelements().get(1);
            assertEquals(parentBeforeUuid, elementLevelDto.getDimension().getUuid());
            assertEquals(1, elementLevelDto.getSubelements().size());
        }

        // Validate target
        {
            IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContext(), INDICATORS_SYSTEM_1, "2.000");
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
    public void testUpdateDimensionLocationChangingDimensionParent() throws Exception {

        String uuid = DIMENSION_1BA_INDICATORS_SYSTEM_1_V2;
        String parentBeforeUuid = DIMENSION_1B_INDICATORS_SYSTEM_1_V2;
        String parentTargetUuid = DIMENSION_1A_INDICATORS_SYSTEM_1_V2;

        // Retrieve actual
        DimensionDto dimensionDto = indicatorsServiceFacade.retrieveDimension(getServiceContext(), uuid);
        assertEquals(parentBeforeUuid, dimensionDto.getParentUuid());

        // Update location
        DimensionDto dimensionDtoChanged = indicatorsServiceFacade.updateDimensionLocation(getServiceContext(), uuid, parentTargetUuid, Long.valueOf(1));

        // Validate dimension
        assertEquals(parentTargetUuid, dimensionDtoChanged.getParentUuid());
        dimensionDtoChanged = indicatorsServiceFacade.retrieveDimension(getServiceContext(), uuid);
        assertEquals(parentTargetUuid, dimensionDtoChanged.getParentUuid());

        // Validate source
        {
            IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContext(), INDICATORS_SYSTEM_1, "2.000");
            ElementLevelDto elementLevelDto = indicatorsSystemStructureDto.getElements().get(1).getSubelements().get(1);
            assertEquals(parentBeforeUuid, elementLevelDto.getDimension().getUuid());
            assertEquals(1, elementLevelDto.getSubelements().size());
        }

        // Validate target
        {
            IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContext(), INDICATORS_SYSTEM_1, "2.000");
            ElementLevelDto elementLevelDto = indicatorsSystemStructureDto.getElements().get(1).getSubelements().get(0);
            assertEquals(parentTargetUuid, elementLevelDto.getDimension().getUuid());
            assertEquals(1, elementLevelDto.getSubelements().size());
            assertEquals(uuid, elementLevelDto.getSubelements().get(0).getDimension().getUuid());
        }

    }

    @Test
    public void testUpdateDimensionLocationActualSameParentOnlyChangeOrderWithoutParent() throws Exception {

        String uuid = DIMENSION_1_INDICATORS_SYSTEM_1_V2;

        // Retrieve actual
        DimensionDto dimensionDto = indicatorsServiceFacade.retrieveDimension(getServiceContext(), uuid);
        assertNull(dimensionDto.getParentUuid());
        assertEquals(Long.valueOf(2), dimensionDto.getOrderInLevel());

        // Update location
        DimensionDto dimensionDtoChanged = indicatorsServiceFacade.updateDimensionLocation(getServiceContext(), uuid, dimensionDto.getParentUuid(), Long.valueOf(4));

        // Validate dimension
        assertNull(dimensionDtoChanged.getParentUuid());
        assertEquals(Long.valueOf(4), dimensionDtoChanged.getOrderInLevel());
        dimensionDtoChanged = indicatorsServiceFacade.retrieveDimension(getServiceContext(), uuid);
        assertNull(dimensionDtoChanged.getParentUuid());
        assertEquals(Long.valueOf(4), dimensionDtoChanged.getOrderInLevel());

        // Validate source and target
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContext(), INDICATORS_SYSTEM_1, "2.000");
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
    public void testUpdateDimensionLocationActualSameParentOnlyChangeOrderWithParent() throws Exception {

        String uuid = DIMENSION_1BA_INDICATORS_SYSTEM_1_V2;
        String parentUuid = DIMENSION_1B_INDICATORS_SYSTEM_1_V2;

        // Retrieve actual
        DimensionDto dimensionDto = indicatorsServiceFacade.retrieveDimension(getServiceContext(), uuid);
        assertEquals(parentUuid, dimensionDto.getParentUuid());
        assertEquals(Long.valueOf(1), dimensionDto.getOrderInLevel());

        // Update location
        indicatorsServiceFacade.updateDimensionLocation(getServiceContext(), uuid, dimensionDto.getParentUuid(), Long.valueOf(2));

        // Validate dimension
        DimensionDto dimensionDtoChanged = indicatorsServiceFacade.retrieveDimension(getServiceContext(), uuid);
        assertEquals(parentUuid, dimensionDtoChanged.getParentUuid());
        assertEquals(Long.valueOf(2), dimensionDtoChanged.getOrderInLevel());

        // Validate source and target
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContext(), INDICATORS_SYSTEM_1, "2.000");
        ElementLevelDto elementLevelDto = indicatorsSystemStructureDto.getElements().get(1).getSubelements().get(1);
        assertEquals(parentUuid, elementLevelDto.getDimension().getUuid());
        assertEquals(2, elementLevelDto.getSubelements().size());
        assertEquals(INDICATOR_INSTANCE_3_INDICATORS_SYSTEM_1_V2, elementLevelDto.getSubelements().get(0).getIndicatorInstance().getUuid());
        assertEquals(Long.valueOf(1), elementLevelDto.getSubelements().get(0).getIndicatorInstance().getOrderInLevel());
        assertEquals(uuid, elementLevelDto.getSubelements().get(1).getDimension().getUuid());
        assertEquals(Long.valueOf(2), elementLevelDto.getSubelements().get(1).getDimension().getOrderInLevel());
    }

    @Test
    public void testUpdateDimensionLocationErrorParentIsChild() throws Exception {

        String uuid = DIMENSION_1_INDICATORS_SYSTEM_1_V2;
        String parentTargetUuid = DIMENSION_1BA_INDICATORS_SYSTEM_1_V2;

        try {
            indicatorsServiceFacade.updateDimensionLocation(getServiceContext(), uuid, parentTargetUuid, Long.valueOf(1));
            fail("It is child");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("PARENT_TARGET_UUID", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateDimensionLocationErrorNotExists() throws Exception {

        String uuid = NOT_EXISTS;

        // Validation
        try {
            indicatorsServiceFacade.updateDimensionLocation(getServiceContext(), uuid, null, Long.valueOf(1));
            fail("Dimension not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DIMENSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateDimensionLocationErrorOrderIncorrect() throws Exception {

        try {
            indicatorsServiceFacade.updateDimensionLocation(getServiceContext(), DIMENSION_1_INDICATORS_SYSTEM_1_V2, null, Long.MAX_VALUE);
            fail("order incorrect");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("ORDER_IN_LEVEL", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRetrieveIndicatorInstance() throws Exception {

        IndicatorInstanceDto indicatorInstanceDto = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContext(), INDICATOR_INSTANCE_3_INDICATORS_SYSTEM_1_V2);

        assertNotNull(indicatorInstanceDto);
        assertEquals(INDICATOR_INSTANCE_3_INDICATORS_SYSTEM_1_V2, indicatorInstanceDto.getUuid());
        assertEquals(INDICATOR_2, indicatorInstanceDto.getIndicatorUuid());
        IndicatorsAsserts.assertEqualsInternationalString(indicatorInstanceDto.getTitle(), "es", "Título IndSys-1-v2-IInstance-3", "en", "Title IndSys-1-v2-IInstance-3");
        assertEquals(DIMENSION_1B_INDICATORS_SYSTEM_1_V2, indicatorInstanceDto.getParentUuid());
        assertEquals(TimeGranularityEnum.YEARLY, indicatorInstanceDto.getTimeGranularity());
        assertNull(indicatorInstanceDto.getTimeValue());
        assertEquals(GEOGRAPHICAL_GRANULARITY_1, indicatorInstanceDto.getGeographicalGranularityUuid());
        assertNull(indicatorInstanceDto.getGeographicalValueUuid());
        IndicatorsAsserts.assertEqualsDate("2011-05-05 01:02:04", indicatorInstanceDto.getCreatedDate());
        assertEquals("user1", indicatorInstanceDto.getCreatedBy());
        IndicatorsAsserts.assertEqualsDate("2011-06-06 05:06:07", indicatorInstanceDto.getLastUpdated());
        assertEquals("user2", indicatorInstanceDto.getLastUpdatedBy());
    }

    @Test
    public void testRetrieveIndicatorInstanceErrorParameterRequired() throws Exception {

        String uuid = null;

        try {
            indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContext(), uuid);
            fail("parameter required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("UUID", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRetrieveIndicatorInstanceErrorNotExists() throws Exception {

        String uuid = NOT_EXISTS;

        try {
            indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContext(), uuid);
            fail("No exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_INSTANCE_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorInstance() throws Exception {

        // Create indicator instance
        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setIndicatorUuid(INDICATOR_2);
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorInstanceDto.setParentUuid(null);
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(5));
        indicatorInstanceDto.setGeographicalValueUuid(GEOGRAPHICAL_VALUE_1);
        indicatorInstanceDto.setTimeValue("2012");

        String uuidIndicatorsSystem = INDICATORS_SYSTEM_1;
        IndicatorInstanceDto indicatorInstanceDtoCreated = indicatorsServiceFacade.createIndicatorInstance(getServiceContext(), uuidIndicatorsSystem, indicatorInstanceDto);
        assertNotNull(indicatorInstanceDtoCreated.getUuid());

        // Retrieve indicator instance
        IndicatorInstanceDto indicatorInstanceDtoRetrieved = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContext(), indicatorInstanceDtoCreated.getUuid());
        IndicatorsAsserts.assertEqualsIndicatorInstance(indicatorInstanceDto, indicatorInstanceDtoRetrieved);

        // Validate new structure
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContext(), INDICATORS_SYSTEM_1, "2.000");
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
    public void testCreateIndicatorInstanceWithOrderInMiddle() throws Exception {

        // Create indicator instance
        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setIndicatorUuid(INDICATOR_2);
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorInstanceDto.setParentUuid(null);
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(3));
        indicatorInstanceDto.setGeographicalGranularityUuid(GEOGRAPHICAL_GRANULARITY_1);
        indicatorInstanceDto.setTimeGranularity(TimeGranularityEnum.DAILY);

        String uuidIndicatorsSystem = INDICATORS_SYSTEM_1;
        IndicatorInstanceDto indicatorInstanceDtoCreated = indicatorsServiceFacade.createIndicatorInstance(getServiceContext(), uuidIndicatorsSystem, indicatorInstanceDto);
        assertNotNull(indicatorInstanceDtoCreated.getUuid());

        // Retrieve indicator instance
        IndicatorInstanceDto indicatorInstanceDtoRetrieved = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContext(), indicatorInstanceDtoCreated.getUuid());
        IndicatorsAsserts.assertEqualsIndicatorInstance(indicatorInstanceDto, indicatorInstanceDtoRetrieved);

        // Validate new structure
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContext(), INDICATORS_SYSTEM_1, "2.000");
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
    public void testCreateIndicatorInstanceInDimension() throws Exception {

        String parentUuid = DIMENSION_1_INDICATORS_SYSTEM_1_V2;

        // Create indicator instance
        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setIndicatorUuid("Indicator-1");
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorInstanceDto.setParentUuid(parentUuid);
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(3));
        indicatorInstanceDto.setGeographicalValueUuid(GEOGRAPHICAL_VALUE_1);
        indicatorInstanceDto.setTimeValue("2012");

        String uuidIndicatorsSystem = INDICATORS_SYSTEM_1;
        IndicatorInstanceDto indicatorInstanceDtoCreated = indicatorsServiceFacade.createIndicatorInstance(getServiceContext(), uuidIndicatorsSystem, indicatorInstanceDto);
        assertNotNull(indicatorInstanceDtoCreated.getUuid());

        // Retrieve indicatorInstance
        IndicatorInstanceDto indicatorInstanceDtoRetrieved = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContext(), indicatorInstanceDtoCreated.getUuid());
        IndicatorsAsserts.assertEqualsIndicatorInstance(indicatorInstanceDto, indicatorInstanceDtoRetrieved);
        assertEquals(parentUuid, indicatorInstanceDtoRetrieved.getParentUuid());

        // Validate new structure
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContext(), INDICATORS_SYSTEM_1, "2.000");
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
    public void testCreateIndicatorInstanceInDimensionInMiddle() throws Exception {

        String parentUuid = DIMENSION_1_INDICATORS_SYSTEM_1_V2;

        // Create indicator instance
        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setIndicatorUuid("Indicator-1");
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorInstanceDto.setParentUuid(parentUuid);
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(2));
        indicatorInstanceDto.setTimeValue("2012");

        String uuidIndicatorsSystem = INDICATORS_SYSTEM_1;
        IndicatorInstanceDto indicatorInstanceDtoCreated = indicatorsServiceFacade.createIndicatorInstance(getServiceContext(), uuidIndicatorsSystem, indicatorInstanceDto);
        assertNotNull(indicatorInstanceDtoCreated.getUuid());

        // Retrieve indicatorInstance
        IndicatorInstanceDto indicatorInstanceDtoRetrieved = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContext(), indicatorInstanceDtoCreated.getUuid());
        IndicatorsAsserts.assertEqualsIndicatorInstance(indicatorInstanceDto, indicatorInstanceDtoRetrieved);
        assertEquals(parentUuid, indicatorInstanceDtoRetrieved.getParentUuid());

        // Validate new structure
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContext(), INDICATORS_SYSTEM_1, "2.000");
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
    public void testCreateIndicatorInstanceInSubDimension() throws Exception {

        String parentUuid = DIMENSION_1B_INDICATORS_SYSTEM_1_V2;

        // Create indicator instance
        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setIndicatorUuid("Indicator-1");
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorInstanceDto.setParentUuid(parentUuid);
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(2));
        indicatorInstanceDto.setTimeValue("2012");

        String uuidIndicatorsSystem = INDICATORS_SYSTEM_1;
        IndicatorInstanceDto indicatorInstanceDtoCreated = indicatorsServiceFacade.createIndicatorInstance(getServiceContext(), uuidIndicatorsSystem, indicatorInstanceDto);
        assertNotNull(indicatorInstanceDtoCreated.getUuid());

        // Retrieve indicatorInstance
        IndicatorInstanceDto indicatorInstanceDtoRetrieved = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContext(), indicatorInstanceDtoCreated.getUuid());
        IndicatorsAsserts.assertEqualsIndicatorInstance(indicatorInstanceDto, indicatorInstanceDtoRetrieved);
        assertEquals(parentUuid, indicatorInstanceDtoRetrieved.getParentUuid());

        // Validate new structure
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContext(), INDICATORS_SYSTEM_1, "2.000");
        assertEquals(4, indicatorsSystemStructureDto.getElements().size());
        ElementLevelDto elementLevelDto = indicatorsSystemStructureDto.getElements().get(1).getSubelements().get(1);
        assertEquals(DIMENSION_1B_INDICATORS_SYSTEM_1_V2, elementLevelDto.getDimension().getUuid());
        assertEquals(3, elementLevelDto.getSubelements().size());
        assertEquals(DIMENSION_1BA_INDICATORS_SYSTEM_1_V2, elementLevelDto.getSubelements().get(0).getDimension().getUuid());
        assertEquals(indicatorInstanceDtoCreated.getUuid(), elementLevelDto.getSubelements().get(1).getIndicatorInstance().getUuid());
        assertEquals(INDICATOR_INSTANCE_3_INDICATORS_SYSTEM_1_V2, elementLevelDto.getSubelements().get(2).getIndicatorInstance().getUuid());
    }

    @Test
    public void testCreateIndicatorInstanceErrorParametersRequired() throws Exception {

        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setTitle(null);
        indicatorInstanceDto.setIndicatorUuid(null);
        indicatorInstanceDto.setParentUuid(null);
        indicatorInstanceDto.setOrderInLevel(null);
        indicatorInstanceDto.setTimeGranularity(null);
        try {
            indicatorsServiceFacade.createIndicatorInstance(getServiceContext(), INDICATORS_SYSTEM_1, indicatorInstanceDto);
            fail("parameters required");
        } catch (MetamacException e) {
            assertEquals(4, e.getExceptionItems().size());

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("INDICATOR_INSTANCE.TITLE", e.getExceptionItems().get(0).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(1).getCode());
            assertEquals(1, e.getExceptionItems().get(1).getMessageParameters().length);
            assertEquals("INDICATOR_INSTANCE.INDICATOR_UUID", e.getExceptionItems().get(1).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(2).getCode());
            assertEquals(2, e.getExceptionItems().get(2).getMessageParameters().length);
            assertEquals("INDICATOR_INSTANCE.TIME_GRANULARITY", e.getExceptionItems().get(2).getMessageParameters()[0]);
            assertEquals("INDICATOR_INSTANCE.TIME_VALUE", e.getExceptionItems().get(2).getMessageParameters()[1]);

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(3).getCode());
            assertEquals(1, e.getExceptionItems().get(3).getMessageParameters().length);
            assertEquals("INDICATOR_INSTANCE.ORDER_IN_LEVEL", e.getExceptionItems().get(3).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorInstanceErrorOrderIncorrect() throws Exception {

        // Create indicatorInstance
        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setIndicatorUuid(INDICATOR_2);
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorInstanceDto.setGeographicalValueUuid(GEOGRAPHICAL_VALUE_1);
        indicatorInstanceDto.setTimeValue("2012");
        indicatorInstanceDto.setParentUuid(null);
        indicatorInstanceDto.setOrderInLevel(Long.MAX_VALUE);

        try {
            indicatorsServiceFacade.createIndicatorInstance(getServiceContext(), INDICATORS_SYSTEM_1, indicatorInstanceDto);
            fail("order incorrect");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("ORDER_IN_LEVEL", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorInstanceErrorOrderIncorrectNegative() throws Exception {

        // Create indicatorInstance
        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setIndicatorUuid("Indicator-1");
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorInstanceDto.setGeographicalValueUuid(GEOGRAPHICAL_VALUE_1);
        indicatorInstanceDto.setTimeValue("2012");
        indicatorInstanceDto.setParentUuid(null);
        indicatorInstanceDto.setOrderInLevel(Long.MIN_VALUE);

        try {
            indicatorsServiceFacade.createIndicatorInstance(getServiceContext(), INDICATORS_SYSTEM_1, indicatorInstanceDto);
            fail("order incorrect");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("INDICATOR_INSTANCE.ORDER_IN_LEVEL", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorInstanceErrorTimeValueIncorrect() throws Exception {

        // Create indicatorInstance
        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setIndicatorUuid(INDICATOR_2);
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorInstanceDto.setGeographicalValueUuid(GEOGRAPHICAL_VALUE_1);
        indicatorInstanceDto.setParentUuid(null);
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(1));

        indicatorInstanceDto.setTimeValue("2012a");

        try {
            indicatorsServiceFacade.createIndicatorInstance(getServiceContext(), INDICATORS_SYSTEM_1, indicatorInstanceDto);
            fail("Time value incorrect");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("INDICATOR_INSTANCE.TIME_VALUE", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorInstanceErrorGeographicValueNotExists() throws Exception {

        // Create indicatorInstance
        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setIndicatorUuid(INDICATOR_2);
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorInstanceDto.setTimeValue("2012");
        indicatorInstanceDto.setGeographicalValueUuid(NOT_EXISTS);
        indicatorInstanceDto.setParentUuid(null);
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(1));

        try {
            indicatorsServiceFacade.createIndicatorInstance(getServiceContext(), INDICATORS_SYSTEM_1, indicatorInstanceDto);
            fail("Geographic value not exits");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.GEOGRAPHICAL_VALUE_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(indicatorInstanceDto.getGeographicalValueUuid(), e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorInstanceErrorIndicatorNotExists() throws Exception {

        // Create indicator instance
        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setIndicatorUuid(NOT_EXISTS);
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorInstanceDto.setParentUuid(null);
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(1));
        indicatorInstanceDto.setGeographicalValueUuid(GEOGRAPHICAL_VALUE_1);
        indicatorInstanceDto.setTimeValue("2012");

        // Root level
        try {
            indicatorsServiceFacade.createIndicatorInstance(getServiceContext(), INDICATORS_SYSTEM_1, indicatorInstanceDto);
            fail("Indicator not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(indicatorInstanceDto.getIndicatorUuid(), e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorInstanceErrorIndicatorDuplicatedInSameLevel() throws Exception {

        // Create indicator instance
        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setIndicatorUuid("Indicator-1");
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorInstanceDto.setParentUuid(null);
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(3));
        indicatorInstanceDto.setGeographicalValueUuid(GEOGRAPHICAL_VALUE_1);
        indicatorInstanceDto.setTimeValue("2012");

        // Root level
        try {
            indicatorsServiceFacade.createIndicatorInstance(getServiceContext(), INDICATORS_SYSTEM_1, indicatorInstanceDto);
            fail("Indicator duplicated");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_INSTANCE_ALREADY_EXIST_INDICATOR_SAME_LEVEL.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(indicatorInstanceDto.getIndicatorUuid(), e.getExceptionItems().get(0).getMessageParameters()[0]);
        }

        // In dimension level
        indicatorInstanceDto.setIndicatorUuid(INDICATOR_2);
        indicatorInstanceDto.setParentUuid(DIMENSION_1B_INDICATORS_SYSTEM_1_V2);
        try {
            indicatorsServiceFacade.createIndicatorInstance(getServiceContext(), INDICATORS_SYSTEM_1, indicatorInstanceDto);
            fail("Indicator duplicated");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_INSTANCE_ALREADY_EXIST_INDICATOR_SAME_LEVEL.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(indicatorInstanceDto.getIndicatorUuid(), e.getExceptionItems().get(0).getMessageParameters()[0]);
        }

    }

    @Test
    public void testCreateIndicatorInstanceErrorIndicatorsSystemNotExists() throws Exception {

        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setIndicatorUuid("Indicator-1");
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorInstanceDto.setGeographicalValueUuid(GEOGRAPHICAL_VALUE_1);
        indicatorInstanceDto.setTimeValue("2012");
        indicatorInstanceDto.setParentUuid(null);
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(1));
        try {
            indicatorsServiceFacade.createIndicatorInstance(getServiceContext(), NOT_EXISTS, indicatorInstanceDto);
            fail("Indicators system not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorInstanceErrorIndicatorsSystemHasNotVersionInProduction() throws Exception {

        String indicatorsSystemUuid = INDICATORS_SYSTEM_3;
        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setIndicatorUuid("Indicator-1");
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorInstanceDto.setGeographicalValueUuid(GEOGRAPHICAL_VALUE_1);
        indicatorInstanceDto.setTimeValue("2012");
        indicatorInstanceDto.setParentUuid(null);
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(1));

        try {
            indicatorsServiceFacade.createIndicatorInstance(getServiceContext(), indicatorsSystemUuid, indicatorInstanceDto);
            fail("Indicators system not in production");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_IN_PRODUCTION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(indicatorsSystemUuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorInstanceErrorDimensionNotExists() throws Exception {

        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setIndicatorUuid("Indicator-1");
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorInstanceDto.setGeographicalValueUuid(GEOGRAPHICAL_VALUE_1);
        indicatorInstanceDto.setTimeValue("2012");
        indicatorInstanceDto.setParentUuid(DIMENSION_NOT_EXISTS);
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(1));

        try {
            indicatorsServiceFacade.createIndicatorInstance(getServiceContext(), INDICATORS_SYSTEM_1, indicatorInstanceDto);
            fail("Dimension not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DIMENSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(DIMENSION_NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorInstanceErrorDimensionNotExistsInIndicatorsSystem() throws Exception {

        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setIndicatorUuid("Indicator-1");
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorInstanceDto.setGeographicalValueUuid(GEOGRAPHICAL_VALUE_1);
        indicatorInstanceDto.setTimeValue("2012");
        indicatorInstanceDto.setParentUuid(DIMENSION_1_INDICATORS_SYSTEM_1_V2);
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(1));

        try {
            indicatorsServiceFacade.createIndicatorInstance(getServiceContext(), INDICATORS_SYSTEM_2, indicatorInstanceDto);
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
    public void testDeleteIndicatorInstance() throws Exception {

        String uuid = INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2;

        // Retrieve
        IndicatorInstanceDto indicatorInstanceDto = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContext(), uuid);
        assertEquals(Long.valueOf(1), indicatorInstanceDto.getOrderInLevel());

        // Delete indicatorInstance
        indicatorsServiceFacade.deleteIndicatorInstance(getServiceContext(), uuid);

        // Validation
        try {
            indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContext(), uuid);
            fail("Indicator instance deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_INSTANCE_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }

        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), indicatorInstanceDto.getIndicatorUuid(), null);
        assertNotNull(indicatorDto);

        // Checks orders of other elements are updated
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContext(), INDICATORS_SYSTEM_1, "2.000");
        assertEquals(3, indicatorsSystemStructureDto.getElements().size());
        assertEquals(DIMENSION_1_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(0).getDimension().getUuid());
        assertEquals(Long.valueOf(1), indicatorsSystemStructureDto.getElements().get(0).getOrderInLevel());
        assertEquals(INDICATOR_INSTANCE_2_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(1).getIndicatorInstance().getUuid());
        assertEquals(Long.valueOf(2), indicatorsSystemStructureDto.getElements().get(1).getOrderInLevel());
        assertEquals(DIMENSION_2_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(2).getDimension().getUuid());
        assertEquals(Long.valueOf(3), indicatorsSystemStructureDto.getElements().get(2).getOrderInLevel());
    }
    @Test
    public void testDeleteIndicatorInstanceErrorIndicatorsSystemVersionPublished() throws Exception {

        try {
            indicatorsServiceFacade.deleteIndicatorInstance(getServiceContext(), INDICATOR_INSTANCE_2_INDICATORS_SYSTEM_3_V1);
            fail("Indicators system not in production");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(INDICATORS_SYSTEM_3, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorsSystemProcStatusEnum.DRAFT, ((IndicatorsSystemProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(IndicatorsSystemProcStatusEnum.VALIDATION_REJECTED, ((IndicatorsSystemProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
            assertEquals(IndicatorsSystemProcStatusEnum.PRODUCTION_VALIDATION, ((IndicatorsSystemProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[2]);
            assertEquals(IndicatorsSystemProcStatusEnum.DIFFUSION_VALIDATION, ((IndicatorsSystemProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[3]);
        }
    }

    @Test
    public void testDeleteIndicatorInstanceErrorNotExists() throws Exception {

        String uuid = NOT_EXISTS;

        // Validation
        try {
            indicatorsServiceFacade.deleteIndicatorInstance(getServiceContext(), uuid);
            fail("IndicatorInstance not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_INSTANCE_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRetrieveIndicatorsInstances() throws Exception {

        String uuidIndicatorsSystem = INDICATORS_SYSTEM_1;

        // Version 1.000
        {
            List<IndicatorInstanceDto> indicatorsInstancesDto = indicatorsServiceFacade.retrieveIndicatorsInstancesByIndicatorsSystem(getServiceContext(), uuidIndicatorsSystem, "1.000");
            assertEquals(1, indicatorsInstancesDto.size());
            assertEquals(INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V1, indicatorsInstancesDto.get(0).getUuid());
        }

        // Version 2.000
        {
            List<IndicatorInstanceDto> indicatorsInstancesDto = indicatorsServiceFacade.retrieveIndicatorsInstancesByIndicatorsSystem(getServiceContext(), uuidIndicatorsSystem, "2.000");
            assertEquals(3, indicatorsInstancesDto.size());

            assertEquals(INDICATOR_INSTANCE_3_INDICATORS_SYSTEM_1_V2, indicatorsInstancesDto.get(0).getUuid());
            assertEquals(INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2, indicatorsInstancesDto.get(1).getUuid()); // orderBy puts null at last place
            assertEquals(INDICATOR_INSTANCE_2_INDICATORS_SYSTEM_1_V2, indicatorsInstancesDto.get(2).getUuid());
        }
    }

    @Test
    public void testRetrieveIndicatorsInstancesErrorNotExists() throws Exception {

        String uuid = NOT_EXISTS;

        // Validation
        try {
            indicatorsServiceFacade.retrieveIndicatorsInstancesByIndicatorsSystem(getServiceContext(), uuid, "1.000");
            fail("Indicators system not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_VERSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals("1.000", e.getExceptionItems().get(0).getMessageParameters()[1]);
        }
    }

    @Test
    public void testUpdateIndicatorInstance() throws Exception {

        String uuid = INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2;
        IndicatorInstanceDto indicatorInstanceDto = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContext(), uuid);
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorInstanceDto.setGeographicalGranularityUuid(GEOGRAPHICAL_GRANULARITY_1);
        indicatorInstanceDto.setGeographicalValueUuid(null);

        // Update
        IndicatorInstanceDto indicatorInstanceDtoUpdated = indicatorsServiceFacade.updateIndicatorInstance(getServiceContext(), indicatorInstanceDto);

        // Validation
        IndicatorsAsserts.assertEqualsIndicatorInstance(indicatorInstanceDto, indicatorInstanceDtoUpdated);
        indicatorInstanceDtoUpdated = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContext(), uuid);
        IndicatorsAsserts.assertEqualsIndicatorInstance(indicatorInstanceDto, indicatorInstanceDtoUpdated);
    }

    @Test
    public void testUpdateIndicatorInstanceErrorChangeParentAndOrderAndIndicator() throws Exception {

        IndicatorInstanceDto indicatorInstanceDto = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContext(), INDICATOR_INSTANCE_3_INDICATORS_SYSTEM_1_V2);
        assertEquals(DIMENSION_1B_INDICATORS_SYSTEM_1_V2, indicatorInstanceDto.getParentUuid());
        indicatorInstanceDto.setParentUuid(DIMENSION_1A_INDICATORS_SYSTEM_1_V2);
        assertEquals(Long.valueOf(2), indicatorInstanceDto.getOrderInLevel());
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(3));
        assertEquals(INDICATOR_2, indicatorInstanceDto.getIndicatorUuid());
        indicatorInstanceDto.setIndicatorUuid("newIndicator");

        try {
            indicatorsServiceFacade.updateIndicatorInstance(getServiceContext(), indicatorInstanceDto);
            fail("Unmodifiable attributes changed");
        } catch (MetamacException e) {
            assertEquals(3, e.getExceptionItems().size());

            assertEquals(ServiceExceptionType.METADATA_UNMODIFIABLE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("INDICATOR_INSTANCE.INDICATOR_UUID", e.getExceptionItems().get(0).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_UNMODIFIABLE.getCode(), e.getExceptionItems().get(1).getCode());
            assertEquals(1, e.getExceptionItems().get(1).getMessageParameters().length);
            assertEquals("INDICATOR_INSTANCE.PARENT_UUID", e.getExceptionItems().get(1).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_UNMODIFIABLE.getCode(), e.getExceptionItems().get(2).getCode());
            assertEquals(1, e.getExceptionItems().get(2).getMessageParameters().length);
            assertEquals("INDICATOR_INSTANCE.ORDER_IN_LEVEL", e.getExceptionItems().get(2).getMessageParameters()[0]);
        }

        // Put parent null
        indicatorInstanceDto.setParentUuid(null);
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(2));
        indicatorInstanceDto.setIndicatorUuid(INDICATOR_2);

        try {
            indicatorsServiceFacade.updateIndicatorInstance(getServiceContext(), indicatorInstanceDto);
            fail("Parent changed");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_UNMODIFIABLE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("INDICATOR_INSTANCE.PARENT_UUID", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateIndicatorInstanceErrorIndicatorsSystemPublished() throws Exception {

        IndicatorInstanceDto indicatorInstanceDto = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContext(), INDICATOR_INSTANCE_2_INDICATORS_SYSTEM_3_V1);

        try {
            indicatorsServiceFacade.updateIndicatorInstance(getServiceContext(), indicatorInstanceDto);
            fail("Indicators system published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(INDICATORS_SYSTEM_3, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorsSystemProcStatusEnum.DRAFT, ((IndicatorsSystemProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(IndicatorsSystemProcStatusEnum.VALIDATION_REJECTED, ((IndicatorsSystemProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
            assertEquals(IndicatorsSystemProcStatusEnum.PRODUCTION_VALIDATION, ((IndicatorsSystemProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[2]);
            assertEquals(IndicatorsSystemProcStatusEnum.DIFFUSION_VALIDATION, ((IndicatorsSystemProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[3]);
        }
    }

    @Test
    public void testUpdateIndicatorInstanceErrorNotExists() throws Exception {

        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setUuid(NOT_EXISTS);
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalString());

        try {
            indicatorsServiceFacade.updateIndicatorInstance(getServiceContext(), indicatorInstanceDto);
            fail("Indicator instance not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_INSTANCE_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateIndicatorInstanceLocation() throws Exception {
        // In other test testUpdateIndicatorInstanceLocation*
    }

    @Test
    public void testUpdateIndicatorInstanceLocationActualWithoutParentTargetWithParent() throws Exception {

        String uuid = INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2;
        String parentBeforeUuid = null;
        String parentTargetUuid = DIMENSION_2_INDICATORS_SYSTEM_1_V2;

        IndicatorInstanceDto indicatorInstanceDto = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContext(), uuid);
        assertEquals(parentBeforeUuid, indicatorInstanceDto.getParentUuid());

        // Update location
        IndicatorInstanceDto indicatorInstanceDtoChanged = indicatorsServiceFacade.updateIndicatorInstanceLocation(getServiceContext(), uuid, parentTargetUuid, Long.valueOf(1));

        // Validate indicatorInstance
        assertEquals(parentTargetUuid, indicatorInstanceDtoChanged.getParentUuid());
        indicatorInstanceDtoChanged = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContext(), uuid);
        assertEquals(parentTargetUuid, indicatorInstanceDtoChanged.getParentUuid());

        // Validate source
        {
            IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContext(), INDICATORS_SYSTEM_1, "2.000");
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
            IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContext(), INDICATORS_SYSTEM_1, "2.000");
            ElementLevelDto elementLevelDto = indicatorsSystemStructureDto.getElements().get(2);
            assertEquals(parentTargetUuid, elementLevelDto.getDimension().getUuid());
            assertEquals(1, elementLevelDto.getSubelements().size());
            assertEquals(uuid, elementLevelDto.getSubelements().get(0).getIndicatorInstance().getUuid());
            assertEquals(Long.valueOf(1), indicatorsSystemStructureDto.getElements().get(0).getOrderInLevel());
        }
    }

    @Test
    public void testUpdateIndicatorInstanceLocationActualWithParentTargetWithoutParent() throws Exception {

        String uuid = INDICATOR_INSTANCE_3_INDICATORS_SYSTEM_1_V2;
        String parentBeforeUuid = DIMENSION_1B_INDICATORS_SYSTEM_1_V2;
        String parentTargetUuid = null;

        // Retrieve actual
        IndicatorInstanceDto indicatorInstanceDto = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContext(), uuid);
        assertEquals(parentBeforeUuid, indicatorInstanceDto.getParentUuid());

        // Update location
        indicatorsServiceFacade.updateIndicatorInstanceLocation(getServiceContext(), uuid, parentTargetUuid, Long.valueOf(2));

        // Validate parent is changed in indicatorInstance
        IndicatorInstanceDto indicatorInstanceDtoChanged = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContext(), uuid);
        assertNull(indicatorInstanceDtoChanged.getParentUuid());

        // Validate source
        {
            IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContext(), INDICATORS_SYSTEM_1, "2.000");
            ElementLevelDto elementLevelDto = indicatorsSystemStructureDto.getElements().get(2).getSubelements().get(1);
            assertEquals(parentBeforeUuid, elementLevelDto.getDimension().getUuid());
            assertEquals(1, elementLevelDto.getSubelements().size());
        }

        // Validate target
        {
            IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContext(), INDICATORS_SYSTEM_1, "2.000");
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
    public void testUpdateIndicatorInstanceLocationChangingDimensionParent() throws Exception {

        String uuid = INDICATOR_INSTANCE_3_INDICATORS_SYSTEM_1_V2;
        String parentBeforeUuid = DIMENSION_1B_INDICATORS_SYSTEM_1_V2;
        String parentTargetUuid = DIMENSION_1A_INDICATORS_SYSTEM_1_V2;

        // Retrieve actual
        IndicatorInstanceDto indicatorInstanceDto = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContext(), uuid);
        assertEquals(parentBeforeUuid, indicatorInstanceDto.getParentUuid());

        // Update location
        indicatorsServiceFacade.updateIndicatorInstanceLocation(getServiceContext(), uuid, parentTargetUuid, Long.valueOf(1));

        // Validate indicatorInstance
        IndicatorInstanceDto indicatorInstanceDtoChanged = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContext(), uuid);
        assertEquals(parentTargetUuid, indicatorInstanceDtoChanged.getParentUuid());

        // Validate source
        {
            IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContext(), INDICATORS_SYSTEM_1, "2.000");
            ElementLevelDto elementLevelDto = indicatorsSystemStructureDto.getElements().get(1).getSubelements().get(1);
            assertEquals(parentBeforeUuid, elementLevelDto.getDimension().getUuid());
            assertEquals(1, elementLevelDto.getSubelements().size());
        }

        // Validate target
        {
            IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContext(), INDICATORS_SYSTEM_1, "2.000");
            ElementLevelDto elementLevelDto = indicatorsSystemStructureDto.getElements().get(1).getSubelements().get(0);
            assertEquals(parentTargetUuid, elementLevelDto.getDimension().getUuid());
            assertEquals(1, elementLevelDto.getSubelements().size());
            assertEquals(uuid, elementLevelDto.getSubelements().get(0).getIndicatorInstance().getUuid());
        }

    }

    @Test
    public void testUpdateIndicatorInstanceLocationActualSameParentOnlyChangeOrderWithoutParent() throws Exception {

        String uuid = INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2;

        // Retrieve actual
        IndicatorInstanceDto indicatorInstanceDto = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContext(), uuid);
        assertNull(indicatorInstanceDto.getParentUuid());
        assertEquals(Long.valueOf(1), indicatorInstanceDto.getOrderInLevel());

        // Update location
        indicatorsServiceFacade.updateIndicatorInstanceLocation(getServiceContext(), uuid, indicatorInstanceDto.getParentUuid(), Long.valueOf(4));

        // Validate indicatorInstance
        IndicatorInstanceDto indicatorInstanceDtoChanged = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContext(), uuid);
        assertNull(indicatorInstanceDtoChanged.getParentUuid());
        assertEquals(Long.valueOf(4), indicatorInstanceDtoChanged.getOrderInLevel());

        // Validate source and target
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContext(), INDICATORS_SYSTEM_1, "2.000");
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
    public void testUpdateIndicatorInstanceLocationActualSameParentOnlyChangeOrderWithParent() throws Exception {

        String uuid = INDICATOR_INSTANCE_3_INDICATORS_SYSTEM_1_V2;
        String parentUuid = DIMENSION_1B_INDICATORS_SYSTEM_1_V2;

        // Retrieve actual
        IndicatorInstanceDto indicatorInstanceDto = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContext(), uuid);
        assertEquals(parentUuid, indicatorInstanceDto.getParentUuid());
        assertEquals(Long.valueOf(2), indicatorInstanceDto.getOrderInLevel());

        // Update location
        IndicatorInstanceDto indicatorInstanceDtoChanged = indicatorsServiceFacade.updateIndicatorInstanceLocation(getServiceContext(), uuid, indicatorInstanceDto.getParentUuid(), Long.valueOf(1));

        // Validate indicatorInstance
        assertEquals(parentUuid, indicatorInstanceDtoChanged.getParentUuid());
        assertEquals(Long.valueOf(1), indicatorInstanceDtoChanged.getOrderInLevel());
        indicatorInstanceDtoChanged = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContext(), uuid);
        assertEquals(parentUuid, indicatorInstanceDtoChanged.getParentUuid());
        assertEquals(Long.valueOf(1), indicatorInstanceDtoChanged.getOrderInLevel());

        // Validate source and target
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContext(), INDICATORS_SYSTEM_1, "2.000");
        ElementLevelDto elementLevelDto = indicatorsSystemStructureDto.getElements().get(1).getSubelements().get(1);
        assertEquals(parentUuid, elementLevelDto.getDimension().getUuid());
        assertEquals(2, elementLevelDto.getSubelements().size());
        assertEquals(uuid, elementLevelDto.getSubelements().get(0).getIndicatorInstance().getUuid());
        assertEquals(Long.valueOf(1), elementLevelDto.getSubelements().get(0).getIndicatorInstance().getOrderInLevel());
        assertEquals(DIMENSION_1BA_INDICATORS_SYSTEM_1_V2, elementLevelDto.getSubelements().get(1).getDimension().getUuid());
        assertEquals(Long.valueOf(2), elementLevelDto.getSubelements().get(1).getDimension().getOrderInLevel());
    }

    @Test
    public void testUpdateIndicatorInstanceLocationErrorNotExists() throws Exception {

        String uuid = NOT_EXISTS;

        // Validation
        try {
            indicatorsServiceFacade.updateIndicatorInstanceLocation(getServiceContext(), uuid, null, Long.valueOf(1));
            fail("IndicatorInstance not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_INSTANCE_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateIndicatorInstanceLocationErrorOrderIncorrect() throws Exception {

        try {
            indicatorsServiceFacade.updateIndicatorInstanceLocation(getServiceContext(), INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2, null, Long.MAX_VALUE);
            fail("order incorrect");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("ORDER_IN_LEVEL", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
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
            for (int j = 1; j <= 52; j++) {
                String week = year + "W" + StringUtils.leftPad(String.valueOf(j), 2, "0");
                assertTrue(week, TimeVariableUtils.isTimeValue(week));
            }
        }

        // Invalid
        assertFalse(TimeVariableUtils.isTimeValue("912"));
        assertFalse(TimeVariableUtils.isTimeValue("3000"));
        assertFalse(TimeVariableUtils.isTimeValue("2012q1"));
        assertFalse(TimeVariableUtils.isTimeValue("2012Q00"));
        assertFalse(TimeVariableUtils.isTimeValue("2012Q0"));
        assertFalse(TimeVariableUtils.isTimeValue("2012Q5"));
        assertFalse(TimeVariableUtils.isTimeValue("2012m1"));
        assertFalse(TimeVariableUtils.isTimeValue("2012M111"));
        assertFalse(TimeVariableUtils.isTimeValue("2012M1"));
        assertFalse(TimeVariableUtils.isTimeValue("2012M14"));
        assertFalse(TimeVariableUtils.isTimeValue("2012w1"));
        assertFalse(TimeVariableUtils.isTimeValue("2012W111"));
        assertFalse(TimeVariableUtils.isTimeValue("2012W1"));
        assertFalse(TimeVariableUtils.isTimeValue("2012W53"));
        assertFalse(TimeVariableUtils.isTimeValue("2012W60"));
        assertFalse(TimeVariableUtils.isTimeValue("20121"));
        assertFalse(TimeVariableUtils.isTimeValue("201212"));
        assertFalse(TimeVariableUtils.isTimeValue("2012121"));
        assertFalse(TimeVariableUtils.isTimeValue("201211223"));
        assertFalse(TimeVariableUtils.isTimeValue("afas"));
        assertFalse(TimeVariableUtils.isTimeValue("201 2"));
    }

    @Test
    public void testCompareTimeGranularities() throws Exception {

        // Equals
        assertTrue(TimeVariableUtils.compareTo("2012", "2012") == 0);
        assertTrue(TimeVariableUtils.compareTo("2012H2", "2012H2") == 0);
        assertTrue(TimeVariableUtils.compareTo("2012Q1", "2012Q1") == 0);
        assertTrue(TimeVariableUtils.compareTo("2012M02", "2012M02") == 0);
        assertTrue(TimeVariableUtils.compareTo("20120102", "20120102") == 0);
        assertTrue(TimeVariableUtils.compareTo("2012W51", "2012W51") == 0);

        // Less
        assertTrue(TimeVariableUtils.compareTo("2011", "2012") < 0);
        assertTrue(TimeVariableUtils.compareTo("2012H1", "2012H2") < 0);
        assertTrue(TimeVariableUtils.compareTo("2011H2", "2012H2") < 0);
        assertTrue(TimeVariableUtils.compareTo("2012Q2", "2012Q3") < 0);
        assertTrue(TimeVariableUtils.compareTo("2011Q1", "2012Q1") < 0);
        assertTrue(TimeVariableUtils.compareTo("2012M01", "2012M02") < 0);
        assertTrue(TimeVariableUtils.compareTo("2011M01", "2012M02") < 0);
        assertTrue(TimeVariableUtils.compareTo("20120102", "20120202") < 0);
        assertTrue(TimeVariableUtils.compareTo("20120102", "20120103") < 0);
        assertTrue(TimeVariableUtils.compareTo("2012W01", "2012W51") < 0);
        assertTrue(TimeVariableUtils.compareTo("2011W51", "2012W51") < 0);

        // Greater
        assertTrue(TimeVariableUtils.compareTo("2013", "2012") > 0);
        assertTrue(TimeVariableUtils.compareTo("2013H1", "2012H2") > 0);
        assertTrue(TimeVariableUtils.compareTo("20130102", "20120103") > 0);
    }

    @Test
    public void testCompareTimeGranularitiesErrorTimeValueIncorrect() throws Exception {
        try {
            TimeVariableUtils.compareTo("2010xx", "2010H2");
            fail("time incorrect");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("2010xx", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }
    
    @Test
    public void testCompareTimeGranularitiesErrorDifferentGranularities() throws Exception {
        try {
            TimeVariableUtils.compareTo("2010", "2010H2");
            fail("granularities different");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("2010H2", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testGuessTimeGranularity() throws Exception {

        assertEquals(TimeGranularityEnum.YEARLY, TimeVariableUtils.guessTimeGranularity("2012"));
        assertEquals(TimeGranularityEnum.BIYEARLY, TimeVariableUtils.guessTimeGranularity("2012H2"));
        assertEquals(TimeGranularityEnum.QUARTERLY, TimeVariableUtils.guessTimeGranularity("2012Q1"));
        assertEquals(TimeGranularityEnum.MONTHLY, TimeVariableUtils.guessTimeGranularity("2012M02"));
        assertEquals(TimeGranularityEnum.DAILY, TimeVariableUtils.guessTimeGranularity("20120102"));
        assertEquals(TimeGranularityEnum.WEEKLY, TimeVariableUtils.guessTimeGranularity("2012W51"));
    }

    @Test
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
    public void testRetrieveGeographicalValue() throws Exception {

        String uuid = GEOGRAPHICAL_VALUE_1;
        GeographicalValueDto geographicalValueDto = indicatorsServiceFacade.retrieveGeographicalValue(getServiceContext(), uuid);

        assertNotNull(geographicalValueDto);
        assertEquals(uuid, geographicalValueDto.getUuid());
        assertEquals("ES", geographicalValueDto.getCode());
        assertEquals(GEOGRAPHICAL_GRANULARITY_1, geographicalValueDto.getGranularityUuid());
        assertEquals(Double.valueOf(-40.689061), geographicalValueDto.getLatitude());
        assertEquals("-40.689061", geographicalValueDto.getLatitude().toString());
        assertEquals(Double.valueOf(368987.22), geographicalValueDto.getLongitude());
        assertEquals("368987.22", geographicalValueDto.getLongitude().toString());
        IndicatorsAsserts.assertEqualsInternationalString(geographicalValueDto.getTitle(), "es", "España", "en", "Spain");
    }

    @Test
    public void testRetrieveGeographicalValueErrorParameterRequired() throws Exception {

        String uuid = null;

        try {
            indicatorsServiceFacade.retrieveGeographicalValue(getServiceContext(), uuid);
            fail("parameter required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("UUID", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRetrieveGeographicalValueErrorNotExists() throws Exception {

        String uuid = NOT_EXISTS;

        try {
            indicatorsServiceFacade.retrieveGeographicalValue(getServiceContext(), uuid);
            fail("No exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.GEOGRAPHICAL_VALUE_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testFindGeographicalValues() throws Exception {

        // All
        {
            List<GeographicalValueDto> geographicalValues = indicatorsServiceFacade.findGeographicalValues(getServiceContext(), null);
            assertEquals(4, geographicalValues.size());
    
            assertEquals(GEOGRAPHICAL_VALUE_1, geographicalValues.get(0).getUuid());
            assertEquals("ES", geographicalValues.get(0).getCode());
            assertEquals(GEOGRAPHICAL_VALUE_2, geographicalValues.get(1).getUuid());
            assertEquals("EN-LN", geographicalValues.get(1).getCode());
            assertEquals(GEOGRAPHICAL_VALUE_3, geographicalValues.get(2).getUuid());
            assertEquals("FR", geographicalValues.get(2).getCode());
            assertEquals(GEOGRAPHICAL_VALUE_4, geographicalValues.get(3).getUuid());
            assertEquals("ES-MD", geographicalValues.get(3).getCode());
        }
        
        // By granularity
        {
            IndicatorsCriteria criteria = new IndicatorsCriteria();
            criteria.setConjunctionRestriction(new IndicatorsCriteriaConjunctionRestriction());
            criteria.getConjunctionRestriction().getRestrictions().add(new IndicatorsCriteriaPropertyRestriction(GeographicalValueCriteriaPropertyEnum.GEOGRAPHICAL_GRANULARITY_UUID.name(), GEOGRAPHICAL_GRANULARITY_1));

            List<GeographicalValueDto> geographicalValues = indicatorsServiceFacade.findGeographicalValues(getServiceContext(), criteria);
            assertEquals(2, geographicalValues.size());
    
            assertEquals(GEOGRAPHICAL_VALUE_1, geographicalValues.get(0).getUuid());
            assertEquals("ES", geographicalValues.get(0).getCode());
            assertEquals(GEOGRAPHICAL_VALUE_3, geographicalValues.get(1).getUuid());
            assertEquals("FR", geographicalValues.get(1).getCode());
        }
    }

    @Test
    public void testRetrieveGeographicalGranularity() throws Exception {

        String uuid = GEOGRAPHICAL_GRANULARITY_2;
        GeographicalGranularityDto geographicalGranularityDto = indicatorsServiceFacade.retrieveGeographicalGranularity(getServiceContext(), uuid);

        assertNotNull(geographicalGranularityDto);
        assertEquals(uuid, geographicalGranularityDto.getUuid());
        assertEquals("MUNICIPALITIES", geographicalGranularityDto.getCode());
        IndicatorsAsserts.assertEqualsInternationalString(geographicalGranularityDto.getTitle(), "es", "Municipios", "en", "Municipalities");
    }

    @Test
    public void testRetrieveGeographicalGranularityErrorParameterRequired() throws Exception {

        String uuid = null;

        try {
            indicatorsServiceFacade.retrieveGeographicalGranularity(getServiceContext(), uuid);
            fail("parameter required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("UUID", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRetrieveGeographicalGranularityErrorNotExists() throws Exception {

        String uuid = NOT_EXISTS;

        try {
            indicatorsServiceFacade.retrieveGeographicalGranularity(getServiceContext(), uuid);
            fail("No exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.GEOGRAPHICAL_GRANULARITY_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRetrieveGeographicalGranularities() throws Exception {

        List<GeographicalGranularityDto> geographicalGranularities = indicatorsServiceFacade.retrieveGeographicalGranularities(getServiceContext());
        assertEquals(2, geographicalGranularities.size());

        assertEquals(GEOGRAPHICAL_GRANULARITY_1, geographicalGranularities.get(0).getUuid());
        assertEquals("COUNTRIES", geographicalGranularities.get(0).getCode());

        assertEquals(GEOGRAPHICAL_GRANULARITY_2, geographicalGranularities.get(1).getUuid());
        assertEquals("MUNICIPALITIES", geographicalGranularities.get(1).getCode());
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/IndicatorsServiceFacadeIndicatorsSystemsTest.xml";
    }
}
