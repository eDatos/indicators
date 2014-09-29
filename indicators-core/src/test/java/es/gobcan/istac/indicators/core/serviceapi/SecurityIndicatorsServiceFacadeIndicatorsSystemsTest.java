package es.gobcan.istac.indicators.core.serviceapi;

import java.util.Arrays;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.sso.client.MetamacPrincipalAccess;
import org.siemac.metamac.sso.client.SsoClientConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.gobcan.istac.indicators.core.constants.IndicatorsConstants;
import es.gobcan.istac.indicators.core.dto.DimensionDto;
import es.gobcan.istac.indicators.core.dto.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.dto.GeographicalValueBaseDto;
import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;
import es.gobcan.istac.indicators.core.dto.IndicatorInstanceDto;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.enume.domain.RoleEnum;
import es.gobcan.istac.indicators.core.enume.domain.VersionTypeEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.security.SecurityUtils;
import es.gobcan.istac.indicators.core.serviceapi.utils.IndicatorsMocks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Security Role Test
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/include/indicators-service-mockito.xml", "classpath:spring/applicationContext-test.xml"})
@TransactionConfiguration(defaultRollback = true, transactionManager = "txManager")
@Transactional
public class SecurityIndicatorsServiceFacadeIndicatorsSystemsTest extends IndicatorsBaseTest {

    @Autowired
    protected IndicatorsServiceFacade indicatorsServiceFacade;

