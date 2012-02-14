package es.gobcan.istac.indicators.core.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorStateEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.serviceapi.utils.IndicatorsAsserts;
import es.gobcan.istac.indicators.core.serviceapi.utils.IndicatorsMocks;

/**
 * Spring based transactional test with DbUnit support.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext-test.xml"})
public class IndicatorsServiceFacadeTest extends IndicatorsBaseTest implements IndicatorsServiceFacadeTestBase {

    @Autowired
    protected IndicatorsServiceFacade indicatorsServiceFacade;

    private static String             NOT_EXISTS  = "not-exists";

    // Indicators
    private static String             INDICATOR_1 = "Indicator-1";
    private static String             INDICATOR_2 = "Indicator-2";
    private static String             INDICATOR_3 = "Indicator-3";

    // private static String INDICATOR_3_VERSION = "11.033";
    // private static String INDICATOR_4 = "Indicator-4";
    // private static String INDICATOR_5 = "Indicator-5";
    // private static String INDICATOR_6 = "Indicator-6";
    // private static String INDICATOR_7 = "Indicator-7";
    // private static String INDICATOR_8 = "Indicator-8";
    // private static String INDICATOR_9 = "Indicator-9";

    @Test
    public void testRetrieveIndicator() throws Exception {

        String uuid = INDICATOR_1;
        String versionNumber = "1.000";
        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumber);

        assertNotNull(indicatorDto);
        assertEquals(uuid, indicatorDto.getUuid());
        assertEquals(versionNumber, indicatorDto.getVersionNumber());
        assertEquals("1.000", indicatorDto.getDiffusionVersion());
        assertEquals("2.000", indicatorDto.getProductionVersion());
        assertEquals("CODE-1", indicatorDto.getCode());
        assertEquals(IndicatorStateEnum.PUBLISHED, indicatorDto.getState());
        IndicatorsAsserts.assertEqualsInternationalString(indicatorDto.getName(), "es", "Nombre Indicator-1-v1", "en", "Name Indicator-1-v1");
        IndicatorsAsserts.assertEqualsInternationalString(indicatorDto.getAcronym(), "es", "Acrónimo Indicator-1-v1", "en", "Acronym Indicator-1-v1");
        IndicatorsAsserts.assertEqualsInternationalString(indicatorDto.getCommentary(), "es", "Comentario Indicator-1-v1", "en", "Commentary Indicator-1-v1");
        IndicatorsAsserts.assertEqualsInternationalString(indicatorDto.getNotes(), "es", "Nota Indicator-1-v1", "en", "Note Indicator-1-v1");
        assertEquals("http://indicators/1", indicatorDto.getNoteUrl());
        IndicatorsAsserts.assertEqualsDate("2011-01-01 01:02:04", indicatorDto.getCreatedDate());
        IndicatorsAsserts.assertEqualsDate("2011-01-02 02:02:04", indicatorDto.getProductionValidationDate());
        IndicatorsAsserts.assertEqualsDate("2011-01-03 03:02:04", indicatorDto.getDiffusionValidationDate());
        IndicatorsAsserts.assertEqualsDate("2011-01-04 04:02:04", indicatorDto.getPublicationDate());
        assertNull(indicatorDto.getArchiveDate());

    }

    @Test
    public void testRetrieveIndicatorWithAndWithoutVersion() throws Exception {

        String uuid = INDICATOR_1;
        String versionNumberProduction = "2.000";
        String versionNumberDiffusion = "1.000";

        // Without version (retrieve last)
        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, null);
            assertEquals(uuid, indicatorDto.getUuid());
            assertEquals(versionNumberProduction, indicatorDto.getVersionNumber());
            assertEquals(versionNumberDiffusion, indicatorDto.getDiffusionVersion());
            assertEquals(versionNumberProduction, indicatorDto.getProductionVersion());
        }

        // With version 1
        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumberDiffusion);
            assertEquals(uuid, indicatorDto.getUuid());
            assertEquals(versionNumberDiffusion, indicatorDto.getVersionNumber());
            assertEquals(versionNumberDiffusion, indicatorDto.getDiffusionVersion());
            assertEquals(versionNumberProduction, indicatorDto.getProductionVersion());
        }

        // With version 2
        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumberProduction);
            assertEquals(uuid, indicatorDto.getUuid());
            assertEquals(versionNumberProduction, indicatorDto.getVersionNumber());
            assertEquals(versionNumberDiffusion, indicatorDto.getDiffusionVersion());
            assertEquals(versionNumberProduction, indicatorDto.getProductionVersion());
        }
    }

    @Test
    public void testRetrieveIndicatorErrorParameterRequired() throws Exception {

        String uuid = null;

        try {
            indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, null);
            fail("parameter required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INVALID_PARAMETER_REQUIRED.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("UUID", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRetrieveIndicatorErrorNotExists() throws Exception {

        String uuid = NOT_EXISTS;

        try {
            indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, null);
            fail("No exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INDICATOR_NOT_FOUND.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRetrieveIndicatorErrorVersionNotExists() throws Exception {

        String uuid = INDICATOR_2;
        String versionNotExists = String.valueOf(99);

        try {
            indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNotExists);
            fail("No exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INDICATOR_VERSION_NOT_FOUND.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(versionNotExists, e.getExceptionItems().get(0).getMessageParameters()[1]);
        }
    }

    @Test
    public void testRetrieveIndicatorPublished() throws Exception {

        String uuid = INDICATOR_3;

        IndicatorDto indicatorDtoPublished = indicatorsServiceFacade.retrieveIndicatorPublished(getServiceContext(), uuid);
        assertEquals(uuid, indicatorDtoPublished.getUuid());
        assertEquals("11.033", indicatorDtoPublished.getVersionNumber());
        assertEquals(IndicatorStateEnum.PUBLISHED, indicatorDtoPublished.getState());
    }

    @Test
    public void testRetrieveIndicatorPublishedWhenHasVersionProduction() throws Exception {

        String uuid = INDICATOR_1;

        IndicatorDto indicatorDtoPublished = indicatorsServiceFacade.retrieveIndicatorPublished(getServiceContext(), uuid);
        assertEquals(uuid, indicatorDtoPublished.getUuid());
        assertEquals("1.000", indicatorDtoPublished.getVersionNumber());
        assertEquals(IndicatorStateEnum.PUBLISHED, indicatorDtoPublished.getState());
    }

    @Test
    public void testRetrieveIndicatorPublishedErrorOnlyProduction() throws Exception {

        String uuid = INDICATOR_2;

        try {
            indicatorsServiceFacade.retrieveIndicatorPublished(getServiceContext(), uuid);
            fail("No published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INDICATOR_WRONG_STATE.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorStateEnum.PUBLISHED, e.getExceptionItems().get(0).getMessageParameters()[1]);
        }
    }

    @Test
    public void testRetrieveIndicatorPublishedErrorParameterRequired() throws Exception {

        String uuid = null;

        try {
            indicatorsServiceFacade.retrieveIndicatorPublished(getServiceContext(), uuid);
            fail("parameter required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INVALID_PARAMETER_REQUIRED.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("UUID", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRetrieveIndicatorPublishedErrorNotExists() throws Exception {

        String uuid = NOT_EXISTS;

        try {
            indicatorsServiceFacade.retrieveIndicatorPublished(getServiceContext(), uuid);
            fail("No exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INDICATOR_NOT_FOUND.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicator() throws Exception {

        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode(IndicatorsMocks.mockString(10));
        indicatorDto.setName(IndicatorsMocks.mockInternationalString());
        indicatorDto.setAcronym(IndicatorsMocks.mockInternationalString());
        indicatorDto.setSubjectCode(IndicatorsMocks.mockString(10));
        indicatorDto.setCommentary(IndicatorsMocks.mockInternationalString());
        indicatorDto.setNotes(IndicatorsMocks.mockInternationalString());
        indicatorDto.setNoteUrl(IndicatorsMocks.mockString(100));

        // Create
        IndicatorDto indicatorDtoCreated = indicatorsServiceFacade.createIndicator(getServiceContext(), indicatorDto);

        // Validate
        assertNotNull(indicatorDtoCreated);
        assertNotNull(indicatorDtoCreated.getUuid());
        assertNotNull(indicatorDtoCreated.getVersionNumber());

         IndicatorDto indicatorDtoRetrieved = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), indicatorDtoCreated.getUuid(),
         indicatorDtoCreated.getVersionNumber());
         assertEquals(IndicatorStateEnum.DRAFT, indicatorDtoCreated.getState());
         assertEquals(IndicatorStateEnum.DRAFT, indicatorDtoRetrieved.getState());
         assertEquals("1.000", indicatorDtoRetrieved.getProductionVersion());
         assertNull(indicatorDtoRetrieved.getDiffusionVersion());
         assertNull(indicatorDtoRetrieved.getProductionValidationDate());
         assertNull(indicatorDtoRetrieved.getProductionValidationUser());
         assertNull(indicatorDtoRetrieved.getDiffusionValidationDate());
         assertNull(indicatorDtoRetrieved.getDiffusionValidationUser());
         assertNull(indicatorDtoRetrieved.getPublicationDate());
         assertNull(indicatorDtoRetrieved.getPublicationUser());
         assertNull(indicatorDtoRetrieved.getArchiveDate());
         assertNull(indicatorDtoRetrieved.getArchiveUser());
        
         IndicatorsAsserts.assertEqualsIndicator(indicatorDto, indicatorDtoRetrieved);
        
        // Validate audit
        assertEquals(getServiceContext().getUserId(), indicatorDtoCreated.getCreatedBy());
        assertTrue(DateUtils.isSameDay(new Date(), indicatorDtoCreated.getCreatedDate()));
        assertTrue(DateUtils.isSameDay(new Date(), indicatorDtoCreated.getLastUpdated()));
        assertEquals(getServiceContext().getUserId(), indicatorDtoCreated.getLastUpdatedBy());
    }

    @Test
    public void testCreateIndicatorCodeRequired() throws Exception {

        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode(null);
        indicatorDto.setName(IndicatorsMocks.mockInternationalString());

        try {
            indicatorsServiceFacade.createIndicator(getServiceContext(), indicatorDto);
            fail("code required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_VALIDATION_METADATA_REQUIRED.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("INDICATOR.CODE", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorNameRequired() throws Exception {

        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode(IndicatorsMocks.mockString(10));
        indicatorDto.setName(null);

        try {
            indicatorsServiceFacade.createIndicator(getServiceContext(), indicatorDto);
            fail("name required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_VALIDATION_METADATA_REQUIRED.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("INDICATOR.NAME", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorCodeDuplicated() throws Exception {

        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode("CoDe-1");
        indicatorDto.setName(IndicatorsMocks.mockInternationalString());

        try {
            indicatorsServiceFacade.createIndicator(getServiceContext(), indicatorDto);
            fail("code duplicated");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INDICATOR_ALREADY_EXIST_CODE_DUPLICATED.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(indicatorDto.getCode(), e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorCodeDuplicatedInsensitive() throws Exception {

        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode("CoDe-1");
        indicatorDto.setName(IndicatorsMocks.mockInternationalString());

        try {
            indicatorsServiceFacade.createIndicator(getServiceContext(), indicatorDto);
            fail("code duplicated");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SERVICE_INDICATOR_ALREADY_EXIST_CODE_DUPLICATED.getErrorCode(), e.getExceptionItems().get(0).getErrorCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(indicatorDto.getCode(), e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/IndicatorsServiceFacadeTest.xml";
    }
}
