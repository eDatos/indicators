package es.gobcan.istac.indicators.core.task.serviceapi;

import static org.junit.Assert.fail;

import org.fornax.cartridges.sculptor.framework.test.AbstractDbUnitJpaTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import es.gobcan.istac.indicators.core.taks.serviceapi.TaskServiceTestBase;

/**
 * Spring based transactional test with DbUnit support.
 */
public class TaskServiceTest extends AbstractDbUnitJpaTests implements TaskServiceTestBase {

    @Autowired
    protected TaskService taskService;

    @Test
    @Override
    public void testExistsTaskForResource() throws Exception {
        // TODO EDATOS-3047 Auto-generated method stub
        fail("testExistsTaskForResource not implemented");
    }

    @Test
    @Override
    public void testPlanifyPopulationIndicatorData() throws Exception {
        // TODO EDATOS-3047 Auto-generated method stub
        fail("testPlanifyPopulationIndicatorData not implemented");
    }

    @Test
    @Override
    public void testProcessPopulationIndicatorDataTask() throws Exception {
        // TODO EDATOS-3047 Auto-generated method stub
        fail("testProcessPopulationIndicatorDataTask not implemented");
    }

    @Test
    @Override
    public void testCreatePopulateIndicatorDataSuccessBackgroundNotification() throws Exception {
        // TODO EDATOS-3047 Auto-generated method stub
        fail("testCreatePopulateIndicatorDataSuccessBackgroundNotification not implemented");
    }

    @Test
    @Override
    public void testCreatePopulateIndicatorDataErrorBackgroundNotification() throws Exception {
        // TODO EDATOS-3047 Auto-generated method stub
        fail("testCreatePopulateIndicatorDataErrorBackgroundNotification not implemented");
    }
}
