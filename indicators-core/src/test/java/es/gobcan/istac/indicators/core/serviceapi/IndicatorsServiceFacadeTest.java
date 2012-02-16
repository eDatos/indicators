package es.gobcan.istac.indicators.core.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.gobcan.istac.indicators.core.dto.serviceapi.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.DataSourceVariableDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorStateEnum;
import es.gobcan.istac.indicators.core.enume.domain.VersiontTypeEnum;
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

    private static String             NOT_EXISTS                   = "not-exists";

    // Indicators
    private static String             INDICATOR_1                  = "Indicator-1";
    private static String             INDICATOR_2                  = "Indicator-2";
    private static String             INDICATOR_3                  = "Indicator-3";
    private static String             INDICATOR_3_VERSION          = "11.033";
    private static String             INDICATOR_4                  = "Indicator-4";
    private static String             INDICATOR_5                  = "Indicator-5";
    private static String             INDICATOR_6                  = "Indicator-6";
    private static String             INDICATOR_7                  = "Indicator-7";
    private static String             INDICATOR_8                  = "Indicator-8";
    private static String             INDICATOR_9                  = "Indicator-9";

    // Data sources
    private static String             DATA_SOURCE_1_INDICATOR_1_V1 = "Indicator-1-v1-DataSource-1";
    private static String             DATA_SOURCE_1_INDICATOR_1_V2 = "Indicator-1-v2-DataSource-1";
    private static String             DATA_SOURCE_2_INDICATOR_1_V2 = "Indicator-1-v2-DataSource-2";
    private static String             DATA_SOURCE_1_INDICATOR_3    = "Indicator-3-v1-DataSource-1";

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
            assertEquals(ServiceExceptionType.PARAMETER_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
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
            assertEquals(ServiceExceptionType.INDICATOR_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
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
            assertEquals(ServiceExceptionType.INDICATOR_VERSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
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
            assertEquals(ServiceExceptionType.INDICATOR_WRONG_STATE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorStateEnum.PUBLISHED, ((IndicatorStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
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
            assertEquals(ServiceExceptionType.PARAMETER_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
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
            assertEquals(ServiceExceptionType.INDICATOR_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
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

        IndicatorDto indicatorDtoRetrieved = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), indicatorDtoCreated.getUuid(), indicatorDtoCreated.getVersionNumber());
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
            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
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
            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
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
            assertEquals(ServiceExceptionType.INDICATOR_ALREADY_EXIST_CODE_DUPLICATED.getCode(), e.getExceptionItems().get(0).getCode());
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
            assertEquals(ServiceExceptionType.INDICATOR_ALREADY_EXIST_CODE_DUPLICATED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(indicatorDto.getCode(), e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testDeleteIndicator() throws Exception {

        String uuid = INDICATOR_2;

        // Delete indicator only in draft
        indicatorsServiceFacade.deleteIndicator(getServiceContext(), uuid);

        // Validation
        try {
            indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, null);
            fail("Indicator deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testDeleteIndicatorCheckDeleteDataSources() throws Exception {

        String uuid = INDICATOR_1;
        String uuidDataSource1 = DATA_SOURCE_1_INDICATOR_1_V2;
        String uuidDataSource2 = DATA_SOURCE_2_INDICATOR_1_V2;
        List<DataSourceDto> datasources = indicatorsServiceFacade.findDataSources(getServiceContext(), uuid, "2.000");
        assertEquals(2, datasources.size());
        assertEquals(uuidDataSource1, datasources.get(0).getUuid());
        assertEquals(uuidDataSource2, datasources.get(1).getUuid());

        // Delete indicator
        indicatorsServiceFacade.deleteIndicator(getServiceContext(), uuid);

        // Validate data sources are deleted
        try {
            indicatorsServiceFacade.retrieveDataSource(getServiceContext(), uuidDataSource1);
            fail("Datasource deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DATA_SOURCE_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuidDataSource1, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
        try {
            indicatorsServiceFacade.retrieveDataSource(getServiceContext(), uuidDataSource2);
            fail("Datasource deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DATA_SOURCE_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuidDataSource2, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testDeleteIndicatorWithPublishedAndDraft() throws Exception {

        String uuid = INDICATOR_1;

        // Retrieve: version 1 is diffusion; version 2 is in production
        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, "1.000");
            assertEquals(uuid, indicatorDto.getUuid());
            assertEquals("1.000", indicatorDto.getVersionNumber());
            assertEquals("1.000", indicatorDto.getDiffusionVersion());
            assertEquals("2.000", indicatorDto.getProductionVersion());
        }

        // Delete indicator
        indicatorsServiceFacade.deleteIndicator(getServiceContext(), uuid);

        // Validation
        // Version 1 exists
        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, "1.000");
            assertEquals(uuid, indicatorDto.getUuid());
            assertEquals("1.000", indicatorDto.getVersionNumber());
            assertEquals("1.000", indicatorDto.getDiffusionVersion());
            assertEquals(null, indicatorDto.getProductionVersion());
        }
        // Version 2 not exists
        try {
            indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, "2.000");
            fail("Indicator version deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_VERSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals("2.000", e.getExceptionItems().get(0).getMessageParameters()[1]);
        }
    }

    @Test
    public void testDeleteIndicatorErrorInDiffusion() throws Exception {

        String uuid = INDICATOR_3;

        // Validation
        try {
            indicatorsServiceFacade.deleteIndicator(getServiceContext(), uuid);
            fail("Indicator is not in draft");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_IN_PRODUCTION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testDeleteIndicatorErrorNotExists() throws Exception {

        String uuid = NOT_EXISTS;

        // Validation
        try {
            indicatorsServiceFacade.deleteIndicator(getServiceContext(), uuid);
            fail("DataSource not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateIndicator() throws Exception {

        String uuid = INDICATOR_1;
        String versionNumber = "2.000";

        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumber);
        assertEquals(IndicatorStateEnum.DRAFT, indicatorDto.getState());

        indicatorDto.setName(IndicatorsMocks.mockInternationalString());
        indicatorDto.setAcronym(IndicatorsMocks.mockInternationalString());
        indicatorDto.setNoteUrl("aa");

        // Update
        indicatorsServiceFacade.updateIndicator(getServiceContext(), indicatorDto);

        // Validation
        IndicatorDto indicatorDtoUpdated = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumber);
        IndicatorsAsserts.assertEqualsIndicator(indicatorDto, indicatorDtoUpdated);
        assertTrue(indicatorDtoUpdated.getLastUpdated().after(indicatorDtoUpdated.getCreatedDate()));
    }

    @Test
    public void testUpdateIndicatorInRejectedValidation() throws Exception {

        String uuid = INDICATOR_9;
        String versionNumber = "1.000";

        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumber);
        assertEquals(IndicatorStateEnum.VALIDATION_REJECTED, indicatorDto.getState());

        indicatorDto.setName(IndicatorsMocks.mockInternationalString());

        // Update
        indicatorsServiceFacade.updateIndicator(getServiceContext(), indicatorDto);

        // Validation
        IndicatorDto indicatorDtoUpdated = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumber);
        assertEquals(IndicatorStateEnum.VALIDATION_REJECTED, indicatorDtoUpdated.getState());
        IndicatorsAsserts.assertEqualsIndicator(indicatorDto, indicatorDtoUpdated);
    }

    @Test
    public void testUpdateIndicatorInProductionValidation() throws Exception {

        String uuid = INDICATOR_4;
        String versionNumber = "1.000";

        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumber);
        assertEquals(IndicatorStateEnum.PRODUCTION_VALIDATION, indicatorDto.getState());

        indicatorDto.setName(IndicatorsMocks.mockInternationalString());

        // Update
        indicatorsServiceFacade.updateIndicator(getServiceContext(), indicatorDto);

        // Validation
        IndicatorDto indicatorDtoUpdated = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumber);
        assertEquals(IndicatorStateEnum.PRODUCTION_VALIDATION, indicatorDtoUpdated.getState());
        IndicatorsAsserts.assertEqualsIndicator(indicatorDto, indicatorDtoUpdated);
    }

    @Test
    public void testUpdateIndicatorInDiffusionValidation() throws Exception {

        String uuid = INDICATOR_5;
        String versionNumber = "1.000";

        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumber);
        assertEquals(IndicatorStateEnum.DIFFUSION_VALIDATION, indicatorDto.getState());

        indicatorDto.setName(IndicatorsMocks.mockInternationalString());

        // Update
        indicatorsServiceFacade.updateIndicator(getServiceContext(), indicatorDto);

        // Validation
        IndicatorDto indicatorDtoUpdated = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumber);
        assertEquals(IndicatorStateEnum.DIFFUSION_VALIDATION, indicatorDtoUpdated.getState());
        IndicatorsAsserts.assertEqualsIndicator(indicatorDto, indicatorDtoUpdated);
    }

    @Test
    public void testUpdateIndicatorReusingLocalisedStrings() throws Exception {

        String uuid = INDICATOR_1;
        String versionNumber = "2.000";

        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumber);
        indicatorDto.getName().getTexts().iterator().next().setLabel("NewLabel");

        // Update
        indicatorsServiceFacade.updateIndicator(getServiceContext(), indicatorDto);

        // Validation
        IndicatorDto indicatorDtoUpdated = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumber);
        IndicatorsAsserts.assertEqualsIndicator(indicatorDto, indicatorDtoUpdated);
    }

    @Test
    public void testUpdateIndicatorErrorNotExists() throws Exception {

        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), INDICATOR_1, "2.000");
        indicatorDto.setUuid(NOT_EXISTS);

        try {
            indicatorsServiceFacade.updateIndicator(getServiceContext(), indicatorDto);
            fail("Indicator not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateIndicatorErrorNotInProduction() throws Exception {

        String uuid = INDICATOR_3;
        String versionNumber = INDICATOR_3_VERSION;

        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumber);

        try {
            indicatorsServiceFacade.updateIndicator(getServiceContext(), indicatorDto);
            fail("Indicator not in production");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_IN_PRODUCTION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateIndicatorErrorWrongVersion() throws Exception {

        String uuid = INDICATOR_1;
        String versionNumber = "1.000";

        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumber);

        try {
            indicatorsServiceFacade.updateIndicator(getServiceContext(), indicatorDto);
            fail("Version 1 not is in production");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_VERSION_WRONG_STATE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(versionNumber, e.getExceptionItems().get(0).getMessageParameters()[1]);
        }
    }

    @Test
    public void testUpdateIndicatorsErrorCodeNonModifiable() throws Exception {

        String uuid = INDICATOR_1;
        String versionNumber = "2.000";

        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumber);
        indicatorDto.setCode("newCode");

        // Update
        try {
            indicatorsServiceFacade.updateIndicator(getServiceContext(), indicatorDto);
            fail("Code is unmodifiable");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_UNMODIFIABLE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("INDICATOR.CODE", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Override
    @Test
    public void testSendIndicatorToProductionValidation() throws Exception {

        String uuid = INDICATOR_1;
        String diffusionVersion = "1.000";
        String productionVersion = "2.000";

        {
            IndicatorDto indicatorDtoV1 = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, diffusionVersion);
            IndicatorDto indicatorDtoV2 = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, productionVersion);
            assertEquals(diffusionVersion, indicatorDtoV1.getDiffusionVersion());
            assertEquals(productionVersion, indicatorDtoV2.getProductionVersion());
            assertEquals(IndicatorStateEnum.PUBLISHED, indicatorDtoV1.getState());
            assertEquals(IndicatorStateEnum.DRAFT, indicatorDtoV2.getState());
        }

        // Sends to production validation
        indicatorsServiceFacade.sendIndicatorToProductionValidation(getServiceContext2(), uuid);

        // Validation
        {
            IndicatorDto indicatorDtoV1 = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, diffusionVersion);
            IndicatorDto indicatorDtoV2 = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, productionVersion);
            assertEquals(diffusionVersion, indicatorDtoV1.getDiffusionVersion());
            assertEquals(productionVersion, indicatorDtoV2.getProductionVersion());
            assertEquals(IndicatorStateEnum.PUBLISHED, indicatorDtoV1.getState());
            assertEquals(IndicatorStateEnum.PRODUCTION_VALIDATION, indicatorDtoV2.getState());

            assertTrue(DateUtils.isSameDay(new Date(), indicatorDtoV2.getProductionValidationDate()));
            assertEquals(getServiceContext2().getUserId(), indicatorDtoV2.getProductionValidationUser());
            assertNull(indicatorDtoV2.getDiffusionValidationDate());
            assertNull(indicatorDtoV2.getDiffusionValidationUser());
            assertNull(indicatorDtoV2.getPublicationDate());
            assertNull(indicatorDtoV2.getPublicationUser());
            assertNull(indicatorDtoV2.getArchiveDate());
            assertNull(indicatorDtoV2.getArchiveUser());
        }
    }

    @Test
    public void testSendIndicatorToProductionValidationInStateRejected() throws Exception {

        String uuid = INDICATOR_9;
        String productionVersion = "1.000";

        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, productionVersion);
            assertEquals(productionVersion, indicatorDto.getProductionVersion());
            assertNull(indicatorDto.getDiffusionVersion());
            assertEquals(IndicatorStateEnum.VALIDATION_REJECTED, indicatorDto.getState());
        }

        // Sends to production validation
        indicatorsServiceFacade.sendIndicatorToProductionValidation(getServiceContext2(), uuid);

        // Validation
        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, productionVersion);
            assertEquals(productionVersion, indicatorDto.getProductionVersion());
            assertNull(indicatorDto.getDiffusionVersion());
            assertEquals(IndicatorStateEnum.PRODUCTION_VALIDATION, indicatorDto.getState());
        }
    }

    @Test
    public void testSendIndicatorToProductionValidationErrorNotExists() throws Exception {

        try {
            indicatorsServiceFacade.sendIndicatorToProductionValidation(getServiceContext(), NOT_EXISTS);
            fail("Indicator not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testSendIndicatorToProductionValidationErrorWrongState() throws Exception {

        String uuid = INDICATOR_3;

        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, INDICATOR_3_VERSION);
            assertEquals(IndicatorStateEnum.PUBLISHED, indicatorDto.getState());
            assertNull(indicatorDto.getProductionVersion());
        }

        try {
            indicatorsServiceFacade.sendIndicatorToProductionValidation(getServiceContext(), uuid);
            fail("Indicator is not draft");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_WRONG_STATE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorStateEnum.DRAFT, ((IndicatorStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(IndicatorStateEnum.VALIDATION_REJECTED, ((IndicatorStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);

        }
    }

    @Test
    public void testSendIndicatorToProductionValidationErrorWithoutDataSources() throws Exception {

        String uuid = INDICATOR_2;

        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, "1.000");
            assertEquals(IndicatorStateEnum.DRAFT, indicatorDto.getState());
            
            // Check zero data sources
            List<DataSourceDto> dataSources = indicatorsServiceFacade.findDataSources(getServiceContext(), uuid, "1.000");
            assertEquals(0, dataSources.size());
        }

        try {
            indicatorsServiceFacade.sendIndicatorToProductionValidation(getServiceContext(), uuid);
            fail("Indicator hasn't data sources");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_MUST_HAVE_DATA_SOURCES.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    // @Override
    // @Test
    // public void testSendIndicatorToDiffusionValidation() throws Exception {
    //
    // String uuid = INDICATOR_4;
    // String versionNumber = "1.000";
    //
    // {
    // IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumber);
    // assertEquals(null, indicatorDto.getDiffusionVersion());
    // assertEquals("1.000", indicatorDto.getProductionVersion());
    // assertEquals(IndicatorStateEnum.PRODUCTION_VALIDATION, indicatorDto.getState());
    // }
    //
    // // Sends to diffusion validation
    // indicatorsServiceFacade.sendIndicatorToDiffusionValidation(getServiceContext(), uuid);
    //
    // // Validation
    // {
    // IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumber);
    // assertEquals(null, indicatorDto.getDiffusionVersion());
    // assertEquals("1.000", indicatorDto.getProductionVersion());
    // assertEquals(IndicatorStateEnum.DIFFUSION_VALIDATION, indicatorDto.getState());
    //
    // IndicatorsAsserts.assertEqualsDate("2011-04-04 01:02:04", indicatorDto.getProductionValidationDate());
    // assertEquals("user1", indicatorDto.getProductionValidationUser());
    // assertTrue(DateUtils.isSameDay(new Date(), indicatorDto.getDiffusionValidationDate()));
    // assertEquals(getServiceContext().getUserId(), indicatorDto.getDiffusionValidationUser());
    // assertNull(indicatorDto.getPublicationDate());
    // assertNull(indicatorDto.getPublicationUser());
    // assertNull(indicatorDto.getArchiveDate());
    // assertNull(indicatorDto.getArchiveUser());
    // }
    // }
    //
    // @Test
    // public void testSendIndicatorToDiffusionValidationErrorNotExists() throws Exception {
    //
    // try {
    // indicatorsServiceFacade.sendIndicatorToDiffusionValidation(getServiceContext(), NOT_EXISTS);
    // fail("Indicator not exists");
    // } catch (MetamacException e) {
    // assertEquals(1, e.getExceptionItems().size());
    // assertEquals(ServiceExceptionType.INDICATOR_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
    // assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
    // assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
    // }
    // }
    //
    // @Test
    // public void testSendIndicatorToDiffusionValidationErrorWrongStateDraft() throws Exception {
    //
    // String uuid = INDICATOR_2;
    //
    // {
    // IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, "1.000");
    // assertEquals(IndicatorStateEnum.DRAFT, indicatorDto.getState());
    // assertEquals("1.000", indicatorDto.getProductionVersion());
    // assertEquals(null, indicatorDto.getDiffusionVersion());
    // }
    //
    // try {
    // indicatorsServiceFacade.sendIndicatorToDiffusionValidation(getServiceContext(), uuid);
    // fail("Indicator is not draft");
    // } catch (MetamacException e) {
    // assertEquals(1, e.getExceptionItems().size());
    // assertEquals(ServiceExceptionType.INDICATOR_WRONG_STATE.getCode(), e.getExceptionItems().get(0).getCode());
    // assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
    // assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
    // assertEquals(IndicatorStateEnum.PRODUCTION_VALIDATION, e.getExceptionItems().get(0).getMessageParameters()[1]);
    // }
    // }
    //
    // @Test
    // public void testSendIndicatorToDiffusionValidationErrorWrongStatePublished() throws Exception {
    //
    // String uuid = INDICATOR_3;
    //
    // {
    // IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, INDICATOR_3_VERSION);
    // assertEquals(IndicatorStateEnum.PUBLISHED, indicatorDto.getState());
    // assertEquals(null, indicatorDto.getProductionVersion());
    // assertEquals(INDICATOR_3_VERSION, indicatorDto.getDiffusionVersion());
    // }
    //
    // try {
    // indicatorsServiceFacade.sendIndicatorToDiffusionValidation(getServiceContext(), uuid);
    // fail("Indicator is not production validation");
    // } catch (MetamacException e) {
    // assertEquals(1, e.getExceptionItems().size());
    // assertEquals(ServiceExceptionType.INDICATOR_WRONG_STATE.getCode(), e.getExceptionItems().get(0).getCode());
    // assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
    // assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
    // assertEquals(IndicatorStateEnum.PRODUCTION_VALIDATION, e.getExceptionItems().get(0).getMessageParameters()[1]);
    // }
    // }
    //
    // @Override
    // @Test
    // public void testRejectIndicatorValidation() throws Exception {
    //
    // String uuid = INDICATOR_4;
    // String versionNumber = "1.000";
    //
    // {
    // IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumber);
    // assertEquals(null, indicatorDto.getDiffusionVersion());
    // assertEquals("1.000", indicatorDto.getProductionVersion());
    // assertEquals(IndicatorStateEnum.PRODUCTION_VALIDATION, indicatorDto.getState());
    // }
    //
    // // Rejects validation
    // indicatorsServiceFacade.rejectIndicatorValidation(getServiceContext(), uuid);
    //
    // // Validation
    // {
    // IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumber);
    // assertEquals(null, indicatorDto.getDiffusionVersion());
    // assertEquals("1.000", indicatorDto.getProductionVersion());
    // assertEquals(IndicatorStateEnum.VALIDATION_REJECTED, indicatorDto.getState());
    //
    // assertNull(indicatorDto.getProductionValidationDate());
    // assertNull(indicatorDto.getProductionValidationUser());
    // assertNull(indicatorDto.getDiffusionValidationDate());
    // assertNull(indicatorDto.getDiffusionValidationUser());
    // assertNull(indicatorDto.getPublicationDate());
    // assertNull(indicatorDto.getPublicationUser());
    // assertNull(indicatorDto.getArchiveDate());
    // assertNull(indicatorDto.getArchiveUser());
    // }
    // }
    //
    // @Test
    // public void testRejectIndicatorValidationInDiffusionValidation() throws Exception {
    //
    // String uuid = INDICATOR_5;
    // String versionNumber = "1.000";
    //
    // {
    // IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumber);
    // assertEquals(null, indicatorDto.getDiffusionVersion());
    // assertEquals("1.000", indicatorDto.getProductionVersion());
    // assertEquals(IndicatorStateEnum.DIFFUSION_VALIDATION, indicatorDto.getState());
    // }
    //
    // // Rejects validation
    // indicatorsServiceFacade.rejectIndicatorValidation(getServiceContext(), uuid);
    //
    // // Validation
    // {
    // IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumber);
    // assertEquals(null, indicatorDto.getDiffusionVersion());
    // assertEquals("1.000", indicatorDto.getProductionVersion());
    // assertEquals(IndicatorStateEnum.VALIDATION_REJECTED, indicatorDto.getState());
    // }
    // }
    //
    // @Test
    // public void testRejectIndicatorValidationErrorNotExists() throws Exception {
    //
    // try {
    // indicatorsServiceFacade.rejectIndicatorValidation(getServiceContext(), NOT_EXISTS);
    // fail("Indicator not exists");
    // } catch (MetamacException e) {
    // assertEquals(1, e.getExceptionItems().size());
    // assertEquals(ServiceExceptionType.INDICATOR_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
    // assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
    // assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
    // }
    // }
    //
    // @Test
    // public void testRejectIndicatorValidationErrorWrongStateProduction() throws Exception {
    //
    // String uuid = INDICATOR_2;
    //
    // {
    // IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, "1.000");
    // assertEquals(IndicatorStateEnum.DRAFT, indicatorDto.getState());
    // assertEquals("1.000", indicatorDto.getProductionVersion());
    // assertEquals(null, indicatorDto.getDiffusionVersion());
    // }
    //
    // try {
    // indicatorsServiceFacade.rejectIndicatorValidation(getServiceContext(), uuid);
    // fail("Indicator is not in validation");
    // } catch (MetamacException e) {
    // assertEquals(1, e.getExceptionItems().size());
    // assertEquals(ServiceExceptionType.INDICATOR_WRONG_STATE.getCode(), e.getExceptionItems().get(0).getCode());
    // assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
    // assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
    // assertEquals(IndicatorStateEnum.PRODUCTION_VALIDATION, ((IndicatorStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
    // assertEquals(IndicatorStateEnum.DIFFUSION_VALIDATION, ((IndicatorStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
    // }
    // }
    //
    // @Test
    // public void testRejectIndicatorValidationErrorWrongStateDiffusion() throws Exception {
    //
    // String uuid = INDICATOR_3;
    //
    // {
    // IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, INDICATOR_3_VERSION);
    // assertEquals(IndicatorStateEnum.PUBLISHED, indicatorDto.getState());
    // assertEquals(null, indicatorDto.getProductionVersion());
    // assertEquals(INDICATOR_3_VERSION, indicatorDto.getDiffusionVersion());
    // }
    //
    // try {
    // indicatorsServiceFacade.rejectIndicatorValidation(getServiceContext(), uuid);
    // fail("Indicator is not in validation");
    // } catch (MetamacException e) {
    // assertEquals(1, e.getExceptionItems().size());
    // assertEquals(ServiceExceptionType.INDICATOR_WRONG_STATE.getCode(), e.getExceptionItems().get(0).getCode());
    // assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
    // assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
    // assertEquals(IndicatorStateEnum.PRODUCTION_VALIDATION, ((IndicatorStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
    // assertEquals(IndicatorStateEnum.DIFFUSION_VALIDATION, ((IndicatorStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
    // }
    // }
    //
    // @Override
    // @Test
    // public void testPublishIndicator() throws Exception {
    //
    // String uuid = INDICATOR_5;
    // String versionNumber = "1.000";
    //
    // {
    // IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumber);
    // assertEquals(versionNumber, indicatorDto.getProductionVersion());
    // assertEquals(null, indicatorDto.getDiffusionVersion());
    // assertEquals(IndicatorStateEnum.DIFFUSION_VALIDATION, indicatorDto.getState());
    // }
    //
    // // Publish
    // indicatorsServiceFacade.publishIndicator(getServiceContext(), uuid);
    //
    // // Validation
    // {
    // IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumber);
    // assertEquals(null, indicatorDto.getProductionVersion());
    // assertEquals(versionNumber, indicatorDto.getDiffusionVersion());
    // assertEquals(IndicatorStateEnum.PUBLISHED, indicatorDto.getState());
    //
    // IndicatorsAsserts.assertEqualsDate("2011-06-06 01:02:04", indicatorDto.getProductionValidationDate());
    // assertEquals("user1", indicatorDto.getProductionValidationUser());
    // IndicatorsAsserts.assertEqualsDate("2011-07-07 03:02:04", indicatorDto.getDiffusionValidationDate());
    // assertEquals("user2", indicatorDto.getDiffusionValidationUser());
    // assertTrue(DateUtils.isSameDay(new Date(), indicatorDto.getPublicationDate()));
    // assertEquals(getServiceContext().getUserId(), indicatorDto.getPublicationUser());
    // assertNull(indicatorDto.getArchiveDate());
    // assertNull(indicatorDto.getArchiveUser());
    // }
    // }
    //
    // @Test
    // public void testPublishIndicatorWithPublishedVersion() throws Exception {
    //
    // String uuid = INDICATOR_6;
    // String diffusionVersionBefore = "1.000"; // will be deleted when publish current version in diffusion validation
    // String productionVersionBefore = "2.000"; // will be published
    //
    // {
    // IndicatorDto indicatorDtoV1 = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, diffusionVersionBefore);
    // IndicatorDto indicatorDtoV2 = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, productionVersionBefore);
    // assertEquals(productionVersionBefore, indicatorDtoV1.getProductionVersion());
    // assertEquals(diffusionVersionBefore, indicatorDtoV1.getDiffusionVersion());
    // assertEquals(IndicatorStateEnum.PUBLISHED, indicatorDtoV1.getState());
    // assertEquals(IndicatorStateEnum.DIFFUSION_VALIDATION, indicatorDtoV2.getState());
    // }
    //
    // // Publish
    // indicatorsServiceFacade.publishIndicator(getServiceContext(), uuid);
    //
    // // Validation
    // {
    // // Version 1 already not exists
    // try {
    // indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, diffusionVersionBefore);
    // fail("Indicator version not exists");
    // } catch (MetamacException e) {
    // assertEquals(1, e.getExceptionItems().size());
    // assertEquals(ServiceExceptionType.INDICATOR_VERSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
    // assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
    // assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
    // assertEquals(diffusionVersionBefore, e.getExceptionItems().get(0).getMessageParameters()[1]);
    // }
    //
    // // Actual version in diffusion is version 2
    // IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, productionVersionBefore);
    // assertEquals(null, indicatorDto.getProductionVersion());
    // assertEquals("2.000", indicatorDto.getDiffusionVersion());
    // assertEquals(IndicatorStateEnum.PUBLISHED, indicatorDto.getState());
    // }
    // }
    //
    // @Test
    // public void testPublishIndicatorWithArchivedVersion() throws Exception {
    //
    // String uuid = INDICATOR_7;
    // String diffusionVersionBefore = "1.000"; // will be deleted when publish current version in diffusion validation
    // String productionVersionBefore = "2.000"; // will be published
    //
    // {
    // IndicatorDto indicatorDtoV1 = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, diffusionVersionBefore);
    // IndicatorDto indicatorDtoV2 = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, productionVersionBefore);
    // assertEquals(productionVersionBefore, indicatorDtoV1.getProductionVersion());
    // assertEquals(diffusionVersionBefore, indicatorDtoV1.getDiffusionVersion());
    // assertEquals(IndicatorStateEnum.ARCHIVED, indicatorDtoV1.getState());
    // assertEquals(IndicatorStateEnum.DIFFUSION_VALIDATION, indicatorDtoV2.getState());
    // }
    //
    // // Publish
    // indicatorsServiceFacade.publishIndicator(getServiceContext(), uuid);
    //
    // // Validation
    // {
    // // Version 1 already not exists
    // try {
    // indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, diffusionVersionBefore);
    // fail("Indicator version not exists");
    // } catch (MetamacException e) {
    // assertEquals(1, e.getExceptionItems().size());
    // assertEquals(ServiceExceptionType.INDICATOR_VERSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
    // assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
    // assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
    // assertEquals(diffusionVersionBefore, e.getExceptionItems().get(0).getMessageParameters()[1]);
    // }
    //
    // // Actual version in diffusion is version 2
    // IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, productionVersionBefore);
    // assertEquals(null, indicatorDto.getProductionVersion());
    // assertEquals("2.000", indicatorDto.getDiffusionVersion());
    // assertEquals(IndicatorStateEnum.PUBLISHED, indicatorDto.getState());
    // }
    // }
    //
    // @Test
    // public void testPublishIndicatorErrorNotExists() throws Exception {
    //
    // try {
    // indicatorsServiceFacade.publishIndicator(getServiceContext(), NOT_EXISTS);
    // fail("Indicator not exists");
    // } catch (MetamacException e) {
    // assertEquals(1, e.getExceptionItems().size());
    // assertEquals(ServiceExceptionType.INDICATOR_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
    // assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
    // assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
    // }
    // }
    //
    // @Test
    // public void testPublishIndicatorErrorWrongStateProduction() throws Exception {
    //
    // String uuid = INDICATOR_2;
    //
    // {
    // IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, "1.000");
    // assertEquals(IndicatorStateEnum.DRAFT, indicatorDto.getState());
    // assertEquals("1.000", indicatorDto.getProductionVersion());
    // assertEquals(null, indicatorDto.getDiffusionVersion());
    // }
    //
    // try {
    // indicatorsServiceFacade.publishIndicator(getServiceContext(), uuid);
    // fail("Indicator is not diffusion validation");
    // } catch (MetamacException e) {
    // assertEquals(1, e.getExceptionItems().size());
    // assertEquals(ServiceExceptionType.INDICATOR_WRONG_STATE.getCode(), e.getExceptionItems().get(0).getCode());
    // assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
    // assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
    // assertEquals(IndicatorStateEnum.DIFFUSION_VALIDATION, e.getExceptionItems().get(0).getMessageParameters()[1]);
    // }
    // }
    //
    // @Test
    // public void testPublishIndicatorErrorWrongStateDiffusion() throws Exception {
    //
    // String uuid = INDICATOR_3;
    //
    // {
    // IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, INDICATOR_3_VERSION);
    // assertEquals(IndicatorStateEnum.PUBLISHED, indicatorDto.getState());
    // assertEquals(null, indicatorDto.getProductionVersion());
    // assertEquals(INDICATOR_3_VERSION, indicatorDto.getDiffusionVersion());
    // }
    //
    // try {
    // indicatorsServiceFacade.publishIndicator(getServiceContext(), uuid);
    // fail("Indicator is not diffusion validation");
    // } catch (MetamacException e) {
    // assertEquals(1, e.getExceptionItems().size());
    // assertEquals(ServiceExceptionType.INDICATOR_WRONG_STATE.getCode(), e.getExceptionItems().get(0).getCode());
    // assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
    // assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
    // assertEquals(IndicatorStateEnum.DIFFUSION_VALIDATION, e.getExceptionItems().get(0).getMessageParameters()[1]);
    // }
    // }
    //
    // @Override
    // @Test
    // public void testArchiveIndicator() throws Exception {
    //
    // String uuid = INDICATOR_3;
    // String versionNumber = INDICATOR_3_VERSION;
    //
    // {
    // IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumber);
    // assertEquals(null, indicatorDto.getProductionVersion());
    // assertEquals(INDICATOR_3_VERSION, indicatorDto.getDiffusionVersion());
    // assertEquals(IndicatorStateEnum.PUBLISHED, indicatorDto.getState());
    // }
    //
    // // Archive
    // indicatorsServiceFacade.archiveIndicator(getServiceContext(), uuid);
    //
    // // Validation
    // {
    // IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumber);
    // assertEquals(null, indicatorDto.getProductionVersion());
    // assertEquals(INDICATOR_3_VERSION, indicatorDto.getDiffusionVersion());
    // assertEquals(IndicatorStateEnum.ARCHIVED, indicatorDto.getState());
    //
    // IndicatorsAsserts.assertEqualsDate("2011-03-03 01:02:04", indicatorDto.getProductionValidationDate());
    // assertEquals("user1", indicatorDto.getProductionValidationUser());
    // IndicatorsAsserts.assertEqualsDate("2011-04-04 03:02:04", indicatorDto.getDiffusionValidationDate());
    // assertEquals("user2", indicatorDto.getDiffusionValidationUser());
    // IndicatorsAsserts.assertEqualsDate("2011-05-05 04:02:04", indicatorDto.getPublicationDate());
    // assertEquals("user3", indicatorDto.getPublicationUser());
    // assertTrue(DateUtils.isSameDay(new Date(), indicatorDto.getArchiveDate()));
    // assertEquals(getServiceContext().getUserId(), indicatorDto.getArchiveUser());
    // }
    // }
    //
    // @Test
    // public void testArchiveIndicatorWithProductionVersion() throws Exception {
    //
    // String uuid = INDICATOR_1;
    // String diffusionVersion = "1.000";
    // String productionVersion = "2.000";
    //
    // {
    // IndicatorDto indicatorDtoV1 = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, diffusionVersion);
    // IndicatorDto indicatorDtoV2 = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, productionVersion);
    // assertEquals(productionVersion, indicatorDtoV1.getProductionVersion());
    // assertEquals(diffusionVersion, indicatorDtoV1.getDiffusionVersion());
    // assertEquals(IndicatorStateEnum.PUBLISHED, indicatorDtoV1.getState());
    // assertEquals(IndicatorStateEnum.DRAFT, indicatorDtoV2.getState());
    // }
    //
    // // Archive
    // indicatorsServiceFacade.archiveIndicator(getServiceContext(), uuid);
    //
    // // Validation
    // {
    // IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, diffusionVersion);
    // assertEquals(productionVersion, indicatorDto.getProductionVersion());
    // assertEquals(diffusionVersion, indicatorDto.getDiffusionVersion());
    // assertEquals(IndicatorStateEnum.ARCHIVED, indicatorDto.getState());
    // }
    // }
    //
    // @Test
    // public void testArchiveIndicatorErrorNotExists() throws Exception {
    //
    // try {
    // indicatorsServiceFacade.archiveIndicator(getServiceContext(), NOT_EXISTS);
    // fail("Indicator not exists");
    // } catch (MetamacException e) {
    // assertEquals(1, e.getExceptionItems().size());
    // assertEquals(ServiceExceptionType.INDICATOR_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
    // assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
    // assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
    // }
    // }
    //
    // @Test
    // public void testArchiveIndicatorErrorWrongStateProduction() throws Exception {
    //
    // String uuid = INDICATOR_2;
    //
    // {
    // IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, "1.000");
    // assertEquals(IndicatorStateEnum.DRAFT, indicatorDto.getState());
    // assertEquals("1.000", indicatorDto.getProductionVersion());
    // assertEquals(null, indicatorDto.getDiffusionVersion());
    // }
    //
    // try {
    // indicatorsServiceFacade.archiveIndicator(getServiceContext(), uuid);
    // fail("Indicator is not published");
    // } catch (MetamacException e) {
    // assertEquals(1, e.getExceptionItems().size());
    // assertEquals(ServiceExceptionType.INDICATOR_WRONG_STATE.getCode(), e.getExceptionItems().get(0).getCode());
    // assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
    // assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
    // assertEquals(IndicatorStateEnum.PUBLISHED, e.getExceptionItems().get(0).getMessageParameters()[1]);
    // }
    // }
    //
    // @Test
    // public void testArchiveIndicatorErrorWrongStateDiffusion() throws Exception {
    //
    // String uuid = INDICATOR_8;
    //
    // {
    // IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, "1.000");
    // assertEquals(IndicatorStateEnum.ARCHIVED, indicatorDto.getState());
    // assertEquals(null, indicatorDto.getProductionVersion());
    // assertEquals("1.000", indicatorDto.getDiffusionVersion());
    // }
    //
    // try {
    // indicatorsServiceFacade.archiveIndicator(getServiceContext(), uuid);
    // fail("Indicator is not published");
    // } catch (MetamacException e) {
    // assertEquals(1, e.getExceptionItems().size());
    // assertEquals(ServiceExceptionType.INDICATOR_WRONG_STATE.getCode(), e.getExceptionItems().get(0).getCode());
    // assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
    // assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
    // assertEquals(IndicatorStateEnum.PUBLISHED, e.getExceptionItems().get(0).getMessageParameters()[1]);
    // }
    // }

    @Override
    @Test
    public void testVersioningIndicator() throws Exception {

        String uuid = INDICATOR_3;
        String newVersionExpected = "12.000";

        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, null);
        assertEquals(INDICATOR_3_VERSION, indicatorDto.getVersionNumber());
        assertEquals(null, indicatorDto.getProductionVersion());
        assertEquals(INDICATOR_3_VERSION, indicatorDto.getDiffusionVersion());

        IndicatorDto indicatorDtoVersioned = indicatorsServiceFacade.versioningIndicator(getServiceContext(), uuid, VersiontTypeEnum.MAJOR);

        // Validate
        assertEquals(newVersionExpected, indicatorDtoVersioned.getVersionNumber());
        assertEquals(newVersionExpected, indicatorDtoVersioned.getProductionVersion());
        assertEquals(INDICATOR_3_VERSION, indicatorDtoVersioned.getDiffusionVersion());
        IndicatorsAsserts.assertEqualsIndicator(indicatorDto, indicatorDtoVersioned);

        {
            IndicatorDto indicatorDtoProduction = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, newVersionExpected);
            IndicatorDto indicatorDtoDiffusion = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, INDICATOR_3_VERSION);

            IndicatorsAsserts.assertEqualsIndicator(indicatorDtoDiffusion, indicatorDtoProduction);

            assertEquals(IndicatorStateEnum.DRAFT, indicatorDtoProduction.getState());
            assertEquals(newVersionExpected, indicatorDtoProduction.getVersionNumber());
            assertEquals(newVersionExpected, indicatorDtoProduction.getProductionVersion());
            assertEquals(INDICATOR_3_VERSION, indicatorDtoProduction.getDiffusionVersion());
            // Data sources
            List<DataSourceDto> dataSources = indicatorsServiceFacade.findDataSources(getServiceContext(), indicatorDtoProduction.getUuid(), indicatorDtoProduction.getProductionVersion());
            assertEquals(1, dataSources.size());
            assertEquals("query-gpe Indicator-3-v1-DataSource-1", dataSources.get(0).getQueryGpe());
            assertEquals("px Indicator-3-v1-DataSource-1", dataSources.get(0).getPx());
            assertEquals("temporary v Indicator-3-v1-DataSource-1", dataSources.get(0).getTemporaryVariable());
            assertEquals("geographic v Indicator-3-v1-DataSource-1", dataSources.get(0).getGeographicVariable());
            assertEquals(1, dataSources.get(0).getOtherVariables().size());
            assertEquals("variable Indicator-3-v1-DataSource-1-Var-1", dataSources.get(0).getOtherVariables().get(0).getVariable());
            assertEquals("category Indicator-3-v1-DataSource-1-Var-1", dataSources.get(0).getOtherVariables().get(0).getCategory());

            assertEquals(IndicatorStateEnum.PUBLISHED, indicatorDtoDiffusion.getState());
            assertEquals(INDICATOR_3_VERSION, indicatorDtoDiffusion.getVersionNumber());
            assertEquals(newVersionExpected, indicatorDtoDiffusion.getProductionVersion());
            assertEquals(INDICATOR_3_VERSION, indicatorDtoDiffusion.getDiffusionVersion());
        }
    }

    @Test
    public void testVersioningIndicatorVersionMinor() throws Exception {

        String uuid = INDICATOR_3;
        String newVersionExpected = "11.034";

        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, null);
        assertEquals(INDICATOR_3_VERSION, indicatorDto.getVersionNumber());
        assertEquals(null, indicatorDto.getProductionVersion());
        assertEquals(INDICATOR_3_VERSION, indicatorDto.getDiffusionVersion());

        IndicatorDto indicatorDtoVersioned = indicatorsServiceFacade.versioningIndicator(getServiceContext(), uuid, VersiontTypeEnum.MINOR);

        // Validate
        assertEquals(newVersionExpected, indicatorDtoVersioned.getVersionNumber());
        assertEquals(newVersionExpected, indicatorDtoVersioned.getProductionVersion());
        assertEquals(INDICATOR_3_VERSION, indicatorDtoVersioned.getDiffusionVersion());
        IndicatorsAsserts.assertEqualsIndicator(indicatorDto, indicatorDtoVersioned);

        {
            IndicatorDto indicatorDtoProduction = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, newVersionExpected);
            IndicatorDto indicatorDtoDiffusion = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, INDICATOR_3_VERSION);

            IndicatorsAsserts.assertEqualsIndicator(indicatorDtoDiffusion, indicatorDtoProduction);

            assertEquals(IndicatorStateEnum.DRAFT, indicatorDtoProduction.getState());
            assertEquals(newVersionExpected, indicatorDtoProduction.getVersionNumber());
            assertEquals(newVersionExpected, indicatorDtoProduction.getProductionVersion());
            assertEquals(INDICATOR_3_VERSION, indicatorDtoProduction.getDiffusionVersion());

            assertEquals(IndicatorStateEnum.PUBLISHED, indicatorDtoDiffusion.getState());
            assertEquals(INDICATOR_3_VERSION, indicatorDtoDiffusion.getVersionNumber());
            assertEquals(newVersionExpected, indicatorDtoDiffusion.getProductionVersion());
            assertEquals(INDICATOR_3_VERSION, indicatorDtoDiffusion.getDiffusionVersion());
        }
    }

    @Test
    public void testVersioningIndicatorErrorNotExists() throws Exception {

        try {
            indicatorsServiceFacade.versioningIndicator(getServiceContext(), NOT_EXISTS, VersiontTypeEnum.MINOR);
            fail("Indicator not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testVersioningIndicatorErrorAlreadyExistsProduction() throws Exception {

        String uuid = INDICATOR_2;

        try {
            indicatorsServiceFacade.versioningIndicator(getServiceContext(), uuid, VersiontTypeEnum.MINOR);
            fail("Indicator already exists in production");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_WRONG_STATE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorStateEnum.PUBLISHED, ((IndicatorStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(IndicatorStateEnum.ARCHIVED, ((IndicatorStateEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
        }
    }

    @Override
    @Test
    public void testFindIndicators() throws Exception {

        // Retrieve last versions...
        List<IndicatorDto> indicatorsDto = indicatorsServiceFacade.findIndicators(getServiceContext());
        assertEquals(9, indicatorsDto.size());

        assertEquals(INDICATOR_1, indicatorsDto.get(0).getUuid());
        assertEquals(IndicatorStateEnum.DRAFT, indicatorsDto.get(0).getState());

        assertEquals(INDICATOR_2, indicatorsDto.get(1).getUuid());
        assertEquals(IndicatorStateEnum.DRAFT, indicatorsDto.get(1).getState());

        assertEquals(INDICATOR_3, indicatorsDto.get(2).getUuid());
        assertEquals(IndicatorStateEnum.PUBLISHED, indicatorsDto.get(2).getState());

        assertEquals(INDICATOR_4, indicatorsDto.get(3).getUuid());
        assertEquals(IndicatorStateEnum.PRODUCTION_VALIDATION, indicatorsDto.get(3).getState());

        assertEquals(INDICATOR_5, indicatorsDto.get(4).getUuid());
        assertEquals(IndicatorStateEnum.DIFFUSION_VALIDATION, indicatorsDto.get(4).getState());

        assertEquals(INDICATOR_6, indicatorsDto.get(5).getUuid());
        assertEquals(IndicatorStateEnum.DIFFUSION_VALIDATION, indicatorsDto.get(5).getState());

        assertEquals(INDICATOR_7, indicatorsDto.get(6).getUuid());
        assertEquals(IndicatorStateEnum.DIFFUSION_VALIDATION, indicatorsDto.get(6).getState());

        assertEquals(INDICATOR_8, indicatorsDto.get(7).getUuid());
        assertEquals(IndicatorStateEnum.ARCHIVED, indicatorsDto.get(7).getState());

        assertEquals(INDICATOR_9, indicatorsDto.get(8).getUuid());
        assertEquals(IndicatorStateEnum.VALIDATION_REJECTED, indicatorsDto.get(8).getState());
    }

    @Override
    @Test
    public void testFindIndicatorsPublished() throws Exception {

        List<IndicatorDto> indicatorsDto = indicatorsServiceFacade.findIndicatorsPublished(getServiceContext());
        assertEquals(3, indicatorsDto.size());

        assertEquals(INDICATOR_1, indicatorsDto.get(0).getUuid());
        assertEquals(IndicatorStateEnum.PUBLISHED, indicatorsDto.get(0).getState());

        assertEquals(INDICATOR_3, indicatorsDto.get(1).getUuid());
        assertEquals(IndicatorStateEnum.PUBLISHED, indicatorsDto.get(1).getState());

        assertEquals(INDICATOR_6, indicatorsDto.get(2).getUuid());
        assertEquals(IndicatorStateEnum.PUBLISHED, indicatorsDto.get(2).getState());
    }

    @Override
    @Test
    public void testRetrieveDataSource() throws Exception {

        DataSourceDto dataSourceDto = indicatorsServiceFacade.retrieveDataSource(getServiceContext(), DATA_SOURCE_1_INDICATOR_1_V2);

        assertNotNull(dataSourceDto);
        assertEquals(DATA_SOURCE_1_INDICATOR_1_V2, dataSourceDto.getUuid());
        assertEquals("query-gpe Indicator-1-v2-DataSource-1", dataSourceDto.getQueryGpe());
        assertEquals("px Indicator-1-v2-DataSource-1", dataSourceDto.getPx());
        assertEquals("temporary v Indicator-1-v2-DataSource-1", dataSourceDto.getTemporaryVariable());
        assertEquals("geographic v Indicator-1-v2-DataSource-1", dataSourceDto.getGeographicVariable());

        assertEquals(2, dataSourceDto.getOtherVariables().size());
        assertEquals("variable Indicator-1-v2-DataSource-1-Variable-1", dataSourceDto.getOtherVariables().get(0).getVariable());
        assertEquals("category Indicator-1-v2-DataSource-1-Variable-1", dataSourceDto.getOtherVariables().get(0).getCategory());
        assertEquals("variable Indicator-1-v2-DataSource-1-Variable-2", dataSourceDto.getOtherVariables().get(1).getVariable());
        assertEquals("category Indicator-1-v2-DataSource-1-Variable-2", dataSourceDto.getOtherVariables().get(1).getCategory());

        IndicatorsAsserts.assertEqualsDate("2011-03-03 01:02:04", dataSourceDto.getCreatedDate());
        assertEquals("user1", dataSourceDto.getCreatedBy());
        IndicatorsAsserts.assertEqualsDate("2011-03-04 05:06:07", dataSourceDto.getLastUpdated());
        assertEquals("user2", dataSourceDto.getLastUpdatedBy());
    }

    @Test
    public void testRetrieveDataSourceErrorParameterRequired() throws Exception {

        String uuid = null;

        try {
            indicatorsServiceFacade.retrieveDataSource(getServiceContext(), uuid);
            fail("parameter required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("UUID", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRetrieveDataSourceErrorNotExists() throws Exception {

        String uuid = NOT_EXISTS;

        try {
            indicatorsServiceFacade.retrieveDataSource(getServiceContext(), uuid);
            fail("No exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DATA_SOURCE_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Override
    @Test
    public void testCreateDataSource() throws Exception {

        // Create dataSource
        DataSourceDto dataSourceDto = new DataSourceDto();
        dataSourceDto.setQueryGpe("queryGpe1");
        dataSourceDto.setPx("px1");
        dataSourceDto.setTemporaryVariable("temporaryVariable1");
        dataSourceDto.setGeographicVariable("geographicVariable1");
        DataSourceVariableDto dataSourceVariableDto1 = new DataSourceVariableDto();
        dataSourceVariableDto1.setVariable("variable1");
        dataSourceVariableDto1.setCategory("category1");
        dataSourceDto.addOtherVariable(dataSourceVariableDto1);
        DataSourceVariableDto dataSourceVariableDto2 = new DataSourceVariableDto();
        dataSourceVariableDto2.setVariable("variable2");
        dataSourceVariableDto2.setCategory("category2");
        dataSourceDto.addOtherVariable(dataSourceVariableDto2);

        String uuidIndicator = INDICATOR_1;
        DataSourceDto dataSourceDtoCreated = indicatorsServiceFacade.createDataSource(getServiceContext(), uuidIndicator, dataSourceDto);
        assertNotNull(dataSourceDtoCreated.getUuid());

        // Retrieve data source
        DataSourceDto dataSourceDtoRetrieved = indicatorsServiceFacade.retrieveDataSource(getServiceContext(), dataSourceDtoCreated.getUuid());
        IndicatorsAsserts.assertEqualsDataSource(dataSourceDto, dataSourceDtoRetrieved);

        // Retrieves all data sources
        List<DataSourceDto> dataSourcesDto = indicatorsServiceFacade.findDataSources(getServiceContext(), uuidIndicator, "2.000");
        assertEquals(3, dataSourcesDto.size());
        assertEquals(DATA_SOURCE_1_INDICATOR_1_V2, dataSourcesDto.get(0).getUuid());
        assertEquals(DATA_SOURCE_2_INDICATOR_1_V2, dataSourcesDto.get(1).getUuid());
        assertEquals(dataSourceDtoCreated.getUuid(), dataSourcesDto.get(2).getUuid());
    }

    @Test
    public void testCreateDataSourceErrorQueryRequired() throws Exception {

        DataSourceDto dataSourceDto = new DataSourceDto();
        dataSourceDto.setQueryGpe(null);
        dataSourceDto.setPx("px1");
        dataSourceDto.setTemporaryVariable("temporaryVariable1");
        dataSourceDto.setGeographicVariable("geographicVariable1");
        try {
            indicatorsServiceFacade.createDataSource(getServiceContext(), INDICATOR_1, dataSourceDto);
            fail("query required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("DATA_SOURCE.QUERY_GPE", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateDataSourceErrorIndicatorNotExists() throws Exception {

        String indicatorsSystemUuid = NOT_EXISTS;

        DataSourceDto dataSourceDto = new DataSourceDto();
        dataSourceDto.setQueryGpe("queryGpe1");
        dataSourceDto.setPx("px1");
        dataSourceDto.setTemporaryVariable("temporaryVariable1");
        dataSourceDto.setGeographicVariable("geographicVariable1");

        try {
            indicatorsServiceFacade.createDataSource(getServiceContext(), indicatorsSystemUuid, dataSourceDto);
            fail("Indicators system not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(indicatorsSystemUuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateDataSourceErrorIndicatorHasNotVersionInProduction() throws Exception {

        String indicatorsSystemUuid = INDICATOR_3;

        DataSourceDto dataSourceDto = new DataSourceDto();
        dataSourceDto.setQueryGpe("queryGpe1");
        dataSourceDto.setPx("px1");
        dataSourceDto.setTemporaryVariable("temporaryVariable1");
        dataSourceDto.setGeographicVariable("geographicVariable1");

        try {
            indicatorsServiceFacade.createDataSource(getServiceContext(), indicatorsSystemUuid, dataSourceDto);
            fail("Indicators system not in production");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_IN_PRODUCTION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(indicatorsSystemUuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testDeleteDataSource() throws Exception {

        String uuid = DATA_SOURCE_1_INDICATOR_1_V2;

        // Delete dataSource
        indicatorsServiceFacade.deleteDataSource(getServiceContext(), uuid);

        // Validation
        try {
            indicatorsServiceFacade.retrieveDataSource(getServiceContext(), uuid);
            fail("DataSource deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DATA_SOURCE_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testDeleteDataSourceErrorIndicatorVersionPublished() throws Exception {

        try {
            indicatorsServiceFacade.deleteDataSource(getServiceContext(), DATA_SOURCE_1_INDICATOR_3);
            fail("Indicators system not in production");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_VERSION_WRONG_STATE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(INDICATOR_3, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals("11.033", e.getExceptionItems().get(0).getMessageParameters()[1]);
        }
    }

    @Test
    public void testDeleteDataSourceErrorNotExists() throws Exception {

        String uuid = NOT_EXISTS;

        // Validation
        try {
            indicatorsServiceFacade.deleteDataSource(getServiceContext(), uuid);
            fail("DataSource not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DATA_SOURCE_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Override
    @Test
    public void testFindDataSources() throws Exception {

        String uuidIndicator = INDICATOR_1;

        // Version 1.000
        {
            List<DataSourceDto> dataSourcesDto = indicatorsServiceFacade.findDataSources(getServiceContext(), uuidIndicator, "1.000");
            assertEquals(1, dataSourcesDto.size());
            assertEquals(DATA_SOURCE_1_INDICATOR_1_V1, dataSourcesDto.get(0).getUuid());
        }

        // Version 2.000
        {
            List<DataSourceDto> dataSourcesDto = indicatorsServiceFacade.findDataSources(getServiceContext(), uuidIndicator, "2.000");
            assertEquals(2, dataSourcesDto.size());

            assertEquals(DATA_SOURCE_1_INDICATOR_1_V2, dataSourcesDto.get(0).getUuid());
            assertEquals(DATA_SOURCE_2_INDICATOR_1_V2, dataSourcesDto.get(1).getUuid());
        }
    }

    @Test
    public void testFindDataSourcesErrorNotExists() throws Exception {

        String uuid = NOT_EXISTS;

        // Validation
        try {
            indicatorsServiceFacade.findDataSources(getServiceContext(), uuid, "1.000");
            fail("Indicators system not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_VERSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals("1.000", e.getExceptionItems().get(0).getMessageParameters()[1]);
        }
    }

    @Override
    @Test
    public void testUpdateDataSource() throws Exception {

        String uuid = DATA_SOURCE_1_INDICATOR_1_V2;
        DataSourceDto dataSourceDto = indicatorsServiceFacade.retrieveDataSource(getServiceContext(), uuid);
        dataSourceDto.setTemporaryVariable("newTemporary");
        dataSourceDto.getOtherVariables().get(0).setCategory("new Category");
        DataSourceVariableDto dataSourceVariableDto3 = new DataSourceVariableDto();
        dataSourceVariableDto3.setVariable("variable3new");
        dataSourceVariableDto3.setCategory("category3new");
        dataSourceDto.addOtherVariable(dataSourceVariableDto3);

        // Update
        indicatorsServiceFacade.updateDataSource(getServiceContext(), dataSourceDto);

        // Validation
        DataSourceDto dataSourceDtoUpdated = indicatorsServiceFacade.retrieveDataSource(getServiceContext(), uuid);
        assertEquals(3, dataSourceDtoUpdated.getOtherVariables().size());
        IndicatorsAsserts.assertEqualsDataSource(dataSourceDto, dataSourceDtoUpdated);
    }

    @Test
    public void testUpdateDataSourceErrorChangeQuery() throws Exception {

        DataSourceDto dataSourceDto = indicatorsServiceFacade.retrieveDataSource(getServiceContext(), DATA_SOURCE_1_INDICATOR_1_V2);
        dataSourceDto.setQueryGpe("newQueryGpe");

        try {
            indicatorsServiceFacade.updateDataSource(getServiceContext(), dataSourceDto);
            fail("Query GPE changed");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_UNMODIFIABLE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("DATA_SOURCE.QUERY_GPE", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateDataSourceErrorChangePx() throws Exception {

        DataSourceDto dataSourceDto = indicatorsServiceFacade.retrieveDataSource(getServiceContext(), DATA_SOURCE_1_INDICATOR_1_V2);
        dataSourceDto.setPx("newPx");

        try {
            indicatorsServiceFacade.updateDataSource(getServiceContext(), dataSourceDto);
            fail("Px changed");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_UNMODIFIABLE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("DATA_SOURCE.PX", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateDataSourceErrorIndicatorPublished() throws Exception {

        DataSourceDto dataSourceDto = indicatorsServiceFacade.retrieveDataSource(getServiceContext(), DATA_SOURCE_1_INDICATOR_1_V1);

        try {
            indicatorsServiceFacade.updateDataSource(getServiceContext(), dataSourceDto);
            fail("Indicators system published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_VERSION_WRONG_STATE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(INDICATOR_1, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals("1.000", e.getExceptionItems().get(0).getMessageParameters()[1]);
        }

    }

    @Test
    public void testUpdateDataSourceErrorNotExists() throws Exception {

        DataSourceDto dataSourceDto = new DataSourceDto();
        dataSourceDto.setUuid(NOT_EXISTS);

        try {
            indicatorsServiceFacade.updateDataSource(getServiceContext(), dataSourceDto);
            fail("Data source not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DATA_SOURCE_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/IndicatorsServiceFacadeTest.xml";
    }
}
