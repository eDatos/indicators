package es.gobcan.istac.indicators.rest.mapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical.operations.internal.ws.v1_0.MetamacExceptionFault;
import org.siemac.metamac.statistical.operations.internal.ws.v1_0.MetamacStatisticalOperationsInternalInterfaceV10;
import org.siemac.metamac.statistical.operations.internal.ws.v1_0.domain.OperationBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponentsBuilder;

import es.gobcan.istac.indicators.core.domain.ElementLevel;
import es.gobcan.istac.indicators.core.domain.GeographicalGranularity;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.Indicator;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystem;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.domain.Quantity;
import es.gobcan.istac.indicators.core.enume.domain.MeasureDimensionTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.TimeGranularityEnum;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsDataService;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsService;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsSystemsService;
import es.gobcan.istac.indicators.rest.RestConstants;
import es.gobcan.istac.indicators.rest.exception.RestRuntimeException;
import es.gobcan.istac.indicators.rest.types.ElementLevelType;
import es.gobcan.istac.indicators.rest.types.GeographicalGranularityType;
import es.gobcan.istac.indicators.rest.types.GeographicalValueType;
import es.gobcan.istac.indicators.rest.types.IndicatorBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorInstanceBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorInstanceType;
import es.gobcan.istac.indicators.rest.types.IndicatorType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemType;
import es.gobcan.istac.indicators.rest.types.LinkType;
import es.gobcan.istac.indicators.rest.types.MeasureValueType;
import es.gobcan.istac.indicators.rest.types.QuantityType;
import es.gobcan.istac.indicators.rest.types.TimeGranularityType;
import es.gobcan.istac.indicators.rest.types.TimeValueType;

@Component
public class Do2TypeMapperImpl implements Do2TypeMapper {

    @Autowired
    private IndicatorsSystemsService                         indicatorsSystemsService = null;

    @Autowired
    private IndicatorsService                                indicatorsService        = null;

    @Autowired
    private IndicatorsDataService                            indicatorsDataService    = null;

    @Autowired
    private MetamacStatisticalOperationsInternalInterfaceV10 statisticalOperations    = null;

