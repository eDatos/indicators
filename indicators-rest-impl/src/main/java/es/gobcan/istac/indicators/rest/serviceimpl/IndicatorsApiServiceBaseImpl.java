package es.gobcan.istac.indicators.rest.serviceimpl;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;

import es.gobcan.istac.indicators.core.domain.DataSource;
import es.gobcan.istac.indicators.core.domain.GeographicalGranularity;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemHistory;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.domain.MeasureValue;
import es.gobcan.istac.indicators.core.domain.TimeValue;
import es.gobcan.istac.indicators.core.domain.UnitMultiplier;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsCoverageService;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsDataService;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsService;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsSystemsService;
import es.gobcan.istac.indicators.core.vo.GeographicalCodeVO;
import es.gobcan.istac.indicators.core.vo.GeographicalValueVO;
import es.gobcan.istac.indicators.rest.RestConstants;
import es.gobcan.istac.indicators.rest.serviceapi.IndicatorsApiService;

public abstract class IndicatorsApiServiceBaseImpl implements IndicatorsApiService {

    @Autowired
    protected IndicatorsSystemsService  indicatorsSystemsService;

    @Autowired
    protected IndicatorsService         indicatorsService;

    @Autowired
    protected IndicatorsDataService     indicatorsDataService;

    @Autowired
    protected IndicatorsCoverageService indicatorsCoverageService;

    @Override
    public List<IndicatorsSystemHistory> findIndicatorsSystemHistory(String idIndicatorSystem, int maxResults) throws MetamacException {
        return indicatorsSystemsService.findIndicatorsSystemHistory(RestConstants.SERVICE_CONTEXT, idIndicatorSystem, maxResults);
    }

    @Override
    public DataSource retrieveDataSource(String uuid) throws MetamacException {
        return indicatorsService.retrieveDataSource(RestConstants.SERVICE_CONTEXT, uuid);
    }

    @Override
    public List<IndicatorsSystemVersion> retrieveIndicatorsSystemPublishedForIndicator(String indicatorUuid) throws MetamacException {
        return indicatorsSystemsService.retrieveIndicatorsSystemPublishedForIndicator(RestConstants.SERVICE_CONTEXT, indicatorUuid);
    }

    @Override
    public UnitMultiplier retrieveUnitMultiplier(Integer unitMultiplier) throws MetamacException {
        return indicatorsService.retrieveUnitMultiplier(RestConstants.SERVICE_CONTEXT, unitMultiplier);
    }

    @Override
    public GeographicalValue retrieveGeographicalValueByCode(String geoCode) throws MetamacException {
        return indicatorsSystemsService.retrieveGeographicalValueByCode(RestConstants.SERVICE_CONTEXT, geoCode);
    }

    @Override
    public TimeValue retrieveTimeValueByCode(String timeCode) throws MetamacException {
        return indicatorsSystemsService.retrieveTimeValue(RestConstants.SERVICE_CONTEXT, timeCode);
    }

    @Override
    public List<GeographicalGranularity> retrieveGeographicalGranularitiesInIndicatorVersion(IndicatorVersion indicatorVersion) throws MetamacException {
        return indicatorsCoverageService.retrieveGeographicalGranularitiesInIndicatorVersion(RestConstants.SERVICE_CONTEXT, indicatorVersion);
    }

    @Override
    public List<GeographicalCodeVO> retrieveGeographicalCodesInIndicatorVersion(IndicatorVersion indicatorVersion) throws MetamacException {
        return indicatorsCoverageService.retrieveGeographicalCodesInIndicatorVersion(RestConstants.SERVICE_CONTEXT, indicatorVersion);
    }

    @Override
    public List<GeographicalValueVO> retrieveGeographicalValuesInIndicatorVersion(IndicatorVersion indicatorVersion) throws MetamacException {
        return indicatorsCoverageService.retrieveGeographicalValuesInIndicatorVersion(RestConstants.SERVICE_CONTEXT, indicatorVersion);
    }

    @Override
    public List<MeasureValue> retrieveMeasureValuesInIndicator(IndicatorVersion indicatorVersion) throws MetamacException {
        return indicatorsCoverageService.retrieveMeasureValuesInIndicatorVersion(RestConstants.SERVICE_CONTEXT, indicatorVersion);
    }

    @Override
    public List<TimeValue> retrieveTimeValuesInIndicatorVersion(IndicatorVersion indicatorVersion) throws MetamacException {
        return indicatorsCoverageService.retrieveTimeValuesInIndicatorVersion(RestConstants.SERVICE_CONTEXT, indicatorVersion);
    }
}
