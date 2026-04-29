package es.gobcan.istac.indicators.rest.mapper;

import static es.gobcan.istac.indicators.rest.constants.IndicatorsRestApiConstants.DEFAULT;
import static es.gobcan.istac.indicators.rest.constants.IndicatorsRestApiConstants.PROP_ATTRIBUTE_OBS_CONF;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Dataset;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Query;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategoryResourceInternal;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Attribute;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.AttributeAttachmentLevelType;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Attributes;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Dimension;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DimensionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceDto;
import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceObservationDto;
import es.gobcan.istac.edatos.dataset.repository.dto.CodeDimensionDto;
import es.gobcan.istac.edatos.dataset.repository.dto.InternationalStringDto;
import es.gobcan.istac.edatos.dataset.repository.dto.LocalisedStringDto;
import es.gobcan.istac.edatos.dataset.repository.dto.ObservationDto;
import es.gobcan.istac.edatos.dataset.repository.dto.ObservationExtendedDto;
import es.gobcan.istac.indicators.core.conf.MetadataProperties;
import es.gobcan.istac.indicators.core.constants.IndicatorsConstants;
import es.gobcan.istac.indicators.core.domain.DataSource;
import es.gobcan.istac.indicators.core.domain.ElementLevel;
import es.gobcan.istac.indicators.core.domain.GeographicalGranularity;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.Indicator;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystem;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemHistory;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.domain.MeasureValue;
import es.gobcan.istac.indicators.core.domain.Quantity;
import es.gobcan.istac.indicators.core.domain.RateDerivation;
import es.gobcan.istac.indicators.core.domain.TimeGranularity;
import es.gobcan.istac.indicators.core.domain.TimeValue;
import es.gobcan.istac.indicators.core.domain.Translation;
import es.gobcan.istac.indicators.core.domain.TranslationRepository;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorDataAttributeTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorDataDimensionTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.MeasureDimensionTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.QuantityTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.QueryEnvironmentEnum;
import es.gobcan.istac.indicators.core.externalitemscache.domain.CategoryCache;
import es.gobcan.istac.indicators.core.service.StatisticalResoucesRestExternalService;
import es.gobcan.istac.indicators.core.vo.GeographicalValueVO;
import es.gobcan.istac.indicators.core.vo.IndicatorObservationsExtendedVO;
import es.gobcan.istac.indicators.rest.IndicatorsRestConstants;
import es.gobcan.istac.indicators.rest.clients.SrmRestInternalFacade;
import es.gobcan.istac.indicators.rest.clients.StatisticalOperationsRestInternalFacade;
import es.gobcan.istac.indicators.rest.clients.StatisticalResourceRestExternalFacade;
import es.gobcan.istac.indicators.rest.clients.adapters.OperationIndicators;
import es.gobcan.istac.indicators.rest.component.UriLinks;
import es.gobcan.istac.indicators.rest.exception.RestRuntimeException;
import es.gobcan.istac.indicators.rest.serviceapi.IndicatorsApiService;
import es.gobcan.istac.indicators.rest.types.AttributeAttachmentLevelEnumType;
import es.gobcan.istac.indicators.rest.types.AttributeType;
import es.gobcan.istac.indicators.rest.types.DataDimensionType;
import es.gobcan.istac.indicators.rest.types.DataRepresentationType;
import es.gobcan.istac.indicators.rest.types.DataType;
import es.gobcan.istac.indicators.rest.types.ElementLevelType;
import es.gobcan.istac.indicators.rest.types.GeographicalValueType;
import es.gobcan.istac.indicators.rest.types.IndicatorBaseType;
import es.gobcan.istac.indicators.rest.types.InternationalDataAttributeType;
import es.gobcan.istac.indicators.rest.types.IndicatorInstanceBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorInstanceType;
import es.gobcan.istac.indicators.rest.types.IndicatorType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemHistoryType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemType;
import es.gobcan.istac.indicators.rest.types.JsonStatDataType;
import es.gobcan.istac.indicators.rest.types.JsonStatDimensionType;
import es.gobcan.istac.indicators.rest.types.LinkType;
import es.gobcan.istac.indicators.rest.types.MetadataAttributeType;
import es.gobcan.istac.indicators.rest.types.MetadataDimensionType;
import es.gobcan.istac.indicators.rest.types.MetadataGranularityType;
import es.gobcan.istac.indicators.rest.types.MetadataRepresentationType;
import es.gobcan.istac.indicators.rest.types.MetadataType;
import es.gobcan.istac.indicators.rest.types.QuantityType;
import es.gobcan.istac.indicators.rest.types.QuantityUnitSymbolPositionEnum;
import es.gobcan.istac.indicators.rest.types.SubjectBaseType;
import es.gobcan.istac.indicators.rest.types.SubjectType;
import es.gobcan.istac.indicators.rest.types.TitleLinkType;
import es.gobcan.istac.indicators.rest.util.GeographicalValuesOldVersionCompatibilityUtils;

@Component
public class Do2TypeMapperImpl implements Do2TypeMapper {

    @Autowired
    UriLinks                                                                                     uriLinks;

    @Autowired
    private TranslationRepository                                                                translationRepository;

    private static ThreadLocal<Map<String, Map<String, Object>>>                                 requestCache                          = new ThreadLocal<Map<String, Map<String, Object>>>() {

                                                                                                                                           @Override
                                                                                                                                           protected java.util.Map<String, Map<String, Object>> initialValue() {
                                                                                                                                               return new HashMap<String, Map<String, Object>>();
                                                                                                                                           }
                                                                                                                                       };
    @Autowired
    private final IndicatorsApiService                                                           indicatorsApiService                  = null;

    @Autowired
    private final StatisticalOperationsRestInternalFacade                                        statisticalOperations                 = null;

    @Autowired
    private final SrmRestInternalFacade                                                          srmRestInternalFacade                 = null;

    @Autowired
    private final MetadataProperties                                                             metadataProperties                    = null;

    @Autowired
    private Do2JsonStatMapperUtil                                                                do2JsonStatMapperUtil;

    @Autowired
    private final StatisticalResourceRestExternalFacade                                          statisticalResourceRestExternalFacade = null;

    private static final List<String>                                                            measuresOrder                         = Arrays.asList(MeasureDimensionTypeEnum.ABSOLUTE.name(),
            MeasureDimensionTypeEnum.ANNUAL_PERCENTAGE_RATE.name(), MeasureDimensionTypeEnum.INTERPERIOD_PERCENTAGE_RATE.name(), MeasureDimensionTypeEnum.ANNUAL_PUNTUAL_RATE.name(),
            MeasureDimensionTypeEnum.INTERPERIOD_PUNTUAL_RATE.name());

    private static final EnumMap<QuantityUnitSymbolPositionEnum, QuantityUnitSymbolPositionEnum> QUANTITY_UNIT_SYMBOL_POSITION_MAPPING = new EnumMap<>(QuantityUnitSymbolPositionEnum.class);

    static {
        QUANTITY_UNIT_SYMBOL_POSITION_MAPPING.put(QuantityUnitSymbolPositionEnum.START, QuantityUnitSymbolPositionEnum.START);
        QUANTITY_UNIT_SYMBOL_POSITION_MAPPING.put(QuantityUnitSymbolPositionEnum.END, QuantityUnitSymbolPositionEnum.END);
    }

    private static final EnumMap<QuantityTypeEnum, es.gobcan.istac.indicators.rest.types.QuantityTypeEnum> QUANTITY_TYPE_MAPPING = new EnumMap<>(
            es.gobcan.istac.indicators.core.enume.domain.QuantityTypeEnum.class);

    static {
        QUANTITY_TYPE_MAPPING.put(es.gobcan.istac.indicators.core.enume.domain.QuantityTypeEnum.AMOUNT, es.gobcan.istac.indicators.rest.types.QuantityTypeEnum.AMOUNT);
        QUANTITY_TYPE_MAPPING.put(es.gobcan.istac.indicators.core.enume.domain.QuantityTypeEnum.CHANGE_RATE, es.gobcan.istac.indicators.rest.types.QuantityTypeEnum.CHANGE_RATE);
        QUANTITY_TYPE_MAPPING.put(es.gobcan.istac.indicators.core.enume.domain.QuantityTypeEnum.FRACTION, es.gobcan.istac.indicators.rest.types.QuantityTypeEnum.FRACTION);
        QUANTITY_TYPE_MAPPING.put(es.gobcan.istac.indicators.core.enume.domain.QuantityTypeEnum.INDEX, es.gobcan.istac.indicators.rest.types.QuantityTypeEnum.INDEX);
        QUANTITY_TYPE_MAPPING.put(es.gobcan.istac.indicators.core.enume.domain.QuantityTypeEnum.MAGNITUDE, es.gobcan.istac.indicators.rest.types.QuantityTypeEnum.MAGNITUDE);
        QUANTITY_TYPE_MAPPING.put(es.gobcan.istac.indicators.core.enume.domain.QuantityTypeEnum.QUANTITY, es.gobcan.istac.indicators.rest.types.QuantityTypeEnum.QUANTITY);
        QUANTITY_TYPE_MAPPING.put(es.gobcan.istac.indicators.core.enume.domain.QuantityTypeEnum.RATE, es.gobcan.istac.indicators.rest.types.QuantityTypeEnum.RATE);
        QUANTITY_TYPE_MAPPING.put(es.gobcan.istac.indicators.core.enume.domain.QuantityTypeEnum.RATIO, es.gobcan.istac.indicators.rest.types.QuantityTypeEnum.RATIO);
    }

