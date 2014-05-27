package es.gobcan.istac.indicators.rest.serviceimpl;

import java.text.MessageFormat;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.search.criteria.SculptorCriteria;
import org.springframework.http.HttpStatus;

import es.gobcan.istac.indicators.core.domain.GeographicalGranularity;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.domain.MeasureValue;
import es.gobcan.istac.indicators.core.domain.TimeGranularity;
import es.gobcan.istac.indicators.core.domain.TimeValue;
import es.gobcan.istac.indicators.core.repositoryimpl.finders.SubjectIndicatorResult;
import es.gobcan.istac.indicators.core.vo.GeographicalCodeVO;
import es.gobcan.istac.indicators.core.vo.GeographicalValueVO;
import es.gobcan.istac.indicators.core.vo.IndicatorObservationsExtendedVO;
import es.gobcan.istac.indicators.core.vo.IndicatorObservationsVO;
import es.gobcan.istac.indicators.core.vo.IndicatorsDataFilterVO;
import es.gobcan.istac.indicators.rest.RestConstants;
import es.gobcan.istac.indicators.rest.exception.RestRuntimeException;
import es.gobcan.istac.indicators.rest.serviceapi.IndicatorsApiService;

public class IndicatorsExternalApiServiceImpl extends IndicatorsApiServiceBaseImpl implements IndicatorsApiService {

    @Override
    public List<SubjectIndicatorResult> retrieveSubjectsInIndicators() throws MetamacException {
        return indicatorsService.retrieveSubjectsInPublishedIndicators(RestConstants.SERVICE_CONTEXT);
    }

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
    public List<TimeGranularity> retrieveTimeGranularitiesInIndicator(String indicatorUuid) throws MetamacException {
        return indicatorsCoverageService.retrieveTimeGranularitiesInIndicatorPublished(RestConstants.SERVICE_CONTEXT, indicatorUuid);
    }

    @Override
    public List<GeographicalGranularity> retrieveGeographicalGranularitiesInIndicatorInstance(String indicatorInstanceUuid) throws MetamacException {
        return indicatorsCoverageService.retrieveGeographicalGranularitiesInIndicatorInstanceWithPublishedIndicator(RestConstants.SERVICE_CONTEXT, indicatorInstanceUuid);
    }

    @Override
    public List<GeographicalValueVO> retrieveGeographicalValuesInIndicatorInstance(String indicatorInstanceUuid) throws MetamacException {
        return indicatorsCoverageService.retrieveGeographicalValuesInIndicatorInstanceWithPublishedIndicator(RestConstants.SERVICE_CONTEXT, indicatorInstanceUuid);
    }

    @Override
    public List<GeographicalCodeVO> retrieveGeographicalCodesInIndicatorInstance(String indicatorInstanceUuid) throws MetamacException {
        return indicatorsCoverageService.retrieveGeographicalCodesInIndicatorInstanceWithPublishedIndicator(RestConstants.SERVICE_CONTEXT, indicatorInstanceUuid);
    }

    @Override
    public List<TimeGranularity> retrieveTimeGranularitiesInIndicatorInstance(String indicatorInstanceUuid) throws MetamacException {
        return indicatorsCoverageService.retrieveTimeGranularitiesInIndicatorInstanceWithPublishedIndicator(RestConstants.SERVICE_CONTEXT, indicatorInstanceUuid);
    }

    @Override
    public List<TimeValue> retrieveTimeValuesInIndicatorInstance(String indicatorInstanceUuid) throws MetamacException {
        return indicatorsCoverageService.retrieveTimeValuesInIndicatorInstanceWithPublishedIndicator(RestConstants.SERVICE_CONTEXT, indicatorInstanceUuid);
    }

    @Override
    public List<MeasureValue> retrieveMeasureValuesInIndicatorInstance(String indicatorInstanceUuid) throws MetamacException {
        return indicatorsCoverageService.retrieveMeasureValuesInIndicatorInstanceWithPublishedIndicator(RestConstants.SERVICE_CONTEXT, indicatorInstanceUuid);
    }

    @Override
    public IndicatorObservationsExtendedVO findObservationsExtendedInIndicatorInstance(String indicatorInstanceUuid, IndicatorsDataFilterVO dataFilter) throws MetamacException {
        return indicatorsDataService.findObservationsExtendedByDimensionsInIndicatorInstanceWithPublishedIndicator(RestConstants.SERVICE_CONTEXT, indicatorInstanceUuid, dataFilter);
    }

    @Override
    public IndicatorObservationsVO findObservationsInIndicatorInstance(String indicatorInstanceUuid, IndicatorsDataFilterVO dataFilter) throws MetamacException {
        return indicatorsDataService.findObservationsInIndicatorInstanceWithPublishedIndicator(RestConstants.SERVICE_CONTEXT, indicatorInstanceUuid, dataFilter);
    }

    @Override
    public IndicatorObservationsExtendedVO findObservationsExtendedInIndicator(String uuid, IndicatorsDataFilterVO dataFilter) throws MetamacException {
        return indicatorsDataService.findObservationsExtendedByDimensionsInIndicatorPublished(RestConstants.SERVICE_CONTEXT, uuid, dataFilter);
    }

    @Override
    public IndicatorObservationsVO findObservationsInIndicator(String uuid, IndicatorsDataFilterVO dataFilter) throws MetamacException {
        return indicatorsDataService.findObservationsInIndicatorPublished(RestConstants.SERVICE_CONTEXT, uuid, dataFilter);
    }

}
