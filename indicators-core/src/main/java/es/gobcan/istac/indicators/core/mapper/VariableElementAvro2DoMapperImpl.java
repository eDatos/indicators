package es.gobcan.istac.indicators.core.mapper;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.stream.message.RelatedResourceAvro;
import org.siemac.metamac.srm.core.stream.message.VariableElementAvro;
import org.springframework.stereotype.Component;

import es.gobcan.istac.indicators.core.domain.GeographicalGranularity;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.invocation.utils.RestMapper;

@Component
public class VariableElementAvro2DoMapperImpl implements VariableElementAvro2DoMapper {

    @Override
    public GeographicalValue variableElementAvroToDoGeographicalValue(ServiceContext ctx, VariableElementAvro variableElementAvro, GeographicalGranularity geographicalGranularity)
            throws MetamacException {
        GeographicalValue geographicalValue = new GeographicalValue();

        geographicalValue.setCode(variableElementAvro.getCode());
        geographicalValue.setGranularity(granularityRelatedResourceToDoGeographicalGranularity(variableElementAvro.getGeographicGranularities()));
        geographicalValue.setLatitude(variableElementAvro.getLatitud());
        geographicalValue.setLongitude(variableElementAvro.getLongitud());
        geographicalValue.setTitle(RestMapper.getInternationalStringFromInternationalStringAvro(variableElementAvro.getShortName()));
        geographicalValue.setOrder(variableElementAvro.getCode());

        return geographicalValue;
    }

    @Override
    public GeographicalGranularity granularityRelatedResourceToDoGeographicalGranularity(RelatedResourceAvro srmGeographicalGranularity) {
        GeographicalGranularity geographicalGranularity = new GeographicalGranularity();

        geographicalGranularity.setCode(srmGeographicalGranularity.getCode());
        geographicalGranularity.setTitle(RestMapper.getInternationalStringFromInternationalStringAvro(srmGeographicalGranularity.getTitle()));

        return geographicalGranularity;
    }
}
