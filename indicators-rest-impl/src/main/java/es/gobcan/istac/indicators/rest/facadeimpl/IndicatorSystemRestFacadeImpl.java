package es.gobcan.istac.indicators.rest.facadeimpl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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

import com.arte.statistic.dataset.repository.dto.ObservationDto;

import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorDataDimensionTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.MeasureDimensionTypeEnum;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsDataService;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsSystemsService;
import es.gobcan.istac.indicators.rest.RestConstants;
import es.gobcan.istac.indicators.rest.exception.RestRuntimeException;
import es.gobcan.istac.indicators.rest.facadeapi.IndicatorSystemRestFacade;
import es.gobcan.istac.indicators.rest.mapper.Do2TypeMapper;
import es.gobcan.istac.indicators.rest.types.DataDimensionType;
import es.gobcan.istac.indicators.rest.types.IndicatorInstanceDataType;
import es.gobcan.istac.indicators.rest.types.IndicatorInstanceType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemType;
import es.gobcan.istac.indicators.rest.types.NoPagedResultType;
import es.gobcan.istac.indicators.rest.types.PagedResultType;
import es.gobcan.istac.indicators.rest.types.RestCriteriaPaginator;

@Service("indicatorSystemRestFacade")
public class IndicatorSystemRestFacadeImpl implements IndicatorSystemRestFacade {

    public static Integer            MAXIMUM_RESULT_SIZE_DEFAULT = Integer.valueOf(25);
    public static Integer            MAXIMUM_RESULT_SIZE_ALLOWED = Integer.valueOf(1000);

    private Logger                   logger                      = LoggerFactory.getLogger(getClass());

    @Autowired
    private IndicatorsSystemsService indicatorsSystemsService    = null;

    @Autowired
    private IndicatorsDataService    indicatorsDataService       = null;

    @Autowired
    private Do2TypeMapper            dto2TypeMapper              = null;

