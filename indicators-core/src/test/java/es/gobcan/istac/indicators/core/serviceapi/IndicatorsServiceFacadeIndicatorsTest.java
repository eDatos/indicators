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

import es.gobcan.istac.indicators.core.constants.IndicatorsConstants;
import es.gobcan.istac.indicators.core.criteria.IndicatorCriteriaPropertyEnum;
import es.gobcan.istac.indicators.core.criteria.IndicatorsCriteria;
import es.gobcan.istac.indicators.core.criteria.IndicatorsCriteriaConjunctionRestriction;
import es.gobcan.istac.indicators.core.criteria.IndicatorsCriteriaPropertyRestriction;
import es.gobcan.istac.indicators.core.dto.serviceapi.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.DataSourceVariableDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.QuantityDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.QuantityUnitDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.RateDerivationDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.SubjectDto;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorProcStatusEnum;
import es.gobcan.istac.indicators.core.enume.domain.QuantityTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.QuantityUnitSymbolPositionEnum;
import es.gobcan.istac.indicators.core.enume.domain.RateDerivationMethodTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.RateDerivationRoundingEnum;
import es.gobcan.istac.indicators.core.enume.domain.VersionTypeEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.repositoryimpl.finders.IndicatorCriteriaPropertyInternalEnum;
import es.gobcan.istac.indicators.core.serviceapi.utils.IndicatorsAsserts;
import es.gobcan.istac.indicators.core.serviceapi.utils.IndicatorsMocks;

