package es.gobcan.istac.indicators.rest.mapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponentsBuilder;

import com.arte.statistic.dataset.repository.dto.AttributeInstanceObservationDto;
import com.arte.statistic.dataset.repository.dto.CodeDimensionDto;
import com.arte.statistic.dataset.repository.dto.ObservationDto;
import com.arte.statistic.dataset.repository.dto.ObservationExtendedDto;

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
import es.gobcan.istac.indicators.core.enume.domain.IndicatorDataAttributeTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorDataDimensionTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.MeasureDimensionTypeEnum;
import es.gobcan.istac.indicators.core.repositoryimpl.finders.SubjectIndicatorResult;
import es.gobcan.istac.indicators.core.vo.GeographicalValueVO;
import es.gobcan.istac.indicators.rest.RestConstants;
import es.gobcan.istac.indicators.rest.clients.StatisticalOperationsRestInternalFacade;
import es.gobcan.istac.indicators.rest.clients.adapters.OperationIndicators;
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

    private final Logger                                         LOG                              = LoggerFactory.getLogger(Do2TypeMapperImpl.class);
    private static final String                                  PROP_ATTRIBUTE_OBS_CONF_LABEL_EN = "Observation confidenciality";
    private static final String                                  PROP_ATTRIBUTE_OBS_CONF_LABEL_ES = "Confidencialidad de la observación";
    private static String                                        PROP_ATTRIBUTE_OBS_CONF          = "OBS_CONF";

    private static ThreadLocal<Map<String, Map<String, Object>>> requestCache                     = new ThreadLocal<Map<String, Map<String, Object>>>() {

                                                                                                      @Override
                                                                                                      protected java.util.Map<String, Map<String, Object>> initialValue() {
                                                                                                          return new HashMap<String, Map<String, Object>>();
                                                                                                      }

                                                                                                      ;
                                                                                                  };

    @Autowired
    private final IndicatorsApiService                           indicatorsApiService             = null;

    @Autowired
    private final StatisticalOperationsRestInternalFacade        statisticalOperations            = null;

    private static final List<String>                            measuresOrder                    = Arrays.asList(MeasureDimensionTypeEnum.ABSOLUTE.name(),
                                                                                                          MeasureDimensionTypeEnum.ANNUAL_PERCENTAGE_RATE.name(),
                                                                                                          MeasureDimensionTypeEnum.INTERPERIOD_PERCENTAGE_RATE.name(),
                                                                                                          MeasureDimensionTypeEnum.ANNUAL_PUNTUAL_RATE.name(),
                                                                                                          MeasureDimensionTypeEnum.INTERPERIOD_PUNTUAL_RATE.name());

    @Override
    public IndicatorsSystemBaseType indicatorsSystemDoToBaseType(IndicatorsSystemVersion source, final String baseURL) {
        Assert.notNull(source);
        Assert.notNull(baseURL);

        try {
            OperationIndicators sourceOperationBase = statisticalOperations.retrieveOperationById(source.getIndicatorsSystem().getCode());
            IndicatorsSystemBaseType target = _indicatorsSystemDoToBaseType(source, sourceOperationBase, baseURL);
            return target;
        } catch (Exception e) {
            throw new RestRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public IndicatorsSystemType indicatorsSystemDoToType(IndicatorsSystemVersion source, final String baseURL) {
        Assert.notNull(source);
        Assert.notNull(baseURL);

        try {
            OperationIndicators sourceOperation = statisticalOperations.retrieveOperationById(source.getIndicatorsSystem().getCode());

            IndicatorsSystemType target = _indicatorsSystemDoToType(source, sourceOperation, baseURL);
            return target;
        } catch (Exception e) {
            throw new RestRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public List<IndicatorsSystemBaseType> indicatorsSystemDoToBaseType(List<IndicatorsSystemVersion> sources, final String baseURL) {
        Assert.notNull(sources);
        Assert.notNull(baseURL);

        try {
            List<IndicatorsSystemBaseType> targets = new ArrayList<IndicatorsSystemBaseType>(sources.size());
            for (IndicatorsSystemVersion source : sources) {
                OperationIndicators sourceOperation = statisticalOperations.retrieveOperationById(source.getIndicatorsSystem().getCode()); // IDEA Make in only one invocation
                IndicatorsSystemBaseType target = _indicatorsSystemDoToBaseType(source, sourceOperation, baseURL);
                targets.add(target);
            }
            return targets;
        } catch (Exception e) {
            throw new RestRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public IndicatorsSystemHistoryType indicatorsSystemHistoryDoToType(final IndicatorsSystemHistory source, final String baseURL) {
        IndicatorsSystemHistoryType target = new IndicatorsSystemHistoryType();
        target.setIndicatorSystemId(source.getIndicatorsSystem().getUuid());
        target.setVersion(source.getVersionNumber());
        target.setPublicationDate(source.getPublicationDate().toDate());

        return target;
    }

    @Override
    public IndicatorInstanceType indicatorsInstanceDoToType(IndicatorInstance source, final String baseURL) {
        Assert.notNull(source);
        Assert.notNull(baseURL);

        IndicatorInstanceType target = new IndicatorInstanceType();
        _indicatorsInstanceDoToType(source, target, baseURL);
        return target;
    }

    @Override
    public List<IndicatorInstanceBaseType> indicatorsInstanceDoToBaseType(List<IndicatorInstance> sources, final String baseURL) {
        Assert.notNull(sources);

        try {
            List<IndicatorInstanceBaseType> targets = new ArrayList<IndicatorInstanceBaseType>(sources.size());
            for (IndicatorInstance source : sources) {
                IndicatorInstanceBaseType target = new IndicatorInstanceBaseType();
                IndicatorVersion indicatorVersion = indicatorsApiService.retrieveIndicator(source.getIndicator().getUuid()); // IDEA Make in only one invocation
                _indicatorsInstanceDoToType(source, indicatorVersion, target, baseURL);
                targets.add(target);
            }
            return targets;
        } catch (MetamacException e) {
            throw new RestRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override
    public IndicatorType indicatorDoToType(IndicatorVersion source, String baseURL) {
        Assert.notNull(source);
        try {
            IndicatorType target = new IndicatorType();
            _indicatorDoToType(source, target, baseURL);
            return target;
        } catch (Exception e) {
            throw new RestRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public List<IndicatorBaseType> indicatorDoToBaseType(final List<IndicatorVersion> sources, final String baseURL) {
        Assert.notNull(sources);
        try {
            List<IndicatorBaseType> targets = new ArrayList<IndicatorBaseType>(sources.size());
            for (IndicatorVersion source : sources) {
                IndicatorBaseType target = new IndicatorBaseType();
                _indicatorDoToType(source, target, baseURL);
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
            result.add(type);
        }

        return result;
    }

    private void _subjectDoToBaseType(SubjectIndicatorResult subject, SubjectBaseType subjectBaseType, String baseUrl) {
        subjectBaseType.setId(subject.getId());
        subjectBaseType.setCode(subject.getId());
        subjectBaseType.setKind(RestConstants.API_INDICATORS_SUBJECTS);
        subjectBaseType.setSelfLink(_createUrlSubject(subject, baseUrl));
        subjectBaseType.setTitle(MapperUtil.getLocalisedLabel(subject.getTitle()));
    }

    @Override
    public SubjectType subjectDoToType(final SubjectIndicatorResult subject, List<IndicatorVersion> indicators, String baseUrl) {
        if (subject == null) {
            return null;
        }
        SubjectType subjectType = new SubjectType();
        _subjectDoToBaseType(subject, subjectType, baseUrl);
        subjectType.setElements(indicatorDoToBaseType(indicators, baseUrl));

        return subjectType;
    }

    @Override
    public List<SubjectBaseType> subjectDoToBaseType(List<SubjectIndicatorResult> subjects, String baseUrl) {
        if (CollectionUtils.isEmpty(subjects)) {
            return null;
        }
        List<SubjectBaseType> subjectTypes = new ArrayList<SubjectBaseType>(subjects.size());
        for (SubjectIndicatorResult subject : subjects) {
            SubjectBaseType subjectType = new SubjectType();
            _subjectDoToBaseType(subject, subjectType, baseUrl);
            subjectTypes.add(subjectType);
        }
        return subjectTypes;
    }

    @Override
    public DataType createDataType(DataTypeRequest dataTypeRequest, boolean includeObservationMetadata) {
        requestCache.remove(); // Remove All Cache
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
                            observationDto.setPrimaryMeasure(".");
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

    private QuantityType quantityDoToBaseType(final Quantity source, final String baseURL) throws MetamacException {
        Assert.notNull(source);

        QuantityType quantityType = new QuantityType();
        quantityType.setType(source.getQuantityType());
        if (source.getUnit() != null) {
            quantityType.setUnit(MapperUtil.getLocalisedLabel(source.getUnit().getTitle()));
            quantityType.setUnitSymbol(source.getUnit().getSymbol());
            quantityType.setUnitSymbolPosition(source.getUnit().getSymbolPosition());
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
            quantityType.setDenominatorLink(_createTitleLinkType(indVersion, baseURL));
        }
        if (source.getNumerator() != null) {
            IndicatorVersion indVersion = indicatorsApiService.retrieveIndicator(source.getNumerator().getUuid());
            quantityType.setNumeratorLink(_createTitleLinkType(indVersion, baseURL));
        }

        quantityType.setIsPercentage(source.getIsPercentage());
        quantityType.setPercentageOf(MapperUtil.getLocalisedLabel(source.getPercentageOf()));
        quantityType.setBaseValue(source.getBaseValue());
        if (source.getBaseTime() != null) {
            TimeValue timeValue = indicatorsApiService.retrieveTimeValueByCode(source.getBaseTime());
            quantityType.setBaseTime(_timeValueDoToType(timeValue));
        }
        if (source.getBaseLocation() != null) {
            GeographicalValue geoValue = indicatorsApiService.retrieveGeographicalValueByCode(source.getBaseLocation().getCode());
            quantityType.setBaseLocation(_geographicalValueDoToType(geoValue));
        }
        if (source.getBaseQuantity() != null) {
            IndicatorVersion indVersion = indicatorsApiService.retrieveIndicator(source.getBaseQuantity().getUuid());
            quantityType.setBaseQuantityLink(_createTitleLinkType(indVersion, baseURL));
        }

        return quantityType;
    }

    private void _indicatorDoToType(IndicatorVersion source, IndicatorBaseType target, String baseURL) throws Exception {
        target.setId(source.getIndicator().getCode());
        target.setKind(RestConstants.KIND_INDICATOR);
        target.setSelfLink(_createUrlIndicators_Indicator(source.getIndicator(), baseURL));
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
                String href = _createUrlIndicatorsSystems_IndicatorsSystem(indicatorsSystemVersion.getIndicatorsSystem(), baseURL);
                LinkType linkType = new LinkType();
                linkType.setKind(RestConstants.KIND_INDICATOR_SYSTEM);
                linkType.setHref(href);
                surveyLinks.add(linkType);
            }
            target.setSystemSurveyLinks(surveyLinks);
        }
        target.setQuantity(quantityDoToBaseType(source.getQuantity(), baseURL));
        target.setConceptDescription(MapperUtil.getLocalisedLabel(source.getConceptDescription()));
        target.setNotes(MapperUtil.getLocalisedLabel(source.getNotes()));
    }

    @Override
    public void indicatorDoToMetadataType(IndicatorVersion source, MetadataType target, String baseURL) throws Exception {
        target.setDimension(new LinkedHashMap<String, MetadataDimensionType>());

        // GEOGRAPHICAL
        List<GeographicalGranularity> geographicalGranularities = indicatorsApiService.retrieveGeographicalGranularitiesInIndicatorVersion(source);
        List<GeographicalValueVO> geographicalValues = indicatorsApiService.retrieveGeographicalValuesInIndicatorVersion(source);

        MetadataDimensionType geographicaDimension = _createGeographicalDimension(geographicalGranularities, geographicalValues);
        target.getDimension().put(geographicaDimension.getCode(), geographicaDimension);

        // TIME
        List<TimeGranularity> timeGranularities = indicatorsApiService.retrieveTimeGranularitiesInIndicator(source.getIndicator().getUuid());
        List<TimeValue> timeValues = indicatorsApiService.retrieveTimeValuesInIndicatorVersion(source);

        MetadataDimensionType timeDimension = _createTimeDimension(timeGranularities, timeValues);
        target.getDimension().put(timeDimension.getCode(), timeDimension);

        // MEASURE
        List<MeasureValue> measureValues = indicatorsApiService.retrieveMeasureValuesInIndicator(source);

        MetadataDimensionType measureDimension = createMeasureDimension(measureValues, source, baseURL);
        target.getDimension().put(measureDimension.getCode(), measureDimension);

        // ATTRIBUTES
        Map<String, MetadataAttributeType> metadataAttributes = new LinkedHashMap<String, MetadataAttributeType>();

        MetadataAttributeType metadataAttributeUnit = createMetadataAttributeType(PROP_ATTRIBUTE_OBS_CONF, PROP_ATTRIBUTE_OBS_CONF_LABEL_ES, PROP_ATTRIBUTE_OBS_CONF_LABEL_EN);
        metadataAttributes.put(PROP_ATTRIBUTE_OBS_CONF, metadataAttributeUnit);

        target.setAttribute(metadataAttributes);
    }

    private void _indicatorDoToType(IndicatorVersion source, IndicatorType target, String baseURL) throws Exception {
        _indicatorDoToType(source, (IndicatorBaseType) target, baseURL);

        target.setDimension(new LinkedHashMap<String, MetadataDimensionType>());

        // GEOGRAPHICAL
        List<GeographicalGranularity> geographicalGranularities = indicatorsApiService.retrieveGeographicalGranularitiesInIndicatorVersion(source);
        List<GeographicalValueVO> geographicalValues = indicatorsApiService.retrieveGeographicalValuesInIndicatorVersion(source);

        MetadataDimensionType geographicaDimension = _createGeographicalDimension(geographicalGranularities, geographicalValues);
        target.getDimension().put(geographicaDimension.getCode(), geographicaDimension);

        // TIME
        List<TimeGranularity> timeGranularities = indicatorsApiService.retrieveTimeGranularitiesInIndicator(source.getIndicator().getUuid());
        List<TimeValue> timeValues = indicatorsApiService.retrieveTimeValuesInIndicatorVersion(source);

        MetadataDimensionType timeDimension = _createTimeDimension(timeGranularities, timeValues);
        target.getDimension().put(timeDimension.getCode(), timeDimension);

        // MEASURE
        List<MeasureValue> measureValues = indicatorsApiService.retrieveMeasureValuesInIndicator(source);

        MetadataDimensionType measureDimension = createMeasureDimension(measureValues, source, baseURL);
        target.getDimension().put(measureDimension.getCode(), measureDimension);

        // ATTRIBUTES
        Map<String, MetadataAttributeType> metadataAttributes = new LinkedHashMap<String, MetadataAttributeType>();
        MetadataAttributeType metadataAttributeUnit = createMetadataAttributeType(PROP_ATTRIBUTE_OBS_CONF, PROP_ATTRIBUTE_OBS_CONF_LABEL_ES, PROP_ATTRIBUTE_OBS_CONF_LABEL_EN);
        metadataAttributes.put(PROP_ATTRIBUTE_OBS_CONF, metadataAttributeUnit);
        target.setAttribute(metadataAttributes);

        // CHILD LINK
        target.setChildLink(_createLinkTypeIndicatorData(source.getIndicator(), baseURL));

        // DECIMAL PLACES
        target.setDecimalPlaces(source.getQuantity().getDecimalPlaces());
    }

    private MetadataAttributeType createMetadataAttributeType(String code, String spanishLabel, String englishLabel) {
        LocalisedString localisedStringES = new LocalisedString();
        localisedStringES.setLocale("es");
        localisedStringES.setLabel(spanishLabel);
        LocalisedString localisedStringEN = new LocalisedString();
        localisedStringEN.setLocale("en");
        localisedStringEN.setLabel(englishLabel);
        InternationalString internationalString = new InternationalString();
        internationalString.getTexts().add(localisedStringES);
        internationalString.getTexts().add(localisedStringEN);

        MetadataAttributeType metadataAttributeUnit = new MetadataAttributeType();
        metadataAttributeUnit.setCode(code);
        metadataAttributeUnit.setTitle(MapperUtil.getLocalisedLabel(internationalString));
        metadataAttributeUnit.setAttachmentLevel(AttributeAttachmentLevelEnumType.OBSERVATION);
        return metadataAttributeUnit;
    }

    private void _indicatorsInstanceDoToType(final IndicatorInstance sourceIndicatorInstance, final IndicatorVersion sourceIndicatorVersion, final IndicatorInstanceBaseType target,
            final String baseURL) {
        Assert.notNull(sourceIndicatorInstance);
        Assert.notNull(baseURL);

        IndicatorsSystem indicatorsSystem = sourceIndicatorInstance.getElementLevel().getIndicatorsSystemVersion().getIndicatorsSystem();
        String parentLinkURL = _createUrlIndicatorsSystems_IndicatorsSystem(indicatorsSystem, baseURL);
        String selfLinkURL = _createUrlIndicatorSystems_IndicatorsSystem_Instances_Instance(indicatorsSystem, sourceIndicatorInstance, baseURL);

        target.setId(sourceIndicatorInstance.getCode());
        target.setKind(RestConstants.KIND_INDICATOR_INSTANCE);
        target.setSelfLink(selfLinkURL);

        LinkType parentLink = new LinkType();
        parentLink.setKind(RestConstants.KIND_INDICATOR_SYSTEM);
        parentLink.setHref(parentLinkURL);
        target.setParentLink(parentLink);

        target.setSystemCode(indicatorsSystem.getCode());
        target.setTitle(MapperUtil.getLocalisedLabel(sourceIndicatorInstance.getTitle()));

        target.setConceptDescription(MapperUtil.getLocalisedLabel(sourceIndicatorVersion.getConceptDescription()));
    }

    @Override
    public void indicatorsInstanceDoToMetadataType(final IndicatorInstance source, final MetadataType target, final String baseURL) {
        try {
            IndicatorVersion indicatorVersion = indicatorsApiService.retrieveIndicator(source.getIndicator().getUuid());

            target.setDimension(new LinkedHashMap<String, MetadataDimensionType>());

            // GEOGRAPHICAL
            List<GeographicalGranularity> geographicalGranularities = indicatorsApiService.retrieveGeographicalGranularitiesInIndicatorInstance(source.getUuid());
            List<GeographicalValueVO> geographicalValues = indicatorsApiService.retrieveGeographicalValuesInIndicatorInstance(source.getUuid());

            MetadataDimensionType geographicaDimension = _createGeographicalDimension(geographicalGranularities, geographicalValues);
            target.getDimension().put(geographicaDimension.getCode(), geographicaDimension);

            // TIME
            List<TimeGranularity> timeGranularities = indicatorsApiService.retrieveTimeGranularitiesInIndicatorInstance(source.getUuid());
            List<TimeValue> timeValues = indicatorsApiService.retrieveTimeValuesInIndicatorInstance(source.getUuid());

            MetadataDimensionType timeDimension = _createTimeDimension(timeGranularities, timeValues);
            target.getDimension().put(timeDimension.getCode(), timeDimension);

            // MEASURE
            List<MeasureValue> measureValues = indicatorsApiService.retrieveMeasureValuesInIndicatorInstance(source.getUuid());

            MetadataDimensionType measureDimension = createMeasureDimension(measureValues, indicatorVersion, baseURL);
            target.getDimension().put(measureDimension.getCode(), measureDimension);
        } catch (MetamacException e) {
            throw new RestRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    private void _indicatorsInstanceDoToType(final IndicatorInstance source, final IndicatorInstanceType target, final String baseURL) {
        try {
            IndicatorVersion indicatorVersion = indicatorsApiService.retrieveIndicator(source.getIndicator().getUuid());

            _indicatorsInstanceDoToType(source, indicatorVersion, target, baseURL);

            IndicatorsSystem indicatorsSystem = source.getElementLevel().getIndicatorsSystemVersion().getIndicatorsSystem();
            target.setDimension(new LinkedHashMap<String, MetadataDimensionType>());

            // GEOGRAPHICAL
            List<GeographicalGranularity> geographicalGranularities = indicatorsApiService.retrieveGeographicalGranularitiesInIndicatorInstance(source.getUuid());
            List<GeographicalValueVO> geographicalValues = indicatorsApiService.retrieveGeographicalValuesInIndicatorInstance(source.getUuid());

            MetadataDimensionType geographicaDimension = _createGeographicalDimension(geographicalGranularities, geographicalValues);
            target.getDimension().put(geographicaDimension.getCode(), geographicaDimension);

            // TIME
            List<TimeGranularity> timeGranularities = indicatorsApiService.retrieveTimeGranularitiesInIndicatorInstance(source.getUuid());
            List<TimeValue> timeValues = indicatorsApiService.retrieveTimeValuesInIndicatorInstance(source.getUuid());

            MetadataDimensionType timeDimension = _createTimeDimension(timeGranularities, timeValues);
            target.getDimension().put(timeDimension.getCode(), timeDimension);

            // MEASURE
            List<MeasureValue> measureValues = indicatorsApiService.retrieveMeasureValuesInIndicatorInstance(source.getUuid());

            MetadataDimensionType measureDimension = createMeasureDimension(measureValues, indicatorVersion, baseURL);
            target.getDimension().put(measureDimension.getCode(), measureDimension);

            // DECIMAL PLACES
            target.setDecimalPlaces(indicatorVersion.getQuantity().getDecimalPlaces());

            // SUBJECT CODE
            target.setSubjectCode(indicatorVersion.getSubjectCode());

            // SUBJECT TITLE
            target.setSubjectTitle(MapperUtil.getLocalisedLabel(indicatorVersion.getSubjectTitle()));

            String childLinkURL = _createUrlIndicatorSystems_IndicatorsSystem_Instances_Instance_Data(indicatorsSystem, source, baseURL);
            LinkType childLink = new LinkType();
            childLink.setKind(RestConstants.KIND_INDICATOR_INSTANCE_DATA);
            childLink.setHref(childLinkURL);
            target.setChildLink(childLink);
        } catch (MetamacException e) {
            throw new RestRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    private MetadataDimensionType createMeasureDimension(List<MeasureValue> measureValues, IndicatorVersion indicatorVersion, String baseURL) throws MetamacException {
        MetadataDimensionType measureDimension = new MetadataDimensionType();
        measureDimension.setCode(IndicatorDataDimensionTypeEnum.MEASURE.name());
        measureDimension.setRepresentation(_measureValueDoToType(measureValues, indicatorVersion, baseURL));
        return measureDimension;
    }

    private MetadataDimensionType _createGeographicalDimension(List<GeographicalGranularity> geographicalGranularities, List<GeographicalValueVO> geographicalValues) {
        MetadataDimensionType geographicaDimension = new MetadataDimensionType();
        geographicaDimension.setCode(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name());
        geographicaDimension.setGranularity(geographicalGranularityDoToType(geographicalGranularities));
        geographicaDimension.setRepresentation(_geographicalValuesDoToType(geographicalValues));
        return geographicaDimension;
    }

    private MetadataDimensionType _createTimeDimension(List<TimeGranularity> timeGranularities, List<TimeValue> timeValues) {
        MetadataDimensionType timeDimension = new MetadataDimensionType();
        timeDimension.setCode(IndicatorDataDimensionTypeEnum.TIME.name());
        timeDimension.setGranularity(timeGranularityDoToType(timeGranularities));
        timeDimension.setRepresentation(_timeValuesDoToType(timeValues));
        return timeDimension;
    }

    private List<MetadataRepresentationType> _geographicalValuesDoToType(List<GeographicalValueVO> geographicalValues) {
        if (CollectionUtils.isEmpty(geographicalValues)) {
            return null;
        }
        List<MetadataRepresentationType> geographicalValueTypes = new ArrayList<MetadataRepresentationType>(geographicalValues.size());
        for (GeographicalValueVO geographicalValue : geographicalValues) {
            MetadataRepresentationType metadataRepresentationType = _geographicalValueVOToType(geographicalValue);
            geographicalValueTypes.add(metadataRepresentationType);
        }
        return geographicalValueTypes;
    }

    protected MetadataRepresentationType _geographicalValueVOToType(GeographicalValueVO geographicalValue) {
        MetadataRepresentationType metadataRepresentationType = new MetadataRepresentationType();
        metadataRepresentationType.setCode(geographicalValue.getCode());
        metadataRepresentationType.setLatitude(geographicalValue.getLatitude());
        metadataRepresentationType.setLongitude(geographicalValue.getLongitude());
        metadataRepresentationType.setTitle(MapperUtil.getLocalisedLabel(geographicalValue.getTitle()));
        metadataRepresentationType.setGranularityCode(geographicalValue.getGranularity().getCode());
        return metadataRepresentationType;
    }

    protected MetadataRepresentationType _geographicalValueDoToType(GeographicalValue geographicalValue) {
        MetadataRepresentationType metadataRepresentationType = new MetadataRepresentationType();
        metadataRepresentationType.setCode(geographicalValue.getCode());
        metadataRepresentationType.setLatitude(geographicalValue.getLatitude());
        metadataRepresentationType.setLongitude(geographicalValue.getLongitude());
        metadataRepresentationType.setTitle(MapperUtil.getLocalisedLabel(geographicalValue.getTitle()));
        metadataRepresentationType.setGranularityCode(geographicalValue.getGranularity().getCode());
        return metadataRepresentationType;
    }

    private List<MetadataRepresentationType> _timeValuesDoToType(List<TimeValue> timeValues) {
        if (CollectionUtils.isEmpty(timeValues)) {
            return null;
        }
        List<MetadataRepresentationType> timeValueTypes = new ArrayList<MetadataRepresentationType>(timeValues.size());
        for (TimeValue timeValue : timeValues) {
            MetadataRepresentationType metadataRepresentationType = _timeValueDoToType(timeValue);
            timeValueTypes.add(metadataRepresentationType);
        }
        return timeValueTypes;
    }

    protected MetadataRepresentationType _timeValueDoToType(TimeValue timeValue) {
        MetadataRepresentationType metadataRepresentationType = new MetadataRepresentationType();
        metadataRepresentationType.setCode(timeValue.getTimeValue());
        metadataRepresentationType.setGranularityCode(timeValue.getGranularity().getName());
        metadataRepresentationType.setTitle(MapperUtil.getLocalisedLabel(timeValue.getTitle()));
        return metadataRepresentationType;
    }

    private List<MetadataRepresentationType> _measureValueDoToType(List<MeasureValue> measureValues, IndicatorVersion indicatorVersion, String baseURL) throws MetamacException {
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
                metadataRepresentationType.setQuantity(quantityDoToBaseType(quantity, baseURL));
            }
            measureValueTypes.add(metadataRepresentationType);
        }
        sortMeasureValuesTypes(measureValueTypes);
        return measureValueTypes;
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
                        rateDerivation.getQuantity().setUnitMultiplier(null);
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
                        rateDerivation.getQuantity().setUnitMultiplier(null);
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

    private TitleLinkType _createTitleLinkType(final IndicatorVersion indicatorVersion, final String baseURL) {
        String href = _createUrlIndicators_Indicator(indicatorVersion.getIndicator(), baseURL);
        TitleLinkType link = new TitleLinkType();
        link.setKind(RestConstants.KIND_INDICATOR);
        link.setHref(href);
        link.setTitle(MapperUtil.getLocalisedLabel(indicatorVersion.getTitle()));
        return link;
    }

    private LinkType _createLinkTypeIndicatorData(final Indicator indicator, final String baseURL) {
        String href = _createUrlIndicators_Indicator_Data(indicator, baseURL);
        LinkType link = new LinkType();
        link.setKind(RestConstants.KIND_INDICATOR_DATA);
        link.setHref(href);
        return link;
    }

    private void _elementsLevelsDoToType(final List<ElementLevel> sources, IndicatorsSystemType target, final String baseURL) {
        if (CollectionUtils.isEmpty(sources)) {
            return;
        }

        List<ElementLevelType> targetElements = _elementsLevelsDoToType(sources, baseURL);
        target.setElements(targetElements);
    }

    private List<ElementLevelType> _elementsLevelsDoToType(final List<ElementLevel> sources, final String baseURL) {
        if (CollectionUtils.isEmpty(sources)) {
            return null;
        }

        List<ElementLevelType> targetElements = new ArrayList<ElementLevelType>();
        for (ElementLevel source : sources) {
            ElementLevelType targetElement = _elementsLevelsDoToType(source, baseURL);
            targetElements.add(targetElement);
        }
        return targetElements;
    }

    private ElementLevelType _elementsLevelsDoToType(ElementLevel source, final String baseURL) {
        ElementLevelType target = new ElementLevelType();

        if (source.getDimension() != null) {
            target.setId(source.getDimension().getUuid());
            target.setKind(RestConstants.KIND_INDICATOR_DIMENSION);
            target.setTitle(MapperUtil.getLocalisedLabel(source.getDimension().getTitle()));
        } else {
            target.setId(source.getIndicatorInstance().getCode());
            target.setKind(RestConstants.KIND_INDICATOR_INSTANCE);
            target.setTitle(MapperUtil.getLocalisedLabel(source.getIndicatorInstance().getTitle()));

            IndicatorsSystem indicatorsSystem = source.getIndicatorsSystemVersion().getIndicatorsSystem();
            String selfLinkURL = _createUrlIndicatorSystems_IndicatorsSystem_Instances_Instance(indicatorsSystem, source.getIndicatorInstance(), baseURL);
            target.setSelfLink(selfLinkURL);
        }

        List<ElementLevelType> targetSubElements = _elementsLevelsDoToType(source.getChildren(), baseURL);
        target.setElements(targetSubElements);

        return target;
    }

    private IndicatorsSystemBaseType _indicatorsSystemDoToBaseType(IndicatorsSystemVersion sourceIndicatorsSystem, OperationIndicators sourceOperation, final String baseURL) {
        IndicatorsSystemBaseType target = new IndicatorsSystemBaseType();
        _indicatorsSystemDoToType(sourceIndicatorsSystem, target, baseURL);
        _operationBaseDoToType(sourceOperation, target, baseURL);
        return target;
    }

    private IndicatorsSystemType _indicatorsSystemDoToType(IndicatorsSystemVersion sourceIndicatorsSystem, OperationIndicators sourceOperation, final String baseURL) {
        IndicatorsSystemType target = new IndicatorsSystemType();
        _indicatorsSystemDoToType(sourceIndicatorsSystem, target, baseURL);
        _operationBaseDoToType(sourceOperation, target, baseURL);
        _elementsLevelsDoToType(sourceIndicatorsSystem.getChildrenFirstLevel(), target, baseURL);
        return target;
    }

    private void _indicatorsSystemDoToType(IndicatorsSystemVersion source, IndicatorsSystemBaseType target, final String baseURL) {
        String selfLinkURL = _createUrlIndicatorsSystems_IndicatorsSystem(source.getIndicatorsSystem(), baseURL);

        target.setKind(RestConstants.KIND_INDICATOR_SYSTEM);
        target.setSelfLink(selfLinkURL);
        target.setVersion(source.getVersionNumber());
        target.setPublicationDate(source.getPublicationDate());
    }

    private void _indicatorsSystemDoToType(IndicatorsSystemVersion source, IndicatorsSystemType target, final String baseURL) {
        _indicatorsSystemDoToType(source, (IndicatorsSystemBaseType) target, baseURL);

        String childLinkURL = _createUrlIndicatorsSystems_IndicatorsSystem_Instances(source.getIndicatorsSystem(), baseURL);

        LinkType childLink = new LinkType();
        childLink.setKind(RestConstants.KIND_INDICATOR_INSTANCES);
        childLink.setHref(childLinkURL);

        target.setChildLink(childLink);
    }

    private void _operationBaseDoToType(OperationIndicators sourceOperation, IndicatorsSystemBaseType target, final String baseURL) {
        target.setId(sourceOperation.getId());
        target.setCode(sourceOperation.getId());
        target.setTitle(sourceOperation.getTitle());
        target.setAcronym(sourceOperation.getAcronym());
        target.setDescription(sourceOperation.getDescription());
        target.setObjective(sourceOperation.getObjective());

        LinkType statisticalOperatioLink = new LinkType();
        statisticalOperatioLink.setKind(RestConstants.KIND_STATISTICAL_OPERATION);
        statisticalOperatioLink.setHref(sourceOperation.getUri());
        target.setStatisticalOperationLink(statisticalOperatioLink);
    }

    private String _createUrlIndicatorsSystems_IndicatorsSystem(final IndicatorsSystem indicatorsSystem, final String baseURL) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(baseURL);
        _createUrlIndicatorsSystems_IndicatorsSystem(indicatorsSystem, uriComponentsBuilder);
        return uriComponentsBuilder.build().encode().toUriString();
    }

    private String _createUrlIndicatorsSystems_IndicatorsSystem_Instances(final IndicatorsSystem indicatorsSystem, final String baseURL) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(baseURL);
        _createUrlIndicatorsSystems_IndicatorsSystem_Instances(indicatorsSystem, uriComponentsBuilder);
        return uriComponentsBuilder.build().encode().toUriString();
    }

    private String _createUrlIndicatorSystems_IndicatorsSystem_Instances_Instance(final IndicatorsSystem indicatorsSystem, final IndicatorInstance indicatorsInstance, final String baseURL) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(baseURL);
        _createUrlIndicatorSystems_IndicatorsSystem_Instances_Instance(indicatorsSystem, indicatorsInstance, uriComponentsBuilder);
        return uriComponentsBuilder.build().encode().toUriString();
    }

    private String _createUrlIndicatorSystems_IndicatorsSystem_Instances_Instance_Data(final IndicatorsSystem indicatorsSystem, final IndicatorInstance indicatorsInstance, final String baseURL) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(baseURL);
        _createUrlIndicatorSystems_IndicatorsSystem_Instances_Instance_Data(indicatorsSystem, indicatorsInstance, uriComponentsBuilder);
        return uriComponentsBuilder.build().encode().toUriString();
    }

    private String _createUrlIndicators_Indicator(final Indicator indicator, final String baseURL) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(baseURL);
        _createUrlIndicators_Indicator(indicator, uriComponentsBuilder);
        return uriComponentsBuilder.build().encode().toUriString();
    }

    private String _createUrlIndicators_Indicator_Data(final Indicator indicator, final String baseURL) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(baseURL);
        _createUrlIndicators_Indicator_Data(indicator, uriComponentsBuilder);
        return uriComponentsBuilder.build().encode().toUriString();
    }

    private void _createUrlIndicatorsSystems(UriComponentsBuilder uriComponentsBuilder) {
        uriComponentsBuilder.path(RestConstants.API_INDICATORS_BASE);
        uriComponentsBuilder.path(RestConstants.API_SLASH);
        uriComponentsBuilder.path(RestConstants.API_INDICATORS_INDICATORS_SYSTEMS);
    }

    private void _createUrlIndicatorsSystems_IndicatorsSystem(final IndicatorsSystem indicatorsSystem, final UriComponentsBuilder uriComponentsBuilder) {
        _createUrlIndicatorsSystems(uriComponentsBuilder);

        uriComponentsBuilder.path(RestConstants.API_SLASH);
        uriComponentsBuilder.path(indicatorsSystem.getCode());
    }

    private void _createUrlIndicatorsSystems_IndicatorsSystem_Instances(final IndicatorsSystem indicatorsSystem, final UriComponentsBuilder uriComponentsBuilder) {
        _createUrlIndicatorsSystems_IndicatorsSystem(indicatorsSystem, uriComponentsBuilder);

        uriComponentsBuilder.path(RestConstants.API_SLASH);
        uriComponentsBuilder.path(RestConstants.API_INDICATORS_INDICATORS_INSTANCES);
    }

    private void _createUrlIndicatorSystems_IndicatorsSystem_Instances_Instance(final IndicatorsSystem indicatorsSystem, final IndicatorInstance indicatorsInstance,
            final UriComponentsBuilder uriComponentsBuilder) {
        _createUrlIndicatorsSystems_IndicatorsSystem_Instances(indicatorsSystem, uriComponentsBuilder);

        uriComponentsBuilder.path(RestConstants.API_SLASH);
        uriComponentsBuilder.path(indicatorsInstance.getCode());
    }

    private void _createUrlIndicatorSystems_IndicatorsSystem_Instances_Instance_Data(final IndicatorsSystem indicatorsSystem, final IndicatorInstance indicatorsInstance,
            final UriComponentsBuilder uriComponentsBuilder) {
        _createUrlIndicatorSystems_IndicatorsSystem_Instances_Instance(indicatorsSystem, indicatorsInstance, uriComponentsBuilder);

        uriComponentsBuilder.path(RestConstants.API_SLASH);
        uriComponentsBuilder.path(RestConstants.API_INDICATORS_INDICATORS_INSTANCES_DATA);
    }

    private void _createUrlIndicators(UriComponentsBuilder uriComponentsBuilder) {
        uriComponentsBuilder.path(RestConstants.API_INDICATORS_BASE);
        uriComponentsBuilder.path(RestConstants.API_SLASH);
        uriComponentsBuilder.path(RestConstants.API_INDICATORS_INDICATORS);
    }

    private void _createUrlIndicators_Indicator(final Indicator indicator, final UriComponentsBuilder uriComponentsBuilder) {
        _createUrlIndicators(uriComponentsBuilder);

        uriComponentsBuilder.path(RestConstants.API_SLASH);
        uriComponentsBuilder.path(indicator.getCode());
    }

    private void _createUrlIndicators_Indicator_Data(final Indicator indicator, final UriComponentsBuilder uriComponentsBuilder) {
        _createUrlIndicators_Indicator(indicator, uriComponentsBuilder);

        uriComponentsBuilder.path(RestConstants.API_SLASH);
        uriComponentsBuilder.path(RestConstants.API_INDICATORS_INDICATORS_DATA);
    }

    private String _createUrlSubject(final SubjectIndicatorResult subject, final String baseURL) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(baseURL);
        uriComponentsBuilder.path(RestConstants.API_INDICATORS_BASE);
        uriComponentsBuilder.path(RestConstants.API_SLASH);
        uriComponentsBuilder.path(RestConstants.API_INDICATORS_SUBJECTS);
        uriComponentsBuilder.path(RestConstants.API_SLASH);
        uriComponentsBuilder.path(subject.getId());
        return uriComponentsBuilder.build().encode().toUriString();
    }

}
