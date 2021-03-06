package es.gobcan.istac.indicators.core.mapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.core.common.serviceimpl.utils.ValidationUtils;
import org.siemac.metamac.core.common.util.OptimisticLockingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import es.gobcan.istac.indicators.core.domain.Data;
import es.gobcan.istac.indicators.core.domain.DataSource;
import es.gobcan.istac.indicators.core.domain.DataSourceVariable;
import es.gobcan.istac.indicators.core.domain.Dimension;
import es.gobcan.istac.indicators.core.domain.ElementLevel;
import es.gobcan.istac.indicators.core.domain.GeographicalGranularity;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.Indicator;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystem;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.domain.Quantity;
import es.gobcan.istac.indicators.core.domain.QuantityUnit;
import es.gobcan.istac.indicators.core.domain.RateDerivation;
import es.gobcan.istac.indicators.core.domain.RateDerivationRepository;
import es.gobcan.istac.indicators.core.domain.Subject;
import es.gobcan.istac.indicators.core.domain.UnitMultiplier;
import es.gobcan.istac.indicators.core.dto.DataDto;
import es.gobcan.istac.indicators.core.dto.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.DataSourceVariableDto;
import es.gobcan.istac.indicators.core.dto.DimensionDto;
import es.gobcan.istac.indicators.core.dto.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.dto.GeographicalValueBaseDto;
import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;
import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.IndicatorInstanceDto;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.dto.QuantityDto;
import es.gobcan.istac.indicators.core.dto.QuantityUnitDto;
import es.gobcan.istac.indicators.core.dto.RateDerivationDto;
import es.gobcan.istac.indicators.core.dto.UnitMultiplierDto;
import es.gobcan.istac.indicators.core.error.ServiceExceptionParameters;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsService;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsSystemsService;
import es.gobcan.istac.indicators.core.serviceimpl.util.ServiceUtils;

@Component
public class Dto2DoMapperImpl extends CommonDto2DoMapperImpl implements Dto2DoMapper {

    @Autowired
    private IndicatorsSystemsService       indicatorsSystemsService;

    @Autowired
    private IndicatorsService              indicatorsService;

    @Autowired
    private RateDerivationRepository       rateDerivationRepository;

    @Autowired
    private IndicatorsConfigurationService configurationService;

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
        // non modifiable after creation
        target.getIndicatorsSystem().setCode(source.getCode());

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
            OptimisticLockingUtils.checkVersion(target.getVersion(), source.getVersionOptimisticLocking());

