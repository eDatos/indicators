package es.gobcan.istac.indicators.core.serviceimpl;

import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class KafkaAsyncProducer implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaAsyncProducer.class);

    @Autowired
    public IndicatorsServiceFacade indicatorsServiceFacade;

    @Override
    @Async
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            indicatorsServiceFacade.resendAllPendingAndFailedMessages(null);
        } catch (Exception e) {
            LOGGER.error("Could not send kafka messages", e);
        }
    }
}
