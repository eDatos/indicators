package es.gobcan.istac.indicators.core.service.stream;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.consumer.CommitFailedException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.siemac.metamac.sso.client.MetamacPrincipal;
import org.siemac.metamac.sso.client.MetamacPrincipalAccess;
import org.siemac.metamac.sso.client.SsoClientConstants;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import es.gobcan.istac.indicators.core.constants.IndicatorsConstants;
import es.gobcan.istac.indicators.core.enume.domain.RoleEnum;
import es.gobcan.istac.indicators.core.service.NoticesRestInternalService;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

@Component
@Scope("prototype")
public class KafkaConsumerThread<T extends SpecificRecordBase> implements Runnable {

    protected static Log LOGGER = LogFactory.getLog(KafkaConsumerThread.class);

    private static final String      MAX_POOL_MSG = "We have set a poll of 1 message at most. This error can not be given.";

    private KafkaConsumer<String, T> consumer;
    private String                   topicName;
    private IndicatorsServiceFacade  indicatorsServiceFacade;
    private NoticesRestInternalService noticesRestInternalService;
    private Cache                      kafkaFailedMessagesCache;

    public void setConsumer(KafkaConsumer<String, T> consumer) {
        this.consumer = consumer;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public void setIndicatorsServiceFacade(IndicatorsServiceFacade indicatorsServiceFacade, NoticesRestInternalService noticesRestInternalService, Cache kafkaFailedMessagesCache) {
        this.indicatorsServiceFacade = indicatorsServiceFacade;
        this.noticesRestInternalService = noticesRestInternalService;
        this.kafkaFailedMessagesCache = kafkaFailedMessagesCache;
    }

    @Override
    public void run() {
        LOGGER.debug("Reading KAFKA topic: " + topicName);

        try {
            
            Map<Integer, Long> pendigOffsetsToCommit = new HashMap<Integer, Long>(); // K:partition, V:offset
            
            while (alwaysWithDelay()) {
                // Milliseconds, spent waiting in poll if data is not available in the buffer
                ConsumerRecords<String, T> records = consumer.poll(100);

                if (records.count() > 1) {
                    LOGGER.error(MAX_POOL_MSG);
                    throw new RuntimeException(MAX_POOL_MSG);
                }

                if (records.isEmpty()) {
                    continue;
                }

                // Process resources
                ConsumerRecord<String, T> record = records.iterator().next();

                if (pendigOffsetsToCommit.containsKey(record.partition()) && record.offset() == pendigOffsetsToCommit.get(record.partition())) {
                    LOGGER.info("The current message already processed successfully");
                    if (commitSync(record)) {
                        pendigOffsetsToCommit.remove(record.partition());
                        removeFromErrorCacheMessagesIfNeccesary(record);
                    }
                    continue;
                }

                StringBuilder logMessageBldr = new StringBuilder("Received message from Kafka -> Topic Name: ");
                // @formatter:off
                logMessageBldr
                    .append(topicName)
                    .append(", Partition: ").append(record.partition())
                    .append(", Offset: ").append(record.offset())
                    .append(", TimestampType: ").append(record.timestampType())
                    .append(", Timestamp: ").append(record.timestamp())
                    .append(" [").append(new DateTime(record.timestamp(), DateTimeZone.forID("Atlantic/Canary"))).append("]");
                // @formatter:on
                String logMessage = logMessageBldr.toString();
                
                pendigOffsetsToCommit.put(record.partition(), record.offset());
                
                LOGGER.info(logMessage.toString());
                try {
                    ServiceContext serviceContext = createServiceContext(logMessage);

                    indicatorsServiceFacade.updateIndicatorsDataFromMetamac(serviceContext, record.value());
                    commitSync(record);
                } catch (Exception e) {
                    LOGGER.error("Unable to process resource received from KAFKA. The business of application has failed", e);

                    // Send a erro notification, the error message will send only if not exist in error cache
                    sendErrorMessageIfNeccesary(record);

                    // For evict commit
                    pendigOffsetsToCommit.remove(record.partition());
                }
            }
        } catch (Exception e) {
            LOGGER.error("An error has occurred in the Kafka client. Finishing the client.", e);
            LOGGER.error(e);
        } finally {
            consumer.close();
        }
    }

    private ServiceContext createServiceContext(String logMessage) {
        ServiceContext serviceContext = new ServiceContext("kafka-query-received", logMessage.toString(), "indicators-core");
        MetamacPrincipal metamacPrincipal = new MetamacPrincipal();
        metamacPrincipal.setUserId(serviceContext.getUserId());
        metamacPrincipal.getAccesses().add(new MetamacPrincipalAccess(RoleEnum.ADMINISTRADOR.getName(), IndicatorsConstants.SECURITY_APPLICATION_ID, null));
        serviceContext.setProperty(SsoClientConstants.PRINCIPAL_ATTRIBUTE, metamacPrincipal);
        return serviceContext;
    }

    private boolean commitSync(ConsumerRecord<String, T> record) {
        try {
            consumer.commitSync(Collections.singletonMap(new TopicPartition(record.topic(), record.partition()), new OffsetAndMetadata(record.offset() + 1)));
            LOGGER.debug("Commited message: " + record.partition() + " : " + record.offset());
        } catch (CommitFailedException e) {
            LOGGER.debug("The message processing takes longer than the session timeout. The coordinator kicks the consumer out of the group (rebalanced)");
            return false;
        }
        return true;
    }

    private void sendErrorMessageIfNeccesary(ConsumerRecord<String, T> record) {
        if (!kafkaFailedMessagesCache.isKeyInCache(record.key())) {
            Element element = new Element(record.key(), record.value());
            element.setEternal(true);
            kafkaFailedMessagesCache.put(element);
            noticesRestInternalService.createConsumerFromKafkaErrorBackgroundNotification(record.key());
        }
    }

    private void removeFromErrorCacheMessagesIfNeccesary(ConsumerRecord<String, T> record) {
        if (kafkaFailedMessagesCache.isKeyInCache(record.key())) {
            kafkaFailedMessagesCache.remove(record.key());
        }
    }

    private boolean alwaysWithDelay() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            LOGGER.error(e);
        }
        return true;
    }

}
