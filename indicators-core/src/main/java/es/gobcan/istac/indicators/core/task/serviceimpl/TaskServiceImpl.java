package es.gobcan.istac.indicators.core.task.serviceimpl;

import static org.quartz.DateBuilder.futureDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
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
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import es.gobcan.istac.indicators.core.domain.Indicator;
import es.gobcan.istac.indicators.core.enume.domain.TaskStatusTypeEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.job.CategoryCacheRefreshJob;
import es.gobcan.istac.indicators.core.job.ExportsDsplJob;
import es.gobcan.istac.indicators.core.job.GeographicalValuesMigrationTemporalJob;
import es.gobcan.istac.indicators.core.job.IndicatorsUpdateJob;
import es.gobcan.istac.indicators.core.job.PopulateIndicatorDataJob;
import es.gobcan.istac.indicators.core.notices.ServiceNoticeAction;
import es.gobcan.istac.indicators.core.service.NoticesRestInternalService;
import es.gobcan.istac.indicators.core.serviceimpl.util.InvocationValidator;
import es.gobcan.istac.indicators.core.task.domain.Task;
import es.gobcan.istac.indicators.core.task.domain.TaskProperties;
import es.gobcan.istac.indicators.core.task.exception.TaskNotFoundException;

/**
 * Implementation of TaskService.
 */
@Service("taskService")
public class TaskServiceImpl extends TaskServiceImplBase implements ApplicationListener<ContextRefreshedEvent> {

    public static final String             PREFIX_JOB_POPULATE_DATA                       = "job_populatedata_";
    public static final String             PREFIX_JOB_EXPORTS_DSPL                        = "exports_dspl_job_";
    public static final String             PREFIX_JOB_UPDATE_CATEGORY_CACHE               = "update_category_cache_from_srm";
    public static final String             GROUP_EXTERNAL_CATEGORY_CACHE                  = "externalCategoryCacheUpdate";
    public static final String             PREFIX_TEMPORAL_JOB_UPDATE_GEOGRAPHICAL_VALUES = "update_geographical_values";

    protected final Logger                 logger                                         = LoggerFactory.getLogger(getClass());

    private SchedulerFactory               schedulerFactory                               = null;

    @Autowired
    private IndicatorsConfigurationService configurationService;

