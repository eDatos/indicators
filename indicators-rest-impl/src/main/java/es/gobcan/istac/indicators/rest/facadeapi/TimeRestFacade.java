package es.gobcan.istac.indicators.rest.facadeapi;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.indicators.rest.types.MetadataGranularityType;

public interface TimeRestFacade {

    List<MetadataGranularityType> findTimeGranularities() throws MetamacException;
}
