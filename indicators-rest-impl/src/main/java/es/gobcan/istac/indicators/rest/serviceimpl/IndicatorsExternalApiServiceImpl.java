package es.gobcan.istac.indicators.rest.serviceimpl;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.search.criteria.SculptorCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.arte.statistic.dataset.repository.dto.ConditionDimensionDto;
import com.arte.statistic.dataset.repository.dto.ObservationExtendedDto;

import es.gobcan.istac.indicators.core.domain.GeographicalGranularity;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.domain.MeasureValue;
import es.gobcan.istac.indicators.core.domain.Subject;
import es.gobcan.istac.indicators.core.domain.TimeGranularity;
import es.gobcan.istac.indicators.core.domain.TimeValue;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsService;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsSystemsService;
import es.gobcan.istac.indicators.rest.RestConstants;
import es.gobcan.istac.indicators.rest.exception.RestRuntimeException;
import es.gobcan.istac.indicators.rest.serviceapi.IndicatorsApiService;


public class IndicatorsExternalApiServiceImpl extends IndicatorsApiServiceBaseImpl implements IndicatorsApiService {
    
    
    @Override
    public PagedResult<IndicatorVersion> findIndicators(List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        return indicatorsService.findIndicatorsPublished(RestConstants.SERVICE_CONTEXT, conditions, pagingParameter);
    }
    
    @Override
    public IndicatorVersion retrieveIndicatorByCode(String code) throws MetamacException {
        return indicatorsService.retrieveIndicatorPublishedByCode(RestConstants.SERVICE_CONTEXT, code);
    }
    
    @Override
    public IndicatorVersion retrieveIndicator(String uuid) throws MetamacException {
        return indicatorsService.retrieveIndicatorPublished(RestConstants.SERVICE_CONTEXT, uuid);
    }
    
    @Override
    public PagedResult<IndicatorsSystemVersion> findIndicatorsSystems(PagingParameter pagingParameter) throws MetamacException {
        return indicatorsSystemsService.findIndicatorsSystemsPublished(RestConstants.SERVICE_CONTEXT, null, pagingParameter);
    }
    
    @Override
    public IndicatorsSystemVersion retrieveIndicatorsSystemByCode(String idIndicatorSystem) throws MetamacException {
        return indicatorsSystemsService.retrieveIndicatorsSystemPublishedByCode(RestConstants.SERVICE_CONTEXT, idIndicatorSystem);
    }
    
    @Override
    public PagedResult<IndicatorInstance> findIndicatorsInstancesInIndicatorsSystems(SculptorCriteria sculptorCriteria) throws MetamacException {
        return indicatorsSystemsService.findIndicatorsInstancesInPublishedIndicatorsSystems(RestConstants.SERVICE_CONTEXT, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());
    }

    @Override
    public IndicatorInstance retrieveIndicatorInstanceByCode(final String idIndicatorSystem, final String idIndicatorInstance) throws MetamacException {
        IndicatorInstance indicatorInstance = indicatorsSystemsService.retrieveIndicatorInstancePublishedByCode(RestConstants.SERVICE_CONTEXT, idIndicatorInstance);
        IndicatorsSystemVersion indicatorsSystemVersion = retrieveIndicatorsSystemByCode(idIndicatorSystem);
        if (!indicatorInstance.getElementLevel().getIndicatorsSystemVersion().getIndicatorsSystem().getCode().equals(indicatorsSystemVersion.getIndicatorsSystem().getCode())) {
            String text = MessageFormat.format("IndicatorInstance: {0}, not in indicatorSystem: {1}", idIndicatorSystem, idIndicatorInstance);

            throw new RestRuntimeException(HttpStatus.NOT_FOUND, text);
        }
        return indicatorInstance;
    }
    
    @Override
    public List<GeographicalGranularity> retrieveGeographicalGranularitiesInIndicator(String indicatorUuid) throws MetamacException {
        return indicatorsDataService.retrieveGeographicalGranularitiesInIndicatorPublished(RestConstants.SERVICE_CONTEXT, indicatorUuid);
    }
    
    @Override
    public List<GeographicalValue> retrieveGeographicalValuesInIndicator(String indicatorUuid) throws MetamacException {
        return indicatorsDataService.retrieveGeographicalValuesInIndicatorPublished(RestConstants.SERVICE_CONTEXT, indicatorUuid);
    }
    
    @Override
    public List<TimeGranularity> retrieveTimeGranularitiesInIndicator(String indicatorUuid) throws MetamacException {
        return indicatorsDataService.retrieveTimeGranularitiesInIndicatorPublished(RestConstants.SERVICE_CONTEXT, indicatorUuid);
    }
    
    @Override
    public List<TimeValue> retrieveTimeValuesInIndicator(String indicatorUuid) throws MetamacException {
        return indicatorsDataService.retrieveTimeValuesInIndicatorPublished(RestConstants.SERVICE_CONTEXT, indicatorUuid);
    }
    
    @Override
    public List<MeasureValue> retrieveMeasureValuesInIndicator(String indicatorUuid) throws MetamacException {
        return indicatorsDataService.retrieveMeasureValuesInIndicatorPublished(RestConstants.SERVICE_CONTEXT, indicatorUuid);
    }
    
    @Override
    public List<GeographicalGranularity> retrieveGeographicalGranularitiesInIndicatorInstance(String indicatorInstanceUuid) throws MetamacException {
        return indicatorsDataService.retrieveGeographicalGranularitiesInIndicatorInstanceWithPublishedIndicator(RestConstants.SERVICE_CONTEXT, indicatorInstanceUuid);
    }
    
    @Override
    public List<GeographicalValue> retrieveGeographicalValuesInIndicatorInstance(String indicatorInstanceUuid) throws MetamacException {
        return indicatorsDataService.retrieveGeographicalValuesInIndicatorInstanceWithPublishedIndicator(RestConstants.SERVICE_CONTEXT, indicatorInstanceUuid);
    }
    
    @Override
    public List<TimeGranularity> retrieveTimeGranularitiesInIndicatorInstance(String indicatorInstanceUuid) throws MetamacException {
        return indicatorsDataService.retrieveTimeGranularitiesInIndicatorInstanceWithPublishedIndicator(RestConstants.SERVICE_CONTEXT, indicatorInstanceUuid);
    }
    
    @Override
    public List<TimeValue> retrieveTimeValuesInIndicatorInstance(String indicatorInstanceUuid) throws MetamacException {
        return indicatorsDataService.retrieveTimeValuesInIndicatorInstanceWithPublishedIndicator(RestConstants.SERVICE_CONTEXT, indicatorInstanceUuid);
    }

    @Override
    public List<MeasureValue> retrieveMeasureValuesInIndicatorInstance(String indicatorInstanceUuid) throws MetamacException {
        return indicatorsDataService.retrieveMeasureValuesInIndicatorInstanceWithPublishedIndicator(RestConstants.SERVICE_CONTEXT, indicatorInstanceUuid);
    }
    
    @Override
    public Map<String, ObservationExtendedDto> findObservationsExtendedByDimensionsInIndicatorInstance(String indicatorInstanceUuid, List<ConditionDimensionDto> conditions) throws MetamacException {
        return indicatorsDataService.findObservationsExtendedByDimensionsInIndicatorInstanceWithPublishedIndicator(RestConstants.SERVICE_CONTEXT, indicatorInstanceUuid, conditions);
    }
    
    
}
