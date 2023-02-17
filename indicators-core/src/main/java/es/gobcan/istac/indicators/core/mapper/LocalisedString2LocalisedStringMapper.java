package es.gobcan.istac.indicators.core.mapper;

import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;

public interface LocalisedString2LocalisedStringMapper {

    org.siemac.metamac.core.common.ent.domain.LocalisedString localisedString2LocalisedString(LocalisedString source);
}
