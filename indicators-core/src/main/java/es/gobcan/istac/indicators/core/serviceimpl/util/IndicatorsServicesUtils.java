package es.gobcan.istac.indicators.core.serviceimpl.util;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.task.serviceapi.TaskService;

public class IndicatorsServicesUtils {

    private IndicatorsServicesUtils() {
        // NOTHING TO DO HERE
    }

    public static void checkNotTasksInProgress(ServiceContext ctx, TaskService taskService, String resourceId) throws MetamacException {
        if (taskService.existsTaskForResource(ctx, resourceId)) {
            throw new MetamacException(ServiceExceptionType.TASKS_IN_PROGRESS, resourceId);
        }
    }

}
