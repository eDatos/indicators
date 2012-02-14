package es.gobcan.istac.indicators.core.mapper;

import java.util.HashSet;
import java.util.Set;

import org.siemac.metamac.core.common.dto.serviceapi.InternationalStringDto;
import org.siemac.metamac.core.common.dto.serviceapi.LocalisedStringDto;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.InternationalStringRepository;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.indicators.core.domain.Dimension;
import es.gobcan.istac.indicators.core.domain.Indicator;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystem;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.dto.serviceapi.DimensionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;

@Component
public class Dto2DoMapperImpl implements Dto2DoMapper {

    @Autowired
    private InternationalStringRepository internationalStringRepository;

    @Override
    public IndicatorsSystem indicatorsSystemDtoToDo(IndicatorsSystemDto source, IndicatorsSystem target)  throws MetamacException {
        target.setCode(source.getCode()); // non modifiable after creation
        return target;
    }

    @Override
    public IndicatorsSystemVersion indicatorsSystemDtoToDo(IndicatorsSystemDto source) throws MetamacException {
        IndicatorsSystemVersion target = new IndicatorsSystemVersion();
        return indicatorsSystemDtoToDo(source, target);
    }

    @Override
    public IndicatorsSystemVersion indicatorsSystemDtoToDo(IndicatorsSystemDto source, IndicatorsSystemVersion target) throws MetamacException {

        if (source == null) {
            return null;
        }
        if (target == null) {
            throw new MetamacException(ServiceExceptionType.SERVICE_PARAMETER_REQUIRED);
        }

        // Non modifiables after creation: code
        // Attributes non modifiables by user: state

        // Attributes modifiables
        target.setTitle(internationalStringToDo(source.getTitle(), target.getTitle()));
        target.setAcronym(internationalStringToDo(source.getAcronym(), target.getAcronym()));
        target.setDescription(internationalStringToDo(source.getDescription(), target.getDescription()));
        target.setObjetive(internationalStringToDo(source.getObjetive(), target.getObjetive()));
        target.setUriGopestat(source.getUriGopestat());

        return target;
    }
    
    @Override
    public Dimension dimensionDtoToDo(DimensionDto source) {
        Dimension target = new Dimension();
        dimensionDtoToDo(source, target);
        return target;
    }

    @Override
    public void dimensionDtoToDo(DimensionDto source, Dimension target) {
        target.setTitle(internationalStringToDo(source.getTitle(), target.getTitle()));
        target.setOrderInLevel(source.getOrderInLevel());
        // Update action never updates dimensions children
    }

    @Override
    public Indicator indicatorDtoToDo(IndicatorDto source, Indicator target) {
        target.setCode(source.getCode()); // non modifiable after creation
        return target;
    }

    @Override
    public IndicatorVersion indicatorDtoToDo(IndicatorDto source) throws MetamacException {
        IndicatorVersion target = new IndicatorVersion();
        return indicatorDtoToDo(source, target);
    }

    @Override
    public IndicatorVersion indicatorDtoToDo(IndicatorDto source, IndicatorVersion target) throws MetamacException {

        if (source == null) {
            return null;
        }
        if (target == null) {
            throw new MetamacException(ServiceExceptionType.SERVICE_PARAMETER_REQUIRED);
        }

        // Non modifiables after creation: code
        // Attributes non modifiables by user: states

        // Attributes modifiables
        target.setName(internationalStringToDo(source.getName(), target.getName()));
        target.setAcronym(internationalStringToDo(source.getAcronym(), target.getAcronym()));
        target.setCommentary(internationalStringToDo(source.getCommentary(), target.getCommentary()));
        target.setSubjectCode(source.getSubjectCode());
        target.setNotes(internationalStringToDo(source.getNotes(), target.getNotes()));
        target.setNoteUrl(source.getNoteUrl());
        
        return target;
    }
    
    private InternationalString internationalStringToDo(InternationalStringDto source, InternationalString target) {
        if (source == null) {
            if (target != null) {
                // delete previous entity
                internationalStringRepository.delete(target);
            }
            return null;
        }

        if (target == null) {
            target = new InternationalString();
        }

        Set<LocalisedString> localisedStringEntities = localisedStringDtoToDo(source.getTexts(), target.getTexts());
        target.getTexts().clear();
        target.getTexts().addAll(localisedStringEntities);

        return target;
    }

    /**
     * Transform LocalisedString, reusing existing locales
     */
    private Set<LocalisedString> localisedStringDtoToDo(Set<LocalisedStringDto> sources, Set<LocalisedString> targets) {

        Set<LocalisedString> targetsBefore = targets;
        targets = new HashSet<LocalisedString>();

        for (LocalisedStringDto source : sources) {
            boolean existsBefore = false;
            for (LocalisedString target : targetsBefore) {
                if (source.getLocale().equals(target.getLocale())) {
                    targets.add(localisedStringDtoToDo(source, target));
                    existsBefore = true;
                    break;
                }
            }
            if (!existsBefore) {
                targets.add(localisedStringDtoToDo(source));
            }
        }
        return targets;
    }

    private LocalisedString localisedStringDtoToDo(LocalisedStringDto source) {
        LocalisedString target = new LocalisedString();
        target.setLabel(source.getLabel());
        target.setLocale(source.getLocale());
        return target;
    }

    private LocalisedString localisedStringDtoToDo(LocalisedStringDto source, LocalisedString target) {
        target.setLabel(source.getLabel());
        target.setLocale(source.getLocale());
        return target;
    }
}
