package es.gobcan.istac.indicators.rest.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.arte.statistic.dataset.repository.dto.ObservationDto;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.search.criteria.SculptorCriteria;
import org.springframework.http.HttpStatus;

import com.arte.statistic.dataset.repository.dto.ConditionDimensionDto;
import com.arte.statistic.dataset.repository.dto.ObservationExtendedDto;

import es.gobcan.istac.indicators.core.domain.GeographicalGranularity;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.Indicator;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorInstanceProperties;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorVersionProperties;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersionProperties;
import es.gobcan.istac.indicators.core.domain.MeasureValue;
import es.gobcan.istac.indicators.core.domain.TimeGranularity;
import es.gobcan.istac.indicators.core.domain.TimeValue;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.repositoryimpl.finders.SubjectIndicatorResult;
import es.gobcan.istac.indicators.rest.RestConstants;
import es.gobcan.istac.indicators.rest.exception.RestRuntimeException;
import es.gobcan.istac.indicators.rest.serviceapi.IndicatorsApiService;


public class IndicatorsInternalApiServiceImpl extends IndicatorsApiServiceBaseImpl implements IndicatorsApiService {

    @Override
    public List<SubjectIndicatorResult> retrieveSubjectsInIndicators() throws MetamacException {
        return indicatorsService.retrieveSubjectsInLastVersionIndicators(RestConstants.SERVICE_CONTEXT);
    }
    
    
    @Override
    public PagedResult<IndicatorVersion> findIndicators(List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        return indicatorsService.findIndicators(RestConstants.SERVICE_CONTEXT, conditions, pagingParameter);
    }
    
    @Override
    public IndicatorVersion retrieveIndicatorByCode(String indicatorCode) throws MetamacException {
        ArrayList<ConditionalCriteria> conditions = new ArrayList<ConditionalCriteria>();
        conditions.add(ConditionalCriteria.equal(IndicatorVersionProperties.indicator().code(), indicatorCode));
        PagingParameter pagingParameter = PagingParameter.pageAccess(1);

        PagedResult<IndicatorVersion> result = indicatorsService.findIndicators(RestConstants.SERVICE_CONTEXT, conditions, pagingParameter);

        if(result.getValues().size() == 0) {
            throw new MetamacException();
        }

        return result.getValues().get(0);
    }
    
    @Override
    public IndicatorVersion retrieveIndicator(String uuid) throws MetamacException {
        ArrayList<ConditionalCriteria> conditions = new ArrayList<ConditionalCriteria>();
        conditions.add(ConditionalCriteria.equal(IndicatorVersionProperties.indicator().uuid(), uuid));
        PagingParameter pagingParameter = PagingParameter.pageAccess(1);

        PagedResult<IndicatorVersion> result = indicatorsService.findIndicators(RestConstants.SERVICE_CONTEXT, conditions, pagingParameter);

        if(result.getValues().size() == 0) {
            throw new MetamacException();
        }

        return result.getValues().get(0);
    }
    
    @Override
    public PagedResult<IndicatorsSystemVersion> findIndicatorsSystems(PagingParameter pagingParameter) throws MetamacException {
        return indicatorsSystemsService.findIndicatorsSystems(RestConstants.SERVICE_CONTEXT, null, pagingParameter);
    }
    
    @Override
    public IndicatorsSystemVersion retrieveIndicatorsSystemByCode(String idIndicatorSystem) throws MetamacException {
        ArrayList<ConditionalCriteria> conditions = new ArrayList<ConditionalCriteria>();
        conditions.add(ConditionalCriteria.equal(IndicatorsSystemVersionProperties.indicatorsSystem().code(), idIndicatorSystem));
        PagingParameter pagingParameter = PagingParameter.pageAccess(1);
        PagedResult<IndicatorsSystemVersion> result = indicatorsSystemsService.findIndicatorsSystems(RestConstants.SERVICE_CONTEXT, conditions, pagingParameter);

        if(result.getValues().size() == 0) {
            throw new RestRuntimeException(HttpStatus.NOT_FOUND, "Indicator System not found");
        }

        return result.getValues().get(0);
    }
    
    @Override
    public PagedResult<IndicatorInstance> findIndicatorsInstancesInIndicatorsSystems(SculptorCriteria sculptorCriteria) throws MetamacException {
        return indicatorsSystemsService.findIndicatorsInstancesInLastVersionIndicatorsSystems(RestConstants.SERVICE_CONTEXT, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());
    }

    @Override
    public IndicatorInstance retrieveIndicatorInstanceByCode(String idIndicatorSystem, String idIndicatorInstance) throws MetamacException {
        ArrayList<ConditionalCriteria> conditions = new ArrayList<ConditionalCriteria>();
        conditions.add(ConditionalCriteria.equal(IndicatorInstanceProperties.elementLevel().indicatorsSystemVersion().indicatorsSystem().code(), idIndicatorSystem));
        conditions.add(ConditionalCriteria.equal(IndicatorInstanceProperties.code(), idIndicatorInstance));

        PagingParameter pagingParameter = PagingParameter.pageAccess(1);
        PagedResult<IndicatorInstance> result = indicatorsSystemsService.findIndicatorsInstancesInLastVersionIndicatorsSystems(RestConstants.SERVICE_CONTEXT, conditions, pagingParameter);

        if(result.getValues().size() == 0) {
            throw new RestRuntimeException(HttpStatus.NOT_FOUND, "Indicator instance not found");
        }

        return result.getValues().get(0);
    }
    
