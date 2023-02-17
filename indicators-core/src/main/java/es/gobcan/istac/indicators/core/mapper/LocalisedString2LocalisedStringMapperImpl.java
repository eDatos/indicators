package es.gobcan.istac.indicators.core.mapper;

import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.springframework.stereotype.Component;

@Component
public class LocalisedString2LocalisedStringMapperImpl implements LocalisedString2LocalisedStringMapper {

    public org.siemac.metamac.core.common.ent.domain.LocalisedString localisedString2LocalisedString(LocalisedString source) {
        org.siemac.metamac.core.common.ent.domain.LocalisedString localisedString = new org.siemac.metamac.core.common.ent.domain.LocalisedString();
        localisedString.setLocale(source.getLang());
        localisedString.setLabel(source.getValue());
        return localisedString;
    }
}
