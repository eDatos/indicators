package es.gobcan.istac.indicators.core.serviceapi.utils;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.mockito.Mockito;
import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.indicators.core.task.serviceapi.TaskService;

public class TaskMockUtils {

    private TaskMockUtils() {
        // NOTHING TO DO HERE
    }

    public static void mockTaskInProgressForIndicator(TaskService taskService, String indicatorUuid, boolean status) throws MetamacException {
        Mockito.when(taskService.existsTaskForResource(Mockito.any(ServiceContext.class), Mockito.eq(indicatorUuid))).thenReturn(status);
    }

    public static void mockAllTaskInProgressForIndicator(TaskService taskService, boolean status) throws MetamacException {
        Mockito.when(taskService.existsTaskForResource(Mockito.any(ServiceContext.class), Mockito.anyString())).thenReturn(status);
    }
}