    @Override
    public List<GeographicalGranularity> retrieveGeographicalGranularitiesInIndicator(String indicatorUuid) throws MetamacException {
        String lastVersion = getLastVersionIndicator(RestConstants.SERVICE_CONTEXT, indicatorUuid);
        return indicatorsDataService.retrieveGeographicalGranularitiesInIndicator(RestConstants.SERVICE_CONTEXT, indicatorUuid, lastVersion);
    }
    
    @Override
    public List<GeographicalValue> retrieveGeographicalValuesInIndicator(String indicatorUuid) throws MetamacException {
        String lastVersion = getLastVersionIndicator(RestConstants.SERVICE_CONTEXT, indicatorUuid);
        return indicatorsDataService.retrieveGeographicalValuesInIndicator(RestConstants.SERVICE_CONTEXT, indicatorUuid, lastVersion);
    }
    
    @Override
    public List<TimeGranularity> retrieveTimeGranularitiesInIndicator(String indicatorUuid) throws MetamacException {
        String lastVersion = getLastVersionIndicator(RestConstants.SERVICE_CONTEXT, indicatorUuid);
        return indicatorsDataService.retrieveTimeGranularitiesInIndicator(RestConstants.SERVICE_CONTEXT, indicatorUuid, lastVersion);
    }
    
    @Override
    public List<TimeValue> retrieveTimeValuesInIndicator(String indicatorUuid) throws MetamacException {
        String lastVersion = getLastVersionIndicator(RestConstants.SERVICE_CONTEXT, indicatorUuid);
        return indicatorsDataService.retrieveTimeValuesInIndicator(RestConstants.SERVICE_CONTEXT, indicatorUuid, lastVersion);
    }
    
    @Override
    public List<MeasureValue> retrieveMeasureValuesInIndicator(String indicatorUuid) throws MetamacException {
        String lastVersion = getLastVersionIndicator(RestConstants.SERVICE_CONTEXT, indicatorUuid);
        return indicatorsDataService.retrieveMeasureValuesInIndicator(RestConstants.SERVICE_CONTEXT, indicatorUuid, lastVersion);
    }
    
    @Override
    public List<GeographicalGranularity> retrieveGeographicalGranularitiesInIndicatorInstance(String indicatorInstanceUuid) throws MetamacException {
        return indicatorsDataService.retrieveGeographicalGranularitiesInIndicatorInstanceWithLastVersionIndicator(RestConstants.SERVICE_CONTEXT, indicatorInstanceUuid);
    }
    
    @Override
    public List<GeographicalValue> retrieveGeographicalValuesInIndicatorInstance(String indicatorInstanceUuid) throws MetamacException {
        return indicatorsDataService.retrieveGeographicalValuesInIndicatorInstanceWithLastVersionIndicator(RestConstants.SERVICE_CONTEXT, indicatorInstanceUuid);
    }
    
    @Override
    public List<TimeGranularity> retrieveTimeGranularitiesInIndicatorInstance(String indicatorInstanceUuid) throws MetamacException {
        return indicatorsDataService.retrieveTimeGranularitiesInIndicatorInstanceWithLastVersionIndicator(RestConstants.SERVICE_CONTEXT, indicatorInstanceUuid);
    }
    
    @Override
    public List<TimeValue> retrieveTimeValuesInIndicatorInstance(String indicatorInstanceUuid) throws MetamacException {
        return indicatorsDataService.retrieveTimeValuesInIndicatorInstanceWithLastVersionIndicator(RestConstants.SERVICE_CONTEXT, indicatorInstanceUuid);
    }

    @Override
    public List<MeasureValue> retrieveMeasureValuesInIndicatorInstance(String indicatorInstanceUuid) throws MetamacException {
        return indicatorsDataService.retrieveMeasureValuesInIndicatorInstanceWithLastVersionIndicator(RestConstants.SERVICE_CONTEXT, indicatorInstanceUuid);
    }
    
    @Override
    public Map<String, ObservationExtendedDto> findObservationsExtendedByDimensionsInIndicatorInstance(String indicatorInstanceUuid, List<ConditionDimensionDto> conditions) throws MetamacException {
        return indicatorsDataService.findObservationsExtendedByDimensionsInIndicatorInstanceWithLastVersionIndicator(RestConstants.SERVICE_CONTEXT, indicatorInstanceUuid, conditions);
    }

    @Override
    public Map<String, ObservationDto> findObservationsByDimensionsInIndicatorInstance(String indicatorInstanceUuid, List<ConditionDimensionDto> conditions) throws MetamacException {
        return indicatorsDataService.findObservationsByDimensionsInIndicatorInstanceWithLastVersionIndicator(RestConstants.SERVICE_CONTEXT, indicatorInstanceUuid, conditions);
    }

    private String getLastVersionIndicator(ServiceContext ctx, String indicatorUuid) throws MetamacException {
        Indicator indicator = indicatorsService.retrieveIndicator(ctx, indicatorUuid);
        
        if (indicator.getProductionVersion() != null) {
            return indicator.getProductionVersion().getVersionNumber();
        } else {
            if (indicator.getIsPublished()) {
                return indicator.getDiffusionVersion().getVersionNumber();
            } else {
                throw new MetamacException(ServiceExceptionType.INDICATOR_VERSION_LAST_ARCHIVED, indicatorUuid);
            }
        }
    }
}
