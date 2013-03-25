package es.gobcan.istac.indicators.rest.facadeapi;

import java.util.List;

import es.gobcan.istac.indicators.rest.types.MetadataGranularityType;

public interface GeographicRestFacade {

    //public MetadataGranularityType retrieveGeographicGranularity(final String baseUrl, final String granularyCode) throws Exception;
    public List<MetadataGranularityType> findGeographicGranularities() throws Exception;
    public List<MetadataGranularityType> findGeographicGranularitiesByIndicatorsSystemCode(String indicatorsSystemCode) throws Exception;
    public List<MetadataGranularityType> findGeographicGranularitiesBySubjectCode(String subjectCode) throws Exception;

}
