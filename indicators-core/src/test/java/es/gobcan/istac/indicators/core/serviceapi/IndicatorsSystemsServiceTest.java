package es.gobcan.istac.indicators.core.serviceapi;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.gobcan.istac.indicators.core.domain.IndicatorsSystem;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.serviceapi.utils.IndicatorsMocks;

/**
 * Test to IndicatorsSystemService. Testing: indicators systems, dimensions, indicators instances
 * Spring based transactional test with DbUnit support.
 * 
 * Only testing properties are not in Dto
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext-test.xml"})
public class IndicatorsSystemsServiceTest extends IndicatorsBaseTest {

    @Autowired
    protected IndicatorsSystemsService indicatorsSystemService;

    private static String              INDICATORS_SYSTEM_3         = "IndSys-3";
    private static String              INDICATORS_SYSTEM_3_VERSION = "11.033";
    private static String              INDICATORS_SYSTEM_5         = "IndSys-5";

    @Test
    public void testCreateIndicatorsSystem() throws Exception {

        IndicatorsSystemVersion indicatorsSystemVersion = new IndicatorsSystemVersion();
        indicatorsSystemVersion.setIndicatorsSystem(new IndicatorsSystem());
        indicatorsSystemVersion.getIndicatorsSystem().setCode(IndicatorsMocks.mockString(10));
        indicatorsSystemVersion.setTitle(new InternationalString());

        // Create
        IndicatorsSystemVersion indicatorsSystemVersionCreated = indicatorsSystemService.createIndicatorsSystem(getServiceContext(), indicatorsSystemVersion);

        // Validate properties are not in Dto
        String uuid = indicatorsSystemVersionCreated.getIndicatorsSystem().getUuid();
        String version = indicatorsSystemVersionCreated.getVersionNumber();
        IndicatorsSystemVersion indicatorsSystemCreated = indicatorsSystemService.retrieveIndicatorsSystem(getServiceContext(), uuid, version);
        assertFalse(indicatorsSystemCreated.getIndicatorsSystem().getIsPublished());
    }

    @Test
    public void testPublishIndicatorsSystem() throws Exception {

        String uuid = INDICATORS_SYSTEM_5;
        String versionNumber = "1.000";

        // Publish
        indicatorsSystemService.publishIndicatorsSystem(getServiceContext(), uuid);

        // Validate properties are not in Dto
        IndicatorsSystemVersion indicatorsSystemCreated = indicatorsSystemService.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
        assertTrue(indicatorsSystemCreated.getIndicatorsSystem().getIsPublished());
    }

    @Test
    public void testArchiveIndicatorsSystem() throws Exception {

        String uuid = INDICATORS_SYSTEM_3;
        String versionNumber = INDICATORS_SYSTEM_3_VERSION;

        // Archive
        indicatorsSystemService.archiveIndicatorsSystem(getServiceContext(), uuid);

        // Validate properties are not in Dto
        IndicatorsSystemVersion indicatorsSystemCreated = indicatorsSystemService.retrieveIndicatorsSystem(getServiceContext(), uuid, versionNumber);
        assertFalse(indicatorsSystemCreated.getIndicatorsSystem().getIsPublished());
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/IndicatorsServiceFacadeIndicatorsSystemsTest.xml";
    }
}
