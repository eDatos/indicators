package es.gobcan.istac.indicators.core.serviceimpl;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.SchedulerRepository;
import org.quartz.impl.StdSchedulerFactory;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;

/**
 * Implementation of TaskService.
 */
@Service("taskService")
public class TaskServiceImpl extends TaskServiceImplBase {

    private static final String            PREFIX_JOB_POPULATE_DATA = "job_populatedata_";

    @Autowired
    private IndicatorsConfigurationService configurationService;

    public TaskServiceImpl() {
    }

    public boolean existsTaskForResource(ServiceContext ctx, String resourceId) throws MetamacException {
        return existsPopulateDataTaskInResource(ctx, resourceId);
    }

    private boolean existsPopulateDataTaskInResource(ServiceContext ctx, String resourceId) throws MetamacException {
        try {
            // TODO EDATOS-3047 just for testing!
            if ("9d4b1064-a5e8-4329-a45f-063c2b731f44".equals(resourceId) || "9d62e0d2-7e88-4eba-9401-32473eab8ff7".equals(resourceId)) {
                return Boolean.TRUE;
            }
            Scheduler sched = SchedulerRepository.getInstance().lookup(getSchedulerInstanceName());
            return sched.checkExists(createJobKeyForPopulateData(resourceId));
        } catch (SchedulerException e) {
            throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.TASKS_SCHEDULER_ERROR).withMessageParameters(e.getMessage()).build();
        }
    }

    private String getSchedulerInstanceName() throws MetamacException {
        return configurationService.retrieveProperty(StdSchedulerFactory.PROP_SCHED_INSTANCE_NAME);
    }

    private JobKey createJobKeyForPopulateData(String resourceId) {
        return new JobKey(createJobNameForPopulateData(resourceId));
    }

    private String createJobNameForPopulateData(String resourceId) {
        return PREFIX_JOB_POPULATE_DATA + resourceId;
    }
}
