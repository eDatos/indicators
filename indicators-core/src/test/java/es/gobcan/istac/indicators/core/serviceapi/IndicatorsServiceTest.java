package es.gobcan.istac.indicators.core.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import es.gobcan.istac.indicators.core.enume.domain.QuantityTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.VersionTypeEnum;
import es.gobcan.istac.indicators.core.serviceapi.utils.IndicatorsMocks;

/**
 * Test to IndicatorService. Testing: indicators, data sources...
 * Spring based transactional test with DbUnit support.
 * Only testing properties are not in Dto
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/include/indicators-service-mockito.xml", "classpath:spring/applicationContext-test.xml"})
@TransactionConfiguration(defaultRollback = true, transactionManager = "txManager")
@Transactional
public class IndicatorsServiceTest extends IndicatorsBaseTest {

    @Autowired
    protected IndicatorsService        indicatorService;

    @Autowired
    protected QuantityUnitRepository   quantityUnitRepository;

    @Autowired
    protected UnitMultiplierRepository unitMultiplierRepository;

    // Indicators
    private static String              INDICATOR_1         = "Indicator-1";
    private static String              INDICATOR_3         = "Indicator-3";
    private static String              INDICATOR_3_VERSION = "11.033";
    private static String              INDICATOR_5         = "Indicator-5";
    private static String              INDICATOR_12        = "Indicator-12";
    private static String              INDICATOR_13        = "Indicator-13";

    // Quantity units
    private static String              QUANTITY_UNIT_1     = "1";

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
            IndicatorVersion indicatorVersion1 = indicatorService.retrieveIndicator(getServiceContextAdministrador(), uuid, "1.000");
            assertFalse(indicatorVersion1.getIsLastVersion());
            IndicatorVersion indicatorVersion2 = indicatorService.retrieveIndicator(getServiceContextAdministrador(), uuid, "2.000");
            assertTrue(indicatorVersion2.getIsLastVersion());
        }

        // Delete indicator
        indicatorService.deleteIndicator(getServiceContextAdministrador(), uuid);

        // Validation
        // Retrieve after delete
        {
            IndicatorVersion indicatorVersion1 = indicatorService.retrieveIndicator(getServiceContextAdministrador(), uuid, "1.000");
            assertTrue(indicatorVersion1.getIsLastVersion());
            assertNull(indicatorVersion1.getIndicator().getProductionVersion());
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
            assertFalse(indicatorVersion1.getInconsistentData());
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
    public void testPublishIndicator() throws Exception {

        String uuid = INDICATOR_5;
        String versionNumber = "1.000";

        // Publish
        indicatorService.publishIndicator(getServiceContextAdministrador(), uuid);

        // Validate properties are not in Dto
        IndicatorVersion indicatorCreated = indicatorService.retrieveIndicator(getServiceContextAdministrador(), uuid, versionNumber);
        assertTrue(indicatorCreated.getIndicator().getIsPublished());
    }

    @Test
    public void testPublishIndicatorSubjectTitleChange() throws Exception {

        String uuid = INDICATOR_13;
        String versionNumber = "1.000";

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
    }

    @Test
    public void testFindIndicatorsByCriteria() throws Exception {
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(IndicatorVersion.class).withProperty(IndicatorVersionProperties.lastValuesCache().geographicalCode())
                    .eq("ES").orderBy(IndicatorVersionProperties.indicator().uuid()).ascending().build();

            PagingParameter paging = PagingParameter.pageAccess(10);

            PagedResult<IndicatorVersion> indicatorsVersion = indicatorService.findIndicatorsPublished(getServiceContextAdministrador(), conditions, paging);

            assertEquals(3, indicatorsVersion.getValues().size());

            assertEquals(INDICATOR_1, indicatorsVersion.getValues().get(0).getIndicator().getUuid());
            assertEquals(INDICATOR_12, indicatorsVersion.getValues().get(1).getIndicator().getUuid());
            assertEquals(INDICATOR_3, indicatorsVersion.getValues().get(2).getIndicator().getUuid());
        }
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(IndicatorVersion.class).withProperty(IndicatorVersionProperties.lastValuesCache().geographicalCode())
                    .eq("FR").orderBy(IndicatorVersionProperties.indicator().uuid()).build();

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

    @Override
    protected String getDataSetFile() {
        return "dbunit/IndicatorsServiceFacadeIndicatorsTest.xml";
    }
}
