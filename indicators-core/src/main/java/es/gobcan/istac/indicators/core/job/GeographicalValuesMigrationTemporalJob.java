package es.gobcan.istac.indicators.core.job;

import java.util.Date;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.gobcan.istac.indicators.core.task.serviceapi.TaskServiceFacade;

@DisallowConcurrentExecution
public class GeographicalValuesMigrationTemporalJob implements Job {

    private static Logger      logger            = LoggerFactory.getLogger(GeographicalValuesMigrationTemporalJob.class);

    public static final String TASK_NAME         = "taskName";

    private TaskServiceFacade  taskServiceFacade = null;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        JobKey jobKey = context.getJobDetail().getKey();

        ServiceContext serviceContext = new ServiceContext("updateJob", context.getFireInstanceId(), "metamac-core");

        try {

            String loggerId = jobKey + " (Automatic job execution) ";
            logger.info("geographical values migration temporal job {} is running at {}", loggerId, new Date());

            getTaskServiceFacade().executeGeographicalValuesMigrationTemporalTask(serviceContext);

            logger.info("geographical values migration temporal job < {} > successfully executed at {}", loggerId, new Date());
        } catch (MetamacException e) {
            logger.error("An unexpected error has occurred during geographical values migration temporal job execution", jobKey, e);
        }
    }

    private TaskServiceFacade getTaskServiceFacade() {
        if (taskServiceFacade == null) {
            taskServiceFacade = (TaskServiceFacade) ApplicationContextProvider.getApplicationContext().getBean(TaskServiceFacade.BEAN_ID);
        }

        return taskServiceFacade;
    }
}
