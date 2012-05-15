package es.gobcan.istac.indicators.core.job;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.gobcan.istac.indicators.core.serviceapi.IndicatorsDataService;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class IndicatorsUpdateJob implements Job {

    private static Logger         logger = LoggerFactory.getLogger(IndicatorsUpdateJob.class);

    private IndicatorsDataService indicatorsDataService;

    public IndicatorsUpdateJob() {
    }

    public IndicatorsDataService getIndicatorsDataService() {
        if (indicatorsDataService == null) {
            indicatorsDataService = ApplicationContextProvider.getApplicationContext().getBean(IndicatorsDataService.class);
        }
        return indicatorsDataService;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        ServiceContext serviceContext = new ServiceContext("updateJob", context.getFireInstanceId(), "metamac-core");
        try {
            getIndicatorsDataService().updateIndicatorsData(serviceContext);
        } catch (MetamacException e) {
            logger.error("Error updating indicators Data");
        }
    }
}