    @Test
    public void testErrorPrincipalNotFound() throws Exception {

        try {
            ServiceContext ctx = getServiceContextAdministrador();
            ctx.setProperty(SsoClientConstants.PRINCIPAL_ATTRIBUTE, null);
            indicatorsServiceFacade.createIndicatorsSystem(ctx, null);
            fail("principal required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_PRINCIPAL_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testCreateIndicatorsSystem() throws Exception {

        IndicatorsSystemDto indicatorsSystemDto = new IndicatorsSystemDto();

        // With access
        indicatorsSystemDto.setCode(IndicatorsMocks.mockString(10));
        indicatorsServiceFacade.createIndicatorsSystem(getServiceContextTecnicoSistemaIndicadores(), indicatorsSystemDto);

        // Without access
        try {
            indicatorsSystemDto.setCode(IndicatorsMocks.mockString(10));
            indicatorsServiceFacade.createIndicatorsSystem(getServiceContextTecnicoProduccion(), indicatorsSystemDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsSystemDto.setCode(IndicatorsMocks.mockString(10));
            indicatorsServiceFacade.createIndicatorsSystem(getServiceContextTecnicoApoyoProduccion(), indicatorsSystemDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsSystemDto.setCode(IndicatorsMocks.mockString(10));
            indicatorsServiceFacade.createIndicatorsSystem(getServiceContextTecnicoDifusion(), indicatorsSystemDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsSystemDto.setCode(IndicatorsMocks.mockString(10));
            indicatorsServiceFacade.createIndicatorsSystem(getServiceContextTecnicoApoyoDifusion(), indicatorsSystemDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testCreateIndicatorsSystemWithAccessOnlyToIndicatorsSystem1() throws Exception {

        IndicatorsSystemDto indicatorsSystemDto = new IndicatorsSystemDto();
        indicatorsSystemDto.setCode(IndicatorsMocks.mockString(10));

        ServiceContext ctx = getServiceContextTecnicoSistemaIndicadoresOnlyAccessToIndicatorsSystem1();
        SecurityUtils.getMetamacPrincipal(ctx).getAccesses()
                .add(new MetamacPrincipalAccess(RoleEnum.TECNICO_SISTEMA_INDICADORES.getName(), IndicatorsConstants.SECURITY_APPLICATION_ID, indicatorsSystemDto.getCode()));

        indicatorsServiceFacade.createIndicatorsSystem(ctx, indicatorsSystemDto);
    }

    @Test
    public void testCreateIndicatorsSystemErrorWithoutAccessToIndicatorsSystem1() throws Exception {

        IndicatorsSystemDto indicatorsSystemDto = new IndicatorsSystemDto();
        indicatorsSystemDto.setCode(IndicatorsMocks.mockString(10));
        try {
            indicatorsServiceFacade.createIndicatorsSystem(getServiceContextTecnicoSistemaIndicadoresOnlyAccessToIndicatorsSystem1(), indicatorsSystemDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_ACCESS_INDICATORS_SYSTEM_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testCreateIndicatorsSystemErrorWithoutAccessToIndicatorsSystem1InRequestedRole() throws Exception {

        IndicatorsSystemDto indicatorsSystemDto = new IndicatorsSystemDto();
        indicatorsSystemDto.setCode(IndicatorsMocks.mockString(10));

        ServiceContext ctx = getServiceContextTecnicoSistemaIndicadores();
        SecurityUtils.getMetamacPrincipal(ctx).getAccesses().clear();
        SecurityUtils.getMetamacPrincipal(ctx).getAccesses().add(new MetamacPrincipalAccess(RoleEnum.TECNICO_SISTEMA_INDICADORES.getName(), IndicatorsConstants.SECURITY_APPLICATION_ID, "other"));
        SecurityUtils.getMetamacPrincipal(ctx).getAccesses()
                .add(new MetamacPrincipalAccess(RoleEnum.TECNICO_APOYO_DIFUSION.getName(), IndicatorsConstants.SECURITY_APPLICATION_ID, indicatorsSystemDto.getCode()));

        try {
            indicatorsServiceFacade.createIndicatorsSystem(ctx, indicatorsSystemDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_ACCESS_INDICATORS_SYSTEM_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testRetrieveIndicatorsSystemByCode() throws Exception {
        // With access
        indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextAdministrador(), INDICATORS_SYSTEM_1_CODE, null);
        indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextTecnicoSistemaIndicadores(), INDICATORS_SYSTEM_1_CODE, null);
        indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextTecnicoProduccion(), INDICATORS_SYSTEM_1_CODE, null);
        indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextTecnicoApoyoProduccion(), INDICATORS_SYSTEM_1_CODE, null);
        indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextTecnicoDifusion(), INDICATORS_SYSTEM_1_CODE, null);
        indicatorsServiceFacade.retrieveIndicatorsSystemByCode(getServiceContextTecnicoApoyoProduccion(), INDICATORS_SYSTEM_1_CODE, null);
    }

    @Test
    public void testDeleteIndicatorsSystem() throws Exception {
        indicatorsServiceFacade.deleteIndicatorsSystem(getServiceContextAdministrador(), INDICATORS_SYSTEM_2);
    }

    @Test
    public void testDeleteIndicatorsSystemRoleTecnicoSistemaIndicadores() throws Exception {
        indicatorsServiceFacade.deleteIndicatorsSystem(getServiceContextTecnicoSistemaIndicadores(), INDICATORS_SYSTEM_2);
    }

    @Test
    public void testDeleteIndicatorsSystemWithAccessOnlyToIndicatorsSystem2() throws Exception {
        indicatorsServiceFacade.deleteIndicatorsSystem(getServiceContextTecnicoSistemaIndicadoresOnlyAccessToIndicatorsSystem2(), INDICATORS_SYSTEM_2);
    }

    @Test
    public void testDeleteIndicatorsSystemErrorWithoutRole() throws Exception {
        try {
            indicatorsServiceFacade.deleteIndicatorsSystem(getServiceContextTecnicoProduccion(), INDICATORS_SYSTEM_2);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.deleteIndicatorsSystem(getServiceContextTecnicoApoyoProduccion(), INDICATORS_SYSTEM_2);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.deleteIndicatorsSystem(getServiceContextTecnicoDifusion(), INDICATORS_SYSTEM_2);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.deleteIndicatorsSystem(getServiceContextTecnicoApoyoDifusion(), INDICATORS_SYSTEM_2);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testDeleteIndicatorsSystemErrorWithoutAccessToIndicatorSystem1() throws Exception {
        try {
            indicatorsServiceFacade.deleteIndicatorsSystem(getServiceContextTecnicoSistemaIndicadoresOnlyAccessToIndicatorsSystem1(), INDICATORS_SYSTEM_2);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_ACCESS_INDICATORS_SYSTEM_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testSendIndicatorsSystemToProductionValidation() throws Exception {
        indicatorsServiceFacade.sendIndicatorsSystemToProductionValidation(getServiceContextAdministrador(), INDICATORS_SYSTEM_1);
    }

    @Test
    public void testSendIndicatorsSystemToProductionValidationTecnicoSistemaIndicadores() throws Exception {
        indicatorsServiceFacade.sendIndicatorsSystemToProductionValidation(getServiceContextTecnicoSistemaIndicadores(), INDICATORS_SYSTEM_1);
    }

    @Test
    public void testSendIndicatorsSystemToProductionValidationWithAccessOnlyToIndicatorSystem1() throws Exception {
        indicatorsServiceFacade.sendIndicatorsSystemToProductionValidation(getServiceContextTecnicoSistemaIndicadoresOnlyAccessToIndicatorsSystem1(), INDICATORS_SYSTEM_1);
    }

    @Test
    public void testSendIndicatorsSystemToProductionValidationErrorWithoutRole() throws Exception {

        try {
            indicatorsServiceFacade.sendIndicatorsSystemToProductionValidation(getServiceContextTecnicoProduccion(), INDICATORS_SYSTEM_1);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.sendIndicatorsSystemToProductionValidation(getServiceContextTecnicoApoyoProduccion(), INDICATORS_SYSTEM_1);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.sendIndicatorsSystemToProductionValidation(getServiceContextTecnicoDifusion(), INDICATORS_SYSTEM_1);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.sendIndicatorsSystemToProductionValidation(getServiceContextTecnicoApoyoDifusion(), INDICATORS_SYSTEM_1);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testSendIndicatorsSystemToProductionValidationErrorWithoutAccessToIndicatorsSystem1() throws Exception {

        try {
            indicatorsServiceFacade.sendIndicatorsSystemToProductionValidation(getServiceContextTecnicoSistemaIndicadoresOnlyAccessToIndicatorsSystem2(), INDICATORS_SYSTEM_1);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_ACCESS_INDICATORS_SYSTEM_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testRejectIndicatorsSystemProductionValidation() throws Exception {
        indicatorsServiceFacade.rejectIndicatorsSystemProductionValidation(getServiceContextAdministrador(), INDICATORS_SYSTEM_4);
    }

    @Test
    public void testRejectIndicatorsSystemProductionValidationWithAccessToIndicatorSystem4() throws Exception {
        // Without access
        try {
            ServiceContext ctx = getServiceContextTecnicoDifusion();
            SecurityUtils.getMetamacPrincipal(ctx).getAccesses().clear();
            SecurityUtils.getMetamacPrincipal(ctx).getAccesses().add(new MetamacPrincipalAccess(RoleEnum.TECNICO_PRODUCCION.getName(), IndicatorsConstants.SECURITY_APPLICATION_ID, "CODE-4"));
            indicatorsServiceFacade.rejectIndicatorsSystemProductionValidation(ctx, INDICATORS_SYSTEM_4);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testRejectIndicatorsSystemProductionValidationErrorWithoutRole() throws Exception {

        // Without access
        try {
            indicatorsServiceFacade.rejectIndicatorsSystemProductionValidation(getServiceContextTecnicoProduccion(), INDICATORS_SYSTEM_4);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }

        try {
            indicatorsServiceFacade.rejectIndicatorsSystemProductionValidation(getServiceContextTecnicoSistemaIndicadores(), INDICATORS_SYSTEM_4);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.rejectIndicatorsSystemProductionValidation(getServiceContextTecnicoApoyoProduccion(), INDICATORS_SYSTEM_4);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.rejectIndicatorsSystemProductionValidation(getServiceContextTecnicoDifusion(), INDICATORS_SYSTEM_4);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.rejectIndicatorsSystemProductionValidation(getServiceContextTecnicoApoyoDifusion(), INDICATORS_SYSTEM_4);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testSendIndicatorsSystemToDiffusionValidation() throws Exception {

        // With access
        indicatorsServiceFacade.sendIndicatorsSystemToDiffusionValidation(getServiceContextAdministrador(), INDICATORS_SYSTEM_4);
    }

    @Test
    public void testSendIndicatorsSystemToDiffusionValidationErrorWithoutRole() throws Exception {

        // Without access
        try {
            indicatorsServiceFacade.sendIndicatorsSystemToDiffusionValidation(getServiceContextTecnicoSistemaIndicadores(), INDICATORS_SYSTEM_4);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }

        try {
            indicatorsServiceFacade.sendIndicatorsSystemToDiffusionValidation(getServiceContextTecnicoProduccion(), INDICATORS_SYSTEM_4);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.sendIndicatorsSystemToDiffusionValidation(getServiceContextTecnicoApoyoProduccion(), INDICATORS_SYSTEM_4);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.sendIndicatorsSystemToDiffusionValidation(getServiceContextTecnicoDifusion(), INDICATORS_SYSTEM_4);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.sendIndicatorsSystemToDiffusionValidation(getServiceContextTecnicoApoyoDifusion(), INDICATORS_SYSTEM_4);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testSendIndicatorsSystemToDiffusionValidationErrorWithoutAccessToIndicatorsSystem1() throws Exception {
        try {
            indicatorsServiceFacade.sendIndicatorsSystemToDiffusionValidation(getServiceContextTecnicoSistemaIndicadoresOnlyAccessToIndicatorsSystem2(), INDICATORS_SYSTEM_1);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testRejectIndicatorsSystemDiffusionValidation() throws Exception {
        indicatorsServiceFacade.rejectIndicatorsSystemDiffusionValidation(getServiceContextTecnicoDifusion(), INDICATORS_SYSTEM_5);
    }

    @Test
    public void testRejectIndicatorsSystemDiffusionValidationErrorWithoutRole() throws Exception {

        // Without access
        try {
            indicatorsServiceFacade.rejectIndicatorsSystemDiffusionValidation(getServiceContextTecnicoSistemaIndicadores(), INDICATORS_SYSTEM_5);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.rejectIndicatorsSystemDiffusionValidation(getServiceContextTecnicoProduccion(), INDICATORS_SYSTEM_5);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.rejectIndicatorsSystemDiffusionValidation(getServiceContextTecnicoApoyoProduccion(), INDICATORS_SYSTEM_5);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.rejectIndicatorsSystemDiffusionValidation(getServiceContextTecnicoApoyoDifusion(), INDICATORS_SYSTEM_5);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testPublishIndicatorsSystem() throws Exception {
        indicatorsServiceFacade.publishIndicatorsSystem(getServiceContextTecnicoDifusion(), INDICATORS_SYSTEM_5);
    }

    @Test
    public void testPublishIndicatorsSystemTecnicoApoyoDifusion() throws Exception {
        indicatorsServiceFacade.publishIndicatorsSystem(getServiceContextTecnicoApoyoDifusion(), INDICATORS_SYSTEM_5);
    }

    @Test
    public void testPublishIndicatorsSystemWithAccessToIndicatorSystem5() throws Exception {
        ServiceContext ctx = getServiceContextTecnicoDifusion();
        SecurityUtils.getMetamacPrincipal(ctx).getAccesses().clear();
        SecurityUtils.getMetamacPrincipal(ctx).getAccesses().add(new MetamacPrincipalAccess(RoleEnum.TECNICO_DIFUSION.getName(), IndicatorsConstants.SECURITY_APPLICATION_ID, "CODE-5"));
        indicatorsServiceFacade.publishIndicatorsSystem(ctx, INDICATORS_SYSTEM_5);
    }

    @Test
    public void testPublishIndicatorsSystemErrorWithoutRole() throws Exception {
        // Without access
        try {
            indicatorsServiceFacade.publishIndicatorsSystem(getServiceContextTecnicoSistemaIndicadores(), INDICATORS_SYSTEM_5);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.publishIndicatorsSystem(getServiceContextTecnicoProduccion(), INDICATORS_SYSTEM_5);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.publishIndicatorsSystem(getServiceContextTecnicoApoyoProduccion(), INDICATORS_SYSTEM_5);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testPublishIndicatorsSystemErrorWithoutAccessToIndicatorsSystem5() throws Exception {

        ServiceContext ctx = getServiceContextTecnicoDifusion();
        SecurityUtils.getMetamacPrincipal(ctx).getAccesses().clear();
        SecurityUtils.getMetamacPrincipal(ctx).getAccesses().add(new MetamacPrincipalAccess(RoleEnum.TECNICO_DIFUSION.getName(), IndicatorsConstants.SECURITY_APPLICATION_ID, "other"));

        try {
            indicatorsServiceFacade.publishIndicatorsSystem(ctx, INDICATORS_SYSTEM_5);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_ACCESS_INDICATORS_SYSTEM_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testArchiveIndicatorsSystem() throws Exception {
        indicatorsServiceFacade.archiveIndicatorsSystem(getServiceContextTecnicoDifusion(), INDICATORS_SYSTEM_3);
    }

    @Test
    public void testArchiveIndicatorsSystemErrorWithoutRole() throws Exception {
        // Without access
        try {
            indicatorsServiceFacade.archiveIndicatorsSystem(getServiceContextTecnicoSistemaIndicadores(), INDICATORS_SYSTEM_3);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.archiveIndicatorsSystem(getServiceContextTecnicoProduccion(), INDICATORS_SYSTEM_3);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.archiveIndicatorsSystem(getServiceContextTecnicoApoyoProduccion(), INDICATORS_SYSTEM_3);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.archiveIndicatorsSystem(getServiceContextTecnicoApoyoDifusion(), INDICATORS_SYSTEM_3);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testVersioningIndicatorsSystem() throws Exception {
        indicatorsServiceFacade.versioningIndicatorsSystem(getServiceContextTecnicoSistemaIndicadores(), INDICATORS_SYSTEM_3, VersionTypeEnum.MAJOR);
    }

    @Test
    public void testVersioningIndicatorsSystemErrorWithoutRole() throws Exception {
        try {
            indicatorsServiceFacade.versioningIndicatorsSystem(getServiceContextTecnicoProduccion(), INDICATORS_SYSTEM_3, VersionTypeEnum.MAJOR);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.versioningIndicatorsSystem(getServiceContextTecnicoApoyoProduccion(), INDICATORS_SYSTEM_3, VersionTypeEnum.MAJOR);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.versioningIndicatorsSystem(getServiceContextTecnicoDifusion(), INDICATORS_SYSTEM_3, VersionTypeEnum.MAJOR);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.versioningIndicatorsSystem(getServiceContextTecnicoApoyoDifusion(), INDICATORS_SYSTEM_3, VersionTypeEnum.MAJOR);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testFindIndicatorsSystems() throws Exception {
        indicatorsServiceFacade.findIndicatorsSystems(getServiceContextAdministrador(), null);
        indicatorsServiceFacade.findIndicatorsSystems(getServiceContextTecnicoSistemaIndicadores(), null);
        indicatorsServiceFacade.findIndicatorsSystems(getServiceContextTecnicoProduccion(), null);
        indicatorsServiceFacade.findIndicatorsSystems(getServiceContextTecnicoApoyoProduccion(), null);
        indicatorsServiceFacade.findIndicatorsSystems(getServiceContextTecnicoDifusion(), null);
        indicatorsServiceFacade.findIndicatorsSystems(getServiceContextTecnicoApoyoProduccion(), null);
    }

    @Test
    public void testRetrieveIndicatorsSystemStructure() throws Exception {
        indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContextAdministrador(), INDICATORS_SYSTEM_1, null);
        indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContextTecnicoSistemaIndicadores(), INDICATORS_SYSTEM_1, null);
        indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContextTecnicoProduccion(), INDICATORS_SYSTEM_1, null);
        indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContextTecnicoApoyoProduccion(), INDICATORS_SYSTEM_1, null);
        indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContextTecnicoDifusion(), INDICATORS_SYSTEM_1, null);
        indicatorsServiceFacade.retrieveIndicatorsSystemStructure(getServiceContextTecnicoApoyoDifusion(), INDICATORS_SYSTEM_1, null);
    }

    @Test
    public void testCreateDimension() throws Exception {

        DimensionDto dimensionDto = new DimensionDto();
        dimensionDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        dimensionDto.setParentUuid(null);
        dimensionDto.setOrderInLevel(Long.valueOf(1));
        String uuidIndicatorsSystem = INDICATORS_SYSTEM_1;

        // With access
        indicatorsServiceFacade.createDimension(getServiceContextAdministrador(), uuidIndicatorsSystem, dimensionDto);
        indicatorsServiceFacade.createDimension(getServiceContextTecnicoSistemaIndicadores(), uuidIndicatorsSystem, dimensionDto);

        // Without access
        try {
            indicatorsServiceFacade.createDimension(getServiceContextTecnicoProduccion(), uuidIndicatorsSystem, dimensionDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.createDimension(getServiceContextTecnicoApoyoProduccion(), uuidIndicatorsSystem, dimensionDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.createDimension(getServiceContextTecnicoDifusion(), uuidIndicatorsSystem, dimensionDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.createDimension(getServiceContextTecnicoApoyoDifusion(), uuidIndicatorsSystem, dimensionDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testCreateDimensionOnlyAccessToIndicatorsSystem1() throws Exception {

        DimensionDto dimensionDto = new DimensionDto();
        dimensionDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        dimensionDto.setParentUuid(null);
        dimensionDto.setOrderInLevel(Long.valueOf(1));
        String uuidIndicatorsSystem = INDICATORS_SYSTEM_1;

        // With access
        indicatorsServiceFacade.createDimension(getServiceContextTecnicoSistemaIndicadoresOnlyAccessToIndicatorsSystem1(), uuidIndicatorsSystem, dimensionDto);
    }

    @Test
    public void testCreateDimensionErrorOnlyAccessToIndicatorsSystem2() throws Exception {

        DimensionDto dimensionDto = new DimensionDto();
        dimensionDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        dimensionDto.setParentUuid(null);
        dimensionDto.setOrderInLevel(Long.valueOf(1));
        String uuidIndicatorsSystem = INDICATORS_SYSTEM_1;

        // Without access
        try {
            indicatorsServiceFacade.createDimension(getServiceContextTecnicoSistemaIndicadoresOnlyAccessToIndicatorsSystem2(), uuidIndicatorsSystem, dimensionDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_ACCESS_INDICATORS_SYSTEM_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testUpdateDimension() throws Exception {
        DimensionDto dimensionDto = indicatorsServiceFacade.retrieveDimension(getServiceContextAdministrador(), DIMENSION_1_INDICATORS_SYSTEM_1_V2);

        // With access
        dimensionDto = indicatorsServiceFacade.updateDimension(getServiceContextAdministrador(), dimensionDto);
        dimensionDto = indicatorsServiceFacade.updateDimension(getServiceContextTecnicoSistemaIndicadores(), dimensionDto);

        // Without access
        try {
            indicatorsServiceFacade.updateDimension(getServiceContextTecnicoProduccion(), dimensionDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.updateDimension(getServiceContextTecnicoApoyoProduccion(), dimensionDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.updateDimension(getServiceContextTecnicoDifusion(), dimensionDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.updateDimension(getServiceContextTecnicoApoyoDifusion(), dimensionDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testUpdateDimensionLocation() throws Exception {
        DimensionDto dimensionDto = indicatorsServiceFacade.retrieveDimension(getServiceContextAdministrador(), DIMENSION_1_INDICATORS_SYSTEM_1_V2);
        indicatorsServiceFacade.updateDimensionLocation(getServiceContextTecnicoSistemaIndicadores(), dimensionDto.getUuid(), dimensionDto.getParentUuid(), Long.valueOf(4));
    }

    @Test
    public void testUpdateDimensionLocationWithAccessOnlyToIndicatorsSystem1() throws Exception {
        DimensionDto dimensionDto = indicatorsServiceFacade.retrieveDimension(getServiceContextAdministrador(), DIMENSION_1_INDICATORS_SYSTEM_1_V2);
        indicatorsServiceFacade.updateDimensionLocation(getServiceContextTecnicoSistemaIndicadoresOnlyAccessToIndicatorsSystem1(), dimensionDto.getUuid(), dimensionDto.getParentUuid(),
                Long.valueOf(4));
    }

    @Test
    public void testUpdateDimensionLocationErrorWithoutRole() throws Exception {
        DimensionDto dimensionDto = indicatorsServiceFacade.retrieveDimension(getServiceContextAdministrador(), DIMENSION_1_INDICATORS_SYSTEM_1_V2);
        try {
            indicatorsServiceFacade.updateDimensionLocation(getServiceContextTecnicoProduccion(), dimensionDto.getUuid(), dimensionDto.getParentUuid(), Long.valueOf(4));
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.updateDimensionLocation(getServiceContextTecnicoApoyoProduccion(), dimensionDto.getUuid(), dimensionDto.getParentUuid(), Long.valueOf(4));
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.updateDimensionLocation(getServiceContextTecnicoDifusion(), dimensionDto.getUuid(), dimensionDto.getParentUuid(), Long.valueOf(4));
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.updateDimensionLocation(getServiceContextTecnicoApoyoDifusion(), dimensionDto.getUuid(), dimensionDto.getParentUuid(), Long.valueOf(4));
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testUpdateDimensionErrorWithoutAccessToIndicatorsSystem1() throws Exception {
        DimensionDto dimensionDto = indicatorsServiceFacade.retrieveDimension(getServiceContextAdministrador(), DIMENSION_1_INDICATORS_SYSTEM_1_V2);
        try {
            indicatorsServiceFacade.updateDimension(getServiceContextTecnicoSistemaIndicadoresOnlyAccessToIndicatorsSystem2(), dimensionDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_ACCESS_INDICATORS_SYSTEM_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testRetrieveDimension() throws Exception {
        indicatorsServiceFacade.retrieveDimension(getServiceContextAdministrador(), DIMENSION_1_INDICATORS_SYSTEM_1_V2);
        indicatorsServiceFacade.retrieveDimension(getServiceContextTecnicoSistemaIndicadores(), DIMENSION_1_INDICATORS_SYSTEM_1_V2);
        indicatorsServiceFacade.retrieveDimension(getServiceContextTecnicoProduccion(), DIMENSION_1_INDICATORS_SYSTEM_1_V2);
        indicatorsServiceFacade.retrieveDimension(getServiceContextTecnicoApoyoProduccion(), DIMENSION_1_INDICATORS_SYSTEM_1_V2);
        indicatorsServiceFacade.retrieveDimension(getServiceContextTecnicoDifusion(), DIMENSION_1_INDICATORS_SYSTEM_1_V2);
        indicatorsServiceFacade.retrieveDimension(getServiceContextTecnicoApoyoDifusion(), DIMENSION_1_INDICATORS_SYSTEM_1_V2);
    }

    @Test
    public void testDeleteDimension() throws Exception {
        indicatorsServiceFacade.deleteDimension(getServiceContextTecnicoSistemaIndicadores(), DIMENSION_1_INDICATORS_SYSTEM_1_V2);
    }

    @Test
    public void testDeleteDimensionOnlyAccessToIndicatorsSystem1() throws Exception {
        indicatorsServiceFacade.deleteDimension(getServiceContextTecnicoSistemaIndicadoresOnlyAccessToIndicatorsSystem1(), DIMENSION_1_INDICATORS_SYSTEM_1_V2);
    }

    @Test
    public void testDeleteDimensionErrorWithoutRole() throws Exception {

        try {
            indicatorsServiceFacade.deleteDimension(getServiceContextTecnicoProduccion(), DIMENSION_1_INDICATORS_SYSTEM_1_V2);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.deleteDimension(getServiceContextTecnicoApoyoProduccion(), DIMENSION_1_INDICATORS_SYSTEM_1_V2);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }

        try {
            indicatorsServiceFacade.deleteDimension(getServiceContextTecnicoDifusion(), DIMENSION_1_INDICATORS_SYSTEM_1_V2);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }

        try {
            indicatorsServiceFacade.deleteDimension(getServiceContextTecnicoApoyoDifusion(), DIMENSION_1_INDICATORS_SYSTEM_1_V2);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testDeleteDimensionErrorWithoutAccessToIndicatorsSystem1() throws Exception {

        try {
            indicatorsServiceFacade.deleteDimension(getServiceContextTecnicoSistemaIndicadoresOnlyAccessToIndicatorsSystem2(), DIMENSION_1_INDICATORS_SYSTEM_1_V2);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_ACCESS_INDICATORS_SYSTEM_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testCreateIndicatorInstance() throws Exception {
        GeographicalValueBaseDto geoValue = new GeographicalValueBaseDto();
        geoValue.setUuid(GEOGRAPHICAL_VALUE_1);

        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setIndicatorUuid(INDICATOR_2);
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorInstanceDto.setParentUuid(null);
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(5));
        indicatorInstanceDto.setGeographicalValues(Arrays.asList(geoValue));
        indicatorInstanceDto.setTimeValues(Arrays.asList("2012"));
        String uuidIndicatorsSystem = INDICATORS_SYSTEM_1;

        // With access
        indicatorsServiceFacade.createIndicatorInstance(getServiceContextTecnicoSistemaIndicadores(), uuidIndicatorsSystem, indicatorInstanceDto);

        // Without access
        try {
            indicatorsServiceFacade.createIndicatorInstance(getServiceContextTecnicoProduccion(), uuidIndicatorsSystem, indicatorInstanceDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.createIndicatorInstance(getServiceContextTecnicoApoyoProduccion(), uuidIndicatorsSystem, indicatorInstanceDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.createIndicatorInstance(getServiceContextTecnicoDifusion(), uuidIndicatorsSystem, indicatorInstanceDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.createIndicatorInstance(getServiceContextTecnicoApoyoDifusion(), uuidIndicatorsSystem, indicatorInstanceDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testCreateIndicatorInstanceOnlyAccessToIndicatorsSystem1() throws Exception {
        GeographicalValueBaseDto geoValue = new GeographicalValueBaseDto();
        geoValue.setUuid(GEOGRAPHICAL_VALUE_1);

        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setIndicatorUuid(INDICATOR_2);
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorInstanceDto.setParentUuid(null);
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(5));
        indicatorInstanceDto.setGeographicalValues(Arrays.asList(geoValue));
        indicatorInstanceDto.setTimeValues(Arrays.asList("2012"));
        String uuidIndicatorsSystem = INDICATORS_SYSTEM_1;

        // With access
        indicatorsServiceFacade.createIndicatorInstance(getServiceContextTecnicoSistemaIndicadoresOnlyAccessToIndicatorsSystem1(), uuidIndicatorsSystem, indicatorInstanceDto);
    }

    @Test
    public void testCreateIndicatorInstanceErrorOnlyWithoutAccessToIndicatorsSystem1() throws Exception {
        GeographicalValueBaseDto geoValue = new GeographicalValueBaseDto();
        geoValue.setUuid(GEOGRAPHICAL_VALUE_1);

        IndicatorInstanceDto indicatorInstanceDto = new IndicatorInstanceDto();
        indicatorInstanceDto.setIndicatorUuid(INDICATOR_2);
        indicatorInstanceDto.setTitle(IndicatorsMocks.mockInternationalStringDto());
        indicatorInstanceDto.setParentUuid(null);
        indicatorInstanceDto.setOrderInLevel(Long.valueOf(5));
        indicatorInstanceDto.setGeographicalValues(Arrays.asList(geoValue));
        indicatorInstanceDto.setTimeValues(Arrays.asList("2012"));
        String uuidIndicatorsSystem = INDICATORS_SYSTEM_1;

        // Without access
        try {
            indicatorsServiceFacade.createIndicatorInstance(getServiceContextTecnicoSistemaIndicadoresOnlyAccessToIndicatorsSystem2(), uuidIndicatorsSystem, indicatorInstanceDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_ACCESS_INDICATORS_SYSTEM_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testUpdateIndicatorInstance() throws Exception {
        IndicatorInstanceDto indicatorInstanceDto = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContextAdministrador(), INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2);

        // With access
        indicatorInstanceDto = indicatorsServiceFacade.updateIndicatorInstance(getServiceContextAdministrador(), indicatorInstanceDto);
        indicatorInstanceDto = indicatorsServiceFacade.updateIndicatorInstance(getServiceContextTecnicoSistemaIndicadores(), indicatorInstanceDto);

        // Without access
        try {
            indicatorsServiceFacade.updateIndicatorInstance(getServiceContextTecnicoProduccion(), indicatorInstanceDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.updateIndicatorInstance(getServiceContextTecnicoApoyoProduccion(), indicatorInstanceDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.updateIndicatorInstance(getServiceContextTecnicoDifusion(), indicatorInstanceDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.updateIndicatorInstance(getServiceContextTecnicoApoyoDifusion(), indicatorInstanceDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testUpdateIndicatorInstanceErrorWithoutAccessToIndicatorsSystem1() throws Exception {
        IndicatorInstanceDto indicatorInstanceDto = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContextAdministrador(), INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2);
        try {
            indicatorsServiceFacade.updateIndicatorInstance(getServiceContextTecnicoSistemaIndicadoresOnlyAccessToIndicatorsSystem2(), indicatorInstanceDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_ACCESS_INDICATORS_SYSTEM_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testUpdateIndicatorInstanceLocation() throws Exception {
        IndicatorInstanceDto indicatorInstanceDto = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContextAdministrador(), INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2);
        indicatorsServiceFacade.updateIndicatorInstanceLocation(getServiceContextTecnicoSistemaIndicadores(), indicatorInstanceDto.getUuid(), indicatorInstanceDto.getParentUuid(), Long.valueOf(4));
    }

    @Test
    public void testUpdateIndicatorInstanceLocationOnlyAccessToIndicatorsSystem1() throws Exception {
        IndicatorInstanceDto indicatorInstanceDto = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContextAdministrador(), INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2);
        indicatorsServiceFacade.updateIndicatorInstanceLocation(getServiceContextTecnicoSistemaIndicadoresOnlyAccessToIndicatorsSystem1(), indicatorInstanceDto.getUuid(),
                indicatorInstanceDto.getParentUuid(), Long.valueOf(4));
    }

    @Test
    public void testUpdateIndicatorInstanceLocationErrorWithoutAccessToIndicatorsSystem1() throws Exception {
        IndicatorInstanceDto indicatorInstanceDto = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContextAdministrador(), INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2);
        try {
            indicatorsServiceFacade.updateIndicatorInstanceLocation(getServiceContextTecnicoSistemaIndicadoresOnlyAccessToIndicatorsSystem2(), indicatorInstanceDto.getUuid(),
                    indicatorInstanceDto.getParentUuid(), Long.valueOf(4));
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_ACCESS_INDICATORS_SYSTEM_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testUpdateIndicatorInstanceLocationErrorWithoutRole() throws Exception {
        IndicatorInstanceDto indicatorInstanceDto = indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContextAdministrador(), INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2);
        try {
            indicatorsServiceFacade.updateIndicatorInstanceLocation(getServiceContextTecnicoProduccion(), indicatorInstanceDto.getUuid(), indicatorInstanceDto.getParentUuid(), Long.valueOf(4));
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.updateIndicatorInstanceLocation(getServiceContextTecnicoApoyoProduccion(), indicatorInstanceDto.getUuid(), indicatorInstanceDto.getParentUuid(), Long.valueOf(4));
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.updateIndicatorInstanceLocation(getServiceContextTecnicoDifusion(), indicatorInstanceDto.getUuid(), indicatorInstanceDto.getParentUuid(), Long.valueOf(4));
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.updateIndicatorInstanceLocation(getServiceContextTecnicoApoyoDifusion(), indicatorInstanceDto.getUuid(), indicatorInstanceDto.getParentUuid(), Long.valueOf(4));
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testRetrieveIndicatorInstance() throws Exception {
        indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContextAdministrador(), INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2);
        indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContextTecnicoSistemaIndicadores(), INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2);
        indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContextTecnicoProduccion(), INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2);
        indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContextTecnicoApoyoProduccion(), INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2);
        indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContextTecnicoDifusion(), INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2);
        indicatorsServiceFacade.retrieveIndicatorInstance(getServiceContextTecnicoApoyoDifusion(), INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2);
    }

    @Test
    public void testDeleteIndicatorInstance() throws Exception {
        indicatorsServiceFacade.deleteIndicatorInstance(getServiceContextTecnicoSistemaIndicadores(), INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2);
    }

    @Test
    public void testDeleteIndicatorInstanceOnlyAccessToIndicatorsSystem1() throws Exception {
        indicatorsServiceFacade.deleteIndicatorInstance(getServiceContextTecnicoSistemaIndicadoresOnlyAccessToIndicatorsSystem1(), INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2);
    }

    @Test
    public void testDeleteIndicatorInstanceErrorWithoutRole() throws Exception {

        try {
            indicatorsServiceFacade.deleteIndicatorInstance(getServiceContextTecnicoProduccion(), INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.deleteIndicatorInstance(getServiceContextTecnicoApoyoProduccion(), INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }

        try {
            indicatorsServiceFacade.deleteIndicatorInstance(getServiceContextTecnicoDifusion(), INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }

        try {
            indicatorsServiceFacade.deleteIndicatorInstance(getServiceContextTecnicoApoyoDifusion(), INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testDeleteIndicatorInstanceErrorWithoutAccessToIndicatorsSystem1() throws Exception {

        try {
            indicatorsServiceFacade.deleteIndicatorInstance(getServiceContextTecnicoSistemaIndicadoresOnlyAccessToIndicatorsSystem2(), INDICATOR_INSTANCE_1_INDICATORS_SYSTEM_1_V2);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_ACCESS_INDICATORS_SYSTEM_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testRetrieveGeographicalValue() throws Exception {
        indicatorsServiceFacade.retrieveGeographicalValue(getServiceContextAdministrador(), GEOGRAPHICAL_VALUE_1);
        indicatorsServiceFacade.retrieveGeographicalValue(getServiceContextTecnicoSistemaIndicadores(), GEOGRAPHICAL_VALUE_1);
        indicatorsServiceFacade.retrieveGeographicalValue(getServiceContextTecnicoProduccion(), GEOGRAPHICAL_VALUE_1);
        indicatorsServiceFacade.retrieveGeographicalValue(getServiceContextTecnicoApoyoProduccion(), GEOGRAPHICAL_VALUE_1);
        indicatorsServiceFacade.retrieveGeographicalValue(getServiceContextTecnicoDifusion(), GEOGRAPHICAL_VALUE_1);
        indicatorsServiceFacade.retrieveGeographicalValue(getServiceContextTecnicoApoyoDifusion(), GEOGRAPHICAL_VALUE_1);
    }

    @Test
    public void testFindGeographicalValues() throws Exception {
        indicatorsServiceFacade.findGeographicalValues(getServiceContextAdministrador(), null);
        indicatorsServiceFacade.findGeographicalValues(getServiceContextTecnicoSistemaIndicadores(), null);
        indicatorsServiceFacade.findGeographicalValues(getServiceContextTecnicoProduccion(), null);
        indicatorsServiceFacade.findGeographicalValues(getServiceContextTecnicoApoyoProduccion(), null);
        indicatorsServiceFacade.findGeographicalValues(getServiceContextTecnicoDifusion(), null);
        indicatorsServiceFacade.findGeographicalValues(getServiceContextTecnicoApoyoDifusion(), null);
    }

    @Test
    public void testCreateGeographicalValue() throws Exception {
        GeographicalValueDto geographicalValueDto = IndicatorsMocks.mockGeographicalValue(IndicatorsMocks.mockString(5), IndicatorsMocks.mockString(5), GEOGRAPHICAL_GRANULARITY_1);

        // With access
        geographicalValueDto.setCode(IndicatorsMocks.mockString(10));
        indicatorsServiceFacade.createGeographicalValue(getServiceContextAdministrador(), geographicalValueDto);

        // Without access
        try {
            geographicalValueDto.setCode(IndicatorsMocks.mockString(10));
            indicatorsServiceFacade.createGeographicalValue(getServiceContextTecnicoSistemaIndicadores(), geographicalValueDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            geographicalValueDto.setCode(IndicatorsMocks.mockString(10));
            indicatorsServiceFacade.createGeographicalValue(getServiceContextTecnicoProduccion(), geographicalValueDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            geographicalValueDto.setCode(IndicatorsMocks.mockString(10));
            indicatorsServiceFacade.createGeographicalValue(getServiceContextTecnicoApoyoProduccion(), geographicalValueDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            geographicalValueDto.setCode(IndicatorsMocks.mockString(10));
            indicatorsServiceFacade.createGeographicalValue(getServiceContextTecnicoDifusion(), geographicalValueDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            geographicalValueDto.setCode(IndicatorsMocks.mockString(10));
            indicatorsServiceFacade.createGeographicalValue(getServiceContextTecnicoApoyoDifusion(), geographicalValueDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testUpdateGeographicalValue() throws Exception {
        GeographicalValueDto geographicalValueDto = indicatorsServiceFacade.retrieveGeographicalValue(getServiceContextAdministrador(), GEOGRAPHICAL_VALUE_1);

        // With access
        geographicalValueDto = indicatorsServiceFacade.updateGeographicalValue(getServiceContextAdministrador(), geographicalValueDto);

        // Without access
        try {
            indicatorsServiceFacade.updateGeographicalValue(getServiceContextTecnicoSistemaIndicadores(), geographicalValueDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.updateGeographicalValue(getServiceContextTecnicoProduccion(), geographicalValueDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.updateGeographicalValue(getServiceContextTecnicoApoyoProduccion(), geographicalValueDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.updateGeographicalValue(getServiceContextTecnicoDifusion(), geographicalValueDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.updateGeographicalValue(getServiceContextTecnicoApoyoDifusion(), geographicalValueDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testDeleteGeographicalValue() throws Exception {
        indicatorsServiceFacade.deleteGeographicalValue(getServiceContextAdministrador(), GEOGRAPHICAL_VALUE_2);
    }

    @Test
    public void testDeleteGeographicalValueErrorWithoutRole() throws Exception {
        try {
            indicatorsServiceFacade.deleteGeographicalValue(getServiceContextTecnicoSistemaIndicadores(), GEOGRAPHICAL_VALUE_1);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.deleteGeographicalValue(getServiceContextTecnicoSistemaIndicadoresOnlyAccessToIndicatorsSystem1(), GEOGRAPHICAL_VALUE_1);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.deleteGeographicalValue(getServiceContextTecnicoProduccion(), GEOGRAPHICAL_VALUE_1);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.deleteGeographicalValue(getServiceContextTecnicoApoyoProduccion(), GEOGRAPHICAL_VALUE_1);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.deleteGeographicalValue(getServiceContextTecnicoDifusion(), GEOGRAPHICAL_VALUE_1);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.deleteGeographicalValue(getServiceContextTecnicoApoyoDifusion(), GEOGRAPHICAL_VALUE_1);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testRetrieveGeographicalGranularity() throws Exception {
        indicatorsServiceFacade.retrieveGeographicalGranularity(getServiceContextAdministrador(), GEOGRAPHICAL_GRANULARITY_2);
        indicatorsServiceFacade.retrieveGeographicalGranularity(getServiceContextTecnicoSistemaIndicadores(), GEOGRAPHICAL_GRANULARITY_2);
        indicatorsServiceFacade.retrieveGeographicalGranularity(getServiceContextTecnicoProduccion(), GEOGRAPHICAL_GRANULARITY_2);
        indicatorsServiceFacade.retrieveGeographicalGranularity(getServiceContextTecnicoApoyoProduccion(), GEOGRAPHICAL_GRANULARITY_2);
        indicatorsServiceFacade.retrieveGeographicalGranularity(getServiceContextTecnicoDifusion(), GEOGRAPHICAL_GRANULARITY_2);
        indicatorsServiceFacade.retrieveGeographicalGranularity(getServiceContextTecnicoApoyoDifusion(), GEOGRAPHICAL_GRANULARITY_2);
    }

    @Test
    public void testRetrieveGeographicalGranularities() throws Exception {
        indicatorsServiceFacade.retrieveGeographicalGranularities(getServiceContextAdministrador());
        indicatorsServiceFacade.retrieveGeographicalGranularities(getServiceContextTecnicoSistemaIndicadores());
        indicatorsServiceFacade.retrieveGeographicalGranularities(getServiceContextTecnicoProduccion());
        indicatorsServiceFacade.retrieveGeographicalGranularities(getServiceContextTecnicoApoyoProduccion());
        indicatorsServiceFacade.retrieveGeographicalGranularities(getServiceContextTecnicoDifusion());
        indicatorsServiceFacade.retrieveGeographicalGranularities(getServiceContextTecnicoApoyoDifusion());
    }

    @Test
    public void testCreateGeographicalGranularity() throws Exception {
        GeographicalGranularityDto geographicalGranularityDto = IndicatorsMocks.mockGeographicalGranularity(IndicatorsMocks.mockString(10));

        // With access
        geographicalGranularityDto.setCode(IndicatorsMocks.mockString(10));
        indicatorsServiceFacade.createGeographicalGranularity(getServiceContextAdministrador(), geographicalGranularityDto);

        // Without access
        try {
            geographicalGranularityDto.setCode(IndicatorsMocks.mockString(10));
            indicatorsServiceFacade.createGeographicalGranularity(getServiceContextTecnicoSistemaIndicadores(), geographicalGranularityDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            geographicalGranularityDto.setCode(IndicatorsMocks.mockString(10));
            indicatorsServiceFacade.createGeographicalGranularity(getServiceContextTecnicoProduccion(), geographicalGranularityDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            geographicalGranularityDto.setCode(IndicatorsMocks.mockString(10));
            indicatorsServiceFacade.createGeographicalGranularity(getServiceContextTecnicoApoyoProduccion(), geographicalGranularityDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            geographicalGranularityDto.setCode(IndicatorsMocks.mockString(10));
            indicatorsServiceFacade.createGeographicalGranularity(getServiceContextTecnicoDifusion(), geographicalGranularityDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            geographicalGranularityDto.setCode(IndicatorsMocks.mockString(10));
            indicatorsServiceFacade.createGeographicalGranularity(getServiceContextTecnicoApoyoDifusion(), geographicalGranularityDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testUpdateGeographicalGranularity() throws Exception {
        GeographicalGranularityDto geographicalGranularityDto = indicatorsServiceFacade.retrieveGeographicalGranularity(getServiceContextAdministrador(), GEOGRAPHICAL_GRANULARITY_1);

        // With access
        geographicalGranularityDto = indicatorsServiceFacade.updateGeographicalGranularity(getServiceContextAdministrador(), geographicalGranularityDto);

        // Without access
        try {
            indicatorsServiceFacade.updateGeographicalGranularity(getServiceContextTecnicoSistemaIndicadores(), geographicalGranularityDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.updateGeographicalGranularity(getServiceContextTecnicoProduccion(), geographicalGranularityDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.updateGeographicalGranularity(getServiceContextTecnicoApoyoProduccion(), geographicalGranularityDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.updateGeographicalGranularity(getServiceContextTecnicoDifusion(), geographicalGranularityDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.updateGeographicalGranularity(getServiceContextTecnicoApoyoDifusion(), geographicalGranularityDto);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testDeleteGeographicalGranularity() throws Exception {
        indicatorsServiceFacade.deleteGeographicalGranularity(getServiceContextAdministrador(), GEOGRAPHICAL_GRANULARITY_5);
    }

    @Test
    public void testDeleteGeographicalGranularityErrorWithoutRole() throws Exception {
        try {
            indicatorsServiceFacade.deleteGeographicalGranularity(getServiceContextTecnicoSistemaIndicadores(), GEOGRAPHICAL_GRANULARITY_1);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.deleteGeographicalGranularity(getServiceContextTecnicoSistemaIndicadoresOnlyAccessToIndicatorsSystem1(), GEOGRAPHICAL_GRANULARITY_1);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.deleteGeographicalGranularity(getServiceContextTecnicoProduccion(), GEOGRAPHICAL_GRANULARITY_1);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.deleteGeographicalGranularity(getServiceContextTecnicoApoyoProduccion(), GEOGRAPHICAL_GRANULARITY_1);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.deleteGeographicalGranularity(getServiceContextTecnicoDifusion(), GEOGRAPHICAL_GRANULARITY_1);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
        try {
            indicatorsServiceFacade.deleteGeographicalGranularity(getServiceContextTecnicoApoyoDifusion(), GEOGRAPHICAL_GRANULARITY_1);
            fail("without access");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/IndicatorsServiceFacadeIndicatorsSystemsTest.xml";
    }

}
