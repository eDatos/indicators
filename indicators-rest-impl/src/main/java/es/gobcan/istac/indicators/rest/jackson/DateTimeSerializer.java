package es.gobcan.istac.indicators.rest.jackson;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.joda.time.DateTime;

public class DateTimeSerializer extends JsonSerializer<DateTime> {

    @Override
    public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeString(value.toString());
    }
}
