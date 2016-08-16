package es.gobcan.istac.indicators.rest.facadeimpl;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.domain.TimeGranularity;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsSystemsService;
import es.gobcan.istac.indicators.rest.IndicatorsRestConstants;
import es.gobcan.istac.indicators.rest.facadeapi.TimeRestFacade;
import es.gobcan.istac.indicators.rest.mapper.Do2TypeMapper;
import es.gobcan.istac.indicators.rest.types.MetadataGranularityType;

@Service("timeRestFacade")
public class TimeRestFacadeImpl implements TimeRestFacade {

    protected static final Logger    LOG                      = LoggerFactory.getLogger(TimeRestFacadeImpl.class);

    @Autowired
    private Do2TypeMapper            dto2TypeMapper           = null;

    @Autowired
    private IndicatorsSystemsService indicatorsSystemsService = null;

    @Override
    public List<MetadataGranularityType> findTimeGranularities() throws MetamacException {
        List<TimeGranularity> timeGranularities = indicatorsSystemsService.retrieveTimeGranularities(IndicatorsRestConstants.SERVICE_CONTEXT);
        return dto2TypeMapper.timeGranularityDoToType(timeGranularities);
    }

}
