package es.gobcan.istac.indicators.core.serviceapi;

import org.fornax.cartridges.sculptor.framework.test.AbstractDbUnitJpaTests;
import static org.junit.Assert.fail;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Spring based transactional test with DbUnit support.
 */
public class IndicatorSystemServiceTest extends AbstractDbUnitJpaTests
    implements IndicatorSystemServiceTestBase {
    @Autowired
    protected IndicatorSystemService indicatorSystemService;

    @Test
    public void testCreateIndicatorSystem() throws Exception {
        // TODO Auto-generated method stub
        fail("testCreateIndicatorSystem not implemented");
    }
}