    @Override
    public IndicatorsSystemBaseType indicatorsSystemDoToBaseType(IndicatorsSystemVersion source) {
        Assert.notNull(source);

        try {
            IndicatorsSystemBaseType target = indicatorsSystemDoToBaseType(source, getOperationFromIndicatorsSystem(source));
            return target;
        } catch (Exception e) {
            throw new RestRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public IndicatorsSystemType indicatorsSystemDoToType(IndicatorsSystemVersion source) {
        Assert.notNull(source);

        try {
            IndicatorsSystemType target = indicatorsSystemDoToType(source, getOperationFromIndicatorsSystem(source));
            return target;
        } catch (Exception e) {
            throw new RestRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public List<IndicatorsSystemBaseType> indicatorsSystemDoToBaseType(List<IndicatorsSystemVersion> sources) {
        Assert.notNull(sources);

        try {
            List<IndicatorsSystemBaseType> targets = new ArrayList<IndicatorsSystemBaseType>(sources.size());
            Map<String, OperationIndicators> operations = new HashMap<>();
            for (IndicatorsSystemVersion source : sources) {
                IndicatorsSystemBaseType target = indicatorsSystemDoToBaseType(source, getOperationByCode(operations, source));
                targets.add(target);
            }
            return targets;
        } catch (Exception e) {
            throw new RestRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public IndicatorsSystemHistoryType indicatorsSystemHistoryDoToType(final IndicatorsSystemHistory source) {
        IndicatorsSystemHistoryType target = new IndicatorsSystemHistoryType();
        target.setIndicatorSystemId(source.getIndicatorsSystem().getUuid());
        target.setVersion(source.getVersionNumber());
        target.setPublicationDate(source.getPublicationDate().toDate());

        return target;
    }

    @Override
    public IndicatorInstanceType indicatorsInstanceDoToType(IndicatorInstance source) {
        Assert.notNull(source);

        IndicatorInstanceType target = new IndicatorInstanceType();
        indicatorsInstanceDoToType(source, target);
        return target;
    }

    @Override
    public List<IndicatorInstanceBaseType> indicatorsInstanceDoToBaseType(List<IndicatorInstance> sources) {
        Assert.notNull(sources);

        try {
            List<IndicatorInstanceBaseType> targets = new ArrayList<IndicatorInstanceBaseType>(sources.size());
            for (IndicatorInstance source : sources) {
                IndicatorInstanceBaseType target = new IndicatorInstanceBaseType();
                IndicatorVersion indicatorVersion = indicatorsApiService.retrieveIndicator(source.getIndicator().getUuid()); // IDEA Make in only one invocation
                indicatorsInstanceDoToType(source, indicatorVersion, target);
                targets.add(target);
            }
            return targets;
        } catch (MetamacException e) {
            throw new RestRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override
    public JsonStatDataType indicatorsInstanceDoToJsonStatType(IndicatorInstance source, IndicatorVersion indicatorVersion, IndicatorObservationsExtendedVO observations) {
        Assert.notNull(source);
        try {
            JsonStatDataType target = new JsonStatDataType();

            target.setLabel(MapperUtil.getDefaultValue(source.getTitle(), metadataProperties.getDefaultInternationalizationLanguage()));
            target.setId(do2JsonStatMapperUtil.createJsonStatId());
            target.setRole(do2JsonStatMapperUtil.createJsonStatRole());
            target.setSize(do2JsonStatMapperUtil.toJsonStatSize(observations));
            Map<String, JsonStatDimensionType> dimensions = do2JsonStatMapperUtil.toJsonStatDimensions(source, indicatorVersion, observations);
            target.setDimension(dimensions);
            target.setExtension(do2JsonStatMapperUtil.toJsonStatExtension(source));
            target.setValue(do2JsonStatMapperUtil.toJsonStatValue(observations));
            target.setUpdated(source.getLastUpdated().toString());
            target.setNote(do2JsonStatMapperUtil.toJsonStatNotes(indicatorVersion.getNotes(), observations, dimensions));

            return target;
        } catch (Exception e) {
            throw new RestRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public IndicatorType indicatorDoToType(IndicatorVersion source) {
        Assert.notNull(source);
        try {
            IndicatorType target = new IndicatorType();
            indicatorDoToType(source, target);
            return target;
        } catch (Exception e) {
            throw new RestRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public JsonStatDataType indicatorDoToJsonStatType(IndicatorVersion source, IndicatorObservationsExtendedVO observations) {
        Assert.notNull(source);
        try {
            JsonStatDataType target = new JsonStatDataType();

            target.setLabel(MapperUtil.getDefaultValue(source.getTitle(), metadataProperties.getDefaultInternationalizationLanguage()));
            target.setId(do2JsonStatMapperUtil.createJsonStatId());
            target.setRole(do2JsonStatMapperUtil.createJsonStatRole());
            target.setSize(do2JsonStatMapperUtil.toJsonStatSize(observations));
            Map<String, JsonStatDimensionType> dimensions = do2JsonStatMapperUtil.toJsonStatDimensions(source, observations);
            target.setDimension(dimensions);
            target.setExtension(do2JsonStatMapperUtil.toJsonStatExtension(source));
            target.setValue(do2JsonStatMapperUtil.toJsonStatValue(observations));
            target.setUpdated(source.getLastUpdated().toString());
            target.setNote(do2JsonStatMapperUtil.toJsonStatNotes(source.getNotes(), observations, dimensions));

            return target;
        } catch (Exception e) {
            throw new RestRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public List<IndicatorBaseType> indicatorDoToBaseType(final List<IndicatorVersion> sources) {
        Assert.notNull(sources);
        try {
            List<IndicatorBaseType> targets = new ArrayList<IndicatorBaseType>(sources.size());
            SrmRestObjectsMapper srmRestObjectsMapper = new SrmRestObjectsMapper();
            srmRestObjectsMapper.setCategoriesByCategoryElement(srmRestInternalFacade.retrieveSrmCategoryResoourcesByCategoryScheme(metadataProperties.getDefaultCategoryScheme()));

            for (IndicatorVersion source : sources) {
                IndicatorBaseType target = new IndicatorBaseType();
                indicatorBaseDoToType(source, target, srmRestObjectsMapper);
                targets.add(target);
            }
            return targets;
        } catch (Exception e) {
            throw new RestRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public MetadataGranularityType geographicalGranularityDoToType(GeographicalGranularity geographicalGranularity) {
        MetadataGranularityType metadataGranularityType = new MetadataGranularityType();
        metadataGranularityType.setCode(geographicalGranularity.getCode());
        metadataGranularityType.setTitle(MapperUtil.getLocalisedLabel(geographicalGranularity.getTitle(), metadataProperties.getDefaultInternationalizationLanguage()));
        return metadataGranularityType;
    }

    @Override
    public List<MetadataGranularityType> geographicalGranularityDoToType(List<GeographicalGranularity> geographicalGranularities) {
        if (CollectionUtils.isEmpty(geographicalGranularities)) {
            return null;
        }
        List<MetadataGranularityType> geographicalGranularityTypes = new ArrayList<MetadataGranularityType>(geographicalGranularities.size());
        for (GeographicalGranularity geographicalGranularity : geographicalGranularities) {
            MetadataGranularityType metadataGranularityType = geographicalGranularityDoToType(geographicalGranularity);
            geographicalGranularityTypes.add(metadataGranularityType);
        }
        return geographicalGranularityTypes;
    }

    @Override
    public List<MetadataGranularityType> timeGranularityDoToType(List<TimeGranularity> timeGranularities) {
        if (CollectionUtils.isEmpty(timeGranularities)) {
            return null;
        }
        List<MetadataGranularityType> timeGranularityTypes = new ArrayList<MetadataGranularityType>(timeGranularities.size());
        for (TimeGranularity timeGranularity : timeGranularities) {
            MetadataGranularityType timeGranularityType = new MetadataGranularityType();
            timeGranularityType.setCode(timeGranularity.getGranularity().name());
            timeGranularityType.setTitle(MapperUtil.getLocalisedLabel(timeGranularity.getTitle(), metadataProperties.getDefaultInternationalizationLanguage()));
            timeGranularityTypes.add(timeGranularityType);
        }
        return timeGranularityTypes;
    }

    @Override
    public List<GeographicalValueType> geographicalValuesDoToType(List<GeographicalValue> geographicalValues) {
        List<GeographicalValueType> result = new ArrayList<GeographicalValueType>();

        for (GeographicalValue geographicalValue : geographicalValues) {
            GeographicalValueType type = new GeographicalValueType();
            type.setCode(geographicalValue.getCode());
            type.setTitle(MapperUtil.getLocalisedLabel(geographicalValue.getTitle(), metadataProperties.getDefaultInternationalizationLanguage()));
            type.setGranularityCode(geographicalValue.getGranularity().getCode());
            type.setLatitude(geographicalValue.getLatitude());
            type.setLongitude(geographicalValue.getLongitude());
            result.add(type);
        }

        return result;
    }

    @Override
    public List<GeographicalValueType> geographicalValuesVOToType(List<GeographicalValueVO> geographicalValues) {
        List<GeographicalValueType> result = new ArrayList<GeographicalValueType>();

        for (GeographicalValueVO geographicalValue : geographicalValues) {
            GeographicalValueType type = new GeographicalValueType();
            type.setCode(geographicalValue.getCode());
            type.setTitle(MapperUtil.getLocalisedLabel(geographicalValue.getTitle(), metadataProperties.getDefaultInternationalizationLanguage()));
            type.setGranularityCode(geographicalValue.getGranularity().getCode());
            type.setLatitude(geographicalValue.getLatitude());
            type.setLongitude(geographicalValue.getLongitude());
            result.add(type);
        }

        return result;
    }

    private void subjectDoToBaseType(CategoryCache category, SubjectBaseType subjectBaseType) {
        subjectBaseType.setId(category.getCategoryCode());
        subjectBaseType.setCode(category.getCategoryCode());
        subjectBaseType.setKind(IndicatorsRestConstants.API_INDICATORS_SUBJECTS);
        subjectBaseType.setTitle(MapperUtil.getLocalisedLabel(category.getTitle(), metadataProperties.getDefaultInternationalizationLanguage()));
    }

    @Override
    public List<SubjectBaseType> subjectDoToBaseType(List<CategoryCache> categoryCacheEntries) {
        if (CollectionUtils.isEmpty(categoryCacheEntries)) {
            return null;
        }
        List<SubjectBaseType> subjectTypes = new ArrayList<SubjectBaseType>(categoryCacheEntries.size());
        for (CategoryCache category : categoryCacheEntries) {
            SubjectBaseType subjectType = new SubjectType();
            subjectDoToBaseType(category, subjectType);
            subjectTypes.add(subjectType);
        }
        return subjectTypes;
    }

    @Override
    public DataType createDataType(DataTypeRequest dataTypeRequest, boolean includeObservationMetadata) {
        return createGenericDataType(dataTypeRequest, includeObservationMetadata, false, new HashMap<>());
    }

    @Override
    public DataType createDataTypeWithGeographicalCodes(DataTypeRequest dataTypeRequest, boolean includeObservationMetadata, Map<String, String> geographicalValuesCodes) {
        return createGenericDataType(dataTypeRequest, includeObservationMetadata, true, geographicalValuesCodes);
    }

    private DataType createGenericDataType(DataTypeRequest dataTypeRequest, boolean includeObservationMetadata, boolean geographicalCodesRequired, Map<String, String> geographicalValuesCodes) {
        requestCache.remove();
        try {
            List<String> geographicalCodes = setGeographicalCodesCompatibility(dataTypeRequest);

            List<String> timeValues = dataTypeRequest.getTimeCodes();
            List<String> measureValues = dataTypeRequest.getMeasureCodes();
            Map<String, ? extends ObservationDto> observationMap = dataTypeRequest.getObservationMap();

            // TRANSFORM
            Integer size = geographicalCodes.size() * timeValues.size() * measureValues.size();

            List<String> format = new ArrayList<String>();
            Map<String, DataDimensionType> dimension = new LinkedHashMap<String, DataDimensionType>();
            List<String> observations = new ArrayList<String>(size);
            List<Map<String, AttributeType>> attributes = new ArrayList<Map<String, AttributeType>>(size);

            format.add(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name());
            format.add(IndicatorDataDimensionTypeEnum.TIME.name());
            format.add(IndicatorDataDimensionTypeEnum.MEASURE.name());

            DataRepresentationType dataRepresentationTypeGeographical = new DataRepresentationType();
            dataRepresentationTypeGeographical.setSize(geographicalCodes.size());
            dataRepresentationTypeGeographical.setIndex(new LinkedHashMap<String, Integer>());
            DataDimensionType dataDimensionTypeGeographical = new DataDimensionType();
            dataDimensionTypeGeographical.setRepresentation(dataRepresentationTypeGeographical);
            dimension.put(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name(), dataDimensionTypeGeographical);

            DataRepresentationType dataRepresentationTypeTime = new DataRepresentationType();
            dataRepresentationTypeTime.setSize(timeValues.size());
            dataRepresentationTypeTime.setIndex(new LinkedHashMap<String, Integer>());
            DataDimensionType dataDimensionTypeTime = new DataDimensionType();
            dataDimensionTypeTime.setRepresentation(dataRepresentationTypeTime);
            dimension.put(IndicatorDataDimensionTypeEnum.TIME.name(), dataDimensionTypeTime);

            DataRepresentationType dataRepresentationTypeMeasure = new DataRepresentationType();
            dataRepresentationTypeMeasure.setSize(measureValues.size());
            dataRepresentationTypeMeasure.setIndex(new LinkedHashMap<String, Integer>());
            DataDimensionType dataDimensionTypeMeasure = new DataDimensionType();
            dataDimensionTypeMeasure.setRepresentation(dataRepresentationTypeMeasure);
            dimension.put(IndicatorDataDimensionTypeEnum.MEASURE.name(), dataDimensionTypeMeasure);

            // At least one geographical code must exist. But some indicators can have an error in their observations and it is not possible to obtain the data.
            // To avoid a general error in this case the observations are not filled but the api does not return an error.
            if (!dataTypeRequest.getGeographicalCodes().isEmpty()) {
                for (int i = 0; i < geographicalCodes.size(); i++) {
                    String geographicalCode = geographicalCodes.get(i);

                    if (dataTypeRequest.getGeographicalCodes().size() > i) {

                        for (int j = 0; j < timeValues.size(); j++) {
                            String timeValueCode = timeValues.get(j);

                            for (int k = 0; k < measureValues.size(); k++) {
                                String measureValueCode = measureValues.get(k);

                                // Observation ID: Be careful!!! don't change order of ids
                                String geographicalValueCode = geographicalCode;
                                String newId = dataTypeRequest.getGeographicalCodes().get(i) + "#" + timeValueCode + "#" + measureValueCode;

                                ObservationDto observationDto = observationMap.get(newId);
                                if (observationDto == null) {
                                    observationDto = createObservationExtendedDto(geographicalValueCode, timeValueCode, measureValueCode, null);
                                } else if (observationDto.getPrimaryMeasure() == null) {
                                    observationDto.setPrimaryMeasure(IndicatorsConstants.DOT_1_NOT_APPLICABLE);
                                }

                                // PRIMARY MEASURE
                                observations.add(observationDto.getPrimaryMeasure());

                                // OBS-level attributes only (DATASET/DIMENSION/GROUP handled separately below)
                                if (includeObservationMetadata) {
                                    ObservationExtendedDto observationExtendedDto = (ObservationExtendedDto) observationDto;
                                    attributes.add(setObservationAttributes(observationExtendedDto));
                                }

                            }
                        }
                    }
                }
            }

            for (int j = 0; j < timeValues.size(); j++) {
                dataRepresentationTypeTime.getIndex().put(timeValues.get(j), j);
            }
            for (int k = 0; k < measureValues.size(); k++) {
                dataRepresentationTypeMeasure.getIndex().put(measureValues.get(k), k);
            }

            convertVariableElementToGeographicalCodes(geographicalCodesRequired, geographicalValuesCodes, geographicalCodes, dataRepresentationTypeGeographical);

            DataType dataType = new DataType();
            dataType.setFormat(format);
            dataType.setDimension(dimension);
            dataType.setObservation(observations);
            if (!attributes.isEmpty()) {
                dataType.setAttribute(attributes);
            }

            if (includeObservationMetadata) {
                List<AttributeInstanceDto> allAttributeInstances = dataTypeRequest.getDatasetAndDimensionAttributes();
                Set<String> multilingualIds = dataTypeRequest.getMultilingualAttributeIds();
                if (allAttributeInstances != null && !allAttributeInstances.isEmpty()) {
                    String languageDefault = metadataProperties.getDefaultInternationalizationLanguage();
                    List<InternationalDataAttributeType> attributesList = buildAttributes(allAttributeInstances, multilingualIds, dataTypeRequest, languageDefault);
                    if (!attributesList.isEmpty()) {
                        dataType.setAttributes(attributesList);
                    }
                }
            }

            return dataType;
        } catch (Exception e) {
            throw new RestRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        } finally {
            // Remove All Cache
            requestCache.remove();
        }
    }

    private void convertVariableElementToGeographicalCodes(boolean geographicalCodesRequired, Map<String, String> geographicalValuesCodes, List<String> geographicalCodes,
            DataRepresentationType dataRepresentationTypeGeographical) {
        if (geographicalCodesRequired) {
            for (int i = 0; i < geographicalCodes.size(); i++) {
                String geographicalCode = geographicalCodes.get(i);
                String codeId = geographicalValuesCodes.get(geographicalCode);

                if (codeId != null) {
                    geographicalCode = codeId;
                }
                dataRepresentationTypeGeographical.getIndex().put(geographicalCode, i);
            }
        } else {
            for (int i = 0; i < geographicalCodes.size(); i++) {
                dataRepresentationTypeGeographical.getIndex().put(geographicalCodes.get(i), i);
            }
        }
    }

    private List<String> setGeographicalCodesCompatibility(DataTypeRequest dataTypeRequest) {
        List<String> geographicalCodes;
        if (dataTypeRequest.geoValuesOldVersionCompatibilityUtils != null && dataTypeRequest.geoValuesOldVersionCompatibilityUtils.needsCompatibilityGeographicalCodes()) {
            geographicalCodes = dataTypeRequest.geoValuesOldVersionCompatibilityUtils.getOriginalSelectedGeographicalRepresentations();
        } else {
            geographicalCodes = dataTypeRequest.getGeographicalCodes();
        }
        return geographicalCodes;
    }

    private ObservationDto createObservationExtendedDto(String geographicalValueCode, String timeValueCode, String measureValueCode, String primaryMeasure) {
        ObservationDto observationDto;
        observationDto = new ObservationExtendedDto();
        observationDto.setPrimaryMeasure(primaryMeasure);
        CodeDimensionDto geoCodeDimDto = new CodeDimensionDto(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name(), geographicalValueCode);
        CodeDimensionDto timeCodeDimDto = new CodeDimensionDto(IndicatorDataDimensionTypeEnum.TIME.name(), timeValueCode);
        CodeDimensionDto measureCodeDimDto = new CodeDimensionDto(IndicatorDataDimensionTypeEnum.MEASURE.name(), measureValueCode);

        observationDto.getCodesDimension().add(geoCodeDimDto);
        observationDto.getCodesDimension().add(timeCodeDimDto);
        observationDto.getCodesDimension().add(measureCodeDimDto);
        return observationDto;
    }

    private Map<String, AttributeType> setObservationAttributes(ObservationExtendedDto observationDto) throws MetamacException {
        Map<String, AttributeType> observationAttributes = new LinkedHashMap<>();

        for (AttributeInstanceObservationDto codeAttributeBasicDto : observationDto.getAttributes()) {
            if (isAttributeValid(codeAttributeBasicDto)) {
                AttributeType attributeType = createAttributeType(codeAttributeBasicDto);
                observationAttributes.put(codeAttributeBasicDto.getAttributeId(), attributeType);
            }
        }

        return observationAttributes.isEmpty() ? null : observationAttributes;
    }

    private boolean isAttributeValid(AttributeInstanceObservationDto attributeDto) {
        String attributeId = attributeDto.getAttributeId();
        String valueLabel = attributeDto.getValue().getLocalisedLabel(metadataProperties.getDefaultInternationalizationLanguage());
        if (attributeId == null || valueLabel == null) {
            return false;
        }

        return !attributeId.equals(IndicatorDataAttributeTypeEnum.CODE.getName()) && !valueLabel.isEmpty();
    }

    private AttributeType createAttributeType(AttributeInstanceObservationDto attributeDto) {
        AttributeType attributeType = new AttributeType();
        attributeType.setCode(attributeDto.getAttributeId());
        attributeType.setValue(MapperUtil.getLocalisedLabel(attributeDto.getValue(), metadataProperties.getDefaultInternationalizationLanguage()));
        return attributeType;
    }

    private boolean isMultilingualAttribute(String attributeId, Set<String> multilingualIds) {
        return multilingualIds != null && multilingualIds.contains(attributeId);
    }

    private boolean isDatasetLevelAttribute(List<AttributeInstanceDto> instances) {
        for (AttributeInstanceDto instance : instances) {
            if (instance.getCodesByDimension() != null && !instance.getCodesByDimension().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private List<String> getAttachedDimensions(List<AttributeInstanceDto> instances) {
        boolean hasGeo = false;
        boolean hasTime = false;
        boolean hasMeasure = false;
        for (AttributeInstanceDto instance : instances) {
            Map<String, List<String>> codes = instance.getCodesByDimension();
            if (codes != null) {
                if (codes.containsKey(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name())) {
                    hasGeo = true;
                }
                if (codes.containsKey(IndicatorDataDimensionTypeEnum.TIME.name())) {
                    hasTime = true;
                }
                if (codes.containsKey(IndicatorDataDimensionTypeEnum.MEASURE.name())) {
                    hasMeasure = true;
                }
            }
        }
        List<String> dimensionIds = new ArrayList<String>();
        if (hasGeo) {
            dimensionIds.add(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name());
        }
        if (hasTime) {
            dimensionIds.add(IndicatorDataDimensionTypeEnum.TIME.name());
        }
        if (hasMeasure) {
            dimensionIds.add(IndicatorDataDimensionTypeEnum.MEASURE.name());
        }
        return dimensionIds;
    }

    private List<List<String>> getDimensionCodeLists(List<String> attachedDimensionIds, DataTypeRequest dataTypeRequest) {
        List<List<String>> result = new ArrayList<List<String>>();
        for (String dimensionId : attachedDimensionIds) {
            if (IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name().equals(dimensionId)) {
                result.add(dataTypeRequest.getGeographicalCodes());
            } else if (IndicatorDataDimensionTypeEnum.TIME.name().equals(dimensionId)) {
                result.add(dataTypeRequest.getTimeCodes());
            } else {
                result.add(dataTypeRequest.getMeasureCodes());
            }
        }
        return result;
    }

    private String buildDimensionLookupKey(AttributeInstanceDto instance, List<String> attachedDimensionIds) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < attachedDimensionIds.size(); i++) {
            if (i > 0) {
                sb.append("#");
            }
            String dimensionId = attachedDimensionIds.get(i);
            Map<String, List<String>> codes = instance.getCodesByDimension();
            List<String> dimensionCodes = codes != null ? codes.get(dimensionId) : null;
            sb.append(dimensionCodes != null && !dimensionCodes.isEmpty() ? dimensionCodes.get(0) : "");
        }
        return sb.toString();
    }

    private String buildPositionKey(int pos, int[] strides, int[] sizes, List<List<String>> dimensionCodeLists) {
        StringBuilder sb = new StringBuilder();
        for (int d = 0; d < dimensionCodeLists.size(); d++) {
            if (d > 0) {
                sb.append("#");
            }
            int dimensionIndex = (pos / strides[d]) % sizes[d];
            sb.append(dimensionCodeLists.get(d).get(dimensionIndex));
        }
        return sb.toString();
    }

    private String getSingleLabel(InternationalStringDto isDto) {
        if (isDto == null || isDto.getTexts() == null || isDto.getTexts().isEmpty()) {
            return "";
        }
        for (LocalisedStringDto lsd : isDto.getTexts()) {
            if (IndicatorsConstants.DATASET_REPOSITORY_LOCALE.equals(lsd.getLocale())) {
                return lsd.getLabel() != null ? lsd.getLabel() : "";
            }
        }
        LocalisedStringDto first = isDto.getTexts().iterator().next();
        return first.getLabel() != null ? first.getLabel() : "";
    }

    private Map<String, String> buildDefaultOnlyMap(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put(DEFAULT, value);
        return map;
    }

    private List<InternationalDataAttributeType> buildAttributes(List<AttributeInstanceDto> allAttrs, Set<String> multilingualIds, DataTypeRequest dataTypeRequest, String languageDefault) {
        List<InternationalDataAttributeType> resultList = new ArrayList<>();
        for (Map.Entry<String, List<AttributeInstanceDto>> entry : groupByAttributeId(allAttrs).entrySet()) {
            String attributeId = entry.getKey();
            List<AttributeInstanceDto> instances = entry.getValue();
            boolean isMultilingual = isMultilingualAttribute(attributeId, multilingualIds);

            List<Map<String, String>> values = isDatasetLevelAttribute(instances)
                    ? buildDatasetValues(instances.get(0), isMultilingual, languageDefault)
                    : buildDimensionalValues(instances, isMultilingual, dataTypeRequest, languageDefault);

            if (values.isEmpty()) {
                continue;
            }
            InternationalDataAttributeType internationalDataAttribute = new InternationalDataAttributeType();
            internationalDataAttribute.setId(attributeId);
            internationalDataAttribute.setValue(values);
            resultList.add(internationalDataAttribute);
        }
        return resultList;
    }

    private Map<String, List<AttributeInstanceDto>> groupByAttributeId(List<AttributeInstanceDto> allAttrs) {
        Map<String, List<AttributeInstanceDto>> byAttributeId = new LinkedHashMap<>();
        for (AttributeInstanceDto attributeInstance : allAttrs) {
            if (!byAttributeId.containsKey(attributeInstance.getAttributeId())) {
                byAttributeId.put(attributeInstance.getAttributeId(), new ArrayList<>());
            }
            byAttributeId.get(attributeInstance.getAttributeId()).add(attributeInstance);
        }
        return byAttributeId;
    }

    private List<Map<String, String>> buildDatasetValues(AttributeInstanceDto attributeInstance, boolean isMultilingual, String languageDefault) {
        Map<String, String> valueMap = isMultilingual
                ? MapperUtil.getLocalisedLabel(attributeInstance.getValue(), languageDefault)
                : buildDefaultOnlyMap(getSingleLabel(attributeInstance.getValue()));
        if (valueMap == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(valueMap);
    }

    private List<Map<String, String>> buildDimensionalValues(List<AttributeInstanceDto> instances, boolean isMultilingual, DataTypeRequest dataTypeRequest, String languageDefault) {
        List<String> attachedDimensionIds = getAttachedDimensions(instances);
        List<List<String>> dimensionCodeLists = getDimensionCodeLists(attachedDimensionIds, dataTypeRequest);
        if (attachedDimensionIds.isEmpty() || dimensionCodeLists.isEmpty()) {
            return Collections.emptyList();
        }

        Map<String, AttributeInstanceDto> lookup = buildDimensionLookup(instances, attachedDimensionIds);

        int[] sizes = new int[dimensionCodeLists.size()];
        int totalSize = 1;
        for (int i = 0; i < dimensionCodeLists.size(); i++) {
            sizes[i] = dimensionCodeLists.get(i).size();
            totalSize *= sizes[i];
        }
        if (totalSize == 0) {
            return Collections.emptyList();
        }

        int[] strides = new int[dimensionCodeLists.size()];
        strides[dimensionCodeLists.size() - 1] = 1;
        for (int i = dimensionCodeLists.size() - 2; i >= 0; i--) {
            strides[i] = strides[i + 1] * sizes[i + 1];
        }

        return buildCartesianValues(lookup, dimensionCodeLists, strides, sizes, totalSize, isMultilingual, languageDefault);
    }

    private Map<String, AttributeInstanceDto> buildDimensionLookup(List<AttributeInstanceDto> instances, List<String> attachedDimensionIds) {
        Map<String, AttributeInstanceDto> lookup = new LinkedHashMap<>();
        for (AttributeInstanceDto attributeInstance : instances) {
            lookup.put(buildDimensionLookupKey(attributeInstance, attachedDimensionIds), attributeInstance);
        }
        return lookup;
    }

    private List<Map<String, String>> buildCartesianValues(Map<String, AttributeInstanceDto> lookup, List<List<String>> dimensionCodeLists, int[] strides, int[] sizes, int totalSize, boolean isMultilingual,
            String languageDefault) {
        List<Map<String, String>> values = new ArrayList<>();
        for (int pos = 0; pos < totalSize; pos++) {
            values.add(resolvePositionValue(lookup.get(buildPositionKey(pos, strides, sizes, dimensionCodeLists)), isMultilingual, languageDefault));
        }
        return values;
    }

    // Null is intentional: a null entry in the value[] array signals "no value at this dimension position"
    // in the JSON response. An empty map {} would be a different (incorrect) semantic.
    @SuppressWarnings("java:S1168")
    private Map<String, String> resolvePositionValue(AttributeInstanceDto attributeInstance, boolean isMultilingual, String languageDefault) {
        if (attributeInstance == null) {
            return null;
        }
        return isMultilingual ? MapperUtil.getLocalisedLabel(attributeInstance.getValue(), languageDefault) : buildDefaultOnlyMap(getSingleLabel(attributeInstance.getValue()));
    }

    private QuantityType quantityDoToBaseType(final Quantity source, SrmRestObjectsMapper srmRestObjectsMapper) throws MetamacException {
        Assert.notNull(source);

        QuantityType quantityType = new QuantityType();
        quantityType.setType(QUANTITY_TYPE_MAPPING.get(source.getQuantityType()));

        if (source.getUnit() != null) {
            quantityType.setUnit(MapperUtil.getLocalisedLabel(source.getUnit().getTitle(), metadataProperties.getDefaultInternationalizationLanguage()));
            MapperUtil.setQuantityUnitMetadata(source, quantityType, metadataProperties, srmRestInternalFacade, srmRestObjectsMapper);
        }

        if (source.getUnitMultiplier() != null) {
            quantityType.setUnitMultiplier(MapperUtil.getLocalisedLabel(source.getUnitMultiplier().getTitle(), metadataProperties.getDefaultInternationalizationLanguage()));
        }
        quantityType.setSignificantDigits(source.getSignificantDigits());
        quantityType.setDecimalPlaces(source.getDecimalPlaces());
        quantityType.setMin(source.getMinimum());
        quantityType.setMax(source.getMaximum());

        if (source.getDenominator() != null) {
            IndicatorVersion indVersion = indicatorsApiService.retrieveIndicator(source.getDenominator().getUuid());
            quantityType.setDenominatorLink(createTitleLinkType(indVersion));
        }
        if (source.getNumerator() != null) {
            IndicatorVersion indVersion = indicatorsApiService.retrieveIndicator(source.getNumerator().getUuid());
            quantityType.setNumeratorLink(createTitleLinkType(indVersion));
        }

        quantityType.setIsPercentage(source.getIsPercentage());
        quantityType.setPercentageOf(MapperUtil.getLocalisedLabel(source.getPercentageOf(), metadataProperties.getDefaultInternationalizationLanguage()));
        quantityType.setBaseValue(source.getBaseValue());
        if (source.getBaseTime() != null) {
            TimeValue timeValue = indicatorsApiService.retrieveTimeValueByCode(source.getBaseTime());
            quantityType.setBaseTime(timeValueDoToMetadataRepresentationType(timeValue));
        }
        if (source.getBaseLocation() != null) {
            GeographicalValue geoValue = indicatorsApiService.retrieveGeographicalValueByCode(source.getBaseLocation().getCode());
            quantityType.setBaseLocation(geographicalValueDoToMetadataRepresentationType(geoValue));
        }
        if (source.getBaseQuantity() != null) {
            IndicatorVersion indVersion = indicatorsApiService.retrieveIndicator(source.getBaseQuantity().getUuid());
            quantityType.setBaseQuantityLink(createTitleLinkType(indVersion));
        }

        return quantityType;
    }

    private void indicatorBaseDoToType(IndicatorVersion source, IndicatorBaseType target, SrmRestObjectsMapper srmRestObjectsMapper) throws MetamacException {
        target.setId(source.getIndicator().getCode());
        target.setKind(IndicatorsRestConstants.KIND_INDICATOR);
        target.setSelfLink(createUrlIndicator(source.getIndicator()));
        target.setCode(source.getIndicator().getCode());
        target.setVersion(source.getVersionNumber());
        target.setTitle(MapperUtil.getLocalisedLabel(source.getTitle(), metadataProperties.getDefaultInternationalizationLanguage()));
        target.setAcronym(MapperUtil.getLocalisedLabel(source.getAcronym(), metadataProperties.getDefaultInternationalizationLanguage()));
        CategoryResourceInternal category = srmRestObjectsMapper.getCategoriesByCategoryElement().get(source.getCategoryElement().getCode());

        if (category != null) {
            target.setSubjectCode(category.getNestedId() != null ? category.getNestedId() : category.getId());
            target.setSubjectTitle(MapperUtil.getLocalisedLabel(category.getName(), metadataProperties.getDefaultInternationalizationLanguage()));
        }

        target.setMainIndicator(source.getIsMainIndicator());

        List<IndicatorsSystemVersion> indicatorsSystemVersions = indicatorsApiService.retrieveIndicatorsSystemPublishedForIndicator(source.getIndicator().getUuid());
        if (indicatorsSystemVersions.size() != 0) {
            List<LinkType> surveyLinks = new ArrayList<LinkType>();
            for (IndicatorsSystemVersion indicatorsSystemVersion : indicatorsSystemVersions) {
                String href = uriLinks.getIndicatorSystemLink(indicatorsSystemVersion.getIndicatorsSystem().getCode());
                surveyLinks.add(new LinkType(IndicatorsRestConstants.KIND_INDICATOR_SYSTEM, href));
            }
            target.setSystemSurveyLinks(surveyLinks);
        }

        target.setQuantity(quantityDoToBaseType(source.getQuantity(), srmRestObjectsMapper));
        target.setConceptDescription(MapperUtil.getLocalisedLabel(source.getConceptDescription(), metadataProperties.getDefaultInternationalizationLanguage()));
        target.setNotes(MapperUtil.getLocalisedLabel(source.getNotes(), metadataProperties.getDefaultInternationalizationLanguage()));
    }

    private CategoryResourceInternal getCategoryByCategoryElement(String categoryElementCode) throws MetamacException {
        return srmRestInternalFacade.retrieveCategoryByCategoryElement(metadataProperties.getDefaultCategoryScheme(), categoryElementCode);
    }

    @Override
    public void indicatorDoToMetadataType(IndicatorVersion source, MetadataType target, GeographicalValuesOldVersionCompatibilityUtils geoValuesOldVersionCompatibilityUtils,
            SrmRestObjectsMapper srmRestObjectsMapper) throws MetamacException {
        List<GeographicalValueVO> geographicalValues = indicatorsApiService.retrieveGeographicalValuesInIndicatorVersion(source);
        if (!geoValuesOldVersionCompatibilityUtils.needsCompatibilityGeographicalCodes()) {
            indicatorDoToMetadataTypeCommon(source, target, geographicalValues, srmRestObjectsMapper);
            return;
        }

        for (GeographicalValueVO geoValue : geographicalValues) {
            String variableElement = geoValuesOldVersionCompatibilityUtils.getGeographicalCodeByVariableElement(geoValue.getCode());
            if (variableElement != null) {
                geoValue.setCode(variableElement);
            }
        }

        indicatorDoToMetadataTypeCommon(source, target, geographicalValues, srmRestObjectsMapper);

    }

    private void indicatorDoToMetadataTypeCommon(IndicatorVersion source, MetadataType target, List<GeographicalValueVO> geographicalValues, SrmRestObjectsMapper srmRestObjectsMapper)
            throws MetamacException {
        target.setDimension(new LinkedHashMap<String, MetadataDimensionType>());

        Map<String, Boolean> dimensionVisualisationShowCodesPreferences = new HashMap<>();

        // GEOGRAPHICAL
        List<GeographicalGranularity> geographicalGranularities = indicatorsApiService.retrieveGeographicalGranularitiesInIndicatorVersion(source);

        MetadataDimensionType geographicaDimension = createGeographicalDimension(geographicalGranularities, geographicalValues, srmRestObjectsMapper.getGeographicalCodesByVariableElement(),
                dimensionVisualisationShowCodesPreferences);
        target.getDimension().put(geographicaDimension.getCode(), geographicaDimension);

        // TIME
        List<TimeGranularity> timeGranularities = indicatorsApiService.retrieveTimeGranularitiesInIndicator(source.getIndicator().getUuid());
        List<TimeValue> timeValues = indicatorsApiService.retrieveTimeValuesInIndicatorVersion(source);

        MetadataDimensionType timeDimension = createTimeDimension(timeGranularities, timeValues);
        target.getDimension().put(timeDimension.getCode(), timeDimension);

        // MEASURE
        List<MeasureValue> measureValues = indicatorsApiService.retrieveMeasureValuesInIndicator(source);

        MetadataDimensionType measureDimension = createMeasureDimension(measureValues, source, srmRestObjectsMapper, dimensionVisualisationShowCodesPreferences);
        target.getDimension().put(measureDimension.getCode(), measureDimension);

        // ATTRIBUTES
        Map<String, MetadataAttributeType> metadataAttributes = new LinkedHashMap<String, MetadataAttributeType>();
        MetadataAttributeType metadataAttributeUnit = createMetadataAttributeType(PROP_ATTRIBUTE_OBS_CONF);
        metadataAttributes.put(PROP_ATTRIBUTE_OBS_CONF, metadataAttributeUnit);
        target.setAttribute(metadataAttributes);
    }

    private void indicatorDoToType(IndicatorVersion source, IndicatorType target) throws MetamacException {
        SrmRestObjectsMapper srmRestObjectsMapper = new SrmRestObjectsMapper();
        srmRestObjectsMapper.setCategoriesByCategoryElement(srmRestInternalFacade.retrieveSrmCategoryResoourcesByCategoryScheme(metadataProperties.getDefaultCategoryScheme()));

        indicatorBaseDoToType(source, target, srmRestObjectsMapper);

        target.setDimension(new LinkedHashMap<String, MetadataDimensionType>());

        Map<String, MetadataAttributeType> metadataAttributes = new HashMap<>();
        Map<String, Boolean> dimensionVisualisationShowCodesPreferences = new HashMap<>();
        extractMetadataFromDataSource(source.getDataSources(), metadataProperties.getDefaultInternationalizationLanguage(), metadataAttributes, dimensionVisualisationShowCodesPreferences);

        // GEOGRAPHICAL
        List<GeographicalGranularity> geographicalGranularities = indicatorsApiService.retrieveGeographicalGranularitiesInIndicatorVersion(source);
        List<GeographicalValueVO> geographicalValues = indicatorsApiService.retrieveGeographicalValuesInIndicatorVersion(source);

        Map<String, String> geographicalValuesCodes = new HashMap<>();
        geographicalValuesCodes = srmRestInternalFacade.retrieveGeographicalElementsIdByCodesOfCodelists(metadataProperties.getDefaultGeographicalCodeListUrn());

        MetadataDimensionType geographicaDimension = createGeographicalDimension(geographicalGranularities, geographicalValues, geographicalValuesCodes, dimensionVisualisationShowCodesPreferences);
        target.getDimension().put(geographicaDimension.getCode(), geographicaDimension);

        // TIME
        List<TimeGranularity> timeGranularities = indicatorsApiService.retrieveTimeGranularitiesInIndicator(source.getIndicator().getUuid());
        List<TimeValue> timeValues = indicatorsApiService.retrieveTimeValuesInIndicatorVersion(source);

        MetadataDimensionType timeDimension = createTimeDimension(timeGranularities, timeValues);
        target.getDimension().put(timeDimension.getCode(), timeDimension);

        // MEASURE
        List<MeasureValue> measureValues = indicatorsApiService.retrieveMeasureValuesInIndicator(source);

        MetadataDimensionType measureDimension = createMeasureDimension(measureValues, source, srmRestObjectsMapper, dimensionVisualisationShowCodesPreferences);
        target.getDimension().put(measureDimension.getCode(), measureDimension);

        // ATTRIBUTES
        metadataAttributes.put(PROP_ATTRIBUTE_OBS_CONF, createMetadataAttributeType(PROP_ATTRIBUTE_OBS_CONF));
        target.setAttribute(metadataAttributes);

        // CHILD LINK
        target.setChildLink(createLinkTypeIndicatorData(source.getIndicator()));

        // PARENT LINK
        target.setParentLink(createLinkTypeIndicators());

        // DECIMAL PLACES
        target.setDecimalPlaces(source.getQuantity().getDecimalPlaces());
    }

    private void extractMetadataFromDataSource(List<DataSource> dataSources, String defaultLang, Map<String, MetadataAttributeType> metadataAttributes,
            Map<String, Boolean> dimensionVisualisationShowCodesPreferences) {

        if (dataSources == null) {
            return;
        }

        /*
         * TODO EDATOS-5057
         * This call to statistical-resources API has a bad performance in the indicators api. If it is necessary to retrieve additional metadata it must not be here.
         * Dataset/query metadata must be saved during the creation/reload/publish indicator process. So these metadata can be retrieved from indicators bd here (in the indicator api)
         * creation/reload metadata from statistical-resources is done here:
         * * Queries: es.gobcan.istac.indicators.core.serviceimpl.util.QueryMetamacUtils
         * * Datasets: es.gobcan.istac.indicators.core.serviceimpl.util.DatasetMetamacUtils
         */

        for (DataSource dataSource : dataSources) {
            if (QueryEnvironmentEnum.METAMAC.equals(dataSource.getQueryEnvironment())) {
                String queryUuid = dataSource.getQueryUuid();
                Attributes metadataAttributesAux = null;
                List<Dimension> dimensions = new ArrayList<>();
                if (StringUtils.startsWithIgnoreCase(queryUuid, UrnUtils.URN_SIEMAC_CLASS_QUERY_PREFIX)) {
                    String queryUrn = dataSource.getQueryUrn();
                    Query queryMetadata = statisticalResourceRestExternalFacade.retrieveQueryByUrn(queryUrn, Collections.singletonList(defaultLang),
                            StatisticalResoucesRestExternalService.QueryFetchEnum.ONLY_METADATA);
                    metadataAttributesAux = queryMetadata.getMetadata().getAttributes();
                    dimensions = queryMetadata.getMetadata().getDimensions().getDimensions();
                } else if (StringUtils.startsWithIgnoreCase(queryUuid, UrnUtils.URN_SIEMAC_CLASS_DATASET_PREFIX)) {
                    Dataset datasetMetadata = statisticalResourceRestExternalFacade.retrieveDatasetByUrn(queryUuid, Collections.singletonList(defaultLang),
                            StatisticalResoucesRestExternalService.QueryFetchEnum.ONLY_METADATA);
                    metadataAttributesAux = datasetMetadata.getMetadata().getAttributes();
                    dimensions = datasetMetadata.getMetadata().getDimensions().getDimensions();
                }

                if (metadataAttributes != null && metadataAttributesAux != null) {
                    for (Attribute metadataAttribute : metadataAttributesAux.getAttributes()) {
                        MetadataAttributeType metadataAttributeUnit = createMetadataAttributeType(metadataAttribute);
                        metadataAttributes.put(metadataAttribute.getId(), metadataAttributeUnit);
                    }
                }

                if (dimensionVisualisationShowCodesPreferences.isEmpty()) { // the preferences of the first datasource.
                    dimensionVisualisationShowCodePreferences(dimensions, dimensionVisualisationShowCodesPreferences);
                }
            }
        }
    }

    private void dimensionVisualisationShowCodePreferences(List<Dimension> dimensions, Map<String, Boolean> dimensionVisualisationShowCodesPreferences) {
        for (Dimension dimension : dimensions) {
            if (DimensionType.GEOGRAPHIC_DIMENSION.equals(dimension.getType())) {
                dimensionVisualisationShowCodesPreferences.put(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name(), dimension.getShowCode());
            } else if (DimensionType.MEASURE_DIMENSION.equals(dimension.getType())) {
                dimensionVisualisationShowCodesPreferences.put(IndicatorDataDimensionTypeEnum.MEASURE.name(), dimension.getShowCode());
            }
        }
    }

    private MetadataAttributeType createMetadataAttributeType(Attribute attribute) {
        MetadataAttributeType metadataAttributeUnit = new MetadataAttributeType();
        metadataAttributeUnit.setCode(attribute.getId());
        String translationCode = new StringBuilder().append(IndicatorsConstants.TRANSLATION_METADATA_ATTRIBUTE).append(".").append(attribute.getId()).toString();
        Translation translation = translationRepository.findTranslationByCode(translationCode);
        Map<String, String> localisedLabel = (translation != null)
                ? MapperUtil.getLocalisedLabel(translation.getTitle(), metadataProperties.getDefaultInternationalizationLanguage())
                : MapperUtil.getLocalisedLabel(attribute.getName(), metadataProperties.getDefaultInternationalizationLanguage());
        metadataAttributeUnit.setTitle(localisedLabel);
        metadataAttributeUnit.setAttachmentLevel(toRestAttachmentLevel(attribute.getAttachmentLevel()));
        return metadataAttributeUnit;
    }

    /**
     * Maps source StatisticalResources attachment level to the REST API enum.
     * PRIMARY_MEASURE → OBSERVATION; DATASET → DATASET; DIMENSION → DIMENSION.
     */
    private AttributeAttachmentLevelEnumType toRestAttachmentLevel(AttributeAttachmentLevelType level) {
        if (AttributeAttachmentLevelType.DATASET.equals(level)) {
            return AttributeAttachmentLevelEnumType.DATASET;
        }
        if (AttributeAttachmentLevelType.DIMENSION.equals(level)) {
            return AttributeAttachmentLevelEnumType.DIMENSION;
        }
        return AttributeAttachmentLevelEnumType.OBSERVATION;
    }

    private MetadataAttributeType createMetadataAttributeType(String code) {
        MetadataAttributeType metadataAttributeUnit = new MetadataAttributeType();
        metadataAttributeUnit.setCode(code);
        String translationCode = new StringBuilder().append(IndicatorsConstants.TRANSLATION_METADATA_ATTRIBUTE).append(".").append(code).toString();
        metadataAttributeUnit
                .setTitle(MapperUtil.getLocalisedLabel(translationRepository.findTranslationByCode(translationCode).getTitle(), metadataProperties.getDefaultInternationalizationLanguage()));
        metadataAttributeUnit.setAttachmentLevel(AttributeAttachmentLevelEnumType.OBSERVATION);
        return metadataAttributeUnit;
    }

    private void indicatorsInstanceDoToType(final IndicatorInstance sourceIndicatorInstance, final IndicatorVersion sourceIndicatorVersion, final IndicatorInstanceBaseType target) {
        Assert.notNull(sourceIndicatorInstance);

        IndicatorsSystem indicatorsSystem = sourceIndicatorInstance.getElementLevel().getIndicatorsSystemVersion().getIndicatorsSystem();

        String selfLink = createUrlIndicatorInstance(indicatorsSystem, sourceIndicatorInstance);

        target.setId(sourceIndicatorInstance.getCode());
        target.setKind(IndicatorsRestConstants.KIND_INDICATOR_INSTANCE);
        target.setSelfLink(selfLink);

        String href = createUrlIndicatorsSystemInstances(indicatorsSystem);
        target.setParentLink(new LinkType(IndicatorsRestConstants.KIND_INDICATOR_INSTANCES, href));

        target.setSystemCode(indicatorsSystem.getCode());
        target.setTitle(MapperUtil.getLocalisedLabel(sourceIndicatorInstance.getTitle(), metadataProperties.getDefaultInternationalizationLanguage()));

        target.setConceptDescription(MapperUtil.getLocalisedLabel(sourceIndicatorVersion.getConceptDescription(), metadataProperties.getDefaultInternationalizationLanguage()));
    }

    @Override
    public void indicatorsInstanceDoToMetadataType(final IndicatorInstance source, final MetadataType target, GeographicalValuesOldVersionCompatibilityUtils geoValuesOldVersionCompatibilityUtils,
            Map<String, String> geographicalValuesCodes) {
        try {
            IndicatorVersion indicatorVersion = indicatorsApiService.retrieveIndicatorByCode(source.getIndicator().getCode());

            target.setDimension(new LinkedHashMap<String, MetadataDimensionType>());

            Map<String, Boolean> dimensionVisualisationShowCodesPreferences = new HashMap<>();

            // GEOGRAPHICAL
            List<GeographicalGranularity> geographicalGranularities = indicatorsApiService.retrieveGeographicalGranularitiesInIndicatorInstance(source.getUuid());
            List<GeographicalValueVO> geographicalValues = indicatorsApiService.retrieveGeographicalValuesInIndicatorInstance(source.getUuid());

            geoValuesOldVersionCompatibilityUtils.getGeoValuesWithOldCompatibility(geographicalValues);

            MetadataDimensionType geographicaDimension = createGeographicalDimension(geographicalGranularities, geographicalValues, geographicalValuesCodes,
                    dimensionVisualisationShowCodesPreferences);
            target.getDimension().put(geographicaDimension.getCode(), geographicaDimension);

            // TIME
            List<TimeGranularity> timeGranularities = indicatorsApiService.retrieveTimeGranularitiesInIndicatorInstance(source.getUuid());
            List<TimeValue> timeValues = indicatorsApiService.retrieveTimeValuesInIndicatorInstance(source.getUuid());

            MetadataDimensionType timeDimension = createTimeDimension(timeGranularities, timeValues);
            target.getDimension().put(timeDimension.getCode(), timeDimension);

            // MEASURE
            List<MeasureValue> measureValues = indicatorsApiService.retrieveMeasureValuesInIndicatorInstance(source.getUuid());

            MetadataDimensionType measureDimension = createMeasureDimension(measureValues, indicatorVersion, new SrmRestObjectsMapper(), dimensionVisualisationShowCodesPreferences);
            target.getDimension().put(measureDimension.getCode(), measureDimension);
        } catch (MetamacException e) {
            throw new RestRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    private void indicatorsInstanceDoToType(final IndicatorInstance source, final IndicatorInstanceType target) {
        try {
            IndicatorVersion indicatorVersion = indicatorsApiService.retrieveIndicator(source.getIndicator().getUuid());

            indicatorsInstanceDoToType(source, indicatorVersion, target);

            IndicatorsSystem indicatorsSystem = source.getElementLevel().getIndicatorsSystemVersion().getIndicatorsSystem();
            target.setDimension(new LinkedHashMap<String, MetadataDimensionType>());

            Map<String, MetadataAttributeType> metadataAttributes = new HashMap<>();
            Map<String, Boolean> dimensionVisualisationShowCodesPreferences = new HashMap<>();
            extractMetadataFromDataSource(source.getIndicator().getDiffusionIndicatorVersion().getDataSources(), metadataProperties.getDefaultInternationalizationLanguage(), metadataAttributes,
                    dimensionVisualisationShowCodesPreferences);

            // GEOGRAPHICAL
            List<GeographicalGranularity> geographicalGranularities = indicatorsApiService.retrieveGeographicalGranularitiesInIndicatorInstance(source.getUuid());
            List<GeographicalValueVO> geographicalValues = indicatorsApiService.retrieveGeographicalValuesInIndicatorInstance(source.getUuid());

            Map<String, String> geographicalValuesCodes = srmRestInternalFacade.retrieveGeographicalElementsIdByCodesOfCodelists(metadataProperties.getDefaultGeographicalCodeListUrn());

            MetadataDimensionType geographicaDimension = createGeographicalDimension(geographicalGranularities, geographicalValues, geographicalValuesCodes,
                    dimensionVisualisationShowCodesPreferences);
            target.getDimension().put(geographicaDimension.getCode(), geographicaDimension);

            // TIME
            List<TimeGranularity> timeGranularities = indicatorsApiService.retrieveTimeGranularitiesInIndicatorInstance(source.getUuid());
            List<TimeValue> timeValues = indicatorsApiService.retrieveTimeValuesInIndicatorInstance(source.getUuid());

            MetadataDimensionType timeDimension = createTimeDimension(timeGranularities, timeValues);
            target.getDimension().put(timeDimension.getCode(), timeDimension);

            // MEASURE
            List<MeasureValue> measureValues = indicatorsApiService.retrieveMeasureValuesInIndicatorInstance(source.getUuid());

            MetadataDimensionType measureDimension = createMeasureDimension(measureValues, indicatorVersion, new SrmRestObjectsMapper(), dimensionVisualisationShowCodesPreferences);
            target.getDimension().put(measureDimension.getCode(), measureDimension);

            // DECIMAL PLACES
            target.setDecimalPlaces(indicatorVersion.getQuantity().getDecimalPlaces());

            // SUBJECT CODE
            CategoryResourceInternal category = getCategoryByCategoryElement(indicatorVersion.getCategoryElement().getCode());
            if (category != null) {
                target.setSubjectCode(category.getNestedId() != null ? category.getNestedId() : category.getId());
                target.setSubjectTitle(MapperUtil.getLocalisedLabel(category.getName(), metadataProperties.getDefaultInternationalizationLanguage()));
            }

            // ATTRIBUTES
            metadataAttributes.put(PROP_ATTRIBUTE_OBS_CONF, createMetadataAttributeType(PROP_ATTRIBUTE_OBS_CONF));
            target.setAttribute(metadataAttributes);

            // CHILD LINK
            String href = createUrlIndicatorInstanceData(indicatorsSystem, source);
            target.setChildLink(new LinkType(IndicatorsRestConstants.KIND_INDICATOR_INSTANCE_DATA, href));

        } catch (MetamacException e) {
            throw new RestRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    private MetadataDimensionType createMeasureDimension(List<MeasureValue> measureValues, IndicatorVersion indicatorVersion, SrmRestObjectsMapper srmRestObjectsMapper,
            Map<String, Boolean> dimensionVisualisationShowCodesPreferences) throws MetamacException {
        MetadataDimensionType measureDimension = new MetadataDimensionType();
        measureDimension.setCode(IndicatorDataDimensionTypeEnum.MEASURE.name());
        measureDimension.setRepresentation(measureValueDoToMeasureRepresentationType(measureValues, indicatorVersion, srmRestObjectsMapper));

        Boolean showCodes = dimensionVisualisationShowCodesPreferences.get(IndicatorDataDimensionTypeEnum.MEASURE.name());

        if (showCodes != null) {
            measureDimension.setShowCode(showCodes);
        }

        return measureDimension;
    }

    private MetadataDimensionType createGeographicalDimension(List<GeographicalGranularity> geographicalGranularities, List<GeographicalValueVO> geographicalValues,
            Map<String, String> geographicalValuesCodes, Map<String, Boolean> dimensionVisualisationShowCodesPreferences) {
        MetadataDimensionType geographicaDimension = new MetadataDimensionType();
        geographicaDimension.setCode(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name());
        geographicaDimension.setGranularity(geographicalGranularityDoToType(geographicalGranularities));
        geographicaDimension.setRepresentation(geographicalValuesDoToMetadataRepresentationType(geographicalValues, geographicalValuesCodes));

        // if source is not from edatos it will always be null de showCodes.
        Boolean showCodes = dimensionVisualisationShowCodesPreferences.get(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name());

        if (showCodes != null) {
            geographicaDimension.setShowCode(showCodes);
        }

        return geographicaDimension;
    }

    private MetadataDimensionType createTimeDimension(List<TimeGranularity> timeGranularities, List<TimeValue> timeValues) {
        MetadataDimensionType timeDimension = new MetadataDimensionType();
        timeDimension.setCode(IndicatorDataDimensionTypeEnum.TIME.name());
        timeDimension.setGranularity(timeGranularityDoToType(timeGranularities));
        timeDimension.setRepresentation(timeValuesDoToMetadataRepresentationType(timeValues));
        return timeDimension;
    }

    private List<MetadataRepresentationType> geographicalValuesDoToMetadataRepresentationType(List<GeographicalValueVO> geographicalValues, Map<String, String> geographicalValuesCodes) {
        if (CollectionUtils.isEmpty(geographicalValues)) {
            return null;
        }

        List<MetadataRepresentationType> geographicalValueTypes = new ArrayList<MetadataRepresentationType>(geographicalValues.size());

        for (GeographicalValueVO geographicalValue : geographicalValues) {
            String codeId = geographicalValuesCodes.get(geographicalValue.getCode());
            if (codeId != null) {
                geographicalValue.setCode(codeId);
            }
            MetadataRepresentationType metadataRepresentationType = geographicalValueVOToMetadataRepresentationType(geographicalValue);
            geographicalValueTypes.add(metadataRepresentationType);
        }

        return geographicalValueTypes;
    }

    protected MetadataRepresentationType geographicalValueVOToMetadataRepresentationType(GeographicalValueVO geographicalValue) {
        MetadataRepresentationType metadataRepresentationType = new MetadataRepresentationType();
        metadataRepresentationType.setCode(geographicalValue.getCode());
        metadataRepresentationType.setLatitude(geographicalValue.getLatitude());
        metadataRepresentationType.setLongitude(geographicalValue.getLongitude());
        metadataRepresentationType.setTitle(MapperUtil.getLocalisedLabel(geographicalValue.getTitle(), metadataProperties.getDefaultInternationalizationLanguage()));
        metadataRepresentationType.setGranularityCode(geographicalValue.getGranularity().getCode());
        return metadataRepresentationType;
    }

    protected MetadataRepresentationType geographicalValueDoToMetadataRepresentationType(GeographicalValue geographicalValue) {
        MetadataRepresentationType metadataRepresentationType = new MetadataRepresentationType();
        metadataRepresentationType.setCode(geographicalValue.getCode());
        metadataRepresentationType.setLatitude(geographicalValue.getLatitude());
        metadataRepresentationType.setLongitude(geographicalValue.getLongitude());
        metadataRepresentationType.setTitle(MapperUtil.getLocalisedLabel(geographicalValue.getTitle(), metadataProperties.getDefaultInternationalizationLanguage()));
        metadataRepresentationType.setGranularityCode(geographicalValue.getGranularity().getCode());
        return metadataRepresentationType;
    }

    private List<MetadataRepresentationType> timeValuesDoToMetadataRepresentationType(List<TimeValue> timeValues) {
        if (CollectionUtils.isEmpty(timeValues)) {
            return null;
        }
        List<MetadataRepresentationType> timeValueTypes = new ArrayList<MetadataRepresentationType>(timeValues.size());
        for (TimeValue timeValue : timeValues) {
            MetadataRepresentationType metadataRepresentationType = timeValueDoToMetadataRepresentationType(timeValue);
            timeValueTypes.add(metadataRepresentationType);
        }
        return timeValueTypes;
    }

    protected MetadataRepresentationType timeValueDoToMetadataRepresentationType(TimeValue timeValue) {
        MetadataRepresentationType metadataRepresentationType = new MetadataRepresentationType();
        metadataRepresentationType.setCode(timeValue.getTimeValue());
        metadataRepresentationType.setGranularityCode(timeValue.getGranularity().getName());
        metadataRepresentationType.setTitle(MapperUtil.getLocalisedLabel(timeValue.getTitle(), metadataProperties.getDefaultInternationalizationLanguage()));
        return metadataRepresentationType;
    }

    private List<MetadataRepresentationType> measureValueDoToMeasureRepresentationType(List<MeasureValue> measureValues, IndicatorVersion indicatorVersion, SrmRestObjectsMapper srmRestObjectsMapper)
            throws MetamacException {
        if (CollectionUtils.isEmpty(measureValues)) {
            return null;
        }
        List<MetadataRepresentationType> measureValueTypes = new ArrayList<MetadataRepresentationType>(measureValues.size());
        for (MeasureValue measureValue : measureValues) {
            MetadataRepresentationType metadataRepresentationType = new MetadataRepresentationType();
            metadataRepresentationType.setCode(measureValue.getMeasureValue().getName());
            metadataRepresentationType.setTitle(MapperUtil.getLocalisedLabel(measureValue.getTitle(), metadataProperties.getDefaultInternationalizationLanguage()));
            Quantity quantity = getQuantityForMeasure(measureValue.getMeasureValue(), indicatorVersion);
            if (quantity != null) {
                metadataRepresentationType.setQuantity(quantityDoToBaseType(quantity, srmRestObjectsMapper));

                removeUnitMultiplierIfRate(measureValue, metadataRepresentationType);
            }
            measureValueTypes.add(metadataRepresentationType);
        }
        sortMeasureValuesTypes(measureValueTypes);
        return measureValueTypes;
    }

    private void removeUnitMultiplierIfRate(MeasureValue measureValue, MetadataRepresentationType metadataRepresentationType) {
        // INDISTAC-831 Las tasas no deben tener públicamente el metadato multiplicador de la unidad
        if (measureValue.getMeasureValue().equals(MeasureDimensionTypeEnum.ANNUAL_PERCENTAGE_RATE) || measureValue.getMeasureValue().equals(MeasureDimensionTypeEnum.INTERPERIOD_PERCENTAGE_RATE)) {
            metadataRepresentationType.getQuantity().setUnitMultiplier(null);
        }
    }

    private void sortMeasureValuesTypes(List<MetadataRepresentationType> measureValueTypes) {
        Collections.sort(measureValueTypes, new Comparator<MetadataRepresentationType>() {

            @Override
            public int compare(MetadataRepresentationType o1, MetadataRepresentationType o2) {
                int indexO1 = measuresOrder.indexOf(o1.getCode());
                int indexO2 = measuresOrder.indexOf(o2.getCode());
                if (indexO1 == indexO2) {
                    return 0;
                } else if (indexO1 < indexO2) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
    }

    private Quantity getQuantityForMeasure(MeasureDimensionTypeEnum measure, IndicatorVersion indicatorVersion) {
        switch (measure) {
            case ABSOLUTE:
                return indicatorVersion.getQuantity();
            default:
                RateDerivation rate = getRateDerivationForMeasure(measure, indicatorVersion);
                if (rate != null) {
                    return rate.getQuantity();
                }
        }
        return null;
    }

    private RateDerivation getRateDerivationForMeasure(MeasureDimensionTypeEnum measure, IndicatorVersion indicatorVersion) {
        for (DataSource datasource : indicatorVersion.getDataSources()) {
            switch (measure) {
                case ANNUAL_PERCENTAGE_RATE:
                    if (datasource.getAnnualPercentageRate() != null) {
                        RateDerivation rateDerivation = datasource.getAnnualPercentageRate();
                        return rateDerivation;
                    }
                    break;
                case ANNUAL_PUNTUAL_RATE:
                    if (datasource.getAnnualPuntualRate() != null) {
                        return datasource.getAnnualPuntualRate();
                    }
                    break;
                case INTERPERIOD_PERCENTAGE_RATE:
                    if (datasource.getInterperiodPercentageRate() != null) {
                        RateDerivation rateDerivation = datasource.getInterperiodPercentageRate();
                        return rateDerivation;
                    }
                    break;
                case INTERPERIOD_PUNTUAL_RATE:
                    if (datasource.getInterperiodPuntualRate() != null) {
                        return datasource.getInterperiodPuntualRate();
                    }
                    break;
            }
        }
        return null;
    }

    private TitleLinkType createTitleLinkType(final IndicatorVersion indicatorVersion) {
        String href = createUrlIndicator(indicatorVersion.getIndicator());
        TitleLinkType link = new TitleLinkType(IndicatorsRestConstants.KIND_INDICATOR, href);
        link.setTitle(MapperUtil.getLocalisedLabel(indicatorVersion.getTitle(), metadataProperties.getDefaultInternationalizationLanguage()));
        return link;
    }

    private LinkType createLinkTypeIndicators() {
        return new LinkType(IndicatorsRestConstants.KIND_INDICATORS, uriLinks.getIndicatorsLink());
    }

    private LinkType createLinkTypeIndicatorData(final Indicator indicator) {
        return new LinkType(IndicatorsRestConstants.KIND_INDICATOR_DATA, createUrlIndicatorData(indicator));
    }

    private void elementsLevelsDoToType(final List<ElementLevel> sources, IndicatorsSystemType target) {
        if (CollectionUtils.isEmpty(sources)) {
            return;
        }

        List<ElementLevelType> targetElements = elementsLevelsDoToType(sources);
        target.setElements(targetElements);
    }

    private List<ElementLevelType> elementsLevelsDoToType(final List<ElementLevel> sources) {
        if (CollectionUtils.isEmpty(sources)) {
            return null;
        }

        List<ElementLevelType> targetElements = new ArrayList<ElementLevelType>();
        for (ElementLevel source : sources) {
            ElementLevelType targetElement = elementsLevelsDoToType(source);
            targetElements.add(targetElement);
        }
        return targetElements;
    }

    private ElementLevelType elementsLevelsDoToType(ElementLevel source) {
        ElementLevelType target = new ElementLevelType();

        if (source.getDimension() != null) {
            target.setId(source.getDimension().getUuid());
            target.setKind(IndicatorsRestConstants.KIND_INDICATOR_DIMENSION);
            target.setTitle(MapperUtil.getLocalisedLabel(source.getDimension().getTitle(), metadataProperties.getDefaultInternationalizationLanguage()));
        } else {
            target.setId(source.getIndicatorInstance().getCode());
            target.setKind(IndicatorsRestConstants.KIND_INDICATOR_INSTANCE);
            target.setTitle(MapperUtil.getLocalisedLabel(source.getIndicatorInstance().getTitle(), metadataProperties.getDefaultInternationalizationLanguage()));

            IndicatorsSystem indicatorsSystem = source.getIndicatorsSystemVersion().getIndicatorsSystem();
            String selfLinkURL = createUrlIndicatorInstance(indicatorsSystem, source.getIndicatorInstance());
            target.setSelfLink(selfLinkURL);
        }

        List<ElementLevelType> targetSubElements = elementsLevelsDoToType(source.getChildren());
        target.setElements(targetSubElements);

        return target;
    }

    private IndicatorsSystemBaseType indicatorsSystemDoToBaseType(IndicatorsSystemVersion sourceIndicatorsSystem, OperationIndicators sourceOperation) {
        IndicatorsSystemBaseType target = new IndicatorsSystemBaseType();
        indicatorsSystemDoToBaseType(sourceIndicatorsSystem, target);

        baseDoToTypeByIndicatorsSystemType(sourceIndicatorsSystem, target, sourceOperation);

        return target;
    }

    private void baseDoToTypeByIndicatorsSystemType(IndicatorsSystemVersion sourceIndicatorsSystem, IndicatorsSystemBaseType target, OperationIndicators sourceOperation) {
        if (Boolean.TRUE.equals(sourceIndicatorsSystem.getIndicatorsSystem().getIsOperational())) {
            operationBaseDoToType(sourceOperation, target);
        } else {
            nonOperationBaseDoToType(sourceIndicatorsSystem, target);
        }
    }

    private IndicatorsSystemType indicatorsSystemDoToType(IndicatorsSystemVersion sourceIndicatorsSystem, OperationIndicators sourceOperation) {
        IndicatorsSystemType target = new IndicatorsSystemType();
        indicatorsSystemDoToType(sourceIndicatorsSystem, target);
        baseDoToTypeByIndicatorsSystemType(sourceIndicatorsSystem, target, sourceOperation);
        elementsLevelsDoToType(sourceIndicatorsSystem.getChildrenFirstLevel(), target);
        return target;
    }

    private void indicatorsSystemDoToBaseType(IndicatorsSystemVersion source, IndicatorsSystemBaseType target) {
        String selfLinkURL = createUrlIndicatorsSystem(source.getIndicatorsSystem());

        target.setKind(IndicatorsRestConstants.KIND_INDICATOR_SYSTEM);
        target.setSelfLink(selfLinkURL);
        target.setVersion(source.getVersionNumber());
        target.setPublicationDate(source.getPublicationDate());
        target.setOperational(Boolean.TRUE.equals(source.getIndicatorsSystem().getIsOperational()));
    }

    private void indicatorsSystemDoToType(IndicatorsSystemVersion source, IndicatorsSystemType target) {
        indicatorsSystemDoToBaseType(source, target);

        String childLinkURL = createUrlIndicatorsSystemInstances(source.getIndicatorsSystem());
        target.setChildLink(new LinkType(IndicatorsRestConstants.KIND_INDICATOR_INSTANCES, childLinkURL));

        String parentLink = uriLinks.getIndicatorsSystemsLink();
        target.setParentLink(new LinkType(IndicatorsRestConstants.KIND_INDICATOR_SYSTEMS, parentLink));
    }

    private void nonOperationBaseDoToType(IndicatorsSystemVersion sourceIndicatorsSystem, IndicatorsSystemBaseType target) {
        target.setId(sourceIndicatorsSystem.getIndicatorsSystem().getCode());
        target.setCode(sourceIndicatorsSystem.getIndicatorsSystem().getCode());
        target.setTitle(MapperUtil.getLocalisedLabel(sourceIndicatorsSystem.getTitle(), metadataProperties.getDefaultInternationalizationLanguage()));
        target.setAcronym(MapperUtil.getLocalisedLabel(sourceIndicatorsSystem.getAcronym(), metadataProperties.getDefaultInternationalizationLanguage()));
        target.setDescription(MapperUtil.getLocalisedLabel(sourceIndicatorsSystem.getDescription(), metadataProperties.getDefaultInternationalizationLanguage()));
        target.setObjective(MapperUtil.getLocalisedLabel(sourceIndicatorsSystem.getObjective(), metadataProperties.getDefaultInternationalizationLanguage()));

    }

    private void operationBaseDoToType(OperationIndicators sourceOperation, IndicatorsSystemBaseType target) {
        target.setId(sourceOperation.getId());
        target.setCode(sourceOperation.getId());
        target.setTitle(sourceOperation.getTitle());
        target.setAcronym(sourceOperation.getAcronym());
        target.setDescription(sourceOperation.getDescription());
        target.setObjective(sourceOperation.getObjective());

        target.setStatisticalOperationLink(new LinkType(IndicatorsRestConstants.KIND_STATISTICAL_OPERATION, sourceOperation.getUri()));
    }

    private String createUrlIndicator(Indicator indicator) {
        return uriLinks.getIndicatorLink(indicator.getCode());
    }

    private String createUrlIndicatorsSystem(final IndicatorsSystem indicatorsSystem) {
        return uriLinks.getIndicatorSystemLink(indicatorsSystem.getCode());
    }

    private String createUrlIndicatorsSystemInstances(final IndicatorsSystem indicatorsSystem) {
        return uriLinks.getIndicatorInstancesLink(indicatorsSystem.getCode());
    }

    private String createUrlIndicatorInstance(final IndicatorsSystem indicatorsSystem, final IndicatorInstance indicatorsInstance) {
        return uriLinks.getIndicatorInstanceLink(indicatorsSystem.getCode(), indicatorsInstance.getCode());
    }

    private String createUrlIndicatorInstanceData(final IndicatorsSystem indicatorsSystem, final IndicatorInstance indicatorsInstance) {
        return uriLinks.getIndicatorInstanceDataLink(indicatorsSystem.getCode(), indicatorsInstance.getCode());
    }

    private String createUrlIndicatorData(final Indicator indicator) {
        return uriLinks.getIndicatorDataLink(indicator.getCode());
    }

    private OperationIndicators getOperationFromIndicatorsSystem(IndicatorsSystemVersion source) throws MetamacException {
        OperationIndicators operationIndicators = null;
        if (Boolean.TRUE.equals(source.getIndicatorsSystem().getIsOperational())) {
            operationIndicators = statisticalOperations.retrieveOperationById(source.getIndicatorsSystem().getCode());
        }
        return operationIndicators;

    }

    private OperationIndicators getOperationByCode(Map<String, OperationIndicators> operations, IndicatorsSystemVersion source) throws MetamacException {
        OperationIndicators operationIndicators = null;
        if (Boolean.TRUE.equals(source.getIndicatorsSystem().getIsOperational())) {
            operationIndicators = operations.get(source.getIndicatorsSystem().getCode());
            if (operationIndicators == null) {
                operationIndicators = statisticalOperations.retrieveOperationById(source.getIndicatorsSystem().getCode());
                operations.put(source.getIndicatorsSystem().getCode(), operationIndicators);
            }
        }
        return operationIndicators;
    }

}
