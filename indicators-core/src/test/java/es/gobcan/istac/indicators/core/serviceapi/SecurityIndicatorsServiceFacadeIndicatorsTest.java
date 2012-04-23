package es.gobcan.istac.indicators.core.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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

import es.gobcan.istac.indicators.core.constants.IndicatorsConstants;
import es.gobcan.istac.indicators.core.dto.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.QuantityDto;
import es.gobcan.istac.indicators.core.enume.domain.QuantityTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.RoleEnum;
import es.gobcan.istac.indicators.core.enume.domain.VersionTypeEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.serviceapi.utils.IndicatorsMocks;

/**
 * Security tester.
 * 
 * Don't test operations to "Data", because is for Any Role
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/include/indicators-service-mockito.xml", "classpath:spring/applicationContext-test.xml"})
public class SecurityIndicatorsServiceFacadeIndicatorsTest extends IndicatorsBaseTest {

    @Autowired
    protected IndicatorsServiceFacade indicatorsServiceFacade;
    
    private static String             NOT_EXISTS                   = "not-exists";

    // Indicators
    private static String             INDICATOR_1                  = "Indicator-1";
    private static String             INDICATOR_1_CODE             = "CODE-1";
    private static String             INDICATOR_3                  = "Indicator-3";
    private static String             INDICATOR_4                  = "Indicator-4";
    private static String             INDICATOR_5                  = "Indicator-5";

    // Data sources
    private static String             DATA_SOURCE_1_INDICATOR_1_V2 = "Indicator-1-v2-DataSource-1";

    // Quantity units
    private static String             QUANTITY_UNIT_1              = "1";

    // Subjects
    private static String             SUBJECT_1                    = "1";
    
    @Test
    public void testErrorPrincipalNotFound() throws Exception {

        try {
            ServiceContext ctx = getServiceContext();
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
            ServiceContext ctx = getServiceContext();
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
        indicatorDto.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorDto.setAcronym(IndicatorsMocks.mockInternationalString());
        indicatorDto.setSubjectCode(SUBJECT_1);
        indicatorDto.setSubjectTitle(IndicatorsMocks.mockInternationalString(IndicatorsConstants.LOCALE_SPANISH, "Área temática 1"));
        indicatorDto.setComments(IndicatorsMocks.mockInternationalString());
        indicatorDto.setNotes(IndicatorsMocks.mockInternationalString());
        indicatorDto.setConceptDescription(IndicatorsMocks.mockInternationalString());
        indicatorDto.setQuantity(new QuantityDto());
        indicatorDto.getQuantity().setType(QuantityTypeEnum.QUANTITY);
        indicatorDto.getQuantity().setUnitUuid(QUANTITY_UNIT_1);
        indicatorDto.getQuantity().setUnitMultiplier(Integer.valueOf(123));

        // With access
        indicatorsServiceFacade.createIndicator(getServiceContextTecnicoProduccion(), indicatorDto);
        indicatorDto.setCode("code" + (new Date()).getTime());
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
        indicatorsServiceFacade.updateIndicator(getServiceContextTecnicoProduccion(), indicatorDto);
        indicatorsServiceFacade.updateIndicator(getServiceContextTecnicoApoyoProduccion(), indicatorDto);

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
    public void testRetrieveIndicatorPublished() throws Exception {
        indicatorsServiceFacade.retrieveIndicatorPublished(getServiceContextAdministrador(), INDICATOR_1);
        indicatorsServiceFacade.retrieveIndicatorPublished(getServiceContextTecnicoSistemaIndicadores(), INDICATOR_1);
        indicatorsServiceFacade.retrieveIndicatorPublished(getServiceContextTecnicoProduccion(), INDICATOR_1);
        indicatorsServiceFacade.retrieveIndicatorPublished(getServiceContextTecnicoApoyoProduccion(), INDICATOR_1);
        indicatorsServiceFacade.retrieveIndicatorPublished(getServiceContextTecnicoDifusion(), INDICATOR_1);
        indicatorsServiceFacade.retrieveIndicatorPublished(getServiceContextTecnicoApoyoDifusion(), INDICATOR_1);
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
    public void testRetrieveIndicatorPublishedByCode() throws Exception {
        indicatorsServiceFacade.retrieveIndicatorPublishedByCode(getServiceContextAdministrador(), INDICATOR_1_CODE);
        indicatorsServiceFacade.retrieveIndicatorPublishedByCode(getServiceContextTecnicoSistemaIndicadores(), INDICATOR_1_CODE);
        indicatorsServiceFacade.retrieveIndicatorPublishedByCode(getServiceContextTecnicoProduccion(), INDICATOR_1_CODE);
        indicatorsServiceFacade.retrieveIndicatorPublishedByCode(getServiceContextTecnicoApoyoProduccion(), INDICATOR_1_CODE);
        indicatorsServiceFacade.retrieveIndicatorPublishedByCode(getServiceContextTecnicoDifusion(), INDICATOR_1_CODE);
        indicatorsServiceFacade.retrieveIndicatorPublishedByCode(getServiceContextTecnicoApoyoDifusion(), INDICATOR_1_CODE);
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
    public void testFindIndicatorsPublished() throws Exception {
        indicatorsServiceFacade.findIndicatorsPublished(getServiceContextAdministrador(), null);
        indicatorsServiceFacade.findIndicatorsPublished(getServiceContextTecnicoSistemaIndicadores(), null);
        indicatorsServiceFacade.findIndicatorsPublished(getServiceContextTecnicoProduccion(), null);
        indicatorsServiceFacade.findIndicatorsPublished(getServiceContextTecnicoApoyoProduccion(), null);
        indicatorsServiceFacade.findIndicatorsPublished(getServiceContextTecnicoDifusion(), null);
        indicatorsServiceFacade.findIndicatorsPublished(getServiceContextTecnicoApoyoProduccion(), null);
    }

    @Test
    public void testCreateDataSource() throws Exception {
        DataSourceDto dataSourceDto = new DataSourceDto();
        dataSourceDto.setDataGpeUuid("queryGpe1");
        dataSourceDto.setPxUri("px1");
        dataSourceDto.setTimeVariable("timeVariable1");
        dataSourceDto.setGeographicalVariable("geographicalVariable1");
        dataSourceDto.setSourceSurveyCode("sourceSurveyCode");
        dataSourceDto.setSourceSurveyTitle(IndicatorsMocks.mockInternationalString());
        dataSourceDto.setSourceSurveyAcronym(IndicatorsMocks.mockInternationalString());
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
        DataSourceDto dataSourceDto = indicatorsServiceFacade.retrieveDataSource(getServiceContext(), DATA_SOURCE_1_INDICATOR_1_V2);

        // With access
        indicatorsServiceFacade.updateDataSource(getServiceContextTecnicoProduccion(), dataSourceDto);
        indicatorsServiceFacade.updateDataSource(getServiceContextTecnicoApoyoProduccion(), dataSourceDto);

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

        // Without access
        try {
            indicatorsServiceFacade.retrieveDataSource(getServiceContextTecnicoSistemaIndicadores(), DATA_SOURCE_1_INDICATOR_1_V2);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.retrieveDataSource(getServiceContextTecnicoDifusion(), DATA_SOURCE_1_INDICATOR_1_V2);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.retrieveDataSource(getServiceContextTecnicoApoyoDifusion(), DATA_SOURCE_1_INDICATOR_1_V2);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
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

        try {
            indicatorsServiceFacade.retrieveDataSourcesByIndicator(getServiceContextTecnicoSistemaIndicadores(), INDICATOR_1, "1.000");
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.retrieveDataSourcesByIndicator(getServiceContextTecnicoDifusion(), INDICATOR_1, "1.000");
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.retrieveDataSourcesByIndicator(getServiceContextTecnicoApoyoDifusion(), INDICATOR_1, "1.000");
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testRetrieveQuantityUnit() throws Exception {
        indicatorsServiceFacade.retrieveQuantityUnit(getServiceContextAdministrador(), QUANTITY_UNIT_1);
        indicatorsServiceFacade.retrieveQuantityUnit(getServiceContextTecnicoSistemaIndicadores(), QUANTITY_UNIT_1);
        indicatorsServiceFacade.retrieveQuantityUnit(getServiceContextTecnicoProduccion(), QUANTITY_UNIT_1);
        indicatorsServiceFacade.retrieveQuantityUnit(getServiceContextTecnicoApoyoProduccion(), QUANTITY_UNIT_1);
        indicatorsServiceFacade.retrieveQuantityUnit(getServiceContextTecnicoDifusion(), QUANTITY_UNIT_1);
        indicatorsServiceFacade.retrieveQuantityUnit(getServiceContextTecnicoApoyoDifusion(), QUANTITY_UNIT_1);
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
    public void testRetrieveSubject() throws Exception {
        indicatorsServiceFacade.retrieveSubject(getServiceContextAdministrador(), SUBJECT_1);
        indicatorsServiceFacade.retrieveSubject(getServiceContextTecnicoSistemaIndicadores(), SUBJECT_1);
        indicatorsServiceFacade.retrieveSubject(getServiceContextTecnicoProduccion(), SUBJECT_1);
        indicatorsServiceFacade.retrieveSubject(getServiceContextTecnicoApoyoProduccion(), SUBJECT_1);
        indicatorsServiceFacade.retrieveSubject(getServiceContextTecnicoDifusion(), SUBJECT_1);
        indicatorsServiceFacade.retrieveSubject(getServiceContextTecnicoApoyoDifusion(), SUBJECT_1);
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
    public void testRetrieveSubjectsInPublishedIndicators() throws Exception {
        indicatorsServiceFacade.retrieveSubjectsInPublishedIndicators(getServiceContextAdministrador());
        indicatorsServiceFacade.retrieveSubjectsInPublishedIndicators(getServiceContextTecnicoSistemaIndicadores());
        indicatorsServiceFacade.retrieveSubjectsInPublishedIndicators(getServiceContextTecnicoProduccion());
        indicatorsServiceFacade.retrieveSubjectsInPublishedIndicators(getServiceContextTecnicoApoyoProduccion());
        indicatorsServiceFacade.retrieveSubjectsInPublishedIndicators(getServiceContextTecnicoDifusion());
        indicatorsServiceFacade.retrieveSubjectsInPublishedIndicators(getServiceContextTecnicoApoyoDifusion());
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/IndicatorsServiceFacadeIndicatorsTest.xml";
    }    
}
