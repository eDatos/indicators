package es.gobcan.istac.indicators.rest.mapper;

import java.util.List;

import org.springframework.web.util.UriComponentsBuilder;

import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.rest.types.IndicatorInstanceType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemType;

public interface Do2TypeMapper {

    // Indicators systems
    public IndicatorsSystemType indicatorsSystemDoToType(IndicatorsSystemVersion source, UriComponentsBuilder uriComponentsBuilder);
    public IndicatorsSystemBaseType indicatorsSystemDoToBaseType(IndicatorsSystemVersion source, UriComponentsBuilder uriComponentsBuilder);
    public List<IndicatorsSystemBaseType> indicatorsSystemDoToBaseType(List<IndicatorsSystemVersion> sources, UriComponentsBuilder uriComponentsBuilder);

    // Indicators Instance
    public IndicatorInstanceType indicatorsInstanceDoToType(IndicatorInstance source, UriComponentsBuilder uriComponentsBuilder);
    public List<IndicatorInstanceType> indicatorsInstanceDoToType(List<IndicatorInstance> sources, UriComponentsBuilder uriComponentsBuilder);

}
