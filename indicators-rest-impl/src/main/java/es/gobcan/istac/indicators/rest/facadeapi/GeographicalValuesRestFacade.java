package es.gobcan.istac.indicators.rest.facadeapi;

import es.gobcan.istac.indicators.rest.types.GeographicalValueType;
import org.siemac.metamac.core.common.exception.MetamacException;

import java.util.List;

public interface GeographicalValuesRestFacade {

    public List<GeographicalValueType> findGeographicalValuesByIndicatorsSystemCode(String indicatorsSystemCode, String granularityCode) throws MetamacException;
    public List<GeographicalValueType> findGeographicalValuesBySubjectCode(String subjectCode, String granularityCode) throws MetamacException;
    public List<GeographicalValueType> findGeographicalValuesByGranularity(String granularityCode) throws MetamacException;

}
