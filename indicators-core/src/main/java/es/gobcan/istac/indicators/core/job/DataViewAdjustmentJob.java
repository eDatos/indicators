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
@Deprecated
public class DataViewAdjustmentJob implements Job {

    private static Logger     logger            = LoggerFactory.getLogger(DataViewAdjustmentJob.class);

    private TaskServiceFacade taskServiceFacade = null;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            logger.debug("Indicators. The data view adjustment with dimension code descriptions job is running at {}", new Date());
            ServiceContext serviceContext = new ServiceContext("Metamac", context.getFireInstanceId(), "metamac-core");
            getTaskServiceFacade().executeDataViewAdjustmentTask(serviceContext);
            logger.debug("Indicators. The data view adjustment with dimension code descriptions job successfully executed at {}", new Date());
        } catch (MetamacException e) {
            logger.error("Indicators. An unexpected error has occurred during the data view adjustment with dimension code descriptions job execution", e);
        }
    }

    private TaskServiceFacade getTaskServiceFacade() {
        if (taskServiceFacade == null) {
            taskServiceFacade = (TaskServiceFacade) ApplicationContextProvider.getApplicationContext().getBean(TaskServiceFacade.BEAN_ID);
        }

        return taskServiceFacade;
    }

}
