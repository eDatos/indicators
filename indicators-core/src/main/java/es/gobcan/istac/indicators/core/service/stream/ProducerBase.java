package es.gobcan.istac.indicators.core.service.stream;

import es.gobcan.istac.indicators.core.dto.stream.MessageBase;
import org.apache.avro.specific.SpecificRecordBase;
import org.siemac.metamac.core.common.exception.MetamacException;

public interface ProducerBase<K, V extends SpecificRecordBase> {
    void sendMessage(MessageBase<K, V> m, String topic) throws MetamacException;
    void close();
}