package es.gobcan.istac.indicators.core.mapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.InternationalStringRepository;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.core.common.serviceimpl.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.indicators.core.constants.IndicatorsConstants;
import es.gobcan.istac.indicators.core.domain.Data;
import es.gobcan.istac.indicators.core.domain.DataSource;
import es.gobcan.istac.indicators.core.domain.DataSourceVariable;
import es.gobcan.istac.indicators.core.domain.Dimension;
import es.gobcan.istac.indicators.core.domain.ElementLevel;
import es.gobcan.istac.indicators.core.domain.Indicator;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystem;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.domain.Quantity;
import es.gobcan.istac.indicators.core.domain.RateDerivation;
import es.gobcan.istac.indicators.core.domain.RateDerivationRepository;
import es.gobcan.istac.indicators.core.domain.Subject;
import es.gobcan.istac.indicators.core.dto.DataDto;
import es.gobcan.istac.indicators.core.dto.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.DataSourceVariableDto;
import es.gobcan.istac.indicators.core.dto.DimensionDto;
import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.IndicatorInstanceDto;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.dto.QuantityDto;
import es.gobcan.istac.indicators.core.dto.RateDerivationDto;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsService;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsSystemsService;
import es.gobcan.istac.indicators.core.serviceimpl.util.ServiceUtils;

@Component
public class Dto2DoMapperImpl implements Dto2DoMapper {

    @Autowired
    private IndicatorsSystemsService      indicatorsSystemsService;

    @Autowired
    private IndicatorsService             indicatorsService;
    
    @Autowired
    private InternationalStringRepository internationalStringRepository;

    @Autowired
    private RateDerivationRepository      rateDerivationRepository;

    @Override
    public IndicatorsSystemVersion indicatorsSystemDtoToDo(ServiceContext ctx, IndicatorsSystemDto source) throws MetamacException {
        
        if (source == null) {
            return null;
        }

        // If exists, retrieves existing entity. Otherwise, creates new entity
        IndicatorsSystemVersion target = null;
        if (source.getUuid() != null) {
            throw new MetamacException(ServiceExceptionType.UNKNOWN, "Indicators system can be updated");
        }
        
        target = new IndicatorsSystemVersion();
        target.setIndicatorsSystem(new IndicatorsSystem());
        target.getIndicatorsSystem().setCode(source.getCode());        // non modifiable after creation

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

        if (source.getGeographicalGranularityUuid() != null) {
            target.setGeographicalGranularity(indicatorsSystemsService.retrieveGeographicalGranularity(ctx, source.getGeographicalGranularityUuid()));
        } else {
            target.setGeographicalGranularity(null);
        }
        if (source.getGeographicalValueUuid() != null) {
            target.setGeographicalValue(indicatorsSystemsService.retrieveGeographicalValue(ctx, source.getGeographicalValueUuid()));
        } else {
            target.setGeographicalValue(null);
        }
        target.setTimeGranularity(source.getTimeGranularity());
        target.setTimeValue(source.getTimeValue());

        // Related entities
        if (source.getParentUuid() != null) {
            Dimension dimensionParent = indicatorsSystemsService.retrieveDimension(ctx, source.getParentUuid());
            target.getElementLevel().setParent(dimensionParent.getElementLevel());
        }
        if (source.getIndicatorUuid() != null) {
            Indicator indicator = indicatorsService.retrieveIndicator(ctx, source.getIndicatorUuid());
            target.setIndicator(indicator);
        }

        return target;
    }

