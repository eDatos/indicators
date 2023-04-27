package es.gobcan.istac.indicators.core.job;

import java.util.Date;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;

public class ExportsDsplJob implements Job {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public static final String INDICATOR_UUID = "indicatorUuid";
    public static final String CODE = "code";
    public static final String USER = "user";
    public static final String MERGE_TIME_GRANULARITIES = "mergeTimeGranularities";

    private IndicatorsServiceFacade indicatorsServiceFacade = null;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobKey jobKey = context.getJobDetail().getKey();
        logger.info("Exports DSPL Job: {} starting at {}", jobKey, new Date());

        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();

        String indicatorUuid = jobDataMap.getString(INDICATOR_UUID);
        String code = (String) jobDataMap.get(CODE);
        String user = (String) jobDataMap.get(USER);
        boolean mergeTimeGranularities = jobDataMap.getBoolean(MERGE_TIME_GRANULARITIES);

        ServiceContext serviceContext = new ServiceContext(user, context.getFireInstanceId(), "metamac-core");

        try {
            getIndicatorsServiceFacade().executeExportDSPL(serviceContext, indicatorUuid, code, mergeTimeGranularities);
        } catch (MetamacException e) {
            logger.error("Error en exports dspl job");
            throw new JobExecutionException(e);
        }

        logger.info("Exports DSPL Job: {} finished at {}", jobKey, new Date());
    }

    private IndicatorsServiceFacade getIndicatorsServiceFacade() {
        if (indicatorsServiceFacade == null) {
            indicatorsServiceFacade = ApplicationContextProvider.getApplicationContext().getBean(IndicatorsServiceFacade.class);
        }
        return indicatorsServiceFacade;
    }
}