            // Metadata unmodifiable
            List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();
            // This metadatas are modified in specific operation
            ValidationUtils.checkMetadataUnmodifiable(source.getParentUuid(), target.getElementLevel().getParentUuid(), ServiceExceptionParameters.DIMENSION_PARENT_UUID, exceptions);
            ValidationUtils.checkMetadataUnmodifiable(source.getOrderInLevel(), target.getElementLevel().getOrderInLevel(), ServiceExceptionParameters.DIMENSION_ORDER_IN_LEVEL, exceptions);
            ExceptionUtils.throwIfException(exceptions);
        }

        // Optimistic locking: Update "update date" attribute to force update to root entity, to increase attribute "version"
        target.setUpdateDate(new DateTime());

        // Metadata modifiable
        target.setTitle(internationalStringDtoToDo(source.getTitle(), target.getTitle(), ServiceExceptionParameters.DIMENSION_TITLE));

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

            target.setCode(UUID.randomUUID().toString());
            target.setElementLevel(new ElementLevel());
            target.getElementLevel().setOrderInLevel(source.getOrderInLevel());
            target.getElementLevel().setIndicatorInstance(target);
            target.getElementLevel().setDimension(null);
        } else {
            target = indicatorsSystemsService.retrieveIndicatorInstance(ctx, source.getUuid());
            OptimisticLockingUtils.checkVersion(target.getVersion(), source.getVersionOptimisticLocking());

            // Metadata unmodifiable (these metadatas are modified in specific operation)
            List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();

            ValidationUtils.checkMetadataUnmodifiable(source.getCode(), target.getCode(), ServiceExceptionParameters.INDICATOR_INSTANCE_CODE, exceptions);
            ValidationUtils.checkMetadataUnmodifiable(source.getIndicatorUuid(), target.getIndicator().getUuid(), ServiceExceptionParameters.INDICATOR_INSTANCE_INDICATOR_UUID, exceptions);
            ValidationUtils.checkMetadataUnmodifiable(source.getParentUuid(), target.getElementLevel().getParentUuid(), ServiceExceptionParameters.INDICATOR_INSTANCE_PARENT_UUID, exceptions);
            ValidationUtils.checkMetadataUnmodifiable(source.getOrderInLevel(), target.getElementLevel().getOrderInLevel(), ServiceExceptionParameters.INDICATOR_INSTANCE_ORDER_IN_LEVEL, exceptions);
            ExceptionUtils.throwIfException(exceptions);
        }

        // Optimistic locking: Update "update date" attribute to force update to root entity, to increase attribute "version"
        target.setUpdateDate(new DateTime());

        // Metadata modifiable
        target.setTitle(internationalStringDtoToDo(source.getTitle(), target.getTitle(), ServiceExceptionParameters.INDICATOR_INSTANCE_TITLE));

        if (source.getGeographicalGranularityUuid() != null) {
            target.setGeographicalGranularity(indicatorsSystemsService.retrieveGeographicalGranularity(ctx, source.getGeographicalGranularityUuid()));
        } else {
            target.setGeographicalGranularity(null);
        }

        target.getGeographicalValues().clear();
        Set<String> geoCodesAdded = new HashSet<String>();
        if (source.getGeographicalValues() != null && source.getGeographicalValues().size() > 0) {
            for (GeographicalValueBaseDto geoValueBase : source.getGeographicalValues()) {
                GeographicalValue geoValue = indicatorsSystemsService.retrieveGeographicalValue(ctx, geoValueBase.getUuid());
                if (!geoCodesAdded.contains(geoValue.getCode())) {
                    target.addGeographicalValue(geoValue);
                    geoCodesAdded.add(geoValue.getCode());
                }
            }
        }

        target.setTimeGranularity(source.getTimeGranularity());
        target.setTimeValuesAsList(source.getTimeValues());

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
            target.getIndicator().setViewCode(source.getViewCode());
        } else {
            target = indicatorsService.retrieveIndicator(ctx, source.getUuid(), source.getVersionNumber());
            OptimisticLockingUtils.checkVersion(target.getVersion(), source.getVersionOptimisticLocking());

            // Metadata unmodifiable
            List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();
            ValidationUtils.checkMetadataUnmodifiable(target.getIndicator().getCode(), source.getCode(), ServiceExceptionParameters.INDICATOR_CODE, exceptions);
            ValidationUtils.checkMetadataUnmodifiable(target.getIndicator().getViewCode(), source.getViewCode(), ServiceExceptionParameters.INDICATOR_VIEW_CODE, exceptions);
            // These attributes are modified by service, not by user
            ValidationUtils.checkMetadataUnmodifiable(target.getDataRepositoryId(), source.getDataRepositoryId(), ServiceExceptionParameters.INDICATOR_DATA_REPOSITORY_ID, exceptions);
            ValidationUtils.checkMetadataUnmodifiable(target.getDataRepositoryTableName(), source.getDataRepositoryTableName(), ServiceExceptionParameters.INDICATOR_DATA_REPOSITORY_TABLE_NAME,
                    exceptions);

            ExceptionUtils.throwIfException(exceptions);
        }

        // Optimistic locking: Update "update date" attribute to force update to root entity, to increase attribute "version"
        target.setUpdateDate(new DateTime());

        // Attributes modifiables
        target.setTitle(internationalStringDtoToDo(source.getTitle(), target.getTitle(), ServiceExceptionParameters.INDICATOR_TITLE));
        target.setAcronym(internationalStringDtoToDo(source.getAcronym(), target.getAcronym(), ServiceExceptionParameters.INDICATOR_ACRONYM));
        target.setComments(internationalStringDtoToDo(source.getComments(), target.getComments(), ServiceExceptionParameters.INDICATOR_COMMENTS));
        target.setConceptDescription(internationalStringDtoToDo(source.getConceptDescription(), target.getConceptDescription(), ServiceExceptionParameters.INDICATOR_CONCEPT_DESCRIPTION));
        target.setNotes(internationalStringDtoToDo(source.getNotes(), target.getNotes(), ServiceExceptionParameters.INDICATOR_NOTES));

        if (source.getSubjectCode() != null) {
            // Although subject is not saved as a relation to table view, it is necessary validate it exists and same title is provided
            Subject subject = indicatorsService.retrieveSubject(ctx, source.getSubjectCode());

            InternationalStringDto subjectTitleIntDto = new InternationalStringDto();
            LocalisedStringDto localised = new LocalisedStringDto();
            localised.setLocale(configurationService.retrieveLanguageDefault());
            localised.setLabel(subject.getTitle());
            subjectTitleIntDto.addText(localised);

            target.setSubjectCode(source.getSubjectCode());
            target.setSubjectTitle(internationalStringDtoToDo(subjectTitleIntDto, target.getSubjectTitle(), ServiceExceptionParameters.INDICATOR_SUBJECT_TITLE));
        } else {
            target.setSubjectCode(null);
            target.setSubjectTitle(null);
        }

        if (hasIndicatorDecimalPlacesChanged(source, target)) {
            target.setNeedsUpdate(Boolean.TRUE);
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
        } else {
            target = indicatorsService.retrieveDataSource(ctx, source.getUuid());
            OptimisticLockingUtils.checkVersion(target.getVersion(), source.getVersionOptimisticLocking());
        }

        // Optimistic locking: Update "update date" attribute to force update to root entity, to increase attribute "version"
        target.setUpdateDate(new DateTime());

        // Metadata modifiable
        target.setQueryEnvironment(source.getQueryEnvironment());
        target.setQueryUuid(source.getQueryUuid());
        target.setQueryUrn(source.getQueryUrn());
        target.setQueryText(source.getQueryText());

        target.setStatResource(externalItemDtoToDo(source.getStatResource(), target.getStatResource(), ServiceExceptionParameters.DATA_SOURCE_DATA_QUERY_ARTEFACT));

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
        target.setSourceSurveyTitle(internationalStringDtoToDo(source.getSourceSurveyTitle(), target.getSourceSurveyTitle(), ServiceExceptionParameters.DATA_SOURCE_SOURCE_SURVEY_TITLE));
        target.setSourceSurveyAcronym(internationalStringDtoToDo(source.getSourceSurveyAcronym(), target.getSourceSurveyAcronym(), ServiceExceptionParameters.DATA_SOURCE_SOURCE_SURVEY_ACRONYM));
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

    @Override
    public InternationalString internationalStringDtoToDo(ServiceContext ctx, InternationalStringDto source, InternationalString target) throws MetamacException {
        if (source == null) {
            return null;
        }

        if (target == null) {
            target = new InternationalString();
        }

        Set<LocalisedString> localisedStringEntities = localisedStringDtoToDo(source.getTexts(), target.getTexts(), target);
        target.getTexts().clear();
        target.getTexts().addAll(localisedStringEntities);

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
        if (source.getUnitMultiplier() != null) {
            UnitMultiplier unitMultiplier = indicatorsService.retrieveUnitMultiplier(ctx, source.getUnitMultiplier()); // only to check exists
            target.setUnitMultiplier(unitMultiplier);
        }
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
        target.setPercentageOf(internationalStringDtoToDo(source.getPercentageOf(), target.getPercentageOf(), ServiceExceptionParameters.INDICATOR_QUANTITY_PERCENTAGE_OF));
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

    private boolean hasIndicatorDecimalPlacesChanged(IndicatorDto source, IndicatorVersion target) {
        Integer oldDecimalPlaces = null;
        if (target.getQuantity() != null) {
            oldDecimalPlaces = target.getQuantity().getDecimalPlaces();
        }
        Integer newDecimalPlaces = null;
        if (source.getQuantity() != null) {
            newDecimalPlaces = source.getQuantity().getDecimalPlaces();
        }
        if (oldDecimalPlaces != null) {
            return !oldDecimalPlaces.equals(newDecimalPlaces);
        } else if (newDecimalPlaces != null) {
            // oldDecimalPlaces is null and newDecimalPlaces isn't so, decimalPlaces has changed
            return true;
        }
        return false;
    }

    @Override
    public QuantityUnit quantityUnitDtoToDo(ServiceContext ctx, QuantityUnitDto source) throws MetamacException {
        if (source == null) {
            return null;
        }

        // If exists, retrieves existing entity. Otherwise, creates new entity
        QuantityUnit target = new QuantityUnit();
        if (source.getUuid() != null) {
            target = indicatorsService.retrieveQuantityUnit(ctx, source.getUuid());
            OptimisticLockingUtils.checkVersion(target.getVersion(), source.getOptimisticLockingVersion());
        }

        quantityUnitDtoToDo(ctx, source, target);
        return target;
    }

    private QuantityUnit quantityUnitDtoToDo(ServiceContext ctx, QuantityUnitDto source, QuantityUnit target) throws MetamacException {
        if (target == null) {
            throw new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.QUANTITY_UNIT);
        }

        target.setSymbol(source.getSymbol());
        target.setSymbolPosition(source.getSymbolPosition());
        target.setTitle(internationalStringDtoToDo(ctx, source.getTitle(), target.getTitle()));

        // Optimistic locking: Update "update date" attribute to force update of the root entity in order to increase attribute "version"
        target.setUpdateDate(new DateTime());

        return target;
    }

    @Override
    public GeographicalValue geographicalValueDtoToDo(ServiceContext ctx, GeographicalValueDto source) throws MetamacException {
        if (source == null) {
            return null;
        }

        // If exists, retrieves existing entity. Otherwise, creates new entity
        GeographicalValue target = new GeographicalValue();
        if (source.getUuid() != null) {
            target = indicatorsSystemsService.retrieveGeographicalValue(ctx, source.getUuid());
            OptimisticLockingUtils.checkVersion(target.getVersion(), source.getOptimisticLockingVersion());
        }

        geographicalValueDtoToDo(ctx, source, target);
        return target;
    }

    private GeographicalValue geographicalValueDtoToDo(ServiceContext ctx, GeographicalValueDto source, GeographicalValue target) throws MetamacException {
        if (target == null) {
            throw new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.GEOGRAPHICAL_VALUE);
        }

        target.setCode(source.getCode());
        target.setLatitude(source.getLatitude());
        target.setLongitude(source.getLongitude());
        target.setOrder(source.getOrder());
        target.setTitle(internationalStringDtoToDo(ctx, source.getTitle(), target.getTitle()));

        if (StringUtils.isNotEmpty(source.getGranularity().getUuid())) {
            target.setGranularity(indicatorsSystemsService.retrieveGeographicalGranularity(ctx, source.getGranularity().getUuid()));
        } else {
            target.setGranularity(null);
        }

        // Optimistic locking: Update "update date" attribute to force update of the root entity in order to increase attribute "version"
        target.setUpdateDate(new DateTime());

        return target;
    }

    @Override
    public GeographicalGranularity geographicalGranularityDtoToDo(ServiceContext ctx, GeographicalGranularityDto source) throws MetamacException {
        if (source == null) {
            return null;
        }

        // If exists, retrieves existing entity. Otherwise, creates new entity
        GeographicalGranularity target = new GeographicalGranularity();
        if (source.getUuid() != null) {
            target = indicatorsSystemsService.retrieveGeographicalGranularity(ctx, source.getUuid());
            OptimisticLockingUtils.checkVersion(target.getVersion(), source.getOptimisticLockingVersion());
        }

        geographicalGranularityDtoToDo(ctx, source, target);
        return target;
    }

    private GeographicalGranularity geographicalGranularityDtoToDo(ServiceContext ctx, GeographicalGranularityDto source, GeographicalGranularity target) throws MetamacException {
        if (target == null) {
            throw new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.GEOGRAPHICAL_GRANULARITY);
        }

        target.setCode(source.getCode());
        target.setTitle(internationalStringDtoToDo(ctx, source.getTitle(), target.getTitle()));

        // Optimistic locking: Update "update date" attribute to force update of the root entity in order to increase attribute "version"
        target.setUpdateDate(new DateTime());

        return target;
    }

    @Override
    public UnitMultiplier unitMultiplierDtoToDo(ServiceContext ctx, UnitMultiplierDto source) throws MetamacException {
        if (source == null) {
            return null;
        }

        // If exists, retrieves existing entity. Otherwise, creates new entity
        UnitMultiplier target = new UnitMultiplier();
        if (source.getUuid() != null) {
            target = indicatorsService.retrieveUnitMultiplier(ctx, source.getUuid());
            OptimisticLockingUtils.checkVersion(target.getVersion(), source.getOptimisticLockingVersion());
        }

        unitMultiplierDtoToDo(ctx, source, target);
        return target;
    }

    private UnitMultiplier unitMultiplierDtoToDo(ServiceContext ctx, UnitMultiplierDto source, UnitMultiplier target) throws MetamacException {
        if (target == null) {
            throw new MetamacException(ServiceExceptionType.PARAMETER_REQUIRED, ServiceExceptionParameters.UNIT_MULTIPLIER);
        }

        target.setUnitMultiplier(source.getUnitMultiplier());
        target.setTitle(internationalStringDtoToDo(ctx, source.getTitle(), target.getTitle()));

        // Optimistic locking: Update "update date" attribute to force update of the root entity in order to increase attribute "version"
        target.setUpdateDate(new DateTime());

        return target;

    }

}