    @Override
    public IndicatorVersion indicatorDtoToDo(ServiceContext ctx, IndicatorDto source) throws MetamacException {

        if (source == null) {
            return null;
        }

        // If exists, retrieves existing entity. Otherwise, creates new entity
        IndicatorVersion target = null;
        if (source.getUuid() == null) {
            target = new IndicatorVersion();
            target.setIndicator(new Indicator());
            // non modifiable after creation
            target.getIndicator().setCode(source.getCode());
        } else {
            target = indicatorsService.retrieveIndicator(ctx, source.getUuid(), source.getVersionNumber());

            // Metadata unmodifiable
            List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();
            ValidationUtils.checkMetadataUnmodifiable(target.getIndicator().getCode(), source.getCode(), "INDICATOR.CODE", exceptions);
            // These attributes are modified by service, not by user
            ValidationUtils.checkMetadataUnmodifiable(target.getDataRepositoryId(), source.getDataRepositoryId(), "INDICATOR.DATA_REPOSITORY_ID", exceptions);
            ValidationUtils.checkMetadataUnmodifiable(target.getDataRepositoryTableName(), source.getDataRepositoryTableName(), "INDICATOR.DATA_REPOSITORY_TABLE_NAME", exceptions);
            
            ExceptionUtils.throwIfException(exceptions);
        }

        // Attributes modifiables
        target.setTitle(internationalStringToDo(ctx, source.getTitle(), target.getTitle(), "INDICATOR.TITLE"));
        target.setAcronym(internationalStringToDo(ctx, source.getAcronym(), target.getAcronym(), "INDICATOR.ACRONYM"));
        target.setComments(internationalStringToDo(ctx, source.getComments(), target.getComments(), "INDICATOR.COMMENTS"));
        target.setCommentsUrl(source.getCommentsUrl());
        target.setConceptDescription(internationalStringToDo(ctx, source.getConceptDescription(), target.getConceptDescription(), "INDICATOR.CONCEPT_DESCRIPTION"));
        target.setNotes(internationalStringToDo(ctx, source.getNotes(), target.getNotes(), "INDICATOR.NOTES"));
        target.setNotesUrl(source.getNotesUrl());

        if (source.getSubjectCode() != null) {
            // Although subject is not saved as a relation to table view, it is necessary validate it exists and same title is provided
            Subject subject = indicatorsService.retrieveSubject(ctx, source.getSubjectCode());
            if (source.getSubjectTitle() != null && (source.getSubjectTitle().getTexts().size() != 1 || !subject.getTitle().equals(source.getSubjectTitle().getLocalisedLabel(IndicatorsConstants.LOCALE_SPANISH)))) {
                throw new MetamacException(ServiceExceptionType.METADATA_INCORRECT, "INDICATOR.SUBJECT_TITLE");
            }
            target.setSubjectCode(source.getSubjectCode());
            target.setSubjectTitle(internationalStringToDo(ctx, source.getSubjectTitle(), target.getSubjectTitle(), "INDICATOR.SUBJECT_TITLE"));
        } else {
            target.setSubjectCode(null);
            target.setSubjectTitle(null);
        }

        // Related entities
        target.setQuantity(quantityDtoToDo(ctx, source.getQuantity(), target.getQuantity()));

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
            target.setDataGpeUuid(source.getDataGpeUuid());
            target.setPxUri(source.getPxUri());
        } else {
            target = indicatorsService.retrieveDataSource(ctx, source.getUuid());

            // Metadata unmodifiable
            List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();
            ValidationUtils.checkMetadataUnmodifiable(target.getDataGpeUuid(), source.getDataGpeUuid(), "DATA_SOURCE.DATA_GPE_UUID", exceptions);
            ValidationUtils.checkMetadataUnmodifiable(target.getPxUri(), source.getPxUri(), "DATA_SOURCE.PX_URI", exceptions);
            ExceptionUtils.throwIfException(exceptions);
        }

        // Metadata modifiable
        target.setTimeVariable(source.getTimeVariable());
        target.setTimeValue(source.getTimeValue());
        target.setGeographicalVariable(source.getGeographicalVariable());
        if (source.getGeographicalValueUuid() != null) {
            target.setGeographicalValue(indicatorsSystemsService.retrieveGeographicalValue(ctx, source.getGeographicalValueUuid()));
        } else {
            target.setGeographicalValue(null);
        }
        target.setAbsoluteMethod(source.getAbsoluteMethod());
        target.setSourceSurveyCode(source.getSourceSurveyCode());
        target.setSourceSurveyTitle(internationalStringToDo(ctx, source.getSourceSurveyTitle(), target.getSourceSurveyTitle(), "DATA_SOURCE.SOURCE_SURVEY_TITLE"));
        target.setSourceSurveyAcronym(internationalStringToDo(ctx, source.getSourceSurveyAcronym(), target.getSourceSurveyAcronym(), "DATA_SOURCE.SOURCE_SURVEY_ACRONYM"));
        target.setSourceSurveyUrl(source.getSourceSurveyUrl());
        target.setPublishers(ServiceUtils.dtoList2DtoString(source.getPublishers()));
        
