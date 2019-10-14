package es.gobcan.istac.indicators.core.taks.serviceapi;

import static org.junit.Assert.fail;

import org.fornax.cartridges.sculptor.framework.test.AbstractDbUnitJpaTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Spring based transactional test with DbUnit support.
 */
public class TaskServiceTest extends AbstractDbUnitJpaTests implements TaskServiceTestBase {

    @Autowired
    protected TaskService taskService;

    @Test
    public void testExistsTaskForResource() throws Exception {
        // TODO EDATOS-3047 Auto-generated method stub
        fail("testExistsTaskForResource not implemented");
    }

    @Test
    public void testPlanifyPopulationIndicatorData() throws Exception {
        // TODO EDATOS-3047 Auto-generated method stub
        fail("testPlanifyPopulationIndicatorData not implemented");
    }

    @Test
    public void testProcessPopulationIndicatorDataTask() throws Exception {
        // TODO EDATOS-3047 Auto-generated method stub
        fail("testProcessPopulationIndicatorDataTask not implemented");
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
