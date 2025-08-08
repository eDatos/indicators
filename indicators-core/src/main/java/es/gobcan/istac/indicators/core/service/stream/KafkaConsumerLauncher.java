package es.gobcan.istac.indicators.core.service.stream;

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
import org.siemac.metamac.srm.core.stream.message.CodelistAvro;
import org.siemac.metamac.srm.core.stream.message.ConceptSchemeAvro;
import org.siemac.metamac.srm.core.stream.message.VariableElementAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.DatasetVersionAvro;
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
import es.ibestat.jaxi.stream.messages.DatasetAvro;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

@Component
public class KafkaConsumerLauncher implements ApplicationListener<ContextRefreshedEvent> {

    protected static final Log             LOGGER                           = LogFactory.getLog(KafkaConsumerLauncher.class);

    private Map<String, Future<?>>         futuresMap;
    private static final String            CONSUMER_DATASET_1_NAME          = "indicators_consumer_dataset_1";
    private static final String            CONSUMER_JAXI_DATASET_1_NAME     = "indicators_consumer_jaxi_dataset_1";
    private static final String            CONSUMER_QUERY_1_NAME            = "indicators_consumer_query_1";
    private static final String            CONSUMER_VARIABLE_ELEMENT_1_NAME = "indicators_consumer_variable_element_1";
    private static final String            CONSUMER_CODELIST_1_NAME         = "indicators_consumer_codelist_1";
    private static final String            CONSUMER_CONCEPT_SCHEME_1_NAME   = "indicators_consumer_concept_scheme_1";
    private static final String            KAFKA_FAILED_CACHE_NAME          = "kafkaFailed";

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
                
                String externalDatasetTopic = getExternalDatasetPublicationTopic();
                KafkaInitializeTopics.propagateCreationOfTopics(configurationService, externalDatasetTopic);
                prepareFailedMessageCache();