        // Related entities
        target.setAnnualPuntualRate(rateDerivationDtoToDo(ctx, source.getAnnualPuntualRate(), target.getAnnualPuntualRate()));
        target.setAnnualPercentageRate(rateDerivationDtoToDo(ctx, source.getAnnualPercentageRate(), target.getAnnualPercentageRate()));
        target.setInterperiodPuntualRate(rateDerivationDtoToDo(ctx, source.getInterperiodPuntualRate(), target.getInterperiodPuntualRate()));
        target.setInterperiodPercentageRate(rateDerivationDtoToDo(ctx, source.getInterperiodPercentageRate(), target.getInterperiodPercentageRate()));

        List<DataSourceVariable> variables = dataSourceVariableDtoToDo(ctx, source.getOtherVariables(), target.getOtherVariables());
        target.getOtherVariables().clear();
        target.getOtherVariables().addAll(variables);

        return target;
    }
    
    @Override
    public Data dataDtoToDo(ServiceContext ctx, DataDto source) {
        Data target = new Data();
        return target;
    }

    private RateDerivation rateDerivationDtoToDo(ServiceContext ctx, RateDerivationDto source, RateDerivation target) throws MetamacException {

        if (source == null) {
            if (target != null) {
                // delete previous entity
                rateDerivationRepository.delete(target);
            }
            return null;
        }
        if (target == null) {
            target = new RateDerivation();
        }

        target.setMethodType(source.getMethodType());
        target.setMethod(source.getMethod());
        target.setRounding(source.getRounding());
        target.setQuantity(quantityDtoToDo(ctx, source.getQuantity(), target.getQuantity()));

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

    // Note: transforms all metadata regardless of the type to be initialized to null all that do not apply to such quantity
    // InvocationValidation checks metadata unexpected for each type
    private Quantity quantityDtoToDo(ServiceContext ctx, QuantityDto source, Quantity target) throws MetamacException {

        if (source == null) {
            return null;
        }
        if (target == null) {
            target = new Quantity();
        }

        target.setQuantityType(source.getType());
        if (source.getUnitUuid() != null) {
            target.setUnit(indicatorsService.retrieveQuantityUnit(ctx, source.getUnitUuid()));
        } else {
            target.setUnit(null);
        }
        target.setUnitMultiplier(source.getUnitMultiplier());
        target.setSignificantDigits(source.getSignificantDigits());
        target.setDecimalPlaces(source.getDecimalPlaces());
        target.setMinimum(source.getMinimum());
        target.setMaximum(source.getMaximum());
        if (source.getNumeratorIndicatorUuid() != null) {
            target.setNumerator(indicatorsService.retrieveIndicator(ctx, source.getNumeratorIndicatorUuid()));
        } else {
            target.setNumerator(null);
        }
        if (source.getDenominatorIndicatorUuid() != null) {
            target.setDenominator(indicatorsService.retrieveIndicator(ctx, source.getDenominatorIndicatorUuid()));
        } else {
            target.setDenominator(null);
        }
        target.setIsPercentage(source.getIsPercentage());
        target.setPercentageOf(internationalStringToDo(ctx, source.getPercentageOf(), target.getPercentageOf(), "INDICATOR.QUANTITY.PERCENTAGE_OF"));
        target.setBaseValue(source.getBaseValue());
        target.setBaseTime(source.getBaseTime());

        if (source.getBaseLocationUuid() != null) {
            target.setBaseLocation(indicatorsSystemsService.retrieveGeographicalValue(ctx, source.getBaseLocationUuid()));
        } else {
            target.setBaseLocation(null);
        }
        if (source.getBaseQuantityIndicatorUuid() != null) {
            target.setBaseQuantity(indicatorsService.retrieveIndicator(ctx, source.getBaseQuantityIndicatorUuid()));
        } else {
            target.setBaseQuantity(null);
        }

        return target;
    }
}
