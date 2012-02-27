package es.gobcan.istac.indicators.core.mapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
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
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsService;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsSystemsService;

@Component
public class Dto2DoMapperImpl implements Dto2DoMapper {

    @Autowired
    private InternationalStringRepository internationalStringRepository;
    
    @Autowired
    private IndicatorsSystemsService indicatorsSystemsService;

    @Autowired
    private IndicatorsService indicatorsService;

    @Override
    public IndicatorsSystem indicatorsSystemDtoToDo(ServiceContext ctx, IndicatorsSystemDto source, IndicatorsSystem target)  throws MetamacException {
        if (source == null) {
            return null;
        }
        if (target == null) {
            throw new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED);
        }
        target.setCode(source.getCode()); // non modifiable after creation
        return target;
    }

    @Override
    public IndicatorsSystemVersion indicatorsSystemDtoToDo(ServiceContext ctx, IndicatorsSystemDto source) throws MetamacException {
        if (source == null) {
            return null;
        }
        IndicatorsSystemVersion target = new IndicatorsSystemVersion();
        return indicatorsSystemDtoToDo(ctx, source, target);
    }

    @Override
    public IndicatorsSystemVersion indicatorsSystemDtoToDo(ServiceContext ctx, IndicatorsSystemDto source, IndicatorsSystemVersion target) throws MetamacException {

        if (source == null) {
            return null;
        }
        if (target == null) {
            throw new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED);
        }

        // Non modifiables after creation: code
        // Attributes non modifiables by user: state

        // Attributes modifiables
        target.setTitle(internationalStringToDo(ctx, source.getTitle(), target.getTitle()));
        target.setAcronym(internationalStringToDo(ctx, source.getAcronym(), target.getAcronym()));
        target.setDescription(internationalStringToDo(ctx, source.getDescription(), target.getDescription()));
        target.setObjetive(internationalStringToDo(ctx, source.getObjetive(), target.getObjetive()));
        target.setUriGopestat(source.getUriGopestat());

        return target;
    }
    
    @Override
    public Dimension dimensionDtoToDo(ServiceContext ctx, DimensionDto source) throws MetamacException {
        if (source == null) {
            return null;
        }
        
        Dimension target = new Dimension();
        target.setElementLevel(new ElementLevel());
        target.getElementLevel().setOrderInLevel(source.getOrderInLevel());
        target.getElementLevel().setIndicatorInstance(null);
        target.getElementLevel().setDimension(target);
        
        dimensionDtoToDo(ctx, source, target);
        
        return target;
    }

    @Override
    public void dimensionDtoToDo(ServiceContext ctx, DimensionDto source, Dimension target) throws MetamacException {
        if (source == null) {
            return;
        }
        if (target == null) {
            throw new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED);
        }
        target.setTitle(internationalStringToDo(ctx, source.getTitle(), target.getTitle()));
        if (source.getParentUuid() != null) {
            Dimension dimensionParent = indicatorsSystemsService.retrieveDimension(ctx, source.getParentUuid());
            target.getElementLevel().setParent(dimensionParent.getElementLevel());
        }
    }
        
    @Override
    public IndicatorInstance indicatorInstanceDtoToDo(ServiceContext ctx, IndicatorInstanceDto source) throws MetamacException {
        if (source == null) {
            return null;
        }
        
        IndicatorInstance target = new IndicatorInstance();
        
        target.setElementLevel(new ElementLevel());
        target.getElementLevel().setOrderInLevel(source.getOrderInLevel());
        target.getElementLevel().setIndicatorInstance(target);
        target.getElementLevel().setDimension(null);
        
        indicatorInstanceDtoToDo(ctx, source, target);
        
        return target;
    }
    
    @Override
    public void indicatorInstanceDtoToDo(ServiceContext ctx, IndicatorInstanceDto source, IndicatorInstance target) throws MetamacException {
        if (source == null) {
            return;
        }
        if (target == null) {
            throw new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED);
        }
        target.setTitle(internationalStringToDo(ctx, source.getTitle(), target.getTitle()));
        target.setGeographicGranularityId(source.getGeographicGranularityId());
        target.setGeographicValue(source.getGeographicValue());
        target.setTemporaryGranularityId(source.getTemporaryGranularityId());
        target.setTemporaryValue(source.getTemporaryValue());
        if (source.getParentUuid() != null) {
            Dimension dimensionParent = indicatorsSystemsService.retrieveDimension(ctx, source.getParentUuid());
            target.getElementLevel().setParent(dimensionParent.getElementLevel());
        }
        if (source.getIndicatorUuid() != null) {
            Indicator indicator = indicatorsService.retrieveIndicatorBorrar(ctx, source.getIndicatorUuid());
            target.setIndicator(indicator);
        }
    }

    @Override
    public Indicator indicatorDtoToDo(ServiceContext ctx, IndicatorDto source, Indicator target) throws MetamacException {
        if (source == null) {
            return null;
        }
        if (target == null) {
            throw new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED);
        }
        target.setCode(source.getCode()); // non modifiable after creation
        return target;
    }

    @Override
    public IndicatorVersion indicatorDtoToDo(ServiceContext ctx, IndicatorDto source) throws MetamacException {
        if (source == null) {
            return null;
        }
        
        IndicatorVersion target = new IndicatorVersion();
        return indicatorDtoToDo(ctx, source, target);
    }

    @Override
    public IndicatorVersion indicatorDtoToDo(ServiceContext ctx, IndicatorDto source, IndicatorVersion target) throws MetamacException {
        if (source == null) {
            return null;
        }
        if (target == null) {
            throw new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED);
        }

        // Non modifiables after creation: code
        // Attributes non modifiables by user: states

        // Attributes modifiables
        target.setName(internationalStringToDo(ctx, source.getName(), target.getName()));
        target.setAcronym(internationalStringToDo(ctx, source.getAcronym(), target.getAcronym()));
        target.setCommentary(internationalStringToDo(ctx, source.getCommentary(), target.getCommentary()));
        target.setSubjectCode(source.getSubjectCode());
        target.setNotes(internationalStringToDo(ctx, source.getNotes(), target.getNotes()));
        target.setNoteUrl(source.getNoteUrl());
        
        return target;
    }
    
    @Override
    public DataSource dataSourceDtoToDo(ServiceContext ctx, DataSourceDto source) throws MetamacException {
        if (source == null) {
            return null;
        }
        DataSource target = new DataSource();
        dataSourceDtoToDo(ctx, source, target);
        return target;
    }

    @Override
    public void dataSourceDtoToDo(ServiceContext ctx, DataSourceDto source, DataSource target) throws MetamacException {
        if (source == null) {
            return;
        }
        if (target == null) {
            throw new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED);
        }
        target.setQueryGpe(source.getQueryGpe());
        target.setPx(source.getPx());
        target.setTemporaryVariable(source.getTemporaryVariable());
        target.setGeographicVariable(source.getGeographicVariable());
        
        List<DataSourceVariable> variables = dataSourceVariableDtoToDo(ctx, source.getOtherVariables(), target.getOtherVariables());
        target.getOtherVariables().clear();
        target.getOtherVariables().addAll(variables);
    }
    
    private InternationalString internationalStringToDo(ServiceContext ctx, InternationalStringDto source, InternationalString target) {
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

        Set<LocalisedString> localisedStringEntities = localisedStringDtoToDo(ctx, source.getTexts(), target.getTexts());
        target.getTexts().clear();
        target.getTexts().addAll(localisedStringEntities);

        return target;
    }

    /**
     * Transform LocalisedString, reusing existing locales
     */
    private Set<LocalisedString> localisedStringDtoToDo(ServiceContext ctx, Set<LocalisedStringDto> sources, Set<LocalisedString> targets) {

        Set<LocalisedString> targetsBefore = targets;
        targets = new HashSet<LocalisedString>();

        for (LocalisedStringDto source : sources) {
            boolean existsBefore = false;
            for (LocalisedString target : targetsBefore) {
                if (source.getLocale().equals(target.getLocale())) {
                    targets.add(localisedStringDtoToDo(ctx, source, target));
                    existsBefore = true;
                    break;
                }
            }
            if (!existsBefore) {
                targets.add(localisedStringDtoToDo(ctx, source));
            }
        }
        return targets;
    }

    private LocalisedString localisedStringDtoToDo(ServiceContext ctx, LocalisedStringDto source) {
        LocalisedString target = new LocalisedString();
        target.setLabel(source.getLabel());
        target.setLocale(source.getLocale());
        return target;
    }

    private LocalisedString localisedStringDtoToDo(ServiceContext ctx, LocalisedStringDto source, LocalisedString target) {
        target.setLabel(source.getLabel());
        target.setLocale(source.getLocale());
        return target;
    }
    
    /**
     * Transform DataSourceVariable, reusing existing variables
     */
    private List<DataSourceVariable> dataSourceVariableDtoToDo(ServiceContext ctx, List<DataSourceVariableDto> sources, List<DataSourceVariable> targets) {

        List<DataSourceVariable> targetsBefore = targets;
        targets = new ArrayList<DataSourceVariable>();

        for (DataSourceVariableDto source : sources) {
            boolean existsBefore = false;
            for (DataSourceVariable target : targetsBefore) {
                if (source.getVariable().equals(target.getVariable())) {
                    targets.add(dataSourceVariableDtoToDo(ctx, source, target));
                    existsBefore = true;
                    break;
                }
            }
            if (!existsBefore) {
                targets.add(dataSourceVariableDtoToDo(ctx, source));
            }
        }
        return targets;
    }
    
    private DataSourceVariable dataSourceVariableDtoToDo(ServiceContext ctx, DataSourceVariableDto source) {
        DataSourceVariable target = new DataSourceVariable();
        target = dataSourceVariableDtoToDo(ctx, source, target);
        return target;
    }

    private DataSourceVariable dataSourceVariableDtoToDo(ServiceContext ctx, DataSourceVariableDto source, DataSourceVariable target) {
        target.setVariable(source.getVariable());
        target.setCategory(source.getCategory());
        return target;
    }
}
