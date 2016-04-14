package es.gobcan.istac.indicators.core.serviceapi;

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
import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaConjunctionRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaDisjunctionRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPaginator;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction.OperationType;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.gobcan.istac.indicators.core.constants.IndicatorsConstants;
import es.gobcan.istac.indicators.core.criteria.IndicatorCriteriaPropertyEnum;
import es.gobcan.istac.indicators.core.domain.QuantityUnit;
import es.gobcan.istac.indicators.core.domain.UnitMultiplier;
import es.gobcan.istac.indicators.core.dto.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.DataSourceVariableDto;
import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.IndicatorSummaryDto;
import es.gobcan.istac.indicators.core.dto.PublishIndicatorResultDto;
import es.gobcan.istac.indicators.core.dto.QuantityDto;
import es.gobcan.istac.indicators.core.dto.QuantityUnitDto;
import es.gobcan.istac.indicators.core.dto.RateDerivationDto;
import es.gobcan.istac.indicators.core.dto.SubjectDto;
import es.gobcan.istac.indicators.core.dto.UnitMultiplierDto;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorProcStatusEnum;
import es.gobcan.istac.indicators.core.enume.domain.QuantityTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.QuantityUnitSymbolPositionEnum;
import es.gobcan.istac.indicators.core.enume.domain.RateDerivationMethodTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.RateDerivationRoundingEnum;
import es.gobcan.istac.indicators.core.enume.domain.VersionTypeEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionParameters;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.mapper.Do2DtoMapper;
import es.gobcan.istac.indicators.core.serviceapi.utils.IndicatorsAsserts;
import es.gobcan.istac.indicators.core.serviceapi.utils.IndicatorsMocks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test to IndicatorsServiceFacade. Testing: indicators, data sources
 * Spring based transactional test with DbUnit support.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/include/indicators-service-mockito.xml", "classpath:spring/applicationContext-test.xml"})
@TransactionConfiguration(defaultRollback = true, transactionManager = "txManager")
@Transactional
public class IndicatorsServiceFacadeIndicatorsTest extends IndicatorsBaseTest {

    @Autowired
    protected IndicatorsDataService   indicatorsDataService;

    @Autowired
    protected IndicatorsServiceFacade indicatorsServiceFacade;

    @Autowired
    private IndicatorsService         indicatorsService;

    @Autowired
    private Do2DtoMapper              do2DtoMapper;

    @Test
    public void testRetrieveIndicator() throws Exception {

        String uuid = INDICATOR_1;
        String versionNumber = "1.000";
        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, versionNumber);

