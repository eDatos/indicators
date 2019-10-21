package es.gobcan.istac.indicators.core.task.serviceimpl;

import static org.quartz.DateBuilder.futureDate;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
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
import es.gobcan.istac.indicators.core.enume.domain.TaskStatusTypeEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.job.PopulateIndicatorDataJob;
import es.gobcan.istac.indicators.core.service.NoticesRestInternalService;
import es.gobcan.istac.indicators.core.serviceimpl.util.InvocationValidator;
import es.gobcan.istac.indicators.core.task.domain.Task;
import es.gobcan.istac.indicators.core.task.domain.TaskProperties;
import es.gobcan.istac.indicators.core.task.exception.TaskNotFoundException;

/**
 * Implementation of TaskService.
 */
@Service("taskService")
public class TaskServiceImpl extends TaskServiceImplBase {

    public static final String             PREFIX_JOB_POPULATE_DATA = "job_populatedata_";

    protected final Logger                 logger                   = LoggerFactory.getLogger(getClass());

    @Autowired
    private IndicatorsConfigurationService configurationService;

    public TaskServiceImpl() {
        // NOTHING TO DO HERE
    }

    public boolean existsTaskForResource(ServiceContext ctx, String resourceId) throws MetamacException {
        InvocationValidator.checkExistsTaskForResource(resourceId, null);

        return existsPopulateDataTaskInResource(resourceId);
    }

    @Override
    public synchronized void planifyPopulationIndicatorData(ServiceContext ctx, String indicatorUuid) throws MetamacException {
        planifyPopulationIndicatorData(ctx, indicatorUuid, ctx.getUserId());
    }

    private synchronized void planifyPopulationIndicatorData(ServiceContext ctx, String indicatorUuid, String user) throws MetamacException {
        InvocationValidator.checkPlanifyPopulationIndicatorData(indicatorUuid, user, null);

        String populationIndicatorDataTaskName = createTaskNameForPopulationIndicatorData(indicatorUuid);
        JobKey populationIndicatorDataJobKey = createJobKeyForPopulationIndicatorData(populationIndicatorDataTaskName);
        TriggerKey populationIndicatorDataTriggerKey = createTriggerKeyForPopulationIndicatorData(populationIndicatorDataTaskName);

        checkExistTaskInResource(populationIndicatorDataJobKey);
        checkExistsGarbage(populationIndicatorDataTaskName);

        JobDetail jobDetail = createPopulateIndicatorDataJob(ctx, indicatorUuid, populationIndicatorDataTaskName, user, populationIndicatorDataJobKey);
        SimpleTrigger trigger = createPopulateIndicatorDataTrigger(populationIndicatorDataTriggerKey);

        Task newTask = new Task(populationIndicatorDataTaskName);
        newTask.setStatus(TaskStatusTypeEnum.IN_PROGRESS);
        newTask.setExtensionPoint(indicatorUuid);
        createTask(ctx, newTask);

        scheduleJob(jobDetail, trigger);

        logger.info("Planned a populate indicator data for indicator uuid {}", indicatorUuid);
    }

    @Override
    public List<MetamacExceptionItem> processPopulationIndicatorDataTask(ServiceContext ctx, String taskName, String indicatorUuid) throws MetamacException {
        try {
            InvocationValidator.checkProcessPopulationIndicatorDataTask(taskName, indicatorUuid, null);

            return getIndicatorsDataService().populateIndicatorData(ctx, indicatorUuid);
        } finally {
            // The task is always deleted, whatever if the process is going OK or not. In this case there is no recovery process, the user is notified of the error, and it's necessary to to correct it
            // and reloading the indicator data again
            markTaskAsFinished(ctx, taskName);
        }
    }

