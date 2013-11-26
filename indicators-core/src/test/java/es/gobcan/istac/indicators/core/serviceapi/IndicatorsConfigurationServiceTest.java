package es.gobcan.istac.indicators.core.serviceapi;

import static org.junit.Assert.fail;

import org.fornax.cartridges.sculptor.framework.test.AbstractDbUnitJpaTests;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Spring based transactional test with DbUnit support.
 */
public class IndicatorsConfigurationServiceTest extends AbstractDbUnitJpaTests implements IndicatorsConfigurationServiceTestBase {

    @Autowired
    protected IndicatorsConfigurationService indicatorsConfigurationService;

    @Override
    @Test
    @Ignore
    public void testRetrieveLastSuccessfulGpeQueryDate() throws Exception {

    }

    @Override
    @Test
    @Ignore
    public void testSetLastSuccessfulGpeQueryDate() throws Exception {
        fail("testSetLastSuccessfulGpeQueryDate not implemented");
    }
}
