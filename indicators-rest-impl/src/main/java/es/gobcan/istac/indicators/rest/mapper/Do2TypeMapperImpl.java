package es.gobcan.istac.indicators.rest.mapper;

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

import org.apache.commons.collections.CollectionUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceObservationDto;
import es.gobcan.istac.edatos.dataset.repository.dto.CodeDimensionDto;
import es.gobcan.istac.edatos.dataset.repository.dto.ObservationDto;
import es.gobcan.istac.edatos.dataset.repository.dto.ObservationExtendedDto;
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
import es.gobcan.istac.indicators.core.domain.TranslationRepository;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorDataAttributeTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorDataDimensionTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.MeasureDimensionTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.QuantityTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.QuantityUnitSymbolPositionEnum;
import es.gobcan.istac.indicators.core.repositoryimpl.finders.SubjectIndicatorResult;
import es.gobcan.istac.indicators.core.vo.GeographicalValueVO;
import es.gobcan.istac.indicators.rest.IndicatorsRestConstants;
import es.gobcan.istac.indicators.rest.clients.StatisticalOperationsRestInternalFacade;
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
import es.gobcan.istac.indicators.rest.types.IndicatorInstanceBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorInstanceType;
import es.gobcan.istac.indicators.rest.types.IndicatorType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemHistoryType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemType;
import es.gobcan.istac.indicators.rest.types.LinkType;
import es.gobcan.istac.indicators.rest.types.MetadataAttributeType;
import es.gobcan.istac.indicators.rest.types.MetadataDimensionType;
import es.gobcan.istac.indicators.rest.types.MetadataGranularityType;
import es.gobcan.istac.indicators.rest.types.MetadataRepresentationType;
import es.gobcan.istac.indicators.rest.types.MetadataType;
import es.gobcan.istac.indicators.rest.types.QuantityType;
import es.gobcan.istac.indicators.rest.types.SubjectBaseType;
import es.gobcan.istac.indicators.rest.types.SubjectType;
import es.gobcan.istac.indicators.rest.types.TitleLinkType;

@Component
public class Do2TypeMapperImpl implements Do2TypeMapper {

    @Autowired
    UriLinks                                                                                                                           uriLinks;

    @Autowired
    private TranslationRepository                                                                                                      translationRepository;

    private static ThreadLocal<Map<String, Map<String, Object>>>                                                                       requestCache                          = new ThreadLocal<Map<String, Map<String, Object>>>() {

                                                                                                                                                                                 @Override
                                                                                                                                                                                 protected java.util.Map<String, Map<String, Object>> initialValue() {
                                                                                                                                                                                     return new HashMap<String, Map<String, Object>>();
                                                                                                                                                                                 }
                                                                                                                                                                             };
    @Autowired
    private final IndicatorsApiService                                                                                                 indicatorsApiService                  = null;

    @Autowired
    private final StatisticalOperationsRestInternalFacade                                                                              statisticalOperations                 = null;

    private static final List<String>                                                                                                  measuresOrder                         = Arrays.asList(
            MeasureDimensionTypeEnum.ABSOLUTE.name(), MeasureDimensionTypeEnum.ANNUAL_PERCENTAGE_RATE.name(), MeasureDimensionTypeEnum.INTERPERIOD_PERCENTAGE_RATE.name(),
            MeasureDimensionTypeEnum.ANNUAL_PUNTUAL_RATE.name(), MeasureDimensionTypeEnum.INTERPERIOD_PUNTUAL_RATE.name());

