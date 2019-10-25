package es.gobcan.istac.indicators.core.task.serviceapi;

import org.springframework.beans.factory.annotation.Autowired;

public class TaskServiceFacadeTest implements TaskServiceFacadeTestBase {

    @Autowired
    protected TaskServiceFacade taskServiceFacade;

    @Override
    public void testExecutePopulationIndicatorDataTask() throws Exception {
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
    public void testMarkAllInProgressTaskToFailed() throws Exception {
        // Quartz jobs are not tested
    }

    @Override
    public void testScheduleIndicatorsUpdateJob() throws Exception {
        // Quartz jobs are not tested
    }
}
