package es.gobcan.istac.indicators.rest.facadeapi;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.indicators.rest.types.MetadataGranularityType;

public interface GeographicRestFacade {

    List<MetadataGranularityType> findGeographicGranularities() throws MetamacException;
    List<MetadataGranularityType> findGeographicGranularitiesByIndicatorsSystemCode(String indicatorsSystemCode) throws MetamacException;
    List<MetadataGranularityType> findGeographicGranularitiesBySubjectCode(String subjectCode) throws MetamacException;

}
