package es.gobcan.istac.indicators.core.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.gobcan.istac.indicators.core.constants.IndicatorsConstants;
import es.gobcan.istac.indicators.core.domain.Indicator;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorVersionProperties;
import es.gobcan.istac.indicators.core.domain.Quantity;
import es.gobcan.istac.indicators.core.domain.QuantityUnitRepository;
import es.gobcan.istac.indicators.core.domain.UnitMultiplierRepository;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorProcStatusEnum;
import es.gobcan.istac.indicators.core.enume.domain.QuantityTypeEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.serviceapi.utils.IndicatorsMocks;

/**
 * Test to IndicatorService. Testing: indicators, data sources...
 * Spring based transactional test with DbUnit support.
 * Only testing properties are not in Dto
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/include/indicators-notices-service-mockito.xml", "classpath:spring/include/indicators-service-mockito.xml",
        "classpath:spring/applicationContext-test.xml"})
@TransactionConfiguration(defaultRollback = true, transactionManager = "txManager")
@Transactional
public class IndicatorsServiceTest extends IndicatorsBaseTest {

    @Autowired
    protected IndicatorsService        indicatorService;

    @Autowired
    protected QuantityUnitRepository   quantityUnitRepository;

    @Autowired
    protected UnitMultiplierRepository unitMultiplierRepository;

    @Test
    public void testCreateIndicator() throws Exception {
        IndicatorVersion indicatorVersion = new IndicatorVersion();
        indicatorVersion.setIndicator(new Indicator());
        indicatorVersion.getIndicator().setCode("MYCODE");
        indicatorVersion.getIndicator().setViewCode("MYVIEWCODE");
        indicatorVersion.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorVersion.setSubjectCode(IndicatorsMocks.mockString(10));
        indicatorVersion.setSubjectTitle(IndicatorsMocks.mockInternationalString());
        indicatorVersion.setQuantity(new Quantity());
        indicatorVersion.getQuantity().setQuantityType(QuantityTypeEnum.AMOUNT);
        indicatorVersion.getQuantity().setUnit(quantityUnitRepository.retrieveQuantityUnit(QUANTITY_UNIT_1));
        indicatorVersion.getQuantity().setUnitMultiplier(unitMultiplierRepository.retrieveUnitMultiplier(Integer.valueOf(1)));

        // Create
        IndicatorVersion indicatorVersionCreated = indicatorService.createIndicator(getServiceContextAdministrador(), indicatorVersion);
        assertEquals(getServiceContextAdministrador().getUserId(), indicatorVersionCreated.getCreatedBy());
        assertEquals(getServiceContextAdministrador().getUserId(), indicatorVersionCreated.getLastUpdatedBy());

        // Validate properties are not in Dto
        String uuid = indicatorVersionCreated.getIndicator().getUuid();
        String version = indicatorVersionCreated.getVersionNumber();
        IndicatorVersion indicatorCreated = indicatorService.retrieveIndicator(getServiceContextAdministrador(), uuid, version);
        assertFalse(indicatorCreated.getIndicator().getIsPublished());
        assertTrue(indicatorVersionCreated.getIsLastVersion());
        assertEquals(indicatorCreated.getProcStatus(), indicatorCreated.getIndicator().getProductionProcStatus());
    }

    @Test
    public void testUpdateIndicator() throws Exception {

        IndicatorVersion indicatorVersion = new IndicatorVersion();
        indicatorVersion.setIndicator(new Indicator());
        indicatorVersion.getIndicator().setCode(("code" + (new Date()).getTime()));
        indicatorVersion.getIndicator().setViewCode(("viewCode" + (new Date()).getTime()));
        indicatorVersion.setTitle(IndicatorsMocks.mockInternationalString());
        indicatorVersion.setSubjectCode(IndicatorsMocks.mockString(10));
        indicatorVersion.setSubjectTitle(IndicatorsMocks.mockInternationalString());
        indicatorVersion.setQuantity(new Quantity());
        indicatorVersion.getQuantity().setQuantityType(QuantityTypeEnum.AMOUNT);
        indicatorVersion.getQuantity().setUnit(quantityUnitRepository.retrieveQuantityUnit(QUANTITY_UNIT_1));
        indicatorVersion.getQuantity().setUnitMultiplier(unitMultiplierRepository.retrieveUnitMultiplier(Integer.valueOf(1)));

        // Create
        IndicatorVersion indicatorVersionCreated = indicatorService.createIndicator(getServiceContextAdministrador(), indicatorVersion);
        assertEquals(getServiceContextAdministrador().getUserId(), indicatorVersionCreated.getCreatedBy());
        assertEquals(getServiceContextAdministrador().getUserId(), indicatorVersionCreated.getLastUpdatedBy());

        // Check after creation needsUpdate is false
        assertFalse(indicatorVersionCreated.getNeedsUpdate());

        indicatorVersionCreated.setNeedsUpdate(Boolean.TRUE);

        IndicatorVersion indicatorVersionUpdated = indicatorService.updateIndicatorVersion(getServiceContextAdministrador2(), indicatorVersionCreated);
        assertTrue(indicatorVersionUpdated.getNeedsUpdate());
        assertEquals(getServiceContextAdministrador().getUserId(), indicatorVersionUpdated.getCreatedBy());
        assertEquals(getServiceContextAdministrador2().getUserId(), indicatorVersionUpdated.getLastUpdatedBy());
    }

    @Test
    public void testDeleteIndicatorWithPublishedAndDraft() throws Exception {

        String uuid = INDICATOR_1;

        // Retrieve before delete
        {
            IndicatorVersion indicatorVersion1 = indicatorService.retrieveIndicator(getServiceContextAdministrador(), uuid, IndicatorsDataBaseTest.INIT_VERSION);
            assertFalse(indicatorVersion1.getIsLastVersion());
            IndicatorVersion indicatorVersion2 = indicatorService.retrieveIndicator(getServiceContextAdministrador(), uuid, IndicatorsDataBaseTest.SECOND_VERSION);
            assertTrue(indicatorVersion2.getIsLastVersion());
        }

        // Delete indicator
        indicatorService.deleteIndicator(getServiceContextAdministrador(), uuid);

        // Validation
        // Retrieve after delete
        {
            IndicatorVersion indicatorVersion1 = indicatorService.retrieveIndicator(getServiceContextAdministrador(), uuid, IndicatorsDataBaseTest.INIT_VERSION);
            assertTrue(indicatorVersion1.getIsLastVersion());
            assertNull(indicatorVersion1.getIndicator().getProductionIdIndicatorVersion());
            assertNull(indicatorVersion1.getIndicator().getProductionVersionNumber());
            assertNull(indicatorVersion1.getIndicator().getProductionProcStatus());
        }
    }

    @Test
    public void testVersioningIndicator() throws Exception {

        String uuid = INDICATOR_3;

        // Retrieve before versioning
        {
            IndicatorVersion indicatorVersion1 = indicatorService.retrieveIndicator(getServiceContextAdministrador(), uuid, INDICATOR_3_VERSION);
            assertTrue(indicatorVersion1.getIsLastVersion());
            assertTrue(StringUtils.isNotEmpty(indicatorVersion1.getDataRepositoryId()));
            assertFalse(indicatorVersion1.getNeedsUpdate());
        }

        // Versioning
        IndicatorVersion newVersion = indicatorService.versioningIndicator(getServiceContextAdministrador(), uuid, VersionTypeEnum.MAJOR);

        // Retrieve after delete
        {
            IndicatorVersion indicatorVersion1 = indicatorService.retrieveIndicator(getServiceContextAdministrador(), uuid, INDICATOR_3_VERSION);
            assertFalse(indicatorVersion1.getIsLastVersion());
            IndicatorVersion indicatorVersion2 = indicatorService.retrieveIndicator(getServiceContextAdministrador(), uuid, newVersion.getVersionNumber());
            assertTrue(indicatorVersion2.getIsLastVersion());
        }
    }

    @Test
    public void testVersioningIndicatorMaximumMinorVersionReached() throws Exception {

        String uuid = INDICATOR_14;

        // Retrieve before versioning
        {
            IndicatorVersion indicatorVersion1 = indicatorService.retrieveIndicator(getServiceContextAdministrador(), uuid, INDICATOR_14_VERSION);
            assertEquals(IndicatorsDataBaseTest.INIT_VERSION_MAXIMUM_MINOR_VERSION, indicatorVersion1.getVersionNumber());
        }

        // Mockito.doNothing().when(noticesRestInternalService).createMinorVersionExpectedMajorVersionOccurredBackgroundNotification(Matchers.anyString());

        // Versioning
        IndicatorVersion newVersion = indicatorService.versioningIndicator(getServiceContextAdministrador(), uuid, VersionTypeEnum.MINOR);

        // Retrieve after delete
        {
            IndicatorVersion indicatorVersion1 = indicatorService.retrieveIndicator(getServiceContextAdministrador(), uuid, INDICATOR_14_VERSION);
            assertEquals(IndicatorsDataBaseTest.INIT_VERSION_MAXIMUM_MINOR_VERSION, indicatorVersion1.getVersionNumber());
            IndicatorVersion indicatorVersion2 = indicatorService.retrieveIndicator(getServiceContextAdministrador(), uuid, newVersion.getVersionNumber());
            assertEquals(IndicatorsDataBaseTest.SECOND_VERSION, indicatorVersion2.getVersionNumber());
        }
    }

    @Test
    public void testVersioningIndicatorMinorVersionLimitReachedError() throws Exception {

        String uuid = INDICATOR_15;

        // Retrieve before versioning
        {
            IndicatorVersion indicatorVersion1 = indicatorService.retrieveIndicator(getServiceContextAdministrador(), uuid, INDICATOR_15_VERSION);
            assertEquals(IndicatorsDataBaseTest.MAXIMUM_LIMIT_VERSION, indicatorVersion1.getVersionNumber());
        }

        // Minor versioning
        expectedMetamacException(
                MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.RESOURCE_MAXIMUM_VERSION_REACHED).withMessageParameters(VersionTypeEnum.MINOR, INDICATOR_15_VERSION).build());

        indicatorService.versioningIndicator(getServiceContextAdministrador(), uuid, VersionTypeEnum.MINOR);
        fail("Should not allow minor versioning when the indicator system version number rearches 99999.9");
    }

    @Test
    public void testVersioningIndicatorMajorVersionLimitReachedError() throws Exception {

        String uuid = INDICATOR_15;

        // Retrieve before versioning
        {
            IndicatorVersion indicatorVersion1 = indicatorService.retrieveIndicator(getServiceContextAdministrador(), uuid, INDICATOR_15_VERSION);
            assertEquals(IndicatorsDataBaseTest.MAXIMUM_LIMIT_VERSION, indicatorVersion1.getVersionNumber());
        }

        // Major versioning
        expectedMetamacException(
                MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.RESOURCE_MAXIMUM_VERSION_REACHED).withMessageParameters(VersionTypeEnum.MAJOR, INDICATOR_15_VERSION).build());

        indicatorService.versioningIndicator(getServiceContextAdministrador(), uuid, VersionTypeEnum.MAJOR);
        fail("Should not allow major versioning when the indicator system version number rearches 99999.9");
    }

    @Test
    public void testPublishIndicator() throws Exception {

        String uuid = INDICATOR_5;
        String versionNumber = IndicatorsDataBaseTest.INIT_VERSION;

        // Publish
        indicatorService.publishIndicator(getServiceContextAdministrador(), uuid);

        // Validate properties are not in Dto
        IndicatorVersion indicatorCreated = indicatorService.retrieveIndicator(getServiceContextAdministrador(), uuid, versionNumber);
        assertTrue(indicatorCreated.getIndicator().getIsPublished());
    }

    @Test
    public void testPublishIndicatorSubjectTitleChange() throws Exception {

        String uuid = INDICATOR_13;
        String versionNumber = IndicatorsDataBaseTest.INIT_VERSION;

        String subjectTitleOld = indicatorService.retrieveIndicator(getServiceContextAdministrador(), uuid, versionNumber).getSubjectTitle().getLocalisedLabel(IndicatorsConstants.LOCALE_SPANISH);
        // Publish
        indicatorService.publishIndicator(getServiceContextAdministrador(), uuid);

        // Validate properties are not in Dto
        IndicatorVersion indicatorCreated = indicatorService.retrieveIndicator(getServiceContextAdministrador(), uuid, versionNumber);
        assertTrue(indicatorCreated.getIndicator().getIsPublished());
        assertFalse(subjectTitleOld.equals(indicatorCreated.getSubjectTitle().getLocalisedLabel(IndicatorsConstants.LOCALE_SPANISH)));
        assertEquals("Área temática 5", indicatorCreated.getSubjectTitle().getLocalisedLabel(IndicatorsConstants.LOCALE_SPANISH));
    }

    @Test
    public void testArchiveIndicator() throws Exception {

        String uuid = INDICATOR_3;
        String versionNumber = INDICATOR_3_VERSION;

        // Archive
        indicatorService.archiveIndicator(getServiceContextAdministrador(), uuid);

        // Validate properties are not in Dto
        IndicatorVersion indicatorCreated = indicatorService.retrieveIndicator(getServiceContextAdministrador(), uuid, versionNumber);
        assertFalse(indicatorCreated.getIndicator().getIsPublished());
        assertEquals(indicatorCreated.getIndicator().getDiffusionProcStatus(), IndicatorProcStatusEnum.ARCHIVED);
    }

    @Test
    public void testFindIndicatorsByCriteria() throws Exception {
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(IndicatorVersion.class).withProperty(IndicatorVersionProperties.lastValuesCache().geographicalCode()).eq("ES")
                    .orderBy(IndicatorVersionProperties.indicator().uuid()).ascending().build();

            PagingParameter paging = PagingParameter.pageAccess(10);

            PagedResult<IndicatorVersion> indicatorsVersion = indicatorService.findIndicatorsPublished(getServiceContextAdministrador(), conditions, paging);

            assertEquals(3, indicatorsVersion.getValues().size());

            assertEquals(INDICATOR_1, indicatorsVersion.getValues().get(0).getIndicator().getUuid());
            assertEquals(INDICATOR_12, indicatorsVersion.getValues().get(1).getIndicator().getUuid());
            assertEquals(INDICATOR_3, indicatorsVersion.getValues().get(2).getIndicator().getUuid());
        }
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(IndicatorVersion.class).withProperty(IndicatorVersionProperties.lastValuesCache().geographicalCode()).eq("FR")
                    .orderBy(IndicatorVersionProperties.indicator().uuid()).build();

            PagingParameter paging = PagingParameter.pageAccess(10);

            PagedResult<IndicatorVersion> indicatorsVersion = indicatorService.findIndicatorsPublished(getServiceContextAdministrador(), conditions, paging);

            assertEquals(2, indicatorsVersion.getValues().size());

            assertEquals(INDICATOR_1, indicatorsVersion.getValues().get(0).getIndicator().getUuid());
            assertEquals(INDICATOR_12, indicatorsVersion.getValues().get(1).getIndicator().getUuid());
        }
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(IndicatorVersion.class).withProperty(IndicatorVersionProperties.lastValuesCache().geographicalCode())
                    .eq("ES-MD").orderBy(IndicatorVersionProperties.indicator().uuid()).build();

            PagingParameter paging = PagingParameter.pageAccess(10);

            PagedResult<IndicatorVersion> indicatorsVersion = indicatorService.findIndicatorsPublished(getServiceContextAdministrador(), conditions, paging);

            assertEquals(1, indicatorsVersion.getValues().size());

            assertEquals(INDICATOR_1, indicatorsVersion.getValues().get(0).getIndicator().getUuid());
        }
    }

    @Test
    public void testExportIndicatorsTsv() throws Exception {

        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(IndicatorVersion.class).withProperty(IndicatorVersionProperties.lastValuesCache().geographicalCode()).eq("ES")
                    .orderBy(IndicatorVersionProperties.indicator().uuid()).ascending().build();

            String fileName = indicatorService.exportIndicatorsTsv(getServiceContextAdministrador(), conditions);
            assertNotNull(fileName);

            // Validate
            File file = new File(tempDirPath() + File.separatorChar + fileName);
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            assertEquals(
                    "code\tnotify_population_errors\tproduction-title#es\tproduction-title#en\tproduction-title#pt\tproduction-subject_title#es\tproduction-subject_title#en\tproduction-subject_title#pt\tproduction-version_number\tproduction-proc_status\tproduction-needs_update\tproduction-production_validation_date\tproduction-production_validation_user\tproduction-diffusion_validation_date\tproduction-diffusion_validation_user\tproduction-publication_date\tproduction-publication_user\tproduction-publication_failed_date\tproduction-publication_failed_user\tproduction-archive_date\tproduction-archive_user\tproduction-created_date\tproduction-created_by\tdiffusion-title#es\tdiffusion-title#en\tdiffusion-title#pt\tdiffusion-subject_title#es\tdiffusion-subject_title#en\tdiffusion-subject_title#pt\tdiffusion-version_number\tdiffusion-proc_status\tdiffusion-needs_update\tdiffusion-production_validation_date\tdiffusion-production_validation_user\tdiffusion-diffusion_validation_date\tdiffusion-diffusion_validation_user\tdiffusion-publication_date\tdiffusion-publication_user\tdiffusion-publication_failed_date\tdiffusion-publication_failed_user\tdiffusion-archive_date\tdiffusion-archive_user\tdiffusion-created_date\tdiffusion-created_by",
                    line);
            line = bufferedReader.readLine();
            assertEquals(
                    "CODE-12\ttrue\tTítulo\t\t\tÁrea temática 3\t\t\t1.0\tPUBLISHED\tfalse\t\t\t\t\t\t\t\t\t\t\t2011-01-01T01:02:04.000Z\tuser1\tTítulo\t\t\tÁrea temática 3\t\t\t1.0\tPUBLISHED\tfalse\t\t\t\t\t\t\t\t\t\t\t2011-01-01T01:02:04.000Z\tuser1",
                    line);
            line = bufferedReader.readLine();
            assertEquals(
                    "CODE-3\ttrue\tTítulo Indicator-3-v1 Educación\tTitle Indicator-3-v1 Education\t\tÁrea temática 3\t\t\t11.33\tPUBLISHED\tfalse\t2011-03-03T01:02:04.000Z\tuser1\t2011-04-04T03:02:04.000+01:00\tuser2\t2011-05-05T04:02:04.000+01:00\tuser3\t\t\t\t\t2011-01-01T01:02:04.000Z\tuser1\tTítulo Indicator-3-v1 Educación\tTitle Indicator-3-v1 Education\t\tÁrea temática 3\t\t\t11.33\tPUBLISHED\tfalse\t2011-03-03T01:02:04.000Z\tuser1\t2011-04-04T03:02:04.000+01:00\tuser2\t2011-05-05T04:02:04.000+01:00\tuser3\t\t\t\t\t2011-01-01T01:02:04.000Z\tuser1",
                    line);

            bufferedReader.close();
        }
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/IndicatorsServiceFacadeIndicatorsTest.xml";
    }

    private String tempDirPath() throws IOException {
        File temp = File.createTempFile("temp-file-name", ".tmp");
        String absolutePath = temp.getAbsolutePath();
        String tempFilePath = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
        temp.delete();
        return tempFilePath;
    }
}
