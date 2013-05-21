package es.gobcan.istac.indicators.rest.facadeimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.search.criteria.SculptorCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arte.statistic.dataset.repository.dto.ConditionDimensionDto;
import com.arte.statistic.dataset.repository.dto.ObservationExtendedDto;

import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorInstanceProperties;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemHistory;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.domain.MeasureValue;
import es.gobcan.istac.indicators.core.domain.TimeValue;
import es.gobcan.istac.indicators.rest.RestConstants;
import es.gobcan.istac.indicators.rest.facadeapi.IndicatorSystemRestFacade;
import es.gobcan.istac.indicators.rest.mapper.DataTypeRequest;
import es.gobcan.istac.indicators.rest.mapper.Do2TypeMapper;
import es.gobcan.istac.indicators.rest.mapper.IndicatorInstancesRest2DoMapper;
import es.gobcan.istac.indicators.rest.serviceapi.IndicatorsApiService;
import es.gobcan.istac.indicators.rest.types.DataType;
import es.gobcan.istac.indicators.rest.types.IndicatorInstanceBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorInstanceType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemHistoryType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemType;
import es.gobcan.istac.indicators.rest.types.MetadataType;
import es.gobcan.istac.indicators.rest.types.PagedResultType;
import es.gobcan.istac.indicators.rest.types.RestCriteriaPaginator;
import es.gobcan.istac.indicators.rest.util.ConditionUtil;
import es.gobcan.istac.indicators.rest.util.CriteriaUtil;
import es.gobcan.istac.indicators.rest.util.RequestUtil;

@Service
public class IndicatorSystemRestFacadeImpl implements IndicatorSystemRestFacade {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected IndicatorsApiService indicatorsApiService = null;

    @Autowired
    private Do2TypeMapper dto2TypeMapper = null;

    @Autowired
    private IndicatorInstancesRest2DoMapper indicatorInstancesRest2DoMapper;

    @Override
    public PagedResultType<IndicatorsSystemBaseType> findIndicatorsSystems(final String baseURL, final RestCriteriaPaginator paginator) throws Exception {
        PagingParameter pagingParameter = CriteriaUtil.createPagingParameter(paginator);
        PagedResult<IndicatorsSystemVersion> indicatorsSystemVersions = findIndicatorsSystems(pagingParameter);

        List<IndicatorsSystemBaseType> result = dto2TypeMapper.indicatorsSystemDoToBaseType(indicatorsSystemVersions.getValues(), baseURL);

        PagedResultType<IndicatorsSystemBaseType> resultType = new PagedResultType<IndicatorsSystemBaseType>();
        resultType.setKind(RestConstants.KIND_INDICATOR_SYSTEMS);
        resultType.setLimit(paginator.getLimit());
        resultType.setOffset(paginator.getOffset());
        resultType.setTotal(indicatorsSystemVersions.getTotalRows());
        resultType.setItems(result);

        return resultType;
    }

    protected PagedResult<IndicatorsSystemVersion> findIndicatorsSystems(PagingParameter pagingParameter) throws MetamacException {
        return indicatorsApiService.findIndicatorsSystems(pagingParameter);
    }

    protected IndicatorsSystemVersion retrieveIndicatorsSystemByCode(String idIndicatorSystem) throws MetamacException {
        return indicatorsApiService.retrieveIndicatorsSystemByCode(idIndicatorSystem);
    }

    protected PagedResult<IndicatorInstance> findIndicatorsInstancesInIndicatorsSystems(SculptorCriteria sculptorCriteria) throws MetamacException {
        return indicatorsApiService.findIndicatorsInstancesInIndicatorsSystems(sculptorCriteria);
    }

    protected IndicatorInstance retrieveIndicatorInstanceByCode(String idIndicatorSystem, String idIndicatorInstance) throws MetamacException {
        return indicatorsApiService.retrieveIndicatorInstanceByCode(idIndicatorSystem, idIndicatorInstance);
    }

    @Override
    public IndicatorsSystemType retrieveIndicatorsSystem(final String baseURL, String idIndicatorSystem) throws Exception {
        IndicatorsSystemVersion indicatorsSystemVersion = retrieveIndicatorsSystemByCode(idIndicatorSystem);
        IndicatorsSystemType result = dto2TypeMapper.indicatorsSystemDoToType(indicatorsSystemVersion, baseURL);
        return result;
    }

    @Override
    public List<IndicatorsSystemHistoryType> findIndicatorsSystemHistoryByCode(final String baseURL, final String code, final int maxResults) throws Exception {
        IndicatorsSystemVersion indicatorsSystemVersion = retrieveIndicatorsSystemByCode(code);
        
        List<IndicatorsSystemHistory> systemHistories = indicatorsApiService.findIndicatorsSystemHistory(indicatorsSystemVersion.getIndicatorsSystem().getUuid(), maxResults);
        
        List<IndicatorsSystemHistoryType> systemHistoriesType = new ArrayList<IndicatorsSystemHistoryType>();
        for (IndicatorsSystemHistory systemHistory : systemHistories) {
            systemHistoriesType.add(dto2TypeMapper.indicatorsSystemHistoryDoToType(systemHistory, baseURL));
        }
        return systemHistoriesType;
    }

