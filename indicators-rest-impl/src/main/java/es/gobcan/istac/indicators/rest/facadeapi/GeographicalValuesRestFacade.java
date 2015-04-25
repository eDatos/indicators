package es.gobcan.istac.indicators.rest.facadeapi;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.indicators.rest.types.GeographicalValueType;

public interface GeographicalValuesRestFacade {

    List<GeographicalValueType> findGeographicalValuesByIndicatorsSystemCode(String indicatorsSystemCode, String granularityCode) throws MetamacException;
    List<GeographicalValueType> findGeographicalValuesBySubjectCode(String subjectCode, String granularityCode) throws MetamacException;
    List<GeographicalValueType> findGeographicalValuesByGranularity(String granularityCode) throws MetamacException;

}
