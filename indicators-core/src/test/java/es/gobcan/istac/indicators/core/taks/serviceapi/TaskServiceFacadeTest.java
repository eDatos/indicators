package es.gobcan.istac.indicators.core.taks.serviceapi;

import static org.junit.Assert.fail;

import org.fornax.cartridges.sculptor.framework.test.AbstractDbUnitJpaTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Spring based transactional test with DbUnit support.
 */
public class TaskServiceFacadeTest extends AbstractDbUnitJpaTests implements TaskServiceFacadeTestBase {

    @Autowired
    protected TaskServiceFacade taskServiceFacade;

    @Test
    @Override
    public void testExecutePopulationIndicatorDataTask() throws Exception {
        // TODO EDATOS-3047 Auto-generated method stub
        fail("testExecuteImportationTask not implemented");
    }

    @Override
    public void testCreatePopulateIndicatorDataSuccessBackgroundNotification() throws Exception {
        // TODO EDATOS-3047 Auto-generated method stub
        fail("testCreatePopulateIndicatorDataSuccessBackgroundNotification not implemented");
    }

    @Override
    public void testCreatePopulateIndicatorDataErrorBackgroundNotification() throws Exception {
        // TODO EDATOS-3047 Auto-generated method stub
        fail("testCreatePopulateIndicatorDataErrorBackgroundNotification not implemented");
    }
}
