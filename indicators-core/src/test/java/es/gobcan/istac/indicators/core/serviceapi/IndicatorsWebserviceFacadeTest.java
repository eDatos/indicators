package es.gobcan.istac.indicators.core.serviceapi;

import org.fornax.cartridges.sculptor.framework.test.AbstractDbUnitJpaTests;
import static org.junit.Assert.fail;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Spring based transactional test with DbUnit support.
 */
public class IndicatorsWebserviceFacadeTest extends AbstractDbUnitJpaTests
    implements IndicatorsWebserviceFacadeTestBase {
    @Autowired
    protected IndicatorsWebserviceFacade indicatorsWebserviceFacade;

    @Test
    public void testFindIndicatorsSystems() throws Exception {
        // TODO Auto-generated method stub
        fail("testFindIndicatorsSystems not implemented");
    }
}