               startConsumers(ac, externalDatasetTopic);
                
            } catch (Exception e) {
                LOGGER.error(e, e.getCause());
            }
            // @formatter:on
        }
    }

    private void startConsumers(ApplicationContext ac, String externalDatasetTopic) throws MetamacException {

        futuresMap = new HashMap<>();
        futuresMap.put(CONSUMER_DATASET_1_NAME, startConsumerForDatasetTopic(ac));
        futuresMap.put(CONSUMER_QUERY_1_NAME, startConsumerForQueryTopic(ac));
        futuresMap.put(CONSUMER_VARIABLE_ELEMENT_1_NAME, startConsumerForVariableElementTopic(ac));
        futuresMap.put(CONSUMER_CODELIST_1_NAME, startConsumerForCodelistTopic(ac));
        futuresMap.put(CONSUMER_CONCEPT_SCHEME_1_NAME, startConsumerForConceptSchemeTopic(ac));

        // only for organisations that have external dataset topic
        if (externalDatasetTopic != null) {
            futuresMap.put(CONSUMER_JAXI_DATASET_1_NAME, startConsumerForJaxiDatasetTopic(ac));
        }

        startKeepAliveKafkaThread(ac);
    }

    private String getExternalDatasetPublicationTopic() {
        try {
            // The topic can not be enabled in some environments.
            return configurationService.retrieveKafkaTopicExternalDatasetPublication();
        } catch (Exception e) {
            LOGGER.info("getExternalDatasetPublicationTopic in indicators not found. Check if must exists in common metadata");

        }
        return null;
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
    private Future<?> startConsumerForDatasetTopic(ApplicationContext context) throws MetamacException {
        String topicDatasetPublication = configurationService.retrieveKafkaTopicDatasetsPublication();
        KafkaConsumerThread<DatasetVersionAvro> consumerThread = (KafkaConsumerThread) context.getBean("kafkaConsumerThread");

        KafkaConsumer<String, DatasetVersionAvro> consumerFromBegin = createDatasetConsumerFromCurrentOffset(topicDatasetPublication, CONSUMER_DATASET_1_NAME);
        consumerThread.setConsumer(consumerFromBegin);
        consumerThread.setTopicName(topicDatasetPublication);
        consumerThread.setIndicatorsServiceFacade(indicatorsServiceFacade);
        consumerThread.setNoticesRestInternalService(noticesRestInternalService);
        consumerThread.setKafkaFailedMessagesCache(kafkaFailedMessagesCache);
        return threadPoolTaskExecutor.submit(consumerThread);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Future<?> startConsumerForJaxiDatasetTopic(ApplicationContext context) throws MetamacException {
        String topicDatasetPublication = configurationService.retrieveKafkaTopicExternalDatasetPublication();
        KafkaConsumerThread<DatasetAvro> consumerThread = (KafkaConsumerThread) context.getBean("kafkaConsumerThread");

        KafkaConsumer<String, DatasetAvro> consumerFromBegin = createJaxiDatasetConsumerFromCurrentOffset(topicDatasetPublication, CONSUMER_JAXI_DATASET_1_NAME);
        consumerThread.setConsumer(consumerFromBegin);
        consumerThread.setTopicName(topicDatasetPublication);
        consumerThread.setIndicatorsServiceFacade(indicatorsServiceFacade);
        consumerThread.setNoticesRestInternalService(noticesRestInternalService);
        consumerThread.setKafkaFailedMessagesCache(kafkaFailedMessagesCache);
        consumerThread.setIsJaxiConsumerDisabled(configurationService.retrieveJaxiPublicationConsumerIsDisabled());
        return threadPoolTaskExecutor.submit(consumerThread);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Future<?> startConsumerForQueryTopic(ApplicationContext context) throws MetamacException {
        String topicQueryPublication = configurationService.retrieveKafkaTopicQueryPublication();
        KafkaConsumerThread<QueryVersionAvro> consumerThread = (KafkaConsumerThread) context.getBean("kafkaConsumerThread");
        KafkaConsumer<String, QueryVersionAvro> consumerFromBegin = createQueryConsumerFromCurrentOffset(topicQueryPublication, CONSUMER_QUERY_1_NAME);
        consumerThread.setConsumer(consumerFromBegin);
        consumerThread.setTopicName(topicQueryPublication);
        consumerThread.setIndicatorsServiceFacade(indicatorsServiceFacade);
        consumerThread.setNoticesRestInternalService(noticesRestInternalService);
        consumerThread.setKafkaFailedMessagesCache(kafkaFailedMessagesCache);
        return threadPoolTaskExecutor.submit(consumerThread);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Future<?> startConsumerForVariableElementTopic(ApplicationContext context) throws MetamacException {
        String topicVariableElementPublication = configurationService.retrieveKafkaTopicVariableElementPublication();
        KafkaConsumerThread<VariableElementAvro> consumerThread = (KafkaConsumerThread) context.getBean("kafkaConsumerThread");
        KafkaConsumer<String, VariableElementAvro> consumerFromBegin = createVariableElementConsumerFromCurrentOffset(topicVariableElementPublication, CONSUMER_VARIABLE_ELEMENT_1_NAME);
        consumerThread.setConsumer(consumerFromBegin);
        consumerThread.setTopicName(topicVariableElementPublication);
        consumerThread.setIndicatorsServiceFacade(indicatorsServiceFacade);
        consumerThread.setNoticesRestInternalService(noticesRestInternalService);
        consumerThread.setKafkaFailedMessagesCache(kafkaFailedMessagesCache);
        return threadPoolTaskExecutor.submit(consumerThread);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Future<?> startConsumerForCodelistTopic(ApplicationContext context) throws MetamacException {
        String topicCodelistPublication = configurationService.retrieveKafkaTopicCodelistsPublication();
        KafkaConsumerThread<CodelistAvro> consumerThread = (KafkaConsumerThread) context.getBean("kafkaConsumerThread");
        KafkaConsumer<String, CodelistAvro> consumerFromBegin = createCodelistConsumerFromCurrentOffset(topicCodelistPublication, CONSUMER_CODELIST_1_NAME);
        consumerThread.setConsumer(consumerFromBegin);
        consumerThread.setTopicName(topicCodelistPublication);
        consumerThread.setIndicatorsServiceFacade(indicatorsServiceFacade);
        consumerThread.setNoticesRestInternalService(noticesRestInternalService);
        consumerThread.setKafkaFailedMessagesCache(kafkaFailedMessagesCache);
        return threadPoolTaskExecutor.submit(consumerThread);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Future<?> startConsumerForConceptSchemeTopic(ApplicationContext context) throws MetamacException {
        String topicConceptSchemePublication = configurationService.retrieveKafkaTopicConceptSchemesPublication();
        KafkaConsumerThread<ConceptSchemeAvro> consumerThread = (KafkaConsumerThread) context.getBean("kafkaConsumerThread");
        KafkaConsumer<String, ConceptSchemeAvro> consumerFromBegin = createConceptSchemeConsumerFromCurrentOffset(topicConceptSchemePublication, CONSUMER_CONCEPT_SCHEME_1_NAME);
        consumerThread.setConsumer(consumerFromBegin);
        consumerThread.setTopicName(topicConceptSchemePublication);
        consumerThread.setIndicatorsServiceFacade(indicatorsServiceFacade);
        consumerThread.setNoticesRestInternalService(noticesRestInternalService);
        consumerThread.setKafkaFailedMessagesCache(kafkaFailedMessagesCache);
        return threadPoolTaskExecutor.submit(consumerThread);
    }

    private Properties getConsumerProperties(String clientId, String group) throws MetamacException {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, configurationService.retrieveKafkaBootStrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, group);
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

    private KafkaConsumer<String, DatasetVersionAvro> createDatasetConsumerFromCurrentOffset(String topic, String clientId) throws MetamacException {
        KafkaConsumer<String, DatasetVersionAvro> kafkaConsumer = new KafkaConsumer<>(getConsumerProperties(clientId, configurationService.retrieveKafkaDatasetGroup()));
        kafkaConsumer.subscribe(Collections.singletonList(topic));
        return kafkaConsumer;
    }

    private KafkaConsumer<String, DatasetAvro> createJaxiDatasetConsumerFromCurrentOffset(String topic, String clientId) throws MetamacException {
        KafkaConsumer<String, DatasetAvro> kafkaConsumer = new KafkaConsumer<>(getConsumerProperties(clientId, configurationService.retrieveKafkaJaxiDatasetGroup()));
        kafkaConsumer.subscribe(Collections.singletonList(topic));
        return kafkaConsumer;
    }

    private KafkaConsumer<String, QueryVersionAvro> createQueryConsumerFromCurrentOffset(String topic, String clientId) throws MetamacException {
        KafkaConsumer<String, QueryVersionAvro> kafkaConsumer = new KafkaConsumer<>(getConsumerProperties(clientId, configurationService.retrieveKafkaQueryGroup()));
        kafkaConsumer.subscribe(Collections.singletonList(topic));
        return kafkaConsumer;
    }

    private KafkaConsumer<String, VariableElementAvro> createVariableElementConsumerFromCurrentOffset(String topic, String clientId) throws MetamacException {
        KafkaConsumer<String, VariableElementAvro> kafkaConsumer = new KafkaConsumer<>(getConsumerProperties(clientId, configurationService.retrieveKafkaVariableElementGroup()));
        kafkaConsumer.subscribe(Collections.singletonList(topic));
        return kafkaConsumer;
    }

    private KafkaConsumer<String, CodelistAvro> createCodelistConsumerFromCurrentOffset(String topic, String clientId) throws MetamacException {
        KafkaConsumer<String, CodelistAvro> kafkaConsumer = new KafkaConsumer<>(getConsumerProperties(clientId, configurationService.retrieveKafkaCodelistGroup()));
        kafkaConsumer.subscribe(Collections.singletonList(topic));
        return kafkaConsumer;
    }

    private KafkaConsumer<String, ConceptSchemeAvro> createConceptSchemeConsumerFromCurrentOffset(String topic, String clientId) throws MetamacException {
        KafkaConsumer<String, ConceptSchemeAvro> kafkaConsumer = new KafkaConsumer<>(getConsumerProperties(clientId, configurationService.retrieveKafkaConceptSchemeGroup()));
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
                                case CONSUMER_DATASET_1_NAME:
                                    futuresMap.put(CONSUMER_DATASET_1_NAME, startConsumerForDatasetTopic(ApplicationContextProvider.getApplicationContext()));
                                    break;
                                case CONSUMER_JAXI_DATASET_1_NAME:
                                    futuresMap.put(CONSUMER_JAXI_DATASET_1_NAME, startConsumerForJaxiDatasetTopic(ApplicationContextProvider.getApplicationContext()));
                                    break;
                                case CONSUMER_QUERY_1_NAME:
                                    futuresMap.put(CONSUMER_QUERY_1_NAME, startConsumerForQueryTopic(ApplicationContextProvider.getApplicationContext()));
                                    break;
                                case CONSUMER_VARIABLE_ELEMENT_1_NAME:
                                    futuresMap.put(CONSUMER_VARIABLE_ELEMENT_1_NAME, startConsumerForVariableElementTopic(ApplicationContextProvider.getApplicationContext()));
                                    break;
                                case CONSUMER_CODELIST_1_NAME:
                                    futuresMap.put(CONSUMER_CODELIST_1_NAME, startConsumerForCodelistTopic(ApplicationContextProvider.getApplicationContext()));
                                    break;
                                case CONSUMER_CONCEPT_SCHEME_1_NAME:
                                    futuresMap.put(CONSUMER_CONCEPT_SCHEME_1_NAME, startConsumerForConceptSchemeTopic(ApplicationContextProvider.getApplicationContext()));
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