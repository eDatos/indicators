package es.gobcan.istac.indicators.core.serviceapi;

import java.util.Date;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.sso.client.MetamacPrincipal;
import org.siemac.metamac.sso.client.MetamacPrincipalAccess;
import org.siemac.metamac.sso.client.SsoClientConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.gobcan.istac.indicators.core.constants.IndicatorsConstants;
import es.gobcan.istac.indicators.core.domain.QuantityUnit;
import es.gobcan.istac.indicators.core.domain.UnitMultiplier;
import es.gobcan.istac.indicators.core.dto.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.QuantityDto;
import es.gobcan.istac.indicators.core.dto.QuantityUnitDto;
import es.gobcan.istac.indicators.core.dto.UnitMultiplierDto;
import es.gobcan.istac.indicators.core.enume.domain.QuantityTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.QueryEnvironmentEnum;
import es.gobcan.istac.indicators.core.enume.domain.RoleEnum;
import es.gobcan.istac.indicators.core.enume.domain.VersionTypeEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.mapper.Do2DtoMapper;
import es.gobcan.istac.indicators.core.serviceapi.utils.IndicatorsMocks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Security tester.
 * Don't test operations to "Data", because is for Any Role
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/include/indicators-service-mockito.xml", "classpath:spring/applicationContext-test.xml"})
@TransactionConfiguration(defaultRollback = true, transactionManager = "txManager")
@Transactional
public class SecurityIndicatorsServiceFacadeIndicatorsTest extends IndicatorsBaseTest {

    @Autowired
    protected IndicatorsServiceFacade indicatorsServiceFacade;

    @Autowired
    private IndicatorsService         indicatorsService;

    @Autowired
    private Do2DtoMapper              do2DtoMapper;

