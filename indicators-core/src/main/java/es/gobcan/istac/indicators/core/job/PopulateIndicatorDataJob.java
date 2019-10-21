package es.gobcan.istac.indicators.core.job;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.task.serviceapi.TaskServiceFacade;

public class PopulateIndicatorDataJob implements Job {

    protected final Logger     logger            = LoggerFactory.getLogger(getClass());

    public static final String USER              = "user";
    public static final String INDICATOR_UUID    = "indicatorUuid";
    public static final String TASK_NAME         = "taskName";

    private TaskServiceFacade  taskServiceFacade = null;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobKey jobKey = context.getJobDetail().getKey();
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();

        String user = jobDataMap.getString(USER);
        String indicatorUuid = jobDataMap.getString(INDICATOR_UUID);
        String taskName = jobDataMap.getString(TASK_NAME);

        ServiceContext serviceContext = new ServiceContext(user, context.getFireInstanceId(), "metamac-core");

        try {
            logger.info("Populate Indicator Data Job: {} starting at {}", jobKey, new Date());

            List<MetamacExceptionItem> populateErrors = getTaskServiceFacade().executePopulationIndicatorDataTask(serviceContext, taskName, indicatorUuid);
            processPopulationIndicatorDataResult(serviceContext, user, indicatorUuid, populateErrors);

            logger.info("Populate Indicator Data Job: {} finished at {}", jobKey, new Date());
        } catch (MetamacException metamacException) {
            logger.error("Populate Indicator Data Job: the population indicator data job with key {} has failed:", jobKey.getName(), metamacException);

            metamacException.setPrincipalException(new MetamacExceptionItem(ServiceExceptionType.POPULATE_INDICATOR_JOB_ERROR));
            sendErrorNotification(user, indicatorUuid, serviceContext, metamacException);
        }
    }

    private void processPopulationIndicatorDataResult(ServiceContext serviceContext, String user, String indicatorUuid, List<MetamacExceptionItem> populateErrors) {
        if (CollectionUtils.isEmpty(populateErrors)) {
            sendSuccessNotification(serviceContext, user, indicatorUuid);
        } else {
            MetamacException metamacException = MetamacExceptionBuilder.builder().withExceptionItems(populateErrors).build();

            sendErrorNotification(user, indicatorUuid, serviceContext, metamacException);
        }
    }

    private void sendSuccessNotification(ServiceContext serviceContext, String user, String indicatorUuid) {
        getTaskServiceFacade().createPopulateIndicatorDataSuccessBackgroundNotification(serviceContext, user, indicatorUuid);
    }

    private void sendErrorNotification(String user, String indicatorUuid, ServiceContext serviceContext, MetamacException metamacException) {
        getTaskServiceFacade().createPopulateIndicatorDataErrorBackgroundNotification(serviceContext, user, indicatorUuid, metamacException);
    }

    private TaskServiceFacade getTaskServiceFacade() {
        if (taskServiceFacade == null) {
            taskServiceFacade = (TaskServiceFacade) ApplicationContextProvider.getApplicationContext().getBean(TaskServiceFacade.BEAN_ID);
        }

        return taskServiceFacade;
    }

}
