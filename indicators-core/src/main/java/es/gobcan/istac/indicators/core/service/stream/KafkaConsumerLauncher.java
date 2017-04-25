package es.gobcan.istac.indicators.core.service.stream;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.statistical.resources.core.stream.messages.QueryVersionAvro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import es.gobcan.istac.indicators.core.service.NoticesRestInternalService;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

@Component
public class KafkaConsumerLauncher implements ApplicationListener<ContextRefreshedEvent> {

    protected static Log                   LOGGER                  = LogFactory.getLog(KafkaConsumerLauncher.class);

    private Map<String, Future<?>>         futuresMap;
    private static final String            CONSUMER_QUERY_1_NAME   = "indicators_consumer_query_1";
    private static final String            KAFKA_FAILED_CACHE_NAME = "kafkaFailed";

    @Autowired
    private ThreadPoolTaskExecutor         threadPoolTaskExecutor;

    @Autowired
    private IndicatorsServiceFacade        indicatorsServiceFacade;

    @Autowired
    private IndicatorsConfigurationService configurationService;

    @Autowired
    private NoticesRestInternalService     noticesRestInternalService;

    private Cache                          kafkaFailedMessagesCache;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext ac = event.getApplicationContext();
        if (ac.getParent() == null) {
            // @formatter:off
            try {
                KafkaInitializeTopics.propagateCreationOfTopics(configurationService);
                prepareFailedMessageCache();

                futuresMap = new HashMap<>();
                futuresMap.put(CONSUMER_QUERY_1_NAME, startConsumerForQueryTopic(ac));
                startKeepAliveKafkaThread(ac);
            } catch (MetamacException | IllegalArgumentException | IOException e) {
                LOGGER.error(e);
            }
            // @formatter:on
        }
    }

    private void prepareFailedMessageCache() {
        CacheManager cacheManager = CacheManager.getInstance();
        cacheManager.addCache(KAFKA_FAILED_CACHE_NAME);
        Cache cache = cacheManager.getCache(KAFKA_FAILED_CACHE_NAME);
        kafkaFailedMessagesCache = cache;
    }

    public void startKeepAliveKafkaThread(ApplicationContext context) throws MetamacException {
        KeepAliveKafkaThread keepAliveKafkaThread = new KeepAliveKafkaThread();
        threadPoolTaskExecutor.execute(keepAliveKafkaThread);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Future<?> startConsumerForQueryTopic(ApplicationContext context) throws MetamacException {
        String topicQueryPublication = configurationService.retrieveKafkaTopicQueryPublication();
        KafkaConsumerThread<QueryVersionAvro> consumerThread = (KafkaConsumerThread) context.getBean("kafkaConsumerThread");
        KafkaConsumer<String, QueryVersionAvro> consumerFromBegin = createConsumerFromCurrentOffset(topicQueryPublication, CONSUMER_QUERY_1_NAME);
        consumerThread.setConsumer(consumerFromBegin);
        consumerThread.setTopicName(topicQueryPublication);
        consumerThread.setIndicatorsServiceFacade(indicatorsServiceFacade);
        consumerThread.setNoticesRestInternalService(noticesRestInternalService);
        consumerThread.setKafkaFailedMessagesCache(kafkaFailedMessagesCache);
        return threadPoolTaskExecutor.submit(consumerThread);
    }

    private Properties getConsumerProperties(String clientId) throws MetamacException {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, configurationService.retrieveKafkaBootStrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, configurationService.retrieveKafkaQueryGroup());
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, clientId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, io.confluent.kafka.serializers.KafkaAvroDeserializer.class);

        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false); // Default is True
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 10000); // 10 s
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 900000); // 15 min, Max time for Bussiness Logic execution of consumer thread
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1); // The maximum number of records returned in a single call to poll()
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, OffsetResetStrategy.EARLIEST.toString().toLowerCase()); // Policy to follow when there are no confirmed offset

        props.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, configurationService.retrieveKafkaSchemaRegistryUrl());
        props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);

        return props;
    }

    private KafkaConsumer<String, QueryVersionAvro> createConsumerFromCurrentOffset(String topic, String clientId) throws MetamacException {
        KafkaConsumer<String, QueryVersionAvro> kafkaConsumer = new KafkaConsumer<>(getConsumerProperties(clientId));
        kafkaConsumer.subscribe(Collections.singletonList(topic));
        return kafkaConsumer;
    }

    class KeepAliveKafkaThread implements Runnable {

        @Override
        public void run() {
            while (alwaysWithDelay(1000)) {

                for (Map.Entry<String, Future<?>> entry : futuresMap.entrySet()) {
                    if (entry.getValue().isDone()) {
                        LOGGER.info("El consumidor " + entry.getKey() + " se ha desconectado. Planificando otro consumidor para el mismo Topic...");
                        try {
                            switch (entry.getKey()) {
                                case CONSUMER_QUERY_1_NAME:
                                    futuresMap.put(CONSUMER_QUERY_1_NAME, startConsumerForQueryTopic(ApplicationContextProvider.getApplicationContext()));
                                    break;
                                default:
                                    break;
                            }
                        } catch (Exception e) {
                            long retyrMS = 6000;
                            LOGGER.error("Imposible replanificar consumidores de Kafka. Volviendolo a intentar en " + retyrMS + "ms", e);
                            alwaysWithDelay(60000);
                        }
                    }
                }
            }
        }

        private boolean alwaysWithDelay(long timeout) {
            try {
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                LOGGER.error(e);
            }
            return true;
        }
    }

}