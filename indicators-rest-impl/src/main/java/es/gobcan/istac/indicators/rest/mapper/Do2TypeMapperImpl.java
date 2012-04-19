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
    public IndicatorsSystemBaseType indicatorsSystemDoToBaseType(IndicatorsSystemVersion source, UriComponentsBuilder uriComponentsBuilder) {
        Assert.notNull(source);
        Assert.notNull(uriComponentsBuilder);
        
        try {
            OperationBase sourceOperationBase = statisticalOperations.retrieveOperation(source.getIndicatorsSystem().getCode());
            IndicatorsSystemBaseType target = _indicatorsSystemDoToBaseType(source, sourceOperationBase, uriComponentsBuilder);
            return target;
        } catch (MetamacExceptionFault e) {
            throw new RestRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }
    
    @Override
    public IndicatorsSystemType indicatorsSystemDoToType(IndicatorsSystemVersion source, UriComponentsBuilder uriComponentsBuilder) {
        Assert.notNull(source);
        Assert.notNull(uriComponentsBuilder);
        
        try {
            OperationBase sourceOperationBase = statisticalOperations.retrieveOperation(source.getIndicatorsSystem().getCode());
            
            IndicatorsSystemType target = _indicatorsSystemDoToType(source, sourceOperationBase, uriComponentsBuilder);
            return target;
        } catch (MetamacExceptionFault e) {
            throw new RestRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override
    public List<IndicatorsSystemBaseType> indicatorsSystemDoToBaseType(List<IndicatorsSystemVersion> sources, UriComponentsBuilder uriComponentsBuilder) {
        Assert.notNull(sources);
        Assert.notNull(uriComponentsBuilder);

        try {
            List<IndicatorsSystemBaseType> targets = new ArrayList<IndicatorsSystemBaseType>(sources.size());
            for (IndicatorsSystemVersion source : sources) {
                OperationBase sourceOperationBase = statisticalOperations.retrieveOperation(source.getIndicatorsSystem().getCode()); // TODO Make in only one invocation
                IndicatorsSystemBaseType target = _indicatorsSystemDoToBaseType(source, sourceOperationBase, uriComponentsBuilder);
                targets.add(target);
            }
            return targets;
        } catch (MetamacExceptionFault e) {
            throw new RestRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override
    public IndicatorInstanceType indicatorsInstanceDoToType(IndicatorInstance source, UriComponentsBuilder uriComponentsBuilder) {
        Assert.notNull(source);
        Assert.notNull(uriComponentsBuilder);
        try {
            IndicatorsSystem indicatorsSystem = source.getElementLevel().getIndicatorsSystemVersion().getIndicatorsSystem();
            String parentLinkURL = _createUrl(indicatorsSystem, uriComponentsBuilder); 
            String selfLinkURL  = _createUrl(indicatorsSystem, source, uriComponentsBuilder);

            IndicatorInstanceType target = new IndicatorInstanceType();
            target.setId(source.getUuid());
            target.setKind(RestConstants.KIND_INDICATOR_INSTANCE);
            target.setSelfLink(selfLinkURL);
            LinkType parentLink = new LinkType();
            parentLink.setKind(RestConstants.KIND_INDICATOR_SYSTEM);
            parentLink.setHref(parentLinkURL);
            target.setParentLink(parentLink);

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
    public List<IndicatorInstanceType> indicatorsInstanceDoToType(List<IndicatorInstance> sources, UriComponentsBuilder uriComponentsBuilder) {
        Assert.notNull(sources);

        List<IndicatorInstanceType> targets = new ArrayList<IndicatorInstanceType>(sources.size());
        for (IndicatorInstance source : sources) {
            IndicatorInstanceType target = indicatorsInstanceDoToType(source, uriComponentsBuilder);
            targets.add(target);
        }
        return  targets;
    }
    
    private void _elementsLevelsDoToType(List<ElementLevel> sources, IndicatorsSystemType target, UriComponentsBuilder uriComponentsBuilder) {
        if (CollectionUtils.isEmpty(sources)) {
            return;
        }

        List<ElementLevelType> targetElements = _elementsLevelsDoToType(sources, uriComponentsBuilder);
        target.setElements(targetElements);
    }
    
    private List<ElementLevelType> _elementsLevelsDoToType(List<ElementLevel> sources, UriComponentsBuilder uriComponentsBuilder) {
        if (CollectionUtils.isEmpty(sources)) {
            return null;
        }

        List<ElementLevelType> targetElements = new ArrayList<ElementLevelType>();
        for (ElementLevel source : sources) {
            ElementLevelType targetElement = _elementsLevelsDoToType(source, uriComponentsBuilder);
            targetElements.add(targetElement);
        }
        return targetElements;
    }
    
    private ElementLevelType _elementsLevelsDoToType(ElementLevel source, UriComponentsBuilder uriComponentsBuilder) {
        ElementLevelType target = new ElementLevelType();
        target.setId(source.getUuid());
        
        if (source.getDimension() != null) {
            target.setKind(RestConstants.KIND_INDICATOR_DIMENSION);
            target.setTitle(MapperUtil.getLocalisedLabel(source.getDimension().getTitle()));
        } else{
            target.setKind(RestConstants.KIND_INDICATOR_INSTANCE);
            target.setTitle(MapperUtil.getLocalisedLabel(source.getIndicatorInstance().getTitle()));
            
            IndicatorsSystem indicatorsSystem = source.getIndicatorsSystemVersion().getIndicatorsSystem();
            String selfLinkURL = _createUrl(indicatorsSystem, source.getIndicatorInstance(), uriComponentsBuilder);
            target.setSelfLink(selfLinkURL);
        }
        
        List<ElementLevelType> targetSubElements = _elementsLevelsDoToType(source.getChildren(), uriComponentsBuilder);
        target.setElements(targetSubElements);
        
        return target;
    }

    
    private IndicatorsSystemBaseType _indicatorsSystemDoToBaseType(IndicatorsSystemVersion sourceIndicatorsSystem, OperationBase sourceOperationBase, UriComponentsBuilder uriComponentsBuilder) {
        IndicatorsSystemBaseType target = new IndicatorsSystemBaseType();
        _indicatorsSystemDoToType(sourceIndicatorsSystem, target, uriComponentsBuilder);
        _operationBaseDoToType(sourceOperationBase, target, uriComponentsBuilder);
        return target;
    }
    
    private IndicatorsSystemType _indicatorsSystemDoToType(IndicatorsSystemVersion sourceIndicatorsSystem, OperationBase sourceOperationBase, UriComponentsBuilder uriComponentsBuilder) {
        IndicatorsSystemType target = new IndicatorsSystemType();
        _indicatorsSystemDoToType(sourceIndicatorsSystem, target, uriComponentsBuilder);
        _operationBaseDoToType(sourceOperationBase, target, uriComponentsBuilder);
        _elementsLevelsDoToType(sourceIndicatorsSystem.getChildrenFirstLevel(), target, uriComponentsBuilder);
        return target;
    }
    
    
    
    private void _indicatorsSystemDoToType(IndicatorsSystemVersion source, IndicatorsSystemBaseType target, UriComponentsBuilder uriComponentsBuilder) {
        String selfLinkURL = _createUrl(source.getIndicatorsSystem(), uriComponentsBuilder);

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
    
    private void _operationBaseDoToType(OperationBase sourceOperationBase, IndicatorsSystemBaseType target, UriComponentsBuilder uriComponentsBuilder) {
        target.setId(sourceOperationBase.getCode());
        target.setCode(sourceOperationBase.getCode());
        target.setTitle(MapperUtil.getLocalisedLabel(sourceOperationBase.getTitle()));
        target.setAcronym(MapperUtil.getLocalisedLabel(sourceOperationBase.getAcronym()));
        target.setDescription(MapperUtil.getLocalisedLabel(sourceOperationBase.getDescription()));
        target.setObjective(MapperUtil.getLocalisedLabel(sourceOperationBase.getObjective()));
    }
    
    
    private String _createUrl(IndicatorsSystem indicatorsSystem, UriComponentsBuilder uriComponentsBuilder) {
        uriComponentsBuilder.replacePath(RestConstants.API_INDICATORS_BASE);
        uriComponentsBuilder.path(RestConstants.API_INDICATORS_INDICATORS_SYSTEMS);
        uriComponentsBuilder.path(RestConstants.API_SLASH);
        uriComponentsBuilder.path(indicatorsSystem.getCode());
        return uriComponentsBuilder.build().encode().toUriString();
    }
    
    private String _createUrl(IndicatorsSystem indicatorsSystem, IndicatorInstance indicatorsInstance, UriComponentsBuilder uriComponentsBuilder) {
        uriComponentsBuilder.replacePath(RestConstants.API_INDICATORS_BASE);
        uriComponentsBuilder.path(RestConstants.API_INDICATORS_INDICATORS_SYSTEMS);
        uriComponentsBuilder.path(RestConstants.API_SLASH);
        uriComponentsBuilder.path(indicatorsSystem.getCode());
        uriComponentsBuilder.path(RestConstants.API_INDICATORS_INDICATORS_INSTANCES);
        uriComponentsBuilder.path(RestConstants.API_SLASH);
        uriComponentsBuilder.path(indicatorsInstance.getUuid());
        return uriComponentsBuilder.build().encode().toUriString();
    }

}
