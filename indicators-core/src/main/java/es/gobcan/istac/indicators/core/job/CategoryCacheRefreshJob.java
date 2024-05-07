package es.gobcan.istac.indicators.core.job;

import java.util.Date;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.notices.ServiceNoticeAction;
import es.gobcan.istac.indicators.core.service.NoticesRestInternalService;
import es.gobcan.istac.indicators.core.task.serviceapi.TaskServiceFacade;

@DisallowConcurrentExecution
public class CategoryCacheRefreshJob implements Job {

    private static Logger      logger             = LoggerFactory.getLogger(CategoryCacheRefreshJob.class);

    public static final String IS_SCHEDULE_MANUAL = "scheduleManual";
    public static final String TASK_NAME          = "taskName";
    public static final String USER               = "user";
    public static final String SEND_NOTIFICATION  = "sendNotification";

    private TaskServiceFacade  taskServiceFacade  = null;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        JobKey jobKey = context.getJobDetail().getKey();
        JobDataMap data = context.getJobDetail().getJobDataMap();
        String taskName = data.getString(TASK_NAME);
        Boolean isScheduleManual = data.getBoolean(IS_SCHEDULE_MANUAL);
        String user = data.getString(USER);
        boolean sendNotification = data.getBoolean(SEND_NOTIFICATION);

        ServiceContext serviceContext = new ServiceContext("updateJob", context.getFireInstanceId(), "metamac-core");

        try {

            String loggerId = jobKey + (isScheduleManual ? " (" + jobKey + ")" : " (Automatic job execution) ");
            logger.debug("Category cache refresh job {} is running at {}", loggerId, new Date());

            if (Boolean.TRUE.equals(isScheduleManual)) {
                getTaskServiceFacade().executeCategoryCacheRefreshManualTask(serviceContext, taskName);
            } else {
                getTaskServiceFacade().executeCategoryCacheRefreshAutomaticTask(serviceContext);

            }

            logger.debug("Category cache refresh job < {} > successfully executed at {}", loggerId, new Date());
        } catch (MetamacException e) {
            processErrorsInExecutionJob(e, serviceContext, taskName, user, sendNotification, jobKey, isScheduleManual);
        }
    }

    private TaskServiceFacade getTaskServiceFacade() {
        if (taskServiceFacade == null) {
            taskServiceFacade = (TaskServiceFacade) ApplicationContextProvider.getApplicationContext().getBean(TaskServiceFacade.BEAN_ID);
        }

        return taskServiceFacade;
    }

    private NoticesRestInternalService getNoticesRestInternalService() {
        return (NoticesRestInternalService) ApplicationContextProvider.getApplicationContext().getBean(NoticesRestInternalService.BEAN_ID);
    }

    private void processErrorsInExecutionJob(MetamacException e, ServiceContext serviceContext, String taskName, String user, Boolean sendNotification, JobKey jobKey, Boolean isScheduleManual) {
        logger.error("An unexpected error has occurred during Category cache refresh job execution {}", jobKey, e);

        if (Boolean.TRUE.equals(sendNotification)) {
            getNoticesRestInternalService().createUpdateCategoryCacheErrorNotification(user, ServiceNoticeAction.UPDATE_CATEGORY_CACHE_JOB, e);
        }

        try {
            if (Boolean.TRUE.equals(isScheduleManual)) {
                getTaskServiceFacade().markTaskAsFailed(serviceContext, taskName, e);
            }

            logger.info("UpdateCategoryCacheJob: {} marked as error at {}", jobKey, new Date());
            e.setPrincipalException(new MetamacExceptionItem(ServiceExceptionType.TASKS_JOB_UPDATE_CATEGORY_CACHE_ERROR));
        } catch (MetamacException e1) {
            logger.error("UpdateCategoryCacheJob: the cache update job with key " + jobKey.getName() + " has failed and it can't marked as error", e1);
            e.setPrincipalException(new MetamacExceptionItem(ServiceExceptionType.UPDATE_CATEGORY_CACHE_JOB_ERROR_AND_CANT_MARK_AS_ERROR));
        }
    }

}