        assertNotNull(indicatorDto);
        assertEquals(uuid, indicatorDto.getUuid());
        assertEquals(versionNumber, indicatorDto.getVersionNumber());
        assertEquals("2.000", indicatorDto.getProductionVersion());
        assertEquals("1.000", indicatorDto.getPublishedVersion());
        assertNull(indicatorDto.getArchivedVersion());
        assertEquals("CODE-1", indicatorDto.getCode());
        assertEquals("VIEWCODE_1", indicatorDto.getViewCode());
        assertEquals(SUBJECT_1, indicatorDto.getSubjectCode());
        IndicatorsAsserts.assertEqualsInternationalString(indicatorDto.getSubjectTitle(), "es", "Área temática 1", null, null);
        assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorDto.getProcStatus());
        IndicatorsAsserts.assertEqualsInternationalString(indicatorDto.getTitle(), "es", "Título Indicator-1-v1 Educación", "en", "Title Indicator-1-v1");
        IndicatorsAsserts.assertEqualsInternationalString(indicatorDto.getAcronym(), "es", "Acrónimo Indicator-1-v1", "en", "Acronym Indicator-1-v1");
        IndicatorsAsserts.assertEqualsInternationalString(indicatorDto.getComments(), "es", "Comentario Indicator-1-v1", "en", "Comments Indicator-1-v1");
        IndicatorsAsserts.assertEqualsInternationalString(indicatorDto.getNotes(), "es", "Nota Indicator-1-v1", "en", "Note Indicator-1-v1");
        IndicatorsAsserts.assertEqualsInternationalString(indicatorDto.getConceptDescription(), "es", "Concepto 1", "en", "Concept 1");
        assertEquals("data:repository:11", indicatorDto.getDataRepositoryId());
        assertEquals("TABLE_NAME_11", indicatorDto.getDataRepositoryTableName());

        assertNotNull(indicatorDto.getQuantity());
        assertEquals(QuantityTypeEnum.CHANGE_RATE, indicatorDto.getQuantity().getType());
        assertEquals("1", indicatorDto.getQuantity().getUnitUuid());
        assertEquals(Integer.valueOf(10), indicatorDto.getQuantity().getUnitMultiplier());
        IndicatorsAsserts.assertEqualsInternationalString(indicatorDto.getQuantity().getUnitMultiplierLabel(), "es", "Decenas", null, null);
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
        String versionNumberPublished = "1.000";

        // Without version (retrieve last)
        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, null);
            assertEquals(uuid, indicatorDto.getUuid());
            assertEquals(versionNumberProduction, indicatorDto.getVersionNumber());
            assertEquals(versionNumberProduction, indicatorDto.getProductionVersion());
            assertEquals(versionNumberPublished, indicatorDto.getPublishedVersion());
            assertNull(indicatorDto.getArchivedVersion());
        }

        // With version 1
        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, versionNumberPublished);
            assertEquals(uuid, indicatorDto.getUuid());
            assertEquals(versionNumberPublished, indicatorDto.getVersionNumber());
            assertEquals(versionNumberProduction, indicatorDto.getProductionVersion());
            assertEquals(versionNumberPublished, indicatorDto.getPublishedVersion());
            assertNull(indicatorDto.getArchivedVersion());
        }

        // With version 2
        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, versionNumberProduction);
            assertEquals(uuid, indicatorDto.getUuid());
            assertEquals(versionNumberProduction, indicatorDto.getVersionNumber());
            assertEquals(versionNumberProduction, indicatorDto.getProductionVersion());
            assertEquals(versionNumberPublished, indicatorDto.getPublishedVersion());
            assertNull(indicatorDto.getArchivedVersion());
        }
    }

    @Test
    public void testRetrieveIndicatorErrorParameterRequired() throws Exception {

        String uuid = null;

        try {
            indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, null);
            fail("parameter required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.UUID, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRetrieveIndicatorErrorNotExists() throws Exception {

        String uuid = NOT_EXISTS;

        try {
            indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, null);
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
            indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, versionNotExists);
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
    public void testRetrieveIndicatorByCode() throws Exception {

        String code = "CODE-1";
        String versionNumber = "1.000";
        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicatorByCode(getServiceContextAdministrador(), code, versionNumber);

        assertNotNull(indicatorDto);
        assertEquals(INDICATOR_1, indicatorDto.getUuid());
        assertEquals("1.000", indicatorDto.getVersionNumber());
        assertEquals("1.000", indicatorDto.getPublishedVersion());
        assertEquals("2.000", indicatorDto.getProductionVersion());
        assertNull(indicatorDto.getArchivedVersion());
        assertEquals("CODE-1", indicatorDto.getCode());
    }

    @Test
    public void testRetrieveIndicatorByCodeLastVersion() throws Exception {

        String code = "CODE-1";
        String versionNumber = null;
        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicatorByCode(getServiceContextAdministrador(), code, versionNumber);

        assertNotNull(indicatorDto);
        assertEquals(INDICATOR_1, indicatorDto.getUuid());
        assertEquals(indicatorDto.getProductionVersion(), indicatorDto.getVersionNumber());
        assertEquals("2.000", indicatorDto.getProductionVersion());
        assertEquals("1.000", indicatorDto.getPublishedVersion());
        assertNull(indicatorDto.getArchivedVersion());
        assertEquals("CODE-1", indicatorDto.getCode());
    }

    @Test
    public void testRetrieveIndicatorByCodeErrorNotExists() throws Exception {

        String code = "CODE_NOT_EXISTS";

        try {
            indicatorsServiceFacade.retrieveIndicatorByCode(getServiceContextAdministrador(), code, null);
            fail("No exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_NOT_FOUND_WITH_CODE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(code, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorQuantity() throws Exception {

        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode("code" + (new Date()).getTime());
        indicatorDto.setViewCode("viewcode" + (new Date()).getTime());
        indicatorDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setAcronym(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setSubjectCode(SUBJECT_1);
        indicatorDto.setSubjectTitle(IndicatorsMocks.mockInternationalStringDto(IndicatorsConstants.LOCALE_SPANISH, "Área temática 1"));
        indicatorDto.setComments(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setNotes(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setConceptDescription(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setQuantity(new QuantityDto());
        indicatorDto.getQuantity().setType(QuantityTypeEnum.QUANTITY);
        indicatorDto.getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        indicatorDto.getQuantity().setUnitMultiplier(Integer.valueOf(1000));

        // Create
        IndicatorDto indicatorDtoCreated = indicatorsServiceFacade.createIndicator(getServiceContextAdministrador(), indicatorDto);

        // Validate
        assertNotNull(indicatorDtoCreated);
        assertNotNull(indicatorDtoCreated.getUuid());
        assertNotNull(indicatorDtoCreated.getVersionNumber());

        IndicatorDto indicatorDtoRetrieved = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), indicatorDtoCreated.getUuid(), indicatorDtoCreated.getVersionNumber());
        assertEquals(IndicatorProcStatusEnum.DRAFT, indicatorDtoCreated.getProcStatus());
        assertEquals(IndicatorProcStatusEnum.DRAFT, indicatorDtoRetrieved.getProcStatus());
        assertEquals("1.000", indicatorDtoRetrieved.getProductionVersion());
        assertNull(indicatorDtoRetrieved.getPublishedVersion());
        assertNull(indicatorDtoRetrieved.getArchivedVersion());
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
        assertEquals(getServiceContextAdministrador().getUserId(), indicatorDtoCreated.getCreatedBy());
        assertTrue(DateUtils.isSameDay(new Date(), indicatorDtoCreated.getCreatedDate()));
        assertTrue(DateUtils.isSameDay(new Date(), indicatorDtoCreated.getLastUpdated()));
        assertEquals(getServiceContextAdministrador().getUserId(), indicatorDtoCreated.getLastUpdatedBy());
    }

    @Test
    public void testCreateIndicatorMagnitude() throws Exception {

        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode("code" + (new Date()).getTime());
        indicatorDto.setViewCode("viewcode" + (new Date()).getTime());
        indicatorDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setAcronym(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setSubjectCode(SUBJECT_1);
        indicatorDto.setSubjectTitle(IndicatorsMocks.mockInternationalStringDto(IndicatorsConstants.LOCALE_SPANISH, "Área temática 1"));
        indicatorDto.setComments(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setNotes(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setConceptDescription(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setQuantity(new QuantityDto());
        indicatorDto.getQuantity().setType(QuantityTypeEnum.MAGNITUDE);
        indicatorDto.getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        indicatorDto.getQuantity().setUnitMultiplier(Integer.valueOf(1000));
        indicatorDto.getQuantity().setMinimum(Integer.valueOf(1000));
        indicatorDto.getQuantity().setMaximum(Integer.valueOf(2000));
        assertTrue(indicatorDto.getQuantity().isMagnituteOrExtension());

        // Create
        IndicatorDto indicatorDtoCreated = indicatorsServiceFacade.createIndicator(getServiceContextAdministrador(), indicatorDto);

        // Validate
        IndicatorDto indicatorDtoRetrieved = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), indicatorDtoCreated.getUuid(), indicatorDtoCreated.getVersionNumber());
        IndicatorsAsserts.assertEqualsIndicator(indicatorDto, indicatorDtoRetrieved);
    }

    @Test
    public void testCreateIndicatorFraction() throws Exception {

        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode("code" + (new Date()).getTime());
        indicatorDto.setViewCode("viewcode" + (new Date()).getTime());
        indicatorDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setAcronym(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setSubjectCode(SUBJECT_1);
        indicatorDto.setSubjectTitle(IndicatorsMocks.mockInternationalStringDto(IndicatorsConstants.LOCALE_SPANISH, "Área temática 1"));
        indicatorDto.setComments(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setNotes(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setConceptDescription(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setQuantity(new QuantityDto());
        indicatorDto.getQuantity().setType(QuantityTypeEnum.FRACTION);
        indicatorDto.getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        indicatorDto.getQuantity().setUnitMultiplier(Integer.valueOf(1000));
        indicatorDto.getQuantity().setMinimum(Integer.valueOf(1000));
        indicatorDto.getQuantity().setMaximum(Integer.valueOf(2000));
        indicatorDto.getQuantity().setNumeratorIndicatorUuid(INDICATOR_2);
        indicatorDto.getQuantity().setDenominatorIndicatorUuid(INDICATOR_3);
        assertTrue(indicatorDto.getQuantity().isFractionOrExtension());

        // Create
        IndicatorDto indicatorDtoCreated = indicatorsServiceFacade.createIndicator(getServiceContextAdministrador(), indicatorDto);

        // Validate
        IndicatorDto indicatorDtoRetrieved = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), indicatorDtoCreated.getUuid(), indicatorDtoCreated.getVersionNumber());
        IndicatorsAsserts.assertEqualsIndicator(indicatorDto, indicatorDtoRetrieved);
    }

    @Test
    public void testCreateIndicatorRatio() throws Exception {

        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode("code" + (new Date()).getTime());
        indicatorDto.setViewCode("viewcode" + (new Date()).getTime());
        indicatorDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setAcronym(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setSubjectCode(SUBJECT_1);
        indicatorDto.setSubjectTitle(IndicatorsMocks.mockInternationalStringDto(IndicatorsConstants.LOCALE_SPANISH, "Área temática 1"));
        indicatorDto.setComments(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setNotes(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setConceptDescription(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setQuantity(new QuantityDto());
        indicatorDto.getQuantity().setType(QuantityTypeEnum.RATIO);
        indicatorDto.getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        indicatorDto.getQuantity().setUnitMultiplier(Integer.valueOf(1000));
        indicatorDto.getQuantity().setMinimum(Integer.valueOf(1000));
        indicatorDto.getQuantity().setMaximum(Integer.valueOf(2000));
        indicatorDto.getQuantity().setNumeratorIndicatorUuid(INDICATOR_2);
        indicatorDto.getQuantity().setDenominatorIndicatorUuid(INDICATOR_3);
        indicatorDto.getQuantity().setIsPercentage(Boolean.FALSE);
        indicatorDto.getQuantity().setPercentageOf(IndicatorsMocks.mockInternationalStringDto());
        assertTrue(indicatorDto.getQuantity().isRatioOrExtension());

        // Create
        IndicatorDto indicatorDtoCreated = indicatorsServiceFacade.createIndicator(getServiceContextAdministrador(), indicatorDto);

        // Validate
        IndicatorDto indicatorDtoRetrieved = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), indicatorDtoCreated.getUuid(), indicatorDtoCreated.getVersionNumber());
        IndicatorsAsserts.assertEqualsIndicator(indicatorDto, indicatorDtoRetrieved);
    }

    @Test
    public void testCreateIndicatorIndex() throws Exception {

        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode("code" + (new Date()).getTime());
        indicatorDto.setViewCode("viewcode" + (new Date()).getTime());
        indicatorDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setAcronym(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setSubjectCode(SUBJECT_1);
        indicatorDto.setSubjectTitle(IndicatorsMocks.mockInternationalStringDto(IndicatorsConstants.LOCALE_SPANISH, "Área temática 1"));
        indicatorDto.setComments(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setNotes(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setConceptDescription(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setQuantity(new QuantityDto());
        indicatorDto.getQuantity().setType(QuantityTypeEnum.INDEX);
        indicatorDto.getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        indicatorDto.getQuantity().setUnitMultiplier(Integer.valueOf(1000));
        indicatorDto.getQuantity().setMinimum(Integer.valueOf(1000));
        indicatorDto.getQuantity().setMaximum(Integer.valueOf(2000));
        indicatorDto.getQuantity().setNumeratorIndicatorUuid(INDICATOR_2);
        indicatorDto.getQuantity().setDenominatorIndicatorUuid(INDICATOR_3);
        indicatorDto.getQuantity().setIsPercentage(Boolean.FALSE);
        indicatorDto.getQuantity().setPercentageOf(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.getQuantity().setBaseTime("2011");
        assertTrue(indicatorDto.getQuantity().isIndexOrExtension());

        // Create
        IndicatorDto indicatorDtoCreated = indicatorsServiceFacade.createIndicator(getServiceContextAdministrador(), indicatorDto);

        // Validate
        IndicatorDto indicatorDtoRetrieved = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), indicatorDtoCreated.getUuid(), indicatorDtoCreated.getVersionNumber());
        IndicatorsAsserts.assertEqualsIndicator(indicatorDto, indicatorDtoRetrieved);
    }

    @Test
    public void testCreateIndicatorChangeRate() throws Exception {

        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode("code" + (new Date()).getTime());
        indicatorDto.setViewCode("viewcode" + (new Date()).getTime());
        indicatorDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setAcronym(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setSubjectCode(SUBJECT_1);
        indicatorDto.setSubjectTitle(IndicatorsMocks.mockInternationalStringDto(IndicatorsConstants.LOCALE_SPANISH, "Área temática 1"));
        indicatorDto.setComments(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setNotes(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setConceptDescription(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setQuantity(new QuantityDto());
        indicatorDto.getQuantity().setType(QuantityTypeEnum.CHANGE_RATE);
        indicatorDto.getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        indicatorDto.getQuantity().setUnitMultiplier(Integer.valueOf(1000));
        indicatorDto.getQuantity().setMinimum(Integer.valueOf(1000));
        indicatorDto.getQuantity().setMaximum(Integer.valueOf(2000));
        indicatorDto.getQuantity().setNumeratorIndicatorUuid(INDICATOR_2);
        indicatorDto.getQuantity().setDenominatorIndicatorUuid(INDICATOR_3);
        indicatorDto.getQuantity().setIsPercentage(Boolean.FALSE);
        indicatorDto.getQuantity().setPercentageOf(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.getQuantity().setBaseQuantityIndicatorUuid(INDICATOR_10);
        assertTrue(indicatorDto.getQuantity().isChangeRateOrExtension());

        // Create
        IndicatorDto indicatorDtoCreated = indicatorsServiceFacade.createIndicator(getServiceContextAdministrador(), indicatorDto);

        // Validate
        IndicatorDto indicatorDtoRetrieved = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), indicatorDtoCreated.getUuid(), indicatorDtoCreated.getVersionNumber());
        IndicatorsAsserts.assertEqualsIndicator(indicatorDto, indicatorDtoRetrieved);
    }

    @Test
    public void testCreateIndicatorWithQuantityEmptyAttributes() throws Exception {

        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode("code" + (new Date()).getTime());
        indicatorDto.setViewCode("viewcode" + (new Date()).getTime());
        indicatorDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setAcronym(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setSubjectCode(SUBJECT_1);
        indicatorDto.setSubjectTitle(IndicatorsMocks.mockInternationalStringDto(IndicatorsConstants.LOCALE_SPANISH, "Área temática 1"));
        indicatorDto.setComments(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setNotes(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setConceptDescription(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setQuantity(new QuantityDto());
        indicatorDto.getQuantity().setType(QuantityTypeEnum.CHANGE_RATE);

        // Create
        IndicatorDto indicatorDtoCreated = indicatorsServiceFacade.createIndicator(getServiceContextAdministrador(), indicatorDto);

        // Validate
        IndicatorDto indicatorDtoRetrieved = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), indicatorDtoCreated.getUuid(), indicatorDtoCreated.getVersionNumber());
        IndicatorsAsserts.assertEqualsIndicator(indicatorDto, indicatorDtoRetrieved);
        assertNull(indicatorDtoRetrieved.getQuantity().getUnitUuid());
    }

    @Test
    public void testCreateIndicatorErrorOperationNotAllowed() throws Exception {

        ServiceContext serviceContext = getServiceContextTecnicoSistemaIndicadores();

        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode("code" + (new Date()).getTime());
        indicatorDto.setViewCode("viewcode" + (new Date()).getTime());
        indicatorDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setAcronym(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setSubjectCode(SUBJECT_1);
        indicatorDto.setSubjectTitle(IndicatorsMocks.mockInternationalStringDto(IndicatorsConstants.LOCALE_SPANISH, "Área temática 1"));
        indicatorDto.setComments(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setNotes(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setConceptDescription(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setQuantity(new QuantityDto());
        indicatorDto.getQuantity().setType(QuantityTypeEnum.CHANGE_RATE);

        try {
            indicatorsServiceFacade.createIndicator(serviceContext, indicatorDto);
            fail("operation not allowed");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());

            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(serviceContext.getUserId(), e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorParametersRequired() throws Exception {

        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode(null);
        indicatorDto.setViewCode(null);
        indicatorDto.setTitle(null);
        indicatorDto.setSubjectCode(null);
        indicatorDto.setSubjectTitle(null);
        indicatorDto.setQuantity(new QuantityDto());

        try {
            indicatorsServiceFacade.createIndicator(getServiceContextAdministrador(), indicatorDto);
            fail("parameters required");
        } catch (MetamacException e) {
            assertEquals(5, e.getExceptionItems().size());

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.INDICATOR_CODE, e.getExceptionItems().get(0).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(1).getCode());
            assertEquals(1, e.getExceptionItems().get(1).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.INDICATOR_VIEW_CODE, e.getExceptionItems().get(1).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(2).getCode());
            assertEquals(1, e.getExceptionItems().get(2).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.INDICATOR_TITLE, e.getExceptionItems().get(2).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(3).getCode());
            assertEquals(1, e.getExceptionItems().get(3).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.INDICATOR_SUBJECT_CODE, e.getExceptionItems().get(3).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(4).getCode());
            assertEquals(1, e.getExceptionItems().get(4).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.INDICATOR_SUBJECT_TITLE, e.getExceptionItems().get(4).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorQuantityErrorBaseUnexpected() throws Exception {

        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode("code" + (new Date()).getTime());
        indicatorDto.setViewCode("viewcode" + (new Date()).getTime());
        indicatorDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setAcronym(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setSubjectCode(SUBJECT_1);
        indicatorDto.setSubjectTitle(IndicatorsMocks.mockInternationalStringDto(IndicatorsConstants.LOCALE_SPANISH, "Área temática 1"));
        indicatorDto.setComments(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setNotes(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setConceptDescription(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setQuantity(new QuantityDto());
        indicatorDto.getQuantity().setType(QuantityTypeEnum.INDEX);
        indicatorDto.getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        indicatorDto.getQuantity().setUnitMultiplier(Integer.valueOf(1000));
        indicatorDto.getQuantity().setMinimum(Integer.valueOf(1000));
        indicatorDto.getQuantity().setMaximum(Integer.valueOf(2000));
        indicatorDto.getQuantity().setNumeratorIndicatorUuid(INDICATOR_2);
        indicatorDto.getQuantity().setDenominatorIndicatorUuid(INDICATOR_3);
        indicatorDto.getQuantity().setIsPercentage(Boolean.FALSE);
        indicatorDto.getQuantity().setPercentageOf(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.getQuantity().setBaseValue(Integer.valueOf(1));
        indicatorDto.getQuantity().setBaseTime("2011");
        indicatorDto.getQuantity().setBaseLocationUuid(GEOGRAPHICAL_VALUE_1);

        try {
            indicatorsServiceFacade.createIndicator(getServiceContextAdministrador(), indicatorDto);
            fail("base location unexpected");
        } catch (MetamacException e) {
            assertEquals(2, e.getExceptionItems().size());

            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.INDICATOR_QUANTITY_BASE_LOCATION_UUID, e.getExceptionItems().get(0).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(1).getCode());
            assertEquals(1, e.getExceptionItems().get(1).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.INDICATOR_QUANTITY_BASE_TIME, e.getExceptionItems().get(1).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorErrorMetadataUnexpected() throws Exception {

        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode("code" + (new Date()).getTime());
        indicatorDto.setViewCode("viewcode" + (new Date()).getTime());
        indicatorDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setAcronym(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setSubjectCode(SUBJECT_1);
        indicatorDto.setSubjectTitle(IndicatorsMocks.mockInternationalStringDto(IndicatorsConstants.LOCALE_SPANISH, "Área temática 1"));
        indicatorDto.setComments(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setNotes(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setConceptDescription(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setQuantity(new QuantityDto());
        indicatorDto.getQuantity().setType(QuantityTypeEnum.AMOUNT);
        indicatorDto.getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        indicatorDto.getQuantity().setUnitMultiplier(Integer.valueOf(1000));
        indicatorDto.getQuantity().setMinimum(Integer.valueOf(1000));
        indicatorDto.getQuantity().setMaximum(Integer.valueOf(2000));
        indicatorDto.getQuantity().setNumeratorIndicatorUuid(INDICATOR_2);
        indicatorDto.getQuantity().setDenominatorIndicatorUuid(INDICATOR_3);
        indicatorDto.getQuantity().setIsPercentage(Boolean.FALSE);
        indicatorDto.getQuantity().setPercentageOf(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.getQuantity().setBaseValue(Integer.valueOf(1));
        indicatorDto.getQuantity().setBaseTime("2011");
        indicatorDto.getQuantity().setBaseLocationUuid(GEOGRAPHICAL_VALUE_1);
        indicatorDto.getQuantity().setBaseQuantityIndicatorUuid(INDICATOR_5);

        try {
            indicatorsServiceFacade.createIndicator(getServiceContextAdministrador(), indicatorDto);
            fail("metadatas unexpected");
        } catch (MetamacException e) {
            assertEquals(10, e.getExceptionItems().size());

            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.INDICATOR_QUANTITY_MINIMUM, e.getExceptionItems().get(0).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(1).getCode());
            assertEquals(1, e.getExceptionItems().get(1).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.INDICATOR_QUANTITY_MAXIMUM, e.getExceptionItems().get(1).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(2).getCode());
            assertEquals(1, e.getExceptionItems().get(2).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.INDICATOR_QUANTITY_NUMERATOR_INDICATOR_UUID, e.getExceptionItems().get(2).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(3).getCode());
            assertEquals(1, e.getExceptionItems().get(3).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.INDICATOR_QUANTITY_DENOMINATOR_INDICATOR_UUID, e.getExceptionItems().get(3).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(4).getCode());
            assertEquals(1, e.getExceptionItems().get(4).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.INDICATOR_QUANTITY_IS_PERCENTAGE, e.getExceptionItems().get(4).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(5).getCode());
            assertEquals(1, e.getExceptionItems().get(5).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.INDICATOR_QUANTITY_PERCENTAGE_OF, e.getExceptionItems().get(5).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(6).getCode());
            assertEquals(1, e.getExceptionItems().get(6).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.INDICATOR_QUANTITY_BASE_VALUE, e.getExceptionItems().get(6).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(7).getCode());
            assertEquals(1, e.getExceptionItems().get(7).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.INDICATOR_QUANTITY_BASE_TIME, e.getExceptionItems().get(7).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(8).getCode());
            assertEquals(1, e.getExceptionItems().get(8).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.INDICATOR_QUANTITY_BASE_LOCATION_UUID, e.getExceptionItems().get(8).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(9).getCode());
            assertEquals(1, e.getExceptionItems().get(9).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.INDICATOR_QUANTITY_BASE_QUANTITY_INDICATOR_UUID, e.getExceptionItems().get(9).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorErrorUnitNotExists() throws Exception {

        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode("code" + (new Date()).getTime());
        indicatorDto.setViewCode("viewcode" + (new Date()).getTime());
        indicatorDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setAcronym(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setSubjectCode(SUBJECT_1);
        indicatorDto.setSubjectTitle(IndicatorsMocks.mockInternationalStringDto(IndicatorsConstants.LOCALE_SPANISH, "Área temática 1"));
        indicatorDto.setComments(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setNotes(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setConceptDescription(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setQuantity(new QuantityDto());
        indicatorDto.getQuantity().setType(QuantityTypeEnum.QUANTITY);
        indicatorDto.getQuantity().setUnitUuid(NOT_EXISTS);
        indicatorDto.getQuantity().setUnitMultiplier(Integer.valueOf(1000));

        try {
            indicatorsServiceFacade.createIndicator(getServiceContextAdministrador(), indicatorDto);
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
        indicatorDto.setViewCode("ViewCode");
        indicatorDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setSubjectCode(SUBJECT_1);
        indicatorDto.setSubjectTitle(IndicatorsMocks.mockInternationalStringDto(IndicatorsConstants.LOCALE_SPANISH, "Área temática 1"));
        indicatorDto.setQuantity(new QuantityDto());
        indicatorDto.getQuantity().setType(QuantityTypeEnum.QUANTITY);
        indicatorDto.getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        indicatorDto.getQuantity().setUnitMultiplier(Integer.valueOf(1000));

        try {
            indicatorsServiceFacade.createIndicator(getServiceContextAdministrador(), indicatorDto);
            fail("code duplicated");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_ALREADY_EXIST_CODE_DUPLICATED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(indicatorDto.getCode(), e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorErrorViewCodeDuplicated() throws Exception {

        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode("CoDe");
        indicatorDto.setViewCode("ViewCode_1");
        indicatorDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setSubjectCode(SUBJECT_1);
        indicatorDto.setSubjectTitle(IndicatorsMocks.mockInternationalStringDto(IndicatorsConstants.LOCALE_SPANISH, "Área temática 1"));
        indicatorDto.setQuantity(new QuantityDto());
        indicatorDto.getQuantity().setType(QuantityTypeEnum.QUANTITY);
        indicatorDto.getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        indicatorDto.getQuantity().setUnitMultiplier(Integer.valueOf(1000));

        try {
            indicatorsServiceFacade.createIndicator(getServiceContextAdministrador(), indicatorDto);
            fail("code duplicated");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_ALREADY_EXIST_VIEW_CODE_DUPLICATED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(indicatorDto.getViewCode(), e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorCodeErrorDuplicatedInsensitive() throws Exception {

        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode("CoDe-1");
        indicatorDto.setViewCode("ViewCode");
        indicatorDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setSubjectCode(SUBJECT_1);
        indicatorDto.setSubjectTitle(IndicatorsMocks.mockInternationalStringDto(IndicatorsConstants.LOCALE_SPANISH, "Área temática 1"));
        indicatorDto.setQuantity(new QuantityDto());
        indicatorDto.getQuantity().setType(QuantityTypeEnum.QUANTITY);
        indicatorDto.getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        indicatorDto.getQuantity().setUnitMultiplier(Integer.valueOf(1000));

        try {
            indicatorsServiceFacade.createIndicator(getServiceContextAdministrador(), indicatorDto);
            fail("code duplicated");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_ALREADY_EXIST_CODE_DUPLICATED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(indicatorDto.getCode(), e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorViewCodeErrorDuplicatedInsensitive() throws Exception {
        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode("CoDe");
        indicatorDto.setViewCode("ViewCode_1");
        indicatorDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setSubjectCode(SUBJECT_1);
        indicatorDto.setSubjectTitle(IndicatorsMocks.mockInternationalStringDto(IndicatorsConstants.LOCALE_SPANISH, "Área temática 1"));
        indicatorDto.setQuantity(new QuantityDto());
        indicatorDto.getQuantity().setType(QuantityTypeEnum.QUANTITY);
        indicatorDto.getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        indicatorDto.getQuantity().setUnitMultiplier(Integer.valueOf(1000));

        try {
            indicatorsServiceFacade.createIndicator(getServiceContextAdministrador(), indicatorDto);
            fail("view code duplicated");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_ALREADY_EXIST_VIEW_CODE_DUPLICATED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(indicatorDto.getViewCode(), e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorErrorSubjectCodeNotExits() throws Exception {

        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode("code" + (new Date()).getTime());
        indicatorDto.setViewCode("viewcode" + (new Date()).getTime());
        indicatorDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setSubjectCode(NOT_EXISTS);
        indicatorDto.setSubjectTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setQuantity(new QuantityDto());
        indicatorDto.getQuantity().setType(QuantityTypeEnum.QUANTITY);
        indicatorDto.getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        indicatorDto.getQuantity().setUnitMultiplier(Integer.valueOf(1000));

        try {
            indicatorsServiceFacade.createIndicator(getServiceContextAdministrador(), indicatorDto);
            fail("subject code not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SUBJECT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(indicatorDto.getSubjectCode(), e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorReloadSubjectTitleIncorrect() throws Exception {

        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode("code" + (new Date()).getTime());
        indicatorDto.setViewCode("viewcode" + (new Date()).getTime());
        indicatorDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setSubjectCode(SUBJECT_1);
        indicatorDto.setSubjectTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setQuantity(new QuantityDto());
        indicatorDto.getQuantity().setType(QuantityTypeEnum.QUANTITY);
        indicatorDto.getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        indicatorDto.getQuantity().setUnitMultiplier(Integer.valueOf(1000));

        IndicatorDto indicatorCreated = indicatorsServiceFacade.createIndicator(getServiceContextAdministrador(), indicatorDto);
        assertEquals("Área temática 1", indicatorCreated.getSubjectTitle().getLocalised(IndicatorsConstants.LOCALE_SPANISH).getLabel());
    }

    @Test
    public void testCreateIndicatorErrorCodeIncorrect() throws Exception {

        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode("A*b-?");
        indicatorDto.setViewCode("viewCode");
        indicatorDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setAcronym(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setSubjectCode(SUBJECT_1);
        indicatorDto.setSubjectTitle(IndicatorsMocks.mockInternationalStringDto(IndicatorsConstants.LOCALE_SPANISH, "Área temática 1"));
        indicatorDto.setComments(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setNotes(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setConceptDescription(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setQuantity(new QuantityDto());
        indicatorDto.getQuantity().setType(QuantityTypeEnum.QUANTITY);
        indicatorDto.getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        indicatorDto.getQuantity().setUnitMultiplier(Integer.valueOf(1000));

        // Create
        try {
            indicatorsServiceFacade.createIndicator(getServiceContextAdministrador(), indicatorDto);
            fail("code incorrect");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.INDICATOR_CODE, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorErrorViewCodeIncorrect() throws Exception {

        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode("code");
        indicatorDto.setViewCode("$viewCode");
        indicatorDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setAcronym(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setSubjectCode(SUBJECT_1);
        indicatorDto.setSubjectTitle(IndicatorsMocks.mockInternationalStringDto(IndicatorsConstants.LOCALE_SPANISH, "Área temática 1"));
        indicatorDto.setComments(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setNotes(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setConceptDescription(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setQuantity(new QuantityDto());
        indicatorDto.getQuantity().setType(QuantityTypeEnum.QUANTITY);
        indicatorDto.getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        indicatorDto.getQuantity().setUnitMultiplier(Integer.valueOf(1000));

        // Create
        try {
            indicatorsServiceFacade.createIndicator(getServiceContextAdministrador(), indicatorDto);
            fail("view code incorrect");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.INDICATOR_VIEW_CODE, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateIndicatorErrorBaseTimeIncorrect() throws Exception {

        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCode("code" + (new Date()).getTime());
        indicatorDto.setViewCode("viewcode" + (new Date()).getTime());
        indicatorDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setAcronym(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setSubjectCode(SUBJECT_1);
        indicatorDto.setSubjectTitle(IndicatorsMocks.mockInternationalStringDto(IndicatorsConstants.LOCALE_SPANISH, "Área temática 1"));
        indicatorDto.setConceptDescription(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setQuantity(new QuantityDto());
        indicatorDto.getQuantity().setType(QuantityTypeEnum.INDEX);
        indicatorDto.getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        indicatorDto.getQuantity().setUnitMultiplier(Integer.valueOf(1000));
        indicatorDto.getQuantity().setIsPercentage(Boolean.FALSE);
        indicatorDto.getQuantity().setBaseTime("2011xx");

        // Create
        try {
            indicatorsServiceFacade.createIndicator(getServiceContextAdministrador(), indicatorDto);
            fail("base time incorrect");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.INDICATOR_QUANTITY_BASE_TIME, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testDeleteIndicator() throws Exception {

        String uuid = INDICATOR_4;

        // Delete indicator only in draft
        indicatorsServiceFacade.deleteIndicator(getServiceContextAdministrador(), uuid);

        // Validation
        try {
            indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, null);
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
        List<DataSourceDto> datasources = indicatorsServiceFacade.retrieveDataSourcesByIndicator(getServiceContextAdministrador(), uuid, "2.000");
        assertEquals(2, datasources.size());
        assertEquals(uuidDataSource1, datasources.get(0).getUuid());
        assertEquals(uuidDataSource2, datasources.get(1).getUuid());

        // Delete indicator
        indicatorsServiceFacade.deleteIndicator(getServiceContextAdministrador(), uuid);

        // Validate data sources are deleted
        try {
            indicatorsServiceFacade.retrieveDataSource(getServiceContextAdministrador(), uuidDataSource1);
            fail("Datasource deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DATA_SOURCE_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuidDataSource1, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
        try {
            indicatorsServiceFacade.retrieveDataSource(getServiceContextAdministrador(), uuidDataSource2);
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
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, "1.000");
            assertEquals(uuid, indicatorDto.getUuid());
            assertEquals("1.000", indicatorDto.getVersionNumber());
            assertEquals("1.000", indicatorDto.getPublishedVersion());
            assertEquals("2.000", indicatorDto.getProductionVersion());
            assertNull(indicatorDto.getArchivedVersion());
        }

        // Delete indicator
        indicatorsServiceFacade.deleteIndicator(getServiceContextAdministrador(), uuid);

        // Validation
        // Version 1 exists
        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, "1.000");
            assertEquals(uuid, indicatorDto.getUuid());
            assertEquals("1.000", indicatorDto.getVersionNumber());
            assertEquals("1.000", indicatorDto.getPublishedVersion());
            assertNull(indicatorDto.getArchivedVersion());
            assertEquals(null, indicatorDto.getProductionVersion());
        }
        // Version 2 not exists
        try {
            indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, "2.000");
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
            indicatorsServiceFacade.deleteIndicator(getServiceContextAdministrador(), uuid);
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
            indicatorsServiceFacade.deleteIndicator(getServiceContextAdministrador(), uuid);
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
            indicatorsServiceFacade.deleteIndicator(getServiceContextAdministrador(), uuid);
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
        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuidLinked, null);
        assertEquals(uuid, indicatorDto.getQuantity().getNumeratorIndicatorUuid());
        assertNull(indicatorDto.getQuantity().getDenominatorIndicatorUuid());
        assertNull(indicatorDto.getQuantity().getBaseQuantityIndicatorUuid());

        // Try delete
        try {
            indicatorsServiceFacade.deleteIndicator(getServiceContextAdministrador(), uuid);
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
        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuidLinked, null);
        indicatorDto.getQuantity().setType(QuantityTypeEnum.FRACTION);
        indicatorDto.getQuantity().setNumeratorIndicatorUuid(null);
        indicatorDto.getQuantity().setDenominatorIndicatorUuid(uuid);
        indicatorDto.getQuantity().setBaseQuantityIndicatorUuid(null);
        indicatorsServiceFacade.updateIndicator(getServiceContextAdministrador(), indicatorDto);

        // Try delete
        try {
            indicatorsServiceFacade.deleteIndicator(getServiceContextAdministrador(), uuid);
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
        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuidLinked, null);
        indicatorDto.getQuantity().setType(QuantityTypeEnum.FRACTION);
        indicatorDto.getQuantity().setNumeratorIndicatorUuid(null);
        indicatorDto.getQuantity().setDenominatorIndicatorUuid(uuid);
        indicatorDto.getQuantity().setBaseQuantityIndicatorUuid(null);
        indicatorsServiceFacade.updateIndicator(getServiceContextAdministrador(), indicatorDto);

        // Try delete
        try {
            indicatorsServiceFacade.deleteIndicator(getServiceContextAdministrador(), uuid);
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

        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, versionNumber);
        assertEquals(IndicatorProcStatusEnum.DRAFT, indicatorDto.getProcStatus());

        indicatorDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setAcronym(IndicatorsMocks.mockInternationalStringDto());

        // Update
        IndicatorDto indicatorDtoUpdated = indicatorsServiceFacade.updateIndicator(getServiceContextAdministrador(), indicatorDto);
        IndicatorsAsserts.assertEqualsIndicator(indicatorDto, indicatorDtoUpdated);

        // Validation
        indicatorDtoUpdated = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, versionNumber);
        IndicatorsAsserts.assertEqualsIndicator(indicatorDto, indicatorDtoUpdated);
        assertTrue(indicatorDtoUpdated.getLastUpdated().after(indicatorDtoUpdated.getCreatedDate()));
    }

    @Test
    public void testUpdateIndicatorInRejectedValidation() throws Exception {

        String uuid = INDICATOR_9;
        String versionNumber = "1.000";

        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, versionNumber);
        assertEquals(IndicatorProcStatusEnum.VALIDATION_REJECTED, indicatorDto.getProcStatus());

        indicatorDto.setTitle(IndicatorsMocks.mockInternationalStringDto());

        // Update
        indicatorsServiceFacade.updateIndicator(getServiceContextAdministrador(), indicatorDto);

        // Validation
        IndicatorDto indicatorDtoUpdated = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, versionNumber);
        assertEquals(IndicatorProcStatusEnum.VALIDATION_REJECTED, indicatorDtoUpdated.getProcStatus());
        IndicatorsAsserts.assertEqualsIndicator(indicatorDto, indicatorDtoUpdated);
    }

    @Test
    public void testUpdateIndicatorInProductionValidation() throws Exception {

        String uuid = INDICATOR_4;
        String versionNumber = "1.000";

        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, versionNumber);
        assertEquals(IndicatorProcStatusEnum.PRODUCTION_VALIDATION, indicatorDto.getProcStatus());

        indicatorDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setSubjectCode(SUBJECT_1);
        indicatorDto.setSubjectTitle(IndicatorsMocks.mockInternationalStringDto(IndicatorsConstants.LOCALE_SPANISH, "Área temática 1"));

        // Update
        indicatorsServiceFacade.updateIndicator(getServiceContextAdministrador(), indicatorDto);

        // Validation
        IndicatorDto indicatorDtoUpdated = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, versionNumber);
        assertEquals(IndicatorProcStatusEnum.PRODUCTION_VALIDATION, indicatorDtoUpdated.getProcStatus());
        IndicatorsAsserts.assertEqualsIndicator(indicatorDto, indicatorDtoUpdated);
    }

    @Test
    public void testUpdateIndicatorInDiffusionValidation() throws Exception {

        String uuid = INDICATOR_5;
        String versionNumber = "1.000";

        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, versionNumber);
        assertEquals(IndicatorProcStatusEnum.DIFFUSION_VALIDATION, indicatorDto.getProcStatus());

        indicatorDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setSubjectCode(SUBJECT_1);
        indicatorDto.setSubjectTitle(IndicatorsMocks.mockInternationalStringDto(IndicatorsConstants.LOCALE_SPANISH, "Área temática 1"));

        // Update
        indicatorsServiceFacade.updateIndicator(getServiceContextAdministrador(), indicatorDto);

        // Validation
        IndicatorDto indicatorDtoUpdated = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, versionNumber);
        assertEquals(IndicatorProcStatusEnum.DIFFUSION_VALIDATION, indicatorDtoUpdated.getProcStatus());
        IndicatorsAsserts.assertEqualsIndicator(indicatorDto, indicatorDtoUpdated);
    }

    @Test
    public void testUpdateIndicatorInPublicationFailed() throws Exception {

        String uuid = INDICATOR_11;
        String versionNumber = "1.000";

        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, versionNumber);
        assertEquals(IndicatorProcStatusEnum.PUBLICATION_FAILED, indicatorDto.getProcStatus());

        indicatorDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.setSubjectCode(SUBJECT_1);
        indicatorDto.setSubjectTitle(IndicatorsMocks.mockInternationalStringDto(IndicatorsConstants.LOCALE_SPANISH, "Área temática 1"));

        // Update
        indicatorsServiceFacade.updateIndicator(getServiceContextAdministrador(), indicatorDto);

        // Validation
        IndicatorDto indicatorDtoUpdated = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, versionNumber);
        assertEquals(IndicatorProcStatusEnum.PUBLICATION_FAILED, indicatorDtoUpdated.getProcStatus());
        IndicatorsAsserts.assertEqualsIndicator(indicatorDto, indicatorDtoUpdated);
    }

    @Test
    public void testUpdateIndicatorReusingLocalisedStrings() throws Exception {

        String uuid = INDICATOR_1;
        String versionNumber = "2.000";

        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, versionNumber);
        indicatorDto.getTitle().getTexts().iterator().next().setLabel("NewLabel");

        // Update
        indicatorsServiceFacade.updateIndicator(getServiceContextAdministrador(), indicatorDto);

        // Validation
        IndicatorDto indicatorDtoUpdated = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, versionNumber);
        IndicatorsAsserts.assertEqualsIndicator(indicatorDto, indicatorDtoUpdated);
    }

    @Test
    public void testUpdateIndicatorErrorParameterRequired() throws Exception {

        String uuid = INDICATOR_1;
        String versionNumber = "2.000";

        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, versionNumber);
        indicatorDto.setTitle(null);

        try {
            indicatorsServiceFacade.updateIndicator(getServiceContextAdministrador(), indicatorDto);
            fail("parameters required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.INDICATOR_TITLE, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateIndicatorErrorNotExists() throws Exception {

        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), INDICATOR_1, "2.000");
        indicatorDto.setUuid(NOT_EXISTS);
        indicatorDto.setVersionNumber(null);

        try {
            indicatorsServiceFacade.updateIndicator(getServiceContextAdministrador(), indicatorDto);
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

        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, versionNumber);
        indicatorDto.setSubjectCode(SUBJECT_1);
        indicatorDto.setSubjectTitle(IndicatorsMocks.mockInternationalStringDto(IndicatorsConstants.LOCALE_SPANISH, "Área temática 1"));

        try {
            indicatorsServiceFacade.updateIndicator(getServiceContextAdministrador(), indicatorDto);
            fail("Indicator not in production");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.INDICATOR_PROC_STATUS_DRAFT, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(ServiceExceptionParameters.INDICATOR_PROC_STATUS_VALIDATION_REJECTED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
            assertEquals(ServiceExceptionParameters.INDICATOR_PROC_STATUS_PRODUCTION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[2]);
            assertEquals(ServiceExceptionParameters.INDICATOR_PROC_STATUS_DIFFUSION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[3]);
            assertEquals(ServiceExceptionParameters.INDICATOR_PROC_STATUS_PUBLICATION_FAILED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[4]);
        }
    }

    @Test
    public void testUpdateIndicatorErrorWrongVersion() throws Exception {

        String uuid = INDICATOR_1;
        String versionNumber = "1.000";

        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, versionNumber);

        try {
            indicatorsServiceFacade.updateIndicator(getServiceContextAdministrador(), indicatorDto);
            fail("Version 1 not is in production");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.INDICATOR_PROC_STATUS_DRAFT, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(ServiceExceptionParameters.INDICATOR_PROC_STATUS_VALIDATION_REJECTED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
            assertEquals(ServiceExceptionParameters.INDICATOR_PROC_STATUS_PRODUCTION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[2]);
            assertEquals(ServiceExceptionParameters.INDICATOR_PROC_STATUS_DIFFUSION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[3]);
            assertEquals(ServiceExceptionParameters.INDICATOR_PROC_STATUS_PUBLICATION_FAILED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[4]);
        }
    }

    @Test
    public void testUpdateIndicatorsErrorCodeNonModifiable() throws Exception {

        String uuid = INDICATOR_1;
        String versionNumber = "2.000";

        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, versionNumber);
        indicatorDto.setCode("newCode");

        // Update
        try {
            indicatorsServiceFacade.updateIndicator(getServiceContextAdministrador(), indicatorDto);
            fail("Code is unmodifiable");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_UNMODIFIABLE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.INDICATOR_CODE, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateIndicatorsErrorViewCodeNonModifiable() throws Exception {

        String uuid = INDICATOR_1;
        String versionNumber = "2.000";

        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, versionNumber);
        indicatorDto.setViewCode("newViewCode");

        // Update
        try {
            indicatorsServiceFacade.updateIndicator(getServiceContextAdministrador(), indicatorDto);
            fail("View Code is unmodifiable");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_UNMODIFIABLE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.INDICATOR_VIEW_CODE, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateIndicatorsErrorDataRepositoryNonModifiable() throws Exception {

        String uuid = INDICATOR_1;
        String versionNumber = "2.000";

        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, versionNumber);
        indicatorDto.setDataRepositoryId("newDataRepositoryId");
        indicatorDto.setDataRepositoryTableName("newTable");

        // Update
        try {
            indicatorsServiceFacade.updateIndicator(getServiceContextAdministrador(), indicatorDto);
            fail("attributes are unmodifiable");
        } catch (MetamacException e) {
            assertEquals(2, e.getExceptionItems().size());

            assertEquals(ServiceExceptionType.METADATA_UNMODIFIABLE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.INDICATOR_DATA_REPOSITORY_ID, e.getExceptionItems().get(0).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_UNMODIFIABLE.getCode(), e.getExceptionItems().get(1).getCode());
            assertEquals(1, e.getExceptionItems().get(1).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.INDICATOR_DATA_REPOSITORY_TABLE_NAME, e.getExceptionItems().get(1).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateIndicatorsErrorOwnIndicatorLinked() throws Exception {

        String uuid = INDICATOR_1;
        String versionNumber = "2.000";

        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, versionNumber);
        indicatorDto.getQuantity().setType(QuantityTypeEnum.FRACTION);
        indicatorDto.getQuantity().setNumeratorIndicatorUuid(uuid);

        // Update
        try {
            indicatorsServiceFacade.updateIndicator(getServiceContextAdministrador(), indicatorDto);
            fail("Indicator linked");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.INDICATOR_QUANTITY_NUMERATOR_INDICATOR_UUID, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateIndicatorErrorOptimisticLocking() throws Exception {

        String uuid = INDICATOR_1;
        String versionNumber = "2.000";

        IndicatorDto indicatorDtoSession1 = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, versionNumber);
        assertEquals(Long.valueOf(1), indicatorDtoSession1.getVersionOptimisticLocking());

        IndicatorDto indicatorDtoSession2 = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, versionNumber);
        assertEquals(Long.valueOf(1), indicatorDtoSession2.getVersionOptimisticLocking());

        // Update by session 1
        IndicatorDto indicatorDtoSession1AfterUpdate = indicatorsServiceFacade.updateIndicator(getServiceContextAdministrador(), indicatorDtoSession1);
        IndicatorsAsserts.assertEqualsIndicator(indicatorDtoSession1, indicatorDtoSession1AfterUpdate);
        assertEquals(Long.valueOf(2), indicatorDtoSession1AfterUpdate.getVersionOptimisticLocking());

        // Fails when is updated by session 2
        try {
            indicatorsServiceFacade.updateIndicator(getServiceContextAdministrador(), indicatorDtoSession2);
            fail("Optimistic locking");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.OPTIMISTIC_LOCKING.getCode(), e.getExceptionItems().get(0).getCode());
            assertNull(e.getExceptionItems().get(0).getMessageParameters());
        }

        // Session 1 can modify because has last version
        indicatorDtoSession1AfterUpdate.setTitle(IndicatorsMocks.mockInternationalStringDto());
        IndicatorDto indicatorDtoSession1AfterUpdate2 = indicatorsServiceFacade.updateIndicator(getServiceContextAdministrador(), indicatorDtoSession1AfterUpdate);
        assertEquals(Long.valueOf(3), indicatorDtoSession1AfterUpdate2.getVersionOptimisticLocking());
        IndicatorsAsserts.assertEqualsIndicator(indicatorDtoSession1AfterUpdate, indicatorDtoSession1AfterUpdate2);
    }

    @Test
    public void testSendIndicatorToProductionValidation() throws Exception {

        String uuid = INDICATOR_1;
        String publishedVersion = "1.000";
        String productionVersion = "2.000";

        {
            IndicatorDto indicatorDtoV1 = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, publishedVersion);
            IndicatorDto indicatorDtoV2 = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, productionVersion);
            assertEquals(publishedVersion, indicatorDtoV1.getPublishedVersion());
            assertEquals(productionVersion, indicatorDtoV2.getProductionVersion());
            assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorDtoV1.getProcStatus());
            assertEquals(IndicatorProcStatusEnum.DRAFT, indicatorDtoV2.getProcStatus());
        }

        // Sends to production validation
        IndicatorDto indicatorDtoV2Updated = indicatorsServiceFacade.sendIndicatorToProductionValidation(getServiceContextAdministrador2(), uuid);

        // Validation
        {
            assertEquals(publishedVersion, indicatorDtoV2Updated.getPublishedVersion());
            assertEquals(productionVersion, indicatorDtoV2Updated.getProductionVersion());
            assertEquals(IndicatorProcStatusEnum.PRODUCTION_VALIDATION, indicatorDtoV2Updated.getProcStatus());

            assertTrue(DateUtils.isSameDay(new Date(), indicatorDtoV2Updated.getProductionValidationDate()));
            assertEquals(getServiceContextAdministrador2().getUserId(), indicatorDtoV2Updated.getProductionValidationUser());
            assertNull(indicatorDtoV2Updated.getDiffusionValidationDate());
            assertNull(indicatorDtoV2Updated.getDiffusionValidationUser());
            assertNull(indicatorDtoV2Updated.getPublicationDate());
            assertNull(indicatorDtoV2Updated.getPublicationUser());
            assertNull(indicatorDtoV2Updated.getArchiveDate());
            assertNull(indicatorDtoV2Updated.getArchiveUser());
        }
        {
            IndicatorDto indicatorDtoV1 = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, publishedVersion);
            IndicatorDto indicatorDtoV2 = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, productionVersion);
            assertEquals(publishedVersion, indicatorDtoV1.getPublishedVersion());
            assertEquals(productionVersion, indicatorDtoV1.getProductionVersion());
            assertEquals(publishedVersion, indicatorDtoV2.getPublishedVersion());
            assertEquals(productionVersion, indicatorDtoV2.getProductionVersion());
            assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorDtoV1.getProcStatus());
            assertEquals(IndicatorProcStatusEnum.PRODUCTION_VALIDATION, indicatorDtoV2.getProcStatus());

            assertTrue(DateUtils.isSameDay(new Date(), indicatorDtoV2.getProductionValidationDate()));
            assertEquals(getServiceContextAdministrador2().getUserId(), indicatorDtoV2.getProductionValidationUser());
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
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, productionVersion);
            assertEquals(productionVersion, indicatorDto.getProductionVersion());
            assertNull(indicatorDto.getPublishedVersion());
            assertNull(indicatorDto.getArchivedVersion());
            assertEquals(IndicatorProcStatusEnum.VALIDATION_REJECTED, indicatorDto.getProcStatus());
        }

        // Sends to production validation
        indicatorsServiceFacade.sendIndicatorToProductionValidation(getServiceContextAdministrador2(), uuid);

        // Validation
        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, productionVersion);
            assertEquals(productionVersion, indicatorDto.getProductionVersion());
            assertNull(indicatorDto.getPublishedVersion());
            assertNull(indicatorDto.getArchivedVersion());
            assertEquals(IndicatorProcStatusEnum.PRODUCTION_VALIDATION, indicatorDto.getProcStatus());
        }
    }

    @Test
    public void testSendIndicatorToProductionValidationErrorNotExists() throws Exception {

        try {
            indicatorsServiceFacade.sendIndicatorToProductionValidation(getServiceContextAdministrador(), NOT_EXISTS);
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
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, INDICATOR_3_VERSION);
            assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorDto.getProcStatus());
            assertNull(indicatorDto.getProductionVersion());
        }

        try {
            indicatorsServiceFacade.sendIndicatorToProductionValidation(getServiceContextAdministrador(), uuid);
            fail("Indicator is not draft");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.INDICATOR_PROC_STATUS_DRAFT, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(ServiceExceptionParameters.INDICATOR_PROC_STATUS_VALIDATION_REJECTED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);

        }
    }

    @Test
    public void testSendIndicatorToProductionValidationErrorWithoutDataSources() throws Exception {

        String uuid = INDICATOR_2;

        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, "1.000");
            assertEquals(IndicatorProcStatusEnum.DRAFT, indicatorDto.getProcStatus());

            // Check zero data sources
            List<DataSourceDto> dataSources = indicatorsServiceFacade.retrieveDataSourcesByIndicator(getServiceContextAdministrador(), uuid, "1.000");
            assertEquals(0, dataSources.size());
        }

        try {
            indicatorsServiceFacade.sendIndicatorToProductionValidation(getServiceContextAdministrador(), uuid);
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

        IndicatorDto indicatorDtoV2 = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, productionVersion);
        assertEquals(productionVersion, indicatorDtoV2.getProductionVersion());
        assertEquals(IndicatorProcStatusEnum.DRAFT, indicatorDtoV2.getProcStatus());

        // Update to clear quantity required attributes
        indicatorDtoV2.getQuantity().setType(QuantityTypeEnum.CHANGE_RATE);
        indicatorDtoV2.getQuantity().setUnitUuid(null);
        indicatorsServiceFacade.updateIndicator(getServiceContextAdministrador(), indicatorDtoV2);

        // Sends to production validation
        try {
            indicatorsServiceFacade.sendIndicatorToProductionValidation(getServiceContextAdministrador(), uuid);
            fail("Indicator quantity incomplete");
        } catch (MetamacException e) {
            assertEquals(3, e.getExceptionItems().size());

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.INDICATOR_QUANTITY_UNIT_UUID, e.getExceptionItems().get(0).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(1).getCode());
            assertEquals(1, e.getExceptionItems().get(1).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.INDICATOR_QUANTITY_IS_PERCENTAGE, e.getExceptionItems().get(1).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(2).getCode());
            assertEquals(1, e.getExceptionItems().get(2).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.INDICATOR_QUANTITY_BASE_QUANTITY_INDICATOR_UUID, e.getExceptionItems().get(2).getMessageParameters()[0]);
        }
    }

    @Test
    public void testSendIndicatorToDiffusionValidation() throws Exception {

        String uuid = INDICATOR_4;
        String versionNumber = "1.000";

        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, versionNumber);
            assertNull(indicatorDto.getPublishedVersion());
            assertNull(indicatorDto.getArchivedVersion());
            assertEquals("1.000", indicatorDto.getProductionVersion());
            assertEquals(IndicatorProcStatusEnum.PRODUCTION_VALIDATION, indicatorDto.getProcStatus());
        }

        // Sends to diffusion validation
        IndicatorDto indicatorDtoV1Updated = indicatorsServiceFacade.sendIndicatorToDiffusionValidation(getServiceContextAdministrador(), uuid);

        // Validation
        {
            assertEquals(null, indicatorDtoV1Updated.getPublishedVersion());
            assertEquals(null, indicatorDtoV1Updated.getArchivedVersion());
            assertEquals("1.000", indicatorDtoV1Updated.getProductionVersion());
            assertEquals(IndicatorProcStatusEnum.DIFFUSION_VALIDATION, indicatorDtoV1Updated.getProcStatus());

            IndicatorsAsserts.assertEqualsDate("2011-04-04 01:02:04", indicatorDtoV1Updated.getProductionValidationDate());
            assertEquals("user1", indicatorDtoV1Updated.getProductionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), indicatorDtoV1Updated.getDiffusionValidationDate()));
            assertEquals(getServiceContextAdministrador().getUserId(), indicatorDtoV1Updated.getDiffusionValidationUser());
            assertNull(indicatorDtoV1Updated.getPublicationDate());
            assertNull(indicatorDtoV1Updated.getPublicationUser());
            assertNull(indicatorDtoV1Updated.getArchiveDate());
            assertNull(indicatorDtoV1Updated.getArchiveUser());
        }
        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, versionNumber);
            assertEquals(null, indicatorDto.getPublishedVersion());
            assertEquals(null, indicatorDto.getArchivedVersion());
            assertEquals("1.000", indicatorDto.getProductionVersion());
            assertEquals(IndicatorProcStatusEnum.DIFFUSION_VALIDATION, indicatorDto.getProcStatus());

            IndicatorsAsserts.assertEqualsDate("2011-04-04 01:02:04", indicatorDto.getProductionValidationDate());
            assertEquals("user1", indicatorDto.getProductionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), indicatorDto.getDiffusionValidationDate()));
            assertEquals(getServiceContextAdministrador().getUserId(), indicatorDto.getDiffusionValidationUser());
            assertNull(indicatorDto.getPublicationDate());
            assertNull(indicatorDto.getPublicationUser());
            assertNull(indicatorDto.getArchiveDate());
            assertNull(indicatorDto.getArchiveUser());
        }
    }

    @Test
    public void testSendIndicatorToDiffusionValidationErrorNotExists() throws Exception {

        try {
            indicatorsServiceFacade.sendIndicatorToDiffusionValidation(getServiceContextAdministrador(), NOT_EXISTS);
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
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, "1.000");
            assertEquals(IndicatorProcStatusEnum.DRAFT, indicatorDto.getProcStatus());
            assertEquals("1.000", indicatorDto.getProductionVersion());
            assertEquals(null, indicatorDto.getPublishedVersion());
        }

        try {
            indicatorsServiceFacade.sendIndicatorToDiffusionValidation(getServiceContextAdministrador(), uuid);
            fail("Indicator is not draft");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.INDICATOR_PROC_STATUS_PRODUCTION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Test
    public void testSendIndicatorToDiffusionValidationErrorWrongProcStatusPublished() throws Exception {

        String uuid = INDICATOR_3;

        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, INDICATOR_3_VERSION);
            assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorDto.getProcStatus());
            assertEquals(null, indicatorDto.getProductionVersion());
            assertEquals(INDICATOR_3_VERSION, indicatorDto.getPublishedVersion());
            assertEquals(null, indicatorDto.getArchivedVersion());
        }

        try {
            indicatorsServiceFacade.sendIndicatorToDiffusionValidation(getServiceContextAdministrador(), uuid);
            fail("Indicator is not production validation");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.INDICATOR_PROC_STATUS_PRODUCTION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Test
    public void testRejectIndicatorProductionValidation() throws Exception {

        String uuid = INDICATOR_4;
        String versionNumber = "1.000";

        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, versionNumber);
            assertEquals(null, indicatorDto.getPublishedVersion());
            assertEquals("1.000", indicatorDto.getProductionVersion());
            assertEquals(IndicatorProcStatusEnum.PRODUCTION_VALIDATION, indicatorDto.getProcStatus());
        }

        // Rejects validation
        IndicatorDto indicatorDtoV1Updated = indicatorsServiceFacade.rejectIndicatorProductionValidation(getServiceContextAdministrador(), uuid);

        // Validation
        {
            assertEquals(null, indicatorDtoV1Updated.getPublishedVersion());
            assertEquals(null, indicatorDtoV1Updated.getArchivedVersion());
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
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, versionNumber);
            assertEquals(null, indicatorDto.getPublishedVersion());
            assertEquals(null, indicatorDto.getArchivedVersion());
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
    public void testRejectIndicatorProductionValidationErrorNotExists() throws Exception {

        try {
            indicatorsServiceFacade.rejectIndicatorProductionValidation(getServiceContextAdministrador(), NOT_EXISTS);
            fail("Indicator not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRejectIndicatorProductionValidationErrorWrongProcStatusProduction() throws Exception {

        String uuid = INDICATOR_2;

        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, "1.000");
            assertEquals(IndicatorProcStatusEnum.DRAFT, indicatorDto.getProcStatus());
            assertEquals("1.000", indicatorDto.getProductionVersion());
            assertEquals(null, indicatorDto.getPublishedVersion());
        }

        try {
            indicatorsServiceFacade.rejectIndicatorProductionValidation(getServiceContextAdministrador(), uuid);
            fail("Indicator is not in validation");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.INDICATOR_PROC_STATUS_PRODUCTION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Test
    public void testRejectIndicatorValidationInDiffusionValidation() throws Exception {

        String uuid = INDICATOR_5;
        String versionNumber = "1.000";

        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, versionNumber);
            assertEquals(null, indicatorDto.getPublishedVersion());
            assertEquals(null, indicatorDto.getArchivedVersion());
            assertEquals("1.000", indicatorDto.getProductionVersion());
            assertEquals(IndicatorProcStatusEnum.DIFFUSION_VALIDATION, indicatorDto.getProcStatus());
        }

        // Rejects validation
        indicatorsServiceFacade.rejectIndicatorDiffusionValidation(getServiceContextAdministrador(), uuid);

        // Validation
        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, versionNumber);
            assertEquals(null, indicatorDto.getPublishedVersion());
            assertEquals(null, indicatorDto.getArchivedVersion());
            assertEquals("1.000", indicatorDto.getProductionVersion());
            assertEquals(IndicatorProcStatusEnum.VALIDATION_REJECTED, indicatorDto.getProcStatus());
        }
    }

    @Test
    public void testRejectIndicatorDiffusionValidationErrorNotExists() throws Exception {

        try {
            indicatorsServiceFacade.rejectIndicatorDiffusionValidation(getServiceContextAdministrador(), NOT_EXISTS);
            fail("Indicator not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRejectIndicatorDiffusionValidationErrorWrongProcStatusProduction() throws Exception {

        String uuid = INDICATOR_2;

        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, "1.000");
            assertEquals(IndicatorProcStatusEnum.DRAFT, indicatorDto.getProcStatus());
            assertEquals("1.000", indicatorDto.getProductionVersion());
            assertEquals(null, indicatorDto.getPublishedVersion());
            assertEquals(null, indicatorDto.getArchivedVersion());
        }

        try {
            indicatorsServiceFacade.rejectIndicatorDiffusionValidation(getServiceContextAdministrador(), uuid);
            fail("Indicator is not in validation");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.INDICATOR_PROC_STATUS_DIFFUSION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Test
    public void testRejectIndicatorDiffusionValidationErrorWrongProcStatus() throws Exception {

        String uuid = INDICATOR_3;

        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, INDICATOR_3_VERSION);
            assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorDto.getProcStatus());
            assertEquals(null, indicatorDto.getProductionVersion());
            assertEquals(INDICATOR_3_VERSION, indicatorDto.getPublishedVersion());
            assertEquals(null, indicatorDto.getArchivedVersion());
        }

        try {
            indicatorsServiceFacade.rejectIndicatorDiffusionValidation(getServiceContextAdministrador(), uuid);
            fail("Indicator is not in validation");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.INDICATOR_PROC_STATUS_DIFFUSION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Test
    public void testPublishIndicator() throws Exception {

        String uuid = INDICATOR_5;
        String versionNumber = "1.000";

        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, versionNumber);
            assertEquals(versionNumber, indicatorDto.getProductionVersion());
            assertEquals(null, indicatorDto.getPublishedVersion());
            assertEquals(null, indicatorDto.getArchivedVersion());
            assertEquals(IndicatorProcStatusEnum.DIFFUSION_VALIDATION, indicatorDto.getProcStatus());
        }

        // Publish
        PublishIndicatorResultDto publishIndicatorResultDto = indicatorsServiceFacade.publishIndicator(getServiceContextAdministrador(), uuid);
        IndicatorDto indicatorDtoV1Updated = publishIndicatorResultDto.getIndicator();
        assertNull(publishIndicatorResultDto.getPublicationFailedReason());

        // Validation
        {
            assertEquals(null, indicatorDtoV1Updated.getProductionVersion());
            assertEquals(versionNumber, indicatorDtoV1Updated.getPublishedVersion());
            assertEquals(null, indicatorDtoV1Updated.getArchivedVersion());
            assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorDtoV1Updated.getProcStatus());

            IndicatorsAsserts.assertEqualsDate("2011-06-06 01:02:04", indicatorDtoV1Updated.getProductionValidationDate());
            assertEquals("user1", indicatorDtoV1Updated.getProductionValidationUser());
            IndicatorsAsserts.assertEqualsDate("2011-07-07 03:02:04", indicatorDtoV1Updated.getDiffusionValidationDate());
            assertEquals("user2", indicatorDtoV1Updated.getDiffusionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), indicatorDtoV1Updated.getPublicationDate()));
            assertEquals(getServiceContextAdministrador().getUserId(), indicatorDtoV1Updated.getPublicationUser());
            assertNull(indicatorDtoV1Updated.getArchiveDate());
            assertNull(indicatorDtoV1Updated.getArchiveUser());
        }
        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, versionNumber);
            assertEquals(null, indicatorDto.getProductionVersion());
            assertEquals(versionNumber, indicatorDto.getPublishedVersion());
            assertEquals(null, indicatorDto.getArchivedVersion());
            assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorDto.getProcStatus());

            IndicatorsAsserts.assertEqualsDate("2011-06-06 01:02:04", indicatorDto.getProductionValidationDate());
            assertEquals("user1", indicatorDto.getProductionValidationUser());
            IndicatorsAsserts.assertEqualsDate("2011-07-07 03:02:04", indicatorDto.getDiffusionValidationDate());
            assertEquals("user2", indicatorDto.getDiffusionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), indicatorDto.getPublicationDate()));
            assertEquals(getServiceContextAdministrador().getUserId(), indicatorDto.getPublicationUser());
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
            IndicatorDto indicatorDtoV1 = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, diffusionVersionBefore);
            IndicatorDto indicatorDtoV2 = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, productionVersionBefore);
            assertEquals(productionVersionBefore, indicatorDtoV1.getProductionVersion());
            assertEquals(diffusionVersionBefore, indicatorDtoV1.getPublishedVersion());
            assertEquals(null, indicatorDtoV1.getArchivedVersion());
            assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorDtoV1.getProcStatus());
            assertEquals(IndicatorProcStatusEnum.DIFFUSION_VALIDATION, indicatorDtoV2.getProcStatus());
        }

        // Publish
        indicatorsServiceFacade.publishIndicator(getServiceContextAdministrador(), uuid);

        // Validation
        {
            // Version 1 already not exists
            try {
                indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, diffusionVersionBefore);
                fail("Indicator version not exists");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.INDICATOR_VERSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
                assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
                assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
                assertEquals(diffusionVersionBefore, e.getExceptionItems().get(0).getMessageParameters()[1]);
            }

            // Actual version in diffusion is version 2
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, productionVersionBefore);
            assertEquals(null, indicatorDto.getProductionVersion());
            assertEquals("2.000", indicatorDto.getPublishedVersion());
            assertEquals(null, indicatorDto.getArchivedVersion());
            assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorDto.getProcStatus());

            // Old diffusion dataset must have been deleted
            // Verify does not work so well with Spring
            // Mockito.verify(indicatorsDataService).deleteIndicatorData(Matchers.any(ServiceContext.class),Matchers.eq(uuid), Matchers.eq(diffusionVersionBefore));
        }
    }

    @Test
    public void testPublishIndicatorWithArchivedVersion() throws Exception {

        String uuid = INDICATOR_7;
        String diffusionVersionBefore = "1.000"; // will be deleted when publish current version in diffusion validation
        String productionVersionBefore = "2.000"; // will be published

        {
            IndicatorDto indicatorDtoV1 = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, diffusionVersionBefore);
            IndicatorDto indicatorDtoV2 = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, productionVersionBefore);
            assertEquals(productionVersionBefore, indicatorDtoV1.getProductionVersion());
            assertEquals(null, indicatorDtoV1.getPublishedVersion());
            assertEquals(diffusionVersionBefore, indicatorDtoV1.getArchivedVersion());
            assertEquals(IndicatorProcStatusEnum.ARCHIVED, indicatorDtoV1.getProcStatus());
            assertEquals(IndicatorProcStatusEnum.DIFFUSION_VALIDATION, indicatorDtoV2.getProcStatus());
        }

        // Publish
        indicatorsServiceFacade.publishIndicator(getServiceContextAdministrador(), uuid);

        // Validation
        {
            // Version 1 already not exists
            try {
                indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, diffusionVersionBefore);
                fail("Indicator version not exists");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.INDICATOR_VERSION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
                assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
                assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
                assertEquals(diffusionVersionBefore, e.getExceptionItems().get(0).getMessageParameters()[1]);
            }

            // Actual version in diffusion is version 2
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, productionVersionBefore);
            assertEquals(null, indicatorDto.getProductionVersion());
            assertEquals("2.000", indicatorDto.getPublishedVersion());
            assertEquals(null, indicatorDto.getArchivedVersion());
            assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorDto.getProcStatus());
        }
    }

    @Test
    public void testPublishIndicatorErrorNotExists() throws Exception {

        try {
            indicatorsServiceFacade.publishIndicator(getServiceContextAdministrador(), NOT_EXISTS);
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
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, "1.000");
            assertEquals(IndicatorProcStatusEnum.DRAFT, indicatorDto.getProcStatus());
            assertEquals("1.000", indicatorDto.getProductionVersion());
            assertEquals(null, indicatorDto.getPublishedVersion());
            assertEquals(null, indicatorDto.getArchivedVersion());
        }

        try {
            indicatorsServiceFacade.publishIndicator(getServiceContextAdministrador(), uuid);
            fail("Indicator is not diffusion validation");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.INDICATOR_PROC_STATUS_DIFFUSION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(ServiceExceptionParameters.INDICATOR_PROC_STATUS_PUBLICATION_FAILED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
        }
    }

    @Test
    public void testPublishIndicatorErrorWrongProcStatusPublished() throws Exception {

        String uuid = INDICATOR_3;

        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, INDICATOR_3_VERSION);
            assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorDto.getProcStatus());
            assertEquals(null, indicatorDto.getProductionVersion());
            assertEquals(INDICATOR_3_VERSION, indicatorDto.getPublishedVersion());
            assertEquals(null, indicatorDto.getArchivedVersion());
        }

        try {
            indicatorsServiceFacade.publishIndicator(getServiceContextAdministrador(), uuid);
            fail("Indicator is not diffusion validation");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.INDICATOR_PROC_STATUS_DIFFUSION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(ServiceExceptionParameters.INDICATOR_PROC_STATUS_PUBLICATION_FAILED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
        }
    }

    @Test
    public void testPublishIndicatorErrorNumeratorNotPublished() throws Exception {

        String uuid = INDICATOR_5;

        // Change indicator to fraction with numerator in draft
        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, null);
        indicatorDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorDto.getQuantity().setType(QuantityTypeEnum.FRACTION);
        indicatorDto.getQuantity().setNumeratorIndicatorUuid(INDICATOR_2);
        indicatorsServiceFacade.updateIndicator(getServiceContextAdministrador(), indicatorDto);

        // Publish
        try {
            indicatorsServiceFacade.publishIndicator(getServiceContextAdministrador(), uuid);
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
        String uuidPublished3 = INDICATOR_3;
        String uuidNotPublished4 = INDICATOR_4;
        String uuidNotPublished7 = INDICATOR_7;

        // Change datasource to fraction with numerator in draft
        List<DataSourceDto> datasources = indicatorsServiceFacade.retrieveDataSourcesByIndicator(getServiceContextAdministrador(), uuid, "1.000");
        {
            DataSourceDto dataSourceDto = datasources.get(0);
            dataSourceDto.setAbsoluteMethod("absoluteMethod");
            // Indicator not published
            dataSourceDto.setAnnualPercentageRate(new RateDerivationDto());
            dataSourceDto.getAnnualPercentageRate().setMethodType(RateDerivationMethodTypeEnum.CALCULATE);
            dataSourceDto.getAnnualPercentageRate().setMethod("aa");
            dataSourceDto.getAnnualPercentageRate().setRounding(RateDerivationRoundingEnum.DOWN);
            dataSourceDto.getAnnualPercentageRate().setQuantity(new QuantityDto());
            dataSourceDto.getAnnualPercentageRate().getQuantity().setType(QuantityTypeEnum.CHANGE_RATE);
            dataSourceDto.getAnnualPercentageRate().getQuantity().setUnitUuid(QUANTITY_UNIT_1);
            dataSourceDto.getAnnualPercentageRate().getQuantity().setDecimalPlaces(Integer.valueOf(2));
            dataSourceDto.getAnnualPercentageRate().getQuantity().setNumeratorIndicatorUuid(uuidNotPublished2);
            dataSourceDto.getAnnualPercentageRate().getQuantity().setDenominatorIndicatorUuid(uuidNotPublished7);
            dataSourceDto.getAnnualPercentageRate().getQuantity().setIsPercentage(Boolean.TRUE);
            dataSourceDto.getAnnualPercentageRate().getQuantity().setBaseQuantityIndicatorUuid(uuid);
            // Indicator not published
            dataSourceDto.setInterperiodPercentageRate(new RateDerivationDto());
            dataSourceDto.getInterperiodPercentageRate().setMethodType(RateDerivationMethodTypeEnum.CALCULATE);
            dataSourceDto.getInterperiodPercentageRate().setMethod("aa");
            dataSourceDto.getInterperiodPercentageRate().setRounding(RateDerivationRoundingEnum.DOWN);
            dataSourceDto.getInterperiodPercentageRate().setQuantity(new QuantityDto());
            dataSourceDto.getInterperiodPercentageRate().getQuantity().setUnitUuid(QUANTITY_UNIT_1);
            dataSourceDto.getInterperiodPercentageRate().getQuantity().setDecimalPlaces(Integer.valueOf(2));
            dataSourceDto.getInterperiodPercentageRate().getQuantity().setType(QuantityTypeEnum.CHANGE_RATE);
            dataSourceDto.getInterperiodPercentageRate().getQuantity().setNumeratorIndicatorUuid(uuidNotPublished4);
            dataSourceDto.getInterperiodPercentageRate().getQuantity().setIsPercentage(Boolean.TRUE);
            dataSourceDto.getInterperiodPercentageRate().getQuantity().setBaseQuantityIndicatorUuid(uuid);
            indicatorsServiceFacade.updateDataSource(getServiceContextAdministrador(), dataSourceDto);
        }
        {
            DataSourceDto dataSourceDto = datasources.get(1);
            dataSourceDto.setAbsoluteMethod("absoluteMethod");
            // Indicator published
            dataSourceDto.setAnnualPercentageRate(new RateDerivationDto());
            dataSourceDto.getAnnualPercentageRate().setMethodType(RateDerivationMethodTypeEnum.CALCULATE);
            dataSourceDto.getAnnualPercentageRate().setMethod("aa");
            dataSourceDto.getAnnualPercentageRate().setRounding(RateDerivationRoundingEnum.DOWN);
            dataSourceDto.getAnnualPercentageRate().setQuantity(new QuantityDto());
            dataSourceDto.getAnnualPercentageRate().getQuantity().setUnitUuid(QUANTITY_UNIT_1);
            dataSourceDto.getAnnualPercentageRate().getQuantity().setDecimalPlaces(Integer.valueOf(2));
            dataSourceDto.getAnnualPercentageRate().getQuantity().setType(QuantityTypeEnum.CHANGE_RATE);
            dataSourceDto.getAnnualPercentageRate().getQuantity().setNumeratorIndicatorUuid(uuidPublished1);
            dataSourceDto.getAnnualPercentageRate().getQuantity().setIsPercentage(Boolean.TRUE);
            dataSourceDto.getAnnualPercentageRate().getQuantity().setBaseQuantityIndicatorUuid(uuid);
            // Indicator published
            dataSourceDto.setInterperiodPercentageRate(new RateDerivationDto());
            dataSourceDto.getInterperiodPercentageRate().setMethodType(RateDerivationMethodTypeEnum.CALCULATE);
            dataSourceDto.getInterperiodPercentageRate().setMethod("aa");
            dataSourceDto.getInterperiodPercentageRate().setRounding(RateDerivationRoundingEnum.DOWN);
            dataSourceDto.getInterperiodPercentageRate().setQuantity(new QuantityDto());
            dataSourceDto.getInterperiodPercentageRate().getQuantity().setType(QuantityTypeEnum.CHANGE_RATE);
            dataSourceDto.getInterperiodPercentageRate().getQuantity().setUnitUuid(QUANTITY_UNIT_1);
            dataSourceDto.getInterperiodPercentageRate().getQuantity().setDecimalPlaces(Integer.valueOf(2));
            dataSourceDto.getInterperiodPercentageRate().getQuantity().setNumeratorIndicatorUuid(uuidPublished3);
            dataSourceDto.getInterperiodPercentageRate().getQuantity().setIsPercentage(Boolean.TRUE);
            dataSourceDto.getInterperiodPercentageRate().getQuantity().setBaseQuantityIndicatorUuid(uuid);
            indicatorsServiceFacade.updateDataSource(getServiceContextAdministrador(), dataSourceDto);
        }

        // Publish
        try {
            indicatorsServiceFacade.publishIndicator(getServiceContextAdministrador(), uuid);
            fail("Indicators non published in datasource");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_MUST_HAVE_ALL_LINKED_INDICATORS_PUBLISHED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(3, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1]).length);
            boolean uuid2 = false;
            boolean uuid4 = false;
            boolean uuid7 = false;
            for (int i = 0; i < ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1]).length; i++) {
                String uuidNotPublished = ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[i];
                if (uuidNotPublished.equals(uuidNotPublished2)) {
                    uuid2 = true;
                } else if (uuidNotPublished.equals(uuidNotPublished4)) {
                    uuid4 = true;
                } else if (uuidNotPublished.equals(uuidNotPublished7)) {
                    uuid7 = true;
                }
            }
            assertTrue(uuid2);
            assertTrue(uuid4);
            assertTrue(uuid7);
        }
    }

    @Test
    public void testArchiveIndicator() throws Exception {

        String uuid = INDICATOR_3;
        String versionNumber = INDICATOR_3_VERSION;

        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, versionNumber);
            assertEquals(null, indicatorDto.getProductionVersion());
            assertEquals(INDICATOR_3_VERSION, indicatorDto.getPublishedVersion());
            assertEquals(null, indicatorDto.getArchivedVersion());
            assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorDto.getProcStatus());
        }

        // Archive
        IndicatorDto indicatorDtoUpdated = indicatorsServiceFacade.archiveIndicator(getServiceContextAdministrador(), uuid);

        // Validation
        {
            assertEquals(null, indicatorDtoUpdated.getProductionVersion());
            assertEquals(null, indicatorDtoUpdated.getPublishedVersion());
            assertEquals(INDICATOR_3_VERSION, indicatorDtoUpdated.getArchivedVersion());
            assertEquals(IndicatorProcStatusEnum.ARCHIVED, indicatorDtoUpdated.getProcStatus());

            IndicatorsAsserts.assertEqualsDate("2011-03-03 01:02:04", indicatorDtoUpdated.getProductionValidationDate());
            assertEquals("user1", indicatorDtoUpdated.getProductionValidationUser());
            IndicatorsAsserts.assertEqualsDate("2011-04-04 03:02:04", indicatorDtoUpdated.getDiffusionValidationDate());
            assertEquals("user2", indicatorDtoUpdated.getDiffusionValidationUser());
            IndicatorsAsserts.assertEqualsDate("2011-05-05 04:02:04", indicatorDtoUpdated.getPublicationDate());
            assertEquals("user3", indicatorDtoUpdated.getPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), indicatorDtoUpdated.getArchiveDate()));
            assertEquals(getServiceContextAdministrador().getUserId(), indicatorDtoUpdated.getArchiveUser());
        }
        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, versionNumber);
            assertEquals(null, indicatorDto.getProductionVersion());
            assertEquals(null, indicatorDto.getPublishedVersion());
            assertEquals(INDICATOR_3_VERSION, indicatorDto.getArchivedVersion());
            assertEquals(IndicatorProcStatusEnum.ARCHIVED, indicatorDto.getProcStatus());

            IndicatorsAsserts.assertEqualsDate("2011-03-03 01:02:04", indicatorDto.getProductionValidationDate());
            assertEquals("user1", indicatorDto.getProductionValidationUser());
            IndicatorsAsserts.assertEqualsDate("2011-04-04 03:02:04", indicatorDto.getDiffusionValidationDate());
            assertEquals("user2", indicatorDto.getDiffusionValidationUser());
            IndicatorsAsserts.assertEqualsDate("2011-05-05 04:02:04", indicatorDto.getPublicationDate());
            assertEquals("user3", indicatorDto.getPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), indicatorDto.getArchiveDate()));
            assertEquals(getServiceContextAdministrador().getUserId(), indicatorDto.getArchiveUser());
        }
    }

    @Test
    public void testArchiveIndicatorWithProductionVersion() throws Exception {

        String uuid = INDICATOR_1;
        String diffusionVersion = "1.000";
        String productionVersion = "2.000";

        {
            IndicatorDto indicatorDtoV1 = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, diffusionVersion);
            IndicatorDto indicatorDtoV2 = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, productionVersion);
            assertEquals(productionVersion, indicatorDtoV1.getProductionVersion());
            assertEquals(diffusionVersion, indicatorDtoV1.getPublishedVersion());
            assertEquals(null, indicatorDtoV1.getArchivedVersion());
            assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorDtoV1.getProcStatus());
            assertEquals(IndicatorProcStatusEnum.DRAFT, indicatorDtoV2.getProcStatus());
        }

        // Archive
        indicatorsServiceFacade.archiveIndicator(getServiceContextAdministrador(), uuid);

        // Validation
        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, diffusionVersion);
            assertEquals(productionVersion, indicatorDto.getProductionVersion());
            assertEquals(null, indicatorDto.getPublishedVersion());
            assertEquals(diffusionVersion, indicatorDto.getArchivedVersion());
            assertEquals(IndicatorProcStatusEnum.ARCHIVED, indicatorDto.getProcStatus());
        }
    }

    @Test
    public void testArchiveIndicatorInIndicatorsSystemArchived() throws Exception {

        String uuid = INDICATOR_12;
        String diffusionVersion = "1.000";

        {
            IndicatorDto indicatorDtoV1 = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, diffusionVersion);
            assertEquals(diffusionVersion, indicatorDtoV1.getPublishedVersion());
            assertEquals(null, indicatorDtoV1.getArchivedVersion());
            assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorDtoV1.getProcStatus());
        }

        indicatorsServiceFacade.archiveIndicator(getServiceContextAdministrador(), uuid);

        // Validation
        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, diffusionVersion);
            assertEquals(null, indicatorDto.getPublishedVersion());
            assertEquals(diffusionVersion, indicatorDto.getArchivedVersion());
            assertEquals(IndicatorProcStatusEnum.ARCHIVED, indicatorDto.getProcStatus());
        }

    }

    @Test
    public void testArchiveIndicatorErrorNotExists() throws Exception {

        try {
            indicatorsServiceFacade.archiveIndicator(getServiceContextAdministrador(), NOT_EXISTS);
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
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, "1.000");
            assertEquals(IndicatorProcStatusEnum.DRAFT, indicatorDto.getProcStatus());
            assertEquals("1.000", indicatorDto.getProductionVersion());
            assertEquals(null, indicatorDto.getPublishedVersion());
            assertEquals(null, indicatorDto.getArchivedVersion());
        }

        try {
            indicatorsServiceFacade.archiveIndicator(getServiceContextAdministrador(), uuid);
            fail("Indicator is not published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.INDICATOR_PROC_STATUS_PUBLISHED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Test
    public void testArchiveIndicatorErrorWrongProcStatusArchived() throws Exception {

        String uuid = INDICATOR_8;

        {
            IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, "1.000");
            assertEquals(IndicatorProcStatusEnum.ARCHIVED, indicatorDto.getProcStatus());
            assertEquals(null, indicatorDto.getProductionVersion());
            assertEquals(null, indicatorDto.getPublishedVersion());
            assertEquals("1.000", indicatorDto.getArchivedVersion());
        }

        try {
            indicatorsServiceFacade.archiveIndicator(getServiceContextAdministrador(), uuid);
            fail("Indicator is not published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.INDICATOR_PROC_STATUS_PUBLISHED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Test
    public void testArchiveIndicatorErrorInIndicatorsSystemPublished() throws Exception {

        String uuid = INDICATOR_6;
        try {
            indicatorsServiceFacade.archiveIndicator(getServiceContextAdministrador(), uuid);
            fail("Indicators system published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(INDICATORS_SYSTEM_2, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_DRAFT, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_VALIDATION_REJECTED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
            assertEquals(ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_PRODUCTION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[2]);
            assertEquals(ServiceExceptionParameters.INDICATORS_SYSTEM_PROC_STATUS_DIFFUSION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[3]);
        }
    }

    @Test
    public void testVersioningIndicator() throws Exception {

        String uuid = INDICATOR_3;
        String newVersionExpected = "12.000";

        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, null);
        assertEquals(INDICATOR_3_VERSION, indicatorDto.getVersionNumber());
        assertEquals(null, indicatorDto.getProductionVersion());
        assertEquals(INDICATOR_3_VERSION, indicatorDto.getPublishedVersion());
        assertEquals(null, indicatorDto.getArchivedVersion());

        IndicatorDto indicatorDtoVersioned = indicatorsServiceFacade.versioningIndicator(getServiceContextAdministrador(), uuid, VersionTypeEnum.MAJOR);

        // Validate
        assertEquals(newVersionExpected, indicatorDtoVersioned.getVersionNumber());
        assertEquals(newVersionExpected, indicatorDtoVersioned.getProductionVersion());
        assertEquals(INDICATOR_3_VERSION, indicatorDtoVersioned.getPublishedVersion());
        assertEquals(null, indicatorDtoVersioned.getArchivedVersion());
        IndicatorsAsserts.assertEqualsIndicator(indicatorDto, indicatorDtoVersioned);

        {
            IndicatorDto indicatorDtoProduction = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, newVersionExpected);
            IndicatorDto indicatorDtoDiffusion = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, INDICATOR_3_VERSION);

            IndicatorsAsserts.assertEqualsIndicator(indicatorDtoDiffusion, indicatorDtoProduction);

            assertEquals(IndicatorProcStatusEnum.DRAFT, indicatorDtoProduction.getProcStatus());
            assertEquals(newVersionExpected, indicatorDtoProduction.getVersionNumber());
            assertEquals(newVersionExpected, indicatorDtoProduction.getProductionVersion());
            assertEquals(INDICATOR_3_VERSION, indicatorDtoProduction.getPublishedVersion());
            assertEquals(null, indicatorDtoProduction.getArchivedVersion());
            // Data sources
            List<DataSourceDto> dataSources = indicatorsServiceFacade.retrieveDataSourcesByIndicator(getServiceContextAdministrador(), indicatorDtoProduction.getUuid(),
                    indicatorDtoProduction.getProductionVersion());
            assertEquals(1, dataSources.size());
            assertEquals("query-gpe Indicator-3-v1-DataSource-1", dataSources.get(0).getDataGpeUuid());
            assertEquals("px Indicator-3-v1-DataSource-1", dataSources.get(0).getPxUri());
            assertEquals("time v Indicator-3-v1-DataSource-1", dataSources.get(0).getTimeVariable());
            assertEquals("geographical v Indicator-3-v1-DataSource-1", dataSources.get(0).getGeographicalVariable());
            assertEquals(1, dataSources.get(0).getOtherVariables().size());
            assertEquals("variable Indicator-3-v1-DataSource-1-Var-1", dataSources.get(0).getOtherVariables().get(0).getVariable());
            assertEquals("category Indicator-3-v1-DataSource-1-Var-1", dataSources.get(0).getOtherVariables().get(0).getCategory());

            {
                RateDerivationDto rateDerivationDto = dataSources.get(0).getInterperiodPuntualRate();
                assertEquals("MethodOfInterperiodPuntual", rateDerivationDto.getMethod());
                assertEquals(RateDerivationMethodTypeEnum.CALCULATE, rateDerivationDto.getMethodType());
                assertEquals(RateDerivationRoundingEnum.DOWN, rateDerivationDto.getRounding());
                assertEquals(QuantityTypeEnum.CHANGE_RATE, rateDerivationDto.getQuantity().getType());
                assertEquals(QUANTITY_UNIT_1, rateDerivationDto.getQuantity().getUnitUuid());
                assertEquals(Integer.valueOf(10), rateDerivationDto.getQuantity().getUnitMultiplier());
                IndicatorsAsserts.assertEqualsInternationalString(rateDerivationDto.getQuantity().getUnitMultiplierLabel(), "es", "Decenas", null, null);
                assertEquals(Integer.valueOf(2), rateDerivationDto.getQuantity().getSignificantDigits());
                assertEquals(Integer.valueOf(3), rateDerivationDto.getQuantity().getDecimalPlaces());
                assertEquals(Integer.valueOf(100), rateDerivationDto.getQuantity().getMinimum());
                assertEquals(Integer.valueOf(200), rateDerivationDto.getQuantity().getMaximum());
                assertEquals(Boolean.TRUE, rateDerivationDto.getQuantity().getIsPercentage());
                IndicatorsAsserts.assertEqualsInternationalString(rateDerivationDto.getQuantity().getPercentageOf(), "es", "Porcentaje xx", null, null);
                assertEquals(Integer.valueOf(5), rateDerivationDto.getQuantity().getBaseValue());
                assertEquals("2010", rateDerivationDto.getQuantity().getBaseTime());
                assertEquals(GEOGRAPHICAL_VALUE_1, rateDerivationDto.getQuantity().getBaseLocationUuid());
                assertEquals(INDICATOR_3, rateDerivationDto.getQuantity().getBaseQuantityIndicatorUuid());
                assertEquals(QUANTITY_UNIT_1, rateDerivationDto.getQuantity().getUnitUuid());
                assertEquals(INDICATOR_3, rateDerivationDto.getQuantity().getNumeratorIndicatorUuid());
                assertEquals(INDICATOR_6, rateDerivationDto.getQuantity().getDenominatorIndicatorUuid());
            }
            {
                RateDerivationDto rateDerivationDto = dataSources.get(0).getAnnualPuntualRate();
                assertEquals("MethodOfAnnualPuntual", rateDerivationDto.getMethod());
                assertEquals(RateDerivationMethodTypeEnum.CALCULATE, rateDerivationDto.getMethodType());
                assertEquals(RateDerivationRoundingEnum.DOWN, rateDerivationDto.getRounding());
                assertEquals(QuantityTypeEnum.AMOUNT, rateDerivationDto.getQuantity().getType());
                assertEquals(QUANTITY_UNIT_1, rateDerivationDto.getQuantity().getUnitUuid());
                assertEquals(Integer.valueOf(10000), rateDerivationDto.getQuantity().getUnitMultiplier());
                IndicatorsAsserts.assertEqualsInternationalString(rateDerivationDto.getQuantity().getUnitMultiplierLabel(), "es", "Decenas de miles", null, null);
            }
            {
                RateDerivationDto rateDerivationDto = dataSources.get(0).getAnnualPercentageRate();
                assertEquals("MethodOfAnnualPercentage", rateDerivationDto.getMethod());
                assertEquals(RateDerivationMethodTypeEnum.CALCULATE, rateDerivationDto.getMethodType());
                assertEquals(RateDerivationRoundingEnum.UPWARD, rateDerivationDto.getRounding());
                assertEquals(QuantityTypeEnum.AMOUNT, rateDerivationDto.getQuantity().getType());
                assertEquals(QUANTITY_UNIT_1, rateDerivationDto.getQuantity().getUnitUuid());
                assertEquals(Integer.valueOf(1000000), rateDerivationDto.getQuantity().getUnitMultiplier());
            }

            assertNull(dataSources.get(0).getInterperiodPercentageRate());

            assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorDtoDiffusion.getProcStatus());
            assertEquals(INDICATOR_3_VERSION, indicatorDtoDiffusion.getVersionNumber());
            assertEquals(newVersionExpected, indicatorDtoDiffusion.getProductionVersion());
            assertEquals(INDICATOR_3_VERSION, indicatorDtoDiffusion.getPublishedVersion());
            assertEquals(null, indicatorDtoDiffusion.getArchivedVersion());
        }
    }

    @Test
    public void testVersioningIndicatorVersionMinor() throws Exception {

        String uuid = INDICATOR_3;
        String newVersionExpected = "11.034";

        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, null);
        assertEquals(INDICATOR_3_VERSION, indicatorDto.getVersionNumber());
        assertEquals(null, indicatorDto.getProductionVersion());
        assertEquals(INDICATOR_3_VERSION, indicatorDto.getPublishedVersion());
        assertEquals(null, indicatorDto.getArchivedVersion());

        IndicatorDto indicatorDtoVersioned = indicatorsServiceFacade.versioningIndicator(getServiceContextAdministrador(), uuid, VersionTypeEnum.MINOR);

        // Validate
        assertEquals(newVersionExpected, indicatorDtoVersioned.getVersionNumber());
        assertEquals(newVersionExpected, indicatorDtoVersioned.getProductionVersion());
        assertEquals(INDICATOR_3_VERSION, indicatorDtoVersioned.getPublishedVersion());
        assertEquals(null, indicatorDtoVersioned.getArchivedVersion());
        IndicatorsAsserts.assertEqualsIndicator(indicatorDto, indicatorDtoVersioned);

        {
            IndicatorDto indicatorDtoProduction = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, newVersionExpected);
            IndicatorDto indicatorDtoDiffusion = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, INDICATOR_3_VERSION);

            IndicatorsAsserts.assertEqualsIndicator(indicatorDtoDiffusion, indicatorDtoProduction);

            assertEquals(IndicatorProcStatusEnum.DRAFT, indicatorDtoProduction.getProcStatus());
            assertEquals(newVersionExpected, indicatorDtoProduction.getVersionNumber());
            assertEquals(newVersionExpected, indicatorDtoProduction.getProductionVersion());
            assertEquals(INDICATOR_3_VERSION, indicatorDtoProduction.getPublishedVersion());
            assertEquals(null, indicatorDtoProduction.getArchivedVersion());

            assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorDtoDiffusion.getProcStatus());
            assertEquals(INDICATOR_3_VERSION, indicatorDtoDiffusion.getVersionNumber());
            assertEquals(newVersionExpected, indicatorDtoDiffusion.getProductionVersion());
            assertEquals(INDICATOR_3_VERSION, indicatorDtoDiffusion.getPublishedVersion());
            assertEquals(null, indicatorDtoDiffusion.getArchivedVersion());
        }
    }

    @Test
    public void testVersioningIndicatorErrorNotExists() throws Exception {

        try {
            indicatorsServiceFacade.versioningIndicator(getServiceContextAdministrador(), NOT_EXISTS, VersionTypeEnum.MINOR);
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
            indicatorsServiceFacade.versioningIndicator(getServiceContextAdministrador(), uuid, VersionTypeEnum.MINOR);
            fail("Indicator already exists in production");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.INDICATOR_PROC_STATUS_PUBLISHED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(ServiceExceptionParameters.INDICATOR_PROC_STATUS_ARCHIVED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
        }
    }

    @Test
    public void testFindIndicators() throws Exception {

        // Retrieve last versions...
        MetamacCriteriaResult<IndicatorSummaryDto> result = indicatorsServiceFacade.findIndicators(getServiceContextTecnicoProduccion(), null);
        assertEquals(13, result.getResults().size());
        List<IndicatorSummaryDto> indicatorsDto = result.getResults();

        {
            IndicatorSummaryDto indicatorSummaryDto = indicatorsDto.get(0);
            assertEquals(INDICATOR_1, indicatorSummaryDto.getUuid());
            assertEquals("CODE-1", indicatorSummaryDto.getCode());

            assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorSummaryDto.getDiffusionVersion().getProcStatus());
            assertEquals("1.000", indicatorSummaryDto.getDiffusionVersion().getVersionNumber());
            assertEquals(Boolean.TRUE, indicatorSummaryDto.getDiffusionVersion().getNeedsUpdate());
            IndicatorsAsserts.assertEqualsInternationalString(indicatorSummaryDto.getDiffusionVersion().getTitle(), "es", "Título Indicator-1-v1 Educación", "en", "Title Indicator-1-v1");

            assertEquals(IndicatorProcStatusEnum.DRAFT, indicatorSummaryDto.getProductionVersion().getProcStatus());
            assertEquals("2.000", indicatorSummaryDto.getProductionVersion().getVersionNumber());
            assertEquals(Boolean.FALSE, indicatorSummaryDto.getProductionVersion().getNeedsUpdate());
            IndicatorsAsserts.assertEqualsInternationalString(indicatorSummaryDto.getProductionVersion().getTitle(), "es", "Título Indicator-1-v2 Educación", "en", "Title Indicator-1-v2");
        }
        {
            IndicatorSummaryDto indicatorSummaryDto = indicatorsDto.get(1);
            assertEquals(INDICATOR_2, indicatorSummaryDto.getUuid());
            assertEquals("CODE-2", indicatorSummaryDto.getCode());

            assertNull(indicatorSummaryDto.getDiffusionVersion());

            assertEquals(IndicatorProcStatusEnum.DRAFT, indicatorSummaryDto.getProductionVersion().getProcStatus());
            assertEquals("1.000", indicatorSummaryDto.getProductionVersion().getVersionNumber());
            assertEquals(Boolean.FALSE, indicatorSummaryDto.getProductionVersion().getNeedsUpdate());
        }
        {
            IndicatorSummaryDto indicatorSummaryDto = indicatorsDto.get(2);
            assertEquals(INDICATOR_3, indicatorSummaryDto.getUuid());
            assertEquals("CODE-3", indicatorSummaryDto.getCode());

            assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorSummaryDto.getDiffusionVersion().getProcStatus());
            assertEquals("11.033", indicatorSummaryDto.getDiffusionVersion().getVersionNumber());

            assertNull(indicatorSummaryDto.getProductionVersion());
        }
        {
            IndicatorSummaryDto indicatorSummaryDto = indicatorsDto.get(3);
            assertEquals(INDICATOR_4, indicatorSummaryDto.getUuid());
            assertEquals("CODE-4", indicatorSummaryDto.getCode());

            assertNull(indicatorSummaryDto.getDiffusionVersion());

            assertEquals(IndicatorProcStatusEnum.PRODUCTION_VALIDATION, indicatorSummaryDto.getProductionVersion().getProcStatus());
            assertEquals("1.000", indicatorSummaryDto.getProductionVersion().getVersionNumber());
        }
        {
            IndicatorSummaryDto indicatorSummaryDto = indicatorsDto.get(4);
            assertEquals(INDICATOR_5, indicatorSummaryDto.getUuid());
            assertEquals("CODE-5", indicatorSummaryDto.getCode());

            assertNull(indicatorSummaryDto.getDiffusionVersion());

            assertEquals(IndicatorProcStatusEnum.DIFFUSION_VALIDATION, indicatorSummaryDto.getProductionVersion().getProcStatus());
            assertEquals("1.000", indicatorSummaryDto.getProductionVersion().getVersionNumber());
        }
        {
            IndicatorSummaryDto indicatorSummaryDto = indicatorsDto.get(5);
            assertEquals(INDICATOR_6, indicatorSummaryDto.getUuid());
            assertEquals("CODE-6", indicatorSummaryDto.getCode());

            assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorSummaryDto.getDiffusionVersion().getProcStatus());
            assertEquals("1.000", indicatorSummaryDto.getDiffusionVersion().getVersionNumber());

            assertEquals(IndicatorProcStatusEnum.DIFFUSION_VALIDATION, indicatorSummaryDto.getProductionVersion().getProcStatus());
            assertEquals("2.000", indicatorSummaryDto.getProductionVersion().getVersionNumber());
        }
        {
            IndicatorSummaryDto indicatorSummaryDto = indicatorsDto.get(6);
            assertEquals(INDICATOR_7, indicatorSummaryDto.getUuid());
            assertEquals("CODE-7", indicatorSummaryDto.getCode());

            assertEquals(IndicatorProcStatusEnum.ARCHIVED, indicatorSummaryDto.getDiffusionVersion().getProcStatus());
            assertEquals("1.000", indicatorSummaryDto.getDiffusionVersion().getVersionNumber());

            assertEquals(IndicatorProcStatusEnum.DIFFUSION_VALIDATION, indicatorSummaryDto.getProductionVersion().getProcStatus());
            assertEquals("2.000", indicatorSummaryDto.getProductionVersion().getVersionNumber());
        }
        {
            IndicatorSummaryDto indicatorSummaryDto = indicatorsDto.get(7);
            assertEquals(INDICATOR_8, indicatorSummaryDto.getUuid());
            assertEquals("CODE-8", indicatorSummaryDto.getCode());

            assertEquals(IndicatorProcStatusEnum.ARCHIVED, indicatorSummaryDto.getDiffusionVersion().getProcStatus());
            assertEquals("1.000", indicatorSummaryDto.getDiffusionVersion().getVersionNumber());

            assertNull(indicatorSummaryDto.getProductionVersion());
        }
        {
            IndicatorSummaryDto indicatorSummaryDto = indicatorsDto.get(8);
            assertEquals(INDICATOR_9, indicatorSummaryDto.getUuid());
            assertEquals("CODE-9", indicatorSummaryDto.getCode());

            assertNull(indicatorSummaryDto.getDiffusionVersion());

            assertEquals(IndicatorProcStatusEnum.VALIDATION_REJECTED, indicatorSummaryDto.getProductionVersion().getProcStatus());
            assertEquals("1.000", indicatorSummaryDto.getProductionVersion().getVersionNumber());
        }
        {
            IndicatorSummaryDto indicatorSummaryDto = indicatorsDto.get(9);
            assertEquals(INDICATOR_10, indicatorSummaryDto.getUuid());
            assertEquals("CODE-10", indicatorSummaryDto.getCode());

            assertNull(indicatorSummaryDto.getDiffusionVersion());

            assertEquals(IndicatorProcStatusEnum.DRAFT, indicatorSummaryDto.getProductionVersion().getProcStatus());
            assertEquals("1.000", indicatorSummaryDto.getProductionVersion().getVersionNumber());
        }
        {
            IndicatorSummaryDto indicatorSummaryDto = indicatorsDto.get(10);
            assertEquals(INDICATOR_11, indicatorSummaryDto.getUuid());
            assertEquals("CODE-11", indicatorSummaryDto.getCode());

            assertNull(indicatorSummaryDto.getDiffusionVersion());

            assertEquals(IndicatorProcStatusEnum.PUBLICATION_FAILED, indicatorSummaryDto.getProductionVersion().getProcStatus());
            assertEquals("1.000", indicatorSummaryDto.getProductionVersion().getVersionNumber());
        }
        {
            IndicatorSummaryDto indicatorSummaryDto = indicatorsDto.get(11);
            assertEquals(INDICATOR_12, indicatorSummaryDto.getUuid());
            assertEquals("CODE-12", indicatorSummaryDto.getCode());

            assertNull(indicatorSummaryDto.getProductionVersion());
            assertNotNull(indicatorSummaryDto.getDiffusionVersion());

            assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorSummaryDto.getDiffusionVersion().getProcStatus());
            assertEquals("1.000", indicatorSummaryDto.getDiffusionVersion().getVersionNumber());
        }
        {
            IndicatorSummaryDto indicatorSummaryDto = indicatorsDto.get(12);
            assertEquals(INDICATOR_13, indicatorSummaryDto.getUuid());
            assertEquals("CODE-13", indicatorSummaryDto.getCode());

            assertNull(indicatorSummaryDto.getDiffusionVersion());

            assertEquals(IndicatorProcStatusEnum.DIFFUSION_VALIDATION, indicatorSummaryDto.getProductionVersion().getProcStatus());
            assertEquals("1.000", indicatorSummaryDto.getProductionVersion().getVersionNumber());
        }
    }

    @Test
    public void testFindIndicatorsByCriteria() throws Exception {

        {
            // Retrieve with subject x
            MetamacCriteria criteria = new MetamacCriteria();
            criteria.setRestriction(new MetamacCriteriaPropertyRestriction(IndicatorCriteriaPropertyEnum.SUBJECT_CODE.name(), SUBJECT_3, OperationType.EQ));

            MetamacCriteriaResult<IndicatorSummaryDto> result = indicatorsServiceFacade.findIndicators(getServiceContextAdministrador(), criteria);
            assertEquals(8, result.getResults().size());
            List<IndicatorSummaryDto> indicatorsDto = result.getResults();

            assertEquals(INDICATOR_3, indicatorsDto.get(0).getUuid());
            assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorsDto.get(0).getDiffusionVersion().getProcStatus());
            assertNull(indicatorsDto.get(0).getProductionVersion());

            assertEquals(INDICATOR_6, indicatorsDto.get(1).getUuid());
            assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorsDto.get(1).getDiffusionVersion().getProcStatus());
            assertEquals(IndicatorProcStatusEnum.DIFFUSION_VALIDATION, indicatorsDto.get(1).getProductionVersion().getProcStatus());

            assertEquals(INDICATOR_7, indicatorsDto.get(2).getUuid());
            assertEquals(IndicatorProcStatusEnum.ARCHIVED, indicatorsDto.get(2).getDiffusionVersion().getProcStatus());
            assertEquals(IndicatorProcStatusEnum.DIFFUSION_VALIDATION, indicatorsDto.get(2).getProductionVersion().getProcStatus());

            assertEquals(INDICATOR_8, indicatorsDto.get(3).getUuid());
            assertEquals(IndicatorProcStatusEnum.ARCHIVED, indicatorsDto.get(3).getDiffusionVersion().getProcStatus());
            assertNull(indicatorsDto.get(3).getProductionVersion());

            assertEquals(INDICATOR_9, indicatorsDto.get(4).getUuid());
            assertNull(indicatorsDto.get(4).getDiffusionVersion());
            assertEquals(IndicatorProcStatusEnum.VALIDATION_REJECTED, indicatorsDto.get(4).getProductionVersion().getProcStatus());

            assertEquals(INDICATOR_10, indicatorsDto.get(5).getUuid());
            assertNull(indicatorsDto.get(5).getDiffusionVersion());
            assertEquals(IndicatorProcStatusEnum.DRAFT, indicatorsDto.get(5).getProductionVersion().getProcStatus());

            assertEquals(INDICATOR_11, indicatorsDto.get(6).getUuid());
            assertNull(indicatorsDto.get(6).getDiffusionVersion());
            assertEquals(IndicatorProcStatusEnum.PUBLICATION_FAILED, indicatorsDto.get(6).getProductionVersion().getProcStatus());

            assertEquals(INDICATOR_12, indicatorsDto.get(7).getUuid());
            assertNull(indicatorsDto.get(7).getProductionVersion());
            assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorsDto.get(7).getDiffusionVersion().getProcStatus());
        }
        {
            // Retrieve with subject x and code = y or z
            MetamacCriteria criteria = new MetamacCriteria();
            MetamacCriteriaConjunctionRestriction conjuction = new MetamacCriteriaConjunctionRestriction();
            conjuction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(IndicatorCriteriaPropertyEnum.SUBJECT_CODE.name(), SUBJECT_3, OperationType.EQ));

            MetamacCriteriaDisjunctionRestriction disjunction = new MetamacCriteriaDisjunctionRestriction();
            disjunction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(IndicatorCriteriaPropertyEnum.CODE.name(), "CODE-3", OperationType.EQ));
            disjunction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(IndicatorCriteriaPropertyEnum.CODE.name(), "CODE-6", OperationType.EQ));
            disjunction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(IndicatorCriteriaPropertyEnum.CODE.name(), "CODE-9", OperationType.EQ));
            conjuction.getRestrictions().add(disjunction);

            criteria.setRestriction(conjuction);

            MetamacCriteriaResult<IndicatorSummaryDto> result = indicatorsServiceFacade.findIndicators(getServiceContextAdministrador(), criteria);
            assertEquals(3, result.getResults().size());
            List<IndicatorSummaryDto> indicatorsDto = result.getResults();

            assertEquals(INDICATOR_3, indicatorsDto.get(0).getUuid());
            assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorsDto.get(0).getDiffusionVersion().getProcStatus());
            assertNull(indicatorsDto.get(0).getProductionVersion());

            assertEquals(INDICATOR_6, indicatorsDto.get(1).getUuid());
            assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorsDto.get(1).getDiffusionVersion().getProcStatus());
            assertEquals(IndicatorProcStatusEnum.DIFFUSION_VALIDATION, indicatorsDto.get(1).getProductionVersion().getProcStatus());

            assertEquals(INDICATOR_9, indicatorsDto.get(2).getUuid());
            assertNull(indicatorsDto.get(2).getDiffusionVersion());
            assertEquals(IndicatorProcStatusEnum.VALIDATION_REJECTED, indicatorsDto.get(2).getProductionVersion().getProcStatus());
        }
        {
            // Retrieve by title
            MetamacCriteria criteria = new MetamacCriteria();

            MetamacCriteriaConjunctionRestriction conjuction = new MetamacCriteriaConjunctionRestriction();
            conjuction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(IndicatorCriteriaPropertyEnum.TITLE.name(), "Educación", OperationType.LIKE));
            criteria.setRestriction(conjuction);

            MetamacCriteriaResult<IndicatorSummaryDto> result = indicatorsServiceFacade.findIndicators(getServiceContextAdministrador(), criteria);
            assertEquals(3, result.getResults().size());
            List<IndicatorSummaryDto> indicatorsDto = result.getResults();

            assertEquals(INDICATOR_1, indicatorsDto.get(0).getUuid());
            assertEquals(IndicatorProcStatusEnum.DRAFT, indicatorsDto.get(0).getProductionVersion().getProcStatus());
            assertEquals(INDICATOR_3, indicatorsDto.get(1).getUuid());
            assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorsDto.get(1).getDiffusionVersion().getProcStatus());
            assertEquals(INDICATOR_4, indicatorsDto.get(2).getUuid());
            assertEquals(IndicatorProcStatusEnum.PRODUCTION_VALIDATION, indicatorsDto.get(2).getProductionVersion().getProcStatus());
            // INDICATOR_6 and INDICATOR_7: version with title "Educación" is not last version
        }
        {
            // Retrieve by title ILIKE
            MetamacCriteria criteria = new MetamacCriteria();

            MetamacCriteriaConjunctionRestriction conjuction = new MetamacCriteriaConjunctionRestriction();
            conjuction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(IndicatorCriteriaPropertyEnum.TITLE.name(), "EduCAción", OperationType.ILIKE));
            criteria.setRestriction(conjuction);

            MetamacCriteriaResult<IndicatorSummaryDto> result = indicatorsServiceFacade.findIndicators(getServiceContextAdministrador(), criteria);
            assertEquals(3, result.getResults().size());
            List<IndicatorSummaryDto> indicatorsDto = result.getResults();

            assertEquals(INDICATOR_1, indicatorsDto.get(0).getUuid());
            assertEquals(IndicatorProcStatusEnum.DRAFT, indicatorsDto.get(0).getProductionVersion().getProcStatus());
            assertEquals(INDICATOR_3, indicatorsDto.get(1).getUuid());
            assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorsDto.get(1).getDiffusionVersion().getProcStatus());
            assertEquals(INDICATOR_4, indicatorsDto.get(2).getUuid());
            assertEquals(IndicatorProcStatusEnum.PRODUCTION_VALIDATION, indicatorsDto.get(2).getProductionVersion().getProcStatus());
        }
        {
            // Retrieve by title
            MetamacCriteria criteria = new MetamacCriteria();

            MetamacCriteriaConjunctionRestriction conjuction = new MetamacCriteriaConjunctionRestriction();
            conjuction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(IndicatorCriteriaPropertyEnum.TITLE.name(), "Educación en España", OperationType.ILIKE));
            criteria.setRestriction(conjuction);

            MetamacCriteriaResult<IndicatorSummaryDto> result = indicatorsServiceFacade.findIndicators(getServiceContextAdministrador(), criteria);
            assertEquals(1, result.getResults().size());
            List<IndicatorSummaryDto> indicatorsDto = result.getResults();

            assertEquals(INDICATOR_4, indicatorsDto.get(0).getUuid());
            assertEquals(IndicatorProcStatusEnum.PRODUCTION_VALIDATION, indicatorsDto.get(0).getProductionVersion().getProcStatus());
        }
        {
            // Retrieve by title ENGLISH
            // IMPORTANT: Find in english do not retrieve results because this method find internationalStrings in the main language of the app.
            // In this test, the configurationService define ES as the main language.

            MetamacCriteria criteria = new MetamacCriteria();
            MetamacCriteriaConjunctionRestriction conjuction = new MetamacCriteriaConjunctionRestriction();
            conjuction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(IndicatorCriteriaPropertyEnum.TITLE.name(), "Education", OperationType.LIKE));
            criteria.setRestriction(conjuction);

            MetamacCriteriaResult<IndicatorSummaryDto> result = indicatorsServiceFacade.findIndicators(getServiceContextAdministrador(), criteria);
            assertEquals(0, result.getResults().size());
        }
        {
            // Retrieve by title and code
            MetamacCriteria criteria = new MetamacCriteria();

            MetamacCriteriaConjunctionRestriction conjuction = new MetamacCriteriaConjunctionRestriction();
            conjuction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(IndicatorCriteriaPropertyEnum.TITLE.name(), "Educación", OperationType.LIKE));
            conjuction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(IndicatorCriteriaPropertyEnum.SUBJECT_CODE.name(), SUBJECT_4, OperationType.EQ));
            criteria.setRestriction(conjuction);

            MetamacCriteriaResult<IndicatorSummaryDto> result = indicatorsServiceFacade.findIndicators(getServiceContextAdministrador(), criteria);
            assertEquals(1, result.getResults().size());
            List<IndicatorSummaryDto> indicatorsDto = result.getResults();

            assertEquals(INDICATOR_4, indicatorsDto.get(0).getUuid());
        }
        {
            // Retrieve by GEOVALUE
            MetamacCriteria criteria = new MetamacCriteria();

            MetamacCriteriaConjunctionRestriction conjuction = new MetamacCriteriaConjunctionRestriction();
            conjuction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(IndicatorCriteriaPropertyEnum.TITLE.name(), "Educación", OperationType.LIKE));
            criteria.setRestriction(conjuction);

            MetamacCriteriaResult<IndicatorSummaryDto> result = indicatorsServiceFacade.findIndicators(getServiceContextAdministrador(), criteria);
            assertEquals(3, result.getResults().size());
            List<IndicatorSummaryDto> indicatorsDto = result.getResults();

            assertEquals(INDICATOR_1, indicatorsDto.get(0).getUuid());
            assertEquals(IndicatorProcStatusEnum.DRAFT, indicatorsDto.get(0).getProductionVersion().getProcStatus());
            assertEquals(INDICATOR_3, indicatorsDto.get(1).getUuid());
            assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorsDto.get(1).getDiffusionVersion().getProcStatus());
            assertEquals(INDICATOR_4, indicatorsDto.get(2).getUuid());
            assertEquals(IndicatorProcStatusEnum.PRODUCTION_VALIDATION, indicatorsDto.get(2).getProductionVersion().getProcStatus());
            // INDICATOR_6 and INDICATOR_7: version with title "Educación" is not last version
        }
    }

    @Test
    public void testFindIndicatorsByCriteriaPaginated() throws Exception {

        {
            // Retrieve with subject x
            MetamacCriteria criteria = new MetamacCriteria();
            MetamacCriteriaConjunctionRestriction conjuction = new MetamacCriteriaConjunctionRestriction();
            conjuction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(IndicatorCriteriaPropertyEnum.SUBJECT_CODE.name(), SUBJECT_3, OperationType.EQ));
            criteria.setRestriction(conjuction);

            MetamacCriteriaPaginator paginator = new MetamacCriteriaPaginator();
            paginator.setCountTotalResults(Boolean.TRUE);
            paginator.setMaximumResultSize(Integer.valueOf(4));
            criteria.setPaginator(paginator);
            {
                paginator.setFirstResult(Integer.valueOf(0));

                MetamacCriteriaResult<IndicatorSummaryDto> result = indicatorsServiceFacade.findIndicators(getServiceContextAdministrador(), criteria);
                assertEquals(4, result.getResults().size());
                assertEquals(Integer.valueOf(8), result.getPaginatorResult().getTotalResults());
                assertEquals(Integer.valueOf(4), result.getPaginatorResult().getMaximumResultSize());
                List<IndicatorSummaryDto> indicatorsDto = result.getResults();

                assertEquals(INDICATOR_3, indicatorsDto.get(0).getUuid());
                assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorsDto.get(0).getDiffusionVersion().getProcStatus());

                assertEquals(INDICATOR_6, indicatorsDto.get(1).getUuid());
                assertEquals(IndicatorProcStatusEnum.DIFFUSION_VALIDATION, indicatorsDto.get(1).getProductionVersion().getProcStatus());

                assertEquals(INDICATOR_7, indicatorsDto.get(2).getUuid());
                assertEquals(IndicatorProcStatusEnum.DIFFUSION_VALIDATION, indicatorsDto.get(2).getProductionVersion().getProcStatus());

                assertEquals(INDICATOR_8, indicatorsDto.get(3).getUuid());
                assertEquals(IndicatorProcStatusEnum.ARCHIVED, indicatorsDto.get(3).getDiffusionVersion().getProcStatus());
            }
            {
                paginator.setFirstResult(Integer.valueOf(4));

                MetamacCriteriaResult<IndicatorSummaryDto> result = indicatorsServiceFacade.findIndicators(getServiceContextAdministrador(), criteria);
                assertEquals(4, result.getResults().size());
                assertEquals(Integer.valueOf(8), result.getPaginatorResult().getTotalResults());
                assertEquals(Integer.valueOf(4), result.getPaginatorResult().getMaximumResultSize());
                List<IndicatorSummaryDto> indicatorsDto = result.getResults();

                assertEquals(INDICATOR_9, indicatorsDto.get(0).getUuid());
                assertEquals(IndicatorProcStatusEnum.VALIDATION_REJECTED, indicatorsDto.get(0).getProductionVersion().getProcStatus());

                assertEquals(INDICATOR_10, indicatorsDto.get(1).getUuid());
                assertEquals(IndicatorProcStatusEnum.DRAFT, indicatorsDto.get(1).getProductionVersion().getProcStatus());

                assertEquals(INDICATOR_11, indicatorsDto.get(2).getUuid());
                assertEquals(IndicatorProcStatusEnum.PUBLICATION_FAILED, indicatorsDto.get(2).getProductionVersion().getProcStatus());

                assertEquals(INDICATOR_12, indicatorsDto.get(3).getUuid());
                assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorsDto.get(3).getDiffusionVersion().getProcStatus());
            }
        }
        {
            // Retrieve by title
            MetamacCriteria criteria = new MetamacCriteria();
            MetamacCriteriaConjunctionRestriction conjuction = new MetamacCriteriaConjunctionRestriction();
            conjuction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(IndicatorCriteriaPropertyEnum.TITLE.name(), "Educación", OperationType.LIKE));
            criteria.setRestriction(conjuction);

            MetamacCriteriaPaginator paginator = new MetamacCriteriaPaginator();
            paginator.setCountTotalResults(Boolean.TRUE);
            paginator.setMaximumResultSize(Integer.valueOf(2));
            criteria.setPaginator(paginator);
            {
                paginator.setFirstResult(Integer.valueOf(0));

                MetamacCriteriaResult<IndicatorSummaryDto> result = indicatorsServiceFacade.findIndicators(getServiceContextAdministrador(), criteria);
                assertEquals(2, result.getResults().size());
                assertEquals(Integer.valueOf(3), result.getPaginatorResult().getTotalResults());
                assertEquals(Integer.valueOf(2), result.getPaginatorResult().getMaximumResultSize());
                List<IndicatorSummaryDto> indicatorsDto = result.getResults();

                assertEquals(INDICATOR_1, indicatorsDto.get(0).getUuid());
                assertEquals(IndicatorProcStatusEnum.DRAFT, indicatorsDto.get(0).getProductionVersion().getProcStatus());
                assertEquals(INDICATOR_3, indicatorsDto.get(1).getUuid());
                assertEquals(IndicatorProcStatusEnum.PUBLISHED, indicatorsDto.get(1).getDiffusionVersion().getProcStatus());
            }
            {
                paginator.setFirstResult(Integer.valueOf(2));

                MetamacCriteriaResult<IndicatorSummaryDto> result = indicatorsServiceFacade.findIndicators(getServiceContextAdministrador(), criteria);
                assertEquals(1, result.getResults().size());
                assertEquals(Integer.valueOf(3), result.getPaginatorResult().getTotalResults());
                assertEquals(Integer.valueOf(2), result.getPaginatorResult().getMaximumResultSize());
                List<IndicatorSummaryDto> indicatorsDto = result.getResults();

                assertEquals(INDICATOR_4, indicatorsDto.get(0).getUuid());
                assertEquals(IndicatorProcStatusEnum.PRODUCTION_VALIDATION, indicatorsDto.get(0).getProductionVersion().getProcStatus());
            }
        }
    }

    @Test
    public void testFindIndicatorsErrorCriteriaIncorrectUsingPropertyUnsuported() throws Exception {

        MetamacCriteria criteria = new MetamacCriteria();
        MetamacCriteriaConjunctionRestriction conjunction = new MetamacCriteriaConjunctionRestriction();
        conjunction.getRestrictions().add(new MetamacCriteriaPropertyRestriction("unsuported", Boolean.TRUE, OperationType.EQ));
        criteria.setRestriction(conjunction);

        try {
            indicatorsServiceFacade.findIndicators(getServiceContextAdministrador(), criteria);
            fail("criteria incorrecto");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals("unsuported", e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testDisableNotifyPopulationErrors() throws Exception {
        String uuid = INDICATOR_1;
        String versionNumber = "2.000";

        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, versionNumber);
        assertTrue(indicatorDto.getNotifyPopulationErrors());

        indicatorDto.setNotifyPopulationErrors(Boolean.FALSE);

        // Mark
        indicatorsServiceFacade.disableNotifyPopulationErrors(getServiceContextAdministrador(), uuid);

        // Validation
        IndicatorDto indicatorDtoUpdated = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, versionNumber);
        assertEquals(Boolean.FALSE, indicatorDtoUpdated.getNotifyPopulationErrors());
    }

    @Test
    public void testEnableNotifyPopulationErrors() throws Exception {
        String uuid = INDICATOR_2;
        String versionNumber = "1.000";

        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, versionNumber);
        assertEquals(Boolean.FALSE, indicatorDto.getNotifyPopulationErrors());

        indicatorDto.setNotifyPopulationErrors(Boolean.TRUE);

        // Mark
        indicatorsServiceFacade.enableNotifyPopulationErrors(getServiceContextAdministrador(), uuid);

        // Validation
        IndicatorDto indicatorDtoUpdated = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), uuid, versionNumber);
        assertTrue(indicatorDtoUpdated.getNotifyPopulationErrors());

    }

    @Test
    public void testRetrieveDataSource() throws Exception {

        DataSourceDto dataSourceDto = indicatorsServiceFacade.retrieveDataSource(getServiceContextAdministrador(), DATA_SOURCE_1_INDICATOR_1_V2);

        assertNotNull(dataSourceDto);
        assertEquals(DATA_SOURCE_1_INDICATOR_1_V2, dataSourceDto.getUuid());
        assertEquals("query-gpe Indicator-1-v2-DataSource-1", dataSourceDto.getDataGpeUuid());
        assertEquals("px Indicator-1-v2-DataSource-1", dataSourceDto.getPxUri());
        assertEquals("time v Indicator-1-v2-DataSource-1", dataSourceDto.getTimeVariable());
        assertEquals("geographical v Indicator-1-v2-DataSource-1", dataSourceDto.getGeographicalVariable());
        assertEquals("absolute-method-1-v2-1", dataSourceDto.getAbsoluteMethod());
        assertEquals("code-Indicator-1-v2-DataSource-1", dataSourceDto.getSourceSurveyCode());
        IndicatorsAsserts.assertEqualsInternationalString(dataSourceDto.getSourceSurveyTitle(), "es", "source survey title Indicator-1-v2-DataSource-1", null, null);
        IndicatorsAsserts.assertEqualsInternationalString(dataSourceDto.getSourceSurveyAcronym(), "es", "source survey acronym Indicator-1-v2-DataSource-1", null, null);
        assertEquals("http Indicator-1-v2-DataSource-1", dataSourceDto.getSourceSurveyUrl());
        assertEquals(2, dataSourceDto.getPublishers().size());
        assertEquals("ISTAC", dataSourceDto.getPublishers().get(0));
        assertEquals("INE", dataSourceDto.getPublishers().get(1));

        {
            RateDerivationDto rateDerivationDto = dataSourceDto.getInterperiodPuntualRate();
            assertEquals(RateDerivationMethodTypeEnum.CALCULATE, rateDerivationDto.getMethodType());
            assertEquals("MethodOfInterperiod", rateDerivationDto.getMethod());
            assertEquals(RateDerivationRoundingEnum.UPWARD, rateDerivationDto.getRounding());
            assertEquals(QuantityTypeEnum.AMOUNT, rateDerivationDto.getQuantity().getType());
            assertEquals(QUANTITY_UNIT_1, rateDerivationDto.getQuantity().getUnitUuid());
            assertEquals(Integer.valueOf(10), rateDerivationDto.getQuantity().getUnitMultiplier());
            assertEquals(Integer.valueOf(2), rateDerivationDto.getQuantity().getSignificantDigits());
            assertEquals(Integer.valueOf(3), rateDerivationDto.getQuantity().getDecimalPlaces());
        }
        {
            RateDerivationDto rateDerivationDto = dataSourceDto.getInterperiodPercentageRate();
            assertEquals("MethodOfInterperiod1212", rateDerivationDto.getMethod());
            assertEquals(RateDerivationMethodTypeEnum.LOAD, rateDerivationDto.getMethodType());
            assertEquals(RateDerivationRoundingEnum.DOWN, rateDerivationDto.getRounding());
            assertEquals(QuantityTypeEnum.CHANGE_RATE, rateDerivationDto.getQuantity().getType());
            assertEquals(QUANTITY_UNIT_2, rateDerivationDto.getQuantity().getUnitUuid());
            assertEquals(Integer.valueOf(10000), rateDerivationDto.getQuantity().getUnitMultiplier());
            assertEquals(Integer.valueOf(3), rateDerivationDto.getQuantity().getSignificantDigits());
            assertEquals(Integer.valueOf(4), rateDerivationDto.getQuantity().getDecimalPlaces());
            assertEquals(Integer.valueOf(111), rateDerivationDto.getQuantity().getMinimum());
            assertEquals(Integer.valueOf(222), rateDerivationDto.getQuantity().getMaximum());
            assertEquals(Boolean.TRUE, rateDerivationDto.getQuantity().getIsPercentage());
            IndicatorsAsserts.assertEqualsInternationalString(rateDerivationDto.getQuantity().getPercentageOf(), "es", "Porcentaje xx", null, null);
            assertNull(rateDerivationDto.getQuantity().getBaseValue());
            assertNull(rateDerivationDto.getQuantity().getBaseTime());
            assertNull(rateDerivationDto.getQuantity().getBaseLocationUuid());
            assertEquals(INDICATOR_1, rateDerivationDto.getQuantity().getBaseQuantityIndicatorUuid());
            assertEquals(INDICATOR_3, rateDerivationDto.getQuantity().getNumeratorIndicatorUuid());
            assertEquals(INDICATOR_6, rateDerivationDto.getQuantity().getDenominatorIndicatorUuid());
        }
        {
            RateDerivationDto rateDerivationDto = dataSourceDto.getAnnualPuntualRate();
            assertEquals(RateDerivationMethodTypeEnum.CALCULATE, rateDerivationDto.getMethodType());
            assertEquals("MethodOfInterperiod123", rateDerivationDto.getMethod());
            assertEquals(RateDerivationRoundingEnum.UPWARD, rateDerivationDto.getRounding());
            assertEquals(QuantityTypeEnum.AMOUNT, rateDerivationDto.getQuantity().getType());
            assertEquals(QUANTITY_UNIT_1, rateDerivationDto.getQuantity().getUnitUuid());
            assertEquals(Integer.valueOf(100), rateDerivationDto.getQuantity().getUnitMultiplier());
            assertEquals(Integer.valueOf(5), rateDerivationDto.getQuantity().getSignificantDigits());
            assertEquals(Integer.valueOf(4), rateDerivationDto.getQuantity().getDecimalPlaces());
        }
        {
            RateDerivationDto rateDerivationDto = dataSourceDto.getAnnualPercentageRate();
            assertEquals(RateDerivationMethodTypeEnum.CALCULATE, rateDerivationDto.getMethodType());
            assertEquals("MethodOfInterperiod1214", rateDerivationDto.getMethod());
            assertEquals(RateDerivationRoundingEnum.UPWARD, rateDerivationDto.getRounding());
            assertEquals(QuantityTypeEnum.CHANGE_RATE, rateDerivationDto.getQuantity().getType());
            assertEquals(QUANTITY_UNIT_2, rateDerivationDto.getQuantity().getUnitUuid());
            assertEquals(Integer.valueOf(1000), rateDerivationDto.getQuantity().getUnitMultiplier());
            assertEquals(Integer.valueOf(3), rateDerivationDto.getQuantity().getSignificantDigits());
            assertEquals(Integer.valueOf(4), rateDerivationDto.getQuantity().getDecimalPlaces());
            assertEquals(Integer.valueOf(333), rateDerivationDto.getQuantity().getMinimum());
            assertEquals(Integer.valueOf(444), rateDerivationDto.getQuantity().getMaximum());
            assertEquals(Boolean.TRUE, rateDerivationDto.getQuantity().getIsPercentage());
            assertNull(rateDerivationDto.getQuantity().getPercentageOf());
            assertNull(rateDerivationDto.getQuantity().getBaseValue());
            assertNull(rateDerivationDto.getQuantity().getBaseTime());
            assertNull(rateDerivationDto.getQuantity().getBaseLocationUuid());
            assertEquals(INDICATOR_1, rateDerivationDto.getQuantity().getBaseQuantityIndicatorUuid());
            assertEquals(INDICATOR_3, rateDerivationDto.getQuantity().getNumeratorIndicatorUuid());
            assertEquals(INDICATOR_6, rateDerivationDto.getQuantity().getDenominatorIndicatorUuid());
        }

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
            indicatorsServiceFacade.retrieveDataSource(getServiceContextAdministrador(), uuid);
            fail("parameter required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.UUID, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRetrieveDataSourceErrorNotExists() throws Exception {

        String uuid = NOT_EXISTS;

        try {
            indicatorsServiceFacade.retrieveDataSource(getServiceContextAdministrador(), uuid);
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
        dataSourceDto.setSourceSurveyCode("sourceSurveyCode");
        dataSourceDto.setSourceSurveyTitle(IndicatorsMocks.mockInternationalStringDto());
        dataSourceDto.setSourceSurveyAcronym(IndicatorsMocks.mockInternationalStringDto());
        dataSourceDto.setSourceSurveyUrl("sourceSurveyUrl");
        dataSourceDto.getPublishers().add("ISTAC");
        dataSourceDto.getPublishers().add("INE");
        dataSourceDto.getPublishers().add("IBESTAT");

        DataSourceVariableDto dataSourceVariableDto1 = new DataSourceVariableDto();
        dataSourceVariableDto1.setVariable("variable1");
        dataSourceVariableDto1.setCategory("category1");
        dataSourceDto.addOtherVariable(dataSourceVariableDto1);
        DataSourceVariableDto dataSourceVariableDto2 = new DataSourceVariableDto();
        dataSourceVariableDto2.setVariable("variable2");
        dataSourceVariableDto2.setCategory("category2");
        dataSourceDto.addOtherVariable(dataSourceVariableDto2);
        dataSourceDto.setAbsoluteMethod("absoluteMethod1");

        dataSourceDto.setInterperiodPuntualRate(new RateDerivationDto());
        dataSourceDto.getInterperiodPuntualRate().setMethodType(RateDerivationMethodTypeEnum.CALCULATE);
        dataSourceDto.getInterperiodPuntualRate().setMethod("Method1");
        dataSourceDto.getInterperiodPuntualRate().setRounding(RateDerivationRoundingEnum.DOWN);
        dataSourceDto.getInterperiodPuntualRate().setQuantity(new QuantityDto());
        dataSourceDto.getInterperiodPuntualRate().getQuantity().setType(QuantityTypeEnum.AMOUNT);
        dataSourceDto.getInterperiodPuntualRate().getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        dataSourceDto.getInterperiodPuntualRate().getQuantity().setDecimalPlaces(Integer.valueOf(2));

        dataSourceDto.setInterperiodPercentageRate(new RateDerivationDto());
        dataSourceDto.getInterperiodPercentageRate().setMethodType(RateDerivationMethodTypeEnum.LOAD);
        dataSourceDto.getInterperiodPercentageRate().setMethod("Method3");
        dataSourceDto.getInterperiodPercentageRate().setRounding(RateDerivationRoundingEnum.DOWN);
        dataSourceDto.getInterperiodPercentageRate().setQuantity(new QuantityDto());
        dataSourceDto.getInterperiodPercentageRate().getQuantity().setType(QuantityTypeEnum.CHANGE_RATE);
        dataSourceDto.getInterperiodPercentageRate().getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        dataSourceDto.getInterperiodPercentageRate().getQuantity().setDecimalPlaces(Integer.valueOf(2));
        dataSourceDto.getInterperiodPercentageRate().getQuantity().setNumeratorIndicatorUuid(INDICATOR_2);
        dataSourceDto.getInterperiodPercentageRate().getQuantity().setIsPercentage(Boolean.FALSE);
        dataSourceDto.getInterperiodPercentageRate().getQuantity().setBaseQuantityIndicatorUuid(INDICATOR_1);

        dataSourceDto.setAnnualPercentageRate(new RateDerivationDto());
        dataSourceDto.getAnnualPercentageRate().setMethodType(RateDerivationMethodTypeEnum.LOAD);
        dataSourceDto.getAnnualPercentageRate().setMethod("Method2");
        dataSourceDto.getAnnualPercentageRate().setRounding(RateDerivationRoundingEnum.UPWARD);
        dataSourceDto.getAnnualPercentageRate().setQuantity(new QuantityDto());
        dataSourceDto.getAnnualPercentageRate().getQuantity().setType(QuantityTypeEnum.CHANGE_RATE);
        dataSourceDto.getAnnualPercentageRate().getQuantity().setUnitUuid(QUANTITY_UNIT_2);
        dataSourceDto.getAnnualPercentageRate().getQuantity().setDecimalPlaces(Integer.valueOf(2));
        dataSourceDto.getAnnualPercentageRate().getQuantity().setNumeratorIndicatorUuid(INDICATOR_3);
        dataSourceDto.getAnnualPercentageRate().getQuantity().setIsPercentage(Boolean.TRUE);
        dataSourceDto.getAnnualPercentageRate().getQuantity().setBaseQuantityIndicatorUuid(INDICATOR_1);

        dataSourceDto.setAnnualPuntualRate(new RateDerivationDto());
        dataSourceDto.getAnnualPuntualRate().setMethodType(RateDerivationMethodTypeEnum.LOAD);
        dataSourceDto.getAnnualPuntualRate().setMethod("Method3");
        dataSourceDto.getAnnualPuntualRate().setRounding(RateDerivationRoundingEnum.DOWN);
        dataSourceDto.getAnnualPuntualRate().setQuantity(new QuantityDto());
        dataSourceDto.getAnnualPuntualRate().getQuantity().setType(QuantityTypeEnum.AMOUNT);
        dataSourceDto.getAnnualPuntualRate().getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        dataSourceDto.getAnnualPuntualRate().getQuantity().setDecimalPlaces(Integer.valueOf(2));

        String uuidIndicator = INDICATOR_1;
        DataSourceDto dataSourceDtoCreated = indicatorsServiceFacade.createDataSource(getServiceContextAdministrador(), uuidIndicator, dataSourceDto);
        assertNotNull(dataSourceDtoCreated.getUuid());

        // Retrieve data source
        DataSourceDto dataSourceDtoRetrieved = indicatorsServiceFacade.retrieveDataSource(getServiceContextAdministrador(), dataSourceDtoCreated.getUuid());
        IndicatorsAsserts.assertEqualsDataSource(dataSourceDto, dataSourceDtoRetrieved);

        // Retrieves all data sources
        List<DataSourceDto> dataSourcesDto = indicatorsServiceFacade.retrieveDataSourcesByIndicator(getServiceContextAdministrador(), uuidIndicator, "2.000");
        assertEquals(3, dataSourcesDto.size());
        assertEquals(DATA_SOURCE_1_INDICATOR_1_V2, dataSourcesDto.get(0).getUuid());
        assertEquals(DATA_SOURCE_2_INDICATOR_1_V2, dataSourcesDto.get(1).getUuid());
        assertEquals(dataSourceDtoCreated.getUuid(), dataSourcesDto.get(2).getUuid());

        IndicatorDto indicator = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), INDICATOR_1, INDICATOR_1_V2);
        assertTrue(indicator.getNeedsUpdate());
    }

    @Test
    public void testCreateDataSourceWithValuesInsteadOfVariables() throws Exception {

        // Create dataSource
        DataSourceDto dataSourceDto = new DataSourceDto();
        dataSourceDto.setAbsoluteMethod("absoluteMethod");
        dataSourceDto.setDataGpeUuid("queryGpe1");
        dataSourceDto.setPxUri("px1");
        dataSourceDto.setTimeValue("2010");
        dataSourceDto.setGeographicalValueUuid(GEOGRAPHICAL_VALUE_1);
        dataSourceDto.setSourceSurveyCode("sourceSurveyCode");
        dataSourceDto.setSourceSurveyTitle(IndicatorsMocks.mockInternationalStringDto());
        dataSourceDto.setSourceSurveyAcronym(IndicatorsMocks.mockInternationalStringDto());
        dataSourceDto.setSourceSurveyUrl("sourceSurveyUrl");
        dataSourceDto.getPublishers().add("ISTAC");
        dataSourceDto.getPublishers().add("INE");
        dataSourceDto.getPublishers().add("IBESTAT");
        DataSourceVariableDto dataSourceVariableDto1 = new DataSourceVariableDto();
        dataSourceVariableDto1.setVariable("variable1");
        dataSourceVariableDto1.setCategory("category1");
        dataSourceDto.addOtherVariable(dataSourceVariableDto1);
        DataSourceVariableDto dataSourceVariableDto2 = new DataSourceVariableDto();
        dataSourceVariableDto2.setVariable("variable2");
        dataSourceVariableDto2.setCategory("category2");
        dataSourceDto.addOtherVariable(dataSourceVariableDto2);

        String uuidIndicator = INDICATOR_1;
        DataSourceDto dataSourceDtoCreated = indicatorsServiceFacade.createDataSource(getServiceContextAdministrador(), uuidIndicator, dataSourceDto);
        assertNotNull(dataSourceDtoCreated.getUuid());

        // Retrieve data source
        DataSourceDto dataSourceDtoRetrieved = indicatorsServiceFacade.retrieveDataSource(getServiceContextAdministrador(), dataSourceDtoCreated.getUuid());
        IndicatorsAsserts.assertEqualsDataSource(dataSourceDto, dataSourceDtoRetrieved);
    }

    @Test
    public void testCreateDataSourceErrorParametersRequired() throws Exception {

        DataSourceDto dataSourceDto = new DataSourceDto();
        dataSourceDto.setDataGpeUuid(null);
        dataSourceDto.setAbsoluteMethod("absoluteMethod");
        dataSourceDto.setPxUri("px1");
        dataSourceDto.setInterperiodPuntualRate(new RateDerivationDto());
        dataSourceDto.setAnnualPercentageRate(new RateDerivationDto());
        dataSourceDto.getAnnualPercentageRate().setMethodType(RateDerivationMethodTypeEnum.CALCULATE);
        dataSourceDto.getAnnualPercentageRate().setQuantity(new QuantityDto());
        DataSourceVariableDto dataSourceVariableDto = new DataSourceVariableDto();
        dataSourceVariableDto.setCategory("category");
        dataSourceVariableDto.setVariable(null);
        dataSourceDto.addOtherVariable(dataSourceVariableDto);

        try {
            indicatorsServiceFacade.createDataSource(getServiceContextAdministrador(), INDICATOR_1, dataSourceDto);
            fail("parameters required");
        } catch (MetamacException e) {
            assertEquals(16, e.getExceptionItems().size());

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.DATA_SOURCE_DATA_GPE_UUID, e.getExceptionItems().get(0).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(1).getCode());
            assertEquals(1, e.getExceptionItems().get(1).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.DATA_SOURCE_SOURCE_SURVEY_CODE, e.getExceptionItems().get(1).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(2).getCode());
            assertEquals(1, e.getExceptionItems().get(2).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.DATA_SOURCE_SOURCE_SURVEY_TITLE, e.getExceptionItems().get(2).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(3).getCode());
            assertEquals(1, e.getExceptionItems().get(3).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.DATA_SOURCE_PUBLISHERS, e.getExceptionItems().get(3).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(4).getCode());
            assertEquals(1, e.getExceptionItems().get(4).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.DATA_SOURCE_ANNUAL_PERCENTAGE_RATE_METHOD, e.getExceptionItems().get(4).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(5).getCode());
            assertEquals(1, e.getExceptionItems().get(5).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.DATA_SOURCE_ANNUAL_PERCENTAGE_RATE_ROUNDING, e.getExceptionItems().get(5).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(6).getCode());
            assertEquals(1, e.getExceptionItems().get(6).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.DATA_SOURCE_ANNUAL_PERCENTAGE_RATE_QUANTITY_TYPE, e.getExceptionItems().get(6).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(7).getCode());
            assertEquals(1, e.getExceptionItems().get(7).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.DATA_SOURCE_ANNUAL_PERCENTAGE_RATE_QUANTITY_UNIT_UUID, e.getExceptionItems().get(7).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(8).getCode());
            assertEquals(1, e.getExceptionItems().get(8).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.DATA_SOURCE_ANNUAL_PERCENTAGE_RATE_QUANTITY_DECIMAL_PLACES, e.getExceptionItems().get(8).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(9).getCode());
            assertEquals(1, e.getExceptionItems().get(9).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE_METHOD_TYPE, e.getExceptionItems().get(9).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(10).getCode());
            assertEquals(1, e.getExceptionItems().get(10).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE_METHOD, e.getExceptionItems().get(10).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(11).getCode());
            assertEquals(1, e.getExceptionItems().get(11).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE_ROUNDING, e.getExceptionItems().get(11).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(12).getCode());
            assertEquals(1, e.getExceptionItems().get(12).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE_QUANTITY, e.getExceptionItems().get(12).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(13).getCode());
            assertEquals(2, e.getExceptionItems().get(13).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.DATA_SOURCE_TIME_VARIABLE, e.getExceptionItems().get(13).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.DATA_SOURCE_TIME_VALUE, e.getExceptionItems().get(13).getMessageParameters()[1]);

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(14).getCode());
            assertEquals(2, e.getExceptionItems().get(14).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.DATA_SOURCE_GEOGRAPHICAL_VARIABLE, e.getExceptionItems().get(14).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.DATA_SOURCE_GEOGRAPHICAL_VALUE_UUID, e.getExceptionItems().get(14).getMessageParameters()[1]);

            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(15).getCode());
            assertEquals(1, e.getExceptionItems().get(15).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.DATA_SOURCE_OTHER_VARIABLE_VARIABLE, e.getExceptionItems().get(15).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateDataSourceErrorQuantityTypeIncorrect() throws Exception {

        // Create dataSource
        DataSourceDto dataSourceDto = new DataSourceDto();
        dataSourceDto.setAbsoluteMethod("absoluteMethod");
        dataSourceDto.setDataGpeUuid("queryGpe1");
        dataSourceDto.setPxUri("px1");
        dataSourceDto.setTimeVariable("timeVariable1");
        dataSourceDto.setGeographicalVariable("geographicalVariable1");
        DataSourceVariableDto dataSourceVariableDto1 = new DataSourceVariableDto();
        dataSourceVariableDto1.setVariable("variable1");
        dataSourceVariableDto1.setCategory("category1");
        dataSourceDto.addOtherVariable(dataSourceVariableDto1);
        dataSourceDto.setSourceSurveyCode("sourceSurveyCode");
        dataSourceDto.setSourceSurveyTitle(IndicatorsMocks.mockInternationalStringDto());
        dataSourceDto.getPublishers().add("ISTAC");

        dataSourceDto.setInterperiodPuntualRate(new RateDerivationDto());
        dataSourceDto.getInterperiodPuntualRate().setMethodType(RateDerivationMethodTypeEnum.LOAD);
        dataSourceDto.getInterperiodPuntualRate().setMethod("Method3");
        dataSourceDto.getInterperiodPuntualRate().setRounding(RateDerivationRoundingEnum.DOWN);
        dataSourceDto.getInterperiodPuntualRate().setQuantity(new QuantityDto());
        dataSourceDto.getInterperiodPuntualRate().getQuantity().setType(QuantityTypeEnum.CHANGE_RATE); // should be amount
        dataSourceDto.getInterperiodPuntualRate().getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        dataSourceDto.getInterperiodPuntualRate().getQuantity().setNumeratorIndicatorUuid(INDICATOR_2);
        dataSourceDto.getInterperiodPuntualRate().getQuantity().setIsPercentage(Boolean.FALSE);
        dataSourceDto.getInterperiodPuntualRate().getQuantity().setBaseQuantityIndicatorUuid(INDICATOR_1);
        dataSourceDto.getInterperiodPuntualRate().getQuantity().setDecimalPlaces(Integer.valueOf(1));

        dataSourceDto.setInterperiodPercentageRate(new RateDerivationDto());
        dataSourceDto.getInterperiodPercentageRate().setMethodType(RateDerivationMethodTypeEnum.CALCULATE);
        dataSourceDto.getInterperiodPercentageRate().setMethod("Method1");
        dataSourceDto.getInterperiodPercentageRate().setRounding(RateDerivationRoundingEnum.DOWN);
        dataSourceDto.getInterperiodPercentageRate().setQuantity(new QuantityDto());
        dataSourceDto.getInterperiodPercentageRate().getQuantity().setType(QuantityTypeEnum.AMOUNT); // should be change_rate
        dataSourceDto.getInterperiodPercentageRate().getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        dataSourceDto.getInterperiodPercentageRate().getQuantity().setDecimalPlaces(Integer.valueOf(2));

        dataSourceDto.setAnnualPercentageRate(new RateDerivationDto());
        dataSourceDto.getAnnualPercentageRate().setMethodType(RateDerivationMethodTypeEnum.LOAD);
        dataSourceDto.getAnnualPercentageRate().setMethod("Method3");
        dataSourceDto.getAnnualPercentageRate().setRounding(RateDerivationRoundingEnum.DOWN);
        dataSourceDto.getAnnualPercentageRate().setQuantity(new QuantityDto());
        dataSourceDto.getAnnualPercentageRate().getQuantity().setType(QuantityTypeEnum.AMOUNT); // should be change_rate
        dataSourceDto.getAnnualPercentageRate().getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        dataSourceDto.getAnnualPercentageRate().getQuantity().setDecimalPlaces(Integer.valueOf(1));

        dataSourceDto.setAnnualPuntualRate(new RateDerivationDto());
        dataSourceDto.getAnnualPuntualRate().setMethodType(RateDerivationMethodTypeEnum.LOAD);
        dataSourceDto.getAnnualPuntualRate().setMethod("Method2");
        dataSourceDto.getAnnualPuntualRate().setRounding(RateDerivationRoundingEnum.UPWARD);
        dataSourceDto.getAnnualPuntualRate().setQuantity(new QuantityDto());
        dataSourceDto.getAnnualPuntualRate().getQuantity().setType(QuantityTypeEnum.CHANGE_RATE); // should be amount
        dataSourceDto.getAnnualPuntualRate().getQuantity().setUnitUuid(QUANTITY_UNIT_2);
        dataSourceDto.getAnnualPuntualRate().getQuantity().setNumeratorIndicatorUuid(INDICATOR_3);
        dataSourceDto.getAnnualPuntualRate().getQuantity().setIsPercentage(Boolean.TRUE);
        dataSourceDto.getAnnualPuntualRate().getQuantity().setBaseQuantityIndicatorUuid(INDICATOR_1);
        dataSourceDto.getAnnualPuntualRate().getQuantity().setDecimalPlaces(Integer.valueOf(1));

        String uuidIndicator = INDICATOR_1;

        try {
            indicatorsServiceFacade.createDataSource(getServiceContextAdministrador(), uuidIndicator, dataSourceDto);
        } catch (MetamacException e) {
            assertEquals(4, e.getExceptionItems().size());

            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.DATA_SOURCE_ANNUAL_PUNTUAL_RATE_QUANTITY_TYPE, e.getExceptionItems().get(0).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(1).getCode());
            assertEquals(1, e.getExceptionItems().get(1).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.DATA_SOURCE_ANNUAL_PERCENTAGE_RATE_QUANTITY_TYPE, e.getExceptionItems().get(1).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(2).getCode());
            assertEquals(1, e.getExceptionItems().get(2).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE_QUANTITY_TYPE, e.getExceptionItems().get(2).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(3).getCode());
            assertEquals(1, e.getExceptionItems().get(3).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.DATA_SOURCE_INTERPERIOD_PERCENTAGE_RATE_QUANTITY_TYPE, e.getExceptionItems().get(3).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateDataSourceErrorParametersIncorrect() throws Exception {

        // Create dataSource: timeValue and decimalPlaces incorrect
        DataSourceDto dataSourceDto = new DataSourceDto();
        dataSourceDto.setAbsoluteMethod("absoluteMethod");
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
        dataSourceDto.setSourceSurveyCode("sourceSurveyCode");
        dataSourceDto.setSourceSurveyTitle(IndicatorsMocks.mockInternationalStringDto());
        dataSourceDto.getPublishers().add("ISTAC");

        dataSourceDto.setInterperiodPuntualRate(new RateDerivationDto());
        dataSourceDto.getInterperiodPuntualRate().setMethodType(RateDerivationMethodTypeEnum.CALCULATE);
        dataSourceDto.getInterperiodPuntualRate().setMethod("Method1");
        dataSourceDto.getInterperiodPuntualRate().setRounding(RateDerivationRoundingEnum.DOWN);
        dataSourceDto.getInterperiodPuntualRate().setQuantity(new QuantityDto());
        dataSourceDto.getInterperiodPuntualRate().getQuantity().setType(QuantityTypeEnum.AMOUNT);
        dataSourceDto.getInterperiodPuntualRate().getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        dataSourceDto.getInterperiodPuntualRate().getQuantity().setDecimalPlaces(Integer.valueOf(11));

        String uuidIndicator = INDICATOR_1;
        try {
            indicatorsServiceFacade.createDataSource(getServiceContextAdministrador(), uuidIndicator, dataSourceDto);
        } catch (MetamacException e) {
            assertEquals(2, e.getExceptionItems().size());

            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.DATA_SOURCE_INTERPERIOD_PUNTUAL_RATE_QUANTITY_DECIMAL_PLACES, e.getExceptionItems().get(0).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(1).getCode());
            assertEquals(1, e.getExceptionItems().get(1).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.DATA_SOURCE_TIME_VALUE, e.getExceptionItems().get(1).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateDataSourceErrorMetadataUnexpected() throws Exception {

        // Create dataSource
        DataSourceDto dataSourceDto = new DataSourceDto();
        dataSourceDto.setAbsoluteMethod("absoluteMethod");
        dataSourceDto.setDataGpeUuid("queryGpe1");
        dataSourceDto.setPxUri("px1");

        dataSourceDto.setTimeVariable("timeVariable");
        dataSourceDto.setTimeValue("2010");
        dataSourceDto.setGeographicalVariable("geographicalVariable");
        dataSourceDto.setGeographicalValueUuid(GEOGRAPHICAL_VALUE_1);
        dataSourceDto.setSourceSurveyCode("sourceSurveyCode");
        dataSourceDto.setSourceSurveyTitle(IndicatorsMocks.mockInternationalStringDto());
        dataSourceDto.getPublishers().add("ISTAC");

        DataSourceVariableDto dataSourceVariableDto1 = new DataSourceVariableDto();
        dataSourceVariableDto1.setVariable("variable1");
        dataSourceVariableDto1.setCategory("category1");
        dataSourceDto.addOtherVariable(dataSourceVariableDto1);
        DataSourceVariableDto dataSourceVariableDto2 = new DataSourceVariableDto();
        dataSourceVariableDto2.setVariable("variable2");
        dataSourceVariableDto2.setCategory("category2");
        dataSourceDto.addOtherVariable(dataSourceVariableDto2);

        String uuidIndicator = INDICATOR_1;
        try {
            indicatorsServiceFacade.createDataSource(getServiceContextAdministrador(), uuidIndicator, dataSourceDto);
        } catch (MetamacException e) {
            assertEquals(2, e.getExceptionItems().size());

            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.DATA_SOURCE_TIME_VALUE, e.getExceptionItems().get(0).getMessageParameters()[0]);

            assertEquals(ServiceExceptionType.METADATA_UNEXPECTED.getCode(), e.getExceptionItems().get(1).getCode());
            assertEquals(1, e.getExceptionItems().get(1).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.DATA_SOURCE_GEOGRAPHICAL_VALUE_UUID, e.getExceptionItems().get(1).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateDataSourceErrorIndicatorNotExists() throws Exception {

        String indicatorUuid = NOT_EXISTS;

        DataSourceDto dataSourceDto = new DataSourceDto();
        dataSourceDto.setAbsoluteMethod("absoluteMethod");
        dataSourceDto.setDataGpeUuid("queryGpe1");
        dataSourceDto.setPxUri("px1");
        dataSourceDto.setTimeVariable("timeVariable1");
        dataSourceDto.setGeographicalVariable("geographicalVariable1");
        dataSourceDto.setSourceSurveyCode("sourceSurveyCode");
        dataSourceDto.setSourceSurveyTitle(IndicatorsMocks.mockInternationalStringDto());
        dataSourceDto.getPublishers().add("ISTAC");

        try {
            indicatorsServiceFacade.createDataSource(getServiceContextAdministrador(), indicatorUuid, dataSourceDto);
            fail("Indicator not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(indicatorUuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateDataSourceErrorIndicatorHasNotVersionInProduction() throws Exception {

        String indicatorUuid = INDICATOR_3;

        DataSourceDto dataSourceDto = new DataSourceDto();
        dataSourceDto.setAbsoluteMethod("absoluteMethod");
        dataSourceDto.setDataGpeUuid("queryGpe1");
        dataSourceDto.setPxUri("px1");
        dataSourceDto.setTimeVariable("timeVariable1");
        dataSourceDto.setGeographicalVariable("geographicalVariable1");
        dataSourceDto.setSourceSurveyCode("sourceSurveyCode");
        dataSourceDto.setSourceSurveyTitle(IndicatorsMocks.mockInternationalStringDto());
        dataSourceDto.getPublishers().add("ISTAC");

        try {
            indicatorsServiceFacade.createDataSource(getServiceContextAdministrador(), indicatorUuid, dataSourceDto);
            fail("Indicator not in production");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_IN_PRODUCTION_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(indicatorUuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
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
        dataSourceDto.setSourceSurveyCode("sourceSurveyCode");
        dataSourceDto.setSourceSurveyTitle(IndicatorsMocks.mockInternationalStringDto());
        dataSourceDto.getPublishers().add("ISTAC");

        DataSourceVariableDto dataSourceVariableDto1 = new DataSourceVariableDto();
        dataSourceVariableDto1.setVariable("variable1");
        dataSourceVariableDto1.setCategory("category1");
        dataSourceDto.addOtherVariable(dataSourceVariableDto1);
        DataSourceVariableDto dataSourceVariableDto2 = new DataSourceVariableDto();
        dataSourceVariableDto2.setVariable("variable2");
        dataSourceVariableDto2.setCategory("category2");
        dataSourceDto.addOtherVariable(dataSourceVariableDto2);

        dataSourceDto.setAnnualPercentageRate(new RateDerivationDto());
        dataSourceDto.getAnnualPercentageRate().setMethodType(RateDerivationMethodTypeEnum.LOAD);
        dataSourceDto.getAnnualPercentageRate().setMethod("Method2");
        dataSourceDto.getAnnualPercentageRate().setRounding(RateDerivationRoundingEnum.UPWARD);
        dataSourceDto.getAnnualPercentageRate().setQuantity(new QuantityDto());
        dataSourceDto.getAnnualPercentageRate().getQuantity().setType(QuantityTypeEnum.CHANGE_RATE);
        dataSourceDto.getAnnualPercentageRate().getQuantity().setUnitUuid(QUANTITY_UNIT_2);
        dataSourceDto.getAnnualPercentageRate().getQuantity().setNumeratorIndicatorUuid(indicatorUuidLinked);
        dataSourceDto.getAnnualPercentageRate().getQuantity().setIsPercentage(Boolean.TRUE);
        dataSourceDto.getAnnualPercentageRate().getQuantity().setBaseQuantityIndicatorUuid(indicatorUuidLinked);
        dataSourceDto.getAnnualPercentageRate().getQuantity().setDecimalPlaces(Integer.valueOf(1));

        try {
            indicatorsServiceFacade.createDataSource(getServiceContextAdministrador(), indicatorUuid, dataSourceDto);
            fail("Base quantity is not own indicator");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.DATA_SOURCE_ANNUAL_PERCENTAGE_RATE_QUANTITY_BASE_QUANTITY_INDICATOR_UUID, e.getExceptionItems().get(0).getMessageParameters()[0]);
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
        dataSourceDto.setSourceSurveyCode("sourceSurveyCode");
        dataSourceDto.setSourceSurveyTitle(IndicatorsMocks.mockInternationalStringDto());
        dataSourceDto.getPublishers().add("ISTAC");

        DataSourceVariableDto dataSourceVariableDto1 = new DataSourceVariableDto();
        dataSourceVariableDto1.setVariable("variable1");
        dataSourceVariableDto1.setCategory("category1");
        dataSourceDto.addOtherVariable(dataSourceVariableDto1);
        DataSourceVariableDto dataSourceVariableDto2 = new DataSourceVariableDto();
        dataSourceVariableDto2.setVariable("variable2");
        dataSourceVariableDto2.setCategory("category2");
        dataSourceDto.addOtherVariable(dataSourceVariableDto2);
        dataSourceDto.setAnnualPercentageRate(new RateDerivationDto());
        dataSourceDto.getAnnualPercentageRate().setMethodType(RateDerivationMethodTypeEnum.LOAD);
        dataSourceDto.getAnnualPercentageRate().setMethod("Method2");
        dataSourceDto.getAnnualPercentageRate().setRounding(RateDerivationRoundingEnum.UPWARD);
        dataSourceDto.getAnnualPercentageRate().setQuantity(new QuantityDto());
        dataSourceDto.getAnnualPercentageRate().getQuantity().setType(QuantityTypeEnum.CHANGE_RATE);
        dataSourceDto.getAnnualPercentageRate().getQuantity().setUnitUuid(QUANTITY_UNIT_2);
        dataSourceDto.getAnnualPercentageRate().getQuantity().setDenominatorIndicatorUuid(indicatorUuid);
        dataSourceDto.getAnnualPercentageRate().getQuantity().setIsPercentage(Boolean.TRUE);
        dataSourceDto.getAnnualPercentageRate().getQuantity().setBaseQuantityIndicatorUuid(indicatorUuid);
        dataSourceDto.getAnnualPercentageRate().getQuantity().setDecimalPlaces(Integer.valueOf(1));

        try {
            indicatorsServiceFacade.createDataSource(getServiceContextAdministrador(), indicatorUuid, dataSourceDto);
            fail("Denominator must not be own indicator");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.DATA_SOURCE_ANNUAL_PERCENTAGE_RATE_QUANTITY_DENOMINATOR_INDICATOR_UUID, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateDataSourceErrorBaseLocationNotExists() throws Exception {

        String indicatorUuid = INDICATOR_1;

        // Create dataSource
        DataSourceDto dataSourceDto = new DataSourceDto();
        dataSourceDto.setDataGpeUuid("queryGpe1");
        dataSourceDto.setPxUri("px1");
        dataSourceDto.setTimeVariable("timeVariable1");
        dataSourceDto.setGeographicalVariable("geographicalVariable1");
        dataSourceDto.setSourceSurveyCode("sourceSurveyCode");
        dataSourceDto.setSourceSurveyTitle(IndicatorsMocks.mockInternationalStringDto());
        dataSourceDto.getPublishers().add("ISTAC");

        DataSourceVariableDto dataSourceVariableDto1 = new DataSourceVariableDto();
        dataSourceVariableDto1.setVariable("variable1");
        dataSourceVariableDto1.setCategory("category1");
        dataSourceDto.addOtherVariable(dataSourceVariableDto1);
        DataSourceVariableDto dataSourceVariableDto2 = new DataSourceVariableDto();
        dataSourceVariableDto2.setVariable("variable2");
        dataSourceVariableDto2.setCategory("category2");
        dataSourceDto.addOtherVariable(dataSourceVariableDto2);

        dataSourceDto.setAnnualPercentageRate(new RateDerivationDto());
        dataSourceDto.getAnnualPercentageRate().setMethodType(RateDerivationMethodTypeEnum.LOAD);
        dataSourceDto.getAnnualPercentageRate().setMethod("Method2");
        dataSourceDto.getAnnualPercentageRate().setRounding(RateDerivationRoundingEnum.UPWARD);
        dataSourceDto.getAnnualPercentageRate().setQuantity(new QuantityDto());
        dataSourceDto.getAnnualPercentageRate().getQuantity().setType(QuantityTypeEnum.CHANGE_RATE);
        dataSourceDto.getAnnualPercentageRate().getQuantity().setUnitUuid(QUANTITY_UNIT_2);
        dataSourceDto.getAnnualPercentageRate().getQuantity().setDenominatorIndicatorUuid(indicatorUuid);
        dataSourceDto.getAnnualPercentageRate().getQuantity().setIsPercentage(Boolean.TRUE);
        dataSourceDto.getAnnualPercentageRate().getQuantity().setBaseLocationUuid(NOT_EXISTS);
        dataSourceDto.getAnnualPercentageRate().getQuantity().setBaseQuantityIndicatorUuid(indicatorUuid);

        try {
            indicatorsServiceFacade.createDataSource(getServiceContextAdministrador(), indicatorUuid, dataSourceDto);
            fail("Base location not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.GEOGRAPHICAL_VALUE_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(dataSourceDto.getAnnualPercentageRate().getQuantity().getBaseLocationUuid(), e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testDeleteDataSource() throws Exception {

        String uuid = DATA_SOURCE_1_INDICATOR_1_V2;

        // Delete dataSource
        indicatorsServiceFacade.deleteDataSource(getServiceContextAdministrador(), uuid);

        // Validation
        try {
            indicatorsServiceFacade.retrieveDataSource(getServiceContextAdministrador(), uuid);
            fail("DataSource deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DATA_SOURCE_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);

            IndicatorDto indicator = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), INDICATOR_1, INDICATOR_1_V2);
            assertTrue(indicator.getNeedsUpdate());
        }
    }

    @Test
    public void testDeleteDataSourceErrorIndicatorVersionPublished() throws Exception {

        try {
            indicatorsServiceFacade.deleteDataSource(getServiceContextAdministrador(), DATA_SOURCE_1_INDICATOR_3);
            fail("Indicator not in production");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(INDICATOR_3, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.INDICATOR_PROC_STATUS_DRAFT, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(ServiceExceptionParameters.INDICATOR_PROC_STATUS_VALIDATION_REJECTED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
            assertEquals(ServiceExceptionParameters.INDICATOR_PROC_STATUS_PRODUCTION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[2]);
            assertEquals(ServiceExceptionParameters.INDICATOR_PROC_STATUS_DIFFUSION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[3]);
            assertEquals(ServiceExceptionParameters.INDICATOR_PROC_STATUS_PUBLICATION_FAILED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[4]);
        }
    }

    @Test
    public void testDeleteDataSourceErrorNotExists() throws Exception {

        String uuid = NOT_EXISTS;

        // Validation
        try {
            indicatorsServiceFacade.deleteDataSource(getServiceContextAdministrador(), uuid);
            fail("DataSource not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DATA_SOURCE_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRetrieveDataSourcesByIndicator() throws Exception {

        String uuidIndicator = INDICATOR_1;

        // Version 1.000
        {
            List<DataSourceDto> dataSourcesDto = indicatorsServiceFacade.retrieveDataSourcesByIndicator(getServiceContextAdministrador(), uuidIndicator, "1.000");
            assertEquals(1, dataSourcesDto.size());
            assertEquals(DATA_SOURCE_1_INDICATOR_1_V1, dataSourcesDto.get(0).getUuid());
        }

        // Version 2.000
        {
            List<DataSourceDto> dataSourcesDto = indicatorsServiceFacade.retrieveDataSourcesByIndicator(getServiceContextAdministrador(), uuidIndicator, "2.000");
            assertEquals(2, dataSourcesDto.size());

            assertEquals(DATA_SOURCE_1_INDICATOR_1_V2, dataSourcesDto.get(0).getUuid());
            assertEquals(DATA_SOURCE_2_INDICATOR_1_V2, dataSourcesDto.get(1).getUuid());
        }
    }

    @Test
    public void testRetrieveDataSourcesErrorNotExists() throws Exception {

        String uuid = NOT_EXISTS;

        // Validation
        try {
            indicatorsServiceFacade.retrieveDataSourcesByIndicator(getServiceContextAdministrador(), uuid, "1.000");
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
        DataSourceDto dataSourceDto = indicatorsServiceFacade.retrieveDataSource(getServiceContextAdministrador(), uuid);
        dataSourceDto.setPxUri("newPx");
        dataSourceDto.setDataGpeUuid("newData");
        dataSourceDto.setTimeVariable("newTime");
        dataSourceDto.getOtherVariables().get(0).setCategory("new Category");
        DataSourceVariableDto dataSourceVariableDto3 = new DataSourceVariableDto();
        dataSourceVariableDto3.setVariable("variable3new");
        dataSourceVariableDto3.setCategory("category3new");
        dataSourceDto.addOtherVariable(dataSourceVariableDto3);
        dataSourceDto.setAnnualPuntualRate(null);
        dataSourceDto.getAnnualPercentageRate().setMethod("newMethod");

        // Update
        DataSourceDto dataSourceDtoUpdated = indicatorsServiceFacade.updateDataSource(getServiceContextAdministrador(), dataSourceDto);

        // Validation
        IndicatorsAsserts.assertEqualsDataSource(dataSourceDto, dataSourceDtoUpdated);
        dataSourceDtoUpdated = indicatorsServiceFacade.retrieveDataSource(getServiceContextAdministrador(), uuid);
        assertEquals(3, dataSourceDtoUpdated.getOtherVariables().size());
        IndicatorsAsserts.assertEqualsDataSource(dataSourceDto, dataSourceDtoUpdated);

        IndicatorDto indicator = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), INDICATOR_1, INDICATOR_1_V2);
        assertTrue(indicator.getNeedsUpdate());
    }

    @Test
    public void testUpdateDataSourceInPublicationFailed() throws Exception {

        String uuid = DATA_SOURCE_1_INDICATOR_11;
        DataSourceDto dataSourceDto = indicatorsServiceFacade.retrieveDataSource(getServiceContextAdministrador(), uuid);
        dataSourceDto.setPxUri("newPx");
        dataSourceDto.setDataGpeUuid("newData");
        dataSourceDto.setTimeVariable("newTime");

        // Update
        DataSourceDto dataSourceDtoUpdated = indicatorsServiceFacade.updateDataSource(getServiceContextAdministrador(), dataSourceDto);

        // Validation
        IndicatorsAsserts.assertEqualsDataSource(dataSourceDto, dataSourceDtoUpdated);
    }

    @Test
    public void testUpdateDataSourceErrorMethodDuplicated() throws Exception {

        String uuid = DATA_SOURCE_1_INDICATOR_1_V2;
        DataSourceDto dataSourceDto = indicatorsServiceFacade.retrieveDataSource(getServiceContextAdministrador(), uuid);
        dataSourceDto.setAbsoluteMethod(dataSourceDto.getAnnualPercentageRate().getMethod());

        try {
            indicatorsServiceFacade.updateDataSource(getServiceContextAdministrador(), dataSourceDto);
            fail("Absolute method of dataset is equals to method of annual percentage rate");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.DATA_SOURCE_ABSOLUTE_METHOD, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateDataSourceErrorIndicatorPublished() throws Exception {

        DataSourceDto dataSourceDto = indicatorsServiceFacade.retrieveDataSource(getServiceContextAdministrador(), DATA_SOURCE_1_INDICATOR_1_V1);

        try {
            indicatorsServiceFacade.updateDataSource(getServiceContextAdministrador(), dataSourceDto);
            fail("Indicator published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.INDICATOR_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(INDICATOR_1, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.INDICATOR_PROC_STATUS_DRAFT, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(ServiceExceptionParameters.INDICATOR_PROC_STATUS_VALIDATION_REJECTED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
            assertEquals(ServiceExceptionParameters.INDICATOR_PROC_STATUS_PRODUCTION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[2]);
            assertEquals(ServiceExceptionParameters.INDICATOR_PROC_STATUS_DIFFUSION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[3]);
            assertEquals(ServiceExceptionParameters.INDICATOR_PROC_STATUS_PUBLICATION_FAILED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[4]);
        }
    }

    @Test
    public void testUpdateDataSourceErrorNotExists() throws Exception {

        DataSourceDto dataSourceDto = new DataSourceDto();
        dataSourceDto.setUuid(NOT_EXISTS);

        try {
            indicatorsServiceFacade.updateDataSource(getServiceContextAdministrador(), dataSourceDto);
            fail("Data source not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.DATA_SOURCE_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateDataSourceErrorOptimisticLocking() throws Exception {

        String uuid = DATA_SOURCE_1_INDICATOR_1_V2;

        DataSourceDto dataSourceDtoSession1 = indicatorsServiceFacade.retrieveDataSource(getServiceContextAdministrador(), uuid);
        assertEquals(Long.valueOf(1), dataSourceDtoSession1.getVersionOptimisticLocking());
        dataSourceDtoSession1.setPxUri("newPx");

        DataSourceDto dataSourceDtoSession2 = indicatorsServiceFacade.retrieveDataSource(getServiceContextAdministrador(), uuid);
        assertEquals(Long.valueOf(1), dataSourceDtoSession2.getVersionOptimisticLocking());
        dataSourceDtoSession2.setSourceSurveyAcronym(IndicatorsMocks.mockInternationalStringDto());

        // Update by session 2
        DataSourceDto dataSourceDtoSession2AfterUpdate = indicatorsServiceFacade.updateDataSource(getServiceContextAdministrador(), dataSourceDtoSession2);
        assertEquals(Long.valueOf(2), dataSourceDtoSession2AfterUpdate.getVersionOptimisticLocking());
        IndicatorsAsserts.assertEqualsDataSource(dataSourceDtoSession2, dataSourceDtoSession2AfterUpdate);

        // Fails when is updated by session 1
        try {
            indicatorsServiceFacade.updateDataSource(getServiceContextAdministrador(), dataSourceDtoSession1);
            fail("Optimistic locking");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.OPTIMISTIC_LOCKING.getCode(), e.getExceptionItems().get(0).getCode());
            assertNull(e.getExceptionItems().get(0).getMessageParameters());
        }

        // Session 2 can modify because has last version
        dataSourceDtoSession2AfterUpdate.setDataGpeUuid("dataGpeNewUuid");
        DataSourceDto dataSourceDtoSession2AfterUpdate2 = indicatorsServiceFacade.updateDataSource(getServiceContextAdministrador(), dataSourceDtoSession2AfterUpdate);
        assertEquals(Long.valueOf(3), dataSourceDtoSession2AfterUpdate2.getVersionOptimisticLocking());
        IndicatorsAsserts.assertEqualsDataSource(dataSourceDtoSession2AfterUpdate, dataSourceDtoSession2AfterUpdate2);
    }

    @Test
    @Transactional
    public void testFindQuantityUnits() throws Exception {
        // All
        {
            MetamacCriteriaResult<QuantityUnitDto> quantityUnitsResult = indicatorsServiceFacade.findQuantityUnits(getServiceContextAdministrador(), null);
            assertEquals(Integer.valueOf(0), quantityUnitsResult.getPaginatorResult().getFirstResult());
            assertEquals(Integer.valueOf(25), quantityUnitsResult.getPaginatorResult().getMaximumResultSize());
            assertEquals(Integer.valueOf(3), quantityUnitsResult.getPaginatorResult().getTotalResults());
            assertEquals(3, quantityUnitsResult.getResults().size());

            List<QuantityUnitDto> quantityUnits = quantityUnitsResult.getResults();
            assertEquals(QUANTITY_UNIT_1, quantityUnits.get(0).getUuid());
            assertEquals(QUANTITY_UNIT_2, quantityUnits.get(1).getUuid());
            assertEquals(QUANTITY_UNIT_3, quantityUnits.get(2).getUuid());
        }

        // All, only 1 results
        {
            MetamacCriteria criteria = new MetamacCriteria();
            criteria.setPaginator(new MetamacCriteriaPaginator());
            criteria.getPaginator().setMaximumResultSize(Integer.valueOf(1));
            criteria.getPaginator().setCountTotalResults(Boolean.TRUE);
            MetamacCriteriaResult<QuantityUnitDto> quantityUnitsResult = indicatorsServiceFacade.findQuantityUnits(getServiceContextAdministrador(), criteria);
            assertEquals(Integer.valueOf(0), quantityUnitsResult.getPaginatorResult().getFirstResult());
            assertEquals(Integer.valueOf(1), quantityUnitsResult.getPaginatorResult().getMaximumResultSize());
            assertEquals(Integer.valueOf(3), quantityUnitsResult.getPaginatorResult().getTotalResults());
            assertEquals(1, quantityUnitsResult.getResults().size());

            List<QuantityUnitDto> quantityUnits = quantityUnitsResult.getResults();
            assertEquals(QUANTITY_UNIT_1, quantityUnits.get(0).getUuid());
        }

        // All, only 1 results second page
        {
            MetamacCriteria criteria = new MetamacCriteria();
            criteria.setPaginator(new MetamacCriteriaPaginator());
            criteria.getPaginator().setMaximumResultSize(Integer.valueOf(2));
            criteria.getPaginator().setFirstResult(Integer.valueOf(2));
            criteria.getPaginator().setCountTotalResults(Boolean.TRUE);
            MetamacCriteriaResult<QuantityUnitDto> quantityUnitsResult = indicatorsServiceFacade.findQuantityUnits(getServiceContextAdministrador(), criteria);
            assertEquals(Integer.valueOf(2), quantityUnitsResult.getPaginatorResult().getFirstResult());
            assertEquals(Integer.valueOf(2), quantityUnitsResult.getPaginatorResult().getMaximumResultSize());
            assertEquals(Integer.valueOf(3), quantityUnitsResult.getPaginatorResult().getTotalResults());
            assertEquals(1, quantityUnitsResult.getResults().size());

            List<QuantityUnitDto> quantityUnits = quantityUnitsResult.getResults();
            assertEquals(QUANTITY_UNIT_3, quantityUnits.get(0).getUuid());
        }
    }

    @Test
    public void testRetrieveQuantityUnits() throws Exception {

        List<QuantityUnitDto> quantityUnits = indicatorsServiceFacade.retrieveQuantityUnits(getServiceContextAdministrador());
        assertEquals(3, quantityUnits.size());

        Map<String, String> quantityUnitsExpected = new HashMap<String, String>();
        quantityUnitsExpected.put(QUANTITY_UNIT_1, "km");
        quantityUnitsExpected.put(QUANTITY_UNIT_2, "kg");
        quantityUnitsExpected.put(QUANTITY_UNIT_3, "m");
        checkQuantityUnitsInCollection(quantityUnitsExpected, quantityUnits);
    }

    @Test
    public void testCreateQuantityUnit() throws Exception {
        QuantityUnitDto quantityUnitDto = IndicatorsMocks.mockQuantityUnit("es", "personas");

        // Create
        QuantityUnitDto quantityUnitDtoCreated = indicatorsServiceFacade.createQuantityUnit(getServiceContextAdministrador(), quantityUnitDto);

        // Validate
        assertNotNull(quantityUnitDtoCreated);
        assertNotNull(quantityUnitDtoCreated.getUuid());
        assertNotNull(quantityUnitDtoCreated.getOptimisticLockingVersion());

        IndicatorsAsserts.assertEqualsCreatedQuantityUnitDto(quantityUnitDto, quantityUnitDtoCreated);

        // Audit validations
        assertNotNull(quantityUnitDtoCreated.getCreatedBy());
        assertNotNull(quantityUnitDtoCreated.getCreatedDate());
        assertNotNull(quantityUnitDtoCreated.getLastUpdated());
        assertNotNull(quantityUnitDtoCreated.getLastUpdatedBy());
        assertEquals(getServiceContextAdministrador().getUserId(), quantityUnitDtoCreated.getCreatedBy());
        assertTrue(DateUtils.isSameDay(new Date(), quantityUnitDtoCreated.getCreatedDate()));
        assertTrue(DateUtils.isSameDay(new Date(), quantityUnitDtoCreated.getLastUpdated()));
        assertEquals(getServiceContextAdministrador().getUserId(), quantityUnitDtoCreated.getLastUpdatedBy());
    }

    @Test
    public void testCreateQuantityUnitErrorQuantityUnitRequired() throws Exception {
        try {
            indicatorsServiceFacade.createQuantityUnit(getServiceContextAdministrador(), null);
            fail("parameter required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.QUANTITY_UNIT, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateQuantityUnitErrorTitleRequired() throws Exception {
        QuantityUnitDto quantityUnitDto = IndicatorsMocks.mockQuantityUnit(null, null);
        try {
            indicatorsServiceFacade.createQuantityUnit(getServiceContextAdministrador(), quantityUnitDto);
            fail("metadata required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.QUANTITY_UNIT_TITLE, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateQuantityUnitErrorTitleRequiredEmpty() throws Exception {
        QuantityUnitDto quantityUnitDto = IndicatorsMocks.mockQuantityUnit("es", StringUtils.EMPTY);
        try {
            indicatorsServiceFacade.createQuantityUnit(getServiceContextAdministrador(), quantityUnitDto);
            fail("metadata required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.QUANTITY_UNIT_TITLE, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateQuantityUnit() throws Exception {
        String uuid = QUANTITY_UNIT_1;

        QuantityUnit quantityUnit = indicatorsService.retrieveQuantityUnit(getServiceContextAdministrador(), uuid);
        QuantityUnitDto quantityUnitDto = do2DtoMapper.quantityUnitDoToDto(quantityUnit);

        quantityUnitDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        quantityUnitDto.setSymbol(IndicatorsMocks.mockString(3));
        quantityUnitDto.setSymbolPosition(QuantityUnitSymbolPositionEnum.START);

        // Update
        QuantityUnitDto quantityUnitDtoUpdated = indicatorsServiceFacade.updateQuantityUnit(getServiceContextAdministrador(), quantityUnitDto);

        // Validations
        IndicatorsAsserts.assertEqualsQuantityUnitDto(quantityUnitDto, quantityUnitDtoUpdated);
        assertTrue(quantityUnitDtoUpdated.getLastUpdated().after(quantityUnitDtoUpdated.getCreatedDate()));
        assertTrue(quantityUnitDtoUpdated.getLastUpdated().after(quantityUnitDto.getLastUpdated()));
    }

    @Test
    public void testUpdateQuantityUnitNotExists() throws Exception {

        QuantityUnit quantityUnit = indicatorsService.retrieveQuantityUnit(getServiceContextAdministrador(), QUANTITY_UNIT_1);
        QuantityUnitDto quantityUnitDto = do2DtoMapper.quantityUnitDoToDto(quantityUnit);

        quantityUnitDto.setUuid(NOT_EXISTS);
        try {
            indicatorsServiceFacade.updateQuantityUnit(getServiceContextAdministrador(), quantityUnitDto);
            fail("quantity unit not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.QUANTITY_UNIT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateQuantityUnitErrorOptimisticLocking() throws Exception {

        String uuid = QUANTITY_UNIT_1;

        QuantityUnit quantityUnit = indicatorsService.retrieveQuantityUnit(getServiceContextAdministrador(), uuid);
        QuantityUnitDto quantityUnitDtoSession1 = do2DtoMapper.quantityUnitDoToDto(quantityUnit);
        assertEquals(Long.valueOf(1), quantityUnitDtoSession1.getOptimisticLockingVersion());

        quantityUnit = indicatorsService.retrieveQuantityUnit(getServiceContextAdministrador(), uuid);
        QuantityUnitDto quantityUnitDtoSession2 = do2DtoMapper.quantityUnitDoToDto(quantityUnit);
        assertEquals(Long.valueOf(1), quantityUnitDtoSession2.getOptimisticLockingVersion());

        // Update by session 1
        quantityUnitDtoSession1.setTitle(IndicatorsMocks.mockInternationalStringDto());
        QuantityUnitDto quantityUnitDtoSession1AfterUpdate = indicatorsServiceFacade.updateQuantityUnit(getServiceContextAdministrador(), quantityUnitDtoSession1);
        IndicatorsAsserts.assertEqualsQuantityUnitDto(quantityUnitDtoSession1, quantityUnitDtoSession1AfterUpdate);
        assertEquals(Long.valueOf(2), quantityUnitDtoSession1AfterUpdate.getOptimisticLockingVersion());

        // Fails when is updated by session 2
        try {
            indicatorsServiceFacade.updateQuantityUnit(getServiceContextAdministrador(), quantityUnitDtoSession2);
            fail("Optimistic locking");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.OPTIMISTIC_LOCKING.getCode(), e.getExceptionItems().get(0).getCode());
            assertNull(e.getExceptionItems().get(0).getMessageParameters());
        }

        // Session 1 can modify because has last version
        quantityUnitDtoSession1AfterUpdate.setTitle(IndicatorsMocks.mockInternationalStringDto());
        QuantityUnitDto quantityUnitDtoSession1AfterUpdate2 = indicatorsServiceFacade.updateQuantityUnit(getServiceContextAdministrador(), quantityUnitDtoSession1AfterUpdate);
        assertEquals(Long.valueOf(3), quantityUnitDtoSession1AfterUpdate2.getOptimisticLockingVersion());
        IndicatorsAsserts.assertEqualsQuantityUnitDto(quantityUnitDtoSession1AfterUpdate, quantityUnitDtoSession1AfterUpdate2);
    }

    @Test
    public void testDeleteQuantityUnit() throws Exception {
        String uuid = QUANTITY_UNIT_3;

        // Delete
        indicatorsServiceFacade.deleteQuantityUnit(getServiceContextAdministrador(), uuid);

        // Validation
        try {
            indicatorsService.retrieveQuantityUnit(getServiceContextAdministrador(), uuid);
            fail("Quantity unit deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.QUANTITY_UNIT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testDeleteQuantityUnitNotExists() throws Exception {
        String uuid = NOT_EXISTS;

        // Delete
        try {
            indicatorsServiceFacade.deleteQuantityUnit(getServiceContextAdministrador(), uuid);
            fail("Quantity unit not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.QUANTITY_UNIT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testDeleteQuantityUnitBeingUsed() throws Exception {
        String uuid = QUANTITY_UNIT_1;

        // Delete
        try {
            indicatorsServiceFacade.deleteQuantityUnit(getServiceContextAdministrador(), uuid);
            fail("Quantity unit being used");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.QUANTITY_UNIT_CAN_NOT_BE_REMOVED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(uuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRetrieveSubjects() throws Exception {

        List<SubjectDto> subjects = indicatorsServiceFacade.retrieveSubjects(getServiceContextAdministrador());
        assertEquals(5, subjects.size());

        assertEquals(SUBJECT_1, subjects.get(0).getCode());
        IndicatorsAsserts.assertEqualsInternationalString(subjects.get(0).getTitle(), "es", "Área temática 1", null, null);
        assertEquals(SUBJECT_2, subjects.get(1).getCode());
        IndicatorsAsserts.assertEqualsInternationalString(subjects.get(1).getTitle(), "es", "Área temática 2", null, null);
        assertEquals(SUBJECT_3, subjects.get(2).getCode());
        IndicatorsAsserts.assertEqualsInternationalString(subjects.get(2).getTitle(), "es", "Área temática 3", null, null);
        assertEquals(SUBJECT_4, subjects.get(3).getCode());
        IndicatorsAsserts.assertEqualsInternationalString(subjects.get(3).getTitle(), "es", "Área temática 4", null, null);
        assertEquals(SUBJECT_5, subjects.get(4).getCode());
        IndicatorsAsserts.assertEqualsInternationalString(subjects.get(4).getTitle(), "es", "Área temática 5", null, null);
    }

    @Test
    @Transactional
    public void testFindUnitMultipliers() throws Exception {
        // All
        {
            MetamacCriteriaResult<UnitMultiplierDto> unitMultipliersResult = indicatorsServiceFacade.findUnitMultipliers(getServiceContextAdministrador(), null);
            assertEquals(Integer.valueOf(0), unitMultipliersResult.getPaginatorResult().getFirstResult());
            assertEquals(Integer.valueOf(25), unitMultipliersResult.getPaginatorResult().getMaximumResultSize());
            assertEquals(Integer.valueOf(7), unitMultipliersResult.getPaginatorResult().getTotalResults());
            assertEquals(7, unitMultipliersResult.getResults().size());

            List<UnitMultiplierDto> unitMultipliers = unitMultipliersResult.getResults();
            assertEquals(UNIT_MULTIPLIER_1, unitMultipliers.get(0).getUuid());
            assertEquals(UNIT_MULTIPLIER_2, unitMultipliers.get(1).getUuid());
            assertEquals(UNIT_MULTIPLIER_3, unitMultipliers.get(2).getUuid());
            assertEquals(UNIT_MULTIPLIER_4, unitMultipliers.get(3).getUuid());
            assertEquals(UNIT_MULTIPLIER_5, unitMultipliers.get(4).getUuid());
            assertEquals(UNIT_MULTIPLIER_6, unitMultipliers.get(5).getUuid());
            assertEquals(UNIT_MULTIPLIER_7, unitMultipliers.get(6).getUuid());
        }

        // All, only 1 results
        {
            MetamacCriteria criteria = new MetamacCriteria();
            criteria.setPaginator(new MetamacCriteriaPaginator());
            criteria.getPaginator().setMaximumResultSize(Integer.valueOf(1));
            criteria.getPaginator().setCountTotalResults(Boolean.TRUE);
            MetamacCriteriaResult<UnitMultiplierDto> unitMultipliersResult = indicatorsServiceFacade.findUnitMultipliers(getServiceContextAdministrador(), criteria);
            assertEquals(Integer.valueOf(0), unitMultipliersResult.getPaginatorResult().getFirstResult());
            assertEquals(Integer.valueOf(1), unitMultipliersResult.getPaginatorResult().getMaximumResultSize());
            assertEquals(Integer.valueOf(7), unitMultipliersResult.getPaginatorResult().getTotalResults());
            assertEquals(1, unitMultipliersResult.getResults().size());

            List<UnitMultiplierDto> unitMultipliers = unitMultipliersResult.getResults();
            assertEquals(UNIT_MULTIPLIER_1, unitMultipliers.get(0).getUuid());
        }

        // All, only 2 results second page
        {
            MetamacCriteria criteria = new MetamacCriteria();
            criteria.setPaginator(new MetamacCriteriaPaginator());
            criteria.getPaginator().setMaximumResultSize(Integer.valueOf(2));
            criteria.getPaginator().setFirstResult(Integer.valueOf(2));
            criteria.getPaginator().setCountTotalResults(Boolean.TRUE);
            MetamacCriteriaResult<UnitMultiplierDto> unitMultipliersResult = indicatorsServiceFacade.findUnitMultipliers(getServiceContextAdministrador(), criteria);
            assertEquals(Integer.valueOf(2), unitMultipliersResult.getPaginatorResult().getFirstResult());
            assertEquals(Integer.valueOf(2), unitMultipliersResult.getPaginatorResult().getMaximumResultSize());
            assertEquals(Integer.valueOf(7), unitMultipliersResult.getPaginatorResult().getTotalResults());
            assertEquals(2, unitMultipliersResult.getResults().size());

            List<UnitMultiplierDto> unitMultipliers = unitMultipliersResult.getResults();
            assertEquals(UNIT_MULTIPLIER_3, unitMultipliers.get(0).getUuid());
        }
    }

    @Test
    public void testRetrieveUnitsMultipliers() throws Exception {

        List<UnitMultiplierDto> unitsMultipliers = indicatorsServiceFacade.retrieveUnitsMultipliers(getServiceContextAdministrador());
        assertEquals(7, unitsMultipliers.size());

        assertEquals(Integer.valueOf(1), unitsMultipliers.get(0).getUnitMultiplier());
        IndicatorsAsserts.assertEqualsInternationalString(unitsMultipliers.get(0).getTitle(), "es", "Unidades", null, null);

        assertEquals(Integer.valueOf(10), unitsMultipliers.get(1).getUnitMultiplier());
        IndicatorsAsserts.assertEqualsInternationalString(unitsMultipliers.get(1).getTitle(), "es", "Decenas", null, null);

        assertEquals(Integer.valueOf(100), unitsMultipliers.get(2).getUnitMultiplier());
        IndicatorsAsserts.assertEqualsInternationalString(unitsMultipliers.get(2).getTitle(), "es", "Cientos", null, null);

        assertEquals(Integer.valueOf(1000), unitsMultipliers.get(3).getUnitMultiplier());
        IndicatorsAsserts.assertEqualsInternationalString(unitsMultipliers.get(3).getTitle(), "es", "Miles", null, null);

        assertEquals(Integer.valueOf(10000), unitsMultipliers.get(4).getUnitMultiplier());
        IndicatorsAsserts.assertEqualsInternationalString(unitsMultipliers.get(4).getTitle(), "es", "Decenas de miles", null, null);

        assertEquals(Integer.valueOf(100000), unitsMultipliers.get(5).getUnitMultiplier());
        IndicatorsAsserts.assertEqualsInternationalString(unitsMultipliers.get(5).getTitle(), "es", "Cientos de miles", null, null);

        assertEquals(Integer.valueOf(1000000), unitsMultipliers.get(6).getUnitMultiplier());
        IndicatorsAsserts.assertEqualsInternationalString(unitsMultipliers.get(6).getTitle(), "es", "Millones", null, null);
    }

    @Test
    public void testCreateUnitMultiplier() throws Exception {
        UnitMultiplierDto unitMultiplierDto = IndicatorsMocks.mockUnitMultiplier(2);

        // Create
        UnitMultiplierDto unitMultiplierDtoCreated = indicatorsServiceFacade.createUnitMultiplier(getServiceContextAdministrador(), unitMultiplierDto);

        // Validate
        assertNotNull(unitMultiplierDtoCreated);
        assertNotNull(unitMultiplierDtoCreated.getUuid());
        assertNotNull(unitMultiplierDtoCreated.getOptimisticLockingVersion());

        IndicatorsAsserts.assertEqualsCreatedUnitMultiplierDto(unitMultiplierDto, unitMultiplierDtoCreated);

        // Audit validations
        assertNotNull(unitMultiplierDtoCreated.getCreatedBy());
        assertNotNull(unitMultiplierDtoCreated.getCreatedDate());
        assertNotNull(unitMultiplierDtoCreated.getLastUpdated());
        assertNotNull(unitMultiplierDtoCreated.getLastUpdatedBy());
        assertEquals(getServiceContextAdministrador().getUserId(), unitMultiplierDtoCreated.getCreatedBy());
        assertTrue(DateUtils.isSameDay(new Date(), unitMultiplierDtoCreated.getCreatedDate()));
        assertTrue(DateUtils.isSameDay(new Date(), unitMultiplierDtoCreated.getLastUpdated()));
        assertEquals(getServiceContextAdministrador().getUserId(), unitMultiplierDtoCreated.getLastUpdatedBy());
    }

    @Test
    public void testCreateUnitMultiplierErrorUnitMultiplierRequired() throws Exception {
        try {
            indicatorsServiceFacade.createUnitMultiplier(getServiceContextAdministrador(), null);
            fail("parameter required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.PARAMETER_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.UNIT_MULTIPLIER, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateUnitMultiplierErrorValueRequired() throws Exception {
        UnitMultiplierDto unitMultiplierDto = IndicatorsMocks.mockUnitMultiplier(null);
        try {
            indicatorsServiceFacade.createUnitMultiplier(getServiceContextAdministrador(), unitMultiplierDto);
            fail("metadata required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_REQUIRED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.UNIT_MULTIPLIER_VALUE, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testCreateUnitMultiplierErrorValueDuplicated() throws Exception {
        Integer value = 1;
        UnitMultiplierDto unitMultiplierDto = IndicatorsMocks.mockUnitMultiplier(value);
        try {
            indicatorsServiceFacade.createUnitMultiplier(getServiceContextAdministrador(), unitMultiplierDto);
            fail("value duplicated");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.UNIT_MULTIPLIER_ALREADY_EXISTS_VALUE_DUPLICATED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(value, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateUnitMultiplier() throws Exception {
        String uuid = UNIT_MULTIPLIER_1;

        UnitMultiplier unitMultiplier = indicatorsService.retrieveUnitMultiplier(getServiceContextAdministrador(), uuid);
        UnitMultiplierDto unitMultiplierDto = do2DtoMapper.unitMultiplierDoToDto(unitMultiplier);

        unitMultiplierDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        unitMultiplierDto.setUnitMultiplier(2);

        // Update
        UnitMultiplierDto unitMultiplierDtoUpdated = indicatorsServiceFacade.updateUnitMultiplier(getServiceContextAdministrador(), unitMultiplierDto);

        // Validations
        IndicatorsAsserts.assertEqualsUnitMultiplierDto(unitMultiplierDto, unitMultiplierDtoUpdated);
        assertTrue(unitMultiplierDtoUpdated.getLastUpdated().after(unitMultiplierDtoUpdated.getCreatedDate()));
        assertTrue(unitMultiplierDtoUpdated.getLastUpdated().after(unitMultiplierDto.getLastUpdated()));
    }

    @Test
    public void testUpdateUnitMultiplierNotExists() throws Exception {

        UnitMultiplier unitMultiplier = indicatorsService.retrieveUnitMultiplier(getServiceContextAdministrador(), UNIT_MULTIPLIER_1);
        UnitMultiplierDto unitMultiplierDto = do2DtoMapper.unitMultiplierDoToDto(unitMultiplier);

        unitMultiplierDto.setUuid(NOT_EXISTS);
        try {
            indicatorsServiceFacade.updateUnitMultiplier(getServiceContextAdministrador(), unitMultiplierDto);
            fail("unit multiplier not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.UNIT_MULTIPLIER_NOT_FOUND_UUID.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateUnitMultiplierValueDuplicated() throws Exception {
        Integer value = 10;
        UnitMultiplier unitMultiplier = indicatorsService.retrieveUnitMultiplier(getServiceContextAdministrador(), 1);
        UnitMultiplierDto unitMultiplierDto = do2DtoMapper.unitMultiplierDoToDto(unitMultiplier);
        unitMultiplierDto.setUnitMultiplier(value);
        try {
            indicatorsServiceFacade.updateUnitMultiplier(getServiceContextAdministrador(), unitMultiplierDto);
            fail("unit multiplier value duplicated");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.UNIT_MULTIPLIER_ALREADY_EXISTS_VALUE_DUPLICATED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(value, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateUnitMultiplierErrorOptimisticLocking() throws Exception {

        String uuid = UNIT_MULTIPLIER_1;

        UnitMultiplier unitMultiplier = indicatorsService.retrieveUnitMultiplier(getServiceContextAdministrador(), uuid);
        UnitMultiplierDto unitMultiplierDtoSession1 = do2DtoMapper.unitMultiplierDoToDto(unitMultiplier);
        assertEquals(Long.valueOf(1), unitMultiplierDtoSession1.getOptimisticLockingVersion());

        unitMultiplier = indicatorsService.retrieveUnitMultiplier(getServiceContextAdministrador(), uuid);
        UnitMultiplierDto unitMultiplierDtoSession2 = do2DtoMapper.unitMultiplierDoToDto(unitMultiplier);
        assertEquals(Long.valueOf(1), unitMultiplierDtoSession2.getOptimisticLockingVersion());

        // Update by session 1
        unitMultiplierDtoSession1.setTitle(IndicatorsMocks.mockInternationalStringDto());
        UnitMultiplierDto unitMultiplierDtoSession1AfterUpdate = indicatorsServiceFacade.updateUnitMultiplier(getServiceContextAdministrador(), unitMultiplierDtoSession1);
        IndicatorsAsserts.assertEqualsUnitMultiplierDto(unitMultiplierDtoSession1, unitMultiplierDtoSession1AfterUpdate);
        assertEquals(Long.valueOf(2), unitMultiplierDtoSession1AfterUpdate.getOptimisticLockingVersion());

        // Fails when is updated by session 2
        try {
            indicatorsServiceFacade.updateUnitMultiplier(getServiceContextAdministrador(), unitMultiplierDtoSession2);
            fail("Optimistic locking");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.OPTIMISTIC_LOCKING.getCode(), e.getExceptionItems().get(0).getCode());
            assertNull(e.getExceptionItems().get(0).getMessageParameters());
        }

        // Session 1 can modify because has last version
        unitMultiplierDtoSession1AfterUpdate.setTitle(IndicatorsMocks.mockInternationalStringDto());
        UnitMultiplierDto unitMultiplierDtoSession1AfterUpdate2 = indicatorsServiceFacade.updateUnitMultiplier(getServiceContextAdministrador(), unitMultiplierDtoSession1AfterUpdate);
        assertEquals(Long.valueOf(3), unitMultiplierDtoSession1AfterUpdate2.getOptimisticLockingVersion());
        IndicatorsAsserts.assertEqualsUnitMultiplierDto(unitMultiplierDtoSession1AfterUpdate, unitMultiplierDtoSession1AfterUpdate2);
    }

    @Test
    public void testDeleteUnitMultiplier() throws Exception {
        String unitMultiplierUuid = UNIT_MULTIPLIER_1;

        // Delete
        indicatorsServiceFacade.deleteUnitMultiplier(getServiceContextAdministrador(), unitMultiplierUuid);

        // Validation
        try {
            indicatorsService.retrieveUnitMultiplier(getServiceContextAdministrador(), unitMultiplierUuid);
            fail("Unit multiplier deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.UNIT_MULTIPLIER_NOT_FOUND_UUID.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(unitMultiplierUuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testDeleteUnitMultiplierNotExists() throws Exception {
        String unitMultiplierUuid = NOT_EXISTS;

        // Delete
        try {
            indicatorsServiceFacade.deleteUnitMultiplier(getServiceContextAdministrador(), unitMultiplierUuid);
            fail("Unit multiplier not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.UNIT_MULTIPLIER_NOT_FOUND_UUID.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(unitMultiplierUuid, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    private void checkQuantityUnitsInCollection(Map<String, String> quantityUnitsExpected, Collection<QuantityUnitDto> quantityUnitsActual) {
        assertEquals(quantityUnitsExpected.size(), quantityUnitsActual.size());

        for (QuantityUnitDto quantityUnit : quantityUnitsActual) {
            String code = quantityUnitsExpected.get(quantityUnit.getUuid());
            assertEquals(code, quantityUnit.getSymbol());
        }
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/IndicatorsServiceFacadeIndicatorsTest.xml";
    }
}
