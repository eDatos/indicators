package es.gobcan.istac.indicators.core.mapper;

import org.joda.time.DateTime;
import org.siemac.metamac.indicators.core.stream.message.DatetimeAvro;
import org.springframework.stereotype.Component;

@Component
public class DatetimeDo2AvroMapper implements Do2AvroMapper<DateTime, DatetimeAvro> {
    @Override
    public DatetimeAvro toAvro(DateTime source) {
        if (source == null) {
            return null;
        }
        return DatetimeAvro.newBuilder().setInstant(source.toInstant().getMillis()).setTimezone(source.getZone().getID()).build();
    }
}
