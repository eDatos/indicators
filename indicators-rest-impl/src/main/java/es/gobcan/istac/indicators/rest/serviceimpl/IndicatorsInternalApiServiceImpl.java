package es.gobcan.istac.indicators.rest.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.search.criteria.SculptorCriteria;
import org.springframework.http.HttpStatus;

import es.gobcan.istac.indicators.core.domain.GeographicalGranularity;
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
import es.gobcan.istac.indicators.core.vo.GeographicalCodeVO;
import es.gobcan.istac.indicators.core.vo.GeographicalValueVO;
import es.gobcan.istac.indicators.core.vo.IndicatorObservationsExtendedVO;
import es.gobcan.istac.indicators.core.vo.IndicatorObservationsVO;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataFilterVO;
import es.gobcan.istac.indicators.rest.IndicatorsRestConstants;
import es.gobcan.istac.indicators.rest.exception.RestRuntimeException;
import es.gobcan.istac.indicators.rest.serviceapi.IndicatorsApiService;

public class IndicatorsInternalApiServiceImpl extends IndicatorsApiServiceBaseImpl implements IndicatorsApiService {

    @Override
    public List<SubjectIndicatorResult> retrieveSubjectsInIndicators() throws MetamacException {
        return indicatorsService.retrieveSubjectsInLastVersionIndicators(IndicatorsRestConstants.SERVICE_CONTEXT);
    }

    @Override
    public PagedResult<IndicatorVersion> findIndicators(List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        return indicatorsService.findIndicators(IndicatorsRestConstants.SERVICE_CONTEXT, conditions, pagingParameter);
    }

    @Override
    public IndicatorVersion retrieveIndicatorByCode(String indicatorCode) throws MetamacException {
        List<ConditionalCriteria> conditions = new ArrayList<ConditionalCriteria>();
        conditions.add(ConditionalCriteria.equal(IndicatorVersionProperties.indicator().code(), indicatorCode));
        PagingParameter pagingParameter = PagingParameter.pageAccess(1);

        PagedResult<IndicatorVersion> result = indicatorsService.findIndicators(IndicatorsRestConstants.SERVICE_CONTEXT, conditions, pagingParameter);

        if (result.getValues().size() == 0) {
            throw new MetamacException();
        }

        return result.getValues().get(0);
    }

    @Override
    public IndicatorVersion retrieveIndicator(String uuid) throws MetamacException {
        List<ConditionalCriteria> conditions = new ArrayList<ConditionalCriteria>();
        conditions.add(ConditionalCriteria.equal(IndicatorVersionProperties.indicator().uuid(), uuid));
        PagingParameter pagingParameter = PagingParameter.pageAccess(1);

        PagedResult<IndicatorVersion> result = indicatorsService.findIndicators(IndicatorsRestConstants.SERVICE_CONTEXT, conditions, pagingParameter);

        if (result.getValues().size() == 0) {
            throw new MetamacException();
        }

        return result.getValues().get(0);
    }

    @Override
    public PagedResult<IndicatorsSystemVersion> findIndicatorsSystems(PagingParameter pagingParameter) throws MetamacException {
        return indicatorsSystemsService.findIndicatorsSystems(IndicatorsRestConstants.SERVICE_CONTEXT, null, pagingParameter);
    }

    @Override
    public IndicatorsSystemVersion retrieveIndicatorsSystemByCode(String idIndicatorSystem) throws MetamacException {
        ArrayList<ConditionalCriteria> conditions = new ArrayList<ConditionalCriteria>();
        conditions.add(ConditionalCriteria.equal(IndicatorsSystemVersionProperties.indicatorsSystem().code(), idIndicatorSystem));
        PagingParameter pagingParameter = PagingParameter.pageAccess(1);
        PagedResult<IndicatorsSystemVersion> result = indicatorsSystemsService.findIndicatorsSystems(IndicatorsRestConstants.SERVICE_CONTEXT, conditions, pagingParameter);

        if (result.getValues().size() == 0) {
            throw new RestRuntimeException(HttpStatus.NOT_FOUND, "Indicator System not found");
        }

        return result.getValues().get(0);
    }

    @Override
    public PagedResult<IndicatorInstance> findIndicatorsInstancesInIndicatorsSystems(SculptorCriteria sculptorCriteria) throws MetamacException {
        return indicatorsSystemsService.findIndicatorsInstancesInLastVersionIndicatorsSystems(IndicatorsRestConstants.SERVICE_CONTEXT, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());
    }

    @Override
    public IndicatorInstance retrieveIndicatorInstanceByCode(String idIndicatorSystem, String idIndicatorInstance) throws MetamacException {
        ArrayList<ConditionalCriteria> conditions = new ArrayList<ConditionalCriteria>();
        conditions.add(ConditionalCriteria.equal(IndicatorInstanceProperties.elementLevel().indicatorsSystemVersion().indicatorsSystem().code(), idIndicatorSystem));
        conditions.add(ConditionalCriteria.equal(IndicatorInstanceProperties.code(), idIndicatorInstance));

        PagingParameter pagingParameter = PagingParameter.pageAccess(1);
        PagedResult<IndicatorInstance> result = indicatorsSystemsService.findIndicatorsInstancesInLastVersionIndicatorsSystems(IndicatorsRestConstants.SERVICE_CONTEXT, conditions, pagingParameter);

        if (result.getValues().size() == 0) {
            throw new RestRuntimeException(HttpStatus.NOT_FOUND, "Indicator instance not found");
        }

        return result.getValues().get(0);
    }

