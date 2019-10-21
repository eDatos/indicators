package es.gobcan.istac.indicators.web.server.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.gobcan.istac.indicators.core.task.serviceapi.TaskServiceFacade;

public class RecoverTaskListener implements ServletContextListener {

    private static Logger     logger = LoggerFactory.getLogger(RecoverTaskListener.class);

    private TaskServiceFacade taskServiceFacade;

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        logger.debug("Recovering tasks...");
        recoveringTask();
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        // NOTHING TO DO HERE
    }

    private void recoveringTask() {
        ServiceContext ctx = new ServiceContext("Metamac", "Tasks", "Metamac");
        getTaskServiceFacade().markAllInProgressTaskToFailed(ctx);
    }

    private TaskServiceFacade getTaskServiceFacade() {
        if (taskServiceFacade == null) {
            taskServiceFacade = (TaskServiceFacade) ApplicationContextProvider.getApplicationContext().getBean(TaskServiceFacade.BEAN_ID);
        }
        return taskServiceFacade;
    }

}
