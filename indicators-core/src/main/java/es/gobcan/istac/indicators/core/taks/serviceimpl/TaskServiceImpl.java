package es.gobcan.istac.indicators.core.taks.serviceimpl;

import static org.quartz.DateBuilder.futureDate;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.SchedulerRepository;
import org.quartz.impl.StdSchedulerFactory;
import org.siemac.metamac.core.common.exception.ExceptionLevelEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import es.gobcan.istac.indicators.core.domain.Indicator;
import es.gobcan.istac.indicators.core.domain.IndicatorRepository;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.job.PopulateIndicatorDataJob;
import es.gobcan.istac.indicators.core.service.NoticesRestInternalService;
import es.gobcan.istac.indicators.core.serviceimpl.util.InvocationValidator;

/**
 * Implementation of TaskService.
 */
@Service("taskService")
public class TaskServiceImpl extends TaskServiceImplBase {

    private static final String            PREFIX_JOB_POPULATE_DATA = "job_populatedata_";

    protected final Logger                 logger                   = LoggerFactory.getLogger(getClass());

    @Autowired
    private IndicatorsConfigurationService configurationService;

    @Autowired
    private IndicatorRepository            indicatorRepository;

    public TaskServiceImpl() {
        // NOTHING TO DO HERE
    }

    public boolean existsTaskForResource(ServiceContext ctx, String resourceId) throws MetamacException {
        InvocationValidator.checkExistsTaskForResource(resourceId, null);
        return existsPopulateDataTaskInResource(ctx, resourceId);
    }

    @Override
    public synchronized String planifyPopulationIndicatorData(ServiceContext ctx, String indicatorUuid) throws MetamacException {
        InvocationValidator.checkPlanifyPopulationIndicatorData(indicatorUuid, null);

        try {
            JobDetail jobDetail = createPopulateIndicatorDataJob(ctx, indicatorUuid);
            SimpleTrigger trigger = createPopulateIndicatorDataTrigger(indicatorUuid);
            getScheduler().scheduleJob(jobDetail, trigger);
            return jobDetail.getKey().getName();
        } catch (Exception e) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.TASKS_ERROR).withMessageParameters(e.getMessage()).withCause(e).withLoggedLevel(ExceptionLevelEnum.ERROR)
                    .build(); // Error
        }
    }

    @Override
    public List<MetamacExceptionItem> processPopulationIndicatorDataTask(ServiceContext ctx, String indicatorUuid) throws MetamacException {
        InvocationValidator.checkProcessPopulationIndicatorDataTask(indicatorUuid, null);

        return getIndicatorsDataService().populateIndicatorData(ctx, indicatorUuid);
    }

    @Override
    public void createPopulateIndicatorDataSuccessBackgroundNotification(ServiceContext ctx, String user, String indicatorUuid) throws MetamacException {
        InvocationValidator.checkCreatePopulateIndicatorDataSuccessBackgroundNotification(user, indicatorUuid, null);

        Indicator indicator = getIndicatorsService().retrieveIndicator(ctx, indicatorUuid);

        getNoticesRestInternalService().createPopulateIndicatorDataSuccessBackgroundNotification(user, indicator);
    }

    @Override
    public void createPopulateIndicatorDataErrorBackgroundNotification(ServiceContext ctx, String user, String indicatorUuid, MetamacException metamacException) throws MetamacException {
        InvocationValidator.checkCreatePopulateIndicatorDataErrorBackgroundNotification(user, indicatorUuid, metamacException, null);

        Indicator indicator = getIndicatorsService().retrieveIndicator(ctx, indicatorUuid);

        getNoticesRestInternalService().createPopulateIndicatorDataErrorBackgroundNotification(user, indicator, metamacException);
    }

    private boolean existsPopulateDataTaskInResource(ServiceContext ctx, String resourceId) throws MetamacException {
        try {
            // TODO EDATOS-3047 just for testing!
            if ("9d4b1064-a5e8-4329-a45f-063c2b731f44".equals(resourceId) || "9d62e0d2-7e88-4eba-9401-32473eab8ff7".equals(resourceId)) {
                return Boolean.TRUE;
            }
            return getScheduler().checkExists(createJobKeyForPopulateData(resourceId));
        } catch (SchedulerException e) {
            throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.TASKS_SCHEDULER_ERROR).withMessageParameters(e.getMessage()).build();
        }
    }

    private JobDetail createPopulateIndicatorDataJob(ServiceContext ctx, String indicatorUuid) {
        // @formatter:off
        return JobBuilder.newJob()
                .ofType(PopulateIndicatorDataJob.class)
                .withIdentity(createJobKeyForPopulateData(indicatorUuid))
                .usingJobData(PopulateIndicatorDataJob.USER, ctx.getUserId())
                .usingJobData(PopulateIndicatorDataJob.INDICATOR_UUID, indicatorUuid)
                .requestRecovery().build();
        // @formatter:on
    }

    private SimpleTrigger createPopulateIndicatorDataTrigger(String indicatorUuid) {
        // @formatter:off
        return TriggerBuilder.newTrigger()
                .withIdentity(createTriggerKeyForPopulateData(indicatorUuid))
                .startAt(futureDate(10, IntervalUnit.SECOND))
                .withSchedule(simpleSchedule()).build();
        // @formatter:on
    }

    private Scheduler getScheduler() throws MetamacException {
        return SchedulerRepository.getInstance().lookup(getSchedulerInstanceName());
    }

    private String getSchedulerInstanceName() throws MetamacException {
        return configurationService.retrieveProperty(StdSchedulerFactory.PROP_SCHED_INSTANCE_NAME);
    }

    private JobKey createJobKeyForPopulateData(String resourceId) {
        return new JobKey(createJobNameForPopulateData(resourceId));
    }

    private TriggerKey createTriggerKeyForPopulateData(String resourceId) {
        return new TriggerKey(createJobNameForPopulateData(resourceId));
    }

    private String createJobNameForPopulateData(String resourceId) {
        return PREFIX_JOB_POPULATE_DATA + resourceId;
    }

    private NoticesRestInternalService getNoticesRestInternalService() {
        return (NoticesRestInternalService) ApplicationContextProvider.getApplicationContext().getBean(NoticesRestInternalService.BEAN_ID);
    }
}