    @Override
    public PagedResultType<IndicatorInstanceBaseType> retrieveIndicatorsInstances(final String baseUrl, final String idIndicatorSystem, String q, String order, Integer limit, Integer offset, String fields, Map<String, List<String>> representation) throws Exception {

        // Parse Query
        SculptorCriteria sculptorCriteria = indicatorInstancesRest2DoMapper.queryParams2SculptorCriteria(q, order, limit, offset);
        ConditionalCriteria systemCondition = ConditionalCriteria.equal(IndicatorInstanceProperties.elementLevel().indicatorsSystemVersion().indicatorsSystem().code(), idIndicatorSystem);
        sculptorCriteria.getConditions().add(systemCondition);


        PagedResult<IndicatorInstance> instances = findIndicatorsInstancesInIndicatorsSystems(sculptorCriteria);


        // Mapping to type
        List<IndicatorInstanceBaseType> indicatorInstanceTypes = dto2TypeMapper.indicatorsInstanceDoToBaseType(instances.getValues(), baseUrl);
        PagedResultType<IndicatorInstanceBaseType> result = new PagedResultType<IndicatorInstanceBaseType>();
        result.setKind(RestConstants.KIND_INDICATOR_INSTANCES);
        result.setLimit(limit);
        result.setTotal(instances.getTotalRows());
        result.setOffset(instances.getStartRow());
        result.setItems(indicatorInstanceTypes);


        // Fields filter. Only support for +metadata, +data
        Set<String> fieldsToAdd = RequestUtil.parseFields(fields);

        if(fieldsToAdd.contains("+metadata")) {
            for(int i = 0; i < result.getItems().size(); i++) {
                IndicatorInstanceBaseType baseType = result.getItems(). get(i);
                IndicatorInstance indicatorInstance = instances.getValues().get(i);

                MetadataType metadataType = new MetadataType();
                dto2TypeMapper.indicatorsInstanceDoToMetadataType(indicatorInstance, metadataType, baseUrl);
                baseType.setMetadata(metadataType);
            }
        }

        if(fieldsToAdd.contains("+data")) {
            for(IndicatorInstanceBaseType type : result.getItems()) {
                Map<String, List<String>> selectedGranularities = MapUtils.EMPTY_MAP;
                DataType dataType = retrieveIndicatorInstanceDataByCode(baseUrl, type.getSystemCode(), type.getId(), representation, selectedGranularities);
                type.setData(dataType);
            }
        }

        return result;
    }



    @Override
    public IndicatorInstanceType retrieveIndicatorInstanceByCode(final String baseURL, final String idIndicatorSystem, final String idIndicatorInstance) throws Exception {
        IndicatorInstance indicatorInstance = retrieveIndicatorInstanceByCode(idIndicatorSystem, idIndicatorInstance);
        IndicatorInstanceType result = dto2TypeMapper.indicatorsInstanceDoToType(indicatorInstance, baseURL);
        return result;
    }

    @Override
    public DataType retrieveIndicatorInstanceDataByCode(String baseUrl, String idIndicatorSystem, String idIndicatorInstance, Map<String, List<String>> selectedRepresentations, Map<String, List<String>> selectedGranularities) throws Exception {
        IndicatorInstance indicatorInstance = retrieveIndicatorInstanceByCode(idIndicatorSystem, idIndicatorInstance);

        List<GeographicalValue> geographicalValues = indicatorsApiService.retrieveGeographicalValuesInIndicatorInstance(indicatorInstance.getUuid());
        List<TimeValue> timeValues = indicatorsApiService.retrieveTimeValuesInIndicatorInstance(indicatorInstance.getUuid());
        List<MeasureValue> measureValues = indicatorsApiService.retrieveMeasureValuesInIndicatorInstance(indicatorInstance.getUuid());

        List<ConditionDimensionDto> conditionDimensionDtos = new ArrayList<ConditionDimensionDto>();
        ConditionUtil.filterGeographicalValues(selectedRepresentations, selectedGranularities, geographicalValues, conditionDimensionDtos);
        ConditionUtil.filterTimeValues(selectedRepresentations, selectedGranularities, timeValues, conditionDimensionDtos);
        ConditionUtil.filterMeasureValues(selectedRepresentations, selectedGranularities, measureValues, conditionDimensionDtos);

        Map<String, ObservationExtendedDto> observationMap = indicatorsApiService.findObservationsExtendedByDimensionsInIndicatorInstance(indicatorInstance.getUuid(), conditionDimensionDtos);

        DataTypeRequest dataTypeRequest = new DataTypeRequest(indicatorInstance, geographicalValues, timeValues, measureValues, observationMap);
        return dto2TypeMapper.createDataType(dataTypeRequest);
    }


}
