package es.gobcan.istac.indicators.rest.facadeimpl;

import es.gobcan.istac.indicators.core.domain.GeographicalGranularity;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsDataService;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsSystemsService;
import es.gobcan.istac.indicators.rest.RestConstants;
import es.gobcan.istac.indicators.rest.facadeapi.GeographicRestFacade;
import es.gobcan.istac.indicators.rest.mapper.Do2TypeMapper;
import es.gobcan.istac.indicators.rest.types.MetadataGranularityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("geographicRestFacade")
public class GeographicRestFacadeImpl implements GeographicRestFacade {

    protected Logger                 logger                   = LoggerFactory.getLogger(getClass());

    @Autowired
    private Do2TypeMapper            dto2TypeMapper           = null;

    @Autowired
    private IndicatorsSystemsService indicatorsSystemsService = null;
    
    @Autowired
    private IndicatorsDataService indicatorsDataService = null;

    
    //TODO: This methods are all used by the widgets, not by JAXI, but they should use IndicatorsApiService so you can provide a complete private API.
    
    public MetadataGranularityType retrieveGeographicGranularity(String baseUrl, String granularyCode) throws Exception {
        //GeographicalGranularity geographicalGranularity = indicatorsSystemsService.retrieveGeographicalGranularity(RestConstants.SERVICE_CONTEXT, null); // TODO
        //MetadataGranularityType granularityType = dto2TypeMapper.geographicalGranularityDoToType(geographicalGranularity);
        //return granularityType;
        return null;
    }
    
    @Override
    public List<MetadataGranularityType> findGeographicGranularities() throws Exception {
        List<GeographicalGranularity> geographicalGranularities = indicatorsSystemsService.retrieveGeographicalGranularities(RestConstants.SERVICE_CONTEXT);
        List<MetadataGranularityType> granularityTypes = dto2TypeMapper.geographicalGranularityDoToType(geographicalGranularities);
        return granularityTypes;
    }
    
    @Override
    public List<MetadataGranularityType> findGeographicGranularitiesByIndicatorsSystemCode(String indicatorsSystemCode) throws Exception {
        List<GeographicalGranularity> geographicalGranularities = indicatorsDataService.retrieveGeographicalGranularitiesInIndicatorsInstanceInPublishedIndicatorsSystem(RestConstants.SERVICE_CONTEXT, indicatorsSystemCode);
        List<MetadataGranularityType> granularityTypes = dto2TypeMapper.geographicalGranularityDoToType(geographicalGranularities);
        return granularityTypes;
    }
    
    @Override
    public List<MetadataGranularityType> findGeographicGranularitiesBySubjectCode(String subjectCode) throws Exception {
        List<GeographicalGranularity> geographicalGranularities = indicatorsDataService.retrieveGeographicalGranularitiesInIndicatorsPublishedWithSubjectCode(RestConstants.SERVICE_CONTEXT, subjectCode);
        List<MetadataGranularityType> granularityTypes = dto2TypeMapper.geographicalGranularityDoToType(geographicalGranularities);
        return granularityTypes;
    }


}