    public TaskServiceImpl() {
        // NOTHING TO DO HERE
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (schedulerFactory != null) {
            return;
        }

        Properties quartzProperties = configurationService.getProperties();

        try {
            schedulerFactory = new StdSchedulerFactory(quartzProperties);
            Scheduler sched = schedulerFactory.getScheduler();

            // Start now
            sched.start();
        } catch (SchedulerException e) {
            throw new IllegalStateException("An unexpected error has occurred during quartz initialization", e);
        }

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
        SimpleTrigger trigger = createTrigger(populationIndicatorDataTriggerKey);

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
    public void processExportDSPLTask(ServiceContext ctx, String jobKey, String indicatorUuid, String code, boolean mergeTimeGranularities) throws MetamacException {
        getIndicatorsDataService().executeExportDSPL(ctx, indicatorUuid, code, mergeTimeGranularities);
        markTaskAsFinished(ctx, jobKey);
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
    public void markTaskAsFailed(ServiceContext ctx, String jobKey, MetamacException exception) throws MetamacException {
        Task task = retrieveTaskByJob(ctx, jobKey);
        // Plannify a recovery job
        if (jobKey.startsWith(PREFIX_JOB_UPDATE_CATEGORY_CACHE)) {
            processRollbackUpdateCategoryCacheTask(ctx, task.getJob(), exception);
        }
    }

    private void processRollbackUpdateCategoryCacheTask(ServiceContext ctx, String jobKey, MetamacException e) throws MetamacException {
        getNoticesRestInternalService().createUpdateCategoryCacheErrorNotification(ctx.getUserId(), ServiceNoticeAction.UPDATE_CATEGORY_CACHE_JOB, e);
        markTaskAsFinished(ctx, jobKey);
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

    @Override
    public void scheduleIndicatorsUpdateJob(ServiceContext ctx) {
        try {
            JobDetail job = JobBuilder.newJob(IndicatorsUpdateJob.class).withIdentity("update_indicators_job", "cron_jobs").build();

            String cronExpr = configurationService.retrieveQuartzExpressionUpdateIndicators();

            CronTrigger trigger = newTrigger().withIdentity("triger_update_indicators", "cron_jobs").withSchedule(CronScheduleBuilder.cronSchedule(cronExpr)).build();

            getScheduler().scheduleJob(job, trigger);

            logger.info("Job has been planned using cron expression: {}", trigger.getCronExpression());
        } catch (Exception e) {
            logger.error("An unexpected error has occurred scheduling indicators update job", e);
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

    private void checkExistTaskInResource(JobKey jobKey) throws MetamacException {
        checkSameJobNotExists(jobKey);
    }

    private void checkExistTaskInResource(ServiceContext ctx, JobKey jobKey) throws MetamacException {
        checkSameJobNotExists(jobKey);

        checkExistUpdateCategoryCacheResource(ctx);

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

    private SimpleTrigger createTrigger(TriggerKey triggerKey) {
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

    public JobKey createJobKeyForUpdateCategoryCache() {
        return new JobKey(createTaskNameForUpdateCategoryCache());
    }

    public String createTaskNameForUpdateCategoryCache() {
        return TaskServiceImpl.PREFIX_JOB_UPDATE_CATEGORY_CACHE;
    }

    public JobKey createTemporalJobKeyForUpdateGeographicalValues() {
        return new JobKey(createTaskNameForUpdateCategoryCache());
    }

    public String createTemporalTaskNameForUpdateGeographicalValues() {
        return TaskServiceImpl.PREFIX_TEMPORAL_JOB_UPDATE_GEOGRAPHICAL_VALUES;
    }

    private TriggerKey createTriggerKeyForUpdateCategoryCach() {
        return new TriggerKey(createTaskNameForUpdateCategoryCache(), GROUP_EXTERNAL_CATEGORY_CACHE);
    }

    private NoticesRestInternalService getNoticesRestInternalService() {
        return (NoticesRestInternalService) ApplicationContextProvider.getApplicationContext().getBean(NoticesRestInternalService.BEAN_ID);
    }

    @Override
    public void planifyExportsDsplJob(ServiceContext ctx, String indicatorUuid, String code, boolean mergeTimeGranularities) throws MetamacException {
        planifyExportsDsplJob(ctx, indicatorUuid, ctx.getUserId(), code, mergeTimeGranularities);
    }

    private synchronized void planifyExportsDsplJob(ServiceContext ctx, String indicatorUuid, String user, String code, boolean mergeTimeGranularities) throws MetamacException {
        String taskName = PREFIX_JOB_EXPORTS_DSPL + indicatorUuid + "_" + System.currentTimeMillis();
        JobKey jobKey = new JobKey(taskName);
        TriggerKey triggerKey = new TriggerKey(taskName);
        checkExistsGarbage(taskName);

        JobDetail jobDetail = createExportsDSPLJob(ctx, indicatorUuid, code, mergeTimeGranularities, taskName, user, jobKey);
        SimpleTrigger trigger = createTrigger(triggerKey);

        Task newTask = new Task(taskName);
        newTask.setStatus(TaskStatusTypeEnum.IN_PROGRESS);
        newTask.setExtensionPoint(indicatorUuid);
        createTask(ctx, newTask);
        scheduleJob(jobDetail, trigger);
    }

    private JobDetail createExportsDSPLJob(ServiceContext ctx, String indicatorUuid, String code, boolean mergeTimeGranularities, String taskName, String user, JobKey jobKey) {
        // @formatter:off
        return JobBuilder.newJob()
                .ofType(ExportsDsplJob.class)
                .withIdentity(jobKey)
                .usingJobData(ExportsDsplJob.INDICATOR_UUID, indicatorUuid)
                .usingJobData(ExportsDsplJob.MERGE_TIME_GRANULARITIES, mergeTimeGranularities)
                .usingJobData(ExportsDsplJob.CODE, code)
                .usingJobData(ExportsDsplJob.USER, ctx.getUserId())
                .requestRecovery()
                .build();
        // @formatter:on
    }

    @Override
    public void scheduleCategoryCacheRefreshManualJob(ServiceContext ctx) throws MetamacException {

        String taskName = createTaskNameForUpdateCategoryCache();
        JobKey jobKey = this.createJobKeyForUpdateCategoryCache();
        TriggerKey triggerKey = this.createTriggerKeyForUpdateCategoryCach();

        try {
            InvocationValidator.checkScheduleCategoryCacheRefreshManualJob(ctx, taskName);

            checkExistTaskInResource(ctx, jobKey);

            JobDetail job = newJob(CategoryCacheRefreshJob.class).withIdentity(jobKey).usingJobData(CategoryCacheRefreshJob.TASK_NAME, taskName)
                    .usingJobData(CategoryCacheRefreshJob.SEND_NOTIFICATION, false).usingJobData(CategoryCacheRefreshJob.IS_SCHEDULE_MANUAL, true).build();

            Task task = new Task(taskName);
            task.setStatus(TaskStatusTypeEnum.IN_PROGRESS);
            createTask(ctx, task);

            SimpleTrigger trigger = newTrigger().withIdentity(triggerKey).startAt(futureDate(10, IntervalUnit.SECOND)).withSchedule(simpleSchedule()).build();

            try {
                Scheduler sched = schedulerFactory.getScheduler();
                sched.scheduleJob(job, trigger);
            } catch (SchedulerException e) {
                logger.error("scheduleCategoryCacheRefreshManualJob: the job with key " + jobKey.getName() + " has failed", e);
            }

        } catch (Exception e) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.TASKS_ERROR).withMessageParameters(e.getMessage()).withCause(e).withLoggedLevel(ExceptionLevelEnum.ERROR)
                    .build();
        }
    }

    @Override
    public void scheduleCategoryCacheRefreshAutomaticJob(ServiceContext ctx) {

        try {
            InvocationValidator.checkScheduleCategoryCacheRefreshCronJob(ctx);

            JobDetail job = newJob(CategoryCacheRefreshJob.class).usingJobData(CategoryCacheRefreshJob.TASK_NAME, "automaticJob").usingJobData(CategoryCacheRefreshJob.SEND_NOTIFICATION, true)
                    .usingJobData(CategoryCacheRefreshJob.IS_SCHEDULE_MANUAL, false).build();

            CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                    .withSchedule(CronScheduleBuilder.cronSchedule(configurationService.retrieveCronExpressionCategoryCacheRefresh()).withMisfireHandlingInstructionDoNothing()).build();

            Scheduler sched = schedulerFactory.getScheduler();
            sched.scheduleJob(job, cronTrigger);

            logger.info("category cache refresh job successfully scheduled at {} ", new Date());

        } catch (Exception e) {
            logger.error("An unexpected error has occurred scheduling category cache refresh job", e);
        }
    }

    @Override
    public void processCategoryCacheRefreshManualTask(ServiceContext ctx, String taskName) throws MetamacException {
        try {
            InvocationValidator.checkScheduleCategoryCacheRefreshManualJob(ctx, taskName);

            updateCategoryCacheAll(ctx);

        } catch (Exception e) {
            logger.error("An unexpected error has occurred trying to refresh category cache in indicators", e);
        }

        markTaskAsFinished(ctx, taskName);

    }

    @Override
    public void processCategoryCacheRefreshAutomaticTask(ServiceContext ctx) throws MetamacException {

        InvocationValidator.checkScheduleCategoryCacheRefreshCronJob(ctx);

        DateTime executionDate = new DateTime();

        logger.info("Execution start - update category cache in background at : {} ", executionDate);

        updateCategoryCacheAll(ctx);

        executionDate = new DateTime();

        logger.info("Execution end - update category cache in background at : {} ", executionDate);

    }

    @Override
    public void scheduleGeographicalValuesMigrationTemporalTask(ServiceContext ctx) {

        try {

            JobDetail job = newJob(GeographicalValuesMigrationTemporalJob.class).usingJobData(GeographicalValuesMigrationTemporalJob.TASK_NAME, "automaticJob").build();

            CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                    .withSchedule(CronScheduleBuilder.cronSchedule(configurationService.retrieveCronExpressionGeographicalValuesMigrationTemporalTask()).withMisfireHandlingInstructionDoNothing())
                    .build();

            CronExpression cronEx = new CronExpression(cronTrigger.getCronExpression());

            if (cronEx.getNextValidTimeAfter(new Date()) == null) {
                logger.info(
                        "ATENTION!! Cron scheduler for temporal job for  migration geographical values  is before actual date. For this reason the job has been aborted and it will not never executed ");
                return;
            }

            Scheduler sched = schedulerFactory.getScheduler();
            sched.scheduleJob(job, cronTrigger);

            logger.info("geographical values migration temporal job successfully scheduled at {} ", new Date());

        } catch (Exception e) {
            logger.error("An unexpected error has occurred scheduling geographical values migration temporal job", e);
        }
    }

    @Override
    public void processGeographicalValuesMigrationTemporalTask(ServiceContext ctx) throws MetamacException {
        try {
            DateTime executionDate = new DateTime();

            logger.info("Execution start - update geographical values migration in background at : {} ", executionDate);

            getIndicatorsService().updateDatasourceCodelistForGeographicalValuesMigration(ctx);

            executionDate = new DateTime();

            logger.info("Execution end - update geographical values migration in background at : {} ", executionDate);
        } catch (Exception e) {
            logger.error("Execution end with errors - update geographical values migration in background", e);
        }
    }

    private void updateCategoryCacheAll(ServiceContext ctx) throws MetamacException {
        List<String> allCategoryElementsInIndicators = getIndicatorsService().retrieveCategoryElementsInIndicators(ctx);

        getCategoryCacheService().updateCategoryCacheAll(ctx, allCategoryElementsInIndicators);
    }

    private void checkExistUpdateCategoryCacheResource(ServiceContext ctx) throws MetamacException {
        if (existUpdateCategoryCacheTaskInResource(ctx)) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.TASKS_JOB_UPDATE_CATEGORY_CACHE_IN_PROCESS).withLoggedLevel(ExceptionLevelEnum.ERROR).build();
        }
    }

    private boolean existUpdateCategoryCacheTaskInResource(ServiceContext ctx) throws MetamacException {
        InvocationValidator.checkExistUpdateCategoryCacheTaskInResource(ctx);
        try {
            Scheduler sched = schedulerFactory.getScheduler();
            return sched.checkExists(createJobKeyForUpdateCategoryCache());
        } catch (SchedulerException e) {
            throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.TASKS_SCHEDULER_ERROR).withMessageParameters(e.getMessage()).build();
        }
    }

}