/**
 * Test to IndicatorsServiceFacade. Testing: indicators, data sources
 * Spring based transactional test with DbUnit support.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext-test.xml"})
public class IndicatorsServiceFacadeIndicatorsTest extends IndicatorsBaseTest {

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
    private static String             INDICATOR_10                 = "Indicator-10";

    // Data sources
    private static String             DATA_SOURCE_1_INDICATOR_1_V1 = "Indicator-1-v1-DataSource-1";
    private static String             DATA_SOURCE_1_INDICATOR_1_V2 = "Indicator-1-v2-DataSource-1";
    private static String             DATA_SOURCE_2_INDICATOR_1_V2 = "Indicator-1-v2-DataSource-2";
    private static String             DATA_SOURCE_1_INDICATOR_3    = "Indicator-3-v1-DataSource-1";

    // Quantity units
    private static String             QUANTITY_UNIT_1              = "1";
    private static String             QUANTITY_UNIT_2              = "2";

    // Geographical values
    private static String             GEOGRAPHICAL_VALUE_1         = "1";

    // Subjects
    private static String             SUBJECT_1                    = "1";
    private static String             SUBJECT_2                    = "2";
    private static String             SUBJECT_3                    = "3";
    private static String             SUBJECT_4                    = "4";

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
        assertEquals(SUBJECT_1, indicatorDto.getSubjectCode());
        IndicatorsAsserts.assertEqualsInternationalString(indicatorDto.getSubjectTitle(), "es", "Área temática 1", null, null);
        assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorDto.getProcStatus());
        IndicatorsAsserts.assertEqualsInternationalString(indicatorDto.getTitle(), "es", "Título Indicator-1-v1", "en", "Title Indicator-1-v1");
        IndicatorsAsserts.assertEqualsInternationalString(indicatorDto.getAcronym(), "es", "Acrónimo Indicator-1-v1", "en", "Acronym Indicator-1-v1");
        IndicatorsAsserts.assertEqualsInternationalString(indicatorDto.getComments(), "es", "Comentario Indicator-1-v1", "en", "Comments Indicator-1-v1");
        assertEquals("http://indicators/comments/1", indicatorDto.getCommentsUrl());
        IndicatorsAsserts.assertEqualsInternationalString(indicatorDto.getNotes(), "es", "Nota Indicator-1-v1", "en", "Note Indicator-1-v1");
        assertEquals("http://indicators/1", indicatorDto.getNotesUrl());
        IndicatorsAsserts.assertEqualsInternationalString(indicatorDto.getConceptDescription(), "es", "Concepto 1", "en", "Concept 1");
        assertEquals("data:repository:11", indicatorDto.getDataRepositoryId());
        assertEquals("TABLE_NAME_11", indicatorDto.getDataRepositoryTableName());

        assertNotNull(indicatorDto.getQuantity());
        assertEquals(QuantityTypeEnum.CHANGE_RATE, indicatorDto.getQuantity().getType());
        assertEquals("1", indicatorDto.getQuantity().getUnitUuid());
        assertEquals(Integer.valueOf(10), indicatorDto.getQuantity().getUnitMultiplier());
        assertEquals(Integer.valueOf(2), indicatorDto.getQuantity().getSignificantDigits());
        assertEquals(Integer.valueOf(3), indicatorDto.getQuantity().getDecimalPlaces());
        assertEquals(Integer.valueOf(100), indicatorDto.getQuantity().getMinimum());
        assertEquals(Integer.valueOf(200), indicatorDto.getQuantity().getMaximum());
        assertEquals(INDICATOR_3, indicatorDto.getQuantity().getNumeratorIndicatorUuid());
        assertEquals(INDICATOR_6, indicatorDto.getQuantity().getDenominatorIndicatorUuid());
        assertEquals(Boolean.TRUE, indicatorDto.getQuantity().getIsPercentage());
        IndicatorsAsserts.assertEqualsInternationalString(indicatorDto.getQuantity().getPercentageOf(), "es", "Porcentaje de 1", "en", "Percentage of 1");
        assertNull(indicatorDto.getQuantity().getBaseValue());
        assertNull(indicatorDto.getQuantity().getBaseTime());
        assertNull(indicatorDto.getQuantity().getBaseLocationUuid());
        assertEquals(INDICATOR_3, indicatorDto.getQuantity().getBaseQuantityIndicatorUuid());

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
        assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorDtoPublished.getProcStatus());
    }

    @Test
    public void testRetrieveIndicatorPublishedWhenHasVersionProduction() throws Exception {

        String uuid = INDICATOR_1;

        IndicatorDto indicatorDtoPublished = indicatorsServiceFacade.retrieveIndicatorPublished(getServiceContext(), uuid);
        assertEquals(uuid, indicatorDtoPublished.getUuid());
        assertEquals("1.000", indicatorDtoPublished.getVersionNumber());
        assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorDtoPublished.getProcStatus());
    }

    @Test
    public void testRetrieveIndicatorPublishedErrorOnlyProduction() throws Exception {

        String uuid = INDICATOR_2;

        try {
            indicatorsServiceFacade.retrieveIndicatorPublished(getServiceContext(), uuid);
            fail("No published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorProcStatusEnum.PUBLISHED, ((IndicatorProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
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
    public void testRetrieveIndicatorByCode() throws Exception {

        String code = "CODE-1";
        String versionNumber = "1.000";
        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicatorByCode(getServiceContext(), code, versionNumber);

        assertNotNull(indicatorDto);
        assertEquals(INDICATOR_1, indicatorDto.getUuid());
        assertEquals("1.000", indicatorDto.getVersionNumber());
        assertEquals("1.000", indicatorDto.getDiffusionVersion());
        assertEquals("2.000", indicatorDto.getProductionVersion());
        assertEquals("CODE-1", indicatorDto.getCode());
    }

    @Test
    public void testRetrieveIndicatorByCodeLastVersion() throws Exception {

        String code = "CODE-1";
        String versionNumber = null;
        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicatorByCode(getServiceContext(), code, versionNumber);

        assertNotNull(indicatorDto);
        assertEquals(INDICATOR_1, indicatorDto.getUuid());
        assertEquals(indicatorDto.getProductionVersion(), indicatorDto.getVersionNumber());
        assertEquals("1.000", indicatorDto.getDiffusionVersion());
        assertEquals("2.000", indicatorDto.getProductionVersion());
        assertEquals("CODE-1", indicatorDto.getCode());
    }

    @Test
    public void testRetrieveIndicatorByCodeErrorNotExists() throws Exception {

        String code = "CODE_NOT_EXISTS";

        try {
            indicatorsServiceFacade.retrieveIndicatorByCode(getServiceContext(), code, null);
            fail("No exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_NOT_FOUND_WITH_CODE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(code, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRetrieveIndicatorPublishedByCode() throws Exception {

        String code = "CODE-1";
        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicatorPublishedByCode(getServiceContext(), code);

        assertNotNull(indicatorDto);
        assertEquals(INDICATOR_1, indicatorDto.getUuid());
        assertEquals(indicatorDto.getDiffusionVersion(), indicatorDto.getVersionNumber());
        assertEquals("1.000", indicatorDto.getDiffusionVersion());
        assertEquals("2.000", indicatorDto.getProductionVersion());
        assertEquals("CODE-1", indicatorDto.getCode());
    }

    @Test
    public void testRetrieveIndicatorPublishedByCodeErrorNotExists() throws Exception {

        String code = "CODE_NOT_EXISTS";

        try {
            indicatorsServiceFacade.retrieveIndicatorPublishedByCode(getServiceContext(), code);
            fail("No exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_NOT_FOUND_WITH_CODE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(code, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRetrieveIndicatorPublishedByCodeErrorNotExistsInDiffusion() throws Exception {

        String code = "CODE-2";

        try {
            indicatorsServiceFacade.retrieveIndicatorPublishedByCode(getServiceContext(), code);
            fail("No exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(INDICATOR_2, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorProcStatusEnum.PUBLISHED, ((IndicatorProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Test
    public void testCreateIndicatorQuantity() throws Exception {

        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode(IndicatorsMocks.mockString(10));
        indicatorDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorDto.setAcronym(IndicatorsMocks.mockInternationalString());
        indicatorDto.setSubjectCode(SUBJECT_1);
        indicatorDto.setSubjectTitle(IndicatorsMocks.mockInternationalString(IndicatorsConstants.LOCALE_SPANISH, "Área temática 1"));
        indicatorDto.setComments(IndicatorsMocks.mockInternationalString());
        indicatorDto.setCommentsUrl(IndicatorsMocks.mockString(100));
        indicatorDto.setNotes(IndicatorsMocks.mockInternationalString());
        indicatorDto.setNotesUrl(IndicatorsMocks.mockString(100));
        indicatorDto.setConceptDescription(IndicatorsMocks.mockInternationalString());
        indicatorDto.setQuantity(new QuantityDto());
        indicatorDto.getQuantity().setType(QuantityTypeEnum.QUANTITY);
        indicatorDto.getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        indicatorDto.getQuantity().setUnitMultiplier(Integer.valueOf(123));

        // Create
        IndicatorDto indicatorDtoCreated = indicatorsServiceFacade.createIndicator(getServiceContext(), indicatorDto);

        // Validate
        assertNotNull(indicatorDtoCreated);
        assertNotNull(indicatorDtoCreated.getUuid());
        assertNotNull(indicatorDtoCreated.getVersionNumber());

        IndicatorDto indicatorDtoRetrieved = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), indicatorDtoCreated.getUuid(), indicatorDtoCreated.getVersionNumber());
        assertEquals(IndicatorProcStatusEnum.DRAFT, indicatorDtoCreated.getProcStatus());
        assertEquals(IndicatorProcStatusEnum.DRAFT, indicatorDtoRetrieved.getProcStatus());
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
    public void testCreateIndicatorMagnitude() throws Exception {

        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode(IndicatorsMocks.mockString(10));
        indicatorDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorDto.setAcronym(IndicatorsMocks.mockInternationalString());
        indicatorDto.setSubjectCode(SUBJECT_1);
        indicatorDto.setSubjectTitle(IndicatorsMocks.mockInternationalString(IndicatorsConstants.LOCALE_SPANISH, "Área temática 1"));
        indicatorDto.setComments(IndicatorsMocks.mockInternationalString());
        indicatorDto.setCommentsUrl(IndicatorsMocks.mockString(100));
        indicatorDto.setNotes(IndicatorsMocks.mockInternationalString());
        indicatorDto.setNotesUrl(IndicatorsMocks.mockString(100));
        indicatorDto.setConceptDescription(IndicatorsMocks.mockInternationalString());
        indicatorDto.setQuantity(new QuantityDto());
        indicatorDto.getQuantity().setType(QuantityTypeEnum.MAGNITUDE);
        indicatorDto.getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        indicatorDto.getQuantity().setUnitMultiplier(Integer.valueOf(123));
        indicatorDto.getQuantity().setMinimum(Integer.valueOf(1000));
        indicatorDto.getQuantity().setMaximum(Integer.valueOf(2000));
        assertTrue(indicatorDto.getQuantity().isMagnituteOrExtension());

        // Create
        IndicatorDto indicatorDtoCreated = indicatorsServiceFacade.createIndicator(getServiceContext(), indicatorDto);

        // Validate
        IndicatorDto indicatorDtoRetrieved = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), indicatorDtoCreated.getUuid(), indicatorDtoCreated.getVersionNumber());
        IndicatorsAsserts.assertEqualsIndicator(indicatorDto, indicatorDtoRetrieved);
    }

    @Test
    public void testCreateIndicatorFraction() throws Exception {

        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode(IndicatorsMocks.mockString(10));
        indicatorDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorDto.setAcronym(IndicatorsMocks.mockInternationalString());
        indicatorDto.setSubjectCode(SUBJECT_1);
        indicatorDto.setSubjectTitle(IndicatorsMocks.mockInternationalString(IndicatorsConstants.LOCALE_SPANISH, "Área temática 1"));
        indicatorDto.setComments(IndicatorsMocks.mockInternationalString());
        indicatorDto.setCommentsUrl(IndicatorsMocks.mockString(100));
        indicatorDto.setNotes(IndicatorsMocks.mockInternationalString());
        indicatorDto.setNotesUrl(IndicatorsMocks.mockString(100));
        indicatorDto.setConceptDescription(IndicatorsMocks.mockInternationalString());
        indicatorDto.setQuantity(new QuantityDto());
        indicatorDto.getQuantity().setType(QuantityTypeEnum.FRACTION);
        indicatorDto.getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        indicatorDto.getQuantity().setUnitMultiplier(Integer.valueOf(123));
        indicatorDto.getQuantity().setMinimum(Integer.valueOf(1000));
        indicatorDto.getQuantity().setMaximum(Integer.valueOf(2000));
        indicatorDto.getQuantity().setNumeratorIndicatorUuid(INDICATOR_2);
        indicatorDto.getQuantity().setDenominatorIndicatorUuid(INDICATOR_3);
        assertTrue(indicatorDto.getQuantity().isFractionOrExtension());

        // Create
        IndicatorDto indicatorDtoCreated = indicatorsServiceFacade.createIndicator(getServiceContext(), indicatorDto);

        // Validate
        IndicatorDto indicatorDtoRetrieved = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), indicatorDtoCreated.getUuid(), indicatorDtoCreated.getVersionNumber());
        IndicatorsAsserts.assertEqualsIndicator(indicatorDto, indicatorDtoRetrieved);
    }

    @Test
    public void testCreateIndicatorRatio() throws Exception {

        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode(IndicatorsMocks.mockString(10));
        indicatorDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorDto.setAcronym(IndicatorsMocks.mockInternationalString());
        indicatorDto.setSubjectCode(SUBJECT_1);
        indicatorDto.setSubjectTitle(IndicatorsMocks.mockInternationalString(IndicatorsConstants.LOCALE_SPANISH, "Área temática 1"));
        indicatorDto.setComments(IndicatorsMocks.mockInternationalString());
        indicatorDto.setCommentsUrl(IndicatorsMocks.mockString(100));
        indicatorDto.setNotes(IndicatorsMocks.mockInternationalString());
        indicatorDto.setNotesUrl(IndicatorsMocks.mockString(100));
        indicatorDto.setConceptDescription(IndicatorsMocks.mockInternationalString());
        indicatorDto.setQuantity(new QuantityDto());
        indicatorDto.getQuantity().setType(QuantityTypeEnum.RATIO);
        indicatorDto.getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        indicatorDto.getQuantity().setUnitMultiplier(Integer.valueOf(123));
        indicatorDto.getQuantity().setMinimum(Integer.valueOf(1000));
        indicatorDto.getQuantity().setMaximum(Integer.valueOf(2000));
        indicatorDto.getQuantity().setNumeratorIndicatorUuid(INDICATOR_2);
        indicatorDto.getQuantity().setDenominatorIndicatorUuid(INDICATOR_3);
        indicatorDto.getQuantity().setIsPercentage(Boolean.FALSE);
        indicatorDto.getQuantity().setPercentageOf(IndicatorsMocks.mockInternationalString());
        assertTrue(indicatorDto.getQuantity().isRatioOrExtension());

        // Create
        IndicatorDto indicatorDtoCreated = indicatorsServiceFacade.createIndicator(getServiceContext(), indicatorDto);

        // Validate
        IndicatorDto indicatorDtoRetrieved = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), indicatorDtoCreated.getUuid(), indicatorDtoCreated.getVersionNumber());
        IndicatorsAsserts.assertEqualsIndicator(indicatorDto, indicatorDtoRetrieved);
    }

    @Test
    public void testCreateIndicatorIndex() throws Exception {

        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode(IndicatorsMocks.mockString(10));
        indicatorDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorDto.setAcronym(IndicatorsMocks.mockInternationalString());
        indicatorDto.setSubjectCode(SUBJECT_1);
        indicatorDto.setSubjectTitle(IndicatorsMocks.mockInternationalString(IndicatorsConstants.LOCALE_SPANISH, "Área temática 1"));
        indicatorDto.setComments(IndicatorsMocks.mockInternationalString());
        indicatorDto.setCommentsUrl(IndicatorsMocks.mockString(100));
        indicatorDto.setNotes(IndicatorsMocks.mockInternationalString());
        indicatorDto.setNotesUrl(IndicatorsMocks.mockString(100));
        indicatorDto.setConceptDescription(IndicatorsMocks.mockInternationalString());
        indicatorDto.setQuantity(new QuantityDto());
        indicatorDto.getQuantity().setType(QuantityTypeEnum.INDEX);
        indicatorDto.getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        indicatorDto.getQuantity().setUnitMultiplier(Integer.valueOf(123));
        indicatorDto.getQuantity().setMinimum(Integer.valueOf(1000));
        indicatorDto.getQuantity().setMaximum(Integer.valueOf(2000));
        indicatorDto.getQuantity().setNumeratorIndicatorUuid(INDICATOR_2);
        indicatorDto.getQuantity().setDenominatorIndicatorUuid(INDICATOR_3);
        indicatorDto.getQuantity().setIsPercentage(Boolean.FALSE);
        indicatorDto.getQuantity().setPercentageOf(IndicatorsMocks.mockInternationalString());
        indicatorDto.getQuantity().setBaseTime("2011");
        assertTrue(indicatorDto.getQuantity().isIndexOrExtension());

        // Create
        IndicatorDto indicatorDtoCreated = indicatorsServiceFacade.createIndicator(getServiceContext(), indicatorDto);

        // Validate
        IndicatorDto indicatorDtoRetrieved = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), indicatorDtoCreated.getUuid(), indicatorDtoCreated.getVersionNumber());
        IndicatorsAsserts.assertEqualsIndicator(indicatorDto, indicatorDtoRetrieved);
    }

    @Test
    public void testCreateIndicatorChangeRate() throws Exception {

        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode(IndicatorsMocks.mockString(10));
        indicatorDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorDto.setAcronym(IndicatorsMocks.mockInternationalString());
        indicatorDto.setSubjectCode(SUBJECT_1);
        indicatorDto.setSubjectTitle(IndicatorsMocks.mockInternationalString(IndicatorsConstants.LOCALE_SPANISH, "Área temática 1"));
        indicatorDto.setComments(IndicatorsMocks.mockInternationalString());
        indicatorDto.setCommentsUrl(IndicatorsMocks.mockString(100));
        indicatorDto.setNotes(IndicatorsMocks.mockInternationalString());
        indicatorDto.setNotesUrl(IndicatorsMocks.mockString(100));
        indicatorDto.setConceptDescription(IndicatorsMocks.mockInternationalString());
        indicatorDto.setQuantity(new QuantityDto());
        indicatorDto.getQuantity().setType(QuantityTypeEnum.CHANGE_RATE);
        indicatorDto.getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        indicatorDto.getQuantity().setUnitMultiplier(Integer.valueOf(123));
        indicatorDto.getQuantity().setMinimum(Integer.valueOf(1000));
        indicatorDto.getQuantity().setMaximum(Integer.valueOf(2000));
        indicatorDto.getQuantity().setNumeratorIndicatorUuid(INDICATOR_2);
        indicatorDto.getQuantity().setDenominatorIndicatorUuid(INDICATOR_3);
        indicatorDto.getQuantity().setIsPercentage(Boolean.FALSE);
        indicatorDto.getQuantity().setPercentageOf(IndicatorsMocks.mockInternationalString());
        indicatorDto.getQuantity().setBaseQuantityIndicatorUuid(INDICATOR_10);
        assertTrue(indicatorDto.getQuantity().isChangeRateOrExtension());

        // Create
        IndicatorDto indicatorDtoCreated = indicatorsServiceFacade.createIndicator(getServiceContext(), indicatorDto);

        // Validate
        IndicatorDto indicatorDtoRetrieved = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), indicatorDtoCreated.getUuid(), indicatorDtoCreated.getVersionNumber());
        IndicatorsAsserts.assertEqualsIndicator(indicatorDto, indicatorDtoRetrieved);
    }

    @Test
    public void testCreateIndicatorWithQuantityEmptyAttributes() throws Exception {

        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode(IndicatorsMocks.mockString(10));
        indicatorDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorDto.setAcronym(IndicatorsMocks.mockInternationalString());
        indicatorDto.setSubjectCode(SUBJECT_1);
        indicatorDto.setSubjectTitle(IndicatorsMocks.mockInternationalString(IndicatorsConstants.LOCALE_SPANISH, "Área temática 1"));
        indicatorDto.setComments(IndicatorsMocks.mockInternationalString());
        indicatorDto.setCommentsUrl(IndicatorsMocks.mockString(100));
        indicatorDto.setNotes(IndicatorsMocks.mockInternationalString());
        indicatorDto.setNotesUrl(IndicatorsMocks.mockString(100));
        indicatorDto.setConceptDescription(IndicatorsMocks.mockInternationalString());
        indicatorDto.setQuantity(new QuantityDto());
        indicatorDto.getQuantity().setType(QuantityTypeEnum.CHANGE_RATE);

        // Create
        IndicatorDto indicatorDtoCreated = indicatorsServiceFacade.createIndicator(getServiceContext(), indicatorDto);

        // Validate
        IndicatorDto indicatorDtoRetrieved = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), indicatorDtoCreated.getUuid(), indicatorDtoCreated.getVersionNumber());
        IndicatorsAsserts.assertEqualsIndicator(indicatorDto, indicatorDtoRetrieved);
        assertNull(indicatorDtoRetrieved.getQuantity().getUnitUuid());
    }

    @Test
    public void testCreateIndicatorParametersRequired() throws Exception {

        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode(null);
        indicatorDto.setTitle(null);
        indicatorDto.setSubjectCode(null);
        indicatorDto.setSubjectTitle(null);
        indicatorDto.setQuantity(new QuantityDto());

        try {
            indicatorsServiceFacade.createIndicator(getServiceContext(), indicatorDto);
            fail("parameters required");
        } catch (MetamacException e) {
            assertEquals(4, e.getExceptionItems().size());

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("INDICATOR.CODE", e.getExceptionItems().get(0).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(1).getCode());
            assertEquals(1, e.getExceptionItems().get(1).getMessageParameters().length);
            assertEquals("INDICATOR.TITLE", e.getExceptionItems().get(1).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(2).getCode());
            assertEquals(1, e.getExceptionItems().get(2).getMessageParameters().length);
            assertEquals("INDICATOR.SUBJECT_CODE", e.getExceptionItems().get(2).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(3).getCode());
            assertEquals(1, e.getExceptionItems().get(3).getMessageParameters().length);
            assertEquals("INDICATOR.SUBJECT_TITLE", e.getExceptionItems().get(3).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorQuantityErrorBaseUnexpected() throws Exception {

        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode(IndicatorsMocks.mockString(10));
        indicatorDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorDto.setAcronym(IndicatorsMocks.mockInternationalString());
        indicatorDto.setSubjectCode(SUBJECT_1);
        indicatorDto.setSubjectTitle(IndicatorsMocks.mockInternationalString(IndicatorsConstants.LOCALE_SPANISH, "Área temática 1"));
        indicatorDto.setComments(IndicatorsMocks.mockInternationalString());
        indicatorDto.setCommentsUrl(IndicatorsMocks.mockString(100));
        indicatorDto.setNotes(IndicatorsMocks.mockInternationalString());
        indicatorDto.setNotesUrl(IndicatorsMocks.mockString(100));
        indicatorDto.setConceptDescription(IndicatorsMocks.mockInternationalString());
        indicatorDto.setQuantity(new QuantityDto());
        indicatorDto.getQuantity().setType(QuantityTypeEnum.INDEX);
        indicatorDto.getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        indicatorDto.getQuantity().setUnitMultiplier(Integer.valueOf(123));
        indicatorDto.getQuantity().setMinimum(Integer.valueOf(1000));
        indicatorDto.getQuantity().setMaximum(Integer.valueOf(2000));
        indicatorDto.getQuantity().setNumeratorIndicatorUuid(INDICATOR_2);
        indicatorDto.getQuantity().setDenominatorIndicatorUuid(INDICATOR_3);
        indicatorDto.getQuantity().setIsPercentage(Boolean.FALSE);
        indicatorDto.getQuantity().setPercentageOf(IndicatorsMocks.mockInternationalString());
        indicatorDto.getQuantity().setBaseValue(Integer.valueOf(1));
        indicatorDto.getQuantity().setBaseTime("2011");
        indicatorDto.getQuantity().setBaseLocationUuid(GEOGRAPHICAL_VALUE_1);

        try {
            indicatorsServiceFacade.createIndicator(getServiceContext(), indicatorDto);
            fail("base location unexpected");
        } catch (MetamacException e) {
            assertEquals(2, e.getExceptionItems().size());

            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("INDICATOR.QUANTITY.BASE_TIME", e.getExceptionItems().get(0).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(1).getCode());
            assertEquals(1, e.getExceptionItems().get(1).getMessageParameters().length);
            assertEquals("INDICATOR.QUANTITY.BASE_LOCATION_UUID", e.getExceptionItems().get(1).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorErrorMetadataUnexpected() throws Exception {

        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode(IndicatorsMocks.mockString(10));
        indicatorDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorDto.setAcronym(IndicatorsMocks.mockInternationalString());
        indicatorDto.setSubjectCode(SUBJECT_1);
        indicatorDto.setSubjectTitle(IndicatorsMocks.mockInternationalString(IndicatorsConstants.LOCALE_SPANISH, "Área temática 1"));
        indicatorDto.setComments(IndicatorsMocks.mockInternationalString());
        indicatorDto.setCommentsUrl(IndicatorsMocks.mockString(100));
        indicatorDto.setNotes(IndicatorsMocks.mockInternationalString());
        indicatorDto.setNotesUrl(IndicatorsMocks.mockString(100));
        indicatorDto.setConceptDescription(IndicatorsMocks.mockInternationalString());
        indicatorDto.setQuantity(new QuantityDto());
        indicatorDto.getQuantity().setType(QuantityTypeEnum.AMOUNT);
        indicatorDto.getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        indicatorDto.getQuantity().setUnitMultiplier(Integer.valueOf(123));
        indicatorDto.getQuantity().setMinimum(Integer.valueOf(1000));
        indicatorDto.getQuantity().setMaximum(Integer.valueOf(2000));
        indicatorDto.getQuantity().setNumeratorIndicatorUuid(INDICATOR_2);
        indicatorDto.getQuantity().setDenominatorIndicatorUuid(INDICATOR_3);
        indicatorDto.getQuantity().setIsPercentage(Boolean.FALSE);
        indicatorDto.getQuantity().setPercentageOf(IndicatorsMocks.mockInternationalString());
        indicatorDto.getQuantity().setBaseValue(Integer.valueOf(1));
        indicatorDto.getQuantity().setBaseTime("2011");
        indicatorDto.getQuantity().setBaseLocationUuid(GEOGRAPHICAL_VALUE_1);
        indicatorDto.getQuantity().setBaseQuantityIndicatorUuid(INDICATOR_5);

        try {
            indicatorsServiceFacade.createIndicator(getServiceContext(), indicatorDto);
            fail("metadatas unexpected");
        } catch (MetamacException e) {
            assertEquals(10, e.getExceptionItems().size());

            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("INDICATOR.QUANTITY.MINIMUM", e.getExceptionItems().get(0).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(1).getCode());
            assertEquals(1, e.getExceptionItems().get(1).getMessageParameters().length);
            assertEquals("INDICATOR.QUANTITY.MAXIMUM", e.getExceptionItems().get(1).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(2).getCode());
            assertEquals(1, e.getExceptionItems().get(2).getMessageParameters().length);
            assertEquals("INDICATOR.QUANTITY.NUMERATOR_INDICATOR_UUID", e.getExceptionItems().get(2).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(3).getCode());
            assertEquals(1, e.getExceptionItems().get(3).getMessageParameters().length);
            assertEquals("INDICATOR.QUANTITY.DENOMINATOR_INDICATOR_UUID", e.getExceptionItems().get(3).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(4).getCode());
            assertEquals(1, e.getExceptionItems().get(4).getMessageParameters().length);
            assertEquals("INDICATOR.QUANTITY.IS_PERCENTAGE", e.getExceptionItems().get(4).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(5).getCode());
            assertEquals(1, e.getExceptionItems().get(5).getMessageParameters().length);
            assertEquals("INDICATOR.QUANTITY.PERCENTAGE_OF", e.getExceptionItems().get(5).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(6).getCode());
            assertEquals(1, e.getExceptionItems().get(6).getMessageParameters().length);
            assertEquals("INDICATOR.QUANTITY.BASE_VALUE", e.getExceptionItems().get(6).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(7).getCode());
            assertEquals(1, e.getExceptionItems().get(7).getMessageParameters().length);
            assertEquals("INDICATOR.QUANTITY.BASE_TIME", e.getExceptionItems().get(7).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(8).getCode());
            assertEquals(1, e.getExceptionItems().get(8).getMessageParameters().length);
            assertEquals("INDICATOR.QUANTITY.BASE_LOCATION_UUID", e.getExceptionItems().get(8).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(9).getCode());
            assertEquals(1, e.getExceptionItems().get(9).getMessageParameters().length);
            assertEquals("INDICATOR.QUANTITY.BASE_QUANTITY_INDICATOR_UUID", e.getExceptionItems().get(9).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorErrorUnitNotExists() throws Exception {

        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode(IndicatorsMocks.mockString(10));
        indicatorDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorDto.setAcronym(IndicatorsMocks.mockInternationalString());
        indicatorDto.setSubjectCode(SUBJECT_1);
        indicatorDto.setSubjectTitle(IndicatorsMocks.mockInternationalString(IndicatorsConstants.LOCALE_SPANISH, "Área temática 1"));
        indicatorDto.setComments(IndicatorsMocks.mockInternationalString());
        indicatorDto.setCommentsUrl(IndicatorsMocks.mockString(100));
        indicatorDto.setNotes(IndicatorsMocks.mockInternationalString());
        indicatorDto.setNotesUrl(IndicatorsMocks.mockString(100));
        indicatorDto.setConceptDescription(IndicatorsMocks.mockInternationalString());
        indicatorDto.setQuantity(new QuantityDto());
        indicatorDto.getQuantity().setType(QuantityTypeEnum.QUANTITY);
        indicatorDto.getQuantity().setUnitUuid(NOT_EXISTS);
        indicatorDto.getQuantity().setUnitMultiplier(Integer.valueOf(123));

        try {
            indicatorsServiceFacade.createIndicator(getServiceContext(), indicatorDto);
            fail("unit not exits");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());

            assertEquals(ServiceExceptionType.QUANTITY_UNIT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(indicatorDto.getQuantity().getUnitUuid(), e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorErrorCodeDuplicated() throws Exception {

        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode("CoDe-1");
        indicatorDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorDto.setSubjectCode(SUBJECT_1);
        indicatorDto.setSubjectTitle(IndicatorsMocks.mockInternationalString(IndicatorsConstants.LOCALE_SPANISH, "Área temática 1"));
        indicatorDto.setQuantity(new QuantityDto());
        indicatorDto.getQuantity().setType(QuantityTypeEnum.QUANTITY);
        indicatorDto.getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        indicatorDto.getQuantity().setUnitMultiplier(Integer.valueOf(123));

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
    public void testCreateIndicatorCodeErrorDuplicatedInsensitive() throws Exception {

        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode("CoDe-1");
        indicatorDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorDto.setSubjectCode(SUBJECT_1);
        indicatorDto.setSubjectTitle(IndicatorsMocks.mockInternationalString(IndicatorsConstants.LOCALE_SPANISH, "Área temática 1"));
        indicatorDto.setQuantity(new QuantityDto());
        indicatorDto.getQuantity().setType(QuantityTypeEnum.QUANTITY);
        indicatorDto.getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        indicatorDto.getQuantity().setUnitMultiplier(Integer.valueOf(123));

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
    public void testCreateIndicatorErrorSubjectCodeNotExits() throws Exception {

        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode(IndicatorsMocks.mockString(10));
        indicatorDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorDto.setSubjectCode(NOT_EXISTS);
        indicatorDto.setSubjectTitle(IndicatorsMocks.mockInternationalString());
        indicatorDto.setQuantity(new QuantityDto());
        indicatorDto.getQuantity().setType(QuantityTypeEnum.QUANTITY);
        indicatorDto.getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        indicatorDto.getQuantity().setUnitMultiplier(Integer.valueOf(123));

        try {
            indicatorsServiceFacade.createIndicator(getServiceContext(), indicatorDto);
            fail("subject code not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SUBJECT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(indicatorDto.getSubjectCode(), e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorErrorSubjectTitleIncorrect() throws Exception {

        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode(IndicatorsMocks.mockString(10));
        indicatorDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorDto.setSubjectCode(SUBJECT_1);
        indicatorDto.setSubjectTitle(IndicatorsMocks.mockInternationalString());
        indicatorDto.setQuantity(new QuantityDto());
        indicatorDto.getQuantity().setType(QuantityTypeEnum.QUANTITY);
        indicatorDto.getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        indicatorDto.getQuantity().setUnitMultiplier(Integer.valueOf(123));

        try {
            indicatorsServiceFacade.createIndicator(getServiceContext(), indicatorDto);
            fail("subject title incorrect not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("INDICATOR.SUBJECT_TITLE", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }
    
    @Test
    public void testCreateIndicatorErrorCodeIncorrect() throws Exception {

        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode("A*b-?");
        indicatorDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorDto.setAcronym(IndicatorsMocks.mockInternationalString());
        indicatorDto.setSubjectCode(SUBJECT_1);
        indicatorDto.setSubjectTitle(IndicatorsMocks.mockInternationalString(IndicatorsConstants.LOCALE_SPANISH, "Área temática 1"));
        indicatorDto.setComments(IndicatorsMocks.mockInternationalString());
        indicatorDto.setCommentsUrl(IndicatorsMocks.mockString(100));
        indicatorDto.setNotes(IndicatorsMocks.mockInternationalString());
        indicatorDto.setNotesUrl(IndicatorsMocks.mockString(100));
        indicatorDto.setConceptDescription(IndicatorsMocks.mockInternationalString());
        indicatorDto.setQuantity(new QuantityDto());
        indicatorDto.getQuantity().setType(QuantityTypeEnum.QUANTITY);
        indicatorDto.getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        indicatorDto.getQuantity().setUnitMultiplier(Integer.valueOf(123));

        // Create
        try {
            indicatorsServiceFacade.createIndicator(getServiceContext(), indicatorDto);
            fail("code incorrect");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("INDICATOR.CODE", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorErrorBaseTimeIncorrect() throws Exception {

        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode(IndicatorsMocks.mockString(10));
        indicatorDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorDto.setAcronym(IndicatorsMocks.mockInternationalString());
        indicatorDto.setSubjectCode(SUBJECT_1);
        indicatorDto.setSubjectTitle(IndicatorsMocks.mockInternationalString(IndicatorsConstants.LOCALE_SPANISH, "Área temática 1"));
        indicatorDto.setConceptDescription(IndicatorsMocks.mockInternationalString());
        indicatorDto.setQuantity(new QuantityDto());
        indicatorDto.getQuantity().setType(QuantityTypeEnum.INDEX);
        indicatorDto.getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        indicatorDto.getQuantity().setUnitMultiplier(Integer.valueOf(123));
        indicatorDto.getQuantity().setIsPercentage(Boolean.FALSE);
        indicatorDto.getQuantity().setBaseTime("2011xx");

        // Create
        try {
            indicatorsServiceFacade.createIndicator(getServiceContext(), indicatorDto);
            fail("base time incorrect");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("INDICATOR.QUANTITY.BASE_TIME", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testDeleteIndicator() throws Exception {

        String uuid = INDICATOR_4;

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
    public void testDeleteIndicatorErrorWithIndicatorInstances() throws Exception {

        String uuid = INDICATOR_10;

        // Validation
        try {
            indicatorsServiceFacade.deleteIndicator(getServiceContext(), uuid);
            fail("Indicator has indicator instances");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_MUST_NOT_BE_IN_INDICATORS_SYSTEMS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testDeleteIndicatorErrorAsQuantityNumerator() throws Exception {

        String uuid = INDICATOR_2;
        String uuidLinked = INDICATOR_4;

        // Retrieves indicator linked
        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuidLinked, null);
        assertEquals(uuid, indicatorDto.getQuantity().getNumeratorIndicatorUuid());
        assertNull(indicatorDto.getQuantity().getDenominatorIndicatorUuid());
        assertNull(indicatorDto.getQuantity().getBaseQuantityIndicatorUuid());

        // Try delete
        try {
            indicatorsServiceFacade.deleteIndicator(getServiceContext(), uuid);
            fail("Indicator is linked to other indicator");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());

            assertEquals(ServiceExceptionType.INDICATOR_MUST_NOT_BE_LINKED_TO_OTHER_INDICATOR.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(uuidLinked, e.getExceptionItems().get(0).getMessageParameters()[1]);
        }
    }

    @Test
    public void testDeleteIndicatorErrorAsQuantityDenominator() throws Exception {

        String uuid = INDICATOR_2;
        String uuidLinked = INDICATOR_4;

        // Links as denominator
        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuidLinked, null);
        indicatorDto.getQuantity().setType(QuantityTypeEnum.FRACTION);
        indicatorDto.getQuantity().setNumeratorIndicatorUuid(null);
        indicatorDto.getQuantity().setDenominatorIndicatorUuid(uuid);
        indicatorDto.getQuantity().setBaseQuantityIndicatorUuid(null);
        indicatorsServiceFacade.updateIndicator(getServiceContext(), indicatorDto);

        // Try delete
        try {
            indicatorsServiceFacade.deleteIndicator(getServiceContext(), uuid);
            fail("Indicator is linked to other indicator");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_MUST_NOT_BE_LINKED_TO_OTHER_INDICATOR.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(uuidLinked, e.getExceptionItems().get(0).getMessageParameters()[1]);
        }
    }

    @Test
    public void testDeleteIndicatorErrorAsQuantityBaseQuantity() throws Exception {

        String uuid = INDICATOR_2;
        String uuidLinked = INDICATOR_4;

        // Links as base quantity
        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuidLinked, null);
        indicatorDto.getQuantity().setType(QuantityTypeEnum.FRACTION);
        indicatorDto.getQuantity().setNumeratorIndicatorUuid(null);
        indicatorDto.getQuantity().setDenominatorIndicatorUuid(uuid);
        indicatorDto.getQuantity().setBaseQuantityIndicatorUuid(null);
        indicatorsServiceFacade.updateIndicator(getServiceContext(), indicatorDto);

        // Try delete
        try {
            indicatorsServiceFacade.deleteIndicator(getServiceContext(), uuid);
            fail("Indicator is linked to other indicator");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_MUST_NOT_BE_LINKED_TO_OTHER_INDICATOR.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(uuidLinked, e.getExceptionItems().get(0).getMessageParameters()[1]);
        }
    }
    @Test
    public void testUpdateIndicator() throws Exception {

        String uuid = INDICATOR_1;
        String versionNumber = "2.000";

        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumber);
        assertEquals(IndicatorProcStatusEnum.DRAFT, indicatorDto.getProcStatus());

        indicatorDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorDto.setAcronym(IndicatorsMocks.mockInternationalString());
        indicatorDto.setNotesUrl("aa");

        // Update
        IndicatorDto indicatorDtoUpdated = indicatorsServiceFacade.updateIndicator(getServiceContext(), indicatorDto);
        IndicatorsAsserts.assertEqualsIndicator(indicatorDto, indicatorDtoUpdated);

        // Validation
        indicatorDtoUpdated = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumber);
        IndicatorsAsserts.assertEqualsIndicator(indicatorDto, indicatorDtoUpdated);
        assertTrue(indicatorDtoUpdated.getLastUpdated().after(indicatorDtoUpdated.getCreatedDate()));
    }

    @Test
    public void testUpdateIndicatorInRejectedValidation() throws Exception {

        String uuid = INDICATOR_9;
        String versionNumber = "1.000";

        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumber);
        assertEquals(IndicatorProcStatusEnum.VALIDATION_REJECTED, indicatorDto.getProcStatus());

        indicatorDto.setTitle(IndicatorsMocks.mockInternationalString());

        // Update
        indicatorsServiceFacade.updateIndicator(getServiceContext(), indicatorDto);

        // Validation
        IndicatorDto indicatorDtoUpdated = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumber);
        assertEquals(IndicatorProcStatusEnum.VALIDATION_REJECTED, indicatorDtoUpdated.getProcStatus());
        IndicatorsAsserts.assertEqualsIndicator(indicatorDto, indicatorDtoUpdated);
    }

    @Test
    public void testUpdateIndicatorInProductionValidation() throws Exception {

        String uuid = INDICATOR_4;
        String versionNumber = "1.000";

        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumber);
        assertEquals(IndicatorProcStatusEnum.PRODUCTION_VALIDATION, indicatorDto.getProcStatus());

        indicatorDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorDto.setSubjectCode(SUBJECT_1);
        indicatorDto.setSubjectTitle(IndicatorsMocks.mockInternationalString(IndicatorsConstants.LOCALE_SPANISH, "Área temática 1"));

        // Update
        indicatorsServiceFacade.updateIndicator(getServiceContext(), indicatorDto);

        // Validation
        IndicatorDto indicatorDtoUpdated = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumber);
        assertEquals(IndicatorProcStatusEnum.PRODUCTION_VALIDATION, indicatorDtoUpdated.getProcStatus());
        IndicatorsAsserts.assertEqualsIndicator(indicatorDto, indicatorDtoUpdated);
    }

    @Test
    public void testUpdateIndicatorInDiffusionValidation() throws Exception {

        String uuid = INDICATOR_5;
        String versionNumber = "1.000";

        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumber);
        assertEquals(IndicatorProcStatusEnum.DIFFUSION_VALIDATION, indicatorDto.getProcStatus());

        indicatorDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorDto.setSubjectCode(SUBJECT_1);
        indicatorDto.setSubjectTitle(IndicatorsMocks.mockInternationalString(IndicatorsConstants.LOCALE_SPANISH, "Área temática 1"));

        // Update
        indicatorsServiceFacade.updateIndicator(getServiceContext(), indicatorDto);

        // Validation
        IndicatorDto indicatorDtoUpdated = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumber);
        assertEquals(IndicatorProcStatusEnum.DIFFUSION_VALIDATION, indicatorDtoUpdated.getProcStatus());
        IndicatorsAsserts.assertEqualsIndicator(indicatorDto, indicatorDtoUpdated);
    }

    @Test
    public void testUpdateIndicatorReusingLocalisedStrings() throws Exception {

        String uuid = INDICATOR_1;
        String versionNumber = "2.000";

        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumber);
        indicatorDto.getTitle().getTexts().iterator().next().setLabel("NewLabel");

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
        indicatorDto.setVersionNumber(null);

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
        indicatorDto.setSubjectCode(SUBJECT_1);
        indicatorDto.setSubjectTitle(IndicatorsMocks.mockInternationalString(IndicatorsConstants.LOCALE_SPANISH, "Área temática 1"));

        try {
            indicatorsServiceFacade.updateIndicator(getServiceContext(), indicatorDto);
            fail("Indicator not in production");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorProcStatusEnum.DRAFT, ((IndicatorProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(IndicatorProcStatusEnum.VALIDATION_REJECTED, ((IndicatorProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
            assertEquals(IndicatorProcStatusEnum.PRODUCTION_VALIDATION, ((IndicatorProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[2]);
            assertEquals(IndicatorProcStatusEnum.DIFFUSION_VALIDATION, ((IndicatorProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[3]);
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
            assertEquals(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorProcStatusEnum.DRAFT, ((IndicatorProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(IndicatorProcStatusEnum.VALIDATION_REJECTED, ((IndicatorProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
            assertEquals(IndicatorProcStatusEnum.PRODUCTION_VALIDATION, ((IndicatorProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[2]);
            assertEquals(IndicatorProcStatusEnum.DIFFUSION_VALIDATION, ((IndicatorProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[3]);
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

    @Test
    public void testUpdateIndicatorsErrorDataRepositoryNonModifiable() throws Exception {

        String uuid = INDICATOR_1;
        String versionNumber = "2.000";

        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumber);
        indicatorDto.setDataRepositoryId("newDataRepositoryId");
        indicatorDto.setDataRepositoryTableName("newTable");

        // Update
        try {
            indicatorsServiceFacade.updateIndicator(getServiceContext(), indicatorDto);
            fail("attributes are unmodifiable");
        } catch (MetamacException e) {
            assertEquals(2, e.getExceptionItems().size());
            
            assertEquals(ServiceExceptionType.METADATA_UNMODIFIABLE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("INDICATOR.DATA_REPOSITORY_ID", e.getExceptionItems().get(0).getMessageParameters()[0]);
            
            assertEquals(ServiceExceptionType.METADATA_UNMODIFIABLE.getCode(), e.getExceptionItems().get(1).getCode());
            assertEquals(1, e.getExceptionItems().get(1).getMessageParameters().length);
            assertEquals("INDICATOR.DATA_REPOSITORY_TABLE_NAME", e.getExceptionItems().get(1).getMessageParameters()[0]);
        }
    }
    
    @Test
    public void testUpdateIndicatorsErrorOwnIndicatorLinked() throws Exception {

        String uuid = INDICATOR_1;
        String versionNumber = "2.000";

        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumber);
        indicatorDto.getQuantity().setType(QuantityTypeEnum.FRACTION);
        indicatorDto.getQuantity().setNumeratorIndicatorUuid(uuid);

        // Update
        try {
            indicatorsServiceFacade.updateIndicator(getServiceContext(), indicatorDto);
            fail("Indicator linked");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("INDICATOR.QUANTITY.NUMERATOR_INDICATOR_UUID", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

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
            assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorDtoV1.getProcStatus());
            assertEquals(IndicatorProcStatusEnum.DRAFT, indicatorDtoV2.getProcStatus());
        }

        // Sends to production validation
        IndicatorDto indicatorDtoV2Updated = indicatorsServiceFacade.sendIndicatorToProductionValidation(getServiceContext2(), uuid);

        // Validation
        {
            assertEquals(diffusionVersion, indicatorDtoV2Updated.getDiffusionVersion());
            assertEquals(productionVersion, indicatorDtoV2Updated.getProductionVersion());
            assertEquals(IndicatorProcStatusEnum.PRODUCTION_VALIDATION, indicatorDtoV2Updated.getProcStatus());

            assertTrue(DateUtils.isSameDay(new Date(), indicatorDtoV2Updated.getProductionValidationDate()));
            assertEquals(getServiceContext2().getUserId(), indicatorDtoV2Updated.getProductionValidationUser());
            assertNull(indicatorDtoV2Updated.getDiffusionValidationDate());
            assertNull(indicatorDtoV2Updated.getDiffusionValidationUser());
            assertNull(indicatorDtoV2Updated.getPublicationDate());
            assertNull(indicatorDtoV2Updated.getPublicationUser());
            assertNull(indicatorDtoV2Updated.getArchiveDate());
            assertNull(indicatorDtoV2Updated.getArchiveUser());
        }
        {
            IndicatorDto indicatorDtoV1 = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, diffusionVersion);
            IndicatorDto indicatorDtoV2 = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, productionVersion);
            assertEquals(diffusionVersion, indicatorDtoV1.getDiffusionVersion());
            assertEquals(productionVersion, indicatorDtoV1.getProductionVersion());
            assertEquals(diffusionVersion, indicatorDtoV2.getDiffusionVersion());
            assertEquals(productionVersion, indicatorDtoV2.getProductionVersion());
            assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorDtoV1.getProcStatus());
            assertEquals(IndicatorProcStatusEnum.PRODUCTION_VALIDATION, indicatorDtoV2.getProcStatus());

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
    public void testSendIndicatorToProductionValidationInProcStatusRejected() throws Exception {

        String uuid = INDICATOR_9;
        String productionVersion = "1.000";

        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, productionVersion);
            assertEquals(productionVersion, indicatorDto.getProductionVersion());
            assertNull(indicatorDto.getDiffusionVersion());
            assertEquals(IndicatorProcStatusEnum.VALIDATION_REJECTED, indicatorDto.getProcStatus());
        }

        // Sends to production validation
        indicatorsServiceFacade.sendIndicatorToProductionValidation(getServiceContext2(), uuid);

        // Validation
        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, productionVersion);
            assertEquals(productionVersion, indicatorDto.getProductionVersion());
            assertNull(indicatorDto.getDiffusionVersion());
            assertEquals(IndicatorProcStatusEnum.PRODUCTION_VALIDATION, indicatorDto.getProcStatus());
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
    public void testSendIndicatorToProductionValidationErrorWrongProcStatus() throws Exception {

        String uuid = INDICATOR_3;

        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, INDICATOR_3_VERSION);
            assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorDto.getProcStatus());
            assertNull(indicatorDto.getProductionVersion());
        }

        try {
            indicatorsServiceFacade.sendIndicatorToProductionValidation(getServiceContext(), uuid);
            fail("Indicator is not draft");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorProcStatusEnum.DRAFT, ((IndicatorProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(IndicatorProcStatusEnum.VALIDATION_REJECTED, ((IndicatorProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);

        }
    }

    @Test
    public void testSendIndicatorToProductionValidationErrorWithoutDataSources() throws Exception {

        String uuid = INDICATOR_2;

        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, "1.000");
            assertEquals(IndicatorProcStatusEnum.DRAFT, indicatorDto.getProcStatus());

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
    
    @Test
    public void testSendIndicatorToProductionValidationErrorQuantityIncomplete() throws Exception {

        String uuid = INDICATOR_1;
        String productionVersion = "2.000";

        IndicatorDto indicatorDtoV2 = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, productionVersion);
        assertEquals(productionVersion, indicatorDtoV2.getProductionVersion());
        assertEquals(IndicatorProcStatusEnum.DRAFT, indicatorDtoV2.getProcStatus());

        // Update to clear quantity required attributes
        indicatorDtoV2.getQuantity().setType(QuantityTypeEnum.CHANGE_RATE);
        indicatorDtoV2.getQuantity().setUnitUuid(null);
        indicatorsServiceFacade.updateIndicator(getServiceContext(), indicatorDtoV2);
        
        // Sends to production validation
        try {
            indicatorsServiceFacade.sendIndicatorToProductionValidation(getServiceContext(), uuid);
            fail("Indicator quantity incomplete");
        } catch (MetamacException e) {
            assertEquals(3, e.getExceptionItems().size());
            
            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("INDICATOR.QUANTITY.UNIT_UUID", e.getExceptionItems().get(0).getMessageParameters()[0]);
            
            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(1).getCode());
            assertEquals(1, e.getExceptionItems().get(1).getMessageParameters().length);
            assertEquals("INDICATOR.QUANTITY.IS_PERCENTAGE", e.getExceptionItems().get(1).getMessageParameters()[0]);
            
            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(2).getCode());
            assertEquals(1, e.getExceptionItems().get(2).getMessageParameters().length);
            assertEquals("INDICATOR.QUANTITY.BASE_QUANTITY_INDICATOR_UUID", e.getExceptionItems().get(2).getMessageParameters()[0]);
        }
    }

    @Test
    public void testSendIndicatorToDiffusionValidation() throws Exception {

        String uuid = INDICATOR_4;
        String versionNumber = "1.000";

        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumber);
            assertEquals(null, indicatorDto.getDiffusionVersion());
            assertEquals("1.000", indicatorDto.getProductionVersion());
            assertEquals(IndicatorProcStatusEnum.PRODUCTION_VALIDATION, indicatorDto.getProcStatus());
        }

        // Sends to diffusion validation
        IndicatorDto indicatorDtoV1Updated = indicatorsServiceFacade.sendIndicatorToDiffusionValidation(getServiceContext(), uuid);

        // Validation
        {
            assertEquals(null, indicatorDtoV1Updated.getDiffusionVersion());
            assertEquals("1.000", indicatorDtoV1Updated.getProductionVersion());
            assertEquals(IndicatorProcStatusEnum.DIFFUSION_VALIDATION, indicatorDtoV1Updated.getProcStatus());

            IndicatorsAsserts.assertEqualsDate("2011-04-04 01:02:04", indicatorDtoV1Updated.getProductionValidationDate());
            assertEquals("user1", indicatorDtoV1Updated.getProductionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), indicatorDtoV1Updated.getDiffusionValidationDate()));
            assertEquals(getServiceContext().getUserId(), indicatorDtoV1Updated.getDiffusionValidationUser());
            assertNull(indicatorDtoV1Updated.getPublicationDate());
            assertNull(indicatorDtoV1Updated.getPublicationUser());
            assertNull(indicatorDtoV1Updated.getArchiveDate());
            assertNull(indicatorDtoV1Updated.getArchiveUser());
        }
        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumber);
            assertEquals(null, indicatorDto.getDiffusionVersion());
            assertEquals("1.000", indicatorDto.getProductionVersion());
            assertEquals(IndicatorProcStatusEnum.DIFFUSION_VALIDATION, indicatorDto.getProcStatus());

            IndicatorsAsserts.assertEqualsDate("2011-04-04 01:02:04", indicatorDto.getProductionValidationDate());
            assertEquals("user1", indicatorDto.getProductionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), indicatorDto.getDiffusionValidationDate()));
            assertEquals(getServiceContext().getUserId(), indicatorDto.getDiffusionValidationUser());
            assertNull(indicatorDto.getPublicationDate());
            assertNull(indicatorDto.getPublicationUser());
            assertNull(indicatorDto.getArchiveDate());
            assertNull(indicatorDto.getArchiveUser());
        }
    }

    @Test
    public void testSendIndicatorToDiffusionValidationErrorNotExists() throws Exception {

        try {
            indicatorsServiceFacade.sendIndicatorToDiffusionValidation(getServiceContext(), NOT_EXISTS);
            fail("Indicator not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testSendIndicatorToDiffusionValidationErrorWrongProcStatusDraft() throws Exception {

        String uuid = INDICATOR_2;

        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, "1.000");
            assertEquals(IndicatorProcStatusEnum.DRAFT, indicatorDto.getProcStatus());
            assertEquals("1.000", indicatorDto.getProductionVersion());
            assertEquals(null, indicatorDto.getDiffusionVersion());
        }

        try {
            indicatorsServiceFacade.sendIndicatorToDiffusionValidation(getServiceContext(), uuid);
            fail("Indicator is not draft");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorProcStatusEnum.PRODUCTION_VALIDATION, ((IndicatorProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Test
    public void testSendIndicatorToDiffusionValidationErrorWrongProcStatusPublished() throws Exception {

        String uuid = INDICATOR_3;

        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, INDICATOR_3_VERSION);
            assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorDto.getProcStatus());
            assertEquals(null, indicatorDto.getProductionVersion());
            assertEquals(INDICATOR_3_VERSION, indicatorDto.getDiffusionVersion());
        }

        try {
            indicatorsServiceFacade.sendIndicatorToDiffusionValidation(getServiceContext(), uuid);
            fail("Indicator is not production validation");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorProcStatusEnum.PRODUCTION_VALIDATION, ((IndicatorProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Test
    public void testRejectIndicatorValidation() throws Exception {

        String uuid = INDICATOR_4;
        String versionNumber = "1.000";

        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumber);
            assertEquals(null, indicatorDto.getDiffusionVersion());
            assertEquals("1.000", indicatorDto.getProductionVersion());
            assertEquals(IndicatorProcStatusEnum.PRODUCTION_VALIDATION, indicatorDto.getProcStatus());
        }

        // Rejects validation
        IndicatorDto indicatorDtoV1Updated = indicatorsServiceFacade.rejectIndicatorValidation(getServiceContext(), uuid);

        // Validation
        {
            assertEquals(null, indicatorDtoV1Updated.getDiffusionVersion());
            assertEquals(versionNumber, indicatorDtoV1Updated.getProductionVersion());
            assertEquals(IndicatorProcStatusEnum.VALIDATION_REJECTED, indicatorDtoV1Updated.getProcStatus());

            assertNull(indicatorDtoV1Updated.getProductionValidationDate());
            assertNull(indicatorDtoV1Updated.getProductionValidationUser());
            assertNull(indicatorDtoV1Updated.getDiffusionValidationDate());
            assertNull(indicatorDtoV1Updated.getDiffusionValidationUser());
            assertNull(indicatorDtoV1Updated.getPublicationDate());
            assertNull(indicatorDtoV1Updated.getPublicationUser());
            assertNull(indicatorDtoV1Updated.getArchiveDate());
            assertNull(indicatorDtoV1Updated.getArchiveUser());
        }
        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumber);
            assertEquals(null, indicatorDto.getDiffusionVersion());
            assertEquals(versionNumber, indicatorDto.getProductionVersion());
            assertEquals(IndicatorProcStatusEnum.VALIDATION_REJECTED, indicatorDto.getProcStatus());

            assertNull(indicatorDto.getProductionValidationDate());
            assertNull(indicatorDto.getProductionValidationUser());
            assertNull(indicatorDto.getDiffusionValidationDate());
            assertNull(indicatorDto.getDiffusionValidationUser());
            assertNull(indicatorDto.getPublicationDate());
            assertNull(indicatorDto.getPublicationUser());
            assertNull(indicatorDto.getArchiveDate());
            assertNull(indicatorDto.getArchiveUser());
        }
    }

    @Test
    public void testRejectIndicatorValidationInDiffusionValidation() throws Exception {

        String uuid = INDICATOR_5;
        String versionNumber = "1.000";

        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumber);
            assertEquals(null, indicatorDto.getDiffusionVersion());
            assertEquals("1.000", indicatorDto.getProductionVersion());
            assertEquals(IndicatorProcStatusEnum.DIFFUSION_VALIDATION, indicatorDto.getProcStatus());
        }

        // Rejects validation
        indicatorsServiceFacade.rejectIndicatorValidation(getServiceContext(), uuid);

        // Validation
        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumber);
            assertEquals(null, indicatorDto.getDiffusionVersion());
            assertEquals("1.000", indicatorDto.getProductionVersion());
            assertEquals(IndicatorProcStatusEnum.VALIDATION_REJECTED, indicatorDto.getProcStatus());
        }
    }

    @Test
    public void testRejectIndicatorValidationErrorNotExists() throws Exception {

        try {
            indicatorsServiceFacade.rejectIndicatorValidation(getServiceContext(), NOT_EXISTS);
            fail("Indicator not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRejectIndicatorValidationErrorWrongProcStatusProduction() throws Exception {

        String uuid = INDICATOR_2;

        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, "1.000");
            assertEquals(IndicatorProcStatusEnum.DRAFT, indicatorDto.getProcStatus());
            assertEquals("1.000", indicatorDto.getProductionVersion());
            assertEquals(null, indicatorDto.getDiffusionVersion());
        }

        try {
            indicatorsServiceFacade.rejectIndicatorValidation(getServiceContext(), uuid);
            fail("Indicator is not in validation");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorProcStatusEnum.PRODUCTION_VALIDATION, ((IndicatorProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(IndicatorProcStatusEnum.DIFFUSION_VALIDATION, ((IndicatorProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
        }
    }

    @Test
    public void testRejectIndicatorValidationErrorWrongProcStatusDiffusion() throws Exception {

        String uuid = INDICATOR_3;

        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, INDICATOR_3_VERSION);
            assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorDto.getProcStatus());
            assertEquals(null, indicatorDto.getProductionVersion());
            assertEquals(INDICATOR_3_VERSION, indicatorDto.getDiffusionVersion());
        }

        try {
            indicatorsServiceFacade.rejectIndicatorValidation(getServiceContext(), uuid);
            fail("Indicator is not in validation");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorProcStatusEnum.PRODUCTION_VALIDATION, ((IndicatorProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(IndicatorProcStatusEnum.DIFFUSION_VALIDATION, ((IndicatorProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
        }
    }

    @Test
    public void testPublishIndicator() throws Exception {

        String uuid = INDICATOR_5;
        String versionNumber = "1.000";

        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumber);
            assertEquals(versionNumber, indicatorDto.getProductionVersion());
            assertEquals(null, indicatorDto.getDiffusionVersion());
            assertEquals(IndicatorProcStatusEnum.DIFFUSION_VALIDATION, indicatorDto.getProcStatus());
        }

        // Publish
        IndicatorDto indicatorDtoV1Updated = indicatorsServiceFacade.publishIndicator(getServiceContext(), uuid);

        // Validation
        {
            assertEquals(null, indicatorDtoV1Updated.getProductionVersion());
            assertEquals(versionNumber, indicatorDtoV1Updated.getDiffusionVersion());
            assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorDtoV1Updated.getProcStatus());

            IndicatorsAsserts.assertEqualsDate("2011-06-06 01:02:04", indicatorDtoV1Updated.getProductionValidationDate());
            assertEquals("user1", indicatorDtoV1Updated.getProductionValidationUser());
            IndicatorsAsserts.assertEqualsDate("2011-07-07 03:02:04", indicatorDtoV1Updated.getDiffusionValidationDate());
            assertEquals("user2", indicatorDtoV1Updated.getDiffusionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), indicatorDtoV1Updated.getPublicationDate()));
            assertEquals(getServiceContext().getUserId(), indicatorDtoV1Updated.getPublicationUser());
            assertNull(indicatorDtoV1Updated.getArchiveDate());
            assertNull(indicatorDtoV1Updated.getArchiveUser());
        }
        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumber);
            assertEquals(null, indicatorDto.getProductionVersion());
            assertEquals(versionNumber, indicatorDto.getDiffusionVersion());
            assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorDto.getProcStatus());

            IndicatorsAsserts.assertEqualsDate("2011-06-06 01:02:04", indicatorDto.getProductionValidationDate());
            assertEquals("user1", indicatorDto.getProductionValidationUser());
            IndicatorsAsserts.assertEqualsDate("2011-07-07 03:02:04", indicatorDto.getDiffusionValidationDate());
            assertEquals("user2", indicatorDto.getDiffusionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), indicatorDto.getPublicationDate()));
            assertEquals(getServiceContext().getUserId(), indicatorDto.getPublicationUser());
            assertNull(indicatorDto.getArchiveDate());
            assertNull(indicatorDto.getArchiveUser());
        }
    }

    @Test
    public void testPublishIndicatorWithPublishedVersion() throws Exception {

        String uuid = INDICATOR_6;
        String diffusionVersionBefore = "1.000"; // will be deleted when publish current version in diffusion validation
        String productionVersionBefore = "2.000"; // will be published

        {
            IndicatorDto indicatorDtoV1 = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, diffusionVersionBefore);
            IndicatorDto indicatorDtoV2 = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, productionVersionBefore);
            assertEquals(productionVersionBefore, indicatorDtoV1.getProductionVersion());
            assertEquals(diffusionVersionBefore, indicatorDtoV1.getDiffusionVersion());
            assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorDtoV1.getProcStatus());
            assertEquals(IndicatorProcStatusEnum.DIFFUSION_VALIDATION, indicatorDtoV2.getProcStatus());
        }

        // Publish
        indicatorsServiceFacade.publishIndicator(getServiceContext(), uuid);

        // Validation
        {
            // Version 1 already not exists
            try {
                indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, diffusionVersionBefore);
                fail("Indicator version not exists");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.INDICATOR_VERSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
                assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
                assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
                assertEquals(diffusionVersionBefore, e.getExceptionItems().get(0).getMessageParameters()[1]);
            }

            // Actual version in diffusion is version 2
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, productionVersionBefore);
            assertEquals(null, indicatorDto.getProductionVersion());
            assertEquals("2.000", indicatorDto.getDiffusionVersion());
            assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorDto.getProcStatus());
        }
    }

    @Test
    public void testPublishIndicatorWithArchivedVersion() throws Exception {

        String uuid = INDICATOR_7;
        String diffusionVersionBefore = "1.000"; // will be deleted when publish current version in diffusion validation
        String productionVersionBefore = "2.000"; // will be published

        {
            IndicatorDto indicatorDtoV1 = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, diffusionVersionBefore);
            IndicatorDto indicatorDtoV2 = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, productionVersionBefore);
            assertEquals(productionVersionBefore, indicatorDtoV1.getProductionVersion());
            assertEquals(diffusionVersionBefore, indicatorDtoV1.getDiffusionVersion());
            assertEquals(IndicatorProcStatusEnum.ARCHIVED, indicatorDtoV1.getProcStatus());
            assertEquals(IndicatorProcStatusEnum.DIFFUSION_VALIDATION, indicatorDtoV2.getProcStatus());
        }

        // Publish
        indicatorsServiceFacade.publishIndicator(getServiceContext(), uuid);

        // Validation
        {
            // Version 1 already not exists
            try {
                indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, diffusionVersionBefore);
                fail("Indicator version not exists");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.INDICATOR_VERSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
                assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
                assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
                assertEquals(diffusionVersionBefore, e.getExceptionItems().get(0).getMessageParameters()[1]);
            }

            // Actual version in diffusion is version 2
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, productionVersionBefore);
            assertEquals(null, indicatorDto.getProductionVersion());
            assertEquals("2.000", indicatorDto.getDiffusionVersion());
            assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorDto.getProcStatus());
        }
    }

    @Test
    public void testPublishIndicatorErrorNotExists() throws Exception {

        try {
            indicatorsServiceFacade.publishIndicator(getServiceContext(), NOT_EXISTS);
            fail("Indicator not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testPublishIndicatorErrorWrongProcStatusProduction() throws Exception {

        String uuid = INDICATOR_2;

        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, "1.000");
            assertEquals(IndicatorProcStatusEnum.DRAFT, indicatorDto.getProcStatus());
            assertEquals("1.000", indicatorDto.getProductionVersion());
            assertEquals(null, indicatorDto.getDiffusionVersion());
        }

        try {
            indicatorsServiceFacade.publishIndicator(getServiceContext(), uuid);
            fail("Indicator is not diffusion validation");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorProcStatusEnum.DIFFUSION_VALIDATION, ((IndicatorProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(IndicatorProcStatusEnum.PUBLICATION_FAILED, ((IndicatorProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
        }
    }

    @Test
    public void testPublishIndicatorErrorWrongProcStatusPublished() throws Exception {

        String uuid = INDICATOR_3;

        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, INDICATOR_3_VERSION);
            assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorDto.getProcStatus());
            assertEquals(null, indicatorDto.getProductionVersion());
            assertEquals(INDICATOR_3_VERSION, indicatorDto.getDiffusionVersion());
        }

        try {
            indicatorsServiceFacade.publishIndicator(getServiceContext(), uuid);
            fail("Indicator is not diffusion validation");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorProcStatusEnum.DIFFUSION_VALIDATION, ((IndicatorProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(IndicatorProcStatusEnum.PUBLICATION_FAILED, ((IndicatorProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
        }
    }

    @Test
    public void testPublishIndicatorErrorNumeratorNotPublished() throws Exception {

        String uuid = INDICATOR_5;

        // Change indicator to fraction with numerator in draft
        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, null);
        indicatorDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorDto.getQuantity().setType(QuantityTypeEnum.FRACTION);
        indicatorDto.getQuantity().setNumeratorIndicatorUuid(INDICATOR_2);
        indicatorsServiceFacade.updateIndicator(getServiceContext(), indicatorDto);

        // Publish
        try {
            indicatorsServiceFacade.publishIndicator(getServiceContext(), uuid);
            fail("Numerator non published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_MUST_HAVE_ALL_LINKED_INDICATORS_PUBLISHED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(1, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1]).length);
            assertEquals(indicatorDto.getQuantity().getNumeratorIndicatorUuid(), ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Test
    public void testPublishIndicatorErrorInDatasourceNotPublished() throws Exception {

        String uuid = INDICATOR_5;
        String uuidPublished1 = INDICATOR_1;
        String uuidNotPublished2 = INDICATOR_2;
        String uuidNotPublished4 = INDICATOR_4;
        String uuidNotPublished7 = INDICATOR_7;

        // Change datasource to fraction with numerator in draft
        List<DataSourceDto> datasources = indicatorsServiceFacade.findDataSources(getServiceContext(), uuid, "1.000");
        {
            DataSourceDto dataSourceDto = datasources.get(0);
            dataSourceDto.getAnnualRate().getQuantity().setType(QuantityTypeEnum.CHANGE_RATE);
            dataSourceDto.getAnnualRate().getQuantity().setNumeratorIndicatorUuid(uuidNotPublished2);
            dataSourceDto.getAnnualRate().getQuantity().setDenominatorIndicatorUuid(uuidNotPublished7);
            dataSourceDto.getAnnualRate().getQuantity().setIsPercentage(Boolean.TRUE);
            dataSourceDto.getAnnualRate().getQuantity().setBaseQuantityIndicatorUuid(uuid);
            dataSourceDto.getInterperiodRate().getQuantity().setType(QuantityTypeEnum.CHANGE_RATE);
            dataSourceDto.getInterperiodRate().getQuantity().setNumeratorIndicatorUuid(uuidNotPublished4);
            dataSourceDto.getInterperiodRate().getQuantity().setIsPercentage(Boolean.TRUE);
            dataSourceDto.getInterperiodRate().getQuantity().setBaseQuantityIndicatorUuid(uuid);
            indicatorsServiceFacade.updateDataSource(getServiceContext(), dataSourceDto);
        }
        {
            DataSourceDto dataSourceDto = datasources.get(1);
            dataSourceDto.getAnnualRate().getQuantity().setType(QuantityTypeEnum.CHANGE_RATE);
            dataSourceDto.getAnnualRate().getQuantity().setNumeratorIndicatorUuid(uuidPublished1);
            dataSourceDto.getAnnualRate().getQuantity().setIsPercentage(Boolean.TRUE);
            dataSourceDto.getAnnualRate().getQuantity().setBaseQuantityIndicatorUuid(uuid);
            indicatorsServiceFacade.updateDataSource(getServiceContext(), dataSourceDto);
        }

        // Publish
        try {
            indicatorsServiceFacade.publishIndicator(getServiceContext(), uuid);
            fail("Indicators non published in datasource");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_MUST_HAVE_ALL_LINKED_INDICATORS_PUBLISHED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(3, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1]).length);
            assertEquals(uuidNotPublished2, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(uuidNotPublished4, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
            assertEquals(uuidNotPublished7, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[2]);
        }
    }

    @Test
    public void testArchiveIndicator() throws Exception {

        String uuid = INDICATOR_3;
        String versionNumber = INDICATOR_3_VERSION;

        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumber);
            assertEquals(null, indicatorDto.getProductionVersion());
            assertEquals(INDICATOR_3_VERSION, indicatorDto.getDiffusionVersion());
            assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorDto.getProcStatus());
        }

        // Archive
        IndicatorDto indicatorDtoUpdated = indicatorsServiceFacade.archiveIndicator(getServiceContext(), uuid);

        // Validation
        {
            assertEquals(null, indicatorDtoUpdated.getProductionVersion());
            assertEquals(INDICATOR_3_VERSION, indicatorDtoUpdated.getDiffusionVersion());
            assertEquals(IndicatorProcStatusEnum.ARCHIVED, indicatorDtoUpdated.getProcStatus());

            IndicatorsAsserts.assertEqualsDate("2011-03-03 01:02:04", indicatorDtoUpdated.getProductionValidationDate());
            assertEquals("user1", indicatorDtoUpdated.getProductionValidationUser());
            IndicatorsAsserts.assertEqualsDate("2011-04-04 03:02:04", indicatorDtoUpdated.getDiffusionValidationDate());
            assertEquals("user2", indicatorDtoUpdated.getDiffusionValidationUser());
            IndicatorsAsserts.assertEqualsDate("2011-05-05 04:02:04", indicatorDtoUpdated.getPublicationDate());
            assertEquals("user3", indicatorDtoUpdated.getPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), indicatorDtoUpdated.getArchiveDate()));
            assertEquals(getServiceContext().getUserId(), indicatorDtoUpdated.getArchiveUser());
        }
        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, versionNumber);
            assertEquals(null, indicatorDto.getProductionVersion());
            assertEquals(INDICATOR_3_VERSION, indicatorDto.getDiffusionVersion());
            assertEquals(IndicatorProcStatusEnum.ARCHIVED, indicatorDto.getProcStatus());

            IndicatorsAsserts.assertEqualsDate("2011-03-03 01:02:04", indicatorDto.getProductionValidationDate());
            assertEquals("user1", indicatorDto.getProductionValidationUser());
            IndicatorsAsserts.assertEqualsDate("2011-04-04 03:02:04", indicatorDto.getDiffusionValidationDate());
            assertEquals("user2", indicatorDto.getDiffusionValidationUser());
            IndicatorsAsserts.assertEqualsDate("2011-05-05 04:02:04", indicatorDto.getPublicationDate());
            assertEquals("user3", indicatorDto.getPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), indicatorDto.getArchiveDate()));
            assertEquals(getServiceContext().getUserId(), indicatorDto.getArchiveUser());
        }
    }

    @Test
    public void testArchiveIndicatorWithProductionVersion() throws Exception {

        String uuid = INDICATOR_1;
        String diffusionVersion = "1.000";
        String productionVersion = "2.000";

        {
            IndicatorDto indicatorDtoV1 = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, diffusionVersion);
            IndicatorDto indicatorDtoV2 = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, productionVersion);
            assertEquals(productionVersion, indicatorDtoV1.getProductionVersion());
            assertEquals(diffusionVersion, indicatorDtoV1.getDiffusionVersion());
            assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorDtoV1.getProcStatus());
            assertEquals(IndicatorProcStatusEnum.DRAFT, indicatorDtoV2.getProcStatus());
        }

        // Archive
        indicatorsServiceFacade.archiveIndicator(getServiceContext(), uuid);

        // Validation
        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, diffusionVersion);
            assertEquals(productionVersion, indicatorDto.getProductionVersion());
            assertEquals(diffusionVersion, indicatorDto.getDiffusionVersion());
            assertEquals(IndicatorProcStatusEnum.ARCHIVED, indicatorDto.getProcStatus());
        }
    }

    @Test
    public void testArchiveIndicatorErrorNotExists() throws Exception {

        try {
            indicatorsServiceFacade.archiveIndicator(getServiceContext(), NOT_EXISTS);
            fail("Indicator not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testArchiveIndicatorErrorWrongProcStatusProduction() throws Exception {

        String uuid = INDICATOR_2;

        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, "1.000");
            assertEquals(IndicatorProcStatusEnum.DRAFT, indicatorDto.getProcStatus());
            assertEquals("1.000", indicatorDto.getProductionVersion());
            assertEquals(null, indicatorDto.getDiffusionVersion());
        }

        try {
            indicatorsServiceFacade.archiveIndicator(getServiceContext(), uuid);
            fail("Indicator is not published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorProcStatusEnum.PUBLISHED, ((IndicatorProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Test
    public void testArchiveIndicatorErrorWrongProcStatusArchived() throws Exception {

        String uuid = INDICATOR_8;

        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, "1.000");
            assertEquals(IndicatorProcStatusEnum.ARCHIVED, indicatorDto.getProcStatus());
            assertEquals(null, indicatorDto.getProductionVersion());
            assertEquals("1.000", indicatorDto.getDiffusionVersion());
        }

        try {
            indicatorsServiceFacade.archiveIndicator(getServiceContext(), uuid);
            fail("Indicator is not published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorProcStatusEnum.PUBLISHED, ((IndicatorProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Test
    public void testVersioningIndicator() throws Exception {

        String uuid = INDICATOR_3;
        String newVersionExpected = "12.000";

        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, null);
        assertEquals(INDICATOR_3_VERSION, indicatorDto.getVersionNumber());
        assertEquals(null, indicatorDto.getProductionVersion());
        assertEquals(INDICATOR_3_VERSION, indicatorDto.getDiffusionVersion());

        IndicatorDto indicatorDtoVersioned = indicatorsServiceFacade.versioningIndicator(getServiceContext(), uuid, VersionTypeEnum.MAJOR);

        // Validate
        assertEquals(newVersionExpected, indicatorDtoVersioned.getVersionNumber());
        assertEquals(newVersionExpected, indicatorDtoVersioned.getProductionVersion());
        assertEquals(INDICATOR_3_VERSION, indicatorDtoVersioned.getDiffusionVersion());
        IndicatorsAsserts.assertEqualsIndicator(indicatorDto, indicatorDtoVersioned);

        {
            IndicatorDto indicatorDtoProduction = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, newVersionExpected);
            IndicatorDto indicatorDtoDiffusion = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, INDICATOR_3_VERSION);

            IndicatorsAsserts.assertEqualsIndicator(indicatorDtoDiffusion, indicatorDtoProduction);

            assertEquals(IndicatorProcStatusEnum.DRAFT, indicatorDtoProduction.getProcStatus());
            assertEquals(newVersionExpected, indicatorDtoProduction.getVersionNumber());
            assertEquals(newVersionExpected, indicatorDtoProduction.getProductionVersion());
            assertEquals(INDICATOR_3_VERSION, indicatorDtoProduction.getDiffusionVersion());
            // Data sources
            List<DataSourceDto> dataSources = indicatorsServiceFacade.findDataSources(getServiceContext(), indicatorDtoProduction.getUuid(), indicatorDtoProduction.getProductionVersion());
            assertEquals(1, dataSources.size());
            assertEquals("query-gpe Indicator-3-v1-DataSource-1", dataSources.get(0).getDataGpeUuid());
            assertEquals("px Indicator-3-v1-DataSource-1", dataSources.get(0).getPxUri());
            assertEquals("time v Indicator-3-v1-DataSource-1", dataSources.get(0).getTimeVariable());
            assertEquals("geographical v Indicator-3-v1-DataSource-1", dataSources.get(0).getGeographicalVariable());
            assertEquals(1, dataSources.get(0).getOtherVariables().size());
            assertEquals("variable Indicator-3-v1-DataSource-1-Var-1", dataSources.get(0).getOtherVariables().get(0).getVariable());
            assertEquals("category Indicator-3-v1-DataSource-1-Var-1", dataSources.get(0).getOtherVariables().get(0).getCategory());
            assertEquals("MethodOfAnnual", dataSources.get(0).getAnnualRate().getMethod());
            assertEquals(QUANTITY_UNIT_1, dataSources.get(0).getAnnualRate().getQuantity().getUnitUuid());
            assertEquals("MethodOfInterperiod", dataSources.get(0).getInterperiodRate().getMethod());
            assertEquals(QUANTITY_UNIT_2, dataSources.get(0).getInterperiodRate().getQuantity().getUnitUuid());

            assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorDtoDiffusion.getProcStatus());
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

        IndicatorDto indicatorDtoVersioned = indicatorsServiceFacade.versioningIndicator(getServiceContext(), uuid, VersionTypeEnum.MINOR);

        // Validate
        assertEquals(newVersionExpected, indicatorDtoVersioned.getVersionNumber());
        assertEquals(newVersionExpected, indicatorDtoVersioned.getProductionVersion());
        assertEquals(INDICATOR_3_VERSION, indicatorDtoVersioned.getDiffusionVersion());
        IndicatorsAsserts.assertEqualsIndicator(indicatorDto, indicatorDtoVersioned);

        {
            IndicatorDto indicatorDtoProduction = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, newVersionExpected);
            IndicatorDto indicatorDtoDiffusion = indicatorsServiceFacade.retrieveIndicator(getServiceContext(), uuid, INDICATOR_3_VERSION);

            IndicatorsAsserts.assertEqualsIndicator(indicatorDtoDiffusion, indicatorDtoProduction);

            assertEquals(IndicatorProcStatusEnum.DRAFT, indicatorDtoProduction.getProcStatus());
            assertEquals(newVersionExpected, indicatorDtoProduction.getVersionNumber());
            assertEquals(newVersionExpected, indicatorDtoProduction.getProductionVersion());
            assertEquals(INDICATOR_3_VERSION, indicatorDtoProduction.getDiffusionVersion());

            assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorDtoDiffusion.getProcStatus());
            assertEquals(INDICATOR_3_VERSION, indicatorDtoDiffusion.getVersionNumber());
            assertEquals(newVersionExpected, indicatorDtoDiffusion.getProductionVersion());
            assertEquals(INDICATOR_3_VERSION, indicatorDtoDiffusion.getDiffusionVersion());
        }
    }

    @Test
    public void testVersioningIndicatorErrorNotExists() throws Exception {

        try {
            indicatorsServiceFacade.versioningIndicator(getServiceContext(), NOT_EXISTS, VersionTypeEnum.MINOR);
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
            indicatorsServiceFacade.versioningIndicator(getServiceContext(), uuid, VersionTypeEnum.MINOR);
            fail("Indicator already exists in production");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorProcStatusEnum.PUBLISHED, ((IndicatorProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(IndicatorProcStatusEnum.ARCHIVED, ((IndicatorProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
        }
    }

    @Test
    public void testFindIndicators() throws Exception {

        // Retrieve last versions...
        List<IndicatorDto> indicatorsDto = indicatorsServiceFacade.findIndicators(getServiceContext(), null);
        assertEquals(10, indicatorsDto.size());

        assertEquals(INDICATOR_1, indicatorsDto.get(0).getUuid());
        assertEquals(IndicatorProcStatusEnum.DRAFT, indicatorsDto.get(0).getProcStatus());

        assertEquals(INDICATOR_2, indicatorsDto.get(1).getUuid());
        assertEquals(IndicatorProcStatusEnum.DRAFT, indicatorsDto.get(1).getProcStatus());

        assertEquals(INDICATOR_3, indicatorsDto.get(2).getUuid());
        assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorsDto.get(2).getProcStatus());

        assertEquals(INDICATOR_4, indicatorsDto.get(3).getUuid());
        assertEquals(IndicatorProcStatusEnum.PRODUCTION_VALIDATION, indicatorsDto.get(3).getProcStatus());

        assertEquals(INDICATOR_5, indicatorsDto.get(4).getUuid());
        assertEquals(IndicatorProcStatusEnum.DIFFUSION_VALIDATION, indicatorsDto.get(4).getProcStatus());

        assertEquals(INDICATOR_6, indicatorsDto.get(5).getUuid());
        assertEquals(IndicatorProcStatusEnum.DIFFUSION_VALIDATION, indicatorsDto.get(5).getProcStatus());

        assertEquals(INDICATOR_7, indicatorsDto.get(6).getUuid());
        assertEquals(IndicatorProcStatusEnum.DIFFUSION_VALIDATION, indicatorsDto.get(6).getProcStatus());

        assertEquals(INDICATOR_8, indicatorsDto.get(7).getUuid());
        assertEquals(IndicatorProcStatusEnum.ARCHIVED, indicatorsDto.get(7).getProcStatus());

        assertEquals(INDICATOR_9, indicatorsDto.get(8).getUuid());
        assertEquals(IndicatorProcStatusEnum.VALIDATION_REJECTED, indicatorsDto.get(8).getProcStatus());

        assertEquals(INDICATOR_10, indicatorsDto.get(9).getUuid());
        assertEquals(IndicatorProcStatusEnum.DRAFT, indicatorsDto.get(9).getProcStatus());
    }

    @Test
    public void testFindIndicatorsWithSubject() throws Exception {

        // Retrieve last versions with subject 1
        IndicatorsCriteria criteria = new IndicatorsCriteria();
        criteria.setConjunctionRestriction(new IndicatorsCriteriaConjunctionRestriction());
        criteria.getConjunctionRestriction().getRestrictions().add(new IndicatorsCriteriaPropertyRestriction(IndicatorCriteriaPropertyEnum.SUBJECT_CODE.name(), SUBJECT_3));

        List<IndicatorDto> indicatorsDto = indicatorsServiceFacade.findIndicators(getServiceContext(), criteria);
        assertEquals(8, indicatorsDto.size());

        assertEquals(INDICATOR_3, indicatorsDto.get(0).getUuid());
        assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorsDto.get(0).getProcStatus());

        assertEquals(INDICATOR_4, indicatorsDto.get(1).getUuid());
        assertEquals(IndicatorProcStatusEnum.PRODUCTION_VALIDATION, indicatorsDto.get(1).getProcStatus());

        assertEquals(INDICATOR_5, indicatorsDto.get(2).getUuid());
        assertEquals(IndicatorProcStatusEnum.DIFFUSION_VALIDATION, indicatorsDto.get(2).getProcStatus());

        assertEquals(INDICATOR_6, indicatorsDto.get(3).getUuid());
        assertEquals(IndicatorProcStatusEnum.DIFFUSION_VALIDATION, indicatorsDto.get(3).getProcStatus());

        assertEquals(INDICATOR_7, indicatorsDto.get(4).getUuid());
        assertEquals(IndicatorProcStatusEnum.DIFFUSION_VALIDATION, indicatorsDto.get(4).getProcStatus());

        assertEquals(INDICATOR_8, indicatorsDto.get(5).getUuid());
        assertEquals(IndicatorProcStatusEnum.ARCHIVED, indicatorsDto.get(5).getProcStatus());

        assertEquals(INDICATOR_9, indicatorsDto.get(6).getUuid());
        assertEquals(IndicatorProcStatusEnum.VALIDATION_REJECTED, indicatorsDto.get(6).getProcStatus());

        assertEquals(INDICATOR_10, indicatorsDto.get(7).getUuid());
        assertEquals(IndicatorProcStatusEnum.DRAFT, indicatorsDto.get(7).getProcStatus());
    }

    @Test
    public void testFindIndicatorsErrorCriteriaIncorrectUsingPropertyInternal() throws Exception {

        IndicatorsCriteria criteria = new IndicatorsCriteria();
        criteria.setConjunctionRestriction(new IndicatorsCriteriaConjunctionRestriction());
        criteria.getConjunctionRestriction().getRestrictions().add(new IndicatorsCriteriaPropertyRestriction(IndicatorCriteriaPropertyInternalEnum.IS_LAST_VERSION.name(), Boolean.TRUE));
        
        try {
            indicatorsServiceFacade.findIndicators(getServiceContext(), criteria);
            fail("criteria incorrecto");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("CRITERIA", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testFindIndicatorsPublished() throws Exception {

        List<IndicatorDto> indicatorsDto = indicatorsServiceFacade.findIndicatorsPublished(getServiceContext(), null);
        assertEquals(3, indicatorsDto.size());

        assertEquals(INDICATOR_1, indicatorsDto.get(0).getUuid());
        assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorsDto.get(0).getProcStatus());

        assertEquals(INDICATOR_3, indicatorsDto.get(1).getUuid());
        assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorsDto.get(1).getProcStatus());

        assertEquals(INDICATOR_6, indicatorsDto.get(2).getUuid());
        assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorsDto.get(2).getProcStatus());
    }

    @Test
    public void testFindIndicatorsPublishedWithSubject() throws Exception {

        // Retrieve last versions with subject 1
        IndicatorsCriteria criteria = new IndicatorsCriteria();
        criteria.setConjunctionRestriction(new IndicatorsCriteriaConjunctionRestriction());
        criteria.getConjunctionRestriction().getRestrictions().add(new IndicatorsCriteriaPropertyRestriction(IndicatorCriteriaPropertyEnum.SUBJECT_CODE.name(), SUBJECT_3));
        
        List<IndicatorDto> indicatorsDto = indicatorsServiceFacade.findIndicatorsPublished(getServiceContext(), criteria);
        assertEquals(2, indicatorsDto.size());

        assertEquals(INDICATOR_3, indicatorsDto.get(0).getUuid());
        assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorsDto.get(0).getProcStatus());

        assertEquals(INDICATOR_6, indicatorsDto.get(1).getUuid());
        assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorsDto.get(1).getProcStatus());
    }

    @Test
    public void testRetrieveDataSource() throws Exception {

        DataSourceDto dataSourceDto = indicatorsServiceFacade.retrieveDataSource(getServiceContext(), DATA_SOURCE_1_INDICATOR_1_V2);

        assertNotNull(dataSourceDto);
        assertEquals(DATA_SOURCE_1_INDICATOR_1_V2, dataSourceDto.getUuid());
        assertEquals("query-gpe Indicator-1-v2-DataSource-1", dataSourceDto.getDataGpeUuid());
        assertEquals("px Indicator-1-v2-DataSource-1", dataSourceDto.getPxUri());
        assertEquals("time v Indicator-1-v2-DataSource-1", dataSourceDto.getTimeVariable());
        assertEquals("geographical v Indicator-1-v2-DataSource-1", dataSourceDto.getGeographicalVariable());

        assertEquals(RateDerivationMethodTypeEnum.CALCULATE, dataSourceDto.getInterperiodRate().getMethodType());
        assertEquals("MethodOfInterperiod", dataSourceDto.getInterperiodRate().getMethod());
        assertEquals(RateDerivationRoundingEnum.UPWARD, dataSourceDto.getInterperiodRate().getRounding());
        assertEquals(QuantityTypeEnum.AMOUNT, dataSourceDto.getInterperiodRate().getQuantity().getType());
        assertEquals(QUANTITY_UNIT_1, dataSourceDto.getInterperiodRate().getQuantity().getUnitUuid());

        assertEquals(RateDerivationMethodTypeEnum.LOAD, dataSourceDto.getAnnualRate().getMethodType());
        assertEquals("MethodOfAnnual", dataSourceDto.getAnnualRate().getMethod());
        assertEquals(RateDerivationRoundingEnum.DOWN, dataSourceDto.getAnnualRate().getRounding());
        assertEquals(QuantityTypeEnum.AMOUNT, dataSourceDto.getAnnualRate().getQuantity().getType());
        assertEquals(QUANTITY_UNIT_2, dataSourceDto.getAnnualRate().getQuantity().getUnitUuid());

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

    @Test
    public void testCreateDataSource() throws Exception {

        // Create dataSource
        DataSourceDto dataSourceDto = new DataSourceDto();
        dataSourceDto.setDataGpeUuid("queryGpe1");
        dataSourceDto.setPxUri("px1");
        dataSourceDto.setTimeVariable("timeVariable1");
        dataSourceDto.setGeographicalVariable("geographicalVariable1");
        DataSourceVariableDto dataSourceVariableDto1 = new DataSourceVariableDto();
        dataSourceVariableDto1.setVariable("variable1");
        dataSourceVariableDto1.setCategory("category1");
        dataSourceDto.addOtherVariable(dataSourceVariableDto1);
        DataSourceVariableDto dataSourceVariableDto2 = new DataSourceVariableDto();
        dataSourceVariableDto2.setVariable("variable2");
        dataSourceVariableDto2.setCategory("category2");
        dataSourceDto.addOtherVariable(dataSourceVariableDto2);
        dataSourceDto.setInterperiodRate(new RateDerivationDto());
        dataSourceDto.getInterperiodRate().setMethodType(RateDerivationMethodTypeEnum.CALCULATE);
        dataSourceDto.getInterperiodRate().setMethod("Method1");
        dataSourceDto.getInterperiodRate().setRounding(RateDerivationRoundingEnum.DOWN);
        dataSourceDto.getInterperiodRate().setQuantity(new QuantityDto());
        dataSourceDto.getInterperiodRate().getQuantity().setType(QuantityTypeEnum.AMOUNT);
        dataSourceDto.getInterperiodRate().getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        dataSourceDto.getInterperiodRate().getQuantity().setDecimalPlaces(Integer.valueOf(2));
        dataSourceDto.setAnnualRate(new RateDerivationDto());
        dataSourceDto.getAnnualRate().setMethodType(RateDerivationMethodTypeEnum.LOAD);
        dataSourceDto.getAnnualRate().setMethod("Method2");
        dataSourceDto.getAnnualRate().setRounding(RateDerivationRoundingEnum.UPWARD);
        dataSourceDto.getAnnualRate().setQuantity(new QuantityDto());
        dataSourceDto.getAnnualRate().getQuantity().setType(QuantityTypeEnum.CHANGE_RATE);
        dataSourceDto.getAnnualRate().getQuantity().setUnitUuid(QUANTITY_UNIT_2);
        dataSourceDto.getAnnualRate().getQuantity().setNumeratorIndicatorUuid(INDICATOR_3);
        dataSourceDto.getAnnualRate().getQuantity().setIsPercentage(Boolean.TRUE);
        dataSourceDto.getAnnualRate().getQuantity().setBaseQuantityIndicatorUuid(INDICATOR_1);

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
    public void testCreateDataSourceWithoutVariables() throws Exception {

        // Create dataSource
        DataSourceDto dataSourceDto = new DataSourceDto();
        dataSourceDto.setDataGpeUuid("queryGpe1");
        dataSourceDto.setPxUri("px1");
        dataSourceDto.setTimeValue("2010");
        dataSourceDto.setGeographicalValueUuid(GEOGRAPHICAL_VALUE_1);
        DataSourceVariableDto dataSourceVariableDto1 = new DataSourceVariableDto();
        dataSourceVariableDto1.setVariable("variable1");
        dataSourceVariableDto1.setCategory("category1");
        dataSourceDto.addOtherVariable(dataSourceVariableDto1);
        DataSourceVariableDto dataSourceVariableDto2 = new DataSourceVariableDto();
        dataSourceVariableDto2.setVariable("variable2");
        dataSourceVariableDto2.setCategory("category2");
        dataSourceDto.addOtherVariable(dataSourceVariableDto2);
        dataSourceDto.setInterperiodRate(new RateDerivationDto());
        dataSourceDto.getInterperiodRate().setMethodType(RateDerivationMethodTypeEnum.CALCULATE);
        dataSourceDto.getInterperiodRate().setMethod("Method1");
        dataSourceDto.getInterperiodRate().setRounding(RateDerivationRoundingEnum.DOWN);
        dataSourceDto.getInterperiodRate().setQuantity(new QuantityDto());
        dataSourceDto.getInterperiodRate().getQuantity().setType(QuantityTypeEnum.AMOUNT);
        dataSourceDto.getInterperiodRate().getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        dataSourceDto.getInterperiodRate().getQuantity().setDecimalPlaces(Integer.valueOf(2));
        dataSourceDto.setAnnualRate(new RateDerivationDto());
        dataSourceDto.getAnnualRate().setMethodType(RateDerivationMethodTypeEnum.LOAD);
        dataSourceDto.getAnnualRate().setMethod("Method2");
        dataSourceDto.getAnnualRate().setRounding(RateDerivationRoundingEnum.UPWARD);
        dataSourceDto.getAnnualRate().setQuantity(new QuantityDto());
        dataSourceDto.getAnnualRate().getQuantity().setType(QuantityTypeEnum.CHANGE_RATE);
        dataSourceDto.getAnnualRate().getQuantity().setUnitUuid(QUANTITY_UNIT_2);
        dataSourceDto.getAnnualRate().getQuantity().setNumeratorIndicatorUuid(INDICATOR_3);
        dataSourceDto.getAnnualRate().getQuantity().setIsPercentage(Boolean.TRUE);
        dataSourceDto.getAnnualRate().getQuantity().setBaseQuantityIndicatorUuid(INDICATOR_1);

        String uuidIndicator = INDICATOR_1;
        DataSourceDto dataSourceDtoCreated = indicatorsServiceFacade.createDataSource(getServiceContext(), uuidIndicator, dataSourceDto);
        assertNotNull(dataSourceDtoCreated.getUuid());

        // Retrieve data source
        DataSourceDto dataSourceDtoRetrieved = indicatorsServiceFacade.retrieveDataSource(getServiceContext(), dataSourceDtoCreated.getUuid());
        IndicatorsAsserts.assertEqualsDataSource(dataSourceDto, dataSourceDtoRetrieved);
    }

    @Test
    public void testCreateDataSourceErrorParametersRequired() throws Exception {

        DataSourceDto dataSourceDto = new DataSourceDto();
        dataSourceDto.setDataGpeUuid(null);
        dataSourceDto.setPxUri("px1");
        dataSourceDto.setInterperiodRate(new RateDerivationDto());
        dataSourceDto.setAnnualRate(new RateDerivationDto());
        dataSourceDto.getAnnualRate().setMethodType(RateDerivationMethodTypeEnum.CALCULATE);
        dataSourceDto.getAnnualRate().setQuantity(new QuantityDto());

        try {
            indicatorsServiceFacade.createDataSource(getServiceContext(), INDICATOR_1, dataSourceDto);
            fail("parameters required");
        } catch (MetamacException e) {
            assertEquals(10, e.getExceptionItems().size());

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("DATA_SOURCE.DATA_GPE_UUID", e.getExceptionItems().get(0).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(1).getCode());
            assertEquals(1, e.getExceptionItems().get(1).getMessageParameters().length);
            assertEquals("DATA_SOURCE.INTERPERIOD_RATE.METHOD_TYPE", e.getExceptionItems().get(1).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(2).getCode());
            assertEquals(1, e.getExceptionItems().get(2).getMessageParameters().length);
            assertEquals("DATA_SOURCE.INTERPERIOD_RATE.METHOD", e.getExceptionItems().get(2).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(3).getCode());
            assertEquals(1, e.getExceptionItems().get(3).getMessageParameters().length);
            assertEquals("DATA_SOURCE.INTERPERIOD_RATE.QUANTITY", e.getExceptionItems().get(3).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(4).getCode());
            assertEquals(1, e.getExceptionItems().get(4).getMessageParameters().length);
            assertEquals("DATA_SOURCE.ANNUAL_RATE.METHOD", e.getExceptionItems().get(4).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(5).getCode());
            assertEquals(1, e.getExceptionItems().get(5).getMessageParameters().length);
            assertEquals("DATA_SOURCE.ANNUAL_RATE.ROUNDING", e.getExceptionItems().get(5).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(6).getCode());
            assertEquals(1, e.getExceptionItems().get(6).getMessageParameters().length);
            assertEquals("DATA_SOURCE.ANNUAL_RATE.QUANTITY.TYPE", e.getExceptionItems().get(6).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(7).getCode());
            assertEquals(1, e.getExceptionItems().get(7).getMessageParameters().length);
            assertEquals("DATA_SOURCE.ANNUAL_RATE.QUANTITY.UNIT_UUID", e.getExceptionItems().get(7).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(8).getCode());
            assertEquals(2, e.getExceptionItems().get(8).getMessageParameters().length);
            assertEquals("DATA_SOURCE.TIME_VARIABLE", e.getExceptionItems().get(8).getMessageParameters()[0]);
            assertEquals("DATA_SOURCE.TIME_VALUE", e.getExceptionItems().get(8).getMessageParameters()[1]);

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(9).getCode());
            assertEquals(2, e.getExceptionItems().get(9).getMessageParameters().length);
            assertEquals("DATA_SOURCE.GEOGRAPHICAL_VARIABLE", e.getExceptionItems().get(9).getMessageParameters()[0]);
            assertEquals("DATA_SOURCE.GEOGRAPHICAL_VALUE_UUID", e.getExceptionItems().get(9).getMessageParameters()[1]);
        }
    }

    @Test
    public void testCreateDataSourceErrorTimeValueIncorrect() throws Exception {

        // Create dataSource
        DataSourceDto dataSourceDto = new DataSourceDto();
        dataSourceDto.setDataGpeUuid("queryGpe1");
        dataSourceDto.setPxUri("px1");
        dataSourceDto.setTimeValue("xxx");
        dataSourceDto.setGeographicalValueUuid(GEOGRAPHICAL_VALUE_1);
        DataSourceVariableDto dataSourceVariableDto1 = new DataSourceVariableDto();
        dataSourceVariableDto1.setVariable("variable1");
        dataSourceVariableDto1.setCategory("category1");
        dataSourceDto.addOtherVariable(dataSourceVariableDto1);
        DataSourceVariableDto dataSourceVariableDto2 = new DataSourceVariableDto();
        dataSourceVariableDto2.setVariable("variable2");
        dataSourceVariableDto2.setCategory("category2");
        dataSourceDto.addOtherVariable(dataSourceVariableDto2);
        dataSourceDto.setInterperiodRate(new RateDerivationDto());
        dataSourceDto.getInterperiodRate().setMethodType(RateDerivationMethodTypeEnum.CALCULATE);
        dataSourceDto.getInterperiodRate().setMethod("Method1");
        dataSourceDto.getInterperiodRate().setRounding(RateDerivationRoundingEnum.DOWN);
        dataSourceDto.getInterperiodRate().setQuantity(new QuantityDto());
        dataSourceDto.getInterperiodRate().getQuantity().setType(QuantityTypeEnum.AMOUNT);
        dataSourceDto.getInterperiodRate().getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        dataSourceDto.getInterperiodRate().getQuantity().setDecimalPlaces(Integer.valueOf(2));
        dataSourceDto.setAnnualRate(new RateDerivationDto());
        dataSourceDto.getAnnualRate().setMethodType(RateDerivationMethodTypeEnum.LOAD);
        dataSourceDto.getAnnualRate().setMethod("Method2");
        dataSourceDto.getAnnualRate().setRounding(RateDerivationRoundingEnum.UPWARD);
        dataSourceDto.getAnnualRate().setQuantity(new QuantityDto());
        dataSourceDto.getAnnualRate().getQuantity().setType(QuantityTypeEnum.CHANGE_RATE);
        dataSourceDto.getAnnualRate().getQuantity().setUnitUuid(QUANTITY_UNIT_2);
        dataSourceDto.getAnnualRate().getQuantity().setNumeratorIndicatorUuid(INDICATOR_3);
        dataSourceDto.getAnnualRate().getQuantity().setIsPercentage(Boolean.TRUE);
        dataSourceDto.getAnnualRate().getQuantity().setBaseQuantityIndicatorUuid(INDICATOR_1);

        String uuidIndicator = INDICATOR_1;
        try {
        indicatorsServiceFacade.createDataSource(getServiceContext(), uuidIndicator, dataSourceDto);
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("DATA_SOURCE.TIME_VALUE", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }
    
    @Test
    public void testCreateDataSourceErrorMetamacUnexpected() throws Exception {

        // Create dataSource
        DataSourceDto dataSourceDto = new DataSourceDto();
        dataSourceDto.setDataGpeUuid("queryGpe1");
        dataSourceDto.setPxUri("px1");
        
        dataSourceDto.setTimeVariable("timeVariable");
        dataSourceDto.setTimeValue("2010");
        dataSourceDto.setGeographicalVariable("geographicalVariable");
        dataSourceDto.setGeographicalValueUuid(GEOGRAPHICAL_VALUE_1);
        
        DataSourceVariableDto dataSourceVariableDto1 = new DataSourceVariableDto();
        dataSourceVariableDto1.setVariable("variable1");
        dataSourceVariableDto1.setCategory("category1");
        dataSourceDto.addOtherVariable(dataSourceVariableDto1);
        DataSourceVariableDto dataSourceVariableDto2 = new DataSourceVariableDto();
        dataSourceVariableDto2.setVariable("variable2");
        dataSourceVariableDto2.setCategory("category2");
        dataSourceDto.addOtherVariable(dataSourceVariableDto2);
        dataSourceDto.setInterperiodRate(new RateDerivationDto());
        dataSourceDto.getInterperiodRate().setMethodType(RateDerivationMethodTypeEnum.CALCULATE);
        dataSourceDto.getInterperiodRate().setMethod("Method1");
        dataSourceDto.getInterperiodRate().setRounding(RateDerivationRoundingEnum.DOWN);
        dataSourceDto.getInterperiodRate().setQuantity(new QuantityDto());
        dataSourceDto.getInterperiodRate().getQuantity().setType(QuantityTypeEnum.AMOUNT);
        dataSourceDto.getInterperiodRate().getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        dataSourceDto.getInterperiodRate().getQuantity().setDecimalPlaces(Integer.valueOf(2));
        dataSourceDto.setAnnualRate(new RateDerivationDto());
        dataSourceDto.getAnnualRate().setMethodType(RateDerivationMethodTypeEnum.LOAD);
        dataSourceDto.getAnnualRate().setMethod("Method2");
        dataSourceDto.getAnnualRate().setRounding(RateDerivationRoundingEnum.UPWARD);
        dataSourceDto.getAnnualRate().setQuantity(new QuantityDto());
        dataSourceDto.getAnnualRate().getQuantity().setType(QuantityTypeEnum.CHANGE_RATE);
        dataSourceDto.getAnnualRate().getQuantity().setUnitUuid(QUANTITY_UNIT_2);
        dataSourceDto.getAnnualRate().getQuantity().setNumeratorIndicatorUuid(INDICATOR_3);
        dataSourceDto.getAnnualRate().getQuantity().setIsPercentage(Boolean.TRUE);
        dataSourceDto.getAnnualRate().getQuantity().setBaseQuantityIndicatorUuid(INDICATOR_1);

        String uuidIndicator = INDICATOR_1;
        try {
        indicatorsServiceFacade.createDataSource(getServiceContext(), uuidIndicator, dataSourceDto);
        } catch (MetamacException e) {
            assertEquals(2, e.getExceptionItems().size());
            
            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("DATA_SOURCE.TIME_VALUE", e.getExceptionItems().get(0).getMessageParameters()[0]);
            
            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(1).getCode());
            assertEquals(1, e.getExceptionItems().get(1).getMessageParameters().length);
            assertEquals("DATA_SOURCE.GEOGRAPHICAL_VALUE_UUID", e.getExceptionItems().get(1).getMessageParameters()[0]);
        }
    }
    
    @Test
    public void testCreateDataSourceErrorIndicatorNotExists() throws Exception {

        String indicatorsSystemUuid = NOT_EXISTS;

        DataSourceDto dataSourceDto = new DataSourceDto();
        dataSourceDto.setDataGpeUuid("queryGpe1");
        dataSourceDto.setPxUri("px1");
        dataSourceDto.setTimeVariable("timeVariable1");
        dataSourceDto.setGeographicalVariable("geographicalVariable1");
        dataSourceDto.setInterperiodRate(new RateDerivationDto());
        dataSourceDto.getInterperiodRate().setMethodType(RateDerivationMethodTypeEnum.CALCULATE);
        dataSourceDto.getInterperiodRate().setMethod("Method1");
        dataSourceDto.getInterperiodRate().setRounding(RateDerivationRoundingEnum.DOWN);
        dataSourceDto.getInterperiodRate().setQuantity(new QuantityDto());
        dataSourceDto.getInterperiodRate().getQuantity().setType(QuantityTypeEnum.AMOUNT);
        dataSourceDto.getInterperiodRate().getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        dataSourceDto.getInterperiodRate().getQuantity().setDecimalPlaces(Integer.valueOf(2));
        dataSourceDto.setAnnualRate(new RateDerivationDto());
        dataSourceDto.getAnnualRate().setMethodType(RateDerivationMethodTypeEnum.LOAD);
        dataSourceDto.getAnnualRate().setMethod("Method2");
        dataSourceDto.getAnnualRate().setRounding(RateDerivationRoundingEnum.UPWARD);
        dataSourceDto.getAnnualRate().setQuantity(new QuantityDto());
        dataSourceDto.getAnnualRate().getQuantity().setType(QuantityTypeEnum.CHANGE_RATE);
        dataSourceDto.getAnnualRate().getQuantity().setUnitUuid(QUANTITY_UNIT_2);
        dataSourceDto.getAnnualRate().getQuantity().setNumeratorIndicatorUuid(INDICATOR_3);
        dataSourceDto.getAnnualRate().getQuantity().setIsPercentage(Boolean.TRUE);
        dataSourceDto.getAnnualRate().getQuantity().setBaseQuantityIndicatorUuid(INDICATOR_1);

        try {
            indicatorsServiceFacade.createDataSource(getServiceContext(), indicatorsSystemUuid, dataSourceDto);
            fail("Indicator not exists");
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
        dataSourceDto.setDataGpeUuid("queryGpe1");
        dataSourceDto.setPxUri("px1");
        dataSourceDto.setTimeVariable("timeVariable1");
        dataSourceDto.setGeographicalVariable("geographicalVariable1");
        dataSourceDto.setInterperiodRate(new RateDerivationDto());
        dataSourceDto.getInterperiodRate().setMethodType(RateDerivationMethodTypeEnum.CALCULATE);
        dataSourceDto.getInterperiodRate().setMethod("Method1");
        dataSourceDto.getInterperiodRate().setRounding(RateDerivationRoundingEnum.DOWN);
        dataSourceDto.getInterperiodRate().setQuantity(new QuantityDto());
        dataSourceDto.getInterperiodRate().getQuantity().setType(QuantityTypeEnum.AMOUNT);
        dataSourceDto.getInterperiodRate().getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        dataSourceDto.getInterperiodRate().getQuantity().setDecimalPlaces(Integer.valueOf(2));
        dataSourceDto.setAnnualRate(new RateDerivationDto());
        dataSourceDto.getAnnualRate().setMethodType(RateDerivationMethodTypeEnum.LOAD);
        dataSourceDto.getAnnualRate().setMethod("Method2");
        dataSourceDto.getAnnualRate().setRounding(RateDerivationRoundingEnum.UPWARD);
        dataSourceDto.getAnnualRate().setQuantity(new QuantityDto());
        dataSourceDto.getAnnualRate().getQuantity().setType(QuantityTypeEnum.CHANGE_RATE);
        dataSourceDto.getAnnualRate().getQuantity().setUnitUuid(QUANTITY_UNIT_2);
        dataSourceDto.getAnnualRate().getQuantity().setNumeratorIndicatorUuid(INDICATOR_3);
        dataSourceDto.getAnnualRate().getQuantity().setIsPercentage(Boolean.TRUE);
        dataSourceDto.getAnnualRate().getQuantity().setBaseQuantityIndicatorUuid(INDICATOR_1);

        try {
            indicatorsServiceFacade.createDataSource(getServiceContext(), indicatorsSystemUuid, dataSourceDto);
            fail("Indicator not in production");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_IN_PRODUCTION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(indicatorsSystemUuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateDataSourceErrorBaseQuantityIsNotOwnIndicator() throws Exception {

        String indicatorUuid = INDICATOR_1;
        String indicatorUuidLinked = INDICATOR_3;

        // Create dataSource
        DataSourceDto dataSourceDto = new DataSourceDto();
        dataSourceDto.setDataGpeUuid("queryGpe1");
        dataSourceDto.setPxUri("px1");
        dataSourceDto.setTimeVariable("timeVariable1");
        dataSourceDto.setGeographicalVariable("geographicalVariable1");
        DataSourceVariableDto dataSourceVariableDto1 = new DataSourceVariableDto();
        dataSourceVariableDto1.setVariable("variable1");
        dataSourceVariableDto1.setCategory("category1");
        dataSourceDto.addOtherVariable(dataSourceVariableDto1);
        DataSourceVariableDto dataSourceVariableDto2 = new DataSourceVariableDto();
        dataSourceVariableDto2.setVariable("variable2");
        dataSourceVariableDto2.setCategory("category2");
        dataSourceDto.addOtherVariable(dataSourceVariableDto2);
        dataSourceDto.setInterperiodRate(new RateDerivationDto());
        dataSourceDto.getInterperiodRate().setMethodType(RateDerivationMethodTypeEnum.CALCULATE);
        dataSourceDto.getInterperiodRate().setMethod("Method1");
        dataSourceDto.getInterperiodRate().setRounding(RateDerivationRoundingEnum.DOWN);
        dataSourceDto.getInterperiodRate().setQuantity(new QuantityDto());
        dataSourceDto.getInterperiodRate().getQuantity().setType(QuantityTypeEnum.AMOUNT);
        dataSourceDto.getInterperiodRate().getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        dataSourceDto.getInterperiodRate().getQuantity().setDecimalPlaces(Integer.valueOf(2));
        dataSourceDto.setAnnualRate(new RateDerivationDto());
        dataSourceDto.getAnnualRate().setMethodType(RateDerivationMethodTypeEnum.LOAD);
        dataSourceDto.getAnnualRate().setMethod("Method2");
        dataSourceDto.getAnnualRate().setRounding(RateDerivationRoundingEnum.UPWARD);
        dataSourceDto.getAnnualRate().setQuantity(new QuantityDto());
        dataSourceDto.getAnnualRate().getQuantity().setType(QuantityTypeEnum.CHANGE_RATE);
        dataSourceDto.getAnnualRate().getQuantity().setUnitUuid(QUANTITY_UNIT_2);
        dataSourceDto.getAnnualRate().getQuantity().setNumeratorIndicatorUuid(indicatorUuidLinked);
        dataSourceDto.getAnnualRate().getQuantity().setIsPercentage(Boolean.TRUE);
        dataSourceDto.getAnnualRate().getQuantity().setBaseQuantityIndicatorUuid(indicatorUuidLinked);

        try {
            indicatorsServiceFacade.createDataSource(getServiceContext(), indicatorUuid, dataSourceDto);
            fail("Base quantity is not own indicator");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("DATA_SOURCE.ANNUAL_RATE.QUANTITY.BASE_QUANTITY_INDICATOR_UUID", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateDataSourceErrorDenominatorMustNotBeOwnIndicator() throws Exception {

        String indicatorUuid = INDICATOR_1;

        // Create dataSource
        DataSourceDto dataSourceDto = new DataSourceDto();
        dataSourceDto.setDataGpeUuid("queryGpe1");
        dataSourceDto.setPxUri("px1");
        dataSourceDto.setTimeVariable("timeVariable1");
        dataSourceDto.setGeographicalVariable("geographicalVariable1");
        DataSourceVariableDto dataSourceVariableDto1 = new DataSourceVariableDto();
        dataSourceVariableDto1.setVariable("variable1");
        dataSourceVariableDto1.setCategory("category1");
        dataSourceDto.addOtherVariable(dataSourceVariableDto1);
        DataSourceVariableDto dataSourceVariableDto2 = new DataSourceVariableDto();
        dataSourceVariableDto2.setVariable("variable2");
        dataSourceVariableDto2.setCategory("category2");
        dataSourceDto.addOtherVariable(dataSourceVariableDto2);
        dataSourceDto.setInterperiodRate(new RateDerivationDto());
        dataSourceDto.getInterperiodRate().setMethodType(RateDerivationMethodTypeEnum.CALCULATE);
        dataSourceDto.getInterperiodRate().setMethod("Method1");
        dataSourceDto.getInterperiodRate().setRounding(RateDerivationRoundingEnum.DOWN);
        dataSourceDto.getInterperiodRate().setQuantity(new QuantityDto());
        dataSourceDto.getInterperiodRate().getQuantity().setType(QuantityTypeEnum.AMOUNT);
        dataSourceDto.getInterperiodRate().getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        dataSourceDto.getInterperiodRate().getQuantity().setDecimalPlaces(Integer.valueOf(2));
        dataSourceDto.setAnnualRate(new RateDerivationDto());
        dataSourceDto.getAnnualRate().setMethodType(RateDerivationMethodTypeEnum.LOAD);
        dataSourceDto.getAnnualRate().setMethod("Method2");
        dataSourceDto.getAnnualRate().setRounding(RateDerivationRoundingEnum.UPWARD);
        dataSourceDto.getAnnualRate().setQuantity(new QuantityDto());
        dataSourceDto.getAnnualRate().getQuantity().setType(QuantityTypeEnum.CHANGE_RATE);
        dataSourceDto.getAnnualRate().getQuantity().setUnitUuid(QUANTITY_UNIT_2);
        dataSourceDto.getAnnualRate().getQuantity().setDenominatorIndicatorUuid(indicatorUuid);
        dataSourceDto.getAnnualRate().getQuantity().setIsPercentage(Boolean.TRUE);
        dataSourceDto.getAnnualRate().getQuantity().setBaseQuantityIndicatorUuid(indicatorUuid);

        try {
            indicatorsServiceFacade.createDataSource(getServiceContext(), indicatorUuid, dataSourceDto);
            fail("Denominator must not be own indicator");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("DATA_SOURCE.ANNUAL_RATE.QUANTITY.DENOMINATOR_INDICATOR_UUID", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateDataSourceErrorBaseLocationNotExists() throws Exception {

        String indicatorUuid = INDICATOR_1;
        String indicatorUuidLinked = INDICATOR_3;

        // Create dataSource
        DataSourceDto dataSourceDto = new DataSourceDto();
        dataSourceDto.setDataGpeUuid("queryGpe1");
        dataSourceDto.setPxUri("px1");
        dataSourceDto.setTimeVariable("timeVariable1");
        dataSourceDto.setGeographicalVariable("geographicalVariable1");
        DataSourceVariableDto dataSourceVariableDto1 = new DataSourceVariableDto();
        dataSourceVariableDto1.setVariable("variable1");
        dataSourceVariableDto1.setCategory("category1");
        dataSourceDto.addOtherVariable(dataSourceVariableDto1);
        DataSourceVariableDto dataSourceVariableDto2 = new DataSourceVariableDto();
        dataSourceVariableDto2.setVariable("variable2");
        dataSourceVariableDto2.setCategory("category2");
        dataSourceDto.addOtherVariable(dataSourceVariableDto2);
        dataSourceDto.setInterperiodRate(new RateDerivationDto());
        dataSourceDto.getInterperiodRate().setMethodType(RateDerivationMethodTypeEnum.CALCULATE);
        dataSourceDto.getInterperiodRate().setMethod("Method1");
        dataSourceDto.getInterperiodRate().setRounding(RateDerivationRoundingEnum.DOWN);
        dataSourceDto.getInterperiodRate().setQuantity(new QuantityDto());
        dataSourceDto.getInterperiodRate().getQuantity().setType(QuantityTypeEnum.AMOUNT);
        dataSourceDto.getInterperiodRate().getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        dataSourceDto.getInterperiodRate().getQuantity().setDecimalPlaces(Integer.valueOf(2));
        dataSourceDto.setAnnualRate(new RateDerivationDto());
        dataSourceDto.getAnnualRate().setMethodType(RateDerivationMethodTypeEnum.LOAD);
        dataSourceDto.getAnnualRate().setMethod("Method2");
        dataSourceDto.getAnnualRate().setRounding(RateDerivationRoundingEnum.UPWARD);
        dataSourceDto.getAnnualRate().setQuantity(new QuantityDto());
        dataSourceDto.getAnnualRate().getQuantity().setType(QuantityTypeEnum.INDEX);
        dataSourceDto.getAnnualRate().getQuantity().setUnitUuid(QUANTITY_UNIT_2);
        dataSourceDto.getAnnualRate().getQuantity().setNumeratorIndicatorUuid(indicatorUuidLinked);
        dataSourceDto.getAnnualRate().getQuantity().setIsPercentage(Boolean.TRUE);
        dataSourceDto.getAnnualRate().getQuantity().setBaseLocationUuid(NOT_EXISTS);

        try {
            indicatorsServiceFacade.createDataSource(getServiceContext(), indicatorUuid, dataSourceDto);
            fail("Base location not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.GEOGRAPHICAL_VALUE_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(dataSourceDto.getAnnualRate().getQuantity().getBaseLocationUuid(), e.getExceptionItems().get(0).getMessageParameters()[0]);
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
            fail("Indicator not in production");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(INDICATOR_3, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorProcStatusEnum.DRAFT, ((IndicatorProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(IndicatorProcStatusEnum.VALIDATION_REJECTED, ((IndicatorProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
            assertEquals(IndicatorProcStatusEnum.PRODUCTION_VALIDATION, ((IndicatorProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[2]);
            assertEquals(IndicatorProcStatusEnum.DIFFUSION_VALIDATION, ((IndicatorProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[3]);
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
            fail("Indicator not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_VERSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals("1.000", e.getExceptionItems().get(0).getMessageParameters()[1]);
        }
    }

    @Test
    public void testUpdateDataSource() throws Exception {

        String uuid = DATA_SOURCE_1_INDICATOR_1_V2;
        DataSourceDto dataSourceDto = indicatorsServiceFacade.retrieveDataSource(getServiceContext(), uuid);
        dataSourceDto.setTimeVariable("newTime");
        dataSourceDto.getOtherVariables().get(0).setCategory("new Category");
        DataSourceVariableDto dataSourceVariableDto3 = new DataSourceVariableDto();
        dataSourceVariableDto3.setVariable("variable3new");
        dataSourceVariableDto3.setCategory("category3new");
        dataSourceDto.addOtherVariable(dataSourceVariableDto3);

        // Update
        DataSourceDto dataSourceDtoUpdated = indicatorsServiceFacade.updateDataSource(getServiceContext(), dataSourceDto);

        // Validation
        IndicatorsAsserts.assertEqualsDataSource(dataSourceDto, dataSourceDtoUpdated);
        dataSourceDtoUpdated = indicatorsServiceFacade.retrieveDataSource(getServiceContext(), uuid);
        assertEquals(3, dataSourceDtoUpdated.getOtherVariables().size());
        IndicatorsAsserts.assertEqualsDataSource(dataSourceDto, dataSourceDtoUpdated);
    }

    @Test
    public void testUpdateDataSourceErrorChangeMetadataUnmodifiable() throws Exception {

        DataSourceDto dataSourceDto = indicatorsServiceFacade.retrieveDataSource(getServiceContext(), DATA_SOURCE_1_INDICATOR_1_V2);
        dataSourceDto.setDataGpeUuid("newQueryGpe");
        dataSourceDto.setPxUri("newPx");

        try {
            indicatorsServiceFacade.updateDataSource(getServiceContext(), dataSourceDto);
            fail("Query GPE and px changed");
        } catch (MetamacException e) {
            assertEquals(2, e.getExceptionItems().size());

            assertEquals(ServiceExceptionType.METADATA_UNMODIFIABLE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("DATA_SOURCE.DATA_GPE_UUID", e.getExceptionItems().get(0).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_UNMODIFIABLE.getCode(), e.getExceptionItems().get(1).getCode());
            assertEquals(1, e.getExceptionItems().get(1).getMessageParameters().length);
            assertEquals("DATA_SOURCE.PX_URI", e.getExceptionItems().get(1).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateDataSourceErrorChangePx() throws Exception {

        DataSourceDto dataSourceDto = indicatorsServiceFacade.retrieveDataSource(getServiceContext(), DATA_SOURCE_1_INDICATOR_1_V2);
        dataSourceDto.setPxUri("newPx");

        try {
            indicatorsServiceFacade.updateDataSource(getServiceContext(), dataSourceDto);
            fail("Px changed");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_UNMODIFIABLE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("DATA_SOURCE.PX_URI", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateDataSourceErrorIndicatorPublished() throws Exception {

        DataSourceDto dataSourceDto = indicatorsServiceFacade.retrieveDataSource(getServiceContext(), DATA_SOURCE_1_INDICATOR_1_V1);

        try {
            indicatorsServiceFacade.updateDataSource(getServiceContext(), dataSourceDto);
            fail("Indicator published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(INDICATOR_1, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(IndicatorProcStatusEnum.DRAFT, ((IndicatorProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(IndicatorProcStatusEnum.VALIDATION_REJECTED, ((IndicatorProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
            assertEquals(IndicatorProcStatusEnum.PRODUCTION_VALIDATION, ((IndicatorProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[2]);
            assertEquals(IndicatorProcStatusEnum.DIFFUSION_VALIDATION, ((IndicatorProcStatusEnum[]) e.getExceptionItems().get(0).getMessageParameters()[1])[3]);
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

    @Test
    public void testRetrieveQuantityUnit() throws Exception {

        String uuid = QUANTITY_UNIT_1;
        QuantityUnitDto quantityUnitDto = indicatorsServiceFacade.retrieveQuantityUnit(getServiceContext(), uuid);

        assertNotNull(quantityUnitDto);
        assertEquals(uuid, quantityUnitDto.getUuid());
        assertEquals("km", quantityUnitDto.getSymbol());
        assertEquals(QuantityUnitSymbolPositionEnum.END, quantityUnitDto.getSymbolPosition());
        IndicatorsAsserts.assertEqualsInternationalString(quantityUnitDto.getTitle(), "es", "Kilómetros", "en", "Kilometers");

    }

    @Test
    public void testRetrieveQuantityUnitErrorParameterRequired() throws Exception {

        String uuid = null;

        try {
            indicatorsServiceFacade.retrieveQuantityUnit(getServiceContext(), uuid);
            fail("parameter required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("UUID", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRetrieveQuantityUnitErrorNotExists() throws Exception {

        String uuid = NOT_EXISTS;

        try {
            indicatorsServiceFacade.retrieveQuantityUnit(getServiceContext(), uuid);
            fail("No exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.QUANTITY_UNIT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testFindQuantityUnits() throws Exception {

        List<QuantityUnitDto> quantityUnits = indicatorsServiceFacade.findQuantityUnits(getServiceContext());
        assertEquals(2, quantityUnits.size());

        assertEquals(QUANTITY_UNIT_1, quantityUnits.get(0).getUuid());
        assertEquals("km", quantityUnits.get(0).getSymbol());
        assertEquals(QuantityUnitSymbolPositionEnum.END, quantityUnits.get(0).getSymbolPosition());

        assertEquals(QUANTITY_UNIT_2, quantityUnits.get(1).getUuid());
        assertEquals("kg", quantityUnits.get(1).getSymbol());
        assertEquals(QuantityUnitSymbolPositionEnum.START, quantityUnits.get(1).getSymbolPosition());
    }

    @Test
    public void testRetrieveSubject() throws Exception {

        String code = SUBJECT_1;
        SubjectDto subjectDto = indicatorsServiceFacade.retrieveSubject(getServiceContext(), code);

        assertNotNull(subjectDto);
        assertEquals(code, subjectDto.getCode());
        IndicatorsAsserts.assertEqualsInternationalString(subjectDto.getTitle(), "es", "Área temática 1", null, null);
    }

    @Test
    public void testRetrieveSubjectErrorParameterRequired() throws Exception {

        String uuid = null;

        try {
            indicatorsServiceFacade.retrieveSubject(getServiceContext(), uuid);
            fail("parameter required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("CODE", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRetrieveSubjectErrorNotExists() throws Exception {

        String uuid = NOT_EXISTS;

        try {
            indicatorsServiceFacade.retrieveSubject(getServiceContext(), uuid);
            fail("No exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SUBJECT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testFindSubjects() throws Exception {

        List<SubjectDto> subjects = indicatorsServiceFacade.findSubjects(getServiceContext());
        assertEquals(4, subjects.size());

        assertEquals(SUBJECT_1, subjects.get(0).getCode());
        IndicatorsAsserts.assertEqualsInternationalString(subjects.get(0).getTitle(), "es", "Área temática 1", null, null);
        assertEquals(SUBJECT_2, subjects.get(1).getCode());
        IndicatorsAsserts.assertEqualsInternationalString(subjects.get(1).getTitle(), "es", "Área temática 2", null, null);
        assertEquals(SUBJECT_3, subjects.get(2).getCode());
        IndicatorsAsserts.assertEqualsInternationalString(subjects.get(2).getTitle(), "es", "Área temática 3", null, null);
        assertEquals(SUBJECT_4, subjects.get(3).getCode());
        IndicatorsAsserts.assertEqualsInternationalString(subjects.get(3).getTitle(), "es", "Área temática 4", null, null);
    }

    @Test
    public void testFindSubjectsInPublishedIndicators() throws Exception {

        List<SubjectDto> subjects = indicatorsServiceFacade.findSubjectsInPublishedIndicators(getServiceContext());
        assertEquals(2, subjects.size());

        assertEquals(SUBJECT_1, subjects.get(0).getCode());
        IndicatorsAsserts.assertEqualsInternationalString(subjects.get(0).getTitle(), "es", "Área temática 1", null, null);
        assertEquals(SUBJECT_3, subjects.get(1).getCode());
        IndicatorsAsserts.assertEqualsInternationalString(subjects.get(1).getTitle(), "es", "Área temática 3", null, null);
    }
    
    @Override
    protected String getDataSetFile() {
        return "dbunit/IndicatorsServiceFacadeIndicatorsTest.xml";
    }
}
