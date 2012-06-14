package es.gobcan.istac.indicators.rest.facadeimpl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.arte.statistic.dataset.repository.dto.ConditionDimensionDto;
import com.arte.statistic.dataset.repository.dto.ObservationExtendedDto;

import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.domain.MeasureValue;
import es.gobcan.istac.indicators.core.domain.TimeValue;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsDataService;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsSystemsService;
import es.gobcan.istac.indicators.rest.RestConstants;
import es.gobcan.istac.indicators.rest.exception.RestRuntimeException;
import es.gobcan.istac.indicators.rest.facadeapi.IndicatorSystemRestFacade;
import es.gobcan.istac.indicators.rest.mapper.DataTypeRequest;
import es.gobcan.istac.indicators.rest.mapper.Do2TypeMapper;
import es.gobcan.istac.indicators.rest.types.DataType;
import es.gobcan.istac.indicators.rest.types.IndicatorInstanceBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorInstanceType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemType;
import es.gobcan.istac.indicators.rest.types.NoPagedResultType;
import es.gobcan.istac.indicators.rest.types.PagedResultType;
import es.gobcan.istac.indicators.rest.types.RestCriteriaPaginator;
import es.gobcan.istac.indicators.rest.util.ConditionUtil;
import es.gobcan.istac.indicators.rest.util.CriteriaUtil;

@Service("indicatorSystemRestFacade")
public class IndicatorSystemRestFacadeImpl implements IndicatorSystemRestFacade {

    private Logger                   logger                   = LoggerFactory.getLogger(getClass());

    @Autowired
    private IndicatorsSystemsService indicatorsSystemsService = null;

    @Autowired
    private IndicatorsDataService    indicatorsDataService    = null;

    @Autowired
    private Do2TypeMapper            dto2TypeMapper           = null;

    @Override
    public PagedResultType<IndicatorsSystemBaseType> findIndicatorsSystems(final String baseURL, final RestCriteriaPaginator paginator) throws Exception {
        PagingParameter pagingParameter = CriteriaUtil.createPagingParameter(paginator);
        PagedResult<IndicatorsSystemVersion> indicatorsSystemVersions = indicatorsSystemsService.findIndicatorsSystemsPublished(RestConstants.SERVICE_CONTEXT, null, pagingParameter);

        List<IndicatorsSystemBaseType> result = dto2TypeMapper.indicatorsSystemDoToBaseType(indicatorsSystemVersions.getValues(), baseURL);

        PagedResultType<IndicatorsSystemBaseType> resultType = new PagedResultType<IndicatorsSystemBaseType>();
        resultType.setKind(RestConstants.KIND_INDICATOR_SYSTEMS);
        resultType.setLimit(paginator.getLimit());
        resultType.setOffset(paginator.getOffset());
        resultType.setTotal(indicatorsSystemVersions.getTotalRows());
        resultType.setItems(result);
        return resultType;
    }

    @Override
    public IndicatorsSystemType retrieveIndicatorsSystem(final String baseURL, String idIndicatorSystem) throws Exception {
        IndicatorsSystemVersion indicatorsSystemVersion = indicatorsSystemsService.retrieveIndicatorsSystemPublishedByCode(RestConstants.SERVICE_CONTEXT, idIndicatorSystem);
        IndicatorsSystemType result = dto2TypeMapper.indicatorsSystemDoToType(indicatorsSystemVersion, baseURL);
        return result;
    }

    @Override
    public NoPagedResultType<IndicatorInstanceBaseType> retrieveIndicatorsInstances(final String baseURL, final String idIndicatorSystem) throws Exception {
        IndicatorsSystemVersion indicatorsSystemVersion = indicatorsSystemsService.retrieveIndicatorsSystemPublishedByCode(RestConstants.SERVICE_CONTEXT, idIndicatorSystem);
        List<IndicatorInstance> indicatorInstances = indicatorsSystemsService.retrieveIndicatorsInstancesByIndicatorsSystem(RestConstants.SERVICE_CONTEXT, indicatorsSystemVersion.getIndicatorsSystem().getUuid(), indicatorsSystemVersion.getVersionNumber());
        List<IndicatorInstanceBaseType> indicatorInstanceTypes = dto2TypeMapper.indicatorsInstanceDoToBaseType(indicatorInstances, baseURL);
        
        NoPagedResultType<IndicatorInstanceBaseType> result = new NoPagedResultType<IndicatorInstanceBaseType>();
        result.setKind(RestConstants.KIND_INDICATOR_INSTANCES);
        result.setItems(indicatorInstanceTypes);
        result.setTotal(indicatorInstanceTypes.size());
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
        
        List<GeographicalValue> geographicalValues = indicatorsDataService.retrieveGeographicalValuesInIndicatorInstance(RestConstants.SERVICE_CONTEXT, indicatorInstance.getUuid());
        List<TimeValue> timeValues = indicatorsDataService.retrieveTimeValuesInIndicatorInstance(RestConstants.SERVICE_CONTEXT, indicatorInstance.getUuid());
        List<MeasureValue> measureValues = indicatorsDataService.retrieveMeasureValuesInIndicatorInstance(RestConstants.SERVICE_CONTEXT, indicatorInstance.getUuid());
        
        List<ConditionDimensionDto> conditionDimensionDtos = new ArrayList<ConditionDimensionDto>();
        ConditionUtil.filterGeographicalValues(selectedRepresentations, selectedGranularities, geographicalValues, conditionDimensionDtos);
        ConditionUtil.filterTimeValues(selectedRepresentations, selectedGranularities, timeValues, conditionDimensionDtos);
        ConditionUtil.filterMeasureValues(selectedRepresentations, selectedGranularities, measureValues, conditionDimensionDtos);
        
        Map<String, ObservationExtendedDto> observationMap = indicatorsDataService.findObservationsExtendedByDimensionsInIndicatorInstance(RestConstants.SERVICE_CONTEXT, indicatorInstance.getUuid(), conditionDimensionDtos);
        
        DataTypeRequest dataTypeRequest = new DataTypeRequest(indicatorInstance, geographicalValues, timeValues, measureValues, observationMap);
        return dto2TypeMapper.createDataType(dataTypeRequest);
    }
 

    private IndicatorInstance retrieveIndicatorInstanceByCode(final String idIndicatorSystem, final String idIndicatorInstance) throws MetamacException {
        IndicatorInstance indicatorInstance = indicatorsSystemsService.retrieveIndicatorInstancePublishedByCode(RestConstants.SERVICE_CONTEXT, idIndicatorInstance);
        IndicatorsSystemVersion indicatorsSystemVersion = indicatorsSystemsService.retrieveIndicatorsSystemPublishedByCode(RestConstants.SERVICE_CONTEXT, idIndicatorSystem);
        if (!indicatorInstance.getElementLevel().getIndicatorsSystemVersion().getIndicatorsSystem().getCode().equals(indicatorsSystemVersion.getIndicatorsSystem().getCode())) {
            String text = MessageFormat.format("IndicatorInstance: {0}, not in indicatorSystem: {1}", idIndicatorSystem, idIndicatorInstance);
            logger.warn(text);
            throw new RestRuntimeException(HttpStatus.NOT_FOUND, text);
        }
        return indicatorInstance;
    }



}
