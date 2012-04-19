package es.gobcan.istac.indicators.rest.mapper;

import java.util.List;

import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.rest.types.IndicatorInstanceType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemType;

public interface Do2TypeMapper {

    // Indicators systems
    public IndicatorsSystemType indicatorsSystemDoToType(IndicatorsSystemVersion source, final String baseURL);
    public IndicatorsSystemBaseType indicatorsSystemDoToBaseType(IndicatorsSystemVersion source, final String baseURL);
    public List<IndicatorsSystemBaseType> indicatorsSystemDoToBaseType(List<IndicatorsSystemVersion> sources, final String baseURL);

    // Indicators Instance
    public IndicatorInstanceType indicatorsInstanceDoToType(IndicatorInstance source, final String baseURL);
    public List<IndicatorInstanceType> indicatorsInstanceDoToType(List<IndicatorInstance> sources, final String baseURL);

}
