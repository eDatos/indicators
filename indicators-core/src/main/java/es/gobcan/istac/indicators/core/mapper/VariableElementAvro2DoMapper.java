package es.gobcan.istac.indicators.core.mapper;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.stream.message.RelatedResourceAvro;
import org.siemac.metamac.srm.core.stream.message.VariableElementAvro;

import es.gobcan.istac.indicators.core.domain.GeographicalGranularity;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;

public interface VariableElementAvro2DoMapper {

    public GeographicalValue variableElementAvroToDoGeographicalValue(ServiceContext ctx, VariableElementAvro variableElementAvro, GeographicalGranularity geographicalGranularity)
            throws MetamacException;
    public GeographicalGranularity granularityRelatedResourceToDoGeographicalGranularity(RelatedResourceAvro srmGeographicalGranularity);
}
