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
import es.gobcan.istac.indicators.rest.IndicatorsRestConstants;
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
        return indicatorsSystemsService.findIndicatorsSystemHistory(IndicatorsRestConstants.SERVICE_CONTEXT, idIndicatorSystem, maxResults);
    }

    @Override
    public DataSource retrieveDataSource(String uuid) throws MetamacException {
        return indicatorsService.retrieveDataSource(IndicatorsRestConstants.SERVICE_CONTEXT, uuid);
    }

    @Override
    public List<IndicatorsSystemVersion> retrieveIndicatorsSystemPublishedForIndicator(String indicatorUuid) throws MetamacException {
        return indicatorsSystemsService.retrieveIndicatorsSystemPublishedForIndicator(IndicatorsRestConstants.SERVICE_CONTEXT, indicatorUuid);
    }

    @Override
    public UnitMultiplier retrieveUnitMultiplier(Integer unitMultiplier) throws MetamacException {
        return indicatorsService.retrieveUnitMultiplier(IndicatorsRestConstants.SERVICE_CONTEXT, unitMultiplier);
    }

    @Override
    public GeographicalValue retrieveGeographicalValueByCode(String geoCode) throws MetamacException {
        return indicatorsSystemsService.retrieveGeographicalValueByCode(IndicatorsRestConstants.SERVICE_CONTEXT, geoCode);
    }

    @Override
    public TimeValue retrieveTimeValueByCode(String timeCode) throws MetamacException {
        return indicatorsSystemsService.retrieveTimeValue(IndicatorsRestConstants.SERVICE_CONTEXT, timeCode);
    }

    @Override
    public List<GeographicalGranularity> retrieveGeographicalGranularitiesInIndicatorVersion(IndicatorVersion indicatorVersion) throws MetamacException {
        return indicatorsCoverageService.retrieveGeographicalGranularitiesInIndicatorVersion(IndicatorsRestConstants.SERVICE_CONTEXT, indicatorVersion);
    }

    @Override
    public List<GeographicalCodeVO> retrieveGeographicalCodesInIndicatorVersion(IndicatorVersion indicatorVersion) throws MetamacException {
        return indicatorsCoverageService.retrieveGeographicalCodesInIndicatorVersion(IndicatorsRestConstants.SERVICE_CONTEXT, indicatorVersion);
    }

    @Override
    public List<GeographicalValueVO> retrieveGeographicalValuesInIndicatorVersion(IndicatorVersion indicatorVersion) throws MetamacException {
        return indicatorsCoverageService.retrieveGeographicalValuesInIndicatorVersion(IndicatorsRestConstants.SERVICE_CONTEXT, indicatorVersion);
    }

    @Override
    public List<MeasureValue> retrieveMeasureValuesInIndicator(IndicatorVersion indicatorVersion) throws MetamacException {
        return indicatorsCoverageService.retrieveMeasureValuesInIndicatorVersion(IndicatorsRestConstants.SERVICE_CONTEXT, indicatorVersion);
    }

    @Override
    public List<TimeValue> retrieveTimeValuesInIndicatorVersion(IndicatorVersion indicatorVersion) throws MetamacException {
        return indicatorsCoverageService.retrieveTimeValuesInIndicatorVersion(IndicatorsRestConstants.SERVICE_CONTEXT, indicatorVersion);
    }
}
