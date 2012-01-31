package es.gobcan.istac.indicators.core.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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

import es.gobcan.istac.indicators.core.domain.IndicatorSystemStateEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.serviceapi.utils.IndicatorsAsserts;
import es.gobcan.istac.indicators.core.serviceapi.utils.IndicatorsMocks;

/**
 * Spring based transactional test with DbUnit support.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext-test.xml"})
public class IndicatorSystemServiceFacadeTest extends IndicatorsBaseTests /* implements IndicatorSystemServiceFacadeTestBase */{ // TODO descomentar implements

    @Autowired
    protected IndicatorSystemServiceFacade indicatorSystemServiceFacade;

    private static String                  INDICATOR_SYSTEM_1          = "IndSys-1";
    private static String                  INDICATOR_SYSTEM_2          = "IndSys-2";
    private static String                  INDICATOR_SYSTEM_3          = "IndSys-3";
    private static String                  INDICATOR_SYSTEM_NOT_EXISTS = "IndSys-not-exists";

    @Test
    public void testCreateIndicatorSystem() throws Exception {

        IndicatorSystemDto indicatorSystemDto = new IndicatorSystemDto();
        indicatorSystemDto.setCode(IndicatorsMocks.mockString(10));
        indicatorSystemDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorSystemDto.setAcronym(IndicatorsMocks.mockInternationalString());
        indicatorSystemDto.setUri(IndicatorsMocks.mockString(100));
        indicatorSystemDto.setObjetive(IndicatorsMocks.mockInternationalString());
        indicatorSystemDto.setDescription(IndicatorsMocks.mockInternationalString());

        // Create
        IndicatorSystemDto indicatorSystemDtoCreated = indicatorSystemServiceFacade.createIndicatorSystem(getServiceContext(), indicatorSystemDto);

        // Validate
        assertNotNull(indicatorSystemDtoCreated);
        assertNotNull(indicatorSystemDtoCreated.getUuid());
        assertNotNull(indicatorSystemDtoCreated.getVersionNumber());
        IndicatorSystemDto indicatorSystemDtoRetrieved = indicatorSystemServiceFacade.retrieveIndicatorSystem(getServiceContext(), indicatorSystemDtoCreated.getUuid(),
                indicatorSystemDtoCreated.getVersionNumber());
        assertEquals(IndicatorSystemStateEnum.DRAFT, indicatorSystemDtoCreated.getState());
        IndicatorsAsserts.assertEqualsIndicatorSystem(indicatorSystemDto, indicatorSystemDtoRetrieved);

        // Validate audit
        assertEquals(getServiceContext().getUserId(), indicatorSystemDtoCreated.getCreatedBy());
        assertTrue(DateUtils.isSameDay(new Date(), indicatorSystemDtoCreated.getCreatedDate()));
        assertTrue(DateUtils.isSameDay(new Date(), indicatorSystemDtoCreated.getLastUpdated()));
        assertEquals(getServiceContext().getUserId(), indicatorSystemDtoCreated.getLastUpdatedBy());
    }

