package es.gobcan.istac.indicators.core.task.serviceapi;

import org.springframework.beans.factory.annotation.Autowired;

public class TaskServiceTest implements TaskServiceTestBase {

    @Autowired
    protected TaskService taskService;

    @Override
    public void testExistsTaskForResource() throws Exception {
        // See tests in IndicatorsServiceTest
    }

    @Override
    public void testPlanifyPopulationIndicatorData() throws Exception {
        // Quartz jobs are not tested
    }

    @Override
    public void testProcessPopulationIndicatorDataTask() throws Exception {
        // See tests in IndicatorsDataServicePopulateTest
    }

    @Override
    public void testCreatePopulateIndicatorDataSuccessBackgroundNotification() throws Exception {
        // Notifications are not tested
    }

    @Override
    public void testCreatePopulateIndicatorDataErrorBackgroundNotification() throws Exception {
        // Notifications are not tested
    }

    @Override
    public void testProcessExportDSPLTask() throws Exception {
        // See tests in DsplExporterServiceTest
    }

    @Override
    public void testCreateTask() throws Exception {
        // No tests for task repository
    }

    @Override
    public void testUpdateTask() throws Exception {
        // No tests for task repository
    }

    @Override
    public void testMarkTaskAsFinished() throws Exception {
        // No tests for task repository
    }

    @Override
    public void testMarkTaskAsFailed() throws Exception {
        // No tests for task repository
    }

    @Override
    public void testRetrieveTaskByJob() throws Exception {
        // No tests for task repository
    }

    @Override
    public void testMarkAllInProgressTaskToFailed() throws Exception {
        // Quartz jobs are not tested
    }

    @Override
    public void testScheduleIndicatorsUpdateJob() throws Exception {
        // Quartz jobs are not tested
    }

    @Override
    public void testPlanifyExportsDsplJob() throws Exception {
        // Quartz jobs are not tested
    }

    @Override
    public void testScheduleCategoryCacheRefreshAutomaticJob() throws Exception {
        // Quartz jobs are not tested
    }

    @Override
    public void testScheduleCategoryCacheRefreshManualJob() throws Exception {
        // Quartz jobs are not tested
    }

    @Override
    public void testProcessCategoryCacheRefreshAutomaticTask() throws Exception {
        // no test
    }

    @Override
    public void testProcessCategoryCacheRefreshManualTask() throws Exception {
        // no test
    }
}
