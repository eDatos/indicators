package es.gobcan.istac.indicators.rest.facadeimpl;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.domain.GeographicalGranularity;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsCoverageService;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsSystemsService;
import es.gobcan.istac.indicators.rest.IndicatorsRestConstants;
import es.gobcan.istac.indicators.rest.facadeapi.GeographicRestFacade;
import es.gobcan.istac.indicators.rest.mapper.Do2TypeMapper;
import es.gobcan.istac.indicators.rest.types.MetadataGranularityType;

@Service("geographicRestFacade")
public class GeographicRestFacadeImpl implements GeographicRestFacade {

    protected static final Logger     LOG                       = LoggerFactory.getLogger(GeographicRestFacadeImpl.class);

    @Autowired
    private Do2TypeMapper             dto2TypeMapper            = null;

    @Autowired
    private IndicatorsSystemsService  indicatorsSystemsService  = null;

    @Autowired
    private IndicatorsCoverageService indicatorsCoverageService = null;

    @Override
    public List<MetadataGranularityType> findGeographicGranularities() throws MetamacException {
        List<GeographicalGranularity> geographicalGranularities = indicatorsSystemsService.retrieveGeographicalGranularities(IndicatorsRestConstants.SERVICE_CONTEXT);
        return dto2TypeMapper.geographicalGranularityDoToType(geographicalGranularities);
    }

    @Override
    public List<MetadataGranularityType> findGeographicGranularitiesByIndicatorsSystemCode(String indicatorsSystemCode) throws MetamacException {
        List<GeographicalGranularity> geographicalGranularities = indicatorsCoverageService.retrieveGeographicalGranularitiesInIndicatorsInstanceInPublishedIndicatorsSystem(
                IndicatorsRestConstants.SERVICE_CONTEXT, indicatorsSystemCode);
        return dto2TypeMapper.geographicalGranularityDoToType(geographicalGranularities);
    }

    @Override
    public List<MetadataGranularityType> findGeographicGranularitiesBySubjectCode(String subjectCode) throws MetamacException {
        List<GeographicalGranularity> geographicalGranularities = indicatorsCoverageService.retrieveGeographicalGranularitiesInIndicatorsPublishedWithSubjectCode(IndicatorsRestConstants.SERVICE_CONTEXT,
                subjectCode);
        return dto2TypeMapper.geographicalGranularityDoToType(geographicalGranularities);
    }

}