    private static final EnumMap<QuantityUnitSymbolPositionEnum, es.gobcan.istac.indicators.rest.types.QuantityUnitSymbolPositionEnum> QUANTITY_UNIT_SYMBOL_POSITION_MAPPING = new EnumMap<>(
            es.gobcan.istac.indicators.core.enume.domain.QuantityUnitSymbolPositionEnum.class);
    static {
        QUANTITY_UNIT_SYMBOL_POSITION_MAPPING.put(es.gobcan.istac.indicators.core.enume.domain.QuantityUnitSymbolPositionEnum.START,
                es.gobcan.istac.indicators.rest.types.QuantityUnitSymbolPositionEnum.START);
        QUANTITY_UNIT_SYMBOL_POSITION_MAPPING.put(es.gobcan.istac.indicators.core.enume.domain.QuantityUnitSymbolPositionEnum.END,
                es.gobcan.istac.indicators.rest.types.QuantityUnitSymbolPositionEnum.END);
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
            OperationIndicators sourceOperationBase = statisticalOperations.retrieveOperationById(source.getIndicatorsSystem().getCode());
            IndicatorsSystemBaseType target = indicatorsSystemDoToBaseType(source, sourceOperationBase);
            return target;
        } catch (Exception e) {
            throw new RestRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public IndicatorsSystemType indicatorsSystemDoToType(IndicatorsSystemVersion source) {
        Assert.notNull(source);

        try {
            OperationIndicators sourceOperation = statisticalOperations.retrieveOperationById(source.getIndicatorsSystem().getCode());

            IndicatorsSystemType target = indicatorsSystemDoToType(source, sourceOperation);
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
            for (IndicatorsSystemVersion source : sources) {
                OperationIndicators sourceOperation = statisticalOperations.retrieveOperationById(source.getIndicatorsSystem().getCode()); // IDEA Make in only one invocation
                IndicatorsSystemBaseType target = indicatorsSystemDoToBaseType(source, sourceOperation);
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
    public List<IndicatorBaseType> indicatorDoToBaseType(final List<IndicatorVersion> sources) {
        Assert.notNull(sources);
        try {
            List<IndicatorBaseType> targets = new ArrayList<IndicatorBaseType>(sources.size());
            for (IndicatorVersion source : sources) {
                IndicatorBaseType target = new IndicatorBaseType();
                indicatorBaseDoToType(source, target);
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
        metadataGranularityType.setTitle(MapperUtil.getLocalisedLabel(geographicalGranularity.getTitle()));
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
            timeGranularityType.setTitle(MapperUtil.getLocalisedLabel(timeGranularity.getTitle()));
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
            type.setTitle(MapperUtil.getLocalisedLabel(geographicalValue.getTitle()));
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
            type.setTitle(MapperUtil.getLocalisedLabel(geographicalValue.getTitle()));
            type.setGranularityCode(geographicalValue.getGranularity().getCode());
            type.setLatitude(geographicalValue.getLatitude());
            type.setLongitude(geographicalValue.getLongitude());
            result.add(type);
        }

        return result;
    }

    private void subjectDoToBaseType(SubjectIndicatorResult subject, SubjectBaseType subjectBaseType) {
        subjectBaseType.setId(subject.getId());
        subjectBaseType.setCode(subject.getId());
        subjectBaseType.setKind(IndicatorsRestConstants.API_INDICATORS_SUBJECTS);
        subjectBaseType.setTitle(MapperUtil.getLocalisedLabel(subject.getTitle()));
    }

    @Override
    public SubjectType subjectDoToType(final SubjectIndicatorResult subject, List<IndicatorVersion> indicators) {
        if (subject == null) {
            return null;
        }
        SubjectType subjectType = new SubjectType();
        subjectDoToBaseType(subject, subjectType);
        subjectType.setElements(indicatorDoToBaseType(indicators));

        return subjectType;
    }

    @Override
    public List<SubjectBaseType> subjectDoToBaseType(List<SubjectIndicatorResult> subjects) {
        if (CollectionUtils.isEmpty(subjects)) {
            return null;
        }
        List<SubjectBaseType> subjectTypes = new ArrayList<SubjectBaseType>(subjects.size());
        for (SubjectIndicatorResult subject : subjects) {
            SubjectBaseType subjectType = new SubjectType();
            subjectDoToBaseType(subject, subjectType);
            subjectTypes.add(subjectType);
        }
        return subjectTypes;
    }

    @Override
    public DataType createDataType(DataTypeRequest dataTypeRequest, boolean includeObservationMetadata) {
        // Remove All Cache
        requestCache.remove();
        try {
            List<String> geographicalCodes = dataTypeRequest.getGeographicalCodes();
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

            for (int i = 0; i < geographicalCodes.size(); i++) {
                String geographicalCode = geographicalCodes.get(i);
                dataRepresentationTypeGeographical.getIndex().put(geographicalCode, i);

                for (int j = 0; j < timeValues.size(); j++) {
                    String timeValueCode = timeValues.get(j);
                    dataRepresentationTypeTime.getIndex().put(timeValueCode, j);

                    for (int k = 0; k < measureValues.size(); k++) {
                        String measureValueCode = measureValues.get(k);
                        dataRepresentationTypeMeasure.getIndex().put(measureValueCode, k);

                        // Observation ID: Be careful!!! don't change order of ids
                        String geographicalValueCode = geographicalCode;
                        String id = geographicalValueCode + "#" + timeValueCode + "#" + measureValueCode;

                        ObservationDto observationDto = observationMap.get(id);
                        if (observationDto == null) {
                            observationDto = createObservationExtendedDto(geographicalValueCode, timeValueCode, measureValueCode, null);
                        } else if (observationDto.getPrimaryMeasure() == null) {
                            observationDto.setPrimaryMeasure(IndicatorsConstants.DOT_1_NOT_APPLICABLE);
                        }

                        // PRIMARY MEASURE
                        observations.add(observationDto.getPrimaryMeasure());

                        // ATTRIBUTES
                        if (includeObservationMetadata) {
                            ObservationExtendedDto observationExtendedDto = (ObservationExtendedDto) observationDto;
                            attributes.add(setObservationAttributes(observationExtendedDto));
                        }

                    }
                }
            }
            DataType dataType = new DataType();
            dataType.setFormat(format);
            dataType.setDimension(dimension);
            dataType.setObservation(observations);

            if (!attributes.isEmpty()) {
                dataType.setAttribute(attributes);
            }

            return dataType;
        } catch (Exception e) {
            throw new RestRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        } finally {
            // Remove All Cache
            requestCache.remove();
        }
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
        for (AttributeInstanceObservationDto codeAttributeBasicDto : observationDto.getAttributes()) {
            if (codeAttributeBasicDto.getAttributeId().equals(IndicatorDataAttributeTypeEnum.OBS_CONF.getName())) {
                AttributeType unitMultiplierAttribute = new AttributeType();
                unitMultiplierAttribute.setCode(PROP_ATTRIBUTE_OBS_CONF);
                unitMultiplierAttribute.setValue(MapperUtil.getLocalisedLabel(codeAttributeBasicDto.getValue()));

                Map<String, AttributeType> observationAttributes = new LinkedHashMap<String, AttributeType>();
                observationAttributes.put(PROP_ATTRIBUTE_OBS_CONF, unitMultiplierAttribute);
                return observationAttributes;
            }
        }
        return null;
    }

    private QuantityType quantityDoToBaseType(final Quantity source) throws MetamacException {
        Assert.notNull(source);

        QuantityType quantityType = new QuantityType();
        quantityType.setType(QUANTITY_TYPE_MAPPING.get(source.getQuantityType()));
        if (source.getUnit() != null) {
            quantityType.setUnit(MapperUtil.getLocalisedLabel(source.getUnit().getTitle()));
            quantityType.setUnitSymbol(source.getUnit().getSymbol());
            quantityType.setUnitSymbolPosition(QUANTITY_UNIT_SYMBOL_POSITION_MAPPING.get(source.getUnit().getSymbolPosition()));
        }
        if (source.getUnitMultiplier() != null) {
            quantityType.setUnitMultiplier(MapperUtil.getLocalisedLabel(source.getUnitMultiplier().getTitle()));
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
        quantityType.setPercentageOf(MapperUtil.getLocalisedLabel(source.getPercentageOf()));
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

    private void indicatorBaseDoToType(IndicatorVersion source, IndicatorBaseType target) throws MetamacException {
        target.setId(source.getIndicator().getCode());
        target.setKind(IndicatorsRestConstants.KIND_INDICATOR);
        target.setSelfLink(createUrlIndicator(source.getIndicator()));
        target.setCode(source.getIndicator().getCode());
        target.setVersion(source.getVersionNumber());
        target.setTitle(MapperUtil.getLocalisedLabel(source.getTitle()));
        target.setAcronym(MapperUtil.getLocalisedLabel(source.getAcronym()));
        target.setSubjectCode(source.getSubjectCode());
        target.setSubjectTitle(MapperUtil.getLocalisedLabel(source.getSubjectTitle()));

        List<IndicatorsSystemVersion> indicatorsSystemVersions = indicatorsApiService.retrieveIndicatorsSystemPublishedForIndicator(source.getIndicator().getUuid());
        if (indicatorsSystemVersions.size() != 0) {
            List<LinkType> surveyLinks = new ArrayList<LinkType>();
            for (IndicatorsSystemVersion indicatorsSystemVersion : indicatorsSystemVersions) {
                String href = uriLinks.getIndicatorSystemLink(indicatorsSystemVersion.getIndicatorsSystem().getCode());
                surveyLinks.add(new LinkType(IndicatorsRestConstants.KIND_INDICATOR_SYSTEM, href));
            }
            target.setSystemSurveyLinks(surveyLinks);
        }
        target.setQuantity(quantityDoToBaseType(source.getQuantity()));
        target.setConceptDescription(MapperUtil.getLocalisedLabel(source.getConceptDescription()));
        target.setNotes(MapperUtil.getLocalisedLabel(source.getNotes()));
    }

    @Override
    public void indicatorDoToMetadataType(IndicatorVersion source, MetadataType target) throws MetamacException {
        target.setDimension(new LinkedHashMap<String, MetadataDimensionType>());

        // GEOGRAPHICAL
        List<GeographicalGranularity> geographicalGranularities = indicatorsApiService.retrieveGeographicalGranularitiesInIndicatorVersion(source);
        List<GeographicalValueVO> geographicalValues = indicatorsApiService.retrieveGeographicalValuesInIndicatorVersion(source);

        MetadataDimensionType geographicaDimension = createGeographicalDimension(geographicalGranularities, geographicalValues);
        target.getDimension().put(geographicaDimension.getCode(), geographicaDimension);

        // TIME
        List<TimeGranularity> timeGranularities = indicatorsApiService.retrieveTimeGranularitiesInIndicator(source.getIndicator().getUuid());
        List<TimeValue> timeValues = indicatorsApiService.retrieveTimeValuesInIndicatorVersion(source);

        MetadataDimensionType timeDimension = createTimeDimension(timeGranularities, timeValues);
        target.getDimension().put(timeDimension.getCode(), timeDimension);

        // MEASURE
        List<MeasureValue> measureValues = indicatorsApiService.retrieveMeasureValuesInIndicator(source);

        MetadataDimensionType measureDimension = createMeasureDimension(measureValues, source);
        target.getDimension().put(measureDimension.getCode(), measureDimension);

        // ATTRIBUTES
        Map<String, MetadataAttributeType> metadataAttributes = new LinkedHashMap<String, MetadataAttributeType>();
        MetadataAttributeType metadataAttributeUnit = createMetadataAttributeType(PROP_ATTRIBUTE_OBS_CONF);
        metadataAttributes.put(PROP_ATTRIBUTE_OBS_CONF, metadataAttributeUnit);
        target.setAttribute(metadataAttributes);
    }

    private void indicatorDoToType(IndicatorVersion source, IndicatorType target) throws MetamacException {
        indicatorBaseDoToType(source, target);

        target.setDimension(new LinkedHashMap<String, MetadataDimensionType>());

        // GEOGRAPHICAL
        List<GeographicalGranularity> geographicalGranularities = indicatorsApiService.retrieveGeographicalGranularitiesInIndicatorVersion(source);
        List<GeographicalValueVO> geographicalValues = indicatorsApiService.retrieveGeographicalValuesInIndicatorVersion(source);

        MetadataDimensionType geographicaDimension = createGeographicalDimension(geographicalGranularities, geographicalValues);
        target.getDimension().put(geographicaDimension.getCode(), geographicaDimension);

        // TIME
        List<TimeGranularity> timeGranularities = indicatorsApiService.retrieveTimeGranularitiesInIndicator(source.getIndicator().getUuid());
        List<TimeValue> timeValues = indicatorsApiService.retrieveTimeValuesInIndicatorVersion(source);

        MetadataDimensionType timeDimension = createTimeDimension(timeGranularities, timeValues);
        target.getDimension().put(timeDimension.getCode(), timeDimension);

        // MEASURE
        List<MeasureValue> measureValues = indicatorsApiService.retrieveMeasureValuesInIndicator(source);

        MetadataDimensionType measureDimension = createMeasureDimension(measureValues, source);
        target.getDimension().put(measureDimension.getCode(), measureDimension);

        // ATTRIBUTES
        Map<String, MetadataAttributeType> metadataAttributes = new LinkedHashMap<String, MetadataAttributeType>();
        MetadataAttributeType metadataAttributeUnit = createMetadataAttributeType(PROP_ATTRIBUTE_OBS_CONF);
        metadataAttributes.put(PROP_ATTRIBUTE_OBS_CONF, metadataAttributeUnit);
        target.setAttribute(metadataAttributes);

        // CHILD LINK
        target.setChildLink(createLinkTypeIndicatorData(source.getIndicator()));

        // PARENT LINK
        target.setParentLink(createLinkTypeIndicators());

        // DECIMAL PLACES
        target.setDecimalPlaces(source.getQuantity().getDecimalPlaces());
    }

    private MetadataAttributeType createMetadataAttributeType(String code) {
        MetadataAttributeType metadataAttributeUnit = new MetadataAttributeType();
        metadataAttributeUnit.setCode(code);
        String translationCode = new StringBuilder().append(IndicatorsConstants.TRANSLATION_METADATA_ATTRIBUTE).append(".").append(code).toString();
        metadataAttributeUnit.setTitle(MapperUtil.getLocalisedLabel(translationRepository.findTranslationByCode(translationCode).getTitle()));
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
        target.setTitle(MapperUtil.getLocalisedLabel(sourceIndicatorInstance.getTitle()));

        target.setConceptDescription(MapperUtil.getLocalisedLabel(sourceIndicatorVersion.getConceptDescription()));
    }

    @Override
    public void indicatorsInstanceDoToMetadataType(final IndicatorInstance source, final MetadataType target) {
        try {
            IndicatorVersion indicatorVersion = indicatorsApiService.retrieveIndicator(source.getIndicator().getUuid());

            target.setDimension(new LinkedHashMap<String, MetadataDimensionType>());

            // GEOGRAPHICAL
            List<GeographicalGranularity> geographicalGranularities = indicatorsApiService.retrieveGeographicalGranularitiesInIndicatorInstance(source.getUuid());
            List<GeographicalValueVO> geographicalValues = indicatorsApiService.retrieveGeographicalValuesInIndicatorInstance(source.getUuid());

            MetadataDimensionType geographicaDimension = createGeographicalDimension(geographicalGranularities, geographicalValues);
            target.getDimension().put(geographicaDimension.getCode(), geographicaDimension);

            // TIME
            List<TimeGranularity> timeGranularities = indicatorsApiService.retrieveTimeGranularitiesInIndicatorInstance(source.getUuid());
            List<TimeValue> timeValues = indicatorsApiService.retrieveTimeValuesInIndicatorInstance(source.getUuid());

            MetadataDimensionType timeDimension = createTimeDimension(timeGranularities, timeValues);
            target.getDimension().put(timeDimension.getCode(), timeDimension);

            // MEASURE
            List<MeasureValue> measureValues = indicatorsApiService.retrieveMeasureValuesInIndicatorInstance(source.getUuid());

            MetadataDimensionType measureDimension = createMeasureDimension(measureValues, indicatorVersion);
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

            // GEOGRAPHICAL
            List<GeographicalGranularity> geographicalGranularities = indicatorsApiService.retrieveGeographicalGranularitiesInIndicatorInstance(source.getUuid());
            List<GeographicalValueVO> geographicalValues = indicatorsApiService.retrieveGeographicalValuesInIndicatorInstance(source.getUuid());

            MetadataDimensionType geographicaDimension = createGeographicalDimension(geographicalGranularities, geographicalValues);
            target.getDimension().put(geographicaDimension.getCode(), geographicaDimension);

            // TIME
            List<TimeGranularity> timeGranularities = indicatorsApiService.retrieveTimeGranularitiesInIndicatorInstance(source.getUuid());
            List<TimeValue> timeValues = indicatorsApiService.retrieveTimeValuesInIndicatorInstance(source.getUuid());

            MetadataDimensionType timeDimension = createTimeDimension(timeGranularities, timeValues);
            target.getDimension().put(timeDimension.getCode(), timeDimension);

            // MEASURE
            List<MeasureValue> measureValues = indicatorsApiService.retrieveMeasureValuesInIndicatorInstance(source.getUuid());

            MetadataDimensionType measureDimension = createMeasureDimension(measureValues, indicatorVersion);
            target.getDimension().put(measureDimension.getCode(), measureDimension);

            // DECIMAL PLACES
            target.setDecimalPlaces(indicatorVersion.getQuantity().getDecimalPlaces());

            // SUBJECT CODE
            target.setSubjectCode(indicatorVersion.getSubjectCode());

            // SUBJECT TITLE
            target.setSubjectTitle(MapperUtil.getLocalisedLabel(indicatorVersion.getSubjectTitle()));

            // CHILD LINK
            String href = createUrlIndicatorInstanceData(indicatorsSystem, source);
            target.setChildLink(new LinkType(IndicatorsRestConstants.KIND_INDICATOR_INSTANCE_DATA, href));

        } catch (MetamacException e) {
            throw new RestRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    private MetadataDimensionType createMeasureDimension(List<MeasureValue> measureValues, IndicatorVersion indicatorVersion) throws MetamacException {
        MetadataDimensionType measureDimension = new MetadataDimensionType();
        measureDimension.setCode(IndicatorDataDimensionTypeEnum.MEASURE.name());
        measureDimension.setRepresentation(measureValueDoToMeasureRepresentationType(measureValues, indicatorVersion));
        return measureDimension;
    }

    private MetadataDimensionType createGeographicalDimension(List<GeographicalGranularity> geographicalGranularities, List<GeographicalValueVO> geographicalValues) {
        MetadataDimensionType geographicaDimension = new MetadataDimensionType();
        geographicaDimension.setCode(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name());
        geographicaDimension.setGranularity(geographicalGranularityDoToType(geographicalGranularities));
        geographicaDimension.setRepresentation(geographicalValuesDoToMetadataRepresentationType(geographicalValues));
        return geographicaDimension;
    }

    private MetadataDimensionType createTimeDimension(List<TimeGranularity> timeGranularities, List<TimeValue> timeValues) {
        MetadataDimensionType timeDimension = new MetadataDimensionType();
        timeDimension.setCode(IndicatorDataDimensionTypeEnum.TIME.name());
        timeDimension.setGranularity(timeGranularityDoToType(timeGranularities));
        timeDimension.setRepresentation(timeValuesDoToMetadataRepresentationType(timeValues));
        return timeDimension;
    }

    private List<MetadataRepresentationType> geographicalValuesDoToMetadataRepresentationType(List<GeographicalValueVO> geographicalValues) {
        if (CollectionUtils.isEmpty(geographicalValues)) {
            return null;
        }
        List<MetadataRepresentationType> geographicalValueTypes = new ArrayList<MetadataRepresentationType>(geographicalValues.size());
        for (GeographicalValueVO geographicalValue : geographicalValues) {
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
        metadataRepresentationType.setTitle(MapperUtil.getLocalisedLabel(geographicalValue.getTitle()));
        metadataRepresentationType.setGranularityCode(geographicalValue.getGranularity().getCode());
        return metadataRepresentationType;
    }

    protected MetadataRepresentationType geographicalValueDoToMetadataRepresentationType(GeographicalValue geographicalValue) {
        MetadataRepresentationType metadataRepresentationType = new MetadataRepresentationType();
        metadataRepresentationType.setCode(geographicalValue.getCode());
        metadataRepresentationType.setLatitude(geographicalValue.getLatitude());
        metadataRepresentationType.setLongitude(geographicalValue.getLongitude());
        metadataRepresentationType.setTitle(MapperUtil.getLocalisedLabel(geographicalValue.getTitle()));
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
        metadataRepresentationType.setTitle(MapperUtil.getLocalisedLabel(timeValue.getTitle()));
        return metadataRepresentationType;
    }

    private List<MetadataRepresentationType> measureValueDoToMeasureRepresentationType(List<MeasureValue> measureValues, IndicatorVersion indicatorVersion) throws MetamacException {
        if (CollectionUtils.isEmpty(measureValues)) {
            return null;
        }
        List<MetadataRepresentationType> measureValueTypes = new ArrayList<MetadataRepresentationType>(measureValues.size());
        for (MeasureValue measureValue : measureValues) {
            MetadataRepresentationType metadataRepresentationType = new MetadataRepresentationType();
            metadataRepresentationType.setCode(measureValue.getMeasureValue().getName());
            metadataRepresentationType.setTitle(MapperUtil.getLocalisedLabel(measureValue.getTitle()));
            Quantity quantity = getQuantityForMeasure(measureValue.getMeasureValue(), indicatorVersion);
            if (quantity != null) {
                metadataRepresentationType.setQuantity(quantityDoToBaseType(quantity));

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
        link.setTitle(MapperUtil.getLocalisedLabel(indicatorVersion.getTitle()));
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
            target.setTitle(MapperUtil.getLocalisedLabel(source.getDimension().getTitle()));
        } else {
            target.setId(source.getIndicatorInstance().getCode());
            target.setKind(IndicatorsRestConstants.KIND_INDICATOR_INSTANCE);
            target.setTitle(MapperUtil.getLocalisedLabel(source.getIndicatorInstance().getTitle()));

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
        operationBaseDoToType(sourceOperation, target);
        return target;
    }

    private IndicatorsSystemType indicatorsSystemDoToType(IndicatorsSystemVersion sourceIndicatorsSystem, OperationIndicators sourceOperation) {
        IndicatorsSystemType target = new IndicatorsSystemType();
        indicatorsSystemDoToType(sourceIndicatorsSystem, target);
        operationBaseDoToType(sourceOperation, target);
        elementsLevelsDoToType(sourceIndicatorsSystem.getChildrenFirstLevel(), target);
        return target;
    }

    private void indicatorsSystemDoToBaseType(IndicatorsSystemVersion source, IndicatorsSystemBaseType target) {
        String selfLinkURL = createUrlIndicatorsSystem(source.getIndicatorsSystem());

        target.setKind(IndicatorsRestConstants.KIND_INDICATOR_SYSTEM);
        target.setSelfLink(selfLinkURL);
        target.setVersion(source.getVersionNumber());
        target.setPublicationDate(source.getPublicationDate());
    }

    private void indicatorsSystemDoToType(IndicatorsSystemVersion source, IndicatorsSystemType target) {
        indicatorsSystemDoToBaseType(source, target);

        String childLinkURL = createUrlIndicatorsSystemInstances(source.getIndicatorsSystem());
        target.setChildLink(new LinkType(IndicatorsRestConstants.KIND_INDICATOR_INSTANCES, childLinkURL));

        String parentLink = uriLinks.getIndicatorsSystemsLink();
        target.setParentLink(new LinkType(IndicatorsRestConstants.KIND_INDICATOR_SYSTEMS, parentLink));
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

}
