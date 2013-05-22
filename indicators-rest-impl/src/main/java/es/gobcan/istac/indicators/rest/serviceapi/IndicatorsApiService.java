package es.gobcan.istac.indicators.rest.serviceapi;

import java.util.List;
import java.util.Map;

import com.arte.statistic.dataset.repository.dto.ObservationDto;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.search.criteria.SculptorCriteria;

import com.arte.statistic.dataset.repository.dto.ConditionDimensionDto;
import com.arte.statistic.dataset.repository.dto.ObservationExtendedDto;

import es.gobcan.istac.indicators.core.domain.DataSource;
import es.gobcan.istac.indicators.core.domain.GeographicalGranularity;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemHistory;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.domain.MeasureValue;
import es.gobcan.istac.indicators.core.domain.TimeGranularity;
import es.gobcan.istac.indicators.core.domain.TimeValue;
import es.gobcan.istac.indicators.core.domain.UnitMultiplier;
import es.gobcan.istac.indicators.core.repositoryimpl.finders.SubjectIndicatorResult;


public interface IndicatorsApiService {
    
    public List<SubjectIndicatorResult> retrieveSubjectsInIndicators() throws MetamacException;

    /* INDICATORS */
    public PagedResult<IndicatorVersion> findIndicators(List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException;
    
    public IndicatorVersion retrieveIndicatorByCode(String code) throws MetamacException;
    
    public IndicatorVersion retrieveIndicator(String uuid) throws MetamacException;
    
    public DataSource retrieveDataSource(String uuid) throws MetamacException;
    
    public UnitMultiplier retrieveUnitMultiplier(Integer unitMultiplier) throws MetamacException;
    
    
    /* DATA */
    public List<GeographicalGranularity> retrieveGeographicalGranularitiesInIndicator(String indicatorUuid) throws MetamacException;
    
    public List<GeographicalValue> retrieveGeographicalValuesInIndicator(String indicatorUuid) throws MetamacException;
    
    public List<TimeGranularity> retrieveTimeGranularitiesInIndicator(String indicatorUuid) throws MetamacException;
    
    public List<TimeValue> retrieveTimeValuesInIndicator(String indicatorUuid) throws MetamacException;
    
    public List<MeasureValue> retrieveMeasureValuesInIndicator(String indicatorUuid) throws MetamacException;
    
    public List<GeographicalGranularity> retrieveGeographicalGranularitiesInIndicatorInstance(String indicatorInstanceUuid) throws MetamacException;
    
    public List<GeographicalValue> retrieveGeographicalValuesInIndicatorInstance(String indicatorInstanceUuid) throws MetamacException;
    
    public List<TimeGranularity> retrieveTimeGranularitiesInIndicatorInstance(String indicatorInstanceUuid) throws MetamacException;
    
    public List<TimeValue> retrieveTimeValuesInIndicatorInstance(String indicatorInstanceUuid) throws MetamacException;
    
    public List<MeasureValue> retrieveMeasureValuesInIndicatorInstance(String indicatorInstanceUuid) throws MetamacException;

    public Map<String, ObservationExtendedDto> findObservationsExtendedByDimensionsInIndicatorInstance(String indicatorInstanceUuid, List<ConditionDimensionDto> conditions) throws MetamacException;

    public Map<String, ObservationDto> findObservationsByDimensionsInIndicatorInstance(String indicatorInstanceUuid, List<ConditionDimensionDto> conditions) throws MetamacException;
    
    /* INDICATORS SYSTEM */
    public PagedResult<IndicatorsSystemVersion> findIndicatorsSystems(PagingParameter pagingParameter) throws MetamacException;
    
    public IndicatorsSystemVersion retrieveIndicatorsSystemByCode(String idIndicatorSystem) throws MetamacException;
    
    public PagedResult<IndicatorInstance> findIndicatorsInstancesInIndicatorsSystems(SculptorCriteria sculptorCriteria) throws MetamacException; 

    public IndicatorInstance retrieveIndicatorInstanceByCode(String idIndicatorSystem, String idIndicatorInstance) throws MetamacException;
    
    public  List<IndicatorsSystemHistory> findIndicatorsSystemHistory(String idIndicatorSystem, int maxResults) throws MetamacException;
    
    public List<IndicatorsSystemVersion> retrieveIndicatorsSystemPublishedForIndicator(String indicatorUuid) throws MetamacException;
}
