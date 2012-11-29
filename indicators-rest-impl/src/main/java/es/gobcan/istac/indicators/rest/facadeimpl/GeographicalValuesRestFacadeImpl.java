package es.gobcan.istac.indicators.rest.facadeimpl;

import es.gobcan.istac.indicators.core.domain.GeographicalGranularity;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsDataService;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsSystemsService;
import es.gobcan.istac.indicators.rest.RestConstants;
import es.gobcan.istac.indicators.rest.facadeapi.GeographicalValuesRestFacade;
import es.gobcan.istac.indicators.rest.mapper.Do2TypeMapper;
import es.gobcan.istac.indicators.rest.types.GeographicalValueType;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("geographicalValuesRestFacade")
public class GeographicalValuesRestFacadeImpl implements GeographicalValuesRestFacade {

    @Autowired
    private IndicatorsDataService indicatorsDataService;

    @Autowired
    private IndicatorsSystemsService indicatorsSystemsService;

    @Autowired
    private Do2TypeMapper mapper;

    private String getGranularityUuidByCode(String granularityCode) throws MetamacException {
        GeographicalGranularity granularity = indicatorsSystemsService.retrieveGeographicalGranularityByCode(RestConstants.SERVICE_CONTEXT, granularityCode);
        return granularity.getUuid();
    }

    @Override
    public List<GeographicalValueType> findGeographicalValuesByIndicatorsSystemCode(String indicatorsSystemCode, String granularityCode) throws MetamacException {
        String granularityUuid = getGranularityUuidByCode(granularityCode);
        List<GeographicalValue> geographicalValues = indicatorsDataService.retrieveGeographicalValuesByGranularityInIndicatorsInstancesInPublishedIndicatorsSystem(RestConstants.SERVICE_CONTEXT, indicatorsSystemCode, granularityUuid);
        List<GeographicalValueType> types = mapper.geographicalValuesDoToType(geographicalValues);
        return types;
    }

    @Override
    public List<GeographicalValueType> findGeographicalValuesBySubjectCode(String subjectCode, String granularityCode) throws MetamacException {
        String granularityUuid = getGranularityUuidByCode(granularityCode);
        List<GeographicalValue> geographicalValues = indicatorsDataService.retrieveGeographicalValuesByGranularityInIndicatorPublishedWithSubjectCode(RestConstants.SERVICE_CONTEXT, subjectCode, granularityUuid);
        List<GeographicalValueType> types = mapper.geographicalValuesDoToType(geographicalValues);
        return types;
    }

}
