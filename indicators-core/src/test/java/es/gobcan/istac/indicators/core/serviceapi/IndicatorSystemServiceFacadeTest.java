package es.gobcan.istac.indicators.core.serviceapi;

import org.fornax.cartridges.sculptor.framework.test.AbstractDbUnitJpaTests;
import static org.junit.Assert.fail;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Spring based transactional test with DbUnit support.
 */
public class IndicatorSystemServiceFacadeTest extends AbstractDbUnitJpaTests
    implements IndicatorSystemServiceFacadeTestBase {
    @Autowired
    protected IndicatorSystemServiceFacade indicatorSystemServiceFacade;

    @Test
    public void testCreateIndicatorSystem() throws Exception {
        // TODO Auto-generated method stub
        fail("testCreateIndicatorSystem not implemented");
    }

    @Test
    public void testMakeDraftIndicatorSystem() throws Exception {
        // TODO Auto-generated method stub
        fail("testMakeDraftIndicatorSystem not implemented");
    }

    @Test
    public void testUpdateIndicatorSystem() throws Exception {
        // TODO Auto-generated method stub
        fail("testUpdateIndicatorSystem not implemented");
    }

    @Test
    public void testPublishIndicatorSystem() throws Exception {
        // TODO Auto-generated method stub
        fail("testPublishIndicatorSystem not implemented");
    }

    @Test
    public void testRetrieveIndicatorSystem() throws Exception {
        // TODO Auto-generated method stub
        fail("testRetrieveIndicatorSystem not implemented");
    }

    @Test
    public void testDeleteIndicatorSystem() throws Exception {
        // TODO Auto-generated method stub
        fail("testDeleteIndicatorSystem not implemented");
    }
}
