package es.gobcan.istac.indicators.core.job;

import java.util.Date;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.gobcan.istac.indicators.core.task.serviceapi.TaskServiceFacade;

@DisallowConcurrentExecution
public class CategoryCacheRefreshJob implements Job {

    private static Logger     logger            = LoggerFactory.getLogger(CategoryCacheRefreshJob.class);

    private TaskServiceFacade taskServiceFacade = null;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            logger.debug("Category cache refresh job is running at {}", new Date());
            ServiceContext serviceContext = new ServiceContext("updateJob", context.getFireInstanceId(), "metamac-core");
            getTaskServiceFacade().executeCategoryCacheRefreshTask(serviceContext);
            logger.debug("Category cache refresh job successfully executed at {}", new Date());
        } catch (MetamacException e) {
            logger.error("An unexpected error has occurred during Category cache refresh job execution", e);
        }
    }

    private TaskServiceFacade getTaskServiceFacade() {
        if (taskServiceFacade == null) {
            taskServiceFacade = (TaskServiceFacade) ApplicationContextProvider.getApplicationContext().getBean(TaskServiceFacade.BEAN_ID);
        }

        return taskServiceFacade;
    }

}