    @Override
    public List<TimeGranularity> retrieveTimeGranularitiesInIndicator(String indicatorUuid) throws MetamacException {
        String lastVersion = getLastVersionIndicator(IndicatorsRestConstants.SERVICE_CONTEXT, indicatorUuid);
        return indicatorsCoverageService.retrieveTimeGranularitiesInIndicator(IndicatorsRestConstants.SERVICE_CONTEXT, indicatorUuid, lastVersion);
    }

    @Override
    public List<GeographicalGranularity> retrieveGeographicalGranularitiesInIndicatorInstance(String indicatorInstanceUuid) throws MetamacException {
        return indicatorsCoverageService.retrieveGeographicalGranularitiesInIndicatorInstanceWithLastVersionIndicator(IndicatorsRestConstants.SERVICE_CONTEXT, indicatorInstanceUuid);
    }

    @Override
    public List<GeographicalValueVO> retrieveGeographicalValuesInIndicatorInstance(String indicatorInstanceUuid) throws MetamacException {
        return indicatorsCoverageService.retrieveGeographicalValuesInIndicatorInstanceWithLastVersionIndicator(IndicatorsRestConstants.SERVICE_CONTEXT, indicatorInstanceUuid);
    }

    @Override
    public List<GeographicalCodeVO> retrieveGeographicalCodesInIndicatorInstance(String indicatorInstanceUuid) throws MetamacException {
        return indicatorsCoverageService.retrieveGeographicalCodesInIndicatorInstanceWithLastVersionIndicator(IndicatorsRestConstants.SERVICE_CONTEXT, indicatorInstanceUuid);
    }

    @Override
    public List<TimeGranularity> retrieveTimeGranularitiesInIndicatorInstance(String indicatorInstanceUuid) throws MetamacException {
        return indicatorsCoverageService.retrieveTimeGranularitiesInIndicatorInstanceWithLastVersionIndicator(IndicatorsRestConstants.SERVICE_CONTEXT, indicatorInstanceUuid);
    }

    @Override
    public List<TimeValue> retrieveTimeValuesInIndicatorInstance(String indicatorInstanceUuid) throws MetamacException {
        return indicatorsCoverageService.retrieveTimeValuesInIndicatorInstanceWithLastVersionIndicator(IndicatorsRestConstants.SERVICE_CONTEXT, indicatorInstanceUuid);
    }

    @Override
    public List<MeasureValue> retrieveMeasureValuesInIndicatorInstance(String indicatorInstanceUuid) throws MetamacException {
        return indicatorsCoverageService.retrieveMeasureValuesInIndicatorInstanceWithLastVersionIndicator(IndicatorsRestConstants.SERVICE_CONTEXT, indicatorInstanceUuid);
    }

    @Override
    public IndicatorObservationsExtendedVO findObservationsExtendedInIndicatorInstance(String indicatorInstanceUuid, IndicatorsDataFilterVO dataFilter) throws MetamacException {
        return indicatorsDataService.findObservationsExtendedByDimensionsInIndicatorInstanceWithLastVersionIndicator(IndicatorsRestConstants.SERVICE_CONTEXT, indicatorInstanceUuid, dataFilter);
    }

    @Override
    public IndicatorObservationsVO findObservationsInIndicatorInstance(String indicatorInstanceUuid, IndicatorsDataFilterVO dataFilter) throws MetamacException {
        return indicatorsDataService.findObservationsInIndicatorInstanceWithLastVersionIndicator(IndicatorsRestConstants.SERVICE_CONTEXT, indicatorInstanceUuid, dataFilter);
    }

    @Override
    public IndicatorObservationsExtendedVO findObservationsExtendedInIndicator(String uuid, IndicatorsDataFilterVO dataFilter) throws MetamacException {
        return indicatorsDataService.findObservationsExtendedByDimensionsInIndicatorLastVersion(IndicatorsRestConstants.SERVICE_CONTEXT, uuid, dataFilter);
    }

    @Override
    public IndicatorObservationsVO findObservationsInIndicator(String uuid, IndicatorsDataFilterVO dataFilter) throws MetamacException {
        return indicatorsDataService.findObservationsInIndicatorLastVersion(IndicatorsRestConstants.SERVICE_CONTEXT, uuid, dataFilter);
    }

    private String getLastVersionIndicator(ServiceContext ctx, String indicatorUuid) throws MetamacException {
        Indicator indicator = indicatorsService.retrieveIndicator(ctx, indicatorUuid);

        if (indicator.getProductionVersionNumber() != null) {
            return indicator.getProductionVersionNumber();
        } else {
            if (indicator.getIsPublished()) {
                return indicator.getDiffusionVersionNumber();
            } else {
                throw new MetamacException(ServiceExceptionType.INDICATOR_VERSION_LAST_ARCHIVED, indicatorUuid);
            }
        }
    }
}