    @Override
    public PagedResultType<IndicatorsSystemBaseType> findIndicatorsSystems(final String baseURL, final RestCriteriaPaginator paginator) throws Exception {
        PagingParameter pagingParameter = createPagingParameter(paginator);
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
    public NoPagedResultType<IndicatorInstanceType> retrieveIndicatorsInstances(final String baseURL, final String idIndicatorSystem) throws Exception {
        IndicatorsSystemVersion indicatorsSystemVersion = indicatorsSystemsService.retrieveIndicatorsSystemPublishedByCode(RestConstants.SERVICE_CONTEXT, idIndicatorSystem);
        List<IndicatorInstance> indicatorInstances = indicatorsSystemsService.retrieveIndicatorsInstancesByIndicatorsSystem(RestConstants.SERVICE_CONTEXT, indicatorsSystemVersion.getIndicatorsSystem().getUuid(), indicatorsSystemVersion.getVersionNumber());
        List<IndicatorInstanceType> indicatorInstanceTypes = dto2TypeMapper.indicatorsInstanceDoToType(indicatorInstances, baseURL);
        
        NoPagedResultType<IndicatorInstanceType> result = new NoPagedResultType<IndicatorInstanceType>();
        result.setKind(RestConstants.KIND_INDICATOR_INSTANCES);
        result.setItems(indicatorInstanceTypes);
        result.setTotal(indicatorInstanceTypes.size());
        return result;
    }

    @Override
    public IndicatorInstanceType retrieveIndicatorsInstance(final String baseURL, final String idIndicatorSystem, final String uuidIndicatorInstance) throws Exception {
        IndicatorInstance indicatorInstance = retrieveIndicatorInstance(idIndicatorSystem, uuidIndicatorInstance);
        IndicatorInstanceType result = dto2TypeMapper.indicatorsInstanceDoToType(indicatorInstance, baseURL);
        return result;
    }

    @Override
    public IndicatorInstanceDataType retrieveIndicatorsInstanceData(String baseUrl, String idIndicatorSystem, String uuidIndicatorInstance) throws Exception {
        retrieveIndicatorInstance(idIndicatorSystem, uuidIndicatorInstance); // Check Published
        
        List<GeographicalValue> geographicalValues = indicatorsDataService.retrieveGeographicalValuesInIndicatorInstance(RestConstants.SERVICE_CONTEXT, uuidIndicatorInstance);
        List<String> timeValues = indicatorsDataService.retrieveTimeValuesInIndicatorInstance(RestConstants.SERVICE_CONTEXT, uuidIndicatorInstance);
        List<MeasureDimensionTypeEnum> measureValues = indicatorsDataService.retrieveMeasureValuesInIndicatorInstance(RestConstants.SERVICE_CONTEXT, uuidIndicatorInstance);
        
        Map<String, ObservationDto> observationMap = indicatorsDataService.findObservationsByDimensionsInIndicatorInstance(RestConstants.SERVICE_CONTEXT, uuidIndicatorInstance, null);
        
        List<String> observations = new ArrayList<String>();
        List<String> formatIds = new ArrayList<String>();
        List<Integer> formatSizes = new ArrayList<Integer>();
        Map<String, DataDimensionType> dimension = new LinkedHashMap<String, DataDimensionType>(); 
        
        formatIds.add(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name());
        formatIds.add(IndicatorDataDimensionTypeEnum.TIME.name());
        formatIds.add(IndicatorDataDimensionTypeEnum.MEASURE.name());
        formatSizes.add(geographicalValues.size());
        formatSizes.add(timeValues.size());
        formatSizes.add(measureValues.size());
        
        DataDimensionType dataDimensionTypeGeographical = new DataDimensionType();
        dataDimensionTypeGeographical.setRepresentationIndex(new LinkedHashMap<String, Integer>());
        dimension.put(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name(), dataDimensionTypeGeographical);
        
        DataDimensionType dataDimensionTypeTime = new DataDimensionType();
        dataDimensionTypeTime.setRepresentationIndex(new LinkedHashMap<String, Integer>());
        dimension.put(IndicatorDataDimensionTypeEnum.TIME.name(), dataDimensionTypeTime);

        DataDimensionType dataDimensionTypeMeasure = new DataDimensionType();
        dataDimensionTypeMeasure.setRepresentationIndex(new LinkedHashMap<String, Integer>());
        dimension.put(IndicatorDataDimensionTypeEnum.MEASURE.name(), dataDimensionTypeMeasure);
        
        for (int i = 0; i < geographicalValues.size(); i++) {
            GeographicalValue geographicalValue = geographicalValues.get(i);
            dimension.get(IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name()).getRepresentationIndex().put(geographicalValue.getCode(), i);
            
            for (int j = 0; j < timeValues.size(); j++) {
                String timeValue = timeValues.get(j);
                dimension.get(IndicatorDataDimensionTypeEnum.TIME.name()).getRepresentationIndex().put(timeValue, j);
                
                for (int k = 0; k < measureValues.size(); k++) {
                    MeasureDimensionTypeEnum measureValue = measureValues.get(k);
                    dimension.get(IndicatorDataDimensionTypeEnum.MEASURE.name()).getRepresentationIndex().put(measureValue.name(), k);
                    
                    // Observation ID: Be careful!!! don't change order of ids
                    String id = new StringBuilder().append(geographicalValue.getCode()).append("#").append(timeValue).append("#").append(measureValue).toString();
                    ObservationDto observationDto = observationMap.get(id);
                    String value = null;
                    if (observationDto != null) {
                        value = observationDto.getPrimaryMeasure();
                    }                
                    observations.add(value);
                }
            }
        }
        
        IndicatorInstanceDataType indicatorInstanceDataType = new IndicatorInstanceDataType();
        indicatorInstanceDataType.setObservation(observations);
        indicatorInstanceDataType.setFormatId(formatIds);
        indicatorInstanceDataType.setFormatSize(formatSizes);
        indicatorInstanceDataType.setDimension(dimension);
        return indicatorInstanceDataType;
    }

    private IndicatorInstance retrieveIndicatorInstance(final String idIndicatorSystem, final String uuidIndicatorInstance) throws MetamacException {
        IndicatorInstance indicatorInstance = indicatorsSystemsService.retrieveIndicatorInstance(RestConstants.SERVICE_CONTEXT, uuidIndicatorInstance);
        IndicatorsSystemVersion indicatorsSystemVersion = indicatorsSystemsService.retrieveIndicatorsSystemPublishedByCode(RestConstants.SERVICE_CONTEXT, idIndicatorSystem);
        if (!indicatorInstance.getElementLevel().getIndicatorsSystemVersion().getIndicatorsSystem().getCode().equals(indicatorsSystemVersion.getIndicatorsSystem().getCode())) {
            String text = MessageFormat.format("IndicatorInstance: {0}, not in indicatorSystem: {1}", idIndicatorSystem, uuidIndicatorInstance);
            logger.warn(text);
            throw new RestRuntimeException(HttpStatus.NOT_FOUND, text);
        }
        return indicatorInstance;
    }

    private PagingParameter createPagingParameter(final RestCriteriaPaginator paginator) {
        if (paginator.getOffset() == null || paginator.getOffset() < 0) {
            paginator.setOffset(0);
        }

        if (paginator.getLimit() == null || paginator.getLimit() < 0) {
            paginator.setLimit(MAXIMUM_RESULT_SIZE_DEFAULT);
        }

        if (paginator.getLimit() > MAXIMUM_RESULT_SIZE_ALLOWED) {
            paginator.setLimit(MAXIMUM_RESULT_SIZE_ALLOWED);
        }
        return PagingParameter.rowAccess(paginator.getOffset(), paginator.getOffset() + paginator.getLimit(), Boolean.TRUE);
    }

}
