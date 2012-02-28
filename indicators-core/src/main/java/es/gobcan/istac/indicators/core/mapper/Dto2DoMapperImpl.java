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
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.core.common.serviceimpl.utils.ValidationUtils;
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
    private IndicatorsSystemsService      indicatorsSystemsService;

    @Autowired
    private IndicatorsService             indicatorsService;

    @Override
    public IndicatorsSystemVersion indicatorsSystemDtoToDo(ServiceContext ctx, IndicatorsSystemDto source) throws MetamacException {

        if (source == null) {
            return null;
        }

        // If exists, retrieves existing entity. Otherwise, creates new entity
        IndicatorsSystemVersion target = null;
        if (source.getUuid() == null) {
            target = new IndicatorsSystemVersion();
            target.setIndicatorsSystem(new IndicatorsSystem());
            // non modifiable after creation
            target.getIndicatorsSystem().setCode(source.getCode());
        } else {
            target = indicatorsSystemsService.retrieveIndicatorsSystem(ctx, source.getUuid(), source.getVersionNumber());

            // Metadata unmodifiable
            List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();
            ValidationUtils.checkMetadataUnmodifiable(target.getIndicatorsSystem().getCode(), source.getCode(), "INDICATORS_SYSTEM.CODE", exceptions);
            ExceptionUtils.throwIfException(exceptions);
        }

        // Attributes modifiables
        target.setTitle(internationalStringToDo(ctx, source.getTitle(), target.getTitle(), "INDICATORS_SYSTEM.TITLE"));
        target.setAcronym(internationalStringToDo(ctx, source.getAcronym(), target.getAcronym(), "INDICATORS_SYSTEM.ACRONYM"));
        target.setDescription(internationalStringToDo(ctx, source.getDescription(), target.getDescription(), "INDICATORS_SYSTEM.DESCRIPTION"));
        target.setObjetive(internationalStringToDo(ctx, source.getObjetive(), target.getObjetive(), "INDICATORS_SYSTEM.OBJETIVE"));
        target.setUriGopestat(source.getUriGopestat());

        return target;
    }

    @Override
    public Dimension dimensionDtoToDo(ServiceContext ctx, DimensionDto source) throws MetamacException {
        if (source == null) {
            return null;
        }

        // If exists, retrieves existing entity. Otherwise, creates new entity
        Dimension target = null;
        if (source.getUuid() == null) {
            target = new Dimension();
            target.setElementLevel(new ElementLevel());
            target.getElementLevel().setOrderInLevel(source.getOrderInLevel());
            target.getElementLevel().setIndicatorInstance(null);
            target.getElementLevel().setDimension(target);
        } else {
            target = indicatorsSystemsService.retrieveDimension(ctx, source.getUuid());

            // Metadata unmodifiable
            List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();
            // This metadatas are modified in specific operation
            ValidationUtils.checkMetadataUnmodifiable(source.getParentUuid(), target.getElementLevel().getParentUuid(), "DIMENSION.PARENT_UUID", exceptions);
            ValidationUtils.checkMetadataUnmodifiable(source.getOrderInLevel(), target.getElementLevel().getOrderInLevel(), "DIMENSION.ORDER_IN_LEVEL", exceptions);
            ExceptionUtils.throwIfException(exceptions);
        }

        // Metadata modifiable
        target.setTitle(internationalStringToDo(ctx, source.getTitle(), target.getTitle(), "DIMENSION.TITLE"));

        // Related entities
        if (source.getParentUuid() != null) {
            Dimension dimensionParent = indicatorsSystemsService.retrieveDimension(ctx, source.getParentUuid());
            target.getElementLevel().setParent(dimensionParent.getElementLevel());
        }

        return target;
    }

    @Override
    public IndicatorInstance indicatorInstanceDtoToDo(ServiceContext ctx, IndicatorInstanceDto source) throws MetamacException {
        if (source == null) {
            return null;
        }

        // If exists, retrieves existing entity. Otherwise, creates new entity
        IndicatorInstance target = null;
        if (source.getUuid() == null) {
            target = new IndicatorInstance();

            target.setElementLevel(new ElementLevel());
            target.getElementLevel().setOrderInLevel(source.getOrderInLevel());
            target.getElementLevel().setIndicatorInstance(target);
            target.getElementLevel().setDimension(null);
        } else {
            target = indicatorsSystemsService.retrieveIndicatorInstance(ctx, source.getUuid());

            // Metadata unmodifiable (these metadatas are modified in specific operation)
            List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

            ValidationUtils.checkMetadataUnmodifiable(source.getIndicatorUuid(), target.getIndicator().getUuid(), "INDICATOR_INSTANCE.INDICATOR_UUID", exceptions);
            ValidationUtils.checkMetadataUnmodifiable(source.getParentUuid(), target.getElementLevel().getParentUuid(), "INDICATOR_INSTANCE.PARENT_UUID", exceptions);
            ValidationUtils.checkMetadataUnmodifiable(source.getOrderInLevel(), target.getElementLevel().getOrderInLevel(), "INDICATOR_INSTANCE.ORDER_IN_LEVEL", exceptions);
            ExceptionUtils.throwIfException(exceptions);
        }

        // Metadata modifiable
        target.setTitle(internationalStringToDo(ctx, source.getTitle(), target.getTitle(), "INDICATOR_INSTANCE.TITLE"));
        target.setGeographicGranularityId(source.getGeographicGranularityId());
        target.setGeographicValue(source.getGeographicValue());
        target.setTemporaryGranularityId(source.getTemporaryGranularityId());
        target.setTemporaryValue(source.getTemporaryValue());

        // Related entities
        if (source.getParentUuid() != null) {
            Dimension dimensionParent = indicatorsSystemsService.retrieveDimension(ctx, source.getParentUuid());
            target.getElementLevel().setParent(dimensionParent.getElementLevel());
        }
        if (source.getIndicatorUuid() != null) {
            Indicator indicator = indicatorsService.retrieveIndicatorBorrar(ctx, source.getIndicatorUuid());
            target.setIndicator(indicator);
        }

        return target;
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
        target.setName(internationalStringToDo(ctx, source.getName(), target.getName(), "INDICATOR.NAME"));
        target.setAcronym(internationalStringToDo(ctx, source.getAcronym(), target.getAcronym(), "INDICATOR.ACRONYM"));
        target.setCommentary(internationalStringToDo(ctx, source.getCommentary(), target.getCommentary(), "INDICATOR.COMMENTARY"));
        target.setSubjectCode(source.getSubjectCode());
        target.setNotes(internationalStringToDo(ctx, source.getNotes(), target.getNotes(), "INDICATOR.NOTES"));
        target.setNoteUrl(source.getNoteUrl());

        return target;
    }

    @Override
    public DataSource dataSourceDtoToDo(ServiceContext ctx, DataSourceDto source) throws MetamacException {
        if (source == null) {
            return null;
        }

        // If exists, retrieves existing entity. Otherwise, creates new entity
        DataSource target = null;
        if (source.getUuid() == null) {
            target = new DataSource();

            // Metadata unmodifiable
            target.setQueryGpe(source.getQueryGpe());
            target.setPx(source.getPx());
        } else {
            target = indicatorsService.retrieveDataSource(ctx, source.getUuid());

            // Metadata unmodifiable
            List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();
            ValidationUtils.checkMetadataUnmodifiable(target.getQueryGpe(), source.getQueryGpe(), "DATA_SOURCE.QUERY_GPE", exceptions);
            ValidationUtils.checkMetadataUnmodifiable(target.getPx(), source.getPx(), "DATA_SOURCE.PX", exceptions);
            ExceptionUtils.throwIfException(exceptions);
        }

        // Metadata modifiable
        target.setTemporaryVariable(source.getTemporaryVariable());
        target.setGeographicVariable(source.getGeographicVariable());

        List<DataSourceVariable> variables = dataSourceVariableDtoToDo(ctx, source.getOtherVariables(), target.getOtherVariables());
        target.getOtherVariables().clear();
        target.getOtherVariables().addAll(variables);

        return target;
    }

    private InternationalString internationalStringToDo(ServiceContext ctx, InternationalStringDto source, InternationalString target, String metadataName) throws MetamacException {
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

        if (ValidationUtils.isEmpty(source)) {
            throw new MetamacException(ServiceExceptionType.METADATA_REQUIRED, metadataName);
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
