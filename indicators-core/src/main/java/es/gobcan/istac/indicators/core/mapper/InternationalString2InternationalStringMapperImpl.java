package es.gobcan.istac.indicators.core.mapper;

import java.util.List;

import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InternationalString2InternationalStringMapperImpl implements InternationalString2InternationalStringMapper {

    @Autowired
    LocalisedString2LocalisedStringMapper localisedString2LocalisedStringMapper;

    public InternationalString internationalString2InternationalString(org.siemac.metamac.rest.common.v1_0.domain.InternationalString source) {
        if (source != null) {
            InternationalString internationalString = new InternationalString();
            List<LocalisedString> localisedStringList = source.getTexts();
            for (LocalisedString localisedStringSource : localisedStringList) {
                org.siemac.metamac.core.common.ent.domain.LocalisedString localisedString = localisedString2LocalisedStringMapper.localisedString2LocalisedString(localisedStringSource);
                internationalString.addText(localisedString);
            }
            return internationalString;
        }
        return null;
    }
}
