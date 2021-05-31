package es.gobcan.istac.indicators.core.service.stream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsOptions;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.errors.TopicExistsException;
import org.apache.kafka.common.serialization.StringSerializer;
import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import io.confluent.kafka.serializers.KafkaAvroSerializer;

public class KafkaInitializeTopics {

    protected static final Log               LOGGER             = LogFactory.getLog(KafkaInitializeTopics.class);

    // Create Topics Request
    private static final int                 NUM_OF_PARTITIONS  = 1;
    private static final short               NUM_OF_REPLICATION = (short) 1;
    private static final int                 TIMEOUT            = 1000;

    private static final String              RETENTION_MS       = "retention.ms";

    private static final Map<String, String> TOPIC_DEFAULT_SETTINGS;

    static {
        TOPIC_DEFAULT_SETTINGS = new HashMap<>();
        TOPIC_DEFAULT_SETTINGS.put(RETENTION_MS, "-1");
    };

    private KafkaInitializeTopics() {

    }

    public static void propagateCreationOfTopics(IndicatorsConfigurationService configurationService) throws MetamacException {
        Properties kafkaProperties = getKafkaProperties(configurationService);

        List<NewTopic> topics = getTopics(configurationService);

        CreateTopicsOptions topicsOptions = getTopicsOptions();

        createTopics(kafkaProperties, topics, topicsOptions);
    }

    private static Properties getKafkaProperties(IndicatorsConfigurationService configurationService) throws MetamacException {
        Properties properties = new Properties();

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, configurationService.retrieveKafkaBootStrapServers());
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);

        return properties;
    }

    private static List<NewTopic> getTopics(IndicatorsConfigurationService configurationService) throws MetamacException {
        List<NewTopic> topics = new ArrayList<>();

        topics.add(createTopic(configurationService.retrieveKafkaTopicQueryPublication()));
        topics.add(createTopic(configurationService.retrieveKafkaTopicDatasetsPublication()));
        topics.add(createTopic(configurationService.retrieveKafkaTopicCollectionPublication()));

        return topics;
    }

    private static NewTopic createTopic(String topic) {
        return new NewTopic(topic, NUM_OF_PARTITIONS, NUM_OF_REPLICATION).configs(TOPIC_DEFAULT_SETTINGS);
    }

    private static CreateTopicsOptions getTopicsOptions() {
        return new CreateTopicsOptions().timeoutMs(TIMEOUT);
    }

    private static void createTopics(Properties kafkaProperties, List<NewTopic> topics, CreateTopicsOptions topicsOptions) {
        try (AdminClient adminClient = AdminClient.create(kafkaProperties)) {
            adminClient.createTopics(topics, topicsOptions).all().get();
        } catch (InterruptedException | ExecutionException e) {
            // Ignore if TopicExistsException, which may be valid if topic exists
            if (!(e.getCause() instanceof TopicExistsException)) {
                throw new RuntimeException("Imposible to create/check Topic in kafka", e);
            }
        }
    }
}
