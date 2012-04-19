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
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystem;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsService;
import es.gobcan.istac.indicators.rest.RestConstants;
import es.gobcan.istac.indicators.rest.exception.RestRuntimeException;
import es.gobcan.istac.indicators.rest.types.ElementLevelType;
import es.gobcan.istac.indicators.rest.types.IndicatorInstanceType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemType;
import es.gobcan.istac.indicators.rest.types.LinkType;

@Component
public class Do2TypeMapperImpl implements Do2TypeMapper {

    @Autowired
    private IndicatorsService                                indicatorsService  = null;

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
        try {
            IndicatorsSystem indicatorsSystem = source.getElementLevel().getIndicatorsSystemVersion().getIndicatorsSystem();
            String parentLinkURL = _createUrlIndicatorsSystems_IndicatorsSystem(indicatorsSystem, baseURL);
            String selfLinkURL = _createUrlIndicatorSystems_IndicatorsSystem_Instances_Instance(indicatorsSystem, source, baseURL);
            String childLinkURL = _createUrlIndicatorSystems_IndicatorsSystem_Instances_Instance_Data(indicatorsSystem, source, baseURL);

            IndicatorInstanceType target = new IndicatorInstanceType();
            target.setId(source.getUuid());
            target.setKind(RestConstants.KIND_INDICATOR_INSTANCE);
            target.setSelfLink(selfLinkURL);
            
            LinkType parentLink = new LinkType();
            parentLink.setKind(RestConstants.KIND_INDICATOR_SYSTEM);
            parentLink.setHref(parentLinkURL);
            target.setParentLink(parentLink);
            
            LinkType childLink = new LinkType();
            childLink.setKind(RestConstants.KIND_INDICATOR_INSTANCE_DATA);
            childLink.setHref(childLinkURL);
            target.setChildLink(childLink);
            

            target.setTimeGranularity(source.getTimeGranularity());
            target.setTimeValue(source.getTimeValue());
            target.setGeographicalGranularity(source.getGeographicalGranularity() != null ? source.getGeographicalGranularity().getCode() : null);
            target.setGeographicalValue(source.getGeographicalValue() != null ? source.getGeographicalValue().getCode() : null);

            IndicatorVersion indicatorVersion = indicatorsService.retrieveIndicatorPublished(RestConstants.SERVICE_CONTEXT, source.getIndicator().getUuid());
            target.setDecimalPlaces(indicatorVersion.getQuantity().getDecimalPlaces());
            target.setTitle(MapperUtil.getLocalisedLabel(source.getTitle()));

            return target;
        } catch (MetamacException e) {
            throw new RestRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override
    public List<IndicatorInstanceType> indicatorsInstanceDoToType(List<IndicatorInstance> sources, final String baseURL) {
        Assert.notNull(sources);

        List<IndicatorInstanceType> targets = new ArrayList<IndicatorInstanceType>(sources.size());
        for (IndicatorInstance source : sources) {
            IndicatorInstanceType target = indicatorsInstanceDoToType(source, baseURL);
            targets.add(target);
        }
        return  targets;
    }
    
    private void _elementsLevelsDoToType(List<ElementLevel> sources, IndicatorsSystemType target, final String baseURL) {
        if (CollectionUtils.isEmpty(sources)) {
            return;
        }

        List<ElementLevelType> targetElements = _elementsLevelsDoToType(sources, baseURL);
        target.setElements(targetElements);
    }
    
    private List<ElementLevelType> _elementsLevelsDoToType(List<ElementLevel> sources, final String baseURL) {
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

}
