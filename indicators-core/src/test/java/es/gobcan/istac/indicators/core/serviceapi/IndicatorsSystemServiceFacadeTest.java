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
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorsSystemStateEnum;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorsSystemVersionEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.serviceapi.utils.IndicatorsAsserts;
import es.gobcan.istac.indicators.core.serviceapi.utils.IndicatorsMocks;

/**
 * Spring based transactional test with DbUnit support.
 * TODO extender de MetamacBaseTests
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext-test.xml"})
public class IndicatorsSystemServiceFacadeTest extends IndicatorsBaseTests implements IndicatorsSystemServiceFacadeTestBase {

    @Autowired
    protected IndicatorsSystemServiceFacade indicatorsSystemServiceFacade;

    private static String                   INDICATORS_SYSTEM_1          = "IndSys-1";
    private static String                   INDICATORS_SYSTEM_2          = "IndSys-2";
    private static String                   INDICATORS_SYSTEM_3          = "IndSys-3";
    private static String                   INDICATORS_SYSTEM_3_VERSION  = "11.033";
    private static String                   INDICATORS_SYSTEM_4          = "IndSys-4";
    private static String                   INDICATORS_SYSTEM_5          = "IndSys-5";
    private static String                   INDICATORS_SYSTEM_6          = "IndSys-6";
    private static String                   INDICATORS_SYSTEM_7          = "IndSys-7";
    private static String                   INDICATORS_SYSTEM_8          = "IndSys-8";
    private static String                   INDICATORS_SYSTEM_NOT_EXISTS = "IndSys-not-exists";

    @Test
    public void testCreateIndicatorsSystem() throws Exception {

        IndicatorsSystemDto indicatorsSystemDto = new IndicatorsSystemDto();
        indicatorsSystemDto.setCode(IndicatorsMocks.mockString(10));
        indicatorsSystemDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorsSystemDto.setAcronym(IndicatorsMocks.mockInternationalString());
        indicatorsSystemDto.setUri(IndicatorsMocks.mockString(100));
        indicatorsSystemDto.setObjetive(IndicatorsMocks.mockInternationalString());
        indicatorsSystemDto.setDescription(IndicatorsMocks.mockInternationalString());

        // Create
        IndicatorsSystemDto indicatorsSystemDtoCreated = indicatorsSystemServiceFacade.createIndicatorsSystem(getServiceContext(), indicatorsSystemDto);

        // Validate
        assertNotNull(indicatorsSystemDtoCreated);
        assertNotNull(indicatorsSystemDtoCreated.getUuid());
        assertNotNull(indicatorsSystemDtoCreated.getVersionNumber());
        IndicatorsSystemDto indicatorsSystemDtoRetrieved = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), indicatorsSystemDtoCreated.getUuid(),
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
    public void testCreateIndicatorsSystemCodeRequired() throws Exception {

        IndicatorsSystemDto indicatorsSystemDto = new IndicatorsSystemDto();
        indicatorsSystemDto.setCode(null);
        indicatorsSystemDto.setTitle(IndicatorsMocks.mockInternationalString());

        try {
            indicatorsSystemServiceFacade.createIndicatorsSystem(getServiceContext(), indicatorsSystemDto);
            fail("code required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_VALIDATION_METADATA_REQUIRED.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("INDICATORS_SYSTEM.CODE", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorsSystemTitleRequired() throws Exception {

        IndicatorsSystemDto indicatorsSystemDto = new IndicatorsSystemDto();
        indicatorsSystemDto.setCode(IndicatorsMocks.mockString(10));
        indicatorsSystemDto.setTitle(null);

        try {
            indicatorsSystemServiceFacade.createIndicatorsSystem(getServiceContext(), indicatorsSystemDto);
            fail("title required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_VALIDATION_METADATA_REQUIRED.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("INDICATORS_SYSTEM.TITLE", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorsSystemCodeDuplicated() throws Exception {

        IndicatorsSystemDto indicatorsSystemDto = new IndicatorsSystemDto();
        indicatorsSystemDto.setCode("CoDe-1");
        indicatorsSystemDto.setTitle(IndicatorsMocks.mockInternationalString());

        try {
            indicatorsSystemServiceFacade.createIndicatorsSystem(getServiceContext(), indicatorsSystemDto);
            fail("code duplicated");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_ALREADY_EXIST_CODE_DUPLICATED.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(indicatorsSystemDto.getCode(), e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorsSystemUriDuplicated() throws Exception {

        IndicatorsSystemDto indicatorsSystemDto = new IndicatorsSystemDto();
        indicatorsSystemDto.setCode(IndicatorsMocks.mockString(10));
        indicatorsSystemDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorsSystemDto.setUri("http://indicators-sytems/1");

        try {
            indicatorsSystemServiceFacade.createIndicatorsSystem(getServiceContext(), indicatorsSystemDto);
            fail("uri duplicated");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_ALREADY_EXIST_URI_DUPLICATED.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(indicatorsSystemDto.getUri(), e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorsSystemCodeDuplicatedInsensitive() throws Exception {

        IndicatorsSystemDto indicatorsSystemDto = new IndicatorsSystemDto();
        indicatorsSystemDto.setCode("CoDe-1");
        indicatorsSystemDto.setTitle(IndicatorsMocks.mockInternationalString());

        try {
            indicatorsSystemServiceFacade.createIndicatorsSystem(getServiceContext(), indicatorsSystemDto);
            fail("code duplicated");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_ALREADY_EXIST_CODE_DUPLICATED.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(indicatorsSystemDto.getCode(), e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRetrieveIndicatorsSystem() throws Exception {

        String uuid = INDICATORS_SYSTEM_1;
        String versionNumber = "1.000";
        IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);

        assertNotNull(indicatorsSystemDto);
        assertEquals(uuid, indicatorsSystemDto.getUuid());
        assertEquals(versionNumber, indicatorsSystemDto.getVersionNumber());
        assertEquals("1.000", indicatorsSystemDto.getDiffusionVersion());
        assertEquals("2.000", indicatorsSystemDto.getProductionVersion());
        assertEquals("CODE-1", indicatorsSystemDto.getCode());
        assertEquals("http://indicators-sytems/1", indicatorsSystemDto.getUri());
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
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, null);
            assertEquals(uuid, indicatorsSystemDto.getUuid());
            assertEquals(versionNumberProduction, indicatorsSystemDto.getVersionNumber());
            assertEquals(versionNumberDiffusion, indicatorsSystemDto.getDiffusionVersion());
            assertEquals(versionNumberProduction, indicatorsSystemDto.getProductionVersion());
        }

        // With version 1
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumberDiffusion);
            assertEquals(uuid, indicatorsSystemDto.getUuid());
            assertEquals(versionNumberDiffusion, indicatorsSystemDto.getVersionNumber());
            assertEquals(versionNumberDiffusion, indicatorsSystemDto.getDiffusionVersion());
            assertEquals(versionNumberProduction, indicatorsSystemDto.getProductionVersion());
        }

        // With version 2
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumberProduction);
            assertEquals(uuid, indicatorsSystemDto.getUuid());
            assertEquals(versionNumberProduction, indicatorsSystemDto.getVersionNumber());
            assertEquals(versionNumberDiffusion, indicatorsSystemDto.getDiffusionVersion());
            assertEquals(versionNumberProduction, indicatorsSystemDto.getProductionVersion());
        }
    }

    @Test
    public void testRetrieveIndicatorsSystemPublished() throws Exception {

        String uuid = INDICATORS_SYSTEM_3;

        IndicatorsSystemDto indicatorsSystemDtoPublished = indicatorsSystemServiceFacade.retrieveIndicatorsSystemPublished(getServiceContext(), uuid);
        assertEquals(uuid, indicatorsSystemDtoPublished.getUuid());
        assertEquals("11.033", indicatorsSystemDtoPublished.getVersionNumber());
        assertEquals(IndicatorsSystemStateEnum.PUBLISHED, indicatorsSystemDtoPublished.getState());
    }

    @Test
    public void testRetrieveIndicatorsSystemPublishedWhenSystemHasVersionProduction() throws Exception {

        String uuid = INDICATORS_SYSTEM_1;

        IndicatorsSystemDto indicatorsSystemDtoPublished = indicatorsSystemServiceFacade.retrieveIndicatorsSystemPublished(getServiceContext(), uuid);
        assertEquals(uuid, indicatorsSystemDtoPublished.getUuid());
        assertEquals("1.000", indicatorsSystemDtoPublished.getVersionNumber());
        assertEquals(IndicatorsSystemStateEnum.PUBLISHED, indicatorsSystemDtoPublished.getState());
    }

    @Test
    public void testRetrieveIndicatorsSystemPublishedErrorOnlyProduction() throws Exception {

        String uuid = INDICATORS_SYSTEM_2;

        try {
            indicatorsSystemServiceFacade.retrieveIndicatorsSystemPublished(getServiceContext(), uuid);
            fail("No published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_WRONG_STATE.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorsSystemStateEnum.PUBLISHED, e.getExceptionItems().get(0).getMessageParameters()[1]);
        }
    }

    @Test
    public void testRetrieveIndicatorsSystemErrorNotExists() throws Exception {

        String uuid = INDICATORS_SYSTEM_NOT_EXISTS;

        try {
            indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, null);
            fail("No exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_NOT_FOUND.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRetrieveIndicatorsSystemErrorVersionNotExists() throws Exception {

        String uuid = INDICATORS_SYSTEM_2;
        String versionNotExists = String.valueOf(99);

        try {
            indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNotExists);
            fail("No exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_NOT_FOUND_IN_VERSION.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(versionNotExists, e.getExceptionItems().get(0).getMessageParameters()[1]);
        }
    }

    @Test
    public void testRetrieveIndicatorsSystemPublishedErrorNotExists() throws Exception {

        String uuid = INDICATORS_SYSTEM_NOT_EXISTS;

        try {
            indicatorsSystemServiceFacade.retrieveIndicatorsSystemPublished(getServiceContext(), uuid);
            fail("No exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_NOT_FOUND.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testDeleteIndicatorsSystem() throws Exception {

        String uuid = INDICATORS_SYSTEM_2;

        // Delete indicators system only in draft
        indicatorsSystemServiceFacade.deleteIndicatorsSystem(getServiceContext(), uuid);

        // Validation
        try {
            indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, null);
            fail("Indicators system deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_NOT_FOUND.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testDeleteIndicatorsSystemWithPublishedAndDraft() throws Exception {

        String uuid = INDICATORS_SYSTEM_1;

        // Retrieve: version 1 is diffusion; version 2 is in production
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, "1.000");
            assertEquals(uuid, indicatorsSystemDto.getUuid());
            assertEquals("1.000", indicatorsSystemDto.getVersionNumber());
            assertEquals("1.000", indicatorsSystemDto.getDiffusionVersion());
            assertEquals("2.000", indicatorsSystemDto.getProductionVersion());
        }

        // Delete indicators system
        indicatorsSystemServiceFacade.deleteIndicatorsSystem(getServiceContext(), uuid);

        // Validation
        // Version 1 exists
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, "1.000");
            assertEquals(uuid, indicatorsSystemDto.getUuid());
            assertEquals("1.000", indicatorsSystemDto.getVersionNumber());
            assertEquals("1.000", indicatorsSystemDto.getDiffusionVersion());
            assertEquals(null, indicatorsSystemDto.getProductionVersion());
        }
        // Version 2 not exists
        try {
            indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, "2.000");
            fail("Indicators system version deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_NOT_FOUND_IN_VERSION.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals("2.000", e.getExceptionItems().get(0).getMessageParameters()[1]);
        }
    }

    @Test
    public void testDeleteIndicatorsSystemErrorInDiffusion() throws Exception {

        String uuid = INDICATORS_SYSTEM_3;

        // Validation
        try {
            indicatorsSystemServiceFacade.deleteIndicatorsSystem(getServiceContext(), uuid);
            fail("Indicators system is not in draft");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_IN_PRODUCTION_NOT_FOUND.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testDeleteIndicatorsSystemErrorNotExists() throws Exception {

        String uuid = INDICATORS_SYSTEM_NOT_EXISTS;

        // Validation
        try {
            indicatorsSystemServiceFacade.deleteIndicatorsSystem(getServiceContext(), uuid);
            fail("Indicators system not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_NOT_FOUND.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateIndicatorsSystem() throws Exception {

        String uuid = INDICATORS_SYSTEM_1;
        String versionNumber = "2.000";

        IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
        assertEquals(IndicatorsSystemStateEnum.DRAFT, indicatorsSystemDto.getState());

        indicatorsSystemDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorsSystemDto.setAcronym(IndicatorsMocks.mockInternationalString());
        indicatorsSystemDto.setUri("newUri");

        // Update
        indicatorsSystemServiceFacade.updateIndicatorsSystem(getServiceContext(), indicatorsSystemDto);

        // Validation
        IndicatorsSystemDto indicatorsSystemDtoUpdated = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
        IndicatorsAsserts.assertEqualsIndicatorsSystem(indicatorsSystemDto, indicatorsSystemDtoUpdated);
        assertTrue(indicatorsSystemDtoUpdated.getLastUpdated().after(indicatorsSystemDtoUpdated.getCreatedDate()));
    }

    @Test
    public void testUpdateIndicatorsSystemInProductionValidation() throws Exception {

        String uuid = INDICATORS_SYSTEM_4;
        String versionNumber = "1.000";

        IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
        assertEquals(IndicatorsSystemStateEnum.PRODUCTION_VALIDATION, indicatorsSystemDto.getState());

        indicatorsSystemDto.setTitle(IndicatorsMocks.mockInternationalString());

        // Update
        indicatorsSystemServiceFacade.updateIndicatorsSystem(getServiceContext(), indicatorsSystemDto);

        // Validation
        IndicatorsSystemDto indicatorsSystemDtoUpdated = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
        IndicatorsAsserts.assertEqualsIndicatorsSystem(indicatorsSystemDto, indicatorsSystemDtoUpdated);
    }

    @Test
    public void testUpdateIndicatorsSystemReusingLocalisedStrings() throws Exception {

        String uuid = INDICATORS_SYSTEM_1;
        String versionNumber = "2.000";

        IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
        indicatorsSystemDto.getTitle().getTexts().iterator().next().setLabel("NewLabel");

        // Update
        indicatorsSystemServiceFacade.updateIndicatorsSystem(getServiceContext(), indicatorsSystemDto);

        // Validation
        IndicatorsSystemDto indicatorsSystemDtoUpdated = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
        IndicatorsAsserts.assertEqualsIndicatorsSystem(indicatorsSystemDto, indicatorsSystemDtoUpdated);
    }

    @Test
    public void testUpdateIndicatorsSystemErrorNotExists() throws Exception {

        IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), INDICATORS_SYSTEM_1, "2.000");
        indicatorsSystemDto.setUuid(INDICATORS_SYSTEM_NOT_EXISTS);

        try {
            indicatorsSystemServiceFacade.updateIndicatorsSystem(getServiceContext(), indicatorsSystemDto);
            fail("Indicators system not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_NOT_FOUND.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(INDICATORS_SYSTEM_NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateIndicatorsSystemErrorNotInProduction() throws Exception {

        String uuid = INDICATORS_SYSTEM_3;
        String versionNumber = INDICATORS_SYSTEM_3_VERSION;

        IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);

        try {
            indicatorsSystemServiceFacade.updateIndicatorsSystem(getServiceContext(), indicatorsSystemDto);
            fail("Indicators system not in production");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_IN_PRODUCTION_NOT_FOUND.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateIndicatorsSystemErrorWrongVersion() throws Exception {

        String uuid = INDICATORS_SYSTEM_1;
        String versionNumber = "1.000";

        IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);

        try {
            indicatorsSystemServiceFacade.updateIndicatorsSystem(getServiceContext(), indicatorsSystemDto);
            fail("Version 1 not is in production");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_VERSION_WRONG_STATE.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(versionNumber, e.getExceptionItems().get(0).getMessageParameters()[1]);
        }
    }

    @Test
    public void testUpdateIndicatorsErrorCodeNonModifiable() throws Exception {

        String uuid = INDICATORS_SYSTEM_1;
        String versionNumber = "2.000";

        IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
        indicatorsSystemDto.setCode("newCode");

        // Update
        try {
            indicatorsSystemServiceFacade.updateIndicatorsSystem(getServiceContext(), indicatorsSystemDto);
            fail("Code is unmodifiable");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_VALIDATION_METADATA_UNMODIFIABLE.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("CODE", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Override
    @Test
    public void testSendIndicatorsSystemToProductionValidation() throws Exception {

        String uuid = INDICATORS_SYSTEM_1;
        String diffusionVersion = "1.000";
        String productionVersion = "2.000";

        {
            IndicatorsSystemDto indicatorsSystemDtoV1 = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, diffusionVersion);
            IndicatorsSystemDto indicatorsSystemDtoV2 = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, productionVersion);
            assertEquals(diffusionVersion, indicatorsSystemDtoV1.getDiffusionVersion());
            assertEquals(productionVersion, indicatorsSystemDtoV2.getProductionVersion());
            assertEquals(IndicatorsSystemStateEnum.PUBLISHED, indicatorsSystemDtoV1.getState());
            assertEquals(IndicatorsSystemStateEnum.DRAFT, indicatorsSystemDtoV2.getState());
        }

        // Sends to production validation
        indicatorsSystemServiceFacade.sendIndicatorsSystemToProductionValidation(getServiceContext2(), uuid);

        // Validation
        {
            IndicatorsSystemDto indicatorsSystemDtoV1 = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, diffusionVersion);
            IndicatorsSystemDto indicatorsSystemDtoV2 = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, productionVersion);
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
    public void testSendIndicatorsSystemToProductionValidationErrorNotExists() throws Exception {

        try {
            indicatorsSystemServiceFacade.sendIndicatorsSystemToProductionValidation(getServiceContext(), INDICATORS_SYSTEM_NOT_EXISTS);
            fail("Indicators system not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_NOT_FOUND.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(INDICATORS_SYSTEM_NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testSendIndicatorsSystemToProductionValidationErrorWrongState() throws Exception {

        String uuid = INDICATORS_SYSTEM_3;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, INDICATORS_SYSTEM_3_VERSION);
            assertEquals(IndicatorsSystemStateEnum.PUBLISHED, indicatorsSystemDto.getState());
            assertNull(indicatorsSystemDto.getProductionVersion());
        }

        try {
            indicatorsSystemServiceFacade.sendIndicatorsSystemToProductionValidation(getServiceContext(), uuid);
            fail("Indicators system is not draft");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_WRONG_STATE.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorsSystemStateEnum.DRAFT, e.getExceptionItems().get(0).getMessageParameters()[1]);
        }
    }

    @Override
    @Test
    public void testSendIndicatorsSystemToDiffusionValidation() throws Exception {

        String uuid = INDICATORS_SYSTEM_4;
        String versionNumber = "1.000";

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
            assertEquals(null, indicatorsSystemDto.getDiffusionVersion());
            assertEquals("1.000", indicatorsSystemDto.getProductionVersion());
            assertEquals(IndicatorsSystemStateEnum.PRODUCTION_VALIDATION, indicatorsSystemDto.getState());
        }

        // Sends to diffusion validation
        indicatorsSystemServiceFacade.sendIndicatorsSystemToDiffusionValidation(getServiceContext(), uuid);

        // Validation
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
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
            indicatorsSystemServiceFacade.sendIndicatorsSystemToDiffusionValidation(getServiceContext(), INDICATORS_SYSTEM_NOT_EXISTS);
            fail("Indicators system not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_NOT_FOUND.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(INDICATORS_SYSTEM_NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testSendIndicatorsSystemToDiffusionValidationErrorWrongStateDraft() throws Exception {

        String uuid = INDICATORS_SYSTEM_2;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, "1.000");
            assertEquals(IndicatorsSystemStateEnum.DRAFT, indicatorsSystemDto.getState());
            assertEquals("1.000", indicatorsSystemDto.getProductionVersion());
            assertEquals(null, indicatorsSystemDto.getDiffusionVersion());
        }

        try {
            indicatorsSystemServiceFacade.sendIndicatorsSystemToDiffusionValidation(getServiceContext(), uuid);
            fail("Indicators system is not draft");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_WRONG_STATE.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorsSystemStateEnum.PRODUCTION_VALIDATION, e.getExceptionItems().get(0).getMessageParameters()[1]);
        }
    }

    @Test
    public void testSendIndicatorsSystemToDiffusionValidationErrorWrongStatePublished() throws Exception {

        String uuid = INDICATORS_SYSTEM_3;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, INDICATORS_SYSTEM_3_VERSION);
            assertEquals(IndicatorsSystemStateEnum.PUBLISHED, indicatorsSystemDto.getState());
            assertEquals(null, indicatorsSystemDto.getProductionVersion());
            assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDto.getDiffusionVersion());
        }

        try {
            indicatorsSystemServiceFacade.sendIndicatorsSystemToDiffusionValidation(getServiceContext(), uuid);
            fail("Indicators system is not production validation");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_WRONG_STATE.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorsSystemStateEnum.PRODUCTION_VALIDATION, e.getExceptionItems().get(0).getMessageParameters()[1]);
        }
    }

    @Override
    @Test
    public void testRefuseIndicatorsSystemValidation() throws Exception {

        String uuid = INDICATORS_SYSTEM_4;
        String versionNumber = "1.000";

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
            assertEquals(null, indicatorsSystemDto.getDiffusionVersion());
            assertEquals("1.000", indicatorsSystemDto.getProductionVersion());
            assertEquals(IndicatorsSystemStateEnum.PRODUCTION_VALIDATION, indicatorsSystemDto.getState());
        }

        // Refuses validation
        indicatorsSystemServiceFacade.refuseIndicatorsSystemValidation(getServiceContext(), uuid);

        // Validation
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
            assertEquals(null, indicatorsSystemDto.getDiffusionVersion());
            assertEquals("1.000", indicatorsSystemDto.getProductionVersion());
            assertEquals(IndicatorsSystemStateEnum.DRAFT, indicatorsSystemDto.getState());

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
    public void testRefuseIndicatorsSystemValidationInDiffusionValidation() throws Exception {

        String uuid = INDICATORS_SYSTEM_5;
        String versionNumber = "1.000";

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
            assertEquals(null, indicatorsSystemDto.getDiffusionVersion());
            assertEquals("1.000", indicatorsSystemDto.getProductionVersion());
            assertEquals(IndicatorsSystemStateEnum.DIFFUSION_VALIDATION, indicatorsSystemDto.getState());
        }

        // Refuses validation
        indicatorsSystemServiceFacade.refuseIndicatorsSystemValidation(getServiceContext(), uuid);

        // Validation
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
            assertEquals(null, indicatorsSystemDto.getDiffusionVersion());
            assertEquals("1.000", indicatorsSystemDto.getProductionVersion());
            assertEquals(IndicatorsSystemStateEnum.DRAFT, indicatorsSystemDto.getState());
        }
    }

    @Test
    public void testRefuseIndicatorsSystemValidationErrorNotExists() throws Exception {

        try {
            indicatorsSystemServiceFacade.refuseIndicatorsSystemValidation(getServiceContext(), INDICATORS_SYSTEM_NOT_EXISTS);
            fail("Indicators system not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_NOT_FOUND.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(INDICATORS_SYSTEM_NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRefuseIndicatorsSystemValidationErrorWrongStateProduction() throws Exception {

        String uuid = INDICATORS_SYSTEM_2;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, "1.000");
            assertEquals(IndicatorsSystemStateEnum.DRAFT, indicatorsSystemDto.getState());
            assertEquals("1.000", indicatorsSystemDto.getProductionVersion());
            assertEquals(null, indicatorsSystemDto.getDiffusionVersion());
        }

        try {
            indicatorsSystemServiceFacade.refuseIndicatorsSystemValidation(getServiceContext(), uuid);
            fail("Indicators system is not in validation");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_WRONG_STATE.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorsSystemStateEnum.PRODUCTION_VALIDATION, ((IndicatorsSystemStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(IndicatorsSystemStateEnum.DIFFUSION_VALIDATION, ((IndicatorsSystemStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
        }
    }

    @Test
    public void testRefuseIndicatorsSystemValidationErrorWrongStateDiffusion() throws Exception {

        String uuid = INDICATORS_SYSTEM_3;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, INDICATORS_SYSTEM_3_VERSION);
            assertEquals(IndicatorsSystemStateEnum.PUBLISHED, indicatorsSystemDto.getState());
            assertEquals(null, indicatorsSystemDto.getProductionVersion());
            assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDto.getDiffusionVersion());
        }

        try {
            indicatorsSystemServiceFacade.refuseIndicatorsSystemValidation(getServiceContext(), uuid);
            fail("Indicators system is not in validation");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_WRONG_STATE.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
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
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
            assertEquals(versionNumber, indicatorsSystemDto.getProductionVersion());
            assertEquals(null, indicatorsSystemDto.getDiffusionVersion());
            assertEquals(IndicatorsSystemStateEnum.DIFFUSION_VALIDATION, indicatorsSystemDto.getState());
        }

        // Publish
        indicatorsSystemServiceFacade.publishIndicatorsSystem(getServiceContext(), uuid);

        // Validation
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
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
            IndicatorsSystemDto indicatorsSystemDtoV1 = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, diffusionVersionBefore);
            IndicatorsSystemDto indicatorsSystemDtoV2 = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, productionVersionBefore);
            assertEquals(productionVersionBefore, indicatorsSystemDtoV1.getProductionVersion());
            assertEquals(diffusionVersionBefore, indicatorsSystemDtoV1.getDiffusionVersion());
            assertEquals(IndicatorsSystemStateEnum.PUBLISHED, indicatorsSystemDtoV1.getState());
            assertEquals(IndicatorsSystemStateEnum.DIFFUSION_VALIDATION, indicatorsSystemDtoV2.getState());
        }

        // Publish
        indicatorsSystemServiceFacade.publishIndicatorsSystem(getServiceContext(), uuid);

        // Validation
        {
            // Version 1 already not exists
            try {
                indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, diffusionVersionBefore);
                fail("Indicators system version not exists");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_NOT_FOUND_IN_VERSION.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
                assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
                assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
                assertEquals(diffusionVersionBefore, e.getExceptionItems().get(0).getMessageParameters()[1]);
            }

            // Actual version in diffusion is version 2
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, productionVersionBefore);
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
            IndicatorsSystemDto indicatorsSystemDtoV1 = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, diffusionVersionBefore);
            IndicatorsSystemDto indicatorsSystemDtoV2 = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, productionVersionBefore);
            assertEquals(productionVersionBefore, indicatorsSystemDtoV1.getProductionVersion());
            assertEquals(diffusionVersionBefore, indicatorsSystemDtoV1.getDiffusionVersion());
            assertEquals(IndicatorsSystemStateEnum.ARCHIVED, indicatorsSystemDtoV1.getState());
            assertEquals(IndicatorsSystemStateEnum.DIFFUSION_VALIDATION, indicatorsSystemDtoV2.getState());
        }

        // Publish
        indicatorsSystemServiceFacade.publishIndicatorsSystem(getServiceContext(), uuid);

        // Validation
        {
            // Version 1 already not exists
            try {
                indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, diffusionVersionBefore);
                fail("Indicators system version not exists");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_NOT_FOUND_IN_VERSION.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
                assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
                assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
                assertEquals(diffusionVersionBefore, e.getExceptionItems().get(0).getMessageParameters()[1]);
            }

            // Actual version in diffusion is version 2
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, productionVersionBefore);
            assertEquals(null, indicatorsSystemDto.getProductionVersion());
            assertEquals("2.000", indicatorsSystemDto.getDiffusionVersion());
            assertEquals(IndicatorsSystemStateEnum.PUBLISHED, indicatorsSystemDto.getState());
        }
    }

    @Test
    public void testPublishIndicatorsSystemErrorNotExists() throws Exception {

        try {
            indicatorsSystemServiceFacade.publishIndicatorsSystem(getServiceContext(), INDICATORS_SYSTEM_NOT_EXISTS);
            fail("Indicators system not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_NOT_FOUND.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(INDICATORS_SYSTEM_NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testPublishIndicatorsSystemErrorWrongStateProduction() throws Exception {

        String uuid = INDICATORS_SYSTEM_2;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, "1.000");
            assertEquals(IndicatorsSystemStateEnum.DRAFT, indicatorsSystemDto.getState());
            assertEquals("1.000", indicatorsSystemDto.getProductionVersion());
            assertEquals(null, indicatorsSystemDto.getDiffusionVersion());
        }

        try {
            indicatorsSystemServiceFacade.publishIndicatorsSystem(getServiceContext(), uuid);
            fail("Indicators system is not diffusion validation");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_WRONG_STATE.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorsSystemStateEnum.DIFFUSION_VALIDATION, e.getExceptionItems().get(0).getMessageParameters()[1]);
        }
    }

    @Test
    public void testPublishIndicatorsSystemErrorWrongStateDiffusion() throws Exception {

        String uuid = INDICATORS_SYSTEM_3;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, INDICATORS_SYSTEM_3_VERSION);
            assertEquals(IndicatorsSystemStateEnum.PUBLISHED, indicatorsSystemDto.getState());
            assertEquals(null, indicatorsSystemDto.getProductionVersion());
            assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDto.getDiffusionVersion());
        }

        try {
            indicatorsSystemServiceFacade.publishIndicatorsSystem(getServiceContext(), uuid);
            fail("Indicators system is not diffusion validation");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_WRONG_STATE.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorsSystemStateEnum.DIFFUSION_VALIDATION, e.getExceptionItems().get(0).getMessageParameters()[1]);
        }
    }

    @Override
    @Test
    public void testArchiveIndicatorsSystem() throws Exception {

        String uuid = INDICATORS_SYSTEM_3;
        String versionNumber = INDICATORS_SYSTEM_3_VERSION;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
            assertEquals(null, indicatorsSystemDto.getProductionVersion());
            assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDto.getDiffusionVersion());
            assertEquals(IndicatorsSystemStateEnum.PUBLISHED, indicatorsSystemDto.getState());
        }

        // Archive
        indicatorsSystemServiceFacade.archiveIndicatorsSystem(getServiceContext(), uuid);

        // Validation
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
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
            IndicatorsSystemDto indicatorsSystemDtoV1 = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, diffusionVersion);
            IndicatorsSystemDto indicatorsSystemDtoV2 = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, productionVersion);
            assertEquals(productionVersion, indicatorsSystemDtoV1.getProductionVersion());
            assertEquals(diffusionVersion, indicatorsSystemDtoV1.getDiffusionVersion());
            assertEquals(IndicatorsSystemStateEnum.PUBLISHED, indicatorsSystemDtoV1.getState());
            assertEquals(IndicatorsSystemStateEnum.DRAFT, indicatorsSystemDtoV2.getState());
        }

        // Archive
        indicatorsSystemServiceFacade.archiveIndicatorsSystem(getServiceContext(), uuid);

        // Validation
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, diffusionVersion);
            assertEquals(productionVersion, indicatorsSystemDto.getProductionVersion());
            assertEquals(diffusionVersion, indicatorsSystemDto.getDiffusionVersion());
            assertEquals(IndicatorsSystemStateEnum.ARCHIVED, indicatorsSystemDto.getState());
        }
    }

    @Test
    public void testArchiveIndicatorsSystemErrorNotExists() throws Exception {

        try {
            indicatorsSystemServiceFacade.archiveIndicatorsSystem(getServiceContext(), INDICATORS_SYSTEM_NOT_EXISTS);
            fail("Indicators system not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_NOT_FOUND.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(INDICATORS_SYSTEM_NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testArchiveIndicatorsSystemErrorWrongStateProduction() throws Exception {

        String uuid = INDICATORS_SYSTEM_2;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, "1.000");
            assertEquals(IndicatorsSystemStateEnum.DRAFT, indicatorsSystemDto.getState());
            assertEquals("1.000", indicatorsSystemDto.getProductionVersion());
            assertEquals(null, indicatorsSystemDto.getDiffusionVersion());
        }

        try {
            indicatorsSystemServiceFacade.archiveIndicatorsSystem(getServiceContext(), uuid);
            fail("Indicators system is not published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_WRONG_STATE.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorsSystemStateEnum.PUBLISHED, e.getExceptionItems().get(0).getMessageParameters()[1]);
        }
    }

    @Test
    public void testArchiveIndicatorsSystemErrorWrongStateDiffusion() throws Exception {

        String uuid = INDICATORS_SYSTEM_8;

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, "1.000");
            assertEquals(IndicatorsSystemStateEnum.ARCHIVED, indicatorsSystemDto.getState());
            assertEquals(null, indicatorsSystemDto.getProductionVersion());
            assertEquals("1.000", indicatorsSystemDto.getDiffusionVersion());
        }

        try {
            indicatorsSystemServiceFacade.archiveIndicatorsSystem(getServiceContext(), uuid);
            fail("Indicators system is not published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_WRONG_STATE.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorsSystemStateEnum.PUBLISHED, e.getExceptionItems().get(0).getMessageParameters()[1]);
        }
    }

    @Override
    @Test
    public void testVersioningIndicatorsSystem() throws Exception {

        String uuid = INDICATORS_SYSTEM_3;
        String newVersionExpected = "12.000";

        IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, null);
        assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDto.getVersionNumber());
        assertEquals(null, indicatorsSystemDto.getProductionVersion());
        assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDto.getDiffusionVersion());

        IndicatorsSystemDto indicatorsSystemDtoVersioned = indicatorsSystemServiceFacade.versioningIndicatorsSystem(getServiceContext(), uuid, IndicatorsSystemVersionEnum.MAJOR);

        // Validate
        assertEquals(newVersionExpected, indicatorsSystemDtoVersioned.getVersionNumber());
        assertEquals(newVersionExpected, indicatorsSystemDtoVersioned.getProductionVersion());
        assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDtoVersioned.getDiffusionVersion());
        IndicatorsAsserts.assertEqualsIndicatorsSystem(indicatorsSystemDto, indicatorsSystemDtoVersioned);

        {
            IndicatorsSystemDto indicatorsSystemDtoProduction = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, newVersionExpected);
            IndicatorsSystemDto indicatorsSystemDtoDiffusion = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, INDICATORS_SYSTEM_3_VERSION);

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
    public void testVersioningIndicatorsSystemVersionMinor() throws Exception {

        String uuid = INDICATORS_SYSTEM_3;
        String newVersionExpected = "11.034";

        IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, null);
        assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDto.getVersionNumber());
        assertEquals(null, indicatorsSystemDto.getProductionVersion());
        assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDto.getDiffusionVersion());

        IndicatorsSystemDto indicatorsSystemDtoVersioned = indicatorsSystemServiceFacade.versioningIndicatorsSystem(getServiceContext(), uuid, IndicatorsSystemVersionEnum.MINOR);

        // Validate
        assertEquals(newVersionExpected, indicatorsSystemDtoVersioned.getVersionNumber());
        assertEquals(newVersionExpected, indicatorsSystemDtoVersioned.getProductionVersion());
        assertEquals(INDICATORS_SYSTEM_3_VERSION, indicatorsSystemDtoVersioned.getDiffusionVersion());
        IndicatorsAsserts.assertEqualsIndicatorsSystem(indicatorsSystemDto, indicatorsSystemDtoVersioned);

        {
            IndicatorsSystemDto indicatorsSystemDtoProduction = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, newVersionExpected);
            IndicatorsSystemDto indicatorsSystemDtoDiffusion = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, INDICATORS_SYSTEM_3_VERSION);

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
            indicatorsSystemServiceFacade.versioningIndicatorsSystem(getServiceContext(), INDICATORS_SYSTEM_NOT_EXISTS, IndicatorsSystemVersionEnum.MINOR);
            fail("Indicators system not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_NOT_FOUND.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(INDICATORS_SYSTEM_NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }
    
    @Test
    public void testVersioningIndicatorsSystemErrorAlreadyExistsProduction() throws Exception {

        String uuid = INDICATORS_SYSTEM_2;
        
        try {
            indicatorsSystemServiceFacade.versioningIndicatorsSystem(getServiceContext(), uuid, IndicatorsSystemVersionEnum.MINOR);
            fail("Indicators system already exists in production");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_WRONG_STATE.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
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
        List<IndicatorsSystemDto> indicatorsSystemsDto = indicatorsSystemServiceFacade.findIndicatorsSystems(getServiceContext());
        assertEquals(8, indicatorsSystemsDto.size());
        assertEquals(INDICATORS_SYSTEM_1, indicatorsSystemsDto.get(0).getUuid());
        assertEquals(INDICATORS_SYSTEM_2, indicatorsSystemsDto.get(1).getUuid());
        assertEquals(INDICATORS_SYSTEM_3, indicatorsSystemsDto.get(2).getUuid());
        assertEquals(INDICATORS_SYSTEM_4, indicatorsSystemsDto.get(3).getUuid());
        assertEquals(INDICATORS_SYSTEM_5, indicatorsSystemsDto.get(4).getUuid());
        assertEquals(INDICATORS_SYSTEM_6, indicatorsSystemsDto.get(5).getUuid());
        assertEquals(INDICATORS_SYSTEM_7, indicatorsSystemsDto.get(6).getUuid());
        assertEquals(INDICATORS_SYSTEM_8, indicatorsSystemsDto.get(7).getUuid());
    }

    @Override
    @Test
    public void testFindIndicatorsSystemsPublished() throws Exception {
        
        List<IndicatorsSystemDto> indicatorsSystemsDto = indicatorsSystemServiceFacade.findIndicatorsSystemsPublished(getServiceContext());
        assertEquals(3, indicatorsSystemsDto.size());
        assertEquals(INDICATORS_SYSTEM_1, indicatorsSystemsDto.get(0).getUuid());
        assertEquals(INDICATORS_SYSTEM_3, indicatorsSystemsDto.get(1).getUuid());
        assertEquals(INDICATORS_SYSTEM_6, indicatorsSystemsDto.get(2).getUuid());
    }
    

    @Override
    @Test
    public void testCreateDimension() throws Exception {
        
        // Create dimension
        DimensionDto dimensionDto = new DimensionDto();
        dimensionDto.setTitle(IndicatorsMocks.mockInternationalString());
        
        String uuidIndicatorsSystem = INDICATORS_SYSTEM_1;
        DimensionDto dimensionDtoCreated = indicatorsSystemServiceFacade.createDimension(getServiceContext(), uuidIndicatorsSystem, dimensionDto);
        assertNotNull(dimensionDtoCreated.getUuid());

//        // Retrieve and validate TODO
//        DimensionDto dimensionRetrieved = indicatorsSystemServiceFacade.retrieveDimension(getServiceContext(), dimensionDtoCreated.getUuid());
//        assertEqualsDimension(dimensionDto, dimensionRetrieved);
//        // Validate audit
//        assertEquals(getServiceContext().getUserId(), dimensionRetrieved.getCreatedBy());
//        assertTrue(DateUtils.isSameDay(new Date(), dimensionRetrieved.getCreatedDate()));
//        assertTrue(DateUtils.isSameDay(new Date(), dimensionRetrieved.getLastUpdated()));
//        assertEquals(getServiceContext().getUserId(), dimensionRetrieved.getLastUpdatedBy());
//        
//        // Retrieve all dimensions (indicators version had one dimension before; now has two dimensions)
//        List<DimensionDto> dimensionsDtoSize1 = dsdService.retrieveDimensions(getServiceContext(), datasetUri);
//        assertEquals(2, dimensionsDtoSize1.size());
//        assertEqualsDimension(dimensionDto1, dimensionsDtoSize1.get(1));
        
    }
//    
//    @Test
//    public void testCreateDimensionErrorDatasetPutVersionPublishedNotVersionInDraft() throws Exception {
//        
//        // Create dimension
//        DimensionDto dimensionDto1 = mockDimensionDto("Dimension 1", true);
//        String datasetUri = DATASET_1_PROVIDER_4;
//        DatasetDto datasetDtoDraft = dsdService.retrieveDataset(getServiceContext(), datasetUri + ":2");
//        assertEquals(DatasetStateEnum.DRAFT, datasetDtoDraft.getState());
//        datasetUri = datasetUri + ":1"; // put published version
//        
//        try {
//            dsdService.createDimension(getServiceContext(), datasetUri, dimensionDto1);
//            fail("Dataset published");
//        } catch (ApplicationException e) {
//            assertEquals(DsdExceptionCodeEnum.DATASET_INCORRECT_STATUS.getName(), e.getErrorCode());
//        }   
//    }
//    
//    @Test
//    public void testCreateDimensionErrorNameRequired() throws Exception  {
//        
//        DimensionDto dimensionDto1 = new DimensionDto();
//        dimensionDto1.setName(null);
//        
//        try {
//            dsdService.createDimension(getServiceContext(), DATASET_1_PROVIDER_4, dimensionDto1);
//            fail("name required");
//        } catch (ApplicationException e) {
//            assertEquals(DsdExceptionCodeEnum.REQUIRED_ATTRIBUTE.getName(), e.getErrorCode());
//            assertTrue(e.getMessage().contains("name"));
//        }    
//    }
//    
//    @Test
//    public void testCreateDimensionErrorLabelRequired() throws Exception  {
//        
//        DimensionDto dimensionDto1 = new DimensionDto();
//        dimensionDto1.setName(getName("Dimension 1"));
//        dimensionDto1.getName().getTexts().get(0).setLabel(null);
//        
//        try {
//            dsdService.createDimension(getServiceContext(), DATASET_1_PROVIDER_4, dimensionDto1);
//            fail("label required");
//        } catch (ApplicationException e) {
//            assertEquals(DsdExceptionCodeEnum.REQUIRED_ATTRIBUTE.getName(), e.getErrorCode());
//            assertTrue(e.getMessage().contains("label"));
//        }    
//    }
//    
//    @Test
//    public void testCreateDimensionErrorDatasetNotExists() throws Exception  {
//        try {
//            DimensionDto dimensionDto1 = new DimensionDto();
//            dimensionDto1.setName(getName("Dimension name 1"));
//            dsdService.createDimension(getServiceContext(), DATASET_NOT_EXISTS, dimensionDto1);
//            fail("Dataset not exists");
//        } catch (ApplicationException e) {
//            assertEquals(DsdExceptionCodeEnum.DATASET_NOT_EXISTS.getName(), e.getErrorCode());
//        }     
//    }
//    
//    @Test
//    public void testCreateDimensionErrorDatasetPublished() throws Exception  {
//        try {
//            DimensionDto dimensionDto1 = new DimensionDto();
//            dimensionDto1.setName(getName("Dimension name 1"));
//            dsdService.createDimension(getServiceContext(), DATASET_1_PROVIDER_1, dimensionDto1);
//            fail("Dataset published");
//        } catch (ApplicationException e) {
//            assertEquals(DsdExceptionCodeEnum.DATASET_INCORRECT_STATUS.getName(), e.getErrorCode());
//        }   
//    }
//    
//    @Test
//    public void testCreateDimensionErrorValueDuplicated() throws Exception  {
//        
//        DimensionDto dimensionDto1 = mockDimensionDto("Dimension 1", true);
//        dimensionDto1.getValues().get(0).setCode("Code1111");
//        dimensionDto1.getValues().get(1).setCode("Code1111");
//        String datasetUri = DATASET_1_PROVIDER_4;
//
//        try {
//            dsdService.createDimension(getServiceContext(), datasetUri, dimensionDto1);
//            fail("Value duplicated");
//        } catch (ApplicationException e) {
//            assertEquals(DsdExceptionCodeEnum.ILLEGAL_ARGUMENT.getName(), e.getErrorCode());
//            assertTrue(e.getMessage().contains("Code1111"));
//        }   
//    }
//    
//    @Test
//    public void testCreateDimensionErrorValueChildrenDuplicated() throws Exception  {
//        
//        DimensionDto dimensionDto1 = mockDimensionDto("Dimension 1", true);
//        dimensionDto1.getValues().get(0).getChildren().get(0).setCode("Code1111");
//        dimensionDto1.getValues().get(0).getChildren().get(1).setCode("Code1111");
//        String datasetUri = DATASET_1_PROVIDER_4;
//
//        try {
//            dsdService.createDimension(getServiceContext(), datasetUri, dimensionDto1);
//            fail("Value duplicated");
//        } catch (ApplicationException e) {
//            assertEquals(DsdExceptionCodeEnum.ILLEGAL_ARGUMENT.getName(), e.getErrorCode());
//            assertTrue(e.getMessage().contains("Code1111"));
//        }   
//    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/IndicatorsSystemServiceFacadeTest.xml";
    }

    @Override
    protected List<String> getTablesToRemoveContent() {
        List<String> tables = new ArrayList<String>();
        tables.add("TBL_LOCALISED_STRINGS");
        tables.add("TBL_DIMENSIONS");
        tables.add("TBL_INDIC_SYSTEMS_VERSIONS");
        tables.add("TBL_INDICATORS_SYSTEMS");
        tables.add("TBL_INTERNATIONAL_STRINGS");
        return tables;
    }

    @Override
    protected List<String> getSequencesToRestart() {
        List<String> sequences = new ArrayList<String>();
        sequences.add("SEQ_I18NSTRS");
        sequences.add("SEQ_L10NSTRS");
        sequences.add("SEQ_INDIC_SYSTEMS_VERSIONS");
        sequences.add("SEQ_INDICATORS_SYSTEMS");
        sequences.add("SEQ_DIMENSIONS");
        return sequences;
    }
}