    @Override
    public IndicatorsSystemBaseType indicatorsSystemDoToBaseType(IndicatorsSystemVersion source, final String baseURL) {
        Assert.notNull(source);
        Assert.notNull(baseURL);
        
        try {
            OperationBase sourceOperationBase = statisticalOperations.retrieveOperation(source.getIndicatorsSystem().getCode());
            IndicatorsSystemBaseType target = _indicatorsSystemDoToBaseType(source, sourceOperationBase, baseURL);
            return target;
        } catch (MetamacExceptionFault e) {
            throw new RestRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }
    
    @Override
    public IndicatorsSystemType indicatorsSystemDoToType(IndicatorsSystemVersion source, final String baseURL) {
        Assert.notNull(source);
        Assert.notNull(baseURL);
        
        try {
            OperationBase sourceOperationBase = statisticalOperations.retrieveOperation(source.getIndicatorsSystem().getCode());
            
            IndicatorsSystemType target = _indicatorsSystemDoToType(source, sourceOperationBase, baseURL);
            return target;
        } catch (MetamacExceptionFault e) {
            throw new RestRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    
    @Override
    public List<IndicatorsSystemBaseType> indicatorsSystemDoToBaseType(List<IndicatorsSystemVersion> sources, final String baseURL) {
        Assert.notNull(sources);
        Assert.notNull(baseURL);

        try {
            List<IndicatorsSystemBaseType> targets = new ArrayList<IndicatorsSystemBaseType>(sources.size());
            for (IndicatorsSystemVersion source : sources) {
                OperationBase sourceOperationBase = statisticalOperations.retrieveOperation(source.getIndicatorsSystem().getCode()); // TODO Make in only one invocation
                IndicatorsSystemBaseType target = _indicatorsSystemDoToBaseType(source, sourceOperationBase, baseURL);
                targets.add(target);
            }
            return targets;
        } catch (MetamacExceptionFault e) {
            throw new RestRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
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

        List<IndicatorInstanceBaseType> targets = new ArrayList<IndicatorInstanceBaseType>(sources.size());
        for (IndicatorInstance source : sources) {
            IndicatorInstanceBaseType target = new IndicatorInstanceBaseType();
            _indicatorsInstanceDoToType(source, target, baseURL);
            targets.add(target);
        }
        return  targets;
    }

    @Override
    public IndicatorType indicatorDoToType(IndicatorVersion source, String baseURL) {
        Assert.notNull(source);
        try {
            IndicatorType target = new IndicatorType();
            _indicatorDoToType(source, target, baseURL);
            return target;
        } catch (Exception e) {
            throw new RestRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR, e);
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
            return  targets;
        } catch (Exception e) {
            throw new RestRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }
    
    private QuantityType quantityDoToBaseType(final Quantity source, final String baseURL) {
        Assert.notNull(source);
        
        QuantityType quantityType = new QuantityType();
        quantityType.setType(source.getQuantityType());
        if (source.getUnit() != null) {
            quantityType.setUnitSymbol(source.getUnit().getSymbol());
            quantityType.setUntiSymbolPosition(source.getUnit().getSymbolPosition());
        }
        quantityType.setUnitMultiplier(source.getUnitMultiplier());
        quantityType.setSignificantDigits(source.getSignificantDigits());
        quantityType.setDecimalPlaces(source.getDecimalPlaces());
        quantityType.setMin(source.getMinimum());
        quantityType.setMax(source.getMaximum());
        
        if (source.getDenominator() != null) {
            quantityType.setDenominatorLink(_createLinkType(source.getDenominator(), baseURL));
        }
        if (source.getNumerator() != null) {
            quantityType.setNumeratorLink(_createLinkType(source.getNumerator(), baseURL));
        }
        
        quantityType.setIsPercentage(source.getIsPercentage());
        quantityType.setPercentageOf(MapperUtil.getLocalisedLabel(source.getPercentageOf()));
        quantityType.setBaseValue(source.getBaseValue());
        quantityType.setBaseTime(source.getBaseTime());
        if (source.getBaseLocation() != null) {
            quantityType.setBaseLocation(source.getBaseLocation().getCode());
        }
        if (source.getBaseQuantity() != null) {
            quantityType.setBaseQuantityLink(_createLinkType(source.getBaseQuantity(), baseURL));
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
        
        List<IndicatorsSystemVersion> indicatorsSystemVersions = indicatorsSystemsService.retrieveIndicatorsSystemPublishedForIndicator(RestConstants.SERVICE_CONTEXT, source.getIndicator().getUuid());
        if (indicatorsSystemVersions.size() != 0) {
            List<LinkType> surveyLinks = new ArrayList<LinkType>();           
            for (IndicatorsSystemVersion indicatorsSystemVersion : indicatorsSystemVersions) {
                String href  = _createUrlIndicatorsSystems_IndicatorsSystem(indicatorsSystemVersion.getIndicatorsSystem(), baseURL);
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
    
    private void _indicatorDoToType(IndicatorVersion source, IndicatorType target, String baseURL) throws Exception {
        _indicatorDoToType(source, (IndicatorBaseType) target, baseURL);

        List<GeographicalGranularity> geographicalGranularities = indicatorsDataService.retrieveGeographicalGranularitiesInIndicator(RestConstants.SERVICE_CONTEXT, source.getIndicator().getUuid(), source.getVersionNumber());
        target.setGeographicalGranularities(_geographicalGranulrityDoToType(geographicalGranularities));


        List<TimeGranularityEnum> timeGranularities = indicatorsDataService.retrieveTimeGranularitiesInIndicator(RestConstants.SERVICE_CONTEXT, source.getIndicator().getUuid(), source.getVersionNumber());
        target.setTimeGranularities(_timeGranularitiesDoToType(timeGranularities));

        List<GeographicalValue> geographicalValues = indicatorsDataService.retrieveGeographicalValuesInIndicator(RestConstants.SERVICE_CONTEXT, source.getIndicator().getUuid(), source.getVersionNumber());
        target.setGeographicalValues(_geographicalValueDoToType(geographicalValues));


        List<String> timeValues = indicatorsDataService.retrieveTimeValuesInIndicator(RestConstants.SERVICE_CONTEXT, source.getIndicator().getUuid(), source.getVersionNumber());
        target.setTimeValues(_timeValueDoToType(timeValues));

        List<MeasureDimensionTypeEnum> measureValues = indicatorsDataService.retrieveMeasureValuesInIndicator(RestConstants.SERVICE_CONTEXT, source.getIndicator().getUuid(), source.getVersionNumber());
        target.setMeasureValues(_measureValueDoToType(measureValues));

        target.setChildLink(_createLinkTypeIndicatorData(source.getIndicator(), baseURL));
    }
    
    private void _indicatorsInstanceDoToType(final IndicatorInstance source, final IndicatorInstanceBaseType target, final String baseURL) {
        Assert.notNull(source);
        Assert.notNull(baseURL);

        IndicatorsSystem indicatorsSystem = source.getElementLevel().getIndicatorsSystemVersion().getIndicatorsSystem();
        String parentLinkURL = _createUrlIndicatorsSystems_IndicatorsSystem(indicatorsSystem, baseURL);
        String selfLinkURL = _createUrlIndicatorSystems_IndicatorsSystem_Instances_Instance(indicatorsSystem, source, baseURL);

        target.setId(source.getUuid());
        target.setKind(RestConstants.KIND_INDICATOR_INSTANCE);
        target.setSelfLink(selfLinkURL);
        
        LinkType parentLink = new LinkType();
        parentLink.setKind(RestConstants.KIND_INDICATOR_SYSTEM);
        parentLink.setHref(parentLinkURL);
        target.setParentLink(parentLink);

        target.setTitle(MapperUtil.getLocalisedLabel(source.getTitle()));
    }
    
    private void _indicatorsInstanceDoToType(final IndicatorInstance source, final IndicatorInstanceType target, final String baseURL) {
        try {
            _indicatorsInstanceDoToType(source, (IndicatorInstanceBaseType)target, baseURL);
    
            IndicatorsSystem indicatorsSystem = source.getElementLevel().getIndicatorsSystemVersion().getIndicatorsSystem();

            // TODO AÑADIR LAS GRANULADIDADES
            //List<GeographicalGranularity> geographicalGranularities = indicatorsDataService.retrievegeoretrieveGeographicalGranularitiesInIndicatorInstance(RestConstants.SERVICE_CONTEXT, source.getUuid());
            //target.setGeographicalGranularities(_geographicalGranulrityDoToType(geographicalGranularities));
    
    
            //List<TimeGranularityEnum> timeGranularities = indicatorsDataService.retrieveTimeGranularitiesInIndicator(RestConstants.SERVICE_CONTEXT, source.getUuid(), source.getVersionNumber());
            //target.setTimeGranularities(_timeGranularitiesDoToType(timeGranularities));
    
            List<GeographicalValue> geographicalValues = indicatorsDataService.retrieveGeographicalValuesInIndicatorInstance(RestConstants.SERVICE_CONTEXT, source.getUuid());
            target.setGeographicalValues(_geographicalValueDoToType(geographicalValues));
    
    
            List<String> timeValues = indicatorsDataService.retrieveTimeValuesInIndicatorInstance(RestConstants.SERVICE_CONTEXT, source.getUuid());
            target.setTimeValues(_timeValueDoToType(timeValues));
    
            List<MeasureDimensionTypeEnum> measureValues = indicatorsDataService.retrieveMeasureValuesInIndicatorInstance(RestConstants.SERVICE_CONTEXT, source.getUuid());
            target.setMeasureValues(_measureValueDoToType(measureValues));
            
            IndicatorVersion indicatorVersion = indicatorsService.retrieveIndicatorPublished(RestConstants.SERVICE_CONTEXT, source.getIndicator().getUuid());
            target.setDecimalPlaces(indicatorVersion.getQuantity().getDecimalPlaces());

            String childLinkURL = _createUrlIndicatorSystems_IndicatorsSystem_Instances_Instance_Data(indicatorsSystem, source, baseURL);
            LinkType childLink = new LinkType();
            childLink.setKind(RestConstants.KIND_INDICATOR_INSTANCE_DATA);
            childLink.setHref(childLinkURL);
            target.setChildLink(childLink);
        } catch (MetamacException e) {
            throw new RestRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }
    
    private List<GeographicalGranularityType> _geographicalGranulrityDoToType(List<GeographicalGranularity> geographicalGranularities) {
        if (CollectionUtils.isEmpty(geographicalGranularities)) {
            return null;
        }
        List<GeographicalGranularityType> geographicalGranularityTypes = new ArrayList<GeographicalGranularityType>(geographicalGranularities.size());
        for (GeographicalGranularity geographicalGranularity : geographicalGranularities) {
            GeographicalGranularityType geographicalGranularityType = new GeographicalGranularityType();
            geographicalGranularityType.setCode(geographicalGranularity.getCode());
            geographicalGranularityType.setTitle(MapperUtil.getLocalisedLabel(geographicalGranularity.getTitle()));
            geographicalGranularityTypes.add(geographicalGranularityType);
        }
        return geographicalGranularityTypes;
    }
    
    private List<TimeGranularityType> _timeGranularitiesDoToType(List<TimeGranularityEnum> timeGranularities) {
        if (CollectionUtils.isEmpty(timeGranularities)) {
            return null;
        }
        List<TimeGranularityType> timeGranularityTypes = new ArrayList<TimeGranularityType>(timeGranularities.size());
        for (TimeGranularityEnum timeGranularity : timeGranularities) {
            TimeGranularityType timeGranularityType = new TimeGranularityType();
            timeGranularityType.setCode(timeGranularity.name());
            // timeGranularityType.setTitle(MapperUtil.getLocalisedLabel(timeGranularity.getTitle()));
            timeGranularityTypes.add(timeGranularityType);
        }
        return timeGranularityTypes;
    }
    
    private List<GeographicalValueType> _geographicalValueDoToType(List<GeographicalValue> geographicalValues) {
        if (CollectionUtils.isEmpty(geographicalValues)) {
            return null;
        }
        List<GeographicalValueType> geographicalValueTypes = new ArrayList<GeographicalValueType>(geographicalValues.size());
        for (GeographicalValue geographicalValue : geographicalValues) {
            GeographicalValueType geographicalValueType = new GeographicalValueType();
            geographicalValueType.setCode(geographicalValue.getCode());
            geographicalValueType.setLatitude(geographicalValue.getLatitude());
            geographicalValueType.setLongitude(geographicalValue.getLongitude());
            geographicalValueType.setTitle(MapperUtil.getLocalisedLabel(geographicalValue.getTitle()));
            geographicalValueTypes.add(geographicalValueType);
        }
        return geographicalValueTypes;
    }
    
    private List<TimeValueType> _timeValueDoToType(List<String> timeValues) {
        if (CollectionUtils.isEmpty(timeValues)) {
            return null;
        }
        List<TimeValueType> timeValueTypes = new ArrayList<TimeValueType>(timeValues.size());
        for (String timeValue : timeValues) {
            TimeValueType timeValueType = new TimeValueType();
            timeValueType.setCode(timeValue);
            // timeValueType.setTitle(MapperUtil.getLocalisedLabel(timeValue.getTitle()));
            timeValueTypes.add(timeValueType);
        }
        return timeValueTypes;
    }
    
    private List<MeasureValueType> _measureValueDoToType(List<MeasureDimensionTypeEnum> measureValues) {
        if (CollectionUtils.isEmpty(measureValues)) {
            return null;
        }
        List<MeasureValueType> measureValueTypes = new ArrayList<MeasureValueType>(measureValues.size());
        for (MeasureDimensionTypeEnum measureValue : measureValues) {
            MeasureValueType measureValueType = new MeasureValueType();
            measureValueType.setCode(measureValue.name());
            // measureValueType.setTitle(MapperUtil.getLocalisedLabel(measureValue.getTitle()));
            measureValueTypes.add(measureValueType);
        }
        return measureValueTypes;
    }
    
    private LinkType _createLinkType(final Indicator indicator, final String baseURL) {
        String href = _createUrlIndicators_Indicator(indicator, baseURL);
        LinkType link = new LinkType();
        link.setKind(RestConstants.KIND_INDICATOR);
        link.setHref(href);
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
        } else{
            target.setId(source.getIndicatorInstance().getUuid());
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

    
    private IndicatorsSystemBaseType _indicatorsSystemDoToBaseType(IndicatorsSystemVersion sourceIndicatorsSystem, OperationBase sourceOperationBase, final String baseURL) {
        IndicatorsSystemBaseType target = new IndicatorsSystemBaseType();
        _indicatorsSystemDoToType(sourceIndicatorsSystem, target, baseURL);
        _operationBaseDoToType(sourceOperationBase, target, baseURL);
        return target;
    }
    
    private IndicatorsSystemType _indicatorsSystemDoToType(IndicatorsSystemVersion sourceIndicatorsSystem, OperationBase sourceOperationBase, final String baseURL) {
        IndicatorsSystemType target = new IndicatorsSystemType();
        _indicatorsSystemDoToType(sourceIndicatorsSystem, target, baseURL);
        _operationBaseDoToType(sourceOperationBase, target, baseURL);
        _elementsLevelsDoToType(sourceIndicatorsSystem.getChildrenFirstLevel(), target, baseURL);
        return target;
    }
    
    
    
    private void _indicatorsSystemDoToType(IndicatorsSystemVersion source, IndicatorsSystemBaseType target, final String baseURL) {
        String selfLinkURL = _createUrlIndicatorsSystems_IndicatorsSystem(source.getIndicatorsSystem(), baseURL);

        target.setKind(RestConstants.KIND_INDICATOR_SYSTEM);
        target.setSelfLink(selfLinkURL);
        // indicatorsSystemBase.setSelfLink(sourceIndicatorsSystem.getUri());
        target.setVersion(source.getVersionNumber());
        target.setPublicationDate(source.getPublicationDate());

        LinkType statisticalOperatioLink = new LinkType();
        statisticalOperatioLink.setKind(RestConstants.KIND_STATISTICAL_OPERATION);
        // statisticalOperatioLink.setHref(sourceOperationBase.getUri()); // TODO Put when Operation Rest API is available
        target.setStatisticalOperationLink(statisticalOperatioLink);
    }
    
    private void _indicatorsSystemDoToType(IndicatorsSystemVersion source, IndicatorsSystemType target, final String baseURL) {
        _indicatorsSystemDoToType(source, (IndicatorsSystemBaseType)target, baseURL);

        String childLinkURL = _createUrlIndicatorsSystems_IndicatorsSystem_Instances(source.getIndicatorsSystem(), baseURL);
        
        LinkType childLink = new LinkType();
        childLink.setKind(RestConstants.KIND_INDICATOR_INSTANCES);
        childLink.setHref(childLinkURL);
        
        target.setChildLink(childLink);
    }
    
    private void _operationBaseDoToType(OperationBase sourceOperationBase, IndicatorsSystemBaseType target, final String baseURL) {
        target.setId(sourceOperationBase.getCode());
        target.setCode(sourceOperationBase.getCode());
        target.setTitle(MapperUtil.getLocalisedLabel(sourceOperationBase.getTitle()));
        target.setAcronym(MapperUtil.getLocalisedLabel(sourceOperationBase.getAcronym()));
        target.setDescription(MapperUtil.getLocalisedLabel(sourceOperationBase.getDescription()));
        target.setObjective(MapperUtil.getLocalisedLabel(sourceOperationBase.getObjective()));
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
    
    private void _createUrlIndicatorSystems_IndicatorsSystem_Instances_Instance(final IndicatorsSystem indicatorsSystem, final IndicatorInstance indicatorsInstance, final UriComponentsBuilder uriComponentsBuilder) {
        _createUrlIndicatorsSystems_IndicatorsSystem_Instances(indicatorsSystem, uriComponentsBuilder);
        
        uriComponentsBuilder.path(RestConstants.API_SLASH);
        uriComponentsBuilder.path(indicatorsInstance.getUuid());
    }
    
    private void _createUrlIndicatorSystems_IndicatorsSystem_Instances_Instance_Data(final IndicatorsSystem indicatorsSystem, final IndicatorInstance indicatorsInstance, final UriComponentsBuilder uriComponentsBuilder) {
        _createUrlIndicatorSystems_IndicatorsSystem_Instances_Instance(indicatorsSystem, indicatorsInstance, uriComponentsBuilder);
        
        uriComponentsBuilder.path(RestConstants.API_SLASH);
        uriComponentsBuilder.path(RestConstants.API_INDICATORS_INDICATORS_INSTANCES_DATA);   
    }
    
    private void _createUrlIndicators(UriComponentsBuilder uriComponentsBuilder) {
        uriComponentsBuilder.path(RestConstants.API_INDICATORS_BASE);
        uriComponentsBuilder.path(RestConstants.API_SLASH);
        uriComponentsBuilder.path(RestConstants.API_INDICATORS_RESOURCES);
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


}
