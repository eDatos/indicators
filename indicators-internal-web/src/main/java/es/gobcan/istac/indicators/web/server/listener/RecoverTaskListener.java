package es.gobcan.istac.indicators.web.server.listener;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import es.gobcan.istac.indicators.core.task.serviceapi.TaskServiceFacade;

@Component
public class RecoverTaskListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final String SCHEDULED_JOBS_APPLICATION_CONTEXT_NAME = "Root WebApplicationContext";
    private static Logger       logger                                  = LoggerFactory.getLogger(RecoverTaskListener.class);

    @Autowired
    private TaskServiceFacade   taskServiceFacade;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // Indicators application has two Spring contexts defined in web.xml: Root WebApplicationContext and Spring MVC Dispatcher Servlet. The Context Refreshed Event is fired by each one of them and
        // the recovering task is executed twice redundantly. To avoid it, we choose a context in which the jobs are scheduled.
        if (SCHEDULED_JOBS_APPLICATION_CONTEXT_NAME.equals(event.getApplicationContext().getDisplayName())) {
            logger.debug("Recovering tasks...");
            recoveringTask();
        }
    }

    private void recoveringTask() {
        ServiceContext ctx = new ServiceContext("Metamac", "Tasks", "Metamac");
        taskServiceFacade.markAllInProgressTaskToFailed(ctx);
    }
}
