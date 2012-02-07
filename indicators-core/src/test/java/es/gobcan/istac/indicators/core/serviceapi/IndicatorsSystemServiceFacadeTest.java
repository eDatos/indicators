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
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.gobcan.istac.indicators.core.enume.domain.IndicatorsSystemStateEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.serviceapi.utils.IndicatorsAsserts;
import es.gobcan.istac.indicators.core.serviceapi.utils.IndicatorsMocks;

/**
 * Spring based transactional test with DbUnit support.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext-test.xml"})
public class IndicatorsSystemServiceFacadeTest extends IndicatorsBaseTests implements IndicatorsSystemServiceFacadeTestBase {

    @Autowired
    protected IndicatorsSystemServiceFacade indicatorsSystemServiceFacade;

    private static String                   INDICATORS_SYSTEM_1          = "IndSys-1";
    private static String                   INDICATORS_SYSTEM_2          = "IndSys-2";
    private static String                   INDICATORS_SYSTEM_3          = "IndSys-3";
    private static String                   INDICATORS_SYSTEM_4          = "IndSys-4";
    private static String                   INDICATORS_SYSTEM_5          = "IndSys-5";
    private static String                   INDICATORS_SYSTEM_6          = "IndSys-6";
    private static String                   INDICATORS_SYSTEM_7          = "IndSys-7";
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
        assertEquals(Long.valueOf(1), indicatorsSystemDtoRetrieved.getProductionVersion());
        assertNull(indicatorsSystemDtoRetrieved.getDiffusionVersion());
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
            assertEquals("CODE", e.getExceptionItems().get(0).getMessageParameters()[0]);
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
            assertEquals("TITLE", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorsSystemCodeDuplicated() throws Exception {

        IndicatorsSystemDto indicatorsSystemDto = new IndicatorsSystemDto();
        indicatorsSystemDto.setCode("CODE-1");
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
        Long versionNumber = Long.valueOf(1);
        IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);

        assertNotNull(indicatorsSystemDto);
        assertEquals(uuid, indicatorsSystemDto.getUuid());
        assertEquals(versionNumber, indicatorsSystemDto.getVersionNumber());
        assertEquals(Long.valueOf(1), indicatorsSystemDto.getDiffusionVersion());
        assertEquals(Long.valueOf(2), indicatorsSystemDto.getProductionVersion());
        assertEquals("CODE-1", indicatorsSystemDto.getCode());
        assertEquals("http://indicators-sytems/1", indicatorsSystemDto.getUri());
        assertEquals(IndicatorsSystemStateEnum.PUBLISHED, indicatorsSystemDto.getState());
        IndicatorsAsserts.assertEqualsInternationalString(indicatorsSystemDto.getTitle(), "es", "Título IndSys-1-v1", "en", "Title IndSys-1-v1");
        IndicatorsAsserts.assertEqualsInternationalString(indicatorsSystemDto.getAcronym(), "es", "Acrónimo IndSys-1-v1", "en", "Acronym IndSys-1-v1");
        IndicatorsAsserts.assertEqualsInternationalString(indicatorsSystemDto.getDescription(), "es", "Descripción IndSys-1-v1", "en", "Description IndSys-1-v1");
        IndicatorsAsserts.assertEqualsInternationalString(indicatorsSystemDto.getObjetive(), "es", "Objetivo IndSys-1-v1", "en", "Objetive IndSys-1-v1");
        assertEquals("2011-01-01T01:02:04.000Z", (new DateTime(indicatorsSystemDto.getCreatedDate())).toString());
        assertEquals("2011-01-02T02:02:02.000Z", (new DateTime(indicatorsSystemDto.getPublishingDate())).toString());

    }

    @Test
    public void testRetrieveIndicatorsSystemWithAndWithoutVersion() throws Exception {

        String uuid = INDICATORS_SYSTEM_1;
        Long versionNumberProduction = Long.valueOf(2);
        Long versionNumberDiffusion = Long.valueOf(1);

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
        assertEquals(Long.valueOf(1), indicatorsSystemDtoPublished.getVersionNumber());
        assertEquals(IndicatorsSystemStateEnum.PUBLISHED, indicatorsSystemDtoPublished.getState());
    }

    @Test
    public void testRetrieveIndicatorsSystemPublishedWhenSystemHasVersionProduction() throws Exception {

        String uuid = INDICATORS_SYSTEM_1;

        IndicatorsSystemDto indicatorsSystemDtoPublished = indicatorsSystemServiceFacade.retrieveIndicatorsSystemPublished(getServiceContext(), uuid);
        assertEquals(uuid, indicatorsSystemDtoPublished.getUuid());
        assertEquals(Long.valueOf(1), indicatorsSystemDtoPublished.getVersionNumber());
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
        Long versionNotExists = Long.valueOf(99);

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
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, Long.valueOf(1));
            assertEquals(uuid, indicatorsSystemDto.getUuid());
            assertEquals(Long.valueOf(1), indicatorsSystemDto.getVersionNumber());
            assertEquals(Long.valueOf(1), indicatorsSystemDto.getDiffusionVersion());
            assertEquals(Long.valueOf(2), indicatorsSystemDto.getProductionVersion());
        }

        // Delete indicators system
        indicatorsSystemServiceFacade.deleteIndicatorsSystem(getServiceContext(), uuid);

        // Validation
        // Version 1 exists
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, Long.valueOf(1));
            assertEquals(uuid, indicatorsSystemDto.getUuid());
            assertEquals(Long.valueOf(1), indicatorsSystemDto.getVersionNumber());
            assertEquals(Long.valueOf(1), indicatorsSystemDto.getDiffusionVersion());
            assertEquals(null, indicatorsSystemDto.getProductionVersion());
        }
        // Version 2 not exists
        try {
            indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, Long.valueOf(2));
            fail("Indicators system version deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_NOT_FOUND_IN_VERSION.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(Long.valueOf(2), e.getExceptionItems().get(0).getMessageParameters()[1]);
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
        Long versionNumber = Long.valueOf(2);

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
        Long versionNumber = Long.valueOf(1);

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
        Long versionNumber = Long.valueOf(2);

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

        IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), INDICATORS_SYSTEM_1, Long.valueOf(2));
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
        Long versionNumber = Long.valueOf(1);

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
        Long versionNumber = Long.valueOf(1);

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
        Long versionNumber = Long.valueOf(2);

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

        {
            IndicatorsSystemDto indicatorsSystemDtoV1 = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, Long.valueOf(1));
            IndicatorsSystemDto indicatorsSystemDtoV2 = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, Long.valueOf(2));
            assertEquals(Long.valueOf(1), indicatorsSystemDtoV1.getDiffusionVersion());
            assertEquals(Long.valueOf(2), indicatorsSystemDtoV2.getProductionVersion());
            assertEquals(IndicatorsSystemStateEnum.PUBLISHED, indicatorsSystemDtoV1.getState());
            assertEquals(IndicatorsSystemStateEnum.DRAFT, indicatorsSystemDtoV2.getState());
        }

        // Sends to production validation
        indicatorsSystemServiceFacade.sendIndicatorsSystemToProductionValidation(getServiceContext(), uuid);
        
        // Validation
        {
            IndicatorsSystemDto indicatorsSystemDtoV1 = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, Long.valueOf(1));
            IndicatorsSystemDto indicatorsSystemDtoV2 = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, Long.valueOf(2));
            assertEquals(Long.valueOf(1), indicatorsSystemDtoV1.getDiffusionVersion());
            assertEquals(Long.valueOf(2), indicatorsSystemDtoV2.getProductionVersion());
            assertEquals(IndicatorsSystemStateEnum.PUBLISHED, indicatorsSystemDtoV1.getState());
            assertEquals(IndicatorsSystemStateEnum.PRODUCTION_VALIDATION, indicatorsSystemDtoV2.getState());
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
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, Long.valueOf(1));
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
        Long versionNumber = Long.valueOf(1);

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
            assertEquals(null, indicatorsSystemDto.getDiffusionVersion());
            assertEquals(Long.valueOf(1), indicatorsSystemDto.getProductionVersion());
            assertEquals(IndicatorsSystemStateEnum.PRODUCTION_VALIDATION, indicatorsSystemDto.getState());
        }

        // Sends to diffusion validation
        indicatorsSystemServiceFacade.sendIndicatorsSystemToDiffusionValidation(getServiceContext(), uuid);
        
        // Validation
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
            assertEquals(null, indicatorsSystemDto.getDiffusionVersion());
            assertEquals(Long.valueOf(1), indicatorsSystemDto.getProductionVersion());
            assertEquals(IndicatorsSystemStateEnum.DIFFUSION_VALIDATION, indicatorsSystemDto.getState());
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
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, Long.valueOf(1));
            assertEquals(IndicatorsSystemStateEnum.DRAFT, indicatorsSystemDto.getState());
            assertEquals(Long.valueOf(1), indicatorsSystemDto.getProductionVersion());
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
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, Long.valueOf(1));
            assertEquals(IndicatorsSystemStateEnum.PUBLISHED, indicatorsSystemDto.getState());
            assertEquals(null, indicatorsSystemDto.getProductionVersion());
            assertEquals(Long.valueOf(1), indicatorsSystemDto.getDiffusionVersion());
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
        Long versionNumber = Long.valueOf(1);

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
            assertEquals(null, indicatorsSystemDto.getDiffusionVersion());
            assertEquals(Long.valueOf(1), indicatorsSystemDto.getProductionVersion());
            assertEquals(IndicatorsSystemStateEnum.PRODUCTION_VALIDATION, indicatorsSystemDto.getState());
        }

        // Refuses validation
        indicatorsSystemServiceFacade.refuseIndicatorsSystemValidation(getServiceContext(), uuid);
        
        // Validation
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
            assertEquals(null, indicatorsSystemDto.getDiffusionVersion());
            assertEquals(Long.valueOf(1), indicatorsSystemDto.getProductionVersion());
            assertEquals(IndicatorsSystemStateEnum.DRAFT, indicatorsSystemDto.getState());
        }
    }
    
    @Test
    public void testRefuseIndicatorsSystemValidationInDiffusionValidation() throws Exception {
        
        String uuid = INDICATORS_SYSTEM_5;
        Long versionNumber = Long.valueOf(1);

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
            assertEquals(null, indicatorsSystemDto.getDiffusionVersion());
            assertEquals(Long.valueOf(1), indicatorsSystemDto.getProductionVersion());
            assertEquals(IndicatorsSystemStateEnum.DIFFUSION_VALIDATION, indicatorsSystemDto.getState());
        }

        // Refuses validation
        indicatorsSystemServiceFacade.refuseIndicatorsSystemValidation(getServiceContext(), uuid);
        
        // Validation
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
            assertEquals(null, indicatorsSystemDto.getDiffusionVersion());
            assertEquals(Long.valueOf(1), indicatorsSystemDto.getProductionVersion());
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
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, Long.valueOf(1));
            assertEquals(IndicatorsSystemStateEnum.DRAFT, indicatorsSystemDto.getState());
            assertEquals(Long.valueOf(1), indicatorsSystemDto.getProductionVersion());
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
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, Long.valueOf(1));
            assertEquals(IndicatorsSystemStateEnum.PUBLISHED, indicatorsSystemDto.getState());
            assertEquals(null, indicatorsSystemDto.getProductionVersion());
            assertEquals(Long.valueOf(1), indicatorsSystemDto.getDiffusionVersion());
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
        Long versionNumber = Long.valueOf(1);

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
            assertEquals(Long.valueOf(1), indicatorsSystemDto.getProductionVersion());
            assertEquals(null, indicatorsSystemDto.getDiffusionVersion());
            assertEquals(IndicatorsSystemStateEnum.DIFFUSION_VALIDATION, indicatorsSystemDto.getState());
        }

        // Publish
        indicatorsSystemServiceFacade.publishIndicatorsSystem(getServiceContext(), uuid);
        
        // Validation
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
            assertEquals(null, indicatorsSystemDto.getProductionVersion());
            assertEquals(Long.valueOf(1), indicatorsSystemDto.getDiffusionVersion());
            assertEquals(IndicatorsSystemStateEnum.PUBLISHED, indicatorsSystemDto.getState());
        }
    }
    
    @Test
    public void testPublishIndicatorsSystemWithPublishedVersion() throws Exception {
        
        String uuid = INDICATORS_SYSTEM_6;
        Long diffusionVersionBefore = Long.valueOf(1);   // will be deleted when publish current version in diffusion validation
        Long productionVersionBefore = Long.valueOf(2); // will be published

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
            assertEquals(Long.valueOf(2), indicatorsSystemDto.getDiffusionVersion());
            assertEquals(IndicatorsSystemStateEnum.PUBLISHED, indicatorsSystemDto.getState());
        }
    }
    
    @Test
    public void testPublishIndicatorsSystemWithArchivedVersion() throws Exception {
        
        String uuid = INDICATORS_SYSTEM_7;
        Long diffusionVersionBefore = Long.valueOf(1);  // will be deleted when publish current version in diffusion validation
        Long productionVersionBefore = Long.valueOf(2); // will be published

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
            assertEquals(Long.valueOf(2), indicatorsSystemDto.getDiffusionVersion());
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
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, Long.valueOf(1));
            assertEquals(IndicatorsSystemStateEnum.DRAFT, indicatorsSystemDto.getState());
            assertEquals(Long.valueOf(1), indicatorsSystemDto.getProductionVersion());
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
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, Long.valueOf(1));
            assertEquals(IndicatorsSystemStateEnum.PUBLISHED, indicatorsSystemDto.getState());
            assertEquals(null, indicatorsSystemDto.getProductionVersion());
            assertEquals(Long.valueOf(1), indicatorsSystemDto.getDiffusionVersion());
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
        Long versionNumber = Long.valueOf(1);

        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
            assertEquals(null, indicatorsSystemDto.getProductionVersion());
            assertEquals(Long.valueOf(1), indicatorsSystemDto.getDiffusionVersion());
            assertEquals(IndicatorsSystemStateEnum.PUBLISHED, indicatorsSystemDto.getState());
        }

        // Archive
        indicatorsSystemServiceFacade.archiveIndicatorsSystem(getServiceContext(), uuid);
        
        // Validation
        {
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
            assertEquals(null, indicatorsSystemDto.getProductionVersion());
            assertEquals(Long.valueOf(1), indicatorsSystemDto.getDiffusionVersion());
            assertEquals(IndicatorsSystemStateEnum.ARCHIVED, indicatorsSystemDto.getState());
        }
    }
    
    @Test
    public void testArchiveIndicatorsSystemWithProductionVersion() throws Exception {
        
        String uuid = INDICATORS_SYSTEM_1;
        Long diffusionVersion = Long.valueOf(1);
        Long productionVersion = Long.valueOf(2);

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
            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, Long.valueOf(1));
            assertEquals(IndicatorsSystemStateEnum.DRAFT, indicatorsSystemDto.getState());
            assertEquals(Long.valueOf(1), indicatorsSystemDto.getProductionVersion());
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
        // TODO
//        String uuid = INDICATORS_SYSTEM_8;
//
//        {
//            IndicatorsSystemDto indicatorsSystemDto = indicatorsSystemServiceFacade.retrieveIndicatorsSystem(getServiceContext(), uuid, Long.valueOf(1));
//            assertEquals(IndicatorsSystemStateEnum.ARCHIVED, indicatorsSystemDto.getState());
//            assertEquals(null, indicatorsSystemDto.getProductionVersion());
//            assertEquals(Long.valueOf(1), indicatorsSystemDto.getDiffusionVersion());
//        }
//        
//        try {
//            indicatorsSystemServiceFacade.archiveIndicatorsSystem(getServiceContext(), uuid);
//            fail("Indicators system is not published");
//        } catch (MetamacException e) {
//            assertEquals(1, e.getExceptionItems().size());
//            assertEquals(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_WRONG_STATE.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
//            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
//            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
//            assertEquals(IndicatorsSystemStateEnum.PUBLISHED, e.getExceptionItems().get(0).getMessageParameters()[1]);
//        }  
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/IndicatorsSystemServiceFacadeTest.xml";
    }

    @Override
    protected List<String> getTablesToRemoveContent() {
        List<String> tables = new ArrayList<String>();
        tables.add("TBL_LOCALISED_STRINGS");
        tables.add("TBL_INDIC_SYSTEMS_VERSIONS");
        tables.add("TBL_INDICATORS_SYSTEMS");
        tables.add("TBL_INTERNATIONAL_STRINGS");
        return tables;
    }
}