    @Test
    public void testErrorPrincipalNotFound() throws Exception {

        try {
            ServiceContext ctx = getServiceContextAdministrador();
            ctx.setProperty(SsoClientConstants.PRINCIPAL_ATTRIBUTE, null);
            indicatorsServiceFacade.retrieveIndicator(ctx, INDICATOR_1, null);
            fail("principal required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_PRINCIPAL_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testErrorPrincipalWithoutRoleIndicators() throws Exception {

        try {
            ServiceContext ctx = getServiceContextAdministrador();
            assertEquals(1, ((MetamacPrincipal) ctx.getProperty(SsoClientConstants.PRINCIPAL_ATTRIBUTE)).getAccesses().size());
            MetamacPrincipalAccess access = ((MetamacPrincipal) ctx.getProperty(SsoClientConstants.PRINCIPAL_ATTRIBUTE)).getAccesses().get(0);
            access.setApplication(NOT_EXISTS);
            access.setRole(RoleEnum.TECNICO_APOYO_DIFUSION.getName());
            indicatorsServiceFacade.retrieveIndicator(ctx, INDICATOR_1, null);
            fail("principal without role");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testCreateIndicator() throws Exception {

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
        indicatorDto.getQuantity().setUnitMultiplier(Integer.valueOf(10));

        // With access
        indicatorsServiceFacade.createIndicator(getServiceContextTecnicoProduccion(), indicatorDto);
        indicatorDto.setCode("code" + (new Date()).getTime());
        indicatorDto.setViewCode("viewcode" + (new Date()).getTime());
        indicatorsServiceFacade.createIndicator(getServiceContextTecnicoApoyoProduccion(), indicatorDto);

        // Without access
        try {
            indicatorsServiceFacade.createIndicator(getServiceContextTecnicoSistemaIndicadores(), indicatorDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.createIndicator(getServiceContextTecnicoDifusion(), indicatorDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.createIndicator(getServiceContextTecnicoApoyoDifusion(), indicatorDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testUpdateIndicator() throws Exception {

        IndicatorDto indicatorDto = indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), INDICATOR_1, "2.000");

        // With access
        indicatorDto = indicatorsServiceFacade.updateIndicator(getServiceContextTecnicoProduccion(), indicatorDto);
        indicatorDto = indicatorsServiceFacade.updateIndicator(getServiceContextTecnicoApoyoProduccion(), indicatorDto);

        // Without access
        try {
            indicatorsServiceFacade.updateIndicator(getServiceContextTecnicoSistemaIndicadores(), indicatorDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.updateIndicator(getServiceContextTecnicoDifusion(), indicatorDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.updateIndicator(getServiceContextTecnicoApoyoDifusion(), indicatorDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testRetrieveIndicator() throws Exception {
        indicatorsServiceFacade.retrieveIndicator(getServiceContextAdministrador(), INDICATOR_1, null);
        indicatorsServiceFacade.retrieveIndicator(getServiceContextTecnicoSistemaIndicadores(), INDICATOR_1, null);
        indicatorsServiceFacade.retrieveIndicator(getServiceContextTecnicoProduccion(), INDICATOR_1, null);
        indicatorsServiceFacade.retrieveIndicator(getServiceContextTecnicoApoyoProduccion(), INDICATOR_1, null);
        indicatorsServiceFacade.retrieveIndicator(getServiceContextTecnicoDifusion(), INDICATOR_1, null);
        indicatorsServiceFacade.retrieveIndicator(getServiceContextTecnicoApoyoDifusion(), INDICATOR_1, null);
    }

    @Test
    public void testRetrieveIndicatorByCode() throws Exception {
        indicatorsServiceFacade.retrieveIndicatorByCode(getServiceContextAdministrador(), INDICATOR_1_CODE, null);
        indicatorsServiceFacade.retrieveIndicatorByCode(getServiceContextTecnicoSistemaIndicadores(), INDICATOR_1_CODE, null);
        indicatorsServiceFacade.retrieveIndicatorByCode(getServiceContextTecnicoProduccion(), INDICATOR_1_CODE, null);
        indicatorsServiceFacade.retrieveIndicatorByCode(getServiceContextTecnicoApoyoProduccion(), INDICATOR_1_CODE, null);
        indicatorsServiceFacade.retrieveIndicatorByCode(getServiceContextTecnicoDifusion(), INDICATOR_1_CODE, null);
        indicatorsServiceFacade.retrieveIndicatorByCode(getServiceContextTecnicoApoyoDifusion(), INDICATOR_1_CODE, null);
    }

    @Test
    public void testDeleteIndicator() throws Exception {
        indicatorsServiceFacade.deleteIndicator(getServiceContextTecnicoProduccion(), INDICATOR_4);
    }

    @Test
    public void testDeleteIndicatorRoleTecnicoApoyoProduccion() throws Exception {
        indicatorsServiceFacade.deleteIndicator(getServiceContextTecnicoApoyoProduccion(), INDICATOR_4);
    }

    @Test
    public void testDeleteIndicatorErrorWithoutAccess() throws Exception {
        try {
            indicatorsServiceFacade.deleteIndicator(getServiceContextTecnicoSistemaIndicadores(), INDICATOR_4);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.deleteIndicator(getServiceContextTecnicoDifusion(), INDICATOR_4);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.deleteIndicator(getServiceContextTecnicoApoyoDifusion(), INDICATOR_4);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testSendIndicatorToProductionValidation() throws Exception {
        indicatorsServiceFacade.sendIndicatorToProductionValidation(getServiceContextTecnicoProduccion(), INDICATOR_1);
    }

    @Test
    public void testSendIndicatorToProductionValidationTecnicoSistemaIndicadores() throws Exception {
        indicatorsServiceFacade.sendIndicatorToProductionValidation(getServiceContextTecnicoApoyoProduccion(), INDICATOR_1);
    }

    @Test
    public void testSendIndicatorToProductionValidationErrorWithoutAccess() throws Exception {

        try {
            indicatorsServiceFacade.sendIndicatorToProductionValidation(getServiceContextTecnicoSistemaIndicadores(), INDICATOR_1);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.sendIndicatorToProductionValidation(getServiceContextTecnicoDifusion(), INDICATOR_1);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.sendIndicatorToProductionValidation(getServiceContextTecnicoApoyoDifusion(), INDICATOR_1);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testRejectIndicatorProductionValidation() throws Exception {
        indicatorsServiceFacade.rejectIndicatorProductionValidation(getServiceContextTecnicoProduccion(), INDICATOR_4);
    }

    @Test
    public void testRejectIndicatorProductionValidationErrorWithoutAccess() throws Exception {

        // Without access
        try {
            indicatorsServiceFacade.rejectIndicatorProductionValidation(getServiceContextTecnicoSistemaIndicadores(), INDICATOR_4);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.rejectIndicatorProductionValidation(getServiceContextTecnicoApoyoProduccion(), INDICATOR_4);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.rejectIndicatorProductionValidation(getServiceContextTecnicoDifusion(), INDICATOR_4);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.rejectIndicatorProductionValidation(getServiceContextTecnicoApoyoDifusion(), INDICATOR_4);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testSendIndicatorToDiffusionValidation() throws Exception {

        // With access
        indicatorsServiceFacade.sendIndicatorToDiffusionValidation(getServiceContextTecnicoProduccion(), INDICATOR_4);
    }

    @Test
    public void testSendIndicatorToDiffusionValidationErrorWithoutAccess() throws Exception {

        // Without access
        try {
            indicatorsServiceFacade.sendIndicatorToDiffusionValidation(getServiceContextTecnicoSistemaIndicadores(), INDICATOR_4);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.sendIndicatorToDiffusionValidation(getServiceContextTecnicoApoyoProduccion(), INDICATOR_4);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.sendIndicatorToDiffusionValidation(getServiceContextTecnicoDifusion(), INDICATOR_4);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.sendIndicatorToDiffusionValidation(getServiceContextTecnicoApoyoDifusion(), INDICATOR_4);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }

    }

    @Test
    public void testRejectIndicatorDiffusionValidation() throws Exception {
        indicatorsServiceFacade.rejectIndicatorDiffusionValidation(getServiceContextTecnicoDifusion(), INDICATOR_5);
    }

    @Test
    public void testRejectIndicatorDiffusionValidationErrorWithoutAccess() throws Exception {

        // Without access
        try {
            indicatorsServiceFacade.rejectIndicatorDiffusionValidation(getServiceContextTecnicoSistemaIndicadores(), INDICATOR_5);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.rejectIndicatorDiffusionValidation(getServiceContextTecnicoProduccion(), INDICATOR_5);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.rejectIndicatorDiffusionValidation(getServiceContextTecnicoApoyoProduccion(), INDICATOR_5);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.rejectIndicatorDiffusionValidation(getServiceContextTecnicoApoyoDifusion(), INDICATOR_5);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testPublishIndicator() throws Exception {
        indicatorsServiceFacade.publishIndicator(getServiceContextTecnicoDifusion(), INDICATOR_5);
    }

    @Test
    public void testPublishIndicatorTecnicoApoyoDifusion() throws Exception {
        indicatorsServiceFacade.publishIndicator(getServiceContextTecnicoApoyoDifusion(), INDICATOR_5);
    }

    @Test
    public void testPublishIndicatorErrorWithoutAccess() throws Exception {
        // Without access
        try {
            indicatorsServiceFacade.publishIndicator(getServiceContextTecnicoSistemaIndicadores(), INDICATOR_5);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.publishIndicator(getServiceContextTecnicoProduccion(), INDICATOR_5);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.publishIndicator(getServiceContextTecnicoApoyoProduccion(), INDICATOR_5);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testArchiveIndicator() throws Exception {
        indicatorsServiceFacade.archiveIndicator(getServiceContextTecnicoDifusion(), INDICATOR_3);
    }

    @Test
    public void testArchiveIndicatorErrorWithoutAccess() throws Exception {
        // Without access
        try {
            indicatorsServiceFacade.archiveIndicator(getServiceContextTecnicoSistemaIndicadores(), INDICATOR_3);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.archiveIndicator(getServiceContextTecnicoProduccion(), INDICATOR_3);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.archiveIndicator(getServiceContextTecnicoApoyoProduccion(), INDICATOR_3);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.archiveIndicator(getServiceContextTecnicoApoyoDifusion(), INDICATOR_3);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testVersioningIndicator() throws Exception {
        indicatorsServiceFacade.versioningIndicator(getServiceContextTecnicoProduccion(), INDICATOR_3, VersionTypeEnum.MAJOR);
    }

    @Test
    public void testVersioningIndicatorTecnicoApoyoProduccion() throws Exception {
        indicatorsServiceFacade.versioningIndicator(getServiceContextTecnicoApoyoProduccion(), INDICATOR_3, VersionTypeEnum.MAJOR);
    }

    @Test
    public void testVersioningIndicatorErrorWithoutAccess() throws Exception {
        try {
            indicatorsServiceFacade.versioningIndicator(getServiceContextTecnicoSistemaIndicadores(), INDICATOR_3, VersionTypeEnum.MAJOR);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.versioningIndicator(getServiceContextTecnicoDifusion(), INDICATOR_3, VersionTypeEnum.MAJOR);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.versioningIndicator(getServiceContextTecnicoApoyoDifusion(), INDICATOR_3, VersionTypeEnum.MAJOR);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testFindIndicators() throws Exception {
        indicatorsServiceFacade.findIndicators(getServiceContextAdministrador(), null);
        indicatorsServiceFacade.findIndicators(getServiceContextTecnicoSistemaIndicadores(), null);
        indicatorsServiceFacade.findIndicators(getServiceContextTecnicoProduccion(), null);
        indicatorsServiceFacade.findIndicators(getServiceContextTecnicoApoyoProduccion(), null);
        indicatorsServiceFacade.findIndicators(getServiceContextTecnicoDifusion(), null);
        indicatorsServiceFacade.findIndicators(getServiceContextTecnicoApoyoProduccion(), null);
    }

    @Test
    public void testCreateDataSource() throws Exception {
        DataSourceDto dataSourceDto = new DataSourceDto();
        dataSourceDto.setQueryEnvironment(QueryEnvironmentEnum.GPE);
        dataSourceDto.setDataGpeUuid("queryGpe1");
        dataSourceDto.setPxUri("px1");
        dataSourceDto.setTimeVariable("timeVariable1");
        dataSourceDto.setGeographicalVariable("geographicalVariable1");
        dataSourceDto.setSourceSurveyCode("sourceSurveyCode");
        dataSourceDto.setSourceSurveyTitle(IndicatorsMocks.mockInternationalStringDto());
        dataSourceDto.setSourceSurveyAcronym(IndicatorsMocks.mockInternationalStringDto());
        dataSourceDto.setSourceSurveyUrl("sourceSurveyUrl");
        dataSourceDto.setAbsoluteMethod("method");
        dataSourceDto.getPublishers().add("IBESTAT");

        indicatorsServiceFacade.createDataSource(getServiceContextTecnicoProduccion(), INDICATOR_1, dataSourceDto);
        indicatorsServiceFacade.createDataSource(getServiceContextTecnicoApoyoProduccion(), INDICATOR_1, dataSourceDto);

        // Without access
        try {
            indicatorsServiceFacade.createDataSource(getServiceContextTecnicoSistemaIndicadores(), INDICATOR_1, dataSourceDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.createDataSource(getServiceContextTecnicoDifusion(), INDICATOR_1, dataSourceDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.createDataSource(getServiceContextTecnicoApoyoDifusion(), INDICATOR_1, dataSourceDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testUpdateDataSource() throws Exception {
        DataSourceDto dataSourceDto = indicatorsServiceFacade.retrieveDataSource(getServiceContextAdministrador(), DATA_SOURCE_1_INDICATOR_1_V2);

        // With access
        dataSourceDto = indicatorsServiceFacade.updateDataSource(getServiceContextTecnicoProduccion(), dataSourceDto);
        dataSourceDto = indicatorsServiceFacade.updateDataSource(getServiceContextTecnicoApoyoProduccion(), dataSourceDto);

        // Without access
        try {
            indicatorsServiceFacade.updateDataSource(getServiceContextTecnicoSistemaIndicadores(), dataSourceDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.updateDataSource(getServiceContextTecnicoDifusion(), dataSourceDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.updateDataSource(getServiceContextTecnicoApoyoDifusion(), dataSourceDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testRetrieveDataSource() throws Exception {

        // With access
        indicatorsServiceFacade.retrieveDataSource(getServiceContextTecnicoProduccion(), DATA_SOURCE_1_INDICATOR_1_V2);
        indicatorsServiceFacade.retrieveDataSource(getServiceContextTecnicoApoyoProduccion(), DATA_SOURCE_1_INDICATOR_1_V2);
        indicatorsServiceFacade.retrieveDataSource(getServiceContextTecnicoSistemaIndicadores(), DATA_SOURCE_1_INDICATOR_1_V2);
        indicatorsServiceFacade.retrieveDataSource(getServiceContextTecnicoDifusion(), DATA_SOURCE_1_INDICATOR_1_V2);
        indicatorsServiceFacade.retrieveDataSource(getServiceContextTecnicoApoyoDifusion(), DATA_SOURCE_1_INDICATOR_1_V2);
    }

    @Test
    public void testDeleteDataSource() throws Exception {
        indicatorsServiceFacade.deleteDataSource(getServiceContextTecnicoProduccion(), DATA_SOURCE_1_INDICATOR_1_V2);
    }

    @Test
    public void testDeleteDataSourceTecnicoApoyoProduccion() throws Exception {
        indicatorsServiceFacade.deleteDataSource(getServiceContextTecnicoApoyoProduccion(), DATA_SOURCE_1_INDICATOR_1_V2);
    }

    @Test
    public void testDeleteDataSourceErrorWithoutAccess() throws Exception {

        try {
            indicatorsServiceFacade.deleteDataSource(getServiceContextTecnicoSistemaIndicadores(), DATA_SOURCE_1_INDICATOR_1_V2);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.deleteDataSource(getServiceContextTecnicoDifusion(), DATA_SOURCE_1_INDICATOR_1_V2);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.deleteDataSource(getServiceContextTecnicoApoyoDifusion(), DATA_SOURCE_1_INDICATOR_1_V2);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testRetrieveDataSourcesByIndicator() throws Exception {
        indicatorsServiceFacade.retrieveDataSourcesByIndicator(getServiceContextAdministrador(), INDICATOR_1, "1.000");
        indicatorsServiceFacade.retrieveDataSourcesByIndicator(getServiceContextTecnicoProduccion(), INDICATOR_1, "1.000");
        indicatorsServiceFacade.retrieveDataSourcesByIndicator(getServiceContextTecnicoApoyoProduccion(), INDICATOR_1, "1.000");
        indicatorsServiceFacade.retrieveDataSourcesByIndicator(getServiceContextTecnicoSistemaIndicadores(), INDICATOR_1, "1.000");
        indicatorsServiceFacade.retrieveDataSourcesByIndicator(getServiceContextTecnicoDifusion(), INDICATOR_1, "1.000");
        indicatorsServiceFacade.retrieveDataSourcesByIndicator(getServiceContextTecnicoApoyoDifusion(), INDICATOR_1, "1.000");
    }

    @Test
    public void testRetrieveQuantityUnits() throws Exception {
        indicatorsServiceFacade.retrieveQuantityUnits(getServiceContextAdministrador());
        indicatorsServiceFacade.retrieveQuantityUnits(getServiceContextTecnicoSistemaIndicadores());
        indicatorsServiceFacade.retrieveQuantityUnits(getServiceContextTecnicoProduccion());
        indicatorsServiceFacade.retrieveQuantityUnits(getServiceContextTecnicoApoyoProduccion());
        indicatorsServiceFacade.retrieveQuantityUnits(getServiceContextTecnicoDifusion());
        indicatorsServiceFacade.retrieveQuantityUnits(getServiceContextTecnicoApoyoDifusion());
    }

    @Test
    public void testCreateQuantityUnit() throws Exception {
        QuantityUnitDto quantityUnitDto = IndicatorsMocks.mockQuantityUnit("es", "personas");

        // With access
        indicatorsServiceFacade.createQuantityUnit(getServiceContextAdministrador(), quantityUnitDto);

        // Without access
        try {
            indicatorsServiceFacade.createQuantityUnit(getServiceContextTecnicoSistemaIndicadores(), quantityUnitDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.createQuantityUnit(getServiceContextTecnicoProduccion(), quantityUnitDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.createQuantityUnit(getServiceContextTecnicoApoyoProduccion(), quantityUnitDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.createQuantityUnit(getServiceContextTecnicoDifusion(), quantityUnitDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.createQuantityUnit(getServiceContextTecnicoApoyoDifusion(), quantityUnitDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testUpdateQuantityUnit() throws Exception {
        QuantityUnit quantityUnit = indicatorsService.retrieveQuantityUnit(getServiceContextAdministrador(), QUANTITY_UNIT_1);
        QuantityUnitDto quantityUnitDto = do2DtoMapper.quantityUnitDoToDto(quantityUnit);

        // With access
        quantityUnitDto = indicatorsServiceFacade.updateQuantityUnit(getServiceContextAdministrador(), quantityUnitDto);

        // Without access
        try {
            indicatorsServiceFacade.updateQuantityUnit(getServiceContextTecnicoSistemaIndicadores(), quantityUnitDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.updateQuantityUnit(getServiceContextTecnicoProduccion(), quantityUnitDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.updateQuantityUnit(getServiceContextTecnicoApoyoProduccion(), quantityUnitDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.updateQuantityUnit(getServiceContextTecnicoDifusion(), quantityUnitDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.updateQuantityUnit(getServiceContextTecnicoApoyoDifusion(), quantityUnitDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testDeleteQuantityUnit() throws Exception {
        indicatorsServiceFacade.deleteQuantityUnit(getServiceContextAdministrador(), QUANTITY_UNIT_3);
    }

    @Test
    public void testDeleteQuantityUnitErrorWithoutRole() throws Exception {
        try {
            indicatorsServiceFacade.deleteQuantityUnit(getServiceContextTecnicoSistemaIndicadores(), QUANTITY_UNIT_1);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.deleteQuantityUnit(getServiceContextTecnicoSistemaIndicadoresOnlyAccessToIndicatorsSystem1(), QUANTITY_UNIT_1);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.deleteQuantityUnit(getServiceContextTecnicoProduccion(), QUANTITY_UNIT_1);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.deleteQuantityUnit(getServiceContextTecnicoApoyoProduccion(), QUANTITY_UNIT_1);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.deleteQuantityUnit(getServiceContextTecnicoDifusion(), QUANTITY_UNIT_1);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.deleteQuantityUnit(getServiceContextTecnicoApoyoDifusion(), QUANTITY_UNIT_1);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testRetrieveSubjects() throws Exception {
        indicatorsServiceFacade.retrieveSubjects(getServiceContextAdministrador());
        indicatorsServiceFacade.retrieveSubjects(getServiceContextTecnicoSistemaIndicadores());
        indicatorsServiceFacade.retrieveSubjects(getServiceContextTecnicoProduccion());
        indicatorsServiceFacade.retrieveSubjects(getServiceContextTecnicoApoyoProduccion());
        indicatorsServiceFacade.retrieveSubjects(getServiceContextTecnicoDifusion());
        indicatorsServiceFacade.retrieveSubjects(getServiceContextTecnicoApoyoDifusion());

    }

    @Test
    public void testRetrieveUnitMultipliers() throws Exception {
        indicatorsServiceFacade.retrieveUnitsMultipliers(getServiceContextAdministrador());
        indicatorsServiceFacade.retrieveUnitsMultipliers(getServiceContextTecnicoSistemaIndicadores());
        indicatorsServiceFacade.retrieveUnitsMultipliers(getServiceContextTecnicoProduccion());
        indicatorsServiceFacade.retrieveUnitsMultipliers(getServiceContextTecnicoApoyoProduccion());
        indicatorsServiceFacade.retrieveUnitsMultipliers(getServiceContextTecnicoDifusion());
        indicatorsServiceFacade.retrieveUnitsMultipliers(getServiceContextTecnicoApoyoDifusion());
    }

    @Test
    public void testCreateUnitMultiplier() throws Exception {
        UnitMultiplierDto unitMultiplierDto = IndicatorsMocks.mockUnitMultiplier(2);

        // With access
        indicatorsServiceFacade.createUnitMultiplier(getServiceContextAdministrador(), unitMultiplierDto);

        // Without access
        try {
            indicatorsServiceFacade.createUnitMultiplier(getServiceContextTecnicoSistemaIndicadores(), unitMultiplierDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.createUnitMultiplier(getServiceContextTecnicoProduccion(), unitMultiplierDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.createUnitMultiplier(getServiceContextTecnicoApoyoProduccion(), unitMultiplierDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.createUnitMultiplier(getServiceContextTecnicoDifusion(), unitMultiplierDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.createUnitMultiplier(getServiceContextTecnicoApoyoDifusion(), unitMultiplierDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testUpdateUnitMultiplier() throws Exception {
        UnitMultiplier unitMultiplier = indicatorsService.retrieveUnitMultiplier(getServiceContextAdministrador(), UNIT_MULTIPLIER_1);
        UnitMultiplierDto unitMultiplierDto = do2DtoMapper.unitMultiplierDoToDto(unitMultiplier);

        // With access
        unitMultiplierDto = indicatorsServiceFacade.updateUnitMultiplier(getServiceContextAdministrador(), unitMultiplierDto);

        // Without access
        try {
            indicatorsServiceFacade.updateUnitMultiplier(getServiceContextTecnicoSistemaIndicadores(), unitMultiplierDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.updateUnitMultiplier(getServiceContextTecnicoProduccion(), unitMultiplierDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.updateUnitMultiplier(getServiceContextTecnicoApoyoProduccion(), unitMultiplierDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.updateUnitMultiplier(getServiceContextTecnicoDifusion(), unitMultiplierDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.updateUnitMultiplier(getServiceContextTecnicoApoyoDifusion(), unitMultiplierDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testDeleteUnitMultiplier() throws Exception {
        indicatorsServiceFacade.deleteUnitMultiplier(getServiceContextAdministrador(), UNIT_MULTIPLIER_1);
    }

    @Test
    public void testDeleteUnitMultiplierErrorWithoutRole() throws Exception {
        try {
            indicatorsServiceFacade.deleteUnitMultiplier(getServiceContextTecnicoSistemaIndicadores(), UNIT_MULTIPLIER_1);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.deleteUnitMultiplier(getServiceContextTecnicoSistemaIndicadoresOnlyAccessToIndicatorsSystem1(), UNIT_MULTIPLIER_1);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.deleteUnitMultiplier(getServiceContextTecnicoProduccion(), UNIT_MULTIPLIER_1);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.deleteUnitMultiplier(getServiceContextTecnicoApoyoProduccion(), UNIT_MULTIPLIER_1);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.deleteUnitMultiplier(getServiceContextTecnicoDifusion(), UNIT_MULTIPLIER_1);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.deleteUnitMultiplier(getServiceContextTecnicoApoyoDifusion(), UNIT_MULTIPLIER_1);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/IndicatorsServiceFacadeIndicatorsTest.xml";
    }
}
