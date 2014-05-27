package es.gobcan.istac.indicators.rest.serviceapi;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.search.criteria.SculptorCriteria;

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
import es.gobcan.istac.indicators.core.vo.GeographicalCodeVO;
import es.gobcan.istac.indicators.core.vo.GeographicalValueVO;
import es.gobcan.istac.indicators.core.vo.IndicatorObservationsExtendedVO;
import es.gobcan.istac.indicators.core.vo.IndicatorObservationsVO;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataFilterVO;

public interface IndicatorsApiService {

    public List<SubjectIndicatorResult> retrieveSubjectsInIndicators() throws MetamacException;

    /* INDICATORS */
    public PagedResult<IndicatorVersion> findIndicators(List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException;

    public IndicatorVersion retrieveIndicatorByCode(String code) throws MetamacException;

    public IndicatorVersion retrieveIndicator(String uuid) throws MetamacException;

    public DataSource retrieveDataSource(String uuid) throws MetamacException;

    public UnitMultiplier retrieveUnitMultiplier(Integer unitMultiplier) throws MetamacException;

    /* DATA */
    public List<GeographicalGranularity> retrieveGeographicalGranularitiesInIndicatorVersion(IndicatorVersion source) throws MetamacException;

    public GeographicalValue retrieveGeographicalValueByCode(String geoCode) throws MetamacException;

    public List<GeographicalValueVO> retrieveGeographicalValuesInIndicatorVersion(IndicatorVersion indicatorVersion) throws MetamacException;

    public List<GeographicalCodeVO> retrieveGeographicalCodesInIndicatorVersion(IndicatorVersion indicatorVersion) throws MetamacException;

    public List<TimeGranularity> retrieveTimeGranularitiesInIndicator(String indicatorUuid) throws MetamacException;

    public List<TimeValue> retrieveTimeValuesInIndicatorVersion(IndicatorVersion indicatorVersion) throws MetamacException;

    public List<MeasureValue> retrieveMeasureValuesInIndicator(IndicatorVersion indicatorVersion) throws MetamacException;

    public List<GeographicalGranularity> retrieveGeographicalGranularitiesInIndicatorInstance(String indicatorInstanceUuid) throws MetamacException;

    public List<GeographicalValueVO> retrieveGeographicalValuesInIndicatorInstance(String indicatorInstanceUuid) throws MetamacException;

    public List<GeographicalCodeVO> retrieveGeographicalCodesInIndicatorInstance(String indicatorInstanceUuid) throws MetamacException;

    public List<TimeGranularity> retrieveTimeGranularitiesInIndicatorInstance(String indicatorInstanceUuid) throws MetamacException;

    public List<TimeValue> retrieveTimeValuesInIndicatorInstance(String indicatorInstanceUuid) throws MetamacException;

    public List<MeasureValue> retrieveMeasureValuesInIndicatorInstance(String indicatorInstanceUuid) throws MetamacException;

    public IndicatorObservationsExtendedVO findObservationsExtendedInIndicatorInstance(String indicatorInstanceUuid, IndicatorsDataFilterVO dataFilter) throws MetamacException;

    public IndicatorObservationsVO findObservationsInIndicatorInstance(String indicatorInstanceUuid, IndicatorsDataFilterVO dataFilter) throws MetamacException;

    public IndicatorObservationsExtendedVO findObservationsExtendedInIndicator(String uuid, IndicatorsDataFilterVO dataFilter) throws MetamacException;

    public IndicatorObservationsVO findObservationsInIndicator(String uuid, IndicatorsDataFilterVO dataFilter) throws MetamacException;

    /* INDICATORS SYSTEM */
    public PagedResult<IndicatorsSystemVersion> findIndicatorsSystems(PagingParameter pagingParameter) throws MetamacException;

    public IndicatorsSystemVersion retrieveIndicatorsSystemByCode(String idIndicatorSystem) throws MetamacException;

    public PagedResult<IndicatorInstance> findIndicatorsInstancesInIndicatorsSystems(SculptorCriteria sculptorCriteria) throws MetamacException;

    public IndicatorInstance retrieveIndicatorInstanceByCode(String idIndicatorSystem, String idIndicatorInstance) throws MetamacException;

    public List<IndicatorsSystemHistory> findIndicatorsSystemHistory(String idIndicatorSystem, int maxResults) throws MetamacException;

    public List<IndicatorsSystemVersion> retrieveIndicatorsSystemPublishedForIndicator(String indicatorUuid) throws MetamacException;

    public TimeValue retrieveTimeValueByCode(String timeCode) throws MetamacException;

}
