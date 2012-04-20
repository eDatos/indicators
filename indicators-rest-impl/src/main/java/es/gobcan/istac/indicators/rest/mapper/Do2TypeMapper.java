package es.gobcan.istac.indicators.rest.mapper;

import java.util.List;

import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.rest.types.IndicatorBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorInstanceBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorInstanceType;
import es.gobcan.istac.indicators.rest.types.IndicatorType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemType;

public interface Do2TypeMapper {

    // Indicators systems
    public IndicatorsSystemType indicatorsSystemDoToType(final IndicatorsSystemVersion source, final String baseURL);
    public IndicatorsSystemBaseType indicatorsSystemDoToBaseType(final IndicatorsSystemVersion source, final String baseURL);
    public List<IndicatorsSystemBaseType> indicatorsSystemDoToBaseType(final List<IndicatorsSystemVersion> sources, final String baseURL);

    // Indicators Instance
    public IndicatorInstanceType indicatorsInstanceDoToType(final IndicatorInstance source, final String baseURL);
    public List<IndicatorInstanceBaseType> indicatorsInstanceDoToBaseType(final List<IndicatorInstance> sources, final String baseURL);
    
    // Indicator
    public IndicatorType indicatorDoToType(final IndicatorVersion sources, final String baseURL);
    public List<IndicatorBaseType> indicatorDoToBaseType(final List<IndicatorVersion> sources, final String baseURL);

}
