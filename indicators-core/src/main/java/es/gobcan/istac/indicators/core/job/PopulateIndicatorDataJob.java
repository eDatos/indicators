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

import es.gobcan.istac.indicators.core.service.NoticesRestInternalService;
import es.gobcan.istac.indicators.core.taks.serviceapi.TaskServiceFacade;

public class PopulateIndicatorDataJob implements Job {

    protected final Logger             logger                     = LoggerFactory.getLogger(getClass());

    public static final String         USER                       = "user";
    public static final String         INDICATOR_UUID             = "indicatorUuid";

    private TaskServiceFacade          taskServiceFacade          = null;
    private NoticesRestInternalService noticesRestInternalService = null;
    protected ServiceContext           serviceContext             = null;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobKey jobKey = context.getJobDetail().getKey();
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();

        String user = jobDataMap.getString(USER);
        String indicatorUuid = jobDataMap.getString(INDICATOR_UUID);

        try {
            logger.info("Populate Indicator Data Job: {} starting at {}", jobKey, new Date());
            serviceContext = new ServiceContext(user, context.getFireInstanceId(), "metamac-core");

            List<MetamacExceptionItem> populateErrors = getTaskServiceFacade().executePopulationIndicatorDataTask(serviceContext, indicatorUuid);
            processPopulationIndicatorDataResult(serviceContext, user, indicatorUuid, populateErrors);

            logger.info("Populate Indicator Data Job: {} finished at {}", jobKey, new Date());
        } catch (MetamacException e) {
            // TODO EDATOS-3047 catch error and send notification?
            try {
                getTaskServiceFacade().createPopulateIndicatorDataErrorBackgroundNotification(serviceContext, user, indicatorUuid, e);
            } catch (MetamacException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        }
    }

    public void processPopulationIndicatorDataResult(ServiceContext serviceContext, String user, String indicatorUuid, List<MetamacExceptionItem> populateErrors) throws MetamacException {
        if (CollectionUtils.isEmpty(populateErrors)) {
            getTaskServiceFacade().createPopulateIndicatorDataSuccessBackgroundNotification(serviceContext, user, indicatorUuid);
        } else {
            MetamacException metamacException = MetamacExceptionBuilder.builder().withExceptionItems(populateErrors).build();

            getTaskServiceFacade().createPopulateIndicatorDataErrorBackgroundNotification(serviceContext, user, indicatorUuid, metamacException);
        }
    }

    private TaskServiceFacade getTaskServiceFacade() {
        if (taskServiceFacade == null) {
            taskServiceFacade = (TaskServiceFacade) ApplicationContextProvider.getApplicationContext().getBean(TaskServiceFacade.BEAN_ID);
        }

        return taskServiceFacade;
    }

}
