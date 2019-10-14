package es.gobcan.istac.indicators.core.taks.serviceimpl;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.taks.serviceapi.TaskService;

/**
 * Implementation of TaskServiceFacade.
 */
@Service("taskServiceFacade")
public class TaskServiceFacadeImpl extends TaskServiceFacadeImplBase {

    @Autowired
    private TaskService taskService;

    public TaskServiceFacadeImpl() {
        // NOTHING TO DO HERE
    }

    public List<MetamacExceptionItem> executePopulationIndicatorDataTask(ServiceContext ctx, String indicatorUuid) throws MetamacException {
        return taskService.processPopulationIndicatorDataTask(ctx, indicatorUuid);
    }

    @Override
    public void createPopulateIndicatorDataSuccessBackgroundNotification(ServiceContext ctx, String user, String indicatorUuid) throws MetamacException {
        taskService.createPopulateIndicatorDataSuccessBackgroundNotification(ctx, user, indicatorUuid);
    }

    @Override
    public void createPopulateIndicatorDataErrorBackgroundNotification(ServiceContext ctx, String user, String indicatorUuid, MetamacException metamacException) throws MetamacException {
        taskService.createPopulateIndicatorDataErrorBackgroundNotification(ctx, user, indicatorUuid, metamacException);
    }
}
