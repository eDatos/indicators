package es.gobcan.istac.indicators.core.serviceapi;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.gobcan.istac.indicators.core.domain.IndicatorsSystem;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.enume.domain.VersionTypeEnum;
import es.gobcan.istac.indicators.core.serviceapi.utils.IndicatorsMocks;

/**
 * Test to IndicatorsSystemService. Testing: indicators systems, dimensions, indicators instances
 * Spring based transactional test with DbUnit support.
 * 
 * Only testing properties are not in Dto
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext-test.xml"})
@TransactionConfiguration(defaultRollback=false,transactionManager="txManager")
@Transactional
public class IndicatorsSystemsServiceTest extends IndicatorsBaseTest {

    @Autowired
    protected IndicatorsSystemsService indicatorsSystemService;

    private static String              INDICATORS_SYSTEM_3         = "IndSys-3";
    private static String              INDICATORS_SYSTEM_1         = "IndSys-1";
    private static String              INDICATORS_SYSTEM_3_VERSION = "11.033";
    private static String              INDICATORS_SYSTEM_5         = "IndSys-5";

    @Test
    public void testCreateIndicatorsSystem() throws Exception {

        IndicatorsSystemVersion indicatorsSystemVersion = new IndicatorsSystemVersion();
        indicatorsSystemVersion.setIndicatorsSystem(new IndicatorsSystem());
        indicatorsSystemVersion.getIndicatorsSystem().setCode(IndicatorsMocks.mockString(10));

        // Create
        IndicatorsSystemVersion indicatorsSystemVersionCreated = indicatorsSystemService.createIndicatorsSystem(getServiceContextAdministrador(), indicatorsSystemVersion);

        // Validate properties are not in Dto
        String uuid = indicatorsSystemVersionCreated.getIndicatorsSystem().getUuid();
        String version = indicatorsSystemVersionCreated.getVersionNumber();
        IndicatorsSystemVersion indicatorsSystemCreated = indicatorsSystemService.retrieveIndicatorsSystem(getServiceContextAdministrador(), uuid, version);
        assertFalse(indicatorsSystemCreated.getIndicatorsSystem().getIsPublished());
        assertTrue(indicatorsSystemVersionCreated.getIsLastVersion());
        assertNull(indicatorsSystemCreated.getIndicatorsSystem().getDiffusionVersion());
    }

    @Test
    public void testDeleteIndicatorWithPublishedAndDraft() throws Exception {

        String uuid = INDICATORS_SYSTEM_1;

        // Retrieve before delete
        {
            IndicatorsSystemVersion indicatorsSystemVersion1 = indicatorsSystemService.retrieveIndicatorsSystem(getServiceContextAdministrador(), uuid, "1.000");
            assertFalse(indicatorsSystemVersion1.getIsLastVersion());
            IndicatorsSystemVersion indicatorsSystemVersion2 = indicatorsSystemService.retrieveIndicatorsSystem(getServiceContextAdministrador(), uuid, "2.000");
            assertTrue(indicatorsSystemVersion2.getIsLastVersion());
        }

        // Delete indicator
        indicatorsSystemService.deleteIndicatorsSystem(getServiceContextAdministrador(), uuid);

        // Validation
        // Retrieve after delete
        {
            IndicatorsSystemVersion indicatorsSystemVersion1 = indicatorsSystemService.retrieveIndicatorsSystem(getServiceContextAdministrador(), uuid, "1.000");
            assertTrue(indicatorsSystemVersion1.getIsLastVersion());
            assertNull(indicatorsSystemVersion1.getIndicatorsSystem().getProductionVersion());
        }
    }

    @Test
    public void testVersioningIndicatorsSystem() throws Exception {

        String uuid = INDICATORS_SYSTEM_3;

        // Retrieve before versioning
        {
            IndicatorsSystemVersion indicatorsSystemVersion1 = indicatorsSystemService.retrieveIndicatorsSystem(getServiceContextAdministrador(), uuid, INDICATORS_SYSTEM_3_VERSION);
            assertTrue(indicatorsSystemVersion1.getIsLastVersion());
        }

        // Versioning
        IndicatorsSystemVersion newVersion = indicatorsSystemService.versioningIndicatorsSystem(getServiceContextAdministrador(), uuid, VersionTypeEnum.MAJOR);

        // Retrieve after delete
        {
            IndicatorsSystemVersion indicatorsSystemVersion1 = indicatorsSystemService.retrieveIndicatorsSystem(getServiceContextAdministrador(), uuid, INDICATORS_SYSTEM_3_VERSION);
            assertFalse(indicatorsSystemVersion1.getIsLastVersion());
            IndicatorsSystemVersion indicatorsSystemVersion2 = indicatorsSystemService.retrieveIndicatorsSystem(getServiceContextAdministrador(), uuid, newVersion.getVersionNumber());
            assertTrue(indicatorsSystemVersion2.getIsLastVersion());
        }
    }

    @Test
    public void testPublishIndicatorsSystem() throws Exception {

        String uuid = INDICATORS_SYSTEM_5;
        String versionNumber = "1.000";

        // Publish
        indicatorsSystemService.publishIndicatorsSystem(getServiceContextAdministrador(), uuid);

        // Validate properties are not in Dto
        IndicatorsSystemVersion indicatorsSystemCreated = indicatorsSystemService.retrieveIndicatorsSystem(getServiceContextAdministrador(), uuid, versionNumber);
        assertTrue(indicatorsSystemCreated.getIndicatorsSystem().getIsPublished());
    }

    @Test
    public void testArchiveIndicatorsSystem() throws Exception {

        String uuid = INDICATORS_SYSTEM_3;
        String versionNumber = INDICATORS_SYSTEM_3_VERSION;

        // Archive
        indicatorsSystemService.archiveIndicatorsSystem(getServiceContextAdministrador(), uuid);

        // Validate properties are not in Dto
        IndicatorsSystemVersion indicatorsSystemCreated = indicatorsSystemService.retrieveIndicatorsSystem(getServiceContextAdministrador(), uuid, versionNumber);
        assertFalse(indicatorsSystemCreated.getIndicatorsSystem().getIsPublished());
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/IndicatorsServiceFacadeIndicatorsSystemsTest.xml";
    }
}