    @Override
    public void createPopulateIndicatorDataSuccessBackgroundNotification(ServiceContext ctx, String user, String indicatorUuid) {
        try {
            InvocationValidator.checkCreatePopulateIndicatorDataSuccessBackgroundNotification(user, indicatorUuid, null);

            Indicator indicator = getIndicatorsService().retrieveIndicator(ctx, indicatorUuid);

            getNoticesRestInternalService().createPopulateIndicatorDataSuccessBackgroundNotification(user, indicator);
        } catch (MetamacException metamacException) {
            // If an error occurs sending a notification, it is logged but not re-throwed in order to not generate additional noise
            logger.error("Unexpected error in createPopulateIndicatorDataSuccessBackgroundNotification: ", metamacException);
        }
    }

    @Override
    public void createPopulateIndicatorDataErrorBackgroundNotification(ServiceContext ctx, String user, String indicatorUuid, MetamacException metamacException) {
        try {
            InvocationValidator.checkCreatePopulateIndicatorDataErrorBackgroundNotification(user, indicatorUuid, metamacException, null);

            Indicator indicator = getIndicatorsService().retrieveIndicator(ctx, indicatorUuid);

            getNoticesRestInternalService().createPopulateIndicatorDataErrorBackgroundNotification(user, indicator, metamacException);
        } catch (MetamacException metamacException1) {
            // If an error occurs sending a notification, it is logged but not re-throwed in order to not generate additional noise
            logger.error("Unexpected error in createPopulateIndicatorDataErrorBackgroundNotification: ", metamacException1);
        }
    }

    @Override
    public Task createTask(ServiceContext ctx, Task task) throws MetamacException {
        return getTaskRepository().save(task);
    }

    @Override
    public Task updateTask(ServiceContext ctx, Task task) throws MetamacException {
        return getTaskRepository().save(task);
    }

