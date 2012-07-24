package es.gobcan.istac.indicators.core.serviceapi;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.gobcan.istac.indicators.core.domain.Indicator;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.Quantity;
import es.gobcan.istac.indicators.core.domain.QuantityUnitRepository;
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
    protected IndicatorsService      indicatorService;

    @Autowired
    protected QuantityUnitRepository quantityUnitRepository;

    // Indicators
    private static String            INDICATOR_1         = "Indicator-1";
    private static String            INDICATOR_3         = "Indicator-3";
    private static String            INDICATOR_3_VERSION = "11.033";
    private static String            INDICATOR_5         = "Indicator-5";

    // Quantity units
    private static String            QUANTITY_UNIT_1     = "1";

    @Test
    public void testCreateIndicator() throws Exception {

        IndicatorVersion indicatorVersion = new IndicatorVersion();
        indicatorVersion.setIndicator(new Indicator());
        indicatorVersion.getIndicator().setCode("MYCODE");
        indicatorVersion.setTitle(new InternationalString());
        indicatorVersion.setSubjectCode(IndicatorsMocks.mockString(10));
        indicatorVersion.setSubjectTitle(new InternationalString());
        indicatorVersion.setQuantity(new Quantity());
        indicatorVersion.getQuantity().setQuantityType(QuantityTypeEnum.AMOUNT);
        indicatorVersion.getQuantity().setUnit(quantityUnitRepository.retrieveQuantityUnit(QUANTITY_UNIT_1));
        indicatorVersion.getQuantity().setUnitMultiplier(Integer.valueOf(1));

        // Create
        IndicatorVersion indicatorVersionCreated = indicatorService.createIndicator(getServiceContextAdministrador(), indicatorVersion);

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
        indicatorVersion.setTitle(new InternationalString());
        indicatorVersion.setSubjectCode(IndicatorsMocks.mockString(10));
        indicatorVersion.setSubjectTitle(new InternationalString());
        indicatorVersion.setQuantity(new Quantity());
        indicatorVersion.getQuantity().setQuantityType(QuantityTypeEnum.AMOUNT);
        indicatorVersion.getQuantity().setUnit(quantityUnitRepository.retrieveQuantityUnit(QUANTITY_UNIT_1));
        indicatorVersion.getQuantity().setUnitMultiplier(Integer.valueOf(1));

        // Create
        IndicatorVersion indicatorVersionCreated = indicatorService.createIndicator(getServiceContextAdministrador(), indicatorVersion);

        // Check after creation needsUpdate is false
        assertFalse(indicatorVersionCreated.getNeedsUpdate());

        indicatorVersionCreated.setNeedsUpdate(Boolean.TRUE);

        IndicatorVersion indicatorVersionUpdated = indicatorService.updateIndicatorVersion(getServiceContextAdministrador(), indicatorVersionCreated);
        assertTrue(indicatorVersionUpdated.getNeedsUpdate());
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
    public void testArchiveIndicator() throws Exception {

        String uuid = INDICATOR_3;
        String versionNumber = INDICATOR_3_VERSION;

        // Archive
        indicatorService.archiveIndicator(getServiceContextAdministrador(), uuid);

        // Validate properties are not in Dto
        IndicatorVersion indicatorCreated = indicatorService.retrieveIndicator(getServiceContextAdministrador(), uuid, versionNumber);
        assertFalse(indicatorCreated.getIndicator().getIsPublished());
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/IndicatorsServiceFacadeIndicatorsTest.xml";
    }

    @Override
    protected Map<String, String> getTablePrimaryKeys() {
        return null;
    }

}
