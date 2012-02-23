package es.gobcan.istac.indicators.core.mapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.siemac.metamac.core.common.dto.serviceapi.InternationalStringDto;
import org.siemac.metamac.core.common.dto.serviceapi.LocalisedStringDto;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.InternationalStringRepository;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.indicators.core.domain.DataSource;
import es.gobcan.istac.indicators.core.domain.DataSourceVariable;
import es.gobcan.istac.indicators.core.domain.Dimension;
import es.gobcan.istac.indicators.core.domain.ElementLevel;
import es.gobcan.istac.indicators.core.domain.Indicator;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystem;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.dto.serviceapi.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.DataSourceVariableDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.DimensionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorInstanceDto;
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
            throw new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED);
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
    public ElementLevel dimensionDtoToDo(DimensionDto source) {
        ElementLevel target = new ElementLevel();
        target.setOrderInLevel(source.getOrderInLevel());
        target.setIndicatorInstance(null);
        
        // Dimension
        target.setDimension(new Dimension());
        target.getDimension().setElementLevel(target);
        dimensionDtoToDo(source, target.getDimension());
        
        return target;
    }

    @Override
    public void dimensionDtoToDo(DimensionDto source, Dimension target) {
        target.setTitle(internationalStringToDo(source.getTitle(), target.getTitle()));
    }
        
    @Override
    public ElementLevel indicatorInstanceDtoToDo(IndicatorInstanceDto source) {
        ElementLevel target = new ElementLevel();
        target.setOrderInLevel(source.getOrderInLevel());
        target.setDimension(null);
        
        // Indicator instance
        target.setIndicatorInstance(new IndicatorInstance());
        target.getIndicatorInstance().setElementLevel(target);
        indicatorInstanceDtoToDo(source, target.getIndicatorInstance());
        return target;
    }
    
    @Override
    public void indicatorInstanceDtoToDo(IndicatorInstanceDto source, IndicatorInstance target) {
        target.setIndicatorUuid(source.getIndicatorUuid());
        target.setTitle(internationalStringToDo(source.getTitle(), target.getTitle()));
        target.setGeographicGranularityId(source.getGeographicGranularityId());
        target.setGeographicValue(source.getGeographicValue());
        target.setTemporaryGranularityId(source.getTemporaryGranularityId());
        target.setTemporaryValue(source.getTemporaryValue());
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
            throw new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED);
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
    
    @Override
    public DataSource dataSourceDtoToDo(DataSourceDto source) {
        DataSource target = new DataSource();
        dataSourceDtoToDo(source, target);
        return target;
    }

    @Override
    public void dataSourceDtoToDo(DataSourceDto source, DataSource target) {
        target.setQueryGpe(source.getQueryGpe());
        target.setPx(source.getPx());
        target.setTemporaryVariable(source.getTemporaryVariable());
        target.setGeographicVariable(source.getGeographicVariable());
        
        List<DataSourceVariable> variables = dataSourceVariableDtoToDo(source.getOtherVariables(), target.getOtherVariables());
        target.getOtherVariables().clear();
        target.getOtherVariables().addAll(variables);
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
    
    /**
     * Transform DataSourceVariable, reusing existing variables
     */
    private List<DataSourceVariable> dataSourceVariableDtoToDo(List<DataSourceVariableDto> sources, List<DataSourceVariable> targets) {

        List<DataSourceVariable> targetsBefore = targets;
        targets = new ArrayList<DataSourceVariable>();

        for (DataSourceVariableDto source : sources) {
            boolean existsBefore = false;
            for (DataSourceVariable target : targetsBefore) {
                if (source.getVariable().equals(target.getVariable())) {
                    targets.add(dataSourceVariableDtoToDo(source, target));
                    existsBefore = true;
                    break;
                }
            }
            if (!existsBefore) {
                targets.add(dataSourceVariableDtoToDo(source));
            }
        }
        return targets;
    }
    
    private DataSourceVariable dataSourceVariableDtoToDo(DataSourceVariableDto source) {
        DataSourceVariable target = new DataSourceVariable();
        target = dataSourceVariableDtoToDo(source, target);
        return target;
    }

    private DataSourceVariable dataSourceVariableDtoToDo(DataSourceVariableDto source, DataSourceVariable target) {
        target.setVariable(source.getVariable());
        target.setCategory(source.getCategory());
        return target;
    }
}
