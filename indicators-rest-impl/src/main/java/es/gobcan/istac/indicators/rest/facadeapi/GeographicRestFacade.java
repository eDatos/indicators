package es.gobcan.istac.indicators.rest.facadeapi;

import java.util.List;

import es.gobcan.istac.indicators.rest.types.MetadataGranularityType;

public interface GeographicRestFacade {

    public MetadataGranularityType retrieveGeographicGranilarity(final String baseUrl, final String granularyCode) throws Exception;
    public List<MetadataGranularityType> findGeographicGranilarities(final String baseUrl) throws Exception;

}
