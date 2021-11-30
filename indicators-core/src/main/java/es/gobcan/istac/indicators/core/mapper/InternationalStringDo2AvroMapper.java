package es.gobcan.istac.indicators.core.mapper;

import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.siemac.metamac.indicators.core.stream.message.InternationalStringAvro;
import org.siemac.metamac.indicators.core.stream.message.LocalisedStringAvro;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InternationalStringDo2AvroMapper implements Do2AvroMapper<InternationalString, InternationalStringAvro> {

    @Override
    public InternationalStringAvro toAvro(InternationalString source) {
        InternationalStringAvro target = null;
        if (source != null && source.getTexts() != null) {
            List<LocalisedStringAvro> avroLocalisedStrings = new ArrayList<>();
            for (LocalisedString localisedString : source.getTexts()) {
                LocalisedStringAvro avroLocalisedString = LocalisedStringAvro.newBuilder()
                        .setLocale(localisedString.getLocale())
                        .setLabel(localisedString.getLabel())
                        .build();
                avroLocalisedStrings.add(avroLocalisedString);
            }
            target = InternationalStringAvro.newBuilder().setLocalisedStrings(avroLocalisedStrings).build();
        }
        return target;

    }
}
