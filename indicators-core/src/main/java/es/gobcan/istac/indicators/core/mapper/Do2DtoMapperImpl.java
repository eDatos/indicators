package es.gobcan.istac.indicators.core.mapper;

import java.util.HashSet;
import java.util.Set;

import org.siemac.metamac.core.common.dto.serviceapi.InternationalStringDto;
import org.siemac.metamac.core.common.dto.serviceapi.LocalisedStringDto;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.springframework.stereotype.Component;

import es.gobcan.istac.indicators.core.domain.IndicatorSystemVersion;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorSystemDto;

@Component
public class Do2DtoMapperImpl implements Do2DtoMapper {

    @Override
    public IndicatorSystemDto indicatorSystemDoToDto(IndicatorSystemVersion source) {

        IndicatorSystemDto target = new IndicatorSystemDto();

        target.setUuid(source.getUuid());
        target.setVersionNumber(source.getVersionNumber());
        target.setCode(source.getIndicatorSystem().getCode());
        target.setTitle(internationalStringToDto(source.getTitle()));
        target.setAcronym(internationalStringToDto(source.getAcronym()));
        target.setUri(source.getUri());
        target.setObjetive(internationalStringToDto(source.getObjetive()));
        target.setDescription(internationalStringToDto(source.getDescription()));
        target.setState(source.getState());
        
        target.setCreatedBy(source.getCreatedBy());
        target.setCreatedDate(source.getCreatedDate().toDate());
        target.setLastUpdatedBy(source.getLastUpdatedBy());
        target.setLastUpdated(source.getLastUpdated().toDate());
        
        return target;
    }
    
    private InternationalStringDto internationalStringToDto(InternationalString source) {
        if (source == null) {
            return null;
        }        
        InternationalStringDto target = new InternationalStringDto();
        target.getTexts().addAll(localisedStringDoToDto(source.getTexts()));
        return target;
    }

    private Set<LocalisedStringDto> localisedStringDoToDto(Set<LocalisedString> sources) {
        Set<LocalisedStringDto> targets = new HashSet<LocalisedStringDto>();
        for (LocalisedString source : sources) {
            LocalisedStringDto target = new LocalisedStringDto();
            target.setLabel(source.getLabel());
            target.setLocale(source.getLocale());
            targets.add(target);
        }
        return targets;
    }
}
