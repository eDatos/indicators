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
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;

@Component
@Scope("prototype")
public class KafkaConsumerThread<T extends SpecificRecordBase> implements Runnable {

    protected static Log LOGGER = LogFactory.getLog(KafkaConsumerThread.class);

    private KafkaConsumer<String, T> consumer;
    private String                   topicName;
    private IndicatorsServiceFacade  indicatorsServiceFacade;

    public void setConsumer(KafkaConsumer<String, T> consumer) {
        this.consumer = consumer;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public void setIndicatorsServiceFacade(IndicatorsServiceFacade indicatorsServiceFacade) {
        this.indicatorsServiceFacade = indicatorsServiceFacade;
    }

    @Override
    public void run() {
        LOGGER.debug("Reading KAFKA topic: " + topicName);

        try {
            
            Map<Integer, Long> pendigOffsetsToCommit = new HashMap<Integer, Long>(); // K:partition, V:offset
            
            while (alwaysWithDelay()) {
                ConsumerRecords<String, T> records = consumer.poll(100); // milliseconds, spent waiting in poll if data is not available in the buffer

                if (records.count() > 1) {
                    String msg = "Tenemos configurado un poll de 1 mensaje como máximo. Este error no puede darse";
                    LOGGER.error(msg);
                    throw new RuntimeException(msg);
                }

                if (records.isEmpty()) {
                    continue;
                }

                // Process resources
                ConsumerRecord<String, T> record = records.iterator().next();

                if (pendigOffsetsToCommit.containsKey(record.partition()) && record.offset() == pendigOffsetsToCommit.get(record.partition())) {
                    System.out.println("El mensaje actual ya lo procesamos satisfactoriamente");
                    if (commitSync(record)) {
                        pendigOffsetsToCommit.remove(record.partition());
                    }
                    continue;
                }

                StringBuilder logMessageBldr = new StringBuilder("Recibido mensaje desde KAFKA -> Topic Name: ");
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
                    LOGGER.error("Imposible procesar recurso recibido desde KAFKA", e);

                    // TODO METAMAC-2503 Que hacemos con los mensajes que provocan error siempre (por ej pq el mensaje está mal, pq nuestra lógica está mal, etc)
                    // Los errores por servicios caidos y demás se supone que en algun intento volveran a funcionar. El problema son los que nunca funcionaran y que
                    // impiden el avance del resto de mensajes de la cola.
                    pendigOffsetsToCommit.remove(record.partition()); // Para no commitearlo en la siguiente iteracion
                }

            }
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
            // The message processing takes longer than the session timeout. The coordinator kicks the consumer out of the group (rebalanced)
            LOGGER.debug("The message processing takes longer than the session timeout. The coordinator kicks the consumer out of the group (rebalanced)");
            return false;
        }
        return true;
    }

    private boolean alwaysWithDelay() {
        try {
            // sleep(2000);
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            LOGGER.error(e);
        }
        return true;
    }

}
