package es.gobcan.istac.indicators.core.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.gobcan.istac.indicators.core.dto.serviceapi.DimensionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.ElementLevelDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorInstanceDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemStructureDto;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorsSystemStateEnum;
import es.gobcan.istac.indicators.core.enume.domain.VersiontTypeEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.serviceapi.utils.IndicatorsAsserts;
import es.gobcan.istac.indicators.core.serviceapi.utils.IndicatorsMocks;

/**
 * Spring based transactional test with DbUnit support.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext-test.xml"})
public class IndicatorsSystemsServiceFacadeTest extends IndicatorsBaseTest implements IndicatorsSystemsServiceFacadeTestBase {

    @Autowired
    protected IndicatorsSystemsServiceFacade indicatorsSystemsServiceFacade;

    private static String                    NOT_EXISTS                                  = "not-exists";

    // Indicators systems
    private static String                    INDICATORS_SYSTEM_1                         = "IndSys-1";
    private static String                    INDICATORS_SYSTEM_2                         = "IndSys-2";
    private static String                    INDICATORS_SYSTEM_3                         = "IndSys-3";
    private static String                    INDICATORS_SYSTEM_3_VERSION                 = "11.033";
    private static String                    INDICATORS_SYSTEM_4                         = "IndSys-4";
    private static String                    INDICATORS_SYSTEM_5                         = "IndSys-5";
    private static String                    INDICATORS_SYSTEM_6                         = "IndSys-6";
    private static String                    INDICATORS_SYSTEM_7                         = "IndSys-7";
    private static String                    INDICATORS_SYSTEM_8                         = "IndSys-8";
    private static String                    INDICATORS_SYSTEM_9                         = "IndSys-9";

    // Dimensions
    private static String                    DIMENSION_NOT_EXISTS                        = "Dim-not-exists";
    private static String                    DIMENSION_1_INDICATORS_SYSTEM_1_V1          = "IndSys-1-v1-Dimension-1";
    private static String                    DIMENSION_1_INDICATORS_SYSTEM_1_V2          = "IndSys-1-v2-Dimension-1";
    private static String                    DIMENSION_1A_INDICATORS_SYSTEM_1_V2         = "IndSys-1-v2-Dimension-1A";
    private static String                    DIMENSION_1B_INDICATORS_SYSTEM_1_V2         = "IndSys-1-v2-Dimension-1B";
    private static String                    DIMENSION_1BA_INDICATORS_SYSTEM_1_V2        = "IndSys-1-v2-Dimension-1BA";
    private static String                    DIMENSION_2_INDICATORS_SYSTEM_1_V2          = "IndSys-1-v2-Dimension-2";
    private static String                    DIMENSION_1_INDICATORS_SYSTEM_3             = "IndSys-3-v1-Dimension-1";

    // Indicator instances
    private static String                    INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2 = "IndSys-1-v2-IInstance-1";
    private static String                    INDICATOR_INSTANCE_2_INDICATORS_SYSTEM_1_V2 = "IndSys-1-v2-IInstance-2";
    private static String                    INDICATOR_INSTANCE_3_INDICATORS_SYSTEM_1_V2 = "IndSys-1-v2-IInstance-3";
    private static String                    INDICATOR_INSTANCE_2_INDICATORS_SYSTEM_3_V1 = "IndSys-3-v1-IInstance-2";

    @Test
    public void testRetrieveIndicatorsSystem() throws Exception {

        String uuid = INDICATORS_SYSTEM_1;
        String versionNumber = "1.000";
        IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);

        assertNotNull(indicatorsSystemDto);
        assertEquals(uuid, indicatorsSystemDto.getUuid());
        assertEquals(versionNumber, indicatorsSystemDto.getVersionNumber());
        assertEquals("1.000", indicatorsSystemDto.getDiffusionVersion());
        assertEquals("2.000", indicatorsSystemDto.getProductionVersion());
        assertEquals("CODE-1", indicatorsSystemDto.getCode());
        assertEquals("http://indicators-sytems/1", indicatorsSystemDto.getUriGopestat());
        assertEquals(IndicatorsSystemStateEnum.PUBLISHED, indicatorsSystemDto.getState());
        IndicatorsAsserts.assertEqualsInternationalString(indicatorsSystemDto.getTitle(), "es", "Título IndSys-1-v1", "en", "Title IndSys-1-v1");
        IndicatorsAsserts.assertEqualsInternationalString(indicatorsSystemDto.getAcronym(), "es", "Acrónimo IndSys-1-v1", "en", "Acronym IndSys-1-v1");
        IndicatorsAsserts.assertEqualsInternationalString(indicatorsSystemDto.getDescription(), "es", "Descripción IndSys-1-v1", "en", "Description IndSys-1-v1");
        IndicatorsAsserts.assertEqualsInternationalString(indicatorsSystemDto.getObjetive(), "es", "Objetivo IndSys-1-v1", "en", "Objetive IndSys-1-v1");
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
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, null);
            assertEquals(uuid, indicatorsSystemDto.getUuid());
            assertEquals(versionNumberProduction, indicatorsSystemDto.getVersionNumber());
            assertEquals(versionNumberDiffusion, indicatorsSystemDto.getDiffusionVersion());
            assertEquals(versionNumberProduction, indicatorsSystemDto.getProductionVersion());
        }

        // With version 1
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumberDiffusion);
            assertEquals(uuid, indicatorsSystemDto.getUuid());
            assertEquals(versionNumberDiffusion, indicatorsSystemDto.getVersionNumber());
            assertEquals(versionNumberDiffusion, indicatorsSystemDto.getDiffusionVersion());
            assertEquals(versionNumberProduction, indicatorsSystemDto.getProductionVersion());
        }

        // With version 2
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumberProduction);
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
            indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, null);
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
            indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, null);
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
            indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNotExists);
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

        IndicatorsSystemDto indicatorsSystemDtoPublished = indicatorsSystemsServiceFacade.retrieveIndicatorsSystemPublished(getServiceContext(), uuid);
        assertEquals(uuid, indicatorsSystemDtoPublished.getUuid());
        assertEquals("11.033", indicatorsSystemDtoPublished.getVersionNumber());
        assertEquals(IndicatorsSystemStateEnum.PUBLISHED, indicatorsSystemDtoPublished.getState());
    }

    @Test
    public void testRetrieveIndicatorsSystemPublishedWhenSystemHasVersionProduction() throws Exception {

        String uuid = INDICATORS_SYSTEM_1;

        IndicatorsSystemDto indicatorsSystemDtoPublished = indicatorsSystemsServiceFacade.retrieveIndicatorsSystemPublished(getServiceContext(), uuid);
        assertEquals(uuid, indicatorsSystemDtoPublished.getUuid());
        assertEquals("1.000", indicatorsSystemDtoPublished.getVersionNumber());
        assertEquals(IndicatorsSystemStateEnum.PUBLISHED, indicatorsSystemDtoPublished.getState());
    }

    @Test
    public void testRetrieveIndicatorsSystemPublishedErrorOnlyProduction() throws Exception {

        String uuid = INDICATORS_SYSTEM_2;

        try {
            indicatorsSystemsServiceFacade.retrieveIndicatorsSystemPublished(getServiceContext(), uuid);
            fail("No published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_STATE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorsSystemStateEnum.PUBLISHED, ((IndicatorsSystemStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Test
    public void testRetrieveIndicatorsSystemPublishedErrorParameterRequired() throws Exception {

        String uuid = null;

        try {
            indicatorsSystemsServiceFacade.retrieveIndicatorsSystemPublished(getServiceContext(), uuid);
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
            indicatorsSystemsServiceFacade.retrieveIndicatorsSystemPublished(getServiceContext(), uuid);
            fail("No exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Override
    @Test
    public void testRetrieveIndicatorsSystemByCode() throws Exception {

        String code = "CODE-1";
        IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContext(), code);

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
            indicatorsSystemsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContext(), code);
            fail("No exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND_WITH_CODE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(code, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Override
    @Test
    public void testRetrieveIndicatorsSystemPublishedByCode() throws Exception {

        String code = "CODE-1";
        IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystemPublishedByCode(getServiceContext(), code);

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
            indicatorsSystemsServiceFacade.retrieveIndicatorsSystemPublishedByCode(getServiceContext(), code);
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
            indicatorsSystemsServiceFacade.retrieveIndicatorsSystemPublishedByCode(getServiceContext(), code);
            fail("No exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_STATE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(INDICATORS_SYSTEM_2, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorsSystemStateEnum.PUBLISHED, ((IndicatorsSystemStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Override
    @Test
    public void testRetrieveIndicatorsSystemStructure() throws Exception {

        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContext(), INDICATORS_SYSTEM_1, "2.000");

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
            assertEquals(0, elementLevelDto.getDimension().getSubdimensions().size()); // when retrieves structure, this attribute is not filled
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
        indicatorsSystemDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorsSystemDto.setAcronym(IndicatorsMocks.mockInternationalString());
        indicatorsSystemDto.setUriGopestat(IndicatorsMocks.mockString(100));
        indicatorsSystemDto.setObjetive(IndicatorsMocks.mockInternationalString());
        indicatorsSystemDto.setDescription(IndicatorsMocks.mockInternationalString());

        // Create
        IndicatorsSystemDto indicatorsSystemDtoCreated = indicatorsSystemsServiceFacade.createIndicatorsSystem(getServiceContext(), indicatorsSystemDto);

        // Validate
        assertNotNull(indicatorsSystemDtoCreated);
        assertNotNull(indicatorsSystemDtoCreated.getUuid());
        assertNotNull(indicatorsSystemDtoCreated.getVersionNumber());
        IndicatorsSystemDto indicatorsSystemDtoRetrieved = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), indicatorsSystemDtoCreated.getUuid(),
                indicatorsSystemDtoCreated.getVersionNumber());
        assertEquals(IndicatorsSystemStateEnum.DRAFT, indicatorsSystemDtoCreated.getState());
        assertEquals(IndicatorsSystemStateEnum.DRAFT, indicatorsSystemDtoRetrieved.getState());
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
        indicatorsSystemDto.setTitle(null);

        try {
            indicatorsSystemsServiceFacade.createIndicatorsSystem(getServiceContext(), indicatorsSystemDto);
            fail("parameters required");
        } catch (MetamacException e) {
            assertEquals(2, e.getExceptionItems().size());

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("INDICATORS_SYSTEM.CODE", e.getExceptionItems().get(0).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(1).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("INDICATORS_SYSTEM.TITLE", e.getExceptionItems().get(1).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorsSystemCodeDuplicated() throws Exception {

        IndicatorsSystemDto indicatorsSystemDto = new IndicatorsSystemDto();
        indicatorsSystemDto.setCode("CoDe-1");
        indicatorsSystemDto.setTitle(IndicatorsMocks.mockInternationalString());

        try {
            indicatorsSystemsServiceFacade.createIndicatorsSystem(getServiceContext(), indicatorsSystemDto);
            fail("code duplicated");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_ALREADY_EXIST_CODE_DUPLICATED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(indicatorsSystemDto.getCode(), e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorsSystemUriDuplicated() throws Exception {

        IndicatorsSystemDto indicatorsSystemDto = new IndicatorsSystemDto();
        indicatorsSystemDto.setCode(IndicatorsMocks.mockString(10));
        indicatorsSystemDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorsSystemDto.setUriGopestat("http://indicators-sytems/1");

        try {
            indicatorsSystemsServiceFacade.createIndicatorsSystem(getServiceContext(), indicatorsSystemDto);
            fail("uri gopestat duplicated");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_ALREADY_EXIST_URI_GOPESTAT_DUPLICATED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(indicatorsSystemDto.getUriGopestat(), e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorsSystemCodeDuplicatedInsensitive() throws Exception {

        IndicatorsSystemDto indicatorsSystemDto = new IndicatorsSystemDto();
        indicatorsSystemDto.setCode("CoDe-1");
        indicatorsSystemDto.setTitle(IndicatorsMocks.mockInternationalString());

        try {
            indicatorsSystemsServiceFacade.createIndicatorsSystem(getServiceContext(), indicatorsSystemDto);
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
        indicatorsSystemsServiceFacade.deleteIndicatorsSystem(getServiceContext(), uuid);

        // Validation
        try {
            indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, null);
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
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, "1.000");
            assertEquals(uuid, indicatorsSystemDto.getUuid());
            assertEquals("1.000", indicatorsSystemDto.getVersionNumber());
            assertEquals("1.000", indicatorsSystemDto.getDiffusionVersion());
            assertEquals("2.000", indicatorsSystemDto.getProductionVersion());
        }

        // Retrieve dimensions to check will be deleted // TODO chequear q también se borran las instancias de indicadores
        List<String> dimensionsUuid = new ArrayList<String>();
        dimensionsUuid.add(DIMENSION_1_INDICATORS_SYSTEM_1_V2);
        dimensionsUuid.add(DIMENSION_1A_INDICATORS_SYSTEM_1_V2);
        dimensionsUuid.add(DIMENSION_1B_INDICATORS_SYSTEM_1_V2);
        dimensionsUuid.add(DIMENSION_1BA_INDICATORS_SYSTEM_1_V2);
        dimensionsUuid.add(DIMENSION_2_INDICATORS_SYSTEM_1_V2);
        for (String dimensionUuid : dimensionsUuid) {
            DimensionDto dimensionDto = indicatorsSystemsServiceFacade.retrieveDimension(getServiceContext(), dimensionUuid);
            assertNotNull(dimensionDto);
        }

        // Delete indicators system
        indicatorsSystemsServiceFacade.deleteIndicatorsSystem(getServiceContext(), uuid);

        // Validation
        // Version 1 exists
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, "1.000");
            assertEquals(uuid, indicatorsSystemDto.getUuid());
            assertEquals("1.000", indicatorsSystemDto.getVersionNumber());
            assertEquals("1.000", indicatorsSystemDto.getDiffusionVersion());
            assertEquals(null, indicatorsSystemDto.getProductionVersion());
        }
        // Version 2 not exists
        try {
            indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, "2.000");
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
                indicatorsSystemsServiceFacade.retrieveDimension(getServiceContext(), dimensionUuid);
                fail("dimension deleted");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.DIMENSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
                assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
                assertEquals(dimensionUuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            }
        }
    }

    @Test
    public void testDeleteIndicatorsSystemErrorInDiffusion() throws Exception {

        String uuid = INDICATORS_SYSTEM_3;

        // Validation
        try {
            indicatorsSystemsServiceFacade.deleteIndicatorsSystem(getServiceContext(), uuid);
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
            indicatorsSystemsServiceFacade.deleteIndicatorsSystem(getServiceContext(), uuid);
            fail("Indicator system not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateIndicatorsSystem() throws Exception {

        String uuid = INDICATORS_SYSTEM_1;
        String versionNumber = "2.000";

        IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
        assertEquals(IndicatorsSystemStateEnum.DRAFT, indicatorsSystemDto.getState());

        indicatorsSystemDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorsSystemDto.setAcronym(IndicatorsMocks.mockInternationalString());
        indicatorsSystemDto.setUriGopestat("newUri");

        // Update
        indicatorsSystemsServiceFacade.updateIndicatorsSystem(getServiceContext(), indicatorsSystemDto);

        // Validation
        IndicatorsSystemDto indicatorsSystemDtoUpdated = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
        IndicatorsAsserts.assertEqualsIndicatorsSystem(indicatorsSystemDto, indicatorsSystemDtoUpdated);
        assertTrue(indicatorsSystemDtoUpdated.getLastUpdated().after(indicatorsSystemDtoUpdated.getCreatedDate()));
    }

    @Test
    public void testUpdateIndicatorsSystemInRejectedValidation() throws Exception {

        String uuid = INDICATORS_SYSTEM_9;
        String versionNumber = "1.000";

        IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
        assertEquals(IndicatorsSystemStateEnum.VALIDATION_REJECTED, indicatorsSystemDto.getState());

        indicatorsSystemDto.setTitle(IndicatorsMocks.mockInternationalString());

        // Update
        indicatorsSystemsServiceFacade.updateIndicatorsSystem(getServiceContext(), indicatorsSystemDto);

        // Validation
        IndicatorsSystemDto indicatorsSystemDtoUpdated = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
        assertEquals(IndicatorsSystemStateEnum.VALIDATION_REJECTED, indicatorsSystemDtoUpdated.getState());
        IndicatorsAsserts.assertEqualsIndicatorsSystem(indicatorsSystemDto, indicatorsSystemDtoUpdated);
    }

    @Test
    public void testUpdateIndicatorsSystemInProductionValidation() throws Exception {

        String uuid = INDICATORS_SYSTEM_4;
        String versionNumber = "1.000";

        IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
        assertEquals(IndicatorsSystemStateEnum.PRODUCTION_VALIDATION, indicatorsSystemDto.getState());

        indicatorsSystemDto.setTitle(IndicatorsMocks.mockInternationalString());

        // Update
        indicatorsSystemsServiceFacade.updateIndicatorsSystem(getServiceContext(), indicatorsSystemDto);

        // Validation
        IndicatorsSystemDto indicatorsSystemDtoUpdated = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
        assertEquals(IndicatorsSystemStateEnum.PRODUCTION_VALIDATION, indicatorsSystemDtoUpdated.getState());
        IndicatorsAsserts.assertEqualsIndicatorsSystem(indicatorsSystemDto, indicatorsSystemDtoUpdated);
    }

    @Test
    public void testUpdateIndicatorsSystemInDiffusionValidation() throws Exception {

        String uuid = INDICATORS_SYSTEM_5;
        String versionNumber = "1.000";

        IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
        assertEquals(IndicatorsSystemStateEnum.DIFFUSION_VALIDATION, indicatorsSystemDto.getState());

        indicatorsSystemDto.setTitle(IndicatorsMocks.mockInternationalString());

        // Update
        indicatorsSystemsServiceFacade.updateIndicatorsSystem(getServiceContext(), indicatorsSystemDto);

        // Validation
        IndicatorsSystemDto indicatorsSystemDtoUpdated = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
        assertEquals(IndicatorsSystemStateEnum.DIFFUSION_VALIDATION, indicatorsSystemDtoUpdated.getState());
        IndicatorsAsserts.assertEqualsIndicatorsSystem(indicatorsSystemDto, indicatorsSystemDtoUpdated);
    }

    @Test
    public void testUpdateIndicatorsSystemReusingLocalisedStrings() throws Exception {

        String uuid = INDICATORS_SYSTEM_1;
        String versionNumber = "2.000";

        IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
        indicatorsSystemDto.getTitle().getTexts().iterator().next().setLabel("NewLabel");

        // Update
        indicatorsSystemsServiceFacade.updateIndicatorsSystem(getServiceContext(), indicatorsSystemDto);

        // Validation
        IndicatorsSystemDto indicatorsSystemDtoUpdated = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
        IndicatorsAsserts.assertEqualsIndicatorsSystem(indicatorsSystemDto, indicatorsSystemDtoUpdated);
    }

    @Test
    public void testUpdateIndicatorsSystemErrorNotExists() throws Exception {

        IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), INDICATORS_SYSTEM_1, "2.000");
        indicatorsSystemDto.setUuid(NOT_EXISTS);

        try {
            indicatorsSystemsServiceFacade.updateIndicatorsSystem(getServiceContext(), indicatorsSystemDto);
            fail("Indicators system not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateIndicatorsSystemErrorNotInProduction() throws Exception {

        String uuid = INDICATORS_SYSTEM_3;
        String versionNumber = INDICATORS_SYSTEM_3_VERSION;

        IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);

        try {
            indicatorsSystemsServiceFacade.updateIndicatorsSystem(getServiceContext(), indicatorsSystemDto);
            fail("Indicators system not in production");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_IN_PRODUCTION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateIndicatorsSystemErrorWrongVersion() throws Exception {

        String uuid = INDICATORS_SYSTEM_1;
        String versionNumber = "1.000";

        IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);

        try {
            indicatorsSystemsServiceFacade.updateIndicatorsSystem(getServiceContext(), indicatorsSystemDto);
            fail("Version 1 not is in production");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_STATE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorsSystemStateEnum.DRAFT, ((IndicatorsSystemStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(IndicatorsSystemStateEnum.VALIDATION_REJECTED, ((IndicatorsSystemStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
            assertEquals(IndicatorsSystemStateEnum.PRODUCTION_VALIDATION, ((IndicatorsSystemStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[2]);
            assertEquals(IndicatorsSystemStateEnum.DIFFUSION_VALIDATION, ((IndicatorsSystemStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[3]);
        }
    }

    @Test
    public void testUpdateIndicatorsErrorCodeNonModifiable() throws Exception {

        String uuid = INDICATORS_SYSTEM_1;
        String versionNumber = "2.000";

        IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
        indicatorsSystemDto.setCode("newCode");

        // Update
        try {
            indicatorsSystemsServiceFacade.updateIndicatorsSystem(getServiceContext(), indicatorsSystemDto);
            fail("Code is unmodifiable");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_UNMODIFIABLE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("INDICATORS_SYSTEM.CODE", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Override
    @Test
    public void testSendIndicatorsSystemToProductionValidation() throws Exception {

        String uuid = INDICATORS_SYSTEM_1;
        String diffusionVersion = "1.000";
        String productionVersion = "2.000";

        {
            IndicatorsSystemDto indicatorsSystemDtoV1 = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, diffusionVersion);
            IndicatorsSystemDto indicatorsSystemDtoV2 = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, productionVersion);
            assertEquals(diffusionVersion, indicatorsSystemDtoV1.getDiffusionVersion());
            assertEquals(productionVersion, indicatorsSystemDtoV2.getProductionVersion());
            assertEquals(IndicatorsSystemStateEnum.PUBLISHED, indicatorsSystemDtoV1.getState());
            assertEquals(IndicatorsSystemStateEnum.DRAFT, indicatorsSystemDtoV2.getState());
        }

        // Sends to production validation
        indicatorsSystemsServiceFacade.sendIndicatorsSystemToProductionValidation(getServiceContext2(), uuid);

        // Validation
        {
            IndicatorsSystemDto indicatorsSystemDtoV1 = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, diffusionVersion);
            IndicatorsSystemDto indicatorsSystemDtoV2 = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, productionVersion);
            assertEquals(diffusionVersion, indicatorsSystemDtoV1.getDiffusionVersion());
            assertEquals(productionVersion, indicatorsSystemDtoV2.getProductionVersion());
            assertEquals(IndicatorsSystemStateEnum.PUBLISHED, indicatorsSystemDtoV1.getState());
            assertEquals(IndicatorsSystemStateEnum.PRODUCTION_VALIDATION, indicatorsSystemDtoV2.getState());

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
    public void testSendIndicatorsSystemToProductionValidationInStateRejected() throws Exception {

        String uuid = INDICATORS_SYSTEM_9;
        String productionVersion = "1.000";

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, productionVersion);
            assertEquals(productionVersion, indicatorsSystemDto.getProductionVersion());
            assertNull(indicatorsSystemDto.getDiffusionVersion());
            assertEquals(IndicatorsSystemStateEnum.VALIDATION_REJECTED, indicatorsSystemDto.getState());
        }

        // Sends to production validation
        indicatorsSystemsServiceFacade.sendIndicatorsSystemToProductionValidation(getServiceContext2(), uuid);

        // Validation
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, productionVersion);
            assertEquals(productionVersion, indicatorsSystemDto.getProductionVersion());
            assertNull(indicatorsSystemDto.getDiffusionVersion());
            assertEquals(IndicatorsSystemStateEnum.PRODUCTION_VALIDATION, indicatorsSystemDto.getState());
        }
    }

    @Test
    public void testSendIndicatorsSystemToProductionValidationErrorNotExists() throws Exception {

        try {
            indicatorsSystemsServiceFacade.sendIndicatorsSystemToProductionValidation(getServiceContext(), NOT_EXISTS);
            fail("Indicators system not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testSendIndicatorsSystemToProductionValidationErrorWrongState() throws Exception {

        String uuid = INDICATORS_SYSTEM_3;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, INDICATORS_SYSTEM_3_VERSION);
            assertEquals(IndicatorsSystemStateEnum.PUBLISHED, indicatorsSystemDto.getState());
            assertNull(indicatorsSystemDto.getProductionVersion());
        }

        try {
            indicatorsSystemsServiceFacade.sendIndicatorsSystemToProductionValidation(getServiceContext(), uuid);
            fail("Indicators system is not draft");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_STATE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorsSystemStateEnum.DRAFT, ((IndicatorsSystemStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(IndicatorsSystemStateEnum.VALIDATION_REJECTED, ((IndicatorsSystemStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);

        }
    }

    @Override
    @Test
    public void testSendIndicatorsSystemToDiffusionValidation() throws Exception {

        String uuid = INDICATORS_SYSTEM_4;
        String versionNumber = "1.000";

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
            assertEquals(null, indicatorsSystemDto.getDiffusionVersion());
            assertEquals("1.000", indicatorsSystemDto.getProductionVersion());
            assertEquals(IndicatorsSystemStateEnum.PRODUCTION_VALIDATION, indicatorsSystemDto.getState());
        }

        // Sends to diffusion validation
        indicatorsSystemsServiceFacade.sendIndicatorsSystemToDiffusionValidation(getServiceContext(), uuid);

        // Validation
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
            assertEquals(null, indicatorsSystemDto.getDiffusionVersion());
            assertEquals("1.000", indicatorsSystemDto.getProductionVersion());
            assertEquals(IndicatorsSystemStateEnum.DIFFUSION_VALIDATION, indicatorsSystemDto.getState());

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
            indicatorsSystemsServiceFacade.sendIndicatorsSystemToDiffusionValidation(getServiceContext(), NOT_EXISTS);
            fail("Indicators system not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testSendIndicatorsSystemToDiffusionValidationErrorWrongStateDraft() throws Exception {

        String uuid = INDICATORS_SYSTEM_2;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, "1.000");
            assertEquals(IndicatorsSystemStateEnum.DRAFT, indicatorsSystemDto.getState());
            assertEquals("1.000", indicatorsSystemDto.getProductionVersion());
            assertEquals(null, indicatorsSystemDto.getDiffusionVersion());
        }

        try {
            indicatorsSystemsServiceFacade.sendIndicatorsSystemToDiffusionValidation(getServiceContext(), uuid);
            fail("Indicators system is not draft");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_STATE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorsSystemStateEnum.PRODUCTION_VALIDATION, ((IndicatorsSystemStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);

        }
    }

    @Test
    public void testSendIndicatorsSystemToDiffusionValidationErrorWrongStatePublished() throws Exception {

        String uuid = INDICATORS_SYSTEM_3;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, INDICATORS_SYSTEM_3_VERSION);
            assertEquals(IndicatorsSystemStateEnum.PUBLISHED, indicatorsSystemDto.getState());
            assertEquals(null, indicatorsSystemDto.getProductionVersion());
            assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDto.getDiffusionVersion());
        }

        try {
            indicatorsSystemsServiceFacade.sendIndicatorsSystemToDiffusionValidation(getServiceContext(), uuid);
            fail("Indicators system is not production validation");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_STATE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorsSystemStateEnum.PRODUCTION_VALIDATION, ((IndicatorsSystemStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);

        }
    }

    @Override
    @Test
    public void testRejectIndicatorsSystemValidation() throws Exception {

        String uuid = INDICATORS_SYSTEM_4;
        String versionNumber = "1.000";

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
            assertEquals(null, indicatorsSystemDto.getDiffusionVersion());
            assertEquals("1.000", indicatorsSystemDto.getProductionVersion());
            assertEquals(IndicatorsSystemStateEnum.PRODUCTION_VALIDATION, indicatorsSystemDto.getState());
        }

        // Rejects validation
        indicatorsSystemsServiceFacade.rejectIndicatorsSystemValidation(getServiceContext(), uuid);

        // Validation
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
            assertEquals(null, indicatorsSystemDto.getDiffusionVersion());
            assertEquals("1.000", indicatorsSystemDto.getProductionVersion());
            assertEquals(IndicatorsSystemStateEnum.VALIDATION_REJECTED, indicatorsSystemDto.getState());

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
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
            assertEquals(null, indicatorsSystemDto.getDiffusionVersion());
            assertEquals("1.000", indicatorsSystemDto.getProductionVersion());
            assertEquals(IndicatorsSystemStateEnum.DIFFUSION_VALIDATION, indicatorsSystemDto.getState());
        }

        // Rejects validation
        indicatorsSystemsServiceFacade.rejectIndicatorsSystemValidation(getServiceContext(), uuid);

        // Validation
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
            assertEquals(null, indicatorsSystemDto.getDiffusionVersion());
            assertEquals("1.000", indicatorsSystemDto.getProductionVersion());
            assertEquals(IndicatorsSystemStateEnum.VALIDATION_REJECTED, indicatorsSystemDto.getState());
        }
    }

    @Test
    public void testRejectIndicatorsSystemValidationErrorNotExists() throws Exception {

        try {
            indicatorsSystemsServiceFacade.rejectIndicatorsSystemValidation(getServiceContext(), NOT_EXISTS);
            fail("Indicators system not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRejectIndicatorsSystemValidationErrorWrongStateProduction() throws Exception {

        String uuid = INDICATORS_SYSTEM_2;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, "1.000");
            assertEquals(IndicatorsSystemStateEnum.DRAFT, indicatorsSystemDto.getState());
            assertEquals("1.000", indicatorsSystemDto.getProductionVersion());
            assertEquals(null, indicatorsSystemDto.getDiffusionVersion());
        }

        try {
            indicatorsSystemsServiceFacade.rejectIndicatorsSystemValidation(getServiceContext(), uuid);
            fail("Indicators system is not in validation");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_STATE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorsSystemStateEnum.PRODUCTION_VALIDATION, ((IndicatorsSystemStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(IndicatorsSystemStateEnum.DIFFUSION_VALIDATION, ((IndicatorsSystemStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
        }
    }

    @Test
    public void testRejectIndicatorsSystemValidationErrorWrongStateDiffusion() throws Exception {

        String uuid = INDICATORS_SYSTEM_3;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, INDICATORS_SYSTEM_3_VERSION);
            assertEquals(IndicatorsSystemStateEnum.PUBLISHED, indicatorsSystemDto.getState());
            assertEquals(null, indicatorsSystemDto.getProductionVersion());
            assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDto.getDiffusionVersion());
        }

        try {
            indicatorsSystemsServiceFacade.rejectIndicatorsSystemValidation(getServiceContext(), uuid);
            fail("Indicators system is not in validation");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_STATE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorsSystemStateEnum.PRODUCTION_VALIDATION, ((IndicatorsSystemStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(IndicatorsSystemStateEnum.DIFFUSION_VALIDATION, ((IndicatorsSystemStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
        }
    }

    @Override
    @Test
    public void testPublishIndicatorsSystem() throws Exception {

        String uuid = INDICATORS_SYSTEM_5;
        String versionNumber = "1.000";

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
            assertEquals(versionNumber, indicatorsSystemDto.getProductionVersion());
            assertEquals(null, indicatorsSystemDto.getDiffusionVersion());
            assertEquals(IndicatorsSystemStateEnum.DIFFUSION_VALIDATION, indicatorsSystemDto.getState());
        }

        // Publish
        indicatorsSystemsServiceFacade.publishIndicatorsSystem(getServiceContext(), uuid);

        // Validation
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
            assertEquals(null, indicatorsSystemDto.getProductionVersion());
            assertEquals(versionNumber, indicatorsSystemDto.getDiffusionVersion());
            assertEquals(IndicatorsSystemStateEnum.PUBLISHED, indicatorsSystemDto.getState());

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
            IndicatorsSystemDto indicatorsSystemDtoV1 = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, diffusionVersionBefore);
            IndicatorsSystemDto indicatorsSystemDtoV2 = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, productionVersionBefore);
            assertEquals(productionVersionBefore, indicatorsSystemDtoV1.getProductionVersion());
            assertEquals(diffusionVersionBefore, indicatorsSystemDtoV1.getDiffusionVersion());
            assertEquals(IndicatorsSystemStateEnum.PUBLISHED, indicatorsSystemDtoV1.getState());
            assertEquals(IndicatorsSystemStateEnum.DIFFUSION_VALIDATION, indicatorsSystemDtoV2.getState());
        }

        // Publish
        indicatorsSystemsServiceFacade.publishIndicatorsSystem(getServiceContext(), uuid);

        // Validation
        {
            // Version 1 already not exists
            try {
                indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, diffusionVersionBefore);
                fail("Indicators system version not exists");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_VERSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
                assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
                assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
                assertEquals(diffusionVersionBefore, e.getExceptionItems().get(0).getMessageParameters()[1]);
            }

            // Actual version in diffusion is version 2
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, productionVersionBefore);
            assertEquals(null, indicatorsSystemDto.getProductionVersion());
            assertEquals("2.000", indicatorsSystemDto.getDiffusionVersion());
            assertEquals(IndicatorsSystemStateEnum.PUBLISHED, indicatorsSystemDto.getState());
        }
    }

    @Test
    public void testPublishIndicatorsSystemWithArchivedVersion() throws Exception {

        String uuid = INDICATORS_SYSTEM_7;
        String diffusionVersionBefore = "1.000"; // will be deleted when publish current version in diffusion validation
        String productionVersionBefore = "2.000"; // will be published

        {
            IndicatorsSystemDto indicatorsSystemDtoV1 = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, diffusionVersionBefore);
            IndicatorsSystemDto indicatorsSystemDtoV2 = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, productionVersionBefore);
            assertEquals(productionVersionBefore, indicatorsSystemDtoV1.getProductionVersion());
            assertEquals(diffusionVersionBefore, indicatorsSystemDtoV1.getDiffusionVersion());
            assertEquals(IndicatorsSystemStateEnum.ARCHIVED, indicatorsSystemDtoV1.getState());
            assertEquals(IndicatorsSystemStateEnum.DIFFUSION_VALIDATION, indicatorsSystemDtoV2.getState());
        }

        // Publish
        indicatorsSystemsServiceFacade.publishIndicatorsSystem(getServiceContext(), uuid);

        // Validation
        {
            // Version 1 already not exists
            try {
                indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, diffusionVersionBefore);
                fail("Indicators system version not exists");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_VERSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
                assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
                assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
                assertEquals(diffusionVersionBefore, e.getExceptionItems().get(0).getMessageParameters()[1]);
            }

            // Actual version in diffusion is version 2
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, productionVersionBefore);
            assertEquals(null, indicatorsSystemDto.getProductionVersion());
            assertEquals("2.000", indicatorsSystemDto.getDiffusionVersion());
            assertEquals(IndicatorsSystemStateEnum.PUBLISHED, indicatorsSystemDto.getState());
        }
    }

    @Test
    public void testPublishIndicatorsSystemErrorNotExists() throws Exception {

        try {
            indicatorsSystemsServiceFacade.publishIndicatorsSystem(getServiceContext(), NOT_EXISTS);
            fail("Indicators system not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testPublishIndicatorsSystemErrorWrongStateProduction() throws Exception {

        String uuid = INDICATORS_SYSTEM_2;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, "1.000");
            assertEquals(IndicatorsSystemStateEnum.DRAFT, indicatorsSystemDto.getState());
            assertEquals("1.000", indicatorsSystemDto.getProductionVersion());
            assertEquals(null, indicatorsSystemDto.getDiffusionVersion());
        }

        try {
            indicatorsSystemsServiceFacade.publishIndicatorsSystem(getServiceContext(), uuid);
            fail("Indicators system is not diffusion validation");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_STATE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorsSystemStateEnum.DIFFUSION_VALIDATION, ((IndicatorsSystemStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);

        }
    }

    @Test
    public void testPublishIndicatorsSystemErrorWrongStateDiffusion() throws Exception {

        String uuid = INDICATORS_SYSTEM_3;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, INDICATORS_SYSTEM_3_VERSION);
            assertEquals(IndicatorsSystemStateEnum.PUBLISHED, indicatorsSystemDto.getState());
            assertEquals(null, indicatorsSystemDto.getProductionVersion());
            assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDto.getDiffusionVersion());
        }

        try {
            indicatorsSystemsServiceFacade.publishIndicatorsSystem(getServiceContext(), uuid);
            fail("Indicators system is not diffusion validation");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_STATE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorsSystemStateEnum.DIFFUSION_VALIDATION, ((IndicatorsSystemStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);

        }
    }

    @Override
    @Test
    public void testArchiveIndicatorsSystem() throws Exception {

        String uuid = INDICATORS_SYSTEM_3;
        String versionNumber = INDICATORS_SYSTEM_3_VERSION;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
            assertEquals(null, indicatorsSystemDto.getProductionVersion());
            assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDto.getDiffusionVersion());
            assertEquals(IndicatorsSystemStateEnum.PUBLISHED, indicatorsSystemDto.getState());
        }

        // Archive
        indicatorsSystemsServiceFacade.archiveIndicatorsSystem(getServiceContext(), uuid);

        // Validation
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
            assertEquals(null, indicatorsSystemDto.getProductionVersion());
            assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDto.getDiffusionVersion());
            assertEquals(IndicatorsSystemStateEnum.ARCHIVED, indicatorsSystemDto.getState());

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
            IndicatorsSystemDto indicatorsSystemDtoV1 = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, diffusionVersion);
            IndicatorsSystemDto indicatorsSystemDtoV2 = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, productionVersion);
            assertEquals(productionVersion, indicatorsSystemDtoV1.getProductionVersion());
            assertEquals(diffusionVersion, indicatorsSystemDtoV1.getDiffusionVersion());
            assertEquals(IndicatorsSystemStateEnum.PUBLISHED, indicatorsSystemDtoV1.getState());
            assertEquals(IndicatorsSystemStateEnum.DRAFT, indicatorsSystemDtoV2.getState());
        }

        // Archive
        indicatorsSystemsServiceFacade.archiveIndicatorsSystem(getServiceContext(), uuid);

        // Validation
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, diffusionVersion);
            assertEquals(productionVersion, indicatorsSystemDto.getProductionVersion());
            assertEquals(diffusionVersion, indicatorsSystemDto.getDiffusionVersion());
            assertEquals(IndicatorsSystemStateEnum.ARCHIVED, indicatorsSystemDto.getState());
        }
    }

    @Test
    public void testArchiveIndicatorsSystemErrorNotExists() throws Exception {

        try {
            indicatorsSystemsServiceFacade.archiveIndicatorsSystem(getServiceContext(), NOT_EXISTS);
            fail("Indicators system not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testArchiveIndicatorsSystemErrorWrongStateProduction() throws Exception {

        String uuid = INDICATORS_SYSTEM_2;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, "1.000");
            assertEquals(IndicatorsSystemStateEnum.DRAFT, indicatorsSystemDto.getState());
            assertEquals("1.000", indicatorsSystemDto.getProductionVersion());
            assertEquals(null, indicatorsSystemDto.getDiffusionVersion());
        }

        try {
            indicatorsSystemsServiceFacade.archiveIndicatorsSystem(getServiceContext(), uuid);
            fail("Indicators system is not published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_STATE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorsSystemStateEnum.PUBLISHED, ((IndicatorsSystemStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);

        }
    }

    @Test
    public void testArchiveIndicatorsSystemErrorWrongStateDiffusion() throws Exception {

        String uuid = INDICATORS_SYSTEM_8;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, "1.000");
            assertEquals(IndicatorsSystemStateEnum.ARCHIVED, indicatorsSystemDto.getState());
            assertEquals(null, indicatorsSystemDto.getProductionVersion());
            assertEquals("1.000", indicatorsSystemDto.getDiffusionVersion());
        }

        try {
            indicatorsSystemsServiceFacade.archiveIndicatorsSystem(getServiceContext(), uuid);
            fail("Indicators system is not published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_STATE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorsSystemStateEnum.PUBLISHED, ((IndicatorsSystemStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Override
    @Test
    public void testVersioningIndicatorsSystem() throws Exception {

        String uuid = INDICATORS_SYSTEM_3;
        String newVersionExpected = "12.000";

        IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, null);
        assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDto.getVersionNumber());
        assertEquals(null, indicatorsSystemDto.getProductionVersion());
        assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDto.getDiffusionVersion());

        IndicatorsSystemDto indicatorsSystemDtoVersioned = indicatorsSystemsServiceFacade.versioningIndicatorsSystem(getServiceContext(), uuid, VersiontTypeEnum.MAJOR);

        // Validate
        assertEquals(newVersionExpected, indicatorsSystemDtoVersioned.getVersionNumber());
        assertEquals(newVersionExpected, indicatorsSystemDtoVersioned.getProductionVersion());
        assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDtoVersioned.getDiffusionVersion());
        IndicatorsAsserts.assertEqualsIndicatorsSystem(indicatorsSystemDto, indicatorsSystemDtoVersioned);

        {
            IndicatorsSystemDto indicatorsSystemDtoProduction = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, newVersionExpected);
            IndicatorsSystemDto indicatorsSystemDtoDiffusion = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, INDICATORS_SYSTEM_3_VERSION);

            IndicatorsAsserts.assertEqualsIndicatorsSystem(indicatorsSystemDtoDiffusion, indicatorsSystemDtoProduction);

            assertEquals(IndicatorsSystemStateEnum.DRAFT, indicatorsSystemDtoProduction.getState());
            assertEquals(newVersionExpected, indicatorsSystemDtoProduction.getVersionNumber());
            assertEquals(newVersionExpected, indicatorsSystemDtoProduction.getProductionVersion());
            assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDtoProduction.getDiffusionVersion());

            // Dimensions
            List<DimensionDto> dimensions = indicatorsSystemsServiceFacade.findDimensions(getServiceContext(), indicatorsSystemDtoProduction.getUuid(),
                    indicatorsSystemDtoProduction.getProductionVersion());
            assertEquals(1, dimensions.size());
            IndicatorsAsserts.assertEqualsInternationalString(dimensions.get(0).getTitle(), "es", "Título IndSys-3-v1-Dimension-1", "en", "Title IndSys-3-v1-Dimension-1");
            assertEquals(1, dimensions.get(0).getSubdimensions().size());
            IndicatorsAsserts.assertEqualsInternationalString(((DimensionDto) dimensions.get(0).getSubdimensions().get(0)).getTitle(), "es", "Título IndSys-3-v1-Dimension-1A", "en",
                    "Title IndSys-3-v1-Dimension-1A");

            // Indicator instances
            List<IndicatorInstanceDto> indicatorsInstances = indicatorsSystemsServiceFacade.findIndicatorsInstances(getServiceContext(), indicatorsSystemDtoProduction.getUuid(),
                    indicatorsSystemDtoProduction.getProductionVersion());
            assertEquals(2, indicatorsInstances.size());
            assertEquals("IndicatorC", indicatorsInstances.get(0).getIndicatorUuid());
            assertEquals("IndicatorB", indicatorsInstances.get(1).getIndicatorUuid());

            assertEquals(IndicatorsSystemStateEnum.PUBLISHED, indicatorsSystemDtoDiffusion.getState());
            assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDtoDiffusion.getVersionNumber());
            assertEquals(newVersionExpected, indicatorsSystemDtoDiffusion.getProductionVersion());
            assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDtoDiffusion.getDiffusionVersion());
        }
    }

    @Test
    public void testVersioningIndicatorsSystemVersionMinor() throws Exception {

        String uuid = INDICATORS_SYSTEM_3;
        String newVersionExpected = "11.034";

        IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, null);
        assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDto.getVersionNumber());
        assertEquals(null, indicatorsSystemDto.getProductionVersion());
        assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDto.getDiffusionVersion());

        IndicatorsSystemDto indicatorsSystemDtoVersioned = indicatorsSystemsServiceFacade.versioningIndicatorsSystem(getServiceContext(), uuid, VersiontTypeEnum.MINOR);

        // Validate
        assertEquals(newVersionExpected, indicatorsSystemDtoVersioned.getVersionNumber());
        assertEquals(newVersionExpected, indicatorsSystemDtoVersioned.getProductionVersion());
        assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDtoVersioned.getDiffusionVersion());
        IndicatorsAsserts.assertEqualsIndicatorsSystem(indicatorsSystemDto, indicatorsSystemDtoVersioned);

        {
            IndicatorsSystemDto indicatorsSystemDtoProduction = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, newVersionExpected);
            IndicatorsSystemDto indicatorsSystemDtoDiffusion = indicatorsSystemsServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, INDICATORS_SYSTEM_3_VERSION);

            IndicatorsAsserts.assertEqualsIndicatorsSystem(indicatorsSystemDtoDiffusion, indicatorsSystemDtoProduction);

            assertEquals(IndicatorsSystemStateEnum.DRAFT, indicatorsSystemDtoProduction.getState());
            assertEquals(newVersionExpected, indicatorsSystemDtoProduction.getVersionNumber());
            assertEquals(newVersionExpected, indicatorsSystemDtoProduction.getProductionVersion());
            assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDtoProduction.getDiffusionVersion());

            assertEquals(IndicatorsSystemStateEnum.PUBLISHED, indicatorsSystemDtoDiffusion.getState());
            assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDtoDiffusion.getVersionNumber());
            assertEquals(newVersionExpected, indicatorsSystemDtoDiffusion.getProductionVersion());
            assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDtoDiffusion.getDiffusionVersion());
        }
    }

    @Test
    public void testVersioningIndicatorsSystemErrorNotExists() throws Exception {

        try {
            indicatorsSystemsServiceFacade.versioningIndicatorsSystem(getServiceContext(), NOT_EXISTS, VersiontTypeEnum.MINOR);
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
            indicatorsSystemsServiceFacade.versioningIndicatorsSystem(getServiceContext(), uuid, VersiontTypeEnum.MINOR);
            fail("Indicators system already exists in production");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_STATE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorsSystemStateEnum.PUBLISHED, ((IndicatorsSystemStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(IndicatorsSystemStateEnum.ARCHIVED, ((IndicatorsSystemStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
        }
    }

    @Override
    @Test
    public void testFindIndicatorsSystems() throws Exception {

        // Retrieve last versions...
        List<IndicatorsSystemDto> indicatorsSystemsDto = indicatorsSystemsServiceFacade.findIndicatorsSystems(getServiceContext());
        assertEquals(9, indicatorsSystemsDto.size());

        assertEquals(INDICATORS_SYSTEM_1, indicatorsSystemsDto.get(0).getUuid());
        assertEquals(IndicatorsSystemStateEnum.DRAFT, indicatorsSystemsDto.get(0).getState());

        assertEquals(INDICATORS_SYSTEM_2, indicatorsSystemsDto.get(1).getUuid());
        assertEquals(IndicatorsSystemStateEnum.DRAFT, indicatorsSystemsDto.get(1).getState());

        assertEquals(INDICATORS_SYSTEM_3, indicatorsSystemsDto.get(2).getUuid());
        assertEquals(IndicatorsSystemStateEnum.PUBLISHED, indicatorsSystemsDto.get(2).getState());

        assertEquals(INDICATORS_SYSTEM_4, indicatorsSystemsDto.get(3).getUuid());
        assertEquals(IndicatorsSystemStateEnum.PRODUCTION_VALIDATION, indicatorsSystemsDto.get(3).getState());

        assertEquals(INDICATORS_SYSTEM_5, indicatorsSystemsDto.get(4).getUuid());
        assertEquals(IndicatorsSystemStateEnum.DIFFUSION_VALIDATION, indicatorsSystemsDto.get(4).getState());

        assertEquals(INDICATORS_SYSTEM_6, indicatorsSystemsDto.get(5).getUuid());
        assertEquals(IndicatorsSystemStateEnum.DIFFUSION_VALIDATION, indicatorsSystemsDto.get(5).getState());

        assertEquals(INDICATORS_SYSTEM_7, indicatorsSystemsDto.get(6).getUuid());
        assertEquals(IndicatorsSystemStateEnum.DIFFUSION_VALIDATION, indicatorsSystemsDto.get(6).getState());

        assertEquals(INDICATORS_SYSTEM_8, indicatorsSystemsDto.get(7).getUuid());
        assertEquals(IndicatorsSystemStateEnum.ARCHIVED, indicatorsSystemsDto.get(7).getState());

        assertEquals(INDICATORS_SYSTEM_9, indicatorsSystemsDto.get(8).getUuid());
        assertEquals(IndicatorsSystemStateEnum.VALIDATION_REJECTED, indicatorsSystemsDto.get(8).getState());
    }

    @Override
    @Test
    public void testFindIndicatorsSystemsPublished() throws Exception {

        List<IndicatorsSystemDto> indicatorsSystemsDto = indicatorsSystemsServiceFacade.findIndicatorsSystemsPublished(getServiceContext());
        assertEquals(3, indicatorsSystemsDto.size());

        assertEquals(INDICATORS_SYSTEM_1, indicatorsSystemsDto.get(0).getUuid());
        assertEquals(IndicatorsSystemStateEnum.PUBLISHED, indicatorsSystemsDto.get(0).getState());

        assertEquals(INDICATORS_SYSTEM_3, indicatorsSystemsDto.get(1).getUuid());
        assertEquals(IndicatorsSystemStateEnum.PUBLISHED, indicatorsSystemsDto.get(1).getState());

        assertEquals(INDICATORS_SYSTEM_6, indicatorsSystemsDto.get(2).getUuid());
        assertEquals(IndicatorsSystemStateEnum.PUBLISHED, indicatorsSystemsDto.get(2).getState());
    }

    @Override
    @Test
    public void testRetrieveDimension() throws Exception {

        DimensionDto dimensionDto = indicatorsSystemsServiceFacade.retrieveDimension(getServiceContext(), DIMENSION_1_INDICATORS_SYSTEM_1_V2);

        assertNotNull(dimensionDto);
        assertEquals(DIMENSION_1_INDICATORS_SYSTEM_1_V2, dimensionDto.getUuid());
        IndicatorsAsserts.assertEqualsInternationalString(dimensionDto.getTitle(), "es", "Título IndSys-1-v2-Dimension-1", "en", "Title IndSys-1-v2-Dimension-1");
        assertNull(dimensionDto.getParentUuid());

        IndicatorsAsserts.assertEqualsDate("2011-03-03 01:02:04", dimensionDto.getCreatedDate());
        assertEquals("user1", dimensionDto.getCreatedBy());
        IndicatorsAsserts.assertEqualsDate("2011-03-04 05:06:07", dimensionDto.getLastUpdated());
        assertEquals("user2", dimensionDto.getLastUpdatedBy());

        // Subdimensions
        assertEquals(2, dimensionDto.getSubdimensions().size());

        {
            DimensionDto subdimensionDto = (DimensionDto) dimensionDto.getSubdimensions().get(0);
            assertEquals(DIMENSION_1A_INDICATORS_SYSTEM_1_V2, subdimensionDto.getUuid());
            assertEquals(dimensionDto.getUuid(), subdimensionDto.getParentUuid());
            IndicatorsAsserts.assertEqualsInternationalString(subdimensionDto.getTitle(), "es", "Título IndSys-1-v2-Dimension-1A", "en", "Title IndSys-1-v2-Dimension-1A");
            assertEquals(0, subdimensionDto.getSubdimensions().size());
        }
        {
            DimensionDto subdimensionDto = (DimensionDto) dimensionDto.getSubdimensions().get(1);
            assertEquals(DIMENSION_1B_INDICATORS_SYSTEM_1_V2, subdimensionDto.getUuid());
            assertEquals(dimensionDto.getUuid(), subdimensionDto.getParentUuid());
            IndicatorsAsserts.assertEqualsInternationalString(subdimensionDto.getTitle(), "es", "Título IndSys-1-v2-Dimension-1B", "en", "Title IndSys-1-v2-Dimension-1B");
            assertEquals(1, subdimensionDto.getSubdimensions().size());

            // Subdimensions
            {
                DimensionDto subsubdimensionDto = (DimensionDto) subdimensionDto.getSubdimensions().get(0);
                assertEquals(DIMENSION_1BA_INDICATORS_SYSTEM_1_V2, subsubdimensionDto.getUuid());
                assertEquals(subdimensionDto.getUuid(), subsubdimensionDto.getParentUuid());
                IndicatorsAsserts.assertEqualsInternationalString(subsubdimensionDto.getTitle(), "es", "Título IndSys-1-v2-Dimension-1BA", "en", "Title IndSys-1-v2-Dimension-1BA");
                assertEquals(0, subsubdimensionDto.getSubdimensions().size());
            }
        }
    }

    @Test
    public void testRetrieveDimensionErrorParameterRequired() throws Exception {

        String uuid = null;

        try {
            indicatorsSystemsServiceFacade.retrieveDimension(getServiceContext(), uuid);
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
            indicatorsSystemsServiceFacade.retrieveDimension(getServiceContext(), uuid);
            fail("No exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DIMENSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Override
    @Test
    public void testCreateDimension() throws Exception {

        // Create dimension
        DimensionDto dimensionDto = new DimensionDto();
        dimensionDto.setTitle(IndicatorsMocks.mockInternationalString());
        dimensionDto.setParentUuid(null);
        dimensionDto.setOrderInLevel(Long.valueOf(5));

        String uuidIndicatorsSystem = INDICATORS_SYSTEM_1;
        DimensionDto dimensionDtoCreated = indicatorsSystemsServiceFacade.createDimension(getServiceContext(), uuidIndicatorsSystem, dimensionDto);
        assertNotNull(dimensionDtoCreated.getUuid());

        // Retrieve dimension
        DimensionDto dimensionDtoRetrieved = indicatorsSystemsServiceFacade.retrieveDimension(getServiceContext(), dimensionDtoCreated.getUuid());
        IndicatorsAsserts.assertEqualsDimension(dimensionDto, dimensionDtoRetrieved);
        assertEquals(0, dimensionDtoRetrieved.getSubdimensions().size());

        // Validate new structure
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContext(), INDICATORS_SYSTEM_1, "2.000");
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
    }

    @Test
    public void testCreateDimensionWithOrderInMiddle() throws Exception {

        // Create dimension
        DimensionDto dimensionDto = new DimensionDto();
        dimensionDto.setTitle(IndicatorsMocks.mockInternationalString());
        dimensionDto.setParentUuid(null);
        dimensionDto.setOrderInLevel(Long.valueOf(2));

        String uuidIndicatorsSystem = INDICATORS_SYSTEM_1;
        DimensionDto dimensionDtoCreated = indicatorsSystemsServiceFacade.createDimension(getServiceContext(), uuidIndicatorsSystem, dimensionDto);
        assertNotNull(dimensionDtoCreated.getUuid());

        // Retrieve dimension
        DimensionDto dimensionDtoRetrieved = indicatorsSystemsServiceFacade.retrieveDimension(getServiceContext(), dimensionDtoCreated.getUuid());
        IndicatorsAsserts.assertEqualsDimension(dimensionDto, dimensionDtoRetrieved);
        assertEquals(0, dimensionDtoRetrieved.getSubdimensions().size());

        // Validate new structure
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContext(), INDICATORS_SYSTEM_1, "2.000");
        assertEquals(5, indicatorsSystemStructureDto.getElements().size());
        assertEquals(INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(0).getIndicatorInstance().getUuid());
        assertEquals(Long.valueOf(1), indicatorsSystemStructureDto.getElements().get(0).getOrderInLevel());
        assertEquals(dimensionDtoCreated.getUuid(), indicatorsSystemStructureDto.getElements().get(1).getDimension().getUuid());
        assertEquals(Long.valueOf(2), indicatorsSystemStructureDto.getElements().get(1).getOrderInLevel());
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

        // Retrieve dimension parent
        DimensionDto dimensionDtoParent = indicatorsSystemsServiceFacade.retrieveDimension(getServiceContext(), parentUuid);
        assertEquals(2, dimensionDtoParent.getSubdimensions().size());

        // Create dimension
        DimensionDto dimensionDto = new DimensionDto();
        dimensionDto.setTitle(IndicatorsMocks.mockInternationalString());
        dimensionDto.setParentUuid(DIMENSION_1_INDICATORS_SYSTEM_1_V2);
        dimensionDto.setOrderInLevel(Long.valueOf(3));
        String uuidIndicatorsSystem = INDICATORS_SYSTEM_1;
        DimensionDto dimensionDtoCreated = indicatorsSystemsServiceFacade.createDimension(getServiceContext(), uuidIndicatorsSystem, dimensionDto);
        assertNotNull(dimensionDtoCreated.getUuid());

        // Retrieve dimension
        DimensionDto dimensionDtoRetrieved = indicatorsSystemsServiceFacade.retrieveDimension(getServiceContext(), dimensionDtoCreated.getUuid());
        IndicatorsAsserts.assertEqualsDimension(dimensionDto, dimensionDtoRetrieved);
        assertEquals(parentUuid, dimensionDtoRetrieved.getParentUuid());
        assertEquals(0, dimensionDtoRetrieved.getSubdimensions().size());

        // Retrieve dimension parent (dimension had two subdimensions before; now has three subdimensions)
        dimensionDtoParent = indicatorsSystemsServiceFacade.retrieveDimension(getServiceContext(), parentUuid);
        assertEquals(3, dimensionDtoParent.getSubdimensions().size());

        assertEquals(DIMENSION_1A_INDICATORS_SYSTEM_1_V2, dimensionDtoParent.getSubdimensions().get(0).getUuid());
        assertEquals(Long.valueOf(1), dimensionDtoParent.getSubdimensions().get(0).getOrderInLevel());
        assertEquals(DIMENSION_1B_INDICATORS_SYSTEM_1_V2, dimensionDtoParent.getSubdimensions().get(1).getUuid());
        assertEquals(Long.valueOf(2), dimensionDtoParent.getSubdimensions().get(1).getOrderInLevel());
        assertEquals(dimensionDtoCreated.getUuid(), dimensionDtoParent.getSubdimensions().get(2).getUuid());
        assertEquals(Long.valueOf(3), dimensionDtoParent.getSubdimensions().get(2).getOrderInLevel());
    }

    @Test
    public void testCreateDimensionSubdimensionOrderInMiddle() throws Exception {

        String parentUuid = DIMENSION_1_INDICATORS_SYSTEM_1_V2;

        // Retrieve dimension parent
        DimensionDto dimensionDtoParent = indicatorsSystemsServiceFacade.retrieveDimension(getServiceContext(), parentUuid);
        assertEquals(2, dimensionDtoParent.getSubdimensions().size());

        // Create dimension
        DimensionDto dimensionDto = new DimensionDto();
        dimensionDto.setTitle(IndicatorsMocks.mockInternationalString());
        dimensionDto.setParentUuid(DIMENSION_1_INDICATORS_SYSTEM_1_V2);
        dimensionDto.setOrderInLevel(Long.valueOf(2));
        String uuidIndicatorsSystem = INDICATORS_SYSTEM_1;
        DimensionDto dimensionDtoCreated = indicatorsSystemsServiceFacade.createDimension(getServiceContext(), uuidIndicatorsSystem, dimensionDto);
        assertNotNull(dimensionDtoCreated.getUuid());

        // Retrieve dimension
        DimensionDto dimensionDtoRetrieved = indicatorsSystemsServiceFacade.retrieveDimension(getServiceContext(), dimensionDtoCreated.getUuid());
        IndicatorsAsserts.assertEqualsDimension(dimensionDto, dimensionDtoRetrieved);
        assertEquals(parentUuid, dimensionDtoRetrieved.getParentUuid());
        assertEquals(0, dimensionDtoRetrieved.getSubdimensions().size());

        // Retrieve dimension parent (indicators system version had two dimensions before; now has three dimensions)
        dimensionDtoParent = indicatorsSystemsServiceFacade.retrieveDimension(getServiceContext(), parentUuid);
        assertEquals(3, dimensionDtoParent.getSubdimensions().size());

        assertEquals(DIMENSION_1A_INDICATORS_SYSTEM_1_V2, dimensionDtoParent.getSubdimensions().get(0).getUuid());
        assertEquals(Long.valueOf(1), dimensionDtoParent.getSubdimensions().get(0).getOrderInLevel());
        assertEquals(dimensionDtoCreated.getUuid(), dimensionDtoParent.getSubdimensions().get(1).getUuid());
        assertEquals(Long.valueOf(2), dimensionDtoParent.getSubdimensions().get(1).getOrderInLevel());
        assertEquals(DIMENSION_1B_INDICATORS_SYSTEM_1_V2, dimensionDtoParent.getSubdimensions().get(2).getUuid());
        assertEquals(Long.valueOf(3), dimensionDtoParent.getSubdimensions().get(2).getOrderInLevel());
    }

    @Test
    public void testCreateDimensionSubSubdimension() throws Exception {

        String parentUuid = DIMENSION_1A_INDICATORS_SYSTEM_1_V2;

        // Retrieve dimension parent
        DimensionDto dimensionDtoParent = indicatorsSystemsServiceFacade.retrieveDimension(getServiceContext(), parentUuid);
        assertEquals(0, dimensionDtoParent.getSubdimensions().size());

        // Create subdimension
        DimensionDto dimensionDto = new DimensionDto();
        dimensionDto.setTitle(IndicatorsMocks.mockInternationalString());
        dimensionDto.setParentUuid(parentUuid);
        dimensionDto.setOrderInLevel(Long.valueOf(1));
        String uuidIndicatorsSystem = INDICATORS_SYSTEM_1;
        DimensionDto dimensionDtoCreated = indicatorsSystemsServiceFacade.createDimension(getServiceContext(), uuidIndicatorsSystem, dimensionDto);
        assertNotNull(dimensionDtoCreated.getUuid());

        // Validate
        DimensionDto dimensionDtoRetrieved = indicatorsSystemsServiceFacade.retrieveDimension(getServiceContext(), dimensionDtoCreated.getUuid());
        IndicatorsAsserts.assertEqualsDimension(dimensionDtoCreated, dimensionDtoRetrieved);
        assertEquals(parentUuid, dimensionDtoRetrieved.getParentUuid());

        // Retrieve dimension parent
        dimensionDtoParent = indicatorsSystemsServiceFacade.retrieveDimension(getServiceContext(), parentUuid);
        assertEquals(1, dimensionDtoParent.getSubdimensions().size());
        IndicatorsAsserts.assertEqualsDimension(dimensionDtoCreated, (DimensionDto) dimensionDtoParent.getSubdimensions().get(0));
    }

    @Test
    public void testCreateDimensionErrorParametersRequired() throws Exception {

        DimensionDto dimensionDto = new DimensionDto();
        dimensionDto.setTitle(null);
        dimensionDto.setOrderInLevel(null);
        try {
            indicatorsSystemsServiceFacade.createDimension(getServiceContext(), INDICATORS_SYSTEM_1, dimensionDto);
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
            indicatorsSystemsServiceFacade.createDimension(getServiceContext(), INDICATORS_SYSTEM_1, dimensionDto);
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
            indicatorsSystemsServiceFacade.createDimension(getServiceContext(), INDICATORS_SYSTEM_1, dimensionDto);
            fail("order incorrect");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("DIMENSION.ORDER_IN_LEVEL", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateDimensionErrorSubdimensionsNotEmpty() throws Exception {

        DimensionDto dimensionDto = new DimensionDto();
        dimensionDto.setTitle(IndicatorsMocks.mockInternationalString());
        dimensionDto.setOrderInLevel(Long.valueOf(1));

        DimensionDto subdimensionDto = new DimensionDto();
        subdimensionDto.setTitle(IndicatorsMocks.mockInternationalString());
        dimensionDto.addSubdimension(subdimensionDto);

        try {
            indicatorsSystemsServiceFacade.createDimension(getServiceContext(), INDICATORS_SYSTEM_1, dimensionDto);
            fail("subdimensions must be empty");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("DIMENSION.SUBDIMENSIONS", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateDimensionErrorIndicatorsSystemNotExists() throws Exception {

        DimensionDto dimensionDto = new DimensionDto();
        dimensionDto.setTitle(IndicatorsMocks.mockInternationalString());
        dimensionDto.setOrderInLevel(Long.valueOf(1));
        try {
            indicatorsSystemsServiceFacade.createDimension(getServiceContext(), NOT_EXISTS, dimensionDto);
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
            indicatorsSystemsServiceFacade.createDimension(getServiceContext(), indicatorsSystemUuid, dimensionDto);
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
            indicatorsSystemsServiceFacade.createDimension(getServiceContext(), INDICATORS_SYSTEM_1, dimensionDto);
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
            indicatorsSystemsServiceFacade.createDimension(getServiceContext(), INDICATORS_SYSTEM_2, dimensionDto);
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
        DimensionDto dimensionDto = indicatorsSystemsServiceFacade.retrieveDimension(getServiceContext(), uuid);
        assertEquals(2, dimensionDto.getSubdimensions().size());
        assertEquals(Long.valueOf(2), dimensionDto.getOrderInLevel());

        // Delete dimension
        indicatorsSystemsServiceFacade.deleteDimension(getServiceContext(), uuid);

        // Validation
        try {
            indicatorsSystemsServiceFacade.retrieveDimension(getServiceContext(), uuid);
            fail("Dimension deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DIMENSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }

        // Check subdimensions deleted
        for (DimensionDto subdDimensionDto : dimensionDto.getSubdimensions()) {
            try {
                indicatorsSystemsServiceFacade.retrieveDimension(getServiceContext(), subdDimensionDto.getUuid());
                fail("Subimension deleted");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.DIMENSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
                assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
                assertEquals(subdDimensionDto.getUuid(), e.getExceptionItems().get(0).getMessageParameters()[0]);
            }
        }

        // Checks orders of other elements are updated in same level
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContext(), INDICATORS_SYSTEM_1, "2.000");
        assertEquals(3, indicatorsSystemStructureDto.getElements().size());
        assertEquals(INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(0).getIndicatorInstance().getUuid());
        assertEquals(Long.valueOf(1), indicatorsSystemStructureDto.getElements().get(0).getOrderInLevel());
        assertEquals(INDICATOR_INSTANCE_2_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(1).getIndicatorInstance().getUuid());
        assertEquals(Long.valueOf(2), indicatorsSystemStructureDto.getElements().get(1).getOrderInLevel());
        assertEquals(DIMENSION_2_INDICATORS_SYSTEM_1_V2, indicatorsSystemStructureDto.getElements().get(2).getDimension().getUuid());
        assertEquals(Long.valueOf(3), indicatorsSystemStructureDto.getElements().get(2).getOrderInLevel());
    }
    
    @Test
    public void testDeleteDimensionSubdimension() throws Exception {

        String uuid = DIMENSION_1B_INDICATORS_SYSTEM_1_V2;

        // Dimension parent
        DimensionDto dimensionDtoParent = indicatorsSystemsServiceFacade.retrieveDimension(getServiceContext(), DIMENSION_1_INDICATORS_SYSTEM_1_V2);
        assertEquals(2, dimensionDtoParent.getSubdimensions().size());
        assertEquals(DIMENSION_1A_INDICATORS_SYSTEM_1_V2, dimensionDtoParent.getSubdimensions().get(0).getUuid());
        assertEquals(DIMENSION_1B_INDICATORS_SYSTEM_1_V2, dimensionDtoParent.getSubdimensions().get(1).getUuid());

        // Delete dimension
        indicatorsSystemsServiceFacade.deleteDimension(getServiceContext(), uuid);

        // Validation
        try {
            indicatorsSystemsServiceFacade.retrieveDimension(getServiceContext(), uuid);
            fail("Dimension deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DIMENSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }

        // Check subdimensions deleted
        try {
            indicatorsSystemsServiceFacade.retrieveDimension(getServiceContext(), DIMENSION_1BA_INDICATORS_SYSTEM_1_V2);
            fail("Dimension deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DIMENSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(DIMENSION_1BA_INDICATORS_SYSTEM_1_V2, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }

        // Checks orders of other elements are updated in same level
        dimensionDtoParent = indicatorsSystemsServiceFacade.retrieveDimension(getServiceContext(), DIMENSION_1_INDICATORS_SYSTEM_1_V2);
        assertEquals(1, dimensionDtoParent.getSubdimensions().size());
        assertEquals(DIMENSION_1A_INDICATORS_SYSTEM_1_V2, dimensionDtoParent.getSubdimensions().get(0).getUuid());
    }

    @Test
    public void testDeleteDimensionErrorIndicatorsSystemVersionPublished() throws Exception {

        try {
            indicatorsSystemsServiceFacade.deleteDimension(getServiceContext(), DIMENSION_1_INDICATORS_SYSTEM_3);
            fail("Indicators system not in production");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_STATE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(INDICATORS_SYSTEM_3, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorsSystemStateEnum.DRAFT, ((IndicatorsSystemStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(IndicatorsSystemStateEnum.VALIDATION_REJECTED, ((IndicatorsSystemStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
            assertEquals(IndicatorsSystemStateEnum.PRODUCTION_VALIDATION, ((IndicatorsSystemStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[2]);
            assertEquals(IndicatorsSystemStateEnum.DIFFUSION_VALIDATION, ((IndicatorsSystemStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[3]);
        }
    }

    @Test
    public void testDeleteDimensionErrorNotExists() throws Exception {

        String uuid = NOT_EXISTS;

        // Validation
        try {
            indicatorsSystemsServiceFacade.deleteDimension(getServiceContext(), uuid);
            fail("Dimension not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DIMENSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Override
    @Test
    public void testFindDimensions() throws Exception {

        String uuidIndicatorsSystem = INDICATORS_SYSTEM_1;

        // Version 1.000
        {
            List<DimensionDto> dimensionsDto = indicatorsSystemsServiceFacade.findDimensions(getServiceContext(), uuidIndicatorsSystem, "1.000");
            assertEquals(1, dimensionsDto.size());
            assertEquals(DIMENSION_1_INDICATORS_SYSTEM_1_V1, dimensionsDto.get(0).getUuid());
        }

        // Version 2.000
        {
            List<DimensionDto> dimensionsDto = indicatorsSystemsServiceFacade.findDimensions(getServiceContext(), uuidIndicatorsSystem, "2.000");
            assertEquals(2, dimensionsDto.size());

            assertEquals(DIMENSION_1_INDICATORS_SYSTEM_1_V2, dimensionsDto.get(0).getUuid());

            assertEquals(2, dimensionsDto.get(0).getSubdimensions().size());
            assertEquals(DIMENSION_1A_INDICATORS_SYSTEM_1_V2, dimensionsDto.get(0).getSubdimensions().get(0).getUuid());
            assertEquals(DIMENSION_1B_INDICATORS_SYSTEM_1_V2, dimensionsDto.get(0).getSubdimensions().get(1).getUuid());
            assertEquals(1, dimensionsDto.get(0).getSubdimensions().get(1).getSubdimensions().size());
            assertEquals(DIMENSION_1BA_INDICATORS_SYSTEM_1_V2, dimensionsDto.get(0).getSubdimensions().get(1).getSubdimensions().get(0).getUuid());

            assertEquals(DIMENSION_2_INDICATORS_SYSTEM_1_V2, dimensionsDto.get(1).getUuid());
        }
    }

    @Test
    public void testFindDimensionsErrorNotExists() throws Exception {

        String uuid = NOT_EXISTS;

        // Validation
        try {
            indicatorsSystemsServiceFacade.findDimensions(getServiceContext(), uuid, "1.000");
            fail("Indicators system not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_VERSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals("1.000", e.getExceptionItems().get(0).getMessageParameters()[1]);
        }
    }

    @Override
    @Test
    public void testUpdateDimension() throws Exception {

        String uuid = DIMENSION_1_INDICATORS_SYSTEM_1_V2;
        DimensionDto dimensionDto = indicatorsSystemsServiceFacade.retrieveDimension(getServiceContext(), uuid);
        assertEquals(2, dimensionDto.getSubdimensions().size());
        dimensionDto.setTitle(IndicatorsMocks.mockInternationalString());
        dimensionDto.getSubdimensions().clear(); // ignore changes of subdimensions

        // Update
        indicatorsSystemsServiceFacade.updateDimension(getServiceContext(), dimensionDto);

        // Validation
        DimensionDto dimensionDtoUpdated = indicatorsSystemsServiceFacade.retrieveDimension(getServiceContext(), uuid);
        assertEquals(2, dimensionDtoUpdated.getSubdimensions().size());
        IndicatorsAsserts.assertEqualsDimension(dimensionDto, dimensionDtoUpdated);
    }

    @Test
    public void testUpdateDimensionErrorChangeParentDimensionAndOrder() throws Exception {

        DimensionDto dimensionDto = indicatorsSystemsServiceFacade.retrieveDimension(getServiceContext(), DIMENSION_1A_INDICATORS_SYSTEM_1_V2);
        assertEquals(DIMENSION_1_INDICATORS_SYSTEM_1_V2, dimensionDto.getParentUuid());
        assertEquals(Long.valueOf(1), dimensionDto.getOrderInLevel());
        dimensionDto.setParentUuid(DIMENSION_1BA_INDICATORS_SYSTEM_1_V2);
        dimensionDto.setOrderInLevel(Long.valueOf(2));

        try {
            indicatorsSystemsServiceFacade.updateDimension(getServiceContext(), dimensionDto);
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
            indicatorsSystemsServiceFacade.updateDimension(getServiceContext(), dimensionDto);
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

        DimensionDto dimensionDto = indicatorsSystemsServiceFacade.retrieveDimension(getServiceContext(), DIMENSION_1_INDICATORS_SYSTEM_1_V1);

        try {
            indicatorsSystemsServiceFacade.updateDimension(getServiceContext(), dimensionDto);
            fail("Indicators system published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_STATE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(INDICATORS_SYSTEM_1, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorsSystemStateEnum.DRAFT, ((IndicatorsSystemStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(IndicatorsSystemStateEnum.VALIDATION_REJECTED, ((IndicatorsSystemStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
            assertEquals(IndicatorsSystemStateEnum.PRODUCTION_VALIDATION, ((IndicatorsSystemStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[2]);
            assertEquals(IndicatorsSystemStateEnum.DIFFUSION_VALIDATION, ((IndicatorsSystemStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[3]);
        }
    }

    @Test
    public void testUpdateDimensionErrorNotExists() throws Exception {

        DimensionDto dimensionDto = new DimensionDto();
        dimensionDto.setUuid(NOT_EXISTS);
        dimensionDto.setTitle(IndicatorsMocks.mockInternationalString());

        try {
            indicatorsSystemsServiceFacade.updateDimension(getServiceContext(), dimensionDto);
            fail("Dimension not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DIMENSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Override
    @Test
    public void testUpdateDimensionLocation() throws Exception {
        // In other test testUpdateDimensionLocation*
    }

    @Test
    public void testUpdateDimensionLocationActualWithoutParentTargetWithParent() throws Exception {

        String uuid = DIMENSION_1_INDICATORS_SYSTEM_1_V2;
        String parentTargetUuid = DIMENSION_2_INDICATORS_SYSTEM_1_V2;

        DimensionDto dimensionDto = indicatorsSystemsServiceFacade.retrieveDimension(getServiceContext(), uuid);
        assertNull(dimensionDto.getParentUuid());

        // Update location
        indicatorsSystemsServiceFacade.updateDimensionLocation(getServiceContext(), uuid, parentTargetUuid, Long.valueOf(1));

        // Validate source
        List<DimensionDto> dimensionsDto = indicatorsSystemsServiceFacade.findDimensions(getServiceContext(), INDICATORS_SYSTEM_1, "2.000");
        assertEquals(1, dimensionsDto.size());
        assertEquals(DIMENSION_2_INDICATORS_SYSTEM_1_V2, dimensionsDto.get(0).getUuid());
        assertEquals(Long.valueOf(3), dimensionsDto.get(0).getOrderInLevel());

        // Validate target
        DimensionDto dimensionParentDto = indicatorsSystemsServiceFacade.retrieveDimension(getServiceContext(), parentTargetUuid);
        assertEquals(1, dimensionParentDto.getSubdimensions().size());
        assertEquals(uuid, dimensionParentDto.getSubdimensions().get(0).getUuid());
        assertEquals(Long.valueOf(1), dimensionParentDto.getSubdimensions().get(0).getOrderInLevel());

        // Validate dimension
        DimensionDto dimensionDtoChanged = indicatorsSystemsServiceFacade.retrieveDimension(getServiceContext(), uuid);
        assertEquals(parentTargetUuid, dimensionDtoChanged.getParentUuid());
        assertEquals(2, dimensionDtoChanged.getSubdimensions().size());
    }

    @Test
    public void testUpdateDimensionLocationActualWithParentTargetWithoutParent() throws Exception {

        String uuid = DIMENSION_1A_INDICATORS_SYSTEM_1_V2;
        String parentTargetUuid = null;
        String parentBeforeUuid = DIMENSION_1_INDICATORS_SYSTEM_1_V2;

        // Retrieve actual
        DimensionDto dimensionDto = indicatorsSystemsServiceFacade.retrieveDimension(getServiceContext(), uuid);
        assertEquals(parentBeforeUuid, dimensionDto.getParentUuid());
        DimensionDto dimensionLastParentDto = indicatorsSystemsServiceFacade.retrieveDimension(getServiceContext(), parentBeforeUuid);
        assertEquals(2, dimensionLastParentDto.getSubdimensions().size());

        // Update location
        indicatorsSystemsServiceFacade.updateDimensionLocation(getServiceContext(), uuid, parentTargetUuid, Long.valueOf(2));

        // Validate parent is changed in dimension
        DimensionDto dimensionDtoChanged = indicatorsSystemsServiceFacade.retrieveDimension(getServiceContext(), uuid);
        assertNull(dimensionDtoChanged.getParentUuid());

        // Validate structure of source
        DimensionDto dimensionLastParentDtoAfterUpdate = indicatorsSystemsServiceFacade.retrieveDimension(getServiceContext(), parentBeforeUuid);
        assertEquals(1, dimensionLastParentDtoAfterUpdate.getSubdimensions().size());
        assertEquals(DIMENSION_1B_INDICATORS_SYSTEM_1_V2, dimensionLastParentDtoAfterUpdate.getSubdimensions().get(0).getUuid());
        assertEquals(Long.valueOf(1), dimensionLastParentDtoAfterUpdate.getSubdimensions().get(0).getOrderInLevel());

        // Validate structure of target
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContext(), INDICATORS_SYSTEM_1, "2.000");
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

    @Test
    public void testUpdateDimensionLocationChangingDimensionParent() throws Exception {

        String uuid = DIMENSION_1A_INDICATORS_SYSTEM_1_V2;
        String parentBeforeUuid = DIMENSION_1_INDICATORS_SYSTEM_1_V2;
        String parentTargetUuid = DIMENSION_1B_INDICATORS_SYSTEM_1_V2;

        // Retrieve actual
        DimensionDto dimensionDto = indicatorsSystemsServiceFacade.retrieveDimension(getServiceContext(), uuid);
        assertEquals(parentBeforeUuid, dimensionDto.getParentUuid());
        DimensionDto dimensionLastParentDto = indicatorsSystemsServiceFacade.retrieveDimension(getServiceContext(), parentBeforeUuid);
        assertEquals(2, dimensionLastParentDto.getSubdimensions().size());

        // Update location
        indicatorsSystemsServiceFacade.updateDimensionLocation(getServiceContext(), uuid, parentTargetUuid, Long.valueOf(1));

        // Validate source
        DimensionDto dimensionLastParentDtoAfterUpdate = indicatorsSystemsServiceFacade.retrieveDimension(getServiceContext(), parentBeforeUuid);
        assertEquals(1, dimensionLastParentDtoAfterUpdate.getSubdimensions().size());
        assertEquals(DIMENSION_1B_INDICATORS_SYSTEM_1_V2, dimensionLastParentDtoAfterUpdate.getSubdimensions().get(0).getUuid());
        assertEquals(Long.valueOf(1), dimensionLastParentDtoAfterUpdate.getSubdimensions().get(0).getOrderInLevel());

        // Validate dimension
        DimensionDto dimensionDtoChanged = indicatorsSystemsServiceFacade.retrieveDimension(getServiceContext(), uuid);
        assertEquals(parentTargetUuid, dimensionDtoChanged.getParentUuid());

        // Validate target
        DimensionDto dimensionParentDtoTarget = indicatorsSystemsServiceFacade.retrieveDimension(getServiceContext(), parentTargetUuid);
        assertEquals(2, dimensionParentDtoTarget.getSubdimensions().size());
        assertEquals(uuid, dimensionParentDtoTarget.getSubdimensions().get(0).getUuid());
        assertEquals(Long.valueOf(1), dimensionParentDtoTarget.getSubdimensions().get(0).getOrderInLevel());
        assertEquals(DIMENSION_1BA_INDICATORS_SYSTEM_1_V2, dimensionParentDtoTarget.getSubdimensions().get(1).getUuid());
        assertEquals(Long.valueOf(2), dimensionParentDtoTarget.getSubdimensions().get(1).getOrderInLevel());
    }

    @Test
    public void testUpdateDimensionLocationActualSameParentOnlyChangeOrderWithoutParent() throws Exception {

        String uuid = DIMENSION_1_INDICATORS_SYSTEM_1_V2;

        // Retrieve actual
        DimensionDto dimensionDto = indicatorsSystemsServiceFacade.retrieveDimension(getServiceContext(), uuid);
        assertNull(dimensionDto.getParentUuid());
        assertEquals(Long.valueOf(2), dimensionDto.getOrderInLevel());

        // Update location
        indicatorsSystemsServiceFacade.updateDimensionLocation(getServiceContext(), uuid, dimensionDto.getParentUuid(), Long.valueOf(4));

        // Validate source and target
        List<DimensionDto> dimensionsDto = indicatorsSystemsServiceFacade.findDimensions(getServiceContext(), INDICATORS_SYSTEM_1, "2.000");
        assertEquals(2, dimensionsDto.size());
        assertEquals(DIMENSION_2_INDICATORS_SYSTEM_1_V2, dimensionsDto.get(0).getUuid());
        assertEquals(Long.valueOf(3), dimensionsDto.get(0).getOrderInLevel());
        assertEquals(uuid, dimensionsDto.get(1).getUuid());
        assertEquals(Long.valueOf(4), dimensionsDto.get(1).getOrderInLevel());

        // Validate dimension
        DimensionDto dimensionDtoChanged = indicatorsSystemsServiceFacade.retrieveDimension(getServiceContext(), uuid);
        assertNull(dimensionDtoChanged.getParentUuid());
    }

    @Test
    public void testUpdateDimensionLocationActualSameParentOnlyChangeOrderWithParent() throws Exception {

        String uuid = DIMENSION_1B_INDICATORS_SYSTEM_1_V2;

        // Retrieve actual
        DimensionDto dimensionDto = indicatorsSystemsServiceFacade.retrieveDimension(getServiceContext(), uuid);
        assertEquals(DIMENSION_1_INDICATORS_SYSTEM_1_V2, dimensionDto.getParentUuid());
        assertEquals(Long.valueOf(2), dimensionDto.getOrderInLevel());

        // Update location
        indicatorsSystemsServiceFacade.updateDimensionLocation(getServiceContext(), uuid, dimensionDto.getParentUuid(), Long.valueOf(1));

        // Validate source and target
        DimensionDto dimensionDtoParentAfterUpdate = indicatorsSystemsServiceFacade.retrieveDimension(getServiceContext(), DIMENSION_1_INDICATORS_SYSTEM_1_V2);
        assertEquals(2, dimensionDtoParentAfterUpdate.getSubdimensions().size());
        assertEquals(DIMENSION_1B_INDICATORS_SYSTEM_1_V2, dimensionDtoParentAfterUpdate.getSubdimensions().get(0).getUuid());
        assertEquals(Long.valueOf(1), dimensionDtoParentAfterUpdate.getSubdimensions().get(0).getOrderInLevel());
        assertEquals(DIMENSION_1A_INDICATORS_SYSTEM_1_V2, dimensionDtoParentAfterUpdate.getSubdimensions().get(1).getUuid());
        assertEquals(Long.valueOf(2), dimensionDtoParentAfterUpdate.getSubdimensions().get(1).getOrderInLevel());

        // Validate dimension
        DimensionDto dimensionDtoChanged = indicatorsSystemsServiceFacade.retrieveDimension(getServiceContext(), uuid);
        assertEquals(DIMENSION_1_INDICATORS_SYSTEM_1_V2, dimensionDtoChanged.getParentUuid());
    }

    @Test
    public void testUpdateDimensionLocationErrorParentIsChild() throws Exception {

        String uuid = DIMENSION_1_INDICATORS_SYSTEM_1_V2;
        String parentTargetUuid = DIMENSION_1BA_INDICATORS_SYSTEM_1_V2;

        try {
            indicatorsSystemsServiceFacade.updateDimensionLocation(getServiceContext(), uuid, parentTargetUuid, Long.valueOf(1));
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
            indicatorsSystemsServiceFacade.updateDimensionLocation(getServiceContext(), uuid, null, Long.valueOf(1));
            fail("Dimension not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DIMENSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateDimensionErrorOrderIncorrect() throws Exception {

        try {
            indicatorsSystemsServiceFacade.updateDimensionLocation(getServiceContext(), DIMENSION_1_INDICATORS_SYSTEM_1_V2, null, Long.MAX_VALUE);
            fail("order incorrect");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("ORDER_IN_LEVEL", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Override
    @Test
    public void testRetrieveIndicatorInstance() throws Exception {

        IndicatorInstanceDto indicatorInstanceDto = indicatorsSystemsServiceFacade.retrieveIndicatorInstance(getServiceContext(), INDICATOR_INSTANCE_3_INDICATORS_SYSTEM_1_V2);

        assertNotNull(indicatorInstanceDto);
        assertEquals(INDICATOR_INSTANCE_3_INDICATORS_SYSTEM_1_V2, indicatorInstanceDto.getUuid());
        assertEquals("IndicatorA", indicatorInstanceDto.getIndicatorUuid());
        IndicatorsAsserts.assertEqualsInternationalString(indicatorInstanceDto.getTitle(), "es", "Título IndSys-1-v2-IInstance-3", "en", "Title IndSys-1-v2-IInstance-3");
        assertEquals(DIMENSION_1B_INDICATORS_SYSTEM_1_V2, indicatorInstanceDto.getParentUuid());
        assertEquals("Annual", indicatorInstanceDto.getTemporaryGranularityId());
        assertNull(indicatorInstanceDto.getTemporaryValue());
        assertEquals("Countries", indicatorInstanceDto.getGeographicGranularityId());
        assertNull(indicatorInstanceDto.getGeographicValue());
        IndicatorsAsserts.assertEqualsDate("2011-05-05 01:02:04", indicatorInstanceDto.getCreatedDate());
        assertEquals("user1", indicatorInstanceDto.getCreatedBy());
        IndicatorsAsserts.assertEqualsDate("2011-06-06 05:06:07", indicatorInstanceDto.getLastUpdated());
        assertEquals("user2", indicatorInstanceDto.getLastUpdatedBy());
    }

    @Test
    public void testRetrieveIndicatorInstanceErrorParameterRequired() throws Exception {

        String uuid = null;

        try {
            indicatorsSystemsServiceFacade.retrieveIndicatorInstance(getServiceContext(), uuid);
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
            indicatorsSystemsServiceFacade.retrieveIndicatorInstance(getServiceContext(), uuid);
            fail("No exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_INSTANCE_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Override
    @Test
    public void testCreateIndicatorInstance() throws Exception {

        // Create indicator instance
        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setIndicatorUuid("Indicator1");
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorInstanceDto.setParentUuid(null);
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(5));
        indicatorInstanceDto.setGeographicValue("Spain");
        indicatorInstanceDto.setTemporaryValue("2012");

        String uuidIndicatorsSystem = INDICATORS_SYSTEM_1;
        IndicatorInstanceDto indicatorInstanceDtoCreated = indicatorsSystemsServiceFacade.createIndicatorInstance(getServiceContext(), uuidIndicatorsSystem, indicatorInstanceDto);
        assertNotNull(indicatorInstanceDtoCreated.getUuid());

        // Retrieve indicator instance
        IndicatorInstanceDto indicatorInstanceDtoRetrieved = indicatorsSystemsServiceFacade.retrieveIndicatorInstance(getServiceContext(), indicatorInstanceDtoCreated.getUuid());
        IndicatorsAsserts.assertEqualsIndicatorInstance(indicatorInstanceDto, indicatorInstanceDtoRetrieved);

        // Validate new structure
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContext(), INDICATORS_SYSTEM_1, "2.000");
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
        indicatorInstanceDto.setIndicatorUuid("Indicator1");
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorInstanceDto.setParentUuid(null);
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(3));
        indicatorInstanceDto.setGeographicGranularityId("countries");
        indicatorInstanceDto.setTemporaryGranularityId("days");

        String uuidIndicatorsSystem = INDICATORS_SYSTEM_1;
        IndicatorInstanceDto indicatorInstanceDtoCreated = indicatorsSystemsServiceFacade.createIndicatorInstance(getServiceContext(), uuidIndicatorsSystem, indicatorInstanceDto);
        assertNotNull(indicatorInstanceDtoCreated.getUuid());

        // Retrieve indicator instance
        IndicatorInstanceDto indicatorInstanceDtoRetrieved = indicatorsSystemsServiceFacade.retrieveIndicatorInstance(getServiceContext(), indicatorInstanceDtoCreated.getUuid());
        IndicatorsAsserts.assertEqualsIndicatorInstance(indicatorInstanceDto, indicatorInstanceDtoRetrieved);

        // Validate new structure
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContext(), INDICATORS_SYSTEM_1, "2.000");
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
        indicatorInstanceDto.setIndicatorUuid("Indicator1");
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorInstanceDto.setParentUuid(parentUuid);
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(3));
        indicatorInstanceDto.setGeographicValue("Spain");
        indicatorInstanceDto.setTemporaryValue("2012");

        String uuidIndicatorsSystem = INDICATORS_SYSTEM_1;
        IndicatorInstanceDto indicatorInstanceDtoCreated = indicatorsSystemsServiceFacade.createIndicatorInstance(getServiceContext(), uuidIndicatorsSystem, indicatorInstanceDto);
        assertNotNull(indicatorInstanceDtoCreated.getUuid());

        // Retrieve indicatorInstance
        IndicatorInstanceDto indicatorInstanceDtoRetrieved = indicatorsSystemsServiceFacade.retrieveIndicatorInstance(getServiceContext(), indicatorInstanceDtoCreated.getUuid());
        IndicatorsAsserts.assertEqualsIndicatorInstance(indicatorInstanceDto, indicatorInstanceDtoRetrieved);
        assertEquals(parentUuid, indicatorInstanceDtoRetrieved.getParentUuid());

        // Validate new structure
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContext(), INDICATORS_SYSTEM_1, "2.000");
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
        indicatorInstanceDto.setIndicatorUuid("Indicator1");
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorInstanceDto.setParentUuid(parentUuid);
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(2));
        indicatorInstanceDto.setTemporaryValue("2012");

        String uuidIndicatorsSystem = INDICATORS_SYSTEM_1;
        IndicatorInstanceDto indicatorInstanceDtoCreated = indicatorsSystemsServiceFacade.createIndicatorInstance(getServiceContext(), uuidIndicatorsSystem, indicatorInstanceDto);
        assertNotNull(indicatorInstanceDtoCreated.getUuid());

        // Retrieve indicatorInstance
        IndicatorInstanceDto indicatorInstanceDtoRetrieved = indicatorsSystemsServiceFacade.retrieveIndicatorInstance(getServiceContext(), indicatorInstanceDtoCreated.getUuid());
        IndicatorsAsserts.assertEqualsIndicatorInstance(indicatorInstanceDto, indicatorInstanceDtoRetrieved);
        assertEquals(parentUuid, indicatorInstanceDtoRetrieved.getParentUuid());

        // Validate new structure
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContext(), INDICATORS_SYSTEM_1, "2.000");
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
        // TODO testear
    }

    @Test
    public void testCreateIndicatorInstanceErrorParametersRequired() throws Exception {

        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setTitle(null);
        indicatorInstanceDto.setIndicatorUuid(null);
        indicatorInstanceDto.setParentUuid(null);
        indicatorInstanceDto.setOrderInLevel(null);
        indicatorInstanceDto.setTemporaryGranularityId(null);
        try {
            indicatorsSystemsServiceFacade.createIndicatorInstance(getServiceContext(), INDICATORS_SYSTEM_1, indicatorInstanceDto);
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
            assertEquals(1, e.getExceptionItems().get(2).getMessageParameters().length);
            assertEquals("INDICATOR_INSTANCE.TEMPORARY", e.getExceptionItems().get(2).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(3).getCode());
            assertEquals(1, e.getExceptionItems().get(3).getMessageParameters().length);
            assertEquals("INDICATOR_INSTANCE.ORDER_IN_LEVEL", e.getExceptionItems().get(3).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorInstanceErrorOrderIncorrect() throws Exception {

        // Create indicatorInstance
        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setIndicatorUuid("Indicator1");
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorInstanceDto.setGeographicValue("Spain");
        indicatorInstanceDto.setTemporaryValue("2012");
        indicatorInstanceDto.setParentUuid(null);
        indicatorInstanceDto.setOrderInLevel(Long.MAX_VALUE);

        try {
            indicatorsSystemsServiceFacade.createIndicatorInstance(getServiceContext(), INDICATORS_SYSTEM_1, indicatorInstanceDto);
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
        indicatorInstanceDto.setIndicatorUuid("Indicator1");
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorInstanceDto.setGeographicValue("Spain");
        indicatorInstanceDto.setTemporaryValue("2012");
        indicatorInstanceDto.setParentUuid(null);
        indicatorInstanceDto.setOrderInLevel(Long.MIN_VALUE);

        try {
            indicatorsSystemsServiceFacade.createIndicatorInstance(getServiceContext(), INDICATORS_SYSTEM_1, indicatorInstanceDto);
            fail("order incorrect");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("INDICATOR_INSTANCE.ORDER_IN_LEVEL", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorInstanceErrorIndicatorDuplicatedInSameLevel() throws Exception {

        // Create indicator instance
        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setIndicatorUuid("IndicatorA");
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorInstanceDto.setParentUuid(null);
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(3));
        indicatorInstanceDto.setGeographicValue("Spain");
        indicatorInstanceDto.setTemporaryValue("2012");

        // Root level
        try {
            indicatorsSystemsServiceFacade.createIndicatorInstance(getServiceContext(), INDICATORS_SYSTEM_1, indicatorInstanceDto);
            fail("Indicator duplicated");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_INSTANCE_ALREADY_EXIST_INDICATOR_SAME_LEVEL.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(indicatorInstanceDto.getIndicatorUuid(), e.getExceptionItems().get(0).getMessageParameters()[0]);
        }

        // In dimension level
        indicatorInstanceDto.setIndicatorUuid("IndicatorA");
        indicatorInstanceDto.setParentUuid(DIMENSION_1B_INDICATORS_SYSTEM_1_V2);
        try {
            indicatorsSystemsServiceFacade.createIndicatorInstance(getServiceContext(), INDICATORS_SYSTEM_1, indicatorInstanceDto);
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
        indicatorInstanceDto.setIndicatorUuid("Indicator1");
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorInstanceDto.setGeographicValue("Spain");
        indicatorInstanceDto.setTemporaryValue("2012");
        indicatorInstanceDto.setParentUuid(null);
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(1));
        try {
            indicatorsSystemsServiceFacade.createIndicatorInstance(getServiceContext(), NOT_EXISTS, indicatorInstanceDto);
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
        indicatorInstanceDto.setIndicatorUuid("Indicator1");
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorInstanceDto.setGeographicValue("Spain");
        indicatorInstanceDto.setTemporaryValue("2012");
        indicatorInstanceDto.setParentUuid(null);
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(1));

        try {
            indicatorsSystemsServiceFacade.createIndicatorInstance(getServiceContext(), indicatorsSystemUuid, indicatorInstanceDto);
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
        indicatorInstanceDto.setIndicatorUuid("Indicator1");
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorInstanceDto.setGeographicValue("Spain");
        indicatorInstanceDto.setTemporaryValue("2012");
        indicatorInstanceDto.setParentUuid(DIMENSION_NOT_EXISTS);
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(1));

        try {
            indicatorsSystemsServiceFacade.createIndicatorInstance(getServiceContext(), INDICATORS_SYSTEM_1, indicatorInstanceDto);
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
        indicatorInstanceDto.setIndicatorUuid("Indicator1");
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorInstanceDto.setGeographicValue("Spain");
        indicatorInstanceDto.setTemporaryValue("2012");
        indicatorInstanceDto.setParentUuid(DIMENSION_1_INDICATORS_SYSTEM_1_V2);
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(1));

        try {
            indicatorsSystemsServiceFacade.createIndicatorInstance(getServiceContext(), INDICATORS_SYSTEM_2, indicatorInstanceDto);
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
        IndicatorInstanceDto indicatorInstanceDto = indicatorsSystemsServiceFacade.retrieveIndicatorInstance(getServiceContext(), uuid);
        assertEquals(Long.valueOf(1), indicatorInstanceDto.getOrderInLevel());

        // Delete indicatorInstance
        indicatorsSystemsServiceFacade.deleteIndicatorInstance(getServiceContext(), uuid);

        // Validation
        try {
            indicatorsSystemsServiceFacade.retrieveIndicatorInstance(getServiceContext(), uuid);
            fail("Indicator instance deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_INSTANCE_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }

        // Checks orders of other elements are updated
        IndicatorsSystemStructureDto indicatorsSystemStructureDto = indicatorsSystemsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContext(), INDICATORS_SYSTEM_1, "2.000");
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
            indicatorsSystemsServiceFacade.deleteIndicatorInstance(getServiceContext(), INDICATOR_INSTANCE_2_INDICATORS_SYSTEM_3_V1);
            fail("Indicators system not in production");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_STATE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(INDICATORS_SYSTEM_3, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorsSystemStateEnum.DRAFT, ((IndicatorsSystemStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(IndicatorsSystemStateEnum.VALIDATION_REJECTED, ((IndicatorsSystemStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
            assertEquals(IndicatorsSystemStateEnum.PRODUCTION_VALIDATION, ((IndicatorsSystemStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[2]);
            assertEquals(IndicatorsSystemStateEnum.DIFFUSION_VALIDATION, ((IndicatorsSystemStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[3]);
        }
    }

    @Test
    public void testDeleteIndicatorInstanceErrorNotExists() throws Exception {

        String uuid = NOT_EXISTS;

        // Validation
        try {
            indicatorsSystemsServiceFacade.deleteIndicatorInstance(getServiceContext(), uuid);
            fail("IndicatorInstance not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_INSTANCE_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Override
    @Test
    public void testFindIndicatorsInstances() throws Exception {

        String uuidIndicatorsSystem = INDICATORS_SYSTEM_1;

        // Version 1.000
        {
            List<IndicatorInstanceDto> indicatorsInstancesDto = indicatorsSystemsServiceFacade.findIndicatorsInstances(getServiceContext(), uuidIndicatorsSystem, "1.000");
            assertEquals(0, indicatorsInstancesDto.size());
        }

        // Version 2.000
        {
            List<IndicatorInstanceDto> indicatorInstancesDto = indicatorsSystemsServiceFacade.findIndicatorsInstances(getServiceContext(), uuidIndicatorsSystem, "2.000");
            assertEquals(3, indicatorInstancesDto.size());

            assertEquals(INDICATOR_INSTANCE_3_INDICATORS_SYSTEM_1_V2, indicatorInstancesDto.get(0).getUuid());
            assertEquals(INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2, indicatorInstancesDto.get(1).getUuid()); // orderBy puts null at last place
            assertEquals(INDICATOR_INSTANCE_2_INDICATORS_SYSTEM_1_V2, indicatorInstancesDto.get(2).getUuid());
        }
    }

    @Test
    public void testFindIndicatorsInstancesErrorNotExists() throws Exception {

        String uuid = NOT_EXISTS;

        // Validation
        try {
            indicatorsSystemsServiceFacade.findIndicatorsInstances(getServiceContext(), uuid, "1.000");
            fail("Indicators system not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_VERSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals("1.000", e.getExceptionItems().get(0).getMessageParameters()[1]);
        }
    }

    @Override
    @Test
    public void testFindIndicatorsInstancesByDimension() throws Exception {

        {
            List<IndicatorInstanceDto> indicatorsInstancesDto = indicatorsSystemsServiceFacade.findIndicatorsInstancesByDimension(getServiceContext(), DIMENSION_1B_INDICATORS_SYSTEM_1_V2);
            assertEquals(1, indicatorsInstancesDto.size());
            assertEquals(INDICATOR_INSTANCE_3_INDICATORS_SYSTEM_1_V2, indicatorsInstancesDto.get(0).getUuid());
        }
        {
            List<IndicatorInstanceDto> indicatorInstancesDto = indicatorsSystemsServiceFacade.findIndicatorsInstancesByDimension(getServiceContext(), DIMENSION_1_INDICATORS_SYSTEM_1_V1);
            assertEquals(0, indicatorInstancesDto.size());
        }
    }

    @Test
    public void testFindIndicatorsInstancesByDimensionErrorNotExists() throws Exception {

        String uuid = NOT_EXISTS;

        // Validation
        try {
            indicatorsSystemsServiceFacade.findIndicatorsInstancesByDimension(getServiceContext(), uuid);
            fail("dimension not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DIMENSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Override
    @Test
    public void testUpdateIndicatorInstance() throws Exception {

        String uuid = INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2;
        IndicatorInstanceDto indicatorInstanceDto = indicatorsSystemsServiceFacade.retrieveIndicatorInstance(getServiceContext(), uuid);
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalString());

        // Update
        indicatorsSystemsServiceFacade.updateIndicatorInstance(getServiceContext(), indicatorInstanceDto);

        // Validation
        IndicatorInstanceDto indicatorInstanceDtoUpdated = indicatorsSystemsServiceFacade.retrieveIndicatorInstance(getServiceContext(), uuid);
        IndicatorsAsserts.assertEqualsIndicatorInstance(indicatorInstanceDto, indicatorInstanceDtoUpdated);
    }

    @Test
    public void testUpdateIndicatorInstanceErrorChangeParentAndOrderAndIndicator() throws Exception {

        IndicatorInstanceDto indicatorInstanceDto = indicatorsSystemsServiceFacade.retrieveIndicatorInstance(getServiceContext(), INDICATOR_INSTANCE_3_INDICATORS_SYSTEM_1_V2);
        assertEquals(DIMENSION_1B_INDICATORS_SYSTEM_1_V2, indicatorInstanceDto.getParentUuid());
        indicatorInstanceDto.setParentUuid(DIMENSION_1A_INDICATORS_SYSTEM_1_V2);
        assertEquals(Long.valueOf(2), indicatorInstanceDto.getOrderInLevel());
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(3));
        assertEquals("IndicatorA", indicatorInstanceDto.getIndicatorUuid());
        indicatorInstanceDto.setIndicatorUuid("newIndicator");

        try {
            indicatorsSystemsServiceFacade.updateIndicatorInstance(getServiceContext(), indicatorInstanceDto);
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
        indicatorInstanceDto.setIndicatorUuid("IndicatorA");

        try {
            indicatorsSystemsServiceFacade.updateIndicatorInstance(getServiceContext(), indicatorInstanceDto);
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

        IndicatorInstanceDto indicatorInstanceDto = indicatorsSystemsServiceFacade.retrieveIndicatorInstance(getServiceContext(), INDICATOR_INSTANCE_2_INDICATORS_SYSTEM_3_V1);

        try {
            indicatorsSystemsServiceFacade.updateIndicatorInstance(getServiceContext(), indicatorInstanceDto);
            fail("Indicators system published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_STATE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(INDICATORS_SYSTEM_3, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorsSystemStateEnum.DRAFT, ((IndicatorsSystemStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(IndicatorsSystemStateEnum.VALIDATION_REJECTED, ((IndicatorsSystemStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
            assertEquals(IndicatorsSystemStateEnum.PRODUCTION_VALIDATION, ((IndicatorsSystemStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[2]);
            assertEquals(IndicatorsSystemStateEnum.DIFFUSION_VALIDATION, ((IndicatorsSystemStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[3]);
        }
    }

    @Test
    public void testUpdateIndicatorInstanceErrorNotExists() throws Exception {

        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setUuid(NOT_EXISTS);
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalString());

        try {
            indicatorsSystemsServiceFacade.updateIndicatorInstance(getServiceContext(), indicatorInstanceDto);
            fail("Indicator instance not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_INSTANCE_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/IndicatorsSystemsServiceFacadeTest.xml";
    }
}