    @Test
    public void testCreateIndicatorSystemCodeRequired() throws Exception {

        IndicatorSystemDto indicatorSystemDto = new IndicatorSystemDto();
        indicatorSystemDto.setCode(null);
        indicatorSystemDto.setTitle(IndicatorsMocks.mockInternationalString());

        try {
            indicatorSystemServiceFacade.createIndicatorSystem(getServiceContext(), indicatorSystemDto);
            fail("code required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_VALIDATION_METADATA_REQUIRED.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("CODE", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorSystemTitleRequired() throws Exception {

        IndicatorSystemDto indicatorSystemDto = new IndicatorSystemDto();
        indicatorSystemDto.setCode(IndicatorsMocks.mockString(10));
        indicatorSystemDto.setTitle(null);

        try {
            indicatorSystemServiceFacade.createIndicatorSystem(getServiceContext(), indicatorSystemDto);
            fail("title required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_VALIDATION_METADATA_REQUIRED.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("TITLE", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorSystemCodeDuplicated() throws Exception {

        IndicatorSystemDto indicatorSystemDto = new IndicatorSystemDto();
        indicatorSystemDto.setCode("CODE-1");
        indicatorSystemDto.setTitle(IndicatorsMocks.mockInternationalString());

        try {
            indicatorSystemServiceFacade.createIndicatorSystem(getServiceContext(), indicatorSystemDto);
            fail("code duplicated");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INDICATORY_SYSTEM_ALREADY_EXIST_CODE_DUPLICATED.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(indicatorSystemDto.getCode(), e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorSystemUriDuplicated() throws Exception {

        IndicatorSystemDto indicatorSystemDto = new IndicatorSystemDto();
        indicatorSystemDto.setCode(IndicatorsMocks.mockString(10));
        indicatorSystemDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorSystemDto.setUri("http://indicators-sytems/1");

        try {
            indicatorSystemServiceFacade.createIndicatorSystem(getServiceContext(), indicatorSystemDto);
            fail("uri duplicated");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INDICATORY_SYSTEM_ALREADY_EXIST_URI_DUPLICATED.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(indicatorSystemDto.getUri(), e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorSystemCodeDuplicatedInsensitive() throws Exception {

        IndicatorSystemDto indicatorSystemDto = new IndicatorSystemDto();
        indicatorSystemDto.setCode("CoDe-1");
        indicatorSystemDto.setTitle(IndicatorsMocks.mockInternationalString());

        try {
            indicatorSystemServiceFacade.createIndicatorSystem(getServiceContext(), indicatorSystemDto);
            fail("code duplicated");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INDICATORY_SYSTEM_ALREADY_EXIST_CODE_DUPLICATED.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(indicatorSystemDto.getCode(), e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRetrieveIndicatorSystem() throws Exception {

        String uuid = INDICATOR_SYSTEM_1;
        Long versionNumber = Long.valueOf(1);
        IndicatorSystemDto indicatorSystemDto = indicatorSystemServiceFacade.retrieveIndicatorSystem(getServiceContext(), uuid, versionNumber);

        assertNotNull(indicatorSystemDto);
        assertEquals(uuid, indicatorSystemDto.getUuid());
        assertEquals(versionNumber, indicatorSystemDto.getVersionNumber());
        assertEquals(Long.valueOf(1), indicatorSystemDto.getPublishedVersion());
        assertEquals(Long.valueOf(2), indicatorSystemDto.getDraftVersion());
        assertEquals("CODE-1", indicatorSystemDto.getCode());
        assertEquals("http://indicators-sytems/1", indicatorSystemDto.getUri());
        assertEquals(IndicatorSystemStateEnum.PUBLISHED, indicatorSystemDto.getState());
        IndicatorsAsserts.assertEqualsInternationalString(indicatorSystemDto.getTitle(), "es", "Título IndSys-1-v1", "en", "Title IndSys-1-v1");
        IndicatorsAsserts.assertEqualsInternationalString(indicatorSystemDto.getAcronym(), "es", "Acrónimo IndSys-1-v1", "en", "Acronym IndSys-1-v1");
        IndicatorsAsserts.assertEqualsInternationalString(indicatorSystemDto.getDescription(), "es", "Descripción IndSys-1-v1", "en", "Description IndSys-1-v1");
        IndicatorsAsserts.assertEqualsInternationalString(indicatorSystemDto.getObjetive(), "es", "Objetivo IndSys-1-v1", "en", "Objetive IndSys-1-v1");
        assertEquals("2012-01-01T01:02:04.000Z", (new DateTime(indicatorSystemDto.getCreatedDate())).toString());
        assertEquals("2012-01-02T02:02:02.000Z", (new DateTime(indicatorSystemDto.getPublishingDate())).toString());

    }

    @Test
    public void testRetrieveIndicatorSystemWithAndWithoutVersion() throws Exception {

        String uuid = INDICATOR_SYSTEM_1;
        Long versionNumberDraft = Long.valueOf(2);
        Long versionNumberPublished = Long.valueOf(1);

        // Without version (retrieve last)
        {
            IndicatorSystemDto indicatorSystemDto = indicatorSystemServiceFacade.retrieveIndicatorSystem(getServiceContext(), uuid, null);
            assertEquals(uuid, indicatorSystemDto.getUuid());
            assertEquals(versionNumberDraft, indicatorSystemDto.getVersionNumber());
            assertEquals(versionNumberPublished, indicatorSystemDto.getPublishedVersion());
            assertEquals(versionNumberDraft, indicatorSystemDto.getDraftVersion());
        }

        // With version 1
        {
            IndicatorSystemDto indicatorSystemDto = indicatorSystemServiceFacade.retrieveIndicatorSystem(getServiceContext(), uuid, versionNumberPublished);
            assertEquals(uuid, indicatorSystemDto.getUuid());
            assertEquals(versionNumberPublished, indicatorSystemDto.getVersionNumber());
            assertEquals(versionNumberPublished, indicatorSystemDto.getPublishedVersion());
            assertEquals(versionNumberDraft, indicatorSystemDto.getDraftVersion());
        }

        // With version 2
        {
            IndicatorSystemDto indicatorSystemDto = indicatorSystemServiceFacade.retrieveIndicatorSystem(getServiceContext(), uuid, versionNumberDraft);
            assertEquals(uuid, indicatorSystemDto.getUuid());
            assertEquals(versionNumberDraft, indicatorSystemDto.getVersionNumber());
            assertEquals(versionNumberPublished, indicatorSystemDto.getPublishedVersion());
            assertEquals(versionNumberDraft, indicatorSystemDto.getDraftVersion());
        }
    }

    @Test
    public void testRetrieveIndicatorSystemPublished() throws Exception {

        String uuid = INDICATOR_SYSTEM_3;

        IndicatorSystemDto indicatorSystemDtoPublished = indicatorSystemServiceFacade.retrieveIndicatorSystemPublished(getServiceContext(), uuid);
        assertEquals(uuid, indicatorSystemDtoPublished.getUuid());
        assertEquals(Long.valueOf(1), indicatorSystemDtoPublished.getVersionNumber());
        assertEquals(IndicatorSystemStateEnum.PUBLISHED, indicatorSystemDtoPublished.getState());
    }

    @Test
    public void testRetrieveIndicatorSystemPublishedWhenSystemHasVersionDraft() throws Exception {

        String uuid = INDICATOR_SYSTEM_1;

        IndicatorSystemDto indicatorSystemDtoPublished = indicatorSystemServiceFacade.retrieveIndicatorSystemPublished(getServiceContext(), uuid);
        assertEquals(uuid, indicatorSystemDtoPublished.getUuid());
        assertEquals(Long.valueOf(1), indicatorSystemDtoPublished.getVersionNumber());
        assertEquals(IndicatorSystemStateEnum.PUBLISHED, indicatorSystemDtoPublished.getState());
    }

    @Test
    public void testRetrieveIndicatorSystemPublishedErrorOnlyDraft() throws Exception {

        String uuid = INDICATOR_SYSTEM_2;

        try {
            indicatorSystemServiceFacade.retrieveIndicatorSystemPublished(getServiceContext(), uuid);
            fail("No published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INDICATORY_SYSTEM_NOT_FOUND_IN_STATE.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorSystemStateEnum.PUBLISHED, e.getExceptionItems().get(0).getMessageParameters()[1]);
        }
    }

    @Test
    public void testRetrieveIndicatorSystemErrorNotExists() throws Exception {
        
        String uuid = INDICATOR_SYSTEM_NOT_EXISTS;
        
        try {
            indicatorSystemServiceFacade.retrieveIndicatorSystem(getServiceContext(), uuid, null);
            fail("No exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INDICATORY_SYSTEM_NOT_FOUND.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }
    
    @Test
    public void testRetrieveIndicatorSystemErrorVersionNotExists() throws Exception {
        
        String uuid = INDICATOR_SYSTEM_2;
        Long versionNotExists = Long.valueOf(99);
        
        try {
            indicatorSystemServiceFacade.retrieveIndicatorSystem(getServiceContext(), uuid, versionNotExists);
            fail("No exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INDICATORY_SYSTEM_NOT_FOUND.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(versionNotExists, e.getExceptionItems().get(0).getMessageParameters()[1]);
        }
    }
    
    @Test
    public void testRetrieveIndicatorSystemPublishedErrorNotExists() throws Exception {
        
        String uuid = INDICATOR_SYSTEM_NOT_EXISTS;
        
        try {
            indicatorSystemServiceFacade.retrieveIndicatorSystemPublished(getServiceContext(), uuid);
            fail("No exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INDICATORY_SYSTEM_NOT_FOUND.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    // @Test
    // public void testMakeDraftIndicatorSystem() throws Exception {
    // // TODO Auto-generated method stub
    // // fail("testMakeDraftIndicatorSystem not implemented");
    // }
    //
    // @Test
    // public void testUpdateIndicatorSystem() throws Exception {
    // // TODO Auto-generated method stub
    // // fail("testUpdateIndicatorSystem not implemented");
    // }
    //
    // @Test
    // public void testPublishIndicatorSystem() throws Exception {
    // // TODO Auto-generated method stub
    // // fail("testPublishIndicatorSystem not implemented");
    // }

    //
    // @Test
    // public void testDeleteIndicatorSystem() throws Exception {
    // // TODO Auto-generated method stub
    // // fail("testDeleteIndicatorSystem not implemented");
    // }

    @Override
    protected String getDataSetFile() {
        return "dbunit/IndicatorSystemServiceFacadeTest.xml";
    }

    @Override
    protected List<String> getTablesToRemoveContent() {
        List<String> tables = new ArrayList<String>();
        tables.add("TBL_LOCALISED_STRINGS");
        tables.add("TBL_INDICATOR_SYSTEM_VERS");
        tables.add("TBL_INDICATORS_SYSTEMS");
        tables.add("TBL_INTERNATIONAL_STRINGS");
        return tables;
    }
}