    @Override
    public Task retrieveTaskByJob(ServiceContext ctx, String jobKey) throws MetamacException {
        try {
            return getTaskRepository().findByKey(jobKey);
        } catch (TaskNotFoundException e) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.TASKS_JOB_NOT_FOUND).withMessageParameters(jobKey).build();
        }
    }

    @Override
    public void markTaskAsFinished(ServiceContext ctx, String jobKey) throws MetamacException {
        // Delete complete task
        Task task = retrieveTaskByJob(ctx, jobKey);
        getTaskRepository().delete(task);
    }

    @Override
    // This method is only used on application startup
    public void markAllInProgressTaskToFailed(ServiceContext ctx) {
        List<ConditionalCriteria> conditionList = ConditionalCriteriaBuilder.criteriaFor(Task.class).withProperty(TaskProperties.status()).eq(TaskStatusTypeEnum.IN_PROGRESS).or()
                .withProperty(TaskProperties.status()).eq(TaskStatusTypeEnum.FAILED).build();
        PagedResult<Task> tasks = findTasksByCondition(conditionList, PagingParameter.noLimits());

        if (!tasks.getValues().isEmpty()) {
            for (Task task : tasks.getValues()) {
                logger.info("Recovering task {} of the user {}", task.getJob(), task.getCreatedBy());
                markTasksAsFailedOnApplicationStartup(ctx, task);
            }
        }
    }

    private void markTasksAsFailedOnApplicationStartup(ServiceContext ctx, Task task) {
        // If the recover process of one task fails, don't stop other recovery process
        try {
            if (task.getJob().startsWith(PREFIX_JOB_POPULATE_DATA)) {
                planifyPopulationIndicatorData(ctx, task.getExtensionPoint(), task.getCreatedBy());
            }
        } catch (MetamacException e) {
            logger.error("Recover of task {} fails", task.getJob(), e);
        }
    }

    private void scheduleJob(JobDetail jobDetail, SimpleTrigger trigger) throws MetamacException {
        try {
            getScheduler().scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.TASKS_ERROR).withMessageParameters(e.getMessage()).withCause(e).withLoggedLevel(ExceptionLevelEnum.ERROR)
                    .build(); // Error
        }
    }

    private void checkExistsGarbage(String populationIndicatorDataTaskName) {
        // Checking garbage
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Task.class).withProperty(TaskProperties.job()).eq(populationIndicatorDataTaskName).distinctRoot().build();
        PagedResult<Task> tasks = findTasksByCondition(conditions, PagingParameter.pageAccess(1, 1));
        if (!tasks.getValues().isEmpty()) {
            // In case that a previous populate indicator data job fails, it does not make sense to launch a recovery process, the recovery process is the reloading of the indicator data itself.
            // Records in TB_TASK must be deleted if they exist and continue with the process.
            for (Task task : tasks.getValues()) {
                getTaskRepository().deleteAndFlush(task);
            }
        }
    }

    private PagedResult<Task> findTasksByCondition(List<ConditionalCriteria> conditions, PagingParameter pageAccess) {
        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(Task.class).distinctRoot().build();
        }
        return getTaskRepository().findByCondition(conditions, pageAccess);
    }

    private void checkExistTaskInResource(JobKey populationIndicatorDataJobKey) throws MetamacException {
        checkSameJobNotExists(populationIndicatorDataJobKey);
    }

    private void checkSameJobNotExists(JobKey populationIndicatorDataJobKey) throws MetamacException {
        try {
            if (getScheduler().checkExists(populationIndicatorDataJobKey)) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.TASKS_ERROR_MAX_CURRENT_JOBS).withLoggedLevel(ExceptionLevelEnum.ERROR).build();
            }
        } catch (SchedulerException e) {
            throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.TASKS_SCHEDULER_ERROR).withMessageParameters(e.getMessage()).build();
        }
    }

    private boolean existsPopulateDataTaskInResource(String resourceId) throws MetamacException {
        try {
            // TODO EDATOS-3047 just for testing!
            if ("9d4b1064-a5e8-4329-a45f-063c2b731f44".equals(resourceId) || "9d62e0d2-7e88-4eba-9401-32473eab8ff7".equals(resourceId)) {
                return Boolean.TRUE;
            }
            return getScheduler().checkExists(createJobKeyForPopulationIndicatorData(createTaskNameForPopulationIndicatorData(resourceId)));
        } catch (SchedulerException e) {
            throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.TASKS_SCHEDULER_ERROR).withMessageParameters(e.getMessage()).build();
        }
    }

    private JobDetail createPopulateIndicatorDataJob(ServiceContext ctx, String indicatorUuid, String populationIndicatorDataTaskName, String user, JobKey jobKey) {
        // @formatter:off
        return JobBuilder.newJob()
                .ofType(PopulateIndicatorDataJob.class)
                .withIdentity(jobKey)
                .usingJobData(PopulateIndicatorDataJob.USER, user)
                .usingJobData(PopulateIndicatorDataJob.INDICATOR_UUID, indicatorUuid)
                .usingJobData(PopulateIndicatorDataJob.TASK_NAME, populationIndicatorDataTaskName)
                .requestRecovery().build();
        // @formatter:on
    }

    private SimpleTrigger createPopulateIndicatorDataTrigger(TriggerKey triggerKey) {
        // @formatter:off
        return TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
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

    private JobKey createJobKeyForPopulationIndicatorData(String populationIndicatorDataTaskName) {
        return new JobKey(populationIndicatorDataTaskName);
    }

    private TriggerKey createTriggerKeyForPopulationIndicatorData(String populationIndicatorDataTaskName) {
        return new TriggerKey(populationIndicatorDataTaskName);
    }

    public String createTaskNameForPopulationIndicatorData(String indicatorUuid) {
        return TaskServiceImpl.PREFIX_JOB_POPULATE_DATA + indicatorUuid;
    }

    private NoticesRestInternalService getNoticesRestInternalService() {
        return (NoticesRestInternalService) ApplicationContextProvider.getApplicationContext().getBean(NoticesRestInternalService.BEAN_ID);
    }
}
