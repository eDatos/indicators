package es.gobcan.istac.indicators.rest.serviceimpl;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;

import es.gobcan.istac.indicators.core.domain.DataSource;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemHistory;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.domain.UnitMultiplier;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsDataService;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsService;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsSystemsService;
import es.gobcan.istac.indicators.rest.RestConstants;
import es.gobcan.istac.indicators.rest.serviceapi.IndicatorsApiService;


public abstract class IndicatorsApiServiceBaseImpl implements IndicatorsApiService {

    @Autowired
    protected IndicatorsSystemsService indicatorsSystemsService;
    
    @Autowired
    protected IndicatorsService indicatorsService;
    
    @Autowired
    protected IndicatorsDataService indicatorsDataService;
    
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
}
